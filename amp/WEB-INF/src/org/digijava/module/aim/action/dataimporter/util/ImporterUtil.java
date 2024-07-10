package org.digijava.module.aim.action.dataimporter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProject;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProjectCurrency;
import org.digijava.module.aim.action.dataimporter.model.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII;

public class ImporterUtil {
    public static Map<String, Long> ConstantsMap = new HashMap<>();

    private static final int BATCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(ImporterUtil.class);

    private static Double parseDouble(String number)
    {
        try
        {
            return Double.parseDouble(number);
        }
        catch(NumberFormatException e)
        {
            //not a double
            return null;
        }

    }
     public static Funding setAFundingItemForExcel(Sheet sheet, Map<String, String> config, Row row, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Cell cell, boolean commitment, boolean disbursement, String
             adjustmentType, Funding fundingItem, AmpActivityVersion existingActivity) {
         int detailColumn = getColumnIndexByName(sheet, getKey(config, "Financing Instrument"));
         String finInstrument= detailColumn>=0? row.getCell(detailColumn).getStringCellValue(): "";

         detailColumn = getColumnIndexByName(sheet, getKey(config, "Exchange Rate"));
         String exchangeRate= detailColumn>=0? row.getCell(detailColumn).getStringCellValue(): "";
         Double exchangeRateValue = !exchangeRate.isEmpty()?parseDouble(exchangeRate): Double.valueOf(0.0);

         detailColumn = getColumnIndexByName(sheet, getKey(config, "Type Of Assistance"));
         String typeOfAss = detailColumn>=0? row.getCell(detailColumn).getStringCellValue(): "";
         int separateFundingDateColumn=getColumnIndexByName(sheet, getKey(config, "Transaction Date"));
         String separateFundingDate = separateFundingDateColumn>=0? row.getCell(separateFundingDateColumn).getStringCellValue(): null;
         int currencyCodeColumn=getColumnIndexByName(sheet, getKey(config, "Currency"));
         String currencyCode=currencyCodeColumn>=0? row.getCell(currencyCodeColumn).getStringCellValue(): CurrencyUtil.getDefaultCurrency().getCurrencyCode();
         if (existingActivity!=null)
         {
             String existingActivityCurrencyCode = getCurrencyCodeFromExistingImported(existingActivity.getName());
             if (existingActivityCurrencyCode!=null)
             {
                 currencyCode = existingActivityCurrencyCode;
             }
         }
         saveCurrencyCode(currencyCode,importDataModel.getProject_title());
            Funding funding;
         int componentNameColumn = getColumnIndexByName(sheet, getKey(config, "Component Name"));
         String componentName= componentNameColumn>=0? row.getCell(componentNameColumn).getStringCellValue(): null;


         if (importDataModel.getDonor_organization()==null || importDataModel.getDonor_organization().isEmpty())
        {
            if (!config.containsValue("Donor Agency"))
            {
                funding=updateFunding(fundingItem,importDataModel, cell.getNumericCellValue(), entry.getKey(),separateFundingDate, getRandomOrg(session),typeOfAss,finInstrument, commitment,disbursement, adjustmentType, currencyCode,componentName,exchangeRateValue);

            }
            else {
                int columnIndex1 = getColumnIndexByName(sheet, getKey(config, "Donor Agency"));
                int donorAgencyCodeColumn = getColumnIndexByName(sheet, getKey(config, "Donor Agency Code"));
                String donorAgencyCode= donorAgencyCodeColumn>=0? row.getCell(donorAgencyCodeColumn).getStringCellValue(): null;
                updateOrgs(importDataModel, columnIndex1>=0? row.getCell(columnIndex1).getStringCellValue().trim():"no org",donorAgencyCode, session, "donor");
                funding=updateFunding(fundingItem,importDataModel, cell.getNumericCellValue(), entry.getKey(),separateFundingDate,  new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(),typeOfAss,finInstrument,commitment,disbursement, adjustmentType, currencyCode,componentName,exchangeRateValue);
            }

        }else {
            funding=updateFunding(fundingItem,importDataModel, cell.getNumericCellValue(), entry.getKey(),separateFundingDate, new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(),typeOfAss,finInstrument,commitment,disbursement, adjustmentType, currencyCode,componentName,exchangeRateValue);
        }
         return funding;
    }



