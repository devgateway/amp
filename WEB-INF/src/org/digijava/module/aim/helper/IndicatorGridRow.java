package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

public class IndicatorGridRow implements Comparable{
	private Long id;
	private String name;
	private List values;
	
	public IndicatorGridRow(AmpThemeIndicators ind,String[] years){
		this.id = ind.getAmpThemeIndId();
		this.name = ind.getName();
		if (years != null){
			this.values = new ArrayList(years.length);
			for (int i = 0; i < years.length; i++) {
				IndicatorGridItem item = new IndicatorGridItem(years[i],ind.getIndicatorValues());
				this.values.add(i,item);
			}
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getValues() {
		return values;
	}
	public void setValues(List values) {
		this.values = values;
	}
	public int compareTo(Object obj) {
		IndicatorGridRow other = (IndicatorGridRow) obj;
		return this.id.compareTo(other.getId());
	}

}
