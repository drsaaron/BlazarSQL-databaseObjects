/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import java.util.Properties;

/**
 *
 * @author AAR1069
 */
public class DB2ConnectionURLBuilder extends SimpleProtocolConnectionURLBuilder {

    @Override
    public String getConnectionUrl(Properties configuration, String serverName, String databaseName) {
        if (databaseName == null) {
            // assume a type 2 connection
            return super.getConnectionUrl(configuration, serverName, databaseName); //To change body of generated methods, choose Tools | Templates.
        } else {
            // assume a type 4 connection
            String baseURL = "jdbc:db2://" + serverName + "/" + databaseName;
            return appendConfigurationProperties(baseURL, configuration);
        }
    }
    
    
}
