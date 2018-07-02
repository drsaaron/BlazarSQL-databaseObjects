/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import java.sql.Connection;

/**
 *
 * @author aar1069
 * @version $Id: DBConnectionImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class DBConnectionImpl implements DBConnection {

    private Connection jdbcConnection;
    private String databaseTypeName;
    private String connectionURL;
    private String databaseName;
    private String serverName;
    private String userID;
    
    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDatabaseTypeName(String databaseTypeName) {
        this.databaseTypeName = databaseTypeName;
    }

    public void setJdbcConnection(Connection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    @Override
    public Connection getJDBCConnection() {
        return jdbcConnection;
    }

    @Override
    public String getDatabaseTypeName() {
        return databaseTypeName;
    }

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getConnectionURL() {
        return connectionURL;
    }
    
}
