/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

/**
 *
 * @author scott
 */
@Component("postgresObjectDataAccess")
public class PostgresObjectDataAccessImpl extends ObjectDataAccessImpl {

    @Override
    public String getMultiQueryDelimiter(DBConnection connection) {
        return ";";
    }
    
    @Override
    public String extractFullTableDefinition(DBConnection connection, String owner, String tableName) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getStoredProcedureCode(DBConnection connection, String procName, String owner) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
