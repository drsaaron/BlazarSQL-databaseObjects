/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl;

import java.util.Collection;

/**
 *
 * @author aar1069
 * @version $Id: ServerTypeDescriptorCollection.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class ServerTypeDescriptorCollection {
    
    private Collection<ServerTypeDescriptor> descriptorCollection;

    public Collection<ServerTypeDescriptor> getDescriptorCollection() {
        return descriptorCollection;
    }

    public void setDescriptorCollection(Collection<ServerTypeDescriptor> descriptorCollection) {
        this.descriptorCollection = descriptorCollection;
    }
}
