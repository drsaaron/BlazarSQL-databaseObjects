/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aar1069
 * @version $Id: SybaseObjectDataAccessImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
public class SybaseObjectDataAccessImpl extends ObjectDataAccessImpl {

    private String getComment(DBConnection connection, int idNumber) throws SQLException {
        String txt = "";
        ResultSet result = executeQuery(connection, "select text from dbo.syscomments where id = ?", new Object[]{idNumber});
        while (result.next()) {
            txt += result.getString("text");
        }
        return txt;
    }

    private String dumpTable(DBConnection connection, String owner, String tableName, int objectIdNumber) throws SQLException {
        // get the current database.
        ResultSet result = executeQuery(connection, "select spid = @@spid");
        result.next();
        int currentProcId = result.getInt(1);
        result.close();
        result = executeQuery(connection, "sp_who");
        while (result.next()) {
            String spid = result.getString("spid");
            int intSpid = Integer.parseInt(spid.trim());
            if (intSpid == currentProcId) {
                break;
            }
        }
        String currentDatabase = result.getString("dbname");
        while (result.next()) {
        }

        // get referenced tables.
        String qry = "select isnull (r.frgndbname, '" + currentDatabase + "') as dbname,    object_name (r.constrid) as constraintName,    object_name (r.reftabid, r.frgndbid) as referencedObjectName,    user_name (o2.uid) as referencedObjectOwner,    fokey1 = col_name (r.tableid, r.fokey1),    fokey2 = col_name (r.tableid, r.fokey2),    fokey3 = col_name (r.tableid, r.fokey3),    fokey4 = col_name (r.tableid, r.fokey4),    fokey5 = col_name (r.tableid, r.fokey5),    fokey6 = col_name (r.tableid, r.fokey6),    fokey7 = col_name (r.tableid, r.fokey7),    fokey8 = col_name (r.tableid, r.fokey8),    fokey9 = col_name (r.tableid, r.fokey9),    fokey10 = col_name (r.tableid, r.fokey10),    fokey11 = col_name (r.tableid, r.fokey11),    fokey12 = col_name (r.tableid, r.fokey12),    fokey13 = col_name (r.tableid, r.fokey13),    fokey14 = col_name (r.tableid, r.fokey14),    fokey15 = col_name (r.tableid, r.fokey15),    fokey16 = col_name (r.tableid, r.fokey16),    refkey1 = col_name (r.reftabid, r.refkey1),    refkey2 = col_name (r.reftabid, r.refkey2),    refkey3 = col_name (r.reftabid, r.refkey3),    refkey4 = col_name (r.reftabid, r.refkey4),    refkey5 = col_name (r.reftabid, r.refkey5),    refkey6 = col_name (r.reftabid, r.refkey6),    refkey7 = col_name (r.reftabid, r.refkey7),    refkey8 = col_name (r.reftabid, r.refkey8),    refkey9 = col_name (r.reftabid, r.refkey9),    refkey10 = col_name (r.reftabid, r.refkey10),    refkey11 = col_name (r.reftabid, r.refkey11),    refkey12 = col_name (r.reftabid, r.refkey12),    refkey13 = col_name (r.reftabid, r.refkey13),    refkey14 = col_name (r.reftabid, r.refkey14),    refkey15 = col_name (r.reftabid, r.refkey15),    refkey16 = col_name (r.reftabid, r.refkey16) from dbo.sysreferences r, dbo.sysobjects o1, dbo.sysobjects o2, dbo.sysusers u where r.tableid = o1.id and r.pmrydbname is null and o1.name = ? and o1.uid = u.uid and u.name = ? and r.reftabid *= o2.id";
        result = executeQuery(connection, qry, new Object[]{tableName, owner});
        List<ConstraintDescriptor> references = new ArrayList<>();
        while (result.next()) {
            ConstraintDescriptor d = new ConstraintDescriptor();
            d.setDatabaseName(result.getString("dbname"));
            d.setConstraintName(result.getString("constraintName"));
            d.setReferencedObjectName(result.getString("referencedObjectName"));
            d.setReferencedObjectOwner(result.getString("referencedObjectOwner"));
            for (int i = 0; i < 16; i++) {
                String keyName = result.getString(i + 5);
                d.setForeignKeyName(i, keyName);
            }
            for (int i = 0; i < 16; i++) {
                String keyName = result.getString(i + 21);
                d.setReferenceKeyName(i, keyName);
            }
            references.add(d);
        }

        // Get the columns.
        result = executeQuery(connection, "select distinct Column_name = c.name,    Type = t.name,    Length = c.length,    Prec = c.prec,    Scale = c.scale,    Nulls = convert(bit, (c.status & 8)),   Default_name = object_name(c.cdefault),   Rule_name = object_name(c.domain),   Ident = convert(bit, (c.status & 0x80)),   Default_Ddl = isnull (d.status & 4096, 0),   Rule_Ddl = isnull (r.status & 4096, 0),   DefaultId = c.cdefault,   RuleId = c.domain from   dbo.syscolumns c, dbo.systypes t,   dbo.sysprocedures d, dbo.sysprocedures r where  c.id = ? and    c.usertype *= t.usertype and    c.cdefault *= d.id and    c.domain *= r.id order by c.colid", new Object[]{objectIdNumber});
        List<ColumnDescriptor> columnList = new ArrayList<>();
        while (result.next()) {
            ColumnDescriptor column = new ColumnDescriptor();
            column.setColumnName(result.getString("Column_name"));
            column.setColumnType(result.getString("Type"));
            column.setLength(result.getInt("Length"));
            column.setPrecision(result.getInt("Prec"));
            column.setScale(result.getInt("Scale"));
            column.setAllowNull(result.getInt("Nulls") > 0);
            column.setDefaultName(result.getString("Default_name"));
            column.setRuleName(result.getString("Rule_name"));
            column.setIdentity(result.getInt("Ident") > 0);
            column.setDefaultDDL(result.getInt("Default_Ddl"));
            column.setRuleDDL(result.getInt("Rule_Ddl"));
            column.setDefaultId(result.getInt("DefaultId"));
            column.setRuleId(result.getInt("RuleId"));
            columnList.add(column);
        }

        // Get the index and keys
        result = executeQuery(connection, "select name, indid, status, status2,    key1 = index_col ('" + owner + "." + tableName + "', indid, 1),    key2 = index_col ('" + owner + "." + tableName + "', indid, 2),    key3 = index_col ('" + owner + "." + tableName + "', indid, 3),    key4 = index_col ('" + owner + "." + tableName + "', indid, 4),    key5 = index_col ('" + owner + "." + tableName + "', indid, 5),    key6 = index_col ('" + owner + "." + tableName + "', indid, 6),    key7 = index_col ('" + owner + "." + tableName + "', indid, 7),    key8 = index_col ('" + owner + "." + tableName + "', indid, 8),    key9 = index_col ('" + owner + "." + tableName + "', indid, 9),    key10 = index_col ('" + owner + "." + tableName + "', indid, 10),    key11 = index_col ('" + owner + "." + tableName + "', indid, 11),    key12 = index_col ('" + owner + "." + tableName + "', indid, 12),    key13 = index_col ('" + owner + "." + tableName + "', indid, 13),    key14 = index_col ('" + owner + "." + tableName + "', indid, 14),    key15 = index_col ('" + owner + "." + tableName + "', indid, 15),    key16 = index_col ('" + owner + "." + tableName + "', indid, 16) from dbo.sysindexes where id = ? and indid between 1 and 254", new Object[]{objectIdNumber});
        List<IndexKeyDescriptor> indexKeyList = new ArrayList<>();
        while (result.next()) {
            IndexKeyDescriptor ik = new IndexKeyDescriptor();
            ik.setName(result.getString("name"));
            ik.setIdNumber(result.getInt("indid"));
            ik.setStatus(result.getInt("status"));
            ik.setStatus2(result.getInt("status2"));
            for (int i = 0; i < 16; i++) {
                ik.setColumn(i, result.getString(i + 5));
            }
            indexKeyList.add(ik);
        }

        // Get check constraints on the table.
        result = executeQuery(connection, "select constrid from dbo.sysconstraints where tableid = ? and status & 128 = 128 and colid = 0", new Object[]{objectIdNumber});
        List<Integer> checkConstraintIdList = new ArrayList<>();
        while (result.next()) {
            checkConstraintIdList.add(result.getInt("constrid"));
        }

        // start building.
        String sql = "\nCREATE TABLE " + owner + "." + tableName + " (\n";
        boolean firstTime = true;
        Map<String, String> ddlMap = new HashMap<>(), ruleMap = new HashMap<>();
        for (ColumnDescriptor c : columnList) {
            if (firstTime) {
                firstTime = false;
            } else {
                sql += ",\n";
            }

            // get DDL default and rule.
            String ddlDefault = "", ddlRule = "";
            if (c.getDefaultDDL() > 0) {
                ddlDefault = getComment(connection, c.getDefaultId());
            }
            if (c.getRuleDDL() > 0) {
                ddlRule = getComment(connection, c.getRuleId());
            }

            // get the nullable property
            String nullable;
            if (c.isIdentity()) {
                nullable = "identity";
            } else {
                nullable = (c.isAllowNull()) ? "null" : "not null";
            }

            // define this column.
            sql += "\t" + c.getColumnName() + "\t" + c.getColumnType();
            if (c.getColumnType().contains("char") || c.getColumnType().contains("bin")) {
                sql += "(" + c.getLength() + ")";
            }
            if (c.getColumnType().contains("numeric") || c.getColumnType().contains("decimal")) {
                sql += "(" + c.getPrecision() + "," + c.getScale() + ")";
            }
            sql += " " + ddlDefault + " " + nullable + " " + ddlRule;

            if (c.getRuleName() != null
                    && c.getRuleDDL() == 0) {
                ruleMap.put(tableName + "." + c.getColumnName(), c.getRuleName());
            }
            if (c.getDefaultName() != null
                    && c.getDefaultDDL() == 0) {
                ddlMap.put(tableName + "." + c.getColumnName(), c.getDefaultName());
            }
        }

        // add the references.
        for (ConstraintDescriptor d : references) {
            sql += ",\n\t";
            sql += "CONSTRAINT " + d.getConstraintName() + " FOREIGN KEY (" + columnList(d.getForeignKeyName()) + ") REFERENCES " + d.getReferencedObjectOwner() + "." + d.getReferencedObjectName() + " (" + columnList(d.getReferenceKeyName()) + ")";
        }

        // add constraints
        List<IndexKeyDescriptor> indexList = new ArrayList<>();
        for (IndexKeyDescriptor ik : indexKeyList) {
            if ((ik.getStatus2() & 2) > 0) {
                sql += ",\n\t";
                if ((ik.getStatus2() & 8) > 0) {
                } else {
                    sql += "CONSTRAINT " + ik.getName() + " ";
                }

                if ((ik.getStatus() & 2048) > 0) {
                    sql += "PRIMARY KEY ";
                    if (ik.getIdNumber() != 1) {
                        sql += "NONCLUSTERED ";
                    }
                } else {
                    sql += "UNIQUE ";
                    if (ik.getIdNumber() == 1) {
                        sql += "CLUSTERED ";
                    }
                }

                sql += "(" + columnList(ik.getColumn()) + ")";
            } else {
                // index, so save it for later.
                indexList.add(ik);
            }
        }

        // add check constraints.
        for (Integer id : checkConstraintIdList) {
            sql += ",\n\t" + getComment(connection, id);
        }

        // done creating table itself.
        sql += "\n)\ngo\n";

        // add the index list.
        for (IndexKeyDescriptor ik : indexList) {
            sql += "\nCREATE ";
            if ((ik.getStatus() & 2) > 0) {
                sql += "UNIQUE ";
            }
            if (ik.getIdNumber() == 1) {
                sql += "CLUSTERED ";
            }
            sql += "INDEX " + ik.getName() + "\n";
            sql += "ON " + owner + "." + tableName + " (" + columnList(ik.getColumn()) + ")";

            boolean firstHit = true;
            if ((ik.getStatus() & 64) > 0) {
                sql += " WITH ALLOW_DUP_ROW";
                firstHit = false;
            }
            if ((ik.getStatus() & 1) > 0) {
                sql += ((firstHit) ? " WITH " : ", ") + "IGNORE_DUP_KEY";
                firstHit = false;
            }
            if ((ik.getStatus() & 4) > 0) {
                sql += ((firstHit) ? " WITH " : ", ") + "IGNORE_DUP_ROW";
                firstHit = false;
            }

            sql += "\ngo\n";
        }

        // add permissions.
        sql += getObjectPermissions(connection, owner, tableName);

        // do the binddefault's
        for (String key : ddlMap.keySet()) {
            String value = (String) ddlMap.get(key);
            sql += "sp_bindefault " + value + "', " + key + "'\ngo\n";
        }

        // add rules.
        for (String key : ruleMap.keySet()) {
            String value = (String) ruleMap.get(key);
            sql += "sp_bindrule " + value + "'" + key + "'\ngo";
        }

        // done.
        return sql;
    }