    public static Funding setAFundingItemForTxt(Map<String, String> row, Map<String, String> config, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Number value, boolean commitment, boolean disbursement, String
            adjustmentType, Funding fundingItem, AmpActivityVersion existingActivity) {
        String finInstrument= row.get(getKey(config, "Financing Instrument"));
        finInstrument = finInstrument!= null? finInstrument : "";

        String typeOfAss =row.get(getKey(config, "Type Of Assistance"));
        typeOfAss=typeOfAss!=null? typeOfAss:"";
        Funding funding;

        String separateFundingDate =row.get(getKey(config, "Transaction Date"));
        separateFundingDate=separateFundingDate!=null? separateFundingDate:"";

        String currencyCode =row.get(getKey(config, "Currency"));
        currencyCode=currencyCode!=null? currencyCode: CurrencyUtil.getDefaultCurrency().getCurrencyCode();
        if (existingActivity!=null)
        {
            String existingActivityCurrencyCode = getCurrencyCodeFromExistingImported(existingActivity.getName());
            if (existingActivityCurrencyCode!=null)
            {
                currencyCode = existingActivityCurrencyCode;
            }
        }
        saveCurrencyCode(currencyCode,importDataModel.getProject_title());
        String componentName =row.get(getKey(config, "Component Name"));
        componentName=componentName!=null? componentName:"";


        String exchangeRate = row.get(getKey(config, "Exchange Rate"));
        exchangeRate=exchangeRate!=null? exchangeRate:"";

        Double exchangeRateValue = !exchangeRate.isEmpty()?parseDouble(exchangeRate): Double.valueOf(0.0);


        if (importDataModel.getDonor_organization()==null || importDataModel.getDonor_organization().isEmpty())
        {
            if (!config.containsValue("Donor Agency"))
            {
                funding=updateFunding(fundingItem,importDataModel, value, entry.getKey(),separateFundingDate, getRandomOrg(session),typeOfAss,finInstrument, commitment,disbursement, adjustmentType, currencyCode,componentName,exchangeRateValue);

            }
            else {
                String donorColumn = row.get(getKey(config, "Donor Agency"));
                String donorAgencyCode= row.get(getKey(config, "Donor Agency Code"));

                updateOrgs(importDataModel, donorColumn!=null && !donorColumn.isEmpty() ? donorColumn.trim():"no org",donorAgencyCode, session, "donor");
                funding=updateFunding(fundingItem,importDataModel, value, entry.getKey(),separateFundingDate,  new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(),typeOfAss,finInstrument,commitment,disbursement, adjustmentType, currencyCode,componentName,exchangeRateValue);
            }

        }else {
            funding=updateFunding(fundingItem,importDataModel, value, entry.getKey(),separateFundingDate, new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(),typeOfAss,finInstrument,commitment,disbursement, adjustmentType,currencyCode,componentName,exchangeRateValue);
        }
        return funding;
    }

    private static void saveCurrencyCode(String currencyCode, String projectName) {
        Session session = getSession();

        String hql = "FROM " + ImportedProjectCurrency.class.getName() + " ipc WHERE ipc.importedProjectName = :importedProjectName";
        Query query = session.createQuery(hql);
        query.setParameter("importedProjectName", projectName);
        List<ImportedProjectCurrency> importedProjectCurrencies = query.list();

        if (importedProjectCurrencies.isEmpty()) {
            ImportedProjectCurrency importedProjectCurrency = new ImportedProjectCurrency();
            importedProjectCurrency.setCurrencyCode(currencyCode);
            importedProjectCurrency.setImportedProjectName(projectName);
            session.saveOrUpdate(importedProjectCurrency);
        } else {
            importedProjectCurrencies.get(0).setCurrencyCode(currencyCode);
            session.update(importedProjectCurrencies.get(0));
        }

        session.flush();
    }

