package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

public class DataFreezeService {

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
			freezeEvents.add(new DataFreezeEvent(event.getAmpDataFreezeSettingsId(), event.getEnabled(),
					event.getGracePeriod(), dateFormatter.format(event.getFreezingDate()),
					dateFormatter.format(event.getOpenPeriodStart()), dateFormatter.format(event.getOpenPeriodEnd()),
					event.getSendNotification(), event.getFreezeOption(), event.getFilters(), null));
		});

		page.setData(freezeEvents);
		page.setTotalRecords(total);

		return page;
	}

	public static AmpDataFreezeSettings fetchOneDataFreezeEvent(long id) {
		return DataFreezeUtil.getDataFreezeEventById(id);
	}

	public static Page<AmpDataFreezeSettings> unfreezeAll() {
		return new Page<>();
	}
}
