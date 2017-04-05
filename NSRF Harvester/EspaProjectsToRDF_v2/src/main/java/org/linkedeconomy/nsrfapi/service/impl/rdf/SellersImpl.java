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

import org.linkedeconomy.nsrfapi.service.SellersService;

import org.linkedeconomy.nsrfapi.ontology.OntologySpecification;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.linkedeconomy.nsrfapi.commons.CommonVariables;
import org.linkedeconomy.nsrfapi.dto.ProjectSellers;
import org.linkedeconomy.nsrfapi.jpa.Sellers;

public class SellersImpl {

    /**
     *
     * Implementation Of Sellers Service layer and transformation of Database
     * data to RDF
     *
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.FileNotFoundException
     */
    public static void exportRfd() throws UnsupportedEncodingException, FileNotFoundException, IOException {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        SellersService sellers = (SellersService) ctx.getBean("sellersServiceImpl");

        List<ProjectSellers> projectSeller = sellers.getProjectSellers();
        List<Sellers> seller = sellers.getSellers();

        @SuppressWarnings("UnusedAssignment")
        Resource instanceSeller = null;
        @SuppressWarnings("UnusedAssignment")
        Resource instanceAddress = null;

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("vcard", OntologySpecification.vcardPrefix);

        for (Sellers seller2 : seller) {

            if (seller2.getVatId() != null) {
                instanceSeller = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + seller2.getVatId());
                instanceSeller.addProperty(OntologySpecification.vatID, seller2.getVatId(), XSDDatatype.XSDstring);
                if (seller2.getAddress() != null) {
                    instanceAddress = infModel.createResource(OntologySpecification.instancePrefix + "Address/" + seller2.getVatId());
                    instanceAddress.addProperty(OntologySpecification.street, seller2.getAddress().trim(), XSDDatatype.XSDstring);
                    instanceSeller.addProperty(OntologySpecification.hasAddress, instanceAddress);
                    infModel.add(instanceAddress, RDF.type, OntologySpecification.addressResource);
                }

            } else {
                instanceSeller = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + seller2.getId() + "-S");
                if (seller2.getAddress() != null) {
                    instanceAddress = infModel.createResource(OntologySpecification.instancePrefix + "Address/" + seller2.getId() + "-S");
                    instanceAddress.addProperty(OntologySpecification.street, seller2.getAddress().trim(), XSDDatatype.XSDstring);
                    instanceSeller.addProperty(OntologySpecification.hasAddress, instanceAddress);
                    infModel.add(instanceAddress, RDF.type, OntologySpecification.addressResource);
                }
            }
            Resource instanceCountry = infModel.createResource("http://linkedeconomy.org/resource/Country/GR");
            infModel.add(instanceSeller, RDF.type, OntologySpecification.organizationResource);
            infModel.add(instanceSeller, RDF.type, OntologySpecification.organizationOrgResource);
            infModel.add(instanceSeller, RDF.type, OntologySpecification.businessResource);
            infModel.add(instanceSeller, RDF.type, OntologySpecification.organizationRovResource);
            if (seller2.getName() != null) {
                if (seller2.getName().isEmpty()) {
                    instanceSeller.addProperty(OntologySpecification.foafName, "no seller specified", "el");
                } else {
                    instanceSeller.addProperty(OntologySpecification.foafName, seller2.getName().trim(), "el");
                    instanceSeller.addProperty(OntologySpecification.countryIsoCode, instanceCountry);
                }
            }
        }

        for (ProjectSellers seller1 : projectSeller) {

            if (seller1.getVatId() != null) {
                System.out.println(seller1.getVatId());
                instanceSeller = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + seller1.getVatId());
                Resource instanceSubproject = infModel.createResource(OntologySpecification.instancePrefix + "Contract/" + seller1.getCode() + "/" + seller1.getSubprojectId());
                instanceSubproject.addProperty(OntologySpecification.seller, instanceSeller);
            } else {
                instanceSeller = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + seller1.getSellerId() + "-S");
                Resource instanceSubproject = infModel.createResource(OntologySpecification.instancePrefix + "Contract/" + seller1.getCode() + "/" + seller1.getSubprojectId());
                instanceSubproject.addProperty(OntologySpecification.seller, instanceSeller);
            }

        }
        try {
            FileOutputStream fout = new FileOutputStream(
                    CommonVariables.serverPath + "sellersEspa_" + CommonVariables.currentDate + ".rdf");
            model.write(fout);
            fout.close();
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }
}
