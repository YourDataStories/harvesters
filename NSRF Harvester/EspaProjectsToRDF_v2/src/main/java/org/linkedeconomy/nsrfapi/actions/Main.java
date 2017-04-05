package org.linkedeconomy.nsrfapi.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import org.linkedeconomy.nsrfapi.service.impl.rdf.BeneficiariesImpl;
import org.linkedeconomy.nsrfapi.service.impl.rdf.OverviewAndDetailsImpl;
import org.linkedeconomy.nsrfapi.service.impl.rdf.SellersImpl;
import org.linkedeconomy.nsrfapi.service.impl.rdf.SubProjectsImpl;

/**
 *
 * @author G. Vafeiadis
 */
public class Main {

    /**
     *
     * The Main method to initiate the process of RDF
     *
     * @param args
     * @throws java.text.ParseException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws ParseException, UnsupportedEncodingException, FileNotFoundException, IOException, ClassNotFoundException, SQLException {

        SubProjectsImpl.exportRfd();
        System.out.println("Subprojects have finished!!!");
        OverviewAndDetailsImpl.exportRdf();
        System.out.println("Public Works have finished!!!");
        BeneficiariesImpl.exportRfd();
        System.out.println("Beneficiaries have finished!!!");
        SellersImpl.exportRfd();
        System.out.println("Sellers have finished!!!");
    }
}
