package org.digijava.module.admin.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.FileStatus;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;
import org.digijava.module.admin.util.ImportedFileUtil;
import org.digijava.module.admin.util.model.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.form.DataImporterForm;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII;

public class DataImporter extends Action {
    static Logger logger = LoggerFactory.getLogger(DataImporter.class);
    private static final int BATCH_SIZE = 1000;
    private static Map<String, Long> constantsMap=new HashMap<>();


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // List of fields
        List<String> fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo", fieldsInfo);
        DataImporterForm dataImporterForm = (DataImporterForm)form;


        if (request.getParameter("uploadTemplate")!=null) {
            logger.info(" this is the action "+request.getParameter("uploadTemplate"));

            InputStream fileInputStream = dataImporterForm.getTemplateFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            Set<String> headersSet = new HashSet<>();
            for (int i=0;i<numberOfSheets;i++)
            {
                Sheet sheet = workbook.getSheetAt(i);
                Row headerRow = sheet.getRow(0);
                Iterator<Cell> cellIterator = headerRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    headersSet.add(cell.getStringCellValue());
                }

            }
            headersSet = headersSet.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
            StringBuilder headers = new StringBuilder();
            headers.append("  <label for=\"columnName\">Select Column Name:</label>\n<select  class=\"select2\" style=\"width: 300px;\" id=\"columnName\">");
            for (String option: headersSet) {
                headers.append("<option>").append(option).append("</option>");
            }
            headers.append("</select>");
            response.setHeader("selectTag",headers.toString());

            response.setHeader("updatedMap","");
            dataImporterForm.getColumnPairs().clear();

