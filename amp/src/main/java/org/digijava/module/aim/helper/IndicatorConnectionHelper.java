package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Indicator connection helper bean.
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorConnectionHelper {
    private Long connectionId;
    private AmpIndicator indicator; 
    private AmpTheme theme;
    private AmpActivityVersion  activity;
    private List<IndicatorValuesBean> values;
    
    public IndicatorConnectionHelper(){
        
    }

    public IndicatorConnectionHelper(IndicatorConnection dbBean){
        connectionId=dbBean.getId();
        indicator=dbBean.getIndicator();
        if (dbBean instanceof IndicatorActivity){
            activity=((IndicatorActivity)dbBean).getActivity();
        }
        if (dbBean instanceof IndicatorTheme){
            theme=((IndicatorTheme)dbBean).getTheme();
        }
        if (dbBean.getValues()!=null){
            this.values=new ArrayList<IndicatorValuesBean>();
            for (AmpIndicatorValue value : dbBean.getValues()) {
                IndicatorValuesBean valueBean=new IndicatorValuesBean(value);
                this.values.add(valueBean);
            }
        }
    }
    
    public Long getConnectionId() {
        return connectionId;
    }
    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
    public AmpIndicator getIndicator() {
        return indicator;
    }
    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }
    public AmpTheme getTheme() {
        return theme;
    }
    public void setTheme(AmpTheme theme) {
        this.theme = theme;
    }
    public List<IndicatorValuesBean> getValues() {
        return values;
    }
    public void setValues(List<IndicatorValuesBean> values) {
        this.values = values;
    }
    public AmpActivityVersion  getActivity() {
        return activity;
    }
    public void setActivity(AmpActivityVersion  activity) {
        this.activity = activity;
    }
    
    
    
}
