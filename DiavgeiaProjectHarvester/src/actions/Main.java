package actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDate;

import organizations.QueryConfiguration;

import ontology.OntologyInitialization;

import objects.Decision;
import objects.DictionaryItems;
//import objects.Organizations;
import objects.Position;

import utils.Configuration;
import utils.HelperMethods;

import virtuoso.jena.driver.VirtGraph;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author G. Razis
 * @author G. Vafeiadis
 */
public class Main {

	public static VirtGraph graphOrgs = null;

	/**
	 * Method to initiate the process
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException, IOException {

		HelperMethods hm = new HelperMethods();

		if (Configuration.searchDay) {
			for (;;) {
				TimeUnit.MINUTES.sleep(hm.timeUntilExecution());
				System.out.println("Woke up!");

				actions();
			}
		} else {
			actions();
		}

	}

	private static void actions() throws IOException {

		HelperMethods hm = new HelperMethods();
		RdfActions rdfActions = new RdfActions();
		MonthlyRdfActions monthlyRdfActions = new MonthlyRdfActions();
		HandleApiRequests handleRequests = new HandleApiRequests();
		OntologyInitialization ontInit = new OntologyInitialization();

		/* Organizations Graph */
		graphOrgs = new VirtGraph(QueryConfiguration.queryGraphOrganizations, QueryConfiguration.connectionString,
				QueryConfiguration.username, QueryConfiguration.password);
		System.out.println("Connected to Organizations Graph!");

		Model model = rdfActions.remoteOrLocalModel(false);

		// Only the first time perform the basic initialization on the model
		ontInit.setPrefixes(model);
		ontInit.createHierarchies(model);
		ontInit.addConceptSchemesToModel(model);

		/** MONTHLY **/
		if (Configuration.searchMontlhy) {
			DictionaryItems dictItemsList = null;
			List<String> dictionaries = Arrays.asList("ORG_CATEGORY", "ORG_UNIT_CATEGORY", "ORG_DOMAIN", "THK",
					"VAT_TYPE");

			for (String dict : dictionaries) {
				dictItemsList = handleRequests.getDictionaryItems(dict); // Fetch
																			// the
																			// items
																			// of
																			// the
																			// provided
																			// dictionary
				monthlyRdfActions.addDiavgeiaRelatedConceptsToModel(model, dictItemsList); // create
																							// their
																							// SKOS
																							// Concepts
			}

			// Create OrganizationStatus
			monthlyRdfActions.addOrganizationStatusToModel(model);

			// Create DecisionStatus
			monthlyRdfActions.addDecisionStatusToModel(model);

			// Create Regulatory Act Type
			monthlyRdfActions.addKanonistikiToModel(model);

			// Create Opinion Org Type
			monthlyRdfActions.addOpinionToModel(model);

			// Create Budget Kind
			monthlyRdfActions.addBudgetKindToModel(model);

			// Create Account Type
			monthlyRdfActions.addAccountTypeToModel(model);

			// Create Position Type
			monthlyRdfActions.addPositionTypeToModel(model);

			// Create Collective Kind
			monthlyRdfActions.addCollectiveKindToModel(model);

			// Create Collective Type
			monthlyRdfActions.addCollectiveTypeToModel(model);

			// Create Vacancy Type
			monthlyRdfActions.addVacancyTypeToModel(model);

			// Create Administrative Change
			monthlyRdfActions.addAdminChangeToModel(model);

			// Create Time Period
			monthlyRdfActions.addTimePeriodToModel(model);

			// Create SelectionCriterion
			monthlyRdfActions.addSelectionCriteriaToModel(model);

			// Create BudgetType
			monthlyRdfActions.addBudgetTypeToModel(model);

			// Create the Fek Type Issues
			dictItemsList = handleRequests.getDictionaryItems("FEKTYPES");
			monthlyRdfActions.addFekTypeIsuesToModel(model, dictItemsList);

			// Create the Countries
			dictItemsList = handleRequests.getDictionaryItems("EE_MEMBER");
			monthlyRdfActions.addCountriesToModel(model, dictItemsList);

			// Create the Currencies
			dictItemsList = handleRequests.getDictionaryItems("CURRENCY");
			monthlyRdfActions.addCurrenciesToModel(model, dictItemsList);

			// Create the Positions
			ArrayList<Position> positionsList = handleRequests.getAllPositions();
			monthlyRdfActions.addAllPositionsToModel(model, positionsList);

			// Create the Organizations, Feks, Units, and Signers
			// Organizations organizationsList =
			// handleRequests.getAllOrganizations();
			// monthlyRdfActions.addAllOrganizationsToModel(model,
			// organizationsList);

			/* store the model */
			rdfActions.writeModel(model);
		}

