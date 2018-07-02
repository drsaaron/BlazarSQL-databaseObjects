/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import com.blazartech.products.blazarsql.components.dataobjects.TableColumnDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A class to access object information from a Microsoft Access database.  Much of
 * the generic JDBC code from the base class is overridden because it appears
 * Access does not work the way other database systems do.
 * 
 * @author aar1069
 * @version $Id: MSAccessObjectDataAccessImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class MSAccessObjectDataAccessImpl extends ObjectDataAccessImpl {

    @Override
    public String extractFullTableDefinition(DBConnection connection, String owner, String tableName) throws SQLException {
        return "Not supported for this server type.";
    }

    @Override
    public String getStoredProcedureCode(DBConnection connection, String procName, String owner) throws SQLException {
        return "Not supported for this server type.";
    }

    /** Get a list of users with objects in the database.  The only user will be Admin.
     * @param connection
     * @throws SQLException if there's an error reading the list.
     * @return List of user IDs (as strings).
     */
    @Override
    public Collection<String> getUserList(DBConnection connection) throws SQLException {
        return createDummyUserList();
    }

    /** Get a list of tables owned by a user.
     * @param connection
     * @param owner The owner.
     * @throws SQLException if there's an error reading the list.
     * @return The list of table names (as strings).
     */
    @Override
    public Collection<String> getTableNames(DBConnection connection, String owner) throws java.sql.SQLException {
        ResultSet tableSet = getDatabaseMetaData(connection).getTables(null, null, null, new String[]{"TABLE"});
        return populateSingleValueList(tableSet, "TABLE_NAME");
    }

    /** Get a list of stored procedure names for a user.
     * @param owner The owner of the stored procedures.
     * @throws SQLException if there's an error reading the list.
     * @return The list of procedures (as strings).
     */
    @Override
    public Collection<String> getStoredProcedureNames(DBConnection connection, String owner) throws java.sql.SQLException {
        return new ArrayList<>();
    }

    /** Get a list of the columns in a table.
     * @param connection
     * @param tableName The name of the table.
     * @param owner The table owner.
     * @throws SQLException if there's an error getting the column list.
     * @return The column list.  This will be list of type {@link TableColumnDescriptor}'s.
     */
    @Override
    public Collection<TableColumnDescriptor> getTableColumns(DBConnection connection, String tableName, String owner) throws java.sql.SQLException {
        ResultSet tableResult = getDatabaseMetaData(connection).getColumns(null, null, tableName, null);
        return buildTableColumnList(tableResult);
    }

    @Override
    protected String buildCanonicalObjectName(String owner, String objectName) {
        return "[" + objectName + "]";
    }
}
