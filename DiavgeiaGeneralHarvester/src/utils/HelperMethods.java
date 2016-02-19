package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;

import actions.HandleApiRequests;

import objects.Decision;
import objects.Organization;

import ontology.Ontology;

/**
 * @author G. Razis
 */
public class HelperMethods {
	
	/**
     * Find and transform the previous day's date into the yyyy-MM-dd format.
     * 
     * @return String the previous day's date in the yyyy-MM-dd format
     */
	public String getPreviousDate() {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, -1);
		String previousDate = sdf.format(cal.getTime());
		
		return previousDate;
	}
	
	/**
     * Get the list of dates among the starting and ending date.
     * 
     * @param LocalDate the starting date
     * @param LocalDate the ending date
     * @return List<String> the list of dates between the starting and ending date
     */
	public List<String> getListOfDates(LocalDate startDate, LocalDate endDate) {
		
		List<String> datesList = new ArrayList<String>();
		
		int days = Days.daysBetween(startDate, endDate).getDays();
		
		for (int i = 0; i < days; i++) {
		    LocalDate dt = startDate.withFieldAdded(DurationFieldType.days(), i);
		    datesList.add(dt.toString());
		}
		
		return datesList;
	}
	
	/**
     * Find the corresponding SKOS Concept, Greek and English name from the provided status of the decision.
     * 
     * @param String the status of the decision
     * @return String[] the corresponding URI of the SKOS Concept and the Greek and English name
     * of the provided status of the decision
     */
	public String[] findDecisionStatusIndividual(String decisionStatus) {
		
		String[] statusDtls = null;
		
		if (decisionStatus.equalsIgnoreCase("Published")) {
			statusDtls = new String[]{Ontology.instancePrefix + "DecisionStatus/" + "Published", "Αναρτημένη και σε ισχύ", "Published and Active"};
		} else if (decisionStatus.equalsIgnoreCase("Pending_Revocation")) {
			statusDtls = new String[]{Ontology.instancePrefix + "DecisionStatus/" + "PendingRevocation", "Αναρτημένη και σε ισχύ, έχει υποβληθεί αίτημα ανάκλησής της", "Published and Active, revocation request has been submitted"};
		} else if (decisionStatus.equalsIgnoreCase("Revoked")) {
			statusDtls = new String[]{Ontology.instancePrefix + "DecisionStatus/" + "Revoked", "Ανακληθείσα", "Revoked"};
		} else if (decisionStatus.equalsIgnoreCase("Submitted")) {
			statusDtls = new String[]{Ontology.instancePrefix + "DecisionStatus/" + "Submitted", "Έχει υποβληθεί αλλά δεν έχει αποδοθεί ΑΔΑ", "Submitted but ADA code has not been assigned"};
		} else {
			statusDtls = new String[]{Ontology.instancePrefix + "DecisionStatus/" + "Unknown", "", ""};
			writeUnknownMetadata("decisionStatus", decisionStatus);
		}
		
		return statusDtls;
	}
	
	/**
     * Find the corresponding SKOS Concept, Greek and English name from the provided status of the organization.
     * 
     * @param String the status of the organization
     * @return String[] the corresponding URI of the SKOS Concept and the Greek and English name 
     * of the provided status of the organization
     */
	public String[] findOrganizationStatusDetails(String organizationStatus) {
		
		String[] statusDtls = null;
		
		if (organizationStatus.equalsIgnoreCase("active")) {
			statusDtls = new String[]{Ontology.instancePrefix + "OrganizationStatus/" + "Active", "Ενεργός και ενταγμένος στη Δι@ύγεια", "Active and registered in Di@vgeia"};
		} else if (organizationStatus.equalsIgnoreCase("pending")) {
			statusDtls = new String[]{Ontology.instancePrefix + "OrganizationStatus/" + "Pending", "Ενταγμένος στη Δι@ύγεια αλλά δεν είναι πλέον σε ισχύ", "Registered in Di@vgeia but is no longer active"};
		} else if (organizationStatus.equalsIgnoreCase("inactive")) {
			statusDtls = new String[]{Ontology.instancePrefix + "OrganizationStatus/" + "Inactive", "Εκκρεμεί η ενεργοποίησή του και η ένταξή του στη Δι@ύγεια", "Pending activation and registration in Di@vgeia"};
		} else {
			statusDtls = new String[]{Ontology.instancePrefix + "OrganizationStatus/" + "Unknown", "", ""};
			writeUnknownMetadata("organizationStatus", organizationStatus);
		}
		
		return statusDtls;
	}
	
	/**
     * Find the corresponding SKOS Concept, Greek and English name from from the provided budget type.
     * 
     * @param String the status of the decision
     * @return String[] the corresponding URI of the SKOS Concept and the Greek and English name
     * of the provided budget type
     */
	public String[] findBudgetTypeIndividual(String budgetType) {
		
		String[] budgetDtls = null;
		
		if (budgetType.equalsIgnoreCase("Τακτικός Προϋπολογισμός")) {
			budgetDtls = new String[]{Ontology.instancePrefix + "BudgetCategory/" + "RegularBudget", "Τακτικός Προϋπολογισμός", "Regular Budget"};
		} else if (budgetType.equalsIgnoreCase("Πρόγραμμα Δημοσίων Επενδύσεων")) {
			budgetDtls = new String[]{Ontology.instancePrefix + "BudgetCategory/" + "PublicInvestmentProject", "Πρόγραμμα Δημοσίων Επενδύσεων", "Public Investment Project"};
		} else if (budgetType.equalsIgnoreCase("Ίδια Έσοδα")) {
			budgetDtls = new String[]{Ontology.instancePrefix + "BudgetCategory/" + "OwnSourceRevenue", "Ίδια Έσοδα", "Own Source Revenue"};
		} else if (budgetType.equalsIgnoreCase("Συγχρηματοδοτούμενο Έργο")) {
			budgetDtls = new String[]{Ontology.instancePrefix + "BudgetCategory/" + "CofundedProject", "Συγχρηματοδοτούμενο Έργο", "Co-funded Project"};
		} else {
			budgetDtls = new String[]{Ontology.instancePrefix + "BudgetCategory/" + "Unknown", "", ""};
			writeUnknownMetadata("budgetType", budgetType);
		}
		
		return budgetDtls;
	}
	
	/**
     * Find the corresponding SKOS Concept from the provided status of the decision.
     * 
     * @param String the status of the decision
     * @return String the corresponding URI of the SKOS Concept
     */
	public String findKindIndividual(String contractType) {
		
		String uri = "";
		
		if (contractType.equalsIgnoreCase("Έργα")) {
			uri = Ontology.publicContractsPrefix + "Works";
		} else if (contractType.equalsIgnoreCase("Υπηρεσίες")) {
			uri = Ontology.publicContractsPrefix + "Services";
		} else if (contractType.equalsIgnoreCase("Προμήθειες")) {
			uri = Ontology.publicContractsPrefix + "Supplies";
		} else if (contractType.equalsIgnoreCase("Μελέτες")) {
			uri = Ontology.eLodPrefix + "Researches";
		}
		
		return uri;
	}
	
	/**
     * Find the corresponding SKOS Concept from the provided type of the procedure.
     * 
     * @param String the type of the procedure
     * @return String the corresponding URI of the SKOS Concept
     */
	public String findProcedureTypeIndividual(String procedureType) {
		
		String uri = "";
		
		if (procedureType.equalsIgnoreCase("Ανοικτός")) {
			uri = Ontology.publicContractsPrefix + "Open";
		} else if (procedureType.equalsIgnoreCase("Κλειστός")) {
			uri = Ontology.publicContractsPrefix + "Restricted";
		} else if (procedureType.equalsIgnoreCase("Πρόχειρος")) {
			uri = Ontology.eLodPrefix + "LowValue";
		} else {
			writeUnknownMetadata("procedureType", procedureType);
		}
		
		return uri;
	}
	
	/**
     * Find the corresponding SKOS Concept, its weight and the Greek and English name from the provided type of the criterion.
     * 
     * @param String the type of the criterion
     * @return String the corresponding URI of the SKOS Concept, its weight and the Greek and English name 
     * of the provided type of the criterion
     */
	public String[] findCriterionIndividual(String criterionType) {
		
		String[] criterionDtls = null;
		
		if (criterionType.equalsIgnoreCase("Χαμηλότερη Τιμή")) {
			criterionDtls = new String[]{Ontology.instancePrefix + "SelectionCriterion/" + "LowestPrice", "100", "Χαμηλότερη Τιμή", "Lowest Price"};
		} else if (criterionType.equalsIgnoreCase("Συμφερότερη από οικονομικής άποψης")) {
			criterionDtls = new String[]{Ontology.instancePrefix + "SelectionCriterion/" + "MostEconomicalOffer", "100", "Συμφερότερη από οικονομικής άποψης", "Most Economical Offer"};
		} else if (criterionType.equalsIgnoreCase("Τεχνική ποιότητα")) {
			criterionDtls = new String[]{Ontology.instancePrefix + "SelectionCriterion/" + "TechnicalQuality", "100", "Τεχνική ποιότητα", "Technical Quality"};
		} else {
			criterionDtls = new String[]{Ontology.instancePrefix + "SelectionCriterion/" + "Unknown", "100", "", ""};
			writeUnknownMetadata("selectionCriterion", criterionType);
		}
		
		return criterionDtls;
	}
	
	/**
     * Export to a file the unknown metadata.
     * 
     * @param String the output filename
     * @param String the unknown metadata
     */
	public void writeUnknownMetadata(String fileName, String metadata) {
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName + ".txt", true)));
		    out.println(metadata);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	/**
     * Write in a file the unknown metadata.
     * 
     * @param String the output filename
     * @param String the unknown metadata
     */
	public void writeMetadata(String fileName, String csvFileName, String[] fields) {
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName + ".txt", true)));
		    out.println(csvFileName + " -> " + fields[0] + ": " + fields[1]);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	/**
     * Check if a field retrieved from API contains non-printable data
     * and return its proper format.
     * 
     * @param String a field which may need cleansing from non-printable data
     * @return String the cleaned data of the field.
     */
	public String cleanInputData(String field) {
		
		try {
    	    //Pattern regex = Pattern.compile("[\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]"); //non-printable data
			Pattern regex = Pattern.compile("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]"); //non-printable data
    	    Matcher regexMatcher = regex.matcher(field);
    	    if (regexMatcher.find()) {
    	    	field = field.replaceAll("[\\p{Cntrl}]", "");
    	    	System.out.print("Cleaned field: " + field + "\n");
    	    }
    	    if (field.contains("\"")) {
    	    	field = field.replace("\"", "");
    	    }
    	    if (field.contains("\\")) {
    	    	field = field.replace("\\", "");
    	    }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
		
		return field;
	}
	
	/**
     * Clean and the return the new representation of a provided Vat Id.
     * 
     * @param String a Vat Id to be cleaned
     * @return String the cleaned representation of the input Vat Id
     */
	public String cleanVatId(String vatId) {
		
		if (vatId.startsWith("O")) {
			vatId = vatId.replaceFirst("O", "0");
		}
		
		if (vatId.contains("ΑΦΜ.")) {
			vatId = vatId.replace("ΑΦΜ.", "");
		}
		
		if (vatId.contains("ΑΦΜ")) {
			vatId = vatId.replace("ΑΦΜ", "");
		}
		
		if ( vatId.contains("VAT No") || vatId.contains("VATNo")) {
			vatId = vatId.replace("VAT No", "");
			vatId = vatId.replace("VATNo", "");
		}
		
		vatId = vatId.replace(" ", "");
		vatId = vatId.replace("`", "");
		vatId = vatId.replace("\"", "");
		vatId = vatId.replaceAll("[^a-zA-Z0-9 ]*", "");
		
		return vatId;
	}
	
	/**
     * Convert the plain text containing CPVs to a list.
     * 
     * @param String the CPV field as retrieved from the API
     * @return List<String> list of CPVs
     */
	public List<String> cpv(String cpvCsvData) {
		
		List<String> cpvCodes = new ArrayList<String>();
		String[] splittedCpvCodes = cpvCsvData.split(";");
		
		for (int i = 0; i < splittedCpvCodes.length; i++) {
			Pattern regex = Pattern.compile("[0-9]{8}-[0-9]");
			Matcher regexMatcher = regex.matcher(splittedCpvCodes[i]);
			while (regexMatcher.find()) {
				cpvCodes.add(regexMatcher.group(0));
			}
		}
		
		return cpvCodes;
	}
	
	/**
     * Find the instance of the class that the related decision belongs to, 
     * the respective resource type and the respective object property.
     * 
     * @param String the related Ada of the decision
     * @return Object[] the instance of the class that the related decision belongs to,
     * the respective resource type and the respective object property
     */
	public Object[] findDecisionTypeInstance(String relatedAda) {
		
		Object[] instanceData = null;
		HandleApiRequests handleReq = new HandleApiRequests();
		Decision decisionObj = handleReq.searchSingleDecision(relatedAda);
		
		if (decisionObj != null) {
			String decisionTypeId = decisionObj.getDecisionTypeId();
			if (decisionTypeId.equalsIgnoreCase("Β.1.3")) {
				instanceData = new Object[]{"CommittedItem", Ontology.committedItemResource, Ontology.hasRelatedCommittedItem};
			} else if (decisionTypeId.equalsIgnoreCase("Β.2.1")) {
				instanceData = new Object[]{"ExpenseApprovalItem", Ontology.expenseApprovalItemResource, Ontology.hasRelatedExpenseApprovalItem};
			} else if (decisionTypeId.equalsIgnoreCase("Β.2.2")) {
				instanceData = new Object[]{"SpendingItem", Ontology.spendingItemResource, Ontology.hasRelatedSpendingItem};
			} else if (decisionTypeId.equalsIgnoreCase("Δ.1") || decisionTypeId.equalsIgnoreCase("Δ.2.1") || 
					   decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
				instanceData = new Object[]{"Contract", Ontology.contractResource, Ontology.hasRelatedContract};
			} else {
				instanceData = new Object[]{"Decision", Ontology.decisionResource, Ontology.hasRelatedDecision};
			}
		}
		
		return instanceData;
	}
	
	/**
     * Find the instance of the class that the related decisions belong to, their resource type 
     * and the object property that is used for the relation of that type of decisions.
     * 
     * @param String the decision type
     * @return Object[] the instance of the class that the related decisions belong to, their 
     * resource type and the object property that is used for the relation of that type of decisions
     */
	public Object[] findRelatedPropertyOfDecisionType(String decisionTypeId) {
		
		Object[] instanceData = null;
		
		if (decisionTypeId.equalsIgnoreCase("Δ.1") || decisionTypeId.equalsIgnoreCase("Δ.2.1") ) {
			instanceData = new Object[]{"CommittedItem", Ontology.committedItemResource, Ontology.hasRelatedCommittedItem};
		} else if (decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
			instanceData = new Object[]{"Contract", Ontology.contractResource, Ontology.hasRelatedContract};
		}
		
		return instanceData;
	}
	
	/**
     * Find the name of the thematic category id that the related decision belongs to.
     * 
     * @param String the id of the thematic category
     * @return String the greek name of the thematic category
     */
	public String findThematicCategoryName(String thematicCategoryId) {
		
		String thematicCategoryName = "";
		
		if (thematicCategoryId.equalsIgnoreCase("04")) {
			thematicCategoryName = "ΠΟΛΙΤΙΚΗ ΖΩΗ";
		} else if (thematicCategoryId.equalsIgnoreCase("08")) {
			thematicCategoryName = "ΔΙΕΘΝΕΙΣ ΣΧΕΣΕΙΣ";
		} else if (thematicCategoryId.equalsIgnoreCase("10")) {
			thematicCategoryName = "ΕΥΡΩΠΑΪΚΗ ΈΝΩΣΗ";
		} else if (thematicCategoryId.equalsIgnoreCase("12")) {
			thematicCategoryName = "ΔΙΚΑΙΟ";
		} else if (thematicCategoryId.equalsIgnoreCase("16")) {
			thematicCategoryName = "ΟΙΚΟΝΟΜΙΚΗ ΖΩΗ";
		} else if (thematicCategoryId.equalsIgnoreCase("20")) {
			thematicCategoryName = "ΟΙΚΟΝΟΜΙΚΕΣ ΚΑΙ ΕΜΠΟΡΙΚΕΣ ΣΥΝΑΛΛΑΓΕΣ";
		} else if (thematicCategoryId.equalsIgnoreCase("24")) {
			thematicCategoryName = "ΔΗΜΟΣΙΟΝΟΜΙΚΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("28")) {
			thematicCategoryName = "ΚΟΙΝΩΝΙΚΑ ΘΕΜΑΤΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("32")) {
			thematicCategoryName = "ΕΠΙΚΟΙΝΩΝΙΑ ΚΑΙ ΜΟΡΦΩΣΗ";
		} else if (thematicCategoryId.equalsIgnoreCase("36")) {
			thematicCategoryName = "ΕΠΙΣΤΗΜΕΣ";
		} else if (thematicCategoryId.equalsIgnoreCase("40")) {
			thematicCategoryName = "ΕΠΙΧΕΙΡΗΣΕΙΣ ΚΑΙ ΑΝΤΑΓΩΝΙΣΜΟΣ";
		} else if (thematicCategoryId.equalsIgnoreCase("44")) {
			thematicCategoryName = "ΑΠΑΣΧΟΛΗΣΗ ΚΑΙ ΕΡΓΑΣΙΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("48")) {
			thematicCategoryName = "ΜΕΤΑΦΟΡΕΣ";
		} else if (thematicCategoryId.equalsIgnoreCase("52")) {
			thematicCategoryName = "ΠΕΡΙΒΑΛΛΟΝ";
		} else if (thematicCategoryId.equalsIgnoreCase("56")) {
			thematicCategoryName = "ΓΕΩΡΓΙΑ, ΔΑΣΟΚΟΜΙΑ ΚΑΙ ΑΛΙΕΙΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("60")) {
			thematicCategoryName = "ΔΙΑΤΡΟΦΗ ΚΑΙ ΓΕΩΡΓΙΚΑ ΠΡΟΪΟΝΤΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("64")) {
			thematicCategoryName = "ΠΑΡΑΓΩΓΗ, ΤΕΧΝΟΛΟΓΙΑ ΚΑΙ ΕΡΕΥΝΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("66")) {
			thematicCategoryName = "ΕΝΕΡΓΕΙΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("68")) {
			thematicCategoryName = "ΒΙΟΜΗΧΑΝΙΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("72")) {
			thematicCategoryName = "ΓΕΩΡΓΡΑΦΙΑ";
		} else if (thematicCategoryId.equalsIgnoreCase("76")) {
			thematicCategoryName = "ΔΙΕΘΝΕΙΣ ΟΡΓΑΝΙΣΜΟΙ";
		} else if (thematicCategoryId.equalsIgnoreCase("10004")) {
			thematicCategoryName = "ΔΗΜΟΣΙΑ ΔΙΟΙΚΗΣΗ";
		} else if (thematicCategoryId.equalsIgnoreCase("10007")) {
			thematicCategoryName = "ΥΓΕΙΑ";
		} else {
			writeUnknownMetadata("thematicCategory", thematicCategoryId);
		}
		
		return thematicCategoryName;
	}
	
	/**
     * Find the name of the category id that the related organization belongs to.
     * 
     * @param String the id of the category of the organization
     * @return String[] the Greek and English name of the category of the organization
     */
	public String[] findOrganizationCategoryName(String organizationCategoryId) {
		
		String[] organizationCategoryName = null;
		
		if (organizationCategoryId.equalsIgnoreCase("ADMINISTRATIVEREGION")) {
			organizationCategoryName = new String[]{"Περιφέρεια", "Administrative Region"};
		} else if (organizationCategoryId.equalsIgnoreCase("COMPANYSA")) {
			organizationCategoryName = new String[]{"Ανώνυμες Εταιρείες Α.Ε.", "Company S.A."};
		} else if (organizationCategoryId.equalsIgnoreCase("COURT")) {
			organizationCategoryName = new String[]{"Δικαστήριο", "Court"};
		} else if (organizationCategoryId.equalsIgnoreCase("DEKO")) {
			organizationCategoryName = new String[]{"ΔΕΚΟ", "DEKO"};
		} else if (organizationCategoryId.equalsIgnoreCase("GENERALGOVERNMENT")) {
			organizationCategoryName = new String[]{"Γενική Κυβέρνηση", "General Government"};
		} else if (organizationCategoryId.equalsIgnoreCase("INDEPENDENTAUTHORITY")) {
			organizationCategoryName = new String[]{"Ανεξάρτητες Αρχές", "Independent Authority"};
		} else if (organizationCategoryId.equalsIgnoreCase("INSTITUTION")) {
			organizationCategoryName = new String[]{"Ιδρύματα", "Institution"};
		} else if (organizationCategoryId.equalsIgnoreCase("LOCAL_AUTHORITY")) {
			organizationCategoryName = new String[]{"ΟΤΑ (μόνο Δήμοι ή Αιρετές Περιφέρειες)", "Local Authority"};
		} else if (organizationCategoryId.equalsIgnoreCase("MINISTRY")) {
			organizationCategoryName = new String[]{"Υπουργείο", "Ministry"};
		} else if (organizationCategoryId.equalsIgnoreCase("MUNICIPALITY")) {
			organizationCategoryName = new String[]{"Δήμος", "Municipality"};
		} else if (organizationCategoryId.equalsIgnoreCase("NONPROFITCOMPANY")) {
			organizationCategoryName = new String[]{"Μη κερδοσκοπικές εταιρείες", "Non Profit Organization"};
		} else if (organizationCategoryId.equalsIgnoreCase("NPDD")) {
			organizationCategoryName = new String[]{"ΝΠΔΔ", "NPDD"};
		} else if (organizationCategoryId.equalsIgnoreCase("NPID")) {
			organizationCategoryName = new String[]{"ΝΠΙΔ", "NPID"};
		} else if (organizationCategoryId.equalsIgnoreCase("OTHERTYPE")) {
			organizationCategoryName = new String[]{"ΑΛΛΟ", "Other Type"};
		} else if (organizationCategoryId.equalsIgnoreCase("PRESIDENTOFREPUBLIC")) {
			organizationCategoryName = new String[]{"Πρόεδρος της Δημοκρατίας", "President of the Republic"};
		} else if (organizationCategoryId.equalsIgnoreCase("UNIVERSITY")) {
			organizationCategoryName = new String[]{"Πανεπιστήμιο", "University"};
		} else if (organizationCategoryId.equalsIgnoreCase("DEYA")) {
			organizationCategoryName = new String[]{"ΔΕΥΑ", "DEYA"};
		} else if (organizationCategoryId.equalsIgnoreCase("REGION")) {
			organizationCategoryName = new String[]{"Περιφέρεια", "Region"};
		} else if (organizationCategoryId.equalsIgnoreCase("PRIME_MINISTER")) {
			organizationCategoryName = new String[]{"Γραφείο πρωθυπουργού", "Prime Minister"};
		} else if (organizationCategoryId.equalsIgnoreCase("HOSPITAL")) {
			organizationCategoryName = new String[]{"Νοσοκομείο", "Hospital"};
		} else if (organizationCategoryId.equalsIgnoreCase("SECRETARIAT_GENERAL")) {
			organizationCategoryName = new String[]{"Γενική Γραμματεία", "SECRETARIAT GENERAL"};
		} else {
			organizationCategoryName = new String[]{"", ""};
			writeUnknownMetadata("organizationCategory", organizationCategoryId);
		}
		
		return organizationCategoryName;
	}
	
	/**
     * Find the name of the category id that the related unit belongs to.
     * 
     * @param String the id of the category of the unit
     * @return String[] the Greek and English name of the category of the unit
     */
	public String[] findUnitCategoryName(String unitCategoryId) {
		
		String[] unitCategoryName = null;
		
		if (unitCategoryId.equalsIgnoreCase("ADMINISTRATION")) {
			unitCategoryName = new String[]{"Διεύθυνση", "Administration"};
		} else if (unitCategoryId.equalsIgnoreCase("BRANCH")) {
			unitCategoryName = new String[]{"Κλάδος", "Branch"};
		} else if (unitCategoryId.equalsIgnoreCase("COMMAND")) {
			unitCategoryName = new String[]{"Αρχηγείο", "Command"};
		} else if (unitCategoryId.equalsIgnoreCase("COMMITTEE")) {
			unitCategoryName = new String[]{"Επιτροπή", "Committee"};
		} else if (unitCategoryId.equalsIgnoreCase("DECENTRALISED_AGENCY")) {
			unitCategoryName = new String[]{"Αποκεντρωμένη Υπηρεσία", "Decentralised Agency"};
		} else if (unitCategoryId.equalsIgnoreCase("DEPARTMENT")) {
			unitCategoryName = new String[]{"Τμήμα", "Department"};
		} else if (unitCategoryId.equalsIgnoreCase("DIOIKISI")) {
			unitCategoryName = new String[]{"Διοίκηση", "Administration"};
		} else if (unitCategoryId.equalsIgnoreCase("FACULTY")) {
			unitCategoryName = new String[]{"Σχολή", "Faculty"};
		} else if (unitCategoryId.equalsIgnoreCase("GENERAL_ADMINISTRATION")) {
			unitCategoryName = new String[]{"Γενική Διεύθυνση", "General Administration"};
		} else if (unitCategoryId.equalsIgnoreCase("GENERAL_SECRETARIAT")) {
			unitCategoryName = new String[]{"Γενική Γραμματεία", "General Secretariat"};
		} else if (unitCategoryId.equalsIgnoreCase("OFFICE")) {
			unitCategoryName = new String[]{"Γραφείο", "Office"};
		} else if (unitCategoryId.equalsIgnoreCase("ORG_UNIT_OTHER")) {
			unitCategoryName = new String[]{"Άλλο", "Org Unit Other"};
		} else if (unitCategoryId.equalsIgnoreCase("PERIPHERAL_AGENCY")) {
			unitCategoryName = new String[]{"Περιφερειακή Υπηρεσία", "Peripheral Agency"};
		} else if (unitCategoryId.equalsIgnoreCase("PERIPHERAL_OFFICE")) {
			unitCategoryName = new String[]{"Περιφερειακό Γραφείο", "PeripheraL Office"};
		} else if (unitCategoryId.equalsIgnoreCase("SECTOR")) {
			unitCategoryName = new String[]{"Τομέας", "Sector"};
		} else if (unitCategoryId.equalsIgnoreCase("SMINARXIA")) {
			unitCategoryName = new String[]{"Σμηναρχία", "Sminarxia"};
		} else if (unitCategoryId.equalsIgnoreCase("SPECIAL_SECRETARIAT")) {
			unitCategoryName = new String[]{"Ειδική Γραμματεία", "Special Secretariat"};
		} else if (unitCategoryId.equalsIgnoreCase("TEAM")) {
			unitCategoryName = new String[]{"Ομάδα", "Team"};
		} else if (unitCategoryId.equalsIgnoreCase("TREASURY")) {
			unitCategoryName = new String[]{"Ταμείο", "Treasury"};
		} else if (unitCategoryId.equalsIgnoreCase("UNIT")) {
			unitCategoryName = new String[]{"Μονάδα", "Unit"};
		} else if (unitCategoryId.equalsIgnoreCase("VICE_ADMINISTRATION")) {
			unitCategoryName = new String[]{"Υποδιεύθυνση", "Vice Administration"};
		} else if (unitCategoryId.equalsIgnoreCase("VICE_ADMINISTRATION_HEAD")) {
			unitCategoryName = new String[]{"Προϊστάμενος Υποδιεύθυνσης", "Vice Administration Head"};
		} else if (unitCategoryId.equalsIgnoreCase("WATCH")) {
			unitCategoryName = new String[]{"Παρατηρητήριο", "Watch"};
		} else if (unitCategoryId.equalsIgnoreCase("WING")) {
			unitCategoryName = new String[]{"Πτέρυγα", "Wing"};
		} else {
			unitCategoryName = new String[]{"", ""};
			writeUnknownMetadata("unitCategory", unitCategoryId);
		}
		
		return unitCategoryName;
	}
	
	/**
     * Find the name of the domain id that the related organization belongs to.
     * 
     * @param String the id of the domain of the organization
     * @return String[] the Greek and English name of the domain of the organization
     */
	public String[] findOrganizationDomainName(String organizationDomainId) {
		
		String[] organizationDomainName = new String[]{"", ""};
		
		if (organizationDomainId.equalsIgnoreCase("Age")) {
			organizationDomainName = new String[]{"Ηλικία", "Age"};
		} else if (organizationDomainId.equalsIgnoreCase("Agriculture")) {
			organizationDomainName = new String[]{"Γεωργία", "Agriculture"};
		} else if (organizationDomainId.equalsIgnoreCase("AgricultureCompensations")) {
			organizationDomainName = new String[]{"Γεωργικές αποζημιώσεις", "Agriculture Compensations"};
		} else if (organizationDomainId.equalsIgnoreCase("AgricultureEnvironmentNaturalResources")) {
			organizationDomainName = new String[]{"Γεωργία, Περιβάλλον και Φυσικοί Πόροι", "Agriculture Environment Natural Resources"};
		} else if (organizationDomainId.equalsIgnoreCase("AnimalHealth")) {
			organizationDomainName = new String[]{"Υγεία των ζώων", "Animal Health"};
		} else if (organizationDomainId.equalsIgnoreCase("AnimalRights")) {
			organizationDomainName = new String[]{"Δικαιώματα και πρόνοια ζώων", "Animal Rights"};
		} else if (organizationDomainId.equalsIgnoreCase("ArbitrarConstructions")) {
			organizationDomainName = new String[]{"Αυθαίρετα", "Arbitrar Constructions"};
		} else if (organizationDomainId.equalsIgnoreCase("ArmedForces")) {
			organizationDomainName = new String[]{"Ένοπλες Δυνάμεις", "Armed Forces"};
		} else if (organizationDomainId.equalsIgnoreCase("ArmyLaws")) {
			organizationDomainName = new String[]{"Στρατιωτική Νομοθεσία", "Army Laws"};
		} else if (organizationDomainId.equalsIgnoreCase("AutoBicyclesBikes")) {
			organizationDomainName = new String[]{"Αυτοκίνητα - Μοτοποδήλατα - Μοτοσικλέτες", "Auto - Bicycles - Bikes"};
		} else if (organizationDomainId.equalsIgnoreCase("AutoWorkshops")) {
			organizationDomainName = new String[]{"Συνεργεία", "Auto Workshops"};
		} else if (organizationDomainId.equalsIgnoreCase("Bankruptcies")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Πτώχευση", "Bankruptcies"};
		} else if (organizationDomainId.equalsIgnoreCase("Baptism")) {
			organizationDomainName = new String[]{"Βάφτιση", "Baptism"};
		} else if (organizationDomainId.equalsIgnoreCase("Birth")) {
			organizationDomainName = new String[]{"Γέννηση", "Birth"};
		} else if (organizationDomainId.equalsIgnoreCase("BoyRecordArchives")) {
			organizationDomainName = new String[]{"Μητρώο Αρρένων", "Boy Record Archives"};
		} else if (organizationDomainId.equalsIgnoreCase("Buildings")) {
			organizationDomainName = new String[]{"Οικοδομές", "Buildings"};
		} else if (organizationDomainId.equalsIgnoreCase("Business")) {
			organizationDomainName = new String[]{"Επιχειρήσεις και κλάδοι", "Business"};
		} else if (organizationDomainId.equalsIgnoreCase("BussesTracks")) {
			organizationDomainName = new String[]{"Λεωφορεία - Φορτηγά", "Busses - Tracks"};
		} else if (organizationDomainId.equalsIgnoreCase("CaringBusinesses")) {
			organizationDomainName = new String[]{"Επιχειρήσεις πρόνοιας - Φροντίδα ηλικιωμένων - ΑΜΕΑ", "Caring Businesses"};
		} else if (organizationDomainId.equalsIgnoreCase("Chambers")) {
			organizationDomainName = new String[]{"Επιμελητήρια", "Chambers"};
		} else if (organizationDomainId.equalsIgnoreCase("Charity")) {
			organizationDomainName = new String[]{"Φιλανθρωπία", "Charity"};
		} else if (organizationDomainId.equalsIgnoreCase("CityArchives")) {
			organizationDomainName = new String[]{"Δημοτολόγιο και Μεταδημότευση", "City Archives"};
		} else if (organizationDomainId.equalsIgnoreCase("CityRing")) {
			organizationDomainName = new String[]{"Δακτύλιος", "City Ring"};
		} else if (organizationDomainId.equalsIgnoreCase("CivilId")) {
			organizationDomainName = new String[]{"Αστυνομική Ταυτότητα", "Civil Id"};
		} else if (organizationDomainId.equalsIgnoreCase("CivilLaws")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Αναγκαστική διαχείριση", "Civil Laws"};
		} else if (organizationDomainId.equalsIgnoreCase("CivilRights")) {
			organizationDomainName = new String[]{"Πολιτικά και ανθρώπινα δικαιώματα", "Civil Rights"};
		} else if (organizationDomainId.equalsIgnoreCase("Companies")) {
			organizationDomainName = new String[]{"Εταιρείες - Σύσταση και λειτουργία εταιρειών", "Companies"};
		} else if (organizationDomainId.equalsIgnoreCase("Constitution")) {
			organizationDomainName = new String[]{"Σύνταγμα της Ελλάδας", "Constitution"};
		} else if (organizationDomainId.equalsIgnoreCase("ConstructionEquipment")) {
			organizationDomainName = new String[]{"Μηχανήματα έργων", "Construction Equipment"};
		} else if (organizationDomainId.equalsIgnoreCase("Consumer")) {
			organizationDomainName = new String[]{"Καταναλωτές", "Consumer"};
		} else if (organizationDomainId.equalsIgnoreCase("Courts")) {
			organizationDomainName = new String[]{"Δικαστήρια - Δικάσιμοι - Αποφάσεις - Ένδικα μέσα", "Courts"};
		} else if (organizationDomainId.equalsIgnoreCase("Crime")) {
			organizationDomainName = new String[]{"Έγκλημα", "Crime"};
		} else if (organizationDomainId.equalsIgnoreCase("Culture")) {
			organizationDomainName = new String[]{"Πολιτισμός", "Culture"};
		} else if (organizationDomainId.equalsIgnoreCase("Customs")) {
			organizationDomainName = new String[]{"Τελωνεία", "Customs"};
		} else if (organizationDomainId.equalsIgnoreCase("Demolution")) {
			organizationDomainName = new String[]{"Κατεδάφιση", "Demolition"};
		} else if (organizationDomainId.equalsIgnoreCase("Divorce")) {
			organizationDomainName = new String[]{"Διαζύγιο", "Divorce"};
		} else if (organizationDomainId.equalsIgnoreCase("DriveSecurity")) {
			organizationDomainName = new String[]{"Οδική ασφάλεια και κυκλοφορία", "Drive Security"};
		} else if (organizationDomainId.equalsIgnoreCase("EarthResources")) {
			organizationDomainName = new String[]{"Γεωφυσικοί Πόροι", "Earth Resources"};
		} else if (organizationDomainId.equalsIgnoreCase("EducationInstitutes")) {
			organizationDomainName = new String[]{"Εκπαιδευτήρια - Παιδότοποι - Φροντιστήρια", "Education Institutes"};
		} else if (organizationDomainId.equalsIgnoreCase("EducationPersonnel")) {
			organizationDomainName = new String[]{"Εκπαιδευτικοί", "Education Personnel"};
		} else if (organizationDomainId.equalsIgnoreCase("EducationProfession")) {
			organizationDomainName = new String[]{"Εκπαίδευση, Καριέρα και Απασχόληση", "Education Profession"};
		} else if (organizationDomainId.equalsIgnoreCase("EducationSkills")) {
			organizationDomainName = new String[]{"Εκπαίδευση και Ικανότητες", "Education Skills"};
		} else if (organizationDomainId.equalsIgnoreCase("Elections")) {
			organizationDomainName = new String[]{"Εκλογές - Εκλογική νομοθεσία - Εκλογικοί κατάλογοι", "Elections"};
		} else if (organizationDomainId.equalsIgnoreCase("ElectricPowerManagement")) {
			organizationDomainName = new String[]{"Ηλεκτροδότηση - Ηλεκτρική ενέργεια", "Electric Power Management"};
		} else if (organizationDomainId.equalsIgnoreCase("EnvironmentPollution")) {
			organizationDomainName = new String[]{"Περιβάλλον - Θόρυβος - Ρύπανση", "Environment - Pollution"};
		} else if (organizationDomainId.equalsIgnoreCase("EUcitizens")) {
			organizationDomainName = new String[]{"Υπήκοοι Κρατών - Μελών της Ε.Ε.", "E.U. Citizens"};
		} else if (organizationDomainId.equalsIgnoreCase("Expatriates")) {
			organizationDomainName = new String[]{"Ομογενείς - Πανινοστούντες", "Expatriates"};
		} else if (organizationDomainId.equalsIgnoreCase("Explosives")) {
			organizationDomainName = new String[]{"Εκρηκτικές ύλες - Επικίνδυνα υλικά", "Explosives"};
		} else if (organizationDomainId.equalsIgnoreCase("Family")) {
			organizationDomainName = new String[]{"Οικογένεια", "Family"};
		} else if (organizationDomainId.equalsIgnoreCase("Financial")) {
			organizationDomainName = new String[]{"Χρηματοοικονομικά", "Financial"};
		} else if (organizationDomainId.equalsIgnoreCase("FinancialInstitutions")) {
			organizationDomainName = new String[]{"Χρηματοπιστωτικά Ιδρύματα - Οργανισμοί", "Financial Institutions"};
		} else if (organizationDomainId.equalsIgnoreCase("FireSafety")) {
			organizationDomainName = new String[]{"Χρηματοπιστωτικά Ιδρύματα - Οργανισμοί", "Fire Safety"};
		} else if (organizationDomainId.equalsIgnoreCase("Fireworks")) {
			organizationDomainName = new String[]{"Βεγγαλικά", "Fireworks"};
		} else if (organizationDomainId.equalsIgnoreCase("Fishing")) {
			organizationDomainName = new String[]{"Αλιεία και υδατοκαλλιέργειες", "Fishing"};
		} else if (organizationDomainId.equalsIgnoreCase("FoodDrinks")) {
			organizationDomainName = new String[]{"Τρόφιμα και ποτά - Κανόνες υγιεινής", "Food - Drinks"};
		} else if (organizationDomainId.equalsIgnoreCase("ForeignCurrency")) {
			organizationDomainName = new String[]{"Συνάλλαγμα", "ForeignCurrency"};
		} else if (organizationDomainId.equalsIgnoreCase("ForeignEducation")) {
			organizationDomainName = new String[]{"Σπουδές εκτός Ελλάδας - Αναγνώριση τίτλου σπουδών", "Foreign Education"};
		} else if (organizationDomainId.equalsIgnoreCase("ForeignLanguages")) {
			organizationDomainName = new String[]{"Ξένες γλώσσες", "Foreign Languages"};
		} else if (organizationDomainId.equalsIgnoreCase("Forrest")) {
			organizationDomainName = new String[]{"Δάση", "Forrest"};
		} else if (organizationDomainId.equalsIgnoreCase("ForrestHarvesting")) {
			organizationDomainName = new String[]{"Υλοτομία", "Forrest Harvesting"};
		} else if (organizationDomainId.equalsIgnoreCase("Fuels")) {
			organizationDomainName = new String[]{"Καύσιμα", "Fuels"};
		} else if (organizationDomainId.equalsIgnoreCase("Funeral")) {
			organizationDomainName = new String[]{"Θάνατος και κηδεία", "Funeral"};
		} else if (organizationDomainId.equalsIgnoreCase("GarbageDisposal")) {
			organizationDomainName = new String[]{"Απόβλητα", "Garbage Disposal"};
		} else if (organizationDomainId.equalsIgnoreCase("GeneralPension")) {
			organizationDomainName = new String[]{"Συντάξεις - Γενικά θέματα", "General Pension"};
		} else if (organizationDomainId.equalsIgnoreCase("GovermentPolitics")) {
			organizationDomainName = new String[]{"Κυβέρνηση, Πολιτική και Δημόσια Διοίκηση", "Goverment Politics"};
		} else if (organizationDomainId.equalsIgnoreCase("Governance")) {
			organizationDomainName = new String[]{"Δημόσια διοίκηση - Πολίτες", "Governance"};
		} else if (organizationDomainId.equalsIgnoreCase("GovernanceInstrumentation")) {
			organizationDomainName = new String[]{"Κυβέρνηση και Κυβερνητικά όργανα", "Governance Instrumentation"};
		} else if (organizationDomainId.equalsIgnoreCase("GovernanceProcess")) {
			organizationDomainName = new String[]{"Κώδικας διοικητικής διαδικασίας", "Governance Process"};
		} else if (organizationDomainId.equalsIgnoreCase("Guns")) {
			organizationDomainName = new String[]{"Όπλα", "Guns"};
		} else if (organizationDomainId.equalsIgnoreCase("HagueConvention")) {
			organizationDomainName = new String[]{"Επισημείωση Εγγράφων (Σύμβαση Χάγης)", "Hague Convention"};
		} else if (organizationDomainId.equalsIgnoreCase("Handicap")) {
			organizationDomainName = new String[]{"Άτομα με αναπηρία (ΑΜΕΑ)", "Handicap"};
		} else if (organizationDomainId.equalsIgnoreCase("Health")) {
			organizationDomainName = new String[]{"Υγεία", "Health"};
		} else if (organizationDomainId.equalsIgnoreCase("HealthBenefits")) {
			organizationDomainName = new String[]{"Επιδόματα σχετικά με υγεία και πρόνοια", "Health Benefits"};
		} else if (organizationDomainId.equalsIgnoreCase("HealthBook")) {
			organizationDomainName = new String[]{"Βιβλιάριο υγείας", "Health Book"};
		} else if (organizationDomainId.equalsIgnoreCase("HealthBusinesses")) {
			organizationDomainName = new String[]{"Επιχειρήσεις υγείας και υγειονομικού ενδιαφέροντος", "Health Businesses"};
		} else if (organizationDomainId.equalsIgnoreCase("HealthEmployees")) {
			organizationDomainName = new String[]{"Εργαζόμενοι στα επαγγέλματα υγείας και φροντίδος", "Health Employees"};
		} else if (organizationDomainId.equalsIgnoreCase("HealthNutrition")) {
			organizationDomainName = new String[]{"Υγεία, Διατροφή και Πρόνοια", "Health Nutrition"};
		} else if (organizationDomainId.equalsIgnoreCase("HelthExprenses")) {
			organizationDomainName = new String[]{"Δαπάνες Υγείας", "Helth Exprenses"};
		} else if (organizationDomainId.equalsIgnoreCase("HigherEducation")) {
			organizationDomainName = new String[]{"Ανώτατη και ανώτερη εκπαίδευση", "Higher Education"};
		} else if (organizationDomainId.equalsIgnoreCase("Hospitalization")) {
			organizationDomainName = new String[]{"Νοσηλεία", "Hospitalization"};
		} else if (organizationDomainId.equalsIgnoreCase("Hunting")) {
			organizationDomainName = new String[]{"Κυνήγι", "Hunting"};
		} else if (organizationDomainId.equalsIgnoreCase("IEK")) {
			organizationDomainName = new String[]{"ΙΕΚ (Ινστιτούτο επαγγελματικής κατάρτισης)", "IEK"};
		} else if (organizationDomainId.equalsIgnoreCase("IKA")) {
			organizationDomainName = new String[]{"ΙΚΑ (Ίδρυμα Κοινωνικών Ασφαλίσεων)", "IKA"};
		} else if (organizationDomainId.equalsIgnoreCase("Immigrants")) {
			organizationDomainName = new String[]{"Μετανάστες - Οικονομικοί μετανάστες - Πρόσφυγες", "Immigrants"};
		} else if (organizationDomainId.equalsIgnoreCase("Industru")) {
			organizationDomainName = new String[]{"Βιομηχανία - Βιοτεχνία", "Industry"};
		} else if (organizationDomainId.equalsIgnoreCase("InfrormationTech")) {
			organizationDomainName = new String[]{"Τεχνιλογίες πληροφορικής και επικοινωνιών - Υπηρεσίες πιστοποίησης - Ψηφιακά πιστοποιητικά", "Infrormation - Tech"};
		} else if (organizationDomainId.equalsIgnoreCase("Inheritance")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Κληρονομιά", "Inheritance"};
		} else if (organizationDomainId.equalsIgnoreCase("InstallationSecurity")) {
			organizationDomainName = new String[]{"Ασφάλεια εγκαταστάσεων και τεχνικού προσωπικού", "Installation Security"};
		} else if (organizationDomainId.equalsIgnoreCase("InsuracePension")) {
			organizationDomainName = new String[]{"Ασφάλιση και Σύνταξη", "Insurace and Pension"};
		} else if (organizationDomainId.equalsIgnoreCase("InsuranceAgencies")) {
			organizationDomainName = new String[]{"Ασφαλιστικοί Φορείς Ειδικών Κατηγοριών", "Insurance Agencies"};
		} else if (organizationDomainId.equalsIgnoreCase("Kioks")) {
			organizationDomainName = new String[]{"Περίπτερα", "Kiosks"};
		} else if (organizationDomainId.equalsIgnoreCase("LaborHomes")) {
			organizationDomainName = new String[]{"Εργατική κατοικία", "Labor Homes"};
		} else if (organizationDomainId.equalsIgnoreCase("LaborHours")) {
			organizationDomainName = new String[]{"Ώρες εργασίας, όροι και συνθήκες", "Labor Hours"};
		} else if (organizationDomainId.equalsIgnoreCase("LaborMatters")) {
			organizationDomainName = new String[]{"Εργασιακά θέματα και σχέσεις", "Labor Matters"};
		} else if (organizationDomainId.equalsIgnoreCase("LaborSupport")) {
			organizationDomainName = new String[]{"Εργατική εστία - Κοινωνικός τουρισμός", "Labor Support"};
		} else if (organizationDomainId.equalsIgnoreCase("LandArchives")) {
			organizationDomainName = new String[]{"Κτηματολόγιο", "Land Archives"};
		} else if (organizationDomainId.equalsIgnoreCase("LawArchives")) {
			organizationDomainName = new String[]{"Ποινικό μητρώο - Ποινική Διώξη", "Law Archives"};
		} else if (organizationDomainId.equalsIgnoreCase("LawSupport")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Δικαστική συμπαράσταση", "Law Support"};
		} else if (organizationDomainId.equalsIgnoreCase("LawyerInsurance")) {
			organizationDomainName = new String[]{"Ταμείο Νομικών και κλάδο Επικουρικής ασφάλισης Δικηγόρων - ΚΕΑΔ", "Lawyer Insurance"};
		} else if (organizationDomainId.equalsIgnoreCase("Lawyers")) {
			organizationDomainName = new String[]{"Δικηγόροι", "Lawyers"};
		} else if (organizationDomainId.equalsIgnoreCase("Leases")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Μισθώσεις", "Leases"};
		} else if (organizationDomainId.equalsIgnoreCase("LicencePlates")) {
			organizationDomainName = new String[]{"Πινακίδες οχημάτων και μηχανημάτων", "Licence Plates"};
		} else if (organizationDomainId.equalsIgnoreCase("LocalGovernance")) {
			organizationDomainName = new String[]{"Τοπική αυτοδιοίκηση", "Local Governance"};
		} else if (organizationDomainId.equalsIgnoreCase("Marriage")) {
			organizationDomainName = new String[]{"Γάμος", "Marriage"};
		} else if (organizationDomainId.equalsIgnoreCase("Medicines")) {
			organizationDomainName = new String[]{"Φάρμακα", "Medicines"};
		} else if (organizationDomainId.equalsIgnoreCase("MilitaryService")) {
			organizationDomainName = new String[]{"Στρατιωτική Θητεία", "Military Service"};
		} else if (organizationDomainId.equalsIgnoreCase("Museum")) {
			organizationDomainName = new String[]{"Αρχαιολογικοί Χώροι - Μουσεία", "Museum"};
		} else if (organizationDomainId.equalsIgnoreCase("Nationality")) {
			organizationDomainName = new String[]{"Ιθαγένεια, Κοινωνική ένταξη, Ομογενείς, Μετανάστες, Ξένοι υπήκοοι", "Nationality"};
		} else if (organizationDomainId.equalsIgnoreCase("NationalityGeneral")) {
			organizationDomainName = new String[]{"Ιθαγένεια", "Nationality"};
		} else if (organizationDomainId.equalsIgnoreCase("NationalResistance")) {
			organizationDomainName = new String[]{"Εθνική αντίσταση", "National Resistance"};
		} else if (organizationDomainId.equalsIgnoreCase("NationRelationsDefence")) {
			organizationDomainName = new String[]{"Διεθνείς Σχέσεις και Άμυνα", "Nation Relations Defence"};
		} else if (organizationDomainId.equalsIgnoreCase("NavyAcademy")) {
			organizationDomainName = new String[]{"Ακαδημία Εμπορικού Ναυτικού", "Navy Academy"};
		} else if (organizationDomainId.equalsIgnoreCase("NewsAndInformation")) {
			organizationDomainName = new String[]{"Πληροφορία και Επικοινωνία", "News and Information"};
		} else if (organizationDomainId.equalsIgnoreCase("OGA")) {
			organizationDomainName = new String[]{"ΟΓΑ (Οργανισμός Γεωργικών Ασφαλίσεων)", "OGA"};
		} else if (organizationDomainId.equalsIgnoreCase("Passports")) {
			organizationDomainName = new String[]{"Διαβατήρια και visas", "Passports"};
		} else if (organizationDomainId.equalsIgnoreCase("PaymentOrder")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Διαταγή πληρωμής", "Payment Order"};
		} else if (organizationDomainId.equalsIgnoreCase("PeopleSocietiesLiving")) {
			organizationDomainName = new String[]{"Άνθρωποι, Κοινότητες και Διαβίωση", "People Societies Living"};
		} else if (organizationDomainId.equalsIgnoreCase("PersonalData")) {
			organizationDomainName = new String[]{"Προστασία προσωπικών δεδομένων", "Personal Data"};
		} else if (organizationDomainId.equalsIgnoreCase("Plots")) {
			organizationDomainName = new String[]{"Οικόπεδα", "Plots"};
		} else if (organizationDomainId.equalsIgnoreCase("Policing")) {
			organizationDomainName = new String[]{"Αστυνόμευση", "Policing"};
		} else if (organizationDomainId.equalsIgnoreCase("PreservedConstructions")) {
			organizationDomainName = new String[]{"Διατηρητέα", "Preserved Constructions"};
		} else if (organizationDomainId.equalsIgnoreCase("ProfessionRights")) {
			organizationDomainName = new String[]{"Άδειες επαγγέλματος", "Profession Rights"};
		} else if (organizationDomainId.equalsIgnoreCase("Prostitution")) {
			organizationDomainName = new String[]{"Εκδιδόμενα πρόσωπα", "Prostitution"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicMarkets")) {
			organizationDomainName = new String[]{"Λαϊκές αγορές", "Public Markets"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicResourcesManagement")) {
			organizationDomainName = new String[]{"Διαχείριση δημοσίου υλικού", "Public Resources Management"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicSecuriryCivilRights")) {
			organizationDomainName = new String[]{"Ασφάλεια, Δημόσια Τάξη, Δικαιοσύνη και Δικαιώματα", "Public Securiry Civil Rights"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicServantIssues")) {
			organizationDomainName = new String[]{"Θέματα δημοσίων υπαλλήλων", "Public Servant Issues"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicServantRecruitment")) {
			organizationDomainName = new String[]{"Πρόσληψη στο δημόσιο", "Public Servant Recruitment"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicServants")) {
			organizationDomainName = new String[]{"Ασφαλισμένοι δημοσίου", "Public Servants"};
		} else if (organizationDomainId.equalsIgnoreCase("PublicTransportation")) {
			organizationDomainName = new String[]{"Μέσα μεταφοράς", "Public Transportation"};
		} else if (organizationDomainId.equalsIgnoreCase("Quarries")) {
			organizationDomainName = new String[]{"Λατομεία", "Quarries"};
		} else if (organizationDomainId.equalsIgnoreCase("Recruitment")) {
			organizationDomainName = new String[]{"Προκηρύξεις - Προσλήψεις", "Recruitment"};
		} else if (organizationDomainId.equalsIgnoreCase("Residence")) {
			organizationDomainName = new String[]{"Κατοικία", "Residence"};
		} else if (organizationDomainId.equalsIgnoreCase("Seaside")) {
			organizationDomainName = new String[]{"Αιγιαλός", "Seaside"};
		} else if (organizationDomainId.equalsIgnoreCase("SecurityBusiness")) {
			organizationDomainName = new String[]{"Κλειδαράδες - Ιδιωτικές επιχειρήσεις ασφαλείας - Συναγερμοί - Αδειοδοτήσεις επαγγελματιών", "Security Business"};
		} else if (organizationDomainId.equalsIgnoreCase("Settlements")) {
			organizationDomainName = new String[]{"Οικισμοί", "Settlements"};
		} else if (organizationDomainId.equalsIgnoreCase("Ships")) {
			organizationDomainName = new String[]{"Πλοία", "Ships"};
		} else if (organizationDomainId.equalsIgnoreCase("ShipsEmployment")) {
			organizationDomainName = new String[]{"Πληρώματα εμπορικών πλοίων", "Ships Employment"};
		} else if (organizationDomainId.equalsIgnoreCase("SmallShips")) {
			organizationDomainName = new String[]{"Σκάφη", "Small Ships"};
		} else if (organizationDomainId.equalsIgnoreCase("SocialAccession")) {
			organizationDomainName = new String[]{"Κοινωνική ένταξη", "Social Accession"};
		} else if (organizationDomainId.equalsIgnoreCase("SocialPolitics")) {
			organizationDomainName = new String[]{"Κοινωνική πολιτική", "Social Politics"};
		} else if (organizationDomainId.equalsIgnoreCase("Sports")) {
			organizationDomainName = new String[]{"Αθλητισμός - Αθλητική Νομοθεσία - Αθλητικά θέματα", "Sports"};
		} else if (organizationDomainId.equalsIgnoreCase("SportsCultureTourism")) {
			organizationDomainName = new String[]{"Αθλητισμός, Πολιτισμός, Τέχνες, Ψυχαγωγία, Τουρισμός", "Sports Culture Tourism"};
		} else if (organizationDomainId.equalsIgnoreCase("StockRaising")) {
			organizationDomainName = new String[]{"Κτηνοτροφία - Πτηνοτροφία", "Stock Raising"};
		} else if (organizationDomainId.equalsIgnoreCase("StreetLayout")) {
			organizationDomainName = new String[]{"Ρυμοτομία", "Street Layout"};
		} else if (organizationDomainId.equalsIgnoreCase("Students")) {
			organizationDomainName = new String[]{"Μαθητές - Σπουδαστές - Φοιτητές", "Students"};
		} else if (organizationDomainId.equalsIgnoreCase("Surname")) {
			organizationDomainName = new String[]{"Επώνυμο", "Surname"};
		} else if (organizationDomainId.equalsIgnoreCase("TAE")) {
			organizationDomainName = new String[]{"ΤΑΕ", "TAE"};
		} else if (organizationDomainId.equalsIgnoreCase("TAJI")) {
			organizationDomainName = new String[]{"ΤΑΞΥ (Ταμείο aσφάλισης Ξενοδοχοϋπαλλήλων", "TAXY"};
		} else if (organizationDomainId.equalsIgnoreCase("Taxi")) {
			organizationDomainName = new String[]{"Ταξί", "Taxi"};
		} else if (organizationDomainId.equalsIgnoreCase("TaxIssuesCertificates")) {
			organizationDomainName = new String[]{"Φορολογικά θέματα - Υπηρεσίες φορολογίας εισοδήματος - Πιστοποιητικά", "Tax Issue Certificates"};
		} else if (organizationDomainId.equalsIgnoreCase("TaxIssuesLaws")) {
			organizationDomainName = new String[]{"Φορολογικά θέματα - Υποχρεώσεις - Κανονισμοί", "Tax Issue Laws"};
		} else if (organizationDomainId.equalsIgnoreCase("TEAYEK")) {
			organizationDomainName = new String[]{"ΤΕΑΥΕΚ (Ταμείο επικουρικής ασφαλίσεως υπαλλήλων εμπορικών καταστημάτων)", "TEAYEK"};
		} else if (organizationDomainId.equalsIgnoreCase("TEBE")) {
			organizationDomainName = new String[]{"ΤΕΒΕ", "TEBE"};
		} else if (organizationDomainId.equalsIgnoreCase("TelecomFaire")) {
			organizationDomainName = new String[]{"Τηλεπικοινωνιακές συνδέσεις και τέλη", "Telecom Faire"};
		} else if (organizationDomainId.equalsIgnoreCase("TelecommunicationsLaw")) {
			organizationDomainName = new String[]{"θεσμικά, ρυθμιστικά και κανονιστικά θέματα τηλεπικοινωνιών και ταχυδρομείων", "Telecommunications Law"};
		} else if (organizationDomainId.equalsIgnoreCase("ThirdAge")) {
			organizationDomainName = new String[]{"Τρίτη ηλικία", "Third Age"};
		} else if (organizationDomainId.equalsIgnoreCase("Tourism")) {
			organizationDomainName = new String[]{"Κοινωνικός τουρισμός - Νεανικός Τουρισμός", "Tourism"};
		} else if (organizationDomainId.equalsIgnoreCase("TourismAccomodation")) {
			organizationDomainName = new String[]{"Ξενοδοχεία - Τουριστικά καταλύματα - Τουριστικά γραφεία", "Tourism Accomodation"};
		} else if (organizationDomainId.equalsIgnoreCase("Transplants")) {
			organizationDomainName = new String[]{"Μεταμοσχεύσεις - Δωρεά Οργάνων", "Transplants"};
		} else if (organizationDomainId.equalsIgnoreCase("Transportations")) {
			organizationDomainName = new String[]{"Μεταφορές", "Transportations"};
		} else if (organizationDomainId.equalsIgnoreCase("TSA")) {
			organizationDomainName = new String[]{"ΤΣΑ", "TSA"};
		} else if (organizationDomainId.equalsIgnoreCase("TSAY")) {
			organizationDomainName = new String[]{"ΤΣΑΥ", "TSAY"};
		} else if (organizationDomainId.equalsIgnoreCase("TSMEDE")) {
			organizationDomainName = new String[]{"ΤΣΜΕΔΕ", "TSMEDE"};
		} else if (organizationDomainId.equalsIgnoreCase("Unemployment")) {
			organizationDomainName = new String[]{"Ανεργία και αναζήτηση εργασίας", "Unemployment"};
		} else if (organizationDomainId.equalsIgnoreCase("Unions")) {
			organizationDomainName = new String[]{"Σωματεία - Σύλλογοι - Συνεταιρισμοί", "Unions"};
		} else if (organizationDomainId.equalsIgnoreCase("UrbanPlanning")) {
			organizationDomainName = new String[]{"Πολεοδομία, Ρυμοτομία, Οικοδομές και Κτηματολόγιο", "Urban Planning"};
		} else if (organizationDomainId.equalsIgnoreCase("UrbanServices")) {
			organizationDomainName = new String[]{"Πολεοδομία - Σχέδιο πόλης", "Urban Services"};
		} else if (organizationDomainId.equalsIgnoreCase("Vaccination")) {
			organizationDomainName = new String[]{"Εμβολιασμοί - Εμβόλια", "Vaccination"};
		} else if (organizationDomainId.equalsIgnoreCase("WaterResources")) {
			organizationDomainName = new String[]{"Υδάτινοι Πόροι", "Water Resources"};
		} else if (organizationDomainId.equalsIgnoreCase("WaterSupply")) {
			organizationDomainName = new String[]{"Ύδρευση - Αποχέτευση", "Water Supply"};
		} else if (organizationDomainId.equalsIgnoreCase("Will")) {
			organizationDomainName = new String[]{"Αστικό Δίκαιο - Διαθήκη", "Will"};
		} else if (organizationDomainId.equalsIgnoreCase("WorkSafety")) {
			organizationDomainName = new String[]{"Ασφάλεια και υγεία στην εργασία", "Work Safety"};
		} else if (organizationDomainId.equalsIgnoreCase("Youth")) {
			organizationDomainName = new String[]{"Νέα γενιά - Τουρισμός - Επιχειρηματικότητα", "Youth"};
		} else if (organizationDomainId.equalsIgnoreCase("YouthTourism")) {
			organizationDomainName = new String[]{"Κοινωνικός τουρισμός - Νεανικός Τουρισμός", "Youth Tourism"};
		} else if (organizationDomainId.equalsIgnoreCase("Εntrepreneurship")) {
			organizationDomainName = new String[]{"Επιχειρηματικότητα", "Εntrepreneurship"};
		} else {
			organizationDomainName = new String[]{"", ""};
			writeUnknownMetadata("organizationDomain", organizationDomainId);
		}
		
		return organizationDomainName;
	}
	
	/**
     * Find the name of the VAT type id.
     * 
     * @param String the id of the VAT type
     * @return String[] the Greek and English name of the VAT type
     */
	public String[] findVatTypeName(String vatTypeId) {
		
		String[] vatTypeName = null;
		
		if (vatTypeId.equalsIgnoreCase("EL")) {
			vatTypeName = new String[]{"Εθνικό (Φυσικά και Νομικά πρόσωπα)", "National (Natural persons and Legal entities)"};
		} else if (vatTypeId.equalsIgnoreCase("EU")) {
			vatTypeName = new String[]{"Νομικό πρόσωπο Κράτους-μέλους της ΕΕ", "Legal entity of a member state of the EU"};
		} else if (vatTypeId.equalsIgnoreCase("EUP")) {
			vatTypeName = new String[]{"Φυσικό πρόσωπο Κράτους-μέλους της ΕΕ", "Natural person of a member State of the EU"};
		} else if (vatTypeId.equalsIgnoreCase("INT")) {
			vatTypeName = new String[]{"Οργανισμός χωρίς ΑΦΜ", "Organization without VAT"};
		} else if (vatTypeId.equalsIgnoreCase("O")) {
			vatTypeName = new String[]{"Χώρας εκτός ΕΕ", "Country outside the EU"};
		} else {
			vatTypeName = new String[]{"", ""};
			writeUnknownMetadata("unitCategory", vatTypeId);
		}
		
		return vatTypeName;
	}
	
	/**
     * Convert a Date to its Calendar instance.
     * 
     * @param Date a date
     * @return Calendar the Calendar instance of the provided Date
     */
	public Calendar dateToCalendarConverter(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return cal;
	}
	
	/**
     * Create the ending part of the URI of the provided organization.
     * 
     * @param Organization the organization under investigation
     * @param Organization the supervisor of the organization
     * @return String the ending part of the URI of the provided organization
     */
	public String organizationUriBuilder(Organization org, Organization supervisor) {
		
		String uri = "";
		String orgVatId = org.getVatNumber().replace(" ", "");
		
		if (orgVatId.equalsIgnoreCase("")) {
			orgVatId = "VatNotProvided";
		}
		
		/*if (org.getSupervisorId() != null) { //org has supervisor
			if (supervisor != null) {
				String superVatId = supervisor.getVatNumber().replace(" ", "");
				if (orgVatId != "") {
					if (superVatId.equalsIgnoreCase(orgVatId)) { //epoptevomenos foreas me idio AFM 
						uri = superVatId + "/" + org.getUid().replace(" ", "");
					} else { //epoptevomenos foreas me diaforetiko AFM 
						uri = orgVatId;
					}
				} else {
					uri = superVatId + "/" + org.getUid().replace(" ", "");
				}
			} else {
				uri = "NullSupervisor" + "/" + orgVatId;
			}
		} else { //org does not have supervisor
			uri = orgVatId;
		}*/
		
		if (supervisor != null) {
			uri = orgVatId + "/" + org.getUid().replace(" ", "");
		} else {
			if (orgVatId.equalsIgnoreCase("")) {
				uri = orgVatId + "/" + org.getUid().replace(" ", "");
			} else {
				uri = orgVatId;
			}
		}
		//uri = orgVatId + "/" + org.getUid().replace(" ", "");
		
		return uri; 
	}
	
	/**
     * Calculate the suspension time until the execution time 
     * (daily at 00:15 local time).
     * 
     * @return int the suspension time until the execution time
     */
	public int timeUntilExecution() {
		
		DateTime today = new DateTime();
		DateTime tommorrow = today.plusDays(1).withTimeAtStartOfDay().plusMinutes(15); //00:15

		Duration duration = new Duration(today, tommorrow);
		int diffMinutes = (int) duration.getStandardMinutes();
		System.out.println("Sleeping for: " + diffMinutes + " minutes...");
		
		return diffMinutes;
	}
	
}