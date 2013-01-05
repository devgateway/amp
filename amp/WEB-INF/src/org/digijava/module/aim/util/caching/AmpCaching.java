package org.digijava.module.aim.util.caching;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.*;

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
	
	/**
	 * list of all fiscal calendars at moment of login
	 */
	public List<AmpFiscalCalendar> allFisCalendars;
	
	public void initSectorsCache(List<AmpSector> liveSectors)
	{
		sectorsCache = new SectorsCache(liveSectors);
	}
	
	public static AmpCaching getInstance()
	{
		HttpSession session = TLSUtils.getRequest().getSession();
		AmpCaching res = (AmpCaching) session.getAttribute("session_local_cache");
		if (res == null)
		{
			res = new AmpCaching();
			session.setAttribute("session_local_cache", res);
		}
		return res;			
	}
	
	public static void clearInstance()
	{
		try{
			TLSUtils.getRequest().getSession().removeAttribute("session_local_cache");
		}
		catch(Exception e){}
	}
	 
}
