package org.digijava.module.aim.helper;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil.AmpIndicatorValuesComparatorByTypeAndYear;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Indicator value helper bean.
 * Stores indicator name and both values, target, and latest actual value for one particular year.
 * Used in reports and grids on NPD inside the IndicatorGridRow beans
 * @see org.digijava.module.aim.helper.IndicatorGridRow
 * @author Irakli Kobiashvili ikobiashvili@picktek.com
 *
 */
public class IndicatorGridItem {
	private Long valueId;
	private Long indicatorId;
	private String year;
	private String actualValue = " - ";
	private String targetValue = " - ";
	private String baseValue = " - ";

	/**
	 * Constructs new object from set of indicator values
	 * @param year string representing year
	 * @param values set of AmpThemeIndicatorValue beans
	 * @see org.digijava.module.aim.dbentity.AmpThemeIndicatorValue
	 */
	public IndicatorGridItem(String year, Set<AmpIndicatorValue> values){
		 this.year = year;
		if (values != null && values.size() >0){
			AmpIndicatorValue latestValue =null;
            AmpIndicatorValue latestTarValue =null;
			AmpIndicatorValue latestBaseValue =null;
			//AmpThemeIndicatorValue latestValue = null;
			int yearValue=Integer.valueOf(this.year).intValue();
				for (AmpIndicatorValue value : values) {
					//if this is actual value
					if (value.getValueType() == 1){
						Date creationDate = value.getValueDate();
						if (creationDate!=null){
							//get creating year only
							Calendar cal = Calendar.getInstance();
							cal.setTime(creationDate);
							int creationYear = cal.get(Calendar.YEAR);
							//if this is the year we want
							if (creationYear == yearValue){
								// and latest Value is not set or current is the latest
								if ((latestValue == null || creationDate.compareTo(latestValue.getValueDate()) > 0)){
									//save latest cos we need latest
									latestValue = value;
								}
							}
						}
					}
                                
			}
			
			   
                                
                                for (AmpIndicatorValue value : values) {
                                    /* if this is target value
                                     * note: we must select only target values which date is greater (or equal) than actual value's date
                                     * (if the  actual value exists)
                                     */
                                    if (value.getValueType() == 0 && (latestTarValue == null||
                                            value.getValueDate().after(latestTarValue.getValueDate())
                                            ||value.getValueDate().equals(latestTarValue.getValueDate()))) {
                                        Date targetDate = value.getValueDate();
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(targetDate);
                                        int targetYear = cal.get(Calendar.YEAR);
                                        if(targetYear >= yearValue){
                                        if (latestTarValue== null) {
                                            latestTarValue = value;
                                        }
                                        if (latestTarValue.getValueDate().after(value.getValueDate())) {
                                            latestTarValue = value;
                                        }
                                        }
                                    }
                                    /* if this is base value
                                     * note: we must select only base value which date is less (or equal) than actual 
                                     * value's date(if there  exists actual value)
                                     */
                                    
                                    if (value.getValueType() == 2 && (latestBaseValue == null || value.getValueDate().before(latestBaseValue.getValueDate()) || value.getValueDate().equals(latestBaseValue.getValueDate()))) {
                                        Date baseDate = value.getValueDate();
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(baseDate);
                                        int baseYear = cal.get(Calendar.YEAR);
                                        if (baseYear <= yearValue) {
                                            if (latestBaseValue == null) {
                                                latestBaseValue = value;
                                            }

                                            if (latestBaseValue.getValueDate().before(value.getValueDate())) {
                                                latestBaseValue = value;
                                            }
                                        }
                                    }
                                }
                            
                              if (latestValue != null) {
                                this.actualValue = latestValue.getValue().toString();
                                latestValue = null;
                            }   
			
                        if(latestBaseValue!=null){
                            this.baseValue=latestBaseValue.getValue().toString();
                        }
                         if(latestTarValue!=null){
                            this.targetValue=latestTarValue.getValue().toString();
                        }
		}
	}
	
	public String getActualValue() {
		return actualValue;
	}
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	public Long getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	public Long getValueId() {
		return valueId;
	}
	public void setValueId(Long valueId) {
		this.valueId = valueId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public String getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(String baseValue) {
		this.baseValue = baseValue;
	}
	
}
