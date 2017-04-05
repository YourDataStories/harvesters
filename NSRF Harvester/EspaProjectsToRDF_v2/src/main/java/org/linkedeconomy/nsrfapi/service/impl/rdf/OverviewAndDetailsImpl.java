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
import java.text.ParseException;
import org.linkedeconomy.nsrfapi.commons.CommonVariables;
import org.linkedeconomy.nsrfapi.dto.OverviewCoordinates;
import org.linkedeconomy.nsrfapi.dto.OverviewDetails;
import org.linkedeconomy.nsrfapi.service.OverviewService;

public class OverviewAndDetailsImpl {

    /**
     *
     * Implementation Of Public Work Service layer and transformation of
     * Database data to RDF
     *
     * @throws java.text.ParseException
     */
    public static void exportRdf() throws ParseException {

        //services for each table
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        OverviewService over = (OverviewService) ctx.getBean("overviewServiceImpl");

        List<OverviewDetails> overviewErga = over.getOverviewAndDetailsErga();
        List<OverviewCoordinates> overviewCoords = over.getOverviewCoordinates();

        //--------------RDF Model--------------//
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", OntologySpecification.elodPrefix);
        model.setNsPrefix("gr", OntologySpecification.goodRelationsPrefix);
        model.setNsPrefix("dcterms", OntologySpecification.dctermsPrefix);

        @SuppressWarnings("UnusedAssignment")
        Resource instanceProject = null;

        for (OverviewDetails overview1 : overviewErga) {

            Double completionPayments = Double.valueOf(overview1.getCompletion()) * 100;
            Double completionContracts = ((overview1.getContracts().doubleValue() / overview1.getBudget().doubleValue()) * 100);

            Resource instanceCurrency = infModel.createResource("http://linkedeconomy.org/resource/Currency/EUR");
            Resource instanceCountry = infModel.createResource("http://linkedeconomy.org/resource/Country/GR");
            Resource instanceAggregatePayment = infModel.createResource(OntologySpecification.instancePrefix
                    + "Aggregate/Payment/" + overview1.getOps());
            Resource instanceAggregateBudget = infModel.createResource(OntologySpecification.instancePrefix
                    + "Aggregate/Budget/" + overview1.getOps());
            Resource instanceAggregateContract = infModel.createResource(OntologySpecification.instancePrefix
                    + "Aggregate/Contract/" + overview1.getOps());
            Resource instanceEpKodikos = infModel.createResource(OntologySpecification.instancePrefix
                    + "OperationalCode/" + overview1.getEpKodikos());
            Resource instanceProjectStatus = infModel.createResource(OntologySpecification.instancePrefix
                    + "ProjectStatus/" + overview1.getTrexousaKatastash());
            Resource instanceRegion = infModel.createResource(OntologySpecification.instancePrefix
                    + "Region/" + overview1.getPerifereia());
            instanceProject = infModel.createResource(OntologySpecification.instancePrefix + "PublicWork/" + overview1.getOps());

            infModel.add(instanceAggregatePayment, RDF.type, OntologySpecification.aggregateResource);
            infModel.add(instanceAggregateBudget, RDF.type, OntologySpecification.aggregateResource);
            infModel.add(instanceAggregateContract, RDF.type, OntologySpecification.aggregateResource);
            infModel.add(instanceProject, RDF.type, OntologySpecification.publicWorkResource);
            infModel.add(instanceProject, RDF.type, OntologySpecification.projectResource);

            instanceProject.addProperty(OntologySpecification.hasSpendingAggregate, instanceAggregatePayment);
            instanceProject.addProperty(OntologySpecification.hasBudgetAggregate, instanceAggregateBudget);
            instanceProject.addProperty(OntologySpecification.hasContractAggregate, instanceAggregateContract);
            instanceProject.addProperty(OntologySpecification.countryIsoCode, instanceCountry);
            instanceProject.addProperty(OntologySpecification.hasProjectStatus, instanceProjectStatus);
            instanceProject.addProperty(OntologySpecification.hasOperationalCode, instanceEpKodikos);
            instanceProject.addProperty(OntologySpecification.hasRegion, instanceRegion);
            instanceProject.addProperty(OntologySpecification.title, String.valueOf(overview1.getTitle().trim()), "el");
            instanceProject.addProperty(OntologySpecification.projectId, String.valueOf(overview1.getOps()), XSDDatatype.XSDstring);
            instanceProject.addProperty(OntologySpecification.modified, overview1.getPublishDate().toString().replace(" ", "T").replace(".0", ""), XSDDatatype.XSDdateTime);
            instanceProject.addProperty(OntologySpecification.completionOfPayments, String.valueOf(CommonVariables.dfNumberOne.format(completionPayments)), XSDDatatype.XSDfloat);
            instanceProject.addProperty(OntologySpecification.completionOfContracts, String.valueOf(CommonVariables.dfNumberOne.format(completionContracts)), XSDDatatype.XSDfloat);
            if (overview1.getStartDate() != null) {
                instanceProject.addProperty(OntologySpecification.startDate, overview1.getStartDate().toString().replace(" ", "T").replace(".0", ""), XSDDatatype.XSDdateTime);
            }
            if (overview1.getEndDate() != null) {
                instanceProject.addProperty(OntologySpecification.endDate, overview1.getEndDate().toString().replace(" ", "T").replace(".0", ""), XSDDatatype.XSDdateTime);
            }
            instanceProject.addProperty(OntologySpecification.desc, String.valueOf(overview1.getDescription()), "el");
            if (overview1.getTitleEnglish() != null) {
                instanceProject.addProperty(OntologySpecification.title, String.valueOf(overview1.getTitleEnglish()), "en");
            } else {
                instanceProject.addProperty(OntologySpecification.title, "no english title", "en");
            }
            if (overview1.getCountSubprojects() != null) {
                instanceProject.addProperty(OntologySpecification.countOfRelatedContracts, String.valueOf(overview1.getCountSubprojects()), XSDDatatype.XSDinteger);
            }
            instanceAggregatePayment.addProperty(OntologySpecification.aggregatedAmount, CommonVariables.dfNumber.format(overview1.getPayments()), XSDDatatype.XSDfloat);
            instanceAggregatePayment.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
            instanceAggregateBudget.addProperty(OntologySpecification.aggregatedAmount, CommonVariables.dfNumber.format(overview1.getBudget()), XSDDatatype.XSDfloat);
            instanceAggregateBudget.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
            instanceAggregateContract.addProperty(OntologySpecification.aggregatedAmount, CommonVariables.dfNumber.format(overview1.getContracts()), XSDDatatype.XSDfloat);
            instanceAggregateContract.addProperty(OntologySpecification.hasCurrency, instanceCurrency);
        }

        for (OverviewCoordinates overviewCoords1 : overviewCoords) {
            String geoDataPoint = "";
            String geoDataLine = "";

            instanceProject = infModel.createResource(OntologySpecification.instancePrefix + "PublicWork/" + overviewCoords1.getKodikos());
            Resource instanceFeature = infModel.createResource(OntologySpecification.instancePrefix + "Feature/" + overviewCoords1.getKodikos() + "/" + overviewCoords1.getCooedinatesId());
            Resource instanceGeometry = infModel.createResource(OntologySpecification.instancePrefix + "Geometry/" + overviewCoords1.getKodikos() + "/" + overviewCoords1.getCooedinatesId());

            infModel.add(instanceProject, RDF.type, OntologySpecification.publicWorkResource);
            infModel.add(instanceFeature, RDF.type, OntologySpecification.featureResource);
            infModel.add(instanceGeometry, RDF.type, OntologySpecification.geometryResource);

            instanceProject.addProperty(OntologySpecification.hasRelatedFeature, instanceFeature);
            instanceFeature.addProperty(OntologySpecification.hasGeometry, instanceGeometry);

            if (overviewCoords1.getCoordinates().contains(",")) {
                String[] linestrings2 = overviewCoords1.getCoordinates().split(",");
                for (String coordinates : linestrings2) {
                    String[] linestringsFlip = coordinates.split(" ");
                    geoDataLine += linestringsFlip[1] + " " + linestringsFlip[0] + ", ";
                }
                String lineData = geoDataLine + ")";

                instanceGeometry.addLiteral(OntologySpecification.asWKT,
                        model.createTypedLiteral("LINESTRING(" + lineData.replace(", )", ")"), OntologySpecification.geoSparqlPrefix + "wktLiteral"));
            } else {
                String[] linestrings = overviewCoords1.getCoordinates().split(" ");

                geoDataPoint += linestrings[1] + " " + linestrings[0];
                instanceGeometry.addLiteral(OntologySpecification.asWKT,
                        model.createTypedLiteral("POINT(" + geoDataPoint + ")", OntologySpecification.geoSparqlPrefix + "wktLiteral"));
            }
        }

        try {
            FileOutputStream fout = new FileOutputStream(
                    CommonVariables.serverPath + "overviewAndDetails_" + CommonVariables.currentDate + ".rdf");
            model.write(fout);
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }
}
