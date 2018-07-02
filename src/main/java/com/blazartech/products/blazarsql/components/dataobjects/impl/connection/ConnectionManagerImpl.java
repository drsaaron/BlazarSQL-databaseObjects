/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.blazarsql.components.dataobjects.impl.ServerTypeDescriptor;
import com.blazartech.products.blazarsql.components.dataobjects.impl.ServerTypeDescriptorBasedManager;
import com.blazartech.products.blazarsql.components.dataobjects.ConnectionManager;
import com.blazartech.products.blazarsql.components.dataobjects.ConnectionURLBuilder;
import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement a connection manager.  
 * 
 * @author aar1069
 * @version $Id: ConnectionManagerImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public abstract class ConnectionManagerImpl extends ServerTypeDescriptorBasedManager implements ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManagerImpl.class);
    
    public abstract String getPassword(String userID, String servername, String databaseName, String databaseTypeName);

    @Override
    public DBConnection openConnection(String userID, String serverName, String databaseName, String databaseTypeName) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        logger.info("opening connection for " + userID + " on " + serverName + "/" + databaseName);
        
        // get the connection descriptor.
        ServerTypeDescriptor descriptor = getDescriptorForServerType(databaseTypeName);
        
        // get the URL builder and get the JDBC connection
        ConnectionURLBuilder urlBuilder = descriptor.getUrlBuilder();
        String jdbcConnectionURL = urlBuilder.getConnectionUrl(descriptor.getConfigurationProperties(), serverName, databaseName);
        logger.info("jdbcConnection = " + jdbcConnectionURL);
        
        // get the password.
        String password = getPassword(userID, serverName, databaseName, databaseTypeName);
        
        // instantiate the JDBC driver class and create the raw connection
        Class.forName(descriptor.getJDBCDriverClassName());
        Connection jdbcConnection = DriverManager.getConnection(jdbcConnectionURL, userID, password);
        
        // build the object.
        DBConnectionImpl connection = new DBConnectionImpl();
        connection.setConnectionURL(jdbcConnectionURL);
        connection.setDatabaseName(databaseName);
        connection.setDatabaseTypeName(databaseTypeName);
        connection.setJdbcConnection(jdbcConnection);
        connection.setServerName(serverName);
        connection.setUserID(userID);
        
        // done
        return connection;
    }

    @Override
    public Collection<String> getSupportedServerTypes() {
        return getServerTypeDescriptorMap().keySet();
    }
    
    
}
