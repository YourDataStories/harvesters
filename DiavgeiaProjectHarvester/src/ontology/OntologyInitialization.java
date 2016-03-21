package ontology;

import utils.HelperMethods;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author G. Razis
 */
public class OntologyInitialization {

	HelperMethods hm = new HelperMethods();

	/**
	 * Add the necessary prefixes to the model we are currently working with.
	 * 
	 * @param Model
	 *            the model we are currently working with
	 */
	public void setPrefixes(Model model) {
		model.setNsPrefix("elod", Ontology.eLodPrefix);
		model.setNsPrefix("elodGeo", Ontology.elodGeoPrefix);
		model.setNsPrefix("pc", Ontology.publicContractsPrefix);
		model.setNsPrefix("skos", Ontology.skosPrefix);
		model.setNsPrefix("gr", Ontology.goodRelationsPrefix);
		model.setNsPrefix("rov", Ontology.regOrgPrefix);
		model.setNsPrefix("org", Ontology.orgPrefix);
		model.setNsPrefix("foaf", Ontology.foafPrefix);
		model.setNsPrefix("xsd", Ontology.xsdPrefix);
		model.setNsPrefix("dcterms", Ontology.dctermsPrefix);
		model.setNsPrefix("dc", Ontology.dcPrefix);
		model.setNsPrefix("pcdt", Ontology.pcdtPrefix);
		model.setNsPrefix("vcard", Ontology.vcardPrefix);
	}

