package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author G. Razis
 */
public class CountryOriented {
	
	/**
     * Find the the official Greek and English name of the country.
     * 
     * @param String the abbreviation of the country
     * @return String[] the official Greek and English name of the country
     */
	public String[] findCountryFromAbbreviation(String abbreviation) {
		
		String[] country = null;
		
		if (abbreviation.equalsIgnoreCase("AT")) {
			country = new String[]{"Αυστρία", "Austria"};
		} else if (abbreviation.equalsIgnoreCase("BE")) {
			country = new String[]{"Βέλγιο", "Belgium"};
		} else if (abbreviation.equalsIgnoreCase("BG")) {
			country = new String[]{"Βουλγαρία", "Bulgaria"};
		} else if (abbreviation.equalsIgnoreCase("CY")) {
			country = new String[]{"Κύπρος", "Cyprus"};
		}  else if (abbreviation.equalsIgnoreCase("CZ")) {
			country = new String[]{"Τσεχική Δημοκρατία", "Czech Republic"};
		} else if (abbreviation.equalsIgnoreCase("DE")) {
			country = new String[]{"Γερμανία", "Germany"};
		} else if (abbreviation.equalsIgnoreCase("DK")) {
			country = new String[]{"Δανία", "Denmark"};
		} else if (abbreviation.equalsIgnoreCase("EE")) {
			country = new String[]{"Εσθονία", "Estonia"};
		} else if (abbreviation.equalsIgnoreCase("ES")) {
			country = new String[]{"Ισπανία (δεν συμπεριλ. XC, XL)", "Spain (not incl XC, XL)"};
		} else if (abbreviation.equalsIgnoreCase("GR") || abbreviation.equalsIgnoreCase("EL")) {
			country = new String[]{"Ελλάδα", "Greece"};
		} else if (abbreviation.equalsIgnoreCase("FI")) {
			country = new String[]{"Φινλανδία", "Finland"};
		} else if (abbreviation.equalsIgnoreCase("FR")) {
			country = new String[]{"Γαλλία", "France"};
		} else if (abbreviation.equalsIgnoreCase("GB")) {
			country = new String[]{"Ηνωμένο Βασίλειο", "Great Britain"};
		} else if (abbreviation.equalsIgnoreCase("HR")) {
			country = new String[]{"Κροατία", "Croatia"};
		} else if (abbreviation.equalsIgnoreCase("HU")) {
			country = new String[]{"Ουγγαρία", "Hungary"};
		} else if (abbreviation.equalsIgnoreCase("IE")) {
			country = new String[]{"Ιρλανδία", "Ireland"};
		} else if (abbreviation.equalsIgnoreCase("IT")) {
			country = new String[]{"Ιταλία", "Italy"};
		} else if (abbreviation.equalsIgnoreCase("LT")) {
			country = new String[]{"Λιθουανία", "Lithuania"};
		} else if (abbreviation.equalsIgnoreCase("LU")) {
			country = new String[]{"Λουξεμβούργο", "Luxembourg"};
		} else if (abbreviation.equalsIgnoreCase("LV")) {
			country = new String[]{"Λεττονία", "Latvia"};
		} else if (abbreviation.equalsIgnoreCase("MT")) {
			country = new String[]{"Μάλτα", "Malta"};
		} else if (abbreviation.equalsIgnoreCase("NL")) {
			country = new String[]{"Κάτω Χώρες (Ολλανδία)", "Netherlands"};
		} else if (abbreviation.equalsIgnoreCase("PL")) {
			country = new String[]{"Πολωνία", "Poland"};
		} else if (abbreviation.equalsIgnoreCase("PT")) {
			country = new String[]{"Πορτογαλία", "Portugal"};
		} else if (abbreviation.equalsIgnoreCase("RO")) {
			country = new String[]{"Ρουμανία (Ρωμανία)", "Romania"};
		} else if (abbreviation.equalsIgnoreCase("SE")) {
			country = new String[]{"Σουηδία", "Sweden"};
		} else if (abbreviation.equalsIgnoreCase("SI")) {
			country = new String[]{"Σλοβενία", "Slovenia"};
		} else if (abbreviation.equalsIgnoreCase("SK")) {
			country = new String[]{"Σλοβακίας", "Slovak Republic"};
		} else if (abbreviation.equalsIgnoreCase("MD")) {
			country = new String[]{"Δημοκρατία της Μολδαβίας", "Republic of Moldova"};
		} else if (abbreviation.equalsIgnoreCase("RU")) {
			country = new String[]{"Ρωσική Ομοσπονδία", "Russian Federation"};
		} else {
			writeUnknownAbbreviation(abbreviation);
		}
		
		return country;
	}
	
