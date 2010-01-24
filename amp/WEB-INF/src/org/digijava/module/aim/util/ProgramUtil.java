/*
 * ProgramUtil.java
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.IndicatorsBean;
import org.digijava.module.aim.helper.TreeItem;
import org.digijava.module.translation.util.DbUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ProgramUtil {

		private static Logger logger = Logger.getLogger(ProgramUtil.class);
        @Deprecated
		public static final int YAERS_LIST_START = 2000;
                public static final String NATIONAL_PLAN_OBJECTIVE ="National Plan Objective";
                public static final String PRIMARY_PROGRAM = "Primary Program";
                public static final String SECONDARY_PROGRAM = "Secondary Program";
                public static final int NATIONAL_PLAN_OBJECTIVE_KEY = 1;
                public static final int PRIMARY_PROGRAM_KEY = 2;
                public static final int SECONDARY_PROGRAM_KEY = 3;



		public static AmpTheme getTheme(String name) {
			Session session = null;
			AmpTheme theme = null;

			try {
				session = PersistenceManager.getRequestDBSession();
				String qryStr = "select theme from " + AmpTheme.class.getName()
						+ " theme where (theme.name=:name)";
				Query qry = session.createQuery(qryStr);
				qry.setParameter("name", name, Hibernate.STRING);
				Iterator itr = qry.list().iterator();
				if (itr.hasNext()) {
					theme = (AmpTheme) itr.next();
				}
			} catch (Exception e) {
				logger.error("Exception from getTheme()");
				logger.error(e.getMessage());
			} 
			return theme;
		}

		
		
		/**
		 * Load theme by ID.
		 * @param ampThemeId
		 * @return
		 * @throws DgException
		 */
        public static AmpTheme getThemeById(Long ampThemeId) throws DgException {
			Session session = PersistenceManager.getRequestDBSession();
			AmpTheme theme = null;
	
			try {
				theme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
			} catch (ObjectNotFoundException e) {
				logger.debug("AmpTheme with id " + ampThemeId + " not found");
			} catch (Exception e) {
				throw new DgException("Cannot load AmpTheme with id " + ampThemeId,
						e);
			}
			return theme;
		}
        
        public static Collection<AmpTheme> getAncestorThemes(AmpTheme theme) throws DgException {
        	HashSet<AmpTheme> returnSet		= new HashSet<AmpTheme>();
        	AmpTheme temp							= getThemeById(theme.getAmpThemeId());
        	if (temp != null)
        			temp										= temp.getParentThemeId();
        	while ( temp!=null ) {
        		returnSet.add(temp);
        		temp		= temp.getParentThemeId();
        	}
        	return returnSet;
        }

        /**
         * Retrieves top level themes.
         * @return
         * @throws DgException
         */
		@SuppressWarnings("unchecked")
		public static Collection<AmpTheme> getParentThemes() throws DgException{
			Session session = PersistenceManager.getRequestDBSession();
			Query qry = null;
			Collection<AmpTheme> themes = new ArrayList<AmpTheme>();

			try {
				String queryString = "select t from " + AmpTheme.class.getName()
						+ " t where t.parentThemeId is null";
				qry = session.createQuery(queryString);
				themes = qry.list();
			} catch (Exception e) {
				throw new DgException("Cannot search parent themes",e);
			}
			return themes;
		}

		/**
		 * Retrieves all themes from db.
		 * @return
		 * @throws DgException
		 */
		@SuppressWarnings("unchecked")
		public static List<AmpTheme> getAllThemes() throws DgException{
			Session session = null;
			Query qry = null;
			List<AmpTheme> themes = new ArrayList<AmpTheme>();

			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString = " from " + AmpTheme.class.getName()
						+ " t where t.parentThemeId is null";
				qry = session.createQuery(queryString);
				themes = qry.list();
			} catch (Exception e) {
				throw new DgException("Cannot retrive all themes from db",e);
			}
			return themes;
		}

		/**
         * Returns All AmpThemes including sub Themes if parameter is true.
         * @param includeSubThemes boolean false - only top level Themes, true - all themes
		 * @return
		 * @throws DgException
		 */
        @SuppressWarnings("unchecked")
		public static Collection<AmpTheme> getAllThemes(boolean includeSubThemes) throws DgException{
            Session session = null;
            Query qry = null;
            Collection<AmpTheme> themes = new ArrayList<AmpTheme>();

            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select t from " + AmpTheme.class.getName()+ " t ";
                if (!includeSubThemes) {
                    queryString += "where t.parentThemeId is null ";
                }
                qry = session.createQuery(queryString);
                themes = qry.list();
            }
            catch (Exception e) {
            	throw new DgException("Cannot load themes hierarchy",e);
            }
            return themes;
        }

