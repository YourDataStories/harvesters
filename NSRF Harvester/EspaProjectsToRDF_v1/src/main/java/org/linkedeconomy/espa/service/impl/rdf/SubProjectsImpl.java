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
import com.ibm.icu.text.DecimalFormat;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import org.linkedeconomy.espa.jpa.SubProjects;
import org.linkedeconomy.espa.service.SubProjectsService;

public class SubProjectsImpl {

    public static void espaSubprojects() throws ParseException {

        //services for each table
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        SubProjectsService sub = (SubProjectsService) ctx.getBean("subProjectsServiceImpl");

        List<SubProjects> subProject = sub.getSubProjects();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("dcterms", OntologySpecification.dctermsPrefix);

        //number format
        DecimalFormat df = new DecimalFormat("0.00");

        for (SubProjects subProject1 : subProject) {

            Resource instanceCurrency = infModel.createResource("http://linkedeconomy.org/resource/Currency/EUR");
            Resource instanceBudgetUps = infModel.createResource(OntologySpecification.instancePrefix
                    + "UnitPriceSpecification/BudgetItem/" + subProject1.getOps() + "/" + subProject1.getId());
            Resource instanceBudget = infModel.createResource(OntologySpecification.instancePrefix + "BudgetItem/" + subProject1.getOps() + "/" + subProject1.getId());
            Resource instanceSubProject = infModel.createResource(OntologySpecification.instancePrefix + "Subproject/" + subProject1.getOps() + "/" + subProject1.getId());
            Resource instanceProject = infModel.createResource(OntologySpecification.instancePrefix
                    + "Subsidy/" + subProject1.getOps());
            DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            java.util.Date stDateStarts;
            java.util.Date stDateEnds;
            stDateStarts = dfDate.parse(subProject1.getStart());
            stDateEnds = dfDate.parse(subProject1.getFinish());
            String startDate = df2.format(stDateStarts);
            String endDate = df2.format(stDateEnds);
            infModel.add(instanceProject, RDF.type, OntologySpecification.projectResource);
            infModel.add(instanceBudgetUps, RDF.type, OntologySpecification.priceSpecificationResource);
            infModel.add(instanceBudget, RDF.type, OntologySpecification.budgetResource);
            infModel.add(instanceSubProject, RDF.type, OntologySpecification.subProjectResource);
            instanceProject.addProperty(OntologySpecification.hasRelatedProject, instanceSubProject);
            instanceSubProject.addProperty(OntologySpecification.hasRelatedBudgetItem, instanceBudget);
            instanceBudget.addProperty(OntologySpecification.price, instanceBudgetUps);
            instanceBudgetUps.addProperty(OntologySpecification.hasCurrencyValue, df.format(subProject1.getBudget()), XSDDatatype.XSDfloat);
            instanceBudgetUps.addProperty(OntologySpecification.valueAddedTaxIncluded, "true", XSDDatatype.XSDboolean);
            instanceBudgetUps.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
            instanceSubProject.addProperty(OntologySpecification.startDate, startDate, XSDDatatype.XSDdateTime);
            instanceSubProject.addProperty(OntologySpecification.endDate, endDate, XSDDatatype.XSDdateTime);
            instanceSubProject.addProperty(OntologySpecification.title, String.valueOf(subProject1.getTitle()), "el");
        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    "/Users/giovaf/Documents/yds_pilot1/espa_tests/22-02-2016_ouput/subProjectEspa.rdf");
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }
}
