package actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import objects.LegalEntity;

import utils.HelperMethods;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author G. Razis
 */
public class VatIdSite {
	
	/**
     * Find the legal details of an organization as retrieved from the VatId.eu service.
     * 
     * @param String the Vat Id as retrieved from the Decision
     * @param String the ADA code of the Decision
     * @return Set <Map.Entry<String, JsonElement>> the legal details of the organization
     */
	public Set <Map.Entry<String, JsonElement>> afmPrefixChecker(String afm, String doyCode) {
		
		Set <Map.Entry<String, JsonElement>> entrySet = null;
		HelperMethods hm = new HelperMethods();
		
		afm = afm.replace(" ", ""); //remove space from Vat Id number
		
		if (afm.equals("057905659")) { //unable to respond
			return entrySet;
		}
		
		if (afm.length() == 9) {
			if (afm.matches("\\d+")) { //only digits -> Greek AFM
				while (entrySet == null) {
					entrySet = vatIdOnlineChecker("EL", afm);
				}
			} else { //error AFM
				System.out.println("Error in AFM: " + afm);
			}
		} else { //possibly foreign AFM, Maybe error
			try {
				if (afm.matches("[a-zA-Z0-9 ]*")) { //No special characters in it
					if (afm.substring(0, 3).matches("[A-Z]*")) { //ie: ATU -> Austria
						String afmPrefix = null;
						if (afm.substring(0, 3).equalsIgnoreCase("ATU")) {
							afmPrefix = "AT";
						} else {
							hm.writeMetadata("newAfmPrefixes", doyCode, new String[] {afm.substring(0, 3), afm.substring(3, afm.length())});
						}
						while (entrySet == null) {
							if (afmPrefix != null) {
								entrySet = vatIdOnlineChecker(afmPrefix, afm.substring(0, 3));
							} else {
								break;
							}
						}
					} else if (afm.substring(0, 2).matches("[A-Z]*")) { //ie: DE -> Germany
						while (entrySet == null) {
							if (afm.substring(0, 2).equalsIgnoreCase("CB")) {
								entrySet = vatIdOnlineChecker("GB", afm.substring(2, afm.length()));
							} else if (afm.substring(0, 2).equalsIgnoreCase("NO")) { //handle special case of Norwegian AFM
								entrySet = createSpecificAfmSet(afm.substring(0, 2), afm.substring(2, afm.length()));
							} else {
								entrySet = vatIdOnlineChecker(afm.substring(0, 2), afm.substring(2, afm.length()));
							}
						}
					} else { //unknown error
						System.out.println("Error in AFM: " + afm);
					}
				} else {
					System.out.println("There are special characters in the AFM.\n");
				}
			} catch (Exception e) {
				System.out.println("Error in AFM: " + afm);
			}
		}
		
		return entrySet;
		
	}
	
	/**
     * Create a custom Set of details regarding an organization.
     * 
     * @param String the ISO code of the country
     * @param String the Vat Id of the organization
     * @return Set<Entry<String, JsonElement>> the Set of custom data containing the details of the organization
     */
	private Set<Entry<String, JsonElement>> createSpecificAfmSet(String countryCode, String vatNumber) {
		
		Set<Entry<String, JsonElement>> entrySet = null;
	    
		String dataAsJson = "{'response': {'country_code': '" + countryCode + "','vat_number': '" + vatNumber + "','valid': 'false'}}";
	    JsonParser parser = new JsonParser();
        JsonObject jObj = parser.parse(dataAsJson).getAsJsonObject();
        JsonObject rootObj = (JsonObject) jObj.get("response");
        entrySet = rootObj.entrySet();
	      
		return entrySet;
	}
	