//        public static Collection searchForindicators(String keyword,String sectorname) {
//    		Session session = null;
//    		Collection col = null;
//
//    		try {
//    			session = PersistenceManager.getRequestDBSession();
//    			String queryString = "select t from "
//    					+ AmpThemeIndicators.class.getName() + " t "
//    					+ "where t.ampThemeIndId in (select t1.themeIndicatorId from " +
//    					AmpIndicatorSector.class.getName() + " t1 join t1.sectorId si "+
//    					"where si.name = '"+sectorname+"')" +
//    					"and t.name like '%" + keyword + "%'";
//    			Query qry = session.createQuery(queryString);
//    			col = qry.list();
//    		} catch (Exception ex) {
//    			logger.debug("Unable to search " + ex);
//    			}
//    		return col;
//    	}


       
        
        /**
         * Returns list of years for drop-down list.
         * @return
         */
        public static Collection<LabelValueBean> getYearsBeanList(){
            String startYear=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START);
            int year=Integer.parseInt(startYear);
            return getYearsBeanList(year);
        }

        public static Collection<LabelValueBean> getYearsBeanList(int from){
            Collection<LabelValueBean> result=new ArrayList<LabelValueBean>();
            int start=from;
            Calendar now=Calendar.getInstance();
            int end=now.get(Calendar.YEAR)+1;
            for (int i = start; i <= end; i++) {
                result.add(new LabelValueBean(String.valueOf(i),String.valueOf(i)));
            }
            return result;
        }

       


        // New function added for the program Indicator Manager by pcsingh
	    public static Collection getAllMainPrograms() throws AimException
        {
        	Session session = null;
        	Query qry = null;
        	Collection colPrg = null;
        	try
        	{
        		session = PersistenceManager.getRequestDBSession();
    			String queryString = " from "
    								+ AmpTheme.class.getName() + " th";
    			qry = session.createQuery(queryString);
    			colPrg = qry.list();
        	}
        	catch(Exception ex)
        	{
        		logger.error("Unable to get all the Themes");
    			logger.debug("Exception " + ex);
    			throw new AimException(ex);
        	}
        	Collection mainProgram = new ArrayList();;
        	Iterator itr = colPrg.iterator();
        	while(itr.hasNext()) {
        		AmpTheme tmpTheme = (AmpTheme)itr.next();
        		if(tmpTheme.getParentThemeId()==null || tmpTheme.getParentThemeId().getAmpThemeId().intValue()==0) {
        			mainProgram.add(tmpTheme);
        		}
        	}
        	return mainProgram;
        }


        public static Collection getAllPrograms() throws AimException
        {
        	Session session = null;
        	Query qry = null;
        	Collection colPrg = null;
        	try
        	{
        		session = PersistenceManager.getRequestDBSession();
    			String queryString = " from "
    								+ AmpTheme.class.getName() + " th";
    			qry = session.createQuery(queryString);
    			colPrg = qry.list();
        	}
        	catch(Exception ex)
        	{
        		logger.error("Unable to get all the Themes");
    			logger.debug("Exception " + ex);
    			throw new AimException(ex);
        	}
        	return colPrg;
        }

       

	    public static ArrayList getThemesByIds(ArrayList ampThemeIds) {
	        Session session = null;
	        Query qry = null;

	        try {
	            session = PersistenceManager.getRequestDBSession();
	            String qryStr = "select t from " + AmpTheme.class.getName()
	                + " t where t.ampThemeId in (:ids)";
	            qry = session.createQuery(qryStr);
	            qry.setParameterList("ids", ampThemeIds);
	            return (ArrayList) qry.list();
	        } catch(Exception e) {
	            logger.error("Unable to get all themes" + e);
	            e.printStackTrace(System.out);
	        }
	        return null;
		}

		public static AmpTheme getThemeObject(Long ampThemeId) {
			Session session = null;
	        AmpTheme ampTheme = new AmpTheme();
	        try {
	        	session = PersistenceManager.getRequestDBSession();
	            ampTheme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
	        }
	        catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ampTheme;
		}


		public static String getHierarchyName(AmpTheme prog){
			String result="";
			List<AmpTheme> progs=new ArrayList<AmpTheme>();
			AmpTheme curProg = prog;
			while (curProg.getParentThemeId()!=null) {
				curProg = curProg.getParentThemeId();
				progs.add(curProg);
			}


			Collections.reverse(progs);

			for (ListIterator<AmpTheme> iterator = progs.listIterator(); iterator.hasNext();) {
				AmpTheme p = (AmpTheme) iterator.next();
				result += p.getName() + " > ";
			}

			result += prog.getName();
			return result;
		}



	/* Commemted by pcsingh
	 * due to some doubt abt assignment of indicator from one theme to other
	 * doubt are like what should be default values, same indicator can be
	 * assign to mant theme or not etc.
	 * Note: same functions are written twice for future use

		public static void assignThemeInd(Long indId, Long themeId){
			Session session = null;
			Transaction tx = null;
			AmpThemeIndicators ampThInd = null;
			AmpTheme ampTh = getThemeObject(themeId);
			try{
				session = PersistenceManager.getRequestDBSession();
				ampThInd = (AmpThemeIndicators)session.load(AmpThemeIndicators.class, indId);
				logger.info("Inside assignIndacators:::\n"+
						"ampThemeIndicators.getName"+ampThInd.getName()+"\n\n\n");
				Iterator getTheme = ampThInd.getThemes().iterator();
				while(getTheme.hasNext()){
					AmpTheme tmpAmpTheme = (AmpTheme)getTheme.next();
					logger.info("Getting thems related to given indicator::::"+
							"tmpAmpTheme.getName="+tmpAmpTheme.getName());
				}
				logger.info("\n\n\n");

				logger.info("Current theme name:::="+ampTh.getName());
				Iterator itr = ampTh.getIndicators().iterator();
				while(itr.hasNext()){
					AmpThemeIndicators tmpAmpThemeInd = (AmpThemeIndicators)itr.next();
					logger.info("\nGetting all Indicators related to theme::::\n"
							+"tmpAmpThemeIndicators.getName="+tmpAmpThemeInd.getName());
				}
				logger.info("\n\n\n");

			}catch(Exception ex){
				logger.error("Exception from assignThemeIndicator  "+ex.getMessage());
				ex.printStackTrace(System.out);
			}
		}
*/
	
		@Deprecated
		public static void assignThemeInd(Long indId, Long themeId)
		{

//			logger.info("\n\nInside program Util\n"+indId+"      "+themeId);
//			Session session = null;
//			Transaction tx = null;
//			AmpIndicator ampThInd = null;
//			AmpTheme ampTh = getThemeObject(themeId);
//			Set tmpTest = ampTh.getIndicators();
//			try
//			{
//				session = PersistenceManager.getRequestDBSession();
//				ampThInd = (AmpIndicator) session.load(AmpIndicator.class, indId);
//				logger.info(ampThInd.getName());
//				Set tempTh = ampThInd.getThemes();
//				Iterator itr = tempTh.iterator();
//				while(itr.hasNext()){
//					AmpTheme tmp = (AmpTheme)itr.next();
//					logger.info("Themes related to indicator\n\n"+tmp.getName());
//				}
//				tempTh.clear();
//				logger.info(tempTh.isEmpty());
//				tempTh.add(ampTh);
//				itr = tempTh.iterator();
//				while(itr.hasNext()){
//					AmpTheme tmp = (AmpTheme)itr.next();
//					logger.info("Now Themes related to indicator\n\n"+tmp.getName());
//				}
//				logger.info(tempTh.isEmpty());
				//ampThInd.setThemes(ampThInd);
//				ampThInd.setThemes(tempTh);

//				tmpTest.add(ampThInd);
//				ampTh.setIndicators(tmpTest);
//				tx = session.beginTransaction();
//				session.saveOrUpdate(ampThInd);
//				tx.commit();
//			}
//			catch(Exception ex) {
//				logger.error("Exception from getThemeIndicators()  " + ex.getMessage());
//				ex.printStackTrace(System.out);
//			}
		}
 //Comment over pcsing
		
		

		
                
      public static Collection getProgramIndicators(Long programId)
			throws DgException {
		Set indicators = new HashSet();
		ArrayList programs = (ArrayList) getRelatedThemes(programId);
		if (programs != null) {
			Iterator<AmpTheme> iterProgram = programs.iterator();
			while (iterProgram.hasNext()) {
				AmpTheme program = iterProgram.next();
				Set inds = IndicatorUtil.getIndicatorThemeConnections(program);
				if (inds != null) {
					indicators.addAll(inds);
				}
			}
		}
		return indicators;
	}
                
		

		//WWWTTTFFFF!!!!????!!!   
		public static Collection getAllSubThemes(Long parentThemeId)
		{
			Session session = null;
			Query qry = null;
			Collection tempCol1 = new ArrayList();
			Collection tempCol2 = new ArrayList();
			Collection tempCol3 = new ArrayList();
			Collection tempCol4 = new ArrayList();
			Collection tempCol5 = new ArrayList();
			Collection tempCol6 = new ArrayList();
			Collection tempCol7 = new ArrayList();
			Collection tempCol8 = new ArrayList();
			Collection allSubThemes = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
				// level 1 starts
				String queryString1 = "select subT from " +AmpTheme.class.getName()
									+ " subT where (subT.parentThemeId=:parentThemeId)";
				qry = session.createQuery(queryString1);
				qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
				tempCol1 = qry.list();
				if(!tempCol1.isEmpty())
				{
					Iterator tempItrCol1 = tempCol1.iterator();
					while(tempItrCol1.hasNext())
					{
						AmpTheme ampTheme1 = (AmpTheme) tempItrCol1.next();
						parentThemeId = ampTheme1.getAmpThemeId();
						allSubThemes.add(ampTheme1);
						//	level 2 starts
						String queryString2 = "select subT from " +AmpTheme.class.getName()
											+ " subT where (subT.parentThemeId=:parentThemeId)";
						qry = session.createQuery(queryString2);
						qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
						tempCol2 = qry.list();
						if(!tempCol2.isEmpty())
						{
							Iterator tempItrCol2 = tempCol2.iterator();
							while(tempItrCol2.hasNext())
							{
								AmpTheme ampTheme2 = (AmpTheme) tempItrCol2.next();
								parentThemeId = ampTheme2.getAmpThemeId();
								allSubThemes.add(ampTheme2);
								//	level 3 starts
								String queryString3 = "select subT from " +AmpTheme.class.getName()
													+ " subT where (subT.parentThemeId=:parentThemeId)";
								qry = session.createQuery(queryString3);
								qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
								tempCol3 = qry.list();
								if(!tempCol3.isEmpty())
								{
									Iterator tempItrCol3 = tempCol3.iterator();
									while(tempItrCol3.hasNext())
									{
										AmpTheme ampTheme3 = (AmpTheme) tempItrCol3.next();
										parentThemeId = ampTheme3.getAmpThemeId();
										allSubThemes.add(ampTheme3);
										//	level 4 starts
										String queryString4 = "select subT from " +AmpTheme.class.getName()
															+ " subT where (subT.parentThemeId=:parentThemeId)";
										qry = session.createQuery(queryString4);
										qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
										tempCol4 = qry.list();
										if(!tempCol4.isEmpty())
										{
											Iterator tempItrCol4 = tempCol4.iterator();
											while(tempItrCol4.hasNext())
											{
												AmpTheme ampTheme4 = (AmpTheme) tempItrCol4.next();
												parentThemeId = ampTheme4.getAmpThemeId();
												allSubThemes.add(ampTheme4);
												//	level 5 starts
												String queryString5 = "select subT from " +AmpTheme.class.getName()
																	+ " subT where (subT.parentThemeId=:parentThemeId)";
												qry = session.createQuery(queryString5);
												qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
												tempCol5 = qry.list();
												if(!tempCol5.isEmpty())
												{
													Iterator tempItrCol5 = tempCol5.iterator();
													while(tempItrCol5.hasNext())
													{
														AmpTheme ampTheme5 = (AmpTheme) tempItrCol5.next();
														parentThemeId = ampTheme5.getAmpThemeId();
														allSubThemes.add(ampTheme5);
														//	level 6 starts
														String queryString6 = "select subT from " +AmpTheme.class.getName()
																			+ " subT where (subT.parentThemeId=:parentThemeId)";
														qry = session.createQuery(queryString6);
														qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
														tempCol6 = qry.list();
														if(!tempCol6.isEmpty())
														{
															Iterator tempItrCol6 = tempCol6.iterator();
															while(tempItrCol6.hasNext())
															{
																AmpTheme ampTheme6 = (AmpTheme) tempItrCol6.next();
																parentThemeId = ampTheme6.getAmpThemeId();
																allSubThemes.add(ampTheme6);
																//	level 7 starts
																String queryString7 = "select subT from " +AmpTheme.class.getName()
																					+ " subT where (subT.parentThemeId=:parentThemeId)";
																qry = session.createQuery(queryString7);
																qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
																tempCol7 = qry.list();
																if(!tempCol7.isEmpty())
																{
																	Iterator tempItrCol7 = tempCol7.iterator();
																	while(tempItrCol7.hasNext())
																	{
																		AmpTheme ampTheme7 = (AmpTheme) tempItrCol7.next();
																		parentThemeId = ampTheme7.getAmpThemeId();
																		allSubThemes.add(ampTheme7);
																		//	level 8 starts
																		String queryString8 = "select subT from " +AmpTheme.class.getName()
																							+ " subT where (subT.parentThemeId=:parentThemeId)";
																		qry = session.createQuery(queryString8);
																		qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
																		tempCol8 = qry.list();
																		if(!tempCol8.isEmpty())
																		{
																			Iterator tempItrCol8 = tempCol8.iterator();
																			while(tempItrCol8.hasNext())
																			{
																				AmpTheme ampTheme8 = (AmpTheme) tempItrCol8.next();
																				allSubThemes.add(ampTheme8);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			catch(Exception e1) {
				logger.error("Unable to get all the Sub-Themes");
				logger.debug("Exception : "+e1);
			}
			return allSubThemes;
		}

		/**
		 * Returns all sub themes of specified one.
		 * @param parentThemeId
		 * @return
		 */
		public static Collection<AmpTheme> getSubThemes(Long parentThemeId) throws DgException {
			
			Session session = session = PersistenceManager.getRequestDBSession();
			Query qry = null;
			Collection<AmpTheme> subThemes = null;
			try {
			    String queryString = "from " + AmpTheme.class.getName() +
			        " subT where subT.parentThemeId.ampThemeId=:parentThemeId";
			    qry = session.createQuery(queryString);
			    qry.setParameter("parentThemeId",parentThemeId);
			    subThemes = qry.list();
			} catch(Exception e1) {
				throw new DgException("Cannot seacr sub themes for theme with id="+parentThemeId,e1);
			}
			return subThemes;
		}

		/**
		 * Return all subchildren in the tree
		 * Recursively iterates on all child programs till the end of the branch.
		 * @param parentThemeId db ID of the parent program
		 * @return collection of AmpTheme beans
		 * @throws AimException if enything goes wrong
		 */
		public static Collection getAllSubThemesFor(Long parentThemeId) throws AimException
		{
			Collection subThemes = new ArrayList();
			try
			{
				Collection progs = getSubThemes(parentThemeId);
				if (progs != null && progs.size()>0){
					subThemes.addAll(progs);
					for (Iterator iter = progs.iterator(); iter.hasNext();) {
						AmpTheme child = (AmpTheme) iter.next();
						Collection col = getAllSubThemes(child.getAmpThemeId());
						subThemes.addAll(col);
					}
				}
			}
			catch(Exception e1)
			{
				logger.error("Unable to get all the sub-themes");
				logger.debug("Exception : "+e1);
				throw new AimException("Cannot get sub themes for "+parentThemeId,e1);
			}
			return subThemes;
		}
                
                
                /**
		 * Returns all subchildren in the tree
		 * Recursively iterates on all children programs till the end of the branch.
                 * The names of children programs will be decorated with dashes.
                 * Use the method only for better presentation purpose
		 * @param parentThemeId db ID of the parent program
                 * @param depth specified the level of program in hierarchy.
                 * e.i. 0 means first level program, 1 second level, etc. 
                 * We use it to add "--" to the name of program for better presentation.
		 * @return collection of AmpTheme beans
		 * @throws AimException if anything goes wrong
                 * @see #getAmpThemesAndSubThemes(AmpTheme parent)
                 * @see #getAllSubThemesFor(Collection parentThemes)
		 */
		public static List getAllSubThemesByParentId(Long parentThemeId, int depth) throws AimException
		{
			List subThemes = new ArrayList();
			try
			{
				Collection progs = getSubThemes(parentThemeId);
				if (progs != null && progs.size()>0){
					for (Iterator iter = progs.iterator(); iter.hasNext();) {
						AmpTheme child = (AmpTheme) iter.next();
                                                AmpTheme childWithNewName=new AmpTheme();
                                                String name="";
                                                for(int i=0;i<depth;i++){
                                                name+="--";
                                                }
                                                childWithNewName.setName(name+"--"+child.getName());
                                                childWithNewName.setAmpThemeId(child.getAmpThemeId());
						List col = getAllSubThemesByParentId(child.getAmpThemeId(),depth+1);
                                                Collections.reverse(col);        
                                                col.add(childWithNewName);
                                                Collections.reverse(col);
						subThemes.addAll(col);
					}
				}
			}
			catch(Exception e1)
			{
				logger.error("Unable to get all the sub-themes");
				logger.debug("Exception : "+e1);
				throw new AimException("Cannot get sub themes for "+parentThemeId,e1);
			}
                        
			return subThemes;
		}
                
                
    /**
     * Returns List of programs and sub-programs using {@link #getAllSubThemesByParentId(Long parentThemeId, int depth) } method.
     *  The names of the programs are embellished 
     * (The main parent program will be in upper case and  dashes will be added to children programs) 
     *  for better presentation. 
     *  
     * @param parent parent program
     * @return collection of AmpTheme beans
     * 
     */
    public static List<AmpTheme> getAmpThemesAndSubThemes(AmpTheme parent) {
        List<AmpTheme> ret = new ArrayList<AmpTheme>();

        try {
            /* 
             We must create new program object because if you modify the name of program 
             the changes will be saved in db even though you don't save collection, strange issue....
            */
            AmpTheme parentWithNewName=new AmpTheme();
            parentWithNewName.setName(parent.getName().toUpperCase());
            parentWithNewName.setAmpThemeId(parent.getAmpThemeId());
            ret.add(parentWithNewName);
            List<AmpTheme> dbChildrenReturnSet =
                    ProgramUtil.getAllSubThemesByParentId(parent.getAmpThemeId(),0);
          //  Collections.reverse(dbChildrenReturnSet);
            ret.addAll(dbChildrenReturnSet);
        

        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }
    public static AmpTheme getAmpThemesAndSubThemesHierarchy(AmpTheme parent) {

        try {
            /* 
             We must create new program object because if you modify the name of program 
             the changes will be saved in db even though you don't save collection, strange issue....
            */
            AmpTheme parentWithNewName=new AmpTheme();
            parentWithNewName.setName(parent.getName().toUpperCase());
            parentWithNewName.setAmpThemeId(parent.getAmpThemeId());
            
            List<AmpTheme> dbChildrenReturnSet =
                    ProgramUtil.getAllSubThemesByParentId(parent.getAmpThemeId(),0);
            
            parent.getChildren().addAll( dbChildrenReturnSet );
        

        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return parent;
    }

                
                /**
                 * Return all subchildren of the parent program
                 * Recursively iterates on all child programs till the end of the branch using {@link #getAmpThemesAndSubThemes(AmpTheme parent)} .
                 * The method is used for better presentation purpose only
                 * @param parentThemes collection of  parent programs
                 * @return collection of AmpTheme beans
                 * @throws AimException if anything goes wrong
                 */
		public static List getAllSubThemesFor(Collection parentThemes) throws AimException
		{
                    List<AmpTheme> programs=new ArrayList();
                    
                    Iterator <AmpTheme> programsIter=parentThemes.iterator();
                    while(programsIter.hasNext()){
                        AmpTheme program=programsIter.next();
                        programs.addAll(getAmpThemesAndSubThemes(program));
                    }
                    return programs;
			
		}

		

		
			
		/**
		 * This was saving theme indicator but deprecated now.
		 * @deprecated there are no Theme Indicators any more. use {@link IndicatorUtil} methods.
		 * @param tempPrgInd
		 * @param ampThemeId
		 */
		
		public static void deleteTheme(Long themeId) throws AimException, DgException
		{
			ArrayList colTheme = (ArrayList)getRelatedThemes(themeId);
			int colSize = colTheme.size();
			for(int i=colSize-1; i>=0; i--)
			{
				AmpTheme ampTh = (AmpTheme) colTheme.get(i);
				/*Set tempIndicators = ampTh.getIndicators();
				Iterator tempInd = tempIndicators.iterator();
				while(tempInd.hasNext())
				{
					AmpThemeIndicators themeInd = (AmpThemeIndicators) tempInd.next();
					//deletePrgIndicator(themeInd.getAmpThemeIndId());
				}*/
				Session sess = null;
				Transaction tx = null;
				try {
					sess = PersistenceManager.getRequestDBSession();
					tx = sess.beginTransaction();
					AmpTheme tempTheme = (AmpTheme) sess.load(AmpTheme.class,ampTh.getAmpThemeId());
					sess.delete(tempTheme);
					tx.commit();
				} catch (HibernateException e) {
					logger.error(e);
					throw new AimException("Cannot delete theme with id "+themeId,e);
				} catch (DgException e) {
					logger.error(e);
				}
			}
		}

	
		
		/**
		 * @deprecated use {@link IndicatorUtil}
		 * @param indId
		 */
		@Deprecated
		public static void deleteProgramIndicator(Long indId){
			Session session = null;
			Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			AmpIndicator tempThemeInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
//			tempThemeInd.getThemes().remove(tempThemeInd);
			session.delete(tempThemeInd);
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to delete the themes");
			logger.debug("Exception : "+e);
		}
	}
		
		/**
		 * Deletes indicator with its values
		 * @param indId db ID of the indicator
		 * @throws AimException if any error with DB
		 * @deprecated
		 */
		@Deprecated
		public static void deletePrgIndicator(Long indId) throws AimException
		{
//			Session session = null;
//			Transaction tx = null;
//			try
//			{
//				//deletePrgIndicatorValue(indId);
//				session = PersistenceManager.getRequestDBSession();
//				tx = session.beginTransaction();
//				AmpIndicator tempThemeInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
//                Iterator itr=tempThemeInd.getThemes().iterator();
//                while(itr.hasNext()){
//                    AmpTheme tm=(AmpTheme)itr.next();
//                    Iterator indItr=tm.getIndicators().iterator();
//                    while(indItr.hasNext()){
//                        AmpIndicator tmInd=(AmpIndicator)indItr.next();
//                        if(tmInd.getIndicatorId().equals(indId)){
//                            indItr.remove();
//                            //please read http://www.hibernate.org/hib_docs/reference/en/html/example-parentchild.html
//                            session.delete(tmInd);
//                            tempThemeInd.getThemes().remove(tm);
//                            
//                        }
//                    }
//                    session.update(tempThemeInd);
//                }
//				tx.commit();
//				session.flush();
//			}
//			catch(Exception e1)
//			{
//				logger.error("The theme indicators were not deleted");
//				logger.debug("Exception : "+e1);
//				throw new AimException(e1);
//			}
		}

	
		
	
		public static void updateTheme(AmpTheme editeTheme)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getRequestDBSession();
				AmpTheme tempAmpTheme = null;
				tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,editeTheme.getAmpThemeId());
				tempAmpTheme.setName(editeTheme.getName());
				tempAmpTheme.setThemeCode(editeTheme.getThemeCode());
				tempAmpTheme.setBudgetProgramCode(editeTheme.getBudgetProgramCode());
				tempAmpTheme.setDescription(editeTheme.getDescription());
				tempAmpTheme.setTypeCategoryValue( editeTheme.getTypeCategoryValue());

				tempAmpTheme.setLeadAgency( editeTheme.getLeadAgency() );
				tempAmpTheme.setTargetGroups( editeTheme.getTargetGroups() );
				tempAmpTheme.setBackground( editeTheme.getBackground() );
				tempAmpTheme.setObjectives( editeTheme.getObjectives() );
				tempAmpTheme.setOutputs( editeTheme.getOutputs() );
				tempAmpTheme.setBeneficiaries( editeTheme.getBeneficiaries() );
				tempAmpTheme.setEnvironmentConsiderations( editeTheme.getEnvironmentConsiderations() );

				tempAmpTheme.setExternalFinancing(editeTheme.getExternalFinancing());
				tempAmpTheme.setInternalFinancing(editeTheme.getInternalFinancing());
				tempAmpTheme.setTotalFinancing(editeTheme.getTotalFinancing());
				
				tx = session.beginTransaction();
				session.update(tempAmpTheme);
				tx.commit();
			}
			catch(Exception ex)
			{
				logger.error("Exception from saveIndicator() : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null)
				{
					try
					{
						tx.rollback();
					}
					catch (Exception trbf)
					{
						logger.error("Transaction roll back failed : "+trbf.getMessage());
						trbf.printStackTrace(System.out);
					}
				}
			}			
		}
		
		
		public static Collection getRelatedThemes(Long id) throws DgException
		{
			AmpTheme ampThemetemp = new AmpTheme();
			ampThemetemp = getThemeObject(id);
			Collection<AmpTheme> themeCol = getSubThemes(id);
			Collection tempPrg = new ArrayList();
			tempPrg.add(ampThemetemp);
			if(!themeCol.isEmpty())
			{
				Iterator itr = themeCol.iterator();
				AmpTheme tempTheme = new AmpTheme();
				while(itr.hasNext())
				{
					tempTheme = (AmpTheme) itr.next();
					tempPrg.addAll(getRelatedThemes(tempTheme.getAmpThemeId()));
				}
			}
			return tempPrg;
		}


        public static String getThemesHierarchyXML(Collection<AmpTheme> allAmpThemes, HttpServletRequest request) throws Exception {
            String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            result += "<progTree>\n";
            if (allAmpThemes != null && allAmpThemes.size() > 0) {

                //make hieararchy of programs wrapped into TreeItem
                Collection themeTree = CollectionUtils.getHierarchy(allAmpThemes,
                        new ProgramHierarchyDefinition(), new XMLtreeItemFactory());

                //get XML from each top level item. They will handle subitems.
                for (Iterator treeItemIter = themeTree.iterator(); treeItemIter.hasNext(); ) {
                    TreeItem item = (TreeItem) treeItemIter.next();
                    result += item.getXml(request);
                }
            }
            result += "</progTree>\n";
            return result;
        }

        public static AmpActivityProgramSettings getAmpActivityProgramSettings(String name) throws DgException {
            Session session = null;
            AmpActivityProgramSettings programSettings=null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select ap from "
                                    + AmpActivityProgramSettings.class.getName()+ " ap "
                                    + "where ap.name=:name";
                    Query qry = session.createQuery(queryString);
                    qry.setString("name", name);
                    programSettings=(AmpActivityProgramSettings)qry.uniqueResult();


            } catch (Exception ex) {
                    logger.debug("Unable to search " + ex);
                    throw new DgException(ex);
                    }
            return programSettings;
    }


      public static List getAmpActivityProgramSettingsList() throws DgException {
            Session session = null;
            List programSettings=null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select ap from "
                                    + AmpActivityProgramSettings.class.getName()+ " ap";

                    Query qry = session.createQuery(queryString);
                    programSettings=qry.list();
                    if( programSettings==null|| programSettings.size()==0){
               programSettings=createDefaultAmpActivityProgramSettingsList();
               }


            } catch (Exception ex) {
                    logger.debug("Unable to search " + ex);
                    throw new DgException(ex);
                    }
            return programSettings;
    }


    public static List createDefaultAmpActivityProgramSettingsList() throws DgException {
        Session session = null;
        List programSettings=null;
        Transaction tx = null;


        try {
                session = PersistenceManager.getRequestDBSession();
                tx = session.beginTransaction();
                AmpActivityProgramSettings settingNPO=new AmpActivityProgramSettings("National Plan Objective");
                AmpActivityProgramSettings settingPP=new AmpActivityProgramSettings("Primary Program");
                AmpActivityProgramSettings settingSP=new AmpActivityProgramSettings("Secondary Program");
                session.save(settingNPO);
                session.save(settingPP);
                session.save(settingSP);
                tx.commit();
                programSettings=new ArrayList();
                programSettings.add(settingNPO);
                programSettings.add(settingPP);
                programSettings.add(settingSP);


             } catch (Exception ex) {
              logger.error("Unable to create Default program Settings List  " + ex);
                        if (tx != null)
                          {
                                  try
                                  {
                                          tx.rollback();
                                  }
                                  catch (Exception extx)
                                  {
                                          logger.error("Transaction roll back failed : "+extx.getMessage());

                                  }
                          }


                    throw new DgException(ex);
                    }

        return programSettings;
}


    public static String printHierarchyNames(AmpTheme child) {
    	Session session = null;
        Transaction tx = null;
        String names = "";
        try {
            session = PersistenceManager.getRequestDBSession();
            //tx = session.beginTransaction();
            session.refresh(child);
    	
            AmpTheme parent = child.getParentThemeId();
            if (parent == null) {
                    return names;
            }
            else {
                    names = printHierarchyNames(parent);
                    names += "[" + parent.getName() + "] ";
            }
            //tx.rollback();
        } catch (Exception ex) {
        	logger.error("Unable to get Hierarchy: " + ex);
        	ex.printStackTrace();
        }
        return names;
    }

    //save new settings
    public static void saveAmpActivityProgramSettings(List settings) throws
        DgException {
            Session session = null;
            Transaction tx = null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    if (settings != null) {
                            Iterator settingsIter = settings.iterator();
                            tx = session.beginTransaction();
                            while (settingsIter.hasNext()) {
                                    AmpActivityProgramSettings setting = (AmpActivityProgramSettings)settingsIter.next();
                                    if(setting.getDefaultHierarchy() != null && setting.getDefaultHierarchy().getAmpThemeId() != null  )
                                    {
                                    	AmpActivityProgramSettings oldSetting = (AmpActivityProgramSettings) session.get(AmpActivityProgramSettings.class,setting.getAmpProgramSettingsId());
	                                    oldSetting.setAllowMultiple(setting.isAllowMultiple());
                                    	if (setting.getDefaultHierarchy().getAmpThemeId() != -1){
    	                                    oldSetting.setDefaultHierarchy(setting.getDefaultHierarchy());
                                    	}else{
    	                                    oldSetting.setDefaultHierarchy(null);
                                    	}
	                                    session.update(oldSetting);
                                    }

                            } 
                            tx.commit();

                    }

            }
            catch (Exception ex) {
                    logger.error("Unable to save program Setting  " + ex);
                    if (tx != null) {
                            try {
                                    tx.rollback();
                            }
                            catch (Exception extx) {
                                    logger.error(
                                        "Transaction roll back failed : " +
                                        extx.getMessage());

                            }
                    }

                    throw new DgException(ex);
            }

    }


    
	 public static String renderLevel(Collection themes,int level,HttpServletRequest request) {
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 //String noProgPresent = "aim:noProgramsPresent"; not used any more cos hash key translation
		 Site site = RequestUtils.getSite(request);
		 //
		 //requirements for translation purposes
		 TranslatorWorker translator = TranslatorWorker.getInstance();
		 Long siteId = site.getId();
		 String locale = RequestUtils.getNavigationLanguage(request).getCode();
		 String translatedText = null;
		 try {
			logger.info("siteID : "+siteId);
			logger.info("locale : "+locale);
			translatedText = TranslatorWorker.translateText("No Programs present", locale, siteId);
		 } catch (WorkerException e) {
			e.printStackTrace();
		 }
		 if (themes == null || themes.size() == 0)
			return "<center><b>"+translatedText+"</b></<center>";		
		 String retVal;
		retVal = "<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"0\" valign=\"top\" align=\"left\" bgcolor=\"#ffffff\" border=\"0\" style=\"border-collapse: collapse;\">\n";
		Iterator iter = themes.iterator();
		int rc = 0;
		while (iter.hasNext()) {
			TreeItem item = (TreeItem) iter.next();
			AmpTheme theme = (AmpTheme) item.getMember();
			retVal += "<tr><td>&nbsp;</td><td width=\"100%\">\n";

			
			// visible div start
			retVal += "<div>";// id=\"div_theme_"+theme.getAmpThemeId()+"\"";
			retVal += " <table width=\"100%\"  border=\"1\" style=\"border-collapse: collapse;border-color: #ffffff\">";
			if (rc++%2 == 0){
				retVal += "<tr class=\"tableEven\" onmouseover=\"this.className='Hovered'\" onmouseout=\"this.className='tableEven'\">";
			}else{
				retVal += "<tr class=\"tableOdd\" onmouseover=\"this.className='Hovered'\" onmouseout=\"this.className='tableOdd'\">";
			}
			retVal += "   <td width=\"1%\" >";
			retVal += "     <img id=\"img_" + theme.getAmpThemeId()+ "\" onclick=\"expandProgram(" + theme.getAmpThemeId()+ ")\" src=\"/TEMPLATE/ampTemplate/imagesSource/common/tree_plus.gif\"/>\n";
			retVal += "     <img id=\"imgh_"+ theme.getAmpThemeId()+ "\" onclick=\"collapseProgram("+ theme.getAmpThemeId()+ ")\" src=\"/TEMPLATE/ampTemplate/imagesSource/common/tree_minus.gif\"  style=\"display : none;\"/>\n";
			retVal += "   </td>";
			if (level>1){
				retVal += "   <td width=\"1%\">";
				retVal += "     <img src=\"/TEMPLATE/ampTemplate/imagesSource/common/link_out_bot.gif\"/>\n";
				retVal += "   </td>";
				retVal += "   <td width=\"1%\">";
				retVal += "     <img src=\""+getLevelImage(level)+"\" />\n";
				retVal += "   </td>";
			}
			retVal += "   <td  class=\"progName\">";
			retVal += "    <a href=\"javascript:editProgram("+ theme.getAmpThemeId()+ ")\">"+getTrn("aim:admin:themeTree:theme_name",((AmpTheme) item.getMember()).getEncodeName(), request)+"</a>\n";
			retVal += "   </td>";
			retVal += "   <td class=\"progCode\"  width=\"45%\" nowrap=\"nowrap\">("+ ((AmpTheme) item.getMember()).getThemeCode() + ")</td>";
			retVal += "   <td nowrap=\"nowrap\" width=\"10%\">";
			retVal += "     <a href=\"javascript:addSubProgram('5','"+theme.getAmpThemeId() +"','"+level+"','"+theme.getEncodeName()+"')\">"+getTrn("aim:admin:themeTree:add_sub_prog", "Add Sub Program", request)+"</a> |\n";
			retVal += "   </td>";
			retVal += "   <td nowrap=\"nowrap\" width=\"10%\">";
			retVal += "     <a href=\"javascript:assignIndicators('"+theme.getAmpThemeId() +"')\">"+getTrn("aim:admin:themeTree:manage_indicators", "Manage Indicators", request)+"</a>\n";
			retVal += "   </td>";
			retVal += "   <td width=\"12\">";
			retVal += "     <a href=\"/aim/themeManager.do~event=delete~themeId="+theme.getAmpThemeId()+"\" onclick=\"return deleteProgram()\"><img src=\"/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif\" border=\"0\"></a>";
			retVal += "   </td>";
			retVal += " </tr></table>";
			retVal += "</div>\n";
	
			// hidden div start
			retVal += "<div id=\"div_theme_" + theme.getAmpThemeId()+ "\" style=\"display : none;\">\n";
			if (item.getChildren() != null || item.getChildren().size() > 0) {
				retVal += renderLevel(item.getChildren(), level+1,request);
			}
			retVal += "</div>\n";

			retVal += "</td></tr>\n";
		}
		retVal += "</table>\n";
		return retVal;
	}

	 public static String getTrn(String key, String defResult, HttpServletRequest request){
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 //return CategoryManagerUtil.translate(key, request, defResult);
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		Long	siteId	= RequestUtils.getSite(request).getId();
		
		Message m = null;
		
		try {
			m = DbUtil.getMessage(key.toLowerCase(), lang, siteId);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (m == null)
		 {
			 return defResult;
		 }
		 else
		 {
			 return m.getMessage();
		 }
		 
	 }
	 
    public static String getLevelImage(int level){
    	switch (level) {
		case 0:
			return "/TEMPLATE/ampTemplate/imagesSource/common/arrow_right.gif";
		case 1:
			return "/TEMPLATE/ampTemplate/imagesSource/common/arrow_right.gif";
		case 2:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square1.gif";
		case 3:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square2.gif";
		case 4:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square3.gif";
		case 5:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square4.gif";
		case 6:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square5.gif";
		case 7:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square6.gif";
		case 8:
			return "/TEMPLATE/ampTemplate/imagesSource/common/square7.gif";
		}
    	return "/TEMPLATE/ampTemplate/imagesSource/common/arrow_right.gif";
    }

    public static Collection<AmpActivity> checkActivitiesUsingProgram( Long programId ) {
    	ArrayList<AmpActivity> activities	= new ArrayList<AmpActivity>();
    	 Session sess = null;
         Collection col = null;
         try {
             ArrayList programs = (ArrayList) getRelatedThemes(programId);
             if (programs != null) {
                 Iterator<AmpTheme> iterProgram = programs.iterator();
                 while (iterProgram.hasNext()) {
                     AmpTheme program = iterProgram.next();
                     sess = PersistenceManager.getRequestDBSession();
                     AmpTheme themeToBeDeleted = (AmpTheme) sess.load(AmpTheme.class, program.getAmpThemeId());
                     if (themeToBeDeleted.getActivities() != null) {
                         activities.addAll(themeToBeDeleted.getActivities());
                     }
                     if (themeToBeDeleted.getActivityId() != null) {
                         activities.add(themeToBeDeleted.getActivityId());
                     }

                     String queryString = "select a from " + AmpActivityProgram.class.getName() + " a where (a.program=:program) ";
                     Query qry = sess.createQuery(queryString);
                     qry.setLong("program", programId);
                     Collection result = qry.list();
                     if (result != null) {
                         Iterator iterator = result.iterator();
                         while (iterator.hasNext()) {
                             AmpActivityProgram actProgram = (AmpActivityProgram) iterator.next();
                             if (actProgram != null && actProgram.getActivity() != null) {
                                 activities.add(actProgram.getActivity());
                             }
                         }
                     }
                 }
             }

             return activities;
         }
         catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
        	 return null;
		}
    	
    }
    public static String getNameOfProgramSettingsUsed(Long programId) { 
    	Collection programSettings					= getProgramSetttingsUsed(programId);
    	
    	Iterator iter	= programSettings.iterator();
    	String result	= "";
    	while ( iter.hasNext() ) {
    		AmpActivityProgramSettings aaps			= (AmpActivityProgramSettings) iter.next();
    		if ( aaps.getName() != null )
    			result	+= "'" + aaps.getName() + "'" + ", ";
    	}
    	if ( result.length() > 0 ) 
    		return result.substring(0, result.length()-2);
    	else
    		return null;
    }
    public static Collection getProgramSetttingsUsed(Long programId) {
    	Session sess 						= null;
        try {
        	sess = PersistenceManager.getRequestDBSession();
        	String qryString 		= "select a from " + AmpActivityProgramSettings.class.getName() + " a where (a.defaultHierarchy=:program) ";
        	Query qry 			= sess.createQuery(qryString);
            qry.setLong("program", programId);
            Collection result	= qry.list();
            return result;
        }
        catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
        	 return null;
		}
	}
    
    /**
     * 
     * @param userSelection collection of AmpTheme objects corresponding to the filters selected by the user
     * @param activityFilterCol this collection will contain the selected objects and their descendants (this collection will be used to filter the activities) 
     * @param columnDataCol this collection will contain the selected objects, their descendants and their ancestors (this collection will be used to filter out column data).
     * One needs the information about ancestors in multi-level hierarchy reports otherwise the report engine won't know to which higher level hierarchy an activity belongs to.
     *  
     * @throws DgException
     */
    public static void collectFilteringInformation(Collection<AmpTheme> userSelection, Collection<AmpTheme> activityFilterCol, 
    																					Set<AmpTheme> columnDataCol) throws DgException {
    	Iterator<AmpTheme> progIter	= userSelection.iterator();
		while ( progIter.hasNext()  ) {
			AmpTheme program		= progIter.next();
			Collection<AmpTheme> descendentPrograms	= ProgramUtil.getRelatedThemes( program.getAmpThemeId() );
			activityFilterCol.addAll( descendentPrograms );
			columnDataCol.addAll(  descendentPrograms );
			columnDataCol.addAll( ProgramUtil.getAncestorThemes(program) );
		}
    }
        /**
         * Hierarchy member factory.
         * Used to create XML enabled members.
         */
        public static class XMLtreeItemFactory implements HierarchyMemberFactory{
            public HierarchyMember createHierarchyMember(){
                TreeItem item=new TreeItem();
                item.setChildren(new ArrayList());
                return item;
            }
        }

        public static class ProgramHierarchyDefinition implements
                HierarchyDefinition {
            public Object getObjectIdentity(Object object) {
                AmpTheme i = (AmpTheme) object;
                return i.getAmpThemeId();

            }

            public Object getParentIdentity(Object object) {
                AmpTheme i = (AmpTheme) object;
                if (i.getParentThemeId() == null) {
                    return null;
                }
                else {
                    return i.getParentThemeId().getAmpThemeId();
                }
            }
        }

        public static class HierarchicalProgramComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                AmpTheme i1 = (AmpTheme) o1;
                AmpTheme i2 = (AmpTheme) o2;

                Long sk1 = i1.getAmpThemeId();
                Long sk2 = i2.getAmpThemeId();

                return sk1.compareTo(sk2);
            }

        }

        /**
         * Added by Govind
         *
         * @deprecated Use Category Manager functions instead
         */

        /*public static Collection getProgramTypes() {
    		Session session = null;
    		Collection col = null;
    		try {
    			session = PersistenceManager.getSession();
    			String qryStr = "select name from " + AmpProgramType.class.getName()
    					+ " name ";
    			Query qry = session.createQuery(qryStr);
    			col=qry.list();
    		} catch (Exception e) {
    			logger.error("Exception from getTheme()");
    			logger.error(e.getMessage());
    		} finally {
    			if (session != null) {
    				try {
    					PersistenceManager.releaseSession(session);
    				} catch (Exception rsf) {
    					logger.error("Release session failed");
    				}
    			}
    		}
    		return col;
    	}
*/
        /**
         * to save New Program Types
         * AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static void saveNewProgramType(AmpProgramType prg) {
        	DbUtil.add(prg);

    	}*/
        /**
         * update a Program
         * AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static void updateProgramType(AmpProgramType prg) {
        	DbUtil.update(prg);

    	}*/
        /**
         * to get the Program Type for Editing
         * AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static Collection getProgramTypeForEdititng(Long Id) {
    		Session session = null;
    		Collection col = null;
    		try {
    			session = PersistenceManager.getSession();
    			String qryStr = "select name from " + AmpProgramType.class.getName()
    					+ " name where name.ampProgramId=:Id ";

    			Query qry = session.createQuery(qryStr);
				qry.setParameter("Id",Id);
    			col=qry.list();
    		} catch (Exception e) {
    			logger.error("Exception from getTheme()");
    			logger.error(e.getMessage());
    		} finally {
    			if (session != null) {
    				try {
    					PersistenceManager.releaseSession(session);
    				} catch (Exception rsf) {
    					logger.error("Release session failed");
    				}
    			}
    		}
    		return col;
    	}*/
        /**
         * @deprecated AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static void deleteProgramType(AmpProgramType prg) {
        	try {
				DbUtil.delete(prg);
			} catch (JDBCException e) {
				logger.error(e);
			}

    	}*/
    	 /**
         * Used to sort indicators by name
         */
                public static class HelperAmpThemeNameComparator implements Comparator {
            public int compare(Object obj1, Object obj2) {
                AmpTheme theme1 = (AmpTheme) obj1;
                AmpTheme theme2 = (AmpTheme) obj2;
                return theme1.getName().compareTo(theme2.getName());
            }
        }

        public static class HelperAmpIndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AmpPrgIndicator indic1 = (AmpPrgIndicator) obj1;
                AmpPrgIndicator indic2 = (AmpPrgIndicator) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }

        }

        public static class HelperAllPrgIndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AllPrgIndicators indic1 = (AllPrgIndicators) obj1;
                AllPrgIndicators indic2 = (AllPrgIndicators) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }
        }

        public static class HelperAllMEIndicatorNameComparator implements Comparator {
           public int compare(Object obj1, Object obj2) {
               AllMEIndicators  indic1 = (AllMEIndicators) obj1;
               AllMEIndicators indic2 = (AllMEIndicators) obj2;
               return indic1.getName().compareTo(indic2.getName());
           }
        }
        public static class HelperAllIndicatorBeanNameComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getName().toLowerCase().compareTo(indic2.getName().toLowerCase());
            }
        }
        
        public static class HelperAllIndicatorBeanNameDescendingComparator implements Comparator<IndicatorsBean> {
	        public int compare(IndicatorsBean obj1, IndicatorsBean obj2) {
	            return -obj1.getName().toLowerCase().compareTo(obj2.getName().toLowerCase());
	        }
        }

        public static class HelperAllIndicatorBeanSectorComparator implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getSectorName().toLowerCase().compareTo(indic2.getSectorName().toLowerCase());
            }
        }

        public static class HelperAllIndicatorBeanSectorDescendingComparator implements Comparator<IndicatorsBean> {
	        public int compare(IndicatorsBean obj1, IndicatorsBean obj2) {
	            return -obj1.getSectorName().toLowerCase().compareTo(obj2.getSectorName().toLowerCase());
	        }
        }
        
        public static class HelperAllIndicatorBeanTypeComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getType().compareTo(indic2.getType());

            }
        }

        public static class ThemeIdComparator implements Comparator<AmpTheme>{

			public int compare(AmpTheme thm1, AmpTheme thm2) {
				return thm1.getAmpThemeId().compareTo(thm2.getAmpThemeId());
			}

        }

 }
