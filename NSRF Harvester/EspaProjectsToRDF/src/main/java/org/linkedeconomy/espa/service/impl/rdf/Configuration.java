/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.service.impl.rdf;

/**
 *
 * @author G. Vafeiadis
 */
public class Configuration {

    public static final String username;
    public static final String password;
    public static final String connectionString;
    public static final String graphHarvester;

    static {
        username = "****";
        password = "*********";
        connectionString = "jdbc:virtuoso://*********/:1111/autoReconnect=true/charset=UTF-8/log_enable=2";
        graphHarvester = "http://yourdatastories.eu/NSRF/Diavgeia";
}