            workbook.close();
            }




            if (request.getParameter("addField")!=null) {
            logger.info(" this is the action "+request.getParameter("addField"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            dataImporterForm.getColumnPairs().put(columnName, selectedField);
            logger.info("Column Pairs:"+dataImporterForm.getColumnPairs());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString( dataImporterForm.getColumnPairs());

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("updatedMap",json);

        }


        if (request.getParameter("removeField")!=null) {
            logger.info(" this is the action "+request.getParameter("removeField"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            dataImporterForm.getColumnPairs().put(columnName, selectedField);
            removeMapItem(dataImporterForm.getColumnPairs(),columnName,selectedField);
            logger.info("Column Pairs:"+dataImporterForm.getColumnPairs());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString( dataImporterForm.getColumnPairs());

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("updatedMap",json);

        }

        if (request.getParameter("uploadDataFile") != null) {
            logger.info("This is the action Upload " + request.getParameter("uploadDataFile"));
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
            if (similarFiles!=null && !similarFiles.isEmpty()) {
            for (ImportedFilesRecord similarFilesRecord : similarFiles) {
                logger.info("Similar file: " + similarFilesRecord);
                if (similarFilesRecord.getFileStatus().equals(FileStatus.IN_PROGRESS))
                {
                    response.setHeader("errorMessage","You have a similar file in progress. Please try again later.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }
            }
            }
            if (dataImporterForm.getColumnPairs().isEmpty() || !dataImporterForm.getColumnPairs().containsValue("{projectTitle}"))
            {
                response.setHeader("errorMessage","You must have atleast the {projectTitle} key in your config.");
                response.setStatus(400);
                return mapping.findForward("importData");
            }
            if (!isFileReadable(tempFile) || !isFileContentValid(tempFile)) {
                // Handle invalid file
                logger.error("Invalid file or content.");
                response.setHeader("errorMessage","Unable to parse the file. Please check the file format/content and try again.");
                response.setStatus(400);
                return mapping.findForward("importData");

                // Optionally, you can respond with an error message to the client.
            } else {
                // Proceed with processing the file
                ImportedFilesRecord importedFilesRecord = ImportedFileUtil.saveFile(tempFile, fileName);
                // Process the file in batches
                int res = processFileInBatches(importedFilesRecord, tempFile, request, dataImporterForm.getColumnPairs());
                if (res != 1) {
                    // Handle error
                    logger.info("Error processing file  " + tempFile);
                    response.setHeader("errorMessage","Unable to parse the file. Please check the file format/content and try again.");
                    response.setStatus(400);
                    return mapping.findForward("importData");
                }

                // Clean up
                Files.delete(tempFile.toPath());
                logger.info("Cache map size: " + constantsMap.size());
                constantsMap.clear();
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
        }

        return mapping.findForward("importData");
    }

    private void removeMapItem(Map<String,String> map,String columnName, String selectedField)
    {
        // Check if the entry's key and value match the criteria
        // Remove the entry
        map.entrySet().removeIf(entry -> columnName.equals(entry.getKey()) && selectedField.equals(entry.getValue()));
    }


    public static boolean isFileReadable(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        return file.canRead();
    }

    // Check if the file content is valid
    public static boolean isFileContentValid(File file) {
        // Define your validation criteria here
        // For example, let's say we want to check if the file contains at least one line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line != null; // If at least one line exists, consider the content valid
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false; // Consider the content invalid if an exception occurs
        }
    }

    public int processFileInBatches(ImportedFilesRecord importedFilesRecord,File file, HttpServletRequest request,Map<String, String> config) {
        // Open the workbook
        ImportedFileUtil.updateFileStatus(importedFilesRecord, FileStatus.IN_PROGRESS);
        try (Workbook workbook = new XSSFWorkbook(file)) {
            int numberOfSheets = workbook.getNumberOfSheets();
            logger.info("Number of sheets: " + numberOfSheets);

            // Process each sheet in the workbook
            for (int i = 0; i < numberOfSheets; i++) {
                logger.info("Sheet number: " + i);
                Sheet sheet = workbook.getSheetAt(i);
                processSheetInBatches(sheet, request,config);
            }

            logger.info("Closing the workbook...");
            ImportedFileUtil.updateFileStatus(importedFilesRecord, FileStatus.SUCCESS);
            return 1;
        } catch (IOException e) {
            ImportedFileUtil.updateFileStatus(importedFilesRecord, FileStatus.FAILED);
            logger.error("Error processing Excel file: " + e.getMessage(), e);
            return 0;
        } catch (InvalidFormatException | InvalidOperationException e) {
            logger.error("Error processing Excel file: " + e.getMessage(),e);
            return 0;
        }
    }

    private void processSheetInBatches(Sheet sheet, HttpServletRequest request,Map<String, String> config) throws JsonProcessingException {
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
            processBatch(batch, sheet, request,config);
        }
    }

    private void processBatch(List<Row> batch,Sheet sheet, HttpServletRequest request, Map<String, String> config) throws JsonProcessingException {
        // Process the batch of rows
        SessionUtil.extendSessionIfNeeded(request);
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        for (Row row : batch) {
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
                                setAFundingItem(sheet, config, row, entry, importDataModel, session, cell,true,true, "Actual");
                                break;
                            case "{plannedCommitment}":
                                setAFundingItem(sheet, config, row, entry, importDataModel, session, cell,true,false, "Planned");
                                break;
                            case "{plannedDisbursement}":
                                setAFundingItem(sheet, config, row, entry, importDataModel, session, cell,false,true, "Planned");
                                break;
                            case "{actualCommitment}":
                                setAFundingItem(sheet, config, row, entry, importDataModel, session, cell,true,false, "Actual");
                                break;
                            case "{actualDisbursement}":
                                setAFundingItem(sheet, config, row, entry, importDataModel, session, cell,false,true, "Actual");
                                break;
                            default:
                                logger.error("Unexpected value: " + entry.getValue());
                                break;
                        }



                }
            }
            importTheData(importDataModel, session);

        }
    }

    private void setAFundingItem(Sheet sheet, Map<String, String> config, Row row, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Cell cell,boolean commitment, boolean disbursement, String
            adjustmentType) {
        int detailColumn = getColumnIndexByName(sheet, getKey(config, "{financingInstrument}"));
        String finInstrument= detailColumn>=0? row.getCell(detailColumn).getStringCellValue(): "";
        detailColumn = getColumnIndexByName(sheet, getKey(config, "{typeOfAssistance}"));
        String typeOfAss = detailColumn>=0? row.getCell(detailColumn).getStringCellValue(): "";

        if (importDataModel.getDonor_organization()==null || importDataModel.getDonor_organization().isEmpty())
        {
            if (!config.containsValue("{donorAgency}"))
            {
                updateFunding(importDataModel, session, cell.getNumericCellValue(), entry.getKey(), getRandomOrg(session),typeOfAss,finInstrument, commitment,disbursement, adjustmentType);

            }
            else {
                int columnIndex1 = getColumnIndexByName(sheet, getKey(config, "{donorAgency}"));
                updateOrgs(importDataModel, columnIndex1>=0? row.getCell(columnIndex1).getStringCellValue().trim():"no org", session, "donor");
                updateFunding(importDataModel, session, cell.getNumericCellValue(), entry.getKey(),  new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(),typeOfAss,finInstrument,commitment,disbursement, adjustmentType);
            }

        }else {
            updateFunding(importDataModel, session, cell.getNumericCellValue(), entry.getKey(), new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(),typeOfAss,finInstrument,commitment,disbursement, adjustmentType);
        }
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String findYearSubstring(String text) {
        Pattern pattern = Pattern.compile("(?:19|20)\\d{2}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }


    private String getFundingDate(String yearString)
    {
        LocalDate date = LocalDate.of(Integer.parseInt(yearString), 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }
    private void updateFunding(ImportDataModel importDataModel, Session session, Number amount, String columnHeader, Long orgId, String assistanceType, String finInst, boolean commitment, boolean disbursement, String
                               adjustmentType) {
        String catHql="SELECT s FROM " + AmpCategoryValue.class.getName() + " s JOIN s.ampCategoryClass c WHERE c.keyName = :categoryKey";
        Long currencyId = getCurrencyId(session);
        Long adjType = getCategoryValue(session, "adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,catHql,adjustmentType );
        Long assType = getCategoryValue(session, "assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, catHql,assistanceType);
        Long finInstrument = getCategoryValue(session, "finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, catHql,finInst);
        Long orgRole = getOrganizationRole(session);

        String yearString = findYearSubstring(columnHeader);
        String fundingDate = yearString != null ? getFundingDate(yearString) : getFundingDate("2000");

        Funding funding = new Funding();
        funding.setDonor_organization_id(orgId);
        funding.setType_of_assistance(assType);
        funding.setFinancing_instrument(finInstrument);
        funding.setSource_role(orgRole);

        Transaction transaction = new Transaction();
        transaction.setCurrency(currencyId);
        transaction.setAdjustment_type(adjType);
        transaction.setTransaction_amount(amount.doubleValue());
        transaction.setTransaction_date(fundingDate);
        if (commitment) {
            funding.getCommitments().add(transaction);
        }
        if (disbursement) {
            funding.getDisbursements().add(transaction);
        }

        DonorOrganization donorOrganization = new DonorOrganization();
        donorOrganization.setOrganization(orgId);
        donorOrganization.setPercentage(100.0);

        importDataModel.getDonor_organization().add(donorOrganization);
        importDataModel.getFundings().add(funding);
    }


    private Long getOrganizationRole(Session session) {

        if (constantsMap.containsKey("orgRole")) {
            Long val= constantsMap.get("orgRole");
            logger.info("In cache... orgRole: "+val);
            return val;

        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT o.ampRoleId FROM " + AmpRole.class.getName() + " o WHERE LOWER(o.name) LIKE LOWER(:name)";

        Query query = session.createQuery(hql);
        query.setParameter("name", "%donor%");
        List<Long> orgRoles = query.list();
        Long orgRole = orgRoles.get(0);
        constantsMap.put("orgRole", orgRole);
        return orgRole;
    }

    private Long getCurrencyId(Session session) {

        if (constantsMap.containsKey("currencyId")) {
            Long val = constantsMap.get("currencyId");
            logger.info("In cache... currency: "+val);
            return val;

        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT ac.ampCurrencyId FROM " + AmpCurrency.class.getName() + " ac " +
                "WHERE ac.currencyCode = :currencyCode";

        Query query = session.createQuery(hql);
        query.setString("currencyCode", "XOF");
        Long currencyId = (Long) query.uniqueResult();
        constantsMap.put("currencyId", currencyId);
        return currencyId;
    }

    private Long getCategoryValue(Session session, String constantKey, String categoryKey, String hql, String possibleValue) {
        String fullKey=constantKey+"_"+possibleValue;
        if (constantsMap.containsKey(fullKey)) {
            Long val = constantsMap.get(fullKey);
            logger.info("In cache... "+fullKey+":"+val);
            return val;

        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        Query query = session.createQuery(hql);
        query.setParameter("categoryKey", categoryKey);
        List<?> values = query.list();
        Long categoryId = ((AmpCategoryValue) values.get(0)).getId();

        if (!Objects.equals(possibleValue, "")  && !Objects.equals(possibleValue, null))
        {
            for (Object categoryValue : values)
            {
                if (Objects.equals(((AmpCategoryValue) categoryValue).getValue().toLowerCase(), possibleValue.toLowerCase()))
                {
                    categoryId = ((AmpCategoryValue) categoryValue).getId();
                    logger.info("Found category: "+((AmpCategoryValue) categoryValue).getValue());
                    break;
                }

            }
        }
        logger.info("Found category: "+categoryId +" for "+constantKey+"_"+possibleValue);
        constantsMap.put(fullKey, categoryId);
        return categoryId;
    }

    private AmpActivityVersion existingActivity(ImportDataModel importDataModel,Session session)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT a FROM " + AmpActivityVersion.class.getName() + " a " +
                "WHERE a.name = :name";
        Query query= session.createQuery(hql);
        query.setString("name", importDataModel.getProject_title());
        List<AmpActivityVersion> ampActivityVersions=query.list();
        return  !ampActivityVersions.isEmpty()? ampActivityVersions.get(ampActivityVersions.size()-1) :null;
    }
    private void setStatus(ImportDataModel importDataModel,Session session)
    {
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s " +
                "JOIN s.ampCategoryClass c " +
                "WHERE c.keyName = :categoryKey";
        Long statusId = getCategoryValue(session,"statusId",CategoryConstants.ACTIVITY_STATUS_KEY,hql,"");
        importDataModel.setActivity_status(statusId);

    }
    private void importTheData(ImportDataModel importDataModel, Session session) throws JsonProcessingException {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        ActivityImportRules rules = new ActivityImportRules(true, false,
                true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(ESCAPE_NON_ASCII, false); // Disable escaping of non-ASCII characters during serialization
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        Map<String, Object> map = objectMapper
                .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
        JsonApiResponse<ActivitySummary> response;
        AmpActivityVersion existing = existingActivity(importDataModel,session);
        logger.info("Existing ?"+existing);
        logger.info("Data model object: "+importDataModel);
    if (existing==null){
        logger.info("New activity");
         response= ActivityInterchangeUtils.importActivity(map, false, rules,  "activity/new");
    }
    else
    {
        logger.info("Existing activity");
        importDataModel.setInternal_id(existing.getAmpActivityId());
        importDataModel.setAmp_id(existing.getAmpId());
        ActivityGroup activityGroup= new ActivityGroup();
        activityGroup.setVersion(existing.getAmpActivityGroup().getVersion());
        importDataModel.setActivity_group(activityGroup);
        map = objectMapper
                .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
        response= ActivityInterchangeUtils.importActivity(map, true, rules,  "activity/update");

    }
        logger.info("Import Response: "+objectMapper.writeValueAsString(response));
    }



    private void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary)
    {

        if (constantsMap.containsKey("sector_"+name)) {
            Long sectorId = constantsMap.get("sector_"+name);
            logger.info("In cache... sector "+"sector_"+name+":"+sectorId);
            createSector(importDataModel,primary,sectorId);
        }
        else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }

            session.doWork(connection -> {
                String query = primary ? "SELECT ams.amp_sector_id AS amp_sector_id, ams.name AS name FROM amp_sector ams JOIN amp_classification_config acc ON ams.amp_sec_scheme_id=acc.classification_id WHERE LOWER(ams.name) = LOWER(?) AND acc.name='Primary'" : "SELECT ams.amp_sector_id AS amp_sector_id, ams.name AS name FROM amp_sector ams JOIN amp_classification_config acc ON ams.amp_sec_scheme_id=acc.classification_id WHERE LOWER(ams.name) = LOWER(?) AND acc.name='Secondary'";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    // Set the name as a parameter to the prepared statement
                    statement.setString(1, name);

                    // Execute the query and process the results
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Long ampSectorId = resultSet.getLong("amp_sector_id");
                            createSector(importDataModel, primary, ampSectorId);
                            constantsMap.put("sector_"+name, ampSectorId);
                        }
                    }

                } catch (SQLException e) {
                    logger.error("Error getting sectors", e);
                }
            });
        }



    }

    private static void createSector(ImportDataModel importDataModel, boolean primary, Long ampSectorId) {
        Sector sector1 = new Sector();
        sector1.setSector_percentage(100.00);
        sector1.setSector(ampSectorId);
        if (primary) {
            importDataModel.getPrimary_sectors().add(sector1);
        }
        else
        {
            importDataModel.getSecondary_sectors().add(sector1);

        }
    }

    private Long getRandomOrg(Session session)
    {
        Long randomOrg;
        if (constantsMap.containsKey("randomOrg")) {
            randomOrg = constantsMap.get("randomOrg");
            logger.info("In cache... randomOrg "+randomOrg);
        }else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }
            String hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o";

            randomOrg = (Long) session.createQuery(hql).setMaxResults(1).uniqueResult();
            constantsMap.put("randomOrg",randomOrg);
        }
        return randomOrg;


    }

    private void updateOrgs(ImportDataModel importDataModel, String name, Session session, String type)
    {
        Long orgId;

        if (constantsMap.containsKey("org_"+name)) {
            orgId = constantsMap.get("org_"+name);
            logger.info("In cache... organisation "+"org_"+name+":"+orgId);
        }
        else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }
            String hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o WHERE LOWER(o.name) LIKE LOWER(:name)";

            Query query = session.createQuery(hql);
            query.setParameter("name", "%" + name + "%");
            List<Long> organisations = query.list();
            if (!organisations.isEmpty()) {
                orgId = organisations.get(0);
            } else {
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o";

                query = session.createQuery(hql).setMaxResults(1);
                orgId = (Long) query.uniqueResult();
            }
            constantsMap.put("org_" + name, orgId);
        }
        logger.info("Organisation: " + orgId);

        if (Objects.equals(type, "donor")) {
                DonorOrganization donorOrganization = new DonorOrganization();
                donorOrganization.setOrganization(orgId);
                donorOrganization.setPercentage(100.0);
                importDataModel.getDonor_organization().add(donorOrganization);
        }



    }


    private static int getColumnIndexByName(Sheet sheet, String columnName) {
        try {
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null && columnName.equals(cell.getStringCellValue())) {
                    return i;
                }
            }
            return -1;
        }catch (Exception e)
        {
            logger.error("Error getting column index",e);
            return -1;
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
