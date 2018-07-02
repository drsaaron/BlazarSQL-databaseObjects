/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects;

import java.sql.Connection;

/**
 *
 * @author aar1069
 */
public interface DBConnection {
    
    /**
     * get the raw JDBC connection
     * @return 
     */
    public Connection getJDBCConnection();
    
    /**
     * get the type of the database
     * @return 
     */
    public String getDatabaseTypeName();
    
    /**
     * get the user ID associated to the connection
     * @return 
     */
    public String getUserID();
    
    /**
     * get the name of the server
     * @return 
     */
    public String getServerName();
    
    /**
     * get the name of the database
     * @return 
     */
    public String getDatabaseName();
    
    /**
     * get the JDBC connection URL
     * @return 
     */
    public String getConnectionURL();
    
    
}
