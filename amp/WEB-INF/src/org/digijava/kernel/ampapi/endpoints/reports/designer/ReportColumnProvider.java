package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsOrder;
import org.digijava.module.aim.dbentity.AmpColumnsVisibility;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.criterion.Order;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.dgfoundation.amp.ar.ColumnConstants.*;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType.*;

/**
 * Fetch the amp columns from the system and provide the needed list based on the report type
 *
 * @author Viorel Chihai
 */
public class ReportColumnProvider extends ReportEntityProvider {

    public static final String REPORTS_FM_NAME = "Reports";
    public static final String ME_FM_NAME = "M & E";

    public static final int DEFAULT_ORDER_VALUE = 999;

    private static final Set<String> COLUMNS_IGNORED_IN_REPORT_WIZARD =
            ImmutableSet.of(ColumnConstants.EXPENDITURE_CLASS);

    private static final Map<String, String> COLUMN_TO_FM_FIELD_MAP = new ImmutableMap.Builder<String, String>()
            .put(REGIONAL_REGION, LOCATION_ADM_LEVEL_1)
            .build();

    private static final Map<String, Integer> ME_COLUMNS_ORDER = new ImmutableMap.Builder<String, Integer>()
            .put(ColumnConstants.INDICATOR_NAME, 1)
            .put(ColumnConstants.INDICATOR_CODE, 2)
            .put(ColumnConstants.INDICATOR_SECTOR, 3)
            .put(ColumnConstants.INDICATOR_DESCRIPTION, 4)
            .put(ColumnConstants.INDICATOR_TYPE, 5)
            .put(ColumnConstants.INDICATOR_CREATION_DATE, 6)
            .put(ColumnConstants.INDICATOR_LOGFRAME_CATEGORY, 7)
            .put(ColumnConstants.INDICATOR_BASE_VALUE, 8)
            .put(ColumnConstants.INDICATOR_BASE_DATE, 9)
            .put(ColumnConstants.INDICATOR_BASE_COMMENT, 10)
            .put(ColumnConstants.INDICATOR_TARGET_VALUE, 11)
            .put(ColumnConstants.INDICATOR_TARGET_DATE, 12)
            .put(ColumnConstants.INDICATOR_TARGET_COMMENT, 13)
            .put(ColumnConstants.INDICATOR_REVISED_TARGET_VALUE, 14)
            .put(ColumnConstants.INDICATOR_REVISED_TARGET_DATE, 15)
            .put(ColumnConstants.INDICATOR_REVISED_TARGET_COMMENT, 16)
            .put(ColumnConstants.INDICATOR_ACTUAL_VALUE, 17)
            .put(ColumnConstants.INDICATOR_ACTUAL_DATE, 18)
            .put(ColumnConstants.INDICATOR_ACTUAL_COMMENT, 19)
            .put(ColumnConstants.INDICATOR_RISK, 20)
            .build();

