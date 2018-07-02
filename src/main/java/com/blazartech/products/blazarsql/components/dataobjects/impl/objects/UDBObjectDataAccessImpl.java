/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import com.blazartech.products.blazarsql.components.dataobjects.TableColumnDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 * @version $Id: UDBObjectDataAccessImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
@Component("udbObjectDataAccess")
public class UDBObjectDataAccessImpl extends ObjectDataAccessImpl {

    private static final Logger logger = LoggerFactory.getLogger(UDBObjectDataAccessImpl.class);
    private static final Map<String, Integer> variableTypeMap = new HashMap<>();

    private String columnList(String[] fieldList) {
        String sql = "";
        for (int i = 1; i < fieldList.length; i++) {
            String fieldName = fieldList[i].substring(1);
            char sign = fieldList[i].charAt(0);
            sql += fieldName;
            if (sign == '-') {
                sql += " DESC";
            }
            if (i < fieldList.length - 1) {
                sql += ", ";
            }
        }
        return sql;
    }

    private String padText(String text, int length) {
        String padded = text;
        while (padded.length() < length) {
            padded += " ";
        }
        return padded;
    }

    /**
     * Get the table columns.  For some reason, the generic process implemented
     * in the base class doesn't work for UDB.  So directly query the SYSCOLUMNS
     * table and get the info.
     * 
     * @param connection
     * @param tableName
     * @param owner
     * @return
     * @throws SQLException 
     */
    @Override
    public Collection<TableColumnDescriptor> getTableColumns(DBConnection connection, String tableName, String owner) throws SQLException {
        logger.info("getting table columns for " + owner + "." + tableName);
        
        String sql = "select * from SYSIBM.SYSCOLUMNS where TBNAME = ? and TBCREATOR = ? ";
        ResultSet rs = executeQuery(connection, sql, new String[] { tableName, owner });
        
        Collection<TableColumnDescriptor> descriptors = new ArrayList<>();
        while (rs.next()) {
            TableColumnDescriptor d = new TableColumnDescriptor();
            d.setName(rs.getString("NAME"));
            d.setType(0); // TODO: fix
            d.setTypeName(rs.getString("COLTYPE"));
            descriptors.add(d);
        }
        
        return descriptors;
    }
    
    

