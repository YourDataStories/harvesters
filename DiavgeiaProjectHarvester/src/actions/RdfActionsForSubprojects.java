package actions;

//import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import organizations.AgentHandler;
import organizations.Queries;

import objects.Attachment;
import objects.Decision;
import objects.Organization;

import ontology.Ontology;
import ontology.OntologyInitialization;

import utils.Configuration;
import utils.Formatters;
import utils.HelperMethods;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author G. Razis
 * @author G. Vafeiadis
 */
public class RdfActionsForSubprojects {

	/**
	 * Fetch all the details from the provided decision object, create the RDF
	 * triples and add them to the existing model.
	 * 
	 * @param Decision
	 *            a decision object
	 * @param Model
	 *            the model to add the triples
	 * @throws IOException
	 */

	public void createRdfFromDecision(Decision decisionObject, Model model, String uriSubproject)
			throws IOException {

		Queries qsOrgs = new Queries();
		HelperMethods hm = new HelperMethods();
		AgentHandler agents = new AgentHandler();
		HandleApiRequests handleRequests = new HandleApiRequests();

		Resource committedItemResource = null;
		Resource decisionResource = null;
		Resource expenditureLineResource = null;
		Resource unitPriceSpecificationResource = null;
		Resource sellerCommittedItemResource = null;
		Resource expenseApprovalOrPaymentResource = null;
		Resource buyerExpenseApprResource = null;
		Resource sellerExpenseApprResource = null;
		Resource contractResource = null;
		Resource sellerTendersResource = null;
		Resource awardCriteriaCombinationResource = null;
		Resource criterionWeightingResource = null;

		String decisionTypeId = decisionObject.getDecisionTypeId();

		String uriDecisionFactor = null;

		Resource projectResource = null;
		Resource subprojectResource = null;

		projectResource = model.createResource(uriSubproject.substring(0, 49).replace("Contract", "PublicWork"));

		subprojectResource = model.createResource(uriSubproject);

		/* B Type */
		if (decisionTypeId.equalsIgnoreCase("Β.1.3")) {
			System.out.println("\nΒ.1.3");
			System.out.println("ada: " + decisionObject.getAda());
			/* Common metadata */
			/** Committed Item **/
			if (decisionObject.getCorrectedVersionId() != null) { // has
																	// correctedVersionId
				uriDecisionFactor = decisionObject.getAda() + "/Corrected/"
						+ Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
				committedItemResource = model.createResource(
						Ontology.instancePrefix + "CommittedItem/" + uriDecisionFactor, Ontology.committedItemResource);
				/** Original Decision - Corrected Decision **/
				Resource originalCommittedItemResource = model
						.createResource(Ontology.instancePrefix + "CommittedItem/" + decisionObject.getAda());
				// model.createResource(Ontology.instancePrefix +
				// "CommittedItem/" + uriDecisionFactor,
				// Ontology.financialResource);
				model.add(committedItemResource, RDF.type, Ontology.financialResource);
				model.add(committedItemResource, RDF.type, Ontology.administrativeResource);
				originalCommittedItemResource.addProperty(Ontology.hasCorrectedDecision, committedItemResource);
//				committedItemResource.addProperty(Ontology.sellerCriteria, "true", XSDDatatype.XSDboolean);
				subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, committedItemResource);
				projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, committedItemResource);
			} else { // normal decision
				uriDecisionFactor = decisionObject.getAda();
				committedItemResource = model.createResource(
						Ontology.instancePrefix + "CommittedItem/" + uriDecisionFactor, Ontology.committedItemResource);
				// model.createResource(Ontology.instancePrefix +
				// "CommittedItem/" + uriDecisionFactor,
				// Ontology.financialResource);
				model.add(committedItemResource, RDF.type, Ontology.financialResource);
				model.add(committedItemResource, RDF.type, Ontology.administrativeResource);
				model.add(committedItemResource, Ontology.decisionType, model.createLiteral("Committed Item", "en"));
				model.add(committedItemResource, Ontology.decisionType,
						model.createLiteral("ΑΝΑΛΗΨΗ ΥΠΟΧΡΕΩΣΗΣ", "el"));
//				committedItemResource.addProperty(Ontology.sellerCriteria, "true", XSDDatatype.XSDboolean);
				subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, committedItemResource);
				projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, committedItemResource);
			}
			committedItemResource.addLiteral(Ontology.ada, decisionObject.getAda());
			committedItemResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			committedItemResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			committedItemResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			committedItemResource.addLiteral(Ontology.issued,
					hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			committedItemResource.addLiteral(Ontology.submissionTimestamp,
					hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** CommittedItem - Organization **/
			if (decisionObject.getUnitIds() != null && decisionObject.getUnitIds().size() > 0) {
				for (String unitId : decisionObject.getUnitIds()) {
					committedItemResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); // find
																												// the
																												// details
																												// of
																												// the
																												// Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					committedItemResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.organizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.orgOrganizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.registeredOrganizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.businessEntityResource);
				}
			}

			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			committedItemResource.addProperty(Ontology.decisionStatus, model.getResource(statusIndividualUri[0]));

			if (decisionObject.getSubject() != null) {
				committedItemResource.addProperty(Ontology.subject, hm.cleanInputData(decisionObject.getSubject()),
						"el");
			}

			if (decisionObject.getProtocolNumber() != null) {
				committedItemResource.addLiteral(Ontology.protocolNumber,
						hm.cleanInputData(decisionObject.getProtocolNumber()));
			}

			if (decisionObject.getDocumentUrl() != null) {
				committedItemResource.addLiteral(Ontology.documentUrl, decisionObject.getDocumentUrl());
			}

			if (decisionObject.getDocumentChecksum() != null) {
				committedItemResource.addLiteral(Ontology.documentChecksum, decisionObject.getDocumentChecksum());
			}

			if (decisionObject.getCorrectedVersionId() != null) {
				committedItemResource.addLiteral(Ontology.correctedVersionId, decisionObject.getCorrectedVersionId());
			}

			for (String themCatId : decisionObject.getThematicCategoryIds()) {
				/** CommittedItem - ThematicCategory **/
				committedItemResource.addProperty(Ontology.hasThematicCategory,
						model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}

			for (String signerId : decisionObject.getSignerIds()) {
				/** CommittedItem - Signer **/
				committedItemResource.addProperty(Ontology.signer,
						model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}

			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(
							Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription,
							hm.cleanInputData(attachment.getDescription()));
					attachmentResource.addLiteral(Ontology.attachmentFilename, attachment.getFilename());
					attachmentResource.addLiteral(Ontology.attachmentMimeType, attachment.getMimeType());
					attachmentResource.addLiteral(Ontology.attachmentChecksum, attachment.getChecksum());
					attachmentResource.addLiteral(Ontology.attachmentSize, attachment.getSize());
					/** CommittedItem - Attachment **/
					committedItemResource.addProperty(Ontology.hasAttachment, attachmentResource);
				}
			}

			/* specific metadata of B.1.3 */
			committedItemResource.addLiteral(Ontology.documentType,
					decisionObject.getExtraFieldValues().get("documentType"));
			if (decisionObject.getExtraFieldValues().get("financialYear") != null) {
				committedItemResource.addLiteral(Ontology.financialYear, model.createTypedLiteral(
						decisionObject.getExtraFieldValues().get("financialYear"), XSDDatatype.XSDgYear));
			}
			if (decisionObject.getExtraFieldValues().get("recalledExpenseDecision") != null) {
				committedItemResource.addLiteral(Ontology.isRecalledExpenseDecision,
						(Boolean) decisionObject.getExtraFieldValues().get("recalledExpenseDecision"));
			}
			if (decisionObject.getExtraFieldValues().get("budgettype") != "") {
				String[] budgetTypeUri = hm
						.findBudgetTypeIndividual((String) decisionObject.getExtraFieldValues().get("budgettype"));
				committedItemResource.addProperty(Ontology.hasBudgetCategory, model.getResource(budgetTypeUri[0]));
			}

			committedItemResource.addLiteral(Ontology.isPartialWithdrawal,
					(Boolean) decisionObject.getExtraFieldValues().get("partialead"));

			if (decisionObject.getExtraFieldValues().get("entryNumber") != null) {
				committedItemResource.addLiteral(Ontology.entryNumber,
						decisionObject.getExtraFieldValues().get("entryNumber"));
			}

			if ((decisionObject.getExtraFieldValues().get("relatedPartialADA") != null)
					&& (decisionObject.getExtraFieldValues().get("relatedPartialADA") != "")) {
				/** relatedCommittedItem **/
				Resource relatedCommittedItem = model.createResource(
						Ontology.instancePrefix + "CommittedItem/"
								+ decisionObject.getExtraFieldValues().get("relatedPartialADA"),
						Ontology.committedItemResource);
				committedItemResource.addProperty(Ontology.hasRelatedCommittedItem, relatedCommittedItem);
			}
			if (decisionObject.getExtraFieldValues().get("relatedDecisions") != null) {
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedDecisions = (ArrayList<Map<String, ? extends Object>>) decisionObject
						.getExtraFieldValues().get("relatedDecisions");
				for (Map<String, ? extends Object> relatedDecision : relatedDecisions) {
					/** relatedDecision **/
					Object[] instanceData = hm
							.findDecisionTypeInstance(relatedDecision.get("relatedDecisionsADA").toString());
					if (instanceData != null) {
						Resource relatedDecisionResource = model.createResource(
								Ontology.instancePrefix + instanceData[0] + "/"
										+ relatedDecision.get("relatedDecisionsADA").toString(),
								(Resource) instanceData[1]);
						committedItemResource.addProperty((Property) instanceData[2], relatedDecisionResource);
					}
				}
			}

			/** unitPriceSpecificationResource **/
			if (decisionObject.getExtraFieldValues().get("amountWithVAT") != null) {
				unitPriceSpecificationResource = model.createResource(
						Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor,
						Ontology.unitPriceSpecificationResource);
				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> amountWithVat = (Map<String, ? extends Object>) decisionObject
						.getExtraFieldValues().get("amountWithVAT");
				System.out.println(amountWithVat);

				if (amountWithVat.get("amount") != null) {
					unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue,
							model.createTypedLiteral(amountWithVat.get("amount"), XSDDatatype.XSDfloat));
					unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
				}

				if ((amountWithVat.get("currency") != null) && (!amountWithVat.get("currency").toString().isEmpty())) {
					unitPriceSpecificationResource.addProperty(Ontology.hasCurrency,
							model.getResource(Ontology.instancePrefix + "Currency/" + amountWithVat.get("currency")));
				}

				/** UnitPriceSpecification - CommittedItem **/
				committedItemResource.addProperty(Ontology.price, unitPriceSpecificationResource);
			}

			@SuppressWarnings("unchecked")
			List<Map<String, ? extends Object>> amountWithKae = (ArrayList<Map<String, ? extends Object>>) decisionObject
					.getExtraFieldValues().get("amountWithKae");
			System.out.println("\n" + amountWithKae);
			if (amountWithKae != null) {
				if (!amountWithKae.isEmpty()) {
					for (int i = 0; i < amountWithKae.size(); i++) {
						/** expenditureLineResource **/
						expenditureLineResource = model.createResource(
								Ontology.instancePrefix + "ExpenditureLine/" + uriDecisionFactor + "_" + (i + 1),
								Ontology.expenditureLineResource);

						@SuppressWarnings("unchecked")
						Map<String, ? extends Object> sponsorAfmDetails = (Map<String, ? extends Object>) amountWithKae
								.get(i).get("sponsorAFMName");
						System.out.println(sponsorAfmDetails);
						if (sponsorAfmDetails != null) {
							if (!sponsorAfmDetails.isEmpty()) {
								String afmSponsor = decisionObject.getAda() + "_NotSponsor";
								boolean isNoVatOrg = false;
								if (sponsorAfmDetails.get("afm") != null) {
									if ((sponsorAfmDetails.get("afm").equals(""))
											|| (hm.cleanVatId((String) sponsorAfmDetails.get("afm")).equals(""))) {
										afmSponsor = decisionObject.getAda() + "_EmptyVatId";
									} else {
										afmSponsor = hm.cleanVatId((String) sponsorAfmDetails.get("afm"));
									}
								} else if (sponsorAfmDetails.get("noVATOrg") != null) {
									afmSponsor = (String) sponsorAfmDetails.get("noVATOrg");
									isNoVatOrg = true;
								}
								afmSponsor = hm.cleanVatId(afmSponsor);
								/** sellerCommittedItemResource **/
								if (!isNoVatOrg) { // has VAT id case
									Object[] agentsDtls = agents.handleAgent(afmSponsor, decisionObject.getAda());
									sellerCommittedItemResource = (Resource) agentsDtls[0];
								} else { // noVatOrg case
									sellerCommittedItemResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									if (sellerCommittedItemResource == null) {
//										qsOrgs.insertAgentUri(Main.graphOrgs, afmSponsor, false);
										sellerCommittedItemResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs,
												afmSponsor);
									}
								}

//								if (sponsorAfmDetails.get("afmType") != null) {
//									qsOrgs.insertVatType(Main.graphOrgs, sellerCommittedItemResource.getURI(),
//											sponsorAfmDetails.get("afmType").toString());
//								}
//								if (sponsorAfmDetails.get("afmCountry") != null) {
//									qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerCommittedItemResource.getURI(),
//											sponsorAfmDetails.get("afmCountry").toString());
//								}
//								if (sponsorAfmDetails.get("name") != null) {
//									qsOrgs.insertName(Main.graphOrgs, sellerCommittedItemResource.getURI(),
//											hm.cleanInputData((String) sponsorAfmDetails.get("name")));
//								}
//								if (sponsorAfmDetails.get("noVATOrg") != null) {
//									qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerCommittedItemResource.getURI(),
//											sponsorAfmDetails.get("noVATOrg").toString());
//								}
								/**
								 * ExpenditureLine - SellerCommittedItem
								 **/
								if (sellerCommittedItemResource != null) {
								expenditureLineResource.addProperty(Ontology.seller, sellerCommittedItemResource);
								}
							} else {
								System.out.println("No *sponsorAfmDetails* provided (empty)\n");
							}
						} else {
							System.out.println("No *sponsorAfmDetails* provided\n");
						}

						if (amountWithKae.get(i).get("amountWithVAT") != null) {
							/**
							 * unitPriceSpecificationResource (amountWithVAT)
							 **/
							unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix
									+ "UnitPriceSpecification/" + uriDecisionFactor + "_amount_" + (i + 1),
									Ontology.unitPriceSpecificationResource);
							unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue,
									model.createTypedLiteral(amountWithKae.get(i).get("amountWithVAT"),
											XSDDatatype.XSDfloat));
							unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
							unitPriceSpecificationResource.addProperty(Ontology.hasCurrency,
									model.getResource(Ontology.instancePrefix + "Currency/" + "EUR"));
							/**
							 * ExpenditureLine - UnitPriceSpecification
							 * (amountWithVAT)
							 **/
							expenditureLineResource.addProperty(Ontology.amount, unitPriceSpecificationResource);

							if (amountWithKae.get(i).get("kaeBudgetRemainder") != null) {
								/**
								 * unitPriceSpecificationResource
								 * (kaeBudgetRemainder)
								 **/
								unitPriceSpecificationResource = model
										.createResource(
												Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor
														+ "_budgetRem_" + (i + 1),
												Ontology.unitPriceSpecificationResource);
								unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue,
										model.createTypedLiteral(amountWithKae.get(i).get("kaeBudgetRemainder"),
												XSDDatatype.XSDfloat));
								unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
								unitPriceSpecificationResource.addProperty(Ontology.hasCurrency,
										model.getResource(Ontology.instancePrefix + "Currency/" + "EUR"));
								/**
								 * ExpenditureLine - UnitPriceSpecification
								 * (kaeBudgetRemainder)
								 **/
								expenditureLineResource.addProperty(Ontology.remainingBudgetAmount,
										unitPriceSpecificationResource);
							}
							if (amountWithKae.get(i).get("kaeCreditRemainder") != null) {
								/**
								 * unitPriceSpecificationResource
								 * (kaeCreditRemainder)
								 **/
								unitPriceSpecificationResource = model
										.createResource(
												Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor
														+ "_creditRem_" + (i + 1),
												Ontology.unitPriceSpecificationResource);
								unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue,
										model.createTypedLiteral(amountWithKae.get(i).get("kaeCreditRemainder"),
												XSDDatatype.XSDfloat));
								unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
								unitPriceSpecificationResource.addProperty(Ontology.hasCurrency,
										model.getResource(Ontology.instancePrefix + "Currency/" + "EUR"));
								/**
								 * ExpenditureLine - UnitPriceSpecification
								 * (kaeCreditRemainder)
								 **/
								expenditureLineResource.addProperty(Ontology.remainingCreditAmount,
										unitPriceSpecificationResource);
							}
						}

						/** kaeResource **/
						Resource kaeResource = model
								.createResource(
										Ontology.instancePrefix + "kaeCodes/" + amountWithKae.get(i).get("kae")
												.toString().replace("\"", "").replace("\\", "").replace(" ", "_"),
								Ontology.kaeResource);
						kaeResource.addLiteral(Ontology.kae,
								hm.cleanInputData((String) amountWithKae.get(i).get("kae")));
						/** ExpenditureLine - KAE **/
						expenditureLineResource.addProperty(Ontology.hasKae, kaeResource);

						/** CommittedItem - ExpenditureLine **/
						committedItemResource.addProperty(Ontology.hasExpenditureLine, expenditureLineResource);
					}
				} else {
					System.out.println("No *amountWithKae* provided (empty)\n");
				}
			} else {
				System.out.println("No *amountWithKae* provided\n");
			}
		}

		// ----------------------------------------starts non financial
		// ----------------------------------------------------//

		else if (decisionTypeId.equalsIgnoreCase("2.4.7.1") || decisionTypeId.equalsIgnoreCase("2.4.6.1")
				|| decisionTypeId.equalsIgnoreCase("Γ.2") || decisionTypeId.equalsIgnoreCase("Γ.3.4")
				|| decisionTypeId.equalsIgnoreCase("Α.2") || decisionTypeId.equalsIgnoreCase("Α.1.1")
				|| decisionTypeId.equalsIgnoreCase("Α.1.2") || decisionTypeId.equalsIgnoreCase("Α.3")
				|| decisionTypeId.equalsIgnoreCase("Α.4") || decisionTypeId.equalsIgnoreCase("Α.5")
				|| decisionTypeId.equalsIgnoreCase("Α.6") || decisionTypeId.equalsIgnoreCase("Β.1.1")
				|| decisionTypeId.equalsIgnoreCase("Β.1.2") || decisionTypeId.equalsIgnoreCase("Β.3")
				|| decisionTypeId.equalsIgnoreCase("Β.4") || decisionTypeId.equalsIgnoreCase("Β.5")
				|| decisionTypeId.equalsIgnoreCase("100") || decisionTypeId.equalsIgnoreCase("Γ.3.1")
				|| decisionTypeId.equalsIgnoreCase("Γ.3.2") || decisionTypeId.equalsIgnoreCase("Γ.3.3")
				|| decisionTypeId.equalsIgnoreCase("Γ.3.5") || decisionTypeId.equalsIgnoreCase("Γ.3.6")
				|| decisionTypeId.equalsIgnoreCase("Ε.1") || decisionTypeId.equalsIgnoreCase("Ε.2")
				|| decisionTypeId.equalsIgnoreCase("Ε.3") || decisionTypeId.equalsIgnoreCase("Ε.4")) {
			// System.out.println("\n2.4.7.1");
			System.out.println("ada: " + decisionObject.getAda());
			/* Common metadata */
			/** Decisions non financial **/
			if (decisionObject.getCorrectedVersionId() != null) { // has
																	// correctedVersionId
				uriDecisionFactor = decisionObject.getAda() + "/Corrected/"
						+ Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
				decisionResource = model.createResource(Ontology.instancePrefix + "Decision/" + uriDecisionFactor,
						Ontology.decisionResource);
				/** Original Decision - Corrected Decision **/
				Resource originalDecisionResource = model
						.createResource(Ontology.instancePrefix + "Decision/" + decisionObject.getAda());
				model.add(decisionResource, RDF.type, Ontology.nonFinancialResource);
				model.add(decisionResource, RDF.type, Ontology.administrativeResource);
				originalDecisionResource.addProperty(Ontology.hasCorrectedDecision, decisionResource);
				projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, decisionResource);
//				decisionResource.addProperty(Ontology.sellerCriteria, "true", XSDDatatype.XSDboolean);
				subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, decisionResource);
			} else { // normal decision
				uriDecisionFactor = decisionObject.getAda();
				decisionResource = model.createResource(Ontology.instancePrefix + "Decision/" + uriDecisionFactor,
						Ontology.decisionResource);
				model.createResource(Ontology.instancePrefix + "Decision/" + uriDecisionFactor,
						Ontology.decisionResource);
				model.add(decisionResource, RDF.type, Ontology.nonFinancialResource);
				model.add(decisionResource, RDF.type, Ontology.administrativeResource);
				projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, decisionResource);
				subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, decisionResource);