    private static final List<String> DONOR_HIERARCHIES = ImmutableList.of(
            AC_CHAPTER, ACTIVITY_CREATED_BY, COMPONENT_TYPE, ACCESSION_INSTRUMENT, ACTIVITY_BUDGET, STATUS,
            IMPLEMENTATION_LEVEL, IMPLEMENTATION_LOCATION, EXECUTING_AGENCY, EXECUTING_AGENCY_TYPE,
            EXECUTING_AGENCY_COUNTRY, ColumnConstants.DONOR_ID, DONOR_AGENCY, DONOR_GROUP, DONOR_COUNTRY, DONOR_TYPE,
            TYPE_OF_ASSISTANCE, FINANCING_INSTRUMENT, LOCATION_ADM_LEVEL_0, LOCATION_ADM_LEVEL_1, LOCATION_ADM_LEVEL_2,
            LOCATION_ADM_LEVEL_3, NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, NATIONAL_PLANNING_OBJECTIVES_LEVEL_2,
            NATIONAL_PLANNING_OBJECTIVES_LEVEL_3, NATIONAL_PLANNING_OBJECTIVES_LEVEL_4,
            NATIONAL_PLANNING_OBJECTIVES_LEVEL_5, NATIONAL_PLANNING_OBJECTIVES_LEVEL_6,
            NATIONAL_PLANNING_OBJECTIVES_LEVEL_7, NATIONAL_PLANNING_OBJECTIVES_LEVEL_8,
            PRIMARY_PROGRAM_LEVEL_1, PRIMARY_PROGRAM_LEVEL_2, PRIMARY_PROGRAM_LEVEL_3, PRIMARY_PROGRAM_LEVEL_4,
            PRIMARY_PROGRAM_LEVEL_5, PRIMARY_PROGRAM_LEVEL_6, PRIMARY_PROGRAM_LEVEL_7, PRIMARY_PROGRAM_LEVEL_8,
            INDIRECT_PRIMARY_PROGRAM_LEVEL_1, INDIRECT_PRIMARY_PROGRAM_LEVEL_2, INDIRECT_PRIMARY_PROGRAM_LEVEL_3,
            INDIRECT_PRIMARY_PROGRAM_LEVEL_4, INDIRECT_PRIMARY_PROGRAM_LEVEL_5, INDIRECT_PRIMARY_PROGRAM_LEVEL_6,
            INDIRECT_PRIMARY_PROGRAM_LEVEL_7, INDIRECT_PRIMARY_PROGRAM_LEVEL_8, SECONDARY_PROGRAM_LEVEL_1,
            SECONDARY_PROGRAM_LEVEL_2, SECONDARY_PROGRAM_LEVEL_3, SECONDARY_PROGRAM_LEVEL_4, SECONDARY_PROGRAM_LEVEL_5,
            SECONDARY_PROGRAM_LEVEL_6, SECONDARY_PROGRAM_LEVEL_7, SECONDARY_PROGRAM_LEVEL_8, TERTIARY_PROGRAM_LEVEL_1,
            TERTIARY_PROGRAM_LEVEL_2, TERTIARY_PROGRAM_LEVEL_3, TERTIARY_PROGRAM_LEVEL_4, TERTIARY_PROGRAM_LEVEL_5,
            TERTIARY_PROGRAM_LEVEL_6, TERTIARY_PROGRAM_LEVEL_7, TERTIARY_PROGRAM_LEVEL_8, CREDIT_DONATION,
            BENEFICIARY_AGENCY, BENEFICIARY_AGENCY_COUNTRY, BENEFICIARY_AGENCY_GROUPS, IMPLEMENTING_AGENCY,
            IMPLEMENTING_AGENCY_GROUPS, IMPLEMENTING_AGENCY_TYPE, RESPONSIBLE_ORGANIZATION, INSTITUTIONS,
            PRIMARY_SECTOR, PRIMARY_SECTOR_SUB_SECTOR, PRIMARY_SECTOR_SUB_SUB_SECTOR, SECONDARY_SECTOR,
            SECONDARY_SECTOR_SUB_SECTOR, SECONDARY_SECTOR_SUB_SUB_SECTOR, TERTIARY_SECTOR, TERTIARY_SECTOR_SUB_SECTOR,
            TERTIARY_SECTOR_SUB_SUB_SECTOR, QUATERNARY_SECTOR, QUATERNARY_SECTOR_SUB_SECTOR,
            QUATERNARY_SECTOR_SUB_SUB_SECTOR, QUINARY_SECTOR, QUINARY_SECTOR_SUB_SECTOR, QUINARY_SECTOR_SUB_SUB_SECTOR,
            FUNDING_STATUS, MODE_OF_PAYMENT, PAYMENT_CAPITAL___RECURRENT, BUDGET_DEPARTMENT, BUDGET_ORGANIZATION,
            BUDGET_PROGRAM, BUDGET_SECTOR, BUDGET_STRUCTURE, GOVERNMENT_APPROVAL_PROCEDURES, JOINT_CRITERIA,
            MULTI_DONOR, CAPITAL_EXPENDITURE, SECTOR_TAG, SECTOR_TAG_SUB_SECTOR, SECTOR_TAG_SUB_SUB_SECTOR,
            AGREEMENT_TITLE_CODE, AGREEMENT_CODE, MODALITIES, INDIRECT_ON_BUDGET, HUMANITARIAN_AID, VULNERABLE_GROUP,
            CONTRACTING_AGENCY, CONTRACTING_AGENCY_ACRONYM, CONTRACTING_AGENCY_GROUPS, CONTRACTING_AGENCY_TYPE,
            PROJECT_IMPLEMENTING_UNIT, TYPE_OF_COOPERATION, TYPE_OF_IMPLEMENTATION, MULTI_STAKEHOLDER_PARTNERSHIP,
            CONCESSIONALITY_LEVEL, DISASTER_RESPONSE_MARKER, PROJECT_CATEGORY, PROJECT_RESULTS_AVAILABLE,
            INDICATOR_NAME, INDICATOR_LOGFRAME_CATEGORY, INDICATOR_RISK, INDICATOR_SECTOR, INDICATOR_TYPE
    );