    private String columnList(String[] l) {
        boolean firstTime = true;
        String t = "";
        for (int i = 0; i < l.length && l[i] != null; i++) {
            if (firstTime) {
                firstTime = false;
            } else {
                t += ", ";
            }
            t += l[i];
        }
        return t;
    }

    private String printKeys(DBConnection connection, String owner, String tableName) throws SQLException {
        String sql = "";
        ResultSet result = executeQuery(connection, "select keytype = convert(char(10), v.name), object = object_name(k.id),    related_object = object_name(k.depid),    key1 = col_name(k.id, key1),    key2 = col_name(k.id, key2),    key3 = col_name(k.id, key3),    key4 = col_name(k.id, key4),    key5 = col_name(k.id, key5),    key6 = col_name(k.id, key6),    key7 = col_name(k.id, key7),    key8 = col_name(k.id, key8),    depkey1 = col_name(k.depid, key1),    depkey2 = col_name(k.depid, key2),    depkey3 = col_name(k.depid, key3),    depkey4 = col_name(k.depid, key4),    depkey5 = col_name(k.depid, key5),    depkey6 = col_name(k.depid, key6),    depkey7 = col_name(k.depid, key7),    depkey8 = col_name(k.depid, key8) from dbo.syskeys k, master.dbo.spt_values v, dbo.sysobjects o, dbo.sysusers u where k.type = v.number and v.type = 'K' and k.id = o.id and o.type != 'S' and o.name = ? and u.name = ? and u.uid = o.uid order by v.number, object, related_object", new Object[]{tableName, owner});
        while (result.next()) {
            String keyType = result.getString("keytype");
            String objectName = result.getString("object");
            String relatedObject = result.getString("related_object");
            String[] keys = new String[8];
            String[] dependantKeys = new String[8];
            for (int i = 0; i < 8; i++) {
                keys[i] = result.getString(i + 4);
            }
            for (int i = 0; i < 8; i++) {
                dependantKeys[i] = result.getString(i + 12);
            }

            if (keyType.startsWith("primary")) {
                sql += "sp_primarykey " + objectName + "," + columnList(keys) + "\ngo\n";
            } else if (keyType.startsWith("foreign")) {
                sql += "sp_foreignkey " + objectName + ", " + relatedObject + "," + columnList(dependantKeys) + "\ngo\n";
            } else if (keyType.startsWith("common")) {
                sql += "sp_commonkey " + objectName + ", " + relatedObject + "," + columnList(keys) + "\ngo\n";
            }
        }
        return sql;
    }

