package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorSector;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.DateConversion;

public class IndicatorUtil {

	private static Logger logger = Logger.getLogger(IndicatorUtil.class);

	public static void saveIndicators(AmpPrgIndicator tempPrgInd) {

		Session session = null;
		Transaction tx = null;
		AmpActivity act = null;
		AmpIndicator tempind = new AmpIndicator();
		try {
			session = PersistenceManager.getRequestDBSession();
			if (tempPrgInd.getIndicatorId() != null) {
				tempind = (AmpIndicator) session.load(AmpIndicator.class,
						tempPrgInd.getIndicatorId());
			}
			tempind.setName(tempPrgInd.getName());
			tempind.setCode(tempPrgInd.getCode());
			tempind.setType(tempPrgInd.getType());
			tempind.setDescription(tempPrgInd.getDescription());
			tempind.setCreationDate(DateConversion.getDate(tempPrgInd
					.getCreationDate()));
			tempind.setDefaultInd(tempPrgInd.isDefaultInd());

			Collection sect = tempPrgInd.getIndSectores();
			if (sect != null && sect.size() > 0) {
				Iterator<ActivitySector> sectIter = sect.iterator();
				while (sectIter.hasNext()) {
					ActivitySector sector = sectIter.next();
					AmpSector tempAmpSector = null;
					tempAmpSector = (AmpSector) session.load(AmpSector.class,
							sector.getSectorId());
					Set ampThemeSet = new HashSet();
					if (tempAmpSector.getIndicators() != null) {
						tempAmpSector.getIndicators().add(tempind);
					} else {
						Set indcators = new HashSet();
						indcators.add(tempind);
						tempAmpSector.setIndicators(indcators);
					}
					ampThemeSet.add(tempAmpSector);
					tempind.setSectors(ampThemeSet);
				}
			}

			if (tempPrgInd.getSelectedActivityId() != null
					&& tempPrgInd.getSelectedActivityId() > 0) {
				AmpActivity tmpAmpActivity = null;
				tmpAmpActivity = (AmpActivity) session.load(AmpActivity.class,
						tempPrgInd.getSelectedActivityId());
				Set activity = new HashSet();
				if (tmpAmpActivity.getIndicators() != null) {
					tmpAmpActivity.getIndicators().add(tempind);
				} else {

					Set indcators = new HashSet();
					indcators.add(tempind);
					tmpAmpActivity.setIndicators(indcators);
				}
				activity.add(tmpAmpActivity);
				tempind.setActivity(activity);
			}
			tx = session.beginTransaction();
			session.saveOrUpdate(tempind);
			tx.commit();

		} catch (Exception ex) {
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + ex);
		}
	}

	public static ArrayList getAmpIndicator() {
		Session session = null;
		Query q = null;
		AmpIndicator ampIndicator = null;
		ArrayList Indicator = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select t from " + AmpIndicator.class.getName()
					+ " t order by t.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampIndicator = (AmpIndicator) iter.next();
				Indicator.add(ampIndicator);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp indicators names  from database "
					+ ex.getMessage());
		}
		return Indicator;
	}

	public static AllPrgIndicators getAmpIndicator(Long indId) {
		Session session = null;
		AllPrgIndicators tempPrgInd = new AllPrgIndicators();

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpIndicator tempInd = (AmpIndicator) session.load(
					AmpIndicator.class, indId);

			tempPrgInd.setIndicatorId(tempInd.getIndicatorId());

			tempPrgInd.setName(tempInd.getName());
			tempPrgInd.setCode(tempInd.getCode());
			tempPrgInd.setType(tempInd.getType());
			tempPrgInd.setDescription(tempInd.getDescription());
			tempPrgInd.setCreationDate(DateConversion
					.ConvertDateToString(tempInd.getCreationDate()));
			tempPrgInd.setCategory(tempInd.getCategory());
			tempPrgInd.setSector(tempInd.getSectors());
			tempPrgInd.setActivity(tempInd.getActivity());
			session.flush();
		} catch (Exception e) {
			logger.error("Unable to get the specified Indicator");
			logger.debug("Exception : " + e);
		}
		return tempPrgInd;
	}

	public static Collection getAllIndicators(Long ampThemeId) {
		Session session = null;
		AmpTheme tempAmpTheme = null;
		Collection themeInd = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			tempAmpTheme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
			Set themeIndSet = tempAmpTheme.getIndicators();
			Iterator itrIndSet = themeIndSet.iterator();
			while (itrIndSet.hasNext()) {
				AmpIndicator tempThemeInd = (AmpIndicator) itrIndSet.next();
				AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
				Long ampThemeIndId = tempThemeInd.getIndicatorId();
				tempPrgInd.setIndicatorId(ampThemeIndId);
				tempPrgInd.setName(tempThemeInd.getName());
				tempPrgInd.setCode(tempThemeInd.getCode());
				tempPrgInd.setCreationDate(DateConversion
						.ConvertDateToString(tempThemeInd.getCreationDate()));
				tempPrgInd.setPrgIndicatorValues(ProgramUtil
						.getThemeIndicatorValues(ampThemeIndId));
				themeInd.add(tempPrgInd);
			}
		} catch (Exception ex) {
			logger.error("Exception from getThemeIndicators()  "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return themeInd;
	}

	public static void deleteIndicator(Long indId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			AmpIndicator tempindInd = (AmpIndicator) session.load(
					AmpIndicator.class, indId);

			Collection sect = tempindInd.getSectors();
			if (sect != null && sect.size() > 0) {
				Iterator<AmpSector> sectIter = sect.iterator();
				while (sectIter.hasNext()) {
					AmpSector sector = sectIter.next();
					AmpSector tempAmpSector = null;
					tempAmpSector = (AmpSector) session.load(AmpSector.class,
							sector.getAmpSectorId());
					Set ampThemeSet = new HashSet();
					tempAmpSector.getIndicators().remove(tempindInd);
					tempindInd.getSectors().remove(tempAmpSector);
				}
			}

			Collection activity = tempindInd.getActivity();
			if (activity != null && activity.size() > 0) {
				Iterator<AmpActivity> actItre = activity.iterator();
				while (actItre.hasNext()) {
					AmpActivity activit = actItre.next();
					AmpActivity tempActivity = null;
					tempActivity = (AmpActivity) session.load(
							AmpActivity.class, activit.getAmpActivityId());
					tempActivity.getIndicators().remove(tempindInd);
					tempindInd.getActivity().remove(tempActivity);
				}
			}

			session.delete(tempindInd);
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to delete the indicator");
			logger.debug("Exception : " + e);
		}
	}

	public static Collection getAllNonDefaultIndicators() {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select nondefInd from "
					+ AmpIndicator.class.getName()
					+ " nondefInd where nondefInd.defaultInd = false";
			qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + e);
			e.printStackTrace();
		}
		return col;
	}

	public static Collection getAllDefaultIndicators() {
		Session session = null;
		Collection<ActivityIndicator> coll = new ArrayList();
		Query qry = null;
		AmpIndicator ampIndicator = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select nondefInd from "
					+ AmpIndicator.class.getName()
					+ " nondefInd where nondefInd.defaultInd = true";
			qry = session.createQuery(queryString);
			iter = qry.list().iterator();

			while (iter.hasNext()) {
				ampIndicator = (AmpIndicator) iter.next();
				ActivityIndicator actInd = new ActivityIndicator();
				actInd.setIndicatorId(ampIndicator.getIndicatorId());
				actInd.setIndicatorCode(ampIndicator.getCode());
				actInd.setIndicatorName(ampIndicator.getName());
				actInd.setIndicatorValId(ampIndicator.getIndicatorId() + 1);
				coll.add(actInd);
			}

		} catch (Exception e) {
			logger.error("Unable to get default indicators");
			logger.debug("Exception : " + e);
			e.printStackTrace();
		}
		return coll;
	}

	public static Collection getActivityIndicatorsList(Long AmpActivityId) {

		Session session = null;
		Query qry = null;
		AmpActivity tempAmpactivity = null;
		Collection coll = new ArrayList();
		try {

			session = PersistenceManager.getRequestDBSession();
			tempAmpactivity = (AmpActivity) session.load(AmpActivity.class,
					AmpActivityId);
			Set activityIndiSet = tempAmpactivity.getIndicators();
			Iterator itrIndSet = activityIndiSet.iterator();
			while (itrIndSet.hasNext()) {
				AmpIndicator tempThemeInd = (AmpIndicator) itrIndSet.next();
				ActivityIndicator actInd = new ActivityIndicator();
				Long ampThemeIndId = tempThemeInd.getIndicatorId();
				actInd.setIndicatorId(ampThemeIndId);
				actInd.setIndicatorCode(tempThemeInd.getCode());
				actInd.setIndicatorName(tempThemeInd.getName());
				actInd.setIndicatorValId(tempThemeInd.getIndicatorId() + 1);
				coll.add(actInd);
			}
		}

		catch (Exception ex) {
			logger.error("Exception from getThemeIndicators()  "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return coll;

	}

	public static String getIndicatorName(Long id) {
		Session session = null;
		AmpIndicator Ind = null;
		String indName = null;
		String indCode = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Ind = (AmpIndicator) session.load(AmpIndicator.class, id);
			indName = Ind.getName();

		} catch (Exception ex1) {
			logger.error("Unable to get AmpIndicator object : " + ex1);
		}
		return indName;
	}

	public static String getIndicatorCode(Long id) {
		Session session = null;
		AmpIndicator Ind = null;
		String indCode = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Ind = (AmpIndicator) session.load(AmpIndicator.class, id);
			indCode = Ind.getCode();
		} catch (Exception ex1) {
			logger.error("Unable to get AmpIndicator object : " + ex1);
		}
		return indCode;
	}
	
	
	  public static ArrayList searchForindicator(String sectorname) {
  		Session session = null;
  		
  		Iterator iter =null;
  		AmpIndicator ampIndicator = null;
  		AmpSector ampsector =null;
		ArrayList Indicator = new ArrayList();
  		try {
  			session = PersistenceManager.getRequestDBSession();
  		    String queryString = "select sec from "
                + AmpSector.class.getName() + " sec "
                + "where sec.name=:name";
                Query qry = session.createQuery(queryString);
                qry.setString("name", sectorname);
   	
  		      iter = qry.list().iterator();

			while (iter.hasNext()) {
				ampsector = (AmpSector) iter.next();
				for(Iterator itr = ampsector.getIndicators().iterator();itr.hasNext();){
					AmpIndicator sect=(AmpIndicator)itr.next();
					Indicator.add(sect);
				}
				
			}
   			   
  		} catch (Exception ex) {
  			logger.debug("Unable to search " + ex);
  			}
  		return Indicator;
  	}
	  
	  
	public static void deleteIndsector(Long sectorid,Long indid){
		
		 logger.info(" deleting the indsectors");
			Session session = null;
			Transaction tx = null;
			AmpIndicator ampInd=null;
 	   
 	   try {
 		   session = PersistenceManager.getRequestDBSession();
 		   tx = session.beginTransaction();
 		  AmpIndicator tempindInd = (AmpIndicator) session.load(AmpIndicator.class, indid);
 		  
 		 Collection sect = tempindInd.getSectors();
			if (sect != null && sect.size() > 0) {
				Iterator<AmpSector> sectIter = sect.iterator();
				while (sectIter.hasNext()) {
					AmpSector sector = sectIter.next();
					
					if(sector!=null && sector.getAmpSectorId().equals(sectorid)){
					
						AmpSector tempAmpSector = null;
						tempAmpSector = (AmpSector) session.load(AmpSector.class,sector.getAmpSectorId());
						Set ampThemeSet = new HashSet();
						tempAmpSector.getIndicators().remove(tempindInd);
						tempindInd.getSectors().remove(tempAmpSector);
					}
	 		  }
		 }
			session.update(tempindInd);
	 		tx.commit();
	 		session.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception from deleteIndSectors:" + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	public static void deleteIndtheme(Long indid){
		
		 logger.info(" deleting the indthemes");
			Session session = null;
			Transaction tx = null;
			AmpIndicator ampInd=null;
	   
	   try {
		   session = PersistenceManager.getRequestDBSession();
		   tx = session.beginTransaction();
		  AmpIndicator tempindInd = (AmpIndicator) session.load(AmpIndicator.class, indid);
		  
		 Collection theme = tempindInd.getThemes();
			if (theme != null && theme.size() > 0) {
				Iterator<AmpTheme> themeItr = theme.iterator();
				while (themeItr.hasNext()) {
					AmpTheme themeInd = themeItr.next();
					
					if(themeInd!=null){
					
						AmpTheme tempAmpTheme = null;
						tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,themeInd.getAmpThemeId());
						Set ampThemeSet = new HashSet();
						tempAmpTheme.getIndicators().remove(tempindInd);
						tempindInd.getThemes().remove(tempAmpTheme);
					}
	 		  }
		 }
			session.update(tempindInd);
	 		tx.commit();
	 		session.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception from deleteIndSectors:" + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	 public static AmpIndicator getIndicatorById(Long indId){
         Session session = null;
         AmpIndicator tempInd = new AmpIndicator();

         try{
             session = PersistenceManager.getRequestDBSession();
             tempInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
             session.flush();
         }
         catch(Exception e){
             logger.error("Unable to get the specified Indicator");
             logger.debug("Exception : "+e);
         }
         return tempInd;
		}
	 
	 
	 public static void saveEditPrgIndValues(Collection<AmpPrgIndicatorValue> prgIndValues,AmpIndicator ampInd)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getRequestDBSession();
				Iterator indValItr = prgIndValues.iterator();
				while(indValItr.hasNext())
				{
					AmpThemeIndicatorValue ampThIndVal = null;
					AmpPrgIndicatorValue ampPrgIndVal = (AmpPrgIndicatorValue) indValItr.next();
					if(ampPrgIndVal.getIndicatorValueId() == null){
						ampThIndVal = new AmpThemeIndicatorValue();
					}else{
												ampThIndVal = (AmpThemeIndicatorValue) session.load(AmpThemeIndicatorValue.class,ampPrgIndVal.getIndicatorValueId());
					}
					ampThIndVal.setValueAmount(ampPrgIndVal.getValAmount());
					ampThIndVal.setCreationDate(DateConversion.getDate(ampPrgIndVal.getCreationDate()));
					ampThIndVal.setValueType(ampPrgIndVal.getValueType());
					ampThIndVal.setIndicatorId(ampInd);
					tx = session.beginTransaction();
					session.saveOrUpdate(ampThIndVal);
					tx.commit();
					}
				}
			
			catch(Exception ex)
			{
				
		             logger.error("Unable to get the specified Indicator");
		             logger.debug("Exception : "+ex);
			}
			
		}
	 
	  
}