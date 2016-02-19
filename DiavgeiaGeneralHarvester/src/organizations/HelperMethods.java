package organizations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;

import java.util.Calendar;

/**
 * @author G. Razis
 */
public class HelperMethods {
	
	/**
     * Clean and the return the new representation of a provided Vat Id.
     * 
     * @param String the vatId
     * @return Object[] whether the Vat Id was cleaned and its cleaned value
     */
	public Object[] cleanVatId(String vatId) {
		
		boolean cleaned = false;
		
		if (vatId.startsWith("O")) {
			vatId = vatId.replaceFirst("O", "0");
			cleaned = true;
		}
		
		if (vatId.contains("ΑΦΜ.")) {
			vatId = vatId.replace("ΑΦΜ.", "");
			cleaned = true;
		}
		
		if (vatId.contains("ΑΦΜ")) {
			vatId = vatId.replace("ΑΦΜ", "");
			cleaned = true;
		}
		
		return new Object[]{cleaned, vatId};
	}
	
	/**
     * Convert a number to its boolean representation.
     * 
     * @param int a number
     * @return boolean the boolean representation of the provided number
     */
	public boolean convertNumberToBoolean(int aNumber) {
		
		boolean response = false;
		
		if (aNumber == 1) {
			response = true;
		}
		
		return response;
	}
	
	/**
     * Find and transform the current date into the yyyy-MM-dd format.
     * 
     * @return String the current date in the yyyy-MM-dd format
     */
	public String getCurrentDate() {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(cal.getTime());
		
		return currentDate;
	}
	
	/**
     * Write in a file the data.
     * 
     * @param String the output filename
     * @param String the data
     */
	public void writeToFile(String fileName, String data) {
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName + ".txt", true)));
		    out.println(data);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}

}