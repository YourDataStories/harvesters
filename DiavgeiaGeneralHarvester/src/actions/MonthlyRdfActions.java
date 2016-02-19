package actions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import objects.DictionaryItem;
import objects.DictionaryItems;
import objects.LegalEntity;
import objects.Organization;
import objects.Organizations;
import objects.Position;
import objects.Signer;
import objects.Signer.SignerUnit;
import objects.Signers;
import objects.Unit;
import objects.Units;

import ontology.Ontology;

import utils.CountryOriented;
import utils.HelperMethods;

import com.google.gson.JsonElement;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author G. Razis
 */
public class MonthlyRdfActions {
	
	private HandleApiRequests handleRequests = new HandleApiRequests();
	private HelperMethods hm = new HelperMethods();
	private CountryOriented countries = new CountryOriented();
	
	/**
     * Add to the model the Resources that contain the Fek Type Issues as retrieved from the respective dictionary
     * 
     * @param Model the model we are currently working with
     * @param DictionaryItems the Fek Type Issues as retrieved from the respective dictionary
     */
	public void addFekTypeIsuesToModel(Model model, DictionaryItems fekTypesList) {
		
		for (DictionaryItem item : fekTypesList.getItems()) {
			Resource fekTypeResource = model.createResource(Ontology.instancePrefix + "FekType/" + item.getUid(), Ontology.fekTypeResource);
			fekTypeResource.addLiteral(Ontology.fekTypeId, item.getUid());
			fekTypeResource.addProperty(Ontology.prefLabel, item.getLabel(), "el");
        }
		
	}
	
	/**
     * Add to the model the Resources that contain the Countries as retrieved from the respective dictionary
     * 
     * @param Model the model we are currently working with
     * @param DictionaryItems the countries as retrieved from the respective dictionary
     */
	public void addCountriesToModel(Model model, DictionaryItems countryList) {
		
		for (DictionaryItem item : countryList.getItems()) {
			Resource fekTypeResource = model.createResource(Ontology.instancePrefix + "Country/" + item.getUid(), Ontology.countryResource);
			String[] countryDtls = countries.findCountryFromAbbreviation(item.getUid());
			fekTypeResource.addLiteral(Ontology.countryId, item.getUid());
			fekTypeResource.addProperty(Ontology.prefLabel, countryDtls[0], "el");
			fekTypeResource.addProperty(Ontology.prefLabel, countryDtls[1], "en");
        }
		
	}
	
	/**
     * Add to the model the Resources that contain the Currencies as retrieved from the respective dictionary
     * 
     * @param Model the model we are currently working with
     * @param DictionaryItems the currencies as retrieved from the respective dictionary
     */
	public void addCurrenciesToModel(Model model, DictionaryItems currencyList) {
		
		for (DictionaryItem item : currencyList.getItems()) {
			Resource fekTypeResource = model.createResource(Ontology.instancePrefix + "Currency/" + item.getUid(), Ontology.currencyResource);
			String[] currencyDtls = countries.findCurrencyFromAbbreviation(item.getUid());
			fekTypeResource.addLiteral(Ontology.currencyId, item.getUid());
			fekTypeResource.addProperty(Ontology.prefLabel, currencyDtls[0], "el");
			fekTypeResource.addProperty(Ontology.prefLabel, currencyDtls[1], "en");
        }
		
	}
	
