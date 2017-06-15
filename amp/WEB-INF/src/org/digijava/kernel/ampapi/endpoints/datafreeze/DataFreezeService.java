package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportColumn;
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
            String name = "ActivityList";
            ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);
            spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
            FilterUtils.applyFilterRules(getFilters(event), spec, null);
            GeneratedReport report = EndpointUtils.runReport(spec);
            count = report.reportContents.getChildren().size();
        }

        return count;
    }

    private static LinkedHashMap<String, Object> getFilters(AmpDataFreezeSettings event) {
        LinkedHashMap<String, Object> filters;
        if (event.getFilters() != null) {
            JsonBean config = JsonBean.getJsonBeanFromString(event.getFilters());
            filters = (LinkedHashMap<String, Object>) config.get(EPConstants.FILTERS);
        } else {
            filters = new LinkedHashMap<>();
        }

        Map<String, Object> dateFilter = new LinkedHashMap<>();
        dateFilter.put("end", DateTimeUtil.formatDate(event.getFreezingDate(), DataFreezeConstants.DATE_FORMAT));
        filters.put(FiltersConstants.DATE, dateFilter);

        return filters;

    }

    public static AmpDataFreezeSettings fetchOneDataFreezeEvent(long id) {
        return DataFreezeUtil.getDataFreezeEventById(id);
    }

    public static void unfreezeAll() {
        DataFreezeUtil.unfreezeAll();
    }
}
