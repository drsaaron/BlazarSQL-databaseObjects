/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author aar1069
 * @version $Id: ServerTypeDescriptorBasedManager.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/

public abstract class ServerTypeDescriptorBasedManager implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        serverTypeDescriptors.getDescriptorCollection().stream().forEach((d) -> {
            serverTypeDescriptorMap.put(d.getServerTypeName(), d);
        });
    }
    
    Map<String, ServerTypeDescriptor> serverTypeDescriptorMap = new TreeMap<>();

    public Map<String, ServerTypeDescriptor> getServerTypeDescriptorMap() {
        return serverTypeDescriptorMap;
    }
    
    @Autowired
    private ServerTypeDescriptorCollection serverTypeDescriptors;
    
    public ServerTypeDescriptor getDescriptorForServerType(String serverTypeName) {
        ServerTypeDescriptor d = serverTypeDescriptorMap.get(serverTypeName);
        if (d != null) {
            return d;
        }
        
        throw new IllegalArgumentException("no descriptor found for " + serverTypeName);
    }
}
