package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * Indicator Value entity.
 * Can be assigned to {@link IndicatorConnection}.
 * @author George Khakhanashvili
 *
 */
public class AmpIndicatorValue implements Serializable{
	
	public static final int TARGET=0;
	public static final int ACTUAL=1;
	public static final int BASE=2;
	public static final int REVISED=3;

	private static final long serialVersionUID = 1L;
	private Long indValId;
	private String comment;
	private Date valueDate;
	private Double value;
	private int valueType;
	private AmpCategoryValue logFrame;
	private AmpIndicatorRiskRatings risk;
	private Boolean defaultInd;
	private IndicatorConnection indicatorConnection;
	private AmpLocation location;
        
        /*these  helper methods are used 
         to set and get date object value to(from) the date input field
        see addEditValue.jsp*/
        public String getValueDateString(){
              if( valueDate!=null){
               return  DateTimeUtil.formatDate(valueDate);
            }
             return null;
        }

        public void setValueDateString(String valueDateString) throws Exception{
            if(!valueDateString.trim().equals("")){
               valueDate=DateTimeUtil.parseDate(valueDateString);
            }   
        }
	
	public Long getIndValId() {
		return indValId;
	}
	public void setIndValId(Long indValId) {
		this.indValId = indValId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	public Double getValue() {
		return FeaturesUtil.applyThousandsForVisibility(value);
	}
	public void setValue(Double value) {
		this.value = FeaturesUtil.applyThousandsForEntry(value);
	}
	public int getValueType() {
		return valueType;
	}
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
	public AmpCategoryValue getLogFrame() {
		return logFrame;
	}
	public void setLogFrame(AmpCategoryValue logFrame) {
		this.logFrame = logFrame;
	}
	public AmpIndicatorRiskRatings getRisk() {
		return risk;
	}
	public void setRisk(AmpIndicatorRiskRatings risk) {
		this.risk = risk;
	}
	public IndicatorConnection getIndicatorConnection() {
		return indicatorConnection;
	}
	public void setIndicatorConnection(IndicatorConnection indicatorConnection) {
		this.indicatorConnection = indicatorConnection;
	}
	public Boolean getDefaultInd() {
		return defaultInd;
	}
	public void setDefaultInd(Boolean defaultInd) {
		this.defaultInd = defaultInd;
	}
	public AmpLocation getLocation() {
		return location;
	}
	public void setLocation(AmpLocation location) {
		this.location = location;
	}
	
	
}
