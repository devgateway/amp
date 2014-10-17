package org.digijava.kernel.ampapi.endpoints.publicportal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;

/**
 * Public Portal Service
 * @author Nadejda Mandrescu
 *
 */
public class PublicPortalService {
	protected static final Logger logger = Logger.getLogger(PublicPortalService.class);
	
	/** the number of top projects to be provided */
	//shouldn't it be configurable?
	private static final int TOP_COUNT = 20;
	
	/**
	 * Retrieves top 20 projects based on fixed requirements.
	 * @return JsonBean object with results
	 * Requirement:
	 * Top 20 projects (by commitment size) during the selected time period. 
	 * The totals should be displayed in the currency selected by the admin 
	 * and fields to display are 
	 * Start Date, 
	 * Donor(s), 
	 * Primary Sector(s), 
	 * Project Title, 
	 * Cumulative Commitments, 
	 * Cumulative Disbursements.
	 */
	public static JsonBean getTopProjects() {
		JsonBean result = new JsonBean();
		List<JsonBean> content = new ArrayList<JsonBean>();
		result.set("topprojects", content);
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("PublicPortal_GetTopProjects");
		spec.addColumn(new ReportColumn(ColumnConstants.ACTUAL_START_DATE));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		
		spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS), false));
		
		spec.setFilters(getDefaultPublicFilter());
		
		GeneratedReport report = EndpointUtils.runReport(spec);
		if (report == null) {
			
		} else {
			if (report.reportContents != null && report.reportContents.getChildren() != null) {
				//provide header titles
				List<String> headers = new ArrayList<String>(report.leafHeaders.size());
				for (ReportOutputColumn leafHeader : report.leafHeaders) {
					headers.add(leafHeader.columnName);
				}
				result.set("headers", headers);
				
				//provide the top projects data
				int count = Math.min(getPublicPortalTopCount(), report.reportContents.getChildren().size());
				Iterator<ReportArea> iter = report.reportContents.getChildren().iterator();
				while (count > 0) {
					ReportArea data = iter.next();
					JsonBean jsonData = new JsonBean();
					for (Entry<ReportOutputColumn, ReportCell> cell : data.getContents().entrySet()) {
						jsonData.set(cell.getKey().columnName, cell.getValue().displayedValue);
					}
					content.add(jsonData);
					count --;
				}
			}
		}
		
		return result;
	}
	
	public static final int getPublicPortalTopCount() {
		//TODO: should be also configured in Admin?
		return TOP_COUNT;
	}
	
	/**
	 * @return period = number of months, the public portal data must be filtered by 
	 */
	public static final int getPublicPortalPeriodInMonths() {
		//TODO: pending the period to be configured in the admin. Setting a fixed one to 12 months till then
		int period = 12;
		return period;	
	}
	
	public static final ReportFilters getDefaultPublicFilter() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -getPublicPortalPeriodInMonths());
		MondrianReportFilters filters = new MondrianReportFilters();
		try {
			filters.addDateRangeFilterRule(cal.getTime(), null);
		} catch (AmpApiException e) {
			logger.error(e);
		}
		return filters;
	}
}
