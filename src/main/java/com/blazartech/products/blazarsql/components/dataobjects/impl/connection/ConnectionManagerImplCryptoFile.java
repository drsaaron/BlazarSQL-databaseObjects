/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import com.blazartech.products.crypto.BlazarCryptoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapt the connection manager to obtain a password from a crypto file, using
 * the database type as the resource ID.
 * 
 * @author aar1069
 * @version $Id: ConnectionManagerImplCryptoFile.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
@Component
public class ConnectionManagerImplCryptoFile extends ConnectionManagerImpl {
        
    @Autowired
    private BlazarCryptoFile cryptoFile;

    @Override
    public String getPassword(String userID, String servername, String databaseName, String databaseTypeName) {
        return cryptoFile.getPassword(userID, databaseTypeName);
    }
}
