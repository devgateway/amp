package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.List;
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
	 * Utility method for creating the small objects for the list of tops
	 * Note -- I (Phil) hacked this in... it could probably be done better
	 * @param pathId the id to use in the path for the actual data
	 * @param name the human-readable name for this top
	 * @return
	 */
	private static JsonBean getTopsListBean(String pathId, String name) {
		JsonBean obj = new JsonBean();
		obj.set("id", pathId);
		obj.set("name", name);
		return obj;
	}

	/**
	 * Return a list of the available top __ for the dashboard charts
	 * Note -- I (Phil) hacked this in, so it probably could use a review
	 * Also, I hard-coded the names ("Donor Agency" etc.) but they should be translated
	 * @return
	 */
	public static List<JsonBean> getTopsList() {
		List<JsonBean> tops = new ArrayList<JsonBean>();
		tops.add(getTopsListBean("do", "Donor Agency"));
		tops.add(getTopsListBean("re", "Region"));
		tops.add(getTopsListBean("ps", "Primary Sector"));
		return tops;
	}
	
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
		List<JsonBean> values = new ArrayList<JsonBean>();
		
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
		spec.getHierarchies().addAll(spec.getColumns());
		spec.addMeasure(new ReportMeasure(adjustmenttype,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addSorter(new SortingInfo(new ReportMeasure(adjustmenttype), false));
		spec.setCalculateRowTotals(true);
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
		//this is the report totals, which is not for the top N, but for ALL results 
		if (report.reportContents != null && report.reportContents.getContents() != null && report.reportContents.getContents().size() > 0) {
			ReportCell totals = (ReportCell)report.reportContents.getContents().values().toArray()[1];
			retlist.set("total", totals.value);
		} else {
			retlist.set("total", 0);
		}

		String currcode = EndpointUtils.getDefaultCurrencyCode();
		retlist.set("currency", currcode);
		
		retlist.set("Numberformat",numberformat);
		
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			JsonBean amountObj = new JsonBean();
			ReportAreaImpl reportArea =  (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[0];
			ReportCell reportcell = (ReportCell) content.values().toArray()[1];
			amountObj.set("name", reportcolumn.displayedValue);
			amountObj.set("amount", reportcell.value);
			values.add(amountObj);
			if (values.size() >= n) {
				break;
			}
		}
		
		retlist.set("values", values);
		return retlist;
	}
}
