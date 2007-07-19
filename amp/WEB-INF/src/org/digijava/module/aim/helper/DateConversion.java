package org.digijava.module.aim.helper ;

import java.util.Date ;
import java.text.SimpleDateFormat ;
import org.apache.log4j.Logger ;

import java.util.Comparator;
import java.util.GregorianCalendar ;
import java.util.Calendar ;

 
public class DateConversion
{
	private static Logger logger = Logger.getLogger(DateConversion.class) ;

	public static Comparator dtComp = new Comparator() {
		public int compare(Object e1,Object e2) {
			if (e1 instanceof String &&
					e2 instanceof String) {
				String tdt1 = (String) e1;
				String tdt2 = (String) e2;
				if (tdt1 == null || tdt1.trim().length() < 1) {
					return -1;
				} else if (tdt2 == null || tdt2.trim().length() < 1) {
					return 1;
				}
				Date dt1 = DateConversion.getDate(tdt1);
				Date dt2 = DateConversion.getDate(tdt2);
				return dt2.compareTo(dt1);
			} else throw new ClassCastException();
		}
	};	
	
	public static String ConvertDateToString(Date mysqlDate )
	{
		String textDate ="";
		if ( mysqlDate != null ) {
			//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy") ;
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT) ;
			textDate = formatter.format(mysqlDate) ;
		}
		return textDate ;
	}	
	
	/**@author jose
	 * This method given a String like dd/mm/yyyy it will parse the date out of it and
	 * return a date object
	 * @param String dd/mm/yyyy or d/mm/yyyy or dd/m/yyyy or d/m/yyyy
	 * @return Date
	 */
	public static Date getDate(String strDate)
	{
		if (strDate == null) return null;
		if (strDate.length() > 10 || strDate.length() < 8) return null;
		
		int index = 0,curr = 0;
		
		index = strDate.indexOf('/', curr);
		int day = Integer.parseInt(strDate.substring(curr, index));
		
		curr = index + 1;
		index = strDate.indexOf('/', curr);
		int mon = Integer.parseInt(strDate.substring(curr, index));
		
		curr = index + 1;
		int yr = Integer.parseInt(strDate.substring(curr, strDate.length()));
		
		GregorianCalendar gc = new GregorianCalendar(yr,mon-1,day) ;
		/*if ( logger.isDebugEnabled() )
			logger.debug("getDate returning date with year " + gc.get(Calendar.YEAR)
					  + " month " + gc.get(Calendar.MONTH) 
					  + " day "  + gc.get(Calendar.DAY_OF_MONTH)) ; */
		return gc.getTime() ;	
	}
	
	/**@author jose
	 * This method given a String like dd/mm/yyyy it will parse the year out of it and
	 * return the year
	 * @param String dd/mm/yyyy or d/mm/yyyy or dd/m/yyyy or d/m/yyyy
	 * @return int year
	 */
	public static int getYear(String s)
	{
		//if ( logger.isDebugEnabled() )
			//logger.debug("getYear passed String" + s ) ;
		int yr = 0 ;
		if ( s != null && s.length() != 0 )		{
			String strYr = "" ;
			char[] arr = s.toCharArray() ;
			int i = s.length() - 1 ;
			while( arr[i] != '/' )
			{
				strYr += arr[i] ;
				i-- ;
			}		  
			StringBuffer sb = new StringBuffer(strYr) ;
			sb.reverse() ;
			yr= Integer.parseInt(sb.toString()) ;	
		}
		//if ( logger.isDebugEnabled() )
			//logger.debug("getYear returning year="+yr) ; 
		return yr ;
	}
	
}