    private String getObject(DBConnection connection, String objectTypeName, String objectCode, String owner, String tableName) throws SQLException {
        String sql = "";

        // Get the objects
        ResultSet result = executeQuery(connection, "select distinct o.name as ObjectName, u.name as OwnerName, o.id as ObjectId from  dbo.sysobjects o, dbo.sysusers u,      dbo.sysprocedures p, dbo.sysobjects o2 where o.type = ?  and o.deltrig = o2.id  and o2.name = ?  and u.name = ?  and u.uid = o.uid  and o.id = p.id and p.status & 4096 != 4096", new Object[]{objectCode, tableName, owner});
        List<SimpleObjectDescriptor> objectList = new ArrayList<>();
        while (result.next()) {
            SimpleObjectDescriptor d = new SimpleObjectDescriptor();
            d.setObjectName(result.getString("ObjectName"));
            d.setOwnerName(result.getString("OwnerName"));
            d.setIdNumber(result.getInt("ObjectId"));
            objectList.add(d);
        }

        // Retrieve object code.
        for (SimpleObjectDescriptor d : objectList) {
            result = executeQuery(connection, "select text from dbo.syscomments where id = ?", new Object[]{d.getIdNumber()});
            sql += "\n\n/* " + objectTypeName + " " + d.getObjectName() + ", owner " + d.getOwnerName() + " */\n";
            while (result.next()) {
                sql += result.getString("text");
            }
            sql += "\ngo\n";
        }

        return sql;
    }