	/**
     * Add to the model the all the Organizations as retrieved from Di@vgeia
     * 
     * @param Model the model we are currently working with
     * @param Organizations the organizations List
     */
	public void addAllOrganizationsToModel(Model model, Organizations organizationsList) {
		
		VatIdSite vatId = new VatIdSite();
		
		/*Organization newOrg = new Organization();
		newOrg.setVatNumber("090151843/12345");
		newOrg.setAbbreviation("sintomografiaAAAA");
		newOrg.setLabel("LABEL malakiaAAAA");
		newOrg.setUid("23185");
		newOrg.setLatinName("latin name");
		newOrg.setCategory("cccCCC");
		newOrg.setStatus("statusSSS");
		newOrg.setFekYear("10");
		newOrg.setFekNumber("10");
		newOrg.setFekIssue("fektype_A");
		organizationsList.getOrganizations().add(0, newOrg);*/
		
		for (Organization org : organizationsList.getOrganizations()) {
			
			Organization supervisor = null;
			LegalEntity legalEntity = null;
			
			if (org.getSupervisorId() != null) {
				supervisor = handleRequests.searchOrganization(org.getSupervisorId()); //find the details of the supervisor Organization
			}
			String orgUri = hm.organizationUriBuilder(org, supervisor);
			
			Resource orgToSearch = model.getResource(Ontology.instancePrefix + "Organization/" + orgUri);
			
			try {
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Organizations.txt", true)));
			    if (supervisor != null) {
			    	out.println(org.getVatNumber() + ";" + hm.cleanInputData(org.getLabel()) + ";" + org.getUid() + ";" + org.getSupervisorId() + ";" + orgUri);
			    } else {
			    	out.println(org.getVatNumber() + ";" + hm.cleanInputData(org.getLabel()) + ";" + org.getUid() + ";" + ";" + orgUri);
			    }
			    out.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			Set <Map.Entry<String, JsonElement>> entrySet = vatId.afmPrefixChecker(org.getVatNumber(), "API");
			
			if (entrySet != null) { //data from VatId.eu
				legalEntity = vatId.createLegalEntity(entrySet);
			}
			
			/* add or edit Organization? */
			if (model.containsResource(orgToSearch)) { //edit existing resource
				System.out.println("Editing Organization " + org.getUid() + "...\n");
				
				if (org.getVatNumber() != "") {
					if (orgToSearch.getProperty(Ontology.vatId) != null) {
						if (!orgToSearch.getProperty(Ontology.vatId).getObject().toString().equals(org.getVatNumber() + "^^http://www.w3.org/2001/XMLSchema#string")) {
							orgToSearch.removeAll(Ontology.vatId);
							orgToSearch.addLiteral(Ontology.vatId, org.getVatNumber());
						}
					} else {
						orgToSearch.addLiteral(Ontology.vatId, org.getVatNumber());
					}
	            } else {
	            	if (!orgToSearch.getProperty(Ontology.vatId).getObject().toString().equals(org.getVatNumber() + "^^http://www.w3.org/2001/XMLSchema#string")) {
						orgToSearch.removeAll(Ontology.vatId);
						orgToSearch.addLiteral(Ontology.vatId, "Empty vatID");
					}
	            }
	            
	            if (legalEntity != null) {
	            	orgToSearch.removeAll(Ontology.validVatId);
	            	orgToSearch.addLiteral(Ontology.validVatId, model.createTypedLiteral(true, XSDDatatype.XSDboolean));
	            } else {
	            	orgToSearch.removeAll(Ontology.validVatId);
	            	orgToSearch.addLiteral(Ontology.validVatId, model.createTypedLiteral(false, XSDDatatype.XSDboolean));
	            }
				
				if ( (org.getWebsite() != null) && (org.getWebsite() != "") ) {
					if (orgToSearch.getProperty(Ontology.organizationWebsite) != null) {
						if (!orgToSearch.getProperty(Ontology.organizationWebsite).getObject().toString().equals(org.getWebsite() + "^^http://www.w3.org/2001/XMLSchema#string")) {
							orgToSearch.removeAll(Ontology.organizationWebsite);
							orgToSearch.addLiteral(Ontology.organizationWebsite, org.getWebsite());
						}
					} else {
						orgToSearch.addLiteral(Ontology.organizationWebsite, org.getWebsite());
					}
				} else {
					orgToSearch.removeAll(Ontology.organizationWebsite);
				}
				
				if ( (org.getOdeManagerEmail() != null) && (org.getOdeManagerEmail() != "") ) {
					if (orgToSearch.getProperty(Ontology.odeManagerEmail) != null) {
						if (!orgToSearch.getProperty(Ontology.odeManagerEmail).getObject().toString().equals(org.getOdeManagerEmail() + "^^http://www.w3.org/2001/XMLSchema#string")) {
							orgToSearch.removeAll(Ontology.odeManagerEmail);
							orgToSearch.addLiteral(Ontology.odeManagerEmail, org.getOdeManagerEmail());
						}
					} else {
						orgToSearch.addLiteral(Ontology.odeManagerEmail, org.getOdeManagerEmail());
					}
				} else {
					orgToSearch.removeAll(Ontology.odeManagerEmail);
				}
				
				if (orgToSearch.getProperty(RDFS.label) != null) {
					if (!orgToSearch.getProperty(RDFS.label).getObject().toString().equals(org.getLatinName() + "@en")) {
						orgToSearch.removeAll(RDFS.label);
						orgToSearch.addProperty(RDFS.label, model.createLiteral(org.getLatinName(), "en"));
					}
				} else {
					orgToSearch.addProperty(RDFS.label, model.createLiteral(org.getLatinName(), "en"));
				}
				
				if (orgToSearch.getProperty(Ontology.legalName) != null) {
					if (!orgToSearch.getProperty(Ontology.legalName).getObject().toString().equals(hm.cleanInputData(org.getLabel()) + "@el")) {
						orgToSearch.removeAll(Ontology.legalName);
						orgToSearch.addProperty(Ontology.legalName, model.createLiteral(hm.cleanInputData(org.getLabel()), "el"));
					}
				} else {
					orgToSearch.addProperty(Ontology.legalName, model.createLiteral(hm.cleanInputData(org.getLabel()), "el"));
				}
				
				if (orgToSearch.getProperty(Ontology.orgCategory) != null) {
					if (!orgToSearch.getProperty(Ontology.orgCategory).getObject().toString().equals(Ontology.instancePrefix + "OrganizationCategory/" + org.getCategory())) {
						orgToSearch.removeAll(Ontology.orgCategory);
						orgToSearch.addProperty(Ontology.orgCategory, model.getResource(Ontology.instancePrefix + "OrganizationCategory/" + org.getCategory()));
					}
				} else {
					orgToSearch.addProperty(Ontology.orgCategory, model.getResource(Ontology.instancePrefix + "OrganizationCategory/" + org.getCategory()));
				}
				
				String[] statusIndividualUri = hm.findOrganizationStatusDetails(org.getStatus());
				if (orgToSearch.getProperty(Ontology.orgStatus) != null) {
					if (!orgToSearch.getProperty(Ontology.orgStatus).getObject().toString().equals(statusIndividualUri[0])) {
						orgToSearch.removeAll(Ontology.orgStatus);
						orgToSearch.addProperty(Ontology.orgStatus, model.getResource(statusIndividualUri[0]));
					}
				} else {
					orgToSearch.addProperty(Ontology.orgStatus, model.getResource(statusIndividualUri[0]));
				}
				
				if (org.getOrganizationDomains() != null) {
					orgToSearch.removeAll(Ontology.orgActivity);
					for (String orgDomain : org.getOrganizationDomains()) {
						orgToSearch.addProperty(Ontology.orgActivity, model.getResource(Ontology.instancePrefix + "OrganizationDomain/" + orgDomain));
					}
				} else {
					orgToSearch.removeAll(Ontology.orgActivity);
				}
				
				if ( (org.getAbbreviation() != null) && (org.getAbbreviation() != "") ) {
					if (orgToSearch.getProperty(Ontology.organizationAbbreviation) != null) {
						if (!orgToSearch.getProperty(Ontology.organizationAbbreviation).getObject().toString().equals(hm.cleanInputData(org.getAbbreviation()) + "@el")) {
							orgToSearch.removeAll(Ontology.organizationAbbreviation);
							orgToSearch.addProperty(Ontology.organizationAbbreviation, model.createLiteral(hm.cleanInputData(org.getAbbreviation()), "el"));
						}
					} else {
						orgToSearch.addProperty(Ontology.organizationAbbreviation, model.createLiteral(hm.cleanInputData(org.getAbbreviation()), "el"));
					}
				} else {
					orgToSearch.removeAll(Ontology.organizationAbbreviation);
				}
				
				if (org.getSupervisorId() != null) {
					if (orgToSearch.getProperty(Ontology.hasSupervisorOrganization) != null) {
						if (supervisor != null) {
							if (!orgToSearch.getProperty(Ontology.hasSupervisorOrganization).getObject().toString().equals("http://linkedeconomy.org/resource/Organization/" + supervisor.getVatNumber())) {
								orgToSearch.removeAll(Ontology.hasSupervisorOrganization);
								createSupervisorOrganization(model, orgToSearch, supervisor);
							}
						} else {
							orgToSearch.removeAll(Ontology.hasSupervisorOrganization);
						}
					} else {
						if (supervisor != null) {
							createSupervisorOrganization(model, orgToSearch, supervisor);
						}
					}
				} else {
					orgToSearch.removeAll(Ontology.hasSupervisorOrganization);
				}
				
				/* edit relating FEK */
	            createFekResource(model, orgToSearch, org.getFekIssue(), org.getFekYear(), org.getFekNumber(), false);
			} else { //create new resource
				System.out.println("Creating Organization " + org.getUid() + "...\n");
	            Resource orgResource = model.createResource(Ontology.instancePrefix + "Organization/" + orgUri, Ontology.organizationResource);
	            model.createResource(Ontology.instancePrefix + "Organization/" + orgUri, Ontology.businessEntityResource);
	            model.createResource(Ontology.instancePrefix + "Organization/" + orgUri, Ontology.orgOrganizationResource);
	            model.createResource(Ontology.instancePrefix + "Organization/" + orgUri, Ontology.registeredOrganizationResource);
	            
	            orgResource.addLiteral(Ontology.organizationId, org.getUid());
				orgResource.addProperty(RDFS.label, model.createLiteral(org.getLatinName(), "en"));
				orgResource.addProperty(Ontology.legalName , model.createLiteral(hm.cleanInputData(org.getLabel()), "el"));
				orgResource.addProperty(Ontology.orgCategory, model.getResource(Ontology.instancePrefix + "OrganizationCategory/" + org.getCategory()));
				
	            if (org.getVatNumber() != "") {
	            	orgResource.addLiteral(Ontology.vatId, org.getVatNumber());
	            } else {
	            	orgResource.addLiteral(Ontology.vatId, "Empty vatID");
	            }
	            
	            if (legalEntity != null) {
	            	orgResource.addLiteral(Ontology.validVatId, model.createTypedLiteral(true, XSDDatatype.XSDboolean));
	            } else {
	            	orgResource.addLiteral(Ontology.validVatId, model.createTypedLiteral(false, XSDDatatype.XSDboolean));
	            }
	            
				if ( (org.getWebsite() != null) && (org.getWebsite() != "") ) {
					orgResource.addLiteral(Ontology.organizationWebsite, org.getWebsite());
				}
				
				if ( (org.getOdeManagerEmail() != null) && (org.getOdeManagerEmail() != "") ) {
					orgResource.addLiteral(Ontology.odeManagerEmail, org.getOdeManagerEmail());
				}
				
				String[] statusIndividualUri = hm.findOrganizationStatusDetails(org.getStatus());
				orgResource.addProperty(Ontology.orgStatus, model.getResource(statusIndividualUri[0]));
				
				if (org.getOrganizationDomains() != null) {
					for (String orgDomain : org.getOrganizationDomains()) {
						orgResource.addProperty(Ontology.orgActivity, model.getResource(Ontology.instancePrefix + "OrganizationDomain/" + orgDomain));
					}
				}
				
				if ( (org.getAbbreviation() != null) && (org.getAbbreviation() != "") ) {
					orgResource.addProperty(Ontology.organizationAbbreviation, model.createLiteral(hm.cleanInputData(org.getAbbreviation()), "el"));
				}
				
				if (supervisor != null) {
					Resource supervisorResource = model.getResource(Ontology.instancePrefix + "Organization/" + supervisor.getVatNumber());
					if (model.containsResource(supervisorResource)) {
						orgResource.addProperty(Ontology.hasSupervisorOrganization, supervisorResource);
					} else {
						createSupervisorOrganization(model, orgResource, supervisor);
					}
				}
				
				/* create relating FEK */
	            createFekResource(model, orgResource, org.getFekIssue(), org.getFekYear(), org.getFekNumber(), true);
				
	            /* create relating Units */
	            Units unitsList = handleRequests.getOrganizationUnits(org.getUid());
	            addOrganizationUnitToModel(model, orgResource, unitsList);
	            
	            /* create relating Signers */
	            Signers signersList = handleRequests.getOrganizationSigners(org.getUid());
	            addSignerToModel(model, orgResource, signersList);
			}
            //break;
        }
		
	}
	
	/**
     * Add to the model the the Fek that is related to the Organization
     * 
     * @param Model the model we are currently working with
     * @param Resource the resource of the related organization
     * @param String the issue type of the Fek 
     * @param String the year that Fek was published 
     * @param String the number of the Fek 
     */
	private void createFekResource(Model model, Resource orgResource, String fekIssue, String fekYear, String fekNumber, boolean newOrganizationFlag) {
		
		String fekUriName = "";
		
		if ( (fekIssue != null) && (fekIssue != "") ) {
			fekUriName =  fekIssue + "/" + fekYear + "/" + fekNumber;
		} else {
			fekUriName = fekYear + "/" + fekNumber;
		}
		
		Resource fekResource = model.getResource(Ontology.instancePrefix  + "Fek/" + fekUriName);
		
		if (model.containsResource(fekResource)) { //if Fek resource exists use it
			orgResource.addProperty(Ontology.relatedFek, fekResource);
		} else { //...else create it
			fekResource = model.createResource(Ontology.instancePrefix + "Fek/" + fekUriName, Ontology.fekResource);
			fekResource.addProperty(Ontology.fekNumber, fekNumber);
			fekResource.addProperty(Ontology.fekYear, fekYear);
			if ( (fekIssue != null) && (fekIssue != "") ) {
				fekResource.addProperty(Ontology.fekIssue, model.getResource(Ontology.instancePrefix + "FekType/" + fekIssue));
			}
		}
		
		/** Organization - FEK **/
		if (newOrganizationFlag) {
			orgResource.addProperty(Ontology.relatedFek, fekResource);
		} else {
			orgResource.removeAll(Ontology.relatedFek); //delete the old relationships
			orgResource.addProperty(Ontology.relatedFek, fekResource);
		}
		
	}
	
	/**
     * Add to the model the the Organization Unit that is related to the Organization
     * 
     * @param Model the model we are currently working with
     * @param Resource the resource of the related organization
     * @param Unit the list of the organization units
     */
	private void addOrganizationUnitToModel(Model model, Resource orgResource, Units unitsList) {
		
		for (Unit unit : unitsList.getUnits()) {
			Resource orgUnitResource = model.createResource(Ontology.instancePrefix + "OrganizationalUnit/" + unit.getUid(), Ontology.organizationalUnitResource);
			orgUnitResource.addLiteral(Ontology.organizationUnitId, unit.getUid());
			//orgUnitResource.addLiteral(Ontology.organizationUnitActive, unit.isActive());
			orgUnitResource.addProperty(RDFS.label, model.createLiteral(hm.cleanInputData(unit.getLabel()), "el"));
			
			if (unit.getAbbreviation() != null) {
				orgUnitResource.addLiteral(Ontology.organizationUnitAbbreviation, unit.getAbbreviation());
			}
				
			if ((unit.getCategory() != null) && (unit.getCategory() != "")) {
				orgUnitResource.addProperty(Ontology.hasOrgUnitCategory, model.getResource(Ontology.instancePrefix + "OrganizationalUnitCategory/" + unit.getCategory()));
			}
			
			if (unit.getUnitDomains() != null) {
				for (String unitDomain : unit.getUnitDomains()) {
					orgUnitResource.addProperty(Ontology.orgActivity, model.getResource(Ontology.instancePrefix + "OrganizationDomain/" + unitDomain));
				}
			}
			/** Organization - OrganizationalUnit **/
			orgResource.addProperty(Ontology.hasUnit, orgUnitResource);
			/** OrganizationalUnit - Organization **/
			orgUnitResource.addProperty(Ontology.unitOf, orgResource);
		}
		
	}
	
	/**
     * Add to the model the supervisor of the current Organization
     * 
     * @param Model the model we are currently working with
     * @param Resource the current organization
     * @param Organization the supervisor of the organization
     */
	private void createSupervisorOrganization(Model model, Resource orgResource, Organization supervisor) {
		
		Resource supervisorResource = model.getResource(Ontology.instancePrefix + "Organization/" + supervisor.getVatNumber());
		
		if (model.containsResource(supervisorResource)) {
			orgResource.addProperty(Ontology.hasSupervisorOrganization, supervisorResource);
		} else {
			supervisorResource = model.createResource(Ontology.instancePrefix + "Organization/" + supervisor.getVatNumber(), Ontology.organizationResource);
            model.createResource(Ontology.instancePrefix + "Organization/" + supervisor.getVatNumber(), Ontology.businessEntityResource);
            model.createResource(Ontology.instancePrefix + "Organization/" + supervisor.getVatNumber(), Ontology.orgOrganizationResource);
            model.createResource(Ontology.instancePrefix + "Organization/" + supervisor.getVatNumber(), Ontology.registeredOrganizationResource);
            
            if (supervisor.getVatNumber() != "") {
            	supervisorResource.addLiteral(Ontology.vatId, supervisor.getVatNumber());
            } else {
            	supervisorResource.addLiteral(Ontology.vatId, "Empty vatID");
            }
            
            supervisorResource.addLiteral(Ontology.organizationId, supervisor.getUid());
            
            /** organization - Supervisor **/
			orgResource.addProperty(Ontology.hasSupervisorOrganization, supervisorResource);
		}
		
	}
	
	/**
     * Add to the model the Signer that is related to the Organization
     * 
     * @param Model the model we are currently working with
     * @param Resource the resource of the related organization
     * @param Unit the list of the signers
     */
	public void addSignerToModel(Model model, Resource orgResource, Signers signersList) {
		
		Organization orgObject = null;
		
		for (Signer signer : signersList.getSigners()) {
            Resource signerResource = model.createResource(Ontology.instancePrefix + "Signer/" + signer.getUid(), Ontology.personResource);
			signerResource.addLiteral(Ontology.signerId, signer.getUid());
			signerResource.addLiteral(Ontology.signerActive, signer.isActive());
			//signerResource.addProperty(Ontology.lastName, model.createLiteral(signer.getLastName(), "el"));
			//signerResource.addProperty(Ontology.firstName, model.createLiteral(signer.getFirstName(), "el"));
			signerResource.addProperty(Ontology.lastName, signer.getLastName(), "el");
			signerResource.addProperty(Ontology.firstName, signer.getFirstName(), "el");
			//signerResource.addProperty(Ontology.belongsTo, model.getResource(Ontology.instancePrefix + "Organization/" + signer.getOrganizationId()));
			
			for (SignerUnit signUnit : signer.getUnits()) {
				/** membershipResource (position) **/
				Resource membershipResource = null;
				if (signUnit.getPositionId() != null) {
					membershipResource = model.createResource(Ontology.instancePrefix + "Membership/" + signer.getUid() + "_position", Ontology.membershipResource);
					membershipResource.addProperty(Ontology.member, model.getResource(Ontology.instancePrefix + "Signer/" + signer.getUid()));
					membershipResource.addProperty(Ontology.role, model.getResource(Ontology.instancePrefix + "Role/" + signUnit.getPositionId()));
					orgObject = handleRequests.searchOrganization(signer.getOrganizationId()); //find the details of the Organization
					if (orgObject != null) {
						membershipResource.addProperty(Ontology.organization, model.getResource(Ontology.instancePrefix + "Organization/" + orgObject.getVatNumber()));
					}
					/** Signer - Membership(position) **/
					signerResource.addProperty(Ontology.hasMember, membershipResource);
				}
				
				/** membershipResource (signer) **/
				membershipResource = model.createResource(Ontology.instancePrefix + "Membership/" + signer.getUid() + "_sign", Ontology.membershipResource);
				membershipResource.addProperty(Ontology.member, model.getResource(Ontology.instancePrefix + "Signer/" + signer.getUid()));
				membershipResource.addProperty(Ontology.role, model.getResource(Ontology.instancePrefix + "Role/Signer"));
				/** Signer - Organization or OrganizationalUnit **/
				signerResource.addLiteral(Ontology.signerOrgSignRights, signer.isHasOrganizationSignRights());
				if (signer.isHasOrganizationSignRights()) {
					if (signUnit.getUid() != null) {
						orgObject = handleRequests.searchOrganization(signUnit.getUid()); //find the details of the Organization
						if (orgObject != null) {
							membershipResource.addProperty(Ontology.organization, model.getResource(Ontology.instancePrefix + "Organization/" + orgObject.getVatNumber()));
						}
					}
				} else {
					if (signUnit.getUid() != null) {
						membershipResource.addProperty(Ontology.organization, model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + signUnit.getUid()));
					}
				}
				/** Signer - Membership(signer) **/
				signerResource.addProperty(Ontology.hasMember, membershipResource);
			}
        }
		
	}
	
