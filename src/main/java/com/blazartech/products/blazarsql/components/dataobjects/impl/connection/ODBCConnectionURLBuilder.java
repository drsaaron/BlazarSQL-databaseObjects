/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import java.util.Properties;

/**
 * a URL builder for ODBC connections.
 * 
 * @author aar1069
 * @version $Id: ODBCConnectionURLBuilder.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class ODBCConnectionURLBuilder extends SimpleProtocolConnectionURLBuilder {

    @Override
    public String getProtocolString() {
        return "odbc";
    }

    @Override
    public String getConnectionUrl(Properties configuration, String serverName, String databaseName) {
        String url = super.getConnectionUrl(configuration, serverName, databaseName);
        if (databaseName != null && databaseName.length() > 0) {
            url +=  ";DATABASE=" + databaseName;
        }
        return url;
    }
    
    
}
