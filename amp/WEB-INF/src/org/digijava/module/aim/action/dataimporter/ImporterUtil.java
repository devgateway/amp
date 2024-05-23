package org.digijava.module.aim.action.dataimporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.ImportStatus;
import org.digijava.module.admin.dbentity.ImportedProject;
import org.digijava.module.admin.util.model.*;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII;

public class ImporterUtil {
    protected static Map<String, Long> ConstantsMap = new HashMap<>();

    private static final int BATCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(ImporterUtil.class);
     static void setAFundingItemForExcel(Sheet sheet, Map<String, String> config, Row row, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Cell cell, boolean commitment, boolean disbursement, String
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

    static void setAFundingItemForTxt(Map<String, String> row ,Map<String, String> config, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, boolean commitment, boolean disbursement, String
            adjustmentType) {
//        int detailColumn = getColumnIndexByName(sheet, getKey(config, "{financingInstrument}"));
        String finInstrument= row.get(getKey(config, "{financingInstrument}"));
        finInstrument = finInstrument!= null? finInstrument : "";

        //        detailColumn = getColumnIndexByName(sheet, getKey(config, "{typeOfAssistance}"));
        String typeOfAss =row.get(getKey(config, "{typeOfAssistance}")); ;
        typeOfAss=typeOfAss!=null? typeOfAss:"";
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

    public static <K, V> K getKey(Map<K, V> map, V value) {
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

    protected static void removeMapItem(Map<String,String> map,String columnName, String selectedField)
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


    private static String getFundingDate(String yearString)
    {
        LocalDate date = LocalDate.of(Integer.parseInt(yearString), 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }
    private static void updateFunding(ImportDataModel importDataModel, Session session, Number amount, String columnHeader, Long orgId, String assistanceType, String finInst, boolean commitment, boolean disbursement, String
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


    private static Long getOrganizationRole(Session session) {

        if (ConstantsMap.containsKey("orgRole")) {
            Long val= ConstantsMap.get("orgRole");
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
        ConstantsMap.put("orgRole", orgRole);
        return orgRole;
    }

    private static Long getCurrencyId(Session session) {

        if (ConstantsMap.containsKey("currencyId")) {
            Long val = ConstantsMap.get("currencyId");
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
        ConstantsMap.put("currencyId", currencyId);
        return currencyId;
    }

    private static Long getCategoryValue(Session session, String constantKey, String categoryKey, String hql, String possibleValue) {
        String fullKey=constantKey+"_"+possibleValue;
        if (ConstantsMap.containsKey(fullKey)) {
            Long val = ConstantsMap.get(fullKey);
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
        ConstantsMap.put(fullKey, categoryId);
        return categoryId;
    }

    private static AmpActivityVersion existingActivity(ImportDataModel importDataModel, Session session)
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
    protected static void setStatus(ImportDataModel importDataModel,Session session)
    {
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s " +
                "JOIN s.ampCategoryClass c " +
                "WHERE c.keyName = :categoryKey";
        Long statusId = getCategoryValue(session,"statusId",CategoryConstants.ACTIVITY_STATUS_KEY,hql,"");
        importDataModel.setActivity_status(statusId);

    }
    static void importTheData(ImportDataModel importDataModel, Session session, ImportedProject importedProject) throws JsonProcessingException {
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
            importedProject.setNewProject(true);
            response= ActivityInterchangeUtils.importActivity(map, false, rules,  "activity/new");
        }
        else
        {
            logger.info("Existing activity");
            importedProject.setNewProject(false);
            importDataModel.setInternal_id(existing.getAmpActivityId());
            importDataModel.setAmp_id(existing.getAmpId());
            ActivityGroup activityGroup= new ActivityGroup();
            activityGroup.setVersion(existing.getAmpActivityGroup().getVersion());
            importDataModel.setActivity_group(activityGroup);
            map = objectMapper
                    .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
            response= ActivityInterchangeUtils.importActivity(map, true, rules,  "activity/update");

        }
        if (response!=null) {
            if (!response.getErrors().isEmpty()) {
                importedProject.setImportStatus(ImportStatus.FAILED);
            } else {
                importedProject.setImportStatus(ImportStatus.SUCCESS);

            }
        }

        String resp = objectMapper.writeValueAsString(response);
        importedProject.setImportResponse(resp);
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        session.saveOrUpdate(importedProject);
        logger.info("Imported project: "+importedProject);
    }



    static void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary)
    {

        if (ConstantsMap.containsKey("sector_"+name)) {
            Long sectorId = ConstantsMap.get("sector_"+name);
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
                            ConstantsMap.put("sector_"+name, ampSectorId);
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

    private static Long getRandomOrg(Session session)
    {
        Long randomOrg;
        if (ConstantsMap.containsKey("randomOrg")) {
            randomOrg = ConstantsMap.get("randomOrg");
            logger.info("In cache... randomOrg "+randomOrg);
        }else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }
            String hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o";

            randomOrg = (Long) session.createQuery(hql).setMaxResults(1).uniqueResult();
            ConstantsMap.put("randomOrg",randomOrg);
        }
        return randomOrg;


    }

    static void updateOrgs(ImportDataModel importDataModel, String name, Session session, String type)
    {
        Long orgId;

        if (ConstantsMap.containsKey("org_"+name)) {
            orgId = ConstantsMap.get("org_"+name);
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
            ConstantsMap.put("org_" + name, orgId);
        }
        logger.info("Organisation: " + orgId);

        if (Objects.equals(type, "donor")) {
            DonorOrganization donorOrganization = new DonorOrganization();
            donorOrganization.setOrganization(orgId);
            donorOrganization.setPercentage(100.0);
            importDataModel.getDonor_organization().add(donorOrganization);
        }



    }
    protected static int getColumnIndexByName(Sheet sheet, String columnName) {
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
            logger.error("Error getting column index for "+columnName,e);
            return -1;
        }

    }
}
