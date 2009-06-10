package org.digijava.module.gis.helper;

import java.util.List;

import org.digijava.module.aim.dbentity.IndicatorSector;

/**
 * This helper class is used to link Indicator Sector and SubGroup(which comes from devInfo db)
 * for Results Dashboard Data Manager.
 * @author dare
 *
 */
public class IndicatorSectorWithSubgroup {
	private IndicatorSector indSector;
	private List<String> subGroups;
	
	public IndicatorSector getIndSector() {
		return indSector;
	}
	public void setIndSector(IndicatorSector indSector) {
		this.indSector = indSector;
	}
	public List<String> getSubGroups() {
		return subGroups;
	}
	public void setSubGroups(List<String> subGroups) {
		this.subGroups = subGroups;
	}
	
	public IndicatorSectorWithSubgroup(){
		
	}
	
	public IndicatorSectorWithSubgroup(IndicatorSector indSect, List<String> subGroups){
		this.indSector=indSect;
		this.subGroups=subGroups;
	}
	
}
