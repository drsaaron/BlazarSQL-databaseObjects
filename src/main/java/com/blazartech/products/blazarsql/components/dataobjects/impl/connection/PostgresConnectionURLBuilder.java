/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.blazarsql.components.dataobjects.ConnectionURLBuilder;
import java.util.Properties;

/**
 *
 * @author scott
 */
public class PostgresConnectionURLBuilder extends BaseConnectionURLBuilder implements ConnectionURLBuilder {
    
    private String defaultConnectionPort;

    public String getDefaultConnectionPort() {
        return defaultConnectionPort;
    }

    public void setDefaultConnectionPort(String defaultConnectionPort) {
        this.defaultConnectionPort = defaultConnectionPort;
    }
    
    @Override
    public String getConnectionUrl(Properties configuration, String serverName, String databaseName) {
        String port = getDefaultConnectionPort();
        if (!serverName.contains(":")) { serverName += ":" + port; } // default port.
        String baseURL = "jdbc:postgresql://" + serverName + "/" + databaseName;
        return appendConfigurationProperties(baseURL, configuration);
    }
}
