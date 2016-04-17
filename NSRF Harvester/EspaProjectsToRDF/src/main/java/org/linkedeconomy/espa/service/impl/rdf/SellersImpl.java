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
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.InfModel;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.linkedeconomy.espa.service.SellersService;

import org.linkedeconomy.espa.ontology.OntologySpecification;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.UUID;
import org.linkedeconomy.espa.jpa.Sellers;

public class SellersImpl {

    public static void espaSellers() {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        SellersService sellers = (SellersService) ctx.getBean("sellersServiceImpl");

        List<Sellers> seller = sellers.getSellers();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("vcard", OntologySpecification.vcardPrefix);

        for (Sellers seller1 : seller) {
            Resource instanceSeller = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + seller1.getId());
            infModel.add(instanceSeller, RDF.type, OntologySpecification.organizationResource);
            instanceSeller.addProperty(OntologySpecification.name, seller1.getEponimia(), XSDDatatype.XSDstring);
        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    "/Users/giovaf/Documents/yds_pilot1/espa_tests/22-02-2016_ouput/sellersEspa.rdf");
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }

}