	/**
     * Find the the official Greek and English name of the currency.
     * 
     * @param String the abbreviation of the currency
     * @return String[] the official Greek and English name of the currency
     */
	public String[] findCurrencyFromAbbreviation(String currencyCode) {
		
		String[] currency = null;
		
		if (currencyCode.equalsIgnoreCase("AED")) {
			currency = new String[] {"Ντιράμ Ηνωμένων Αραβικών Εμιράτων", "United Arab Emirates Dirham"};
		} else if (currencyCode.equalsIgnoreCase("AFN")) {
			currency = new String[] {"Αφγανί Αφγανιστάν", "Afghan Afghani"};
		} else if (currencyCode.equalsIgnoreCase("ALL")) {
			currency = new String[] {"Λεκ Αλβανίας", "Albanian Lek"};
		} else if (currencyCode.equalsIgnoreCase("AMD")) {
			currency = new String[] {"Ντραμ Αρμενίας", "Armenian Dram"};
		} else if (currencyCode.equalsIgnoreCase("AOA")) {
			currency = new String[] {"Κουάνζα Ανγκόλας", "Angolan Kwanza"};
		} else if (currencyCode.equalsIgnoreCase("ARS")) {
			currency = new String[] {"Πέσο Αργεντινής", "Argentine Peso"};
		} else if (currencyCode.equalsIgnoreCase("AUD")) {
			currency = new String[] {"Δολάριο Αυστραλίας", "Australian Dollar"};
		} else if (currencyCode.equalsIgnoreCase("AWG")) {
			currency = new String[] {"Γκίλντα Αρούμπα", "Aruban Guilder"};
		} else if (currencyCode.equalsIgnoreCase("AZN")) {
			currency = new String[] {"Μανάτ Αζερμπαϊτζάν", "Azerbaijani Manat"};
		} else if (currencyCode.equalsIgnoreCase("BAM")) {
			currency = new String[] {"Μετατρέψιμο Μάρκο Βοσνίας-Ερζεγοβίνης", "Bosnia and Herzegovina Convertible Marka"};
		} else if (currencyCode.equalsIgnoreCase("BBD")) {
			currency = new String[] {"Δολάριο Μπαρμπάντος", "Barbados Dollar"};
		} else if (currencyCode.equalsIgnoreCase("BDT")) {
			currency = new String[] {"Τάκα Μπαγκλαντές", "Bangladeshi Taka"};
		} else if (currencyCode.equalsIgnoreCase("BGN")) {
			currency = new String[] {"Νέο Λεβ Βουλγαρίας", "New Bulgaria Lev"};
		} else if (currencyCode.equalsIgnoreCase("BHD")) {
			currency = new String[] {"Δηνάριο Μπαχρέιν", "Bahraini Dinar"};
		} else if (currencyCode.equalsIgnoreCase("BIF")) {
			currency = new String[] {"Φράγκο Μπουρούντι", "Burundian Franc"};
		} else if (currencyCode.equalsIgnoreCase("BMD")) {
			currency = new String[] {"Δολάριο Βερμούδων", "Bermudian Dollar"};
		} else if (currencyCode.equalsIgnoreCase("BND")) {
			currency = new String[] {"Δολάριο Μπρουνέι", "Brunei Dollar"};
		} else if (currencyCode.equalsIgnoreCase("BOB")) {
			currency = new String[] {"Μπολιβιάνο Βολιβίας", "Boliviano"};
		} else if (currencyCode.equalsIgnoreCase("BRL")) {
			currency = new String[] {"Ρεάλ Βραζιλίας", "Brazilian Real"};
		} else if (currencyCode.equalsIgnoreCase("BWP")) {
			currency = new String[] {"Πούλα Μποτσουάνας", "Botswana Pula"};
		} else if (currencyCode.equalsIgnoreCase("BYR")) {
			currency = new String[] {"Ρούβλι Λευκορωσίας", "Belarusian Ruble"};
		} else if (currencyCode.equalsIgnoreCase("BZD")) {
			currency = new String[] {"Δολάριο Μπελίζ", "Belize Dollar"};
		} else if (currencyCode.equalsIgnoreCase("CAD")) {
			currency = new String[] {"Δολάριο Καναδά", "Canadian Dollar"};
		} else if (currencyCode.equalsIgnoreCase("CDF")) {
			currency = new String[] {"Φράγκο Κονγκό", "Congolese Franc"};
		} else if (currencyCode.equalsIgnoreCase("CHF")) {
			currency = new String[] {"Φράγκο Ελβετίας", "Swiss Franc"};
		} else if (currencyCode.equalsIgnoreCase("CLP")) {
			currency = new String[] {"Πέσο Χιλής", "Chilean Peso"};
		} else if (currencyCode.equalsIgnoreCase("CNY")) {
			currency = new String[] {"Γιουάν Ρενμίμπι Κίνας", "Chinese Yuan"};
		} else if (currencyCode.equalsIgnoreCase("COP")) {
			currency = new String[] {"Πέσο Κολομβίας", "Colombian Peso"};
		} else if (currencyCode.equalsIgnoreCase("CRC")) {
			currency = new String[] {"Κολόν Κόστα Ρίκα", "Costa Rican Colon"};
		} else if (currencyCode.equalsIgnoreCase("CVE")) {
			currency = new String[] {"Εσκούδο Πράσινου Ακρωτηρίου", "Cape Verde Escudo"};
		} else if (currencyCode.equalsIgnoreCase("CZK")) {
			currency = new String[] {"Κορόνα Τσέχικης Δημοκρατίας", "Czech Koruna"};
		} else if (currencyCode.equalsIgnoreCase("DJF")) {
			currency = new String[] {"Φράγκο Τζιμπουτί", "Djiboutian Franc"};
		} else if (currencyCode.equalsIgnoreCase("DKK")) {
			currency = new String[] {"Κορόνα Δανίας", "Danish Krone"};
		} else if (currencyCode.equalsIgnoreCase("DOP")) {
			currency = new String[] {"Πέσο Δομίνικου", "Dominican Peso"};
		} else if (currencyCode.equalsIgnoreCase("DZD")) {
			currency = new String[] {"Δηνάριο Αλγερίας", "Algerian Dinar"};
		} else if (currencyCode.equalsIgnoreCase("EGP")) {
			currency = new String[] {"Λίρα Αιγύπτου", "Egyptian Pound"};
		} else if (currencyCode.equalsIgnoreCase("ERN")) {
			currency = new String[] {"Νάκφα Ερυθραίας", "Eritrean Nakfa"};
		} else if (currencyCode.equalsIgnoreCase("ETB")) {
			currency = new String[] {"Μπιρ Αιθιοπίας", "Ethiopian Birr"};
		} else if (currencyCode.equalsIgnoreCase("EUR")) {
			currency = new String[] {"Ευρώ", "Euro"};
		} else if (currencyCode.equalsIgnoreCase("GBP")) {
			currency = new String[] {"Λίρα Στερλίνα Βρετανίας", "Pound Sterling"};
		} else if (currencyCode.equalsIgnoreCase("GEL")) {
			currency = new String[] {"Λάρι Γεωργίας", " Georgian Lari"};
		} else if (currencyCode.equalsIgnoreCase("GHS")) {
			currency = new String[] {"Σέντι Γκάνας", "Ghanaian Cedi"};
		} else if (currencyCode.equalsIgnoreCase("GNF")) {
			currency = new String[] {"Φράγκο Γουινέας", "Guinean Franc"};
		} else if (currencyCode.equalsIgnoreCase("GTQ")) {
			currency = new String[] {"Κουετσάλ Γουατεμάλας", "Guatemalan Quetzal"};
		} else if (currencyCode.equalsIgnoreCase("GYD")) {
			currency = new String[] {"Δολάριο Γουιάνας", "Guyanese Dollar"};
		} else if (currencyCode.equalsIgnoreCase("HKD")) {
			currency = new String[] {"Δολάριο Χονγκ Κονγκ", "Hong Kong Dollar"};
		} else if (currencyCode.equalsIgnoreCase("HNL")) {
			currency = new String[] {"Λεμπίρα Ονδούρας", "Honduran Lempira"};
		} else if (currencyCode.equalsIgnoreCase("HRK")) {
			currency = new String[] {"Κούνα Κροατίας", "Croatian Kuna"};
		} else if (currencyCode.equalsIgnoreCase("HUF")) {
			currency = new String[] {"Φιορίνι Ουγγαρίας", "Hungarian Forint"};
		} else if (currencyCode.equalsIgnoreCase("IDR")) {
			currency = new String[] {"Ρούπια Ινδονησίας", "Indonesian Rupiah"};
		} else if (currencyCode.equalsIgnoreCase("ILS")) {
			currency = new String[] {"Νέο Σέκελ Ισραήλ", "Israeli New Shekel"};
		} else if (currencyCode.equalsIgnoreCase("INR")) {
			currency = new String[] {"Ρούπια Ινδίας", "Indian Rupee"};
		} else if (currencyCode.equalsIgnoreCase("IQD")) {
			currency = new String[] {"Δηνάριο Ιράκ", "Iraqi Dinar"};
		} else if (currencyCode.equalsIgnoreCase("IRR")) {
			currency = new String[] {"Ριάλ Ιράν", "Iranian Rial"};
		} else if (currencyCode.equalsIgnoreCase("ISK")) {
			currency = new String[] {"Κορόνα Ισλανδίας", "Icelandic Krona"};
		} else if (currencyCode.equalsIgnoreCase("JMD")) {
			currency = new String[] {"Δολάριο Τζαμάικας", "Jamaican Dollar"};
		} else if (currencyCode.equalsIgnoreCase("JOD")) {
			currency = new String[] {"Δηνάριο Ιορδανίας", "Jordanian Dinar"};
		} else if (currencyCode.equalsIgnoreCase("JPY")) {
			currency = new String[] {"Γιεν Ιαπωνίας", "Japanese Yen"};
		} else if (currencyCode.equalsIgnoreCase("KES")) {
			currency = new String[] {"Σελίνι Κένυας", "Kenyan Shilling"};
		} else if (currencyCode.equalsIgnoreCase("KHR")) {
			currency = new String[] {"Ρίελ Καμπότζης", "Cambodian Riel"};
		} else if (currencyCode.equalsIgnoreCase("KMF")) {
			currency = new String[] {"Φράγκο Κομόρος", "Comoro Franc"};
		} else if (currencyCode.equalsIgnoreCase("KRW")) {
			currency = new String[] {"Γον Νότιας Κορέας", "South Korean Won"};
		} else if (currencyCode.equalsIgnoreCase("KWD")) {
			currency = new String[] {"Δηνάριο Κουβέιτ", "Kuwaiti Dinar"};
		} else if (currencyCode.equalsIgnoreCase("KZT")) {
			currency = new String[] {"Τένγκε Καζακστάν", "Kazakhstani Tenge"};
		} else if (currencyCode.equalsIgnoreCase("LBP")) {
			currency = new String[] {"Λίρα Λιβάνου", "Lebanese Pound"};
		} else if (currencyCode.equalsIgnoreCase("LKR")) {
			currency = new String[] {"Ρούπια Σρι Λάνκα", "Sri Lankan Rupee"};
		} else if (currencyCode.equalsIgnoreCase("LRD")) {
			currency = new String[] {"Δολάριο Λιβερίας", "Liberian dollar"};
		} else if (currencyCode.equalsIgnoreCase("LTL")) {
			currency = new String[] {"Λίτα Λιθουανίας", "Lithuanian litas"};
		} else if (currencyCode.equalsIgnoreCase("LVL")) {
			currency = new String[] {"Λατς Λετονίας", "Latvian Lats"};
		} else if (currencyCode.equalsIgnoreCase("LYD")) {
			currency = new String[] {"Δηνάριο Λιβύης", "Libyan Dinar"};
		} else if (currencyCode.equalsIgnoreCase("MAD")) {
			currency = new String[] {"Ντιράμ Μαρόκου", "Moroccan Dirham"};
		} else if (currencyCode.equalsIgnoreCase("MDL")) {
			currency = new String[] {"Λέι Μολδαβίας", "Moldovan Leu"};
		} else if (currencyCode.equalsIgnoreCase("MGA")) {
			currency = new String[] {"Αριανί Μαδαγασκάρης", "Malagasy Ariary"};
		} else if (currencyCode.equalsIgnoreCase("MKD")) {
			currency = new String[] {"Δηνάριο Π.Γ.Δ.Μ.", "Macedonian Denar"};
		} else if (currencyCode.equalsIgnoreCase("MMK")) {
			currency = new String[] {"Κυάτ Μιανμάρ", "Myanma Kyat"};
		} else if (currencyCode.equalsIgnoreCase("MOP")) {
			currency = new String[] {"Πατάκα Μακάο", "Macanese Pataca"};
		} else if (currencyCode.equalsIgnoreCase("MUR")) {
			currency = new String[] {"Ρούπια Μαυρικίου", "Mauritian Rupee"};
		} else if (currencyCode.equalsIgnoreCase("MXN")) {
			currency = new String[] {"Πέσο Μεξικού", "Mexican Peso"};
		} else if (currencyCode.equalsIgnoreCase("MYR")) {
			currency = new String[] {"Ρινγκίτ Μαλαισίας", "Malaysian Ringgit"};
		} else if (currencyCode.equalsIgnoreCase("MZN")) {
			currency = new String[] {"Μετικάλ Μοζαμβίκης", "Mozambican Metical"};
		} else if (currencyCode.equalsIgnoreCase("NAD")) {
			currency = new String[] {"Δολάριο Ναμίμπια", "Namibian Dollar"};
		} else if (currencyCode.equalsIgnoreCase("NGN")) {
			currency = new String[] {"Νάιρα Νιγηρίας", "Nigerian Naira"};
		} else if (currencyCode.equalsIgnoreCase("NIO")) {
			currency = new String[] {"Χρυσή Κόρδοβα Νικαράγουας", "Nicaraguan Cordoba"};
		} else if (currencyCode.equalsIgnoreCase("NOK")) {
			currency = new String[] {"Κορόνα Νορβηγίας", "Norwegian Krone"};
		} else if (currencyCode.equalsIgnoreCase("NPR")) {
			currency = new String[] {"Ρούπια Νεπάλ", "Nepalese Rupee"};
		} else if (currencyCode.equalsIgnoreCase("NZD")) {
			currency = new String[] {"Δολάριο Νέας Ζηλανδίας", "New Zealand Dollar"};
		} else if (currencyCode.equalsIgnoreCase("OMR")) {
			currency = new String[] {"Ριάλ Ομάν", "Omani Rial"};
		} else if (currencyCode.equalsIgnoreCase("PAB")) {
			currency = new String[] {"Μπαλμπόα Παναμά", "Panamanian Balboa"};
		} else if (currencyCode.equalsIgnoreCase("PEN")) {
			currency = new String[] {"Νέο Σολ Περού", "Peruvian Nuevo Sol"};
		} else if (currencyCode.equalsIgnoreCase("PHP")) {
			currency = new String[] {"Πέσο Φιλιππίνων", "Philippine Peso"};
		} else if (currencyCode.equalsIgnoreCase("PKR")) {
			currency = new String[] {"Ρούπια Πακιστάν", "Pakistani Rupee"};
		} else if (currencyCode.equalsIgnoreCase("PLN")) {
			currency = new String[] {"Ζλότυ Πολωνίας", "Polish Zloty"};
		} else if (currencyCode.equalsIgnoreCase("PYG")) {
			currency = new String[] {"Γκουαρανί Παραγουάης", "Paraguayan Guarani"};
		} else if (currencyCode.equalsIgnoreCase("QAR")) {
			currency = new String[] {"Ριάλ Κατάρ", "Qatari Riyal"};
		} else if (currencyCode.equalsIgnoreCase("RON")) {
			currency = new String[] {"Λεβ Ρουμανίας", "Romanian Leu"};
		} else if (currencyCode.equalsIgnoreCase("RSD")) {
			currency = new String[] {"Δηνάριο Σερβίας", "Serbian Dinar"};
		} else if (currencyCode.equalsIgnoreCase("RUB")) {
			currency = new String[] {"Ρούβλι Ρωσίας", "Russian Ruble"};
		} else if (currencyCode.equalsIgnoreCase("RWF")) {
			currency = new String[] {"Φράγκο Ρουάντας", "Rwandan Franc"};
		} else if (currencyCode.equalsIgnoreCase("SAR")) {
			currency = new String[] {"Ριάλ Σαουδικής Αραβίας", "Saudi Riyal"};
		} else if (currencyCode.equalsIgnoreCase("SDG")) {
			currency = new String[] {"Λίρα Σουδάν", "Sudanese Pound"};
		} else if (currencyCode.equalsIgnoreCase("SEK")) {
			currency = new String[] {"Κορόνα Σουηδίας", "Swedish Krona"};
		} else if (currencyCode.equalsIgnoreCase("SGD")) {
			currency = new String[] {"Δολάριο Σιγκαπούρης", "Singapore Dollar"};
		} else if (currencyCode.equalsIgnoreCase("SOS")) {
			currency = new String[] {"Σελίνι Σομαλίας", "Somali Shilling"};
		} else if (currencyCode.equalsIgnoreCase("STD")) {
			currency = new String[] {"Ντόμπρα Σάο Τομέ και Πρίνσιπε", "Sao Tome and Principe Dobra"};
		} else if (currencyCode.equalsIgnoreCase("SYP")) {
			currency = new String[] {"Λίρα Συρίας", "Syrian Pound"};
		} else if (currencyCode.equalsIgnoreCase("THB")) {
			currency = new String[] {"Μπατ Ταϊλάνδης", "Thai Baht"};
		} else if (currencyCode.equalsIgnoreCase("TND")) {
			currency = new String[] {"Δηνάριο Τυνησίας", "Tunisian Dinar"};
		} else if (currencyCode.equalsIgnoreCase("TOP")) {
			currency = new String[] {"Παάνγκα Τόνγκας", "Tongan Pa'anga"};
		} else if (currencyCode.equalsIgnoreCase("TRY")) {
			currency = new String[] {"Τουρκική Λίρα", "Turkish Lira"};
		} else if (currencyCode.equalsIgnoreCase("TTD")) {
			currency = new String[] {"Δολάριο Τρινιντάντ και Τομπάγκο", "Trinidad and Tobago Dollar"};
		} else if (currencyCode.equalsIgnoreCase("TWD")) {
			currency = new String[] {"Νέο Δολάριο Ταϊβάν", "New Taiwan Dollar"};
		} else if (currencyCode.equalsIgnoreCase("TZS")) {
			currency = new String[] {"Σελίνι Τανζανίας", "Tanzanian Shilling"};
		} else if (currencyCode.equalsIgnoreCase("UAH")) {
			currency = new String[] {"Χρίφνα Ουκρανίας", "Ukrainian Hryvnia"};
		} else if (currencyCode.equalsIgnoreCase("UGX")) {
			currency = new String[] {"Σελίνι Ουγκάντας", "Ugandan Shilling"};
		} else if (currencyCode.equalsIgnoreCase("USD")) {
			currency = new String[] {"Δολάριο ΗΠΑ", "United States Dollar"};
		} else if (currencyCode.equalsIgnoreCase("UYU")) {
			currency = new String[] {"Πέσο Ουρουγουάης", "Uruguayan Peso"};
		} else if (currencyCode.equalsIgnoreCase("UZS")) {
			currency = new String[] {"Σομ Ουζμπεκιστάν", "Uzbekistan Som"};
		} else if (currencyCode.equalsIgnoreCase("VEF")) {
			currency = new String[] {"Βολιβάρ Φουέρτε Βενεζουέλας", "Venezuelan Bolivar"};
		} else if (currencyCode.equalsIgnoreCase("VND")) {
			currency = new String[] {"Ντονγκ Βιετνάμ", "Vietnamese Dong"};
		} else if (currencyCode.equalsIgnoreCase("XAF")) {
			currency = new String[] {"Φράγκο Κεντρικής Αφρικής", "Communauté Financière Africaine (BEAC) CFA Franc"};
		} else if (currencyCode.equalsIgnoreCase("XOF")) {
			currency = new String[] {"Φράγκο Δυτικής Αφρικής", "Communauté Financière Africaine (BCEAO) CFA Franc"};
		} else if (currencyCode.equalsIgnoreCase("YER")) {
			currency = new String[] {"Ριάλ Υεμένης", " 	Yemeni Rial"};
		} else if (currencyCode.equalsIgnoreCase("ZAR")) {
			currency = new String[] {"Ραντ Νότιας Αφρικής", "South African Rand"};
		} else if (currencyCode.equalsIgnoreCase("ZMK")) {
			currency = new String[] {"Κουάνζα Ζαΐρ", "Zambian Kwacha"};
		} else {
			writeUnknownCurrency(currencyCode);
		}
		
		return currency;
	}
	
	/**
     * Write in a file the unknown abbreviation of a country.
     * 
     * @param String the unknown abbreviation
     */
	public void writeUnknownAbbreviation(String abbreviation) {
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("abbreviations.txt", true)));
		    out.println(abbreviation);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	/**
     * Write in a file the unknown currency.
     * 
     * @param String the unknown currency
     */
	public void writeUnknownCurrency(String currency) {
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("currencies.txt", true)));
		    out.println(currency);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}

}