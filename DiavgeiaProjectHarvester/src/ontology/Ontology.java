package ontology;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * @author G. Razis
 * @author G. Vafeiadis
 */
public class Ontology {
	
	/** prefixes **/
	public static final String instancePrefix;
	public static final String eLodPrefix;
	public static final String elodGeoPrefix;
	public static final String foafPrefix;
	public static final String publicContractsPrefix;
	public static final String skosPrefix;
	public static final String goodRelationsPrefix;
	public static final String regOrgPrefix;
	public static final String orgPrefix;
	public static final String xsdPrefix;
	public static final String cpvPrefix;
	public static final String dctermsPrefix;
	public static final String dcPrefix;
	public static final String pcdtPrefix;
	public static final String vcardPrefix;
	public static final String muPrefix;
	public static final String kindPrefix;
	public static final String procTypesPrefix;
	
	/**Classes**/
	//eLod
	public static final Resource orgUnitCategoryResource;
	public static final Resource cpvResource;
	public static final Resource fekResource;
	public static final Resource fekTypeResource;
	public static final Resource kaeResource;
	public static final Resource thematicCategoryResource;
	public static final Resource countryResource;
	public static final Resource currencyResource;
	public static final Resource committedItemResource;
	public static final Resource attachmentResource;
	public static final Resource decisionResource;
	public static final Resource financialResource;
	public static final Resource nonFinancialResource;
	public static final Resource regulatoryActResource;
	public static final Resource opinionResource;
	public static final Resource budgetKindResource;
	public static final Resource projectResource;
	public static final Resource expenditureLineResource;
	public static final Resource expenseApprovalItemResource;
	public static final Resource revenueItemResource;
	public static final Resource spendingItemResource;
	public static final Resource vatTypeResource;
	public static final Resource decisionStatusResource;
	public static final Resource organizationStatusResource;
	public static final Resource organizationDomainResource;
	public static final Resource organizationCategoryResource;
	public static final Resource selectionCriterionResource;
	public static final Resource budgetCategoryResource;
	public static final Resource accountResource;
	public static final Resource timeResource;
	public static final Resource positionResource;
	public static final Resource collectiveTypeResource;
	public static final Resource collectiveKindResource;
	public static final Resource vacancyTypeResource;
	public static final Resource adminChangeResource;
	//FOAF
	public static final Resource personResource;
	public static final Resource agentResource;
	public static final Resource organizationResource;
	//SKOS
	public static final Resource conceptResource;
	public static final Resource conceptSchemeResource;
	//GR
	public static final Resource businessEntityResource;
	public static final Resource unitPriceSpecificationResource;
	//RegOrg
	public static final Resource registeredOrganizationResource;
	//pc
	public static final Resource contractResource;
	public static final Resource awardCriteriaCombinationResource;
	public static final Resource criterionWeightingResource;
	//org
	public static final Resource orgOrganizationResource;
	public static final Resource organizationalUnitResource;
	public static final Resource roleResource;
	public static final Resource membershipResource;
	//vCard
	public static final Resource addressResource;
	//elodGeo
	/*public static final Resource countryGeoResource;
	public static final Resource postalCodeAreaResource;*/
	
