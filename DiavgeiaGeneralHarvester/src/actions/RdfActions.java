package actions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

/**
 * @author G. Razis
 */
public class RdfActions {
	
	/**
     * Fetch all the details from the provided decision object, 
     * create the RDF triples and add them to the existing model.
     * 
     * @param Decision a decision object
     * @param Model the model to add the triples
     */
	public void createRdfFromDecision(Decision decisionObject, Model model) {
		
		Queries qsOrgs = new Queries();
		HelperMethods hm = new HelperMethods();
		AgentHandler agents = new AgentHandler();
		HandleApiRequests handleRequests = new HandleApiRequests();
		
		Resource committedItemResource = null;
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
		
		/* B Type */
		if (decisionTypeId.equalsIgnoreCase("Β.1.3")) {
			System.out.println("\nΒ.1.3");
			System.out.println("ada: " + decisionObject.getAda());
			/* Common metadata */
			/** Committed Item **/
			if (decisionObject.getCorrectedVersionId() != null) { //has correctedVersionId
				uriDecisionFactor = decisionObject.getAda() + "/Corrected/" + Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
				committedItemResource = model.createResource(Ontology.instancePrefix + "CommittedItem/" + uriDecisionFactor, Ontology.committedItemResource);
				/** Original Decision - Corrected Decision **/
				Resource originalCommittedItemResource = model.createResource(Ontology.instancePrefix + "CommittedItem/" + decisionObject.getAda(), Ontology.committedItemResource);
				originalCommittedItemResource.addProperty(Ontology.hasCorrectedDecision, committedItemResource);
			} else { //normal decision
				uriDecisionFactor = decisionObject.getAda();
				committedItemResource = model.createResource(Ontology.instancePrefix + "CommittedItem/" + uriDecisionFactor, Ontology.committedItemResource);
			}
			committedItemResource.addLiteral(Ontology.ada, decisionObject.getAda());
			committedItemResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			committedItemResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			committedItemResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			committedItemResource.addLiteral(Ontology.issued, hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			committedItemResource.addLiteral(Ontology.submissionTimestamp, hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** CommittedItem - Organization **/
			if (decisionObject.getUnitIds() != null) {
				for (String unitId : decisionObject.getUnitIds()) {
					committedItemResource.addProperty(Ontology.publisher, model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); //find the details of the Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					committedItemResource.addProperty(Ontology.publisher, model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
				}
			}
			
			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			committedItemResource.addProperty(Ontology.decisionStatus, model.getResource(statusIndividualUri[0]));
			
			if (decisionObject.getSubject() != null) {
				committedItemResource.addProperty(Ontology.subject, hm.cleanInputData(decisionObject.getSubject()), "el");
			}
			
			if (decisionObject.getProtocolNumber() != null) {
				committedItemResource.addLiteral(Ontology.protocolNumber, hm.cleanInputData(decisionObject.getProtocolNumber()));
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
				committedItemResource.addProperty(Ontology.hasThematicCategory, model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}
			
			for (String signerId : decisionObject.getSignerIds()) {
				/** CommittedItem - Signer **/
				committedItemResource.addProperty(Ontology.signer, model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}
			
			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription, hm.cleanInputData(attachment.getDescription()));
					attachmentResource.addLiteral(Ontology.attachmentFilename, attachment.getFilename());
					attachmentResource.addLiteral(Ontology.attachmentMimeType, attachment.getMimeType());
					attachmentResource.addLiteral(Ontology.attachmentChecksum, attachment.getChecksum());
					attachmentResource.addLiteral(Ontology.attachmentSize, attachment.getSize());
					/** CommittedItem - Attachment **/
					committedItemResource.addProperty(Ontology.hasAttachment, attachmentResource);
				}
			}
			
			/* specific metadata of B.1.3 */
			committedItemResource.addLiteral(Ontology.documentType, decisionObject.getExtraFieldValues().get("documentType"));
			if (decisionObject.getExtraFieldValues().get("financialYear") != null) {
				committedItemResource.addLiteral(Ontology.financialYear, model.createTypedLiteral(decisionObject.getExtraFieldValues().get("financialYear"), XSDDatatype.XSDgYear));
			}
			if (decisionObject.getExtraFieldValues().get("recalledExpenseDecision") != null) {
				committedItemResource.addLiteral(Ontology.isRecalledExpenseDecision, (Boolean) decisionObject.getExtraFieldValues().get("recalledExpenseDecision"));
			}
			if (decisionObject.getExtraFieldValues().get("budgettype") != "") {
				String[] budgetTypeUri = hm.findBudgetTypeIndividual((String) decisionObject.getExtraFieldValues().get("budgettype"));
				committedItemResource.addProperty(Ontology.hasBudgetCategory, model.getResource(budgetTypeUri[0]));
			}
			
			committedItemResource.addLiteral(Ontology.isPartialWithdrawal, (Boolean) decisionObject.getExtraFieldValues().get("partialead"));
			
			if (decisionObject.getExtraFieldValues().get("entryNumber") != null) {
				committedItemResource.addLiteral(Ontology.entryNumber, decisionObject.getExtraFieldValues().get("entryNumber"));
			}
			
			if ( (decisionObject.getExtraFieldValues().get("relatedPartialADA") != null) && (decisionObject.getExtraFieldValues().get("relatedPartialADA") != "") ) {
				/** relatedCommittedItem **/
				Resource relatedCommittedItem = model.createResource(Ontology.instancePrefix + "CommittedItem/" + decisionObject.getExtraFieldValues().get("relatedPartialADA"), Ontology.committedItemResource);
				committedItemResource.addProperty(Ontology.hasRelatedCommittedItem, relatedCommittedItem);
			}
			if (decisionObject.getExtraFieldValues().get("relatedDecisions") != null) {
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedDecisions = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("relatedDecisions");
				for (Map<String, ? extends Object> relatedDecision : relatedDecisions) {
					/** relatedDecision **/
					Object[] instanceData = hm.findDecisionTypeInstance(relatedDecision.get("relatedDecisionsADA").toString());
					if (instanceData != null) {
						Resource relatedDecisionResource = model.createResource(Ontology.instancePrefix + instanceData[0] + "/" + relatedDecision.get("relatedDecisionsADA").toString(), (Resource) instanceData[1]);
						committedItemResource.addProperty((Property) instanceData[2], relatedDecisionResource);
					}
				}
			}
			
			/** unitPriceSpecificationResource **/
			if (decisionObject.getExtraFieldValues().get("amountWithVAT") != null) {
				unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor, Ontology.unitPriceSpecificationResource);
				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> amountWithVat = (Map<String, ? extends Object>) decisionObject.getExtraFieldValues().get("amountWithVAT");
				System.out.println(amountWithVat);
				
				if (amountWithVat.get("amount") != null) {
					unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(amountWithVat.get("amount"), XSDDatatype.XSDfloat));
					unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
				}
				
				if (amountWithVat.get("currency") != null) {
					unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + amountWithVat.get("currency")));
				}
				
