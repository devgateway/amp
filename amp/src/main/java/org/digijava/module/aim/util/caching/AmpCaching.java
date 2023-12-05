package org.digijava.module.aim.util.caching;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * various caching stuff. <b> Only cache seldomly-changing data here</b><br />They are put together in a class to be easy to find out what to invalidate in case some global stuff changes
 * @author Dolghier Constantin
 *
 */
public class AmpCaching {
    
    /**
     * list of not deleted or archived sectors 
     */
    public SectorsCache sectorsCache = null;    
    public CurrencyCache currencyCache = new CurrencyCache();
    
    public Map<String, List<AmpOrganisation>> allOrgByRoleOfPortfolio = new HashMap<String, List<AmpOrganisation>>();
    public List<AmpOrgType> allOrgTypesOfPortfolio;
    public List<AmpOrgGroup> allOrgGroupsOfPortfolio;
    public List<AmpOrgGroup> allContractingAgencyGroupsOfPortfolio; 
    
    public AmpApplicationSettings applicationSettings;
    public boolean applicationSettingsRetrieved;
    
    
    /**
     * list of all fiscal calendars at moment of login
     */
    public List<AmpFiscalCalendar> allFisCalendars;
    
    public void initSectorsCache(List<AmpSector> liveSectors)
    {
        sectorsCache = new SectorsCache(liveSectors);
    }
    
    private static Map<String, AmpCaching> getAmpCaches()
    {
        HttpSession session = TLSUtils.getRequest().getSession();
        Map<String, AmpCaching> caches = (Map<String, AmpCaching>) session.getAttribute("session_local_cache");
        if (caches == null)
        {
            caches = new HashMap<String, AmpCaching>();
            session.setAttribute("session_local_cache", caches);
        }
        return caches;
    }
    
    public static AmpCaching getInstance()
    {
//      HttpSession session = TLSUtils.getRequest().getSession();
        String locale = TLSUtils.getEffectiveLangCode();
        
        Map<String, AmpCaching> caches = getAmpCaches();
        if (!caches.containsKey(locale))
            caches.put(locale, new AmpCaching());
        return caches.get(locale);          
    }
    
    public static void clearInstance()
    {
        try{
            TLSUtils.getRequest().getSession().removeAttribute("session_local_cache");
        }
        catch(Exception e){}
    }
}

