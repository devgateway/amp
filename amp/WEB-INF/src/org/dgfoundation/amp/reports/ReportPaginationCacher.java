/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.LRUMap;
import org.dgfoundation.amp.ar.GroupReportDataCacher;
import org.digijava.kernel.request.TLSUtils;

/**
 * Report pagination cacher that reuses very same approach as in {@link GroupReportDataCacher} 
 * @author Nadejda Mandrescu
 */
public class ReportPaginationCacher {
	public final static int MAX_CACHED_REPORTS_PER_USER = 7;
	public final static String REPORT_AREA_CACHER_SESSION_ATTRIBUTE = "report_area_cache";
	
	private Map<Long, ReportAreaMultiLinked[]> lru = Collections.synchronizedMap(new LRUMap(MAX_CACHED_REPORTS_PER_USER));
	
	public ReportPaginationCacher() {
	}
	
	public void addAreas(Long reportId, ReportAreaMultiLinked[] areas) {
		if (areas != null) {
			lru.put(reportId, areas);
		}
	}
	
	public ReportAreaMultiLinked[] getAreas(Long reportId) {
		return lru.get(reportId);
	}
	
	public ReportAreaMultiLinked[] deleteAreas(Long reportId) {
		return lru.remove(reportId);
	}
	
	public static ReportAreaMultiLinked[] getReportAreas(Long reportId) {
		ReportPaginationCacher cacher = getOrCreateCacher();
		return cacher.getAreas(reportId);
	}
	
	public static void addReportAreas(Long reportId, ReportAreaMultiLinked[] areas) {
		ReportPaginationCacher cacher = getOrCreateCacher();
		cacher.addAreas(reportId, areas);
	}
	
	public static void deleteReportAreas(Long reportId, ReportAreaMultiLinked[] areas) {
		ReportPaginationCacher cacher = getOrCreateCacher();
		cacher.deleteAreas(reportId);
	}
	
	private static ReportPaginationCacher getOrCreateCacher()
	{
		HttpSession session = TLSUtils.getRequest().getSession();
		ReportPaginationCacher cacher = (ReportPaginationCacher) session.getAttribute(REPORT_AREA_CACHER_SESSION_ATTRIBUTE);
		if (cacher == null)
		{
			cacher = new ReportPaginationCacher();
			session.setAttribute(REPORT_AREA_CACHER_SESSION_ATTRIBUTE, cacher);
		}
		return cacher;
	}
}
