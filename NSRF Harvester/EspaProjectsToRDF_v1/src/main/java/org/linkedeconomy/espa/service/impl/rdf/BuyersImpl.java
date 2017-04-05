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

import org.linkedeconomy.espa.ontology.OntologySpecification;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.linkedeconomy.espa.dto.Buyer;
import org.linkedeconomy.espa.jpa.Buyers;
import org.linkedeconomy.espa.service.BuyersService;

public class BuyersImpl {

    UUID uuidAddress = UUID.randomUUID();
    String randomUUIDAddress = uuidAddress.toString();
    UUID uuidBuyer = UUID.randomUUID();
    String randomUUIDBuyer = uuidBuyer.toString();

    public static void espaBuyers() {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        BuyersService buyers = (BuyersService) ctx.getBean("buyersServiceImpl");

        List<Buyers> buyer = buyers.getBuyers();
        List<Buyer> projectBuyer = buyers.getProjectBuyers();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("vcard", OntologySpecification.vcardPrefix);

        for (Buyer projectBuyer1 : projectBuyer) {

            Resource instanceBuyer = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + projectBuyer1.getBuyerId());
            Resource instanceProject = infModel.createResource(OntologySpecification.instancePrefix + "Subsidy/" + projectBuyer1.getOps());


            infModel.add(instanceBuyer, RDF.type, OntologySpecification.organizationResource);
            infModel.add(instanceProject, RDF.type, OntologySpecification.subsidyResource);

            instanceProject.addProperty(OntologySpecification.beneficiary, instanceBuyer);
            instanceBuyer.addProperty(OntologySpecification.name, String.valueOf(projectBuyer1.getEponimia()), XSDDatatype.XSDstring);

        }


        try {
            FileOutputStream fout = new FileOutputStream(
                    "/Users/giovaf/Documents/yds_pilot1/espa_tests/22-02-2016_ouput/buyersEspa.rdf");
            // /home/svaf/buyersEspa.rdf
            // /Users/giovaf/Documents/yds_pilot1/espa_tests/06-01-2016_output/buyersEspa.rdf
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }

}
