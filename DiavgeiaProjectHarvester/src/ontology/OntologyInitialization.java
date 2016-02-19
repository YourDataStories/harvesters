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
     * Add the necessary prefixes to the model we are currently 
     * working with.
     * 
     * @param Model the model we are currently working with
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
     * Create the basic hierarchies of the OWL classes and their labels 
     * to the model we are currently working with.
     * 
     * @param Model the model we are currently working with
     */
	public void createHierarchies(Model model) {
		
		//Agent
		model.add(Ontology.agentResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.agentResource, RDFS.label, model.createLiteral("Agent", "en"));
		model.add(Ontology.agentResource, RDFS.label, model.createLiteral("Πράκτορας", "el"));
		
		//Concept
		model.add(Ontology.conceptResource, RDFS.subClassOf, OWL.Thing);
		
		//Concept Scheme
		model.add(Ontology.conceptSchemeResource, RDFS.subClassOf, OWL.Thing);
		
		//Concept - Vat Type
		model.add(Ontology.vatTypeResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.vatTypeResource, RDFS.label, model.createLiteral("VAT Type", "en"));
		model.add(Ontology.vatTypeResource, RDFS.label, model.createLiteral("Τύπος ΑΦΜ", "el"));
		
		//Concept - Thematic Category
		model.add(Ontology.thematicCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.thematicCategoryResource, RDFS.label, model.createLiteral("Thematic Category Type", "en"));
		model.add(Ontology.thematicCategoryResource, RDFS.label, model.createLiteral("Τύπος Θεματικής Κατηγορίας", "el"));
		
		//Concept - Role
		model.add(Ontology.roleResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.roleResource, RDFS.label, model.createLiteral("Role", "en"));
		model.add(Ontology.roleResource, RDFS.label, model.createLiteral("Ρόλος", "el"));
		
		//Concept - Country
		model.add(Ontology.countryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.countryResource, RDFS.label, model.createLiteral("Country", "en"));
		model.add(Ontology.countryResource, RDFS.label, model.createLiteral("Χώρα", "el"));

		//Concept - Currency
		model.add(Ontology.currencyResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.currencyResource, RDFS.label, model.createLiteral("Currency", "en"));
		model.add(Ontology.currencyResource, RDFS.label, model.createLiteral("Νόμισμα", "el"));
		
		//Concept - Organizational Unit Category
		model.add(Ontology.orgUnitCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.orgUnitCategoryResource, RDFS.label, model.createLiteral("Organizational Unit Category", "en"));
		model.add(Ontology.orgUnitCategoryResource, RDFS.label, model.createLiteral("Τύποι Οργανωτικών Μονάδων", "el"));
		
		//Concept - Organization Domain
		model.add(Ontology.organizationDomainResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.organizationDomainResource, RDFS.label, model.createLiteral("Organization Domain", "en"));
		model.add(Ontology.organizationDomainResource, RDFS.label, model.createLiteral("Πεδίο Αρμοδιότητας Φορέων", "el"));
		
		//Concept - Organization Status
		model.add(Ontology.organizationStatusResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.organizationStatusResource, RDFS.label, model.createLiteral("Organization Status", "en"));
		model.add(Ontology.organizationStatusResource, RDFS.label, model.createLiteral("Κατάσταση Οργανισμού", "el"));
		
		//Concept - Organization Category
		model.add(Ontology.organizationCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.organizationCategoryResource, RDFS.label, model.createLiteral("Organization Category", "en"));
		model.add(Ontology.organizationCategoryResource, RDFS.label, model.createLiteral("Κατηγορίες Φορέων", "el"));
		
		//Concept - Decision Status
		model.add(Ontology.decisionStatusResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.decisionStatusResource, RDFS.label, model.createLiteral("Decision Status", "en"));
		model.add(Ontology.decisionStatusResource, RDFS.label, model.createLiteral("Κατάσταση Πράξης", "el"));
		
		//Concept - Selection Criterion
		model.add(Ontology.selectionCriterionResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.selectionCriterionResource, RDFS.label, model.createLiteral("Selection Criterion", "en"));
		model.add(Ontology.selectionCriterionResource, RDFS.label, model.createLiteral("Κριτήριο Επιλογής", "el"));
		
		//Concept - Budget Category
		model.add(Ontology.budgetCategoryResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.budgetCategoryResource, RDFS.label, model.createLiteral("Budget Category", "en"));
		model.add(Ontology.budgetCategoryResource, RDFS.label, model.createLiteral("Κατηγορία Προϋπολογισμού", "el"));
		
		//Fek Type
		model.add(Ontology.fekTypeResource, RDFS.subClassOf, Ontology.conceptResource);
		model.add(Ontology.fekTypeResource, RDFS.label, model.createLiteral("Greek Government Gazzette Issue Type ", "en"));
		model.add(Ontology.fekTypeResource, RDFS.label, model.createLiteral("Τύπος Τεύχους Φύλλου Εφημερίδος Κυβερνήσεως", "el"));
		
		//Agent - Business Entity
		model.add(Ontology.businessEntityResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.businessEntityResource, RDFS.label, model.createLiteral("Business Entity", "en"));
		model.add(Ontology.businessEntityResource, RDFS.label, model.createLiteral("Επιχειρηματική Οντότητα", "el"));
		
		//Agent - Registered Organization
		model.add(Ontology.registeredOrganizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.registeredOrganizationResource, RDFS.label, model.createLiteral("Registered Organization", "en"));
		model.add(Ontology.registeredOrganizationResource, RDFS.label, model.createLiteral("Καταχωρημένος Οργανισμός", "el"));
		
		//Agent - Organization (FOAF)
		model.add(Ontology.organizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Organization", "en"));
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Οργανισμός", "el"));
				
		//Agent - Organization (org)
		model.add(Ontology.orgOrganizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.orgOrganizationResource, RDFS.label, model.createLiteral("Organization", "en"));
		model.add(Ontology.orgOrganizationResource, RDFS.label, model.createLiteral("Οργανισμός", "el"));
		
		//Orgs equivalence
		/*model.add(Ontology.businessEntityResource, OWL.equivalentClass, Ontology.registeredOrganizationResource);
		model.add(Ontology.businessEntityResource, OWL.equivalentClass, Ontology.organizationResource);
		model.add(Ontology.businessEntityResource, OWL.equivalentClass, Ontology.orgOrganizationResource);
		model.add(Ontology.registeredOrganizationResource, OWL.equivalentClass, Ontology.organizationResource);
		model.add(Ontology.registeredOrganizationResource, OWL.equivalentClass, Ontology.orgOrganizationResource);
		model.add(Ontology.organizationResource, OWL.equivalentClass, Ontology.orgOrganizationResource);*/
		
		//Agent - Person
		model.add(Ontology.personResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.personResource, RDFS.label, model.createLiteral("Person", "en"));
		model.add(Ontology.personResource, RDFS.label, model.createLiteral("Πρόσωπο", "el"));
		
		//Agent - Organization
		model.add(Ontology.organizationResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Organization", "en"));
		model.add(Ontology.organizationResource, RDFS.label, model.createLiteral("Οργανισμός", "el"));
		
		//Agent - Organizational Unit
		model.add(Ontology.organizationalUnitResource, RDFS.subClassOf, Ontology.agentResource);
		model.add(Ontology.organizationalUnitResource, RDFS.label, model.createLiteral("Organizational Unit", "en"));
		model.add(Ontology.organizationalUnitResource, RDFS.label, model.createLiteral("Μονάδα Οργανισμού", "el"));	
		
		//Spending Item
		model.add(Ontology.spendingItemResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.spendingItemResource, RDFS.label, model.createLiteral("Spending Item", "en"));
		model.add(Ontology.spendingItemResource, RDFS.label, model.createLiteral("Αντικείμενο Δαπάνης", "el"));
		
		//Award Criteria Combination
		model.add(Ontology.awardCriteriaCombinationResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.awardCriteriaCombinationResource, RDFS.label, model.createLiteral("Combination of Contract Award Criteria", "en"));
		model.add(Ontology.awardCriteriaCombinationResource, RDFS.label, model.createLiteral("Συνδυασμός των Κριτηρίων Ανάθεσης της Σύμβασης", "el"));
		
		//Criterion Weighting
		model.add(Ontology.criterionWeightingResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.criterionWeightingResource, RDFS.label, model.createLiteral("Award Criterion", "en"));
		model.add(Ontology.criterionWeightingResource, RDFS.label, model.createLiteral("Κριτήριο Ανάθεσης", "el"));
		
		//Contract
		model.add(Ontology.contractResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.contractResource, RDFS.label, model.createLiteral("Public Contract", "en"));
		model.add(Ontology.contractResource, RDFS.label, model.createLiteral("Δημόσια Σύμβαση", "el"));
				
		//CPV
		model.add(Ontology.cpvResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.cpvResource, RDFS.label, model.createLiteral("Common Procurement Vocabulary", "en"));
		model.add(Ontology.cpvResource, RDFS.label, model.createLiteral("Κοινό Λεξιλόγιο Προμηθειών", "el"));
		
		//Attachment
		model.add(Ontology.attachmentResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.attachmentResource, RDFS.label, model.createLiteral("Attachments", "en"));
		model.add(Ontology.attachmentResource, RDFS.label, model.createLiteral("Συνημμένα Έγγραφα", "el"));
		
		//Decision
		model.add(Ontology.decisionResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.decisionResource, RDFS.label, model.createLiteral("Decision", "en"));
		model.add(Ontology.decisionResource, RDFS.label, model.createLiteral("Πράξη", "el"));
		
		//Committed Amount
		model.add(Ontology.committedItemResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.committedItemResource, RDFS.label, model.createLiteral("Committed Item", "en"));
		model.add(Ontology.committedItemResource, RDFS.label, model.createLiteral("Δεσμευθέν Αντικείμενο", "el"));
		
		//Expenditure Line
		model.add(Ontology.expenditureLineResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.expenditureLineResource, RDFS.label, model.createLiteral("Expenditure Line", "en"));
		model.add(Ontology.expenditureLineResource, RDFS.label, model.createLiteral("Τμήμα Δαπάνης", "el"));
		
		//Expense Approval Item
		model.add(Ontology.expenseApprovalItemResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.expenseApprovalItemResource, RDFS.label, model.createLiteral("Expense Approval", "en"));
		model.add(Ontology.expenseApprovalItemResource, RDFS.label, model.createLiteral("Έγκριση Δαπάνης", "el"));
		
		//FEK
		model.add(Ontology.fekResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.fekResource, RDFS.label, model.createLiteral("Greek Government Gazzette", "en"));
		model.add(Ontology.fekResource, RDFS.label, model.createLiteral("Φύλλο Εφημερίδος Κυβερνήσεως", "el"));
		
		//KAE
		model.add(Ontology.kaeResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.kaeResource, RDFS.label, model.createLiteral("Code Number of Revenues/Expenses", "en"));
		model.add(Ontology.kaeResource, RDFS.label, model.createLiteral("Κωδικός Αριθμού Εσόδων/Εξόδων", "el"));
		
		//Unit Price Specification
		model.add(Ontology.unitPriceSpecificationResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.unitPriceSpecificationResource, RDFS.label, model.createLiteral("Unit Price Specification", "en"));
		model.add(Ontology.unitPriceSpecificationResource, RDFS.label, model.createLiteral("Προδιαγραφή τιμής ανά μονάδα", "el"));
		
		//Membership
		model.add(Ontology.membershipResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.membershipResource, RDFS.label, model.createLiteral("Membership", "en"));
		model.add(Ontology.membershipResource, RDFS.label, model.createLiteral("Μέλος", "el"));
		
		//Address
		model.add(Ontology.addressResource, RDFS.subClassOf, OWL.Thing);
		model.add(Ontology.addressResource, RDFS.label, model.createLiteral("Adress", "en"));
		model.add(Ontology.addressResource, RDFS.label, model.createLiteral("Διεύθυνση", "el"));
	}
	
	/**
     * Create the SKOS Concepts and Concept Schemes to the 
     * model we are currently working with.
     * 
     * @param Model the model we are currently working with
     */
	public void addConceptSchemesToModel(Model model) {
		
		/** Concepts **/
		/* regarding KindScheme */
		Resource conceptServicesResource = model.createResource(Ontology.publicContractsPrefix + "Services", Ontology.conceptResource);
		Resource conceptWorksResource = model.createResource(Ontology.publicContractsPrefix + "Works", Ontology.conceptResource);
		Resource conceptSuppliesResource = model.createResource(Ontology.publicContractsPrefix + "Supplies", Ontology.conceptResource);
		Resource conceptStudiesResource = model.createResource(Ontology.eLodPrefix + "Studies", Ontology.conceptResource);
		
		/* regarding ProcedureTypeScheme */
		Resource conceptOpenResource = model.createResource(Ontology.publicContractsPrefix + "Open", Ontology.conceptResource);
		Resource conceptRestrictedResource = model.createResource(Ontology.publicContractsPrefix + "Restricted", Ontology.conceptResource);
		Resource conceptProxeirosDiagonismosResource = model.createResource(Ontology.eLodPrefix + "LowValue", Ontology.conceptResource);	
		
		/** Concept Schemes **/ 
		Resource kindSchemeResource = model.createResource(Ontology.publicContractsPrefix + "KindScheme", Ontology.conceptSchemeResource);
		Resource procedureTypeSchemeResource = model.createResource(Ontology.publicContractsPrefix + "ProcedureTypeScheme", Ontology.conceptSchemeResource);
		
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
		conceptProxeirosDiagonismosResource.addProperty(Ontology.prefLabel, model.createLiteral("Πρόχειρος Διαγωνισμός", "el"));
		
		conceptOpenResource.addProperty(Ontology.prefLabel, model.createLiteral("Open", "en"));
		conceptRestrictedResource.addProperty(Ontology.prefLabel, model.createLiteral("Restricted", "en"));
		conceptProxeirosDiagonismosResource.addProperty(Ontology.prefLabel, model.createLiteral("Low Value", "en"));
	}
	
}