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
public class ReportCacher {
	public final static int MAX_CACHED_REPORTS_PER_USER = 7;
	public final static String REPORT_CACHER_SESSION_ATTRIBUTE = "report_cache";
	
	private Map<Long, CachedReportData> lru = Collections.synchronizedMap(
			new LRUMap(MAX_CACHED_REPORTS_PER_USER));
	
	public ReportCacher() {
	}
	
	public void addCachedReportData(Long reportId, CachedReportData cachedReportData) {
		if (cachedReportData != null) {
			lru.put(reportId, cachedReportData);
		}
	}
	
	public CachedReportData getCachedReportData(Long reportId) {
		return lru.get(reportId);
	}
	
	public CachedReportData deleteCachedReportData(Long reportId) {
		return lru.remove(reportId);
	}
	
	public static CachedReportData getReportData(Long reportId) {
		ReportCacher cacher = getOrCreateCacher();
		return cacher.getCachedReportData(reportId);
	}
	
	public static void addReportData(Long reportId, CachedReportData cachedReportData) {
		ReportCacher cacher = getOrCreateCacher();
		cacher.addCachedReportData(reportId, cachedReportData);
	}
	
	public static void deleteReportData(Long reportId) {
		ReportCacher cacher = getOrCreateCacher();
		cacher.deleteCachedReportData(reportId);
	}
	
	private static ReportCacher getOrCreateCacher()
	{
		HttpSession session = TLSUtils.getRequest().getSession();
		ReportCacher cacher = (ReportCacher) session.getAttribute(REPORT_CACHER_SESSION_ATTRIBUTE);
		if (cacher == null)
		{
			cacher = new ReportCacher();
			session.setAttribute(REPORT_CACHER_SESSION_ATTRIBUTE, cacher);
		}
		return cacher;
	}
}
