package org.digijava.module.aim.action.dataimporter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.IndicatorManagerService;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportStatus;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProject;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProjectCurrency;
import org.digijava.module.aim.action.dataimporter.model.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StringType;
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
import java.time.ZoneId;
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
    public static final boolean SKIP_EXISTING=true;

    private static Double parseDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            //not a double
            return null;
        }

    }

    public static Funding setAFundingItemForExcel(Sheet sheet, Map<String, String> config, Row row, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Cell cell, boolean commitment, boolean disbursement, boolean expenditure, String
            adjustmentType, Funding fundingItem, AmpActivityVersion existingActivity) {
        int detailColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.FINANCING_INSTRUMENT));
        String finInstrument = detailColumn >= 0 ? getStringValueFromCell(row.getCell(detailColumn), false) : "";

        detailColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.EXCHANGE_RATE));
        String exchangeRate = detailColumn >= 0 ? getStringValueFromCell(row.getCell(detailColumn), false) : "";
        Double exchangeRateValue = !exchangeRate.isEmpty() ? parseDouble(exchangeRate) : Double.valueOf(0.0);

        detailColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.TYPE_OF_ASSISTANCE));
        String typeOfAss = detailColumn >= 0 ? getStringValueFromCell(row.getCell(detailColumn), false) : "";
        int separateFundingDateColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.TRANSACTION_DATE));
        String separateFundingDate = separateFundingDateColumn >= 0 ? getDateFromExcel(row, separateFundingDateColumn) : null;

        int currencyCodeColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.CURRENCY));
        String currencyCode = currencyCodeColumn >= 0 ? getStringValueFromCell(row.getCell(currencyCodeColumn), true) : CurrencyUtil.getDefaultCurrency().getCurrencyCode();
        if (existingActivity != null) {
            String existingActivityCurrencyCode = getCurrencyCodeFromExistingImported(existingActivity.getName());
            if (existingActivityCurrencyCode != null) {
                currencyCode = existingActivityCurrencyCode;
            }
        }
        saveCurrencyCode(currencyCode, importDataModel.getProject_title());
        Funding funding;
        int componentNameColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.COMPONENT_NAME));
        String componentName = componentNameColumn >= 0 ? getStringValueFromCell(row.getCell(componentNameColumn), true) : null;
        if (importDataModel.getDonor_organization() == null || importDataModel.getDonor_organization().isEmpty()) {
            if (!config.containsValue(ImporterConstants.DONOR_AGENCY)) {
                funding = updateFunding(fundingItem, importDataModel, getNumericValueFromCell(cell), entry.getKey(), separateFundingDate, getRandomOrg(session), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue);

            } else {
                int columnIndex1 = getColumnIndexByName(sheet, getKey(config, ImporterConstants.DONOR_AGENCY));
                int donorAgencyCodeColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.DONOR_AGENCY_CODE));
                String donorAgencyCode = donorAgencyCodeColumn >= 0 ? getStringValueFromCell(row.getCell(donorAgencyCodeColumn), true) : null;
                updateOrgs(importDataModel, columnIndex1 >= 0 ? Objects.requireNonNull(getStringValueFromCell(row.getCell(columnIndex1), false)).trim() : "no org", donorAgencyCode, session, "donor");
                funding = updateFunding(fundingItem, importDataModel, getNumericValueFromCell(cell), entry.getKey(), separateFundingDate, new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue);
            }

        } else {
            funding = updateFunding(fundingItem, importDataModel, getNumericValueFromCell(cell), entry.getKey(), separateFundingDate, new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue);
        }
        return funding;
    }


    public static Funding setAFundingItemForTxt(Map<String, String> row, Map<String, String> config, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Number value, boolean commitment, boolean disbursement, boolean expenditure, String
            adjustmentType, Funding fundingItem, AmpActivityVersion existingActivity) {
        String finInstrument = row.get(getKey(config, ImporterConstants.FINANCING_INSTRUMENT));
        finInstrument = finInstrument != null ? finInstrument : "";

        String typeOfAss = row.get(getKey(config, ImporterConstants.TYPE_OF_ASSISTANCE));
        typeOfAss = typeOfAss != null ? typeOfAss : "";
        Funding funding;

        String separateFundingDate = row.get(getKey(config, ImporterConstants.TRANSACTION_DATE));
        separateFundingDate = separateFundingDate != null ? separateFundingDate : "";

        String currencyCode = row.get(getKey(config, ImporterConstants.CURRENCY));
        currencyCode = currencyCode != null ? currencyCode : CurrencyUtil.getDefaultCurrency().getCurrencyCode();
        if (existingActivity != null) {
            String existingActivityCurrencyCode = getCurrencyCodeFromExistingImported(existingActivity.getName());
            if (existingActivityCurrencyCode != null) {
                currencyCode = existingActivityCurrencyCode;
            }
        }
        saveCurrencyCode(currencyCode, importDataModel.getProject_title());
        String componentName = row.get(getKey(config, ImporterConstants.COMPONENT_NAME));
        componentName = componentName != null ? componentName : "";


        String exchangeRate = row.get(getKey(config, ImporterConstants.EXCHANGE_RATE));
        exchangeRate = exchangeRate != null ? exchangeRate : "";

        Double exchangeRateValue = !exchangeRate.isEmpty() ? parseDouble(exchangeRate) : Double.valueOf(0.0);


        if (importDataModel.getDonor_organization() == null || importDataModel.getDonor_organization().isEmpty()) {
            if (!config.containsValue(ImporterConstants.DONOR_AGENCY)) {
                funding = updateFunding(fundingItem, importDataModel, value, entry.getKey(), separateFundingDate, getRandomOrg(session), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue);

            } else {
                String donorColumn = row.get(getKey(config, ImporterConstants.DONOR_AGENCY));
                String donorAgencyCode = row.get(getKey(config, ImporterConstants.DONOR_AGENCY_CODE));

                updateOrgs(importDataModel, donorColumn != null && !donorColumn.isEmpty() ? donorColumn.trim() : "no org", donorAgencyCode, session, "donor");
                funding = updateFunding(fundingItem, importDataModel, value, entry.getKey(), separateFundingDate, new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue);
            }

        } else {
            funding = updateFunding(fundingItem, importDataModel, value, entry.getKey(), separateFundingDate, new ArrayList<>(importDataModel.getDonor_organization()).get(0).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue);
        }
        return funding;
    }

    public static String getStringValueFromCell(Cell cell, boolean nullable) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            }
            if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            }
            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                return String.valueOf(cell.getCellFormula());
            }
            return cell.getStringCellValue();
        } catch (Exception e) {
            logger.error("Error getting cell {} value: ", cell, e);
            return nullable ? null : "";
        }
    }

    public static Number getNumericValueFromCell(Cell cell) {
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            logger.error("Error getting cell {} value: ", cell, e);
            return 0;
        }
    }

    private static String getDateFromExcel(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return extractDateFromStringCell(cell);

    }

    private static String extractDateFromStringCell(Cell cell) {
        if (cell == null) {
            return null;
        }

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String rawValue = cell.getStringCellValue().trim();

            if (rawValue.isEmpty()) {
                return null;
            }

            if (rawValue.matches("\\d+(\\.0+)?")) {
                double numericValue = Double.parseDouble(rawValue);
                int intVal = (int) numericValue;
                // Year-only: whole number in reasonable year range (e.g. 2023 or 2023.0) -> use as calendar year, not Excel serial days
                if (intVal >= 1800 && intVal <= 2700 && numericValue == Math.floor(numericValue)) {
                    return intVal + "-12-31";
                }
                if (numericValue > 59) {  // Excel bug: after 28 Feb 1900, 60+ is valid
                    Date date = DateUtil.getJavaDate(numericValue);
                    return outputFormat.format(date);
                }
            }

            String formatted = formatDateFromDateObject(rawValue);
            if (formatted != null) {
                return formatted;
            }

        } catch (Exception e) {
            System.out.println("Error extracting date: " + e.getMessage());
        }

        return null;
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