//				decisionResource.addProperty(Ontology.sellerCriteria, "true", XSDDatatype.XSDboolean);
			}

			// define decision types of Non Financial Decisions

			if (decisionTypeId.equalsIgnoreCase("Α.2")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΚΑΝΟΝΙΣΤΙΚΗ ΠΡΑΞΗ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Regulatory Act", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Α.1.1")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΝΟΜΟΣ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Law", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Α.1.2")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΡΑΞΗ ΝΟΜΟΘΕΤΙΚΟΥ ΠΕΡΙΕΧΟΜΕΝΟΥ (Σύνταγμα, άρθρο 44, παρ 1)", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Legislative Content Act (Constitution article 44, par.1)", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Α.3")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΕΓΚΥΚΛΙΟΣ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Circular", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Α.4")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΓΝΩΜΟΔΟΤΗΣΗ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Legal Opinion", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Α.5")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΡΑΚΤΙΚΑ (Νομικού Συμβουλίου του Κράτους)", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Proceeding Records (of the State's Legal Council)", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Α.6")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΕΚΘΕΣΗ ΑΠΟΤΙΜΗΣΗΣ ΓΙΑ ΤΗΝ ΚΑΤΑΣΤΑΣΗ ΤΗΣ ΥΦΙΣΤΑΜΕΝΗΣ ΝΟΜΟΘΕΣΙΑΣ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Subordinate Legislation Report", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Β.1.1")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΕΓΚΡΙΣΗ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Budget Approval", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Β.1.2")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΕΠΙΤΡΟΠΙΚΟ ΕΝΤΑΛΜΑ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Commission Writ", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Β.4")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΔΩΡΕΑ - ΕΠΙΧΟΡΗΓΗΣΗ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Grant", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Β.5")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΑΡΑΧΩΡΗΣΗ ΧΡΗΣΗΣ ΠΕΡΙΟΥΣΙΑΚΩΝ ΣΤΟΙΧΕΙΩΝ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Property Use Assignment/Transfer", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("100")) {
				model.add(decisionResource, Ontology.decisionType, model
						.createLiteral("ΠΡΑΞΗ ΠΟΥ ΑΦΟΡΑ ΣΕ ΘΕΣΗ ΓΕΝΙΚΟΥ - ΕΙΔΙΚΟΥ ΓΡΑΜΜΑΤΕΑ - ΜΟΝΟΜΕΛΕΣ ΟΡΓΑΝΟ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("General Secretary's - Special Secretary's - Single Body's Deed", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.2")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral(
								"ΠΡΑΞΗ ΠΟΥ ΑΦΟΡΑ ΣΕ ΣΥΛΛΟΓΙΚΟ ΟΡΓΑΝΟ - ΕΠΙΤΡΟΠΗ - ΟΜΑΔΑ ΕΡΓΑΣΙΑΣ - ΟΜΑΔΑ ΕΡΓΟΥ - ΜΕΛΗ ΣΥΛΛΟΓΙΚΟΥ ΟΡΓΑΝΟΥ",
								"el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Collective Body's Work - Collective Act", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.3.1")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΡΟΚΗΡΥΞΗ ΠΛΗΡΩΣΗΣ ΘΕΣΕΩΝ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Proclamation of Filling Posts", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.3.2")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΙΝΑΚΕΣ ΕΠΙΤΥΧΟΝΤΩΝ, ΔΙΟΡΙΣΤΕΩΝ & ΕΠΙΛΑΧΟΝΤΩΝ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Appointed and Reserve Lists", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.3.3")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΔΙΟΡΙΣΜΟΣ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Assignment", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.3.4")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΣΥΜΒΑΣΗ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Contract", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.3.5")) {
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("ΥΠΗΡΕΣΙΑΚΗ ΜΕΤΑΒΟΛΗ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Agency/Bureau Alteration", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Γ.3.6")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΑΘΩΩΤΙΚΗ ΠΕΙΘΑΡΧΙΚΗ ΑΠΟΦΑΣΗ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Exoneratory Discipline Ruling", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Ε.1")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΡΑΞΗ ΥΠΑΓΩΓΗΣ ΕΠΕΝΔΥΣΕΩΝ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Investment Conformity Act", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Ε.2")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΣΥΜΒΑΣΗ-ΠΡΑΞΕΙΣ ΑΝΑΠΤΥΞΙΑΚΩΝ ΝΟΜΩΝ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Agreement - Development Law Acts", "en"));
			}
			if (decisionTypeId.equalsIgnoreCase("Ε.3")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΑΠΟΦΑΣΗ ΕΝΑΡΞΗΣ ΠΑΡΑΓΩΓΙΚΗΣ ΛΕΙΤΟΥΡΓΙΑΣ ΕΠΕΝΔΥΣΗΣ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Decision for the commencement of  investment productive operation", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("Ε.4")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΑΛΛΗ ΠΡΑΞΗ ΑΝΑΠΤΥΞΙΑΚΟΥ ΝΟΜΟΥ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Development Law Act", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("2.4.6.1")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΠΡΑΞΕΙΣ ΧΩΡΟΤΑΞΙΚΟΥ - ΠΟΛΕΟΔΟΜΙΚΟΥ ΠΕΡΙΕΧΟΜΕΝΟΥ", "el"));
				model.add(decisionResource, Ontology.decisionType, model.createLiteral("Land Use Acts", "en"));
			}

			if (decisionTypeId.equalsIgnoreCase("2.4.7.1")) {
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("ΛΟΙΠΕΣ ΑΤΟΜΙΚΕΣ ΔΙΟΙΚΗΤΙΚΕΣ ΠΡΑΞΕΙΣ", "el"));
				model.add(decisionResource, Ontology.decisionType,
						model.createLiteral("Other Individual Administrative Acts", "en"));
			}

			decisionResource.addLiteral(Ontology.ada, decisionObject.getAda());
			decisionResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			decisionResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			decisionResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			decisionResource.addLiteral(Ontology.issued, hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			decisionResource.addLiteral(Ontology.submissionTimestamp,
					hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** Decision - Organization **/
			if (decisionObject.getUnitIds() != null && decisionObject.getUnitIds().size() > 0) {
				for (String unitId : decisionObject.getUnitIds()) {
					decisionResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); // find
																												// the
																												// details
																												// of
																												// the
																												// Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					decisionResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
				}
			}

			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			decisionResource.addProperty(Ontology.decisionStatus, model.getResource(statusIndividualUri[0]));

			if (decisionObject.getSubject() != null) {
				decisionResource.addProperty(Ontology.subject, hm.cleanInputData(decisionObject.getSubject()), "el");
			}

			if (decisionObject.getProtocolNumber() != null) {
				decisionResource.addLiteral(Ontology.protocolNumber,
						hm.cleanInputData(decisionObject.getProtocolNumber()));
			}

			if (decisionObject.getDocumentUrl() != null) {
				decisionResource.addLiteral(Ontology.documentUrl, decisionObject.getDocumentUrl());
			}

			// if (decisionObject.getDocumentChecksum() != null) {
			// committedItemResource.addLiteral(Ontology.documentChecksum,
			// decisionObject.getDocumentChecksum());
			// }

			if (decisionObject.getCorrectedVersionId() != null) {
				decisionResource.addLiteral(Ontology.correctedVersionId, decisionObject.getCorrectedVersionId());
			}

			for (String themCatId : decisionObject.getThematicCategoryIds()) {
				/** Decision - ThematicCategory **/
				decisionResource.addProperty(Ontology.hasThematicCategory,
						model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}

			for (String signerId : decisionObject.getSignerIds()) {
				/** Decision - Signer **/
				decisionResource.addProperty(Ontology.signer,
						model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}

			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(
							Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription,
							hm.cleanInputData(attachment.getDescription()));
					attachmentResource.addLiteral(Ontology.attachmentFilename, attachment.getFilename());
					attachmentResource.addLiteral(Ontology.attachmentMimeType, attachment.getMimeType());
					attachmentResource.addLiteral(Ontology.attachmentChecksum, attachment.getChecksum());
					attachmentResource.addLiteral(Ontology.attachmentSize, attachment.getSize());
					/** Decision - Attachment **/
					decisionResource.addProperty(Ontology.hasAttachment, attachmentResource);
				}
			}
			
//			/** contract Info **/ // for Γ.3.4
//			if (decisionObject.getExtraFieldValues().get("person") != null) {
//				@SuppressWarnings("unchecked")
//				List<Map<String, ? extends Object>> sponsorDetails = (ArrayList<Map<String, ? extends Object>>) decisionObject
//						.getExtraFieldValues().get("person");
//				for (Map<String, ? extends Object> sponsorDetail : sponsorDetails) {
//					System.out.println("\n" + sponsorDetail);
//					if (sponsorDetail != null) {
//						if (!sponsorDetail.isEmpty()) {
//								/** expenditureLineResource **/
//								expenditureLineResource = model.createResource(Ontology.instancePrefix
//										+ "ExpenditureLine/" + uriDecisionFactor,
//										Ontology.expenditureLineResource);
//
//								// @SuppressWarnings("unchecked")
//								// Map<String, ? extends Object>
//								// sponsorAfmDetails = (Map<String, ? extends
//								// Object>) sponsorDetails
//								// .get(i).get("sponsorAFMName");
//								// System.out.println(sponsorAfmDetails);
//								// if (sponsorAfmDetails != null) {
//								// if (!sponsorAfmDetails.isEmpty()) {
//								String afmSponsor = decisionObject.getAda() + "_NotSponsor";
//								boolean isNoVatOrg = false;
//								if (sponsorDetail.get("afm") != null) {
//									if (sponsorDetail.get("afm").equals("")) {
//										afmSponsor = decisionObject.getAda() + "_EmptyVatId";
//									} else {
//										afmSponsor = (String) sponsorDetail.get("afm");
//									}
//								}
//								// else if (sponsorAfmDetails.get("noVATOrg") !=
//								// null) {
//								// afmSponsor = (String)
//								// sponsorAfmDetails.get("noVATOrg");
//								// isNoVatOrg = true;
//								// }
//								afmSponsor = hm.cleanVatId(afmSponsor);
//								/** sellerExpApprResource **/
//								if (!isNoVatOrg) { // has VAT id case
//									Object[] agentsDtls = agents.handleAgent(afmSponsor, decisionObject.getAda());
//									sellerExpenseApprResource = (Resource) agentsDtls[0];
//								} else { // noVatOrg case
//									sellerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
//									if (sellerExpenseApprResource == null) {
//										qsOrgs.insertAgentUri(Main.graphOrgs, afmSponsor, false);
//										sellerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
//									}
//								}
//
//								if (sponsorDetail.get("afmType") != null) {
//									qsOrgs.insertVatType(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											sponsorDetail.get("afmType").toString());
//								}
//								if (sponsorDetail.get("afmCountry") != null) {
//									qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											sponsorDetail.get("afmCountry").toString());
//								}
//								if (sponsorDetail.get("name") != null) {
//									qsOrgs.insertName(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											hm.cleanInputData((String) sponsorDetail.get("name")));
//								}
//								// if (sponsorAfmDetails.get("noVATOrg") !=
//								// null) {
//								// qsOrgs.insertNoVatOrgId(Main.graphOrgs,
//								// sellerExpenseApprResource.getURI(),
//								// sponsorAfmDetails.get("noVATOrg").toString());
//								// }
//								/**
//								 * ExpenditureLine - SellerExpenseApprovalItem
//								 **/
//								expenditureLineResource.addProperty(Ontology.seller, sellerExpenseApprResource);
//								// } else {
//								// System.out.println("No *sponsorAfmDetails*
//								// provided (empty)\n");
//								// }
//								// } else {
//								// System.out.println("No *sponsorAfmDetails*
//								// provided\n");
//								// }
//
//								/**
//								 * ExpenseApprovalItem or Payment -
//								 * ExpenditureLine
//								 **/
//								decisionResource.addProperty(Ontology.hasExpenditureLine, expenditureLineResource);
//						} else {
//							System.out.println("No *sponsorDetails* provided (empty)\n");
//						}
//					} else {
//						System.out.println("No *sponsorDetails* provided\n");
//					}
//				}
//			}
//			
//			@SuppressWarnings("unchecked")
//			Map<String, ? extends Object> contractAmountDetails = (Map<String, ? extends Object>) decisionObject
//					.getExtraFieldValues().get("contractAmount");
//			System.out.println(contractAmountDetails);
//			if (contractAmountDetails != null) {
//				if (!contractAmountDetails.isEmpty()) {
//					/**
//					 * unitPriceSpecificationResource
//					 * (contractAmount)
//					 **/
//					unitPriceSpecificationResource = model
//							.createResource(
//									Ontology.instancePrefix + "UnitPriceSpecification/"
//											+ uriDecisionFactor,
//									Ontology.unitPriceSpecificationResource);
//					if (contractAmountDetails.get("amount") != null) {
//						unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue,
//								model.createTypedLiteral(contractAmountDetails.get("amount"),
//										XSDDatatype.XSDfloat));
//						unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded,
//								true);
//					}
//					if ((contractAmountDetails.get("currency") != null)
//							&& (!contractAmountDetails.get("currency").toString().isEmpty())) {
//						unitPriceSpecificationResource.addProperty(Ontology.hasCurrency,
//								model.getResource(Ontology.instancePrefix + "Currency/"
//										+ contractAmountDetails.get("currency")));
//					}
//					/**
//					 * ExpenditureLine -
//					 * UnitPriceSpecification
//					 * (expenseAmount)
//					 **/
//					expenditureLineResource.addProperty(Ontology.amount,
//							unitPriceSpecificationResource);
//				} else {
//					System.out.println("No *expenseAmountDetails* provided (empty)\n");
//				}
//			} else {
//				System.out.println("No *expenseAmountDetails* provided\n");
//			}
		}


		// ------------------------------------------ ends non financial
		// ----------------------------------------------------//

		else if (decisionTypeId.equalsIgnoreCase("Β.2.1") || decisionTypeId.equalsIgnoreCase("Β.2.2")) {
			/** expenseApprovalOrPaymentResource **/
			if (decisionTypeId.equalsIgnoreCase("Β.2.1")) {
				System.out.println("\nΒ.2.1");
				System.out.println("ada: " + decisionObject.getAda());
				/** ExpenseApprovalItem **/
				if (decisionObject.getCorrectedVersionId() != null) { // has
																		// correctedVersionId
					uriDecisionFactor = decisionObject.getAda() + "/Corrected/"
							+ Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
					expenseApprovalOrPaymentResource = model.createResource(
							Ontology.instancePrefix + "ExpenseApprovalItem/" + uriDecisionFactor,
							Ontology.expenseApprovalItemResource);
					/** Original Decision - Corrected Decision **/
					Resource originalExpenseApprovalOrPaymentResource = model
							.createResource(Ontology.instancePrefix + "ExpenseApprovalItem/" + decisionObject.getAda());
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.financialResource);
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.administrativeResource);
					originalExpenseApprovalOrPaymentResource.addProperty(Ontology.hasCorrectedDecision,
							expenseApprovalOrPaymentResource);
					subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
//					expenseApprovalOrPaymentResource.addProperty(Ontology.sellerCriteria, "true",
//							XSDDatatype.XSDboolean);
					projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
				} else { // normal decision
					uriDecisionFactor = decisionObject.getAda();
					expenseApprovalOrPaymentResource = model.createResource(
							Ontology.instancePrefix + "ExpenseApprovalItem/" + uriDecisionFactor,
							Ontology.expenseApprovalItemResource);
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.financialResource);
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.administrativeResource);
					subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
