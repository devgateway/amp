package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class DashboarsService {
	
	/**
	 * Return (n) Donors sorted by amount
	 * @param type (Donor, Regions, Primary Sector)
	 * @param adjtype (Actual Commitments, Actual Disbursement)
	 * @param n
	 * @return
	 */
	public static JsonBean getTops(String type,String adjtype, Integer n) {
		String err = null;
		String column = "";
		String adjustmenttype = "";
		JsonBean retlist = new JsonBean();
		JsonBean values = new JsonBean();
		
		switch (type.toUpperCase()) {
		case "DO":
			column = MoConstants.DONOR_AGENCY;
			break;
		case "RE":
			column = MoConstants.H_REGIONS;
			break;
		case "PS":
			column = MoConstants.PRIMARY_SECTOR;
			break;
		default:
			column = MoConstants.DONOR_AGENCY;
			break;
		}
		
		switch (adjtype.toUpperCase()) {
		case "AC":
			adjustmenttype = MoConstants.ACTUAL_COMMITMENTS;
			break;
		case "AD":
			adjustmenttype = MoConstants.ACTUAL_DISBURSEMENTS;
			break;
		default:
			adjustmenttype = MoConstants.ACTUAL_COMMITMENTS;
			break;
		}
		
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetTops");
		spec.addColumn(new ReportColumn(column,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(adjustmenttype,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addSorter(new SortingInfo(new ReportMeasure(adjustmenttype), false));
		spec.setCalculateColumnTotals(true);
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember");
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		
		//Format the report output return a simple list.
		//TODO: check why generate totals is not working
		retlist.set("Total","15000000");
		
		String currcode = EndpointUtils.getDefaultCurrencyCode();
		retlist.set("currency", currcode);
		
		retlist.set("Numberformat",numberformat);
		
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			ReportAreaImpl reportArea =  (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[0];
			ReportCell reportcell = (ReportCell) content.values().toArray()[1];
			if(values.getSize()<= n-1){
				values.set(reportcolumn.displayedValue,reportcell.value);
			}else{
				break;
			}
		}
		
		retlist.set("Vaues", values);
		return retlist;
	}
}