    private static final List<String> COMPONENT_HIERARCHIES = ImmutableList.of(
            STATUS, PRIMARY_SECTOR, NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, LOCATION_ADM_LEVEL_1, LOCATION_ADM_LEVEL_2,
            LOCATION_ADM_LEVEL_3, COMPONENT_TYPE, COMPONENT_NAME, PROJECT_TITLE, COMPONENT_FUNDING_ORGANIZATION,
            COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION);

    private static final List<String> REGIONAL_HIERARCHIES = ImmutableList.of(
            STATUS, PRIMARY_SECTOR, PRIMARY_SECTOR_SUB_SECTOR, NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, REGIONAL_REGION);

    private static final List<String> PLEDGE_HIERARCHIES = ImmutableList.of(
            RELATED_PROJECTS, PLEDGES_DONOR_GROUP, PLEDGES_AID_MODALITY, PLEDGES_TYPE_OF_ASSISTANCE, PLEDGES_TITLES,
            PLEDGE_STATUS, PLEDGES_SECTORS, PLEDGES_SECONDARY_SECTORS, PLEDGES_TERTIARY_SECTORS,
            PLEDGES_QUATERNARY_SECTORS, PLEDGES_QUINARY_SECTORS, PLEDGES_PROGRAMS, PLEDGES_SECONDARY_PROGRAMS,
            PLEDGES_TERTIARY_PROGRAMS, PLEDGES_LOCATION_ADM_LEVEL_1, PLEDGES_LOCATION_ADM_LEVEL_2);

    private static final List<String> MEASURELESS_ONLY_HIERARCHIES = ImmutableList.of(
            INDICATOR_NAME, INDICATOR_LOGFRAME_CATEGORY, INDICATOR_RISK, INDICATOR_SECTOR, INDICATOR_TYPE);

    private static final Map<ReportType, List<String>> REPORT_TYPE_HIERARCHIES =
            new ImmutableMap.Builder<ReportType, List<String>>()
                    .put(DONOR, DONOR_HIERARCHIES)
                    .put(COMPONENT, COMPONENT_HIERARCHIES)
                    .put(REGIONAL, REGIONAL_HIERARCHIES)
                    .put(PLEDGE, PLEDGE_HIERARCHIES)
                    .build();

    private static final Comparator<AmpColumns> ME_COLS_COMPARATOR =
            Comparator.comparing(c -> ME_COLUMNS_ORDER.getOrDefault(c.getColumnName(), DEFAULT_ORDER_VALUE));

    private TranslatorService translatorService;
    public TranslatorService getTranslatorService() {
        return translatorService;
    }

    private Map<ReportType, Map<String, List<AmpColumns>>> ampTreeColumnsMapByType = new HashMap<>();

    private AmpTreeVisibility ampTreeVisibility;

