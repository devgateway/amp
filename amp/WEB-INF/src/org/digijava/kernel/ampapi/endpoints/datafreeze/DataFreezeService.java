package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityFrozen;
import org.digijava.module.aim.dbentity.AmpDataFreezeExclusion;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;

public final class DataFreezeService {

    private DataFreezeService() {
    }

    public static JsonBean saveDataFreezeEvent(DataFreezeEvent dataFreezeEvent) {
        JsonBean result = new JsonBean();
        List<JsonBean> validationErrors = validate(dataFreezeEvent);
        if (validationErrors.size() == 0) {
            AmpDataFreezeSettings dataFreezeSettings = getOrCreate(dataFreezeEvent);
            updateModel(dataFreezeSettings, dataFreezeEvent);
            DataFreezeUtil.saveDataFreezeEvent(dataFreezeSettings);
            JsonBean saved = modelToJsonBean(dataFreezeSettings);
            if (dataFreezeEvent.getCid() != null) {
                saved.set(DataFreezeConstants.FIELD_CID, dataFreezeEvent.getCid());
            }
            result.set(DataFreezeConstants.DATA, saved);
            result.set(DataFreezeConstants.RESULT, DataFreezeConstants.SAVE_SUCCESSFUL);
        } else {
            result.set(DataFreezeConstants.DATA, dataFreezeEvent);
            result.set(DataFreezeConstants.RESULT, DataFreezeConstants.SAVE_FAILED);
            result.set(DataFreezeConstants.ERRORS, validationErrors);
        }

        return result;
    }

    private static List<JsonBean> validate(DataFreezeEvent dataFreezeEvent) {
        List<JsonBean> errors = new ArrayList<>();
        if(DataFreezeUtil.freezeDateExists(dataFreezeEvent.getId(), DateTimeUtil.parseDate(dataFreezeEvent.getFreezingDate(), DataFreezeConstants.DATE_FORMAT))){
            JsonBean error = new JsonBean();
            error.set(ApiError.getErrorCode(DataFreezeErrors.FREEZING_DATE_EXISTS),
                    DataFreezeErrors.FREEZING_DATE_EXISTS.description);
            errors.add(error);
        }
        
        Date openPeriodStart = DateTimeUtil.parseDate(dataFreezeEvent.getOpenPeriodStart(), DataFreezeConstants.DATE_FORMAT);
        Date openPeriodEnd = DateTimeUtil.parseDate(dataFreezeEvent.getOpenPeriodEnd(), DataFreezeConstants.DATE_FORMAT);       
        if(DataFreezeUtil.openPeriodOverlaps(dataFreezeEvent.getId(), openPeriodStart, openPeriodEnd)){
            JsonBean error = new JsonBean();
            error.set(ApiError.getErrorCode(DataFreezeErrors.OPEN_PERIOD_OVERLAPS),
                    DataFreezeErrors.OPEN_PERIOD_OVERLAPS.description);
            errors.add(error); 
        }
        return errors;
    }

    private static JsonBean modelToJsonBean(AmpDataFreezeSettings dataFreezeEvent) {
        JsonBean json = new JsonBean();
        json.set(DataFreezeConstants.FIELD_ID, dataFreezeEvent.getAmpDataFreezeSettingsId());
        json.set(DataFreezeConstants.FIELD_ENABLED, dataFreezeEvent.getEnabled());
        json.set(DataFreezeConstants.FIELD_GRACE_PERIOD, dataFreezeEvent.getGracePeriod());
        json.set(DataFreezeConstants.FIELD_FREEZING_DATE,
                DateTimeUtil.formatDate(dataFreezeEvent.getFreezingDate(), DataFreezeConstants.DATE_FORMAT));
        json.set(DataFreezeConstants.FIELD_OPEN_PERIOD_START,
                DateTimeUtil.formatDate(dataFreezeEvent.getOpenPeriodStart(), DataFreezeConstants.DATE_FORMAT));
        json.set(DataFreezeConstants.FIELD_OPEN_PERIOD_END,
                DateTimeUtil.formatDate(dataFreezeEvent.getOpenPeriodEnd(), DataFreezeConstants.DATE_FORMAT));
        json.set(DataFreezeConstants.FIELD_FREEZE_OPTION, dataFreezeEvent.getFreezeOption());
        json.set(DataFreezeConstants.FIELD_FILTERS, dataFreezeEvent.getFilters());
        json.set(DataFreezeConstants.FIELD_SEND_NOTIFICATION, dataFreezeEvent.getSendNotification());
        json.set(DataFreezeConstants.FIELD_ENABLED, dataFreezeEvent.getEnabled());
        json.set(DataFreezeConstants.FIELD_COUNT, getCountOfFrozenActivities(dataFreezeEvent));
        json.set(DataFreezeConstants.FIELD_NOTIFICATION_DAYS, dataFreezeEvent.getNotificationDays());
        return json;
    }

