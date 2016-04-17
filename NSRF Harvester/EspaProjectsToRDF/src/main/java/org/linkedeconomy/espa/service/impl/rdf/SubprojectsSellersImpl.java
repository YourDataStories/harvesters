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
import org.linkedeconomy.espa.jpa.SubProjectSellers;
import org.linkedeconomy.espa.service.SubProjectSellersService;

public class SubprojectsSellersImpl {

    public static void subprojectsSellers() {

        //services for each table
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        SubProjectSellersService sub = (SubProjectSellersService) ctx.getBean("subProjectSellersServiceImpl");

        List<SubProjectSellers> subProjectSeller = sub.getSubProjectSellers();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);


        for (SubProjectSellers subProjectSeller1 : subProjectSeller) {
            Resource instanceSeller = infModel.createResource(OntologySpecification.instancePrefix
                    + "Organization/" + subProjectSeller1.getSellerId());
            Resource instanceSubProject = infModel.createResource(OntologySpecification.instancePrefix
                    + "Subproject/" + subProjectSeller1.getOps() + "/" + subProjectSeller1.getSubProjectId());
            infModel.add(instanceSeller, RDF.type, OntologySpecification.organizationResource);
            infModel.add(instanceSeller, RDF.type, OntologySpecification.businessResource);
            infModel.add(instanceSubProject, RDF.type, OntologySpecification.subProjectResource);
            instanceSubProject.addProperty(OntologySpecification.seller, instanceSeller);
        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    "/Users/giovaf/Documents/yds_pilot1/espa_tests/22-02-2016_ouput/subProjectSellersEspa.rdf");
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }
}
