package org.digijava.module.esrigis.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class MapsConfigurationForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 1 - Base map default 2 - Country map 3 - Indicator map 4 - Geometry
	 * service 5 - ArcGis javascript API URL 7 - Geo Locator Service 8 - Root
	 * URL for services where base maps are published 9 - National Border 10 -
	 * Poverty 11 - Population
	 */

	private String basemap;
	private String mainmap;
	private String admin1;
	private String admin2;
	private String geoid;
	private String Geometry;
	private String api;
	private String basemapsroot;
	private String geolocator;
	private String national;
	private String poverty;
	private String population;
	private String selectedtype;
	private String selectedurl;
	private String selectedadmin1;
	private String selectedadmin2;
	private String selectedgeoid;
	private Integer selectedlegend;
	private FormFile legend;
	
	public Integer getSelectedlegend() {
		return selectedlegend;
	}

	public void setSelectedlegend(Integer selectedlegend) {
		this.selectedlegend = selectedlegend;
	}

	public FormFile getLegend() {
		return legend;
	}

	public void setLegend(FormFile legend) {
		this.legend = legend;
	}

	public String getSelectedadmin1() {
		return selectedadmin1;
	}

	public void setSelectedadmin1(String selectedadmin1) {
		this.selectedadmin1 = selectedadmin1;
	}

	public String getSelectedadmin2() {
		return selectedadmin2;
	}

	public void setSelectedadmin2(String selectedadmin2) {
		this.selectedadmin2 = selectedadmin2;
	}

	public String getSelectedgeoid() {
		return selectedgeoid;
	}

	public void setSelectedgeoid(String selectedgeoid) {
		this.selectedgeoid = selectedgeoid;
	}

	public String getBasemap() {
		return basemap;
	}

	public void setBasemap(String basemap) {
		this.basemap = basemap;
	}

	public String getMainmap() {
		return mainmap;
	}

	public void setMainmap(String mainmap) {
		this.mainmap = mainmap;
	}

	public String getGeometry() {
		return Geometry;
	}

	public void setGeometry(String geometry) {
		Geometry = geometry;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getBasemapsroot() {
		return basemapsroot;
	}

	public void setBasemapsroot(String basemapsroot) {
		this.basemapsroot = basemapsroot;
	}

	public String getGeolocator() {
		return geolocator;
	}

	public void setGeolocator(String geolocator) {
		this.geolocator = geolocator;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getPoverty() {
		return poverty;
	}

	public void setPoverty(String poverty) {
		this.poverty = poverty;
	}

	public String getPopulation() {
		return population;
	}

	public void setPopulation(String population) {
		this.population = population;
	}

	public String getAdmin1() {
		return admin1;
	}

	public void setAdmin1(String admin1) {
		this.admin1 = admin1;
	}

	public String getAdmin2() {
		return admin2;
	}

	public void setAdmin2(String admin2) {
		this.admin2 = admin2;
	}

	public String getGeoid() {
		return geoid;
	}

	public void setGeoid(String geoid) {
		this.geoid = geoid;
	}

	public String getSelectedtype() {
		return selectedtype;
	}

	public void setSelectedtype(String selectedtype) {
		this.selectedtype = selectedtype;
	}

	public String getSelectedurl() {
		return selectedurl;
	}

	public void setSelectedurl(String selectedurl) {
		this.selectedurl = selectedurl;
	}
}
