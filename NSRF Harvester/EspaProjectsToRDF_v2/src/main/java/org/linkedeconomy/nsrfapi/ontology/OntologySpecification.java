package org.linkedeconomy.nsrfapi.ontology;

/**
 *
 * @author G. Vafeiadis
 */
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class OntologySpecification {

    /**
     *
     * Define Ontology Specification(Vocabularies, Classes and Properties)
     *
     */
    /**
     * prefixes *
     */
    public static final String goodRelationsPrefix;
    public static final String elodPrefix;
    public static final String elodGeoPrefix;
    public static final String instancePrefix;
    public static final String rdfsPrefix;
    public static final String foafPrefix;
    public static final String vcardPrefix;
    public static final String dctermsPrefix;
    public static final String dcPrefix;
    public static final String muPrefix;
    public static final String geoSparqlPrefix;
    public static final String geoFunctionSparqlPrefix;
    public static final String skosPrefix;
    public static final String timePrefix;
    public static final String rovPrefix;
    public static final String orgPrefix;
    public static final String publicContractsPrefix;

    /**
     * Classes for ESPA*
     */
    public static final Resource organizationResource;
    public static final Resource organizationOrgResource;
    public static final Resource organizationRovResource;
    public static final Resource personResource;
    public static final Resource organizationalUnitResource;
    public static final Resource businessResource;
    public static final Resource priceSpecificationResource;
    public static final Resource budgetResource;
    public static final Resource spendingResource;
    public static final Resource expLineResource;
    public static final Resource projectResource;
    public static final Resource subProjectResource;
    public static final Resource addressResource;
    public static final Resource subsidyResource;
    public static final Resource statisticResource;
    public static final Resource placeOfInterestResource;
    public static final Resource pointResource;
    public static final Resource featureResource;
    public static final Resource geometryResource;
    public static final Resource curveResource;
    public static final Resource lineStringResource;
    public static final Resource conceptResource;
    public static final Resource publicProjectResource;
    public static final Resource publicWorkResource;
    public static final Resource contractResource;
    public static final Resource aggregateResource;
    public static final Resource paymentCategoryResource;
    public static final Resource budgetCategoryResource;
    public static final Resource contractCategoryResource;

    /**
     * Object Properties ESPA*
     */
    public static final Property hasPriceSpecification;
    public static final Property beneficiary;
    public static final Property buyer;
    public static final Property seller;
    public static final Property price;
    public static final Property amount;
    public static final Property hasRelatedProject;
    public static final Property hasRelatedBudgetItem;
    public static final Property hasRelatedSpendingItem;
    public static final Property hasRelatedContract;
    public static final Property hasExpenditureLine;
    public static final Property hasAddress;
    public static final Property isStatisticOf;
    public static final Property hasRelatedFeature;
    public static final Property hasGeometry;
    public static final Property unitOf;
    public static final Property hasUnit;
    public static final Property hasCurrency;
    public static final Property subsidyMunicipality;
    public static final Property estimatedPrice;
    public static final Property agreedPrice;
    public static final Property narrower;
    public static final Property broader;
    public static final Property countryIsoCode;
    public static final Property hasProjectStatus;
    public static final Property hasOperationalCode;
    public static final Property hasRegion;
    public static final Property hasSpendingAggregate;
    public static final Property hasBudgetAggregate;
    public static final Property hasContractAggregate;
    public static final Property hasCategory;

    /**
     * Data Properties ESPA*
     */
    public static final Property name;
    public static final Property foafName;
    public static final Property issued;
    public static final Property modified;
    public static final Property title;
    public static final Property desc;
    public static final Property description;
    public static final Property hasCurrencyValue;
    public static final Property valueAddedTaxIncluded;
    public static final Property legalName;
    public static final Property label;
    public static final Property countOfRelatedContracts;
    public static final Property completion;
    public static final Property completionOfPayments;
    public static final Property completionOfContracts;
    public static final Property startDate;
    public static final Property endDate;
    public static final Property street;
    public static final Property vatID;
    public static final Property translation;
    public static final Property projectId;
    public static final Property coordinates;
    public static final Property uuid;
    public static final Property postal;
    public static final Property asWKT;
    public static final Property geoName;
    public static final Property pcStartDate;
    public static final Property actualEndDate;
    public static final Property estimatedEndDate;
    public static final Property aggregatedAmount;

    public static final Property prefLabel;
    public static final Property notation;

    static {
        /**
         * prefixes *
         */
        instancePrefix = "http://linkedeconomy.org/resource/";
        goodRelationsPrefix = "http://purl.org/goodrelations/v1#";
        elodPrefix = "http://linkedeconomy.org/ontology#";
        elodGeoPrefix = "http://linkedeconomy.org/geoOntology#";
        timePrefix = "http://www.w3.org/2006/time#";
        rdfsPrefix = "http://www.w3.org/2000/01/rdf-schema#";
        foafPrefix = "http://xmlns.com/foaf/0.1/";
        vcardPrefix = "http://www.w3.org/2006/vcard/ns#";
        dctermsPrefix = "http://purl.org/dc/terms/";
        dcPrefix = "http://purl.org/dc/elements/1.1/";
        muPrefix = "http://mu.semte.ch/vocabularies/core/";
        geoSparqlPrefix = "http://www.opengis.net/ont/geosparql#";
        geoFunctionSparqlPrefix = "http://www.opengis.net/ont/sf#";
        skosPrefix = "http://www.w3.org/2004/02/skos/core#";
        rovPrefix = "http://www.w3.org/ns/regorg#";
        orgPrefix = "http://www.w3.org/ns/org#";
        publicContractsPrefix = "http://purl.org/procurement/public-contracts#";

        /**
         * classes for ESPA *
         */
        organizationResource = ResourceFactory.createResource(foafPrefix + "Organization");
        organizationRovResource = ResourceFactory.createResource(rovPrefix + "RegisteredOrganization");
        organizationOrgResource = ResourceFactory.createResource(orgPrefix + "Organization");
        personResource = ResourceFactory.createResource(foafPrefix + "Person");
        organizationalUnitResource = ResourceFactory.createResource(orgPrefix + "OrganizationalUnit");
        businessResource = ResourceFactory.createResource(goodRelationsPrefix + "BusinessEntity");
        priceSpecificationResource = ResourceFactory.createResource(goodRelationsPrefix + "UnitPriceSpecification");
        budgetResource = ResourceFactory.createResource(elodPrefix + "BudgetItem");
        spendingResource = ResourceFactory.createResource(elodPrefix + "SpendingItem");
        expLineResource = ResourceFactory.createResource(elodPrefix + "ExpenditureLine");
        projectResource = ResourceFactory.createResource(elodPrefix + "PublicProject");
        subProjectResource = ResourceFactory.createResource(elodPrefix + "Subproject");
        addressResource = ResourceFactory.createResource(vcardPrefix + "Address");
        subsidyResource = ResourceFactory.createResource(elodPrefix + "Subsidy");
        statisticResource = ResourceFactory.createResource(elodPrefix + "Statistic");
        placeOfInterestResource = ResourceFactory.createResource(elodPrefix + "PlaceOfInterest");
        pointResource = ResourceFactory.createResource(geoFunctionSparqlPrefix + "Point");
        featureResource = ResourceFactory.createResource(geoSparqlPrefix + "Feature");
        geometryResource = ResourceFactory.createResource(geoSparqlPrefix + "Geometry");
        curveResource = ResourceFactory.createResource(geoFunctionSparqlPrefix + "Curve");
        lineStringResource = ResourceFactory.createResource(geoFunctionSparqlPrefix + "LineString");
        conceptResource = ResourceFactory.createResource(skosPrefix + "Concept");
        publicProjectResource = ResourceFactory.createResource(elodPrefix + "PublicProject");
        publicWorkResource = ResourceFactory.createResource(elodPrefix + "PublicWork");
        contractResource = ResourceFactory.createResource(publicContractsPrefix + "Contract");
        aggregateResource = ResourceFactory.createResource(elodPrefix + "Aggregate");
        paymentCategoryResource = ResourceFactory.createResource(elodPrefix + "Payment");
        budgetCategoryResource = ResourceFactory.createResource(elodPrefix + "Budget");
        contractCategoryResource = ResourceFactory.createResource(elodPrefix + "Contract");

        /**
         * object properties ESPA *
         */
        hasPriceSpecification = ResourceFactory.createProperty(goodRelationsPrefix + "hasPriceSpecification");
        beneficiary = ResourceFactory.createProperty(elodPrefix + "beneficiary");
        buyer = ResourceFactory.createProperty(elodPrefix + "buyer");
        seller = ResourceFactory.createProperty(elodPrefix + "seller");
        price = ResourceFactory.createProperty(elodPrefix + "price");
        amount = ResourceFactory.createProperty(elodPrefix + "amount");
        hasRelatedProject = ResourceFactory.createProperty(elodPrefix + "hasRelatedProject");
        hasRelatedBudgetItem = ResourceFactory.createProperty(elodPrefix + "hasRelatedBudgetItem");
        hasRelatedSpendingItem = ResourceFactory.createProperty(elodPrefix + "hasRelatedSpendingItem");
        hasRelatedContract = ResourceFactory.createProperty(elodPrefix + "hasRelatedContract");
        hasExpenditureLine = ResourceFactory.createProperty(elodPrefix + "hasExpenditureLine");
        hasAddress = ResourceFactory.createProperty(vcardPrefix + "hasAddress");
        isStatisticOf = ResourceFactory.createProperty(elodPrefix + "isStatisticOf");
        hasRelatedFeature = ResourceFactory.createProperty(elodPrefix + "hasRelatedFeature");
        hasGeometry = ResourceFactory.createProperty(geoSparqlPrefix + "hasGeometry");
        unitOf = ResourceFactory.createProperty(orgPrefix + "unitOf");
        hasUnit = ResourceFactory.createProperty(orgPrefix + "hasUnit");
        hasCurrency = ResourceFactory.createProperty(elodPrefix + "hasCurrency");
        subsidyMunicipality = ResourceFactory.createProperty(elodPrefix + "subsidyMunicipality");
        estimatedPrice = ResourceFactory.createProperty(publicContractsPrefix + "estimatedPrice");
        agreedPrice = ResourceFactory.createProperty(publicContractsPrefix + "agreedPrice");
        narrower = ResourceFactory.createProperty(skosPrefix + "narrower");
        broader = ResourceFactory.createProperty(skosPrefix + "broader");
        countryIsoCode = ResourceFactory.createProperty(elodPrefix + "countryIsoCode");
        hasProjectStatus = ResourceFactory.createProperty(elodPrefix + "hasProjectStatus");
        hasOperationalCode = ResourceFactory.createProperty(elodPrefix + "hasOperationalCode");
        hasRegion = ResourceFactory.createProperty(elodPrefix + "hasRegion");
        hasSpendingAggregate = ResourceFactory.createProperty(elodPrefix + "hasSpendingAggregate");
        hasBudgetAggregate = ResourceFactory.createProperty(elodPrefix + "hasBudgetAggregate");
        hasContractAggregate = ResourceFactory.createProperty(elodPrefix + "hasContractAggregate");
        hasCategory = ResourceFactory.createProperty(elodPrefix + "hasCategory");

        /**
         * datatype properties ESPA *
         */
        name = ResourceFactory.createProperty(goodRelationsPrefix + "name");
        foafName = ResourceFactory.createProperty(foafPrefix + "name");
        description = ResourceFactory.createProperty(goodRelationsPrefix + "description");
        hasCurrencyValue = ResourceFactory.createProperty(goodRelationsPrefix + "hasCurrencyValue");
        valueAddedTaxIncluded = ResourceFactory.createProperty(goodRelationsPrefix + "valueAddedTaxIncluded");
        legalName = ResourceFactory.createProperty(goodRelationsPrefix + "legalName");
        label = ResourceFactory.createProperty(rdfsPrefix + "label");
        countOfRelatedContracts = ResourceFactory.createProperty(elodPrefix + "countOfRelatedContracts");
        completion = ResourceFactory.createProperty(elodPrefix + "completion");
        completionOfPayments = ResourceFactory.createProperty(elodPrefix + "completionOfPayments");
        completionOfContracts = ResourceFactory.createProperty(elodPrefix + "completionOfContracts");
        startDate = ResourceFactory.createProperty(elodPrefix + "startDate");
        endDate = ResourceFactory.createProperty(elodPrefix + "endDate");
        street = ResourceFactory.createProperty(vcardPrefix + "street-address");
        vatID = ResourceFactory.createProperty(goodRelationsPrefix + "vatID");
        issued = ResourceFactory.createProperty(dctermsPrefix + "issued");
        modified = ResourceFactory.createProperty(dctermsPrefix + "modified");
        title = ResourceFactory.createProperty(dctermsPrefix + "title");
        desc = ResourceFactory.createProperty(dctermsPrefix + "description");
        translation = ResourceFactory.createProperty(elodPrefix + "translation");
        projectId = ResourceFactory.createProperty(elodPrefix + "projectId");
        coordinates = ResourceFactory.createProperty(elodPrefix + "coordinates");
        uuid = ResourceFactory.createProperty(muPrefix + "uuid");
        postal = ResourceFactory.createProperty(vcardPrefix + "postal-code");
        asWKT = ResourceFactory.createProperty(geoSparqlPrefix + "asWKT");
        geoName = ResourceFactory.createProperty(elodGeoPrefix + "name");
        pcStartDate = ResourceFactory.createProperty(publicContractsPrefix + "startDate");
        actualEndDate = ResourceFactory.createProperty(publicContractsPrefix + "actualEndDate");
        estimatedEndDate = ResourceFactory.createProperty(publicContractsPrefix + "estimatedEndDate");
        aggregatedAmount = ResourceFactory.createProperty(elodPrefix + "aggregatedAmount");

        prefLabel = ResourceFactory.createProperty(skosPrefix + "prefLabel");
        notation = ResourceFactory.createProperty(skosPrefix + "notation");

    }

}