    private static AmpDataFreezeSettings updateModel(AmpDataFreezeSettings dataFreezeSettings,
            DataFreezeEvent dataFreezeEvent) {
        dataFreezeSettings.setEnabled(dataFreezeEvent.getEnabled());
        dataFreezeSettings.setGracePeriod(dataFreezeEvent.getGracePeriod());
        dataFreezeSettings.setFreezingDate(
                DateTimeUtil.parseDate(dataFreezeEvent.getFreezingDate(), DataFreezeConstants.DATE_FORMAT));
        dataFreezeSettings.setOpenPeriodStart(
                DateTimeUtil.parseDate(dataFreezeEvent.getOpenPeriodStart(), DataFreezeConstants.DATE_FORMAT));
        dataFreezeSettings.setOpenPeriodEnd(
                DateTimeUtil.parseDate(dataFreezeEvent.getOpenPeriodEnd(), DataFreezeConstants.DATE_FORMAT));
        dataFreezeSettings.setFreezeOption(dataFreezeEvent.getFreezeOption());
        dataFreezeSettings.setFilters(dataFreezeEvent.getFilters());
        dataFreezeSettings.setSendNotification(dataFreezeEvent.getSendNotification());
        dataFreezeSettings.setEnabled(dataFreezeEvent.getEnabled());
        dataFreezeSettings.setNotificationDays(dataFreezeEvent.getNotificationDays());
        return dataFreezeSettings;
    }

    public static AmpDataFreezeSettings getOrCreate(DataFreezeEvent dataFreezeEvent) {
        AmpDataFreezeSettings dataFreezeSettings;
        if (dataFreezeEvent.getId() != null) {
            dataFreezeSettings = DataFreezeUtil.getDataFreezeEventById(dataFreezeEvent.getId());
        } else {
            dataFreezeSettings = new AmpDataFreezeSettings();
        }

        return dataFreezeSettings;
    }

    public static void deleteDataFreezeEvent(long id) {
        DataFreezeUtil.deleteDataFreezeEvent(id);
    }

