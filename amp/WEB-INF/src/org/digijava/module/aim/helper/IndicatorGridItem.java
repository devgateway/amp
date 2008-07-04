package org.digijava.module.aim.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpIndicatorValue;

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
			if (values!=null){
				for (AmpIndicatorValue value : values) {
					//if this is target value
					if (value.getValueType() == 0) {
                                        //currently there are no constraints when vales are entered.
                                        //this means that in theory we may have several target values.
                                        if (latestTarValue == null) {
                                            latestTarValue = value;
                                        }
                                        Date targDate = value.getValueDate();
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(targDate);
                                        int tarYear = cal.get(Calendar.YEAR);
                                            /*searching  target value.  
                                              Its date must be equal or greater than current year 
                                              and be the oldest for selected year. 
                                              To recap if there is no target value for current year we will search in the following years targets values)
                                             */
                                        if (tarYear >= yearValue && latestTarValue.getValueDate().after(value.getValueDate())) {
                                            latestTarValue = value;
                                        }
                                    }
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
								if (latestValue == null || creationDate.compareTo(latestValue.getValueDate()) >= 0){
									//save latest cos we need latest
									latestValue = value;
								}
							}
						}
					}
                                    /*searching  base value.  
                                    Its date must be equal or less than current year 
                                    and be the newest for selected year. 
                                    To recap if there is no base value for current year we will search in the previous years targets values)
                                     */
                                    if (value.getValueType() == 2) {
                                        if (latestBaseValue == null) {
                                            latestBaseValue = value;
                                        }
                                        Date baseDate = value.getValueDate();
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(baseDate);
                                        int baseYear = cal.get(Calendar.YEAR);
                                        if (baseYear <= yearValue && latestBaseValue.getValueDate().before(value.getValueDate())) {
                                            latestBaseValue = value;
                                        }

                                    }

				}
			}
			
			if (latestValue !=null){
				this.actualValue = latestValue.getValue().toString();
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
