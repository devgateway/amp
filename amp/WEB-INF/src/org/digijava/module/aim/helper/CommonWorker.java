package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.util.DbUtil;

public  class CommonWorker	{

	public static String getPerspective(String perspectiveName)	{
		String code = null;
		if ( perspectiveName.equalsIgnoreCase("Donor") )
			code = "DN";
		else if ( perspectiveName.equalsIgnoreCase("MOFED")	)
			code = "MA";
		else if ( perspectiveName.equalsIgnoreCase("Implementing Agency")	)
			code = "IA";
		return code;
	}
	
	public static FinancialFilters getFilters(Long teamId,
								  			  String pageCode)	{
		Long pageId = DbUtil.getPageId(pageCode);
		Collection c = DbUtil.getTeamPageFilters(teamId,pageId);
		FinancialFilters ff = new FinancialFilters();
		Iterator iter = c.iterator();
		ff.setCalendarPresent(false);
		ff.setCurrencyPresent(false);
		ff.setPerspectivePresent(false);
		ff.setYearRangePresent(false);
		ff.setGoButtonPresent(false);
		while ( iter.hasNext() )	{
			Long ampFilterId = (Long)iter.next();
			if ( ampFilterId.intValue()==1 )	{
				ff.setCalendarPresent(true);
				ff.setYearRangePresent(true);
				ff.setGoButtonPresent(true);
			}
				
			if ( ampFilterId.intValue()==2)	{
				ff.setCurrencyPresent(true);
				ff.setGoButtonPresent(true);
			}
				
			if ( ampFilterId.intValue()==8)
				ff.setPerspectivePresent(true);
		}
		return ff;
	}
}	