    public static Page<DataFreezeEvent> fetchDataFreezeEventList(Integer offset, Integer count, String orderBy,
            String sort) {
        Page<DataFreezeEvent> page = new Page<>();
        AmpDateFormatter dateFormatter = AmpDateFormatterFactory.getDefaultFormatter(DataFreezeConstants.DATE_FORMAT);
        Integer total = DataFreezeUtil.getFreezeEventsTotalCount();
        List<AmpDataFreezeSettings> fetchedData = DataFreezeUtil.getDataFreeEventsList(offset, count, orderBy, sort,
                total);
        List<DataFreezeEvent> freezeEvents = new ArrayList<>();

        fetchedData.forEach(event -> {
            DataFreezeEvent dataFreezeEvent = new DataFreezeEvent();
            dataFreezeEvent.setId(event.getAmpDataFreezeSettingsId());
            dataFreezeEvent.setEnabled(event.getEnabled());
            dataFreezeEvent.setGracePeriod(event.getGracePeriod());
            dataFreezeEvent.setFreezingDate(dateFormatter.format(event.getFreezingDate()));
            dataFreezeEvent.setOpenPeriodStart(dateFormatter.format(event.getOpenPeriodStart()));
            dataFreezeEvent.setOpenPeriodEnd(dateFormatter.format(event.getOpenPeriodEnd()));
            dataFreezeEvent.setSendNotification(event.getSendNotification());
            dataFreezeEvent.setFreezeOption(event.getFreezeOption());
            dataFreezeEvent.setFilters(event.getFilters());
            dataFreezeEvent.setNotificationDays(event.getNotificationDays());
            dataFreezeEvent.setExecuted(event.getExecuted());
            dataFreezeEvent.setCount(getCountOfFrozenActivities(event));
            freezeEvents.add(dataFreezeEvent);
        });

        page.setData(freezeEvents);
        page.setTotalRecords(total);

        return page;
    }

    private static Integer getCountOfFrozenActivities(AmpDataFreezeSettings event) {
        Integer count = 0;
        if (event.getEnabled()) {
            GeneratedReport report = getFrozenActivitiesReport(event);
            count = report.reportContents.getChildren().size();
        }
        return count;
    }

