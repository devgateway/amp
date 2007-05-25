package org.digijava.module.aim.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;

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

	/**
	 * Constructs new object from set of indicator values
	 * @param year string representing year
	 * @param values set of AmpThemeIndicatorValue beans
	 * @see org.digijava.module.aim.dbentity.AmpThemeIndicatorValue
	 */
	public IndicatorGridItem(String year, Set values){
		this.year = year;
		if (values != null && values.size() >0){
			
			AmpThemeIndicatorValue latestValue = null;
			int yearValue=Integer.valueOf(this.year).intValue();
			for (Iterator iter = values.iterator(); iter.hasNext();) {
				AmpThemeIndicatorValue value = (AmpThemeIndicatorValue) iter.next();
				//if this is target value
				if (value.getValueType() == 0){
					//currently there are no constraints ven vales are entered.
					//this means that in theory we may have several target values.
					this.targetValue = value.getValueAmount().toString();
				}
				//if this is actual value
				if (value.getValueType() == 1){
					Date creationDate = value.getCreationDate();
					if (creationDate!=null){
						//get creating year only
						Calendar cal = Calendar.getInstance();
						cal.setTime(creationDate);
						int creationYear = cal.get(Calendar.YEAR);
						//if this is the year we want
						if (creationYear == yearValue){
							// and latest Value is not set or current is the latest
							if (latestValue == null || creationDate.compareTo(latestValue.getCreationDate()) >= 0){
								//save latest cos we need latest
								latestValue = value;
							}
						}
					}
				}
			}
			
			if (latestValue !=null){
				this.actualValue = latestValue.getValueAmount().toString();
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
	
}