//					expenseApprovalOrPaymentResource.addProperty(Ontology.sellerCriteria, "true",
//							XSDDatatype.XSDboolean);
					projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
					model.createResource(Ontology.instancePrefix + "ExpenseApprovalItem/" + uriDecisionFactor,
							Ontology.financialResource);
					model.add(expenseApprovalOrPaymentResource, Ontology.decisionType,
							model.createLiteral("Expense Approval", "en"));
					model.add(expenseApprovalOrPaymentResource, Ontology.decisionType,
							model.createLiteral("ΕΓΚΡΙΣΗ ΔΑΠΑΝΗΣ", "el"));
				}
			} else {
				System.out.println("\nΒ.2.2");
				System.out.println("ada: " + decisionObject.getAda());
				/** Payment **/
				if (decisionObject.getCorrectedVersionId() != null) { // has
																		// correctedVersionId
					uriDecisionFactor = decisionObject.getAda() + "/Corrected/"
							+ Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
					expenseApprovalOrPaymentResource = model.createResource(
							Ontology.instancePrefix + "SpendingItem/" + uriDecisionFactor,
							Ontology.spendingItemResource);
					/** Original Decision - Corrected Decision **/
					Resource originalExpenseApprovalOrPaymentResource = model
							.createResource(Ontology.instancePrefix + "SpendingItem/" + decisionObject.getAda());
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.financialResource);
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.administrativeResource);
					originalExpenseApprovalOrPaymentResource.addProperty(Ontology.hasCorrectedDecision,
							expenseApprovalOrPaymentResource);
					subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
