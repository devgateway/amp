package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;

public class AmpFiscalCalendar implements Serializable, Identifiable
{
	private Long ampFiscalCalId ;
	private Integer startMonthNum ;
	private Integer yearOffset ;
	private Integer startDayNum ;
	private String name ;
	private String description ;
	private String baseCal ;
	
	/**
	 * @return
	 */
	public Long getAmpFiscalCalId() {
		return ampFiscalCalId;
	}

	/**
	 * @return
	 */
	public Integer getStartDayNum() {
		return startDayNum;
	}

	/**
	 * @return
	 */
	public Integer getStartMonthNum() {
		return startMonthNum;
	}

	/**
	 * @return
	 */
	public Integer getYearOffset() {
		return yearOffset;
	}

	/**
	 * @param long1
	 */
	public void setAmpFiscalCalId(Long long1) {
		ampFiscalCalId = long1;
	}

	/**
	 * @param i
	 */
	public void setStartDayNum(Integer i) {
		startDayNum = i;
	}

	/**
	 * @param i
	 */
	public void setStartMonthNum(Integer i) {
		startMonthNum = i;
	}

	/**
	 * @param i
	 */
	public void setYearOffset(Integer i) {
		yearOffset = i;
	}

	public String getBaseCal() {
		return baseCal;
	}

	public void setBaseCal(String string) {
		baseCal = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String string) {
		description = string;
	}

	public Object getIdentifier() {
		return this.getAmpFiscalCalId();
	}

}
	
	
	
	