    private static String getCurrencyCodeFromExistingImported(String importedProjectName)
    {
        Session session = getSession();
        String hql = "FROM "+ ImportedProjectCurrency.class.getName() +" ipc where ipc.importedProjectName= :importedProjectName";
        Query query = session.createQuery(hql);
        query.setParameter("importedProjectName", importedProjectName);
        List<ImportedProjectCurrency> importedProjectCurrencies = query.list();
        return importedProjectCurrencies!=null&&!importedProjectCurrencies.isEmpty()?importedProjectCurrencies.get(0).getCurrencyCode():null;
    }

    @NotNull
    private static Session getSession() {
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        return session;
    }


    private static String getFundingDate(String dateString)
    {
        LocalDate date = LocalDate.now();
        if (isCommonDateFormat(dateString)){
            List<DateTimeFormatter> formatters = Arrays.asList(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                    DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
            );

            for (DateTimeFormatter formatter : formatters) {
                try {
                    date = LocalDate.parse(dateString, formatter);
                    break;
                } catch (DateTimeParseException e) {
                    // Continue to next formatter
                }
            }
        }
        else {
            date = LocalDate.of(Integer.parseInt(dateString), 1, 1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }


    public static boolean isCommonDateFormat(String dateString) {
        List<String> dateFormats = Arrays.asList(
                "yyyy-MM-dd",
                "dd-MM-yyyy",
                "MM-dd-yyyy",
                "MM/dd/yyyy",
                "dd/MM/yyyy",
                "dd.MM.yyyy",
                "yyyy/MM/dd"
        );

        for (String dateFormat : dateFormats) {
            try {
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern(dateFormat));
                return true;
            } catch (Exception e) {
                // Ignore and continue with the next format
            }
        }

        return false;
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

    public static void removeMapItem(Map<String, String> map, String columnName, String selectedField)
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



     private static Funding updateFunding(Funding fundingItem, ImportDataModel importDataModel, Number amount, String separateFundingDate, String columnHeader, Long orgId, String assistanceType, String finInst, boolean commitment, boolean disbursement, String
             adjustmentType, String currencyCode, String componentName, Double exchangeRate) {
         // TODO: 27/06/2024 pick Month from file and use it in funding
         Session session = getSession();
        Long currencyId = getCurrencyId(session,currencyCode);
        Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, adjustmentType );
        Long assType = getCategoryValue("assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, assistanceType);
        Long finInstrument = getCategoryValue("finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, finInst);
        Long orgRole = getOrganizationRole(session);



        String yearString;
        String fundingDate;
        if (separateFundingDate!=null)
        {
            if (isCommonDateFormat(separateFundingDate)){
            fundingDate=getFundingDate(separateFundingDate);
            }else {
                yearString = findYearSubstring(separateFundingDate);
                fundingDate = yearString != null ? getFundingDate(yearString) : getFundingDate("2000");

            }
        }
        else {
            yearString=findYearSubstring(columnHeader);
            fundingDate = yearString != null ? getFundingDate(yearString) : getFundingDate("2000");

        }

        fundingItem.setDonor_organization_id(orgId);
        fundingItem.setType_of_assistance(assType);
        fundingItem.setFinancing_instrument(finInstrument);
        fundingItem.setSource_role(orgRole);

        Transaction transaction = new Transaction();
        transaction.setCurrency(currencyId);
        transaction.setAdjustment_type(adjType);
        transaction.setTransaction_amount(amount!=null?amount.doubleValue():0.0);
        transaction.setTransaction_date(fundingDate);
        transaction.setFixed_exchange_rate(exchangeRate);
        if (commitment) {
            fundingItem.getCommitments().add(transaction);
        }
        if (disbursement) {
            fundingItem.getDisbursements().add(transaction);
        }


        DonorOrganization donorOrganization = new DonorOrganization();
        donorOrganization.setOrganization(orgId);
        donorOrganization.setPercentage(100.0);

        importDataModel.getDonor_organization().add(donorOrganization);
         if (componentName==null || componentName.isEmpty())
         {
             importDataModel.getFundings().add(fundingItem);

         }
        return fundingItem;
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

    private static Long getCurrencyId(Session session, String currencyCode) {

        if (ConstantsMap.containsKey("currencyId")) {
            Long val = ConstantsMap.get("currencyId");
            logger.info("In cache... currency: "+val);
            return val;

        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        if (currencyCode==null)
        {
            currencyCode="USD";
        }
        String hql = "SELECT ac.ampCurrencyId FROM " + AmpCurrency.class.getName() + " ac " +
                "WHERE ac.currencyCode = :currencyCode";

        Query query = session.createQuery(hql);
        query.setString("currencyCode", currencyCode);
        Long currencyId = (Long) query.uniqueResult();
        ConstantsMap.put("currencyId", currencyId);
        return currencyId;
    }
    private static Long getCategoryValue(String constantKey, String categoryKey, String possibleValue) {
        String hql="SELECT s FROM " + AmpCategoryValue.class.getName() + " s JOIN s.ampCategoryClass c WHERE c.keyName = :categoryKey";
        String fullKey=constantKey+"_"+possibleValue;
        if (ConstantsMap.containsKey(fullKey)) {
            Long val = ConstantsMap.get(fullKey);
            logger.info("In cache... "+fullKey+":"+val);
            return val;

        }
        Session session = getSession();
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

    public static AmpActivityVersion existingActivity(String projectTitle, String projectCode, Session session)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT a FROM " + AmpActivityVersion.class.getName() + " a " +
                "WHERE a.name = :name OR a.projectCode = :projectCode";
        Query query= session.createQuery(hql);
        query.setCacheable(true);
        query.setString("name", projectTitle);
        query.setString("projectCode", projectCode);
        List<AmpActivityVersion> ampActivityVersions=query.list();
        return  !ampActivityVersions.isEmpty()? ampActivityVersions.get(ampActivityVersions.size()-1) :null;
    }
    public static void setStatus(ImportDataModel importDataModel, Session session)
    {
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s " +
                "JOIN s.ampCategoryClass c " +
                "WHERE c.keyName = :categoryKey";
        Long statusId = getCategoryValue("statusId",CategoryConstants.ACTIVITY_STATUS_KEY, "");
        importDataModel.setActivity_status(statusId);

    }
    public static void importTheData(ImportDataModel importDataModel, Session session, ImportedProject importedProject, String componentName, String componentCode, Long responsibleOrgId, List<Funding> fundings, AmpActivityVersion existing) throws JsonProcessingException {
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
            updateFundingAndOrgsWithAlreadyExisting(existing,importDataModel);
            map = objectMapper
                    .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
            response= ActivityInterchangeUtils.importActivity(map, true, rules,  "activity/update");

        }
        if (response!=null) {
            if (!response.getErrors().isEmpty()) {
                importedProject.setImportStatus(ImportStatus.FAILED);
            } else {
                importedProject.setImportStatus(ImportStatus.SUCCESS);
                logger.info("Successfully imported the project. Now adding component if present");
                logger.info("--------------------------------");
                logger.info("Component name at start: " + componentName);
                if (componentName!=null && !componentName.isEmpty()) {
                    addComponentsAndProjectCode(response, componentName, componentCode, responsibleOrgId, fundings, importDataModel.getProject_code());
                }
                if (!session.isOpen()) {
                    session=PersistenceManager.getRequestDBSession();
                }

                session.flush();


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

    private static void updateFundingAndOrgsWithAlreadyExisting(AmpActivityVersion ampActivityVersion,ImportDataModel importDataModel)
    {

        if (ampActivityVersion.getFunding()!=null)
        {
            Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, "" );
            Long assType = getCategoryValue("assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, "");
            Long finInstrument = getCategoryValue("finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, "");
            for (AmpFunding ampFunding : ampActivityVersion.getFunding())
            {
                Funding funding = new Funding();
                funding.setDonor_organization_id(ampFunding.getAmpDonorOrgId().getAmpOrgId());
                funding.setType_of_assistance(ampFunding.getTypeOfAssistance()!=null?ampFunding.getTypeOfAssistance().getId():assType);
                funding.setFinancing_instrument(ampFunding.getFinancingInstrument()!=null?ampFunding.getFinancingInstrument().getId():finInstrument);
                funding.setSource_role(ampFunding.getSourceRole().getAmpRoleId());
                for (AmpFundingDetail ampFundingDetail: ampFunding.getFundingDetails()) {
                    Transaction transaction = new Transaction();
                    transaction.setCurrency(ampFundingDetail.getAmpCurrencyId().getAmpCurrencyId());
                    transaction.setAdjustment_type(ampFundingDetail.getAdjustmentType()!=null?ampFundingDetail.getAdjustmentType().getId():adjType);
                    transaction.setTransaction_amount(ampFundingDetail.getTransactionAmount());
                    if (ampFundingDetail.getTransactionDate() != null) {
                        transaction.setTransaction_date(getFundingDate(ampFundingDetail.getTransactionDate().toString()));
                    }
                    transaction.setFixed_exchange_rate(ampFundingDetail.getFixedExchangeRate());
                    if (ampFundingDetail.getTransactionType() == 0) {
                        funding.getCommitments().add(transaction);
                    } else if (ampFundingDetail.getTransactionType() == 1) {
                        funding.getDisbursements().add(transaction);
                    }
                }

            }
        }
        if (ampActivityVersion.getOrgrole()!=null) {
            for (AmpOrgRole ampOrgRole:ampActivityVersion.getOrgrole())
            {
                if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase("DN")) {
                    DonorOrganization donorOrganization = new DonorOrganization();
                    donorOrganization.setOrganization(ampOrgRole.getOrganisation().getAmpOrgId());
                    donorOrganization.setPercentage((double) ampOrgRole.getPercentage());
                    importDataModel.getDonor_organization().add(donorOrganization);
                }
                else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase("EA"))
                {
                    Organization responsibleOrg = new Organization();
                    responsibleOrg.setOrganization(ampOrgRole.getOrganisation().getAmpOrgId());
                    importDataModel.getResponsible_organization().add(responsibleOrg);

                }
                else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase("BA"))
                {
                    Organization beneficiaryAgency = new Organization();
                    beneficiaryAgency.setOrganization(ampOrgRole.getOrganisation().getAmpOrgId());
                    importDataModel.getBeneficiary_agency().add(beneficiaryAgency);

                }
            }
        }
    }


    static void addComponentsAndProjectCode(JsonApiResponse<ActivitySummary> response, String componentName, String componentCode, Long responsibleOrgId, List<Funding> fundings, String projectCode) {
        Long activityId = (Long) response.getContent().getAmpActivityId();
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpActivityVersion.class.getName() + " a WHERE a.ampActivityId= :activityId";
        Query query = session.createQuery(hql);
        query.setParameter("activityId", activityId);
        query.setMaxResults(1);
        List<AmpActivityVersion> activityVersions = query.list();

        if (activityVersions != null && !activityVersions.isEmpty()) {
            AmpActivityVersion ampActivityVersion = (AmpActivityVersion) query.list().get(0);
            AmpComponent ampComponent = new AmpComponent();
            boolean found = false;
            logger.info("Component name: {}" ,componentName);

            for (AmpComponent component : ampActivityVersion.getComponents()) {
                if (component.getTitle()!=null && StringUtils.equalsIgnoreCase(component.getTitle(), componentName)) {
                        logger.info("Found component: " + component.getTitle());
                        ampComponent = component;
                        found = true;

                        break;

                }
            }
            if (!found){
                ampComponent.setTitle(componentName);
                ampComponent.setCode(componentCode);
            }

            ampComponent.setActivity(ampActivityVersion);

            for (Funding funding: new HashSet<>(fundings)) {
                if (funding != null) {


                    if (!funding.getCommitments().isEmpty()) {
                        for (Transaction commitment: funding.getCommitments())
                        {
                            AmpComponentFunding ampComponentFunding = new AmpComponentFunding();
                            ampComponentFunding.setComponent(ampComponent);
                            ampComponentFunding.setReportingDate(new Date());
                            if (responsibleOrgId!=null)
                            {
                                ampComponentFunding.setReportingOrganization(getAmpOrganisationById(responsibleOrgId));
                            }
                            ampComponentFunding.setTransactionType(0);
                            ampComponentFunding.setAdjustmentType(getCategoryValueObjectById(commitment.getAdjustment_type()));
                            ampComponentFunding.setCurrency(getAmpCurrencyById(commitment.getCurrency()));
                            ampComponentFunding.setTransactionAmount(commitment.getTransaction_amount());
                            ampComponentFunding.setTransactionDate(convertStringToDate(commitment.getTransaction_date()));

                            if (found){
                                if (!componentFundingExists(ampComponentFunding, ampComponent)) {

                                    ampComponent.getFundings().add(ampComponentFunding);
                                }
                            }else
                            {
                                ampComponent.getFundings().add(ampComponentFunding);

                            }

                        }

                    }
                    if (!funding.getDisbursements().isEmpty()) {
                        for (Transaction disbursement: funding.getDisbursements())
                        {
                            AmpComponentFunding ampComponentFunding = new AmpComponentFunding();
                            ampComponentFunding.setComponent(ampComponent);
                            ampComponentFunding.setReportingDate(new Date());
                            if (responsibleOrgId!=null)
                            {
                                ampComponentFunding.setReportingOrganization(getAmpOrganisationById(responsibleOrgId));
                            }
                            ampComponentFunding.setTransactionType(1);
                            ampComponentFunding.setCurrency(getAmpCurrencyById(disbursement.getCurrency()));
                            ampComponentFunding.setAdjustmentType(getCategoryValueObjectById(disbursement.getAdjustment_type()));
                            ampComponentFunding.setTransactionAmount(disbursement.getTransaction_amount());
                            ampComponentFunding.setTransactionDate(convertStringToDate(disbursement.getTransaction_date()));
                            if (found){
                                if (!componentFundingExists(ampComponentFunding, ampComponent)) {

                                    ampComponent.getFundings().add(ampComponentFunding);
                                }
                            }else
                            {
                                ampComponent.getFundings().add(ampComponentFunding);

                            }
                        }


                    }

                }
            }
            boolean updateActivity=false;
            if (projectCode!=null && !projectCode.isEmpty())
            {
                ampActivityVersion.setProjectCode(projectCode);
                updateActivity=true;
            }

            logger.info("Component:  {}",ampComponent);
            if (found) {
                logger.info("Found component in  activity already. So we just save the component.");
                session.update(ampComponent);
            } else {
                ampActivityVersion.getComponents().add(ampComponent);
                updateActivity=true;
                logger.info("Added component and now saving the activity");
            }
            if (updateActivity) {
                session.saveOrUpdate(ampActivityVersion);
            }

        }
    }

    private static boolean componentFundingExists(AmpComponentFunding ampComponentFunding, AmpComponent ampComponent) {
//        Session session = PersistenceManager.getRequestDBSession();
        String sql = "SELECT COUNT(*) FROM amp_component_funding WHERE rep_organization_id = ? " +
                "AND adjustment_type = ? AND transaction_amount = ? " +
                "AND transaction_date = ? AND amp_component_id = ?";

        try (PreparedStatement statement =PersistenceManager.getJdbcConnection().prepareStatement(sql)) {
            statement.setLong(1, ampComponentFunding.getReportingOrganization().getAmpOrgId());
            statement.setLong(2, ampComponentFunding.getAdjustmentType().getId());
            statement.setDouble(3, ampComponentFunding.getTransactionAmount());
            statement.setDate(4, new java.sql.Date(ampComponentFunding.getTransactionDate().getTime()));
            statement.setLong(5, ampComponent.getAmpComponentId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing native query", e);
        }

        return false;
    }

        public static Date convertStringToDate(String dateString) {
        SimpleDateFormat[] formats = {
                new SimpleDateFormat("dd/MM/yyyy"),
                new SimpleDateFormat("MM/dd/yyyy"),
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("MM-dd-yyyy"),
                new SimpleDateFormat("yyyy/MM/dd"),
                new SimpleDateFormat("dd-MM-yyyy")
        };



        for (SimpleDateFormat format : formats) {
            try {
                return format.parse(dateString);
            } catch (ParseException e) {
                // Try the next format
            }
        }

        // If none of the formats matched, return null
        return null;



    }
    private static AmpCategoryValue getCategoryValueObjectById(Long id)
    {
        Session session= PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpCategoryValue.class.getName() + " a " +
                "WHERE a.id = :id";
        Query query= session.createQuery(hql);
        query.setParameter("id", id);
        List<AmpCategoryValue> ampCategoryValues=query.list();
        if (ampCategoryValues!=null && !ampCategoryValues.isEmpty())
        {
            return ampCategoryValues.get(0);
        }
        return null;
    }
    protected static AmpOrganisation getAmpOrganisationById(Long id)
    {
        Session session= PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpOrganisation.class.getName() + " a " +
                "WHERE a.ampOrgId = :id";
        Query query= session.createQuery(hql);
        query.setParameter("id", id);
        List<AmpOrganisation> ampOrganisations=query.list();
        if (ampOrganisations!=null && !ampOrganisations.isEmpty())
        {
            return ampOrganisations.get(0);
        }
        return null;
    }
    protected static AmpCurrency getAmpCurrencyById(Long id)
    {
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpCurrency.class.getName() + " a " +
                "WHERE a.ampCurrencyId = :id";
        Query query= session.createQuery(hql);
        query.setParameter("id", id);
        List<AmpCurrency> ampCurrencies=query.list();
        if (ampCurrencies!=null && !ampCurrencies.isEmpty())
            return ampCurrencies.get(0);
        return null;
    }



    public static void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary)
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

    public static Long updateOrgs(ImportDataModel importDataModel, String name, String code, Session session, String type)
    {
        Long orgId;

        if (ConstantsMap.containsKey("org_"+name+"_"+code)) {
            orgId = ConstantsMap.get("org_"+name+"_"+code);
            logger.info("In cache... organisation "+"org_"+name+"_"+code+":"+orgId);
        }
        else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }
            String hql = "";
            Query query;
            List<Long> organisations= new ArrayList<>();
            if (name!=null) {
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o WHERE LOWER(o.name) LIKE LOWER(:name)";
                 query = session.createQuery(hql);
                query.setParameter("name", "%" + name + "%");
                organisations = query.list();
            }
            if (organisations.isEmpty() && (code!=null)) {
                    hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o WHERE LOWER(o.orgCode)=LOWER(:code)";
                    query = session.createQuery(hql);
                    query.setParameter("code", code);
                    organisations = query.list();

            }
            if (!organisations.isEmpty()) {
                orgId = organisations.get(0);
            } else {
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o";

                query = session.createQuery(hql).setMaxResults(1);
                orgId = (Long) query.uniqueResult();
            }
            ConstantsMap.put("org_"+name+"_"+code, orgId);
        }
        logger.info("Organisation: " + orgId);

        if (Objects.equals(type, "donor")) {
            DonorOrganization donorOrganization = new DonorOrganization();
            donorOrganization.setOrganization(orgId);
            donorOrganization.setPercentage(100.0);
            importDataModel.getDonor_organization().add(donorOrganization);
        }
        else if (Objects.equals(type, "responsibleOrg"))
        {
            Organization responsibleOrg = new Organization();
            responsibleOrg.setOrganization(orgId);
            importDataModel.getResponsible_organization().add(responsibleOrg);

        }
        else if (Objects.equals(type, "beneficiaryAgency"))
        {
            Organization beneficiaryAgency = new Organization();
            beneficiaryAgency.setOrganization(orgId);
            importDataModel.getBeneficiary_agency().add(beneficiaryAgency);

        }
        return orgId;



    }
    public static int getColumnIndexByName(Sheet sheet, String columnName) {
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
