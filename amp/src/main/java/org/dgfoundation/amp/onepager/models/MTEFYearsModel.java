package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;

import java.util.Calendar;
import java.util.Date;

public class MTEFYearsModel implements IModel<KeyValue> {
    private static final long serialVersionUID = 1L;
    private IModel<Date> sourceModel;
    private boolean fiscal;


    public static boolean getFiscal(){
        boolean fiscal = true;

        //TeamMember tm = ((AmpAuthWebSession) Session.get()).getCurrentMember();
        //if (tm != null){
            //AmpApplicationSettings tempSettings = AmpARFilter.getEffectiveSettings(tm);
            Long defaultCalendarId = null;
//            if (tempSettings!=null && tempSettings.getFiscalCalendar()!=null){
//                defaultCalendarId=tempSettings.getFiscalCalendar().getAmpFiscalCalId();
//            }else{
                defaultCalendarId = Long.parseLong(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
//            }
            AmpFiscalCalendar ampFiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
            if (!ampFiscalCalendar.getIsFiscal())
                fiscal = false;
//        }
        return fiscal;
    }
    
    public static KeyValue convert(int year, boolean fiscal) {
        String key = Integer.toString(year);
        String value = Integer.toString(year);
        if (fiscal)
            value += "/" + Integer.toString(year+1);
        return new KeyValue(key, value);
    }

    public static KeyValue convert(Date date, boolean fiscal) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        return convert(year, fiscal);
    }

    public MTEFYearsModel(IModel<Date> sourceModel) {
        this.sourceModel = sourceModel;
        this.fiscal = getFiscal();
    }
    
    @Override
    public void setObject(KeyValue object) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(object.getKey()));
        sourceModel.setObject(calendar.getTime());
    }
    
    @Override
    public KeyValue getObject() {
        Date date = sourceModel.getObject();
        return convert(date, fiscal);
    }

    @Override
    public void detach() {
        sourceModel.detach();
    }
}



