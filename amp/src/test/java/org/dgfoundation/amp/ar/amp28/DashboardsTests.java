package org.dgfoundation.amp.ar.amp28;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.visualization.util.DashboardUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** 
 * @author Gabriel Inchauspe
 * @since 05/18/2014
 */
public class DashboardsTests extends AmpTestCase {	

	public DashboardsTests(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MiscTests28.class.getName());
		suite.addTest(new DashboardsTests("testGetFiscalYearFromDate"));
		suite.addTest(new DashboardsTests("testGetStartDate"));
		suite.addTest(new DashboardsTests("testGetEndDate"));
		return suite;
	}
	
	public void testGetFiscalYearFromDate() throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");		
		
		// Test different dates for a fiscal year not starting on January 1st.
		AmpFiscalCalendar fiscalCalendar = new AmpFiscalCalendar();
		fiscalCalendar.setBaseCal("GREG-CAL");
		fiscalCalendar.setDescription("for testing fiscal calendar");
		fiscalCalendar.setIsFiscal(true);
		fiscalCalendar.setName("fiscal calendar");
		fiscalCalendar.setStartDayNum(1);
		fiscalCalendar.setStartMonthNum(7);
		assertEquals(2009, DashboardUtil.getFiscalYearFromDate(fiscalCalendar, formatter.parse("01/01/2010")));
		assertEquals(2009, DashboardUtil.getFiscalYearFromDate(fiscalCalendar, formatter.parse("06/01/2010")));
		assertEquals(2009, DashboardUtil.getFiscalYearFromDate(fiscalCalendar, formatter.parse("06/30/2010")));
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(fiscalCalendar, formatter.parse("07/01/2010")));
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(fiscalCalendar, formatter.parse("12/31/2010")));
		
		// Test different dates for a fiscal year not starting on January 1st.
		AmpFiscalCalendar calendar = new AmpFiscalCalendar();
		calendar.setBaseCal("GREG-CAL");
		calendar.setDescription("for testing normal calendar");
		calendar.setIsFiscal(false);
		calendar.setName("normal calendar");
		calendar.setStartDayNum(1);
		calendar.setStartMonthNum(1);
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(calendar, formatter.parse("01/01/2010")));
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(calendar, formatter.parse("06/01/2010")));
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(calendar, formatter.parse("06/30/2010")));
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(calendar, formatter.parse("07/01/2010")));
		assertEquals(2010, DashboardUtil.getFiscalYearFromDate(calendar, formatter.parse("12/31/2010")));				
	}
	
	public void testGetStartDate() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		AmpFiscalCalendar fiscalCalendar = new AmpFiscalCalendar();
		fiscalCalendar.setBaseCal("GREG-CAL");
		fiscalCalendar.setDescription("for testing fiscal calendar");
		fiscalCalendar.setIsFiscal(true);
		fiscalCalendar.setName("fiscal calendar");
		fiscalCalendar.setStartDayNum(1);
		fiscalCalendar.setStartMonthNum(7);
		assertEquals(formatter.parse("07/01/2010").toString(), DashboardUtil.getStartDate(fiscalCalendar, 2010).toString());
		assertEquals(formatter.parse("07/01/2011").toString(), DashboardUtil.getStartDate(fiscalCalendar, 2011).toString());
	}
	
	public void testGetEndDate() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		AmpFiscalCalendar fiscalCalendar = new AmpFiscalCalendar();
		fiscalCalendar.setBaseCal("GREG-CAL");
		fiscalCalendar.setDescription("for testing fiscal calendar");
		fiscalCalendar.setIsFiscal(true);
		fiscalCalendar.setName("fiscal calendar");
		fiscalCalendar.setStartDayNum(1);
		fiscalCalendar.setStartMonthNum(7);
		assertEquals(formatter.parse("06/30/2011").toString(), DashboardUtil.getEndDate(fiscalCalendar, 2010).toString());
		assertEquals(formatter.parse("06/30/2012").toString(), DashboardUtil.getEndDate(fiscalCalendar, 2011).toString());
	}
	
}
