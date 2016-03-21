package utils;

import java.util.HashMap;

/**
 * @author G. Razis
 * @see http://www.translitteration.com/transliteration/en/greek/un-elot/ (UN/ELOT same as ISO 843)
 */
public class TransliterationMethods {
		
	/**
     * Transliterate the provided character (if applicable).
     * 
     * @return HashMap<String, String> a HashMap containing the 
     * Greek letter and its transliterated representation
     */
	private HashMap<String, String> createTransliterationMap() {
		
		HashMap<String, String> conversionMap = new HashMap<String, String>();
		
		conversionMap.put("α", "a");
		conversionMap.put("β", "v");
		conversionMap.put("γ", "g");
		conversionMap.put("δ", "d");
		conversionMap.put("ε", "e");
		conversionMap.put("ζ", "z");
		conversionMap.put("η", "i");
		conversionMap.put("θ", "th");
		conversionMap.put("ι", "i");
		conversionMap.put("κ", "k");
		conversionMap.put("λ", "l");
		conversionMap.put("μ", "m");
		conversionMap.put("ν", "n");
		conversionMap.put("ξ", "x");
		conversionMap.put("ο", "o");
		conversionMap.put("π", "p");
		conversionMap.put("ρ", "r");
		conversionMap.put("σ", "s");
		conversionMap.put("τ", "t");
		conversionMap.put("υ", "y");
		conversionMap.put("φ", "f");
		conversionMap.put("χ", "ch");
		conversionMap.put("ψ", "ps");
		conversionMap.put("ω", "o");
		conversionMap.put("Α", "A");
		conversionMap.put("Β", "V");
		conversionMap.put("Γ", "G");
		conversionMap.put("Δ", "D");
		conversionMap.put("Ε", "E");
		conversionMap.put("Ζ", "Z");
		conversionMap.put("Η", "I");
		conversionMap.put("Θ", "TH");
		conversionMap.put("Ι", "I");
		conversionMap.put("Κ", "K");
		conversionMap.put("Λ", "L");
		conversionMap.put("Μ", "M");
		conversionMap.put("Ν", "N");
		conversionMap.put("Ξ", "X");
		conversionMap.put("Ο", "O");
		conversionMap.put("Π", "P");
		conversionMap.put("Ρ", "R");
		conversionMap.put("Σ", "S");
		conversionMap.put("Τ", "T");
		conversionMap.put("Υ", "Y");
		conversionMap.put("Φ", "F");
		conversionMap.put("Χ", "CH");
		conversionMap.put("Ψ", "PS");
		conversionMap.put("Ω", "O");	
		conversionMap.put("ά", "a");
		conversionMap.put("έ", "e");
		conversionMap.put("ή", "i");
		conversionMap.put("ί", "i");
		conversionMap.put("ϊ", "i");
		conversionMap.put("ΐ", "i");
		conversionMap.put("ό", "o");
		conversionMap.put("ύ", "y");
		conversionMap.put("ώ", "o");
		conversionMap.put("ς", "s");
		
		return conversionMap;
	}

	/**
     * Transliterate the provided character (if applicable).
     * 
     * @param String a character to transliterate
     * @return String the transliterated representation of the character (if applicable) 
     */
	private String transliterateCharacter(HashMap<String, String> conversionMap, String charToTrans) {
		
		String tempStr = charToTrans;		
		
		tempStr = conversionMap.get(charToTrans);			
		
		if (tempStr == null) {
			return charToTrans;
		} else {
			return tempStr;
		}
	}
	
	/**
     * Transliterate the provided legal name (if applicable) according to 
     * the UN/ELOT - ISO 843 standard.
     * 
     * @param String a string to transliterate
     * @return String the transliterated representation of the string (if applicable)
     */
	public String transliterationGenerator(String legalName) {
		
		String transliterated = "";
		
		legalName = cleanInvalidChars(legalName);
		
		HashMap<String, String> conversionMap = createTransliterationMap();
		
		for (int i = 0; i < legalName.length(); i++) {
		    char c = legalName.charAt(i);		    
		    transliterated += transliterateCharacter(conversionMap, Character.toString(c));
		}
		
		return transliterated;
	}
	
	/**
     * Clean the string from invalid characters and return its proper format.
     * 
     * @param String a field which may need cleansing from non-printable data
     * @return String the cleaned data of the field.
     */
	private String cleanInvalidChars(String field) {

		if (field.contains("\"")) {
			field = field.replace("\"", "");
		}
		
		if (field.contains("\\")) {
			field = field.replace("\\", "");
		}
		
		return field;
	}
	
}