    /** Extract the full table definition SQL.  The implementation of this functionality
     * is adapted from the dbschema.pl script included in the standard distribution of
     * the Perl module DBD::Sybase.
     * @param connection
     * @param owner
     * @param tableName The table name
     * @throws SQLException on error reading information from database
     * @return the SQL to fully create the table
     */
    
    @Override
    public String extractFullTableDefinition(DBConnection connection, String owner, String tableName) throws SQLException {
        // Get basic table information.
        int objectId;
        ResultSet result = executeQuery(connection, "select o.name, u.name, o.id as ObjectId, 'N' from dbo.sysobjects o,      dbo.sysusers u where o.type = 'U'   and o.name = ?  and u.uid = o.uid  and u.name = ? order by o.name", new String[]{tableName, owner});
        if (result.next()) {
            objectId = result.getInt("ObjectId");
        } else {
            return "table not found.";
        }

        // dump the table.
        String extractSql = dumpTable(connection, owner, tableName, objectId);

        // key definitions.
        extractSql += printKeys(connection, owner, tableName);

        // triggers
        extractSql += getObject(connection, "Trigger", "TR", owner, tableName);

        // all done.
        return extractSql;
    }

    /** Extract the SQL code for a stored procedure.
     * @param connection
     * @param procName the stored procedure name
     * @param owner the stored procedure owner
     * @throws SQLException on error reading information from the database
     * @return the procedure's SQL code.
     */
    @Override
    public String getStoredProcedureCode(DBConnection connection, String procName, String owner) throws SQLException {
        // get the actual code.
        PreparedStatement stmt = prepareStatement(connection, "select c.text as Text from sysusers    a,     sysobjects  b,     syscomments c where a.name = ?  and b.type = 'P'  and b.name = ?  and a.uid  = b.uid  and b.id   = c.id", new String[]{owner, procName});
        ResultSet codeResult = stmt.executeQuery();
        String code = "";
        while (codeResult.next()) {
            code += codeResult.getString("Text");
        }

        // add the permissions.
        code += "\ngo\n\n" + getObjectPermissions(connection, owner, procName);

        // done.
        return code;
    }

