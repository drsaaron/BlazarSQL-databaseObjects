/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl;

import com.blazartech.products.blazarsql.components.dataobjects.ConnectionURLBuilder;
import com.blazartech.products.blazarsql.components.dataobjects.ObjectDataAccess;
import java.util.Properties;

/**
 *
 * @author aar1069
 * @version $Id: ServerTypeDescriptor.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class ServerTypeDescriptor {
    
    private String serverTypeName;
    private String JDBCDriverClassName;
    private ConnectionURLBuilder urlBuilder;
    private Properties configurationProperties;
    private ObjectDataAccess objectDataAccess;

    public ObjectDataAccess getObjectDataAccess() {
        return objectDataAccess;
    }

    public void setObjectDataAccess(ObjectDataAccess objectDataAccess) {
        this.objectDataAccess = objectDataAccess;
    }

    public String getJDBCDriverClassName() {
        return JDBCDriverClassName;
    }

    public void setJDBCDriverClassName(String JDBCDriverClassName) {
        this.JDBCDriverClassName = JDBCDriverClassName;
    }

    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public String getServerTypeName() {
        return serverTypeName;
    }

    public void setServerTypeName(String databaseTypeName) {
        this.serverTypeName = databaseTypeName;
    }

    public ConnectionURLBuilder getUrlBuilder() {
        return urlBuilder;
    }

    public void setUrlBuilder(ConnectionURLBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }
    
}
