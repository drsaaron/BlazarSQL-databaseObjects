/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase;

import java.io.IOException;

/**
 *
 * @author aar1069
 */
public interface SybaseInterfacesFileReader {
    
    public SybaseConnectionParameters readInterfacesFile(String serverName) throws IOException;
}
