/**
 * 
 */
package org.dgfoundation.amp.ar.dyn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Alex Gartner
 *
 */
public class DynamicColumnsUtil {
	private static Logger logger = Logger.getLogger(DynamicColumnsUtil.class);
	
	private static ArrayList<AmpColumns> cachedMtefColumnList	= 	null;
	private static ArrayList<AmpMeasures> cachedMtefMeasureList	= 	null;
	
	public static void createInexistentMtefColumns (ServletContext sCtx) {
		List<Integer> mtefYears		= showInexistentMtefYears();
		if ( mtefYears != null && mtefYears.size() > 0 ) {
			for ( Integer year: mtefYears ) {
				AmpColumns	col		= new AmpColumns();
				col.setColumnName("MTEF " + year + "/" + (year+1));
				col.setAliasName("mtef" + year);
				col.setExtractorView("v_mtef_funding");
				col.setTokenExpression("MTEF " + year + "/" + (year+1) );
				col.setCellType("org.dgfoundation.amp.ar.cell.ComputedAmountCell");
				
				logger.info("Adding MTEF column for year " + year + "/" + (year+1) );
				dynamicallyCreateNewColumn(col,"Funding Information", sCtx);
			}
			DynamicColumnsUtil.cachedMtefColumnList	= null;
			MathExpressionRepository.buildMtefColumn();
		}
	}
	
	
	/**
	 * 
	 * @param newCol create a new AmpColumn object and give it to this function as parameter
	 * @param featureName name of the parent feature of this field   
	 */
	public static void dynamicallyCreateNewColumn(AmpColumns newCol, String featureName, ServletContext sCtx) {
		ColumnSavingEngine cse	= new ColumnSavingEngine(newCol, featureName, sCtx);
		cse.startSavingProcess();
	}
	
	
	public static List<AmpColumns> getMtefColumns() {
		ArrayList<AmpColumns> retList	= new ArrayList<AmpColumns>();
		if ( DynamicColumnsUtil.cachedMtefColumnList != null && DynamicColumnsUtil.cachedMtefColumnList.size() > 0 ) {
			retList.addAll(cachedMtefColumnList);
		}
		else {
			Collection<AmpColumns> allCols	= AdvancedReportUtil.getColumnList();
			if ( allCols != null ) {
				for ( AmpColumns col: allCols )  {
					if ( col.getColumnName().contains("MTEF") ) {
						retList.add(col);
					}
				}
			}
			DynamicColumnsUtil.cachedMtefColumnList	= new ArrayList<AmpColumns>();
			DynamicColumnsUtil.cachedMtefColumnList.addAll(retList);
		}
		return retList;
	}
	
	public static List<AmpMeasures> getMtefMeasures() {
		ArrayList<AmpMeasures> retList	= new ArrayList<AmpMeasures>();
		if ( DynamicColumnsUtil.cachedMtefMeasureList != null && DynamicColumnsUtil.cachedMtefMeasureList.size() > 0 ) {
			retList.addAll(cachedMtefMeasureList);
		}
		else {
			Collection<AmpMeasures> allCols	= AdvancedReportUtil.getMeasureList();
			if ( allCols != null ) {
				for ( AmpMeasures mes: allCols )  {
					if ( mes.getMeasureName().contains("MTEF") ) {
						retList.add(mes);
					}
				}
			}
			DynamicColumnsUtil.cachedMtefMeasureList	= new ArrayList<AmpMeasures>();
			DynamicColumnsUtil.cachedMtefMeasureList.addAll(retList);
		}
		return retList;
	}
	
	public static List<Integer> showInexistentMtefYears() {
		Session session;
		Query q;
		try {
    		session = PersistenceManager.getRequestDBSession();
    		String queryString = new String();
    		queryString = "select c.columnName from "
    			+ AmpColumns.class.getName()
    			+ " c where c.extractorView='v_mtef_funding' ";
    		q = session.createQuery(queryString);
    		List<String> mtefColNames	 	= q.list();
    		List<Integer> mtefFundingYears	= getMtefYears();
    		
    		if ( mtefFundingYears != null && mtefFundingYears.size() > 0 ) {
    			if ( mtefColNames != null ) {
    				for (String colName: mtefColNames) {
    					String yearStr		= colName.substring(colName.length()-4, colName.length() );
    					Integer year		= Integer.parseInt(yearStr)-1;
    					mtefFundingYears.remove(year);
    				}
    			}
    		}
    		
    		return mtefFundingYears;
    		
    		
    	} catch (Exception ex) {
    		ex.printStackTrace();	
    	}
    	return null;
	}
	
	public static List<Integer> getMtefYears() {
		Session session;
		Query q;
		try {
    		session = PersistenceManager.getRequestDBSession();
    		String queryString = new String();
    		queryString = "select distinct(year(p.projectionDate)) from "
    			+ AmpFundingMTEFProjection.class.getName()
    			+ " p ";
    		q = session.createQuery(queryString);
    		List<Integer> retList = q.list();
    		if ( retList!=null && retList.size()>0 ) {
    			Iterator<Integer> iter	= retList.iterator();
    			while (iter.hasNext() ) {
    				Integer item	= iter.next();
    				if ( item == null ) {
    					logger.warn("There seem to be some null projection_date-s in the amp_funding_mtef_projection");
    					iter.remove();
    				}
    			}
    		}
    		return retList;
    	} catch (Exception ex) {
    		ex.printStackTrace();	
    	}
    	return null;
	}
}
