/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.objects;

import com.blazartech.products.blazarsql.components.dataobjects.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * a specialized Sybase object access implementation, specific to the JConnect (jconn3)
 * Sybase driver.  It appears to have a bug in the com.sybase.jdbc3.jdbc.SybDatabaseMetaData.getSchemas
 * method as it throws an exception every time.  That call is made in the base class
 * version of @see getUserList.  So this class will override that and get the user
 * list directly from the Sybase tables rather than via the driver.
 * 
 * @author aar1069
 * @version $Id: JConnectSybaseObjectDataAccessImpl.java 33 2015-04-23 19:55:40Z aar1069 $
 */

/* $Log$
 *******************************************************************************/
@Component("sybaseObjectDataAccess")
public class JConnectSybaseObjectDataAccessImpl extends SybaseObjectDataAccessImpl {

    @Override
    public Collection<String> getUserList(DBConnection connection) throws SQLException {
        String query = "select distinct a.name from sysusers a,      sysobjects b where a.uid = b.uid order by a.name";
        ResultSet rs = executeQuery(connection, query);

        List<String> userList = new ArrayList<>();
        while (rs.next()) {
            userList.add(rs.getString("name"));
        }

        return userList;
    }
}
