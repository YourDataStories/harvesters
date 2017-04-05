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
import java.io.FileOutputStream;
import java.io.IOException;
import org.linkedeconomy.nsrfapi.commons.CommonVariables;
import org.linkedeconomy.nsrfapi.dto.ProjectBeneficiaries;
import org.linkedeconomy.nsrfapi.jpa.Beneficiaries;
import org.linkedeconomy.nsrfapi.service.BeneficiariesService;

public class BeneficiariesImpl {

    /**
     *
     * Implementation Of Beneficiaries Service layer and transformation of
     * Database data to RDF
     *
     */
    public static void exportRfd() {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        BeneficiariesService beneficiaries = (BeneficiariesService) ctx.getBean("beneficiariesServiceImpl");

        List<Beneficiaries> buyers = beneficiaries.getBeneficiaries();
//        List<ProjectBeneficiaries> beneficiary = beneficiaries.getProjectBeneficiaries();
        List<ProjectBeneficiaries> projectBuyers = beneficiaries.getProjectBuyers();
        List<ProjectBeneficiaries> subProjectBuyers = beneficiaries.getSubProjectBuyers();

        @SuppressWarnings("UnusedAssignment")
        Resource instanceBeneficiary = null;
        @SuppressWarnings("UnusedAssignment")
        Resource instanceAddress = null;

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("vcard", OntologySpecification.vcardPrefix);

        for (Beneficiaries buyer : buyers) {

            System.out.println(buyer.getId());

            if (buyer.getVatId() != null) {
                instanceBeneficiary = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + buyer.getVatId());
                if (buyer.getAddress() != null) {
                    instanceAddress = infModel.createResource(OntologySpecification.instancePrefix + "Address/" + buyer.getVatId());
                    instanceBeneficiary.addProperty(OntologySpecification.hasAddress, instanceAddress);
                    infModel.add(instanceAddress, RDF.type, OntologySpecification.addressResource);
                    instanceAddress.addProperty(OntologySpecification.street, buyer.getAddress().trim(), XSDDatatype.XSDstring);
                }
                instanceBeneficiary.addProperty(OntologySpecification.vatID, buyer.getVatId(), XSDDatatype.XSDstring);
            } else {
                instanceBeneficiary = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + buyer.getId() + "-B");
                if (buyer.getAddress() != null) {
                    instanceAddress = infModel.createResource(OntologySpecification.instancePrefix + "Address/" + buyer.getId() + "-B");
                    instanceBeneficiary.addProperty(OntologySpecification.hasAddress, instanceAddress);
                    infModel.add(instanceAddress, RDF.type, OntologySpecification.addressResource);
                    instanceAddress.addProperty(OntologySpecification.street, buyer.getAddress().trim(), XSDDatatype.XSDstring);
                }
            }
            Resource instanceCountry = infModel.createResource("http://linkedeconomy.org/resource/Country/GR");
            infModel.add(instanceBeneficiary, RDF.type, OntologySpecification.organizationResource);
            infModel.add(instanceBeneficiary, RDF.type, OntologySpecification.organizationOrgResource);
            infModel.add(instanceBeneficiary, RDF.type, OntologySpecification.businessResource);
            infModel.add(instanceBeneficiary, RDF.type, OntologySpecification.organizationRovResource);
            if (buyer.getName() != null) {
                instanceBeneficiary.addProperty(OntologySpecification.foafName, buyer.getName().trim(), "el");
            }
            instanceBeneficiary.addProperty(OntologySpecification.countryIsoCode, instanceCountry);
        }

        for (ProjectBeneficiaries buyer1 : projectBuyers) {

            if (buyer1.getVatId() != null) {
                instanceBeneficiary = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + buyer1.getVatId());
                Resource instanceProject = infModel.createResource(OntologySpecification.instancePrefix + "PublicWork/" + buyer1.getOps());
                instanceProject.addProperty(OntologySpecification.buyer, instanceBeneficiary);
            } else {
                instanceBeneficiary = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + buyer1.getBeneficiaryId() + "-B");
                Resource instanceProject = infModel.createResource(OntologySpecification.instancePrefix + "PublicWork/" + buyer1.getOps());
                instanceProject.addProperty(OntologySpecification.buyer, instanceBeneficiary);
            }

        }

        for (ProjectBeneficiaries buyer1 : subProjectBuyers) {

            if (buyer1.getVatId() != null) {
                instanceBeneficiary = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + buyer1.getVatId());
                Resource instanceContract = infModel.createResource(OntologySpecification.instancePrefix + "Contract/" + buyer1.getOps() + "/" + buyer1.getSubprojectId());
                instanceContract.addProperty(OntologySpecification.buyer, instanceBeneficiary);
            } else {
                instanceBeneficiary = infModel.createResource(OntologySpecification.instancePrefix + "Organization/" + buyer1.getBeneficiaryId() + "-B");
                Resource instanceContract = infModel.createResource(OntologySpecification.instancePrefix + "Contract/" + buyer1.getOps() + "/" + buyer1.getSubprojectId());
                instanceContract.addProperty(OntologySpecification.buyer, instanceBeneficiary);
            }

        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    CommonVariables.serverPath + "beneficiaries_" + CommonVariables.currentDate + ".rdf");
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }

    }

}
