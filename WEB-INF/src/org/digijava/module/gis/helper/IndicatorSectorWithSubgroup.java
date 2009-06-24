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
	private String shortIndName;
	private String shortSectName;
	private String shortRegionName;
	
	public String getShortIndName() {
		return shortIndName;
	}
	public void setShortIndName(String shortIndName) {
		this.shortIndName = shortIndName;
	}
	public String getShortSectName() {
		return shortSectName;
	}
	public void setShortSectName(String shortSectName) {
		this.shortSectName = shortSectName;
	}
	public String getShortRegionName() {
		return shortRegionName;
	}
	public void setShortRegionName(String shortRegionName) {
		this.shortRegionName = shortRegionName;
	}
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
