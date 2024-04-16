package org.digijava.module.admin.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ecs.wml.Do;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.admin.util.model.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.form.DataImporterForm;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII;

public class DataImporter extends Action {
    Logger logger = LoggerFactory.getLogger(DataImporter.class);


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // List of fields
        List<String> fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo", fieldsInfo);
        DataImporterForm dataImporterForm = (DataImporterForm)form;


        if (request.getParameter("uploadTemplate")!=null) {
            logger.info(" this is the action "+request.getParameter("Upload"));

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
            StringBuilder headers = new StringBuilder();
            headers.append("  <label for=\"columnName\">Select Column Name:</label>\n<select  class=\"select2\" style=\"width: 300px;\" id=\"columnName\">");
            for (String option: headersSet) {
                headers.append("<option>").append(option).append("</option>");
            }
            headers.append("</select>");
            response.setHeader("selectTag",headers.toString());


            workbook.close();
            }




            if (request.getParameter("addField")!=null) {
            logger.info(" this is the action "+request.getParameter("addField"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            dataImporterForm.getColumnPairs().put(columnName, selectedField);
            logger.info("Datas:"+dataImporterForm.getColumnPairs());

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
            logger.info("Datas:"+dataImporterForm.getColumnPairs());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString( dataImporterForm.getColumnPairs());

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("updatedMap",json);

        }

        if (request.getParameter("Upload")!=null) {
            logger.info(" this is the action Upload "+request.getParameter("Upload"));

            InputStream fileInputStream = dataImporterForm.getUploadedFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                parseData(dataImporterForm.getColumnPairs(),workbook,i, request);
            }

            workbook.close();


        }
        return mapping.findForward("importData");
    }

    private void removeMapItem(Map<String,String> map,String columnName, String selectedField)
    {
        // Check if the entry's key and value match the criteria
        // Remove the entry
        map.entrySet().removeIf(entry -> columnName.equals(entry.getKey()) && selectedField.equals(entry.getValue()));
    }