    /** Extract the <CODE>grant</CODE> commands associated with a database object.
     * @param connection
     * @param owner the object owner
     * @param objectName the object name
     * @throws SQLException on error reading information from the database
     * @return the <CODE>grant</CODE> commands in a string
     */
    public String getObjectPermissions(DBConnection connection, String owner, String objectName) throws SQLException {
        String perms = "\n";
        String fullObjectName = owner + "." + objectName;
        Statement stmt = connection.getJDBCConnection().createStatement();
        ResultSet results = stmt.executeQuery("sp_helprotect '" + fullObjectName + "'");
        while (results.next()) {
            String grantor = results.getString(1);
            String grantee = results.getString(2);
            String type = results.getString(3);
            String action = results.getString(4);
            String object = results.getString(5);
            String column = results.getString(6);
            String grantable = results.getString(7);
            perms += type + " " + action + " on " + object + " " + (type.compareToIgnoreCase("revoke") == 0 ? "from" : "to") + " " + grantee + "\n";
        }
        if (perms.length() > 0) {
            perms += "go\n\n";
        }
        return perms;
    }

    private Collection<String> getObjectNames(DBConnection connection, String owner, char objectTypeCode) throws SQLException {
        String sql = "select b.name as ObjectName from sysusers a,     sysobjects b where a.uid = b.uid  and a.name = ?  and b.type = ? order by b.name";
        PreparedStatement s = prepareStatement(connection, sql, new String[]{owner, Character.toString(objectTypeCode)});
        ResultSet r = s.executeQuery();
        return populateSingleValueList(r, "ObjectName");
    }

    /** Get a list of the tables owned by a given user.  This overrides the parent class
     * version to directly retrieve the list from system tables rather than from JDBC
     * meta data.
     * @param connection
     * @param owner the owner of the tables
     * @throws SQLException on error reading information from the database
     * @return the list of table names
     */
    @Override
    public Collection<String> getTableNames(DBConnection connection, String owner) throws SQLException {
        return getObjectNames(connection, owner, 'U');
    }

