/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.blazarsql.components.dataobjects.ConnectionURLBuilder;
import java.util.Properties;

/**
 *
 * @author aar1069
 * @version $Id: MySQLConnectionURLBuilder.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class MySQLConnectionURLBuilder extends BaseConnectionURLBuilder implements ConnectionURLBuilder {

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
        String baseURL = "jdbc:mysql://" + serverName + "/" + databaseName;
        return appendConfigurationProperties(baseURL, configuration);
    }
    
}
