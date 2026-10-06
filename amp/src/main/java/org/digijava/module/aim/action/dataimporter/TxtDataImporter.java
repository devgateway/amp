package org.digijava.module.aim.action.dataimporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProject;
import org.digijava.module.aim.action.dataimporter.model.Funding;
import org.digijava.module.aim.action.dataimporter.util.ImporterConstants;
import org.digijava.module.aim.action.dataimporter.model.ImportDataModel;
import org.digijava.module.aim.action.dataimporter.util.ImporterUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.*;

public class TxtDataImporter {
    private static final int BATCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(TxtDataImporter.class);


    public static int processTxtFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config, boolean isInternal, boolean skipExisting, boolean createMissingOrgs, boolean createMissingSectors, Long orgGroupId, boolean createMissingOrgGroups, boolean skipRecordsWithoutTransactions, boolean validateActivities, boolean addDisbursementForCommitment, Long defaultActivityStatusId, Long defaultLocationId, String defaultProgramClassification, boolean createMissingPrograms, boolean replaceExistingTransactions)
    {
        logger.info("Processing txt file: " + file.getName());
        CSVParser parser = new CSVParserBuilder().withSeparator(request.getParameter("dataSeparator").charAt(0)).build();

        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAwareBuilder(new FileReader(file)).withCSVParser(parser).build()) {
            List<Map<String, String>> batch = new ArrayList<>();
            Map<String, String> values;
            int batchNumber =1;
            while ((values = reader.readMap()) != null) {
                if (isInternal) {
                    values.put("Donor Agency", FeaturesUtil.getGlobalSettingValue("Internal Ecowas Donor"));
                }
                batch.add(values);

                if (batch.size() == BATCH_SIZE) {
                    logger.info("Batch number here: {}",batchNumber);

                    // Process the batch
                    processBatch(batch, request, config, importedFilesRecord, skipExisting, createMissingOrgs, createMissingSectors, orgGroupId, createMissingOrgGroups, skipRecordsWithoutTransactions, validateActivities, addDisbursementForCommitment, defaultActivityStatusId, defaultLocationId, defaultProgramClassification, createMissingPrograms, replaceExistingTransactions);
                    // Clear the batch for the next set of rows
                    batch.clear();
                    batchNumber+=1;
                }
            }

            // Process any remaining rows in the batch
            if (!batch.isEmpty()) {
                logger.info("Processing last batch of size {}", batch.size());
                processBatch(batch, request, config, importedFilesRecord, skipExisting, createMissingOrgs, createMissingSectors, orgGroupId, createMissingOrgGroups, skipRecordsWithoutTransactions, validateActivities, addDisbursementForCommitment, defaultActivityStatusId, defaultLocationId, defaultProgramClassification, createMissingPrograms, replaceExistingTransactions);
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error processing txt file "+e.getMessage(),e);
            return 0;
        }
        return 1;
    }


    private static void processBatch(List<Map<String, String>> batch, HttpServletRequest request, Map<String, String> config, ImportedFilesRecord importedFilesRecord, boolean skipExisting, boolean createMissingOrgs, boolean createMissingSectors, Long orgGroupId, boolean createMissingOrgGroups, boolean skipRecordsWithoutTransactions, boolean validateActivities, boolean addDisbursementForCommitment, Long defaultActivityStatusId, Long defaultLocationId, String defaultProgramClassification, boolean createMissingPrograms, boolean replaceExistingTransactions) throws JsonProcessingException {
        logger.info("Processing txt batch");
        SessionUtil.extendSessionIfNeeded(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        for (Map<String, String> row : batch) {
            final Map<String, String> rowRef = row;
            ImportedProject importedProject= new ImportedProject();
            importedProject.setImportedFilesRecord(importedFilesRecord);
            List<Funding> fundings= new ArrayList<>();

            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setTeam(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam().getAmpTeamId());
            importDataModel.setIs_draft(!validateActivities);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            importDataModel.setCreation_date(now.format(formatter));
            String componentName= rowRef.get(getKey(config, ImporterConstants.COMPONENT_NAME));
            String componentCode= rowRef.get(getKey(config, ImporterConstants.COMPONENT_CODE));
            String projectCode= rowRef.get(getKey(config, ImporterConstants.PROJECT_CODE));
            String projectTitle= rowRef.get(getKey(config, ImporterConstants.PROJECT_TITLE));
            String projectDesc= rowRef.get(getKey(config, ImporterConstants.PROJECT_DESCRIPTION));
            String objective= rowRef.get(getKey(config, ImporterConstants.OBJECTIVE));
            String primarySubSector= rowRef.get(getKey(config, ImporterConstants.PRIMARY_SUBSECTOR));
            String secondarySubSector= rowRef.get(getKey(config, ImporterConstants.SECONDARY_SUBSECTOR));
            String projectStatusStr = rowRef.get(getKey(config, ImporterConstants.PROJECT_STATUS));
            String importedOrgGroupName;
            if (config.containsValue(ImporterConstants.ORG_GROUP)) {
                String configuredOrgGroupName = rowRef.get(getKey(config, ImporterConstants.ORG_GROUP));
                if (configuredOrgGroupName != null && !configuredOrgGroupName.trim().isEmpty()) {
                    importedOrgGroupName = configuredOrgGroupName.trim();
                } else {
                    importedOrgGroupName = null;
                }
            } else {
                importedOrgGroupName = null;
            }
            String donorOrgGroupNames = rowRef.get(getKey(config, ImporterConstants.DONOR_ORGANIZATION_GROUP));
            String responsibleOrgGroupNames = rowRef.get(getKey(config, ImporterConstants.RESPONSIBLE_ORGANIZATION_GROUP));
            String beneficiaryOrgGroupNames = rowRef.get(getKey(config, ImporterConstants.BENEFICIARY_AGENCY_GROUP));
            String executingOrgGroupNames = rowRef.get(getKey(config, ImporterConstants.EXECUTING_AGENCY_GROUP));
            String implementingOrgGroupNames = rowRef.get(getKey(config, ImporterConstants.IMPLEMENTING_AGENCY_GROUP));
            String contractingOrgGroupNames = rowRef.get(getKey(config, ImporterConstants.CONTRACTING_AGENCY_GROUP));

            // Use holder arrays to capture values from lambda (for effectively final requirement)
            final Long[] existingActivityIdHolder = new Long[1];  // Store only the ID, not the entity
            final Long[] responsibleOrgIdHolder = new Long[1];
            final String[] programNamesHolder = new String[1];
            final String[] programClassificationHolder = new String[1];
            final Map<String, String> specificProgramValuesHolder = new java.util.LinkedHashMap<>();
            
            // Phase 1: Data preparation - use transaction for reading/preparing data ONLY
            try {
                PersistenceManager.inTransaction(() -> {
                    Session session = PersistenceManager.getRequestDBSession();
                    
                    AmpActivityVersion existing = existingActivity(projectTitle, projectCode, session);
                    existingActivityIdHolder[0] = existing != null ? existing.getAmpActivityId() : null;
                    if (existing != null && skipExisting) {
                        logger.info("Instructed to skip existing activities");
                        importedProject.setImportStatus(ImportStatus.SKIPPED);
                        return;
                    }

                    importDataModel.setProject_title(projectTitle);
                    importDataModel.setObjective(objective);
                    importDataModel.setProject_code(projectCode);
                    importDataModel.setDescription(projectDesc);

                    if (projectStatusStr != null && !projectStatusStr.trim().isEmpty()) {
                        Long statusId = getOrCreateActivityStatusCategoryValue(projectStatusStr.trim(), session);
                        if (statusId != null) {
                            importDataModel.setActivity_status(statusId);
                        }
                    }
                    setStatus(importDataModel, validateActivities, defaultActivityStatusId);

                    String donorAgencyCode = rowRef.get(getKey(config, ImporterConstants.DONOR_AGENCY_CODE));
                    String responsibleOrgCode = rowRef.get(getKey(config, ImporterConstants.RESPONSIBLE_ORGANIZATION_CODE));

                    logger.info("Configuration: " + config);
                    for (Map.Entry<String, String> entry : config.entrySet()) {
                        switch (entry.getValue()) {
                            case ImporterConstants.PROJECT_START_DATE: {
                                String dateStr = rowRef.get(entry.getKey().trim());
                                if (dateStr != null && !dateStr.trim().isEmpty()) {
                                    String formatted = org.digijava.module.aim.action.dataimporter.util.ImporterUtil.formatDateFromDateObject(dateStr.trim());
                                    if (formatted != null) {
                                        importDataModel.setActual_start_date(formatted);
                                    }
                                }
                                break;
                            }
                            case ImporterConstants.PROJECT_END_DATE: {
                                String dateStr = rowRef.get(entry.getKey().trim());
                                if (dateStr != null && !dateStr.trim().isEmpty()) {
                                    String formatted = org.digijava.module.aim.action.dataimporter.util.ImporterUtil.formatDateFromDateObject(dateStr.trim());
                                    if (formatted != null) {
                                        importDataModel.setActual_completion_date(formatted);
                                    }
                                }
                                break;
                            }
                            case ImporterConstants.PROJECT_LOCATION:
                                updateLocations(importDataModel, rowRef.get(entry.getKey().trim()), session);
                                break;
                            case ImporterConstants.PRIMARY_SECTOR:
                                updateSectors(importDataModel, rowRef.get(entry.getKey().trim()), session,
                                        true, primarySubSector, createMissingSectors,
                                        ImporterConstants.PRIMARY_SECTOR);
                                break;
                            case ImporterConstants.SECONDARY_SECTOR:
                                updateSectors(importDataModel, rowRef.get(entry.getKey().trim()), session,
                                        false, secondarySubSector, createMissingSectors,
                                        ImporterConstants.SECONDARY_SECTOR);
                                break;
                            case ImporterConstants.DONOR_AGENCY:
                                updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), donorAgencyCode, session, ImporterConstants.ORG_TYPE_DONOR, createMissingOrgs, orgGroupId, resolveOrgGroups(donorOrgGroupNames, importedOrgGroupName), createMissingOrgGroups);
                                break;
                            case ImporterConstants.RESPONSIBLE_ORGANIZATION:
                                responsibleOrgIdHolder[0] = updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), responsibleOrgCode, session, ImporterConstants.ORG_TYPE_RESPONSIBLE_ORG, createMissingOrgs, orgGroupId, resolveOrgGroups(responsibleOrgGroupNames, importedOrgGroupName), createMissingOrgGroups);
                                break;
                            case ImporterConstants.BENEFICIARY_AGENCY:
                                responsibleOrgIdHolder[0] = updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), responsibleOrgCode, session, ImporterConstants.ORG_TYPE_BENEFICIARY_AGENCY, createMissingOrgs, orgGroupId, resolveOrgGroups(beneficiaryOrgGroupNames, importedOrgGroupName), createMissingOrgGroups);
                                break;
                            case ImporterConstants.EXECUTING_AGENCY:
                                updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), null, session, ImporterConstants.ORG_TYPE_EXECUTING_AGENCY, createMissingOrgs, orgGroupId, resolveOrgGroups(executingOrgGroupNames, importedOrgGroupName), createMissingOrgGroups);
                                break;
                            case ImporterConstants.IMPLEMENTING_AGENCY:
                                updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), null, session, ImporterConstants.ORG_TYPE_IMPLEMENTING_AGENCY, createMissingOrgs, orgGroupId, resolveOrgGroups(implementingOrgGroupNames, importedOrgGroupName), createMissingOrgGroups);
                                break;
                            case ImporterConstants.CONTRACTING_AGENCY:
                                updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), null, session, ImporterConstants.ORG_TYPE_CONTRACTING_AGENCY, createMissingOrgs, orgGroupId, resolveOrgGroups(contractingOrgGroupNames, importedOrgGroupName), createMissingOrgGroups);
                                break;
                            case ImporterConstants.TRANSACTION_AMOUNT: {
                                boolean commitment = true, disbursement = true, expenditure = false;
                                String adjustmentType = ImporterConstants.ADJUSTMENT_TYPE_ACTUAL;
                                if (config.containsValue(ImporterConstants.MEASURE_TYPE)) {
                                    String measureTypeStr = rowRef.get(getKey(config, ImporterConstants.MEASURE_TYPE));
                                    ImporterUtil.MeasureTypeResult parsed = parseMeasureType(measureTypeStr);
                                    if (parsed != null) {
                                        commitment = parsed.commitment;
                                        disbursement = parsed.disbursement;
                                        expenditure = parsed.expenditure;
                                        adjustmentType = parsed.adjustmentType;
                                    }
                                }
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), commitment, disbursement, expenditure, adjustmentType, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            }
                            case ImporterConstants.PLANNED_COMMITMENT:
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), true, false, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            case ImporterConstants.PLANNED_DISBURSEMENT:
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, true, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            case ImporterConstants.PLANNED_EXPENDITURE:
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, false, true, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            case ImporterConstants.ACTUAL_COMMITMENT:
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), true, false, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            case ImporterConstants.ACTUAL_DISBURSEMENT:
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, true, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            case ImporterConstants.ACTUAL_EXPENDITURE:
                                fundings.addAll(setFundingItemsForTxt(rowRef, config, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, false, true, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, null, createMissingOrgs, orgGroupId, importedOrgGroupName, createMissingOrgGroups, addDisbursementForCommitment));
                                break;
                            case ImporterConstants.MEASURE_TYPE:
                                break;
                            case ImporterConstants.ORG_GROUP:
                            case ImporterConstants.DONOR_ORGANIZATION_GROUP:
                            case ImporterConstants.RESPONSIBLE_ORGANIZATION_GROUP:
                            case ImporterConstants.BENEFICIARY_AGENCY_GROUP:
                            case ImporterConstants.EXECUTING_AGENCY_GROUP:
                            case ImporterConstants.IMPLEMENTING_AGENCY_GROUP:
                            case ImporterConstants.CONTRACTING_AGENCY_GROUP:
                                break;
                            case ImporterConstants.PROJECT_STATUS:
                                break;
                            case ImporterConstants.PROGRAM_NAME:
                                programNamesHolder[0] = rowRef.get(entry.getKey().trim());
                                break;
                            case ImporterConstants.PROGRAM_CLASSIFICATION:
                                programClassificationHolder[0] = rowRef.get(entry.getKey().trim());
                                break;
                            case ImporterConstants.PRIMARY_PROGRAM:
                                specificProgramValuesHolder.put(ImporterConstants.PRIMARY_PROGRAM, rowRef.get(entry.getKey().trim()));
                                break;
                            case ImporterConstants.SECONDARY_PROGRAM:
                                specificProgramValuesHolder.put(ImporterConstants.SECONDARY_PROGRAM, rowRef.get(entry.getKey().trim()));
                                break;
                            case ImporterConstants.TERTIARY_PROGRAM:
                                specificProgramValuesHolder.put(ImporterConstants.TERTIARY_PROGRAM, rowRef.get(entry.getKey().trim()));
                                break;
                            case ImporterConstants.NATIONAL_PLAN_OBJECTIVE:
                                specificProgramValuesHolder.put(ImporterConstants.NATIONAL_PLAN_OBJECTIVE, rowRef.get(entry.getKey().trim()));
                                break;
                            default:
                                logger.error("Unexpected value: " + entry.getValue());
                                break;
                        }
                    }
                    if (!config.containsValue(ImporterConstants.PROJECT_LOCATION) && defaultLocationId != null) {
                        applyDefaultLocation(importDataModel, defaultLocationId, session);
                    }
                    logger.info("Funding items :{}", fundings);
                });
            } catch (RuntimeException e) {
                Throwable cause = e.getCause();
                if (cause instanceof JsonProcessingException) {
                    throw (JsonProcessingException) cause;
                }
                importedProject.setImportStatus(ImportStatus.FAILED);
                persistImportedProjectStatus(importedProject);
                logger.error("Error preparing txt row for project {}: {}", projectCode, e.getMessage(), e);
                continue;
            }

            if (importedProject.getImportStatus() == ImportStatus.SKIPPED) {
                persistImportedProjectStatus(importedProject);
                continue;
            }

            if (skipRecordsWithoutTransactions && !hasTransactions(fundings)) {
                importedProject.setImportStatus(ImportStatus.SKIPPED);
                persistImportedProjectStatus(importedProject);
                logger.info("Skipping txt row for project {} because no non-zero transactions were found", projectCode);
                continue;
            }

            // Clear session after Phase 1 to avoid contamination of Phase 2's transaction
            // Phase 1's committed changes may leave pending actions in the session that conflict with ActivityGatekeeper
            Session currentSession = PersistenceManager.getRequestDBSession();
            if (currentSession != null && currentSession.isOpen()) {
                currentSession.clear();
            }

            // Phase 2: Activity import - DO NOT wrap in transaction, let ActivityGatekeeper handle it
            // This avoids nested transaction issues when ActivityGatekeeper.doWithLock creates its own transaction
            try {
                // Pass only the ID, not the entity - importTheData will re-fetch in its own transaction context
                Long activityId = importTheData(importDataModel, null, importedProject, componentName, componentCode, responsibleOrgIdHolder[0], fundings, existingActivityIdHolder[0], validateActivities, replaceExistingTransactions);
                if (activityId != null && programNamesHolder[0] != null && !programNamesHolder[0].trim().isEmpty()) {
                    final Long activityIdFinal = activityId;
                    PersistenceManager.inTransaction(() -> {
                        Session s = PersistenceManager.getRequestDBSession();
                        addProgramsToActivity(activityIdFinal, programNamesHolder[0], programClassificationHolder[0],
                                defaultProgramClassification, createMissingPrograms, s);
                    });
                }
                if (activityId != null && !specificProgramValuesHolder.isEmpty()) {
                    final Long activityIdFinal = activityId;
                    PersistenceManager.inTransaction(() -> {
                        Session s = PersistenceManager.getRequestDBSession();
                        for (Map.Entry<String, String> specificProgramEntry : specificProgramValuesHolder.entrySet()) {
                            String rawProgramNames = specificProgramEntry.getValue();
                            if (rawProgramNames == null || rawProgramNames.trim().isEmpty()) {
                                continue;
                            }
                            addProgramsToActivity(activityIdFinal, rawProgramNames,
                                    specificProgramEntry.getKey(), null, createMissingPrograms, s);
                        }
                    });
                }
            } catch (JsonProcessingException e) {
                throw e;
            }
        }

    }

    private static String resolveOrgGroups(String roleSpecificGroupNames, String fallbackGroupNames) {
        if (roleSpecificGroupNames != null && !roleSpecificGroupNames.trim().isEmpty()) {
            return roleSpecificGroupNames.trim();
        }
        return fallbackGroupNames;
    }
}
