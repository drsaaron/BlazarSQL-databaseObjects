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
 * @version $Id: SimpleProtocolConnectionURLBuilder.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class SimpleProtocolConnectionURLBuilder extends BaseConnectionURLBuilder implements ConnectionURLBuilder {

    private String protocolString;

    public String getProtocolString() {
        return protocolString;
    }

    public void setProtocolString(String protocolString) {
        this.protocolString = protocolString;
    }

    @Override
    public String getConnectionUrl(Properties configuration, String serverName, String databaseName) {
        return appendConfigurationProperties("jdbc:" + getProtocolString() + ":" + serverName, configuration);
    }
    
}
