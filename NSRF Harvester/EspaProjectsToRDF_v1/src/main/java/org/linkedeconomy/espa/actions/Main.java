/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.actions;

/**
 *
 * @author G. Vafeiadis
 */
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import org.linkedeconomy.espa.service.impl.rdf.BuyersImpl;
import org.linkedeconomy.espa.service.impl.rdf.InsertionsToVirtuoso;
import org.linkedeconomy.espa.service.impl.rdf.ReviewEspaImpl;
import org.linkedeconomy.espa.service.impl.rdf.SellersImpl;
import org.linkedeconomy.espa.service.impl.rdf.SubProjectsImpl;
import org.linkedeconomy.espa.service.impl.rdf.SubprojectsSellersImpl;

public class Main {

    public static void main(String[] args) throws ParseException, FileNotFoundException, UnsupportedEncodingException {

        ReviewEspaImpl.review();
        SellersImpl.espaSellers();
        BuyersImpl.espaBuyers();
        SubProjectsImpl.espaSubprojects();
        SubprojectsSellersImpl.subprojectsSellers();
        InsertionsToVirtuoso.insertSectors();
    }
}