//					expenseApprovalOrPaymentResource.addProperty(Ontology.sellerCriteria, "true",
//							XSDDatatype.XSDboolean);
					projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
				} else { // normal decision
					uriDecisionFactor = decisionObject.getAda();
					expenseApprovalOrPaymentResource = model.createResource(
							Ontology.instancePrefix + "SpendingItem/" + uriDecisionFactor,
							Ontology.spendingItemResource);
					model.createResource(Ontology.instancePrefix + "SpendingItem/" + uriDecisionFactor,
							Ontology.financialResource);
					model.add(expenseApprovalOrPaymentResource, RDF.type, Ontology.administrativeResource);
					subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
//					expenseApprovalOrPaymentResource.addProperty(Ontology.sellerCriteria, "true",
//							XSDDatatype.XSDboolean);
					projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision,
							expenseApprovalOrPaymentResource);
					model.add(expenseApprovalOrPaymentResource, Ontology.decisionType,
							model.createLiteral("Spending Item", "en"));
					model.add(expenseApprovalOrPaymentResource, Ontology.decisionType,
							model.createLiteral("ΟΡΙΣΤΙΚΟΠΟΙΗΣΗ ΠΛΗΡΩΜΗΣ", "el"));
				}
			}

			/* Common Resources */
			expenseApprovalOrPaymentResource.addLiteral(Ontology.ada, decisionObject.getAda());
			expenseApprovalOrPaymentResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			expenseApprovalOrPaymentResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			expenseApprovalOrPaymentResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			expenseApprovalOrPaymentResource.addLiteral(Ontology.issued,
					hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			expenseApprovalOrPaymentResource.addLiteral(Ontology.submissionTimestamp,
					hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** ExpenseApprovalItem or Payment - Organization **/
			if (decisionObject.getUnitIds() != null && decisionObject.getUnitIds().size() > 0) {
				for (String unitId : decisionObject.getUnitIds()) {
					expenseApprovalOrPaymentResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); // find
																												// the
																												// details
																												// of
																												// the
																												// Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					expenseApprovalOrPaymentResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.organizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.orgOrganizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.registeredOrganizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.businessEntityResource);
				}
			}

			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			expenseApprovalOrPaymentResource.addProperty(Ontology.decisionStatus,
					model.getResource(statusIndividualUri[0]));

			if (decisionObject.getSubject() != null) {
				expenseApprovalOrPaymentResource.addProperty(Ontology.subject,
						hm.cleanInputData(decisionObject.getSubject()), "el");
			}

			if (decisionObject.getProtocolNumber() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.protocolNumber,
						hm.cleanInputData(decisionObject.getProtocolNumber()));
			}

			if (decisionObject.getDocumentUrl() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.documentUrl, decisionObject.getDocumentUrl());
			}

			if (decisionObject.getDocumentChecksum() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.documentChecksum,
						decisionObject.getDocumentChecksum());
			}

			if (decisionObject.getCorrectedVersionId() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.correctedVersionId,
						decisionObject.getCorrectedVersionId());
			}

			for (String themCatId : decisionObject.getThematicCategoryIds()) {
				/** ExpenseApprovalItem or Payment - ThematicCategory **/
				expenseApprovalOrPaymentResource.addProperty(Ontology.hasThematicCategory,
						model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}

			for (String signerId : decisionObject.getSignerIds()) {
				/** ExpenseApprovalItem or Payment - Signer **/
				expenseApprovalOrPaymentResource.addProperty(Ontology.signer,
						model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}

			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(
							Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription,
							hm.cleanInputData(attachment.getDescription()));
					attachmentResource.addLiteral(Ontology.attachmentFilename, attachment.getFilename());
					attachmentResource.addLiteral(Ontology.attachmentMimeType, attachment.getMimeType());
					attachmentResource.addLiteral(Ontology.attachmentChecksum, attachment.getChecksum());
					attachmentResource.addLiteral(Ontology.attachmentSize, attachment.getSize());
					/** ExpenseApprovalItem or Payment - Attachment **/
					expenseApprovalOrPaymentResource.addProperty(Ontology.hasAttachment, attachmentResource);
				}
			}

			/* specific metadata of Β.2.1 or Β.2.2 */
			expenseApprovalOrPaymentResource.addLiteral(Ontology.documentType,
					decisionObject.getExtraFieldValues().get("documentType"));

			@SuppressWarnings("unchecked") // for Β.2.1 and Β.2.2
			Map<String, ? extends Object> orgDetails = (Map<String, ? extends Object>) decisionObject
					.getExtraFieldValues().get("org");
			System.out.println(orgDetails);
			if (orgDetails != null) {
				if (!orgDetails.isEmpty()) {
					String afmOrg = decisionObject.getAda() + "_NotSponsor";
					boolean isNoVatOrg = false;
					if (orgDetails.get("afm") != null) {
						if (orgDetails.get("afm").equals("")) {
							afmOrg = decisionObject.getAda() + "_EmptyVatId";
						} else {
							afmOrg = (String) orgDetails.get("afm");
						}
					} else if (orgDetails.get("noVATOrg") != null) {
						afmOrg = (String) orgDetails.get("noVATOrg");
						isNoVatOrg = true;
					}
					afmOrg = hm.cleanVatId(afmOrg);
					/** buyerExpenseApprResource **/
					if (!isNoVatOrg) { // has VAT id case
						Object[] agentsDtls = agents.handleAgent(afmOrg, decisionObject.getAda());
						buyerExpenseApprResource = (Resource) agentsDtls[0];
					} else { // noVatOrg case
						buyerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmOrg);
						if (buyerExpenseApprResource == null) {
//							qsOrgs.insertAgentUri(Main.graphOrgs, afmOrg, false);
							buyerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmOrg);
						}
					}

