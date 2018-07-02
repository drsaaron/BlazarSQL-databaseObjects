/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * reads the %SYBASE%/%SYBASE_OCS%/sql.ini file, which is the windows-formatted
 * version of the interfaces file.
 * 
 * @author aar1069
 * @version $Id: WindowsSybaseInterfacesFileReader.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class WindowsSybaseInterfacesFileReader extends SybaseInterfacesFileReaderImpl implements SybaseInterfacesFileReader {

    @Override
    public SybaseConnectionParameters readInterfacesFile(String serverName) throws IOException {
        BufferedReader is = openInterfacesFile();
        String line;
        do {
            line = is.readLine();
        } while (!line.equals("[" + serverName + "]"));

        // Read the next line, which gives the host and port.
        String nextLine = is.readLine();
        
        /* the line is structured like 
         * WIN3_QUERY=WNLWNSCK,poggle1,2532,URGENT
         * so split it and grab the server and port.
         */
        String[] elements = nextLine.split(",");
        String host = elements[1];
        String port = elements[2];

        // Build the ConnectionData.
        SybaseConnectionParameters parameters = new SybaseConnectionParameters();
        parameters.host = host;
        parameters.port = port;
        
        // done.
        return parameters;
    }
    
}