    private void parseData(Map<String,String> config, Workbook workbook, int sheetNumber, HttpServletRequest request) throws JsonProcessingException {
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        Sheet sheet = workbook.getSheetAt(sheetNumber);
        for (Row row : sheet) {
            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setIs_draft(true);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            importDataModel.setCreation_date(now.format(formatter));
            setStatus(importDataModel, session);
            config= sortByValues(config);

            if (row.getRowNum() == 0) {
                continue;
            }
            if (row.getRowNum()<=5) {

                for (Map.Entry<String, String> entry : config.entrySet()) {
                    int columnIndex = getColumnIndexByName(sheet, entry.getKey());
                    if (columnIndex >= 0) {
                        Cell cell = row.getCell(columnIndex);
                        switch (entry.getValue()) {
                            case "{projectName}":
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
                                if (importDataModel.getDonor_organization()==null || importDataModel.getDonor_organization().isEmpty())
                                {
                                    updateFunding(importDataModel,session,cell.getNumericCellValue(),entry.getKey(), getOrgs(session).get(0).getAmpOrgId());

                                }

                                updateFunding(importDataModel,session,cell.getNumericCellValue(),entry.getKey(), new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization());
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + entry.getValue());
                        }
                        importTheData(importDataModel, session);

                    }

                }
            }

        }

    }

    public static String findYearSubstring(String input) {
        String pattern = "\\b(\\d{4})\\b"; //
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    private String getFundingDate(String yearString)
    {
        LocalDate date = LocalDate.of(Integer.parseInt(yearString), 1, 1);

//        LocalTime time = LocalTime.MIDNIGHT;
//        LocalDateTime dateTime = LocalDateTime.of(date, time);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }
    private void updateFunding(ImportDataModel importDataModel,Session session, Number amount, String columnHeader, Long orgId)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT ac.ampCurrencyId FROM " + AmpCurrency.class.getName() + " ac " +
                "WHERE ac.currencyCode = :currencyCode";
        Query query= session.createQuery(hql);
        query.setString("currencyCode", "XOF");
        Long currencyId = (Long)query.uniqueResult();
         hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s " +
                "JOIN s.ampCategoryClass c " +
                "WHERE c.keyName = :categoryKey";

         query= session.createQuery(hql);
        query.setParameter("categoryKey", CategoryConstants.ADJUSTMENT_TYPE_KEY );

        List<AmpCategoryValue> values= query.list();
        logger.info("Adj Types: "+values);
        Long adjType = values.get(0).getId();

//        double amount = Double.parseDouble();
        String yearString = findYearSubstring(columnHeader);
        String fundingDate = yearString!=null?getFundingDate(yearString):getFundingDate("2000");
        Funding funding = new Funding();
        funding.setDonor_organization_id(orgId);
        funding.setSource_role(1L);
        Transaction commitment  = new Transaction();
        commitment.setCurrency(currencyId);
        commitment.setAdjustment_type(adjType);
        commitment.setTransaction_amount(Double.parseDouble(String.valueOf(amount)));
        commitment.setTransaction_date(fundingDate);
        funding.getCommitments().add(commitment);
        funding.getDisbursements().add(commitment);

        importDataModel.getFundings().add(funding);
    }

    private AmpActivityVersion existingActivity(ImportDataModel importDataModel,Session session)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT a FROM " + AmpActivityVersion.class.getName() + " a " +
                "WHERE a.name = :name";
        Query query= session.createQuery(hql);
        query.setString("name", "%"+importDataModel.getProject_title()+"%");
        return (AmpActivityVersion) query.uniqueResult();
    }
    private void setStatus(ImportDataModel importDataModel,Session session)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }


        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s " +
                "JOIN s.ampCategoryClass c " +
                "WHERE c.keyName = :categoryKey";

        Query query= session.createQuery(hql);
        query.setParameter("categoryKey", CategoryConstants.ACTIVITY_STATUS_KEY );
        List<AmpCategoryValue> values= query.list();
        logger.info("Statuses: "+values);
        importDataModel.setActivity_status(values.get(0).getId());

    }
    private void importTheData(ImportDataModel importDataModel, Session session) throws JsonProcessingException {
        logger.info("Trying to import tha data...");
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        ActivityImportRules rules = new ActivityImportRules(false, false,
                true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(ESCAPE_NON_ASCII, false); // Disable escaping of non-ASCII characters during serialization
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        Map<String, Object> map = objectMapper
                .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
        logger.info("Data map: "+map);
        JsonApiResponse<ActivitySummary> response;
        AmpActivityVersion existing = existingActivity(importDataModel,session);
    if (existing==null){
        logger.info("New activity");
         response= ActivityInterchangeUtils.importActivity(map, false, rules,  "activity/new");
    }

    else
    {
        logger.info("Existing activity");
        importDataModel.setInternal_id(existing.getAmpActivityId());
        importDataModel.setAmp_id(existing.getAmpId());
        map = objectMapper
                .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
        response= ActivityInterchangeUtils.importActivity(map, true, rules,  "activity/update");

    }
        logger.info("Import Response: "+objectMapper.writeValueAsString(response));
    }



    private void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }

        session.doWork(connection -> {
//            String query = String.format("SELECT amp_sector_id, sector_config_name FROM all_sectors_with_levels WHERE LOWER(name) = LOWER(%s)", name);
//            String query = "SELECT amp_sector_id, sector_config_name FROM all_sectors_with_levels WHERE LOWER(name) = LOWER(?)";
            String query = "SELECT amp_sector_id, name FROM amp_sector WHERE LOWER(name) = LOWER(?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the name as a parameter to the prepared statement
                statement.setString(1, name);

                // Execute the query and process the results
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long ampSectorId = resultSet.getLong("amp_sector_id");
                        String sectorConfigName = resultSet.getString("name");
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
                }

        } catch (SQLException e) {
        logger.error("Error getting sectors",e);
    }
        });



    }
    private List<AmpOrganisation> getOrgs(Session session)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
            String hql = "SELECT o FROM " + AmpOrganisation.class.getName() + " o";

            Query query= session.createQuery(hql);
            List<AmpOrganisation> organisations =query.list();
            return organisations;

    }

    private void updateOrgs(ImportDataModel importDataModel, String name, Session session, String type)
    {
        if (!session.isOpen()) {
        session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT o FROM " + AmpOrganisation.class.getName() + " o WHERE LOWER(o.name) LIKE LOWER(:name)";

        Query query= session.createQuery(hql);
        query.setParameter("name", "%" + name + "%");
        List<AmpOrganisation> organisations =query.list();
        if (organisations.isEmpty())
        {
             hql = "SELECT o FROM " + AmpOrganisation.class.getName() + " o";

             query= session.createQuery(hql);
            organisations =query.list();
        }
            if (Objects.equals(type, "donor")) {
                DonorOrganization donorOrganization = new DonorOrganization();

            donorOrganization.setOrganization(organisations.get(0).getAmpOrgId());
            donorOrganization.setPercentage(100.0);
                importDataModel.getDonor_organization().add(donorOrganization);
            }



    }


    private static int getColumnIndexByName(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && columnName.equals(cell.getStringCellValue())) {
                return i;
            }
        }
        return -1;
    }
    private List<String> getEntityFieldsInfo() {
        List<String> fieldsInfos = new ArrayList<>();
        fieldsInfos.add("{projectName}");
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
        fieldsInfos.add("{fundingItem}");
        return fieldsInfos;
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