    /** Create the SQL code to fully create the table.  This will
     * include all referential and check constraints, indexes,
     * primary keys, permissions, etc.
     * @param connection
     * @param owner The table owner.
     * @param tableName The table name.
     * @throws SQLException If there is an error retrieving the information.
     * @return The full SQL code.
     */
    @Override
    public String extractFullTableDefinition(DBConnection connection, String owner, String tableName) throws java.sql.SQLException {

        // if we haven't yet gotten the variable length types, get them now.
        if (variableTypeMap.isEmpty()) {
            try {
                ResultSet result = executeQuery(connection, "select TYPENAME from SYSCAT.DATATYPES where LENGTH = 0");
                String typeName = result.getString("TYPENAME");
                variableTypeMap.put(typeName, 1);
            } catch (SQLException e) {
                logger.info("exception " + e.getErrorCode() + " " + e.getMessage());
//                if (e.getSQLState().equals("42704")) 
            }
        }

        // start.
        String sql = "CREATE TABLE " + owner + "." + tableName + "\n(\n";
        ResultSet result = executeQuery(connection, "select * from SYSCAT.COLUMNS where TABSCHEMA = '" + owner + "' and TABNAME = '" + tableName + "' order by COLNO");
        while (result.next()) {
            String columnName = result.getString("COLNAME");
            String typeName = result.getString("TYPENAME");
            int length = result.getInt("LENGTH");
            int scale = result.getInt("SCALE");
            boolean allowNulls = result.getString("NULLS").equals("Y");
            boolean isIdentity = result.getString("IDENTITY").equals("Y");
            String generated = result.getString("GENERATED");
            String defaultValue = result.getString("DEFAULT");
            int codePage = result.getInt("CODEPAGE");

            String rowSql = "    " + padText(columnName, 25);

            String typeSql = typeName;
            if (variableTypeMap.get(typeName) != null) {
                typeSql += "(" + length;
                if (scale > 0) {
                    typeSql += "," + scale;
                }
                typeSql += ")";
            }
            rowSql += padText(typeSql, 15);

            if (isIdentity) {
                rowSql += "GENERATED BY ";
                if (generated.equals("D")) {
                    rowSql += "DEFAULT ";
                }
                rowSql += "AS IDENTITY (START WITH 1, INCREMENT BY " + result.getString("KEYSEQ") + ", CACHE " + result.getString("NQUANTILES") + ")";
            } else if (!allowNulls) {
                rowSql += "NOT NULL";
            }

            if (defaultValue != null) {
                if (codePage > 0) {
                    // is a string or character
                    defaultValue = "'" + defaultValue + "'";
                } else if (typeName.equals("DATE")) {
                    defaultValue = "date('" + defaultValue + "')";
                } else if (typeName.equals("TIMESTAMP")) {
                    // not sure....
                }
                rowSql += " DEFAULT " + defaultValue;
            }

            rowSql += ",\n";
            sql += rowSql;
        }
        sql = sql.replaceAll(",$", "\n)");

        // add the indices.
        result = executeQuery(connection, "select * from SYSCAT.INDEXES where INDSCHEMA = '" + owner + "' and TABNAME = '" + tableName + "'");
        while (result.next()) {
            String indexName = result.getString("INDNAME");
            String[] fieldList = splitColumnNames(result.getString("COLNAMES"));
            boolean isUnique = result.getInt("UNIQUE_COLCOUNT") > 0;
            sql += ";\n";
            sql += "CREATE ";
            if (isUnique) {
                sql += "UNIQUE ";
            }
            sql += "INDEX " + owner + "." + indexName + "\n   ON " + owner + "." + tableName + "(" + columnList(fieldList) + ")\n";
        }

        // primary keys.  This is obtained from the index list.
        result = executeQuery(connection, "select * from SYSCAT.INDEXES where INDSCHEMA = '" + owner + "' and TABNAME = '" + tableName + "' and UNIQUERULE = 'P'");
        while (result.next()) {
            String indexName = result.getString("INDNAME");
            String[] fieldList = splitColumnNames(result.getString("COLNAMES"));
            sql += ";\n";
            sql += "ALTER TABLE " + owner + "." + tableName + "\n";
            sql += "    ADD CONSTRAINT " + indexName + "\n";
            sql += "PRIMARY KEY (" + columnList(fieldList) + ")\n";
        }

        // referential constraints.
        result = executeQuery(connection, "select * from SYSCAT.REFERENCES where TABSCHEMA = '" + owner + "' and TABNAME = '" + tableName + "'");
        while (result.next()) {
            String constraintName = result.getString("CONSTNAME");
            String fkColumns = result.getString("FK_COLNAMES");
            String pkColumns = result.getString("PK_COLNAMES");
            String refTable = result.getString("REFTABNAME");
            String refTableOwner = result.getString("REFTABSCHEMA").replaceAll(" *$", "");

            sql += ";\n";
            sql += "ALTER TABLE " + owner + "." + tableName + "\n";
            sql += "    ADD CONSTRAINT " + constraintName + " FOREIGN KEY (" + fkColumns + ") REFERENCES " + refTableOwner + "." + refTable + " (" + pkColumns + ")";
            sql += " ON DELETE " + (result.getString("DELETERULE").equals("R") ? "RESTRICT" : "NO ACTION");
            sql += " ON UPDATE " + (result.getString("UPDATERULE").equals("R") ? "RESTRICT" : "NO ACTION");
            sql += "\n";
        }

        // Grant statements.  These may not be accessible by the user.
        try {
            String grantSql = "";
            result = executeQuery(connection, "select * from SYSCAT.TABAUTH where TABSCHEMA = '" + owner + "' and TABNAME = '" + tableName + "' order by GRANTEE");
            while (result.next()) {
                String grantor = result.getString("GRANTOR");
                String grantee = result.getString("GRANTEE");
                String granteeType = result.getString("GRANTEETYPE");
                for (String permission : permissionList) {
                    String permKey = permission + "AUTH";
                    String permValue = result.getString(permKey);
                    if (!permValue.equals("N")) {
                        grantSql += ";\n";
                        grantSql += "GRANT " + (permission.equals("REF") ? "REFERENCES" : permission) + " ON " + owner + "." + tableName + " TO " + groupTypeMap.get(granteeType) + " " + grantee;
                        if (permValue.equals("G")) {
                            grantSql += " WITH GRANT OPTION";
                        }
                        grantSql += "\n";
                    }
                }

            }

            sql += grantSql;
        } catch (SQLException ex) {
            logger.error("error getting permissions: " + ex.getMessage(), ex);
        }

        // all done.
        return sql;
    }
    private static final String[] permissionList = {"CONTROL", "ALTER", "DELETE", "INDEX", "INSERT", "SELECT", "REF", "UPDATE"};
    private static final Map<String, String> groupTypeMap = new HashMap<>();

    static {
        groupTypeMap.put("G", "GROUP");
        groupTypeMap.put("U", "USER");
    }

    /** Retrieve the code for a given stored procedure.
     * @param connection
     * @param procName The procedure name.
     * @param owner The procedure owner.
     * @throws SQLException if there is an error retrieving the code.
     * @return The procedure code.
     */
    @Override
    public String getStoredProcedureCode(DBConnection connection, String procName, String owner) throws java.sql.SQLException {
        return "not yet implemented.";
    }

    private String[] splitColumnNames(String s) {
        String modString = s.replace("+", " +").replace("-", " -");
        return modString.split(" ");
    }

    /**
     * get the delimiter for multi-SQL statement queries
     * 
     * @param connection
     * @return delimiter, default is empty string
     */
    @Override
    public String getMultiQueryDelimiter(DBConnection connection) {
        return ";";
    }

    /**
     * I'm not sure why I need to override this here since the parent version
     * is generic and uses the driver's metadata, but with the version 4 driver
     * that doesn't seem to work.
     * 
     * @param connection
     * @param owner
     * @return
     * @throws SQLException 
     */
    @Override
    public Collection<String> getTableNames(DBConnection connection, String owner) throws SQLException {
        String sql = "select NAME as TABLE_NAME from sysibm.systables where CREATOR = ? and TYPE = ?";
        ResultSet rs = executeQuery(connection, sql, new String[] { owner, "T" });
        return populateSingleValueList(rs, "TABLE_NAME");
    }
    
}
