package organizations;

import java.util.Map;
import java.util.Set;

import objects.LegalEntity;

import utils.TransliterationMethods;

import actions.Main;
import actions.VatIdSite;

import com.google.gson.JsonElement;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author G. Razis
 */
public class AgentHandler {
	
	/**
     * Search whether a URI exists in the Organizations graph with the 
     * provided Vat Id. If yes return the needed information. If not 
     * request its details from the VatId.eu service and store them.
     * 
     * @param String the Vat Id 
     * @param String the file where the the Vat Id was found
     * @return Object[] The URI of Agent, its VatId and whether 
     * it is an Organization or a Physical Person
     */
	public Object[] handleAgent(String vatId, String ada) {
		
		Queries qs = new Queries();
		TransliterationMethods trans = new TransliterationMethods();
		
		Resource agentUri = null;
		boolean isCompany = false;
		
		if (vatId.length() > 3) {
			if (vatId.length() == 9) {
				if (vatId.matches("\\d+")) { //only digits -> Greek AFM
						//continue normally
				} else { //error AFM
					System.out.println("Error in AFM: " + vatId);
				}
			} else if (vatId.matches("[a-zA-Z0-9 ]*")) { //No special characters in it
				if ( (vatId.substring(0, 3).matches("[A-Z]*")) || (vatId.substring(0, 2).matches("[A-Z]*")) ) { //ie: ATU -> Austria, DE -> Germany
					vatId = vatId.substring(2, vatId.length());
				} else { //unknown
					System.out.println("Unknown AFM: " + vatId);
				}
			} else {
				System.out.println("There are special characters in the AFM.\n");
			}
		} else {
			System.out.println("Less than 3 characters in the AFM.\n");
		}
		
		agentUri = qs.getAgentUri(Main.graphOrgs, vatId);
		
		if (agentUri == null) { //Agent does not exist in graph
			
			VatIdSite vatIdSite = new VatIdSite();
			Set <Map.Entry<String, JsonElement>> entrySet = vatIdSite.afmPrefixChecker(vatId, ada);
			
			if (entrySet != null) { //response from VatId.eu service
				
				LegalEntity legalEntity = vatIdSite.createLegalEntity(entrySet);
				
				//insert the Agent details
				if (legalEntity.isValid()) { /** valid AFM for Organization **/
					
					qs.insertAgentUri(Main.graphOrgs, vatId, legalEntity.isPhysicalPerson());
					
					String uri = qs.createAgentUri(vatId, legalEntity.isPhysicalPerson());
					
					//gr:vatId
					qs.insertVatId(Main.graphOrgs, uri, legalEntity.getVatNumber());
					
					agentUri = qs.getAgentUri(Main.graphOrgs, legalEntity.getVatNumber());
					
					//gr:legalName
					if (legalEntity.getName() != null) {
						qs.insertLegalName(Main.graphOrgs, agentUri.getURI(), legalEntity.getName());
						qs.insertTransliteration(Main.graphOrgs, agentUri.getURI(), trans.transliterationGenerator(legalEntity.getName()));
					}
					
					//elod:validVatId
					qs.insertValidVatId(Main.graphOrgs, agentUri.getURI(), true);
					
					//vcard:Address
					qs.insertAddress(Main.graphOrgs, agentUri.getURI(), legalEntity.getVatNumber(), legalEntity);
					
					//elod:hasVatType
					qs.insertVatType(Main.graphOrgs, agentUri.getURI(), legalEntity.getCountryCode());
					
					//elod:isRegisteredAt
					qs.insertRegisteredAt(Main.graphOrgs, agentUri.getURI(), legalEntity.getCountryCode());
					
					//dcterms:created
					qs.insertCreated(Main.graphOrgs, agentUri.getURI());
					
					isCompany = true;
				
				} else { //Physical Person or invalid AFM
					
					if (legalEntity.isPhysicalPerson()) { /** Physical Person **/
						
						qs.insertAgentUri(Main.graphOrgs, vatId, legalEntity.isPhysicalPerson());
						
						String uri = qs.createAgentUri(vatId, legalEntity.isPhysicalPerson());
						
						//gr:vatId
						qs.insertVatId(Main.graphOrgs, uri, legalEntity.getVatNumber());
						
						agentUri = qs.getAgentUri(Main.graphOrgs, legalEntity.getVatNumber());
						
						//elod:validVatId
						qs.insertValidVatId(Main.graphOrgs, agentUri.getURI(), true);
						
						//elod:hasVatType
						qs.insertVatType(Main.graphOrgs, agentUri.getURI(), legalEntity.getCountryCode());
						
						//elod:isRegisteredAt
						qs.insertRegisteredAt(Main.graphOrgs, agentUri.getURI(), legalEntity.getCountryCode());
						
						//dcterms:created
						qs.insertCreated(Main.graphOrgs, agentUri.getURI());
						
					} else { /** invalid AFM **/
						
						qs.insertAgentUri(Main.graphOrgs, vatId, false);
						
						String uri = qs.createAgentUri(vatId, false);
						
						//gr:vatId
						qs.insertVatId(Main.graphOrgs, uri, legalEntity.getVatNumber());
						
						agentUri = qs.getAgentUri(Main.graphOrgs, legalEntity.getVatNumber());
						
						//elod:validVatId
						qs.insertValidVatId(Main.graphOrgs, agentUri.getURI(), false);
						
						//elod:hasVatType
						qs.insertVatType(Main.graphOrgs, agentUri.getURI(), legalEntity.getCountryCode());
						
						//elod:isRegisteredAt
						qs.insertRegisteredAt(Main.graphOrgs, agentUri.getURI(), legalEntity.getCountryCode());
						
						//dcterms:created
						qs.insertCreated(Main.graphOrgs, agentUri.getURI());
						
						isCompany = true;
					}
				}
			} else { //NO response from VatId.eu service
				qs.insertAgentUri(Main.graphOrgs, vatId, false);
				
				String uri = qs.createAgentUri(vatId, false);
				
				//gr:vatId
				qs.insertVatId(Main.graphOrgs, uri, vatId);
				
				//elod:validVatId
				qs.insertValidVatId(Main.graphOrgs, uri, false);
				
				//dcterms:created
				qs.insertCreated(Main.graphOrgs, uri);
				
				agentUri = qs.getAgentUri(Main.graphOrgs, vatId);
				
				isCompany = true;
			}
			
		} else {
			if (agentUri.getURI().contains("Organization")) {
				isCompany = true;
			}
		}
		
		return new Object[]{agentUri, vatId, isCompany};
	}

}