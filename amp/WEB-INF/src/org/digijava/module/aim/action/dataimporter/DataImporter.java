package org.digijava.module.aim.action.dataimporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.ImportStatus;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;
import org.digijava.module.admin.dbentity.ImportedProject;
import org.digijava.module.admin.util.ImportedFileUtil;
import org.digijava.module.admin.util.model.ImportDataModel;
import org.digijava.module.aim.form.DataImporterForm;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.aim.action.dataimporter.ImporterUtil.*;

public class DataImporter extends Action {
    static Logger logger = LoggerFactory.getLogger(DataImporter.class);
    private static final int BATCH_SIZE = 1000;


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // List of fields
        List<String> fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo", fieldsInfo);
        DataImporterForm dataImporterForm = (DataImporterForm) form;


        if (Objects.equals(request.getParameter("action"), "uploadTemplate")) {
            logger.info(" this is the action " + request.getParameter("action"));
            if (request.getParameter("uploadTemplate") != null) {
                logger.info(" this is the action " + request.getParameter("uploadTemplate"));
                Set<String> headersSet = new HashSet<>();

                if (request.getParameter("fileType") != null) {
                    InputStream fileInputStream = dataImporterForm.getTemplateFile().getInputStream();
                    if ((Objects.equals(request.getParameter("fileType"), "excel") || Objects.equals(request.getParameter("fileType"), "csv"))) {
                        Workbook workbook = new XSSFWorkbook(fileInputStream);
                        int numberOfSheets = workbook.getNumberOfSheets();
                        for (int i = 0; i < numberOfSheets; i++) {
                            Sheet sheet = workbook.getSheetAt(i);
                            Row headerRow = sheet.getRow(0);
                            Iterator<Cell> cellIterator = headerRow.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                headersSet.add(cell.getStringCellValue());
                            }

                        }
                        workbook.close();


                    } else if (Objects.equals(request.getParameter("fileType"), "text")) {
                        CSVParser parser = new CSVParserBuilder().withSeparator(request.getParameter("dataSeparator").charAt(0)).build();

                        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(fileInputStream)).withCSVParser(parser).build()) {
                            String[] headers = reader.readNext(); // Read the first line which contains headers

                            if (headers != null) {
                                // Print each header
                                headersSet.addAll(Arrays.asList(headers));
                            } else {
                                logger.info("File is empty or does not contain headers.");
                            }
                        } catch (IOException | CsvValidationException e) {
                            logger.error("An error occurred during extraction of headers.",e);
                        }

                    }
                }
                headersSet = headersSet.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
                StringBuilder headers = new StringBuilder();
                headers.append("  <label for=\"columnName\">Select Column Name:</label>\n<select  class=\"select2\" style=\"width: 300px;\" id=\"columnName\">");
                for (String option : headersSet) {
                    headers.append("<option>").append(option).append("</option>");
                }
                headers.append("</select>");
                response.setHeader("selectTag", headers.toString());

                response.setHeader("updatedMap", "");

                dataImporterForm.getColumnPairs().clear();

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

                // Send JSON response
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

                // Send JSON response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
