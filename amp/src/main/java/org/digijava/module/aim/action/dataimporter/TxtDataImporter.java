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


    public static int processTxtFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config, boolean isInternal, boolean skipExisting, boolean createMissingOrgs, Long orgGroupId)
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
                    processBatch(batch, request,config,importedFilesRecord, skipExisting, createMissingOrgs, orgGroupId);
                    // Clear the batch for the next set of rows
                    batch.clear();
                    batchNumber+=1;
                }
            }

            // Process any remaining rows in the batch
            if (!batch.isEmpty()) {
                logger.info("Processing last batch of size {}", batch.size());
                processBatch(batch, request,config,importedFilesRecord, skipExisting, createMissingOrgs, orgGroupId);
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error processing txt file "+e.getMessage(),e);
            return 0;
        }
        return 1;
    }


    private static void processBatch(List<Map<String, String>> batch,  HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord, boolean skipExisting, boolean createMissingOrgs, Long orgGroupId) throws JsonProcessingException {
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
            importDataModel.setIs_draft(true);
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

            // Use holder arrays to capture values from lambda (for effectively final requirement)
            final Long[] existingActivityIdHolder = new Long[1];  // Store only the ID, not the entity
            final Long[] responsibleOrgIdHolder = new Long[1];
            
            // Phase 1: Data preparation - use transaction for reading/preparing data ONLY
            try {
                PersistenceManager.inTransaction(() -> {
                    Session session = PersistenceManager.getRequestDBSession();
                    
                    AmpActivityVersion existing = existingActivity(projectTitle, projectCode, session);
                    existingActivityIdHolder[0] = existing != null ? existing.getAmpActivityId() : null;
                    if (existing != null && skipExisting) {
                        logger.info("Skipping existing activity: {}", existing.getAmpActivityId());
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
                    setStatus(importDataModel);

                    String donorAgencyCode = rowRef.get(getKey(config, ImporterConstants.DONOR_AGENCY_CODE));
                    String responsibleOrgCode = rowRef.get(getKey(config, ImporterConstants.RESPONSIBLE_ORGANIZATION_CODE));

                    logger.info("Configuration: " + config);
                    for (Map.Entry<String, String> entry : config.entrySet()) {
                        Funding fundingItem = new Funding();
                        switch (entry.getValue()) {
                            case ImporterConstants.PROJECT_LOCATION:
                                updateLocations(importDataModel, rowRef.get(entry.getKey().trim()), session);
                                break;
                            case ImporterConstants.PRIMARY_SECTOR:
                                updateSectors(importDataModel, rowRef.get(entry.getKey().trim()), session, true, primarySubSector);
                                break;
                            case ImporterConstants.SECONDARY_SECTOR:
                                updateSectors(importDataModel, rowRef.get(entry.getKey().trim()), session, false, secondarySubSector);
                                break;
                            case ImporterConstants.DONOR_AGENCY:
                                updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), donorAgencyCode, session, ImporterConstants.ORG_TYPE_DONOR, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.RESPONSIBLE_ORGANIZATION:
                                responsibleOrgIdHolder[0] = updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), responsibleOrgCode, session, ImporterConstants.ORG_TYPE_RESPONSIBLE_ORG, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.BENEFICIARY_AGENCY:
                                responsibleOrgIdHolder[0] = updateOrgs(importDataModel, rowRef.get(entry.getKey().trim()), responsibleOrgCode, session, ImporterConstants.ORG_TYPE_BENEFICIARY_AGENCY, createMissingOrgs, orgGroupId);
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
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), commitment, disbursement, expenditure, adjustmentType, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            }
                            case ImporterConstants.PLANNED_COMMITMENT:
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), true, false, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.PLANNED_DISBURSEMENT:
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, true, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.PLANNED_EXPENDITURE:
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, false, true, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.ACTUAL_COMMITMENT:
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), true, false, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.ACTUAL_DISBURSEMENT:
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, true, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.ACTUAL_EXPENDITURE:
                                setAFundingItemForTxt(config, rowRef, entry, importDataModel, session, Double.parseDouble(rowRef.get(entry.getKey().trim())), false, false, true, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, fundingItem, null, createMissingOrgs, orgGroupId);
                                break;
                            case ImporterConstants.MEASURE_TYPE:
                                break;
                            case ImporterConstants.PROJECT_STATUS:
                                break;
                            default:
                                logger.error("Unexpected value: " + entry.getValue());
                                break;
                        }
                        fundings.add(fundingItem);
                        logger.info("Funding items :{}", fundings);
                    }
                });
            } catch (RuntimeException e) {
                Throwable cause = e.getCause();
                if (cause instanceof JsonProcessingException) {
                    throw (JsonProcessingException) cause;
                }
                throw e;
            }

            // Clear session after Phase 1 to avoid contamination of Phase 2's transaction
            // Phase 1's committed changes may leave pending actions in the session that conflict with ActivityGatekeeper
            Session currentSession = PersistenceManager.getRequestDBSession();
            if (currentSession != null && currentSession.isOpen()) {
                currentSession.clear();
            }

            // Phase 2: Activity import - DO NOT wrap in transaction, let ActivityGatekeeper handle it
            // This avoids nested transaction issues when ActivityGatekeeper.doWithLock creates its own transaction
            if (importedProject.getImportStatus() != ImportStatus.SKIPPED) {
                try {
                    // Pass only the ID, not the entity - importTheData will re-fetch in its own transaction context
                    importTheData(importDataModel, null, importedProject, componentName, componentCode, responsibleOrgIdHolder[0], fundings, existingActivityIdHolder[0]);
                } catch (JsonProcessingException e) {
                    throw e;
                }
            }
        }

    }
}
