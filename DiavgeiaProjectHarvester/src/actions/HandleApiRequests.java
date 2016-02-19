package actions;

import http.HttpRequests;
import http.HttpResponse;
import http.IHttpRequestBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;

import objects.Decision;
import objects.DecisionSearchResult;
import objects.DictionaryItems;
import objects.Info;
import objects.Organization;
import objects.Organizations;
import objects.Position;
import objects.Positions;
import objects.Signer;
import objects.Signers;
import objects.Unit;
import objects.Units;
import utils.Configuration;
import utils.JsonUtil;
import utils.StringUtil;

/**
 * @author G. Razis
 * @author G. Vafeiadis
 */
public class HandleApiRequests {
	
	/**
     * Fetch all the decisions of the specific date.
     * 
     * @param SimpleDateFormat the date to search for decisions
     * @return List<Decision> list of decisions
     */
	public List<Decision> searchDecisions(String dateToSearch, Model model) {
		
		int pageNumber = 0;
		boolean fetchedAll = false;
		
		Configuration conf = null;
		HttpResponse response = null;
		
		List<Decision> descionsList = new ArrayList<Decision>();
		//read csv
		String csvFile = "csv_file/nsrf_projects.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		try {
			conf = new Configuration();
	        
			//csv
			br = new BufferedReader(new FileReader(csvFile));
			while(!fetchedAll) { //while results exist
				while ((line = br.readLine()) != null) {
				//csv
				String[] project = line.split(cvsSplitBy);
				System.out.println("Search: " +project[1]);
				String projects = project[1];
				String query = "/search/advanced?q=subject:\""+projects+"\""
						+ "&size=" + Configuration.RESULTS_PER_PAGE + "&page=" + pageNumber;
				
		        IHttpRequestBuilder req = HttpRequests.get(conf.getBaseUrl() + query);
		        
		        if (conf.isAuthenticationEnabled()) {
		        	System.out.println("Authenticated Request");
		            req.addCredentials(conf.getUsername(), conf.getPassword());
		        }
		        req.addHeader("Accept", "application/json");
		        
		        response = req.execute();
		        
		        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
		            String body = StringUtil.readInputStream(response.getBody());
		            
		            DecisionSearchResult searchResults = JsonUtil.fromString(body, DecisionSearchResult.class);
		            Info info = searchResults.getInfo();
		            
		            if (info.getActualSize() < Configuration.RESULTS_PER_PAGE) {
		            	fetchedAll = true;
		            } else {
		            	pageNumber += 1;
		            	System.out.println("Requesting page number: " + pageNumber + "\n");
		            }
		            System.out.println(String.format("Just retrieved %d decisions", info.getActualSize()));
		            System.out.println(String.format("Total matching decisions: %d", info.getTotal()));
		            System.out.println(String.format("Search query syntax: %s", info.getQuery()));
		            System.out.print("\n");
		            
		            descionsList.addAll(searchResults.getDecisions());
		        }
			}	
	
				
		}
        } catch (Exception e) {
        	e.printStackTrace();
		}
		
		return descionsList;
	}
	
	/**
     * Fetch the details of the specific signer id.
     * 
     * @param String the signer's id
     * @return Signer the provided signer's id object
     */
	public Signer searchSigner(String signerId) {
		
		Signer signerObject = null;
		
        Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        try {
        	conf = new Configuration();
        	
        	while (response == null) {
        		req = HttpRequests.get(conf.getBaseUrl() + "/signers/" + signerId);
        		req.addHeader("Accept", "application/json");
	        	response = req.execute();
			}
            
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                String body = StringUtil.readInputStream(response.getBody());
                signerObject = JsonUtil.fromString(body, Signer.class);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return signerObject;
	}
	
	/**
     * Fetch the details of the specific unit id.
     * 
     * @param String the unit's id
     * @return Unit the provided unit's id object
     */
	public Unit searchUnit(String unitId) {
		
		Unit unitObject = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        try {
        	conf = new Configuration();
        
        	while (response == null) {
        		req = HttpRequests.get(conf.getBaseUrl() + "/units/" + unitId);
        		req.addHeader("Accept", "application/json");
	        	response = req.execute();
			}
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	            String body = StringUtil.readInputStream(response.getBody());
	            unitObject = JsonUtil.fromString(body, Unit.class);
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return unitObject;
    }
	
	/**
     * Fetch the details of the specific organization id.
     * 
     * @param String the organization's id
     * @return Organization the provided organization's id object
     */
	public Organization searchOrganization(String orgId) {

		Organization orgObject = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
	        	//System.out.println("Retrieving the details of Organization " + orgId + "...\n");
		        req = HttpRequests.get(conf.getBaseUrl() + "/organizations/" + orgId);
		        req.addHeader("Accept", "application/json");
	        	response = req.execute();
			}
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	            String body = StringUtil.readInputStream(response.getBody());
	            orgObject = JsonUtil.fromString(body, Organization.class);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return orgObject;
    }
	
	/**
     * Fetch the details of the specific decision ada.
     * 
     * @param String the decision's ada
     * @return Decision the provided decision's ada object
     */
	public Decision searchSingleDecision(String ada) {
    	
		Decision decObj = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
		        req = HttpRequests.get(conf.getBaseUrl() + "/decisions/" + ada);
		        req.addHeader("Accept", "application/json");
	        	response = req.execute();
			}
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	            String body = StringUtil.readInputStream(response.getBody());
	            decObj = JsonUtil.fromString(body, Decision.class);
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return decObj;
    }
	
	/**
     * Fetch all the organizations provided by Di@vgeia.
     * 
     * @return Organizations an arraylist with the organizations
     */
	public Organizations getAllOrganizations() {
		
		Organizations organizationsList = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        System.out.println("Retrieving all Organizations...\n");
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
		        req = HttpRequests.get(conf.getBaseUrl() + "/organizations");
		        req.addHeader("Accept", "application/json");
		        response = req.execute();
	        }
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	            String body = StringUtil.readInputStream(response.getBody());
	            organizationsList = JsonUtil.fromString(body, Organizations.class);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return organizationsList;
	}
	
	/**
     * Fetch the units of the provided organization.
     * 
     * @param String the id of the investigated organization
     * @return Units an arraylist with the units of the provided organization
     */
	public Units getOrganizationUnits(String orgId) {
		
		Units unitsList = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        System.out.println("Retrieving the Units of Organization " + orgId + "...\n");
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
		        req = HttpRequests.get(conf.getBaseUrl() + "/organizations/" + orgId + "/units");
		        req.addHeader("Accept", "application/json");
		        response = req.execute();
	        }
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	        	String body = StringUtil.readInputStream(response.getBody());
	            unitsList = JsonUtil.fromString(body, Units.class);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return unitsList;
	}
	
	/**
     * Fetch the signers of the provided organization.
     * 
     * @param String the id of the investigated organization
     * @return Signers an arraylist with the signers of the provided organization
     */
	public Signers getOrganizationSigners(String orgId) {
		
		Signers signersList = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        System.out.println("Retrieving the Signers of Organization " + orgId + "...\n");
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
		        req = HttpRequests.get(conf.getBaseUrl() + "/organizations/" + orgId + "/signers");
		        req.addHeader("Accept", "application/json");
		        response = req.execute();
	        }
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	        	String body = StringUtil.readInputStream(response.getBody());
	        	signersList = JsonUtil.fromString(body, Signers.class);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return signersList;
	}
	
	/**
     * Fetch the items of the dictionary as provided by Di@vgeia.
     * 
     * @param String the name of the investigated dictionary
     * @return DictionaryItems an arraylist with the dictionary items as provided by Di@vgeia
     */
	public DictionaryItems getDictionaryItems(String dictName) {
		
		DictionaryItems dictItemsList = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        System.out.println("Retrieving Dictionary " + dictName + "...\n");
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
		        req = HttpRequests.get(conf.getBaseUrl() + "/dictionaries/" + dictName);
		        req.addHeader("Accept", "application/json");
		        response = req.execute();
	        }
	        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	        	String body = StringUtil.readInputStream(response.getBody());
	        	dictItemsList = JsonUtil.fromString(body, DictionaryItems.class);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return dictItemsList;
	}
	
	/**
     * Fetch the all the positions as provided by Di@vgeia. 
     * 
     * @param String the name of the investigated dictionary
     * @return ArrayList<Position> an arraylist with the positions
     */
	public ArrayList<Position> getAllPositions() {
		
		ArrayList<Position> uniquePositions = null;
		
		Configuration conf = null;
        HttpResponse response = null;
        IHttpRequestBuilder req = null;
        
        System.out.println("Retrieving Positions...\n");
        
        try {
	        conf = new Configuration();
	        
	        while (response == null) {
		        req = HttpRequests.get(conf.getBaseUrl() + "/positions");
		        req.addHeader("Accept", "application/json");
		        response = req.execute();
	        }
        
	        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
	            String body = StringUtil.readInputStream(response.getBody());
	            Positions positions = JsonUtil.fromString(body, Positions.class);
	            
	            uniquePositions = new ArrayList<Position>();
	            Set<String> tempList = new HashSet<String>();
	            
	            //Remove the duplicate positions
	            for (Position pos : positions.getPositions()) {
	            	if (tempList.add(pos.getUid())) {
	                	uniquePositions.add(pos);
	                }
	            }
	        }
        } catch (Exception e) {
	    	e.printStackTrace();
	    }
        
		return uniquePositions;
	}
	
}