	/**Object Properties**/
	//eLod
	public static final Property signer; //CommittedItem/Contract/Payment signer Signer
	public static final Property hasOrgUnitCategory; //OrgUnit hasOrganizationalUnitCategory OrganizationalUnitCategory
	public static final Property hasSupervisorOrganization; //OrgUnit hasSupervisor Org
	public static final Property relatedFek; //Org relatedFek Fek
	public static final Property fekIssue; //Fek fekIssue FekType
	public static final Property hasKae; //CommittedItem/ExpenseApprovalItem hasKae Kae
	public static final Property hasCpv; //Thing hasCpv Cpv
	public static final Property hasThematicCategory; //CommittedItem/Contract/ExpenseApprovalItem/Payment hasThematicCategory ThematicCategory
	public static final Property hasVatType; //Organization hasVatType VatType
	public static final Property seller;
	public static final Property buyer;
	public static final Property price; //CommittedItem price UnitPriceSpecification
	public static final Property amount; //ExpenditureLine amount UnitPriceSpecification
	public static final Property decisionStatus; //CommittedItem decisionStatus decisionStatus
	public static final Property hasBudgetCategory; //CommittedItem hasBudgetCategory budgetCategory
	public static final Property hasExpenditureLine; //PaymentPart hasExpenditureLine ExpenditureLine
	public static final Property hasAttachment; //CommittedItem, Payment, Contract hasAttachment Attachment
	public static final Property hasRelatedCommittedItem; //CommittedItem/Contract/Payment hasRelatedCommittedItem CommittedItem
	public static final Property hasRelatedExpenseApprovalItem; //CommittedItem/ExpenseApprovalItem/Payment hasRelatedExpenseApprovalItem ExpenseApprovalItem
	public static final Property hasRelatedContract; //CommittedItem/ExpenseApprovalItem/Contract hasRelatedContract Contract
	public static final Property hasRelatedDecision; //CommittedItem/Contract/Payment hasRelatedDecision Decision
	public static final Property hasRelatedSpendingItem; //ExpenseApprovalItem hasRelatedSpendingItem SpendingItem
	public static final Property hasCorrectedDecision; //OriginalDecision hasCorrectedDecision CorrectedDecision
	public static final Property hasCurrency; //UnitPriceSpecification hasCurrency Currency
	public static final Property isRegisteredAt; //Org or Person isRegisteredAt Country
	public static final Property remainingBudgetAmount;
	public static final Property remainingCreditAmount;
	public static final Property relatedLaw;
	public static final Property budgetRefersTo;
	public static final Property accountRefersTo;
	public static final Property employerOrg;
	public static final Property donationGiver;
	public static final Property donationReceiver;
	public static final Property assetGrantor;
	public static final Property assetGrantee;
	public static final Property regulatoryAct;
	public static final Property hasOpinionOrgType;
	public static final Property hasBudgetKind;
	public static final Property hasRelatedAdministrativeDecision;
	public static final Property accountType;
	public static final Property timePeriod;
	public static final Property hasPositionType;
	public static final Property collectiveBodyKind;
	public static final Property collectiveBodyType;
	public static final Property hasVacancyType;
	public static final Property competentMinistry;
	public static final Property competentUnit;
	public static final Property hasOfficialAdministrativeChange;
	//SKOS
	public static final Property hasTopConcept;
	public static final Property topConceptOf;
	public static final Property inScheme;
	public static final Property narrower;
	public static final Property broader;
	public static final Property prefLabel;
	//dc
	public static final Property publisher;
	//pc
	public static final Property procedureType; //Contract procedureType ProcedureTypeScheme
	public static final Property kind; //Contract kind KindScheme
	public static final Property weightedCriterion;
	public static final Property criterionWeight;
	public static final Property awardCriteriaCombination; //Contract awardCriteriaCombination AwardCriteriaCombination
	public static final Property awardCriterion; //AwardCriteriaCombination awardCriterion CriterionWeighting
	public static final Property mainObject; //Contract mainObject CpvCode
	public static final Property additionalObject; //Contract additionalObject CpvCode
	public static final Property agreedPrice; //Contract agreedPrice UnitPriceSpecification
	public static final Property documentsPrice; //Contract documentsPrice UnitPriceSpecification
	public static final Property actualPrice; //Contract actualPrice UnitPriceSpecification
	//rov
	public static final Property orgStatus;
	public static final Property orgCategory;
	public static final Property orgActivity;
	public static final Property hasUnit; //Organization hasUnit OrganizationalUnit //na ginei org prefix
	public static final Property unitOf; //OrganizationalUnit unitOf Organization //na ginei org prefix
	//org
	public static final Property role;
	public static final Property hasMember;
	public static final Property member;
	public static final Property organization;
	//vCard
	public static final Property hasAddress;
	//elodGeo
	public static final Property postalCodeArea;//Agent postalCodeArea PostalCodeArea
	//former psnet
	public static final Property cpa;
	
