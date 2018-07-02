/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author aar1069
 * @version $Id: SybaseInterfacesFileReaderImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public abstract class SybaseInterfacesFileReaderImpl {
    
    private String interfacesFilePath;

    public String getInterfacesFilePath() {
        return interfacesFilePath;
    }

    public void setInterfacesFilePath(String interfacesFilePath) {
        this.interfacesFilePath = interfacesFilePath;
    }
    
    public BufferedReader openInterfacesFile() throws IOException {
        File interfacesFile = new File(getInterfacesFilePath());
        BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(interfacesFile)));
        return is;
    }
}
