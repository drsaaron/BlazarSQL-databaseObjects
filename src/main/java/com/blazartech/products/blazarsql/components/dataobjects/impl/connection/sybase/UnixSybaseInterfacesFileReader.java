/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection.sybase;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * reads the $SYBASE/$SYBASE_OCS/interfaces file, which is the Unix-formatted
 * version of the interfaces file.
 * 
 * @author aar1069
 * @version $Id: UnixSybaseInterfacesFileReader.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class UnixSybaseInterfacesFileReader extends SybaseInterfacesFileReaderImpl implements SybaseInterfacesFileReader {

    private static final String ETHER = "ether";
    
    @Override
    public SybaseConnectionParameters readInterfacesFile(String serverName) throws IOException {
        BufferedReader is = openInterfacesFile();
        String line;
        do {
            line = is.readLine();
        } while (!line.startsWith(serverName));

        // Read the next line, which gives the host and port.
        String nextLine = is.readLine();
        int pos = nextLine.indexOf(ETHER);
        pos += ETHER.length() + 1;
        int endPos = nextLine.indexOf(' ', pos);
        String host = nextLine.substring(pos, endPos);
        String port = nextLine.substring(endPos + 1);

        // Build the ConnectionData.
        SybaseConnectionParameters parameters = new SybaseConnectionParameters();
        parameters.host = host;
        parameters.port = port;
        
        // done.
        return parameters;
    }
}