	/**Data Properties**/
	//eLod
	public static final Property ada;
	public static final Property protocolNumber;
	public static final Property decisionTypeId;
	public static final Property decisionType;
	public static final Property submissionTimestamp;
	public static final Property versionId;
	public static final Property correctedVersionId;
	public static final Property url;
	public static final Property documentUrl;
	public static final Property documentChecksum;
	public static final Property privateData;
	public static final Property organizationId;
	public static final Property organizationAbbreviation;
	public static final Property odeManagerEmail;
	public static final Property organizationWebsite;
	public static final Property signerId;
	public static final Property signerActive;
	public static final Property signerOrgSignRights;
	public static final Property organizationUnitId;
	public static final Property organizationUnitAbbreviation;
	public static final Property attachmentId;
	public static final Property attachmentDescription;
	public static final Property attachmentFilename;
	public static final Property attachmentMimeType;
	public static final Property attachmentChecksum;
	public static final Property attachmentSize;
	public static final Property fekNumber;
	public static final Property fekYear;
	public static final Property fekTypeId;
	public static final Property currencyId;
	public static final Property countryId;
	public static final Property documentType;
	public static final Property entryNumber;
	public static final Property financialYear;
	public static final Property isRecalledExpenseDecision;
	public static final Property kae;
	public static final Property isPartialWithdrawal;
	public static final Property noVatOrgId;
	public static final Property validVatId;
	public static final Property instruction;
	public static final Property proceedingNumber;
	public static final Property numberOfPeople;
	public static final Property duration;
	public static final Property europeanLaw;
	public static final Property interCountryLaw;
	public static final Property passAuthority;
	public static final Property decisionNumber;
	public static final Property regulatoryActNumber;
	public static final Property budgetApprovalForOrg;
	public static final Property primaryOfficer;
	public static final Property secondaryOfficer;
	public static final Property accountApprovalForOtherOrg;
	public static final Property asset;
	public static final Property cofinancedProject;
	public static final Property lawYear;
	public static final Property lawNumber;
	public static final Property refersTo;
	public static final Property uuid;
	//GR
	public static final Property vatId;
	public static final Property name;
	public static final Property legalName;
	public static final Property hasCurrencyValue;
	public static final Property valueAddedTaxIncluded;
	//FOAF
	public static final Property firstName;
	public static final Property lastName;
	//dc
	public static final Property subject;
	//dcTerms
	public static final Property issued;
	//vCard
	public static final Property streetAddress; 
	public static final Property locality;
	public static final Property countryName; 
	public static final Property postalCode;
	//former psnet
	public static final Property phoneNumber;
	public static final Property faxNumber;
	public static final Property countOfBranches; 
	public static final Property stopDate;
	public static final Property registrationDate;
	public static final Property fpFlag;
	public static final Property legalStatus;
	public static final Property doyName;
	public static final Property doy;
	public static final Property firmDescription;
	public static final Property profitStatus;
	public static final Property category;
	public static final Property abbreviationASE;
	public static final Property abbreviation;
	public static final Property translation;
	public static final Property transliterationLegalName;
	
