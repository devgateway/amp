package org.dgfoundation.amp.test.widget;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class WidgetTest extends TestCase {

	public WidgetTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testYearsGenerating(){
		String []fiscalYears1= new String [] {"FY 2005/06", "FY 2006/07", "FY 2007/08","FY 2008/09"};
		String []fiscalYears2= new String [] {"FY 2008/09"};
		String [] generatedYears1 =generateYearsArray(2005, 2008);
		String [] generatedYears2 =generateYearsArray(2011, 2008);
		
		assertTrue(Arrays.equals(fiscalYears1, generatedYears1));
		assertTrue(Arrays.equals(fiscalYears2, generatedYears2));
	}
	
//	public void testFundingDates(){
//		//Configuration.initConfig();
//		Date fromDate=generateDate(2008, 7, 1);
//		Date toDate=generateDate(2009, 7, 1);
//		//these two records should be between 2008/09 fiscal year
//		AmpFundingDetail afd1=new AmpFundingDetail(new Integer(0),new Integer(1),generateDate(2008,9,1),null,null);
//		AmpFundingDetail afd2=new AmpFundingDetail(new Integer(0),new Integer(1),generateDate(2008,10,6),null,null);
//		//this should be in 2007/2008 fiscal year
//		AmpFundingDetail afd3=new AmpFundingDetail(new Integer(0),new Integer(1),generateDate(2008,2,1),null,null);
//		Long firstFundDetailId,secondFundDetailId,thirdFundDetailId;
//		//save them in db
//		try {
//			firstFundDetailId=saveFundingDetail(afd1);
//			secondFundDetailId=saveFundingDetail(afd2);
//			thirdFundDetailId=saveFundingDetail(afd3);
//			Double[] allFundingWrapper={new Double(0)};			
//			//check 2008/2009 years' data
//			assertTrue(ChartWidgetUtil.getDonorSectorFunding(null,fromDate,toDate,allFundingWrapper).size()==2);
//			//check 2007/2008 years' data
//			fromDate=generateDate(2008, 1, 2);
//			toDate=generateDate(2008, 7, 1);			
//			assertTrue(ChartWidgetUtil.getDonorSectorFunding(null,fromDate,toDate,allFundingWrapper).size()==1);
//			//remove that funding details from db
//			removeFundingDetail(firstFundDetailId);
//			removeFundingDetail(secondFundDetailId);
//			removeFundingDetail(thirdFundDetailId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	private String[] generateYearsArray(int fromYear,int toYear){
		if(fromYear>toYear){
			fromYear=toYear;
		}
		String[] retValue=new String [(toYear-fromYear)+1];
		int i=0;
		for (int year=fromYear;year<=toYear;year++) {			
    		String nextYear=new Integer(year+1).toString();
    		String label="FY "+year+"/"+nextYear.substring(2);
    		retValue[i]=label;
    		i++;
		}
		return retValue;
	}	
	
	private Date generateDate(int year, int month,int day){
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month, day, 0, 0, 0);
		return cal.getTime();
	}
	
//	private Long saveFundingDetail(AmpFundingDetail afd) throws AimException {
//		Session session= null;
//		Transaction tx=null;
//		try {
//			session=PersistenceManager.getRequestDBSession();
//			tx=session.beginTransaction();
//			session.saveOrUpdate(afd);
//			tx.commit();
//		}catch(Exception ex) {
//			if(tx!=null) {
//				try {
//					tx.rollback();					
//				}catch(Exception e ) {					
//					throw new AimException("Can't rollback", e);
//				}			
//			}
//			throw new AimException("update failed",ex);
//		}
//		return afd.getAmpFundDetailId();
//	}
//	
//	private AmpFundingDetail getFundingDetail(Long id) throws AimException{
//		Session session=null;
//		String queryString =null;
//		Query query=null;
//		AmpFundingDetail returnValue=null;
//		try {
//			session=PersistenceManager.getRequestDBSession();
//			queryString= "select a from " + AmpFundingDetail.class.getName()+ " a where a.id="+id;
//			query=session.createQuery(queryString);
//			returnValue=(AmpFundingDetail)query.uniqueResult();
//		}catch(Exception ex) {				
//			ex.printStackTrace();
//		}
//		return returnValue;
//	}
//	
//	private void removeFundingDetail(Long id) throws Exception{
//		Session session=null;
//		Transaction trans=null;
//		try {
//			session=PersistenceManager.getRequestDBSession();
//			trans=session.beginTransaction();
//			AmpFundingDetail afd=getFundingDetail(id);
//			session.delete(afd);
//			trans.commit();
//		} catch (Exception ex) {
//			if(trans!=null) {
//				try {
//					trans.rollback();					
//				}catch(Exception e ) {					
//					throw new AimException("Can't rollback", e);
//				}			
//			}
//			throw new AimException("delete failed",ex);
//		}
//	}	
}