	/**
	 * Create the basic hierarchies of the OWL classes and their labels to the
	 * model we are currently working with.
	 * 
	 * @param Model
	 *            the model we are currently working with
	 */
	public void createHierarchies(Model model) {

		// Agent
		model.add(Ontology.agentResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.agentResource, RDFS.label, model.createLiteral("Agent", "en"));
		model.add(Ontology.agentResource, RDFS.label, model.createLiteral("Πράκτορας", "el"));

		// Concept
		model.add(Ontology.conceptResource, RDFS.subClassOf, OWL.Thing);

		// Concept Scheme
		model.add(Ontology.conceptSchemeResource, RDFS.subClassOf, OWL.Thing);

		// Concept - Vat Type
		model.add(Ontology.vatTypeResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.vatTypeResource, RDFS.label, model.createLiteral("VAT Type", "en"));
		model.add(Ontology.vatTypeResource, RDFS.label, model.createLiteral("Τύπος ΑΦΜ", "el"));

		// Concept - Thematic Category
		model.add(Ontology.thematicCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.thematicCategoryResource, RDFS.label, model.createLiteral("Thematic Category Type", "en"));
		model.add(Ontology.thematicCategoryResource, RDFS.label,
				model.createLiteral("Τύπος Θεματικής Κατηγορίας", "el"));

		// Concept - Role
		model.add(Ontology.roleResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.roleResource, RDFS.label, model.createLiteral("Role", "en"));
		model.add(Ontology.roleResource, RDFS.label, model.createLiteral("Ρόλος", "el"));

		// Concept - Country
		model.add(Ontology.countryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.countryResource, RDFS.label, model.createLiteral("Country", "en"));
		model.add(Ontology.countryResource, RDFS.label, model.createLiteral("Χώρα", "el"));

		// Concept - Currency
		model.add(Ontology.currencyResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.currencyResource, RDFS.label, model.createLiteral("Currency", "en"));
		model.add(Ontology.currencyResource, RDFS.label, model.createLiteral("Νόμισμα", "el"));

		// Concept - Organizational Unit Category
		model.add(Ontology.orgUnitCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.orgUnitCategoryResource, RDFS.label,
				model.createLiteral("Organizational Unit Category", "en"));
		model.add(Ontology.orgUnitCategoryResource, RDFS.label, model.createLiteral("Τύποι Οργανωτικών Μονάδων", "el"));

		// Concept - Organization Domain
		model.add(Ontology.organizationDomainResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.organizationDomainResource, RDFS.label, model.createLiteral("Organization Domain", "en"));
		model.add(Ontology.organizationDomainResource, RDFS.label,
				model.createLiteral("Πεδίο Αρμοδιότητας Φορέων", "el"));

		// Concept - Organization Status
		model.add(Ontology.organizationStatusResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.organizationStatusResource, RDFS.label, model.createLiteral("Organization Status", "en"));
		model.add(Ontology.organizationStatusResource, RDFS.label, model.createLiteral("Κατάσταση Οργανισμού", "el"));

		// Concept - Organization Category
		model.add(Ontology.organizationCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.organizationCategoryResource, RDFS.label,
				model.createLiteral("Organization Category", "en"));
		model.add(Ontology.organizationCategoryResource, RDFS.label, model.createLiteral("Κατηγορίες Φορέων", "el"));

		// Concept - Decision Status
		model.add(Ontology.decisionStatusResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.decisionStatusResource, RDFS.label, model.createLiteral("Decision Status", "en"));
		model.add(Ontology.decisionStatusResource, RDFS.label, model.createLiteral("Κατάσταση Πράξης", "el"));

		// Concept - Selection Criterion
		model.add(Ontology.selectionCriterionResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.selectionCriterionResource, RDFS.label, model.createLiteral("Selection Criterion", "en"));
		model.add(Ontology.selectionCriterionResource, RDFS.label, model.createLiteral("Κριτήριο Επιλογής", "el"));

		// Concept - Budget Category
		model.add(Ontology.budgetCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.budgetCategoryResource, RDFS.label, model.createLiteral("Budget Category", "en"));
		model.add(Ontology.budgetCategoryResource, RDFS.label, model.createLiteral("Κατηγορία Προϋπολογισμού", "el"));

		// Fek Type
		model.add(Ontology.fekTypeResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.fekTypeResource, RDFS.label,
				model.createLiteral("Greek Government Gazzette Issue Type ", "en"));
		model.add(Ontology.fekTypeResource, RDFS.label,
				model.createLiteral("Τύπος Τεύχους Φύλλου Εφημερίδος Κυβερνήσεως", "el"));

		// Agent - Business Entity
		model.add(Ontology.businessEntityResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.businessEntityResource, RDFS.label, model.createLiteral("Business Entity", "en"));
		model.add(Ontology.businessEntityResource, RDFS.label, model.createLiteral("Επιχειρηματική Οντότητα", "el"));

		// Agent - Registered Organization
		model.add(Ontology.registeredOrganizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.registeredOrganizationResource, RDFS.label,
				model.createLiteral("Registered Organization", "en"));
		model.add(Ontology.registeredOrganizationResource, RDFS.label,
				model.createLiteral("Καταχωρημένος Οργανισμός", "el"));

		// Agent - Organization (FOAF)
		model.add(Ontology.organizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Organization", "en"));
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Οργανισμός", "el"));

		// Agent - Organization (org)
		model.add(Ontology.orgOrganizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.orgOrganizationResource, RDFS.label, model.createLiteral("Organization", "en"));
		model.add(Ontology.orgOrganizationResource, RDFS.label, model.createLiteral("Οργανισμός", "el"));

		// Orgs equivalence
		/*
		 * model.add(Ontology.businessEntityResource, OWL.equivalentClass,
		 * Ontology.registeredOrganizationResource);
		 * model.add(Ontology.businessEntityResource, OWL.equivalentClass,
		 * Ontology.organizationResource);
		 * model.add(Ontology.businessEntityResource, OWL.equivalentClass,
		 * Ontology.orgOrganizationResource);
		 * model.add(Ontology.registeredOrganizationResource,
		 * OWL.equivalentClass, Ontology.organizationResource);
		 * model.add(Ontology.registeredOrganizationResource,
		 * OWL.equivalentClass, Ontology.orgOrganizationResource);
		 * model.add(Ontology.organizationResource, OWL.equivalentClass,
		 * Ontology.orgOrganizationResource);
		 */

		// Agent - Person
		model.add(Ontology.personResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.personResource, RDFS.label, model.createLiteral("Person", "en"));
		model.add(Ontology.personResource, RDFS.label, model.createLiteral("Πρόσωπο", "el"));

		// Agent - Organization
		model.add(Ontology.organizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Organization", "en"));
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Οργανισμός", "el"));

		// Agent - Organizational Unit
		model.add(Ontology.organizationalUnitResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.organizationalUnitResource, RDFS.label, model.createLiteral("Organizational Unit", "en"));
		model.add(Ontology.organizationalUnitResource, RDFS.label, model.createLiteral("Μονάδα Οργανισμού", "el"));

		// Spending Item
		model.add(Ontology.spendingItemResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.spendingItemResource, RDFS.label, model.createLiteral("Spending Item", "en"));
		model.add(Ontology.spendingItemResource, RDFS.label, model.createLiteral("Αντικείμενο Δαπάνης", "el"));

		// Award Criteria Combination
		model.add(Ontology.awardCriteriaCombinationResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.awardCriteriaCombinationResource, RDFS.label,
				model.createLiteral("Combination of Contract Award Criteria", "en"));
		model.add(Ontology.awardCriteriaCombinationResource, RDFS.label,
				model.createLiteral("Συνδυασμός των Κριτηρίων Ανάθεσης της Σύμβασης", "el"));

		// Criterion Weighting
		model.add(Ontology.criterionWeightingResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.criterionWeightingResource, RDFS.label, model.createLiteral("Award Criterion", "en"));
		model.add(Ontology.criterionWeightingResource, RDFS.label, model.createLiteral("Κριτήριο Ανάθεσης", "el"));

		// Contract
		model.add(Ontology.contractResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.contractResource, RDFS.label, model.createLiteral("Public Contract", "en"));
		model.add(Ontology.contractResource, RDFS.label, model.createLiteral("Δημόσια Σύμβαση", "el"));

		// CPV
		model.add(Ontology.cpvResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.cpvResource, RDFS.label, model.createLiteral("Common Procurement Vocabulary", "en"));
		model.add(Ontology.cpvResource, RDFS.label, model.createLiteral("Κοινό Λεξιλόγιο Προμηθειών", "el"));

		// Attachment
		model.add(Ontology.attachmentResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.attachmentResource, RDFS.label, model.createLiteral("Attachments", "en"));
		model.add(Ontology.attachmentResource, RDFS.label, model.createLiteral("Συνημμένα Έγγραφα", "el"));

		// Decision
		model.add(Ontology.decisionResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.decisionResource, RDFS.label, model.createLiteral("Decision", "en"));
		model.add(Ontology.decisionResource, RDFS.label, model.createLiteral("Πράξη", "el"));

		// Committed Amount
		model.add(Ontology.committedItemResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.committedItemResource, RDFS.label, model.createLiteral("Committed Item", "en"));
		model.add(Ontology.committedItemResource, RDFS.label, model.createLiteral("Δεσμευθέν Αντικείμενο", "el"));

		// Expenditure Line
		model.add(Ontology.expenditureLineResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.expenditureLineResource, RDFS.label, model.createLiteral("Expenditure Line", "en"));
		model.add(Ontology.expenditureLineResource, RDFS.label, model.createLiteral("Τμήμα Δαπάνης", "el"));

		// Expense Approval Item
		model.add(Ontology.expenseApprovalItemResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.expenseApprovalItemResource, RDFS.label, model.createLiteral("Expense Approval", "en"));
		model.add(Ontology.expenseApprovalItemResource, RDFS.label, model.createLiteral("Έγκριση Δαπάνης", "el"));

		// FEK
		model.add(Ontology.fekResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.fekResource, RDFS.label, model.createLiteral("Greek Government Gazzette", "en"));
		model.add(Ontology.fekResource, RDFS.label, model.createLiteral("Φύλλο Εφημερίδος Κυβερνήσεως", "el"));

		// KAE
		model.add(Ontology.kaeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.kaeResource, RDFS.label, model.createLiteral("Code Number of Revenues/Expenses", "en"));
		model.add(Ontology.kaeResource, RDFS.label, model.createLiteral("Κωδικός Αριθμού Εσόδων/Εξόδων", "el"));

		// Unit Price Specification
		model.add(Ontology.unitPriceSpecificationResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.unitPriceSpecificationResource, RDFS.label,
				model.createLiteral("Unit Price Specification", "en"));
		model.add(Ontology.unitPriceSpecificationResource, RDFS.label,
				model.createLiteral("Προδιαγραφή τιμής ανά μονάδα", "el"));

		// Membership
		model.add(Ontology.membershipResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.membershipResource, RDFS.label, model.createLiteral("Membership", "en"));
		model.add(Ontology.membershipResource, RDFS.label, model.createLiteral("Μέλος", "el"));

		// Address
		model.add(Ontology.addressResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.addressResource, RDFS.label, model.createLiteral("Adress", "en"));
		model.add(Ontology.addressResource, RDFS.label, model.createLiteral("Διεύθυνση", "el"));

		// Regulatory Act
		model.add(Ontology.regulatoryActResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.regulatoryActResource, RDFS.label, model.createLiteral("Regulatory Act", "en"));
		model.add(Ontology.regulatoryActResource, RDFS.label, model.createLiteral("Κανονιστική Πράξη", "el"));

		// Position Type
		model.add(Ontology.positionResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.positionResource, RDFS.label, model.createLiteral("Position Type", "en"));
		model.add(Ontology.positionResource, RDFS.label, model.createLiteral("Τύπος Θέσης", "el"));

		// Collective Body Type
		model.add(Ontology.collectiveTypeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.collectiveTypeResource, RDFS.label, model.createLiteral("Collective Body Type", "en"));
		model.add(Ontology.collectiveTypeResource, RDFS.label, model.createLiteral("Τύπος Συλλογικού Οργάνου", "el"));

		// Collective Body Kind
		model.add(Ontology.collectiveKindResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.collectiveKindResource, RDFS.label, model.createLiteral("Είδος Συλλογικού Οργάνου", "en"));
		model.add(Ontology.collectiveKindResource, RDFS.label, model.createLiteral("", "el"));

		// Opinion Org Type
		model.add(Ontology.opinionResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.opinionResource, RDFS.label, model.createLiteral("Opinion Organization Type", "en"));
		model.add(Ontology.opinionResource, RDFS.label, model.createLiteral("Τύπος Οργανισμού Γνωμοδότησης", "el"));

		// Budget Kind
		model.add(Ontology.budgetKindResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.budgetKindResource, RDFS.label, model.createLiteral("Budget Kind", "en"));
		model.add(Ontology.budgetKindResource, RDFS.label, model.createLiteral("Είδος Προϋπολογισμού", "el"));

		// Account Type
		model.add(Ontology.accountResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.accountResource, RDFS.label, model.createLiteral("Account Type", "en"));
		model.add(Ontology.accountResource, RDFS.label, model.createLiteral("Τύπος Λογαριασμού", "el"));

		// Time Period
		model.add(Ontology.timeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.timeResource, RDFS.label, model.createLiteral("Time Period", "en"));
		model.add(Ontology.timeResource, RDFS.label, model.createLiteral("Χρονική Περίοδος", "el"));

		// Vacancy Type
		model.add(Ontology.vacancyTypeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.vacancyTypeResource, RDFS.label, model.createLiteral("Vacancy Type", "en"));
		model.add(Ontology.vacancyTypeResource, RDFS.label, model.createLiteral("Είδος Κενής Θέσης", "el"));

		// Official Administrative Change
		model.add(Ontology.adminChangeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.adminChangeResource, RDFS.label,
				model.createLiteral("Official Administrative Change", "en"));
		model.add(Ontology.adminChangeResource, RDFS.label, model.createLiteral("Επίσημη Διοικητική Αλλαγή", "el"));

		// -------------------------------------------Overall Specification of
		// OWL file-------------------------------------------------//

		// Subclasses of OWL.Thing
		model.add(Ontology.addressResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.attachmentResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.cpvResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.kaeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.expenditureLineResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.fekResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.unitPriceSpecificationResource, RDFS.subClassOf, OWL.Thing);

		// Object Properties Domain and Ranges

		// mainObject
		model.add(Ontology.mainObject, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.mainObject, RDFS.range, Ontology.cpvResource);
		// hasAttachment
		model.add(Ontology.hasAttachment, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasAttachment, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.hasAttachment, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.hasAttachment, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasAttachment, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.hasAttachment, RDFS.range, Ontology.attachmentResource);
		// assetGrantee
		model.add(Ontology.assetGrantee, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.assetGrantee, RDFS.range, Ontology.agentResource);
		// budgetRefersTo
		model.add(Ontology.budgetRefersTo, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.budgetRefersTo, RDFS.range, Ontology.organizationResource);
		model.add(Ontology.budgetRefersTo, RDFS.range, Ontology.businessEntityResource);
		model.add(Ontology.budgetRefersTo, RDFS.range, Ontology.registeredOrganizationResource);
		// accountRefersTo
		model.add(Ontology.accountRefersTo, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.accountRefersTo, RDFS.range, Ontology.organizationResource);
		model.add(Ontology.accountRefersTo, RDFS.range, Ontology.businessEntityResource);
		model.add(Ontology.accountRefersTo, RDFS.range, Ontology.registeredOrganizationResource);
		// accountType
		model.add(Ontology.accountType, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.accountType, RDFS.range, Ontology.accountResource);
		// agreedPrice
		model.add(Ontology.agreedPrice, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.agreedPrice, RDFS.range, Ontology.unitPriceSpecificationResource);
		// amount
		model.add(Ontology.amount, RDFS.domain, Ontology.expenditureLineResource);
		model.add(Ontology.amount, RDFS.range, Ontology.unitPriceSpecificationResource);
		// assetGrantor
		model.add(Ontology.assetGrantor, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.assetGrantor, RDFS.range, Ontology.organizationResource);
		model.add(Ontology.assetGrantor, RDFS.range, Ontology.businessEntityResource);
		model.add(Ontology.assetGrantor, RDFS.range, Ontology.registeredOrganizationResource);
		model.add(Ontology.assetGrantor, RDFS.range, Ontology.personResource);
		// broader
		model.add(Ontology.broader, RDFS.domain, Ontology.conceptResource);
		model.add(Ontology.broader, RDFS.range, Ontology.conceptResource);
		model.add(Ontology.broader, OWL.inverseOf, Ontology.narrower);
		// buyer
		model.add(Ontology.buyer, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.buyer, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.buyer, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.buyer, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.buyer, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.buyer, RDFS.range, Ontology.agentResource);
		// collectiveBodyKind
		model.add(Ontology.collectiveBodyKind, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.collectiveBodyKind, RDFS.range, Ontology.collectiveKindResource);
		// collectiveBodyType
		model.add(Ontology.collectiveBodyType, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.collectiveBodyType, RDFS.range, Ontology.collectiveTypeResource);
		// competentMinistry
		model.add(Ontology.competentMinistry, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.competentMinistry, RDFS.range, Ontology.organizationResource);
		model.add(Ontology.competentMinistry, RDFS.range, Ontology.businessEntityResource);
		model.add(Ontology.competentMinistry, RDFS.range, Ontology.registeredOrganizationResource);
		model.add(Ontology.competentMinistry, RDFS.range, Ontology.organizationalUnitResource);
		// competentUnit
		model.add(Ontology.competentUnit, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.competentUnit, RDFS.range, Ontology.organizationalUnitResource);
		// decisionStatus
		model.add(Ontology.decisionStatus, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.decisionStatus, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.decisionStatus, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.decisionStatus, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.decisionStatus, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.decisionStatus, RDFS.range, Ontology.decisionStatusResource);
		// documentsPrice
		model.add(Ontology.documentsPrice, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.documentsPrice, RDFS.range, Ontology.unitPriceSpecificationResource);
		// donationGiver
		model.add(Ontology.donationGiver, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.donationGiver, RDFS.range, Ontology.agentResource);
		// donationReceiver
		model.add(Ontology.donationReceiver, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.donationReceiver, RDFS.range, Ontology.agentResource);
		// employerOrg
		model.add(Ontology.employerOrg, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.employerOrg, RDFS.range, Ontology.organizationResource);
		model.add(Ontology.employerOrg, RDFS.range, Ontology.businessEntityResource);
		model.add(Ontology.employerOrg, RDFS.range, Ontology.registeredOrganizationResource);
		// fekIssue
		model.add(Ontology.fekIssue, RDFS.domain, Ontology.fekResource);
		model.add(Ontology.fekIssue, RDFS.range, Ontology.fekTypeResource);
		// hasAddress
		model.add(Ontology.hasAddress, RDFS.domain, Ontology.agentResource);
		model.add(Ontology.hasAddress, RDFS.range, Ontology.addressResource);
		// hasBudgetCategory
		model.add(Ontology.hasBudgetCategory, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.hasBudgetCategory, RDFS.range, Ontology.budgetCategoryResource);
		// hasBudgetKind
		model.add(Ontology.hasBudgetKind, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasBudgetKind, RDFS.range, Ontology.budgetKindResource);
		// hasExpenditureLine
		model.add(Ontology.hasExpenditureLine, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasExpenditureLine, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.hasExpenditureLine, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasExpenditureLine, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.hasExpenditureLine, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.hasExpenditureLine, RDFS.range, Ontology.expenditureLineResource);
		// hasCorrectedDecision
		model.add(Ontology.hasCorrectedDecision, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.range, Ontology.decisionResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.range, Ontology.committedItemResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.range, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.range, Ontology.spendingItemResource);
		model.add(Ontology.hasCorrectedDecision, RDFS.range, Ontology.contractResource);
		// hasCpv
		model.add(Ontology.hasCpv, RDFS.domain, Ontology.expenditureLineResource);
		model.add(Ontology.hasCpv, RDFS.range, Ontology.cpvResource);
		// hasCurrency
		model.add(Ontology.hasCurrency, RDFS.domain, Ontology.unitPriceSpecificationResource);
		model.add(Ontology.hasCurrency, RDFS.range, Ontology.currencyResource);
		// hasKae
		model.add(Ontology.hasKae, RDFS.domain, Ontology.expenditureLineResource);
		model.add(Ontology.hasKae, RDFS.range, Ontology.kaeResource);
		// hasOfficialAdministrativeChange
		model.add(Ontology.hasOfficialAdministrativeChange, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasOfficialAdministrativeChange, RDFS.range, Ontology.adminChangeResource);
		// hasOpinionOrgType
		model.add(Ontology.hasOpinionOrgType, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasOpinionOrgType, RDFS.range, Ontology.opinionResource);
//		// hasPositionType
//		model.add(Ontology.hasPositionType, RDFS.domain, Ontology.decisionResource);
//		model.add(Ontology.hasPositionType, RDFS.range, Ontology.positionResource);
		// hasRelatedAdministrativeDecision
		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.domain, Ontology.projectResource);
//		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.domain, Ontology.subprojectResource);
		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.range, Ontology.decisionResource);
		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.range, Ontology.committedItemResource);
		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.range, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.range, Ontology.spendingItemResource);
		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.range, Ontology.contractResource);
		// hasRelatedDecision
		model.add(Ontology.hasRelatedDecision, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasRelatedDecision, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.hasRelatedDecision, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasRelatedDecision, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.hasRelatedDecision, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.hasRelatedDecision, RDFS.range, Ontology.decisionResource);
		// hasThematicCategory
		model.add(Ontology.hasThematicCategory, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasThematicCategory, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.hasThematicCategory, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.hasThematicCategory, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.hasThematicCategory, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.hasThematicCategory, RDFS.range, Ontology.thematicCategoryResource);
		// hasTopConcept
		model.add(Ontology.hasTopConcept, RDFS.domain, Ontology.conceptSchemeResource);
		model.add(Ontology.hasTopConcept, RDFS.range, Ontology.conceptResource);
		// hasVacancyType
		model.add(Ontology.hasVacancyType, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.hasVacancyType, RDFS.range, Ontology.vacancyTypeResource);
		// hasVatType
		model.add(Ontology.hasVatType, RDFS.domain, Ontology.agentResource);
		model.add(Ontology.hasVatType, RDFS.range, Ontology.vatTypeResource);
		// inScheme
		model.add(Ontology.inScheme, RDFS.domain, Ontology.conceptResource);
		model.add(Ontology.inScheme, RDFS.range, Ontology.conceptSchemeResource);
		// topConceptOf
		model.add(Ontology.topConceptOf, RDFS.domain, Ontology.conceptResource);
		model.add(Ontology.topConceptOf, RDFS.range, Ontology.conceptSchemeResource);
		// isRegisteredAt
		model.add(Ontology.isRegisteredAt, RDFS.domain, Ontology.agentResource);
		model.add(Ontology.isRegisteredAt, RDFS.range, Ontology.addressResource);
		// kind
		model.add(Ontology.kind, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.kind, RDFS.range, Ontology.conceptResource);
		// narrower
		model.add(Ontology.narrower, RDFS.domain, Ontology.conceptResource);
		model.add(Ontology.narrower, RDFS.range, Ontology.conceptResource);
		// orgCategory
		model.add(Ontology.orgCategory, RDFS.domain, Ontology.organizationResource);
		model.add(Ontology.orgCategory, RDFS.domain, Ontology.businessEntityResource);
		model.add(Ontology.orgCategory, RDFS.domain, Ontology.registeredOrganizationResource);
		model.add(Ontology.orgCategory, RDFS.range, Ontology.organizationCategoryResource);
		// orgStatus
		model.add(Ontology.orgStatus, RDFS.domain, Ontology.organizationResource);
		model.add(Ontology.orgStatus, RDFS.domain, Ontology.businessEntityResource);
		model.add(Ontology.orgStatus, RDFS.domain, Ontology.registeredOrganizationResource);
		model.add(Ontology.orgStatus, RDFS.range, Ontology.organizationStatusResource);