	/**
     * Create the Set of details regarding an organization. The details may be retrieved from the VatId.eu 
     * service (case of valid Vat Id) but may also be customly created (case of invalid country parameter).
     * 
     * @param String the ISO code of the country
     * @param String the Vat Id of the organization
     * @return Set <Map.Entry<String, JsonElement>> the Set of data containing the details of the organization
     */
	public Set <Map.Entry<String, JsonElement>> vatIdOnlineChecker(String countryCode, String vatNumber) {
		
		String url = "http://vatid.eu/check/" + countryCode + "/" + vatNumber.trim();
		Set <Map.Entry<String, JsonElement>> entrySet = null;
        
        try {
        	int responseCode = 0;
        	HttpURLConnection connection = null;
        	URL urlObj = null;
        	
        	while (responseCode != 200) {
				
        		urlObj = new URL(url);
        		
	        	connection = (HttpURLConnection) urlObj.openConnection();
	    		connection.setRequestMethod("GET"); //optional, default is GET
	    		connection.setRequestProperty("Accept", "application/json"); //add request header
	            
	            responseCode = connection.getResponseCode();
        	}
        	
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            String inputLine;
            String response = "";
            
            while ((inputLine = in.readLine()) != null) {
            	response += inputLine.trim();
                System.out.println(inputLine);
            }
            
            in.close();
            
            JsonParser parser = new JsonParser();
            JsonObject jObj = parser.parse(response).getAsJsonObject();
            JsonObject rootObj = null;
            try {
            	rootObj = (JsonObject) jObj.get("response");
            	entrySet = rootObj.entrySet();
			} catch (Exception e) {
				entrySet = invalidCountryCodeFix(jObj, countryCode, vatNumber.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return entrySet;
        
	}
	
	/**
     * Create a custom Set of legal details of an organization due to invalid country parameter.
     * 
     * @param JsonObject the response of VatId.eu service in JSON format
     * @param String the ISO code of the country
     * @param String the Vat Id of the organization
     * @return Set <Map.Entry<String, JsonElement>> the Set of legal details of the organization
     */
	private Set <Map.Entry<String, JsonElement>> invalidCountryCodeFix(JsonObject jObj, String countryCode, String vatNumber) {
		
		Set <Map.Entry<String, JsonElement>> entrySet = null;
		
		JsonObject rootObj = (JsonObject) jObj.get("error");
		String errorCode = null;
		String jsonValues = null;
		for (Map.Entry<String, JsonElement> entry : rootObj.entrySet()) {
			jsonValues = entry.getValue().toString().replace("\"", "");
			if (entry.getKey().equalsIgnoreCase("code")) {
				errorCode = jsonValues;
				break;
        	}
		}
		if (errorCode.equalsIgnoreCase("invalid-input")) { //invalid country code
			entrySet = createSpecificAfmSet(countryCode, vatNumber);
		} else { //other unknown error
			entrySet = null;
		}
		
		return entrySet;
	}
	
	/**
     * Create the custom LegalEntity object of the organization from the Set of data 
     * containing these details as retrieved from the VatId.eu service.
     * 
     * @param Set<Map.Entry<String, JsonElement>> the Set containing the details 
     * of the organization as retrieved from the VatId.eu service
     * @return LegalEntity the custom LegalEntity object of the organization
     */
	public LegalEntity createLegalEntity(Set<Map.Entry<String, JsonElement>> entrySet) {
		
		HelperMethods hm = new HelperMethods();
		
		boolean valid = false;
		boolean physicalPerson = false;
		String name = null;
		String vatNumber = null;
		String countryCode = null;
		String addressName = null;
		String addressNumber = null;
		String postalCode = null;
		String addressRegion = null;
		String jsonValues = null;
		
		for (Map.Entry<String, JsonElement> entry:entrySet) {
			
			jsonValues = entry.getValue().toString().replace("\"", ""); //clean data from special chars: ",'
			
        	//System.out.println(entry.getKey() + " -> " + jsonValues);
        	
        	if (entry.getKey().equalsIgnoreCase("country_code")) {
        		countryCode = jsonValues;
        	} else if (entry.getKey().equalsIgnoreCase("vat_number")) {
        		vatNumber = jsonValues;
        	} else if (entry.getKey().equalsIgnoreCase("valid")) {
        		String input = jsonValues.substring(0, 1).toUpperCase() + jsonValues.substring(1);
        		valid = Boolean.parseBoolean(input);
        	} else if (entry.getKey().equalsIgnoreCase("name")) {
        		if (jsonValues.equalsIgnoreCase("---")) {
        			name = null;
        		} else if (jsonValues.equalsIgnoreCase("")) {
        			physicalPerson = true;
        		} else {
        			name = hm.cleanInputData(jsonValues);
        		}
        	} else if (entry.getKey().equalsIgnoreCase("address")) {
        		String[] addresDtlsExtractedData = findLegalEntityAddressDtls(jsonValues);
        		addressName = addresDtlsExtractedData[0];
        		addressNumber = addresDtlsExtractedData[1];
        		postalCode = addresDtlsExtractedData[2];
        		addressRegion = addresDtlsExtractedData[3];
        	}
        }
		
		LegalEntity entity = new LegalEntity(valid, physicalPerson, name, vatNumber, countryCode, addressName, 
											 addressNumber, postalCode, addressRegion);
		
		return entity;
	}
	
	/**
     * Convert the address details as retrieved from the VatId.eu service in the form of: 
     * Street name - Street number - Postal Code - Region.
     * 
     * @param String the address details as retrieved from the VatId.eu service
     * @return String[] the address details of the organization in the form of: 
     * Street name - Street number - Postal Code - Region
     */
	public String[] findLegalEntityAddressDtls(String addressData) {
		
		String[] addressDtlsExtractedData = new String[4];
		String[] tempData = new String[2];
		String[] addrTempData = new String[3];
		
		String address = "";
		String seperator = " - ";
		
		String[] counterIndex = addressSeparatorCounterIndex(addressData, seperator);
		
		if (Integer.valueOf(counterIndex[0]) == 0) {
			addressData = addressData.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2").replace("\t"," ");
			addressData = addressData.replace("\\n"," ");
			addressData = addressData.replace("\\","");
			addressData = addressData.replace("\"","");
			addressData = addressData.trim();
			
			addrTempData = addressData.split(" ");
			
			int l = addrTempData.length;
			
			if (l > 1) {
				if (l > 3) {
					for (int i = l; i > 2; i--) {
						address += " " + addrTempData[l-i];
					}
					addressDtlsExtractedData[0] = address.trim();
				} else {
					addressDtlsExtractedData[0] = addrTempData[0];
				}
				
				if (l > 3) {
					addressDtlsExtractedData[1] = addrTempData[l-4].trim();
					addressDtlsExtractedData[2] = addrTempData[l-3].trim();
					addressDtlsExtractedData[3] = addrTempData[l-2].trim() + " " + 
												  addrTempData[l-1].trim();
				} else {
					for (int i = 1; i < l; i++) {
						addressDtlsExtractedData[i] = addrTempData[l-i].trim();
					}
					for (int i = 3; i >= l; i--) {
						addressDtlsExtractedData[i] = "-";
					}
				}
			}
		} else if (Integer.valueOf(counterIndex[0]) == 1) {
			tempData = addressData.split(" - ");
			tempData[0] = tempData[0].replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2").replace("\t"," ");
			tempData[0] = tempData[0].replace("\\n"," ");
			tempData[0] = tempData[0].replace("\\","");
			tempData[0] = tempData[0].replace("\"","");
			tempData[0] = tempData[0].trim();
			
			addrTempData = tempData[0].split(" ");
			
			int l = addrTempData.length;
			
			if (l > 3) {
				for (int i = l; i > 2; i--) {
					address += " " + addrTempData[l-i];
				}
				addressDtlsExtractedData[0] = address.trim();
			} else {
				addressDtlsExtractedData[0] = addrTempData[0];
			}
			
			addressDtlsExtractedData[1] = addrTempData[l-2].trim();
			addressDtlsExtractedData[2] = addrTempData[l-1].trim();
			addressDtlsExtractedData[3] = tempData[1].trim();
		} else if (Integer.valueOf(counterIndex[0]) > 1) {
			tempData[0] =  addressData.substring(0, Integer.valueOf(counterIndex[1])).replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2").replace("\t"," ");
			tempData[0] = tempData[0].replace("\\n"," ");
			tempData[0] = tempData[0].replace("\\","");
			tempData[0] = tempData[0].replace("\"","");
			tempData[0] = tempData[0].trim();
			tempData[1] =  addressData.substring(Integer.valueOf(counterIndex[1]) + seperator.length());
			
			addrTempData = tempData[0].split(" ");
			
			int l = addrTempData.length;
				
			if (l > 3) {
				for (int i = l; i > 2; i--) {
					address += " " + addrTempData[l-i];
				}
				addressDtlsExtractedData[0] = address.trim();
			} else {
				addressDtlsExtractedData[0] = addrTempData[0];
			}
			
			addressDtlsExtractedData[1] = addrTempData[l-2].trim();
			addressDtlsExtractedData[2] = addrTempData[l-1].trim();
			addressDtlsExtractedData[3] = tempData[1].trim();
		}
		
		return addressDtlsExtractedData;
	}
	
	/**
     * Return the number of appearances of the separator and its last index.
     * 
     * @param String the address details as retrieved from the VatId.eu service
     * @return String[] the number of appearances of the separator and its last index
     */
	private String[] addressSeparatorCounterIndex(String addressData, String seperator) {
		
		int lastIndexSaved = 0;
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {
			lastIndex = addressData.indexOf(seperator, lastIndex);

			if (lastIndex != -1) {
				count++;
				lastIndexSaved = lastIndex;
				lastIndex += seperator.length();
			}
		}
		
		String[] counterIndex = {String.valueOf(count), String.valueOf(lastIndexSaved)};
		
		return counterIndex;
	}

}