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
import com.hp.hpl.jena.vocabulary.RDFS;
import com.ibm.icu.text.DecimalFormat;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.linkedeconomy.espa.jpa.Review;
import org.linkedeconomy.espa.service.ReviewService;

public class ReviewEspaImpl {

    public static void review() throws ParseException {

        //services for each table
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        ReviewService rev = (ReviewService) ctx.getBean("reviewServiceImpl");

        List<Review> review = rev.getReview();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("dcterms", OntologySpecification.dctermsPrefix);

        //number format
        DecimalFormat df = new DecimalFormat("0.00");

        for (Review review1 : review) {

            Resource instanceCurrency = infModel.createResource("http://linkedeconomy.org/resource/Currency/EUR");
            Resource instanceCountry = infModel.createResource("http://linkedeconomy.org/resource/Country/GR");
            Resource instanceBudgetUps = infModel.createResource(OntologySpecification.instancePrefix
                    + "UnitPriceSpecification/BudgetItem/" + review1.getOps());
            Resource instanceSpendingUps = infModel.createResource(OntologySpecification.instancePrefix
                    + "UnitPriceSpecification/SpendingItem/" + review1.getOps());
            Resource instanceBudget = infModel.createResource(OntologySpecification.instancePrefix + "BudgetItem/" + review1.getOps());
            Resource instanceSpending = infModel.createResource(OntologySpecification.instancePrefix + "SpendingItem/" + review1.getOps());
            Resource instanceSpendingExpLine = infModel.createResource(OntologySpecification.instancePrefix
                    + "ExpenditureLine/SpendingItem/" + review1.getOps());
            Resource instanceProject = infModel.createResource(OntologySpecification.instancePrefix + "Subsidy/" + review1.getOps());
            DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dfDate2 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            java.util.Date stDateStarts;
            java.util.Date stDateEnds;
            java.util.Date stDateIssued;
            stDateStarts = dfDate.parse(review1.getStartDate());
            stDateEnds = dfDate.parse(review1.getFinishDate());
            stDateIssued = dfDate2.parse(review1.getDate().toString());
            String startDate = df2.format(stDateStarts);
            String endDate = df2.format(stDateEnds);
            String issuedDate = df2.format(stDateIssued);

            infModel.add(instanceBudgetUps, RDF.type, OntologySpecification.priceSpecificationResource);
            infModel.add(instanceSpendingUps, RDF.type, OntologySpecification.priceSpecificationResource);
            infModel.add(instanceBudget, RDF.type, OntologySpecification.budgetResource);
            infModel.add(instanceSpending, RDF.type, OntologySpecification.spendingResource);
            infModel.add(instanceSpendingExpLine, RDF.type, OntologySpecification.expLineResource);
            infModel.add(instanceProject, RDF.type, OntologySpecification.subsidyResource);
            instanceProject.addProperty(OntologySpecification.hasRelatedBudgetItem, instanceBudget);
            model.add(OntologySpecification.subsidyResource, RDFS.subClassOf, OntologySpecification.projectResource);
            model.add(OntologySpecification.budgetResource, RDFS.subClassOf, OntologySpecification.financialResource);
            model.add(OntologySpecification.spendingResource, RDFS.subClassOf, OntologySpecification.financialResource);
            instanceProject.addProperty(OntologySpecification.hasRelatedSpendingItem, instanceSpending);
            instanceProject.addProperty(OntologySpecification.countryIsoCode, instanceCountry);
//            instanceBudget.addProperty(OntologySpecification.hasExpenditureLine, instanceBudgetExpLine);
            instanceSpending.addProperty(OntologySpecification.hasExpenditureLine, instanceSpendingExpLine);
            instanceBudget.addProperty(OntologySpecification.price, instanceBudgetUps);
            instanceSpendingExpLine.addProperty(OntologySpecification.amount, instanceSpendingUps);
            instanceSpendingUps.addProperty(OntologySpecification.hasCurrencyValue, df.format(review1.getSpending()), XSDDatatype.XSDfloat);
            instanceSpendingUps.addProperty(OntologySpecification.valueAddedTaxIncluded, "true", XSDDatatype.XSDboolean);
            instanceSpendingUps.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
            instanceBudgetUps.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
            instanceBudgetUps.addProperty(OntologySpecification.hasCurrencyValue, df.format(review1.getBudget()), XSDDatatype.XSDfloat);
            instanceBudgetUps.addProperty(OntologySpecification.valueAddedTaxIncluded, "true", XSDDatatype.XSDboolean);
            instanceProject.addProperty(OntologySpecification.desc, String.valueOf(review1.getDescription()), "el");
            instanceProject.addProperty(OntologySpecification.title, String.valueOf(review1.getTitle()), "el");
            instanceProject.addProperty(OntologySpecification.projectId, String.valueOf(review1.getOps()), XSDDatatype.XSDstring);
            instanceProject.addProperty(OntologySpecification.issued, issuedDate, XSDDatatype.XSDdateTime);
            instanceProject.addProperty(OntologySpecification.completion, String.valueOf(review1.getCompletion()), XSDDatatype.XSDfloat);
            instanceProject.addProperty(OntologySpecification.countOfRelatedProjects, String.valueOf(review1.getSubProjects()), XSDDatatype.XSDstring);
            instanceProject.addProperty(OntologySpecification.startDate, startDate, XSDDatatype.XSDdateTime);
            instanceProject.addProperty(OntologySpecification.endDate, endDate, XSDDatatype.XSDdateTime);
        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    "/Users/giovaf/Documents/yds_pilot1/espa_tests/22-02-2016_ouput/reviewEspa.rdf");
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }
}
