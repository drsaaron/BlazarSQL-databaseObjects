/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects;

import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author aar1069
 */
public interface ObjectDataAccess {
    
        /**
     * Create the SQL code to fully create the table.  This will
     * include all referential and check constraints, indexes,
     * primary keys, permissions, etc.
     * @param connection
     * @param owner The table owner.
     * @param tableName The table name.
     * @throws SQLException If there is an error retrieving the information.
     * @return The full SQL code.
     */
    String extractFullTableDefinition(DBConnection connection, String owner, String tableName) throws SQLException;

    /**
     * Get the catalog list from the database.
     * @param connection
     * @throws SQLException if there's an error reading the list.
     * @return The list.
     */
    Collection<String> getCatalogList(DBConnection connection) throws SQLException;


    /**
     * get the delimiter for multi-SQL statement queries
     * @param connection
     * @return delimiter, default is empty string
     */
    String getMultiQueryDelimiter(DBConnection connection);

    /**
     * Retrieve the code for a given stored procedure.
     * @param connection
     * @param procName The procedure name.
     * @param owner The procedure owner.
     * @throws SQLException if there is an error retrieving the code.
     * @return The procedure code.
     */
    String getStoredProcedureCode(DBConnection connection, String procName, String owner) throws SQLException;

    /**
     * Get a list of stored procedure names for a user.
     * @param connection
     * @param owner The owner of the stored procedures.
     * @throws SQLException if there's an error reading the list.
     * @return The list of procedures (as strings).
     */
    Collection<String> getStoredProcedureNames(DBConnection connection, String owner) throws SQLException;

    /**
     * Get a list of the columns in a table.
     * @param connection
     * @param tableName The name of the table.
     * @param owner The table owner.
     * @throws SQLException if there's an error getting the column list.
     * @return The column list.  This will be list of type {@link TableColumnDescriptor}'s.
     */
    Collection<TableColumnDescriptor> getTableColumns(DBConnection connection, String tableName, String owner) throws SQLException;

    /**
     * Get the SQL to insert all the data into a table.
     * @param connection
     * @param owner table schema/owner
     * @param tableName table name
     * @throws java.sql.SQLException on error
     * @return insert SQL
     */
    String getTableDataInsertSQL(DBConnection connection, String owner, String tableName) throws SQLException;

    /**
     * Get the SQL code for a query to retrieve all the data in a table.
     * @param connection
     * @param owner The table owner.
     * @param tableName The table name
     * @return The retrieve SQL.
     */
    String getTableDataSQL(DBConnection connection, String owner, String tableName);

    /**
     * Get a list of tables owned by a user.
     * @param connection
     * @param owner The owner.
     * @throws SQLException if there's an error reading the list.
     * @return The list of table names (as strings).
     */
    Collection<String> getTableNames(DBConnection connection, String owner) throws SQLException;

    /**
     * Get a list of users with objects in the database.
     * @param connection
     * @throws SQLException if there's an error reading the list.
     * @return List of user IDs (as strings).
     */
    Collection<String> getUserList(DBConnection connection) throws SQLException;

    /**
     * is this column a text-like column, i.e. it requires
     * quoting?
     * @param connection
     * @param column the column
     * @return true or false
     */
    boolean isColumnQuotable(DBConnection connection, TableColumnDescriptor column);

    /**
     * get the number of rows in a table.
     * @param connection
     * @param owner
     * @param tableName
     * @return 
     */
    int getTableSize(DBConnection connection, String owner, String tableName);
}