//        session.flush();
    }

    private static String getCurrencyCodeFromExistingImported(String importedProjectName) {
        Session session = getSession();
        session.clear();
        String hql = "FROM " + ImportedProjectCurrency.class.getName() + " c where c.importedProjectName= :importedProjectName";
        Query query = session.createQuery(hql);
        query.setParameter("importedProjectName", importedProjectName);
        List<ImportedProjectCurrency> importedProjectCurrencies = query.list();
        return importedProjectCurrencies != null && !importedProjectCurrencies.isEmpty() ? importedProjectCurrencies.get(0).getCurrencyCode() : null;
    }

    @NotNull
    private static Session getSession() {
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        return session;
    }


    private static String getFundingDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (dateString != null && dateString.trim().matches("\\d{4}")) {
            int year = Integer.parseInt(dateString.trim());
            return LocalDate.of(year, 12, 31).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        LocalDate date = LocalDate.now();
        if (isCommonDateFormat(dateString)) {
            List<DateTimeFormatter> formatters = Arrays.asList(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                    DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
                    DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy")
            );

            for (DateTimeFormatter formatter : formatters) {
                try {
                    date = LocalDate.parse(dateString, formatter);
                    break;
                } catch (DateTimeParseException e) {
                    // Continue to next formatter
                }
            }
        } else {
            if (StringUtils.isNumeric(dateString)) {
                date = LocalDate.of(Integer.parseInt(dateString), 1, 1);
            } else {
                throw new IllegalArgumentException("Invalid year format: " + dateString);
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }


    private static String formatDateFromDateObject(String date) {
        List<SimpleDateFormat> formatters = Arrays.asList(
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("dd/MM/yyyy"),
                new SimpleDateFormat("MM/dd/yyyy"),
                new SimpleDateFormat("MM-dd-yyyy"),
                new SimpleDateFormat("yyyy/MM/dd"),
                new SimpleDateFormat("dd-MM-yyyy"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        );

        String formattedDate = null;

        // Check if date is in year-only format (e.g., "2024")
        if (Pattern.matches("\\d{4}", date)) {
            try {
                // Parse the year and create a Date object for December 31 of that year
                Date decemberLast = new SimpleDateFormat("yyyy-MM-dd").parse(date + "-12-31");
                return new SimpleDateFormat("yyyy-MM-dd").format(decemberLast); // Return as "yyyy-MM-dd"
            } catch (Exception e) {
                logger.info("Error parsing date", e);
            }
        }

        // Try other date formats if not year-only
        for (SimpleDateFormat formatter : formatters) {
            try {
                formatter.setLenient(false);
                Date parsedDate = formatter.parse(date);
                formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(parsedDate); // Convert to "yyyy-MM-dd"
                break;
            } catch (ParseException e) {
                logger.info("Error formatting date:"+e.getMessage());
            }
        }

        return formattedDate;
    }


    public static boolean isCommonDateFormat(String dateString) {
        List<String> dateFormats = Arrays.asList(
                "yyyy-MM-dd",
                "dd-MM-yyyy",
                "MM-dd-yyyy",
                "MM/dd/yyyy",
                "dd/MM/yyyy",
                "dd.MM.yyyy",
                "yyyy/MM/dd",
                "yyyy-MM-dd HH:mm:ss.S",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss.SSS",
                "EEE MMM dd HH:mm:ss yyyy"
        );

        for (String dateFormat : dateFormats) {
            try {
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern(dateFormat));
                return true;
            } catch (Exception e) {
                // Ignore and continue with the next format
                logger.info("Date format error: ",e);
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

    /**
     * Result of parsing a Measure Type string (e.g. "PC - Planned Commitment").
     * Used to set commitment/disbursement/expenditure and Actual/Planned for funding.
     */
    public static class MeasureTypeResult {
        public final boolean commitment;
        public final boolean disbursement;
        public final boolean expenditure;
        public final String adjustmentType; // "Actual" or "Planned"

        public MeasureTypeResult(boolean commitment, boolean disbursement, boolean expenditure, String adjustmentType) {
            this.commitment = commitment;
            this.disbursement = disbursement;
            this.expenditure = expenditure;
            this.adjustmentType = adjustmentType;
        }
    }

    /**
     * Parses a Measure Type value from the template (e.g. "PC - Planned Commitment", "AC", or "Actual Commitment").
     * @return MeasureTypeResult or null if not recognized
     */
    public static MeasureTypeResult parseMeasureType(String value) {
        if (value == null) return null;
        String s = value.trim();
        if (s.isEmpty()) return null;
        // AC / PC / AD / PD / AE / PE
        if (s.equalsIgnoreCase("AC")) return new MeasureTypeResult(true, false, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
        if (s.equalsIgnoreCase("PC")) return new MeasureTypeResult(true, false, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
        if (s.equalsIgnoreCase("AD")) return new MeasureTypeResult(false, true, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
        if (s.equalsIgnoreCase("PD")) return new MeasureTypeResult(false, true, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
        if (s.equalsIgnoreCase("AE")) return new MeasureTypeResult(false, false, true, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
        if (s.equalsIgnoreCase("PE")) return new MeasureTypeResult(false, false, true, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
        // Full form "PC - Planned Commitment" or label only "Planned Commitment"
        if (s.contains(" - ")) {
            String code = s.substring(0, s.indexOf(" - ")).trim();
            if (code.equalsIgnoreCase("AC")) return new MeasureTypeResult(true, false, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
            if (code.equalsIgnoreCase("PC")) return new MeasureTypeResult(true, false, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
            if (code.equalsIgnoreCase("AD")) return new MeasureTypeResult(false, true, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
            if (code.equalsIgnoreCase("PD")) return new MeasureTypeResult(false, true, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
            if (code.equalsIgnoreCase("AE")) return new MeasureTypeResult(false, false, true, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
            if (code.equalsIgnoreCase("PE")) return new MeasureTypeResult(false, false, true, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
        }
        if (s.equalsIgnoreCase(ImporterConstants.ACTUAL_COMMITMENT)) return new MeasureTypeResult(true, false, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
        if (s.equalsIgnoreCase(ImporterConstants.PLANNED_COMMITMENT)) return new MeasureTypeResult(true, false, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
        if (s.equalsIgnoreCase(ImporterConstants.ACTUAL_DISBURSEMENT)) return new MeasureTypeResult(false, true, false, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
        if (s.equalsIgnoreCase(ImporterConstants.PLANNED_DISBURSEMENT)) return new MeasureTypeResult(false, true, false, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
        if (s.equalsIgnoreCase(ImporterConstants.ACTUAL_EXPENDITURE)) return new MeasureTypeResult(false, false, true, ImporterConstants.ADJUSTMENT_TYPE_ACTUAL);
        if (s.equalsIgnoreCase(ImporterConstants.PLANNED_EXPENDITURE)) return new MeasureTypeResult(false, false, true, ImporterConstants.ADJUSTMENT_TYPE_PLANNED);
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

    public static void removeMapItem(Map<String, String> map, String columnName, String selectedField) {
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


    private static Funding updateFunding(Funding fundingItem, ImportDataModel importDataModel, Number amount, String columnHeaderContainingYear, String separateFundingDate, Long orgId, String assistanceType, String finInst, boolean commitment, boolean disbursement, boolean expenditure, String
            adjustmentType, String currencyCode, String componentName, Double exchangeRate) {
        // TODO: 27/06/2024 pick Month from file and use it in funding
        Session session = getSession();
        Long currencyId = getCurrencyId(session, currencyCode);
        Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, adjustmentType);
        Long assType = getCategoryValue("assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, assistanceType);
        Long finInstrument = getCategoryValue("finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, finInst);
        Long orgRole = getOrganizationRole(session);


        String yearString;
        String fundingDate;
        if (separateFundingDate != null) {
            if (isCommonDateFormat(separateFundingDate)) {
                fundingDate = getFundingDate(separateFundingDate);
            } else {
                yearString = findYearSubstring(separateFundingDate);
                fundingDate = yearString != null ? getFundingDate(yearString) : getFundingDate(null);

            }
        } else {
            yearString = findYearSubstring(columnHeaderContainingYear);
            fundingDate = yearString != null ? getFundingDate(yearString) : getFundingDate(null);

        }

        fundingItem.setDonor_organization_id(orgId);
        fundingItem.setType_of_assistance(assType);
        fundingItem.setFinancing_instrument(finInstrument);
        fundingItem.setSource_role(orgRole);

        Transaction transaction = new Transaction();
        transaction.setCurrency(currencyId);
        transaction.setAdjustment_type(adjType);
        transaction.setTransaction_amount(amount != null ? amount.doubleValue() : 0.0);
        transaction.setTransaction_date(fundingDate);
        transaction.setFixed_exchange_rate(exchangeRate);
        
        // Check for duplicate transactions by currency, amount, and date before adding
        if (commitment) {
            if (!transactionExists(fundingItem.getCommitments(), transaction)) {
                fundingItem.getCommitments().add(transaction);
            }
        }
        if (disbursement) {
            if (!transactionExists(fundingItem.getDisbursements(), transaction)) {
                fundingItem.getDisbursements().add(transaction);
            }
        }
        if (expenditure) {
            if (!transactionExists(fundingItem.getExpenditures(), transaction)) {
                fundingItem.getExpenditures().add(transaction);
            }

        }


        createDonorOrg(importDataModel,orgId);
        if (componentName == null || componentName.isEmpty()) {
            importDataModel.getFundings().add(fundingItem);

        }
        return fundingItem;
    }

    /**
     * Check if a transaction with the same currency, amount, and date already exists in the list
     * @param transactions List of existing transactions
     * @param newTransaction Transaction to check for duplicates
     * @return true if a duplicate exists, false otherwise
     */
    private static boolean transactionExists(List<Transaction> transactions, Transaction newTransaction) {
        if (transactions == null || newTransaction == null) {
            return false;
        }
        
        return transactions.stream().anyMatch(existing -> 
            existing.getCurrency() != null && existing.getCurrency().equals(newTransaction.getCurrency()) &&
            Double.compare(existing.getTransaction_amount(), newTransaction.getTransaction_amount()) == 0 &&
            existing.getTransaction_date() != null && existing.getTransaction_date().equals(newTransaction.getTransaction_date())
        );
    }

    private static Long getOrganizationRole(Session session) {

        if (ConstantsMap.containsKey("orgRole")) {
            Long val = ConstantsMap.get("orgRole");
            logger.info("In cache... orgRole: " + val);
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
        if (currencyCode == null) {
            currencyCode = "USD";
        }
        String cacheKey = "currencyId_" + currencyCode;
        if (ConstantsMap.containsKey(cacheKey)) {
            Long val = ConstantsMap.get(cacheKey);
            logger.info("In cache... currency: " + val);
            return val;
        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT ac.ampCurrencyId FROM " + AmpCurrency.class.getName() + " ac " +
                "WHERE ac.currencyCode = :currencyCode";

        Query query = session.createQuery(hql);
        query.setString("currencyCode", currencyCode);
        Long currencyId = (Long) query.uniqueResult();
        
        // If currency not found, create it
        if (currencyId == null) {
            logger.info("Currency not found: {}. Creating new currency.", currencyCode);
            AmpCurrency newCurrency = new AmpCurrency();
            newCurrency.setCurrencyCode(currencyCode);
            newCurrency.setCurrencyName(currencyCode); // Use code as name if not specified
            newCurrency.setActiveFlag(1); // Active by default
            newCurrency.setVirtual(false);
            session.save(newCurrency);
            session.flush();
            currencyId = newCurrency.getAmpCurrencyId();
            logger.info("Created new currency: {} (id={})", currencyCode, currencyId);
        }
        
        ConstantsMap.put(cacheKey, currencyId);
        return currencyId;
    }

    private static Long getCategoryValue(String constantKey, String categoryKey, String possibleValue) {
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s JOIN s.ampCategoryClass c WHERE c.keyName = :categoryKey";
        String fullKey = constantKey + "_" + possibleValue;
        if (ConstantsMap.containsKey(fullKey)) {
            Long val = ConstantsMap.get(fullKey);
            logger.info("In cache... " + fullKey + ":" + val);
            return val;
        }
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("categoryKey", categoryKey);
        List<?> values = query.list();
        Long categoryId = ((AmpCategoryValue) values.get(0)).getId();

        if (!Objects.equals(possibleValue, "") && !Objects.equals(possibleValue, null)) {
            for (Object categoryValue : values) {
                if (Objects.equals(((AmpCategoryValue) categoryValue).getValue().toLowerCase(), possibleValue.toLowerCase())) {
                    categoryId = ((AmpCategoryValue) categoryValue).getId();
                    logger.info("Found category: " + ((AmpCategoryValue) categoryValue).getValue());
                    break;
                }

            }
        }
        logger.info("Found category: " + categoryId + " for " + constantKey + "_" + possibleValue);
        ConstantsMap.put(fullKey, categoryId);
        return categoryId;
    }

    /**
     * Resolves activity (project) status by value: looks up existing category value for ACTIVITY_STATUS_KEY;
     * if not found in DB, creates a new category value and returns its id.
     * @param statusValue value from the file (e.g. "Ongoing", "Completed")
     * @param session current session (used for create and flush)
     * @return category value id, or null if statusValue is null/empty
     */
    public static Long getOrCreateActivityStatusCategoryValue(String statusValue, Session session) {
        if (statusValue == null || statusValue.trim().isEmpty()) return null;
        String trimmed = statusValue.trim();
        String cacheKey = "statusId_" + trimmed;
        if (ConstantsMap.containsKey(cacheKey)) {
            return ConstantsMap.get(cacheKey);
        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s JOIN s.ampCategoryClass c WHERE c.keyName = :categoryKey";
        Query query = session.createQuery(hql);
        query.setParameter("categoryKey", CategoryConstants.ACTIVITY_STATUS_KEY);
        @SuppressWarnings("unchecked")
        List<AmpCategoryValue> values = (List<AmpCategoryValue>) query.list();
        if (values != null) {
            for (AmpCategoryValue cv : values) {
                if (cv.getValue() != null && cv.getValue().equalsIgnoreCase(trimmed)) {
                    Long id = cv.getId();
                    ConstantsMap.put(cacheKey, id);
                    return id;
                }
            }
        }
        AmpCategoryClass categoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
        if (categoryClass == null) {
            logger.warn("Activity status category class not found; cannot create value: " + trimmed);
            return null;
        }
        try {
            AmpCategoryValue newValue = new AmpCategoryValue();
            newValue.setValue(trimmed);
            newValue.setAmpCategoryClass(categoryClass);
            if (categoryClass.getPossibleValues() == null) {
                categoryClass.setPossibleValues(new java.util.ArrayList<>());
            }
            newValue.setIndex(categoryClass.getPossibleValues().size());
            session.save(newValue);
            session.flush();
            Long id = newValue.getId();
            if (id != null) {
                ConstantsMap.put(cacheKey, id);
                logger.info("Created new activity status category value: " + trimmed + " (id=" + id + ")");
                return id;
            }
        } catch (Exception e) {
            logger.warn("Failed to create activity status value: " + trimmed, e);
        }
        return null;
    }

    public static AmpActivityVersion existingActivity(String projectTitle, String projectCode, Session session) {
        if ((projectTitle == null || projectTitle.trim().isEmpty()) &&
                (projectCode == null || projectCode.trim().isEmpty())) {
            return null;
        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        // Prefer project code if provided
        if (projectCode != null && !projectCode.trim().isEmpty()) {
            String hqlByCode = "SELECT a FROM " + AmpActivityVersion.class.getName() + " a LEFT JOIN FETCH a.activityCreator WHERE a.projectCode = :projectCode";
            Query queryByCode = session.createQuery(hqlByCode);
            queryByCode.setCacheable(true);
            queryByCode.setParameter("projectCode", projectCode.trim(), StringType.INSTANCE);
            List<AmpActivityVersion> byCode = queryByCode.list();
            if (!byCode.isEmpty()) {
                return byCode.get(byCode.size() - 1);
            }
        }
        // Fall back to project title (name)
        if (projectTitle != null && !projectTitle.trim().isEmpty()) {
            String hql = "SELECT a FROM " + AmpActivityVersion.class.getName() + " a LEFT JOIN FETCH a.activityCreator WHERE a.name = :name";
            Query query = session.createQuery(hql);
            query.setCacheable(true);
            query.setParameter("name", projectTitle.trim(), StringType.INSTANCE);
            List<AmpActivityVersion> ampActivityVersions = query.list();
            return !ampActivityVersions.isEmpty() ? ampActivityVersions.get(ampActivityVersions.size() - 1) : null;
        }
        return null;
    }

    /**
     * Sets default activity status and approval status on the import model.
     * If activity_status is already set (e.g. from Project Status column), it is left unchanged.
     */
    public static void setStatus(ImportDataModel importDataModel) {
        if (importDataModel.getActivity_status() == null) {
            Long statusId = getCategoryValue("statusId", CategoryConstants.ACTIVITY_STATUS_KEY, "");
            importDataModel.setActivity_status(statusId);
        }
        importDataModel.setApproval_status(ApprovalStatus.started.getId());
    }

    private static final String CREATED_BY_KEY = "created_by";

    /**
     * Ensures created_by in the activity map is set to a valid team member id when null,
     * so the activity API validator does not reject with "(Invalid field value) created_by".
     * For new activities uses current user; for updates uses existing activity's creator only
     * when that creator is present (never overwrite with current user for existing activities).
     */
    private static void ensureCreatedBySet(Map<String, Object> map, AmpActivityVersion existing) {
        if (existing != null) {
            AmpTeamMember creator = existing.getActivityCreator();
            if (creator == null) {
                // Existing activity has no creator (legacy); API expects null.
                map.put(CREATED_BY_KEY, null);
                return;
            }
            map.put(CREATED_BY_KEY, creator.getAmpTeamMemId());
            return;
        }
        Object createdBy = map.get(CREATED_BY_KEY);
        if (createdBy != null) {
            return;
        }
        AmpTeamMember currentMember = TeamUtil.getCurrentAmpTeamMember();
        if (currentMember != null) {
            map.put(CREATED_BY_KEY, currentMember.getAmpTeamMemId());
        }
    }

    /** @return activity ID on success, null on skip or failure */
    public static Long importTheData(ImportDataModel importDataModel, Session session, ImportedProject importedProject, String componentName, String componentCode, Long responsibleOrgId, List<Funding> fundings, Long existingActivityId) throws JsonProcessingException {
        if (session == null || !session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        
        // Re-fetch existing activity in this transaction if ID is provided to avoid detached entity issues
        AmpActivityVersion existing = null;
        if (existingActivityId != null) {
            existing = session.get(AmpActivityVersion.class, existingActivityId);
        }
        ActivityImportRules rules = new ActivityImportRules(true, false,
                true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(ESCAPE_NON_ASCII, false); // Disable escaping of non-ASCII characters during serialization
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        normalizeLocationPercentages(importDataModel);
        Map<String, Object> map = objectMapper
                .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {
                });
        // Remove null values and "null" strings from the map to avoid API validation errors
        map.entrySet().removeIf(entry -> entry.getValue() == null || "null".equals(String.valueOf(entry.getValue())));
        
        // Do not send indicators in the payload so activity/update does not replace or clear existing indicators.
        // Indicator data is appended separately in addIndicatorDataToActivity.
        map.remove("indicators");
        JsonApiResponse<ActivitySummary> response;
        logger.info("Data model object: " + importDataModel);
        if (importDataModel.getProject_title().trim().isEmpty() && importDataModel.getProject_code().trim().isEmpty()) {
            logger.info("Project title and code are empty. Skipping import");
            importedProject.setImportStatus(ImportStatus.SKIPPED);
            return null;
        }
        if (existing == null) {
            ensureCreatedBySet(map, null);
            logger.info("New activity");
            importedProject.setNewProject(true);
            response = ActivityInterchangeUtils.importActivity(map, false, rules, "activity/new");
        } else {
            logger.info("Existing activity");
            importedProject.setNewProject(false);
            importDataModel.setInternal_id(existing.getAmpActivityId());
            importDataModel.setAmp_id(existing.getAmpId());
            // Only set activity group if it exists and has the data we need
            if (existing.getAmpActivityGroup() != null) {
                ActivityGroup activityGroup = new ActivityGroup();
                activityGroup.setVersion(existing.getAmpActivityGroup().getVersion());
                importDataModel.setActivity_group(activityGroup);
            }
            importDataModel.setProject_title(existing.getName() != null ? existing.getName() : "");
            importDataModel.setProject_code(!Objects.equals(importDataModel.getProject_code(), "") ? importDataModel.getProject_code() : (existing.getProjectCode() != null ? existing.getProjectCode() : ""));
            updateFundingOrgsAndSectorsWithAlreadyExisting(existing, importDataModel);
            // Merge existing activity locations into payload so we only add (row + existing), never remove
            mergeExistingActivityLocationsIntoImport(existing, importDataModel);
            ensureImplementationLevelWhenHasLocations(importDataModel, session);
            normalizeLocationPercentages(importDataModel);
            map = objectMapper
                    .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {
                    });
            // Remove null values and "null" strings from the map to avoid API validation errors
            map.entrySet().removeIf(entry -> entry.getValue() == null || "null".equals(String.valueOf(entry.getValue())));
            
            map.remove("indicators"); // preserve existing indicators; we append in addIndicatorDataToActivity
            // Do not replace programs; avoids StaleStateException when deleting AMP_ACTIVITY_PROGRAM rows
            map.remove("national_plan_objective");
            map.remove("primary_programs");
            map.remove("secondary_programs");
            map.remove("tertiary_programs");
            // Avoid triggering merge of contacts/documents that may reference deleted rows (ObjectNotFoundException)
            map.remove("activity_contacts");
            map.remove("activityContacts");
            map.remove("donor_contact_information");
            map.remove("project_coordinator_contact_information");
            map.remove("sector_ministry_contact_information");
            map.remove("mofed_contact_information");
            map.remove("implementing_executing_agency_contact_information");
            evictActivityFromSecondLevelCache(existing.getAmpActivityId());
            ensureCreatedBySet(map, existing);
            try {
                response = ActivityInterchangeUtils.importActivity(map, true, rules, "activity/update");
            } catch (Exception e) {
                logger.error("Activity import failed for row", e);
                importedProject.setImportStatus(ImportStatus.FAILED);
                Map<String, Collection<Object>> errMap = new LinkedHashMap<>();
                errMap.put("1", Collections.singletonList("Internal Error : [" + (e.getMessage() != null ? e.getMessage() : "Activity import failed") + "]"));
                response = new JsonApiResponse<>(errMap, null, null, null);
            }
        }
        Long activityId = null;
        if (response != null) {
            if (!response.getErrors().isEmpty()) {
                importedProject.setImportStatus(ImportStatus.FAILED);
            } else {
                importedProject.setImportStatus(ImportStatus.SUCCESS);
                activityId = existing != null ? existing.getAmpActivityId() : (Long) response.getContent().getAmpActivityId();
                logger.info("Successfully imported the project. Now adding component if present");
                logger.info("--------------------------------");
                logger.info("Component name at start: " + componentName);
                if (componentName != null && !componentName.isEmpty()) {
                    addComponentsAndProjectCode(response, componentName, componentCode, responsibleOrgId, fundings, importDataModel.getProject_code());
                }
            }
        }

        String resp = objectMapper.writeValueAsString(response);
        importedProject.setImportResponse(resp);
        try {
            if (session == null || !session.isOpen()) {
                // After importActivityInNewSession fails, thread's current session may be closed; use a fresh transaction to save status
                PersistenceManager.doInTransaction(s -> {
                    s.saveOrUpdate(importedProject);
                    s.flush();
                });
            } else {
                session.saveOrUpdate(importedProject);
                session.flush();
            }
        } catch (Exception e) {
            logger.warn("Could not save import status for imported project (response already set): {}", e.getMessage());
        }

        logger.info("Imported project: " + importedProject);
        return activityId;
    }

    private static void updateFundingOrgsAndSectorsWithAlreadyExisting(AmpActivityVersion ampActivityVersion, ImportDataModel importDataModel) {

        if (ampActivityVersion.getFunding() != null) {
            Hibernate.initialize(ampActivityVersion.getFunding());
            Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, "");
            Long assType = getCategoryValue("assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, "");
            Long finInstrument = getCategoryValue("finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, "");
            if (importDataModel.getFundings() == null) importDataModel.setFundings(new HashSet<>());
            for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {
                Funding funding = new Funding();
                if (ampFunding.getAmpFundingId() != null) funding.setFunding_id(ampFunding.getAmpFundingId());
                funding.setDonor_organization_id(ampFunding.getAmpDonorOrgId().getAmpOrgId());
                funding.setType_of_assistance(ampFunding.getTypeOfAssistance() != null ? ampFunding.getTypeOfAssistance().getId() : assType);
                funding.setFinancing_instrument(ampFunding.getFinancingInstrument() != null ? ampFunding.getFinancingInstrument().getId() : finInstrument);
                funding.setSource_role(ampFunding.getSourceRole().getAmpRoleId());
                if (ampFunding.getFundingDetails() != null) {
                    Hibernate.initialize(ampFunding.getFundingDetails());
                    for (AmpFundingDetail ampFundingDetail : ampFunding.getFundingDetails()) {
                        Transaction transaction = new Transaction();
                        if (ampFundingDetail.getAmpFundDetailId() != null) transaction.setTransaction_id(ampFundingDetail.getAmpFundDetailId());
                        transaction.setCurrency(ampFundingDetail.getAmpCurrencyId().getAmpCurrencyId());
                        transaction.setAdjustment_type(ampFundingDetail.getAdjustmentType() != null ? ampFundingDetail.getAdjustmentType().getId() : adjType);
                        transaction.setTransaction_amount(ampFundingDetail.getTransactionAmount());
                        if (ampFundingDetail.getTransactionDate() != null) {
                            transaction.setTransaction_date(getFundingDate(ampFundingDetail.getTransactionDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate().toString()));
                        }
                        transaction.setFixed_exchange_rate(ampFundingDetail.getFixedExchangeRate());
                        if (ampFundingDetail.getTransactionType() == 0) {
                            funding.getCommitments().add(transaction);
                        } else if (ampFundingDetail.getTransactionType() == 1) {
                            funding.getDisbursements().add(transaction);
                        }
                    }
                }
                importDataModel.getFundings().add(funding);
            }
        }
        if (ampActivityVersion.getOrgrole() != null && !ampActivityVersion.getOrgrole().isEmpty()) {
            for (AmpOrgRole ampOrgRole : ampActivityVersion.getOrgrole()) {
                if (ampOrgRole.getRole() == null) continue;
                String roleCode = ampOrgRole.getRole().getRoleCode();
                if (roleCode == null) continue;
                if (roleCode.equalsIgnoreCase("DN")) {
                    createDonorOrg(importDataModel, ampOrgRole.getOrganisation().getAmpOrgId(), ampOrgRole.getAmpOrgRoleId());
                } else if (roleCode.equalsIgnoreCase("EA")) {
                    Organization responsibleOrg = new Organization();
                    responsibleOrg.setOrganization(ampOrgRole.getOrganisation().getAmpOrgId());
                    if (ampOrgRole.getAmpOrgRoleId() != null) responsibleOrg.setId(ampOrgRole.getAmpOrgRoleId());
                    importDataModel.getResponsible_organization().add(responsibleOrg);
                } else if (roleCode.equalsIgnoreCase("BA")) {
                    Organization beneficiaryAgency = new Organization();
                    beneficiaryAgency.setOrganization(ampOrgRole.getOrganisation().getAmpOrgId());
                    if (ampOrgRole.getAmpOrgRoleId() != null) beneficiaryAgency.setId(ampOrgRole.getAmpOrgRoleId());
                    importDataModel.getBeneficiary_agency().add(beneficiaryAgency);
                }
            }
        }

        if (ampActivityVersion.getSectors() != null && !ampActivityVersion.getSectors().isEmpty()) {
            Hibernate.initialize(ampActivityVersion.getSectors());
            for (AmpActivitySector ampActivitySector : ampActivityVersion.getSectors()) {
                if (ampActivitySector.getSectorId() == null) continue;
                boolean primary = ampActivitySector.getClassificationConfig() != null && "primary".equalsIgnoreCase(ampActivitySector.getClassificationConfig().getName());
                createSector(importDataModel, primary, ampActivitySector.getSectorId().getAmpSectorId(), ampActivitySector.getAmpActivitySectorId());
            }
        }
    }

    /**
     * For an existing activity, merges its current locations into the import payload so we only add locations
     * (row locations + existing), never remove. Any existing activity location not already in importDataModel
     * is added. This avoids activity/update deleting locations (e.g. those referenced by indicator connections).
     */
    private static void mergeExistingActivityLocationsIntoImport(AmpActivityVersion existing, ImportDataModel importDataModel) {
        if (existing == null || importDataModel == null) return;
        if (existing.getLocations() == null) return;
        Hibernate.initialize(existing.getLocations());
        Set<Long> alreadyInImport = new HashSet<>();
        if (importDataModel.getLocations() != null) {
            for (Location loc : importDataModel.getLocations()) {
                if (loc != null && loc.getLocation() != null) alreadyInImport.add(loc.getLocation());
            }
        }
        for (AmpActivityLocation aal : existing.getLocations()) {
            AmpCategoryValueLocations loc = aal.getLocation();
            if (loc == null) continue;
            Long locId = loc.getId();
            if (locId == null || alreadyInImport.contains(locId)) continue;
            if (importDataModel.getLocations() == null) importDataModel.setLocations(new HashSet<>());
            double pct = aal.getLocationPercentage() != null ? aal.getLocationPercentage().doubleValue() : 100.0;
            // Include aal.getId() (amp_activity_location_id) so the API matches and keeps this row; otherwise removeByIdExcept drops it and Hibernate deletes it (FK violation if referenced by amp_indicator_connection).
            Long aalId = aal.getId();
            importDataModel.getLocations().add(aalId != null ? new Location(aalId, locId, pct) : new Location(locId, pct));
            alreadyInImport.add(locId);
        }
    }

    /**
     * Scales location percentages so they sum to 100, as required by activity validation.
     * If there are no locations or sum is 0, does nothing.
     */
    private static void normalizeLocationPercentages(ImportDataModel importDataModel) {
        if (importDataModel == null || importDataModel.getLocations() == null || importDataModel.getLocations().isEmpty())
            return;
        Set<Location> locs = importDataModel.getLocations();
        double sum = 0;
        for (Location loc : locs) {
            Double pct = loc.getLocation_percentage();
            sum += (pct != null ? pct : 0);
        }
        if (sum <= 0) return;
        if (Math.abs(sum - 100.0) < 0.001) return; // already 100
        List<Location> list = new ArrayList<>(locs);
        double scale = 100.0 / sum;
        double running = 0;
        for (int i = 0; i < list.size(); i++) {
            Location loc = list.get(i);
            double v;
            if (i == list.size() - 1) {
                v = 100.0 - running; // last one gets remainder so total is exactly 100
            } else {
                Double pct = loc.getLocation_percentage();
                v = (pct != null ? pct : 0) * scale;
                running += v;
            }
            loc.setLocation_percentage(v);
        }
    }

    /**
     * When the payload has locations, implementation level is required. Sets default if missing (e.g. after merging locations for existing activity).
     */
    private static void ensureImplementationLevelWhenHasLocations(ImportDataModel importDataModel, Session session) {
        if (importDataModel == null || importDataModel.getLocations() == null || importDataModel.getLocations().isEmpty())
            return;
        if (importDataModel.getImplementation_level() != null) return;
        updateImpLevels(importDataModel, session);
    }

    static void updateExpendituresIfAny(JsonApiResponse<ActivitySummary> response) {
        Long activityId = (Long) response.getContent().getAmpActivityId();
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpActivityVersion.class.getName() + " a WHERE a.ampActivityId= :activityId";
        Query query = session.createQuery(hql);
        query.setParameter("activityId", activityId);
        List<AmpActivityVersion> activityVersions = query.list();
        if (activityVersions != null && !activityVersions.isEmpty()) {
            Set<AmpFunding> ampFundings = activityVersions.get(activityVersions.size() - 1).getFunding();
            for (AmpFunding ampFunding : ampFundings) {
                for (AmpFundingDetail ampFundingDetail : ampFunding.getFundingDetails()) {
                    if (ampFundingDetail.getTransactionAmount() < 0) {
                        ampFundingDetail.setTransactionType(2);
                        if (ampFundingDetail.getTransactionAmount() == -1) {
                            ampFundingDetail.setTransactionAmount(0.0);
                        }
                        ampFundingDetail.setTransactionAmount(Math.abs(ampFundingDetail.getTransactionAmount()));
                        session.saveOrUpdate(ampFundingDetail);
                        logger.info("AmpFunding refund: " + ampFundingDetail);
                    }
                }

            }
            Set<AmpComponent> components = activityVersions.get(activityVersions.size() - 1).getComponents();
            logger.info("Components: " + components);
            for (AmpComponent ampComponent : components) {
                for (AmpComponentFunding ampComponentFunding : ampComponent.getFundings()) {
                    if (ampComponentFunding.getTransactionAmount() < 0) {
                        ampComponentFunding.setTransactionType(2);
                        if (ampComponentFunding.getTransactionAmount() == -1) {
                            ampComponentFunding.setTransactionAmount(0.0);
                        }
                        ampComponentFunding.setTransactionAmount(Math.abs(ampComponentFunding.getTransactionAmount()));
                        session.saveOrUpdate(ampComponentFunding);
                        logger.info("AmpComponent expenditure: " + ampComponentFunding);
                    }
                }
            }
        }
    }


    static void addComponentsAndProjectCode(JsonApiResponse<ActivitySummary> response, String componentName, String componentCode, Long responsibleOrgId, List<Funding> fundings, String projectCode) {
        Long activityId = (Long) response.getContent().getAmpActivityId();
        Session session = getSession();

        AmpActivityVersion ampActivityVersion = getActivityVersion(session, activityId);

        if (ampActivityVersion != null) {
            AmpComponent ampComponent = getOrCreateComponent(ampActivityVersion, componentName, componentCode);
            ampComponent.setActivity(ampActivityVersion);
            processFundings(ampComponent, fundings, responsibleOrgId);

            boolean updateActivity = updateProjectCodeIfNeeded(ampActivityVersion, projectCode);

            saveOrUpdateComponent(session, ampActivityVersion, ampComponent, updateActivity);
        }
    }


    private static AmpActivityVersion getActivityVersion(Session session, Long activityId) {
        String hql = "FROM " + AmpActivityVersion.class.getName() + " a WHERE a.ampActivityId= :activityId";
        Query query = session.createQuery(hql);
        query.setParameter("activityId", activityId);
        query.setMaxResults(1);
        return (AmpActivityVersion) query.uniqueResult();
    }

    private static AmpComponent getOrCreateComponent(AmpActivityVersion ampActivityVersion, String componentName, String componentCode) {
        return ampActivityVersion.getComponents().stream()
                .filter(c -> StringUtils.equalsIgnoreCase(c.getTitle(), componentName))
                .findFirst()
                .orElseGet(() -> {
                    AmpComponent newComponent = new AmpComponent();
                    newComponent.setTitle(componentName);
                    newComponent.setCode(componentCode);
                    return newComponent;
                });
    }

    private static void processFundings(AmpComponent ampComponent, List<Funding> fundings, Long responsibleOrgId) {
        for (Funding funding : new HashSet<>(fundings)) {
            if (funding != null && funding.getDonor_organization_id() != null) {
                processTransactions(ampComponent, funding.getCommitments(), responsibleOrgId, 0);
                processTransactions(ampComponent, funding.getDisbursements(), responsibleOrgId, 1);
                processTransactions(ampComponent, funding.getExpenditures(), responsibleOrgId, 2);
            }
        }
    }

    private static void processTransactions(AmpComponent ampComponent, List<Transaction> transactions, Long responsibleOrgId, int defaultType) {
        for (Transaction transaction : transactions) {
            AmpComponentFunding ampComponentFunding = createFunding(ampComponent, transaction, responsibleOrgId, defaultType);
            if (!componentFundingExists(ampComponentFunding, ampComponent)) {
                ampComponent.getFundings().add(ampComponentFunding);
            }
        }
    }

    private static AmpComponentFunding createFunding(AmpComponent ampComponent, Transaction transaction, Long responsibleOrgId, int defaultType) {
        AmpComponentFunding funding = new AmpComponentFunding();
        funding.setComponent(ampComponent);
        funding.setReportingDate(new Date());
        if (responsibleOrgId != null) {
            funding.setReportingOrganization(getAmpOrganisationById(responsibleOrgId));
        }
        funding.setTransactionAmount(transaction.getTransaction_amount());
        funding.setTransactionType(transaction.getTransaction_amount() < 0 ? 2 : defaultType);
        funding.setCurrency(getAmpCurrencyById(transaction.getCurrency()));
        funding.setAdjustmentType(getCategoryValueObjectById(transaction.getAdjustment_type()));
        funding.setTransactionDate(convertStringToDate(transaction.getTransaction_date()));
        return funding;
    }

    private static boolean updateProjectCodeIfNeeded(AmpActivityVersion ampActivityVersion, String projectCode) {
        if (projectCode != null && !projectCode.isEmpty()) {
            ampActivityVersion.setProjectCode(projectCode);
            return true;
        }
        return false;
    }

    private static void saveOrUpdateComponent(Session session, AmpActivityVersion ampActivityVersion, AmpComponent ampComponent, boolean updateActivity) {
        if (!ampActivityVersion.getComponents().contains(ampComponent)) {
            ampActivityVersion.getComponents().add(ampComponent);
            updateActivity = true;
        }
        if (updateActivity) {
            session.saveOrUpdate(ampActivityVersion);
        } else {
            session.saveOrUpdate(ampComponent);
        }
    }


    private static boolean componentFundingExists(AmpComponentFunding ampComponentFunding, AmpComponent ampComponent) {
        logger.info("AmpComponentFunding to search: " + ampComponentFunding);

        if (ampComponent.getAmpComponentId() == null) {
            logger.info("Component is null");
            return false;
        }
        for (AmpComponentFunding ampComponentFunding1 : ampComponent.getFundings()) {
            logger.info("AmpComponentFunding here: " + ampComponentFunding);

            if (Objects.equals(ampComponentFunding.getTransactionAmount(), ampComponentFunding1.getTransactionAmount()) && Objects.equals(ampComponentFunding.getTransactionDate(), ampComponentFunding1.getTransactionDate()) && Objects.equals(ampComponentFunding.getAdjustmentType(), ampComponentFunding1.getAdjustmentType()) && Objects.equals(ampComponentFunding.getReportingOrganization(), ampComponentFunding1.getReportingOrganization())) {
                logger.info("AmpComponentFunding has been found");

                return true;
            }
        }
        logger.info("AmpComponentFunding not found: " + ampComponentFunding);


        return false;
    }

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat[] formats = {
                new SimpleDateFormat("dd/MM/yyyy"),
                new SimpleDateFormat("MM/dd/yyyy"),
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("MM-dd-yyyy"),
                new SimpleDateFormat("yyyy/MM/dd"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
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

    private static AmpCategoryValue getCategoryValueObjectById(Long id) {
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpCategoryValue.class.getName() + " a " +
                "WHERE a.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        List<AmpCategoryValue> ampCategoryValues = query.list();
        if (ampCategoryValues != null && !ampCategoryValues.isEmpty()) {
            return ampCategoryValues.get(0);
        }
        return null;
    }

    protected static AmpOrganisation getAmpOrganisationById(Long id) {
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpOrganisation.class.getName() + " a " +
                "WHERE a.ampOrgId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        List<AmpOrganisation> ampOrganisations = query.list();
        if (ampOrganisations != null && !ampOrganisations.isEmpty()) {
            return ampOrganisations.get(0);
        }
        return null;
    }

    protected static AmpCurrency getAmpCurrencyById(Long id) {
        Session session = PersistenceManager.getRequestDBSession();
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        String hql = "FROM " + AmpCurrency.class.getName() + " a " +
                "WHERE a.ampCurrencyId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        List<AmpCurrency> ampCurrencies = query.list();
        if (ampCurrencies != null && !ampCurrencies.isEmpty())
            return ampCurrencies.get(0);
        return null;
    }


    public static void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary, String subSector) {
        if (subSector!=null && !subSector.isEmpty())
        {
            name = subSector;
        }
        if (ConstantsMap.containsKey("sector_" + name)) {
            Long sectorId = ConstantsMap.get("sector_" + name);
            logger.info("In cache... sector " + "sector_" + name + ":" + sectorId);
            createSector(importDataModel, primary, sectorId);
        } else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }

            String finalName = name;
            session.doWork(connection -> {
                String query = primary ? "SELECT ams.amp_sector_id AS amp_sector_id, ams.name AS name FROM amp_sector ams JOIN amp_classification_config acc ON ams.amp_sec_scheme_id=acc.classification_id WHERE LOWER(ams.name) = LOWER(?) AND acc.name='Primary'" : "SELECT ams.amp_sector_id AS amp_sector_id, ams.name AS name FROM amp_sector ams JOIN amp_classification_config acc ON ams.amp_sec_scheme_id=acc.classification_id WHERE LOWER(ams.name) = LOWER(?) AND acc.name='Secondary'";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    // Set the name as a parameter to the prepared statement
                    statement.setString(1, finalName);

                    // Execute the query and process the results
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Long ampSectorId = resultSet.getLong("amp_sector_id");
                            createSector(importDataModel, primary, ampSectorId);
                            ConstantsMap.put("sector_" + finalName, ampSectorId);
                        }
                    }

                } catch (SQLException e) {
                    logger.error("Error getting sectors", e);
                }
            });
        }


    }

    /**
     * Splits a comma- or semicolon-separated string into non-empty trimmed parts.
     */
    private static List<String> splitLocationNames(String locationNames) {
        if (locationNames == null || locationNames.trim().isEmpty()) return Collections.emptyList();
        List<String> result = new ArrayList<>();
        for (String part : locationNames.split("[,;]")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) result.add(trimmed);
        }
        return result;
    }

    public static void updateLocations(ImportDataModel importDataModel, String locationNames, Session session) {
        logger.info("Updating locations");
        if (locationNames == null || locationNames.trim().isEmpty()) return;
        for (String locationName : splitLocationNames(locationNames)) {
            if (ConstantsMap.containsKey("location_" + locationName)) {
                Long location = ConstantsMap.get("location_" + locationName);
                logger.info("In cache... location " + "location_" + locationName + ":" + location);
                importDataModel.getLocations().add(new Location(location, 100.00));

            } else {
                if (!session.isOpen()) {
                    session = PersistenceManager.getRequestDBSession();
                }

                final String locationNameFinal = locationName;
                session.doWork(connection -> {
                    String query = "SELECT acvl.id AS location_id FROM amp_category_value_location acvl WHERE LOWER(acvl.location_name) = LOWER(?)";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, locationNameFinal);

                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                Long location = resultSet.getLong("location_id");
                                logger.info("Location:" + location);
                                importDataModel.getLocations().add(new Location(location, 100.00));
                                ConstantsMap.put("location_" + locationNameFinal, location);
                            }
                        }

                    } catch (SQLException e) {
                        logger.error("Error getting locations", e);
                    }

                });
            }
        }
        updateImpLevels(importDataModel, session);
    }

    /**
     * Ensures the activity has an activity location for the given location name (for indicator location).
     * If the location is not already on the activity, resolves it by name and adds it.
     * @return the AmpActivityLocation for the name, or null if the location name cannot be resolved
     */
    private static AmpActivityLocation getOrAddActivityLocationForName(AmpActivityVersion activity, String locationName, Session session) {
        if (activity == null || locationName == null || locationName.trim().isEmpty()) return null;
        locationName = locationName.trim();
        if (ConstantsMap.containsKey("location_" + locationName)) {
            Long locationId = ConstantsMap.get("location_" + locationName);
            AmpCategoryValueLocations loc = session.get(AmpCategoryValueLocations.class, locationId);
            if (loc == null) return null;
            AmpActivityLocation aal = new AmpActivityLocation();
            aal.setActivity(activity);
            aal.setLocation(loc);
            aal.setLocationPercentage(100f);
            if (activity.getLocations() == null) activity.setLocations(new HashSet<>());
            activity.getLocations().add(aal);
            session.save(aal);
            session.flush();
            return aal;
        }
        if (!session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }
        final String locationNameFinal = locationName;
        final Long[] foundId = new Long[1];
        session.doWork(connection -> {
            String query = "SELECT acvl.id AS location_id FROM amp_category_value_location acvl WHERE LOWER(acvl.location_name) = LOWER(?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, locationNameFinal);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        foundId[0] = resultSet.getLong("location_id");
                        ConstantsMap.put("location_" + locationNameFinal, foundId[0]);
                    }
                }
            } catch (SQLException e) {
                logger.error("Error resolving location by name: " + locationNameFinal, e);
            }
        });
        if (foundId[0] == null) return null;
        AmpCategoryValueLocations loc = session.get(AmpCategoryValueLocations.class, foundId[0]);
        if (loc == null) return null;
        AmpActivityLocation aal = new AmpActivityLocation();
        aal.setActivity(activity);
        aal.setLocation(loc);
        aal.setLocationPercentage(100f);
        if (activity.getLocations() == null) activity.setLocations(new HashSet<>());
        activity.getLocations().add(aal);
        session.save(aal);
        return aal;
    }

    public static void updateImpLevels(ImportDataModel importDataModel, Session session)
    {
        if (ConstantsMap.containsKey("implementation_level_")) {
            Long implementationLevel = ConstantsMap.get("implementation_level_");
            logger.info("In cache... imp level "+"implementation_level:"+implementationLevel);
            importDataModel.setImplementation_level(implementationLevel);
        }else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }

            session.doWork(connection -> {
            String query2 = "SELECT acv.id as implementation_level FROM amp_category_value acv JOIN amp_category_class acc ON acv.amp_category_class_id=acc.id WHERE LOWER(acv.category_value)=? AND LOWER(acc.keyname)=?";
            try (PreparedStatement statement = connection.prepareStatement(query2)) {
                statement.setString(1, "national");
                statement.setString(2, "implementation_level");

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long implementationLevel = resultSet.getLong("implementation_level");
                        logger.info("Imp level:" + implementationLevel);
                        importDataModel.setImplementation_level(implementationLevel);
                        ConstantsMap.put("implementation_level_", implementationLevel);
                    }
                }

            } catch (SQLException e) {
                logger.error("Error getting imp levels", e);
            }});
        }
    }

    private static void createSector(ImportDataModel importDataModel, boolean primary, Long ampSectorId) {
        createSector(importDataModel, primary, ampSectorId, null);
    }

    private static void createSector(ImportDataModel importDataModel, boolean primary, Long ampSectorId, Long sectorPkId) {
        Sector sector1 = new Sector();
        sector1.setSector(ampSectorId);
        if (sectorPkId != null) sector1.setId(sectorPkId);
        if (primary) {
            importDataModel.getPrimary_sectors().add(sector1);
            Map<Integer, Float> percentages = divide100(importDataModel.getPrimary_sectors().size());
            int index = 0;
            for (Sector sec : importDataModel.getPrimary_sectors()) {
                sec.setSector_percentage(percentages.get(index));
                index++;
            }
        } else {
            importDataModel.getSecondary_sectors().add(sector1);
            Map<Integer, Float> percentages = divide100(importDataModel.getSecondary_sectors().size());
            int index = 0;
            for (Sector sec : importDataModel.getSecondary_sectors()) {
                sec.setSector_percentage(percentages.get(index));
                index++;
            }
        }
    }

    //get random undefined agency if name is missing in DB
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
            String hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o where o.name = :name";

            randomOrg = (Long) session.createQuery(hql).setParameter("name", "Undefined Agency", StringType.INSTANCE).setMaxResults(1).uniqueResult();
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
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o WHERE LOWER(o.name)=LOWER(:name) OR LOWER(o.acronym)=LOWER(:name)";
                 query = session.createQuery(hql);
                query.setParameter("name",  name);
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
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o where o.name= :name";

                query = session.createQuery(hql).setParameter("name", "Undefined Agency", StringType.INSTANCE).setMaxResults(1);
                orgId = (Long) query.uniqueResult();
            }
            ConstantsMap.put("org_"+name+"_"+code, orgId);
        }
        logger.info("Organisation: " + orgId);

        if (Objects.equals(type, "donor")) {
            createDonorOrg(importDataModel, orgId);
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

    public static Map<Integer, Float> divide100(int n) {
        Map<Integer, Float> result = new HashMap<>();

        if (n == 0) {
            result.put(0, 100f);
            return result;
        }

        int baseValue = 100 / n;  // Get base distribution
        int remainder = 100 % n;  // Find remainder

        // Assign baseValue to all indexes
        for (int i = 0; i < n; i++) {
            result.put(i, (float) baseValue);
        }

        for (int i = 0; i < remainder; i++) {
            result.put(i, result.get(i) + 1);
        }

        return result;
    }

    private static void createDonorOrg(ImportDataModel importDataModel, Long orgId) {
        createDonorOrg(importDataModel, orgId, null);
    }

    private static void createDonorOrg(ImportDataModel importDataModel, Long orgId, Long orgRoleId) {
        DonorOrganization donorOrganization = new DonorOrganization();
        donorOrganization.setOrganization(orgId);
        if (orgRoleId != null) donorOrganization.setId(orgRoleId);
        importDataModel.getDonor_organization().add(donorOrganization);
        Map<Integer, Float> percentages = divide100(importDataModel.getDonor_organization().size());
        int index = 0;
        for (DonorOrganization donorOrganization1 : importDataModel.getDonor_organization()) {
            donorOrganization1.setPercentage(percentages.get(index));
            index++;
        }
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
//            logger.error("Error getting column index for "+columnName,e);
            return -1;
        }

    }

    /** Parse date from Excel cell or string; returns today if null/empty/invalid. */
    public static Date parseDateDefaultToday(Row row, Sheet sheet, Map<String, String> config, String columnName) {
        String key = getKey(config, columnName);
        if (key == null) return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        int col = getColumnIndexByName(sheet, key);
        if (col < 0) return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Cell cell = row.getCell(col);
        if (cell == null) return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String dateStr = extractDateFromStringCell(cell);
        if (dateStr == null || dateStr.isEmpty()) return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        try {
            return java.sql.Date.valueOf(dateStr);
        } catch (Exception e) {
            return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    /** Add indicator data to an activity from the current row. Called after importTheData when indicator columns are mapped. */
    public static void addIndicatorDataToActivity(Long activityId, Row row, Sheet sheet, Map<String, String> config, Session session) {
        logger.info("addIndicatorDataToActivity: activityId={}, row={}", activityId, row != null ? row.getRowNum() : null);
        if (activityId == null || config == null || row == null || sheet == null) {
            logger.info("addIndicatorDataToActivity: skipping - activityId, config, row or sheet is null");
            return;
        }
        // importTheData runs inside ActivityGatekeeper.doWithLock which commits and closes the session; use a fresh one if closed
        if (session == null || !session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
            logger.info("addIndicatorDataToActivity: obtained fresh session");
        }
        String locationConfigKey = getKey(config, ImporterConstants.INDICATOR_LOCATION) != null
                ? ImporterConstants.INDICATOR_LOCATION
                : ImporterConstants.LOCATION;
        if (getKey(config, ImporterConstants.INDICATOR_NAME) == null || getKey(config, locationConfigKey) == null || getKey(config, ImporterConstants.ACTUAL_VALUE) == null) {
            logger.info("addIndicatorDataToActivity: skipping - missing config for indicator name, location or actual value");
            return;
        }
        String indicatorName = getCellValueByConfig(row, sheet, config, ImporterConstants.INDICATOR_NAME);
        String locationNamesStr = getCellValueByConfig(row, sheet, config, locationConfigKey);
        if (indicatorName == null || indicatorName.trim().isEmpty() || locationNamesStr == null || locationNamesStr.trim().isEmpty()) {
            logger.info("addIndicatorDataToActivity: skipping - indicatorName or locationNamesStr empty (indicatorName='{}', locationNamesStr='{}')", indicatorName, locationNamesStr);
            return;
        }
        indicatorName = indicatorName.trim();
        List<String> locationNames = splitLocationNames(locationNamesStr);
        logger.info("addIndicatorDataToActivity: indicator='{}', locations(count={}): {}", indicatorName, locationNames.size(), locationNames);
        if (locationNames.isEmpty()) {
            logger.debug("addIndicatorDataToActivity: no location names after split");
            return;
        }

        String programName = getCellValueByConfig(row, sheet, config, ImporterConstants.PROGRAM_NAME);
        if (programName != null) programName = programName.trim();

        AmpTheme programTheme = null;
        if (programName != null && !programName.isEmpty()) {
            try {
                programTheme = getOrCreateProgramByName(programName, session);
            } catch (Exception e) {
                logger.error("Could not resolve or create program by name: " + programName, e);
            }
        }

        IndicatorManagerService indicatorService = new IndicatorManagerService();
        MEIndicatorDTO indicatorDto = indicatorService.getMeIndicatorByNameAndProgramNameOptional(indicatorName, (programName == null || programName.isEmpty()) ? null : programName);
        AmpIndicator indicator;
        if (indicatorDto == null) {
            logger.info("addIndicatorDataToActivity: creating new indicator '{}' (program={})", indicatorName, programName);
            MEIndicatorDTO createDto = new MEIndicatorDTO();
            createDto.setName(indicatorName);
            createDto.setCode(indicatorName + "_" + System.currentTimeMillis());
            createDto.setCreationDate(new Date());
            createDto.setAscending(true);
            createDto.setSectorIds(new ArrayList<>());
            if (programTheme != null && programTheme.getAmpThemeId() != null) {
                createDto.setProgramId(programTheme.getAmpThemeId());
            }
            try {
                indicatorDto = indicatorService.createMEIndicator(createDto);
            } catch (Exception e) {
                logger.error("Failed to create indicator: " + indicatorName, e);
                return;
            }
            indicator = session.get(AmpIndicator.class, indicatorDto.getId());
            logger.info("addIndicatorDataToActivity: created indicator id={}", indicator != null ? indicator.getIndicatorId() : null);
        } else {
            indicator = session.get(AmpIndicator.class, indicatorDto.getId());
            logger.info("addIndicatorDataToActivity: using existing indicator id={} name='{}'", indicator != null ? indicator.getIndicatorId() : null, indicatorName);
        }
        if (indicator == null) {
            logger.info("addIndicatorDataToActivity: indicator is null after lookup/create");
            return;
        }

        AmpActivityVersion activity = session.get(AmpActivityVersion.class, activityId);
        if (activity == null) {
            logger.info("addIndicatorDataToActivity: activity not found for activityId={}", activityId);
            return;
        }
        // Force-load indicators so we append to existing; avoid replacing due to lazy/uninitialized collection
        if (activity.getIndicators() != null) {
            Hibernate.initialize(activity.getIndicators());
        }
        int indicatorsCountBefore = activity.getIndicators() == null ? 0 : activity.getIndicators().size();
        logger.info("addIndicatorDataToActivity: activityId={} existing indicators count={}", activityId, indicatorsCountBefore);

        if (programTheme != null) {
            addProgramToActivityIfMissing(activity, programTheme, session);
        }

        AmpIndicatorGlobalValue existingBase = indicator.getBaseValue();
        double origBase = parseDoubleFromConfig(row, sheet, config, ImporterConstants.ORIGINAL_BASE_VALUE);
        boolean hasOrigBase = getKey(config, ImporterConstants.ORIGINAL_BASE_VALUE) != null && !Double.isNaN(origBase);
        double revBase = parseDoubleFromConfig(row, sheet, config, ImporterConstants.REVISED_BASE_VALUE);
        boolean hasRevBase = getKey(config, ImporterConstants.REVISED_BASE_VALUE) != null && !Double.isNaN(revBase);
        double origTarget = parseDoubleFromConfig(row, sheet, config, ImporterConstants.ORIGINAL_TARGET_VALUE);
        double revTarget = parseDoubleFromConfig(row, sheet, config, ImporterConstants.REVISED_TARGET_VALUE);
        String actualValueConfigKey = getKey(config, ImporterConstants.ACTUAL_VALUE);
        double actualVal = parseDoubleFromConfig(row, sheet, config, ImporterConstants.ACTUAL_VALUE);
        if (Double.isNaN(actualVal)) actualVal = 0.0;
        logger.info("addIndicatorDataToActivity: actual value configKey='{}' parsed actualVal={} (NaN->0)", actualValueConfigKey, actualVal);

        Date origBaseDate = parseDateDefaultToday(row, sheet, config, ImporterConstants.ORIGINAL_BASE_VALUE_DATE);
        Date revBaseDate = parseDateDefaultToday(row, sheet, config, ImporterConstants.REVISED_BASE_VALUE_DATE);
        Date origTargetDate = parseDateDefaultToday(row, sheet, config, ImporterConstants.ORIGINAL_TARGET_VALUE_DATE);
        Date revTargetDate = parseDateDefaultToday(row, sheet, config, ImporterConstants.REVISED_TARGET_VALUE_DATE);
        Date actualDate = parseDateDefaultToday(row, sheet, config, ImporterConstants.ACTUAL_VALUE_DATE);
        logger.info("addIndicatorDataToActivity: actualDate={}", actualDate);

        double baseOrigVal = hasOrigBase ? origBase : (existingBase != null && existingBase.getOriginalValue() != null ? existingBase.getOriginalValue() : 0.0);
        double baseRevVal = hasRevBase ? revBase : (existingBase != null && existingBase.getRevisedValue() != null ? existingBase.getRevisedValue() : 0.0);
        double targetOrigVal = Double.isNaN(origTarget) ? 0.0 : origTarget;
        double targetRevVal = Double.isNaN(revTarget) ? 0.0 : revTarget;
        String unit = getCellValueByConfig(row, sheet, config, ImporterConstants.UNIT_OF_MEASURE);
        if (unit != null) unit = unit.trim();
        String actualComment = (unit != null && !unit.isEmpty()) ? "Unit: " + unit : null;

        int merged = 0, created = 0, skipped = 0;
        for (String locationName : locationNames) {
            logger.info("addIndicatorDataToActivity: processing location '{}' for activityId={} indicator='{}'", locationName, activityId, indicatorName);
            AmpActivityLocation activityLocation = null;
            if (activity.getLocations() != null) {
                for (AmpActivityLocation aal : activity.getLocations()) {
                    if (aal.getLocation() != null && locationName.equalsIgnoreCase(aal.getLocation().getName())) {
                        activityLocation = aal;
                        break;
                    }
                }
            }
            if (activityLocation == null) {
                activityLocation = getOrAddActivityLocationForName(activity, locationName, session);
            }
            if (activityLocation == null) {
                logger.info("addIndicatorDataToActivity: could not resolve or add location '{}' for activityId={}, skipping", locationName, activityId);
                skipped++;
                continue;
            }
            logger.info("addIndicatorDataToActivity: activityLocation id={} for '{}'", activityLocation.getLocation() != null ? activityLocation.getLocation().getId() : null, locationName);

            IndicatorActivity ia = findExistingIndicatorActivity(activity, indicator, activityLocation);
            if (ia != null) {
                logger.info("addIndicatorDataToActivity: merging into existing IndicatorActivity for location '{}' (actual={})", locationName, actualVal);
                mergeIndicatorValuesIntoExisting(ia, activityLocation, session, config,
                        baseOrigVal, origBaseDate, baseRevVal, revBaseDate,
                        targetOrigVal, origTargetDate, targetRevVal, revTargetDate,
                        actualVal, actualDate, actualComment);
                session.flush();
                merged++;
                continue;
            }

            logger.info("addIndicatorDataToActivity: creating new IndicatorActivity for activityId={} indicator='{}' location='{}' (actual={})", activityId, indicatorName, locationName, actualVal);
            ia = new IndicatorActivity();
            ia.setActivity(activity);
            ia.setIndicator(indicator);
            ia.setActivityLocation(activityLocation);

            Set<AmpIndicatorValue> values = new HashSet<>();
            if (getKey(config, ImporterConstants.ORIGINAL_BASE_VALUE) != null || getKey(config, ImporterConstants.REVISED_BASE_VALUE) != null || existingBase != null) {
                AmpIndicatorValue baseOrig = new AmpIndicatorValue(AmpIndicatorValue.BASE);
                baseOrig.setValue(baseOrigVal);
                baseOrig.setValueDate(origBaseDate);
                baseOrig.setIndicatorConnection(ia);
                baseOrig.setActivityLocation(activityLocation);
                values.add(baseOrig);
                AmpIndicatorValue baseRev = new AmpIndicatorValue(AmpIndicatorValue.REVISED);
                baseRev.setValue(baseRevVal);
                baseRev.setValueDate(revBaseDate);
                baseRev.setIndicatorConnection(ia);
                baseRev.setActivityLocation(activityLocation);
                values.add(baseRev);
            }
            if (getKey(config, ImporterConstants.ORIGINAL_TARGET_VALUE) != null || getKey(config, ImporterConstants.REVISED_TARGET_VALUE) != null) {
                AmpIndicatorValue tOrig = new AmpIndicatorValue(AmpIndicatorValue.TARGET);
                tOrig.setValue(targetOrigVal);
                tOrig.setValueDate(origTargetDate);
                tOrig.setIndicatorConnection(ia);
                tOrig.setActivityLocation(activityLocation);
                values.add(tOrig);
                AmpIndicatorValue tRev = new AmpIndicatorValue(AmpIndicatorValue.TARGET);
                tRev.setValue(targetRevVal);
                tRev.setValueDate(revTargetDate);
                tRev.setIndicatorConnection(ia);
                tRev.setActivityLocation(activityLocation);
                values.add(tRev);
            }
            AmpIndicatorValue actual = new AmpIndicatorValue(AmpIndicatorValue.ACTUAL);
            actual.setValue(actualVal);
            actual.setValueDate(actualDate);
            actual.setIndicatorConnection(ia);
            actual.setActivityLocation(activityLocation); // required for OnePager form to show value (filters by activityLocation)
            if (actualComment != null) actual.setComment(actualComment);
            values.add(actual);
            logger.info("addIndicatorDataToActivity: new IndicatorActivity - created ACTUAL value: value={}, valueDate={}, saving child", actualVal, actualDate);

            ia.setValues(values);
            if (activity.getIndicators() == null) activity.setIndicators(new HashSet<>());
            activity.getIndicators().add(ia);
            session.save(ia);
            session.save(actual);
            logger.info("addIndicatorDataToActivity: saved IndicatorActivity id={} and ACTUAL AmpIndicatorValue", ia.getId());
            created++;
        }
        session.flush();
        int indicatorsCountAfter = activity.getIndicators() == null ? 0 : activity.getIndicators().size();
        logger.info("addIndicatorDataToActivity: done for activityId={} indicator='{}' - merged={}, created={}, skipped={}, indicatorsCount before={} after={}", activityId, indicatorName, merged, created, skipped, indicatorsCountBefore, indicatorsCountAfter);
    }

    /**
     * Finds an existing activity–indicator connection for the same activity, indicator, and location.
     * Match is by indicator id and activity location (both null or same location).
     */
    private static IndicatorActivity findExistingIndicatorActivity(AmpActivityVersion activity, AmpIndicator indicator,
            AmpActivityLocation activityLocation) {
        if (activity.getIndicators() == null) return null;
        Long indicatorId = indicator != null ? indicator.getIndicatorId() : null;
        Long locationId = activityLocation != null && activityLocation.getLocation() != null
                ? activityLocation.getLocation().getId() : null;
        for (IndicatorActivity ia : activity.getIndicators()) {
            if (ia.getIndicator() == null) continue;
            if (!Objects.equals(ia.getIndicator().getIndicatorId(), indicatorId)) continue;
            Long existingLocId = ia.getActivityLocation() != null && ia.getActivityLocation().getLocation() != null
                    ? ia.getActivityLocation().getLocation().getId() : null;
            if (Objects.equals(existingLocId, locationId)) return ia;
        }
        return null;
    }

    /**
     * Merges imported values into an existing indicator connection: updates existing values by type where present,
     * adds new values only for types that are missing. Sets activityLocation on values so OnePager form can display them.
     */
    private static void mergeIndicatorValuesIntoExisting(IndicatorActivity ia, AmpActivityLocation activityLocation, Session session, Map<String, String> config,
            double baseOrigVal, Date origBaseDate, double baseRevVal, Date revBaseDate,
            double targetOrigVal, Date origTargetDate, double targetRevVal, Date revTargetDate,
            double actualVal, Date actualDate, String actualComment) {
        logger.debug("mergeIndicatorValuesIntoExisting: indicatorConnection id={}, actualVal={}", ia.getId(), actualVal);
        Set<AmpIndicatorValue> existing = ia.getValues();
        if (existing == null) {
            existing = new HashSet<>();
            ia.setValues(existing);
        }
        boolean hasBase = getKey(config, ImporterConstants.ORIGINAL_BASE_VALUE) != null || getKey(config, ImporterConstants.REVISED_BASE_VALUE) != null;
        boolean hasTarget = getKey(config, ImporterConstants.ORIGINAL_TARGET_VALUE) != null || getKey(config, ImporterConstants.REVISED_TARGET_VALUE) != null;

        // Add a new ACTUAL value only if we don't already have the same value on the same date for this location
        if (hasActualWithSameValueAndDate(existing, activityLocation, actualVal, actualDate)) {
            logger.info("mergeIndicatorValuesIntoExisting: skipping ACTUAL - same value {} and date {} already present for this location", actualVal, actualDate);
        } else {
            logger.info("mergeIndicatorValuesIntoExisting: adding new ACTUAL value: value={} valueDate={}", actualVal, actualDate);
            AmpIndicatorValue actual = new AmpIndicatorValue(AmpIndicatorValue.ACTUAL);
            actual.setValue(actualVal);
            actual.setValueDate(actualDate);
            if (actualComment != null) actual.setComment(actualComment);
            actual.setIndicatorConnection(ia);
            actual.setActivityLocation(activityLocation);
            existing.add(actual);
            session.save(actual);
        }

        if (hasBase) {
            AmpIndicatorValue existingBase = findValueByType(existing, AmpIndicatorValue.BASE);
            if (existingBase != null) {
                existingBase.setValue(baseOrigVal);
                existingBase.setValueDate(origBaseDate);
                if (activityLocation != null && existingBase.getActivityLocation() == null) existingBase.setActivityLocation(activityLocation);
            } else {
                AmpIndicatorValue baseOrig = new AmpIndicatorValue(AmpIndicatorValue.BASE);
                baseOrig.setValue(baseOrigVal);
                baseOrig.setValueDate(origBaseDate);
                baseOrig.setIndicatorConnection(ia);
                baseOrig.setActivityLocation(activityLocation);
                existing.add(baseOrig);
                session.save(baseOrig);
            }
            AmpIndicatorValue existingRev = findValueByType(existing, AmpIndicatorValue.REVISED);
            if (existingRev != null) {
                existingRev.setValue(baseRevVal);
                existingRev.setValueDate(revBaseDate);
                if (activityLocation != null && existingRev.getActivityLocation() == null) existingRev.setActivityLocation(activityLocation);
            } else {
                AmpIndicatorValue baseRev = new AmpIndicatorValue(AmpIndicatorValue.REVISED);
                baseRev.setValue(baseRevVal);
                baseRev.setValueDate(revBaseDate);
                baseRev.setIndicatorConnection(ia);
                baseRev.setActivityLocation(activityLocation);
                existing.add(baseRev);
                session.save(baseRev);
            }
        }

        if (hasTarget) {
            List<AmpIndicatorValue> targets = getValuesByType(existing, AmpIndicatorValue.TARGET);
            if (targets.size() >= 2) {
                targets.get(0).setValue(targetOrigVal);
                targets.get(0).setValueDate(origTargetDate);
                if (activityLocation != null && targets.get(0).getActivityLocation() == null) targets.get(0).setActivityLocation(activityLocation);
                targets.get(1).setValue(targetRevVal);
                targets.get(1).setValueDate(revTargetDate);
                if (activityLocation != null && targets.get(1).getActivityLocation() == null) targets.get(1).setActivityLocation(activityLocation);
            } else if (targets.size() == 1) {
                targets.get(0).setValue(targetOrigVal);
                targets.get(0).setValueDate(origTargetDate);
                if (activityLocation != null && targets.get(0).getActivityLocation() == null) targets.get(0).setActivityLocation(activityLocation);
                AmpIndicatorValue tRev = new AmpIndicatorValue(AmpIndicatorValue.TARGET);
                tRev.setValue(targetRevVal);
                tRev.setValueDate(revTargetDate);
                tRev.setIndicatorConnection(ia);
                tRev.setActivityLocation(activityLocation);
                existing.add(tRev);
                session.save(tRev);
            } else {
                AmpIndicatorValue tOrig = new AmpIndicatorValue(AmpIndicatorValue.TARGET);
                tOrig.setValue(targetOrigVal);
                tOrig.setValueDate(origTargetDate);
                tOrig.setIndicatorConnection(ia);
                tOrig.setActivityLocation(activityLocation);
                existing.add(tOrig);
                session.save(tOrig);
                AmpIndicatorValue tRev = new AmpIndicatorValue(AmpIndicatorValue.TARGET);
                tRev.setValue(targetRevVal);
                tRev.setValueDate(revTargetDate);
                tRev.setIndicatorConnection(ia);
                tRev.setActivityLocation(activityLocation);
                existing.add(tRev);
                session.save(tRev);
            }
        }
        evictIndicatorConnectionFromSecondLevelCache(ia);
    }

    /**
     * Evicts the activity from the second-level cache before an update. Avoids ObjectNotFoundException
     * when the cached activity (or its activityContacts) references deleted entities (e.g. AmpActivityDocument).
     */
    private static void evictActivityFromSecondLevelCache(Long activityId) {
        if (activityId == null) return;
        try {
            org.hibernate.SessionFactory sessionFactory = org.digijava.kernel.persistence.PersistenceManager.sf();
            if (sessionFactory == null) return;
            org.hibernate.Cache cache = sessionFactory.getCache();
            if (cache == null) return;
            cache.evictEntityData(AmpActivityVersion.class, activityId);
        } catch (Exception e) {
            logger.debug("Could not evict activity from cache: {}", e.getMessage());
        }
    }

    /**
     * Evicts the indicator connection and its values from the second-level cache so the activity form
     * sees updated values on next load (otherwise cached stale values can be shown).
     */
    private static void evictIndicatorConnectionFromSecondLevelCache(IndicatorActivity ia) {
        try {
            org.hibernate.SessionFactory sessionFactory = org.digijava.kernel.persistence.PersistenceManager.sf();
            if (sessionFactory == null) return;
            org.hibernate.Cache cache = sessionFactory.getCache();
            if (cache == null) return;
            if (ia.getId() != null) cache.evictEntityData(IndicatorConnection.class, ia.getId());
            if (ia.getValues() != null) {
                for (AmpIndicatorValue v : ia.getValues()) {
                    if (v != null && v.getIndValId() != null) cache.evictEntityData(AmpIndicatorValue.class, v.getIndValId());
                }
            }
        } catch (Exception e) {
            logger.debug("Could not evict indicator connection from cache: {}", e.getMessage());
        }
    }

    private static AmpIndicatorValue findValueByType(Set<AmpIndicatorValue> values, int valueType) {
        if (values == null) return null;
        for (AmpIndicatorValue v : values) {
            if (v.getValueType() == valueType) return v;
        }
        return null;
    }

    /** Returns true if there is already an ACTUAL value for this location with the same value and same date (calendar day). */
    private static boolean hasActualWithSameValueAndDate(Set<AmpIndicatorValue> values, AmpActivityLocation activityLocation, double value, Date valueDate) {
        if (values == null) return false;
        Long wantLocId = activityLocation != null && activityLocation.getLocation() != null ? activityLocation.getLocation().getId() : null;
        for (AmpIndicatorValue v : values) {
            if (v.getValueType() != AmpIndicatorValue.ACTUAL) continue;
            Long vLocId = v.getActivityLocation() != null && v.getActivityLocation().getLocation() != null
                    ? v.getActivityLocation().getLocation().getId() : null;
            if (!Objects.equals(vLocId, wantLocId)) continue;
            if (Double.compare(v.getValue(), value) != 0) continue;
            if (!isSameDay(v.getValueDate(), valueDate)) continue;
            return true;
        }
        return false;
    }

    /**
     * Converts a Date to LocalDate, handling both java.util.Date and java.sql.Date.
     * java.sql.Date doesn't support toInstant(), so we use toLocalDate() for it.
     */
    private static LocalDate toLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static boolean isSameDay(Date a, Date b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return toLocalDate(a).equals(toLocalDate(b));
    }

    /** Finds ACTUAL value matching this location; falls back to ACTUAL with null location if none match (for backward compat). */
    private static AmpIndicatorValue findValueByTypeAndLocation(Set<AmpIndicatorValue> values, int valueType, AmpActivityLocation activityLocation) {
        if (values == null) return null;
        AmpIndicatorValue fallbackNull = null;
        Long wantLocId = activityLocation != null && activityLocation.getLocation() != null ? activityLocation.getLocation().getId() : null;
        for (AmpIndicatorValue v : values) {
            if (v.getValueType() != valueType) continue;
            Long vLocId = v.getActivityLocation() != null && v.getActivityLocation().getLocation() != null
                    ? v.getActivityLocation().getLocation().getId() : null;
            if (Objects.equals(vLocId, wantLocId)) return v;
            if (v.getActivityLocation() == null) fallbackNull = v;
        }
        return fallbackNull;
    }

    private static List<AmpIndicatorValue> getValuesByType(Set<AmpIndicatorValue> values, int valueType) {
        if (values == null) return Collections.emptyList();
        List<AmpIndicatorValue> list = new ArrayList<>();
        for (AmpIndicatorValue v : values) {
            if (v.getValueType() == valueType) list.add(v);
        }
        list.sort(Comparator.comparing(AmpIndicatorValue::getValueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return list;
    }

    /**
     * Adds the program (theme) to the activity's programs if not already present.
     * When the Program Percentage field is enabled, percentages are recalculated and
     * divided evenly among all activity programs (including the one just added).
     */
    public static void addProgramToActivityIfMissing(AmpActivityVersion activity, AmpTheme program, Session session) {
        if (activity == null || program == null) return;
        Set<AmpActivityProgram> actPrograms = activity.getActPrograms();
        if (actPrograms == null) {
            actPrograms = new HashSet<>();
            activity.setActPrograms(actPrograms);
        }
        for (AmpActivityProgram ap : actPrograms) {
            if (ap.getProgram() != null && program.getAmpThemeId() != null
                    && program.getAmpThemeId().equals(ap.getProgram().getAmpThemeId())) {
                return;
            }
        }
        AmpActivityProgram activityProgram = new AmpActivityProgram();
        activityProgram.setActivity(activity);
        activityProgram.setProgram(program);
        activityProgram.setProgramPercentage(100f);
        actPrograms.add(activityProgram);
        session.save(activityProgram);

        // If Program Percentage field is enabled, distribute 100% evenly among all programs
        boolean percentageEnabled = false;
        try {
            percentageEnabled = FeaturesUtil.isVisibleField(ArConstants.PROGRAM_PERCENTAGE);
        } catch (Exception e) {
            // No request/session (e.g. batch) – skip percentage redistribution
            logger.error("Could not determine if Program Percentage field is enabled; skipping percentage redistribution", e);
        }
        if (percentageEnabled && !actPrograms.isEmpty()) {
            List<AmpActivityProgram> list = new ArrayList<>(actPrograms);
            int n = list.size();
            Map<Integer, Float> percentages = divide100(n);
            for (int i = 0; i < n; i++) {
                list.get(i).setProgramPercentage(percentages.get(i));
            }
        }
    }

    /**
     * Returns the program (theme) by name, or creates a new root-level program if it does not exist.
     * @param programName program name (must be non-empty)
     * @param session current session (used for create and flush)
     * @return AmpTheme or null if programName is null/empty or creation fails
     */
    public static AmpTheme getOrCreateProgramByName(String programName, Session session) {
        if (programName == null || programName.trim().isEmpty()) return null;
        programName = programName.trim();
        AmpTheme theme = ProgramUtil.getTheme(programName);
        if (theme != null) return theme;
        try {
            AmpTheme newTheme = new AmpTheme();
            newTheme.setName(programName);
            String code = programName.replaceAll("[^a-zA-Z0-9_-]", "_").replaceAll("_+", "_").trim();
            if (code.length() > 45) code = code.substring(0, 45);
            newTheme.setThemeCode("IMP_" + code + "_" + System.currentTimeMillis());
            newTheme.setIndlevel(0);
            newTheme.setParentThemeId(null);
            session.save(newTheme);
            session.flush();
            return newTheme;
        } catch (Exception e) {
            logger.warn("Failed to create program: " + programName, e);
            return null;
        }
    }

    public static String getCellValueByConfig(Row row, Sheet sheet, Map<String, String> config, String fieldName) {
        String key = getKey(config, fieldName);
        if (key == null) return null;
        int col = getColumnIndexByName(sheet, key);
        if (col < 0) return null;
        return getStringValueFromCell(row.getCell(col), true);
    }

    private static double parseDoubleFromConfig(Row row, Sheet sheet, Map<String, String> config, String fieldName) {
        String key = getKey(config, fieldName);
        if (key == null) {
            if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: no config key for field '{}'", fieldName);
            return Double.NaN;
        }
        int col = getColumnIndexByName(sheet, key);
        if (col < 0) {
            if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: column not found for key '{}' in sheet", key);
            return Double.NaN;
        }
        Cell cell = row.getCell(col);
        if (cell == null) {
            if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: cell is null for col={} key='{}'", col, key);
            return Double.NaN;
        }
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                double v = cell.getNumericCellValue();
                if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: ACTUAL_VALUE from numeric cell col='{}' value={}", key, v);
                return v;
            }
            String s = getStringValueFromCell(cell, true);
            if (s == null || s.trim().isEmpty()) {
                if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: ACTUAL_VALUE cell empty for col='{}'", key);
                return Double.NaN;
            }
            double v = Double.parseDouble(s.trim());
            if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: ACTUAL_VALUE from string cell col='{}' raw='{}' parsed={}", key, s, v);
            return v;
        } catch (Exception e) {
            if (ImporterConstants.ACTUAL_VALUE.equals(fieldName)) logger.info("parseDoubleFromConfig: ACTUAL_VALUE parse failed for col='{}' cellType={} error={}", key, cell.getCellType(), e.getMessage());
            return Double.NaN;
        }
    }
}
