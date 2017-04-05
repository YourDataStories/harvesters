/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.nsrfapi.service.impl.rdf;

import virtuoso.jena.driver.VirtGraph;

/**
 *
 * @author G. Vafeiadis
 */
public class Configuration {
    
    /**
     *
     * Connection String of Virtuoso
     *
     */

    public static final String username;
    public static final String password;
    public static final String connectionString;

    static {
        username = "****";
        password = "*************";
        connectionString = "jdbc:virtuoso://********/:1111/autoReconnect=true/charset=UTF-8/log_enable=2";
    }

}
