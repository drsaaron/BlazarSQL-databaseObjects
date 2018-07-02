/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author aar1069
 */
public interface ConnectionManager {
    
    public Collection<String> getSupportedServerTypes();

    /**
     * open a new connection.  A password is not needed.  The password will
     * be obtained internally.
     * 
     * @param userId
     * @param serverName
     * @param databaseName
     * @param databaseTypeName
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public DBConnection openConnection(String userId, String serverName, String databaseName, String databaseTypeName)  throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException;
}