    public ReportColumnProvider(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public List<ReportColumn> getColumns(final ReportProfile profile, final ReportType type) {
        Map<String, List<AmpColumns>> stringListMap = getAmpTreeColumnByType(type);

        List<ReportColumn> reportColumns = new ArrayList<>();

        for (Map.Entry<String, List<AmpColumns>> entry : stringListMap.entrySet()) {
            String category = entry.getKey();
            reportColumns.addAll(entry.getValue().stream()
                    .map(column -> convertAmpColumnToReportColumn(column, category, profile, type))
                    .collect(Collectors.toList())
            );
        }

        return reportColumns;
    }

    private Map<String, List<AmpColumns>> getAmpTreeColumnByType(final ReportType type) {
        if (ampTreeColumnsMapByType.containsKey(type)) {
            return ampTreeColumnsMapByType.get(type);
        }

        List<AmpColumns> ampColumns = fetchAmpColumns();
        ampTreeColumnsMapByType.put(type, buildAmpTreeColumnSimple(ampColumns, type));

        return ampTreeColumnsMapByType.get(type);
    }

    public List<AmpColumns> fetchAmpColumns() {
        return PersistenceManager.getSession()
                .createCriteria(AmpColumns.class)
                .setCacheable(true)
                .addOrder(Order.asc("columnName"))
                .list();
    }

    private ReportColumn convertAmpColumnToReportColumn(final AmpColumns ampColumn, final String category,
                                                        final ReportProfile profile, final ReportType type) {
        ReportColumn reportColumn = new ReportColumn(ampColumn.getColumnId(),
                ampColumn.getColumnName(), translatorService.translateText(ampColumn.getColumnName()),
                translatorService.translateText(ampColumn.getDescription()),
                translatorService.translateText(category),
                isHierarchyColumn(ampColumn.getColumnName(), profile, type),
                isAmountColumn(ampColumn.getColumnName()));

        return reportColumn;
    }

    private Map<String, List<AmpColumns>> buildAmpTreeColumnSimple(List<AmpColumns> ampColumns, ReportType type) {
        ArrayList<AmpColumnsVisibility> visibleAmpColumns = new ArrayList<>();

        HttpSession session = TLSUtils.getRequest().getSession();
        ServletContext context = session.getServletContext();

        ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(context, session);

        TreeSet<AmpColumnsOrder> ampThemesOrdered = new TreeSet<>();

        List<AmpColumnsOrder> ampColumnsOrder = (ArrayList<AmpColumnsOrder>) context.getAttribute("ampColumnsOrder");
        Map<String, AmpColumnsOrder> ampColumnsOrderByName = new HashMap<>();

        for (AmpColumnsOrder order : ampColumnsOrder) {
            ampColumnsOrderByName.put(order.getColumnName(), order);
        }

        Map<String, AmpFieldsVisibility> ampAllFieldsByName = new HashMap<>();
        List<AmpFieldsVisibility> ampAllFields = fetchFields();
        for (AmpFieldsVisibility field : ampAllFields) {
            ampAllFieldsByName.put(field.getName(), field);
        }

        boolean mtefColumnsEnabled = FeaturesUtil.
                getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT);

        List<AmpColumns> filteredAmpColumns = ampColumns.stream()
                .filter(col -> isColumnVisible(col, type, ampAllFieldsByName, mtefColumnsEnabled))
                .collect(Collectors.toList());

        for (AmpColumns ampColumn : filteredAmpColumns) {
            String columnName = ampColumn.getColumnName();
            AmpFieldsVisibility ampFieldVisibility = ampAllFieldsByName
                    .get(COLUMN_TO_FM_FIELD_MAP.getOrDefault(columnName, columnName));

            AmpColumnsVisibility ampColumnVisibilityObj = new AmpColumnsVisibility();
            ampColumnVisibilityObj.setAmpColumn(ampColumn);
            ampColumnVisibilityObj.setAmpfield(ampFieldVisibility);
            ampColumnVisibilityObj.setParent((AmpFeaturesVisibility) ampFieldVisibility.getParent());
            visibleAmpColumns.add(ampColumnVisibilityObj);

            if (type.isPledge()) {
                ampThemesOrdered.addAll(ampColumnsOrder.stream()
                        .filter(this::isColumnOrderColumnPledge)
                        .collect(Collectors.toList())
                );
            } else {
                AmpColumnsOrder aco = ampColumnsOrderByName.get(
                        getModuleName(ampFieldVisibility.getParent().getName()));
                if (aco != null && !isColumnOrderColumnPledge(aco)) {
                    ampThemesOrdered.add(aco);
                }
            }
        }
        Map<String, List<AmpColumns>> ampTreeColumn = new LinkedHashMap<>();

        for (AmpColumnsOrder aco : ampThemesOrdered) {
            String themeName = aco.getColumnName();
            ArrayList<AmpColumns> aux = new ArrayList<>();
            boolean added = false;
            for (AmpColumnsVisibility acv : visibleAmpColumns) {
                if (themeName.equals(getModuleName(acv.getParent().getName()))) {
                    aux.add(acv.getAmpColumn());
                    added = true;
                }
            }
            if (added) {
                if (themeName.equals(ME_FM_NAME)) {
                    aux.sort(ME_COLS_COMPARATOR);
                }
                ampTreeColumn.put(themeName, aux);
            }
        }

        return ampTreeColumn;
    }