//		// price
//		model.add(Ontology.price, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.price, RDFS.range, Ontology.Thing);
		// publisher
		model.add(Ontology.publisher, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.publisher, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.publisher, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.publisher, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.publisher, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.publisher, RDFS.range, Ontology.businessEntityResource);
		model.add(Ontology.publisher, RDFS.range, Ontology.registeredOrganizationResource);
		model.add(Ontology.publisher, RDFS.range, Ontology.organizationalUnitResource);
		model.add(Ontology.publisher, RDFS.range, Ontology.organizationStatusResource);
		// regulatoryAct
		model.add(Ontology.regulatoryAct, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.regulatoryAct, RDFS.range, Ontology.regulatoryAct);
		// relatedFek
		model.add(Ontology.relatedFek, RDFS.domain, Ontology.businessEntityResource);
		model.add(Ontology.relatedFek, RDFS.domain, Ontology.registeredOrganizationResource);
		model.add(Ontology.relatedFek, RDFS.domain, Ontology.organizationalUnitResource);
		model.add(Ontology.relatedFek, RDFS.domain, Ontology.organizationStatusResource);
		model.add(Ontology.relatedFek, RDFS.range, Ontology.fekResource);
//		// relatedLaw
//		model.add(Ontology.relatedLaw, RDFS.domain, Ontology.decisionResource);
//		model.add(Ontology.relatedLaw, RDFS.range, Ontology);
		// seller
		model.add(Ontology.seller, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.seller, RDFS.domain, Ontology.expenditureLineResource);
