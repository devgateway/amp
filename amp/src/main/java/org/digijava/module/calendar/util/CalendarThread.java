package org.digijava.module.calendar.util;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
/**
 * 
 * @author Diego Dimunzio
 *
 */
public class CalendarThread {

    private static ThreadLocal<Site> site = new ThreadLocal<Site>();
    private static ThreadLocal<Locale> locale = new ThreadLocal<Locale>();
    
    public static Site getSite() {
        return site.get();
    }
    public static void setSite(Site site) {
        CalendarThread.site.set(site);
    }
    public static Locale getLocale() {
        return locale.get();
    }
    public static void setLocale(Locale locale) {
        CalendarThread.locale.set(locale);
    }
    
    
}
