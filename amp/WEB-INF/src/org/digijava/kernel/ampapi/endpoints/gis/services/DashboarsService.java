package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dgfoundation.amp.ar.MeasureConstants;
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
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class DashboarsService {
	
	/**
	 * Return (n) Donors sorted by amount
	 * @param limit
	 * @return
	 */
	public static JsonBean getTopDonors(Integer n) {
		String err = null;
		JsonBean retlist = new JsonBean();
		//Set default value for n to 5
		if (n==null){
			n=5;
		}
		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetTopDonors");
		spec.addColumn(new ReportColumn(MoConstants.DONOR_AGENCY,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS), false));
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
		retlist.set("Total","1523000");
		
		//TODO: Currencies for public user
		if(tm!=null){
			String currcode = CurrencyUtil.getCurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
			retlist.set("currency",currcode);
		}
		retlist.set("Numberformat",numberformat);
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			ReportAreaImpl reportArea =  (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[0];
			ReportCell reportcell = (ReportCell) content.values().toArray()[1];
			if(retlist.getSize()-2 <= n){
				retlist.set(reportcolumn.displayedValue,reportcell.value);
			}else{
				break;
			}
		}
		
		return retlist;
	}
}