    /** Get a list of stored procedures owned by a given user.  This version overrides
     * that in the base class to extract the list directly from system tables rather
     * than JDBC metadata, which may not include all the procedures.
     * @param connection
     * @param owner The procedures' owner.
     * @throws SQLException on error reading information from database
     * @return The list of stored procedure names
     */
    @Override
    public Collection<String> getStoredProcedureNames(DBConnection connection, String owner) throws SQLException {
        return getObjectNames(connection, owner, 'P');
    }

    /**
     * get the delimiter for multi-SQL statement queries
     * 
     * @param connection
     * @return delimiter, default is empty string
     */
    @Override
    public String getMultiQueryDelimiter(DBConnection connection) {
        return "\ngo";
    }

    /** A class to store information about a constraint in the database.  This is used
     * as part of the table extraction.
     */
    public static class ConstraintDescriptor {

        /** Holds value of property databaseName. */
        private String databaseName;
        /** Holds value of property constraintName. */
        private String constraintName;
        /** Holds value of property referencedObjectName. */
        private String referencedObjectName;
        /** Holds value of property referencedObjectOwner. */
        private String referencedObjectOwner;
        /** Holds value of property foreignKeyName. */
        private String[] foreignKeyName = new String[16];
        /** Holds value of property referenceKeyName. */
        private String[] referenceKeyName = new String[16];

        /** Getter for property databaseName.
         * @return Value of property databaseName.
         *
         */
        public String getDatabaseName() {
            return this.databaseName;
        }

        /** Setter for property databaseName.
         * @param databaseName New value of property databaseName.
         *
         */
        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        /** Getter for property constraintName.
         * @return Value of property constraintName.
         *
         */
        public String getConstraintName() {
            return this.constraintName;
        }

        /** Setter for property constraintName.
         * @param constraintName New value of property constraintName.
         *
         */
        public void setConstraintName(String constraintName) {
            this.constraintName = constraintName;
        }

        /** Getter for property referencedObjectName.
         * @return Value of property referencedObjectName.
         *
         */
        public String getReferencedObjectName() {
            return this.referencedObjectName;
        }

        /** Setter for property referencedObjectName.
         * @param referencedObjectName New value of property referencedObjectName.
         *
         */
        public void setReferencedObjectName(String referencedObjectName) {
            this.referencedObjectName = referencedObjectName;
        }

        /** Getter for property referencedObjectOwner.
         * @return Value of property referencedObjectOwner.
         *
         */
        public String getReferencedObjectOwner() {
            return this.referencedObjectOwner;
        }

        /** Setter for property referencedObjectOwner.
         * @param referencedObjectOwner New value of property referencedObjectOwner.
         *
         */
        public void setReferencedObjectOwner(String referencedObjectOwner) {
            this.referencedObjectOwner = referencedObjectOwner;
        }

        /** Indexed getter for property foreignKeyName.
         * @param index Index of the property.
         * @return Value of the property at <CODE>index</CODE>.
         *
         */
        public String getForeignKeyName(int index) {
            return this.foreignKeyName[index];
        }

        /** Getter for property foreignKeyName.
         * @return Value of property foreignKeyName.
         *
         */
        public String[] getForeignKeyName() {
            return this.foreignKeyName;
        }

        /** Indexed setter for property foreignKeyName.
         * @param index Index of the property.
         * @param foreignKeyName New value of the property at <CODE>index</CODE>.
         *
         */
        public void setForeignKeyName(int index, String foreignKeyName) {
            this.foreignKeyName[index] = foreignKeyName;
        }

        /** Setter for property foreignKeyName.
         * @param foreignKeyName New value of property foreignKeyName.
         *
         */
        public void setForeignKeyName(String[] foreignKeyName) {
            this.foreignKeyName = foreignKeyName;
        }

        /** Indexed getter for property referenceKeyName.
         * @param index Index of the property.
         * @return Value of the property at <CODE>index</CODE>.
         *
         */
        public String getReferenceKeyName(int index) {
            return this.referenceKeyName[index];
        }

        /** Getter for property referenceKeyName.
         * @return Value of property referenceKeyName.
         *
         */
        public String[] getReferenceKeyName() {
            return this.referenceKeyName;
        }

