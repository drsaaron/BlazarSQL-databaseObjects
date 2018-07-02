/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase.SybaseConnectionParameters;
import com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase.SybaseInterfacesFileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aar1069
 * @version $Id: SybaseConnectionURLBuilder.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public abstract class SybaseConnectionURLBuilder extends BaseConnectionURLBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SybaseConnectionURLBuilder.class);

    private static final Map<String, SybaseConnectionParameters> serverMap = new HashMap<>();

    private SybaseInterfacesFileReader interfacesReader;

    public SybaseInterfacesFileReader getInterfacesReader() {
        return interfacesReader;
    }

    public void setInterfacesReader(SybaseInterfacesFileReader interfacesReader) {
        this.interfacesReader = interfacesReader;
    }
    
    /**
     * Read the sybase interfaces file to get the host and port for the desired server.
     * @param serverName desired server name
     * @param databaseName desired database name
     * @return host and port
     */
    protected synchronized SybaseConnectionParameters readInterfacesFile(String serverName, String databaseName) {
        // try to find the URL from the map.
        SybaseConnectionParameters parameters = serverMap.get(serverName);
        if (parameters == null) {
            try {
                parameters = interfacesReader.readInterfacesFile(serverName);
                serverMap.put(serverName, parameters);
            } catch (IOException e) {
                throw new RuntimeException("error reading interfaces file: " + e.getMessage(), e);
            }
        }
        return parameters;
    }
}
