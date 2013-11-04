package org.digijava.kernel.request;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.RequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Thread-local storage utils: data stored per-current-request in the TLS area, to avoid carrying HttpServletRequest deep inside the stack
 * @author simple
 *
 */
public class TLSUtils {
	private static final Logger logger = Logger.getLogger(TLSUtils.class);
	private static ThreadLocal<TLSUtils> threadLocalInstance = new ThreadLocal<TLSUtils>();

	public Site site;
	public HttpServletRequest request;
	
	public static String getLangCode() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        Locale lang = (Locale) request.getAttribute(Constants.NAVIGATION_LANGUAGE);
        if (lang == null){
            for (Cookie cookie: request.getCookies()){
                if (cookie.getName().equals("digi_language")) {
                    return cookie.getValue();
                }
            }
            //no cookie found
            logger.error("Barely missed an exception", new RuntimeException("please enable cookies!")); //we shouldn't get here :) - but we do EVERYTIME IN CHROME
            return "en";
        }
        String code = lang.getCode();
        //logger.error("Current language:" + code);
        return code;
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
	
	public static void populate(HttpServletRequest request){
		SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
		TLSUtils.getThreadLocalInstance().request = request;
        TLSUtils.getThreadLocalInstance().site = siteDomain == null ? null : siteDomain.getSite();
	}
}
