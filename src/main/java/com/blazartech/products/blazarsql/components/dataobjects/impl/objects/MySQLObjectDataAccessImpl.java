/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 * @version $Id: MySQLObjectDataAccessImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
@Component("mysqlObjectDataAccess")
public class MySQLObjectDataAccessImpl extends ObjectDataAccessImpl {

    @Override
    public String getMultiQueryDelimiter(DBConnection connection) {
        return ";";
    }
   
    /** Create the SQL code to fully create the table.  This will
     * include all referential and check constraints, indexes,
     * primary keys, permissions, etc.
     * @param connection
     * @param owner The table owner.
     * @param tableName The table name.
     * @throws SQLException If there is an error retrieving the information.
     * @return The full SQL code.
     */
    @Override
    public String extractFullTableDefinition(DBConnection connection, String owner, String tableName) throws SQLException {
        ResultSet r = executeQuery(connection, "show create table " + tableName);
        String createSQL;
        
        if (r.next()) {
            createSQL = r.getString("Create Table") + getMultiQueryDelimiter(connection);
            r.close();
        } else {
            return "Table not found.";
        }
        
        // get triggers.
        r = executeQuery(connection, "show triggers");
        while (r.next()) {
            String triggerTableName = r.getString("Table");
            if (triggerTableName.equals(tableName)) {
                createSQL += "\n\n";
                createSQL += "create trigger " + r.getString("Trigger") + " " + r.getString("Timing") + " " + r.getString("Event") + " on " + tableName + "\nfor each row " + r.getString("Statement") + getMultiQueryDelimiter(connection);
            }
        }
        
        return createSQL;
    }

    @Override
    protected String buildCanonicalObjectName(String owner, String objectName) {
        return objectName;
    }

    @Override
    public Collection<String> getStoredProcedureNames(DBConnection connection, String owner) throws SQLException {
        String sql = "SELECT routine_name, routine_type, definer, routine_definition FROM information_schema.routines WHERE routine_schema = DATABASE();";
        ResultSet tableSet = executeQuery(connection, sql);
        return populateSingleValueList(tableSet, "routine_name");
    }

    @Override
    public String getStoredProcedureCode(DBConnection connection, String procName, String owner) throws SQLException {
        ResultSet r = executeQuery(connection, "show create procedure " + procName);
        if (r.next()) {
            return r.getString("Create Procedure");
        } else {
            return "Procedure not found.";
        }
    }

    /**
     * override the general logic to simply return a default user; MySQL doesn't
     * really work the same way as other databases, apparently.  Make the default user
     * the database name since that makes things like the insert SQL generate actually
     * usable SQL.
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    @Override
    public Collection<String> getUserList(DBConnection connection) throws SQLException {
        List<String> databaseList = Arrays.asList(connection.getDatabaseName());
        return databaseList;
    }
    
    
}
