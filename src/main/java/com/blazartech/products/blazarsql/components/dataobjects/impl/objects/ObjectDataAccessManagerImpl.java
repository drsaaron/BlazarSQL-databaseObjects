/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.impl.ServerTypeDescriptorBasedManager;
import com.blazartech.products.blazarsql.components.dataobjects.ObjectDataAccess;
import com.blazartech.products.blazarsql.components.dataobjects.ObjectDataAccessManager;
import com.blazartech.products.blazarsql.components.dataobjects.impl.ServerTypeDescriptor;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 * @version $Id: ObjectDataAccessManagerImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/

@Component
public class ObjectDataAccessManagerImpl extends ServerTypeDescriptorBasedManager implements ObjectDataAccessManager {
    
    @Override
    public ObjectDataAccess getObjectDataAccess(String databaseTypeName) {
        ServerTypeDescriptor d = getDescriptorForServerType(databaseTypeName);
        return d.getObjectDataAccess();
    }
    
}