		/** DAILY **/
		if (Configuration.searchDay) {
			// Find the previous date
			String previousDate = hm.getPreviousDate();
			// previousDate = "2015-04-22";

			// fetch the decisions of the specific date
			dailyDecisions(previousDate, model);
		}

		/** PERIOD **/
		if (Configuration.searchPeriod) {
			LocalDate startDate = new LocalDate("2015-12-01");
			LocalDate endDate = new LocalDate("2015-12-16"); // this date is not
																// retrieved

			List<String> datesList = hm.getListOfDates(startDate, endDate);

			for (String date : datesList) {
				// fetch the decisions of the specific date
				dailyDecisions(date, model);
			}
		}

		/* close the model */
		model.close();

		/* close the graphOrgs connection */
		graphOrgs.close();

		System.out.println("\nFinished!");
	}

	/**
	 * Fetch all the decisions related to the provided date and add them to the
	 * existing model.
	 * 
	 * @param String
	 *            the date to search for decisions
	 * @param Model
	 *            the model we are working with
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static void dailyDecisions(String aDate, Model model) throws IOException {

		RdfActions rdfActions = new RdfActions();
		HandleApiRequests handleRequests = new HandleApiRequests();
		HelperMethods hm = new HelperMethods();

		// fetch the Decisions for that date
		System.out.println("Searching date: " + aDate);
		// starts
		// read csv
		String csvFile = "csv_file/subprojects.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
			String[] project = line.split(cvsSplitBy);
			String opsCode = project[0];
			String code = project[1];
			String subjectOfProject = project[2];
			String afm = project[3];

			List<Decision> descionsList = handleRequests.searchDecisions(aDate, model, subjectOfProject, afm);

			int decisionCounter = 1;
			int totalDecisions = descionsList.size();

			// For each Decision add it to the RDF graph
			for (Decision decision : descionsList) {
				System.out.print("\nDecision number " + decisionCounter + " out of " + totalDecisions);
				// decision = handleRequests.searchSingleDecision("");
				if (decision.getAda() != null) {
					rdfActions.createRdfFromDecision(decision, model, opsCode, code);
				}
				decisionCounter += 1;
				// break;
			}
		} // ends

		/* store the model */
		rdfActions.writeModel(model);

		hm.writeUnknownMetadata("datesOK", aDate);
	}

	/*
	 * Β.1.3 --> ΩΘΓΝ9-Ψ7Ψ Β.2.1 --> ΩΚΣΛ469Β7Τ-69Η Β.2.2 --> 9ΠΔ5Ι-ΕΑ1 Δ.1 -->
	 * 6ΣΝΤΟΞΘΟ-165 Δ.2.1 --> 7ΖΒΘΟΞΧΓ-Ι9Ρ Δ.2.2 --> Ω3ΙΔΟΡΛΟ-ΦΤΕ corrected -->
	 * ΒΝΩ9Ι-Μ10 no AFF provided --> ΒΙ6ΝΟΡΥΑ-8ΦΦ noVatOrg as 9... --> 4ΑΘΓΩ0Ο-Ι
	 * noVatOrg --> Ω04Ο46ΨΧΗ8-ΚΤΙ
	 */

}