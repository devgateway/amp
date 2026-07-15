package org.digijava.module.aim.action.dataimporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import static org.digijava.module.aim.action.dataimporter.ExcelImporter.processExcelFileInBatches;
import org.digijava.module.aim.action.dataimporter.dbentity.DataImporterConfig;
import org.digijava.module.aim.action.dataimporter.dbentity.DataImporterConfigValues;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.digijava.module.aim.action.dataimporter.util.ImportedFileUtil;
import org.digijava.module.aim.action.dataimporter.util.ImporterUtil;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.ConstantsMap;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.isFileContentValid;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.isFileReadable;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.removeMapItem;

import org.digijava.module.aim.action.dataimporter.util.ImporterConstants;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.DataImporterForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class DataImporter extends Action {
    static Logger logger = LoggerFactory.getLogger(DataImporter.class);

    private boolean canAccessDataImporter(HttpServletRequest request) {
        if ("yes".equals(request.getSession().getAttribute("ampAdmin"))) {
            return true;
        }
        AmpTeamMember current = TeamUtil.getCurrentAmpTeamMember();
        return current != null
                && current.getAmpMemberRole() != null
                && (Boolean.TRUE.equals(current.getAmpMemberRole().getTeamHead())
                || current.getAmpMemberRole().isApprover());
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!canAccessDataImporter(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        // List of fields - map of original to translated
        Map<String, String> fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo", fieldsInfo);
        request.setAttribute("fieldsInfoList", new ArrayList<>(fieldsInfo.values()));
        List<String> configNames= getConfigNames();
        request.setAttribute("configNames", configNames);
        List<AmpOrgGroup> orgGroups = DbUtil.getAllOrgGroups();
        request.setAttribute("orgGroups", orgGroups);
        List<AmpOrganisation> recordingOrganizations = getRecordingOrganizationsForImporter();
        request.setAttribute("recordingOrganizations", recordingOrganizations);
        request.setAttribute("activityStatuses", getActivityStatuses());
        request.setAttribute("programClassifications", getProgramClassificationNames());
        List<AmpCategoryValueLocations> availableLocations = getAvailableLocations();
        request.setAttribute("availableLocations", availableLocations);
        AmpCategoryValueLocations defaultLocation = DynLocationManagerUtil.getDefaultCountry();
        request.setAttribute("defaultLocationId", defaultLocation != null ? defaultLocation.getId() : null);
        DataImporterForm dataImporterForm = (DataImporterForm) form;

        if (Objects.equals(request.getParameter("action"), "configByName")) {
            logger.info(" this is the action " + request.getParameter("action"));
            String configName = request.getParameter("configName");
            Map<String, String> config= getConfigByName(configName);
            dataImporterForm.setColumnPairs(config);

            logger.info("Column Pairs:" + config);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(config);

            // Send response
            response.setContentType("application/json");
            response.getWriter().write(json);
            response.setCharacterEncoding("UTF-8");

            return null;
        }


        if (Objects.equals(request.getParameter("action"), "uploadTemplate")) {
            logger.info(" this is the action " + request.getParameter("action"));
            if (request.getParameter("uploadTemplate") != null) {
                logger.info(" this is the action " + request.getParameter("uploadTemplate"));
                response.setCharacterEncoding("UTF-8");

                // Only clear in-memory column pairs when no existing named config is active.
                // If the user has a configName selected, preserve its pairs so uploading a new
                // template just lets them add more column mappings without losing existing ones.
                String uploadConfigName = request.getParameter("configName");
                if (uploadConfigName == null || uploadConfigName.trim().isEmpty()) {
                    dataImporterForm.getColumnPairs().clear();
                } else {
                    // Reload from DB so the in-memory map reflects the latest saved state
                    Map<String, String> savedPairs = getConfigByName(uploadConfigName.trim());
                    dataImporterForm.getColumnPairs().clear();
                    dataImporterForm.getColumnPairs().putAll(savedPairs);
                }

                if (request.getParameter("fileType") != null) {
                    InputStream fileInputStream = dataImporterForm.getTemplateFile().getInputStream();
                    if (Objects.equals(request.getParameter("fileType"), "excel")) {
                        // Excel: return sheet names and columns per sheet for template configuration
                        List<String> sheetNames = new ArrayList<>();
                        Map<String, List<String>> columnsBySheet = new HashMap<>();
                        try (Workbook workbook = ImporterUtil.openWorkbookWithStrictFallback(fileInputStream)) {
                            int numberOfSheets = workbook.getNumberOfSheets();
                            for (int i = 0; i < numberOfSheets; i++) {
                                Sheet sheet = workbook.getSheetAt(i);
                                String sheetName = sheet.getSheetName();
                                sheetNames.add(sheetName);
                                List<String> columns = new ArrayList<>();
                                Row headerRow = sheet.getRow(0);
                                if (headerRow != null) {
                                    Iterator<Cell> cellIterator = headerRow.cellIterator();
                                    while (cellIterator.hasNext()) {
                                        Cell cell = cellIterator.next();
                                        String val = cell.getStringCellValue();
                                        if (val != null && !val.trim().isEmpty()) {
                                            columns.add(val.trim());
                                        }
                                    }
                                }
                                columnsBySheet.put(sheetName, columns.stream().sorted().collect(Collectors.toList()));
                            }
                        }
                        Map<String, Object> jsonResponse = new HashMap<>();
                        jsonResponse.put("sheetNames", sheetNames);
                        jsonResponse.put("columnsBySheet", columnsBySheet);
                        response.setContentType("application/json");
                        new ObjectMapper().writeValue(response.getWriter(), jsonResponse);
                    } else if (Objects.equals(request.getParameter("fileType"), "csv")) {
                        Set<String> headersSet = new HashSet<>();
                        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(fileInputStream)).build()) {
                            String[] headers = reader.readNext();
                            if (headers != null) {
                                headersSet.addAll(Arrays.asList(headers));
                            }
                        } catch (IOException | CsvValidationException e) {
                            logger.error("An error occurred during extraction of headers.", e);
                        }
                        headersSet = headersSet.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
                        StringBuilder headers = new StringBuilder();
                        headers.append("  <label for=\"columnName\">Select Column Name:</label>\n<select  class=\"select2\" style=\"width: 300px;\" id=\"columnName\">");
                        for (String option : headersSet) {
                            headers.append("<option>").append(option).append("</option>");
                        }
                        headers.append("</select>");
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().write(headers.toString());
                    } else if (Objects.equals(request.getParameter("fileType"), "text")) {
                        Set<String> headersSet = new HashSet<>();
                        String sep = request.getParameter("dataSeparator");
                        char separator = (sep != null && !sep.isEmpty()) ? sep.charAt(0) : ',';
                        CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
                        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(fileInputStream)).withCSVParser(parser).build()) {
                            String[] headers = reader.readNext();
                            if (headers != null) {
                                headersSet.addAll(Arrays.asList(headers));
                            } else {
                                logger.info("File is empty or does not contain headers.");
                            }
                        } catch (IOException | CsvValidationException e) {
                            logger.error("An error occurred during extraction of headers.", e);
                        }
                        headersSet = headersSet.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
                        StringBuilder headers = new StringBuilder();
                        headers.append("  <label for=\"columnName\">Select Column Name:</label>\n<select  class=\"select2\" style=\"width: 300px;\" id=\"columnName\">");
                        for (String option : headersSet) {
                            headers.append("<option>").append(option).append("</option>");
                        }
                        headers.append("</select>");
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().write(headers.toString());
                    }
                }
                response.setHeader("updatedMap", "");
            }
            return null;
        }


        if (Objects.equals(request.getParameter("action"), "addField")) {
            logger.info(" this is the action " + request.getParameter("action"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            String configName = request.getParameter("configName");
            Map<String, String> columnPairs = dataImporterForm.getColumnPairs();

            if (configName != null && !configName.trim().isEmpty()) {
                addColumnPairToConfig(configName.trim(), columnName, selectedField);
                columnPairs = getConfigByName(configName.trim());
            } else {
                columnPairs.put(columnName, selectedField);
            }
            logger.info("Column Pairs:" + columnPairs);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(columnPairs);

            response.setContentType("application/json");
            response.getWriter().write(json);
            response.setCharacterEncoding("UTF-8");

            return null;

        }


        if (Objects.equals(request.getParameter("action"), "removeField")) {
            logger.info(" this is the action " + request.getParameter("action"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            String configName = request.getParameter("configName");
            Map<String, String> columnPairs = dataImporterForm.getColumnPairs();

            if (configName != null && !configName.trim().isEmpty()) {
                removeColumnPairFromConfig(configName.trim(), columnName);
                columnPairs = getConfigByName(configName.trim());
            } else {
                removeMapItem(columnPairs, columnName, selectedField);
            }
            logger.info("Column Pairs:" + columnPairs);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(columnPairs);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

            return null;

        }

        if (Objects.equals(request.getParameter("action"), "getDataFileSheets")) {
            logger.info("This is the action getDataFileSheets");
            if (dataImporterForm.getDataFile() == null || dataImporterForm.getDataFile().getFileSize() == 0) {
                response.setStatus(400);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"No file provided\"}");
                return null;
            }
            String fileType = request.getParameter("fileType");
            if (!Objects.equals(fileType, "excel")) {
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getWriter(), Collections.emptyList());
                return null;
            }
            List<String> sheetNames = new ArrayList<>();
              try (InputStream is = dataImporterForm.getDataFile().getInputStream();
                  Workbook workbook = ImporterUtil.openWorkbookWithStrictFallback(is)) {
                int n = workbook.getNumberOfSheets();
                for (int i = 0; i < n; i++) {
                    sheetNames.add(workbook.getSheetAt(i).getSheetName());
                }
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), sheetNames);
            return null;
        }

        if (Objects.equals(request.getParameter("action"), "uploadDataFile")) {
            logger.info("This is the action " + request.getParameter("action"));
            Instant start = Instant.now();
            String fileName = dataImporterForm.getDataFile().getFileName();
            String tempDirPath = System.getProperty("java.io.tmpdir");
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            String tempFilePath = tempDirPath + File.separator + fileName;
            try (InputStream inputStream = dataImporterForm.getDataFile().getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFilePath)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // Check if the file is readable and has correct content
            File tempFile = new File(tempFilePath);
            List<ImportedFilesRecord> similarFiles = ImportedFileUtil.getSimilarFiles(tempFile);
            if (similarFiles != null && !similarFiles.isEmpty()) {
                for (ImportedFilesRecord similarFilesRecord : similarFiles) {
                    logger.info("Similar file: " + similarFilesRecord);
                    if (similarFilesRecord.getImportStatus().equals(ImportStatus.IN_PROGRESS)) {
                        response.setHeader("errorMessage", "You have a similar file in progress. Please try again later.");
                        response.setStatus(400);
                        return mapping.findForward("importData");
                    }
                }
            }
            // Resolve which config to use: saved config by name (from load/edit) or form's column pairs
            String existingConfig = request.getParameter("existingConfig");
            String configNameParam = request.getParameter("configName");
            String configNameToUse = (configNameParam != null && !configNameParam.trim().isEmpty())
                    ? configNameParam.trim()
                    : (existingConfig != null && !existingConfig.isEmpty() && !"0".equals(existingConfig) && !"1".equals(existingConfig) ? existingConfig.trim() : null);
            Map<String, String> columnPairsToUse = (configNameToUse != null)
                    ? getConfigByName(configNameToUse)
                    : dataImporterForm.getColumnPairs();

            if (columnPairsToUse.isEmpty() || (!columnPairsToUse.containsValue(ImporterConstants.PROJECT_TITLE) && !columnPairsToUse.containsValue(ImporterConstants.PROJECT_CODE))) {
                response.setHeader("errorMessage", "You must have at least the 'Project Title' or 'Project Code' column in your config.");
                response.setStatus(400);
                return mapping.findForward("importData");
            }
            if (!isFileReadable(tempFile) || !isFileContentValid(tempFile)) {
                // Handle invalid file
                logger.error("Invalid file or content.");
                response.setHeader("errorMessage", "Unable to parse the file. Please check the file format/content and try again.");
                response.setStatus(400);
                return mapping.findForward("importData");

            } else {
                logger.info("Existing configuration: {}", existingConfig);
                if (configNameToUse == null) {
                    saveImportConfig(request, fileName, dataImporterForm.getColumnPairs());
                }

                int res = 0;
                ImportedFilesRecord importedFilesRecord = ImportedFileUtil.saveFile(tempFile, fileName);
                logger.info("Saved file record: {}",importedFilesRecord);
                boolean isInternal= dataImporterForm.isInternal();
                boolean skipExisting = dataImporterForm.isSkipExisting();
                boolean validateActivities = dataImporterForm.isValidateActivities();
                boolean addDisbursementForCommitment = dataImporterForm.isAddDisbursementForCommitment();
                boolean skipRecordsWithoutTransactions = dataImporterForm.isSkipRecordsWithoutTransactions();
                boolean createMissingOrgs = dataImporterForm.isCreateMissingOrgs();
                boolean createMissingSectors = dataImporterForm.isCreateMissingSectors();
                boolean createMissingOrgGroups = dataImporterForm.isCreateMissingOrgGroups();
                boolean createMissingPrograms = dataImporterForm.isCreateMissingPrograms();
                boolean replaceExistingTransactions = dataImporterForm.isReplaceExistingTransactions();
                boolean replaceExistingLocations = dataImporterForm.isReplaceExistingLocations();
                Long orgGroupId = dataImporterForm.getOrgGroupId();
                Long defaultActivityStatusId = dataImporterForm.getDefaultActivityStatusId();
                Long defaultRecordingOrganizationId = dataImporterForm.getDefaultRecordingOrganizationId();
                Long defaultLocationId = dataImporterForm.getDefaultLocationId();
                String defaultProgramClassification = dataImporterForm.getDefaultProgramClassification();
                logger.info("Internal: "+ isInternal);
                logger.info("Skip existing: "+ skipExisting);
                logger.info("Validate activities: "+ validateActivities);
                logger.info("Add disbursement for commitment: "+ addDisbursementForCommitment);
                logger.info("Skip records without transactions: " + skipRecordsWithoutTransactions);
                logger.info("Create missing orgs: "+ createMissingOrgs);
                logger.info("Create missing sectors: {}", createMissingSectors);
                logger.info("Create missing org groups: " + createMissingOrgGroups);
                logger.info("Create missing programs: " + createMissingPrograms);
                logger.info("Replace existing transactions: " + replaceExistingTransactions);
                logger.info("Replace existing locations: " + replaceExistingLocations);
                logger.info("Org group id: "+ orgGroupId);
                logger.info("Default activity status id: {}", defaultActivityStatusId);
                logger.info("Default recording organization id: {}", defaultRecordingOrganizationId);
                logger.info("Default location id: {}", defaultLocationId);
                logger.info("Default program classification: {}", defaultProgramClassification);
                boolean hasGenericOrgGroupMapping = columnPairsToUse.containsValue(ImporterConstants.ORG_GROUP);
                boolean donorMissingGroupMapping = columnPairsToUse.containsValue(ImporterConstants.DONOR_AGENCY)
                    && !hasGenericOrgGroupMapping
                    && !columnPairsToUse.containsValue(ImporterConstants.DONOR_ORGANIZATION_GROUP);
                boolean responsibleMissingGroupMapping = columnPairsToUse.containsValue(ImporterConstants.RESPONSIBLE_ORGANIZATION)
                    && !hasGenericOrgGroupMapping
                    && !columnPairsToUse.containsValue(ImporterConstants.RESPONSIBLE_ORGANIZATION_GROUP);
                boolean beneficiaryMissingGroupMapping = columnPairsToUse.containsValue(ImporterConstants.BENEFICIARY_AGENCY)
                    && !hasGenericOrgGroupMapping
                    && !columnPairsToUse.containsValue(ImporterConstants.BENEFICIARY_AGENCY_GROUP);
                boolean executingMissingGroupMapping = columnPairsToUse.containsValue(ImporterConstants.EXECUTING_AGENCY)
                    && !hasGenericOrgGroupMapping
                    && !columnPairsToUse.containsValue(ImporterConstants.EXECUTING_AGENCY_GROUP);
                boolean implementingMissingGroupMapping = columnPairsToUse.containsValue(ImporterConstants.IMPLEMENTING_AGENCY)
                    && !hasGenericOrgGroupMapping
                    && !columnPairsToUse.containsValue(ImporterConstants.IMPLEMENTING_AGENCY_GROUP);
                boolean contractingMissingGroupMapping = columnPairsToUse.containsValue(ImporterConstants.CONTRACTING_AGENCY)
                    && !hasGenericOrgGroupMapping
                    && !columnPairsToUse.containsValue(ImporterConstants.CONTRACTING_AGENCY_GROUP);
                boolean anyMappedOrgMissingGroup = donorMissingGroupMapping
                    || responsibleMissingGroupMapping
                    || beneficiaryMissingGroupMapping
                    || executingMissingGroupMapping
                    || implementingMissingGroupMapping
                    || contractingMissingGroupMapping;
                if (createMissingOrgs && orgGroupId == null && !createMissingOrgGroups
                    && anyMappedOrgMissingGroup) {
                    response.setHeader("errorMessage",
                        "Please select a fallback Organization Group (or enable organization-group creation) when any mapped organization field has no corresponding group mapping.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }
                if (isInternal) {
                    columnPairsToUse = new HashMap<>(columnPairsToUse);
                    columnPairsToUse.put("Donor Agency", "Donor Agency");
                }
                if (!columnPairsToUse.containsValue(ImporterConstants.PROJECT_LOCATION)
                        && defaultLocationId == null) {
                    response.setHeader("errorMessage", "Please select a fallback location when no 'Project Location' column is mapped.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }
                if (columnPairsToUse.containsValue(ImporterConstants.PROGRAM_NAME)
                        && !columnPairsToUse.containsValue(ImporterConstants.PROGRAM_CLASSIFICATION)
                        && (defaultProgramClassification == null || defaultProgramClassification.trim().isEmpty())) {
                    response.setHeader("errorMessage", "Please select a default program classification when no 'Program Classification' column is mapped.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }
                if (columnPairsToUse.containsValue(ImporterConstants.ACTIVITY_INTERNAL_ID)
                        && !columnPairsToUse.containsValue(ImporterConstants.RECORDING_ORGANIZATION)
                        && defaultRecordingOrganizationId == null) {
                    response.setHeader("errorMessage", "Please select a default recording organization when 'Activity Internal ID' is mapped without 'Recording Organization'.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }
                logger.info("Configuration: {}", columnPairsToUse);
                try {
                    if ((Objects.equals(request.getParameter("fileType"), "excel") || Objects.equals(request.getParameter("fileType"), "csv"))) {
                        String dataSheetChoice = request.getParameter("dataSheetChoice");
                        String dataSheetName = request.getParameter("dataSheetName");
                        boolean useSpecificSheet = "sheet".equals(dataSheetChoice) && dataSheetName != null && !dataSheetName.trim().isEmpty();
                        res = processExcelFileInBatches(importedFilesRecord, tempFile, request, columnPairsToUse, isInternal, skipExisting, useSpecificSheet ? dataSheetName : null, createMissingOrgs, createMissingSectors, orgGroupId, createMissingOrgGroups, skipRecordsWithoutTransactions, validateActivities, addDisbursementForCommitment, defaultActivityStatusId, defaultRecordingOrganizationId, defaultLocationId, defaultProgramClassification, createMissingPrograms, replaceExistingTransactions, replaceExistingLocations);
                    } else if ( Objects.equals(request.getParameter("fileType"), "text")) {
                        res = TxtDataImporter.processTxtFileInBatches(importedFilesRecord, tempFile, request, columnPairsToUse, isInternal, skipExisting, createMissingOrgs, createMissingSectors, orgGroupId, createMissingOrgGroups, skipRecordsWithoutTransactions, validateActivities, addDisbursementForCommitment, defaultActivityStatusId, defaultRecordingOrganizationId, defaultLocationId, defaultProgramClassification, createMissingPrograms, replaceExistingTransactions, replaceExistingLocations);
                    }
                } catch (Exception e) {
                    ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.FAILED);
                    throw e;
                } finally {
                    Instant finish = Instant.now();
                    long timeElapsedMillis = Duration.between(start, finish).toMillis();
                    ImportedFileUtil.updateFileProcessingTime(importedFilesRecord, timeElapsedMillis);
                    long minutes = timeElapsedMillis / 60000;
                    long seconds = (timeElapsedMillis % 60000) / 1000;
                    logger.info("Time Elapsed: " + minutes + "m " + seconds + "s");
                }
                if (res != 1) {
                    // Handle error
                    logger.info("Error processing file  " + tempFile);
                    ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.FAILED);
                    response.setHeader("errorMessage", "Unable to parse the file. Please check the file format/content and try again.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }


                // Clean up
                ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.SUCCESS);
                Files.delete(tempFile.toPath());
                logger.info("Cache map size: " + ConstantsMap.size());
                ConstantsMap.clear();
                logger.info("File path is " + tempFilePath + " and size is " + tempFile.length() / (1024 * 1024) + " mb");
                logger.info("Start time: " + start);

                // Send response
                response.setHeader("updatedMap", "");
                dataImporterForm.getColumnPairs().clear();
            }
            return null;
        }

        return mapping.findForward("importData");
    }
    private static List<String> getConfigNames()
    {
        Session session = PersistenceManager.getRequestDBSession();

        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT c.configName FROM DataImporterConfig c";
        Query query = session.createQuery(hql);

        List< String> configNames = query.list();
        return configNames==null?Collections.emptyList():configNames;

    }

    private static List<AmpCategoryValueLocations> getAvailableLocations() {
        return LocationUtil.getAllCountriesAndRegions().stream()
                .filter(Objects::nonNull)
                .filter(location -> !location.isSoftDeleted())
                .sorted(Comparator.comparing(AmpCategoryValueLocations::getHierarchicalName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    private static Map<String, String> getConfigByName(String configName) {
        logger.info("Getting import config for configName: {}", configName);
        Session session = PersistenceManager.getRequestDBSession();
        Map<String, String> configValues = new HashMap<>();
        // Query DataImporterConfigValues directly to bypass Hibernate first-level cache
        // (querying through the parent entity's collection returns stale cached results
        // when new pairs were added in the same session via addColumnPairToConfig)
        String hql = "SELECT cv.configKey, cv.configValue FROM "
                + DataImporterConfigValues.class.getName()
                + " cv WHERE cv.dataImporterConfig.configName = :configName";
        Query query = session.createQuery(hql);
        query.setParameter("configName", configName, StringType.INSTANCE);
        List<Object[]> rows = query.list();
        logger.info("Config rows found for '{}': {}", configName, rows.size());
        for (Object[] row : rows) {
            configValues.put((String) row[0], (String) row[1]);
        }
        return configValues;
    }

    private static DataImporterConfig getConfigEntityByName(String configName) {
        Session session = PersistenceManager.getRequestDBSession();
        String hql = "FROM DataImporterConfig WHERE configName = :configName";
        Query query = session.createQuery(hql);
        query.setParameter("configName", configName, StringType.INSTANCE);
        query.setMaxResults(1);
        List<DataImporterConfig> list = query.list();
        return list.isEmpty() ? null : list.get(0);
    }

    private static void addColumnPairToConfig(String configName, String columnName, String selectedField) {
        DataImporterConfig config = getConfigEntityByName(configName);
        if (config == null) {
            logger.warn("Config not found for name: {}", configName);
            return;
        }
        Session session = PersistenceManager.getRequestDBSession();
        DataImporterConfigValues cv = new DataImporterConfigValues(columnName, selectedField, config);
        session.save(cv);
        config.getConfigValues().add(cv);
        session.flush();
    }

    private static void removeColumnPairFromConfig(String configName, String columnName) {
        DataImporterConfig config = getConfigEntityByName(configName);
        if (config == null) {
            logger.warn("Config not found for name: {}", configName);
            return;
        }
        DataImporterConfigValues toRemove = null;
        for (DataImporterConfigValues v : config.getConfigValues()) {
            if (columnName.equals(v.getConfigKey())) {
                toRemove = v;
                break;
            }
        }
        if (toRemove != null) {
            config.getConfigValues().remove(toRemove);
            Session session = PersistenceManager.getRequestDBSession();
            session.delete(toRemove);
            session.flush();
        }
    }

    public static void saveImportConfig(HttpServletRequest request, String fileName, Map<String, String> config) {
        logger.info("Saving import config");

        try (Connection connection = PersistenceManager.getJdbcConnection()) {
            connection.setAutoCommit(false); // Start transaction

            String configName = fileName + "_" + LocalDateTime.now().toString().replace(":", "_");

            if (request.getParameter("configName") != null) {
                configName = request.getParameter("configName");

                // Check if configName already exists
                String checkSql = "SELECT COUNT(*) FROM DATA_IMPORTER_CONFIG WHERE config_name = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                    checkStmt.setString(1, configName);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        configName += "_" + LocalDateTime.now().toString().replace(":", "_");
                    }
                }
            }

            // Insert into DataImporterConfig
            String insertConfigSql = "INSERT INTO DATA_IMPORTER_CONFIG (id,config_name) VALUES (nextval('DATA_IMPORTER_CONFIG_SEQ'),?) RETURNING id";
            long configId;

            try (PreparedStatement insertConfigStmt = connection.prepareStatement(insertConfigSql)) {
                insertConfigStmt.setString(1, configName);
                ResultSet rs = insertConfigStmt.executeQuery();
                if (rs.next()) {
                    configId = rs.getLong(1);
                } else {
                    throw new SQLException("Failed to insert into DATA_IMPORTER_CONFIG");
                }
            }

            // Insert into DataImporterConfigValues
            String insertValuesSql = "INSERT INTO DATA_IMPORTER_CONFIG_VALUES (id,data_importer_config_id, config_key, config_value) VALUES (nextval('DATA_IMPORTER_CONFIG_VALUES_SEQ'),?, ?, ?)";
            try (PreparedStatement insertValuesStmt = connection.prepareStatement(insertValuesSql)) {
                for (Map.Entry<String, String> entry : config.entrySet()) {
                    insertValuesStmt.setLong(1, configId);
                    insertValuesStmt.setString(2, entry.getKey());
                    insertValuesStmt.setString(3, entry.getValue());
                    insertValuesStmt.addBatch();
                }
                insertValuesStmt.executeBatch();
            }

            connection.commit(); // Commit transaction
            logger.info("Saved configuration: {}", configName);

        } catch (SQLException e) {
            logger.error("Error saving import config: {}", e.getMessage(), e);
            throw new RuntimeException("Database error while saving import config.", e);
        }
    }






    private Map<String, String> getEntityFieldsInfo() {
        List<String> fieldsInfos = new ArrayList<>();
        fieldsInfos.add(ImporterConstants.PROJECT_TITLE);
        fieldsInfos.add(ImporterConstants.PROJECT_CODE);
        fieldsInfos.add(ImporterConstants.OBJECTIVE);
        fieldsInfos.add(ImporterConstants.PROJECT_DESCRIPTION);
        fieldsInfos.add(ImporterConstants.PRIMARY_SECTOR);
        fieldsInfos.add(ImporterConstants.SECONDARY_SECTOR);
        fieldsInfos.add(ImporterConstants.PROJECT_LOCATION);
        fieldsInfos.add(ImporterConstants.PROJECT_START_DATE);
        fieldsInfos.add(ImporterConstants.PROPOSED_PROJECT_START_DATE);
        fieldsInfos.add(ImporterConstants.PROPOSED_PROJECT_END_DATE);
        fieldsInfos.add(ImporterConstants.PROJECT_AGREEMENT_SIGN_DATE);
        fieldsInfos.add(ImporterConstants.PROJECT_END_DATE);
        fieldsInfos.add(ImporterConstants.DONOR_AGENCY);
        fieldsInfos.add(ImporterConstants.EXCHANGE_RATE);
        fieldsInfos.add(ImporterConstants.DONOR_AGENCY_CODE);
        fieldsInfos.add(ImporterConstants.ORG_GROUP);
        fieldsInfos.add(ImporterConstants.DONOR_ORGANIZATION_GROUP);
        fieldsInfos.add(ImporterConstants.RESPONSIBLE_ORGANIZATION);
        fieldsInfos.add(ImporterConstants.RESPONSIBLE_ORGANIZATION_GROUP);
        fieldsInfos.add(ImporterConstants.RESPONSIBLE_ORGANIZATION_CODE);
        fieldsInfos.add(ImporterConstants.EXECUTING_AGENCY);
        fieldsInfos.add(ImporterConstants.EXECUTING_AGENCY_GROUP);
        fieldsInfos.add(ImporterConstants.IMPLEMENTING_AGENCY);
        fieldsInfos.add(ImporterConstants.IMPLEMENTING_AGENCY_GROUP);
        fieldsInfos.add(ImporterConstants.BENEFICIARY_AGENCY_GROUP);
        fieldsInfos.add(ImporterConstants.CONTRACTING_AGENCY_GROUP);
        fieldsInfos.add(ImporterConstants.ACTUAL_DISBURSEMENT);
        fieldsInfos.add(ImporterConstants.ACTUAL_COMMITMENT);
        fieldsInfos.add(ImporterConstants.ACTUAL_EXPENDITURE);
        fieldsInfos.add(ImporterConstants.PLANNED_DISBURSEMENT);
        fieldsInfos.add(ImporterConstants.PLANNED_COMMITMENT);
        fieldsInfos.add(ImporterConstants.PLANNED_EXPENDITURE);
        fieldsInfos.add(ImporterConstants.TRANSACTION_AMOUNT);
        fieldsInfos.add(ImporterConstants.MEASURE_TYPE);
        fieldsInfos.add(ImporterConstants.TRANSACTION_DATE);
        fieldsInfos.add(ImporterConstants.FINANCING_INSTRUMENT);
        fieldsInfos.add(ImporterConstants.TYPE_OF_ASSISTANCE);
        fieldsInfos.add(ImporterConstants.SECONDARY_SUBSECTOR);
        fieldsInfos.add(ImporterConstants.PRIMARY_SUBSECTOR);
        fieldsInfos.add(ImporterConstants.CURRENCY);
        fieldsInfos.add(ImporterConstants.COMPONENT_NAME);
        fieldsInfos.add(ImporterConstants.COMPONENT_CODE);
        fieldsInfos.add(ImporterConstants.BENEFICIARY_AGENCY);
        fieldsInfos.add(ImporterConstants.PROJECT_STATUS);
        fieldsInfos.add(ImporterConstants.PROCUREMENT_SYSTEM);
        fieldsInfos.add(ImporterConstants.ACTIVITY_INTERNAL_ID);
        fieldsInfos.add(ImporterConstants.RECORDING_ORGANIZATION);
        // Indicator columns for M&E import
        fieldsInfos.add(ImporterConstants.INDICATOR_NAME);
        fieldsInfos.add(ImporterConstants.PROGRAM_NAME);
        fieldsInfos.add(ImporterConstants.PROGRAM_CLASSIFICATION);
        fieldsInfos.add(ImporterConstants.PRIMARY_PROGRAM);
        fieldsInfos.add(ImporterConstants.SECONDARY_PROGRAM);
        fieldsInfos.add(ImporterConstants.TERTIARY_PROGRAM);
        fieldsInfos.add(ImporterConstants.NATIONAL_PLAN_OBJECTIVE);
        fieldsInfos.add(ImporterConstants.INDICATOR_LOCATION);
        fieldsInfos.add(ImporterConstants.ORIGINAL_BASE_VALUE);
        fieldsInfos.add(ImporterConstants.ORIGINAL_BASE_VALUE_DATE);
        fieldsInfos.add(ImporterConstants.REVISED_BASE_VALUE);
        fieldsInfos.add(ImporterConstants.REVISED_BASE_VALUE_DATE);
        fieldsInfos.add(ImporterConstants.ORIGINAL_TARGET_VALUE);
        fieldsInfos.add(ImporterConstants.ORIGINAL_TARGET_VALUE_DATE);
        fieldsInfos.add(ImporterConstants.REVISED_TARGET_VALUE);
        fieldsInfos.add(ImporterConstants.REVISED_TARGET_VALUE_DATE);
        fieldsInfos.add(ImporterConstants.ACTUAL_VALUE);
        fieldsInfos.add(ImporterConstants.ACTUAL_VALUE_DATE);
        fieldsInfos.add(ImporterConstants.UNIT_OF_MEASURE);

        // Create map of original field names to translated field names
        Map<String, String> fieldMap = new LinkedHashMap<>();
        for (String field : fieldsInfos) {
            String translated = org.digijava.kernel.translator.TranslatorWorker.translateText(field);
            fieldMap.put(field, translated);

        }


        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    private List<AmpCategoryValue> getActivityStatuses() {
        List<AmpCategoryValue> activityStatuses = new ArrayList<>(
                CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY));
        activityStatuses.sort(Comparator
                .comparing(AmpCategoryValue::getIndex, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(AmpCategoryValue::getValue, String.CASE_INSENSITIVE_ORDER));
        return activityStatuses;
    }

    private List<String> getProgramClassificationNames() {
        List<AmpActivityProgramSettings> settings = ProgramUtil.getEnabledProgramSettings();
        if (settings == null || settings.isEmpty()) {
            settings = ProgramUtil.getAmpActivityProgramSettingsList(true);
        }
        return settings.stream()
                .filter(Objects::nonNull)
                .map(AmpActivityProgramSettings::getName)
                .filter(Objects::nonNull)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    private List<AmpOrganisation> getRecordingOrganizationsForImporter() {
        List<AmpOrganisation> organizations = OrganisationUtil.getAllOrganisations();
        if (organizations == null) {
            organizations = new ArrayList<>();
        }

        return organizations.stream()
                .filter(Objects::nonNull)
                .filter(org -> org.getDeleted() == null || !org.getDeleted())
                .sorted((left, right) -> {
                    String leftName = left.getName();
                    String rightName = right.getName();
                    return Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER).compare(leftName, rightName);
                })
                .collect(Collectors.toList());
    }

}