	/**
     * Add to the model we are currently working with the Classes and their individuals from the
     * the dictionaries: ORG_CATEGORY, ORG_UNIT_CATEGORY, ORG_DOMAIN, THK and VAT_TYPE.
     * 
     * @param Model the model we are currently working with
     * @param DictionaryItems the items of the dictionary
     */
	public void addDiavgeiaRelatedConceptsToModel(Model model, DictionaryItems dictItemsList) {
		
		if (dictItemsList.getName().equalsIgnoreCase("ORG_CATEGORY")) {
			for (DictionaryItem item : dictItemsList.getItems()) {
				/** itemResource **/
				Resource itemResource = model.createResource(Ontology.instancePrefix + "OrganizationCategory/" + item.getUid(), Ontology.organizationCategoryResource);
				model.createResource(Ontology.instancePrefix + "OrganizationCategory/" + item.getUid(), Ontology.conceptResource);
				/** configure prefLabels **/
				String[] orgCatNames = hm.findOrganizationCategoryName(item.getUid());
				itemResource.addProperty(Ontology.prefLabel, model.createLiteral(orgCatNames[0], "el"));
				itemResource.addProperty(Ontology.prefLabel, model.createLiteral(orgCatNames[1], "en"));
            }
//			/* manually add Category DEYA */
//			/** itemResource **/
//			Resource deyaResource = model.createResource(Ontology.instancePrefix + "OrganizationCategory/" + "DEYA", Ontology.organizationCategoryResource);
//			model.createResource(Ontology.instancePrefix + "OrganizationCategory/" + "DEYA", Ontology.conceptResource);
//			/** configure prefLabels **/
//			String[] orgCatNames = hm.findOrganizationCategoryName("DEYA");
//			deyaResource.addProperty(Ontology.prefLabel, model.createLiteral(orgCatNames[0], "el"));
//			deyaResource.addProperty(Ontology.prefLabel, model.createLiteral(orgCatNames[1], "en"));
		} else if (dictItemsList.getName().equalsIgnoreCase("ORG_UNIT_CATEGORY")) {
			for (DictionaryItem item : dictItemsList.getItems()) {
				/** itemResource **/
				Resource itemResource = model.createResource(Ontology.instancePrefix + "OrganizationalUnitCategory/" + item.getUid(), Ontology.orgUnitCategoryResource);
				model.createResource(Ontology.instancePrefix + "OrganizationalUnitCategory/" + item.getUid(), Ontology.conceptResource);
				/** configure prefLabels **/
				String[] unitNames = hm.findUnitCategoryName(item.getUid());
				itemResource.addProperty(Ontology.prefLabel, model.createLiteral(unitNames[0], "el"));
				itemResource.addProperty(Ontology.prefLabel, model.createLiteral(unitNames[1], "en"));
            }
		} else if (dictItemsList.getName().equalsIgnoreCase("ORG_DOMAIN")) {
			for (DictionaryItem item : dictItemsList.getItems()) {
				/** itemResource **/
				Resource itemResource = model.createResource(Ontology.instancePrefix + "OrganizationDomain/" + item.getUid(), Ontology.organizationDomainResource);
				model.createResource(Ontology.instancePrefix + "OrganizationDomain/" + item.getUid(), Ontology.conceptResource);
				/** configure OrganizationDomain broader **/
				if ( (item.getParent() != null) && (item.getParent() != "") ) {
					itemResource.addProperty(Ontology.broader, model.createResource(Ontology.instancePrefix + "OrganizationDomain/" + item.getParent(), Ontology.conceptResource));
				}
				/** configure prefLabels **/
				String[] orgDomain = hm.findOrganizationDomainName(item.getUid());
				itemResource.addProperty(Ontology.prefLabel, model.createLiteral(orgDomain[0], "el"));
				itemResource.addProperty(Ontology.prefLabel, model.createLiteral(orgDomain[1], "en"));
            }
		} else if (dictItemsList.getName().equalsIgnoreCase("THK")) {
			for (DictionaryItem item : dictItemsList.getItems()) {
				/** itemResource **/
				Resource itemResource = model.createResource(Ontology.instancePrefix + "ThematicCategory/" + item.getUid(), Ontology.thematicCategoryResource);
				model.createResource(Ontology.instancePrefix + "ThematicCategory/" + item.getUid(), Ontology.conceptResource);
				/** configure prefLabel **/
				itemResource.addProperty(Ontology.prefLabel, item.getLabel(), "el");
            }
		} else if (dictItemsList.getName().equalsIgnoreCase("VAT_TYPE")) {
			for (DictionaryItem item : dictItemsList.getItems()) {
				/** itemResource **/
				Resource itemResource = model.createResource(Ontology.instancePrefix + "VatType/" + item.getUid(), Ontology.vatTypeResource);
				model.createResource(Ontology.instancePrefix + "VatType/" + item.getUid(), Ontology.conceptResource);
				/** configure prefLabel **/
				String[] vatTypeNames = hm.findVatTypeName(item.getUid());
				itemResource.addProperty(Ontology.prefLabel, vatTypeNames[0], "el");
				itemResource.addProperty(Ontology.prefLabel, vatTypeNames[1], "el");
            }
		}
		
	}
		