        /** Indexed setter for property referenceKeyName.
         * @param index Index of the property.
         * @param referenceKeyName New value of the property at <CODE>index</CODE>.
         *
         */
        public void setReferenceKeyName(int index, String referenceKeyName) {
            this.referenceKeyName[index] = referenceKeyName;
        }

        /** Setter for property referenceKeyName.
         * @param referenceKeyName New value of property referenceKeyName.
         *
         */
        public void setReferenceKeyName(String[] referenceKeyName) {
            this.referenceKeyName = referenceKeyName;
        }
    }

    /** A class to store information about a table column.  This is used in the table
     * extraction code.
     */
    public static class ColumnDescriptor {

        /** Holds value of property columnName. */
        private String columnName;
        /** Holds value of property columnType. */
        private String columnType;
        /** Holds value of property length. */
        private int length;
        /** Holds value of property precision. */
        private int precision;
        /** Holds value of property scale. */
        private int scale;
        /** Holds value of property allowNull. */
        private boolean allowNull;
        /** Holds value of property defaultName. */
        private String defaultName;
        /** Holds value of property ruleName. */
        private String ruleName;
        /** Holds value of property identity. */
        private boolean identity;
        /** Holds value of property defaultId. */
        private int defaultId;
        /** Holds value of property ruleId. */
        private int ruleId;
        /** Holds value of property defaultDDL. */
        private int defaultDDL;
        /** Holds value of property ruleDDL. */
        private int ruleDDL;

        /** Getter for property columnName.
         * @return Value of property columnName.
         *
         */
        public String getColumnName() {
            return this.columnName;
        }

        /** Setter for property columnName.
         * @param columnName New value of property columnName.
         *
         */
        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        /** Getter for property columnType.
         * @return Value of property columnType.
         *
         */
        public String getColumnType() {
            return this.columnType;
        }

        /** Setter for property columnType.
         * @param columnType New value of property columnType.
         *
         */
        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        /** Getter for property length.
         * @return Value of property length.
         *
         */
        public int getLength() {
            return this.length;
        }

        /** Setter for property length.
         * @param length New value of property length.
         *
         */
        public void setLength(int length) {
            this.length = length;
        }

        /** Getter for property precision.
         * @return Value of property precision.
         *
         */
        public int getPrecision() {
            return this.precision;
        }

        /** Setter for property precision.
         * @param precision New value of property precision.
         *
         */
        public void setPrecision(int precision) {
            this.precision = precision;
        }

        /** Getter for property scale.
         * @return Value of property scale.
         *
         */
        public int getScale() {
            return this.scale;
        }

        /** Setter for property scale.
         * @param scale New value of property scale.
         *
         */
        public void setScale(int scale) {
            this.scale = scale;
        }

        /** Getter for property allowNull.
         * @return Value of property allowNull.
         *
         */
        public boolean isAllowNull() {
            return this.allowNull;
        }

        /** Setter for property allowNull.
         * @param allowNull New value of property allowNull.
         *
         */
        public void setAllowNull(boolean allowNull) {
            this.allowNull = allowNull;
        }

        /** Getter for property defaultName.
         * @return Value of property defaultName.
         *
         */
        public String getDefaultName() {
            return this.defaultName;
        }

        /** Setter for property defaultName.
         * @param defaultName New value of property defaultName.
         *
         */
        public void setDefaultName(String defaultName) {
            this.defaultName = defaultName;
        }

        /** Getter for property ruleName.
         * @return Value of property ruleName.
         *
         */
        public String getRuleName() {
            return this.ruleName;
        }

        /** Setter for property ruleName.
         * @param ruleName New value of property ruleName.
         *
         */
        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        /** Getter for property identity.
         * @return Value of property identity.
         *
         */
        public boolean isIdentity() {
            return this.identity;
        }

        /** Setter for property identity.
         * @param identity New value of property identity.
         *
         */
        public void setIdentity(boolean identity) {
            this.identity = identity;
        }

        /** Getter for property defaultId.
         * @return Value of property defaultId.
         *
         */
        public int getDefaultId() {
            return this.defaultId;
        }

        /** Setter for property defaultId.
         * @param defaultId New value of property defaultId.
         *
         */
        public void setDefaultId(int defaultId) {
            this.defaultId = defaultId;
        }

        /** Getter for property ruleId.
         * @return Value of property ruleId.
         *
         */
        public int getRuleId() {
            return this.ruleId;
        }