//		model.add(Ontology.seller, RDFS.domain, Ontology.subprojectResource);
		model.add(Ontology.seller, RDFS.range, Ontology.agentResource);
		// signer
		model.add(Ontology.signer, RDFS.domain, Ontology.decisionResource);
		model.add(Ontology.signer, RDFS.domain, Ontology.committedItemResource);
		model.add(Ontology.signer, RDFS.domain, Ontology.expenseApprovalItemResource);
		model.add(Ontology.signer, RDFS.domain, Ontology.contractResource);
		model.add(Ontology.signer, RDFS.domain, Ontology.spendingItemResource);
		model.add(Ontology.signer, RDFS.range, Ontology.personResource);
//		// timePeriod
//		model.add(Ontology.timePeriod, RDFS.domain, Ontology.decisionResource);
//		model.add(Ontology.timePeriod, RDFS.range, Ontology);

		// Datatype Properties Domain and Ranges

//		// mainObject
//		model.add(Ontology.mainObject, RDFS.domain, Ontology.countryResource);
//		model.add(Ontology.mainObject, RDFS.domain, xsdString);
//		// hasAttachment
//		model.add(Ontology.hasAttachment, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasAttachment, RDFS.range, Ontology.Thing);
//		// assetGrantee
//		model.add(Ontology.assetGrantee, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.assetGrantee, RDFS.range, Ontology.Thing);
//		// budgetRefersTo
//		model.add(Ontology.budgetRefersTo, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.budgetRefersTo, RDFS.range, Ontology.Thing);
//		// accountRefersTo
//		model.add(Ontology.accountRefersTo, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.accountRefersTo, RDFS.range, Ontology.Thing);
//		// accountType
//		model.add(Ontology.accountType, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.accountType, RDFS.range, Ontology.Thing);
//		// agreedPrice
//		model.add(Ontology.agreedPrice, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.agreedPrice, RDFS.range, Ontology.Thing);
//		// amount
//		model.add(Ontology.amount, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.amount, RDFS.range, Ontology.Thing);
//		// assetGrantor
//		model.add(Ontology.assetGrantor, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.assetGrantor, RDFS.range, Ontology.Thing);
//		// broader
//		model.add(Ontology.broader, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.broader, RDFS.range, Ontology.Thing);
//		// buyer
//		model.add(Ontology.buyer, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.buyer, RDFS.range, Ontology.Thing);
//		// collectiveBodyKind
//		model.add(Ontology.collectiveBodyKind, RDFS.subClassOf, Ontology.Thing);
//		model.add(Ontology.collectiveBodyKind, RDFS.range, Ontology.Thing);
//		// collectiveBodyType
//		model.add(Ontology.collectiveBodyType, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.collectiveBodyType, RDFS.range, Ontology.Thing);
//		// competentMinistry
//		model.add(Ontology.competentMinistry, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.competentMinistry, RDFS.range, Ontology.Thing);
//		// competentUnit
//		model.add(Ontology.competentUnit, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.competentUnit, RDFS.range, Ontology.Thing);
//		// decisionStatus
//		model.add(Ontology.decisionStatus, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.decisionStatus, RDFS.range, Ontology.Thing);
//		// documentsPrice
//		model.add(Ontology.documentsPrice, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.documentsPrice, RDFS.range, Ontology.Thing);
//		// donationGiver
//		model.add(Ontology.donationGiver, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.donationGiver, RDFS.range, Ontology.Thing);
//		// donationReceiver
//		model.add(Ontology.donationReceiver, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.donationReceiver, RDFS.range, Ontology.Thing);
//		// employerOrg
//		model.add(Ontology.employerOrg, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.employerOrg, RDFS.range, Ontology.Thing);
//		// fekIssue
//		model.add(Ontology.fekIssue, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.fekIssue, RDFS.range, Ontology.Thing);
//		// hasAddress
//		model.add(Ontology.hasAddress, RDFS.domain, Ontology.contractResource);
//		model.add(Ontology.hasAddress, RDFS.range, Ontology.cpvResource);
//		// hasBudgetCategory
//		model.add(Ontology.hasBudgetCategory, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasBudgetCategory, RDFS.range, Ontology.Thing);
//		// hasBudgetKind
//		model.add(Ontology.hasBudgetKind, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasBudgetKind, RDFS.range, Ontology.Thing);
//		// hasExpenditureLine
//		model.add(Ontology.hasExpenditureLine, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasExpenditureLine, RDFS.range, Ontology.Thing);
//		// hasCorrectedDecision
//		model.add(Ontology.hasCorrectedDecision, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasCorrectedDecision, RDFS.range, Ontology.Thing);
//		// hasCpv
//		model.add(Ontology.hasCpv, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasCpv, RDFS.range, Ontology.Thing);
//		// hasCurrency
//		model.add(Ontology.hasCurrency, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasCurrency, RDFS.range, Ontology.Thing);
//		// hasKae
//		model.add(Ontology.hasKae, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasKae, RDFS.range, Ontology.Thing);
//		// hasOfficialAdministrativeChange
//		model.add(Ontology.hasOfficialAdministrativeChange, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasOfficialAdministrativeChange, RDFS.range, Ontology.Thing);
//		// hasOpinionOrgType
//		model.add(Ontology.hasOpinionOrgType, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasOpinionOrgType, RDFS.range, Ontology.Thing);
//		// hasPositionType
//		model.add(Ontology.hasPositionType, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasPositionType, RDFS.range, Ontology.Thing);
//		// hasRelatedAdministrativeDecision
//		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.subClassOf, Ontology.Thing);
//		model.add(Ontology.hasRelatedAdministrativeDecision, RDFS.range, Ontology.Thing);
//		// hasRelatedDecision
//		model.add(Ontology.hasRelatedDecision, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasRelatedDecision, RDFS.range, Ontology.Thing);
//		// hasThematicCategory
//		model.add(Ontology.hasThematicCategory, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasThematicCategory, RDFS.range, Ontology.Thing);
//		// hasTopConcept
//		model.add(Ontology.hasTopConcept, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasTopConcept, RDFS.range, Ontology.Thing);
//		// hasVacancyType
//		model.add(Ontology.hasVacancyType, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasVacancyType, RDFS.range, Ontology.Thing);
//		// hasVatType
//		model.add(Ontology.hasVatType, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.hasVatType, RDFS.range, Ontology.Thing);
//		// inScheme
//		model.add(Ontology.inScheme, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.inScheme, RDFS.range, Ontology.Thing);
//		// topConceptOf
//		model.add(Ontology.topConceptOf, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.topConceptOf, RDFS.range, Ontology.Thing);
//		// isRegisteredAt
//		model.add(Ontology.isRegisteredAt, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.isRegisteredAt, RDFS.range, Ontology.Thing);
//		// kind
//		model.add(Ontology.kind, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.kind, RDFS.range, Ontology.Thing);
//		// narrower
//		model.add(Ontology.narrower, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.narrower, RDFS.range, Ontology.Thing);
//		// orgCategory
//		model.add(Ontology.orgCategory, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.orgCategory, RDFS.range, Ontology.Thing);
//		// orgStatus
//		model.add(Ontology.orgStatus, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.orgStatus, RDFS.range, Ontology.Thing);
//		// price
//		model.add(Ontology.price, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.price, RDFS.range, Ontology.Thing);
//		// publisher
//		model.add(Ontology.publisher, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.publisher, RDFS.range, Ontology.Thing);
//		// regulatoryAct
//		model.add(Ontology.regulatoryAct, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.regulatoryAct, RDFS.range, Ontology.Thing);
//		// relatedFek
//		model.add(Ontology.relatedFek, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.relatedFek, RDFS.range, Ontology.Thing);
//		// relatedLaw
//		model.add(Ontology.relatedLaw, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.relatedLaw, RDFS.range, Ontology.Thing);
//		// seller
//		model.add(Ontology.seller, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.seller, RDFS.range, Ontology.Thing);
//		// signer
//		model.add(Ontology.signer, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.signer, RDFS.range, Ontology.Thing);
//		// timePeriod
//		model.add(Ontology.timePeriod, RDFS.domain, Ontology.Thing);
//		model.add(Ontology.timePeriod, RDFS.range, Ontology.Thing);

	}

	/**
	 * Create the SKOS Concepts and Concept Schemes to the model we are
	 * currently working with.
	 * 
	 * @param Model
	 *            the model we are currently working with
	 */
	public void addConceptSchemesToModel(Model model) {

		/** Concepts **/
		/* regarding KindScheme */
		Resource conceptServicesResource = model.createResource(Ontology.kindPrefix + "Services",
				Ontology.conceptResource);
		Resource conceptWorksResource = model.createResource(Ontology.kindPrefix + "Works", Ontology.conceptResource);
		Resource conceptSuppliesResource = model.createResource(Ontology.kindPrefix + "Supplies",
				Ontology.conceptResource);
		Resource conceptStudiesResource = model.createResource(Ontology.eLodPrefix + "Studies",
				Ontology.conceptResource);

		/* regarding ProcedureTypeScheme */
		Resource conceptOpenResource = model.createResource(Ontology.procTypesPrefix + "Open",
				Ontology.conceptResource);
		Resource conceptRestrictedResource = model.createResource(Ontology.procTypesPrefix + "Restricted",
				Ontology.conceptResource);
		Resource conceptProxeirosDiagonismosResource = model.createResource(Ontology.eLodPrefix + "LowValue",
				Ontology.conceptResource);

		/** Concept Schemes **/
		Resource kindSchemeResource = model.createResource(Ontology.kindPrefix + "KindScheme",
				Ontology.conceptSchemeResource);
		Resource procedureTypeSchemeResource = model.createResource(Ontology.kindPrefix + "ProcedureTypeScheme",
				Ontology.conceptSchemeResource);

		/** configure StatusSchemes **/
		/* regarding KindScheme */
		kindSchemeResource.addProperty(Ontology.hasTopConcept, conceptServicesResource);
		kindSchemeResource.addProperty(Ontology.hasTopConcept, conceptWorksResource);
		kindSchemeResource.addProperty(Ontology.hasTopConcept, conceptSuppliesResource);
		kindSchemeResource.addProperty(Ontology.hasTopConcept, conceptStudiesResource);

		conceptServicesResource.addProperty(Ontology.topConceptOf, kindSchemeResource);
		conceptWorksResource.addProperty(Ontology.topConceptOf, kindSchemeResource);
		conceptSuppliesResource.addProperty(Ontology.topConceptOf, kindSchemeResource);
		conceptStudiesResource.addProperty(Ontology.topConceptOf, kindSchemeResource);

		conceptServicesResource.addProperty(Ontology.inScheme, kindSchemeResource);
		conceptWorksResource.addProperty(Ontology.inScheme, kindSchemeResource);
		conceptSuppliesResource.addProperty(Ontology.inScheme, kindSchemeResource);
		conceptStudiesResource.addProperty(Ontology.inScheme, kindSchemeResource);

		/* regarding ProcedureTypeScheme */
		procedureTypeSchemeResource.addProperty(Ontology.hasTopConcept, conceptOpenResource);
		procedureTypeSchemeResource.addProperty(Ontology.hasTopConcept, conceptRestrictedResource);
		procedureTypeSchemeResource.addProperty(Ontology.hasTopConcept, conceptProxeirosDiagonismosResource);

		conceptOpenResource.addProperty(Ontology.topConceptOf, procedureTypeSchemeResource);
		conceptRestrictedResource.addProperty(Ontology.topConceptOf, procedureTypeSchemeResource);
		conceptProxeirosDiagonismosResource.addProperty(Ontology.topConceptOf, procedureTypeSchemeResource);

		conceptOpenResource.addProperty(Ontology.inScheme, procedureTypeSchemeResource);
		conceptRestrictedResource.addProperty(Ontology.inScheme, procedureTypeSchemeResource);
		conceptProxeirosDiagonismosResource.addProperty(Ontology.inScheme, procedureTypeSchemeResource);

		/** configure prefLabels **/
		/* regarding KindScheme */
		conceptServicesResource.addProperty(Ontology.prefLabel, model.createLiteral("Υπηρεσίες", "el"));
		conceptWorksResource.addProperty(Ontology.prefLabel, model.createLiteral("Έργα", "el"));
		conceptSuppliesResource.addProperty(Ontology.prefLabel, model.createLiteral("Προμήθειες", "el"));
		conceptStudiesResource.addProperty(Ontology.prefLabel, model.createLiteral("Μελέτες", "el"));

		conceptServicesResource.addProperty(Ontology.prefLabel, model.createLiteral("Services", "en"));
		conceptWorksResource.addProperty(Ontology.prefLabel, model.createLiteral("Works", "en"));
		conceptSuppliesResource.addProperty(Ontology.prefLabel, model.createLiteral("Supplies", "en"));
		conceptStudiesResource.addProperty(Ontology.prefLabel, model.createLiteral("Researches", "en"));

		/* regarding ProcedureTypeScheme */
		conceptOpenResource.addProperty(Ontology.prefLabel, model.createLiteral("Ανοικτός Διαγωνισμός", "el"));
		conceptRestrictedResource.addProperty(Ontology.prefLabel, model.createLiteral("Κλειστός Διαγωνισμός", "el"));
		conceptProxeirosDiagonismosResource.addProperty(Ontology.prefLabel,
				model.createLiteral("Πρόχειρος Διαγωνισμός", "el"));

		conceptOpenResource.addProperty(Ontology.prefLabel, model.createLiteral("Open", "en"));
		conceptRestrictedResource.addProperty(Ontology.prefLabel, model.createLiteral("Restricted", "en"));
		conceptProxeirosDiagonismosResource.addProperty(Ontology.prefLabel, model.createLiteral("Low Value", "en"));
	}

}