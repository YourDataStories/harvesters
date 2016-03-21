package organizations;

import organizations.QueryConfiguration;

import objects.LegalEntity;

import utils.CountryOriented;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author G. Razis
 */
public class Queries {
	
	private HelperMethods hm = new HelperMethods();
	private CountryOriented co = new CountryOriented();
	
	public Resource getAgentUri(VirtGraph graphOrgs, String vatId) {
		
		Resource agentUri = null;
		
		String queryUri = "PREFIX gr: <http://purl.org/goodrelations/v1#> " +
					"SELECT ?org ?vatId " +
					"FROM <" + QueryConfiguration.queryGraphOrganizations + "> " +
					"WHERE { " +
					"?org gr:vatID ?vatId . " +
					"FILTER ( ?vatId = \"" + vatId + "\"^^<http://www.w3.org/2001/XMLSchema#string> ) " +
					"}";

		VirtuosoQueryExecution vqeUri = VirtuosoQueryExecutionFactory.create(queryUri, graphOrgs);		
		ResultSet resultsUri = vqeUri.execSelect();
		
		if (resultsUri.hasNext()) {
			QuerySolution result = resultsUri.nextSolution();
			agentUri = result.getResource("org");
		}
		
		vqeUri.close();
		
		return agentUri;
	}
	
	public Resource getAgentUriNoVat(VirtGraph graphOrgs, String vatId) {
		
		Resource agentUri = null;
		
		String queryUri = "PREFIX gr: <http://purl.org/goodrelations/v1#> " +
					"SELECT DISTINCT ?org " +
					"FROM <" + QueryConfiguration.queryGraphOrganizations + "> " +
					"WHERE { " +
					"?org rdf:type foaf:Organization . " +
					"FILTER (STR(?org) = \"http://linkedeconomy.org/resource/Organization/" + vatId + "\") . " +
					"}";

		VirtuosoQueryExecution vqeUri = VirtuosoQueryExecutionFactory.create(queryUri, graphOrgs);		
		ResultSet resultsUri = vqeUri.execSelect();
		
		if (resultsUri.hasNext()) {
			QuerySolution result = resultsUri.nextSolution();
			agentUri = result.getResource("org");
		}
		
		vqeUri.close();
		
		return agentUri;
	}

	public String createAgentUri(String vatId, boolean personFlag) {
		
		String agentUri = "http://linkedeconomy.org/resource/";
		
		if (!personFlag) {
			agentUri += "Organization/" + vatId;
		} else {
			agentUri += "Person/" + vatId;
		}
		
		return agentUri;
	}
	
	/** Insertions **/
	public void insertAgentUri(VirtGraph graphOrgs, String vatId, boolean personFlag) {
		
		String insertData = null;
		String agentUri = "http://linkedeconomy.org/resource/";
		
		if (!personFlag) {
			agentUri += "Organization/" + vatId;
			insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
					"{ <" + agentUri + "> rdf:type gr:BusinessEntity ; " +
								   	   "rdf:type foaf:Organization ; " +
								   	   "rdf:type org:Organization ; " +
								   	   "rdf:type rov:RegisteredOrganization . " +
					"}";
		} else {
			agentUri += "Person/" + vatId;
			insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
					"{ <" + agentUri + "> rdf:type foaf:Person . }";
		}
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(agentUri + " inserted");
	}
	
	public void insertLegalName(VirtGraph graphOrgs, String orgUri, String legalName) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> gr:legalName \"" + legalName + "\"^^<http://www.w3.org/2001/XMLSchema#string> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " gr:legalName inserted");
	}
	
	/**
     * Insert the transliterated legal name of the Organization.
     * 
     * @param VirtGraph the Organizations graph
     * @param String the URI of the Organization
     * @param String the transliterated legal name
     */
	public void insertTransliteration(VirtGraph graphOrgs, String orgUri, String transLegalName) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> elod:transliterationLegalName \"" + transLegalName + "\"^^<http://www.w3.org/2001/XMLSchema#string> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " elod:transliterationLegalName inserted");
	}
	
	public void insertName(VirtGraph graphOrgs, String orgUri, String name) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> gr:name \"" + name + "\"^^<http://www.w3.org/2001/XMLSchema#string> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " gr:name inserted");
	}
	
	public void insertVatId(VirtGraph graphOrgs, String orgUri, String vatId) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> gr:vatID \"" + vatId + "\"^^<http://www.w3.org/2001/XMLSchema#string> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " gr:vatID inserted");
	}

	public void insertValidVatId(VirtGraph graphOrgs, String orgUri, boolean isValid) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> elod:validVatId \"" + String.valueOf(isValid) + "\"^^<http://www.w3.org/2001/XMLSchema#boolean> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " elod:validVatId inserted");
	}
	
	public void insertAddress(VirtGraph graphOrgs, String orgUri, String vatId, LegalEntity legalEntity) {
		
		String addressUri = "http://linkedeconomy.org/resource/Address/" + vatId;
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
							"{ <" + orgUri + "> vcard2006:hasAddress <" + addressUri + "> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " vcard:hasAddress inserted");
		
		insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + addressUri + "> rdf:type vcard2006:Address ";
		
		if ( (legalEntity.getAddressName() != null) && (legalEntity.getAddressNumber() != null) ) {
			insertData += "; vcard2006:street-address \"" + legalEntity.getAddressName() + " " + legalEntity.getAddressNumber() + "\"@el ";
		} else if (legalEntity.getAddressName() != null) {
			insertData += "; vcard2006:street-address \"" + legalEntity.getAddressName() + "\"@el ";
		} else if (legalEntity.getAddressNumber() != null) {
			insertData += "; vcard2006:street-address \"" + legalEntity.getAddressNumber() + "\"@el ";
		}
		if (legalEntity.getPostalCode() != null) {
			insertData += "; vcard2006:postal-code \"" + legalEntity.getPostalCode() + "\"^^<http://www.w3.org/2001/XMLSchema#string> ";
		}
		if (legalEntity.getAddressRegion() != null) {
			insertData += "; vcard2006:locality \"" + legalEntity.getAddressRegion() + "\"@el ";
		}
		if (legalEntity.getCountryCode() != null) {
			insertData += "; vcard2006:country-name \"" + co.findCountryFromAbbreviation(legalEntity.getCountryCode())[0] + "\"@el ";
		}
		
		insertData += ". }";
		
		vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " Address Details added");
	}
	
	public void insertCreated(VirtGraph graphOrgs, String orgUri) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> dcterms:created \"" + hm.getCurrentDate() + "\"^^<http://www.w3.org/2001/XMLSchema#date> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " dcterms:created inserted");
	}
	
	public void insertVatType(VirtGraph graphOrgs, String orgUri, String countryCode) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> elod:hasVatType <http://linkedeconomy.org/resource/VatType/" + countryCode + "> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " elod:hasVatType inserted");
	}

	public void insertRegisteredAt(VirtGraph graphOrgs, String orgUri, String countryCode) {
	
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> elod:isRegisteredAt <http://linkedeconomy.org/resource/Country/" + countryCode + "> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " elod:isRegisteredAt inserted");
	}
	
	public void insertNoVatOrgId(VirtGraph graphOrgs, String orgUri, String noVatOrgId) {
		
		String insertData = "INSERT INTO GRAPH <" + QueryConfiguration.queryGraphOrganizations + "> " +
				"{ <" + orgUri + "> gr:noVatOrgId \"" + noVatOrgId + "\"^^<http://www.w3.org/2001/XMLSchema#string> . }";
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertData, graphOrgs);
		vur.exec();
		
		System.out.println(orgUri + " gr:noVatOrgId inserted");
	}

}