    /**
     * Run report to generate a list of ids of frozen activities This takes into
     * consideration the Freezing Date and the applied filters
     * 
     * @param event
     * @return
     */
    private static GeneratedReport getFrozenActivitiesReport(AmpDataFreezeSettings event) {
        String name = "ActivityList";
        ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);
        spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
        FilterUtils.applyFilterRules(getFilters(event), spec, null);
        GeneratedReport report = EndpointUtils.runReport(spec);
        return report;
    }

    /**
     * Get Map of filters to apply to report - combines any filters applied in
     * the Data Freeze Event with a date filter created from Freezing Date
     * 
     * @param event
     * @return
     */
    private static LinkedHashMap<String, Object> getFilters(AmpDataFreezeSettings event) {
        LinkedHashMap<String, Object> filters;

        if (event.getFilters() != null) {
            // get any filters applied while creating the data freezing event
            JsonBean config = JsonBean.getJsonBeanFromString(event.getFilters());
            filters = (LinkedHashMap<String, Object>) config.get(EPConstants.FILTERS);
        } else {
            filters = new LinkedHashMap<>();
        }

        // add date filter in order to fetch activities before the freezing date
        Map<String, Object> dateFilter = new LinkedHashMap<>();
        dateFilter.put("end", DateTimeUtil.formatDate(AmpDateUtils.getDateBeforeDays(event.getFreezingDate(), 1),
                DataFreezeConstants.DATE_FORMAT));
        filters.put(FiltersConstants.DATE, dateFilter);

        return filters;
    }

    public static AmpDataFreezeSettings fetchOneDataFreezeEvent(long id) {
        return DataFreezeUtil.getDataFreezeEventById(id);
    }

    /**
     * Disable all data freeze events
     */
    public static void unfreezeAll() {
        DataFreezeUtil.unfreezeAll();
    }

    public static AmpActivityFrozen getActivityFrozenForActivity(Long activityId) {
        return DataFreezeUtil.getAmpActivityFrozenForActivity(activityId);
    }

    /**
     * Check if activity is editable
     * 
     * @param activityId
     * @param ampTeamMemberId
     * @return
     */
    public static boolean isEditable(AmpActivityFrozen ampActivityFrozen, AmpTeamMember atm) {
        return !isActivityAffectedByFreezing(ampActivityFrozen, atm) || ampActivityFrozen.getDataFreezeEvent()
                .getFreezeOption() == AmpDataFreezeSettings.FreezeOptions.FUNDING;
    }

    public static boolean isActivityAffectedByFreezing(AmpActivityFrozen ampActivityFrozen, AmpTeamMember atm) {
        return !(Boolean.TRUE.equals(atm.getUser().getExemptFromDataFreezing()) || ampActivityFrozen == null);
    }

    public static void processFreezingEvent() {
        AmpDataFreezeSettings currentFreezingEvent = DataFreezeUtil.getCurrentFreezingEvent();
        if (currentFreezingEvent != null) {
            freezeActivities(currentFreezingEvent);
        }
    }

    private static void freezeActivities(AmpDataFreezeSettings currentFreezingEvent) {
        // run the report for the corresponding freezing event
        GeneratedReport report = getFrozenActivitiesReport(currentFreezingEvent);
        Set<Long> activityIds = getActivityIds(report);
        // we disable previous freezing event since we have only one set of
        // activities frozen
        DataFreezeUtil.disablePreviousFrozenActivities();
        // we freeze the activities with the actual freezing event
        DataFreezeUtil.freezeActivitiesForFreezingDate(currentFreezingEvent, activityIds);
    }

    /**
     * Return the freezing configuration with the open period if all funding is
     * editable or the user is exempt we return null
     * 
     * @param activityId
     * @param ampTeamMemberId
     * @return
     * 
     */
    public static boolean isFundingEditable(AmpActivityFrozen ampActivityFrozen, AmpTeamMember atm,
            Date transactionDate) {

        // check if user is exempt for data freezing
        if (Boolean.TRUE.equals(atm.getUser().getExemptFromDataFreezing())) {
            return true;
        }
        if (ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart() == null
                || ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd() == null) {
            return false;
        }
        return transactionDate.compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart()) >= 0
                && transactionDate.compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd()) <= 0;
    }

    public static Set<Long> getActivityIds(GeneratedReport report) {
        Set<Long> ids = new HashSet<>();
        ReportOutputColumn ampIdCol = report.leafHeaders.get(0);
        for (Iterator<ReportArea> iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
            Map<ReportOutputColumn, ReportCell> contents = iterator.next().getContents();
            IdentifiedReportCell ampId = (IdentifiedReportCell) contents.get(ampIdCol);
            ids.add(ampId.entityId);
        }

        return ids;
    }

    public static boolean isOpenPeriod(AmpDataFreezeSettings event, Date todaysDate) {
        if (event.getOpenPeriodStart() == null || event.getOpenPeriodEnd() == null) {
            return false;
        }

        return (!todaysDate.before(event.getOpenPeriodStart()) && !todaysDate.after(event.getOpenPeriodEnd()));
    }

    public static boolean isGracePeriod(AmpDataFreezeSettings event, Date today) {
        Integer gracePeriod = event.getGracePeriod() != null ? event.getGracePeriod() : 0;
        if (gracePeriod.equals(0)) {
            return false;
        }
        Date gracePeriodEnd = AmpDateUtils.getDateAfterDays(event.getFreezingDate(), gracePeriod);
        return (!today.after(gracePeriodEnd));
    }

    public static Date getTodaysDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    public static Set<Long> getFronzeActivities() {
    return DataFreezeUtil.getFrozenActivities();
    }

    public static void unfreezeActivities(Set<Long>activitiesIdToFreeze) {
        DataFreezeUtil.unfreezeActivities(activitiesIdToFreeze);
    }

    public static JsonBean getFronzeActivitiesInformation() {
        String freezingDate = null;
        Integer freezingCount = 0;
        AmpDataFreezeSettings ampDataFreezeSettings = DataFreezeUtil.getLatestFreezingConfiguration();
        if (ampDataFreezeSettings != null) {
            Set<Long> frozenActivities = DataFreezeUtil.getFrozenActivities();
            String defaultDateFormat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(defaultDateFormat);
            freezingDate = dateFormatter.format(ampDataFreezeSettings.getFreezingDate());
            freezingCount = frozenActivities.size();
        }

        JsonBean freezingInformation = new JsonBean();
        freezingInformation.set("freezingDate", freezingDate);
        freezingInformation.set("freezingCount", freezingCount);
        return freezingInformation;
    }
}
