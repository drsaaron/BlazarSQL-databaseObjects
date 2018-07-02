/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import com.blazartech.products.blazarsql.components.dataobjects.ObjectDataAccess;
import com.blazartech.products.blazarsql.components.dataobjects.TableColumnDescriptor;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aar1069
 * @version $Id: ObjectDataAccessImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public abstract class ObjectDataAccessImpl implements ObjectDataAccess {

    private static final Logger logger = LoggerFactory.getLogger(ObjectDataAccessImpl.class);
    
    public DatabaseMetaData getDatabaseMetaData(DBConnection connection) throws SQLException {
        return connection.getJDBCConnection().getMetaData();
    }

    /** Extract a single column from a result set and store the data in a list.
     * @param set the query result set
     * @param key the column name to be extracted
     * @throws SQLException on error reading the data
     * @return the column converted to a list
     */
    protected Collection<String> populateSingleValueList(ResultSet set, String key) throws SQLException {
        Collection<String> v = new ArrayList<>();
        while (set.next()) {
            String value = set.getString(key);
            if (!v.contains(value)) {
                v.add(value);
            }
        }
        return v;
    }

    /** Get a list of users with objects in the database.
     * @throws SQLException if there's an error reading the list.
     * @return List of user IDs (as strings).
     */
    @Override
    public Collection<String> getUserList(DBConnection connection) throws SQLException {
        ResultSet schemaResult = getDatabaseMetaData(connection).getSchemas();
        Collection<String> userList = populateSingleValueList(schemaResult, "TABLE_SCHEM");
        return userList;
    }
    
    /**
     * create a "dummy" user list, for those server types that don't work the way
     * we expect them too, <em>e.g.</em> MSAccess.
     * 
     * @return 
     */
    protected Collection<String> createDummyUserList() {
        List<String> userList = new ArrayList<>();
        userList.add("Admin");
        return userList;
    }

    /** Get a list of stored procedure names for a user.
     * @param owner The owner of the stored procedures.
     * @throws SQLException if there's an error reading the list.
     * @return The list of procedures (as strings).
     */
    @Override
    public Collection<String> getStoredProcedureNames(DBConnection connection, String owner) throws SQLException {
        ResultSet procResults = getDatabaseMetaData(connection).getProcedures(connection.getDatabaseName(), owner, null);
        return populateSingleValueList(procResults, "PROCEDURE_NAME");
    }

    /** Get a list of tables owned by a user.
     * @param owner The owner.
     * @throws SQLException if there's an error reading the list.
     * @return The list of table names (as strings).
     */
    @Override
    public Collection<String> getTableNames(DBConnection connection, String owner) throws SQLException {
        ResultSet tableResult = getDatabaseMetaData(connection).getTables(connection.getDatabaseName(), owner, "%", null);
        return populateSingleValueList(tableResult, "TABLE_NAME");
    }

    /** Get a list of the columns in a table.
     * @param tableName The name of the table.
     * @param owner The table owner.
     * @throws SQLException if there's an error getting the column list.
     * @return The column list.  This will be list of type {@link TableColumnDescriptor}'s.
     */
    @Override
    public Collection<TableColumnDescriptor> getTableColumns(DBConnection connection, String tableName, String owner) throws SQLException {
        ResultSet tableResult = getDatabaseMetaData(connection).getColumns(connection.getDatabaseName(), owner, tableName, null);
        return buildTableColumnList(tableResult);
    }

    protected String buildCanonicalObjectName(String owner, String objectName) {
        return owner + "." + objectName;
    }

    /** Get the SQL code for a query to retrieve all the data in a table.
     * @param owner The table owner.
     * @param tableName The table name
     * @return The retrieve SQL.
     */
    @Override
    public String getTableDataSQL(DBConnection connection, String owner, String tableName) {
        return "select * from " + buildCanonicalObjectName(owner, tableName);
    }

    @Override
    public int getTableSize(DBConnection connection, String owner, String tableName) {
        String sql = "select count(*) as RC from " + buildCanonicalObjectName(owner, tableName);
        try {
            ResultSet rs = executeQuery(connection, sql);
            rs.next();
            int size = rs.getInt("RC");
            return size;
        } catch (SQLException e) {
            throw new RuntimeException("error getting table size: " + e.getMessage(), e);
        }
    }

    /** Construct a list of {@link TableColumnDescriptor}'s from the {@link
     * #getTableColumns} query.
     * @param tableResult The {@link ResultSet} from {@link #getTableColumns}
     * @throws SQLException on error reading the results.
     * @return list of columns
     */
    protected Collection<TableColumnDescriptor> buildTableColumnList(ResultSet tableResult) throws SQLException {
        List<TableColumnDescriptor> l = new ArrayList<>();
        while (tableResult.next()) {
            String colName = tableResult.getString("COLUMN_NAME");
            int dataType = tableResult.getInt("DATA_TYPE");
            String colType = tableResult.getString("TYPE_NAME");
            TableColumnDescriptor d = new TableColumnDescriptor();
            d.setName(colName);
            d.setTypeName(colType);
            d.setType(dataType);
            l.add(d);
        }
        return l;
    }

    /** Get the catalog list from the database.
     * @throws SQLException if there's an error reading the list.
     * @return The list.
     */
    @Override
    public Collection<String> getCatalogList(DBConnection connection) throws SQLException {
        ResultSet catalogResult = getDatabaseMetaData(connection).getCatalogs();
        return populateSingleValueList(catalogResult, "TABLE_CAT");
    }

    /**
     * Prepare to execute a query.
     * @param connection
     * @param query the query
     * @throws java.sql.SQLException on error
     * @return the statement
     */
    protected PreparedStatement prepareStatement(DBConnection connection, String query) throws SQLException {
        return prepareStatement(connection, query, null);
    }

    /**
     * Execute a query with no parameters.
     * @param connection
     * @param query the query
     * @throws java.sql.SQLException on error
     * @return query result
     */
    protected ResultSet executeQuery(DBConnection connection, String query) throws SQLException {
        return executeQuery(connection, query, null);
    }

    /**
     * execute query with arguments
     * @param connection
     * @param query the query
     * @param args list of arguments
     * @throws java.sql.SQLException on error
     * @return result set.
     */
    protected ResultSet executeQuery(DBConnection connection, String query, Object[] args) throws SQLException {
        PreparedStatement s = prepareStatement(connection, query, args);
        return s.executeQuery();
    }

    /**
     * prepare a query with parameters
     * @param connection
     * @param query the query
     * @param args argument list
     * @throws java.sql.SQLException on error
     * @return query results
     */
    protected PreparedStatement prepareStatement(DBConnection connection, String query, Object[] args) throws SQLException {
        PreparedStatement stmt = connection.getJDBCConnection().prepareStatement(query);
        if (args == null) {
            args = new Object[]{};
        }
        for (int i = 0; i < args.length; i++) {
            Object a = args[i];
            int argIndx = i + 1;
            if (a == null) {
                stmt.setNull(argIndx, Types.VARCHAR);
            } else if (a instanceof Integer) {
                stmt.setInt(argIndx, ((Integer) a));
            } else if (a instanceof String) {
                stmt.setString(argIndx, (String) a);
            } else {
                throw new IllegalArgumentException("Argument of type " + a.getClass().toString() + " found.");
            }
        }
        return stmt;
    }

    /**
     * Get the SQL to insert all the data into a table.
     * @param owner table schema/owner
     * @param tableName table name
     * @throws java.sql.SQLException on error
     * @return insert SQL
     */
    @Override
    public String getTableDataInsertSQL(DBConnection connection, String owner, String tableName) throws SQLException {
        String sql = "";

        // get the list of columns for the table.
        Collection<TableColumnDescriptor> columnList = getTableColumns(connection, tableName, owner);

        // get the data in the table.
        ResultSet rs = executeQuery(connection, getTableDataSQL(connection, owner, tableName), null);
        while (rs.next()) {
            int columnNumber = 0;
            String insertSQL = "insert into " + owner + "." + tableName + "(";
            for (TableColumnDescriptor column : columnList) {
                insertSQL += column.getName();
                if (++columnNumber < columnList.size()) {
                    insertSQL += ", ";
                }
            }

            insertSQL += ") values (";
            columnNumber = 0;
            for (TableColumnDescriptor column : columnList) {
                boolean isQuotable = isColumnQuotable(connection, column);
                String value = rs.getString(column.getName());
                if (value == null) {
                    insertSQL += "NULL";
                } else {
                    if (isQuotable) {
                        insertSQL += "\"";
                    }
                    insertSQL += value;
                    if (isQuotable) {
                        insertSQL += "\"";
                    }
                }
                if (++columnNumber < columnList.size()) {
                    insertSQL += ", ";
                }
            }

            sql += insertSQL + ")" + getMultiQueryDelimiter(connection) + "\n";
        }

        return sql;
    }

    /**
     * get the delimiter for multi-SQL statement queries
     * @return delimiter, default is empty string
     */
    @Override
    public String getMultiQueryDelimiter(DBConnection connection) {
        return "";
    }

    /**
     * is this column a text-like column, i.e. it requires
     * quoting?
     * @param column the column
     * @return true or false
     */
    @Override
    public boolean isColumnQuotable(DBConnection connection, TableColumnDescriptor column) {
        switch (column.getType()) {
            case Types.CHAR:
            case Types.DATE:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.VARCHAR:
                return true;
            default:
                return false;
        }
    }
}
