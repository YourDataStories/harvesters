/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linkedeconomy.espa.ontology;

/**
 *
 * @author G. Vafeiadis
 */
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class OntologySpecification {

    /**
     * prefixes *
     */
    public static final String goodRelationsPrefix;
    public static final String elodPrefix;
    public static final String elodGeoPrefix;
    public static final String elodQBPrefix;
    public static final String qbPrefix;
    public static final String sdmxAttributePrefix;
    public static final String sdmxDimensionPrefix;
    public static final String sdmxConceptPrefix;
    public static final String sdmxMeasurePrefix;
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
    public static final Resource yearResource;
    public static final Resource budgetResource;
    public static final Resource spendingResource;
    public static final Resource expLineResource;
    public static final Resource cpvResource;
    public static final Resource kaeResource;
    public static final Resource thematicResource;
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
    public static final Resource financialResource;
    public static final Resource conceptResource;
//    public static final Resource orgUnitResource;
    public static final Resource municipalityResource;
    public static final Resource pcodeResource;

    /**
     * Classes for DataCube*
     */
    public static final Resource observationResource;
    public static final Resource dataSetResource;
    public static final Resource dimensionProperty;
    public static final Resource attributeProperty;
    public static final Resource measureProperty;
    public static final Resource intervalResource;
    public static final Resource instantResource;
    public static final Resource dataStructureResource;
    public static final Resource hierarchicalCodeListResource;

    /**
     * properties DataCube Diavgeia*
     */
    public static final Property qbThematic;
    public static final Property qbCPV;
    public static final Property qbKAE;
    public static final Property qbTimestamp;
    public static final Property qbDecisionType;
    public static final Property qbhasCurrencyValue;
    public static final Property qbhasCurrency;
    public static final Property dataSet;
    public static final Property concept;
    public static final Property codeList;
    public static final Property timePeriod;
    public static final Property conceptTimePeriod;
    public static final Property obsValue;
    public static final Property structure;
    public static final Property component;
    public static final Property dimension;
    public static final Property measure;
    public static final Property attribute;
    public static final Property componentRequired;
    public static final Property componentAttachment;
    public static final Property hierarchyRoot;
    public static final Property inXSD;

    /**
     * Object Properties ESPA*
     */
    public static final Property hasPriceSpecification;
    public static final Property beneficiary;
    public static final Property seller;
    public static final Property year;
    public static final Property price;
    public static final Property amount;
    public static final Property hasRelatedProject;
    public static final Property hasRelatedBudgetItem;
    public static final Property hasRelatedSpendingItem;
    public static final Property hasExpenditureLine;
    public static final Property hasAddress;
    public static final Property isStatisticOf;
    public static final Property hasRelatedFeature;
    public static final Property hasGeometry;
    public static final Property unitOf;
    public static final Property hasUnit;
    public static final Property hasPart;
    public static final Property hasCurrency;
    public static final Property hasKae;
    public static final Property subsidyMunicipality;
    public static final Property estimatedPrice;
    public static final Property countryIsoCode;

    /**
     * Data Properties ESPA*
     */
    public static final Property name;
    public static final Property issued;
    public static final Property title;
    public static final Property desc;
    public static final Property description;
    public static final Property hasCurrencyValue;
    public static final Property valueAddedTaxIncluded;
    public static final Property legalName;
    public static final Property label;
    public static final Property countOfRelatedProjects;
    public static final Property completion;
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
    public static final Property cpvCode;
    public static final Property cpvGreekSubject;
    public static final Property cpvEnglishSubject;
    public static final Property geoName;
    public static final Property geoPostalCode;
    public static final Property kae;
    public static final Property decisionType;


    static {
        /**
         * prefixes *
         */
        instancePrefix = "http://linkedeconomy.org/resource/";
        goodRelationsPrefix = "http://purl.org/goodrelations/v1#";
        elodPrefix = "http://linkedeconomy.org/ontology#";
        elodGeoPrefix = "http://linkedeconomy.org/geoOntology#";
        elodQBPrefix = "http://linkedeconomy.org/data-cube/ontology#";
        qbPrefix = "http://purl.org/linked-data/cube#";
        sdmxAttributePrefix = "http://purl.org/linked-data/sdmx/2009/attribute#";
        sdmxDimensionPrefix = "http://purl.org/linked-data/sdmx/2009/dimension#";
        sdmxConceptPrefix = "http://purl.org/linked-data/sdmx/2009/concept#";
        sdmxMeasurePrefix = "http://purl.org/linked-data/sdmx/2009/measure#";
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
         * classes for Espa *
         */
        organizationResource = ResourceFactory.createResource(foafPrefix + "Organization");
        organizationRovResource = ResourceFactory.createResource(rovPrefix + "RegisteredOrganization");
        organizationOrgResource = ResourceFactory.createResource(orgPrefix + "Organization");
        personResource = ResourceFactory.createResource(foafPrefix + "Person");
        organizationalUnitResource = ResourceFactory.createResource(orgPrefix + "OrganizationalUnit");
        businessResource = ResourceFactory.createResource(goodRelationsPrefix + "BusinessEntity");
        priceSpecificationResource = ResourceFactory.createResource(goodRelationsPrefix + "UnitPriceSpecification");
        yearResource = ResourceFactory.createResource(elodPrefix + "Year");
        budgetResource = ResourceFactory.createResource(elodPrefix + "BudgetItem");
        spendingResource = ResourceFactory.createResource(elodPrefix + "SpendingItem");
        expLineResource = ResourceFactory.createResource(elodPrefix + "ExpenditureLine");
        cpvResource = ResourceFactory.createResource(elodPrefix + "CPV");
        kaeResource = ResourceFactory.createResource(elodPrefix + "KAE");
        thematicResource = ResourceFactory.createResource(elodPrefix + "ThematicCategory");
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
        financialResource = ResourceFactory.createResource(elodPrefix + "FinancialDecision");
        conceptResource = ResourceFactory.createResource(skosPrefix + "Concept");
//        orgUnitResource = ResourceFactory.createResource(skosPrefix + "OrganizationalUnit");
        pcodeResource = ResourceFactory.createResource(elodGeoPrefix + "PostalCodeArea");
        municipalityResource = ResourceFactory.createResource(elodGeoPrefix + "Municipality");

        /**
         * classes for Data Cube *
         */
        observationResource = ResourceFactory.createResource(qbPrefix + "Observation");
        dataSetResource = ResourceFactory.createResource(qbPrefix + "DataSet");
        dimensionProperty = ResourceFactory.createResource(qbPrefix + "DimensionProperty");
        attributeProperty = ResourceFactory.createResource(qbPrefix + "AttributeProperty");
        measureProperty = ResourceFactory.createResource(qbPrefix + "MeasureProperty");
        intervalResource = ResourceFactory.createResource(timePrefix + "Interval");
        instantResource = ResourceFactory.createResource(timePrefix + "Instant");
        dataStructureResource = ResourceFactory.createResource(qbPrefix + "DataStructureDefinition");
        hierarchicalCodeListResource = ResourceFactory.createResource(qbPrefix + "HierarchicalCodeList");

        /**
         * properties DataCube *
         */
        qbThematic = ResourceFactory.createProperty(elodQBPrefix + "hasThematicCategory");
        qbCPV = ResourceFactory.createProperty(elodQBPrefix + "hasCpv");
        qbKAE = ResourceFactory.createProperty(elodQBPrefix + "hasKae");
        qbTimestamp = ResourceFactory.createProperty(elodQBPrefix + "submissionTimestamp");
        qbDecisionType = ResourceFactory.createProperty(elodQBPrefix + "decisionTypeId");
        qbhasCurrencyValue = ResourceFactory.createProperty(elodQBPrefix + "hasCurrencyValue");
        qbhasCurrency = ResourceFactory.createProperty(sdmxAttributePrefix + "currency");
        dataSet = ResourceFactory.createProperty(qbPrefix + "dataSet");
        concept = ResourceFactory.createProperty(qbPrefix + "concept");
        codeList = ResourceFactory.createProperty(qbPrefix + "codeList");
        timePeriod = ResourceFactory.createProperty(sdmxDimensionPrefix + "timePeriod");
        conceptTimePeriod = ResourceFactory.createProperty(sdmxConceptPrefix + "timePeriod");
        obsValue = ResourceFactory.createProperty(sdmxMeasurePrefix + "obsValue");
        structure = ResourceFactory.createProperty(qbPrefix + "structure");
        component = ResourceFactory.createProperty(qbPrefix + "component");
        dimension = ResourceFactory.createProperty(qbPrefix + "dimension");
        measure = ResourceFactory.createProperty(qbPrefix + "measure");
        attribute = ResourceFactory.createProperty(qbPrefix + "attribute");
        componentRequired = ResourceFactory.createProperty(qbPrefix + "componentRequired");
        componentAttachment = ResourceFactory.createProperty(qbPrefix + "componentAttachment");
        hierarchyRoot = ResourceFactory.createProperty(qbPrefix + "hierarchyRoot");
        inXSD = ResourceFactory.createProperty(timePrefix + "inXSDDateTime");

        /**
         * object properties ESPA *
         */
        hasPriceSpecification = ResourceFactory.createProperty(goodRelationsPrefix + "hasPriceSpecification");
        year = ResourceFactory.createProperty(elodPrefix + "year");
        beneficiary = ResourceFactory.createProperty(elodPrefix + "beneficiary");
        seller = ResourceFactory.createProperty(elodPrefix + "seller");
        price = ResourceFactory.createProperty(elodPrefix + "price");
        amount = ResourceFactory.createProperty(elodPrefix + "amount");
        hasRelatedProject = ResourceFactory.createProperty(elodPrefix + "hasRelatedProject");
        hasRelatedBudgetItem = ResourceFactory.createProperty(elodPrefix + "hasRelatedBudgetItem");
        hasRelatedSpendingItem = ResourceFactory.createProperty(elodPrefix + "hasRelatedSpendingItem");
        hasExpenditureLine = ResourceFactory.createProperty(elodPrefix + "hasExpenditureLine");
        hasAddress = ResourceFactory.createProperty(vcardPrefix + "hasAddress");
        isStatisticOf = ResourceFactory.createProperty(elodPrefix + "isStatisticOf");
        hasRelatedFeature = ResourceFactory.createProperty(elodPrefix + "hasRelatedFeature");
        hasGeometry = ResourceFactory.createProperty(geoSparqlPrefix + "hasGeometry");
        unitOf = ResourceFactory.createProperty(orgPrefix + "unitOf");
        hasUnit = ResourceFactory.createProperty(orgPrefix + "hasUnit");
        hasPart = ResourceFactory.createProperty(elodGeoPrefix + "hasPart");
        hasCurrency = ResourceFactory.createProperty(elodPrefix + "hasCurrency");
        hasKae = ResourceFactory.createProperty(elodPrefix + "hasKae");
        subsidyMunicipality = ResourceFactory.createProperty(elodPrefix + "subsidyMunicipality");
        estimatedPrice = ResourceFactory.createProperty(publicContractsPrefix + "estimatedPrice");
        countryIsoCode = ResourceFactory.createProperty(elodPrefix + "countryIsoCode");

        /**
         * datatype properties KATH *
         */
        name = ResourceFactory.createProperty(goodRelationsPrefix + "name");
        description = ResourceFactory.createProperty(goodRelationsPrefix + "description");
        hasCurrencyValue = ResourceFactory.createProperty(goodRelationsPrefix + "hasCurrencyValue");
        valueAddedTaxIncluded = ResourceFactory.createProperty(goodRelationsPrefix + "valueAddedTaxIncluded");
        legalName = ResourceFactory.createProperty(goodRelationsPrefix + "legalName");
        label = ResourceFactory.createProperty(rdfsPrefix + "label");
        countOfRelatedProjects = ResourceFactory.createProperty(elodPrefix + "countOfRelatedProjects");
        completion = ResourceFactory.createProperty(elodPrefix + "completion");
        startDate = ResourceFactory.createProperty(elodPrefix + "startDate");
        endDate = ResourceFactory.createProperty(elodPrefix + "endDate");
        street = ResourceFactory.createProperty(vcardPrefix + "street-address");
        vatID = ResourceFactory.createProperty(goodRelationsPrefix + "vatID");
        issued = ResourceFactory.createProperty(dctermsPrefix + "issued");
        title = ResourceFactory.createProperty(dctermsPrefix + "title");
        desc = ResourceFactory.createProperty(dctermsPrefix + "description");
        translation = ResourceFactory.createProperty(elodPrefix + "translation");
        projectId = ResourceFactory.createProperty(elodPrefix + "projectId");
        coordinates = ResourceFactory.createProperty(elodPrefix + "coordinates");
        uuid = ResourceFactory.createProperty(muPrefix + "uuid");
        postal = ResourceFactory.createProperty(vcardPrefix + "postal-code");
        asWKT = ResourceFactory.createProperty(geoSparqlPrefix + "asWKT");
        cpvCode = ResourceFactory.createProperty(elodPrefix + "cpvCode");
        cpvGreekSubject = ResourceFactory.createProperty(elodPrefix + "cpvGreekSubject");
        cpvEnglishSubject = ResourceFactory.createProperty(elodPrefix + "cpvEnglishSubject");
        geoName = ResourceFactory.createProperty(elodGeoPrefix + "name");
        geoPostalCode = ResourceFactory.createProperty(elodGeoPrefix + "postalCode");
        kae = ResourceFactory.createProperty(elodPrefix + "kae");
        decisionType = ResourceFactory.createProperty(elodPrefix + "decisionType");


    }

}