				/** UnitPriceSpecification - CommittedItem **/
				committedItemResource.addProperty(Ontology.price, unitPriceSpecificationResource);
			}
			
			@SuppressWarnings("unchecked")
			List<Map<String, ? extends Object>> amountWithKae = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("amountWithKae");
			System.out.println("\n" + amountWithKae);
			if (amountWithKae != null) {
				if (!amountWithKae.isEmpty()) {
					for (int i = 0; i < amountWithKae.size(); i++) {
						/** expenditureLineResource **/
						expenditureLineResource = model.createResource(Ontology.instancePrefix + "ExpenditureLine/" + uriDecisionFactor + "_" + (i+1), Ontology.expenditureLineResource);
						
						@SuppressWarnings("unchecked")
						Map<String, ? extends Object> sponsorAfmDetails = (Map<String, ? extends Object>) amountWithKae.get(i).get("sponsorAFMName");
						System.out.println(sponsorAfmDetails);
						if (sponsorAfmDetails != null) {
							if (!sponsorAfmDetails.isEmpty()) {
								String afmSponsor = decisionObject.getAda() + "_NotSponsor";
								boolean isNoVatOrg = false;
								if (sponsorAfmDetails.get("afm") != null) {
									afmSponsor = (String) sponsorAfmDetails.get("afm");
								} else if (sponsorAfmDetails.get("noVATOrg") != null) {
									afmSponsor = (String) sponsorAfmDetails.get("noVATOrg");
									isNoVatOrg = true;
								}
								afmSponsor = hm.cleanVatId(afmSponsor);
								/** sellerCommittedItemResource **/
								if (!isNoVatOrg) { //has VAT id case
									Object[] agentsDtls = agents.handleAgent(afmSponsor, decisionObject.getAda());
									sellerCommittedItemResource = (Resource) agentsDtls[0];
								} else { //noVatOrg case
									sellerCommittedItemResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									if (sellerCommittedItemResource == null) {
										qsOrgs.insertAgentUri(Main.graphOrgs, afmSponsor, false);
										sellerCommittedItemResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									}
								}
								
								if (sponsorAfmDetails.get("afmType") != null) {
									qsOrgs.insertVatType(Main.graphOrgs, sellerCommittedItemResource.getURI(), sponsorAfmDetails.get("afmType").toString());
								}
								if (sponsorAfmDetails.get("afmCountry") != null) {
									qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerCommittedItemResource.getURI(), sponsorAfmDetails.get("afmCountry").toString());
								}
								if (sponsorAfmDetails.get("name") != null) {
									qsOrgs.insertName(Main.graphOrgs, sellerCommittedItemResource.getURI(), hm.cleanInputData((String) sponsorAfmDetails.get("name")));
								}
								if (sponsorAfmDetails.get("noVATOrg") != null) {
									qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerCommittedItemResource.getURI(), sponsorAfmDetails.get("noVATOrg").toString());
								}
								/** ExpenditureLine - SellerCommittedItem **/
								expenditureLineResource.addProperty(Ontology.seller, sellerCommittedItemResource);
							} else {
								System.out.println("No *sponsorAfmDetails* provided (empty)\n");
							}
						} else {
							System.out.println("No *sponsorAfmDetails* provided\n");
						}
						
						if ( amountWithKae.get(i).get("amountWithVAT") != null) {
							/** unitPriceSpecificationResource (amountWithVAT) **/
							unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor + 
															"_amount_" + (i+1), Ontology.unitPriceSpecificationResource);
							unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(amountWithKae.get(i).get("amountWithVAT"), XSDDatatype.XSDfloat));
							unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
							unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + "EUR"));
							/** ExpenditureLine - UnitPriceSpecification (amountWithVAT) **/
							expenditureLineResource.addProperty(Ontology.amount, unitPriceSpecificationResource);
							
							if (amountWithKae.get(i).get("kaeBudgetRemainder") != null) {
								/** unitPriceSpecificationResource (kaeBudgetRemainder) **/
								unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor + 
										 						"_budgetRem_" + (i+1), Ontology.unitPriceSpecificationResource);
								unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(amountWithKae.get(i).get("kaeBudgetRemainder"), XSDDatatype.XSDfloat));
								unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
								unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + "EUR"));
								/** ExpenditureLine - UnitPriceSpecification (kaeBudgetRemainder) **/
								expenditureLineResource.addProperty(Ontology.remainingBudgetAmount, unitPriceSpecificationResource);
							}
							if (amountWithKae.get(i).get("kaeCreditRemainder") != null) {
								/** unitPriceSpecificationResource (kaeCreditRemainder) **/
								unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor + 
										 						"_creditRem_" + (i+1), Ontology.unitPriceSpecificationResource);
								unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(amountWithKae.get(i).get("kaeCreditRemainder"), XSDDatatype.XSDfloat));
								unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
								unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + "EUR"));
								/** ExpenditureLine - UnitPriceSpecification (kaeCreditRemainder) **/
								expenditureLineResource.addProperty(Ontology.remainingCreditAmount, unitPriceSpecificationResource);
							}
						}
						
						/** kaeResource **/
						Resource kaeResource = model.createResource(Ontology.instancePrefix + "kaeCodes/" + amountWithKae.get(i).get("kae"), Ontology.kaeResource);
						kaeResource.addLiteral(Ontology.kae, hm.cleanInputData((String) amountWithKae.get(i).get("kae")));
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
		} else if (decisionTypeId.equalsIgnoreCase("Β.2.1") || decisionTypeId.equalsIgnoreCase("Β.2.2")) {
			/** expenseApprovalOrPaymentResource **/
			if (decisionTypeId.equalsIgnoreCase("Β.2.1")) {
				System.out.println("\nΒ.2.1");
				System.out.println("ada: " + decisionObject.getAda());
				/** ExpenseApprovalItem **/
				if (decisionObject.getCorrectedVersionId() != null) { //has correctedVersionId
					uriDecisionFactor = decisionObject.getAda() + "/Corrected/" + Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
					expenseApprovalOrPaymentResource = model.createResource(Ontology.instancePrefix + "ExpenseApprovalItem/" + uriDecisionFactor, Ontology.expenseApprovalItemResource);
					/** Original Decision - Corrected Decision **/
					Resource originalExpenseApprovalOrPaymentResource = model.createResource(Ontology.instancePrefix + "ExpenseApprovalItem/" + decisionObject.getAda(), Ontology.expenseApprovalItemResource);
					originalExpenseApprovalOrPaymentResource.addProperty(Ontology.hasCorrectedDecision, expenseApprovalOrPaymentResource);
				} else { //normal decision
					uriDecisionFactor = decisionObject.getAda();
					expenseApprovalOrPaymentResource = model.createResource(Ontology.instancePrefix + "ExpenseApprovalItem/" + uriDecisionFactor, Ontology.expenseApprovalItemResource);
				}
			} else {
				System.out.println("\nΒ.2.2");
				System.out.println("ada: " + decisionObject.getAda());
				/** Payment **/
				if (decisionObject.getCorrectedVersionId() != null) { //has correctedVersionId
					uriDecisionFactor = decisionObject.getAda() + "/Corrected/" + Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
					expenseApprovalOrPaymentResource = model.createResource(Ontology.instancePrefix + "SpendingItem/" + uriDecisionFactor, Ontology.spendingItemResource);
					/** Original Decision - Corrected Decision **/
					Resource originalExpenseApprovalOrPaymentResource = model.createResource(Ontology.instancePrefix + "SpendingItem/" + decisionObject.getAda(), Ontology.spendingItemResource);
					originalExpenseApprovalOrPaymentResource.addProperty(Ontology.hasCorrectedDecision, expenseApprovalOrPaymentResource);
				} else { //normal decision
					uriDecisionFactor = decisionObject.getAda();
					expenseApprovalOrPaymentResource = model.createResource(Ontology.instancePrefix + "SpendingItem/" + uriDecisionFactor, Ontology.spendingItemResource);
				}
			}
			/* Common Resources */
			expenseApprovalOrPaymentResource.addLiteral(Ontology.ada, decisionObject.getAda());
			expenseApprovalOrPaymentResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			expenseApprovalOrPaymentResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			expenseApprovalOrPaymentResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			expenseApprovalOrPaymentResource.addLiteral(Ontology.issued, hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			expenseApprovalOrPaymentResource.addLiteral(Ontology.submissionTimestamp, hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** ExpenseApprovalItem or Payment - Organization **/
			if (decisionObject.getUnitIds() != null) {
				for (String unitId : decisionObject.getUnitIds()) {
					expenseApprovalOrPaymentResource.addProperty(Ontology.publisher, model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); //find the details of the Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					expenseApprovalOrPaymentResource.addProperty(Ontology.publisher, model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
				}
			}

			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			expenseApprovalOrPaymentResource.addProperty(Ontology.decisionStatus, model.getResource(statusIndividualUri[0]));

			if (decisionObject.getSubject() != null) {
				expenseApprovalOrPaymentResource.addProperty(Ontology.subject, hm.cleanInputData(decisionObject.getSubject()), "el");
			}

			if (decisionObject.getProtocolNumber() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.protocolNumber, hm.cleanInputData(decisionObject.getProtocolNumber()));
			}

			if (decisionObject.getDocumentUrl() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.documentUrl, decisionObject.getDocumentUrl());
			}

			if (decisionObject.getDocumentChecksum() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.documentChecksum, decisionObject.getDocumentChecksum());
			}

			if (decisionObject.getCorrectedVersionId() != null) {
				expenseApprovalOrPaymentResource.addLiteral(Ontology.correctedVersionId, decisionObject.getCorrectedVersionId());
			}

			for (String themCatId : decisionObject.getThematicCategoryIds()) {
				/** ExpenseApprovalItem or Payment - ThematicCategory **/
				expenseApprovalOrPaymentResource.addProperty(Ontology.hasThematicCategory, model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}

			for (String signerId : decisionObject.getSignerIds()) {
				/** ExpenseApprovalItem or Payment - Signer **/
				expenseApprovalOrPaymentResource.addProperty(Ontology.signer, model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}

			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription, hm.cleanInputData(attachment.getDescription()));
					attachmentResource.addLiteral(Ontology.attachmentFilename, attachment.getFilename());
					attachmentResource.addLiteral(Ontology.attachmentMimeType, attachment.getMimeType());
					attachmentResource.addLiteral(Ontology.attachmentChecksum, attachment.getChecksum());
					attachmentResource.addLiteral(Ontology.attachmentSize, attachment.getSize());
					/** ExpenseApprovalItem or Payment - Attachment **/
					expenseApprovalOrPaymentResource.addProperty(Ontology.hasAttachment, attachmentResource);
				}
			}
			
			/* specific metadata of Β.2.1 or Β.2.2 */
			expenseApprovalOrPaymentResource.addLiteral(Ontology.documentType, decisionObject.getExtraFieldValues().get("documentType"));
			
			@SuppressWarnings("unchecked") //for Β.2.1 and Β.2.2
			Map<String, ? extends Object> orgDetails = (Map<String, ? extends Object>) decisionObject.getExtraFieldValues().get("org");
			System.out.println(orgDetails);
			if (orgDetails != null) {
				if (!orgDetails.isEmpty()) {
					String afmOrg = decisionObject.getAda() + "_NotSponsor";
					boolean isNoVatOrg = false;
					if (orgDetails.get("afm") != null) {
						if (orgDetails.get("afm").equals("")){
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
					if (!isNoVatOrg) { //has VAT id case
						Object[] agentsDtls = agents.handleAgent(afmOrg, decisionObject.getAda());
						buyerExpenseApprResource = (Resource) agentsDtls[0];
					} else { //noVatOrg case
						buyerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmOrg);
						if (buyerExpenseApprResource == null) {
							qsOrgs.insertAgentUri(Main.graphOrgs, afmOrg, false);
							buyerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmOrg);
						}
					}	
					
			  		if (orgDetails.get("afmType") != null) {
			  			qsOrgs.insertVatType(Main.graphOrgs, buyerExpenseApprResource.getURI(), orgDetails.get("afmType").toString());
			  		}
			  		if (orgDetails.get("afmCountry") != null) {
			  			qsOrgs.insertRegisteredAt(Main.graphOrgs, buyerExpenseApprResource.getURI(), orgDetails.get("afmCountry").toString());
			  		}
			  		if (orgDetails.get("name") != null) {
			  			qsOrgs.insertName(Main.graphOrgs, buyerExpenseApprResource.getURI(), hm.cleanInputData((String) orgDetails.get("name")));
			  		}
			  		if (orgDetails.get("noVATOrg") != null) {
			  			qsOrgs.insertNoVatOrgId(Main.graphOrgs, buyerExpenseApprResource.getURI(), orgDetails.get("noVATOrg").toString());
			  		}
			  		/** ExpenseApprovalItem or Payment - BuyerExpenseApprovalItem **/
			  		expenseApprovalOrPaymentResource.addProperty(Ontology.buyer, buyerExpenseApprResource);
				} else {
					System.out.println("No *orgDetails* provided (empty)\n");
				}
			} else {
				System.out.println("No *orgDetails* provided\n");
			}
			
			/** payeeSpendingResource **/ //for Β.2.1 and Β.2.2
			@SuppressWarnings("unchecked")
			List<Map<String, ? extends Object>> sponsorDetails = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("sponsor");
			System.out.println("\n" + sponsorDetails);
			if (sponsorDetails != null) {
				if (!sponsorDetails.isEmpty()) {
					for (int i = 0; i < sponsorDetails.size(); i++) {
						/** expenditureLineResource **/
						expenditureLineResource = model.createResource(Ontology.instancePrefix + "ExpenditureLine/" + uriDecisionFactor + "_" + (i+1), Ontology.expenditureLineResource);
						
						@SuppressWarnings("unchecked")
						Map<String, ? extends Object> sponsorAfmDetails = (Map<String, ? extends Object>) sponsorDetails.get(i).get("sponsorAFMName");
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
								if (!isNoVatOrg) { //has VAT id case
									Object[] agentsDtls = agents.handleAgent(afmSponsor, decisionObject.getAda());
									sellerExpenseApprResource = (Resource) agentsDtls[0];
								} else { //noVatOrg case
									sellerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									if (sellerExpenseApprResource == null) {
										qsOrgs.insertAgentUri(Main.graphOrgs, afmSponsor, false);
										sellerExpenseApprResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmSponsor);
									}
								}
								
								if (sponsorAfmDetails.get("afmType") != null) {
									qsOrgs.insertVatType(Main.graphOrgs, sellerExpenseApprResource.getURI(), sponsorAfmDetails.get("afmType").toString());
								}
								if (sponsorAfmDetails.get("afmCountry") != null) {
									qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerExpenseApprResource.getURI(), sponsorAfmDetails.get("afmCountry").toString());
								}
								if (sponsorAfmDetails.get("name") != null) {
									qsOrgs.insertName(Main.graphOrgs, sellerExpenseApprResource.getURI(), hm.cleanInputData((String) sponsorAfmDetails.get("name")));
								}
								if (sponsorAfmDetails.get("noVATOrg") != null) {
									qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerExpenseApprResource.getURI(), sponsorAfmDetails.get("noVATOrg").toString());
								}
								/** ExpenditureLine - SellerExpenseApprovalItem **/
								expenditureLineResource.addProperty(Ontology.seller, sellerExpenseApprResource);
							} else {
								System.out.println("No *sponsorAfmDetails* provided (empty)\n");
							}
						} else {
							System.out.println("No *sponsorAfmDetails* provided\n");
						}
						
						if ( (sponsorDetails.get(i).get("cpv") != null) && (sponsorDetails.get(i).get("cpv") != "") ) {
							/** cpvCodeResource **/
							Resource cpvCodeResource = model.createResource(Ontology.instancePrefix + "CPV/" + hm.cleanInputData((String) sponsorDetails.get(i).get("cpv")), Ontology.cpvResource);
							/** ExpenditureLine - CPV **/
							expenditureLineResource.addProperty(Ontology.hasCpv, cpvCodeResource);
						}
						
						if ( (sponsorDetails.get(i).get("kae") != null) && (sponsorDetails.get(i).get("kae") != "") ) {
							/** kaeResource **/
							Resource kaeResource = model.createResource(Ontology.instancePrefix + "kaeCodes/" + sponsorDetails.get(i).get("kae"), Ontology.kaeResource);
							kaeResource.addLiteral(Ontology.kae, hm.cleanInputData((String) sponsorDetails.get(i).get("kae")));
							/** ExpenditureLine - KAE **/
							expenditureLineResource.addProperty(Ontology.hasKae, kaeResource);
						}
						
						@SuppressWarnings("unchecked")
						Map<String, ? extends Object> expenseAmountDetails = (Map<String, ? extends Object>) sponsorDetails.get(i).get("expenseAmount");
						System.out.println(expenseAmountDetails);
						if (expenseAmountDetails != null) {
							if (!expenseAmountDetails.isEmpty()) {
								/** unitPriceSpecificationResource (expenseAmount) **/
								unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor + "_" + (i+1), Ontology.unitPriceSpecificationResource);
								if (expenseAmountDetails.get("amount") != null) {
									unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(expenseAmountDetails.get("amount"), XSDDatatype.XSDfloat));
									unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
								}
								if (expenseAmountDetails.get("currency") != null) {
									unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + expenseAmountDetails.get("currency")));
								}
								/** ExpenditureLine - UnitPriceSpecification (expenseAmount) **/
								expenditureLineResource.addProperty(Ontology.amount, unitPriceSpecificationResource);
							} else {
								System.out.println("No *expenseAmountDetails* provided (empty)\n");
							}
						} else {
							System.out.println("No *expenseAmountDetails* provided\n");
						}
						
						/** ExpenseApprovalItem or Payment - ExpenditureLine **/
						expenseApprovalOrPaymentResource.addProperty(Ontology.hasExpenditureLine, expenditureLineResource);
					} 
				}else {
					System.out.println("No *sponsorDetails* provided (empty)\n");
				}
			} else {
				System.out.println("No *sponsorDetails* provided\n");
			}
			
			if (decisionObject.getExtraFieldValues().get("relatedDecisions") != null) {//for Β.2.1 and Β.2.2 //lista { "relatedDecisionsADA": "ΒΛ9ΛΝ-32Ν" }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedDecisions = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("relatedDecisions");
				for (Map<String, ? extends Object> relatedDecision : relatedDecisions) {
					Object[] instanceData = hm.findDecisionTypeInstance(relatedDecision.get("relatedDecisionsADA").toString());
					if (instanceData != null) {
						Resource relatedDecisionResource = model.createResource(Ontology.instancePrefix + instanceData[0] + "/" + relatedDecision.get("relatedDecisionsADA").toString(), (Resource) instanceData[1]);
						expenseApprovalOrPaymentResource.addProperty((Property) instanceData[2], relatedDecisionResource);
					}
				}
			}
			
			if (decisionObject.getExtraFieldValues().get("relatedAnalipsiYpoxreosis") != null) {//for Β.2.1 //lista { "textRelatedADA": "ΒΙ65Ν-ΜΚΞ" }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedAnalipsiYpoxreosisAdas = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("relatedAnalipsiYpoxreosis");
				for (Map<String, ? extends Object> relatedAda : relatedAnalipsiYpoxreosisAdas) {
					if ( (relatedAda.get("textRelatedADA") != null) && (relatedAda.get("textRelatedADA") != "") ) {
						Resource relatedAnalipsiYpoxreosisAdaResource = model.createResource(Ontology.instancePrefix + "CommittedItem/" + relatedAda.get("textRelatedADA"), Ontology.committedItemResource);
						expenseApprovalOrPaymentResource.addProperty(Ontology.hasRelatedCommittedItem, relatedAnalipsiYpoxreosisAdaResource);
					}
				}
			}
			
			if (decisionObject.getExtraFieldValues().get("relatedEkgrisiDapanis") != null) {//for Β.2.2 //lista { "textRelatedADA": "ΒΛ0ΖΝ-889" }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedEgkrisiDapanisAdas = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("relatedEkgrisiDapanis");
				for (Map<String, ? extends Object> relatedAda : relatedEgkrisiDapanisAdas) {
					if ( (relatedAda.get("textRelatedADA") != null) && (relatedAda.get("textRelatedADA") != "") ) {
						Object[] instanceData = hm.findDecisionTypeInstance(relatedAda.get("textRelatedADA").toString());
						if (instanceData != null) {
							Resource relatedEgkrisiDapanisAdaResource = model.createResource(Ontology.instancePrefix + instanceData[0] + "/" + relatedAda.get("textRelatedADA").toString(), (Resource) instanceData[1]);
							expenseApprovalOrPaymentResource.addProperty((Property) instanceData[2], relatedEgkrisiDapanisAdaResource);
						}
					}
				}
			}
		/* Δ Type */
		} else if (decisionTypeId.equalsIgnoreCase("Δ.1") || decisionTypeId.equalsIgnoreCase("Δ.2.1") || decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
			System.out.println("\n" + decisionTypeId);
			System.out.println("ada: " + decisionObject.getAda());
			/* Common metadata */
			/** contractResource **/
			if (decisionObject.getCorrectedVersionId() != null) { //has correctedVersionId
				uriDecisionFactor = decisionObject.getAda() + "/Corrected/" + Formatters.DATE_FORMAT.format(decisionObject.getSubmissionTimestamp());
				contractResource = model.createResource(Ontology.instancePrefix + "Contract/" + uriDecisionFactor, Ontology.contractResource);
				/** Original Decision - Corrected Decision **/
				Resource originalContractResource = model.createResource(Ontology.instancePrefix + "Contract/" + decisionObject.getAda(), Ontology.contractResource);
				originalContractResource.addProperty(Ontology.hasCorrectedDecision, contractResource);
			} else { //normal decision
				uriDecisionFactor = decisionObject.getAda();
				contractResource = model.createResource(Ontology.instancePrefix + "Contract/" + uriDecisionFactor, Ontology.contractResource);
			}
			contractResource.addLiteral(Ontology.ada, decisionObject.getAda());
			contractResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
			contractResource.addLiteral(Ontology.versionId, decisionObject.getVersionId());
			contractResource.addLiteral(Ontology.privateData, decisionObject.isPrivateData());
			contractResource.addLiteral(Ontology.issued, hm.dateToCalendarConverter(decisionObject.getIssueDate()));
			contractResource.addLiteral(Ontology.submissionTimestamp, hm.dateToCalendarConverter(decisionObject.getSubmissionTimestamp()));
			/** Contract - Organization **/
			if (decisionObject.getUnitIds() != null) {
				for (String unitId : decisionObject.getUnitIds()) {
					contractResource.addProperty(Ontology.publisher, model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
					contractResource.addProperty(Ontology.buyer, model.getResource(Ontology.instancePrefix + "OrganizationalUnit/" + unitId));
				}
			} else {
				Organization orgObject = handleRequests.searchOrganization(decisionObject.getOrganizationId()); //find the details of the Organization
				if (orgObject != null) {
					String orgVatId = orgObject.getVatNumber();
					contractResource.addProperty(Ontology.publisher, model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
					contractResource.addProperty(Ontology.buyer, model.getResource(Ontology.instancePrefix + "Organization/" + orgVatId));
				}
			}

			String[] statusIndividualUri = hm.findDecisionStatusIndividual(decisionObject.getStatus());
			contractResource.addProperty(Ontology.decisionStatus, model.getResource(statusIndividualUri[0]));

			if (decisionObject.getSubject() != null) {
				contractResource.addProperty(Ontology.subject, hm.cleanInputData(decisionObject.getSubject()), "el");
			}

			if (decisionObject.getProtocolNumber() != null) {
				contractResource.addLiteral(Ontology.protocolNumber, hm.cleanInputData(decisionObject.getProtocolNumber()));
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
				contractResource.addProperty(Ontology.hasThematicCategory, model.getResource(Ontology.instancePrefix + "ThematicCategory/" + themCatId));
			}

			for (String signerId : decisionObject.getSignerIds()) {
				/** Contract - Signer **/
				contractResource.addProperty(Ontology.signer, model.getResource(Ontology.instancePrefix + "Signer/" + signerId));
			}

			/** attachmentResource(s) **/
			if (decisionObject.getAttachments() != null) {
				for (Attachment attachment : decisionObject.getAttachments()) {
					Resource attachmentResource = model.createResource(Ontology.instancePrefix + "Attachment/" + attachment.getId(), Ontology.attachmentResource);
					attachmentResource.addLiteral(Ontology.attachmentId, attachment.getId());
					attachmentResource.addLiteral(Ontology.attachmentDescription, hm.cleanInputData(attachment.getDescription()));
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
				contractResource.addLiteral(Ontology.documentType, decisionObject.getExtraFieldValues().get("documentType"));
				
				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> estimatedAmountDetails = (Map<String, ? extends Object>) decisionObject.getExtraFieldValues().get("estimatedAmount");
				System.out.println(estimatedAmountDetails);
				if (estimatedAmountDetails != null) {
					if (!estimatedAmountDetails.isEmpty()) {
						/** unitPriceSpecificationResource **/
						unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor, Ontology.unitPriceSpecificationResource);
						if (estimatedAmountDetails.get("amount") != null) {
							unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(estimatedAmountDetails.get("amount"), XSDDatatype.XSDfloat));
							unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, false);
						}
						if (estimatedAmountDetails.get("currency") != null) {
							unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + estimatedAmountDetails.get("currency")));
						}
						/** Contract - UnitPriceSpecification (estimatedAmount) **/
						contractResource.addProperty(Ontology.documentsPrice, unitPriceSpecificationResource);
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
						Resource cpvCodeResource = model.createResource(Ontology.instancePrefix + "CPV/" + hm.cleanInputData(cpvCodes.get(i)), Ontology.cpvResource);
						/** Contract - CPV **/
						if (i == 0) {
							contractResource.addProperty(Ontology.mainObject, cpvCodeResource);
						} else {
							contractResource.addProperty(Ontology.additionalObject, cpvCodeResource);
						}
					}
				}
				
				if (decisionObject.getExtraFieldValues().get("contestProgressType") != null) {
					String procedureTypeIndividualUri = hm.findProcedureTypeIndividual((String) decisionObject.getExtraFieldValues().get("contestProgressType"));
					contractResource.addProperty(Ontology.procedureType, model.getResource(procedureTypeIndividualUri));
				}
				
				if (decisionObject.getExtraFieldValues().get("manifestContractType") != null) {
					String kindTypeIndividualUri = hm.findKindIndividual((String) decisionObject.getExtraFieldValues().get("manifestContractType"));
					contractResource.addProperty(Ontology.kind, model.getResource(kindTypeIndividualUri));
				}
				
				if ( (decisionObject.getExtraFieldValues().get("orgBudgetCode") != null) && (decisionObject.getExtraFieldValues().get("orgBudgetCode") != "") ) {
					String[] budgetTypeIndividualUri = hm.findBudgetTypeIndividual((String) decisionObject.getExtraFieldValues().get("orgBudgetCode"));
					contractResource.addProperty(Ontology.hasBudgetCategory, model.getResource(budgetTypeIndividualUri[0]));
				}
				
				if (decisionObject.getExtraFieldValues().get("manifestSelectionCriterion") != null) {
					/** awardCriteriaCombinationResource **/
					awardCriteriaCombinationResource = model.createResource(Ontology.instancePrefix + "AwardCriteriaCombination/" + uriDecisionFactor, Ontology.awardCriteriaCombinationResource);
					/** criterionWeightingResource **/
					criterionWeightingResource = model.createResource(Ontology.instancePrefix + "CriterionWeighting/" + uriDecisionFactor, Ontology.criterionWeightingResource);
					String[] criterionIndividual = hm.findCriterionIndividual((String) decisionObject.getExtraFieldValues().get("manifestSelectionCriterion"));
					criterionWeightingResource.addProperty(Ontology.weightedCriterion, model.getResource(criterionIndividual[0]));
					criterionWeightingResource.addLiteral(Ontology.criterionWeight, model.createTypedLiteral(criterionIndividual[1], Ontology.pcdtPrefix + "percentage"));
					
					/** AwardCriteriaCombination - CriterionWeighting **/
					awardCriteriaCombinationResource.addProperty(Ontology.awardCriterion, criterionWeightingResource);
					/** Contract - AwardCriteriaCombination **/
					contractResource.addProperty(Ontology.awardCriteriaCombination, awardCriteriaCombinationResource);
				}
			} else if (decisionTypeId.equalsIgnoreCase("Δ.1") || decisionTypeId.equalsIgnoreCase("Δ.2.2")) {
				/* specific metadata of Δ.1 or Δ.2.2 */
				contractResource.addLiteral(Ontology.decisionTypeId, decisionTypeId);
				contractResource.addLiteral(Ontology.documentType, decisionObject.getExtraFieldValues().get("documentType"));
				
				//for Δ.1 and Δ.2.2
				try { //Sometimes the data are in a List of Maps and sometimes in a Map 
					@SuppressWarnings("unchecked")
					List<Map<String, ? extends Object>> personDetailsList = (List<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("person");
					if (personDetailsList != null) {
						for (Map<String, ? extends Object> personDetails : personDetailsList) {
							System.out.println(personDetails);
							if (personDetails != null) {
								if (!personDetails.isEmpty()) {
									String afmPerson = decisionObject.getAda() + "_NotPerson";
									boolean isNoVatOrg = false;
									if (personDetails.get("afm") != null) {
										afmPerson = (String) personDetails.get("afm");
									} else if (personDetails.get("noVATOrg") != null) {
										afmPerson = (String) personDetails.get("noVATOrg");
										isNoVatOrg = true;
									}
									afmPerson = hm.cleanVatId(afmPerson);
									/** sellerTendersResource **/
									if (!isNoVatOrg) { //has VAT id case
										Object[] agentsDtls = agents.handleAgent(afmPerson, decisionObject.getAda());
										sellerTendersResource = (Resource) agentsDtls[0];
									} else { //noVatOrg case
										sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
										if (sellerTendersResource == null) {
											qsOrgs.insertAgentUri(Main.graphOrgs, afmPerson, false);
											sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
										}
									}
									
									if (personDetails.get("afmType") != null) {
										qsOrgs.insertVatType(Main.graphOrgs, sellerTendersResource.getURI(), personDetails.get("afmType").toString());
									}
									if (personDetails.get("afmCountry") != null) {
										qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerTendersResource.getURI(), personDetails.get("afmCountry").toString());
									}
									if (personDetails.get("name") != null) {
										qsOrgs.insertName(Main.graphOrgs, sellerTendersResource.getURI(), hm.cleanInputData((String) personDetails.get("name")));
									}
									if (personDetails.get("noVATOrg") != null) {
										qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerTendersResource.getURI(), personDetails.get("noVATOrg").toString());
									}
									/** Contract - SellerTenders **/
									contractResource.addProperty(Ontology.seller, sellerTendersResource);
								} else {
									System.out.println("No *personDetails* provided (empty)\n");
								}
							} else {
								System.out.println("No *personDetails* provided\n");
							}
						} 
					}else {
							System.out.println("No *personDetails* as List provided\n");
					}
				} catch(Exception e) {
					@SuppressWarnings("unchecked")
					Map<String, ? extends Object> personDetails = (Map<String, ? extends Object>) decisionObject.getExtraFieldValues().get("person");
					if (personDetails != null) {
						System.out.println(personDetails);
						if (!personDetails.isEmpty()) {
							String afmPerson = decisionObject.getAda() + "_NotPerson";
							boolean isNoVatOrg = false;
							if (personDetails.get("afm") != null) {
								afmPerson = (String) personDetails.get("afm");
							} else if (personDetails.get("noVATOrg") != null) {
								afmPerson = (String) personDetails.get("noVATOrg");
								isNoVatOrg = true;
							}
							afmPerson = hm.cleanVatId(afmPerson);
							/** sellerTendersResource **/
							if (!isNoVatOrg) { //has VAT id case
								Object[] agentsDtls = agents.handleAgent(afmPerson, decisionObject.getAda());
								sellerTendersResource = (Resource) agentsDtls[0];
							} else { //noVatOrg case
								sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
								if (sellerTendersResource == null) {
									qsOrgs.insertAgentUri(Main.graphOrgs, afmPerson, false);
									sellerTendersResource = qsOrgs.getAgentUriNoVat(Main.graphOrgs, afmPerson);
								}
							}
							
							if (personDetails.get("afmType") != null) {
								qsOrgs.insertVatType(Main.graphOrgs, sellerTendersResource.getURI(), personDetails.get("afmType").toString());
							}
							if (personDetails.get("afmCountry") != null) {
								qsOrgs.insertRegisteredAt(Main.graphOrgs, sellerTendersResource.getURI(), personDetails.get("afmCountry").toString());
							}
							if (personDetails.get("name") != null) {
								qsOrgs.insertName(Main.graphOrgs, sellerTendersResource.getURI(), hm.cleanInputData((String) personDetails.get("name")));
							}
							if (personDetails.get("noVATOrg") != null) {
								qsOrgs.insertNoVatOrgId(Main.graphOrgs, sellerTendersResource.getURI(), personDetails.get("noVATOrg").toString());
							}
							/** Contract - SellerTenders **/
							contractResource.addProperty(Ontology.seller, sellerTendersResource);
						} else {
							System.out.println("No *personDetails* provided (empty)\n");
						}
					} else {
							System.out.println("No *personDetails* as List provided\n");
					}
				}
				
				//for Δ.1 and Δ.2.2
				@SuppressWarnings("unchecked")
				Map<String, ? extends Object> estimatedAmountDetails = (Map<String, ? extends Object>) decisionObject.getExtraFieldValues().get("awardAmount");
				System.out.println(estimatedAmountDetails);
				if (estimatedAmountDetails != null) {
					if (!estimatedAmountDetails.isEmpty()) {
						/** unitPriceSpecificationResource **/
						unitPriceSpecificationResource = model.createResource(Ontology.instancePrefix + "UnitPriceSpecification/" + uriDecisionFactor, Ontology.unitPriceSpecificationResource);
						if (estimatedAmountDetails.get("amount") != null) {
							unitPriceSpecificationResource.addLiteral(Ontology.hasCurrencyValue, model.createTypedLiteral(estimatedAmountDetails.get("amount"), XSDDatatype.XSDfloat));
							unitPriceSpecificationResource.addLiteral(Ontology.valueAddedTaxIncluded, true);
						}
						if (estimatedAmountDetails.get("currency") != null) {
							unitPriceSpecificationResource.addProperty(Ontology.hasCurrency, model.getResource(Ontology.instancePrefix + "Currency/" + estimatedAmountDetails.get("currency")));
						}
						/** Contract - UnitPriceSpecification (awardAmount) **/
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
				
				if (decisionObject.getExtraFieldValues().get("cpv") != null) { //for Δ.1
					@SuppressWarnings("unchecked")
					List<String> cpvCodes = (List<String>) decisionObject.getExtraFieldValues().get("cpv");
					for (int i = 0; i < cpvCodes.size(); i++) {
						/** cpvCodeResource **/
						Resource cpvCodeResource = model.createResource(Ontology.instancePrefix + "CPV/" + hm.cleanInputData(cpvCodes.get(i)), Ontology.cpvResource);
						/** Contract - CPV **/
						if (i == 0) {
							contractResource.addProperty(Ontology.mainObject, cpvCodeResource);
						} else {
							contractResource.addProperty(Ontology.additionalObject, cpvCodeResource);
						}
					}
				}
				
				if (decisionObject.getExtraFieldValues().get("assignmentType") != null) { //for Δ.1
					String kindTypeIndividualUri = hm.findKindIndividual((String) decisionObject.getExtraFieldValues().get("assignmentType"));
					contractResource.addProperty(Ontology.kind, model.getResource(kindTypeIndividualUri));
				}
				
			}
			
			//for Δ.1, Δ.2.1 and Δ.2.2
			if ( (decisionObject.getExtraFieldValues().get("textRelatedADA") != null) && (decisionObject.getExtraFieldValues().get("textRelatedADA") != "") ) {
				Object[] instanceData = hm.findRelatedPropertyOfDecisionType(decisionTypeId);
				if (instanceData != null) {
					Resource relatedDecisionResource = model.createResource(Ontology.instancePrefix + instanceData[0] + "/" + decisionObject.getExtraFieldValues().get("textRelatedADA"), (Resource) instanceData[1]);
					contractResource.addProperty((Property) instanceData[2], relatedDecisionResource);
				}
			}
			
			if (decisionObject.getExtraFieldValues().get("relatedDecisions") != null) { //lista { "relatedDecisionsADA": "ΒΛ0ΖΝ-889" }
				@SuppressWarnings("unchecked")
				List<Map<String, ? extends Object>> relatedDecisionsAdas = (ArrayList<Map<String, ? extends Object>>) decisionObject.getExtraFieldValues().get("relatedDecisions");
				for (Map<String, ? extends Object> relatedAda : relatedDecisionsAdas) {
					Object[] instanceData = hm.findDecisionTypeInstance(relatedAda.get("relatedDecisionsADA").toString());
					if (instanceData != null) {
						Resource relatedDecisionResource = model.createResource(Ontology.instancePrefix + instanceData[0] + "/" + relatedAda.get("relatedDecisionsADA").toString(), (Resource) instanceData[1]);
						contractResource.addProperty((Property) instanceData[2], relatedDecisionResource);
					}
				}
			}
			
		}
		
		/* store the model */
		//writeModel(model);
		
	}
	
	/**
     * Load the existing model depending whether it is located remote or local.
     * 
     * @param boolean is the model located remote or local?
     * @return Model the model
     */
	public Model remoteOrLocalModel(boolean isRemote) {
		
		OntologyInitialization ontInit = new OntologyInitialization();
		
		Model remoteModel = ModelFactory.createDefaultModel();
		
		if (isRemote) {
			String graphName = "http://diavgeiaII/makis/new/test1";
			String connectionString = "jdbc:virtuoso://178.59.22.123:1111/autoReconnect=true/charset=UTF-8/log_enable=2";
			VirtGraph graph = new VirtGraph (graphName, connectionString, "razis", "m@kisr@zis");
			System.out.println("\nConnected!\n");
	    	remoteModel = new VirtModel(graph);
		} else {
			try {
				InputStream is = new FileInputStream(Configuration.FILEPATH + Configuration.rdfName);
				remoteModel.read(is,null);
				is.close();
			} catch (Exception e) { //empty file
			}
		}
		
		ontInit.setPrefixes(remoteModel);
		
		return remoteModel;
		
	}
	
	/**
     * Store the Model.
     * 
     * @param Model the model
     */
	public void writeModel(Model model) {
		
		try {
			System.out.println("\nSaving Model...");
			FileOutputStream fos = new FileOutputStream(Configuration.FILEPATH + Configuration.rdfName);
			model.write(fos, "RDF/XML-ABBREV", Configuration.FILEPATH + Configuration.rdfName);
			fos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}