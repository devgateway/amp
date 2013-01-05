package org.digijava.kernel.request;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.RequestUtils;

/**
 * Thread-local storage utils: data stored per-current-request in the TLS area, to avoid carrying HttpServletRequest deep inside the stack
 * @author simple
 *
 */
public class TLSUtils {
	private static ThreadLocal<TLSUtils> threadLocalInstance = new ThreadLocal<TLSUtils>();
	
	public Site site;
	public Locale locale;
	public HttpServletRequest request;
	
	public static String getLangCode()
	{
		TLSUtils instance = getThreadLocalInstance();
		if (instance == null)
			return null;
		if (instance.locale == null)
			return null;
		
		return instance.locale.getCode();
	}
	
	public static Long getSiteId()
	{
		Site site = getSite();
		return Site.getIdOf(site);
	}
	
	public static Site getSite()
	{
		TLSUtils instance = getThreadLocalInstance();
		return instance.site;
	}
	
	public static HttpServletRequest getRequest()
	{
		TLSUtils instance = getThreadLocalInstance();
		return instance.request;
	}
	
	public static TLSUtils getThreadLocalInstance()
	{
		TLSUtils res = threadLocalInstance.get();
		if (res == null)
		{
			res = new TLSUtils();
			threadLocalInstance.set(res);
		}
		return res;
	}
	
	public static void populate(HttpServletRequest request)
	{
		SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
		TLSUtils.getThreadLocalInstance().request = request;
        TLSUtils.getThreadLocalInstance().site = siteDomain == null ? null : siteDomain.getSite();
        TLSUtils.getThreadLocalInstance().locale = RequestUtils.getNavigationLanguage(request);

	}
}
