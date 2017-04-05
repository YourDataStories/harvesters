package org.linkedeconomy.nsrfapi.service.impl.rdf;

/**
 *
 * @author G. Vafeiadis
 */
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.InfModel;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.linkedeconomy.nsrfapi.ontology.OntologySpecification;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.icu.text.DecimalFormat;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import org.linkedeconomy.nsrfapi.commons.CommonVariables;
import org.linkedeconomy.nsrfapi.dto.SubprojectsProjects;
import org.linkedeconomy.nsrfapi.service.SubProjectsService;

public class SubProjectsImpl {
    
    /**
     *
     * Implementation Of Subproject Service layer 
     * and transformation of Database data to RDF
     *
     * @throws java.text.ParseException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.FileNotFoundException
     */

    public static void exportRfd() throws ParseException, UnsupportedEncodingException, FileNotFoundException, IOException {

        //services for each table
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        SubProjectsService sub = (SubProjectsService) ctx.getBean("subProjectsServiceImpl");

        List<SubprojectsProjects> subProject = sub.getInfoSubproject();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("dcterms", OntologySpecification.dctermsPrefix);

        //number format
        DecimalFormat df = new DecimalFormat("0.00");
        
        for (SubprojectsProjects subProject1 : subProject) {

            Resource instanceCurrency = infModel.createResource("http://linkedeconomy.org/resource/Currency/EUR");
            Resource instanceUps = infModel.createResource(OntologySpecification.instancePrefix
                    + "UnitPriceSpecification/" + subProject1.getOps() + "/" + subProject1.getSubprojectId());
            Resource instanceBudget = infModel.createResource(OntologySpecification.instancePrefix + "BudgetItem/" + subProject1.getOps() + "/" + subProject1.getSubprojectId());
            Resource instanceSubProject = infModel.createResource(OntologySpecification.instancePrefix + "Contract/" + subProject1.getOps() + "/" + subProject1.getSubprojectId());
            Resource instanceProject = infModel.createResource(OntologySpecification.instancePrefix
                    + "PublicWork/" + subProject1.getOps());

            infModel.add(instanceUps, RDF.type, OntologySpecification.priceSpecificationResource);
            infModel.add(instanceBudget, RDF.type, OntologySpecification.budgetResource);
            infModel.add(instanceSubProject, RDF.type, OntologySpecification.contractResource);
            instanceProject.addProperty(OntologySpecification.hasRelatedContract, instanceSubProject);
            instanceSubProject.addProperty(OntologySpecification.price, instanceUps);
            instanceUps.addProperty(OntologySpecification.hasCurrencyValue, df.format(subProject1.getBudget()), XSDDatatype.XSDfloat);
            instanceUps.addProperty(OntologySpecification.valueAddedTaxIncluded, "true", XSDDatatype.XSDboolean);
            instanceUps.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
            if (subProject1.getStart() != null) {
                instanceSubProject.addProperty(OntologySpecification.pcStartDate, subProject1.getStart().replace("/", "-"), XSDDatatype.XSDdate);
            }
            if (subProject1.getFinish() != null) {
                instanceSubProject.addProperty(OntologySpecification.actualEndDate, subProject1.getFinish().replace("/", "-"), XSDDatatype.XSDdate);
            }
            instanceSubProject.addProperty(OntologySpecification.title, String.valueOf(subProject1.getTitle()), "el");
        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    CommonVariables.serverPath + "subProjectEspa_"+ CommonVariables.currentDate + ".rdf");
            model.write(fout);
            fout.close();
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }
}