//					if (orgDetails.get("afmType") != null) {
//						qsOrgs.insertVatType(Main.graphOrgs, buyerExpenseApprResource.getURI(),
//								orgDetails.get("afmType").toString());
//					}
//					if (orgDetails.get("afmCountry") != null) {
//						qsOrgs.insertRegisteredAt(Main.graphOrgs, buyerExpenseApprResource.getURI(),
//								orgDetails.get("afmCountry").toString());
//					}
//					if (orgDetails.get("name") != null) {
//						qsOrgs.insertName(Main.graphOrgs, buyerExpenseApprResource.getURI(),
//								hm.cleanInputData((String) orgDetails.get("name")));
//					}
//					if (orgDetails.get("noVATOrg") != null) {
//						qsOrgs.insertNoVatOrgId(Main.graphOrgs, buyerExpenseApprResource.getURI(),
//								orgDetails.get("noVATOrg").toString());
//					}
					/**
					 * ExpenseApprovalItem or Payment - BuyerExpenseApprovalItem
					 **/
					if (buyerExpenseApprResource != null) {
					expenseApprovalOrPaymentResource.addProperty(Ontology.buyer, buyerExpenseApprResource);
					}
				} else {
					System.out.println("No *orgDetails* provided (empty)\n");
				}
			} else {
				System.out.println("No *orgDetails* provided\n");
			}

			/** payeeSpendingResource **/ // for Β.2.1 and Β.2.2
			@SuppressWarnings("unchecked")
			List<Map<String, ? extends Object>> sponsorDetails = (ArrayList<Map<String, ? extends Object>>) decisionObject
					.getExtraFieldValues().get("sponsor");
			System.out.println("\n" + sponsorDetails);
			if (sponsorDetails != null) {
				if (!sponsorDetails.isEmpty()) {
					for (int i = 0; i < sponsorDetails.size(); i++) {
						/** expenditureLineResource **/
						expenditureLineResource = model.createResource(
								Ontology.instancePrefix + "ExpenditureLine/" + uriDecisionFactor + "_" + (i + 1),
								Ontology.expenditureLineResource);

						@SuppressWarnings("unchecked")
						Map<String, ? extends Object> sponsorAfmDetails = (Map<String, ? extends Object>) sponsorDetails
								.get(i).get("sponsorAFMName");
						System.out.println(sponsorAfmDetails);
						if (sponsorAfmDetails != null) {
							if (!sponsorAfmDetails.isEmpty()) {
								String afmSponsor = decisionObject.getAda() + "_NotSponsor";
								boolean isNoVatOrg = false;
								if (sponsorAfmDetails.get("afm") != null) {
									if (sponsorAfmDetails.get("afm").equals("")) {
										afmSponsor = decisionObject.getAda() + "_EmptyVatId";
									} else {
										afmSponsor = (String) sponsorAfmDetails.get("afm");
									}
								} else if (sponsorAfmDetails.get("noVATOrg") != null) {
									afmSponsor = (String) sponsorAfmDetails.get("noVATOrg");
									isNoVatOrg = true;
								}
								afmSponsor = hm.cleanVatId(afmSponsor);
								/** sellerExpApprResource **/
								if (!isNoVatOrg) { // has VAT id case
									Object[] agentsDtls = agents.handleAgent(afmSponsor, decisionObject.getAda());
									sellerExpenseApprResource = (Resource) agentsDtls[0];
								} else { // noVatOrg case
									sellerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									if (sellerExpenseApprResource == null) {
//										qsOrgs.insertAgentUri(Main.graphOrgs, afmSponsor, false);
										sellerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									}
								}

//								if (sponsorAfmDetails.get("afmType") != null) {
//									qsOrgs.insertVatType(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											sponsorAfmDetails.get("afmType").toString());
//								}
//								if (sponsorAfmDetails.get("afmCountry") != null) {
//									qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											sponsorAfmDetails.get("afmCountry").toString());
//								}
//								if (sponsorAfmDetails.get("name") != null) {
//									qsOrgs.insertName(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											hm.cleanInputData((String) sponsorAfmDetails.get("name")));
//								}
//								if (sponsorAfmDetails.get("noVATOrg") != null) {
//									qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerExpenseApprResource.getURI(),
//											sponsorAfmDetails.get("noVATOrg").toString());
//								}
								/**
								 * ExpenditureLine - SellerExpenseApprovalItem
								 **/
								if (sellerExpenseApprResource != null) {
								expenditureLineResource.addProperty(Ontology.seller, sellerExpenseApprResource);
								}
							} else {
								System.out.println("No *sponsorAfmDetails* provided (empty)\n");
							}
						} else {
							System.out.println("No *sponsorAfmDetails* provided\n");
						}

						if ((sponsorDetails.get(i).get("cpv") != null) && (sponsorDetails.get(i).get("cpv") != "")) {
							/** cpvCodeResource **/
							Resource cpvCodeResource = model.createResource(
									Ontology.instancePrefix + "CPV/"
											+ hm.cleanInputData((String) sponsorDetails.get(i).get("cpv")),
									Ontology.cpvResource);
							/** ExpenditureLine - CPV **/
							expenditureLineResource.addProperty(Ontology.hasCpv, cpvCodeResource);
						}

						if ((sponsorDetails.get(i).get("kae") != null) && (sponsorDetails.get(i).get("kae") != "")) {
							/** kaeResource **/
							Resource kaeResource = model.createResource(
									Ontology.instancePrefix + "kaeCodes/" + sponsorDetails.get(i).get("kae").toString()
											.replace("\"", "").replace("\\", "").replace(" ", "_"),
									Ontology.kaeResource);
							kaeResource.addLiteral(Ontology.kae,
									hm.cleanInputData((String) sponsorDetails.get(i).get("kae")));
							/** ExpenditureLine - KAE **/
							expenditureLineResource.addProperty(Ontology.hasKae, kaeResource);
						}

						@SuppressWarnings("unchecked")
						Map<String, ? extends Object> expenseAmountDetails = (Map<String, ? extends Object>) sponsorDetails
								.get(i).get("expenseAmount");
						System.out.println(expenseAmountDetails);
						if (expenseAmountDetails != null) {
							if (!expenseAmountDetails.isEmpty()) {
								/**
								 * unitPriceSpecificationResource
								 * (expenseAmount)
								 **/
								unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix
										+ "UnitPriceSpecification/" + uriDecisionFactor + "_" + (i + 1),
										Ontology.unitPriceSpecificationResource);
								if (expenseAmountDetails.get("amount") != null) {
									unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue,
											model.createTypedLiteral(expenseAmountDetails.get("amount"),
													XSDDatatype.XSDfloat));
									unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
								}
								if ((expenseAmountDetails.get("currency") != null)
										&& (!expenseAmountDetails.get("currency").toString().isEmpty())) {
									unitPriceSpecificationResource.addProperty(Ontology.hasCurrency,
											model.getResource(Ontology.instancePrefix + "Currency/"
													+ expenseAmountDetails.get("currency")));
								}
								/**
								 * ExpenditureLine - UnitPriceSpecification
								 * (expenseAmount)
								 **/
								expenditureLineResource.addProperty(Ontology.amount, unitPriceSpecificationResource);
							} else {
								System.out.println("No *expenseAmountDetails* provided (empty)\n");
							}
						} else {
							System.out.println("No *expenseAmountDetails* provided\n");
						}

						/**
						 * ExpenseApprovalItem or Payment - ExpenditureLine
						 **/
						expenseApprovalOrPaymentResource.addProperty(Ontology.hasExpenditureLine,
								expenditureLineResource);
					}
				} else {
					System.out.println("No *sponsorDetails* provided (empty)\n");
				}
			} else {
				System.out.println("No *sponsorDetails* provided\n");
			}

			if (decisionObject.getExtraFieldValues().get("relatedDecisions") != null) {// for
																						// Β.2.1
																						// and
																						// Β.2.2
																						// //lista
																						// {
																						// "relatedDecisionsADA":
																						// "ΒΛ9ΛΝ-32Ν"
																						// }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedDecisions = (ArrayList<Map<String, ? extends Object>>) decisionObject
						.getExtraFieldValues().get("relatedDecisions");
				for (Map<String, ? extends Object> relatedDecision : relatedDecisions) {
					Object[] instanceData = hm
							.findDecisionTypeInstance(relatedDecision.get("relatedDecisionsADA").toString());
					if (instanceData != null) {
						Resource relatedDecisionResource = model.createResource(
								Ontology.instancePrefix + instanceData[0] + "/"
										+ relatedDecision.get("relatedDecisionsADA").toString(),
								(Resource) instanceData[1]);
						expenseApprovalOrPaymentResource.addProperty((Property) instanceData[2],
								relatedDecisionResource);
					}
				}
			}

			if (decisionObject.getExtraFieldValues().get("relatedAnalipsiYpoxreosis") != null) {// for
																								// Β.2.1
																								// //lista
																								// {
																								// "textRelatedADA":
																								// "ΒΙ65Ν-ΜΚΞ"
																								// }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedAnalipsiYpoxreosisAdas = (ArrayList<Map<String, ? extends Object>>) decisionObject
						.getExtraFieldValues().get("relatedAnalipsiYpoxreosis");
				for (Map<String, ? extends Object> relatedAda : relatedAnalipsiYpoxreosisAdas) {
					if ((relatedAda.get("textRelatedADA") != null) && (relatedAda.get("textRelatedADA") != "")) {
						Resource relatedAnalipsiYpoxreosisAdaResource = model.createResource(
								Ontology.instancePrefix + "CommittedItem/" + relatedAda.get("textRelatedADA"),
								Ontology.committedItemResource);
						expenseApprovalOrPaymentResource.addProperty(Ontology.hasRelatedCommittedItem,
								relatedAnalipsiYpoxreosisAdaResource);
					}
				}
			}

			if (decisionObject.getExtraFieldValues().get("relatedEkgrisiDapanis") != null) {// for
																							// Β.2.2
																							// //lista
																							// {
																							// "textRelatedADA":
																							// "ΒΛ0ΖΝ-889"
																							// }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedEgkrisiDapanisAdas = (ArrayList<Map<String, ? extends Object>>) decisionObject
						.getExtraFieldValues().get("relatedEkgrisiDapanis");
				for (Map<String, ? extends Object> relatedAda : relatedEgkrisiDapanisAdas) {
					if ((relatedAda.get("textRelatedADA") != null) && (relatedAda.get("textRelatedADA") != "")) {
						Object[] instanceData = hm
								.findDecisionTypeInstance(relatedAda.get("textRelatedADA").toString());
						if (instanceData != null) {
							Resource relatedEgkrisiDapanisAdaResource = model
									.createResource(
											Ontology.instancePrefix + instanceData[0] + "/"
													+ relatedAda.get("textRelatedADA").toString(),
											(Resource) instanceData[1]);
							expenseApprovalOrPaymentResource.addProperty((Property) instanceData[2],
									relatedEgkrisiDapanisAdaResource);
						}
					}
				}
			}
			/* Δ Type */
		} else if (decisionTypeId.equalsIgnoreCase("Δ.1") || decisionTypeId.equalsIgnoreCase("Δ.2.1")
				|| decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
			System.out.println("\n" + decisionTypeId);
			System.out.println("ada: " + decisionObject.getAda());
			/* Common metadata */
			/** contractResource **/
			if (decisionObject.getCorrectedVersionId() != null) { // has
																	// correctedVersionId
				uriDecisionFactor = decisionObject.getAda() + "/Corrected/"
						+ Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
				contractResource = model.createResource(Ontology.instancePrefix + "Contract/" + uriDecisionFactor,
						Ontology.contractResource);
				/** Original Decision - Corrected Decision **/
				Resource originalContractResource = model
						.createResource(Ontology.instancePrefix + "Contract/" + decisionObject.getAda());
				model.add(contractResource, RDF.type, Ontology.financialResource);
				model.add(contractResource, RDF.type, Ontology.administrativeResource);
				originalContractResource.addProperty(Ontology.hasCorrectedDecision, contractResource);
				subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, contractResource);
//				contractResource.addProperty(Ontology.sellerCriteria, "true", XSDDatatype.XSDboolean);
				projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, contractResource);
			} else { // normal decision
				uriDecisionFactor = decisionObject.getAda();
				contractResource = model.createResource(Ontology.instancePrefix + "Contract/" + uriDecisionFactor,
						Ontology.contractResource);
				model.add(contractResource, RDF.type, Ontology.financialResource);
				model.add(contractResource, RDF.type, Ontology.administrativeResource);
				// model.createResource(Ontology.instancePrefix + "Contract/" +
				// uriDecisionFactor,
				// Ontology.financialResource);
				subprojectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, contractResource);
