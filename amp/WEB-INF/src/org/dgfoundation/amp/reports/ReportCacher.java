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
	
	private Map<String, CachedReportData> lru = Collections.synchronizedMap(
			new LRUMap(MAX_CACHED_REPORTS_PER_USER));
	
	public ReportCacher() {
	}
	
	public void addCachedReportData(String reportToken, CachedReportData cachedReportData) {
		if (cachedReportData != null) {
			lru.put(reportToken, cachedReportData);
		}
	}
	
	public CachedReportData getCachedReportData(String reportToken) {
		return lru.get(reportToken);
	}
	
	public CachedReportData deleteCachedReportData(String reportToken) {
		return lru.remove(reportToken);
	}
	
	public static CachedReportData getReportData(String reportToken) {
		ReportCacher cacher = getOrCreateCacher();
		return cacher.getCachedReportData(reportToken);
	}
	
	public static void addReportData(String reportToken, CachedReportData cachedReportData) {
		ReportCacher cacher = getOrCreateCacher();
		cacher.addCachedReportData(reportToken, cachedReportData);
	}
	
	public static void deleteReportData(String reportToken) {
		ReportCacher cacher = getOrCreateCacher();
		cacher.deleteCachedReportData(reportToken);
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