    private boolean isColumnVisible(final AmpColumns column, final ReportType type,
                                    final Map<String, AmpFieldsVisibility> ampAllFieldsByName,
                                    final boolean mtefColumnsEnabled) {

        String columnName = column.getColumnName();
        if (COLUMNS_IGNORED_IN_REPORT_WIZARD.contains(columnName)) {
            return false;
        }

        if (!mtefColumnsEnabled && isMTEFName(column.getColumnName())) {
            return false;
        }

        if (type.isRegional() && columnName.equals(LOCATION_ADM_LEVEL_1)
                || !type.isRegional() && columnName.equals(REGIONAL_REGION)) {
            return false;
        }

        AmpFieldsVisibility ampFieldVisibility = ampAllFieldsByName
                .get(COLUMN_TO_FM_FIELD_MAP.getOrDefault(columnName, columnName));

        if (ampFieldVisibility == null || !ampFieldVisibility.isFieldActive(ampTreeVisibility)) {
            return false;
        }

        // skip build columns with no rights
        Map scope = PermissionUtil.getScope(TLSUtils.getRequest().getSession());
        if (ampFieldVisibility.getPermission(false) != null
                && !ampFieldVisibility.canDo(GatePermConst.Actions.VIEW, scope)) {
            return false;
        }

        return true;
    }

    public List<AmpFieldsVisibility> fetchFields() {
        return PersistenceManager.getSession()
                .createCriteria(AmpFieldsVisibility.class)
                .setCacheable(true)
                .addOrder(Order.asc("name"))
                .list();
    }

    /**
     * Get theme name from feature name. Usually theme name matches feature name, but in some cases it was not
     * possible to do so. Indicator columns are grouped under /Monitoring & Evaluation/M & E/Reports, in this case
     * this mechanism allows to swap Reports with M & E.
     *
     * @param featureName feature name
     * @return theme name
     */
    private String getModuleName(String featureName) {
        return REPORTS_FM_NAME.equals(featureName) ? ME_FM_NAME : featureName;
    }

    private boolean isColumnOrderColumnPledge(final AmpColumnsOrder aco) {
        return aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_COLUMNS)
                || aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_1)
                || aco.getColumnName().equalsIgnoreCase(ArConstants.PLEDGES_CONTACTS_2);
    }

    private boolean isHierarchyColumn(final String columnName, final ReportProfile profile, final ReportType type) {
        if (columnName.equals(ArConstants.COLUMN_PROJECT_TITLE)
                && FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECT_TITLE_HIRARCHY)
                .equalsIgnoreCase("true")) {
            return true;
        }
        
        if (profile.isTab() && MEASURELESS_ONLY_HIERARCHIES.contains(columnName)) {
            return false;
        }

        if (REPORT_TYPE_HIERARCHIES.containsKey(type)) {
            return REPORT_TYPE_HIERARCHIES.get(type).contains(columnName);
        }

        return false;
    }

    private boolean isAmountColumn(final String columnName) {
        return PROPOSED_PROJECT_AMOUNT.equalsIgnoreCase(columnName)
                || REVISED_PROJECT_AMOUNT.equalsIgnoreCase(columnName)
                || isMTEFName(columnName);
    }

    public List<AmpColumns> getMeasurelessColumns() {
        return fetchAmpColumns().stream()
                .filter(c -> MEASURELESS_ONLY_HIERARCHIES.contains(c.getColumnName()))
                .collect(Collectors.toList());
    }

    public List<AmpColumns> getAmountColumns() {
        return fetchAmpColumns().stream()
                .filter(c -> isAmountColumn(c.getColumnName()))
                .collect(Collectors.toList());
    }
}
