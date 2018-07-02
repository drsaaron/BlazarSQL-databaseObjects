/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects;

import java.util.Properties;

/**
 *
 * @author aar1069
 */
public interface ConnectionURLBuilder {

    public String getConnectionUrl(Properties configuration, String serverName, String databaseName);
}
