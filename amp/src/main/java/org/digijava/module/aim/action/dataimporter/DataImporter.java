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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.ConstantsMap;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.isFileContentValid;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.isFileReadable;
import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.removeMapItem;
import org.digijava.module.aim.form.DataImporterForm;
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

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // List of fields
        List<String> fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo", fieldsInfo);
        List<String> configNames= getConfigNames();
        request.setAttribute("configNames", configNames);
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
                dataImporterForm.getColumnPairs().clear();

                if (request.getParameter("fileType") != null) {
                    InputStream fileInputStream = dataImporterForm.getTemplateFile().getInputStream();
                    if (Objects.equals(request.getParameter("fileType"), "excel")) {
                        // Excel: return sheet names and columns per sheet for template configuration
                        List<String> sheetNames = new ArrayList<>();
                        Map<String, List<String>> columnsBySheet = new HashMap<>();
                        try (Workbook workbook = new XSSFWorkbook(fileInputStream)) {
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
                dataImporterForm.getColumnPairs().put(columnName, selectedField);
                logger.info("Column Pairs:" + dataImporterForm.getColumnPairs());

                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(dataImporterForm.getColumnPairs());

                // Send response
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.setCharacterEncoding("UTF-8");

                return null;

            }


            if (Objects.equals(request.getParameter("action"), "removeField")) {
                logger.info(" this is the action " + request.getParameter("action"));

                String columnName = request.getParameter("columnName");
                String selectedField = request.getParameter("selectedField");
                dataImporterForm.getColumnPairs().put(columnName, selectedField);
                removeMapItem(dataImporterForm.getColumnPairs(), columnName, selectedField);
                logger.info("Column Pairs:" + dataImporterForm.getColumnPairs());

                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(dataImporterForm.getColumnPairs());

                // Send response
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
                     Workbook workbook = new XSSFWorkbook(is)) {
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
                if (dataImporterForm.getColumnPairs().isEmpty() ||(!dataImporterForm.getColumnPairs().containsValue("Project Title") && !dataImporterForm.getColumnPairs().containsValue("Project Code"))) {

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
                    // Proceed with processing the file
                    String existingConfig = request.getParameter("existingConfig");
                    logger.info("Existing configuration: {}",existingConfig);
                    if (!Objects.equals(existingConfig, "1")) {
                        saveImportConfig(request, fileName, dataImporterForm.getColumnPairs());
                    }

                    int res = 0;
                    ImportedFilesRecord importedFilesRecord = ImportedFileUtil.saveFile(tempFile, fileName);
                    logger.info("Saved file record: {}",importedFilesRecord);
                    boolean isInternal= dataImporterForm.isInternal();
                    logger.info("Internal: "+ isInternal);
                    if (isInternal)
                    {
                        dataImporterForm.getColumnPairs().put("Donor Agency", "Donor Agency");
                    }
                    logger.info("Configuration"+ dataImporterForm.getColumnPairs());
                    if ((Objects.equals(request.getParameter("fileType"), "excel") || Objects.equals(request.getParameter("fileType"), "csv"))) {
                        String dataSheetChoice = request.getParameter("dataSheetChoice");
                        String dataSheetName = request.getParameter("dataSheetName");
                        boolean useSpecificSheet = "sheet".equals(dataSheetChoice) && dataSheetName != null && !dataSheetName.trim().isEmpty();
                        // Process the file in batches
                         res = processExcelFileInBatches(importedFilesRecord, tempFile, request, dataImporterForm.getColumnPairs(), isInternal, useSpecificSheet ? dataSheetName : null);
                    } else if ( Objects.equals(request.getParameter("fileType"), "text")) {
                        res=TxtDataImporter.processTxtFileInBatches(importedFilesRecord, tempFile, request, dataImporterForm.getColumnPairs(), isInternal);
                    }
                    if (res != 1) {
                        // Handle error
                        logger.info("Error processing file  " + tempFile);
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
                    Instant finish = Instant.now();
                    long timeElapsed = Duration.between(start, finish).toMillis();
                    logger.info("Time Elapsed: " + timeElapsed);

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

    private static Map<String, String> getConfigByName(String configName) {
        logger.info("Getting import config for configName: {}", configName);
        Session session = PersistenceManager.getRequestDBSession();
        Map<String, String> configValues = new HashMap<>();

            String hql = "FROM DataImporterConfig WHERE configName = :configName";
            Query query = session.createQuery(hql);
            query.setParameter("configName", configName, StringType.INSTANCE);
            query.setMaxResults(1);

            List<DataImporterConfig> resultList = query.list();
            logger.info("Configs found: {}",resultList);


            if (!resultList.isEmpty()) {
                Set<DataImporterConfigValues> values = resultList.get(0).getConfigValues();
                logger.info("Config Values found: {}",values);

                if (!values.isEmpty())
                {
                    values.forEach(value-> configValues.put(value.getConfigKey(),value.getConfigValue()));
                }
            }

        return configValues;
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






    private List<String> getEntityFieldsInfo() {
        List<String> fieldsInfos = new ArrayList<>();
        fieldsInfos.add("Project Title");
        fieldsInfos.add("Project Code");
        fieldsInfos.add("Objective");
        fieldsInfos.add("Project Description");
        fieldsInfos.add("Primary Sector");
        fieldsInfos.add("Secondary Sector");
        fieldsInfos.add("Project Location");
        fieldsInfos.add("Project Start Date");
        fieldsInfos.add("Project End Date");
        fieldsInfos.add("Donor Agency");
        fieldsInfos.add("Exchange Rate");
        fieldsInfos.add("Donor Agency Code");
        fieldsInfos.add("Responsible Organization");
        fieldsInfos.add("Responsible Organization Code");
        fieldsInfos.add("Executing Agency");
        fieldsInfos.add("Implementing Agency");
        fieldsInfos.add("Actual Disbursement");
        fieldsInfos.add("Actual Commitment");
        fieldsInfos.add("Actual Expenditure");
        fieldsInfos.add("Planned Disbursement");
        fieldsInfos.add("Planned Commitment");
        fieldsInfos.add("Planned Expenditure");
        fieldsInfos.add("Funding Item");
        fieldsInfos.add("Transaction Date");
        fieldsInfos.add("Financing Instrument");
        fieldsInfos.add("Type Of Assistance");
        fieldsInfos.add("Secondary Subsector");
        fieldsInfos.add("Primary Subsector");
        fieldsInfos.add("Currency");
        fieldsInfos.add("Component Name");
        fieldsInfos.add("Component Code");
        fieldsInfos.add("Beneficiary Agency");
        // Indicator columns for M&E import
        fieldsInfos.add("Indicator Name");
        fieldsInfos.add("Program Name");
        fieldsInfos.add("Location");
        fieldsInfos.add("Original Base Value");
        fieldsInfos.add("Original Base Value Date");
        fieldsInfos.add("Revised Base Value");
        fieldsInfos.add("Revised Base Value Date");
        fieldsInfos.add("Original Target Value");
        fieldsInfos.add("Original Target Value Date");
        fieldsInfos.add("Revised Target Value");
        fieldsInfos.add("Revised Target Value Date");
        fieldsInfos.add("Actual Value");
        fieldsInfos.add("Actual Value Date");
        fieldsInfos.add("Unit of Measure");
        return fieldsInfos.stream().sorted().collect(Collectors.toList());
    }

}
