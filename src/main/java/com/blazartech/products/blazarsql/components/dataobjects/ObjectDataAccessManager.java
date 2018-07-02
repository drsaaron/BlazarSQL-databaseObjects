/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects;

/**
 *
 * @author aar1069
 */
public interface ObjectDataAccessManager {
 
    /**
     * get the data access bean for a given database type
     * 
     * @param databaseTypeName
     * @return 
     */
    public ObjectDataAccess getObjectDataAccess(String databaseTypeName);
}
