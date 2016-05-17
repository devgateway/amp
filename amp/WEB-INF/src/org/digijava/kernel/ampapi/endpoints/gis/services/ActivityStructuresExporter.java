package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;

public class ActivityStructuresExporter extends ActivityExporter {

	protected ActivityStructuresExporter(Map<String, Object> filters) {
		super(filters);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<String> getOriginalNames() {
		return Arrays.asList("Time Stamp", "Activity Id", "Project Title", "Project Site Description", 
				"Project Site", "Latitude", "Longitude", "Sectors", "Donors", "Total Project Commitments", 
				"Total Project Disbursements");
	}

	@Override
	public HSSFWorkbook export(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ReportSpecificationImpl generateCustomSpec() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<List<String>> generateRowValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Activity> generateActivities() {
		// TODO Auto-generated method stub
		return null;
	}

}