//				contractResource.addProperty(Ontology.sellerCriteria, "true", XSDDatatype.XSDboolean);
				projectResource.addProperty(Ontology.hasRelatedAdministrativeDecision, contractResource);
			}

			// define decision types of Δ
			if (decisionTypeId.equalsIgnoreCase("Δ.1")) {
				// Contract
				model.add(contractResource, Ontology.decisionType,
						model.createLiteral("Assignement Works / Supplies / Services / Studies", "en"));
				model.add(contractResource, Ontology.decisionType,
						model.createLiteral("ΑΝΑΘΕΣΗ ΕΡΓΩΝ / ΠΡΟΜΗΘΕΙΩΝ / ΥΠΗΡΕΣΙΩΝ / ΜΕΛΕΤΩΝ", "el"));
			}

			if (decisionTypeId.equalsIgnoreCase("Δ.2.1")) {
				// Contract
				model.add(contractResource, Ontology.decisionType,
						model.createLiteral("Summaries of Declarations", "en"));
				model.add(contractResource, Ontology.decisionType, model.createLiteral("ΠΕΡΙΛΗΨΗ ΔΙΑΚΗΡΥΞΗΣ", "el"));
			}

			if (decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
				// Contract
				model.add(contractResource, Ontology.decisionType, model.createLiteral("Awards", "en"));
				model.add(contractResource, Ontology.decisionType, model.createLiteral("ΚΑΤΑΚΥΡΩΣΗ", "el"));
			}

			contractResource.addLiteral(Ontology.ada, decisionObject.getAda());
			contractResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			contractResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			contractResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			contractResource.addLiteral(Ontology.issued, hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			contractResource.addLiteral(Ontology.submissionTimestamp,
					hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** Contract - Organization **/
			if (decisionObject.getUnitIds() != null && decisionObject.getUnitIds().size() > 0) {
				for (String unitId : decisionObject.getUnitIds()) {
					contractResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
					contractResource.addProperty(Ontology.buyer,
							model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); // find
																												// the
																												// details
																												// of
																												// the
																												// Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					contractResource.addProperty(Ontology.publisher,
							model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
					contractResource.addProperty(Ontology.buyer,
							model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.organizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.orgOrganizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.registeredOrganizationResource);
					// model.add(model.getResource(Ontology.instancePrefix +
					// "Organization/" + orgVatId), RDF.type,
					// Ontology.businessEntityResource);
				}
			}

			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			contractResource.addProperty(Ontology.decisionStatus, model.getResource(statusIndividualUri[0]));

			if (decisionObject.getSubject() != null) {
				contractResource.addProperty(Ontology.subject, hm.cleanInputData(decisionObject.getSubject()), "el");
			}

			if (decisionObject.getProtocolNumber() != null) {
				contractResource.addLiteral(Ontology.protocolNumber,
						hm.cleanInputData(decisionObject.getProtocolNumber()));
			}

			if (decisionObject.getDocumentUrl() != null) {
				contractResource.addLiteral(Ontology.documentUrl, decisionObject.getDocumentUrl());
			}

			if (decisionObject.getDocumentChecksum() != null) {
				contractResource.addLiteral(Ontology.documentChecksum, decisionObject.getDocumentChecksum());
			}

			if (decisionObject.getCorrectedVersionId() != null) {
				contractResource.addLiteral(Ontology.correctedVersionId, decisionObject.getCorrectedVersionId());
			}

			for (String themCatId : decisionObject.getThematicCategoryIds()) {
				/** Contract - ThematicCategory **/
				contractResource.addProperty(Ontology.hasThematicCategory,
						model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}

			for (String signerId : decisionObject.getSignerIds()) {
				/** Contract - Signer **/
				contractResource.addProperty(Ontology.signer,
						model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}

			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(
							Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription,
							hm.cleanInputData(attachment.getDescription()));
					attachmentResource.addLiteral(Ontology.attachmentFilename, attachment.getFilename());
					attachmentResource.addLiteral(Ontology.attachmentMimeType, attachment.getMimeType());
					attachmentResource.addLiteral(Ontology.attachmentChecksum, attachment.getChecksum());
					attachmentResource.addLiteral(Ontology.attachmentSize, attachment.getSize());
					/** Contract - Attachment **/
					contractResource.addProperty(Ontology.hasAttachment, attachmentResource);
				}
			}
			/* Specific Resources */
			if (decisionTypeId.equalsIgnoreCase("Δ.2.1")) {
				/* specific metadata of Δ.2.1 */
				contractResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
				contractResource.addLiteral(Ontology.documentType,
						decisionObject.getExtraFieldValues().get("documentType"));

				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> estimatedAmountDetails = (Map<String, ? extends Object>) decisionObject
						.getExtraFieldValues().get("estimatedAmount");
				System.out.println(estimatedAmountDetails);
				if (estimatedAmountDetails != null) {
					if (!estimatedAmountDetails.isEmpty()) {
						/** unitPriceSpecificationResource **/
						unitPriceSpecificationResource = model.createResource(
								Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor,
								Ontology.unitPriceSpecificationResource);
						if (estimatedAmountDetails.get("amount") != null) {
							unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model
									.createTypedLiteral(estimatedAmountDetails.get("amount"), XSDDatatype.XSDfloat));
							unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, false);
						}
						if ((estimatedAmountDetails.get("currency") != null)
								&& (!estimatedAmountDetails.get("currency").toString().isEmpty())) {
							unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(
									Ontology.instancePrefix + "Currency/" + estimatedAmountDetails.get("currency")));
						}
						/**
						 * Contract - UnitPriceSpecification (estimatedAmount)
						 **/
						contractResource.addProperty(Ontology.documentsPrice, unitPriceSpecificationResource);
						contractResource.addProperty(Ontology.estimatedPrice, unitPriceSpecificationResource);
					} else {
						System.out.println("No *estimatedAmountDetails* provided (empty)\n");
					}
				} else {
					System.out.println("No *estimatedAmountDetails* provided\n");
				}

				@SuppressWarnings("unchecked")
				List<String> cpvCodes = (List<String>) decisionObject.getExtraFieldValues().get("cpv");
				if (cpvCodes != null) {
					for (int i = 0; i < cpvCodes.size(); i++) {
						/** cpvCodeResource **/
						Resource cpvCodeResource = model.createResource(
								Ontology.instancePrefix + "CPV/" + hm.cleanInputData(cpvCodes.get(i)),
								Ontology.cpvResource);
						/** Contract - CPV **/
						if (i == 0) {
							contractResource.addProperty(Ontology.mainObject, cpvCodeResource);
						} else {
							contractResource.addProperty(Ontology.additionalObject, cpvCodeResource);
						}
					}
				}

				if (decisionObject.getExtraFieldValues().get("contestProgressType") != null) {
					String procedureTypeIndividualUri = hm.findProcedureTypeIndividual(
							(String) decisionObject.getExtraFieldValues().get("contestProgressType"));
					if (procedureTypeIndividualUri != null) {
						contractResource.addProperty(Ontology.procedureType,
								model.getResource(procedureTypeIndividualUri));
					}
				}

				if (decisionObject.getExtraFieldValues().get("manifestContractType") != null) {
					String kindTypeIndividualUri = hm.findKindIndividual(
							(String) decisionObject.getExtraFieldValues().get("manifestContractType"));
					if (kindTypeIndividualUri != null) {
						contractResource.addProperty(Ontology.kind, model.getResource(kindTypeIndividualUri));
					}
				}

				if ((decisionObject.getExtraFieldValues().get("orgBudgetCode") != null)
						&& (decisionObject.getExtraFieldValues().get("orgBudgetCode") != "")) {
					String[] budgetTypeIndividualUri = hm.findBudgetTypeIndividual(
							(String) decisionObject.getExtraFieldValues().get("orgBudgetCode"));
					contractResource.addProperty(Ontology.hasBudgetCategory,
							model.getResource(budgetTypeIndividualUri[0]));
				}

				if (decisionObject.getExtraFieldValues().get("manifestSelectionCriterion") != null) {
					/** awardCriteriaCombinationResource **/
					awardCriteriaCombinationResource = model.createResource(
							Ontology.instancePrefix + "AwardCriteriaCombination/" + uriDecisionFactor,
							Ontology.awardCriteriaCombinationResource);
					/** criterionWeightingResource **/
					criterionWeightingResource = model.createResource(
							Ontology.instancePrefix + "CriterionWeighting/" + uriDecisionFactor,
							Ontology.criterionWeightingResource);
					String[] criterionIndividual = hm.findCriterionIndividual(
							(String) decisionObject.getExtraFieldValues().get("manifestSelectionCriterion"));
					criterionWeightingResource.addProperty(Ontology.weightedCriterion,
							model.getResource(criterionIndividual[0]));
					criterionWeightingResource.addLiteral(Ontology.criterionWeight,
							model.createTypedLiteral(criterionIndividual[1], Ontology.pcdtPrefix + "percentage"));

					/** AwardCriteriaCombination - CriterionWeighting **/
					awardCriteriaCombinationResource.addProperty(Ontology.awardCriterion, criterionWeightingResource);
					/** Contract - AwardCriteriaCombination **/
					contractResource.addProperty(Ontology.awardCriteriaCombination, awardCriteriaCombinationResource);
				}
			} else if (decisionTypeId.equalsIgnoreCase("Δ.1") || decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
				/* specific metadata of Δ.1 or Δ.2.2 */
				contractResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
				contractResource.addLiteral(Ontology.documentType,
						decisionObject.getExtraFieldValues().get("documentType"));

				// for Δ.1 and Δ.2.2
				try { // Sometimes the data are in a List of Maps and
						// sometimes in a Map
					@SuppressWarnings("unchecked")
					List<Map<String, ? extends Object>> personDetailsList = (List<Map<String, ? extends Object>>) decisionObject
							.getExtraFieldValues().get("person");
					if (personDetailsList != null) {
						for (Map<String, ? extends Object> personDetails : personDetailsList) {
							System.out.println(personDetails);
							if (personDetails != null) {
								if (!personDetails.isEmpty()) {
									String afmPerson = decisionObject.getAda() + "_NotPerson";
									boolean isNoVatOrg = false;
									if (personDetails.get("afm") != null) {
										if ((personDetails.get("afm").equals(""))
												|| (hm.cleanVatId((String) personDetails.get("afm")).equals(""))) {
											afmPerson = decisionObject.getAda() + "_EmptyVatId";
										} else {
											afmPerson = hm.cleanVatId((String) personDetails.get("afm"));
										}
									} else if (personDetails.get("noVATOrg") != null) {
										afmPerson = (String) personDetails.get("noVATOrg");
										isNoVatOrg = true;
									}
									afmPerson = hm.cleanVatId(afmPerson);
									/** sellerTendersResource **/
									if (!isNoVatOrg) { // has VAT id case
										Object[] agentsDtls = agents.handleAgent(afmPerson, decisionObject.getAda());
										sellerTendersResource = (Resource) agentsDtls[0];
									} else { // noVatOrg case
										sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
										if (sellerTendersResource == null) {
//											qsOrgs.insertAgentUri(Main.graphOrgs, afmPerson, false);
											sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
										}
									}

//									if (personDetails.get("afmType") != null) {
//										qsOrgs.insertVatType(Main.graphOrgs, sellerTendersResource.getURI(),
//												personDetails.get("afmType").toString());
//									}
//									if (personDetails.get("afmCountry") != null) {
//										qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerTendersResource.getURI(),
//												personDetails.get("afmCountry").toString());
//									}
//									if (personDetails.get("name") != null) {
//										qsOrgs.insertName(Main.graphOrgs, sellerTendersResource.getURI(),
//												hm.cleanInputData((String) personDetails.get("name")));
//									}
//									if (personDetails.get("noVATOrg") != null) {
//										qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerTendersResource.getURI(),
//												personDetails.get("noVATOrg").toString());
//									}
									/** Contract - SellerTenders **/
									if (sellerTendersResource != null) {
									contractResource.addProperty(Ontology.seller, sellerTendersResource);
									}
								} else {
									System.out.println("No *personDetails* provided (empty)\n");
								}
							} else {
								System.out.println("No *personDetails* provided\n");
							}
						}
					} else {
						System.out.println("No *personDetails* as List provided\n");
					}
				} catch (Exception e) {
					@SuppressWarnings("unchecked")
					Map<String, ? extends Object> personDetails = (Map<String, ? extends Object>) decisionObject
							.getExtraFieldValues().get("person");
					if (personDetails != null) {
						System.out.println(personDetails);
						if (!personDetails.isEmpty()) {
							String afmPerson = decisionObject.getAda() + "_NotPerson";
							boolean isNoVatOrg = false;
							if (personDetails.get("afm") != null) {
								if ((personDetails.get("afm").equals(""))
										|| (hm.cleanVatId((String) personDetails.get("afm")).equals(""))) {
									afmPerson = decisionObject.getAda() + "_EmptyVatId";
								} else {
									afmPerson = hm.cleanVatId((String) personDetails.get("afm"));
								}
							} else if (personDetails.get("noVATOrg") != null) {
								afmPerson = (String) personDetails.get("noVATOrg");
								isNoVatOrg = true;
							}
							afmPerson = hm.cleanVatId(afmPerson);
							/** sellerTendersResource **/
							if (!isNoVatOrg) { // has VAT id case
								Object[] agentsDtls = agents.handleAgent(afmPerson, decisionObject.getAda());
								sellerTendersResource = (Resource) agentsDtls[0];
							} else { // noVatOrg case
								sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
								if (sellerTendersResource == null) {
//									qsOrgs.insertAgentUri(Main.graphOrgs, afmPerson, false);
									sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
								}
							}

//							if (personDetails.get("afmType") != null) {
//								qsOrgs.insertVatType(Main.graphOrgs, sellerTendersResource.getURI(),
//										personDetails.get("afmType").toString());
//							}
//							if (personDetails.get("afmCountry") != null) {
//								qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerTendersResource.getURI(),
//										personDetails.get("afmCountry").toString());
//							}
//							if (personDetails.get("name") != null) {
//								qsOrgs.insertName(Main.graphOrgs, sellerTendersResource.getURI(),
//										hm.cleanInputData((String) personDetails.get("name")));
//							}
//							if (personDetails.get("noVATOrg") != null) {
//								qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerTendersResource.getURI(),
//										personDetails.get("noVATOrg").toString());
//							}
							/** Contract - SellerTenders **/
							if (sellerTendersResource != null) {
							contractResource.addProperty(Ontology.seller, sellerTendersResource);
							}
						} else {
							System.out.println("No *personDetails* provided (empty)\n");
						}
					} else {
						System.out.println("No *personDetails* as List provided\n");
					}
				}

				// for Δ.1 and Δ.2.2
				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> estimatedAmountDetails = (Map<String, ? extends Object>) decisionObject
						.getExtraFieldValues().get("awardAmount");
				System.out.println(estimatedAmountDetails);
				if (estimatedAmountDetails != null) {
					if (!estimatedAmountDetails.isEmpty()) {
						/** unitPriceSpecificationResource **/
						unitPriceSpecificationResource = model.createResource(
								Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor,
								Ontology.unitPriceSpecificationResource);
						if (estimatedAmountDetails.get("amount") != null) {
							unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model
									.createTypedLiteral(estimatedAmountDetails.get("amount"), XSDDatatype.XSDfloat));
							unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
						}
						if ((estimatedAmountDetails.get("currency") != null)
								&& (!estimatedAmountDetails.get("currency").toString().isEmpty())) {
							unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(
									Ontology.instancePrefix + "Currency/" + estimatedAmountDetails.get("currency")));
						}
						/**
						 * Contract - UnitPriceSpecification (awardAmount)
						 **/
						if (decisionTypeId.equalsIgnoreCase("Δ.1")) {
							contractResource.addProperty(Ontology.agreedPrice, unitPriceSpecificationResource);
						} else if (decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
							contractResource.addProperty(Ontology.actualPrice, unitPriceSpecificationResource);
						}
					} else {
						System.out.println("No *awardAmount* provided (empty)\n");
					}
				} else {
					System.out.println("No *awardAmount* provided\n");
				}

				if (decisionObject.getExtraFieldValues().get("cpv") != null) { // for
																				// Δ.1
					@SuppressWarnings("unchecked")
					List<String> cpvCodes = (List<String>) decisionObject.getExtraFieldValues().get("cpv");
					for (int i = 0; i < cpvCodes.size(); i++) {
						/** cpvCodeResource **/
						Resource cpvCodeResource = model.createResource(
								Ontology.instancePrefix + "CPV/" + hm.cleanInputData(cpvCodes.get(i)),
								Ontology.cpvResource);

						/** Contract - CPV **/
						if (i == 0) {
							contractResource.addProperty(Ontology.mainObject, cpvCodeResource);
						} else {
							contractResource.addProperty(Ontology.additionalObject, cpvCodeResource);
						}
					}
				}

				if (decisionObject.getExtraFieldValues().get("assignmentType") != null) { // for
																							// Δ.1
					String kindTypeIndividualUri = hm
							.findKindIndividual((String) decisionObject.getExtraFieldValues().get("assignmentType"));
					if (kindTypeIndividualUri != null) {
						contractResource.addProperty(Ontology.kind, model.getResource(kindTypeIndividualUri));
					}
				}

			}

			// for Δ.1, Δ.2.1 and Δ.2.2
			if ((decisionObject.getExtraFieldValues().get("textRelatedADA") != null)
					&& (decisionObject.getExtraFieldValues().get("textRelatedADA") != "")) {
				Object[] instanceData = hm.findRelatedPropertyOfDecisionType(decisionTypeId);
				if (instanceData != null) {
					Resource relatedDecisionResource = model.createResource(
							Ontology.instancePrefix + instanceData[0] + "/"
									+ decisionObject.getExtraFieldValues().get("textRelatedADA"),
							(Resource) instanceData[1]);
					contractResource.addProperty((Property) instanceData[2], relatedDecisionResource);
				}
			}

			if (decisionObject.getExtraFieldValues().get("relatedDecisions") != null) { // lista
																						// {
																						// "relatedDecisionsADA":
																						// "ΒΛ0ΖΝ-889"
																						// }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedDecisionsAdas = (ArrayList<Map<String, ? extends Object>>) decisionObject
						.getExtraFieldValues().get("relatedDecisions");
				for (Map<String, ? extends Object> relatedAda : relatedDecisionsAdas) {
					Object[] instanceData = hm
							.findDecisionTypeInstance(relatedAda.get("relatedDecisionsADA").toString());
					if (instanceData != null) {
						Resource relatedDecisionResource = model
								.createResource(
										Ontology.instancePrefix + instanceData[0] + "/"
												+ relatedAda.get("relatedDecisionsADA").toString(),
										(Resource) instanceData[1]);
						contractResource.addProperty((Property) instanceData[2], relatedDecisionResource);
					}
				}
			}
		}
		// }
		/* store the model */
		// writeModel(model);
	}

	/**
	 * Load the existing model depending whether it is located remote or local.
	 * 
	 * @param boolean
	 *            is the model located remote or local?
	 * @return Model the model
	 */
	public Model remoteOrLocalModel(boolean isRemote) {

		OntologyInitialization ontInit = new OntologyInitialization();

		Model remoteModel = ModelFactory.createDefaultModel();

		if (isRemote) {
			String graphName = "http://diavgeiaII/makis/new/test1";
			String connectionString = "jdbc:virtuoso://178.59.22.123:1111/autoReconnect=true/charset=UTF-8/log_enable=2";
			VirtGraph graph = new VirtGraph(graphName, connectionString, "razis", "m@kisr@zis");
			System.out.println("\nConnected!\n");
			remoteModel = new VirtModel(graph);
		} else {
			try {
				InputStream is = new FileInputStream(Configuration.FILEPATH + Configuration.rdfName);
				remoteModel.read(is, null);
				is.close();
			} catch (Exception e) { // empty file
			}
		}

		ontInit.setPrefixes(remoteModel);

		return remoteModel;

	}

	/**
	 * Store the Model.
	 * 
	 * @param Model
	 *            the model
	 */
	public void writeModel(Model model) {

		try {
			System.out.println("\nSaving Model...");
			FileOutputStream fos = new FileOutputStream(Configuration.FILEPATH + Configuration.rdfName);
			model.write(fos, "RDF/XML-ABBREV", Configuration.FILEPATH + Configuration.rdfName);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}