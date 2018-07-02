/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects;

import java.io.Serializable;

/**
 *
 * @author aar1069
 * @version $Id: TableColumnDescriptor.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class TableColumnDescriptor implements Serializable {
    
    private String name;
    private String typeName;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    
}
