package org.digijava.module.aim.action.dataimporter;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProject;
import org.digijava.module.aim.action.dataimporter.model.Funding;
import org.digijava.module.aim.action.dataimporter.model.ImportDataModel;
import org.digijava.module.aim.action.dataimporter.util.ImporterConstants;
import org.digijava.module.aim.action.dataimporter.util.ImportedFileUtil;
import org.digijava.module.aim.action.dataimporter.util.ImporterUtil;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.existingActivity;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.getColumnIndexByName;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.getKey;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.getStringValueFromCell;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.parseMeasureType;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.addIndicatorDataToActivity;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.importTheData;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.setAFundingItemForExcel;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.setStatus;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.updateLocations;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.updateOrgs;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.updateSectors;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ExcelImporter {
    static Logger logger = LoggerFactory.getLogger(ExcelImporter.class);
    private static final int BATCH_SIZE = 1000;

    public static int processExcelFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config, boolean isInternal) {
        return processExcelFileInBatches(importedFilesRecord, file, request, config, isInternal, false, null, false, null);
    }

    public static int processExcelFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config, boolean isInternal, boolean skipExisting, String sheetNameToProcess, boolean createMissingOrgs, Long orgGroupId) {
        int res = 0;
        ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.IN_PROGRESS);
        try (Workbook workbook = new XSSFWorkbook(file)) {
            int numberOfSheets = workbook.getNumberOfSheets();
            logger.info("Number of sheets: {}", numberOfSheets);

            if (sheetNameToProcess != null && !sheetNameToProcess.trim().isEmpty()) {
                Sheet sheet = workbook.getSheet(sheetNameToProcess);
                if (sheet == null) {
                    logger.error("Sheet not found: {}", sheetNameToProcess);
                    ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.FAILED);
                    return 0;
                }
                if (isInternal) {
                    addDonorAgencyColumn(sheet, FeaturesUtil.getGlobalSettingValue("Internal Ecowas Donor"));
                }
                processSheetInBatches(sheet, request, config, importedFilesRecord, skipExisting, createMissingOrgs, orgGroupId);
            } else {
                // Process each sheet in the workbook
                for (int i = 0; i < numberOfSheets; i++) {
                    logger.info("Sheet number: {}", i);
                    Sheet sheet = workbook.getSheetAt(i);
                    if (isInternal) {
                        addDonorAgencyColumn(sheet, FeaturesUtil.getGlobalSettingValue("Internal Ecowas Donor"));
                    }
                    processSheetInBatches(sheet, request, config, importedFilesRecord, skipExisting, createMissingOrgs, orgGroupId);
                }
            }

            logger.info("Closing the workbook...");
            res = 1;
        } catch (IOException e) {
            ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.FAILED);
            logger.error("Error processing Excel file: {}", e.getMessage(), e);
        } catch (InvalidFormatException | InvalidOperationException e) {
            logger.error("Error processing Excel file: {}", e.getMessage(), e);
        }
        logger.info("Finished processing file record id: {} with status: {}", importedFilesRecord.getId(), importedFilesRecord.getImportStatus());
        return res;

    }

    private static void addDonorAgencyColumn(Sheet sheet, String donorAgencyValue) {
        // Get the header row, create if it doesn't exist
        logger.info("Adding Ecowas column");
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
        }

        // Set the header for the "Donor Agency" column
        int donorAgencyColumnIndex = headerRow.getLastCellNum();
        Cell headerCell = headerRow.createCell(donorAgencyColumnIndex);
        headerCell.setCellValue("Donor Agency");

        // Populate each row in the new column with the donor agency value
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            Cell cell = row.createCell(donorAgencyColumnIndex);
            cell.setCellValue(donorAgencyValue);
            logger.info("Cell newly created: " + row.getCell(donorAgencyColumnIndex).getStringCellValue());
        }
    }


    public static void processSheetInBatches(Sheet sheet, HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord, boolean skipExisting, boolean createMissingOrgs, Long orgGroupId) throws JsonProcessingException {
        // Get the number of rows in the sheet
        int rowCount = sheet.getPhysicalNumberOfRows();
        logger.info("There are {} rows in sheet {} " , rowCount, sheet.getSheetName());

        // Process each row in batches
        for (int i = 0; i < rowCount; i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, rowCount);
            List<Row> batch = new ArrayList<>();

            // Retrieve a batch of rows
            for (int j = i; j < endIndex; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    batch.add(row);
                }
            }

            // Process the batch
            processBatch(batch, sheet, request,config, importedFilesRecord, skipExisting, createMissingOrgs, orgGroupId);
        }
    }


    public static void processBatch(List<Row> batch,Sheet sheet, HttpServletRequest request, Map<String, String> config, ImportedFilesRecord importedFilesRecord, boolean skipExisting, boolean createMissingOrgs, Long orgGroupId) throws JsonProcessingException {
        // Process the batch of rows
        SessionUtil.extendSessionIfNeeded(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        for (Row row : batch) {
            if (row != null) {
                final Row rowRef = row;
                ImportedProject importedProject = new ImportedProject();
                importedProject.setImportedFilesRecord(importedFilesRecord);
                List<Funding> fundings = new ArrayList<>();

                ImportDataModel importDataModel = new ImportDataModel();
                importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
                // created_by is set in ensureCreatedBySet when building the API map (correct for new vs existing)
                importDataModel.setTeam(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam().getAmpTeamId());
                importDataModel.setIs_draft(true);
                OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
                importDataModel.setCreation_date(now.format(formatter));

                int componentCodeColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.COMPONENT_CODE));
                String componentCode = componentCodeColumn >= 0 ? getStringValueFromCell(rowRef.getCell(componentCodeColumn),true) : null;

                int componentNameColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.COMPONENT_NAME));
                String componentName = componentNameColumn >= 0 ? getStringValueFromCell(rowRef.getCell(componentNameColumn),true): null;

                int donorAgencyCodeColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.DONOR_AGENCY_CODE));
                String donorAgencyCode = donorAgencyCodeColumn >= 0 ? getStringValueFromCell(rowRef.getCell(donorAgencyCodeColumn),true) : null;

                int responsibleOrgCodeColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.RESPONSIBLE_ORGANIZATION_CODE));
                String responsibleOrgCode = responsibleOrgCodeColumn >= 0 ? getStringValueFromCell(rowRef.getCell(responsibleOrgCodeColumn),true) : null;

                int primarySubSectorColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.PRIMARY_SUBSECTOR));
                String primarySubSector = primarySubSectorColumn >= 0 ? getStringValueFromCell(rowRef.getCell(primarySubSectorColumn),true) : null;

                int secondarySubSectorColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.SECONDARY_SUBSECTOR));
                String secondarySubSector = secondarySubSectorColumn >= 0 ? getStringValueFromCell(rowRef.getCell(secondarySubSectorColumn),true) : null;

                int projectCodeColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.PROJECT_CODE));
                String projectCode = projectCodeColumn >= 0 ? getStringValueFromCell(rowRef.getCell(projectCodeColumn),false) : "";
                importDataModel.setProject_code(projectCode);

                int projectTitleColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.PROJECT_TITLE));
                String projectTitle = projectTitleColumn >= 0 ? getStringValueFromCell(rowRef.getCell(projectTitleColumn),false) : "";
                importDataModel.setProject_title(projectTitle);
                int objectiveColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.OBJECTIVE));
                String objective = objectiveColumn >= 0 ? getStringValueFromCell(rowRef.getCell(objectiveColumn),false) : null;
                importDataModel.setObjective(objective);

                int projectDescColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.PROJECT_DESCRIPTION));
                String projectDesc = projectDescColumn >= 0 ? getStringValueFromCell(rowRef.getCell(projectDescColumn),false) : null;
                importDataModel.setDescription(projectDesc);

                // Use holder arrays to capture values from lambda (for effectively final requirement)
                final Long[] existingActivityIdHolder = new Long[1];  // Store only the ID, not the entity
                final Long[] responsibleOrgIdHolder = new Long[1];
                
                // Phase 1: Data preparation - use transaction for reading/preparing data ONLY
                try {
                    PersistenceManager.inTransaction(() -> {
                        Session session = PersistenceManager.getRequestDBSession();
                        
                        if (config.containsValue(ImporterConstants.PROJECT_STATUS)) {
                            String projectStatusStr = ImporterUtil.getCellValueByConfig(rowRef, sheet, config, ImporterConstants.PROJECT_STATUS);
                            if (projectStatusStr != null && !projectStatusStr.trim().isEmpty()) {
                                Long statusId = ImporterUtil.getOrCreateActivityStatusCategoryValue(projectStatusStr.trim(), session);
                                if (statusId != null) {
                                    importDataModel.setActivity_status(statusId);
                                }
                            }
                        }
                        if (config.containsValue(ImporterConstants.PROCUREMENT_SYSTEM)) {
                            String procurementSystemStr = ImporterUtil.getCellValueByConfig(rowRef, sheet, config, ImporterConstants.PROCUREMENT_SYSTEM);
                            if (procurementSystemStr != null && !procurementSystemStr.trim().isEmpty()) {
                                Long procId = ImporterUtil.getCategoryValueByName(CategoryConstants.PROCUREMENT_SYSTEM_KEY, procurementSystemStr.trim(), session);
                                if (procId != null) {
                                    importDataModel.setProcurement_system(procId);
                                }
                            }
                        }
                        setStatus(importDataModel);

                        AmpActivityVersion existing = existingActivity(projectTitle, projectCode, session);
                        existingActivityIdHolder[0] = existing != null ? existing.getAmpActivityId() : null;
                        if (existing != null && skipExisting) {
                            logger.info("Skipping existing activity: {}", existing.getAmpActivityId());
                            importedProject.setImportStatus(ImportStatus.SKIPPED);
                            return;
                        }

                        logger.info("Row Number: {}, Sheet Name: {}", rowRef.getRowNum(), sheet.getSheetName());
                        for (Map.Entry<String, String> entry : config.entrySet()) {
                            Funding fundingItem = new Funding();

                            int columnIndex = getColumnIndexByName(sheet, entry.getKey());

                            if (columnIndex >= 0) {
                                Cell cell = rowRef.getCell(columnIndex);
                                switch (entry.getValue()) {
                                    case ImporterConstants.PROJECT_LOCATION:
                                        updateLocations(importDataModel,Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(),session);
                                        break;
                                    case ImporterConstants.PRIMARY_SECTOR:
                                        updateSectors(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), session, true, primarySubSector);
                                        break;
                                    case ImporterConstants.SECONDARY_SECTOR:
                                        updateSectors(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), session, false, secondarySubSector);
                                        break;
                                    case ImporterConstants.DONOR_AGENCY:
                                        logger.info("Getting donor");
                                        updateOrgs(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), donorAgencyCode, session, ImporterConstants.ORG_TYPE_DONOR, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.RESPONSIBLE_ORGANIZATION:
                                        responsibleOrgIdHolder[0] = updateOrgs(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), responsibleOrgCode, session, ImporterConstants.ORG_TYPE_RESPONSIBLE_ORG, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.BENEFICIARY_AGENCY:
                                        responsibleOrgIdHolder[0] = updateOrgs(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), responsibleOrgCode, session, ImporterConstants.ORG_TYPE_BENEFICIARY_AGENCY, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.TRANSACTION_AMOUNT: {
                                        boolean commitment = true, disbursement = true, expenditure = false;
                                        String adjustmentType = ImporterConstants.ADJUSTMENT_TYPE_ACTUAL;
                                        if (config.containsValue(ImporterConstants.MEASURE_TYPE)) {
                                            String measureTypeStr = ImporterUtil.getCellValueByConfig(rowRef, sheet, config, ImporterConstants.MEASURE_TYPE);
                                            ImporterUtil.MeasureTypeResult parsed = parseMeasureType(measureTypeStr);
                                            if (parsed != null) {
                                                commitment = parsed.commitment;
                                                disbursement = parsed.disbursement;
                                                expenditure = parsed.expenditure;
                                                adjustmentType = parsed.adjustmentType;
                                            }
                                        }
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, commitment, disbursement, expenditure, adjustmentType, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    }
                                    case ImporterConstants.PLANNED_COMMITMENT:
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, true, false,false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.PLANNED_DISBURSEMENT:
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, false, true, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.PLANNED_EXPENDITURE:
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, false, false,true, ImporterConstants.ADJUSTMENT_TYPE_PLANNED, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.ACTUAL_COMMITMENT:
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, true, false, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.ACTUAL_DISBURSEMENT:
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, false, true, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.ACTUAL_EXPENDITURE:
                                        setAFundingItemForExcel(sheet, config, rowRef, entry, importDataModel, session, cell, false, false,true, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL, fundingItem, null, createMissingOrgs, orgGroupId);
                                        break;
                                    case ImporterConstants.MEASURE_TYPE:
                                        break;
                                    case ImporterConstants.PROJECT_STATUS:
                                        break;
                                    case ImporterConstants.PROCUREMENT_SYSTEM:
                                        break;
                                    case ImporterConstants.REPORTING_DATE:
                                    default:
                                        logger.error("Unexpected value: " + entry.getValue());
                                        break;
                                }
                            }
                            fundings.add(fundingItem);
                        }
                    });
                } catch (RuntimeException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof JsonProcessingException) {
                        throw (JsonProcessingException) cause;
                    }
                    logger.error("Error preparing data for row " + rowRef.getRowNum() + " in sheet " + sheet.getSheetName() + ": " + e.getMessage(), e);
                }

                // Clear session after Phase 1 to avoid contamination of Phase 2's transaction
                // Phase 1's committed changes may leave pending actions in the session that conflict with ActivityGatekeeper
                Session currentSession = PersistenceManager.getRequestDBSession();
                if (currentSession != null && currentSession.isOpen()) {
                    currentSession.clear();
                }

                // Phase 2: Activity import - DO NOT wrap in transaction, let ActivityGatekeeper handle it
                // This avoids nested transaction issues when ActivityGatekeeper.doWithLock creates its own transaction
                Long activityId = null;
                if (importedProject.getImportStatus() != ImportStatus.SKIPPED) {
                    try {
                        // Pass only the ID, not the entity - importTheData will re-fetch in its own transaction context
                        activityId = importTheData(importDataModel, null, importedProject, componentName, componentCode, responsibleOrgIdHolder[0], fundings, existingActivityIdHolder[0]);
                    } catch (JsonProcessingException e) {
                        throw e;
                    }
                }

                // Phase 3: Indicator import - use separate transaction
                if (activityId != null && config.containsValue(ImporterConstants.INDICATOR_NAME)) {
                    logger.info("Adding indicator data for activity " + activityId);
                    try {
                        final Long activityIdFinal = activityId;
                        PersistenceManager.inTransaction(() -> {
                            Session s = PersistenceManager.getRequestDBSession();
                            addIndicatorDataToActivity(activityIdFinal, rowRef, sheet, config, s);
                            logger.info("Indicator data added for activity " + activityIdFinal);
                        });
                    } catch (Exception e) {
                        logger.error("Failed to add indicator data for activity " + activityId, e);
                    }
                }
            }
        }
    }
}
