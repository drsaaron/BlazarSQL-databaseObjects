/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.products.blazarsql.components.dataobjects.impl.connection;

import java.util.Properties;

/**
 *
 * @author AAR1069
 */
abstract class BaseConnectionURLBuilder {

    String appendConfigurationProperties(String baseURL, Properties props) {
        if (props != null) {
            boolean first = true;
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                if (first) {
                    baseURL += "?";
                } else {
                    baseURL += "&";
                }
                baseURL += key + "=" + value;
                first = false;
            }
        }
        
        return baseURL;
    }
}