	/**
     * Add to the model the positions.
     * 
     * @param Model the model we are currently working with
     * @param ArrayList<Position> the list containing the positions
     */
	public void addAllPositionsToModel(Model model, ArrayList<Position> positionsList) {
		for (Position position : positionsList) {
			/** itemResource **/
			Resource positionResource = model.createResource(Ontology.instancePrefix + "Role/" + position.getUid(), Ontology.roleResource);
			model.createResource(Ontology.instancePrefix + "Role/" + position.getUid(), Ontology.conceptResource);
			/** configure prefLabel **/
			positionResource.addProperty(Ontology.prefLabel, position.getLabel(), "el");
        }
		/* add signer Role */
		/** signerResource **/
		Resource positionResource = model.createResource(Ontology.instancePrefix + "Role/Signer", Ontology.roleResource);
		model.createResource(Ontology.instancePrefix + "Role/Signer", Ontology.conceptResource);
		/** configure prefLabel **/
		positionResource.addProperty(Ontology.prefLabel, "Τελικός Υπογράφων", "el");
		positionResource.addProperty(Ontology.prefLabel, "Signer", "en");
		
	}
	
	/**
     * Add to the model the organization statuses.
     * 
     * @param Model the model we are currently working with
     */
	public void addOrganizationStatusToModel(Model model) {
		
		List<String> statusesList = Arrays.asList("Active", "Inactive", "Pending");
		
		for (String status : statusesList) {
			/** statusResource **/
			Resource statusResource = model.createResource(Ontology.instancePrefix + "OrganizationStatus/" + status, Ontology.organizationStatusResource);
			model.createResource(Ontology.instancePrefix + "OrganizationStatus/" + status, Ontology.conceptResource);
			/** configure prefLabel **/
			String[] statusDtls = hm.findOrganizationStatusDetails(status);
			statusResource.addProperty(Ontology.prefLabel, statusDtls[1], "el");
			statusResource.addProperty(Ontology.prefLabel, statusDtls[2], "en");
        }
		
	}
	
