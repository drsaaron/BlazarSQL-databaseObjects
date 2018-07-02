/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.blazarsql.components.dataobjects.ConnectionURLBuilder;
import com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase.SybaseConnectionParameters;
import java.util.Properties;

/**
 *
 * @author aar1069
 * @version $Id: JConnectConnectionURLBuilder.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class JConnectConnectionURLBuilder extends SybaseConnectionURLBuilder implements ConnectionURLBuilder {

    @Override
    public String getConnectionUrl(Properties connectionDescriptorProperties, String serverName, String databaseName) {
        String serverPart = "";
        if (serverName.contains(":")) {
            // assume the caller has given us the host and port already
            serverPart = serverName;
        } else {
            SybaseConnectionParameters data = readInterfacesFile(serverName, databaseName);
            serverPart = data.host + ":" + data.port;
        }
        
        String url = "jdbc:sybase:Tds:" + serverPart;
        
        if (databaseName != null && databaseName.length() > 0) { url += "/" + databaseName; }
        
        // add any properties.
        url = appendConfigurationProperties(url, connectionDescriptorProperties);

        return url;
    }

}