//            response.setHeader("updatedMap",json);
                response.getWriter().write(json);

                return null;

            }

            if (Objects.equals(request.getParameter("action"), "uploadDataFile")) {
                logger.info("This is the action " + request.getParameter("action"));
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
                if (dataImporterForm.getColumnPairs().isEmpty() || !dataImporterForm.getColumnPairs().containsValue("{projectTitle}")) {
                    response.setHeader("errorMessage", "You must have atleast the {projectTitle} key in your config.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }
                if (!isFileReadable(tempFile) || !isFileContentValid(tempFile)) {
                    // Handle invalid file
                    logger.error("Invalid file or content.");
                    response.setHeader("errorMessage", "Unable to parse the file. Please check the file format/content and try again.");
                    response.setStatus(400);
                    return mapping.findForward("importData");

                    // Optionally, you can respond with an error message to the client.
                } else {
                    // Proceed with processing the file
                    int res = 0;
                    ImportedFilesRecord importedFilesRecord = ImportedFileUtil.saveFile(tempFile, fileName);
                    if ((Objects.equals(request.getParameter("fileType"), "excel") || Objects.equals(request.getParameter("fileType"), "csv"))) {

                        // Process the file in batches
                         res = processExcelFileInBatches(importedFilesRecord, tempFile, request, dataImporterForm.getColumnPairs());
                    } else if ( Objects.equals(request.getParameter("fileType"), "text")) {
                        res=TxtDataImporter.processTxtFileInBatches(importedFilesRecord, tempFile, request, dataImporterForm.getColumnPairs());
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
                    Instant start = Instant.now();
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



    public int processExcelFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config) {
        // Open the workbook
        int res=0;
        ImportedFileUtil.updateFileStatus(importedFilesRecord, ImportStatus.IN_PROGRESS);
        try (Workbook workbook = new XSSFWorkbook(file)) {
            int numberOfSheets = workbook.getNumberOfSheets();
            logger.info("Number of sheets: " + numberOfSheets);

            // Process each sheet in the workbook
            for (int i = 0; i < numberOfSheets; i++) {
                logger.info("Sheet number: " + i);
                Sheet sheet = workbook.getSheetAt(i);
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


    private void processSheetInBatches(Sheet sheet, HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord) throws JsonProcessingException {
        // Get the number of rows in the sheet
        int rowCount = sheet.getPhysicalNumberOfRows();
        logger.info("Total number of rows: " + rowCount);

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

    private void processBatch(List<Row> batch,Sheet sheet, HttpServletRequest request, Map<String, String> config, ImportedFilesRecord importedFilesRecord) throws JsonProcessingException {
        // Process the batch of rows
        SessionUtil.extendSessionIfNeeded(request);
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        for (Row row : batch) {
            ImportedProject importedProject= new ImportedProject();
            importedProject.setImportedFilesRecord(importedFilesRecord);

            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setCreated_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setTeam(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam().getAmpTeamId());
            importDataModel.setIs_draft(true);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            importDataModel.setCreation_date(now.format(formatter));
            setStatus(importDataModel, session);

            logger.info("Row Number: "+row.getRowNum()+", Sheet Name: "+sheet.getSheetName());

                for (Map.Entry<String, String> entry : config.entrySet()) {
                    int columnIndex = getColumnIndexByName(sheet, entry.getKey());
                    if (columnIndex >= 0) {
                        Cell cell = row.getCell(columnIndex);
                        switch (entry.getValue()) {
                            case "{projectTitle}":
                                importDataModel.setProject_title(cell.getStringCellValue().trim());
                                break;
                            case "{projectDescription}":
                                importDataModel.setDescription(cell.getStringCellValue().trim());
                                break;
                            case "{projectLocation}":
//                        ampActivityVersion.addLocation(new AmpActivityLocation());
                                break;
                            case "{primarySector}":
                                updateSectors(importDataModel, cell.getStringCellValue().trim(), session, true);
                                break;
                            case "{secondarySector}":
                                updateSectors(importDataModel, cell.getStringCellValue().trim(), session, false);
                                break;
                            case "{donorAgency}":
                                updateOrgs(importDataModel, cell.getStringCellValue().trim(), session, "donor");
                                break;
                            case "{fundingItem}":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell,true,true, "Actual");
                                break;
                            case "{plannedCommitment}":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell,true,false, "Planned");
                                break;
                            case "{plannedDisbursement}":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell,false,true, "Planned");
                                break;
                            case "{actualCommitment}":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell,true,false, "Actual");
                                break;
                            case "{actualDisbursement}":
                                setAFundingItemForExcel(sheet, config, row, entry, importDataModel, session, cell,false,true, "Actual");
                                break;
                            default:
                                logger.error("Unexpected value: " + entry.getValue());
                                break;
                        }



                }
            }
            importTheData(importDataModel, session, importedProject);

        }
    }


    private List<String> getEntityFieldsInfo() {
        List<String> fieldsInfos = new ArrayList<>();
        fieldsInfos.add("{projectTitle}");
        fieldsInfos.add("{projectDescription}");
        fieldsInfos.add("{primarySector}");
        fieldsInfos.add("{secondarySector}");
        fieldsInfos.add("{projectLocation}");
        fieldsInfos.add("{projectStartDate}");
        fieldsInfos.add("{projectEndDate}");
        fieldsInfos.add("{donorAgency}");
        fieldsInfos.add("{executingAgency}");
        fieldsInfos.add("{implementingAgency}");
        fieldsInfos.add("{actualDisbursement}");
        fieldsInfos.add("{actualCommitment}");
        fieldsInfos.add("{plannedDisbursement}");
        fieldsInfos.add("{plannedCommitment}");
        fieldsInfos.add("{fundingItem}");
        fieldsInfos.add("{financingInstrument}");
        fieldsInfos.add("{typeOfAssistance}");
        fieldsInfos.add("{secondarySubSector}");
        return fieldsInfos.stream().sorted().collect(Collectors.toList());
    }

    // Method to sort a map by its values
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValues(Map<K, V> map) {
        // Create a TreeMap to store the sorted entries
        Map<K, V> sortedMap = new TreeMap<>(new ValueComparator<>(map));

        // Put all entries from the original map into the TreeMap
        sortedMap.putAll(map);

        return sortedMap;
    }

    // Custom comparator to compare map entries by values
    static class ValueComparator<K, V extends Comparable<? super V>> implements Comparator<K> {
        Map<K, V> map;

        // Constructor that takes the original map
        public ValueComparator(Map<K, V> map) {
            this.map = map;
        }

        // Compare method to compare two keys based on their values in the map
        @Override
        public int compare(K key1, K key2) {
            V value1 = map.get(key1);
            V value2 = map.get(key2);
            return value1.compareTo(value2); // Compare values
        }
    }
}
