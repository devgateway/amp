package org.digijava.module.aim.action.dataimporter;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.digijava.module.aim.action.dataimporter.util.ImportedFileUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.*;

public class ExcelImporter {
    static Logger logger = LoggerFactory.getLogger(ExcelImporter.class);
    private static final int BATCH_SIZE = 1000;

    public static int processExcelFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config, boolean isInternal) {
        int res=0;
        ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.IN_PROGRESS);
        try (Workbook workbook = new XSSFWorkbook(file)) {
            int numberOfSheets = workbook.getNumberOfSheets();
            logger.info("Number of sheets: " + numberOfSheets);

            // Process each sheet in the workbook
            for (int i = 0; i < numberOfSheets; i++) {
                logger.info("Sheet number: " + i);
                Sheet sheet = workbook.getSheetAt(i);
                if (isInternal) {
                    addDonorAgencyColumn(sheet, FeaturesUtil.getGlobalSettingValue("Internal Ecowas Donor"));

                }

                processSheetInBatches(sheet, request,config, importedFilesRecord);
            }

            logger.info("Closing the workbook...");
            res =1;
        } catch (IOException e) {
            ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.FAILED);
            logger.error("Error processing Excel file: " + e.getMessage(), e);
        } catch (InvalidFormatException | InvalidOperationException e) {
            logger.error("Error processing Excel file: " + e.getMessage(),e);
        }
        logger.info("Finished processing file record id: "+importedFilesRecord.getId()+" with status: "+importedFilesRecord.getImportStatus());
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


    public static void processSheetInBatches(Sheet sheet, HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord) throws JsonProcessingException {
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
            processBatch(batch, sheet, request,config, importedFilesRecord);
        }
    }


    public static void processBatch(List<Row> batch,Sheet sheet, HttpServletRequest request, Map<String, String> config, ImportedFilesRecord importedFilesRecord) throws JsonProcessingException {
        // Process the batch of rows
        SessionUtil.extendSessionIfNeeded(request);
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        for (Row row : batch) {
            if (row != null) {
                ImportedProject importedProject = new ImportedProject();
                importedProject.setImportedFilesRecord(importedFilesRecord);
                List<Funding> fundings = new ArrayList<>();

                ImportDataModel importDataModel = new ImportDataModel();
                importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
                importDataModel.setCreated_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
                importDataModel.setTeam(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam().getAmpTeamId());
                importDataModel.setIs_draft(true);
                OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
                importDataModel.setCreation_date(now.format(formatter));
                setStatus(importDataModel);

                int componentCodeColumn = getColumnIndexByName(sheet, getKey(config, "Component Code"));
                String componentCode = componentCodeColumn >= 0 ? getStringValueFromCell(row.getCell(componentCodeColumn),true) : null;

                int componentNameColumn = getColumnIndexByName(sheet, getKey(config, "Component Name"));
                String componentName = componentNameColumn >= 0 ? getStringValueFromCell(row.getCell(componentNameColumn),true): null;

                int donorAgencyCodeColumn = getColumnIndexByName(sheet, getKey(config, "Donor Agency Code"));
                String donorAgencyCode = donorAgencyCodeColumn >= 0 ? getStringValueFromCell(row.getCell(donorAgencyCodeColumn),true) : null;

                int responsibleOrgCodeColumn = getColumnIndexByName(sheet, getKey(config, "Responsible Organization Code"));
                String responsibleOrgCode = responsibleOrgCodeColumn >= 0 ? getStringValueFromCell(row.getCell(responsibleOrgCodeColumn),true) : null;


                int projectCodeColumn = getColumnIndexByName(sheet, getKey(config, "Project Code"));
                String projectCode = projectCodeColumn >= 0 ? getStringValueFromCell(row.getCell(projectCodeColumn),false) : "";
                importDataModel.setProject_code(projectCode);

                int projectTitleColumn = getColumnIndexByName(sheet, getKey(config, "Project Title"));
                String projectTitle = projectTitleColumn >= 0 ? getStringValueFromCell(row.getCell(projectTitleColumn),false) : "";
                importDataModel.setProject_title(projectTitle);

                int proposedStartDateColumn = getColumnIndexByName(sheet, getKey(config, "Proposed Start Date"));
                String proposedStartDate = proposedStartDateColumn >= 0 ? getDateFromExcel(row, proposedStartDateColumn) : null;
                importDataModel.setProposed_start_date(proposedStartDate);

                int originalCompletionDateColumn = getColumnIndexByName(sheet, getKey(config, "Proposed Completion Date"));
                String originalCompletionDate = originalCompletionDateColumn >= 0 ? getDateFromExcel(row, originalCompletionDateColumn) : null;
                importDataModel.setOriginal_completion_date(originalCompletionDate);

                int actualStartDateColumn = getColumnIndexByName(sheet, getKey(config, "Actual Start Date"));
                String actualStartDate = actualStartDateColumn >= 0 ? getDateFromExcel(row, actualStartDateColumn) : null;
                importDataModel.setActual_start_date(actualStartDate);

                int actualCompletionDateColumn = getColumnIndexByName(sheet, getKey(config, "Actual Completion Date"));
                String actualCompletionDate = actualCompletionDateColumn >= 0 ? getDateFromExcel(row, actualCompletionDateColumn) : null;
                importDataModel.setActual_completion_date(actualCompletionDate);


                int projectDescColumn = getColumnIndexByName(sheet, getKey(config, "Project Description"));
                String projectDesc = projectDescColumn >= 0 ? getStringValueFromCell(row.getCell(projectDescColumn),false) : null;
                importDataModel.setDescription(projectDesc);

                AmpActivityVersion existing = existingActivity(projectTitle, projectCode, session);
                Long responsibleOrgId = null;

                logger.info("Row Number: {}, Sheet Name: {}", row.getRowNum(), sheet.getSheetName());
                for (Map.Entry<String, String> entry : config.entrySet()) {
                    Funding fundingItem = new Funding();

                    int columnIndex = getColumnIndexByName(sheet, entry.getKey());

                    if (columnIndex >= 0) {
                        Cell cell = row.getCell(columnIndex);
                        switch (entry.getValue()) {
                            case "Project Location":
                                 updateLocations(importDataModel,Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(),session);
                                break;
                            case "Primary Sector":
                                updateSectors(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), session, true);
                                break;
                            case "Secondary Sector":
                                updateSectors(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), session, false);
                                break;
                            case "Donor Agency":
                                logger.info("Getting donor");
                                updateOrgs(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), donorAgencyCode, session, "donor");
                                break;
                            case "Responsible Organization":
                                responsibleOrgId = updateOrgs(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), responsibleOrgCode, session, "responsibleOrg");
                                break;
                            case "Beneficiary Agency":
                                responsibleOrgId = updateOrgs(importDataModel, Objects.requireNonNull(getStringValueFromCell(cell, false)).trim(), responsibleOrgCode, session, "beneficiaryAgency");
                                break;
                            case "Funding Item":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, true, true, false,"Actual", fundingItem, existing);
                                break;
                            case "Planned Commitment":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, true, false,false, "Planned", fundingItem, existing);
                                break;
                            case "Planned Disbursement":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, false, true, false,"Planned", fundingItem, existing);
                                break;
                            case "Planned Expenditure":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, false, false,true, "Planned", fundingItem, existing);
                                break;
                            case "Actual Commitment":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, true, false, false,"Actual", fundingItem, existing);
                                break;
                            case "Actual Disbursement":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, false, true, false,"Actual", fundingItem, existing);
                                break;
                            case "Actual Expenditure":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell, false, false,true, "Actual", fundingItem, existing);
                                break;
                            case "Reporting Date":
                            default:
                                logger.error("Unexpected value: " + entry.getValue());
                                break;

                        }


                    }
                    fundings.add(fundingItem);


                }
                logger.info("Fundings at this point: {}",fundings);


                importTheData(importDataModel, session, importedProject, componentName, componentCode, responsibleOrgId, fundings, existing, projectCode);

            }
        }
    }
}