	static {
		/** prefixes **/
		instancePrefix = "http://linkedeconomy.org/resource/";
		eLodPrefix = "http://linkedeconomy.org/ontology#";
		elodGeoPrefix = "http://linkedeconomy.org/geoOntology#";
		foafPrefix = "http://xmlns.com/foaf/0.1/";
		publicContractsPrefix = "http://purl.org/procurement/public-contracts#";
		skosPrefix = "http://www.w3.org/2004/02/skos/core#";
		goodRelationsPrefix = "http://purl.org/goodrelations/v1#";
		regOrgPrefix = "http://www.w3.org/ns/regorg#";
		orgPrefix = "http://www.w3.org/ns/org#";
		xsdPrefix = "http://www.w3.org/2001/XMLSchema#";
		cpvPrefix = "http://www.e-nvision.org/ontologies/CPVOntology.owl#";
		dctermsPrefix = "http://purl.org/dc/terms/";
		dcPrefix = "http://purl.org/dc/elements/1.1/";
		pcdtPrefix = "http://purl.org/procurement/public-contracts-datatypes#";
		vcardPrefix = "http://www.w3.org/2006/vcard/ns#";
		muPrefix = "http://mu.semte.ch/vocabularies/";
		kindPrefix = "http://purl.org/procurement/public-contracts-kinds#";
		procTypesPrefix = "http://purl.org/procurement/public-contracts-procedure-types#";
		
		/** Resources **/
		//eLod
		committedItemResource = ResourceFactory.createResource(eLodPrefix + "CommittedItem");
		attachmentResource = ResourceFactory.createResource(eLodPrefix + "Attachment");
		decisionResource = ResourceFactory.createResource(eLodPrefix + "Decision");
		expenditureLineResource = ResourceFactory.createResource(eLodPrefix + "ExpenditureLine");
		expenseApprovalItemResource = ResourceFactory.createResource(eLodPrefix + "ExpenseApprovalItem");
		revenueItemResource = ResourceFactory.createResource(eLodPrefix + "RevenueItem");
		spendingItemResource = ResourceFactory.createResource(eLodPrefix + "SpendingItem");
		orgUnitCategoryResource = ResourceFactory.createResource(eLodPrefix + "OrganizationalUnitCategory");
		cpvResource = ResourceFactory.createResource(eLodPrefix + "CPV");
		fekResource = ResourceFactory.createResource(eLodPrefix + "Fek");
		fekTypeResource = ResourceFactory.createResource(eLodPrefix + "FekType");
		kaeResource = ResourceFactory.createResource(eLodPrefix + "KAE");
		thematicCategoryResource = ResourceFactory.createResource(eLodPrefix + "ThematicCategory");
		countryResource = ResourceFactory.createResource(eLodPrefix + "Country");
		currencyResource = ResourceFactory.createResource(eLodPrefix + "Currency");
		vatTypeResource = ResourceFactory.createResource(eLodPrefix + "VatType");
		decisionStatusResource = ResourceFactory.createResource(eLodPrefix + "DecisionStatus");
		organizationStatusResource = ResourceFactory.createResource(eLodPrefix + "OrganizationStatus");
		organizationDomainResource = ResourceFactory.createResource(eLodPrefix + "OrganizationDomain");
		organizationCategoryResource = ResourceFactory.createResource(eLodPrefix + "OrganizationCategory");
		selectionCriterionResource = ResourceFactory.createResource(eLodPrefix + "SelectionCriterion");
		budgetCategoryResource = ResourceFactory.createResource(eLodPrefix + "BudgetCategory");
		
		financialResource = ResourceFactory.createResource(eLodPrefix + "FinancialDecision");
		nonFinancialResource = ResourceFactory.createResource(eLodPrefix + "NonFinancialDecision");
		regulatoryActResource = ResourceFactory.createResource(eLodPrefix + "RegulatoryAct");
		opinionResource = ResourceFactory.createResource(eLodPrefix + "OpinionOrgType");
		budgetKindResource = ResourceFactory.createResource(eLodPrefix + "BudgetKind");
		projectResource = ResourceFactory.createResource(eLodPrefix + "Subsidy");
		accountResource = ResourceFactory.createResource(eLodPrefix + "AccountType");
		timeResource = ResourceFactory.createResource(eLodPrefix + "TimePeriod");
		positionResource = ResourceFactory.createResource(eLodPrefix + "PositionType");
		collectiveTypeResource = ResourceFactory.createResource(eLodPrefix + "CollectiveBodyType");
		collectiveKindResource = ResourceFactory.createResource(eLodPrefix + "CollectiveBodyKind");
		vacancyTypeResource = ResourceFactory.createResource(eLodPrefix + "VacancyType");
		adminChangeResource = ResourceFactory.createResource(eLodPrefix + "OfficialAdministrativeChange");
		
		//FOAF
		organizationResource = ResourceFactory.createResource(foafPrefix + "Organization");
		personResource = ResourceFactory.createResource(foafPrefix + "Person");
		agentResource = ResourceFactory.createResource(foafPrefix + "Agent");
		//SKOS
		conceptResource = ResourceFactory.createResource(skosPrefix + "Concept");
		conceptSchemeResource = ResourceFactory.createResource(skosPrefix + "ConceptScheme");
		//GR
		businessEntityResource = ResourceFactory.createResource(goodRelationsPrefix + "BusinessEntity");
		unitPriceSpecificationResource = ResourceFactory.createResource(goodRelationsPrefix + "UnitPriceSpecification");
		//RegOrg
		registeredOrganizationResource = ResourceFactory.createResource(regOrgPrefix + "RegisteredOrganization");
		//pc
		contractResource = ResourceFactory.createResource(publicContractsPrefix + "Contract");
		//org
		orgOrganizationResource = ResourceFactory.createResource(orgPrefix + "Organization");
		organizationalUnitResource = ResourceFactory.createResource(orgPrefix + "OrganizationalUnit");
		roleResource = ResourceFactory.createResource(orgPrefix + "Role");
		membershipResource = ResourceFactory.createResource(orgPrefix + "Membership");
		//vCard
		addressResource = ResourceFactory.createResource(vcardPrefix + "Address");
		//elodGeo
		/*countryGeoResource = ResourceFactory.createResource(elodGeoPrefix + "Country");
		postalCodeAreaResource = ResourceFactory.createResource(elodGeoPrefix + "PostalCodeArea");*/
		
		/** Object Properties **/
		//eLod
		signer = ResourceFactory.createProperty(eLodPrefix + "signer");
		hasOrgUnitCategory = ResourceFactory.createProperty(eLodPrefix + "hasOrganizationalUnitCategory");
		hasSupervisorOrganization = ResourceFactory.createProperty(eLodPrefix + "hasSupervisorOrganization");
		relatedFek = ResourceFactory.createProperty(eLodPrefix + "relatedFek");
		fekIssue = ResourceFactory.createProperty(eLodPrefix + "fekIssue");
		hasKae = ResourceFactory.createProperty(eLodPrefix + "hasKae");
		hasCpv = ResourceFactory.createProperty(eLodPrefix + "hasCpv");
		hasThematicCategory = ResourceFactory.createProperty(eLodPrefix + "hasThematicCategory");
		hasVatType = ResourceFactory.createProperty(eLodPrefix + "hasVatType");
		seller = ResourceFactory.createProperty(eLodPrefix + "seller");
		buyer = ResourceFactory.createProperty(eLodPrefix + "buyer");
		price = ResourceFactory.createProperty(eLodPrefix + "price");
		amount = ResourceFactory.createProperty(eLodPrefix + "amount");
		decisionStatus = ResourceFactory.createProperty(eLodPrefix + "decisionStatus");
		hasBudgetCategory = ResourceFactory.createProperty(eLodPrefix + "hasBudgetCategory");
		hasExpenditureLine = ResourceFactory.createProperty(eLodPrefix + "hasExpenditureLine");
		hasAttachment = ResourceFactory.createProperty(eLodPrefix + "hasAttachment");
		hasRelatedCommittedItem = ResourceFactory.createProperty(eLodPrefix + "hasRelatedCommittedItem");
		hasRelatedExpenseApprovalItem = ResourceFactory.createProperty(eLodPrefix + "hasRelatedExpenseApprovalItem");
		hasRelatedContract = ResourceFactory.createProperty(eLodPrefix + "hasRelatedContract");
		hasRelatedDecision = ResourceFactory.createProperty(eLodPrefix + "hasRelatedDecision");
		hasRelatedSpendingItem = ResourceFactory.createProperty(eLodPrefix + "hasRelatedSpendingItem");
		hasCorrectedDecision = ResourceFactory.createProperty(eLodPrefix + "hasCorrectedDecision");
		hasCurrency = ResourceFactory.createProperty(eLodPrefix + "hasCurrency");
		isRegisteredAt = ResourceFactory.createProperty(eLodPrefix + "isRegisteredAt");
		remainingBudgetAmount = ResourceFactory.createProperty(eLodPrefix + "remainingBudgetAmount");
		remainingCreditAmount = ResourceFactory.createProperty(eLodPrefix + "remainingCreditAmount");
		relatedLaw = ResourceFactory.createProperty(eLodPrefix + "relatedLaw");
		budgetRefersTo = ResourceFactory.createProperty(eLodPrefix + "budgetRefersTo");
		accountRefersTo = ResourceFactory.createProperty(eLodPrefix + "accountRefersTo");
		employerOrg = ResourceFactory.createProperty(eLodPrefix + "employerOrg");
		donationGiver = ResourceFactory.createProperty(eLodPrefix + "donationGiver");
		donationReceiver = ResourceFactory.createProperty(eLodPrefix + "donationReceiver");
		assetGrantor = ResourceFactory.createProperty(eLodPrefix + "assetGrantor");
		assetGrantee = ResourceFactory.createProperty(eLodPrefix + "assetGrantee");
		regulatoryAct = ResourceFactory.createProperty(eLodPrefix + "regulatoryAct");
		hasOpinionOrgType = ResourceFactory.createProperty(eLodPrefix + "hasOpinionOrgType");
		hasBudgetKind = ResourceFactory.createProperty(eLodPrefix + "hasBudgetKind");
		hasRelatedAdministrativeDecision = ResourceFactory.createProperty(eLodPrefix + "hasRelatedAdministrativeDecision");
		accountType = ResourceFactory.createProperty(eLodPrefix + "accountType");
		timePeriod = ResourceFactory.createProperty(eLodPrefix + "timePeriod");
		hasPositionType = ResourceFactory.createProperty(eLodPrefix + "hasPositionType");
		collectiveBodyKind = ResourceFactory.createProperty(eLodPrefix + "collectiveBodyKind");
		collectiveBodyType = ResourceFactory.createProperty(eLodPrefix + "collectiveBodyType");
		hasVacancyType = ResourceFactory.createProperty(eLodPrefix + "hasVacancyType");
		competentMinistry = ResourceFactory.createProperty(eLodPrefix + "competentMinistry");
		competentUnit = ResourceFactory.createProperty(eLodPrefix + "competentUnit");
		hasOfficialAdministrativeChange = ResourceFactory.createProperty(eLodPrefix + "hasOfficialAdministrativeChange");
		//SKOS
		hasTopConcept = ResourceFactory.createProperty(skosPrefix + "hasTopConcept");
		topConceptOf = ResourceFactory.createProperty(skosPrefix + "topConceptOf");
		inScheme = ResourceFactory.createProperty(skosPrefix + "inScheme");
		narrower = ResourceFactory.createProperty(skosPrefix + "narrower");
		broader = ResourceFactory.createProperty(skosPrefix + "broader");
		prefLabel = ResourceFactory.createProperty(skosPrefix + "prefLabel");
		//dc
		publisher = ResourceFactory.createProperty(dctermsPrefix + "publisher");
		//pc
		procedureType = ResourceFactory.createProperty(publicContractsPrefix + "procedureType");
		kind = ResourceFactory.createProperty(publicContractsPrefix + "kind");
		awardCriteriaCombinationResource = ResourceFactory.createResource(publicContractsPrefix + "AwardCriteriaCombination");
		criterionWeightingResource = ResourceFactory.createResource(publicContractsPrefix + "CriterionWeighting");
		weightedCriterion = ResourceFactory.createProperty(publicContractsPrefix + "weightedCriterion");
		criterionWeight = ResourceFactory.createProperty(publicContractsPrefix + "criterionWeight");
		awardCriteriaCombination = ResourceFactory.createProperty(publicContractsPrefix + "awardCriteriaCombination");
		awardCriterion = ResourceFactory.createProperty(publicContractsPrefix + "awardCriterion");
		mainObject = ResourceFactory.createProperty(publicContractsPrefix + "mainObject");
		additionalObject = ResourceFactory.createProperty(publicContractsPrefix + "additionalObject");
		agreedPrice = ResourceFactory.createProperty(publicContractsPrefix + "agreedPrice");
		documentsPrice = ResourceFactory.createProperty(publicContractsPrefix + "documentsPrice");
		actualPrice = ResourceFactory.createProperty(publicContractsPrefix + "actualPrice");
		//rov
		orgStatus = ResourceFactory.createProperty(regOrgPrefix + "orgStatus");
		orgCategory = ResourceFactory.createProperty(regOrgPrefix + "orgCategory");
		orgActivity = ResourceFactory.createProperty(regOrgPrefix + "orgActivity");
		hasUnit = ResourceFactory.createProperty(regOrgPrefix + "hasUnit");
		unitOf = ResourceFactory.createProperty(regOrgPrefix + "unitOf");
		//org
		role = ResourceFactory.createProperty(orgPrefix + "role");
		hasMember = ResourceFactory.createProperty(orgPrefix + "hasMember");
		member = ResourceFactory.createProperty(orgPrefix + "member");
		organization = ResourceFactory.createProperty(orgPrefix + "organization");
		//vCard
		hasAddress = ResourceFactory.createProperty(vcardPrefix + "hasAddress");
		//elodGeo
		postalCodeArea = ResourceFactory.createProperty(elodGeoPrefix + "postalCodeArea");
		//former psnet
		cpa = ResourceFactory.createProperty(eLodPrefix + "cpa");
		
		/** Data Properties **/
		//eLod
		signerId = ResourceFactory.createProperty(eLodPrefix + "signerId");
		submissionTimestamp = ResourceFactory.createProperty(eLodPrefix + "submissionTimestamp");
		protocolNumber = ResourceFactory.createProperty(eLodPrefix + "protocolNumber");
		ada = ResourceFactory.createProperty(eLodPrefix + "ada");
		organizationId = ResourceFactory.createProperty(eLodPrefix + "organizationId");
		organizationAbbreviation = ResourceFactory.createProperty(eLodPrefix + "organizationAbbreviation");
		odeManagerEmail = ResourceFactory.createProperty(eLodPrefix + "odeManagerEmail");
		organizationWebsite = ResourceFactory.createProperty(eLodPrefix + "organizationWebsite");
		organizationUnitId = ResourceFactory.createProperty(eLodPrefix + "organizationUnitId");
		organizationUnitAbbreviation = ResourceFactory.createProperty(eLodPrefix + "organizationUnitAbbreviation");
		decisionTypeId = ResourceFactory.createProperty(eLodPrefix + "decisionTypeId");
		decisionType = ResourceFactory.createProperty(eLodPrefix + "decisionType");
		versionId = ResourceFactory.createProperty(eLodPrefix + "versionId");
		correctedVersionId = ResourceFactory.createProperty(eLodPrefix + "correctedVersionId");
		url = ResourceFactory.createProperty(eLodPrefix + "url");
		documentUrl = ResourceFactory.createProperty(eLodPrefix + "documentUrl");
		documentChecksum = ResourceFactory.createProperty(eLodPrefix + "documentChecksum");
		privateData = ResourceFactory.createProperty(eLodPrefix + "privateData");
		attachmentId = ResourceFactory.createProperty(eLodPrefix + "attachmentId");
		attachmentDescription = ResourceFactory.createProperty(eLodPrefix + "attachmentDescription");
		attachmentFilename = ResourceFactory.createProperty(eLodPrefix + "attachmentFilename");
		attachmentMimeType = ResourceFactory.createProperty(eLodPrefix + "attachmentMimeType");
		attachmentChecksum = ResourceFactory.createProperty(eLodPrefix + "attachmentChecksum");
		attachmentSize = ResourceFactory.createProperty(eLodPrefix + "attachmentSize");
		fekNumber = ResourceFactory.createProperty(eLodPrefix + "fekNumber");
		fekYear = ResourceFactory.createProperty(eLodPrefix + "fekYear");
		fekTypeId = ResourceFactory.createProperty(eLodPrefix + "fekTypeId");
		currencyId = ResourceFactory.createProperty(eLodPrefix + "currencyId");
		countryId = ResourceFactory.createProperty(eLodPrefix + "countryId");
		documentType = ResourceFactory.createProperty(eLodPrefix + "documentType");
		entryNumber = ResourceFactory.createProperty(eLodPrefix + "entryNumber");
		financialYear = ResourceFactory.createProperty(eLodPrefix + "financialYear");
		isRecalledExpenseDecision = ResourceFactory.createProperty(eLodPrefix + "isRecalledExpenseDecision");
		kae = ResourceFactory.createProperty(eLodPrefix + "kae");
		isPartialWithdrawal = ResourceFactory.createProperty(eLodPrefix + "isPartialWithdrawal");
		noVatOrgId = ResourceFactory.createProperty(eLodPrefix + "noVatOrgId");
		signerOrgSignRights = ResourceFactory.createProperty(eLodPrefix + "hasOrganizationSignRights");
		signerActive = ResourceFactory.createProperty(eLodPrefix + "signerActive");
		validVatId = ResourceFactory.createProperty(eLodPrefix + "validVatId");
		instruction = ResourceFactory.createProperty(eLodPrefix + "instruction");
		proceedingNumber = ResourceFactory.createProperty(eLodPrefix + "proceedingNumber");
		numberOfPeople = ResourceFactory.createProperty(eLodPrefix + "numberOfPeople");
		duration = ResourceFactory.createProperty(eLodPrefix + "duration");
		europeanLaw = ResourceFactory.createProperty(eLodPrefix + "europeanLaw");
		interCountryLaw = ResourceFactory.createProperty(eLodPrefix + "interCountryLaw");
		passAuthority = ResourceFactory.createProperty(eLodPrefix + "passAuthority");
		decisionNumber = ResourceFactory.createProperty(eLodPrefix + "decisionNumber");
		regulatoryActNumber = ResourceFactory.createProperty(eLodPrefix + "regulatoryActNumber");
		budgetApprovalForOrg = ResourceFactory.createProperty(eLodPrefix + "budgetApprovalForOrg");
		primaryOfficer = ResourceFactory.createProperty(eLodPrefix + "primaryOfficer");
		secondaryOfficer = ResourceFactory.createProperty(eLodPrefix + "secondaryOfficer");
		accountApprovalForOtherOrg = ResourceFactory.createProperty(eLodPrefix + "accountApprovalForOtherOrg");
		asset = ResourceFactory.createProperty(eLodPrefix + "asset");
		cofinancedProject = ResourceFactory.createProperty(eLodPrefix + "coFinancedProject");
		lawYear = ResourceFactory.createProperty(eLodPrefix + "lawYear");
		lawNumber = ResourceFactory.createProperty(eLodPrefix + "lawNumber");
		refersTo = ResourceFactory.createProperty(eLodPrefix + "refersTo");
		uuid = ResourceFactory.createProperty(muPrefix + "uuid");
		//GR
		vatId = ResourceFactory.createProperty(goodRelationsPrefix + "vatID");
		valueAddedTaxIncluded = ResourceFactory.createProperty(goodRelationsPrefix + "valueAddedTaxIncluded");
		hasCurrencyValue = ResourceFactory.createProperty(goodRelationsPrefix + "hasCurrencyValue");
		name = ResourceFactory.createProperty(goodRelationsPrefix + "name");
		legalName = ResourceFactory.createProperty(goodRelationsPrefix + "legalName");
		//FOAF
		firstName = ResourceFactory.createProperty(foafPrefix + "firstName");
		lastName = ResourceFactory.createProperty(foafPrefix + "lastName");
		//dc
		subject = ResourceFactory.createProperty(dctermsPrefix + "subject");
		//dcTerms
		issued = ResourceFactory.createProperty(dctermsPrefix + "issued");
		//vCard
		streetAddress = ResourceFactory.createProperty(vcardPrefix + "street-address"); 
		locality = ResourceFactory.createProperty(vcardPrefix + "locality");
		countryName = ResourceFactory.createProperty(vcardPrefix + "country-name"); 
		postalCode = ResourceFactory.createProperty(vcardPrefix + "postal-code");
		//former psnet
		phoneNumber = ResourceFactory.createProperty(eLodPrefix + "phoneNumber"); 
		faxNumber = ResourceFactory.createProperty(eLodPrefix + "faxNumber");
		countOfBranches = ResourceFactory.createProperty(eLodPrefix + "countOfBranches"); 
		stopDate = ResourceFactory.createProperty(eLodPrefix + "stopDate");
		registrationDate = ResourceFactory.createProperty(eLodPrefix + "registrationDate");
		fpFlag = ResourceFactory.createProperty(eLodPrefix + "fpFlag");
		legalStatus = ResourceFactory.createProperty(eLodPrefix + "legalStatus");
		doyName = ResourceFactory.createProperty(eLodPrefix + "doyName");
		doy = ResourceFactory.createProperty(eLodPrefix + "doy");
		firmDescription = ResourceFactory.createProperty(eLodPrefix + "firmDescription");
		profitStatus = ResourceFactory.createProperty(eLodPrefix + "profitStatus");
		category = ResourceFactory.createProperty(eLodPrefix + "category");
		abbreviationASE = ResourceFactory.createProperty(eLodPrefix + "abbreviationASE");
		abbreviation = ResourceFactory.createProperty(eLodPrefix + "abbreviation");
		translation = ResourceFactory.createProperty(eLodPrefix + "translation");
		transliterationLegalName = ResourceFactory.createProperty(eLodPrefix + "transliterationLegalName");		
	}
	
}