	/**
     * Add to the model the decision statuses.
     * 
     * @param Model the model we are currently working with
     */
	public void addDecisionStatusToModel(Model model) {
		
		List<String> statusesList = Arrays.asList("Published", "Pending_Revocation", "Revoked", "Submitted");
		
		for (String status : statusesList) {
			String[] statusDtls = hm.findDecisionStatusIndividual(status);
			/** statusResource **/
			Resource statusResource = model.createResource(statusDtls[0], Ontology.decisionStatusResource);
			model.createResource(statusDtls[0], Ontology.conceptResource);
			/** configure prefLabel **/
			statusResource.addProperty(Ontology.prefLabel, statusDtls[1], "el");
			statusResource.addProperty(Ontology.prefLabel, statusDtls[2], "en");
        }
		
	}
	
	/**
     * Add to the model the selection criteria.
     * 
     * @param Model the model we are currently working with
     */
	public void addSelectionCriteriaToModel(Model model) {
		
		List<String> criteriaList = Arrays.asList("Χαμηλότερη Τιμή", "Συμφερότερη από οικονομικής άποψης", "Τεχνική ποιότητα");
		
		for (String criterion : criteriaList) {
			String[] selectionDtls = hm.findCriterionIndividual(criterion);
			/** statusResource **/
			Resource statusResource = model.createResource(selectionDtls[0], Ontology.selectionCriterionResource);
			model.createResource(selectionDtls[0], Ontology.conceptResource);
			/** configure prefLabel **/
			statusResource.addProperty(Ontology.prefLabel, selectionDtls[2], "el");
			statusResource.addProperty(Ontology.prefLabel, selectionDtls[3], "en");
        }
		
	}
	
	/**
     * Add to the model the budget types.
     * 
     * @param Model the model we are currently working with
     */
	public void addBudgetTypeToModel(Model model) {
		
		List<String> budgetTypeList = Arrays.asList("Τακτικός Προϋπολογισμός", "Πρόγραμμα Δημοσίων Επενδύσεων", "Ίδια Έσοδα", "Συγχρηματοδοτούμενο Έργο");
		
		for (String budgetType : budgetTypeList) {
			String[] budgetDtls = hm.findBudgetTypeIndividual(budgetType);
			/** statusResource **/
			Resource statusResource = model.createResource(budgetDtls[0], Ontology.budgetCategoryResource);
			model.createResource(budgetDtls[0], Ontology.conceptResource);
			/** configure prefLabel **/
			statusResource.addProperty(Ontology.prefLabel, budgetDtls[1], "el");
			statusResource.addProperty(Ontology.prefLabel, budgetDtls[2], "en");
        }
		
	}

}