        /** Setter for property ruleId.
         * @param ruleId New value of property ruleId.
         *
         */
        public void setRuleId(int ruleId) {
            this.ruleId = ruleId;
        }

        /** Getter for property defaultDDL.
         * @return Value of property defaultDDL.
         *
         */
        public int getDefaultDDL() {
            return this.defaultDDL;
        }

        /** Setter for property defaultDDL.
         * @param defaultDDL New value of property defaultDDL.
         *
         */
        public void setDefaultDDL(int defaultDDL) {
            this.defaultDDL = defaultDDL;
        }

        /** Getter for property ruleDDL.
         * @return Value of property ruleDDL.
         *
         */
        public int getRuleDDL() {
            return this.ruleDDL;
        }

        /** Setter for property ruleDDL.
         * @param ruleDDL New value of property ruleDDL.
         *
         */
        public void setRuleDDL(int ruleDDL) {
            this.ruleDDL = ruleDDL;
        }
    }

    /** A class to store information about indexes and keys in a table.  This is used in the table
     * extraction code.
     */
    public static class IndexKeyDescriptor {

        /** Holds value of property name. */
        private String name;
        /** Holds value of property idNumber. */
        private int idNumber;
        /** Holds value of property status. */
        private int status;
        /** Holds value of property status2. */
        private int status2;
        /** Holds value of property column. */
        private String[] column = new String[16];

        /** Getter for property name.
         * @return Value of property name.
         *
         */
        public String getName() {
            return this.name;
        }

        /** Setter for property name.
         * @param name New value of property name.
         *
         */
        public void setName(String name) {
            this.name = name;
        }

        /** Getter for property idNumber.
         * @return Value of property idNumber.
         *
         */
        public int getIdNumber() {
            return this.idNumber;
        }

        /** Setter for property idNumber.
         * @param idNumber New value of property idNumber.
         *
         */
        public void setIdNumber(int idNumber) {
            this.idNumber = idNumber;
        }

        /** Getter for property status.
         * @return Value of property status.
         *
         */
        public int getStatus() {
            return this.status;
        }

        /** Setter for property status.
         * @param status New value of property status.
         *
         */
        public void setStatus(int status) {
            this.status = status;
        }

        /** Getter for property status2.
         * @return Value of property status2.
         *
         */
        public int getStatus2() {
            return this.status2;
        }

        /** Setter for property status2.
         * @param status2 New value of property status2.
         *
         */
        public void setStatus2(int status2) {
            this.status2 = status2;
        }

        /** Indexed getter for property column.
         * @param index Index of the property.
         * @return Value of the property at <CODE>index</CODE>.
         *
         */
        public String getColumn(int index) {
            return this.column[index];
        }

        /** Getter for property column.
         * @return Value of property column.
         *
         */
        public String[] getColumn() {
            return this.column;
        }

        /** Indexed setter for property column.
         * @param index Index of the property.
         * @param column New value of the property at <CODE>index</CODE>.
         *
         */
        public void setColumn(int index, String column) {
            this.column[index] = column;
        }

        /** Setter for property column.
         * @param column New value of property column.
         *
         */
        public void setColumn(String[] column) {
            this.column = column;
        }
    }

    /** A class to store basic information about a database object.  This is used in the table
     * extraction code.
     */
    public static class SimpleObjectDescriptor {

        /** Holds value of property objectName. */
        private String objectName;
        /** Holds value of property ownerName. */
        private String ownerName;
        /** Holds value of property idNumber. */
        private int idNumber;

        /** Getter for property objectName.
         * @return Value of property objectName.
         *
         */
        public String getObjectName() {
            return this.objectName;
        }

        /** Setter for property objectName.
         * @param objectName New value of property objectName.
         *
         */
        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        /** Getter for property ownerName.
         * @return Value of property ownerName.
         *
         */
        public String getOwnerName() {
            return this.ownerName;
        }

        /** Setter for property ownerName.
         * @param ownerName New value of property ownerName.
         *
         */
        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        /** Getter for property idNumber.
         * @return Value of property idNumber.
         *
         */
        public int getIdNumber() {
            return this.idNumber;
        }

        /** Setter for property idNumber.
         * @param idNumber New value of property idNumber.
         *
         */
        public void setIdNumber(int idNumber) {
            this.idNumber = idNumber;
        }
    }
}
