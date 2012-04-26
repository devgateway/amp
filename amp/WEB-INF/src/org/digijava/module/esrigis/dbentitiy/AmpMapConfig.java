package org.digijava.module.esrigis.dbentitiy;

/***
 * 
 * @author Diego Dimunzio
 *
 */

public class AmpMapConfig {

	private long id;
	private String mapurl;
	private int maptype;
	private String countyfield;
	private String districtfield;
	private String geoidfield;
	private String countfield;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMapurl() {
		return mapurl;
	}

	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}

	public int getMaptype() {
		return maptype;
	}

	public void setMaptype(int maptype) {
		this.maptype = maptype;
	}

	public String getCountyfield() {
		return countyfield;
	}

	public void setCountyfield(String countyfield) {
		this.countyfield = countyfield;
	}

	public String getDistrictfield() {
		return districtfield;
	}

	public void setDistrictfield(String districtfield) {
		this.districtfield = districtfield;
	}

	public String getGeoidfield() {
		return geoidfield;
	}

	public void setGeoidfield(String geoidfield) {
		this.geoidfield = geoidfield;
	}

	public String getCountfield() {
		return countfield;
	}

	public void setCountfield(String countfield) {
		this.countfield = countfield;
	}

}
