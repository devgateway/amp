package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

public class DataFreezeService {
	
	public static JsonBean saveDataFreezeEvent(JsonBean data){
		JsonBean result = new JsonBean();
		List<JsonBean> validationErrors =  validate(data);
		if (validationErrors.size() == 0) {
			AmpDataFreezeSettings dataFreezeEvent = getOrCreate(data);
			updateModel(dataFreezeEvent, data);
			DataFreezeUtil.saveDataFreezeEvent(dataFreezeEvent);
			JsonBean saved = modelToJsonBean(dataFreezeEvent);
			result.set(DataFreezeConstants.DATA, saved);
			result.set(DataFreezeConstants.RESULT, DataFreezeConstants.SAVE_SUCCESSFUL);			
		} else {
			result.set(DataFreezeConstants.DATA, data);
			result.set(DataFreezeConstants.RESULT, DataFreezeConstants.SAVE_FAILED);
			result.set(DataFreezeConstants.ERRORS, validationErrors);
		}
		
		return result;
	}
	
	private static List<JsonBean> validate(JsonBean data){
		List<JsonBean> errors = new ArrayList<>();
		return errors;
	}
	
	private static JsonBean modelToJsonBean(AmpDataFreezeSettings dataFreezeEvent) {
		JsonBean json  = new JsonBean();
		json.set(DataFreezeConstants.FIELD_ID, dataFreezeEvent.getAmpDataFreezeSettingsId());
		json.set(DataFreezeConstants.FIELD_ENABLED, dataFreezeEvent.getEnabled());
		json.set(DataFreezeConstants.FIELD_GRACE_PERIOD, dataFreezeEvent.getGracePeriod());
		json.set(DataFreezeConstants.FIELD_FREEZING_DATE, dataFreezeEvent.getFreezingDate());
		json.set(DataFreezeConstants.FIELD_OPEN_PERIOD_START, dataFreezeEvent.getOpenPeriodStart());
		json.set(DataFreezeConstants.FIELD_OPEN_PERIOD_END, dataFreezeEvent.getOpenPeriodEnd());
		json.set(DataFreezeConstants.FIELD_FREEZE_OPTION, dataFreezeEvent.getFreezeOption());
		json.set(DataFreezeConstants.FIELD_FILTERS, dataFreezeEvent.getFilters());
		json.set(DataFreezeConstants.FIELD_SEND_NOTIFICATION, dataFreezeEvent.getSendNotification());
		return json;
	}
	
	private static AmpDataFreezeSettings updateModel(AmpDataFreezeSettings dataFreezeSettings, JsonBean data){
		if (data.get(DataFreezeConstants.FIELD_ENABLED) != null) {
			dataFreezeSettings.setEnabled((Boolean)data.get(DataFreezeConstants.FIELD_ENABLED));
		}
		
		if (data.get(DataFreezeConstants.FIELD_GRACE_PERIOD) != null) {					
			dataFreezeSettings.setGracePeriod((Integer)(data.get(DataFreezeConstants.FIELD_GRACE_PERIOD)));
		}
		
		if (data.get(DataFreezeConstants.FIELD_FREEZING_DATE) != null) {
			Date freezingDate = DateTimeUtil.parseDate(data.getString(DataFreezeConstants.FIELD_FREEZING_DATE), DataFreezeConstants.DATE_FORMAT);		
			dataFreezeSettings.setFreezingDate(freezingDate);
		}
		
		if (data.get(DataFreezeConstants.FIELD_OPEN_PERIOD_START) != null) {
			dataFreezeSettings.setOpenPeriodStart(DateTimeUtil.parseDate(data.getString(DataFreezeConstants.FIELD_OPEN_PERIOD_START), DataFreezeConstants.DATE_FORMAT));
		}
		
		if (data.get(DataFreezeConstants.FIELD_OPEN_PERIOD_END) != null) {
			dataFreezeSettings.setOpenPeriodEnd(DateTimeUtil.parseDate(data.getString(DataFreezeConstants.FIELD_OPEN_PERIOD_END), DataFreezeConstants.DATE_FORMAT));
		}
		
		if (data.get(DataFreezeConstants.FIELD_FREEZE_OPTION) != null) {
			dataFreezeSettings.setFreezeOption(AmpDataFreezeSettings.FreezeOptions.valueOf(data.getString(DataFreezeConstants.FIELD_FREEZE_OPTION)));
		}
		
		if (data.get(DataFreezeConstants.FIELD_FILTERS) != null) {
			dataFreezeSettings.setFilters(data.getString(DataFreezeConstants.FIELD_FILTERS));
		}
		
		if (data.get(DataFreezeConstants.FIELD_SEND_NOTIFICATION) != null) {
			dataFreezeSettings.setSendNotification((Boolean)data.get(DataFreezeConstants.FIELD_SEND_NOTIFICATION));
		}
		
		return dataFreezeSettings;
	}
	
	public static AmpDataFreezeSettings getOrCreate(JsonBean data){
		AmpDataFreezeSettings dataFreezeSettings;
		if (data.getString(DataFreezeConstants.FIELD_ID) != null && NumberUtils.isNumber(data.getString(DataFreezeConstants.FIELD_ID))) {
			Long id = Long.parseLong(String.valueOf(data.get(DataFreezeConstants.FIELD_ID)));
			dataFreezeSettings = DataFreezeUtil.getDataFreezeEventById(id);
		} else {
			dataFreezeSettings = new AmpDataFreezeSettings();
		}
		
		return dataFreezeSettings;
	}

	public static void deleteDataFreezeEvent(long id){	
		DataFreezeUtil.deleteDataFreezeEvent(id);
	}
	
	public static Page<AmpDataFreezeSettings> fetchDataFreezeEventList(){
		return new Page<>();
	}
	
	public static AmpDataFreezeSettings fetchOneDataFreezeEvent(long id){
		return DataFreezeUtil.getDataFreezeEventById(id);
	}
	
	public static Page<AmpDataFreezeSettings> unfreezeAll(){
		return new Page<>();
	}
}
