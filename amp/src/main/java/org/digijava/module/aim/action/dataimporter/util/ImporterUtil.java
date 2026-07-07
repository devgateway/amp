package org.digijava.module.aim.action.dataimporter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.ar.ARUtil;
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
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

        public static List<Funding> setFundingItemsForExcel(Sheet sheet, Map<String, String> config, Row row, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Cell cell, boolean commitment, boolean disbursement, boolean expenditure, String
            adjustmentType, AmpActivityVersion existingActivity, boolean createMissingOrgs, Long orgGroupId, String importedOrgGroupName, boolean createMissingOrgGroups, boolean addDisbursementForCommitment) {
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
        List<Funding> fundings = new ArrayList<>();
        int componentNameColumn = getColumnIndexByName(sheet, getKey(config, ImporterConstants.COMPONENT_NAME));
        String componentName = componentNameColumn >= 0 ? getStringValueFromCell(row.getCell(componentNameColumn), true) : null;
        if (importDataModel.getDonor_organization() == null || importDataModel.getDonor_organization().isEmpty()) {
            if (!config.containsValue(ImporterConstants.DONOR_AGENCY)) {
                Funding f = new Funding();
                updateFunding(f, importDataModel, getNumericValueFromCell(cell), entry.getKey(), separateFundingDate, getRandomOrg(session), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue, addDisbursementForCommitment);
                
                fundings.add(f);

            } else {
                String donorName = getCellValueByConfig(row, sheet, config, ImporterConstants.DONOR_AGENCY);
                String donorAgencyCode = getCellValueByConfig(row, sheet, config, ImporterConstants.DONOR_AGENCY_CODE);
                String donorOrgGroupNames = getCellValueByConfig(row, sheet, config, ImporterConstants.DONOR_ORGANIZATION_GROUP);
                String resolvedDonorOrgGroups = StringUtils.isNotBlank(donorOrgGroupNames) ? donorOrgGroupNames.trim() : importedOrgGroupName;
                String resolvedDonorName = StringUtils.isNotBlank(donorName) ? donorName.trim() : "no org";
                if ("no org".equals(resolvedDonorName)) {
                    logger.warn("Donor Agency lookup resolved empty while creating funding row; falling back to 'no org'. configKey='{}', transactionField='{}'",
                            getKey(config, ImporterConstants.DONOR_AGENCY), entry.getKey());
                }
                updateOrgs(importDataModel, resolvedDonorName, donorAgencyCode, session, "donor", createMissingOrgs, orgGroupId, resolvedDonorOrgGroups, createMissingOrgGroups);
                List<DonorOrganization> donors = new ArrayList<>(importDataModel.getDonor_organization());
                List<Double> splits = splitAmounts(getNumericValueFromCell(cell).doubleValue(), donors.size());
                for (int i = 0; i < donors.size(); i++) {
                    Funding f = new Funding();
                    updateFunding(f, importDataModel, splits.get(i), entry.getKey(), separateFundingDate, donors.get(i).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue, addDisbursementForCommitment);
                    
                    fundings.add(f);
                }

            }

        } else {
            List<DonorOrganization> donors = new ArrayList<>(importDataModel.getDonor_organization());
            List<Double> splits = splitAmounts(getNumericValueFromCell(cell).doubleValue(), donors.size());
            for (int i = 0; i < donors.size(); i++) {
                Funding f = new Funding();
                updateFunding(f, importDataModel, splits.get(i), entry.getKey(), separateFundingDate, donors.get(i).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue, addDisbursementForCommitment);
                
                fundings.add(f);
            }
        }
        return fundings;
    }


        public static List<Funding> setFundingItemsForTxt(Map<String, String> row, Map<String, String> config, Map.Entry<String, String> entry, ImportDataModel importDataModel, Session session, Number value, boolean commitment, boolean disbursement, boolean expenditure, String
            adjustmentType, AmpActivityVersion existingActivity, boolean createMissingOrgs, Long orgGroupId, String importedOrgGroupName, boolean createMissingOrgGroups, boolean addDisbursementForCommitment) {
        String finInstrument = row.get(getKey(config, ImporterConstants.FINANCING_INSTRUMENT));
        finInstrument = finInstrument != null ? finInstrument : "";

        String typeOfAss = row.get(getKey(config, ImporterConstants.TYPE_OF_ASSISTANCE));
        typeOfAss = typeOfAss != null ? typeOfAss : "";
        List<Funding> fundings = new ArrayList<>();

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
                Funding f = new Funding();
                updateFunding(f, importDataModel, value, entry.getKey(), separateFundingDate, getRandomOrg(session), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue, addDisbursementForCommitment);
                
                fundings.add(f);

            } else {
                String donorColumn = getCellValueByConfig(row, config, ImporterConstants.DONOR_AGENCY);
                String donorAgencyCode = getCellValueByConfig(row, config, ImporterConstants.DONOR_AGENCY_CODE);
                String donorOrgGroupNames = getCellValueByConfig(row, config, ImporterConstants.DONOR_ORGANIZATION_GROUP);
                String resolvedDonorOrgGroups = StringUtils.isNotBlank(donorOrgGroupNames) ? donorOrgGroupNames.trim() : importedOrgGroupName;
                String resolvedDonorName = StringUtils.isNotBlank(donorColumn) ? donorColumn.trim() : "no org";
                if ("no org".equals(resolvedDonorName)) {
                    logger.info("Donor Agency lookup resolved empty while creating TXT funding row; falling back to 'no org'. configKey='{}', transactionField='{}'",
                            getKey(config, ImporterConstants.DONOR_AGENCY), entry.getKey());
                }

                updateOrgs(importDataModel, resolvedDonorName, donorAgencyCode, session, "donor", createMissingOrgs, orgGroupId, resolvedDonorOrgGroups, createMissingOrgGroups);
                List<DonorOrganization> donors = new ArrayList<>(importDataModel.getDonor_organization());
                List<Double> splits = splitAmounts(value != null ? value.doubleValue() : 0.0, donors.size());
                for (int i = 0; i < donors.size(); i++) {
                    Funding f = new Funding();
                    updateFunding(f, importDataModel, splits.get(i), entry.getKey(), separateFundingDate, donors.get(i).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue, addDisbursementForCommitment);
                    
                    fundings.add(f);
                }
            }

        } else {
            List<DonorOrganization> donors = new ArrayList<>(importDataModel.getDonor_organization());
            List<Double> splits = splitAmounts(value != null ? value.doubleValue() : 0.0, donors.size());
            for (int i = 0; i < donors.size(); i++) {
                Funding f = new Funding();
                updateFunding(f, importDataModel, splits.get(i), entry.getKey(), separateFundingDate, donors.get(i).getOrganization(), typeOfAss, finInstrument, commitment, disbursement, expenditure, adjustmentType, currencyCode, componentName, exchangeRateValue, addDisbursementForCommitment);
                fundings.add(f);
            }
        }
        return fundings;
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
            logger.error("Error getting cell {} value: ", cell);
            return nullable ? null : "";
        }
    }

    public static Number getNumericValueFromCell(Cell cell) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String raw = cell.getStringCellValue().trim().replace(",", "");
                if (!raw.isEmpty()) {
                    return Double.parseDouble(raw);
                }
                return 0;
            }
            return cell.getNumericCellValue();
        } catch (Exception e) {
            logger.error("Error getting cell {} value: ", cell);
            return 0;
        }
    }

    private static String getDateFromExcel(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return extractDateFromStringCell(cell);

    }

    public static String extractDateFromStringCell(Cell cell) {
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
            return LocalDate.of(year, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        LocalDate date = LocalDate.now();
        if (isCommonDateFormat(dateString)) {
            List<DateTimeFormatter> formatters = Arrays.asList(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("d/M/yyyy"),
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


    public static String formatDateFromDateObject(String date) {
        List<SimpleDateFormat> formatters = Arrays.asList(
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("d/M/yyyy"),
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
                "d/M/yyyy",
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

    /**
     * Opens an OOXML workbook with fallback support for Strict OOXML files.
     * Older Apache POI versions cannot open strict workbooks directly.
     */
    public static Workbook openWorkbookWithStrictFallback(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return openWorkbookWithStrictFallback(is);
        }
    }

    /**
     * Opens an OOXML workbook with fallback support for Strict OOXML files.
     * Caller is responsible for closing the returned workbook.
     */
    public static Workbook openWorkbookWithStrictFallback(InputStream inputStream) throws IOException {
        byte[] sourceBytes = toByteArray(inputStream);
        try {
            return new XSSFWorkbook(new ByteArrayInputStream(sourceBytes));
        } catch (POIXMLException e) {
            if (!isStrictOoxmlException(e)) {
                throw e;
            }
            logger.warn("Strict OOXML detected, attempting namespace conversion fallback.");
            byte[] convertedBytes = convertStrictOoxmlToTransitional(sourceBytes);
            return new XSSFWorkbook(new ByteArrayInputStream(convertedBytes));
        }
    }

    private static boolean isStrictOoxmlException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String msg = current.getMessage();
            if (msg != null && msg.contains("Strict OOXML isn't currently supported")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private static byte[] convertStrictOoxmlToTransitional(byte[] sourceBytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(sourceBytes));
             ZipOutputStream zos = new ZipOutputStream(out)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                ZipEntry newEntry = new ZipEntry(entry.getName());
                zos.putNextEntry(newEntry);

                byte[] entryBytes = toByteArray(zis);
                if (!entry.isDirectory() && isXmlLikeEntry(entry.getName())) {
                    String xml = new String(entryBytes, java.nio.charset.StandardCharsets.UTF_8);
                    xml = strictToTransitionalNamespaces(xml);
                    entryBytes = xml.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                }

                zos.write(entryBytes);
                zos.closeEntry();
                zis.closeEntry();
            }
            zos.finish();
        }
        return out.toByteArray();
    }

    private static boolean isXmlLikeEntry(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        return lower.endsWith(".xml") || lower.endsWith(".rels");
    }

    private static String strictToTransitionalNamespaces(String xml) {
        String converted = xml;
        converted = converted.replace("http://purl.oclc.org/ooxml/spreadsheetml/main", "http://schemas.openxmlformats.org/spreadsheetml/2006/main");
        converted = converted.replace("http://purl.oclc.org/ooxml/officeDocument/relationships", "http://schemas.openxmlformats.org/officeDocument/2006/relationships");
        converted = converted.replace("http://purl.oclc.org/ooxml/drawingml/main", "http://schemas.openxmlformats.org/drawingml/2006/main");
        converted = converted.replace("http://purl.oclc.org/ooxml/wordprocessingml/main", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");
        converted = converted.replace("http://purl.oclc.org/ooxml/presentationml/main", "http://schemas.openxmlformats.org/presentationml/2006/main");
        return converted;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }


        private static Funding updateFunding(Funding fundingItem, ImportDataModel importDataModel, Number amount, String columnHeaderContainingYear, String separateFundingDate, Long orgId, String assistanceType, String finInst, boolean commitment, boolean disbursement, boolean expenditure, String
            adjustmentType, String currencyCode, String componentName, Double exchangeRate, boolean addDisbursementForCommitment) {
        // TODO: 27/06/2024 pick Month from file and use it in funding
        Session session = getSession();
        Long currencyId = getCurrencyId(session, currencyCode);
        Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, adjustmentType);
        Long assType = getCategoryValue("assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, assistanceType);
        Long finInstrument = getCategoryValue("finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, finInst);
        Long orgRole = getOrganizationRole(session);


        String yearString;
        String fundingDate;
        boolean inferredFundingDate = false;
        if (separateFundingDate != null) {
            if (isCommonDateFormat(separateFundingDate)) {
                fundingDate = getFundingDate(separateFundingDate);
            } else {
                yearString = findYearSubstring(separateFundingDate);
                if (yearString != null) {
                    fundingDate = getFundingDate(yearString);
                } else {
                    fundingDate = getFundingDate(null);
                    inferredFundingDate = true;
                }

            }
        } else {
            yearString = findYearSubstring(columnHeaderContainingYear);
            if (yearString != null) {
                fundingDate = getFundingDate(yearString);
            } else {
                fundingDate = getFundingDate(null);
                inferredFundingDate = true;
            }

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
        transaction.setInferredTransactionDate(inferredFundingDate);
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

        if (addDisbursementForCommitment && commitment && !disbursement) {
            createCorrespondingDisbursement(fundingItem, adjustmentType);
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
            ((existing.isInferredTransactionDate() || newTransaction.isInferredTransactionDate())
                    || Objects.equals(existing.getTransaction_date(), newTransaction.getTransaction_date())) &&
            Objects.equals(existing.getAdjustment_type(), newTransaction.getAdjustment_type())
        );
    }

    private static Set<Funding> mergeFundingsByDonor(Set<Funding> fundings) {
        if (fundings == null || fundings.size() <= 1) return fundings;
        Map<String, Funding> merged = new LinkedHashMap<>();
        for (Funding f : fundings) {
            String key = f.getDonor_organization_id() + "|"
                    + f.getSource_role() + "|"
                    + f.getType_of_assistance() + "|"
                    + f.getFinancing_instrument();
            Funding existing = merged.get(key);
            if (existing == null) {
                merged.put(key, f);
            } else {
                for (Transaction t : f.getCommitments()) {
                    if (!transactionExists(existing.getCommitments(), t)) existing.getCommitments().add(t);
                }
                for (Transaction t : f.getDisbursements()) {
                    if (!transactionExists(existing.getDisbursements(), t)) existing.getDisbursements().add(t);
                }
                for (Transaction t : f.getExpenditures()) {
                    if (!transactionExists(existing.getExpenditures(), t)) existing.getExpenditures().add(t);
                }
            }
        }
        return new LinkedHashSet<>(merged.values());
    }

    /**
     * Creates a corresponding disbursement transaction for each commitment transaction.
     * The disbursement will have the same amount, currency, and date as the commitment.
     * @param funding The funding object containing commitments
     * @param adjustmentType The adjustment type ("actual" or "planned")
     */
    private static void createCorrespondingDisbursement(Funding funding, String adjustmentType) {
        if (funding == null || funding.getCommitments() == null || funding.getCommitments().isEmpty()) {
            return;
        }
        
        for (Transaction commitment : funding.getCommitments()) {
            Transaction disbursement = new Transaction();
            disbursement.setCurrency(commitment.getCurrency());
            disbursement.setTransaction_amount(commitment.getTransaction_amount());
            disbursement.setTransaction_date(commitment.getTransaction_date());
            disbursement.setInferredTransactionDate(commitment.isInferredTransactionDate());
            disbursement.setFixed_exchange_rate(commitment.getFixed_exchange_rate());
            
            // Set the adjustment type for disbursement
            Session session = getSession();
            Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, adjustmentType);
            disbursement.setAdjustment_type(adjType);
            
            // Add the disbursement if it doesn't already exist
            if (!transactionExists(funding.getDisbursements(), disbursement)) {
                funding.getDisbursements().add(disbursement);
                logger.info("Created corresponding disbursement transaction: amount={}, date={}, currency={}", 
                    disbursement.getTransaction_amount(), disbursement.getTransaction_date(), disbursement.getCurrency());
            }
        }
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
     * Looks up a category value ID by its category key and value name.
     * @param categoryKey the category class key (e.g. "procurement_system")
     * @param valueName the value name from the file (e.g. "National Competitive Bidding")
     * @param session current Hibernate session
     * @return category value id, or null if not found
     */
    public static Long getCategoryValueByName(String categoryKey, String valueName, Session session) {
        if (valueName == null || valueName.trim().isEmpty()) {
            return null;
        }
        String cacheKey = "catVal_" + categoryKey + "_" + valueName;
        if (ConstantsMap.containsKey(cacheKey)) {
            return ConstantsMap.get(cacheKey);
        }
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s JOIN s.ampCategoryClass c WHERE c.keyName = :categoryKey";
        Query query = session.createQuery(hql);
        query.setParameter("categoryKey", categoryKey);
        List<?> values = query.list();
        for (Object val : values) {
            AmpCategoryValue cv = (AmpCategoryValue) val;
            if (cv.getValue() != null && cv.getValue().trim().equalsIgnoreCase(valueName.trim())) {
                ConstantsMap.put(cacheKey, cv.getId());
                logger.info("Found category value: " + cv.getValue() + " (id=" + cv.getId() + ") for key=" + categoryKey);
                return cv.getId();
            }
        }
        logger.warn("Category value not found for key=" + categoryKey + ", value=" + valueName);
        return null;
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
    public static void setStatus(ImportDataModel importDataModel, boolean validateActivities) {
        setStatus(importDataModel, validateActivities, null);
    }

    /**
     * Sets default activity status and approval status on the import model.
     * If activity_status is already set (e.g. from Project Status column), it is left unchanged.
     */
    public static void setStatus(ImportDataModel importDataModel, boolean validateActivities, Long defaultActivityStatusId) {
        if (importDataModel.getActivity_status() == null) {
            Long statusId = defaultActivityStatusId != null
                    ? defaultActivityStatusId
                    : getCategoryValue("statusId", CategoryConstants.ACTIVITY_STATUS_KEY, "");
            importDataModel.setActivity_status(statusId);
        }
        if (validateActivities) {
            logger.info("validateActivities=true: approval status will be derived during activity import");
            importDataModel.setApproval_status(null);
        } else {
            importDataModel.setApproval_status(ApprovalStatus.started.getId());
        }
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
    public static Long importTheData(ImportDataModel importDataModel, Session session, ImportedProject importedProject, String componentName, String componentCode, Long responsibleOrgId, List<Funding> fundings, Long existingActivityId, boolean validateActivities, boolean replaceExistingTransactions, boolean replaceExistingLocations) throws JsonProcessingException {
        if (session == null || !session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }

        // Re-fetch existing activity in this transaction if ID is provided to avoid detached entity issues
        AmpActivityVersion existing = null;
        if (existingActivityId != null) {
            existing = session.get(AmpActivityVersion.class, existingActivityId);
        }
        // Let ActivityImporter/ActivityUtil derive approval fields during prepareToSave().
        // Data importer payload approval fields are too easy to drift from the server-side rules.
        ActivityImportRules rules = new ActivityImportRules(true, false,
                true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(ESCAPE_NON_ASCII, false); // Disable escaping of non-ASCII characters during serialization
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        importDataModel.setFundings(mergeFundingsByDonor(importDataModel.getFundings()));
        pruneParentLocationsWhenChildPresent(importDataModel, session);
        ensureImplementationLevelWhenHasLocations(importDataModel, session);
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
            if (validateActivities) {
                logger.info("validateActivities=true: approval fields will be derived by ActivityImporter");
            }
            logger.info("New activity");
            importedProject.setNewProject(true);
            response = ActivityInterchangeUtils.importActivity(map, false, rules, "dataimporter/activity/new");
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
            updateFundingOrgsAndSectorsWithAlreadyExisting(existing, importDataModel, replaceExistingTransactions);
            // Stamp existing activity-location row ids onto imported locations so matching rows update in place,
            // then optionally merge the rest of the existing rows when replace is not requested.
            mergeExistingActivityLocationsIntoImport(existing, importDataModel, replaceExistingLocations);
            // Preserve existing programs with their DB IDs so the API updates in-place; avoids both
            // StaleStateException (delete+insert) and SizeValidator failures on re-validation
            preserveExistingPrograms(existing, importDataModel);
            pruneParentLocationsWhenChildPresent(importDataModel, session);
            ensureImplementationLevelWhenHasLocations(importDataModel, session);
            normalizeLocationPercentages(importDataModel);
            if (replaceExistingLocations) {
                failIfReplaceExistingLocationsWouldRemoveIndicatorLinkedRows(existing, importDataModel);
            }
            map = objectMapper
                    .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {
                    });
            // Remove null values and "null" strings from the map to avoid API validation errors
            map.entrySet().removeIf(entry -> entry.getValue() == null || "null".equals(String.valueOf(entry.getValue())));

            map.remove("indicators"); // preserve existing indicators; we append in addIndicatorDataToActivity
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
            if (validateActivities) {
                logger.info("validateActivities=true: approval fields will be derived by ActivityImporter");
            }
            // All data from 'existing' has been extracted into 'map'. Clear the session first-level
            // cache before handing off to ActivityGatekeeper so its internal doInTransaction starts
            // with a clean context. Without this, entities loaded above remain in the session action
            // queue and Hibernate raises HHH000099 (possible non-threadsafe access to session) when
            // EntityInsertAction.execute() checks the persistence context during flush.
            session.clear();
            try {
                response = ActivityInterchangeUtils.importActivity(map, true, rules, "dataimporter/activity/update");
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

    private static void updateFundingOrgsAndSectorsWithAlreadyExisting(AmpActivityVersion ampActivityVersion, ImportDataModel importDataModel, boolean replaceExistingTransactions) {

        if (!replaceExistingTransactions && ampActivityVersion.getFunding() != null) {
            Hibernate.initialize(ampActivityVersion.getFunding());
            Long adjType = getCategoryValue("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY, "");
            Long assType = getCategoryValue("assistanceType", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, "");
            Long finInstrument = getCategoryValue("finInstrument", CategoryConstants.FINANCING_INSTRUMENT_KEY, "");
            if (importDataModel.getFundings() == null) importDataModel.setFundings(new HashSet<>());
            for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {
                Long existingId = ampFunding.getAmpFundingId();
                Long donorOrgId = ampFunding.getAmpDonorOrgId().getAmpOrgId();
                Long typeOfAssistance = ampFunding.getTypeOfAssistance() != null ? ampFunding.getTypeOfAssistance().getId() : assType;
                Long financingInstrument = ampFunding.getFinancingInstrument() != null ? ampFunding.getFinancingInstrument().getId() : finInstrument;
                Long sourceRole = ampFunding.getSourceRole().getAmpRoleId();

                // Check if the Excel importer already added a new funding entry (no ID yet) for the same
                // donor+role+type combination. If so, mark it with the existing funding_id so the API
                // updates the existing DB record instead of inserting a duplicate.
                Funding matchedNewFunding = null;
                for (Funding newFunding : importDataModel.getFundings()) {
                    if (newFunding.getFunding_id() == null
                            && Objects.equals(donorOrgId, newFunding.getDonor_organization_id())
                            && Objects.equals(sourceRole, newFunding.getSource_role())
                            && Objects.equals(typeOfAssistance, newFunding.getType_of_assistance())
                            && Objects.equals(financingInstrument, newFunding.getFinancing_instrument())) {
                        matchedNewFunding = newFunding;
                        break;
                    }
                }

                if (matchedNewFunding != null) {
                    // Stamp the existing DB funding_id so the API updates the existing record instead of inserting.
                    matchedNewFunding.setFunding_id(existingId);
                    // Also merge the existing DB transactions into the Excel entry so the API's
                    // removeByIdExcept doesn't delete them (DB transactions carry transaction_id;
                    // the Excel transactions have none, so they'd be the only ones in jsonIds={null},
                    // causing all DB transactions to be removed on flush).
                    // transactionExists guards against re-adding a transaction already supplied by Excel.
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
                                if (!transactionExists(matchedNewFunding.getCommitments(), transaction)) {
                                    matchedNewFunding.getCommitments().add(transaction);
                                }
                            } else if (ampFundingDetail.getTransactionType() == 1) {
                                if (!transactionExists(matchedNewFunding.getDisbursements(), transaction)) {
                                    matchedNewFunding.getDisbursements().add(transaction);
                                }
                            } else if (ampFundingDetail.getTransactionType() == 2) {
                                if (!transactionExists(matchedNewFunding.getExpenditures(), transaction)) {
                                    matchedNewFunding.getExpenditures().add(transaction);
                                }
                            }
                        }
                    }
                    continue;
                }

                // No Excel entry for this existing funding — preserve it so it is not deleted.
                Funding funding = new Funding();
                if (existingId != null) funding.setFunding_id(existingId);
                funding.setDonor_organization_id(donorOrgId);
                funding.setType_of_assistance(typeOfAssistance);
                funding.setFinancing_instrument(financingInstrument);
                funding.setSource_role(sourceRole);
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
                        } else if (ampFundingDetail.getTransactionType() == 2) {
                            funding.getExpenditures().add(transaction);
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
                Long orgId = ampOrgRole.getOrganisation().getAmpOrgId();
                Long orgRoleId = ampOrgRole.getAmpOrgRoleId();
                if (roleCode.equalsIgnoreCase("DN")) {
                    createDonorOrg(importDataModel, orgId, orgRoleId);
                } else if (roleCode.equalsIgnoreCase("RO")) {
                    Organization org = new Organization();
                    org.setOrganization(orgId);
                    if (orgRoleId != null) org.setId(orgRoleId);
                    importDataModel.getResponsible_organization().add(org);
                } else if (roleCode.equalsIgnoreCase("BA")) {
                    Organization org = new Organization();
                    org.setOrganization(orgId);
                    if (orgRoleId != null) org.setId(orgRoleId);
                    importDataModel.getBeneficiary_agency().add(org);
                } else if (roleCode.equalsIgnoreCase("EA")) {
                    Organization org = new Organization();
                    org.setOrganization(orgId);
                    if (orgRoleId != null) org.setId(orgRoleId);
                    importDataModel.getExecuting_agency().add(org);
                } else if (roleCode.equalsIgnoreCase("IA")) {
                    createImplementingAgency(importDataModel, orgId, orgRoleId);
                } else if (roleCode.equalsIgnoreCase("CA")) {
                    Organization org = new Organization();
                    org.setOrganization(orgId);
                    if (orgRoleId != null) org.setId(orgRoleId);
                    importDataModel.getContracting_agency().add(org);
                }
            }
        }

        if (ampActivityVersion.getSectors() != null && !ampActivityVersion.getSectors().isEmpty()) {
            Hibernate.initialize(ampActivityVersion.getSectors());
            for (AmpActivitySector ampActivitySector : ampActivityVersion.getSectors()) {
                if (ampActivitySector.getSectorId() == null) continue;
                boolean primary = ampActivitySector.getClassificationConfig() != null && ampActivitySector.getClassificationConfig().isPrimary();
                createSector(importDataModel, primary, ampActivitySector.getSectorId().getAmpSectorId(), ampActivitySector.getAmpActivitySectorId());
            }
        }
    }

    /**
     * For an existing activity, merges its current locations into the import payload so we only add locations
     * (row locations + existing), never remove. Any existing activity location not already in importDataModel
     * is added. This avoids activity/update deleting locations (e.g. those referenced by indicator connections).
     */
    private static void preserveExistingPrograms(AmpActivityVersion existing, ImportDataModel importDataModel) {
        if (existing == null || importDataModel == null) return;
        Set<AmpActivityProgram> actPrograms = existing.getActPrograms();
        if (actPrograms == null || actPrograms.isEmpty()) return;
        Hibernate.initialize(actPrograms);
        for (AmpActivityProgram ap : actPrograms) {
            if (ap.getProgram() == null || ap.getProgram().getAmpThemeId() == null) continue;
            Program p = new Program();
            p.setId(ap.getAmpActivityProgramId());
            p.setProgram(ap.getProgram().getAmpThemeId());
            String settingName = ap.getProgramSetting() != null ? ap.getProgramSetting().getName() : null;
            if (ProgramUtil.NATIONAL_PLAN_OBJECTIVE.equals(settingName)) {
                importDataModel.getNational_plan_objective().add(p);
            } else if (ProgramUtil.PRIMARY_PROGRAM.equals(settingName)) {
                importDataModel.getPrimary_programs().add(p);
            } else if (ProgramUtil.SECONDARY_PROGRAM.equals(settingName)) {
                importDataModel.getSecondary_programs().add(p);
            } else if (ProgramUtil.TERTIARY_PROGRAM.equals(settingName)) {
                importDataModel.getTertiary_programs().add(p);
            }
        }
    }

    private static void mergeExistingActivityLocationsIntoImport(AmpActivityVersion existing,
                                                                 ImportDataModel importDataModel,
                                                                 boolean replaceExistingLocations) {
        if (existing == null || importDataModel == null) return;
        if (existing.getLocations() == null) return;
        Hibernate.initialize(existing.getLocations());
        Map<Long, AmpActivityLocation> existingByLocationId = new HashMap<>();
        for (AmpActivityLocation aal : existing.getLocations()) {
            if (aal == null || aal.getLocation() == null || aal.getLocation().getId() == null) {
                continue;
            }
            existingByLocationId.putIfAbsent(aal.getLocation().getId(), aal);
        }
        Set<Long> alreadyInImport = new HashSet<>();
        if (importDataModel.getLocations() != null) {
            for (Location loc : importDataModel.getLocations()) {
                if (loc != null && loc.getLocation() != null) {
                    alreadyInImport.add(loc.getLocation());
                    AmpActivityLocation existingLocation = existingByLocationId.get(loc.getLocation());
                    if (existingLocation != null && loc.getId() == null && existingLocation.getId() != null) {
                        loc.setId(existingLocation.getId());
                    }
                }
            }
        }
        if (replaceExistingLocations) {
            return;
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

    private static void failIfReplaceExistingLocationsWouldRemoveIndicatorLinkedRows(AmpActivityVersion existing,
                                                                                     ImportDataModel importDataModel) {
        if (existing == null || importDataModel == null) {
            return;
        }

        Set<Long> keptActivityLocationIds = new HashSet<>();
        if (importDataModel.getLocations() != null) {
            for (Location location : importDataModel.getLocations()) {
                if (location != null && location.getId() != null) {
                    keptActivityLocationIds.add(location.getId());
                }
            }
        }

        Set<Long> indicatorLinkedLocationIds = getIndicatorLinkedActivityLocationIds(existing);
        indicatorLinkedLocationIds.removeAll(keptActivityLocationIds);
        if (!indicatorLinkedLocationIds.isEmpty()) {
            throw new IllegalStateException("Cannot replace existing locations because one or more existing locations are referenced by indicator data. Keep those locations in the import or clear indicator data first.");
        }
    }

    private static Set<Long> getIndicatorLinkedActivityLocationIds(AmpActivityVersion existing) {
        Set<Long> activityLocationIds = new HashSet<>();
        if (existing == null || existing.getIndicators() == null) {
            return activityLocationIds;
        }

        Hibernate.initialize(existing.getIndicators());
        for (IndicatorActivity indicatorActivity : existing.getIndicators()) {
            if (indicatorActivity == null) {
                continue;
            }
            if (indicatorActivity.getActivityLocation() != null && indicatorActivity.getActivityLocation().getId() != null) {
                activityLocationIds.add(indicatorActivity.getActivityLocation().getId());
            }
            if (indicatorActivity.getValues() == null) {
                continue;
            }
            Hibernate.initialize(indicatorActivity.getValues());
            for (AmpIndicatorValue value : indicatorActivity.getValues()) {
                if (value != null && value.getActivityLocation() != null && value.getActivityLocation().getId() != null) {
                    activityLocationIds.add(value.getActivityLocation().getId());
                }
            }
        }

        return activityLocationIds;
    }

    /**
     * Scales location percentages so they sum to 100, as required by activity validation.
     * If there are no locations or sum is 0, does nothing.
     */
    private static void normalizeLocationPercentages(ImportDataModel importDataModel) {
        if (importDataModel == null || importDataModel.getLocations() == null || importDataModel.getLocations().isEmpty())
            return;
        Set<Location> locs = importDataModel.getLocations();
        // Deduplicate by location id first; the API effectively treats location rows as unique by location,
        // so assigning percentages before collapsing duplicates can leave totals below 100 after validation.
        Map<Long, Location> uniqueLocations = new LinkedHashMap<>();
        for (Location loc : locs) {
            if (loc != null && loc.getLocation() != null) {
                Location existing = uniqueLocations.get(loc.getLocation());
                if (existing == null || (existing.getId() == null && loc.getId() != null)) {
                    uniqueLocations.put(loc.getLocation(), loc);
                }
            }
        }
        List<Location> foundLocations = new ArrayList<>(uniqueLocations.values());
        if (foundLocations.isEmpty()) {
            return;
        }

        Map<Integer, Float> percentages = divide100(foundLocations.size());
        for (int i = 0; i < foundLocations.size(); i++) {
            foundLocations.get(i).setLocation_percentage((double)percentages.get(i));
        }
        importDataModel.setLocations(new LinkedHashSet<>(foundLocations));
    }

    /**
     * Removes broader locations when more specific ones are present in payload.
     * AMP validation rejects parent+child combinations, and importer rows that mix
     * administrative layers should keep the most specific layer.
     */
    private static void pruneParentLocationsWhenChildPresent(ImportDataModel importDataModel, Session session) {
        if (importDataModel == null || importDataModel.getLocations() == null || importDataModel.getLocations().size() < 2) {
            return;
        }
        if (session == null || !session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }

        Set<Long> selectedLocationIds = new HashSet<>();
        for (Location loc : importDataModel.getLocations()) {
            if (loc != null && loc.getLocation() != null) {
                selectedLocationIds.add(loc.getLocation());
            }
        }
        if (selectedLocationIds.size() < 2) {
            return;
        }

        Set<Long> ancestorsToRemove = new HashSet<>();
        for (Long locId : selectedLocationIds) {
            AmpCategoryValueLocations current = session.get(AmpCategoryValueLocations.class, locId);
            while (current != null && current.getParentLocation() != null) {
                current = current.getParentLocation();
                if (current != null && current.getId() != null && selectedLocationIds.contains(current.getId())) {
                    ancestorsToRemove.add(current.getId());
                }
            }
        }
        Set<Location> filteredLocations = new LinkedHashSet<>();
        for (Location loc : importDataModel.getLocations()) {
            if (loc == null || loc.getLocation() == null || !ancestorsToRemove.contains(loc.getLocation())) {
                filteredLocations.add(loc);
            }
        }

        int mostSpecificLayerIndex = Integer.MIN_VALUE;
        Set<Integer> layerIndexes = new HashSet<>();
        Map<Long, Integer> layerIndexByLocationId = new HashMap<>();
        for (Location loc : filteredLocations) {
            if (loc == null || loc.getLocation() == null) {
                continue;
            }
            AmpCategoryValueLocations current = session.get(AmpCategoryValueLocations.class, loc.getLocation());
            if (current == null || current.getParentCategoryValue() == null
                    || current.getParentCategoryValue().getIndex() == null) {
                continue;
            }
            int layerIndex = current.getParentCategoryValue().getIndex();
            layerIndexes.add(layerIndex);
            layerIndexByLocationId.put(loc.getLocation(), layerIndex);
            if (layerIndex > mostSpecificLayerIndex) {
                mostSpecificLayerIndex = layerIndex;
            }
        }

        if (layerIndexes.size() > 1) {
            Set<Location> mostSpecificLocations = new LinkedHashSet<>();
            for (Location loc : filteredLocations) {
                if (loc == null || loc.getLocation() == null) {
                    mostSpecificLocations.add(loc);
                    continue;
                }
                Integer layerIndex = layerIndexByLocationId.get(loc.getLocation());
                if (layerIndex == null || layerIndex == mostSpecificLayerIndex) {
                    mostSpecificLocations.add(loc);
                }
            }
            filteredLocations = mostSpecificLocations;
        }

        importDataModel.setLocations(filteredLocations);
    }

    /**
     * When the payload has locations, derive implementation location and level
     * from the selected locations (e.g. after merging locations for existing activity).
     */
    private static void ensureImplementationLevelWhenHasLocations(ImportDataModel importDataModel, Session session) {
        if (importDataModel == null || importDataModel.getLocations() == null || importDataModel.getLocations().isEmpty())
            return;
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


    public static void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary,
                                     String subSector, boolean createMissingSectors, String importerSectorField) {
        if (subSector!=null && !subSector.isEmpty())
        {
            name = subSector;
        }
        for (String sectorName : splitMultipleValues(name)) {
            updateSingleSector(importDataModel, sectorName, session, primary, createMissingSectors, importerSectorField);
        }
    }

    private static void updateSingleSector(ImportDataModel importDataModel, String name, Session session, boolean primary,
                                           boolean createMissingSectors, String importerSectorField) {
        if (ConstantsMap.containsKey("sector_" + name)) {
            Long sectorId = ConstantsMap.get("sector_" + name);
            logger.info("In cache... sector " + "sector_" + name + ":" + sectorId);
            createSector(importDataModel, primary, sectorId);
        } else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }

            String finalName = name;
            String classificationName = getClassificationNameForImporterField(importerSectorField, primary);
            final Long[] foundSectorId = new Long[1];
            session.doWork(connection -> {
                String query = primary
                        ? "SELECT ams.amp_sector_id AS amp_sector_id, ams.name AS name " +
                        "FROM amp_sector ams " +
                        "JOIN amp_classification_config acc ON ams.amp_sec_scheme_id = acc.classification_id " +
                        "WHERE LOWER(ams.name) = LOWER(?) " +
                    "AND (acc.is_primary_sector = TRUE OR LOWER(acc.name) = LOWER(?))"
                        : "SELECT ams.amp_sector_id AS amp_sector_id, ams.name AS name " +
                        "FROM amp_sector ams " +
                        "JOIN amp_classification_config acc ON ams.amp_sec_scheme_id = acc.classification_id " +
                        "WHERE LOWER(ams.name) = LOWER(?) " +
                        "AND LOWER(acc.name) = LOWER(?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    // Set the name as a parameter to the prepared statement
                    statement.setString(1, finalName);
                    statement.setString(2, classificationName);


                    // Execute the query and process the results
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Long ampSectorId = resultSet.getLong("amp_sector_id");
                            foundSectorId[0] = ampSectorId;
                            createSector(importDataModel, primary, ampSectorId);
                            ConstantsMap.put("sector_" + finalName, ampSectorId);
                        }
                    }

                } catch (SQLException e) {
                    logger.error("Error getting sectors", e);
                }
            });

            if (foundSectorId[0] == null && createMissingSectors) {
                Long createdSectorId = createMissingSector(finalName, session, primary, importerSectorField);
                if (createdSectorId != null) {
                    createSector(importDataModel, primary, createdSectorId);
                    ConstantsMap.put("sector_" + finalName, createdSectorId);
                }
            }
        }
    }

    private static Long createMissingSector(String name, Session session, boolean primary, String importerSectorField) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String classificationName = getClassificationNameForImporterField(importerSectorField, primary);
        AmpClassificationConfiguration classificationConfig = resolveClassificationConfig(session, primary, classificationName);
        if (classificationConfig == null || classificationConfig.getClassification() == null) {
            logger.warn("No classification configuration found for {} sector; cannot create '{}'.",
                    classificationName, name);
            return null;
        }

        try {
            AmpSector newSector = new AmpSector();
            newSector.setParentSectorId(null);
            newSector.setAmpOrgId(null);
            newSector.setAmpSecSchemeId(SectorUtil.getAmpSectorScheme(
                    classificationConfig.getClassification().getAmpSecSchemeId()));
            newSector.setSectorCode("101");
            newSector.setSectorCodeOfficial(name);
            newSector.setName(name);
            newSector.setDescription(" ");
            // Keep the source sector type (Primary/Secondary/etc.) based on the mapped import column.
            newSector.setType(classificationConfig.getName());
            newSector.setLanguage(null);
            newSector.setVersion(null);
            newSector.setDeleted(false);
            DbUtil.add(newSector);
            logger.info("Created missing {} sector '{}' with id={}",
                    classificationConfig.getName(), name, newSector.getAmpSectorId());
            return newSector.getAmpSectorId();
        } catch (Exception e) {
            logger.error("Failed creating missing sector '{}': {}", name, e.getMessage(), e);
            return null;
        }
    }

    private static AmpClassificationConfiguration resolveClassificationConfig(Session session, boolean primary,
                                                                              String classificationName) {
        String hql = primary
                ? "FROM " + AmpClassificationConfiguration.class.getName() + " c WHERE c.primary = true"
                : "FROM " + AmpClassificationConfiguration.class.getName() + " c WHERE LOWER(c.name) = LOWER(:name)";
        Query query = session.createQuery(hql);
        if (!primary) {
            query.setParameter("name", classificationName, StringType.INSTANCE);
        }
        query.setMaxResults(1);
        return (AmpClassificationConfiguration) query.uniqueResult();
    }

    private static String getClassificationNameForImporterField(String importerSectorField, boolean primary) {
        if (ImporterConstants.PRIMARY_SECTOR.equals(importerSectorField)) {
            return AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME;
        }
        if (ImporterConstants.SECONDARY_SECTOR.equals(importerSectorField)) {
            return AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME;
        }
        return primary
                ? AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME
                : AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME;
    }

    private static List<String> splitMultipleValues(String value) {
        return splitMultipleValues(value, "[;\\u061B\\uFF1B]");
    }

    private static List<String> splitMultipleValues(String value, String separatorRegex) {
        if (value == null || value.trim().isEmpty()) return Collections.emptyList();
        List<String> result = new ArrayList<>();
        // Support standard and locale-specific semicolons used in spreadsheet exports.
        for (String part : value.split(separatorRegex)) {
            String trimmed = part.trim();
            if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
                trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
            }
            if (!trimmed.isEmpty()) result.add(trimmed);
        }
        return result;
    }

    public static void updateLocations(ImportDataModel importDataModel, String locationNames, Session session) {
        logger.info("Updating locations");
        if (locationNames == null || locationNames.trim().isEmpty()) return;
        for (String locationName : splitMultipleValues(locationNames, "[,;\\u061B\\uFF1B]")) {
            String normalizedLocationName = normalizeLocationNameForLookup(locationName);
            if (normalizedLocationName.isEmpty()) {
                continue;
            }
            Long cachedLocationId = ConstantsMap.get("location_" + normalizedLocationName);
            if (cachedLocationId == null) {
                cachedLocationId = ConstantsMap.get("location_" + locationName);
            }
            if (cachedLocationId != null) {
                Long location = cachedLocationId;
                logger.info("In cache... location " + "location_" + normalizedLocationName + ":" + location);
                importDataModel.getLocations().add(new Location(location, 100.00));

            } else {
                if (!session.isOpen()) {
                    session = PersistenceManager.getRequestDBSession();
                }

                Set<Long> resolvedLocationIds = resolveLocationIdsByName(session, normalizedLocationName);
                if (resolvedLocationIds.isEmpty()) {
                    logger.warn("Location not found for importer value '{}'", normalizedLocationName);
                    continue;
                }
                for (Long location : resolvedLocationIds) {
                    logger.info("Location:" + location);
                    importDataModel.getLocations().add(new Location(location, 100.00));
                    ConstantsMap.put("location_" + locationName, location);
                    ConstantsMap.put("location_" + normalizedLocationName, location);
                }
            }
        }
        updateImpLevels(importDataModel, session);
    }

    private static String normalizeLocationNameForLookup(String rawLocationName) {
        if (rawLocationName == null) {
            return "";
        }
        String normalized = rawLocationName
                .replace('\u00A0', ' ')
                .replace('\u202F', ' ')
                .trim();
        normalized = normalized.replaceAll("\\.{2,}$", "").trim();
        return normalized;
    }

    private static Set<Long> resolveLocationIdsByName(Session session, String locationName) {
        Set<Long> result = new LinkedHashSet<>();
        if (locationName == null || locationName.trim().isEmpty()) {
            return result;
        }

        if (session == null || !session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }

        final String exactName = locationName.trim();
        final String likeName = "%" + exactName + "%";

        session.doWork(connection -> {
            // 1) Exact match (fast path)
            String exactQuery = "SELECT acvl.id AS location_id FROM amp_category_value_location acvl WHERE LOWER(acvl.location_name) = LOWER(?)";
            try (PreparedStatement statement = connection.prepareStatement(exactQuery)) {
                statement.setString(1, exactName);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(rs.getLong("location_id"));
                    }
                }
            } catch (SQLException e) {
                logger.error("Error resolving location by exact name", e);
            }

            if (!result.isEmpty()) {
                return;
            }

            // 2) Normalized match (ignore punctuation/spacing differences, e.g. "Saint Louis" vs "Saint-Louis")
            String normalizedQuery = "SELECT acvl.id AS location_id "
                    + "FROM amp_category_value_location acvl "
                    + "WHERE LOWER(regexp_replace(acvl.location_name, '[^[:alnum:]]', '', 'g')) "
                    + "= LOWER(regexp_replace(?, '[^[:alnum:]]', '', 'g'))";
            try (PreparedStatement statement = connection.prepareStatement(normalizedQuery)) {
                statement.setString(1, exactName);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(rs.getLong("location_id"));
                    }
                }
            } catch (SQLException e) {
                logger.error("Error resolving location by normalized name", e);
            }

            if (!result.isEmpty()) {
                return;
            }

            // 3) Loose contains match as last fallback.
            String containsQuery = "SELECT acvl.id AS location_id FROM amp_category_value_location acvl WHERE LOWER(acvl.location_name) LIKE LOWER(?)";
            try (PreparedStatement statement = connection.prepareStatement(containsQuery)) {
                statement.setString(1, likeName);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(rs.getLong("location_id"));
                    }
                }
            } catch (SQLException e) {
                logger.error("Error resolving location by contains search", e);
            }
        });

        return result;
    }

    public static void applyDefaultLocation(ImportDataModel importDataModel, Long locationId, Session session) {
        if (importDataModel == null || locationId == null) {
            return;
        }
        if (importDataModel.getLocations() == null) {
            importDataModel.setLocations(new HashSet<>());
        }
        boolean alreadyPresent = importDataModel.getLocations().stream()
                .filter(Objects::nonNull)
                .anyMatch(location -> Objects.equals(location.getLocation(), locationId));
        if (!alreadyPresent) {
            importDataModel.getLocations().add(new Location(locationId, 100.00));
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
        if (importDataModel == null) {
            return;
        }
        if (session == null || !session.isOpen()) {
            session = PersistenceManager.getRequestDBSession();
        }

        AmpCategoryValue resolvedImplementationLocation = resolveImplementationLocationForLocations(importDataModel, session);
        if (resolvedImplementationLocation != null && resolvedImplementationLocation.getId() != null) {
            importDataModel.setImplementation_location(resolvedImplementationLocation.getId());
        }

        Long resolved = resolveImplementationLevelForLocations(importDataModel, session, resolvedImplementationLocation);
        if (resolved != null) {
            importDataModel.setImplementation_level(resolved);
            return;
        }

        // Fallback when no locations are present: keep previous default behavior (National).
        if (ConstantsMap.containsKey("implementation_level_")) {
            Long implementationLevel = ConstantsMap.get("implementation_level_");
            logger.info("In cache... imp level " + "implementation_level:" + implementationLevel);
            importDataModel.setImplementation_level(implementationLevel);
        } else {
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
                }
            });
        }
    }

    private static AmpCategoryValue resolveImplementationLocationForLocations(ImportDataModel importDataModel,
                                                                              Session session) {
        if (importDataModel.getLocations() == null || importDataModel.getLocations().isEmpty()) {
            return null;
        }

        AmpCategoryValue resolvedImplementationLocation = null;
        Set<Long> implementationLocationIds = new LinkedHashSet<>();

        for (Location loc : importDataModel.getLocations()) {
            if (loc == null || loc.getLocation() == null) {
                continue;
            }
            AmpCategoryValueLocations location = session.get(AmpCategoryValueLocations.class, loc.getLocation());
            if (location == null || location.getParentCategoryValue() == null) {
                continue;
            }

            AmpCategoryValue implementationLocation = location.getParentCategoryValue();
            if (resolvedImplementationLocation == null) {
                resolvedImplementationLocation = implementationLocation;
            }
            if (implementationLocation.getId() != null) {
                implementationLocationIds.add(implementationLocation.getId());
            }
        }

        if (implementationLocationIds.isEmpty()) {
            return null;
        }
        if (implementationLocationIds.size() == 1) {
            return resolvedImplementationLocation;
        }

        return CategoryConstants.IMPLEMENTATION_LOCATION_ALL.getAmpCategoryValueFromDB();
    }

    private static Long resolveImplementationLevelForLocations(ImportDataModel importDataModel, Session session,
                                                               AmpCategoryValue implementationLocation) {
        if (importDataModel.getLocations() == null || importDataModel.getLocations().isEmpty()) {
            return null;
        }

        Long existingImplementationLevel = importDataModel.getImplementation_level();

        if (implementationLocation != null) {
            Set<Long> allowedByImplementationLocation = new LinkedHashSet<>();
            if (implementationLocation.getUsedValues() != null) {
                for (AmpCategoryValue level : implementationLocation.getUsedValues()) {
                    if (level != null && level.getId() != null) {
                        allowedByImplementationLocation.add(level.getId());
                    }
                }
            }

            if (existingImplementationLevel != null && allowedByImplementationLocation.contains(existingImplementationLevel)) {
                return existingImplementationLevel;
            }

            if (implementationLocation.getDefaultUsedValue() != null
                    && implementationLocation.getDefaultUsedValue().getId() != null
                    && allowedByImplementationLocation.contains(implementationLocation.getDefaultUsedValue().getId())) {
                return implementationLocation.getDefaultUsedValue().getId();
            }

            if (allowedByImplementationLocation.size() == 1) {
                return allowedByImplementationLocation.iterator().next();
            }
        }

        Set<Long> commonAllowedLevels = null;
        for (Location loc : importDataModel.getLocations()) {
            if (loc == null || loc.getLocation() == null) {
                continue;
            }
            Set<Long> allowedForLocation = getAllowedImplementationLevelsForLocation(loc.getLocation(), session);
            if (allowedForLocation.isEmpty()) {
                continue;
            }
            if (commonAllowedLevels == null) {
                commonAllowedLevels = new HashSet<>(allowedForLocation);
            } else {
                commonAllowedLevels.retainAll(allowedForLocation);
            }
        }

        if (commonAllowedLevels == null || commonAllowedLevels.isEmpty()) {
            return null;
        }

        if (existingImplementationLevel != null && commonAllowedLevels.contains(existingImplementationLevel)) {
            return existingImplementationLevel;
        }

        if (implementationLocation != null
                && implementationLocation.getDefaultUsedValue() != null
                && implementationLocation.getDefaultUsedValue().getId() != null
                && commonAllowedLevels.contains(implementationLocation.getDefaultUsedValue().getId())) {
            return implementationLocation.getDefaultUsedValue().getId();
        }

        // Prefer common hard-coded levels if available to keep behavior predictable.
        Long national = CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL.getIdInDatabase();
        if (national != null && commonAllowedLevels.contains(national)) {
            return national;
        }
        Long regional = CategoryConstants.IMPLEMENTATION_LEVEL_REGIONAL.getIdInDatabase();
        if (regional != null && commonAllowedLevels.contains(regional)) {
            return regional;
        }
        Long international = CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.getIdInDatabase();
        if (international != null && commonAllowedLevels.contains(international)) {
            return international;
        }

        return commonAllowedLevels.iterator().next();
    }

    private static Set<Long> getAllowedImplementationLevelsForLocation(Long locationId, Session session) {
        Set<Long> allowed = new HashSet<>();
        if (locationId == null) {
            return allowed;
        }
        AmpCategoryValueLocations location = session.get(AmpCategoryValueLocations.class, locationId);
        if (location == null || location.getParentCategoryValue() == null || location.getParentCategoryValue().getUsedValues() == null) {
            return allowed;
        }
        for (AmpCategoryValue level : location.getParentCategoryValue().getUsedValues()) {
            if (level != null && level.getId() != null) {
                allowed.add(level.getId());
            }
        }
        return allowed;
    }

    private static void createSector(ImportDataModel importDataModel, boolean primary, Long ampSectorId) {
        createSector(importDataModel, primary, ampSectorId, null);
    }

    private static void createSector(ImportDataModel importDataModel, boolean primary, Long ampSectorId, Long sectorPkId) {
        Sector sector1 = new Sector();
        sector1.setSector(ampSectorId);
        if (sectorPkId != null) sector1.setId(sectorPkId);
        if (primary) {
            boolean added = importDataModel.getPrimary_sectors().add(sector1);
            if (!added && sectorPkId != null) {
                // Sector already present from import row (id=null). Stamp the existing
                // activity-sector PK so the API updates rather than inserts.
                importDataModel.getPrimary_sectors().stream()
                        .filter(s -> Objects.equals(s.getSector(), ampSectorId))
                        .findFirst()
                        .ifPresent(s -> s.setId(sectorPkId));
            }
            Map<Integer, Float> percentages = divide100(importDataModel.getPrimary_sectors().size());
            int index = 0;
            for (Sector sec : importDataModel.getPrimary_sectors()) {
                sec.setSector_percentage(percentages.get(index));
                index++;
            }
        } else {
            boolean added = importDataModel.getSecondary_sectors().add(sector1);
            if (!added && sectorPkId != null) {
                importDataModel.getSecondary_sectors().stream()
                        .filter(s -> Objects.equals(s.getSector(), ampSectorId))
                        .findFirst()
                        .ifPresent(s -> s.setId(sectorPkId));
            }
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

    public static Long updateOrgs(ImportDataModel importDataModel, String name, String code, Session session, String type) {
        return updateOrgs(importDataModel, name, code, session, type, false, null, null, false);
    }

    public static Long updateOrgs(ImportDataModel importDataModel, String name, String code, Session session, String type, boolean createMissingOrgs, Long orgGroupId, String importedOrgGroupName, boolean createMissingOrgGroups)
    {
        List<String> names = splitMultipleValues(name);
        List<String> codes = splitMultipleValues(code);
        List<String> importedOrgGroups = splitMultipleValues(importedOrgGroupName);
        String fallbackOrgGroup = importedOrgGroups.isEmpty() ? null : importedOrgGroups.get(importedOrgGroups.size() - 1);
        Long lastOrgId = null;
        for (int i = 0; i < names.size(); i++) {
            String singleName = names.get(i);
            String singleCode = i < codes.size() ? codes.get(i) : null;
            String singleOrgGroupName = importedOrgGroups.isEmpty()
                    ? null
                    : (i < importedOrgGroups.size() ? importedOrgGroups.get(i) : fallbackOrgGroup);
            lastOrgId = updateSingleOrg(importDataModel, singleName, singleCode, session, type, createMissingOrgs, orgGroupId, singleOrgGroupName, createMissingOrgGroups);
        }
        return lastOrgId;
    }

    /**
     * Strips any parenthetical suffix and percentage annotation from a name.
     * e.g. "World Bank (IDA)" → "World Bank"
     *      "IsDB (BID) - 92%" → "IsDB"
     *      "State of Senegal - 8%" → "State of Senegal"
     */
    private static String stripParenthetical(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        int parenIdx = trimmed.indexOf('(');
        if (parenIdx > 0) {
            trimmed = trimmed.substring(0, parenIdx).trim();
        }
        // Strip percentage suffix (e.g. "- 8%") for names without parentheses
        return trimmed.replaceAll("\\s*-\\s*\\d+(\\.\\d+)?%\\s*$", "").trim();
    }

    private static Long updateSingleOrg(ImportDataModel importDataModel, String name, String code, Session session, String type, boolean createMissingOrgs, Long orgGroupId, String importedOrgGroupName, boolean createMissingOrgGroups)
    {
        Long orgId;
        String cleanName = stripParenthetical(name);

        if (ConstantsMap.containsKey("org_"+cleanName+"_"+code)) {
            orgId = ConstantsMap.get("org_"+cleanName+"_"+code);
            logger.info("In cache... organisation "+"org_"+cleanName+"_"+code+":"+orgId);
        }
        else {
            if (!session.isOpen()) {
                session = PersistenceManager.getRequestDBSession();
            }

            String hql;
            Query query;
            List<Long> organisations= new ArrayList<>();

            if (cleanName!=null) {
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o WHERE LOWER(o.name)=LOWER(:name) OR LOWER(o.acronym)=LOWER(:name) OR LOWER(o.orgCode)=LOWER(:name)";
                 query = session.createQuery(hql);
                query.setParameter("name",  cleanName);
                organisations = query.list();
            }
            if (!organisations.isEmpty()) {
                orgId = organisations.get(0);
            } else if (createMissingOrgs) {
                // Create the organisation if it does not exist and the user opted in
                logger.info("Organisation not found, creating new: " + cleanName);
                AmpOrganisation newOrg = new AmpOrganisation();
                newOrg.setName(cleanName);
                if (code != null) {
                    newOrg.setOrgCode(code);
                }
                AmpOrgGroup orgGroup = resolveOrgGroup(session, orgGroupId, importedOrgGroupName, createMissingOrgGroups, cleanName);
                if (orgGroup == null) {
                    throw new IllegalStateException("Unable to resolve an organization group for new organization '" + cleanName + "'");
                }
                logger.info("Group being set for the new org is: " + orgGroup.getName());
                newOrg.setOrgGrpId(orgGroup);
                session.save(newOrg);
                session.flush();
                orgId = newOrg.getAmpOrgId();
                logger.info("Created new organisation: " + cleanName + " with id: " + orgId);
            } else {
                // Fallback to "Undefined Agency"
                hql = "SELECT o.ampOrgId FROM " + AmpOrganisation.class.getName() + " o where o.name= :name";
                query = session.createQuery(hql).setParameter("name", "Undefined Agency", StringType.INSTANCE).setMaxResults(1);
                orgId = (Long) query.uniqueResult();
                logger.info("Organisation not found, using Undefined Agency for: " + cleanName);
            }
            ConstantsMap.put("org_"+cleanName+"_"+code, orgId);
        }
        logger.info("Organisation: " + orgId);

        if (Objects.equals(type, "donor")) {
            createDonorOrg(importDataModel, orgId);
        }
        else if (Objects.equals(type, ImporterConstants.ORG_TYPE_RESPONSIBLE_ORG))
        {
            Organization responsibleOrg = new Organization();
            responsibleOrg.setOrganization(orgId);
            importDataModel.getResponsible_organization().add(responsibleOrg);

        }
        else if (Objects.equals(type, ImporterConstants.ORG_TYPE_BENEFICIARY_AGENCY))
        {
            Organization beneficiaryAgency = new Organization();
            beneficiaryAgency.setOrganization(orgId);
            importDataModel.getBeneficiary_agency().add(beneficiaryAgency);
        }
        else if (Objects.equals(type, ImporterConstants.ORG_TYPE_EXECUTING_AGENCY))
        {
            Organization executingAgency = new Organization();
            executingAgency.setOrganization(orgId);
            importDataModel.getExecuting_agency().add(executingAgency);
        }
        else if (Objects.equals(type, ImporterConstants.ORG_TYPE_IMPLEMENTING_AGENCY))
        {
            createImplementingAgency(importDataModel, orgId);
        }
        else if (Objects.equals(type, ImporterConstants.ORG_TYPE_CONTRACTING_AGENCY))
        {
            Organization contractingAgency = new Organization();
            contractingAgency.setOrganization(orgId);
            importDataModel.getContracting_agency().add(contractingAgency);
        }
        return orgId;
    }

    public static boolean hasTransactions(List<Funding> fundings) {
        if (fundings == null || fundings.isEmpty()) {
            return false;
        }
        for (Funding funding : fundings) {
            if (funding == null) {
                continue;
            }
            if (hasNonZeroTransaction(funding.getCommitments())
                    || hasNonZeroTransaction(funding.getDisbursements())
                    || hasNonZeroTransaction(funding.getExpenditures())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNonZeroTransaction(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return false;
        }
        for (Transaction transaction : transactions) {
            if (transaction != null && Double.compare(transaction.getTransaction_amount(), 0.0d) != 0) {
                return true;
            }
        }
        return false;
    }

    public static void persistImportedProjectStatus(ImportedProject importedProject) {
        if (importedProject == null) {
            return;
        }
        Session session = PersistenceManager.getRequestDBSession();
        try {
            if (session == null || !session.isOpen()) {
                PersistenceManager.doInTransaction(s -> {
                    s.saveOrUpdate(importedProject);
                    s.flush();
                });
                return;
            }
            session.saveOrUpdate(importedProject);
            session.flush();
        } catch (Exception e) {
            logger.warn("Could not save imported project status: {}", e.getMessage());
        }
    }

    private static AmpOrgGroup resolveOrgGroup(Session session, Long orgGroupId, String importedOrgGroupName, boolean createMissingOrgGroups, String organizationName) {
        AmpOrgGroup importedOrgGroup = findOrgGroupByNameOrCode(session, importedOrgGroupName);
        if (importedOrgGroup != null) {
            return importedOrgGroup;
        }

        if (importedOrgGroupName != null && createMissingOrgGroups) {
            return getOrCreateOrgGroup(session, importedOrgGroupName, orgGroupId);
        }

        if (orgGroupId != null) {
            return (AmpOrgGroup) session.get(AmpOrgGroup.class, orgGroupId);
        }

        if (createMissingOrgGroups && organizationName != null && !organizationName.trim().isEmpty()) {
            return getOrCreateOrgGroup(session, organizationName.trim(), null);
        }

        return null;
    }

    private static AmpOrgGroup findOrgGroupByNameOrCode(Session session, String orgGroupValue) {
        if (orgGroupValue == null || orgGroupValue.trim().isEmpty()) {
            return null;
        }

        String normalizedValue = orgGroupValue.trim();
        String orgGroupNameHql = AmpOrgGroup.hqlStringForName("grp");
        String hql = "SELECT grp FROM " + AmpOrgGroup.class.getName() + " grp WHERE (grp.deleted IS NULL OR grp.deleted = false) "
                + "AND (LOWER(" + orgGroupNameHql + ") = LOWER(:value) OR LOWER(grp.orgGrpCode) = LOWER(:value))";
        return (AmpOrgGroup) session.createQuery(hql)
                .setParameter("value", normalizedValue, StringType.INSTANCE)
                .setMaxResults(1)
                .uniqueResult();
    }

    private static AmpOrgGroup getOrCreateOrgGroup(Session session, String orgGroupName, Long fallbackOrgGroupId) {
        AmpOrgGroup existingGroup = findOrgGroupByNameOrCode(session, orgGroupName);
        if (existingGroup != null) {
            return existingGroup;
        }

        AmpOrgGroup newOrgGroup = new AmpOrgGroup();
        newOrgGroup.setOrgGrpName(orgGroupName.trim());

        AmpOrgType orgType = null;
        if (fallbackOrgGroupId != null) {
            AmpOrgGroup fallbackGroup = (AmpOrgGroup) session.get(AmpOrgGroup.class, fallbackOrgGroupId);
            if (fallbackGroup != null) {
                orgType = fallbackGroup.getOrgType();
            }
        }
        if (orgType == null) {
            orgType = getDefaultOrgType();
        }

        newOrgGroup.setOrgType(orgType);
        ARUtil.clearOrgGroupTypeDimensions();
        DbUtil.add(newOrgGroup);
        session.flush();
        return newOrgGroup;
    }

    private static AmpOrgType getDefaultOrgType() {
        List<AmpOrgType> allAmpOrgTypes = DbUtil.getAmpOrgTypes();
        AmpOrgType otherOrgType = null;
        AmpOrgType bilateralOrgType = null;

        if (allAmpOrgTypes == null || allAmpOrgTypes.isEmpty()) {
            throw new IllegalStateException("No organization types are available to create a new organization group");
        }

        for (AmpOrgType type : allAmpOrgTypes) {
            if (type.getOrgType() != null && type.getOrgType().equalsIgnoreCase("other")) {
                otherOrgType = type;
            }
            if (type.getOrgType() != null && type.getOrgType().equalsIgnoreCase("bilateral")) {
                bilateralOrgType = type;
            }
        }

        if (otherOrgType != null) {
            return otherOrgType;
        }
        if (bilateralOrgType != null) {
            return bilateralOrgType;
        }
        return allAmpOrgTypes.get(0);
    }

    /**
     * Splits a total amount evenly across n donors, using the same whole-number percentages
     * as {@link #divide100}. The last donor absorbs any floating-point remainder so the
     * individual amounts always sum exactly to totalAmount.
     */
    private static List<Double> splitAmounts(double totalAmount, int n) {
        if (n <= 0) return Collections.emptyList();
        if (n == 1) return Collections.singletonList(totalAmount);
        Map<Integer, Float> percentages = divide100(n);
        List<Double> amounts = new ArrayList<>();
        double allocated = 0;
        for (int i = 0; i < n - 1; i++) {
            double share = totalAmount * percentages.get(i) / 100.0;
            amounts.add(share);
            allocated += share;
        }
        amounts.add(totalAmount - allocated);
        return amounts;
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

    private static void createImplementingAgency(ImportDataModel importDataModel, Long orgId) {
        createImplementingAgency(importDataModel, orgId, null);
    }

    private static void createImplementingAgency(ImportDataModel importDataModel, Long orgId, Long orgRoleId) {
        ImplementingAgency implementingAgency = new ImplementingAgency();
        implementingAgency.setOrganization(orgId);
        if (orgRoleId != null) implementingAgency.setId(orgRoleId);
        importDataModel.getImplementing_agency().add(implementingAgency);

        Set<Organization> normalizedImplementingAgencies = new LinkedHashSet<>();
        for (Organization organization : importDataModel.getImplementing_agency()) {
            if (organization instanceof ImplementingAgency) {
                normalizedImplementingAgencies.add(organization);
            } else if (organization != null && organization.getOrganization() != null) {
                ImplementingAgency normalized = new ImplementingAgency();
                normalized.setId(organization.getId());
                normalized.setOrganization(organization.getOrganization());
                normalizedImplementingAgencies.add(normalized);
            }
        }
        importDataModel.setImplementing_agency(normalizedImplementingAgencies);

        int count = importDataModel.getImplementing_agency().size();
        Map<Integer, Float> percentages = divide100(count);
        int index = 0;
        for (Organization organization : importDataModel.getImplementing_agency()) {
            if (organization instanceof ImplementingAgency) {
                ((ImplementingAgency) organization).setPercentage(percentages.get(index));
            }
            index++;
        }
    }

    public static int getColumnIndexByName(Sheet sheet, String columnName) {
        try {
            String normalizedColumnName = normalizeImporterLookupValue(columnName);
            if (normalizedColumnName.isEmpty()) {
                return -1;
            }
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                String headerValue = normalizeImporterLookupValue(getStringValueFromCell(cell, true));
                if (cell != null && normalizedColumnName.equalsIgnoreCase(headerValue)) {
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
        List<String> locationNames = splitMultipleValues(locationNamesStr, "[,;\\u061B\\uFF1B]");
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
        addProgramToActivityIfMissing(activity, program, session, null);
    }

    public static void addProgramToActivityIfMissing(AmpActivityVersion activity, AmpTheme program, Session session,
                                                     AmpActivityProgramSettings programSetting) {
        if (activity == null || program == null) return;
        Set<AmpActivityProgram> actPrograms = activity.getActPrograms();
        if (actPrograms == null) {
            actPrograms = new HashSet<>();
            activity.setActPrograms(actPrograms);
        }
        for (AmpActivityProgram ap : actPrograms) {
            if (ap.getProgram() != null && program.getAmpThemeId() != null
                    && program.getAmpThemeId().equals(ap.getProgram().getAmpThemeId())) {
                if (ap.getProgramSetting() == null && programSetting != null) {
                    ap.setProgramSetting(programSetting);
                    session.saveOrUpdate(ap);
                }
                return;
            }
        }
        AmpActivityProgram activityProgram = new AmpActivityProgram();
        activityProgram.setActivity(activity);
        activityProgram.setProgram(program);
        activityProgram.setProgramSetting(programSetting);
        activityProgram.setProgramPercentage(100f);
        actPrograms.add(activityProgram);
    }

    public static void addProgramsToActivity(Long activityId, String rawProgramNames, String rowProgramClassification,
                                             String fallbackProgramClassification, boolean createMissingPrograms,
                                             Session session) {
        if (activityId == null || rawProgramNames == null || rawProgramNames.trim().isEmpty()) {
            return;
        }
        logger.info("Adding programs to activity {}: rawProgramNames='{}', rowProgramClassification='{}', fallbackProgramClassification='{}', createMissingPrograms={}",
                activityId, rawProgramNames, rowProgramClassification, fallbackProgramClassification, createMissingPrograms);
        String resolvedClassification = (rowProgramClassification != null && !rowProgramClassification.trim().isEmpty())
                ? rowProgramClassification.trim()
                : (fallbackProgramClassification != null ? fallbackProgramClassification.trim() : null);
        if (resolvedClassification == null || resolvedClassification.isEmpty()) {
            logger.info("Skipping programs because program classification could not be resolved");
            return;
        }
        AmpActivityVersion activity = session.get(AmpActivityVersion.class, activityId);
        if (activity == null) {
            logger.info("Skipping programs because activity {} was not found", activityId);
            return;
        }

        AmpActivityProgramSettings programSetting = resolveProgramSettingByName(resolvedClassification);
        if (programSetting == null) {
            logger.warn("Program classification '{}' not found; skipping programs for activity {}",
                    resolvedClassification, activityId);
            return;
        }
        logger.info("Adding programs to activity {}: classification='{}', createMissingPrograms={}", activityId, resolvedClassification, createMissingPrograms);

        for (String programName : splitMultipleValues(rawProgramNames)) {
            AmpTheme program = createMissingPrograms
                    ? getOrCreateProgramByName(programName, resolvedClassification, session)
                    : getProgramByNameAndClassification(programName, resolvedClassification, session);
            logger.info("Processing program '{}' for activity {}: resolved to theme id={}", programName, activityId, program != null ? program.getAmpThemeId() : null);
            if (program == null) {
                logger.info("Program '{}' not found and createMissingPrograms is disabled; skipping", programName);
                continue;
            }
            addProgramToActivityIfMissing(activity, program, session, programSetting);
        }

        logger.info("Adding percentages to programs");
        if (activity.getActPrograms() != null && !activity.getActPrograms().isEmpty()) {
            // Group by programSetting (classification)
            List<AmpActivityProgram> group = new ArrayList<>();
            for (AmpActivityProgram ap : activity.getActPrograms()) {
                if (ap.getProgramSetting() != null && ap.getProgramSetting().equals(programSetting)) {
                    group.add(ap);
                }
            }
            if (!group.isEmpty()) {
                Map<Integer, Float> percentages = divide100(group.size());
                for (int i = 0; i < group.size(); i++) {
                    group.get(i).setProgramPercentage(percentages.get(i));
                    session.saveOrUpdate(group.get(i));
                }
            }
        }
        session.flush();
    }

    private static AmpTheme getProgramByNameAndClassification(String programName, String classification,
                                                               Session session) {
        if (programName == null || programName.trim().isEmpty()) {
            return null;
        }
        AmpActivityProgramSettings setting = resolveProgramSettingByName(classification);
        if (setting == null) {
            return ProgramUtil.getTheme(programName.trim());
        }
        String hql = "SELECT t FROM " + AmpTheme.class.getName() + " t WHERE LOWER(t.name) = LOWER(:name)";
        Query query;
        if (setting.getDefaultHierarchy() != null && setting.getDefaultHierarchy().getAmpThemeId() != null) {
            hql += " AND t.parentThemeId.ampThemeId = :parentId";
            query = session.createQuery(hql);
            query.setParameter("parentId", setting.getDefaultHierarchy().getAmpThemeId());
        } else {
            query = session.createQuery(hql);
        }
        query.setParameter("name", programName.trim(), StringType.INSTANCE);
        query.setMaxResults(1);
        AmpTheme theme = (AmpTheme) query.uniqueResult();
        return theme != null ? theme : ProgramUtil.getTheme(programName.trim());
    }

    public static AmpTheme getOrCreateProgramByName(String programName, String classification, Session session) {
        AmpTheme existing = getProgramByNameAndClassification(programName, classification, session);
        if (existing != null) {
            return existing;
        }
        if (programName == null || programName.trim().isEmpty()) {
            return null;
        }
        logger.info("Creating new program '{}' for classification '{}'", programName, classification);
        try {
            AmpTheme newTheme = new AmpTheme();
            String trimmedName = programName.trim();
            newTheme.setName(trimmedName);
            String code = trimmedName.replaceAll("[^a-zA-Z0-9_-]", "_").replaceAll("_+", "_").trim();
            if (code.length() > 45) code = code.substring(0, 45);
            newTheme.setThemeCode("IMP_" + code + "_" + System.currentTimeMillis());
            newTheme.setIndlevel(0);
            AmpActivityProgramSettings setting = resolveProgramSettingByName(classification);
            if (setting != null && setting.getDefaultHierarchy() != null) {
                newTheme.setParentThemeId(setting.getDefaultHierarchy());
                Integer rootLevel = setting.getDefaultHierarchy().getIndlevel();
                newTheme.setIndlevel(rootLevel != null ? rootLevel + 1 : 1);
            } else {
                newTheme.setParentThemeId(null);
            }
            // Set typeCategoryValue from classification's defaultHierarchy's category value
            if (setting != null && setting.getDefaultHierarchy() != null && setting.getDefaultHierarchy().getTypeCategoryValue() != null) {
                newTheme.setTypeCategoryValue(setting.getDefaultHierarchy().getTypeCategoryValue());
            } else {
                throw new IllegalStateException("Cannot create program: classification '" + classification + "' does not resolve to a valid typeCategoryValue (required for DB constraint)");
            }
            session.save(newTheme);
            session.flush();
            return newTheme;
        } catch (Exception e) {
            logger.warn("Failed to create program '{}' for classification '{}'", programName, classification, e);
            return null;
        }
    }

    private static AmpActivityProgramSettings resolveProgramSettingByName(String classification) {
        if (classification == null || classification.trim().isEmpty()) {
            return null;
        }
        String normalizedClassification = classification.trim();
        try {
            AmpActivityProgramSettings setting = ProgramUtil.getAmpActivityProgramSettings(normalizedClassification);
            if (setting != null) {
                return setting;
            }

            for (AmpActivityProgramSettings candidate : ProgramUtil.getAmpActivityProgramSettingsList(false)) {
                if (candidate == null) {
                    continue;
                }
                if (candidate.getName() != null && normalizedClassification.equalsIgnoreCase(candidate.getName().trim())) {
                    return candidate;
                }
                AmpTheme defaultHierarchy = candidate.getDefaultHierarchy();
                if (defaultHierarchy == null) {
                    continue;
                }
                if (defaultHierarchy.getName() != null
                        && normalizedClassification.equalsIgnoreCase(defaultHierarchy.getName().trim())) {
                    return candidate;
                }
                if (defaultHierarchy.getThemeCode() != null
                        && normalizedClassification.equalsIgnoreCase(defaultHierarchy.getThemeCode().trim())) {
                    return candidate;
                }
            }
            return null;
        } catch (Exception e) {
            logger.warn("Could not resolve program setting '{}'", classification, e);
            return null;
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

    public static String getCellValueByConfig(Map<String, String> row, Map<String, String> config, String fieldName) {
        String key = getKey(config, fieldName);
        if (key == null || row == null || row.isEmpty()) {
            return null;
        }
        if (row.containsKey(key)) {
            return row.get(key);
        }

        String normalizedKey = normalizeImporterLookupValue(key);
        for (Map.Entry<String, String> entry : row.entrySet()) {
            if (normalizedKey.equalsIgnoreCase(normalizeImporterLookupValue(entry.getKey()))) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static String normalizeImporterLookupValue(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\u00A0', ' ')
                .replace('\u202F', ' ')
                .trim();
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
