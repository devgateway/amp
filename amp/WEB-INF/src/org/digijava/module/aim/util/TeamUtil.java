/*
 * TeamUtil.java Created: 07-Apr-2005
 */

package org.digijava.module.aim.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DonorTeam;
import org.digijava.module.aim.helper.ReportsCollection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.contentrepository.dbentity.CrSharedDoc;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Persister class for all Team/Workspaces related Objects
 *
 * @author priyajith
 */
public class TeamUtil {

    private static Logger logger = Logger.getLogger(TeamUtil.class);

    /**
     * The function will get all the teams/workspaces which is unassociated with
     * a parent team. It will also filter the teams based on the 'workspace
     * type' and 'team category'
     *
     * @param workspaceType
     * @param teamCategory
     * @return The collection of all teams
     */

    public static Collection getUnassignedWorkspaces(String workspaceType, String team) {

        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getSession();

            // get all teams whose 'parent id' is set to null
            String qryStr = "select t from "
                + AmpTeam.class.getName()
                + " t"
                //+ " where t.parentTeamId is null and (t.teamCategory=:team) ";
                + " where t.parentTeamId is null ";

            boolean wTypeFlag = false;
           // boolean tCatFlag = false;

            // if user has specified a workspaceType filter
            if(workspaceType != null && workspaceType.trim().length() != 0) {
                qryStr += " and (t.accessType=:wType)";
                wTypeFlag = true;
            }
            // if user has specified team category filter
//            if(teamCategoryId != null && teamCategoryId.longValue() != 0) {
//                qryStr += " and (t.type=:typeId)";
//                tCatFlag = true;
//            }

            Query qry = session.createQuery(qryStr);
          //  qry.setParameter("team", team, Hibernate.STRING);
            if(wTypeFlag) {
                qry.setParameter("wType", workspaceType, Hibernate.STRING);
            }
//            if(tCatFlag) {
//                qry.setParameter("typeId", teamCategoryId, Hibernate.LONG);
//            }

            col = qry.list();

        } catch(Exception e) {
            logger.error("Exception from getUnassignedWorkspcaes : "
                         + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release Session failed");
                }
            }
        }
        return col;
    }

    public static Set<AmpTeam> getRelatedTeamsForTeams(Collection<AmpTeam> teams)
    {
    	Set<AmpTeam> result = new HashSet<AmpTeam>();
    	for(AmpTeam ampTeam:teams)
    	{
    		result.add(ampTeam);
    		result.addAll(TeamUtil.getAmpLevel0Teams(ampTeam.getAmpTeamId()));
    	}
    	return result;
    }
    
    public static Set getRelatedTeamsForMember(TeamMember tm) 
    {
    	AmpTeam ampTeam = TeamUtil.getAmpTeam(tm.getTeamId());	    
		return getRelatedTeamsForTeams(Arrays.asList(new AmpTeam[]{ampTeam}));
    }

    
	public static Set getComputedOrgs(Collection relatedTeams) {
		Set teamAssignedOrgs = new TreeSet();
		try {
			Session session = PersistenceManager.getRequestDBSession();
			Iterator i = relatedTeams.iterator();
			while (i.hasNext()) {
				AmpTeam team = (AmpTeam) i.next();
				AmpTeam loadedTeam=(AmpTeam) session.load(AmpTeam.class,team.getAmpTeamId());
				// if("Computed".equals(team.getAccessType())) {
				if (loadedTeam.getComputation() != null
						&& loadedTeam.getComputation() == true) {
					teamAssignedOrgs.addAll(loadedTeam.getOrganizations());
				}
			}
		} catch (DgException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return teamAssignedOrgs;
	}
    
    public static Collection getAllTeams(Long teamId[]) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            if(teamId != null && teamId.length > 0) {
                session = PersistenceManager.getSession();
                StringBuffer qryBuf = new StringBuffer();
                qryBuf.append("select t from ");
                qryBuf.append(AmpTeam.class.getName());
                qryBuf.append(" t where t.ampTeamId in (");
                for(int i = 0; i < teamId.length; i++) {
                    if(teamId[i] != null) {
                        qryBuf.append(teamId[i]);
                        if((i + 1) != teamId.length) {
                            qryBuf.append(",");
                        }
                    }
                }
                qryBuf.append(")");
                Query qry = session.createQuery(qryBuf.toString());
                col = qry.list();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        return col;
    }

    public static Collection<AmpTeam> getAllRelatedTeams() {
        Session session = null;
        Collection<AmpTeam> col = new ArrayList<AmpTeam>();

        try {
            session = PersistenceManager.getSession();
            String query = "select team from " + AmpTeam.class.getName()
                + " team where (team.accessType=:accessType)";
            Query qry = session.createQuery(query);
            qry.setParameter("accessType", "Team");
            col = qry.list();

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        return col;
    }

    public static Collection getAllChildrenWorkspaces(Long id) {
        Session session = null;
        Collection col = new ArrayList();
        try {
            session = PersistenceManager.getSession();
            String query = "select team from " + AmpTeam.class.getName()
                + " team where (team.parentTeamId.ampTeamId=:pid) order by name";
            Query qry = session.createQuery(query);
            qry.setParameter("pid", id);
            col = qry.list();

        } catch(Exception e) {
            throw new RuntimeException(e);

        }
       return col;
    }
    
    public static Collection getAllRelatedTeamsByType(String type) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            String query = "select team from "
                + AmpTeam.class.getName()
                + " team where (team.accessType=:accessType) and (team.type=:type)";
            Query qry = session.createQuery(query);
            qry.setParameter("accessType", "Team");
            qry.setParameter("type", type);
            col = qry.list();

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        return col;
    }

    public static Set getAllRelatedTeamsByAccessType(String accessType) {
        Session session = null;
        Collection col = new ArrayList();
        Set colSet= new TreeSet();
        try {
            session = PersistenceManager.getSession();
            String query = "select team from "
                + AmpTeam.class.getName()
                + " team where (team.accessType=:accessType) ";
            Query qry = session.createQuery(query);
            qry.setParameter("accessType", accessType);
            col = qry.list();

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        colSet.addAll(col);
        //return col;
        return colSet;
    }
    
    
    /**
     * Creates a new team
     *
     * @param team
     *            The team object which is to be persisted
     * @param childTeams
     *            The collection of child teams which is to be associated with
     *            the parent team
     * @return false if the team is successfully created else returns true
     */
    public static boolean createTeam(AmpTeam team, Collection<AmpTeam> childTeams) {
    	boolean teamExist = false;
    	Session session = null;

    	try {
    		session = PersistenceManager.getSession();
//beginTransaction();
            //check whether a team with the same name already exists
    		String teamNameHql = AmpTeam.hqlStringForName("t");
    		String qryStr = "select t from " + AmpTeam.class.getName() + " t "
    				+ "where (" + teamNameHql + "=:name)";
    		Query qry = session.createQuery(qryStr);
    		qry.setString("name", team.getName());
    		if(qry.list().size() > 0) {
                // throw new AimException("Cannot create team: The team name " +
                // team.getName() + " already exist");
    			teamExist = true;
    			return teamExist;
    		} 
            
    		// save the new team
    		session.save(team);

    		qryStr = "select fiscal from "
    				+ AmpFiscalCalendar.class.getName() + " fiscal "
    				+ "where (fiscal.name=:cal) or (fiscal.yearOffset=:yearOffset)";
    		qry = session.createQuery(qryStr);
    		qry.setString("cal", "Gregorian Calendar");
    		qry.setInteger("yearOffset", 0);
    		List<AmpFiscalCalendar> calendars = qry.list();
    		AmpFiscalCalendar fiscal = null;
    		if(calendars.size() > 0) {
    			for(Iterator<AmpFiscalCalendar> itr = calendars.iterator(); itr.hasNext(); ) {
    				fiscal = (AmpFiscalCalendar) itr.next();
    				logger.debug("[createTeam(-)] fiscal calendar - " + fiscal.getName());
    				if(fiscal != null)
    					break;
    			}
    		} 
    		else
    			logger.error("[createTeam(-)] fiscal calendar collection is empty");
                
    		qryStr = "select curr from " + AmpCurrency.class.getName()
    				+ " curr " + "where (curr.currencyCode=:code)";
    		qry = session.createQuery(qryStr);
    		qry.setString("code", "USD");
    		List<AmpCurrency> currencies = qry.list();
    		AmpCurrency curr = null;
    		if(currencies.size() > 0)
    			curr = currencies.get(0);


    		// create application settings for the team and save
    		AmpApplicationSettings ampAppSettings = new AmpApplicationSettings();
    		ampAppSettings.setTeam(team);
    		ampAppSettings.setMember(null);
    		ampAppSettings.setDefaultRecordsPerPage(new Integer(10));
    		ampAppSettings.setCurrency(curr);
    		ampAppSettings.setFiscalCalendar(fiscal);
    		ampAppSettings.setLanguage("English");
    		ampAppSettings.setValidation("validationOff");
    		session.save(ampAppSettings);

    		// update all child workspaces parent team
    		if(childTeams != null && childTeams.size() > 0) {    			
    			for(AmpTeam childTeam:childTeams)
    			{
    				childTeam.setParentTeamId(team);
    				session.update(childTeam);
    			}
    		}
                
                //link shared resources with team
    		List<Object[]> sharedDocs = null;
    		String qstr="select c.nodeUUID,c.sharedNodeVersionUUID from " +CrSharedDoc.class.getName()  +" c where " +
    				" c.state="+CrConstants.SHARED_AMONG_WORKSPACES +  " group by c.nodeUUID,c.sharedNodeVersionUUID";
    		qry = session.createQuery(qstr);
    		sharedDocs = qry.list();
    		if(sharedDocs!=null && sharedDocs.size()>0){
    			for (Object[] row : sharedDocs) {
    				String nodeUUID = (String) row[0];
    				String versionUUID = (String) row[1];
    				CrSharedDoc sharedDoc = new CrSharedDoc(nodeUUID, team, CrConstants.SHARED_AMONG_WORKSPACES);
    				sharedDoc.setSharedNodeVersionUUID(versionUUID);
    				session.save(sharedDoc);
    				}
    		}
                // commit the changes
                //tx.commit();
        } catch(Exception e) {
            /*
             * teamExist = true; logger.error(ae.getMessage());
             */
            throw new RuntimeException(e);
        }
        return teamExist;
    }

    /**
     * Retreives the team/workspace information
     *
     * @param id
     *            The teamId
     * @return The Workspace object
     */
    public static Workspace getWorkspace(Long id) {
        Workspace workspace = null;
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            String qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + "where (t.ampTeamId=:id)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                AmpTeam team = (AmpTeam) itr.next();
                if(team.getOrganizations()!=null){
                	 team.getOrganizations().size(); //lazy init;
                }               
                workspace = new Workspace();
                workspace.setDescription(team.getDescription().trim());
                workspace.setId(team.getAmpTeamId().toString());
                workspace.setName(team.getName());
                workspace.setChildOrgs(team.getOrganizations());
                workspace.setTeamCategory(team.getTeamCategory());
                workspace.setType(team.getType());
                workspace.setWorkspaceType(team.getAccessType());
                workspace.setAddActivity(team.getAddActivity());
                workspace.setComputation(team.getComputation());
                workspace.setUseFilter(team.getUseFilter());
                workspace.setWorkspaceGroup(team.getWorkspaceGroup());
                workspace.setHideDraftActivities(team.getHideDraftActivities() );
                workspace.setFmTemplate(team.getFmTemplate());
                workspace.setWorkspacePrefix(team.getWorkspacePrefix());
                
                if(null == team.getRelatedTeamId())
                    workspace.setRelatedTeam(null);
                else
                    workspace.setRelatedTeam(team.getRelatedTeamId()
                                             .getAmpTeamId());
                qryStr = "select count(*) from "
                    + AmpTeamMember.class.getName() + " t "
                    + "where (t.ampTeam=:teamId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
                Iterator itr1 = qry.list().iterator();
                int numMem = 0;
                if(itr1.hasNext()) {
                    Integer num = (Integer) itr1.next();
                    numMem = num.intValue();
                }
                if(numMem == 0) {
                    workspace.setHasMembers(false);
                } else {
                    workspace.setHasMembers(true);
                }
                qryStr = "select count(*) from " + AmpActivity.class.getName()
                    + " act " + "where (act.team=:teamId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
                itr1 = qry.list().iterator();
                int numAct = 0;
                if(itr1.hasNext()) {
                    Integer num = (Integer) itr1.next();
                    numAct = num.intValue();
                }
                if(numAct == 0) {
                    workspace.setHasActivities(false);
                } else {
                    workspace.setHasActivities(true);
                }
                qryStr = "select t from " + AmpTeam.class.getName() + " t "
                    + "where (t.parentTeamId.ampTeamId=:teamId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
                itr1 = qry.list().iterator();
                Collection childWorkspaces = new ArrayList();
                while(itr1.hasNext()) {
                    AmpTeam childTeam = (AmpTeam) itr1.next();
                    childWorkspaces.add(childTeam);
                }
                workspace.setChildWorkspaces(childWorkspaces);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }

        return workspace;
    }

    /**
     * Updates a team/workspace
     *
     * @param team
     * @param childTeams
     * @return
     */
    public static boolean updateTeam(AmpTeam team, Collection childTeams) {
        boolean teamExist = false;
        Session session = null;
        Transaction tx = null;

        try {

            session = PersistenceManager.getSession();
//beginTransaction();
            String teamNameHql = AmpTeam.hqlStringForName("t");
            // check whether a team with the same name already exist
            String qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + String.format("where (%s=:name)", teamNameHql);
            Query qry = session.createQuery(qryStr);
            qry.setParameter("name", team.getName(), Hibernate.STRING);
            Iterator tempItr = qry.list().iterator();
            if(tempItr.hasNext()) {
                AmpTeam tempTeam = (AmpTeam) tempItr.next();
                if(!(tempTeam.getAmpTeamId().equals(team.getAmpTeamId()))) {
                    // throw new AimException("Cannot create team: The team name
                    // " + team.getName() + " already exist");
                    teamExist = true;
                    return teamExist;
                }
            }

            logger.debug("Updating team....");

            qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + "where (t.ampTeamId=:id)";
            qry = session.createQuery(qryStr);
            qry.setParameter("id", team.getAmpTeamId(), Hibernate.LONG);
            logger.debug("Getting the team with id " + team.getAmpTeamId());
            tempItr = qry.list().iterator();
            if(tempItr.hasNext()) {
                logger.debug("Before update....");
                AmpTeam updTeam = (AmpTeam) tempItr.next();
                updTeam.setName(team.getName());
                updTeam.setDescription(team.getDescription());
                updTeam.setTeamCategory(team.getTeamCategory());
                updTeam.setAccessType(team.getAccessType());
                updTeam.setComputation(team.getComputation());
                updTeam.setType(team.getType());
                updTeam.setRelatedTeamId(team.getRelatedTeamId());
                updTeam.setOrganizations(team.getOrganizations());
                updTeam.setAddActivity(team.getAddActivity());
                updTeam.setComputation(team.getComputation());
                updTeam.setUseFilter(team.getUseFilter());
                updTeam.setHideDraftActivities(team.getHideDraftActivities() );
                updTeam.setWorkspaceGroup(team.getWorkspaceGroup());
                updTeam.setFmTemplate(team.getFmTemplate());
                updTeam.setWorkspacePrefix(team.getWorkspacePrefix());
                if (updTeam.getFilterDataSet() == null)
                	updTeam.setFilterDataSet(new HashSet());
                else
                	updTeam.getFilterDataSet().clear();
                if (team.getFilterDataSet() != null){
                	updTeam.getFilterDataSet().addAll(team.getFilterDataSet());
                }
                session.saveOrUpdate(updTeam);

                qryStr = "select t from " + AmpTeam.class.getName() + " t "
                    + "where (t.parentTeamId.ampTeamId=:parId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("parId", updTeam.getAmpTeamId(),
                                 Hibernate.LONG);

                Iterator itr = qry.list().iterator();
                while(itr.hasNext()) {
                    AmpTeam child = (AmpTeam) itr.next();
                    child.setParentTeamId(null);
                    session.saveOrUpdate(child);
                }

                logger.debug("Team updated");

                if(childTeams != null && childTeams.size() > 0) {
                    itr = childTeams.iterator();
                    logger.info("Size " + childTeams.size());
                    while(itr.hasNext()) {
                        AmpTeam childTeam = (AmpTeam) itr.next();
                        AmpTeam upChildTeam = (AmpTeam) session.load(
                            AmpTeam.class, childTeam.getAmpTeamId());
                        upChildTeam.setParentTeamId(updTeam);
                        session.saveOrUpdate(upChildTeam);
                    }
                }
                // commit the changes
                //tx.commit();
            }

        } catch(Exception e) {

            /*
             * teamExist = true; logger.error("Execption from updateTeam() :" +
             * ae.getMessage()); ae.printStackTrace(System.out);
             */
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);

        }
        /*
        * catch (Exception e) { logger.error("Execption from updateTeam() :" +
        * e.getMessage()); e.printStackTrace(System.out); if (tx != null) {
        * try { tx.rollback(); } catch (Exception rbf) {
        * logger.error("Rollback failed"); } } }
        finally
        */ {
           if(session != null) {
               try {
                   PersistenceManager.releaseSession(session);
               } catch(Exception rsf) {
                   logger.error("Release session failed");
               }
           }
       }
        return teamExist;
    }

    public static boolean membersExist(Long teamId) {
        boolean memExist = false;
        Session session = null;
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select count(*) from " + AmpTeamMember.class.getName()
                + " tm" + " where (tm.ampTeam=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setParameter("teamId", teamId, Hibernate.LONG);

            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                Integer cnt = (Integer) itr.next();
                logger.info("cnt.intValue = " + cnt.intValue());
                if(cnt.intValue() > 0)
                    memExist = true;
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return memExist;
    }

    public static boolean teamHasActivities(Long teamId) {
        boolean memExist = false;
        Session session = null;
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select count(*) from " + AmpActivity.class.getName()
                + " tm" + " where (tm.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setParameter("teamId", teamId, Hibernate.LONG);

            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                Integer cnt = (Integer) itr.next();
                logger.info("cnt.intValue = " + cnt.intValue());
                if(cnt.intValue() > 0)
                    memExist = true;
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return memExist;
    }
    
	/**
	 * 
	 * @param teamId
	 */
	public static void unlinkParentWorkspace(Long teamId) {
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			
//beginTransaction();
			AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);
			team.setParentTeamId(null);
			session.saveOrUpdate(team);
			//transaction.commit();
			PersistenceManager.releaseSession(session);
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (DgException e) {
			logger.error(e);
			e.printStackTrace();
		}

	}
        
    
    /**
     * Removes a team
     *
     * @param teamId
     *            The team id of the team to be removed
     */
    public static void removeTeam(Long teamId) {
        Session session = null;
        Transaction tx = null;
        String qryStr = null;
        Query qry = null;

        try {
        	
        	RepairDbUtil.repairDb();
        	
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            // Remove reference from activity
            qryStr = "select act from " + AmpActivityVersion.class.getName() + " act"
                + " where (act.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            Iterator itr = qry.list().iterator();
            while(itr.hasNext()) {
            	AmpActivityVersion act = (AmpActivityVersion) itr.next();
                act.setTeam(null);
                session.update(act);
            }

            // Remove reference from AmpTeamPageFilters
            qryStr = "select tpf from " + AmpTeamPageFilters.class.getName()
                + " tpf" + " where (tpf.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpTeamPageFilters tpf = (AmpTeamPageFilters) itr.next();
                session.delete(tpf);
            }
            
            try{
	            // Remove reference from AmpTeamReports
	            qryStr = "select tr from " + AmpTeamReports.class.getName() + " tr"
	                + " where (tr.team=:teamId)";
	            qry = session.createQuery(qryStr);
	            qry.setLong("teamId", teamId);
	            itr = qry.list().iterator();
	            while(itr.hasNext()) {
	                AmpTeamReports tr = (AmpTeamReports) itr.next();
	                session.delete(tr);
	            }
            }catch(Exception ex){
            	ex.printStackTrace();
            }
            // Remove reference from AmpTeam
            qryStr = "select t from " + AmpTeam.class.getName() + " t"
                + " where (t.parentTeamId.ampTeamId=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpTeam t = (AmpTeam) itr.next();
                t.setParentTeamId(null);
                session.update(t);
            }
            
            // Remove reference from RelatedTeam
            qryStr = "select t from " + AmpTeam.class.getName() + " t"
                + " where (t.relatedTeamId=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpTeam t = (AmpTeam) itr.next();
                t.setRelatedTeamId(null);
                session.update(t);
            }

            // Remove reference from AmpApplicationSettings
            qryStr = "select a from " + AmpApplicationSettings.class.getName()
                + " a " + "where (a.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            if(itr.hasNext()) {
                AmpApplicationSettings as = (AmpApplicationSettings) itr.next();
                session.delete(as);
            }
            //remove references for shared docs
            qryStr = "delete from " + CrSharedDoc.class.getName() +" c where c.team="+teamId;
            qry = session.createQuery(qryStr);
            qry.executeUpdate();
            
            session.delete(team);
            
            
            
            //tx.commit();
        } catch(ObjectNotFoundException objectNotFoundEx) {
            logger.error("Execption from removeTeam() :" + objectNotFoundEx.getMessage());
            return;
        } catch(Exception ex) {
            logger.error("Execption from removeTeam() :" + ex.getMessage());
            ex.printStackTrace();
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(ex);
        }
    }

    /**
     * Return an AmpTeam object corresponding to the id
     *
     * @param id
     *            The id of the team whose object is to be returned.
     * @return The AmpTeam object
     */
    public static AmpTeam getAmpTeam(Long id) {

        if (id == null)
        	return null;
        Session session = null;
        AmpTeam team = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            team = (AmpTeam) session.load(AmpTeam.class, id);
        } catch(Exception e) {
            throw new RuntimeException(e);

        } 
        return team;
    }

    /**
     * Return an AmpTeamMember object corresponding to the id
     *
     * @param id
     *            The id of the team member whose object is to be returned.
     * @return The AmpTeamMember object
     */
    public static AmpTeamMember getAmpTeamMember(Long id) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getRequestDBSession();
//session.flush();
            member = (AmpTeamMember) session.load(AmpTeamMember.class, id);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return member;
    }

    public static void testDan(AmpTeamMember member, AmpApplicationSettings appSettings) {
			Session session = null;
			Transaction tx = null;
			
			try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();
			session.saveOrUpdate(member);
			session.saveOrUpdate(appSettings);
			
			}catch(Exception e) {
	            logger.error("Exception from addTeamMember :" + e);
	            e.printStackTrace();
	            if(tx != null) {
	                try {
	                    tx.rollback();
	                } catch(Exception rbf) {
	                    logger.error("Rollback failed :" + rbf);
	                }
	            }
			}
    }
    public static void addTeamMember(AmpTeamMember member,
                                     AmpApplicationSettings appSettings, Site site) {
        Session session = null;
        Transaction tx = null;
       
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(member);
            session.saveOrUpdate(appSettings);
            // this is where newly-created AmpApplicationSettings settings are saved in the database
            
            if(member.getAmpMemberRole().getTeamHead()!=null&&member.getAmpMemberRole().getTeamHead()) {
                AmpTeam team = (AmpTeam) session.load(AmpTeam.class, (Serializable) member.getAmpTeam().getIdentifier());
                team.setTeamLead(member);
                session.saveOrUpdate(team);
            }
            User user = (User) session.load(User.class, member.getUser().getId());
            String qryStr = "select grp from " + Group.class.getName()
                + " grp " + "where (grp.key=:key) and (grp.site=:sid)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("key", Group.EDITORS, Hibernate.STRING);
            qry.setParameter("sid", site.getId(), Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            Group group = null;
            if(itr.hasNext())
                group = (Group) itr.next();
            user.getGroups().add(group);
            //tx.commit();
            logger.debug("User added to group " + group.getName());
        } catch(Exception e) {
            logger.error("Exception from addTeamMember :" + e);
            e.printStackTrace();
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed :" + rbf);
                }
            }
            throw new RuntimeException(e);

        } 
    }

    public static boolean isMemberExisting(Long teamId, String email) {
        Session session = null;
        Query qry = null;
        String qryStr = null;
        boolean memberExist = true;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr ="select tm  from "
                    + AmpTeamMember.class.getName() 
                    + " tm inner join tm.ampTeam t "+
                    " inner join tm.user u where u.email=:email and t.ampTeamId=:teamId";
         
            qry = session.createQuery(qryStr);
            qry.setString("email", email);
            qry.setLong("teamId", teamId);
            if(qry.list()==null||qry.list().size()==0){
               memberExist=false;
            }
           
            
        } catch(Exception e) {
            throw new RuntimeException(e);

        
        }

        return memberExist;
    }

    public static void removeActivitiesFromDonorTeam(Long activities[],
        Long teamId) {
        Session session = null;
        Transaction tx = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            for(int i = 0; i < activities.length; i++) {
                AmpActivity activity = (AmpActivity) session.load(
                    AmpActivity.class, activities[i]);

                team.getActivityList().remove(activity);
                Iterator membersItr = activity.getMember().iterator();
                while(membersItr.hasNext()) {
                    member = (AmpTeamMember) membersItr.next();
                    if(member.getAmpTeam().getAmpTeamId().equals(teamId)) {
                        member.getActivities().remove(activity);
                        session.update(member);
                    }
                }
                session.update(team);
//session.flush();
            }

            //tx.commit();
        } catch(Exception e) {

            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static void removeActivitiesFromTeam(Long activities[],Long teamId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();

           	String qs = "update "+ AmpActivityVersion.class.getName()+" set amp_team_id = null where amp_activity_id in (:activity)";
        	Query query = session.createQuery(qs);
            query.setParameterList("activity", activities);
            query.executeUpdate();
            query = session.createSQLQuery("delete from amp_member_activities where amp_activity_id in (:activity)");
            query.setParameterList("activity", activities);
            query.executeUpdate();
            tx.commit();
        } catch(Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }

    }

    public static Collection getAllDonorsToDesktop(Long teamId) {
        Session session = null;
        Query qry = null;
        Collection donors = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            Vector teams = new Vector();
            Vector temp = new Vector();

            teams.add(teamId);
            temp.add(teamId);
            String qryStr = "";
            Long tId = new Long(0);
            String inclause = "";

            ArrayList dbReturnSet = (ArrayList) getAmpLevel0TeamIds(teamId);
            if(dbReturnSet.size() == 0)
                inclause = "'" + teamId + "'";
            else {
                Iterator iter = dbReturnSet.iterator();
                while(iter.hasNext()) {
                    tId = (Long) iter.next();
                    if(inclause == null || inclause.trim().length() == 0) {
                        inclause = "'" + tId + "'";
                    } else {
                        inclause = inclause + ",'" + tId + "'";
                    }
                }
            }

            qryStr = "select act.ampActivityId from "
                + AmpActivity.class.getName() + ""
                + " act where act.team in (" + inclause.toString() + ")";

            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            inclause = "";
            while(itr.hasNext()) {
                if(inclause.length() != 0)
                    inclause += ",";
                inclause += (Long) itr.next();
            }

            if(!"".equals(inclause)) {
                qryStr = "select distinct aor.organisation from "
                    + AmpOrgRole.class.getName() + " aor, "
                    + AmpOrganisation.class.getName() + " " + "org, "
                    + AmpRole.class.getName() + " role where "
                    + "aor.activity in (" + inclause + ") and "
                    + "aor.role = role.ampRoleId and "
                    + "aor.organisation = org.ampOrgId and "
                    + "role.roleCode = '" + Constants.FUNDING_AGENCY + "' and "
                    + "(org.deleted is null or org.deleted = false) order by org.acronym asc";

                qry = session.createQuery(qryStr);
                itr = qry.list().iterator();
                while(itr.hasNext()) {
                    AmpOrganisation ampOrg = (AmpOrganisation) itr.next();
                    if(ampOrg.getAcronym().length() > 20)
                        ampOrg.setAcronym(ampOrg.getAcronym().substring(0, 20)
                                          + "...");
                    donors.add(ampOrg);
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }

        return donors;
    }

    public static void updateTeamPageConfiguration(Long teamId, Long pageId,
        Long filters[]) {
        Session session = null;
        Transaction tx = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class, teamId);
            AmpPages ampPage = (AmpPages) session.load(AmpPages.class, pageId);
            String qryStr = "select tpf from "
                + AmpTeamPageFilters.class.getName() + " tpf "
                + "where (tpf.team=:tId) and (tpf.page=:pId)";
            qry = session.createQuery(qryStr);
            qry.setParameter("tId", teamId, Hibernate.LONG);
            qry.setParameter("pId", pageId, Hibernate.LONG);
            Iterator tpfItr = qry.list().iterator();
            while(tpfItr.hasNext()) {
                AmpTeamPageFilters ampTpf = (AmpTeamPageFilters) tpfItr.next();
                session.delete(ampTpf);
            }

            for(int i = 0; i < filters.length; i++) {
                AmpFilters ampFilter = (AmpFilters) session.load(
                    AmpFilters.class, filters[i]);
                AmpTeamPageFilters teamPageFilters = new AmpTeamPageFilters();
                teamPageFilters.setFilter(ampFilter);
                teamPageFilters.setPage(ampPage);
                teamPageFilters.setTeam(ampTeam);
                session.save(teamPageFilters);
            }
            //tx.commit();
        } catch(Exception e) {
            logger.error("Unable to update team page filters" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static Collection getDonorTeams(Long teamId) {
        // Check whether the team whose donor teams need to be found is a
        // MOFED team

        Collection col = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getSession();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);
            if(team.getTeamCategory().equalsIgnoreCase("MOFED")) {
                String qryStr = "select t from " + AmpTeam.class.getName()
                    + " t " + "where (t.relatedTeamId=:tId)";
                Query qry = session.createQuery(qryStr);
                qry.setParameter("tId", teamId, Hibernate.LONG);
                Iterator itr = qry.list().iterator();
                while(itr.hasNext()) {
                    AmpTeam ampTeam = (AmpTeam) itr.next();
                    DonorTeam dt = new DonorTeam();
                    dt.setTeamId(ampTeam.getAmpTeamId());
                    if(ampTeam.getTeamLead() != null) {
                        dt.setTeamMeberId(ampTeam.getTeamLead()
                                          .getAmpTeamMemId());
                        dt.setTeamMemberName(ampTeam.getTeamLead().getUser()
                                             .getEmail());
                    }
                    dt.setTeamName(ampTeam.getName());
                    col.add(dt);
                }
            }

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return col;
    }

    public static Collection getDonorTeamActivities(Long teamId) {

        Collection col = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);
            return team.getActivityList();
          

        } catch(Exception e) {
            logger.error("Unable to getDonorTeamActivities" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Collection getDonorUnassignedActivities(Long dnrTeamId,
        Long teamId) {

        Collection col = new ArrayList();
        Session session = null;

        try {

            Collection temp = getDonorTeamActivities(dnrTeamId);

            session = PersistenceManager.getSession();
            String qryStr = "select act from " + AmpActivity.class.getName()
                + " act where "
                + "(act.team=:teamId) and (act.approvalStatus=:status)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            qry.setParameter("status", Constants.APPROVED_STATUS,
                             Hibernate.STRING);
            col = qry.list();
            
             

        } catch(Exception e) {
            logger.error("Unable to getDonorTeamActivities" + e.getMessage());
            throw new RuntimeException(e);
        } 
        return col;
    }

    public static void assignActivitiesToDonor(Long dnrTeamId,
                                               Long activityId[]) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, dnrTeamId);
            if(ampTeam.getActivityList() == null) {
                ampTeam.setActivityList(new HashSet());
            }
            logger.info("ActivityId length = " + activityId.length);
            for(int i = 0; i < activityId.length; i++) {
                logger.info("Id = " + activityId[i]);
                if(activityId[i] != null) {
                    AmpActivity ampActivity = (AmpActivity) session.get(
                        AmpActivity.class, activityId[i]);
                    ampTeam.getActivityList().add(ampActivity);
                }
            }

            session.update(ampTeam);
            //tx.commit();

        } catch(Exception e) {
            logger.error("Unable to assignActivitiesToDonor" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static void removeActivitiesFromDonor(Long dnrTeamId, Long activityId[]) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, dnrTeamId);
            Set newList = new HashSet();

            Iterator itr = ampTeam.getActivityList().iterator();
            while(itr.hasNext()) {
                AmpActivity act = (AmpActivity) itr.next();
                boolean present = false;
                for(int i = 0; i < activityId.length; i++) {
                    if(act.getAmpActivityId().longValue() == activityId[i]
                       .longValue()) {
                        present = true;
                        break;
                    }
                }
                if(!present) {
                    newList.add(act);
                }
            }

            ampTeam.setActivityList(newList);

            session.update(ampTeam);
            //tx.commit();

        } catch(Exception e) {
            logger.error("Unable to assignActivitiesToDonor" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static boolean checkForParentTeam(Long ampTeamId) {
        Session session = null;
        Query q = null;
        boolean ans = false;
        try {
            session = PersistenceManager.getSession();
            String qry = "select tm from " + AmpTeam.class.getName()
                + " tm where tm.parentTeamId.ampTeamId=:ampTeamId";
            q = session.createQuery(qry);
            q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
            if(q != null && q.list().size() > 0)
                ans = false;
            else
                ans = true;
        } catch(Exception ex) {
            logger.error("Unable to get AmpTeam [checkForParentTeam()]", ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
            }
        }
        logger.debug("Getting checkForParentTeam Executed successfully ");
        return ans;
    }

    public static AmpTeam getTeamByName(String teamName) {
        Session session = null;
        Query qry = null;
        AmpTeam team = null;

        try {
            session = PersistenceManager.getSession();
            String teamNameHql = AmpTeam.hqlStringForName("t");
            String queryString = "select t from " + AmpTeam.class.getName()
                + String.format(" t where (%s=:teamName)", teamNameHql);
            qry = session.createQuery(queryString);
            qry.setParameter("teamName", teamName, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                team = (AmpTeam) itr.next();
            }
        } catch(Exception e) {
            logger.error("Unable to get team");
            logger.debug("Exceptiion " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return team;
    }
 
    /**
     * gets list of documents attached any of the AmpActivityId's in the given list 
     * @return
     */
    public static Map<Long, List<AmpActivityDocument>> getDocumentsByActivityIds(Collection<Long> activityIds)
    {
    	try
    	{
    		Map<Long, List<AmpActivityDocument>> result = new java.util.TreeMap<Long, List<AmpActivityDocument>>();
    		
    		if(activityIds.size()==0) return result;
    		
    		String queryString = "select doc, doc.ampActivity.ampActivityId FROM " + AmpActivityDocument.class.getName() + " doc WHERE doc.ampActivity.ampActivityId IN (" + getCommaSeparatedList(activityIds) + ")";
    		
            Session session = PersistenceManager.getRequestDBSession();
            Query qry = session.createQuery(queryString.toString());
            for(Object[] rs:(List<Object[]>) qry.list())
            {
            	Long actId = (Long) rs[1];
            	if (result.get(actId) == null)
            		result.put(actId, new ArrayList<AmpActivityDocument>());
            	result.get(actId).add((AmpActivityDocument) rs[0]);
            }
            return result;
    	}
    	catch(DgException e)
    	{
    		throw new RuntimeException(e);
    	}
    }
    
    /**
     * to avoid loading huge datasets from the DB, because AmpActivityVersions has huge rows
     * DO NOT ADD collections as names here, as they (mostly) don't work but lead to Hibernate crashing
     * @param teamId
     * @param includedraft
     * @param keyword
     * @param fieldsList
     * @return
     */
    public static Map<Long, Object[]> getAllTeamAmpActivitiesResume(Long teamId, boolean includedraft, String keyword, String...fieldsList) {
    	Session session = null;
    	
        StringBuilder fieldsQuery = new StringBuilder();
        for(String field:fieldsList)
        {
        	if (fieldsQuery.length() > 0)
        		fieldsQuery.append(", ");
        	fieldsQuery.append("g.ampActivityLastVersion." + field);
        }
        
        List<Object[]> col = new ArrayList<Object[]>();

        try {
            session = PersistenceManager.getRequestDBSession();
            StringBuilder queryString = new StringBuilder("select " + fieldsQuery.toString() + " from "+ AmpActivityGroup.class.getName()+" g where");
            Query qry = null;
            queryString.append(" (g.ampActivityLastVersion.deleted is null or g.ampActivityLastVersion.deleted=false) and ") ;
            if(teamId == null) {
            	queryString.append(" g.ampActivityLastVersion.team is null") ;
            }else{
            	queryString.append(" (g.ampActivityLastVersion.team=:teamId)") ;
            }
            if(!includedraft){
            	queryString.append("  and   (g.ampActivityLastVersion.draft is null or g.ampActivityLastVersion.draft=false)) ");
            }
            if(keyword!=null && keyword.length()>0){
            	queryString.append(" and lower(g.ampActivityLastVersion.name) like lower(:name)") ;
            }
            
            qry = session.createQuery(queryString.toString());
          	if(keyword!=null && keyword.length()>0){
              	qry.setString("name", "%" + keyword + "%");
            }
          	if(teamId!=null){
          		qry.setLong("teamId", teamId);
          	}          
   
            col  = qry.list();
            
            Map<Long, Object[]> res = new TreeMap<Long, Object[]>();
            for(Object[] obj:col)
            	res.put((Long) obj[0], obj);
            return res;
                
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamAmpActivitiesResume()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } 
    }

    /**
     * @deprecated
     * ONLY USE THIS IF YOU NEED THE FULL DATASET - EXTREMELY SLOW. Use {@link #getAllTeamAmpActivitiesResume(Long, boolean, String, String...)} instead
     * @param teamId
     * @param includedraft
     * @param keyword
     * @return
     */
    public static Set<Long> fetchIds(PreparedStatement statement)
    {
    	ResultSet rs = null;
    	try
    	{
    		rs = statement.executeQuery();
    		Set<Long> res = new HashSet<Long>();
    		while (rs.next())
    			res.add(rs.getLong(1));
    		return res;
    	}
    	catch(SQLException ex)
    	{
    		return null;
    	}
    	finally
    	{
    		if (rs != null)
    		{
    			try{rs.close();}
    			catch(SQLException e){}
    		}
    	}
    	
    }
        
  public static List<AmpActivity> getAllTeamAmpActivities(Long teamId, boolean includedraft, String keyword) {
	Connection conn = null;
	try {
		conn = PersistenceManager.getJdbcConnection();

		StringBuilder queryString = new StringBuilder("select distinct(amp_activity_id) from amp_activity A WHERE ");
        
		queryString.append(" (A.deleted is null or A.deleted=false) and ") ;
		if (teamId == null) {
			queryString.append(" (A.amp_team_id is null)") ;
		}else{
			queryString.append(" (A.amp_team_id=" + teamId + ")") ;
		}
        if(!includedraft){
        	queryString.append("  and   (A.draft is null or A.draft=false) ");
        }
        if(keyword!=null && keyword.length()>0){
        	queryString.append(" and lower(A.name) like lower(?)") ;
        }
        
        PreparedStatement statement = conn.prepareStatement(queryString.toString());
        if(keyword!=null && keyword.length()>0){
        	statement.setString(1, "%" + keyword + "%");
        }
        Set<Long> ids = fetchIds(statement);
        
        List<AmpActivity> res = new ArrayList<AmpActivity>();
        Session session = PersistenceManager.getSession();
        	res.addAll(ActivityUtil.getActivityById(ids, session));
        return res;
        
    } catch(Exception e) {
        logger.debug("Exception from getAllTeamAmpActivities()");
        logger.debug(e.toString());
        throw new RuntimeException(e);
    }
	finally
	{
		org.digijava.module.gis.util.DbUtil.closeConnection(conn);
	}
}

    
    public static boolean hasActivities(Long teamId) {
        Session session = null;
        boolean flag = false;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select count(*) from "
                + AmpActivity.class.getName()
                + " act where (act.team=:teamId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while(itr.hasNext()) {
                Integer numActivities = (Integer) itr.next();
                if(numActivities.intValue() != 0) {
                    flag = true;
                }
            }
        } catch(Exception e) {
            logger.debug("Exception from hasActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return flag;
    }

    public static Collection<AmpActivityVersion> getManagementTeamActivities(Long teamId, String keyword) {
        Session session = null;
        Collection<AmpActivityVersion> activities=null;
        Query qry = null;

        try {
            
            Collection childIds = DesktopUtil.getAllChildrenIds(teamId);
            childIds.add(teamId);
            if(childIds != null && childIds.size() > 0) { 
            	session = PersistenceManager.getRequestDBSession();
            	StringBuilder queryString = new StringBuilder("select new AmpActivityVersion(a.ampActivityId,a.name, a.ampId) from "+ AmpActivityGroup.class.getName()+" g inner join g.ampActivityLastVersion a inner join a.team tm where tm.ampTeamId in (:params) and (a.draft is null or a.draft=false) and ( a.deleted is null or a.deleted=false )");     
                if(keyword!=null && keyword.length()>0){
                 	queryString.append(" and lower(a.name) like lower(:name)") ;
                }
                qry = session.createQuery(queryString.toString());
                if(keyword!=null){
                	qry.setString("name", "%" + keyword + "%");
                } 
                qry.setParameterList("params", childIds);
                activities = qry.list();
            }
        } catch(Exception e) {
            logger.debug("Exception from getAllManagementTeamActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } 
        return activities;
    }


    
    public static Collection<AmpActivityVersion> getAllTeamActivities(Long teamId, boolean includedraft, String keyword) {
    	Session session = null;
		Collection<AmpActivityVersion> col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();

			String queryString = "";
			Query qry = null;
			
			String activityNameHql = AmpActivityVersion.hqlStringForName("act");			
			queryString = "select new AmpActivityVersion(act.ampActivityId, " + activityNameHql + ", act.ampId) from "+ AmpActivity.class.getName()	+ " act  ";

			if(teamId!=null){
				queryString+="where act.team="+teamId;
			}else{
				queryString+="where act.team is null";
			}
			if(!includedraft){
	        	queryString+="  and   (act.draft is null or act.draft=false) ";
	        }
            queryString+=" and ( act.deleted is null or act.deleted=false )";
            if(keyword!=null){
            	queryString += " and lower(" + activityNameHql + ") like lower(:name)" ;
            }
			qry = session.createQuery(queryString);
            if(keyword!=null){
            	qry.setParameter("name", "%" + keyword + "%", Hibernate.STRING);
            } 
			
			col=(ArrayList) qry.list();

		} catch (Exception e) {
			logger.debug("Exception from getAllTeamActivities()");
			logger.debug(e.toString());
			throw new RuntimeException(e);
		} 
		return col;		
	}

    public static String getCommaSeparatedList(Collection objs)
    {
    	StringBuilder buf = new StringBuilder();
    	buf.append("");
		for(Object obj:objs)
		{
			if (buf.length() > 0)
				buf.append(", ");
			buf.append(obj.toString());
		}
		buf.append("");
		return buf.toString();
    }
    
    public static List<ReportsCollection> getTeamReportsCollection(Long teamId, Boolean tabs,String keyword) {
        Session session = null;
        ArrayList<ReportsCollection> col = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select tr.report.ampReportId, tr.teamView from "
                + AmpTeamReports.class.getName() + " tr where (tr.team=:teamId) ";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            List<Object[]> rs = qry.list();
            Map<Long, Boolean> map = new TreeMap<Long, Boolean>(); // we want them ordered by id
            for(Object[] item:rs)
            	map.put((Long) item[0], (Boolean) item[1]);
            
            String idsList = getCommaSeparatedList(map.keySet());
            
            col = new ArrayList();
            queryString = String.format("select r from " + AmpReports.class.getName()
	                    + " r " + "where (r.ampReportId in (%s)) ", idsList);
            
            if (tabs != null) {
            	if (tabs) {
            		queryString += " and r.drilldownTab=true ";
            	} else {
            		queryString += " and r.drilldownTab=false ";
            		}
            }
            
            if(keyword != null && keyword.trim().length() > 0){
            	queryString += " and lower(r.name) like lower(:keyword) ";
            }
	                
            queryString += " order by r.name";
            qry = session.createQuery(queryString);
            if(keyword != null && keyword.trim().length() > 0){
            	qry.setString("keyword", '%' + keyword + '%');
            }
            
            //qry.setLong("id", ampTeamRep.getReport().getAmpReportId());
            Iterator<AmpReports> itrTemp = qry.list().iterator();
            while(itrTemp.hasNext()) {
            	AmpReports ampReport = itrTemp.next();
            	ReportsCollection rc = new ReportsCollection();
            	rc.setReport(ampReport);
            	rc.setTeamView(map.get(ampReport.getAmpReportId()));
//            	if(ampTeamRep.getTeamView() == false) {
//		                    rc.setTeamView(false);
//		                } else {
//		                    rc.setTeamView(true);
//		                }		
                col.add(rc);
            }
            Collections.sort(col);
        } catch(Exception e) {
            logger.error("Exception from getTeamReportsCollection", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }
    public static List getTeamReportsCollection(Long teamId, int currentPage, int recordPerPage, Boolean tabs, String keyword) {
        Session session = null;
        List col = new ArrayList<ReportsCollection>();
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select new "+ReportsCollection.class.getName()+"(r, tr.teamView) from "
                + AmpTeamReports.class.getName()
                + " tr inner join tr.report r where (tr.team=:teamId) ";
            
            if (tabs != null) {
				if (tabs) {
					queryString += " and r.drilldownTab=true ";
				} else {
					queryString += " and r.drilldownTab=false ";
				}
			}
            if(keyword != null && keyword.trim().length() > 0){
            	String reportNameHql = AmpReports.hqlStringForName("tr.report");
            	queryString += " and (lower(" + reportNameHql + ") like lower(:keyword) or lower(tr.report.ownerId.user.firstNames) like lower(:keyword) " +
            			"or lower(tr.report.ownerId.user.lastName) like lower(:keyword))";
            }
            queryString += "  order by tr.report";
            Query qry = session.createQuery(queryString);
            
            if(keyword!=null&&keyword.trim().length()>0){
           	 	qry.setString("keyword", '%' + keyword.trim() + '%');
            }
            qry.setFirstResult(currentPage);
            qry.setMaxResults(recordPerPage);
            qry.setLong("teamId", teamId);
            
            col=qry.list();


        } catch(Exception e) {
            logger.debug("Exception from getTeamReportsCollection");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        }
        return col;
    }

    public static int getTeamReportsCollectionSize(Long teamId, Boolean tabs,String keyword) {
       Session session = null;
       List col =new ArrayList();
       int size=0;
       try {
           session = PersistenceManager.getRequestDBSession();
           String queryString = "select r, tr.teamView from "
               + AmpTeamReports.class.getName()
               + " tr inner join tr.report r where (tr.team=:teamId) "; 
           if (tabs != null) {
				if (tabs) {
					queryString += " and r.drilldownTab=true ";
				} else {
					queryString += " and r.drilldownTab=false ";
				}
			}
           if(keyword != null && keyword.trim().length() > 0){
        	   String reportNameHql = AmpReports.hqlStringForName("tr.report");
        	   queryString += " and lower(" + reportNameHql + ") like lower(:keyword) ";
           }
           queryString += " order by tr.report";
           
           Query qry = session.createQuery(queryString);
           if(keyword != null && keyword.trim().length() > 0){
        	   qry.setString("keyword", '%' + keyword + '%');
	       }
	       qry.setLong("teamId", teamId);
           col = qry.list();
           size=col.size();
       } catch(Exception e) {
           logger.debug("Exception from getTeamReportsCollection");
           logger.debug(e.toString());
           throw new RuntimeException(e);
       }
       return size;
   }


    /**
     * Ugly!
     * @param teamId
     * @return
     * @deprecated please use {@link #getAllTeamReports(Long, Integer, Integer)} 
     */
    public static Collection getAllTeamReports(Long teamId) {
        Session session = null;
        Collection col = new ArrayList();
        try {
            session = PersistenceManager.getSession();
            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            String queryString = null;
            Query qry = null;

            if(team.getAccessType().equalsIgnoreCase(
                Constants.ACCESS_TYPE_MNGMT)) {
                queryString = "select r from " + AmpReports.class.getName()
                    + " r " + " order by r.name";
                qry = session.createQuery(queryString);
                col = qry.list();
            } else {
                queryString = "select tr from "
                    + AmpTeamReports.class.getName()
                    + " tr where (tr.team=:teamId)";
                qry = session.createQuery(queryString);
                qry.setParameter("teamId", teamId, Hibernate.LONG);
                Iterator itr = qry.list().iterator();
                col = new ArrayList();
                StringBuffer qryBuffer = new StringBuffer();
                AmpTeamReports ampTeamRep = null;
                while(itr.hasNext()) {
                    ampTeamRep = (AmpTeamReports) itr.next();
                    if(qryBuffer.length() != 0)
                        qryBuffer.append(",");
                    qryBuffer.append(ampTeamRep.getReport().getAmpReportId());
                }

                if(qryBuffer != null && qryBuffer.length() > 0) {
                    queryString = "select r from " + AmpReports.class.getName()
                        + " r " + "where r.ampReportId in (" + qryBuffer
                        + ") order by r.name";
                    qry = session.createQuery(queryString);
                    col = qry.list();
                }
                Collections.sort((ArrayList) col);
            }
        } catch(Exception e) {
            logger.error("Exception from getAllTeamReports()", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }

    /**
     * Returns TeamLeader Reports.
     * Returns all reports from amp_team_reports table for specified team, or all reaports if team is managment team.
     * @param teamId db id of the team
     * @param getTabs when true - gets only reports marked as desktop tabs, when false - gets only reports NOT marked as desktop tabs, when null - gets all reports
     * @param currentPage page start - can be null
     * @param reportPerPage number of reports on page - can be null
     * @return list of {@link AmpReports} objects
     * @author Dare
     */

    public synchronized static List<AmpReports> getAllTeamReports(Long teamId, Boolean getTabs, Integer currentPage, Integer reportPerPage, Boolean inlcludeMemberReport, Long memberId,String name,Long reportCategoryId) {
	    return getAllTeamReports(teamId, getTabs, currentPage, reportPerPage, inlcludeMemberReport, memberId, name, reportCategoryId, null);
    	
    }    
    
    public synchronized static List<AmpReports> getAllTeamReports(Long teamId, Boolean getTabs, Integer currentPage, Integer reportPerPage, Boolean inlcludeMemberReport, Long memberId,String name,Long reportCategoryId,Boolean onlyCategorized) {
 	   Session session 	= null;
        List<AmpReports> col 		= new ArrayList<AmpReports>();
        String tabFilter	= "";
        try {
     	   if ( getTabs!=null ) {
     			   tabFilter	= "r.drilldownTab=:getTabs AND";
     	   }
     	   
            session = PersistenceManager.getRequestDBSession();
            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);
            
            /*AMP-2685 Team leader should not see all reports*/
            AmpTeamMember ampteammember = TeamMemberUtil.getAmpTeamMember(memberId);
            String queryString = null;
            Query qry = null;
            String reportNameHql = AmpReports.hqlStringForName("r");
            if(team.getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                queryString = "select DISTINCT r from " + AmpReports.class.getName()
                    + " r where " + tabFilter + " (r.ownerId.ampTeamMemId = :memberid or r.ampReportId IN (select r2.report from " 
                    + AmpTeamReports.class.getName() 
                    + " r2 where r2.team.ampTeamId = :teamid and r2.teamView = true)) ";
                   if(onlyCategorized!=null && onlyCategorized){
                	   queryString += " and r.reportCategory is not null ";
                   }
                   if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
                  	 queryString += " and r.reportCategory=:repCat ";
                  }
                   if (name != null) {
                	   queryString += String.format(" and lower(%s) like lower(:name) ", reportNameHql);
               	       queryString +=  " order by " + reportNameHql;
                   }

                qry = session.createQuery(queryString);
                qry.setParameter("memberid", ampteammember.getAmpTeamMemId());
                qry.setParameter("teamid", teamId);
                if ( getTabs!=null )
             	   qry.setBoolean("getTabs", getTabs);
                if (name != null) {
                    qry.setString("name", '%' + name + '%');
                }
                if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
               	 qry.setLong("repCat", reportCategoryId);
                }
                if (currentPage !=null){
             	   qry.setFirstResult(currentPage);
                }
                if(reportPerPage!=null && reportPerPage.intValue()>0){
             	   qry.setMaxResults(reportPerPage);
                }
                col = qry.list();
            } else if (!inlcludeMemberReport){
                queryString = "select r from "+ AmpTeamReports.class.getName()+" tr inner join  tr.report r " + "  where " + tabFilter + " (tr.team=:teamId)";
                   if (name != null) {
                    queryString += String.format(" and lower(%s) like lower(:name) ", reportNameHql);
                   }
                   if(onlyCategorized!=null && onlyCategorized){
                	   queryString += " and r.reportCategory is not null ";
                   }
                   if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
                  	 queryString += " and r.reportCategory=:repCat ";
                  }

                queryString +=  " order by " + reportNameHql;
                qry = session.createQuery(queryString);
                qry.setLong("teamId", teamId);
                if ( getTabs!=null )
             	   qry.setBoolean("getTabs", getTabs);
                 if (name != null) {
                    qry.setString("name", '%' + name + '%');
                }
                if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
                   	 qry.setLong("repCat", reportCategoryId);
                }
                
                if (currentPage !=null){
             	   qry.setFirstResult(currentPage);
                }
                if(reportPerPage!=null && reportPerPage.intValue()>0){
             	   qry.setMaxResults(reportPerPage);
                }
                col = qry.list();
            }else if(inlcludeMemberReport){

         	   queryString="select distinct r from " + AmpReports.class.getName()+
				"  r left join r.members m where " + tabFilter + " ((m.ampTeamMemId is not null and m.ampTeamMemId=:ampTeamMemId)"+ 
				" or r.id in (select r2.id from "+ AmpTeamReports.class.getName() + 
				" tr inner join  tr.report r2 where tr.team=:teamId and tr.teamView = true))";
               if(name!=null){
                        queryString += String.format(" and lower(%s) like lower(:name)", reportNameHql);
               }
               if(onlyCategorized!=null && onlyCategorized){
            	   queryString += " and r.reportCategory is not null ";
               }
               if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
              	 queryString += " and r.reportCategory=:repCat ";
              }
         	  qry = session.createQuery(queryString); 
         	  qry.setLong("ampTeamMemId", memberId);
          	  qry.setLong("teamId", teamId);
          	  if ( getTabs!=null )
          		  qry.setBoolean("getTabs", getTabs);
                if (name != null) {
                    qry.setString("name", '%' + name + '%');
                }
                if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
                  	 qry.setLong("repCat", reportCategoryId);
                }
         	  if (currentPage !=null){
            	   qry.setFirstResult(currentPage);
               }
               if(reportPerPage!=null && reportPerPage.intValue()>0){
            	   qry.setMaxResults(reportPerPage);
               }
               col = qry.list();

            }
            //transaction.commit();
        } catch(Exception e) {
            logger.error("Exception from getAllTeamReports()", e);
            throw new RuntimeException(e);
        }
        return col;	
 	
 }
    
    
    public static ArrayList<AmpReports> getLastShownReports(Long teamId, Long memberId, Boolean getTabs,Boolean onlyCategorized) 
    {       
    	Session session 	= null;
    	ArrayList<AmpReports> col = new ArrayList<AmpReports>();
    	String tabFilter	= "";
    	if ( getTabs!=null ) {
    		tabFilter	= "r.drilldownTab=:getTabs AND ";
    	}
    	String favourites ="";
    	if(onlyCategorized!=null && onlyCategorized){
    		favourites = " r.reportCategory is not null AND ";
    	}

       try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            String queryString = null;
            Query qry = null;
            //oracle doesn't support order by from a column that is not part of the distinct Statement  so we have to include the  m.lastView  in the select part 

         	queryString="select distinct r,m.lastView from " + AmpReports.class.getName()+
			"  r inner join r.logs m where "+tabFilter+favourites+" (m.member is not null and m.member.ampTeamMemId=:ampTeamMemId) order by m.lastView desc";
         	qry = session.createQuery(queryString); 
         	qry.setLong("ampTeamMemId", memberId);
       	    if ( getTabs!=null )
     		  qry.setBoolean("getTabs", getTabs);
             
       	 //Sience we include a new column in the query the return will be an collection havin an array the object    
       	 Iterator itData = null;
       	 itData = qry.iterate();
	       	while(itData.hasNext()){
	       		col.add((AmpReports)((Object[])itData.next())[0]);
	       	}
       	  //end fix for oracle
	       	
	       	if (col.isEmpty()){
            	queryString="select distinct r from " + AmpReports.class.getName()+ " r where r.drilldownTab=false AND "+favourites+"  r.ownerId is not null and r.ownerId=:ampTeamMemId";
            	qry = session.createQuery(queryString); 
             	qry.setLong("ampTeamMemId", memberId);
             	col = new ArrayList<AmpReports>(qry.list());
            }
	       	//transaction.commit();
        } catch(Exception e) {
            logger.error("Exception from getAllTeamReports()", e);
            throw new RuntimeException(e);
        }
        return col;	
 }

    public static List getAllTeamReports(Long teamId, Integer currentPage, Integer reportPerPage) {
    	return getAllTeamReports( teamId, null,  currentPage,  reportPerPage, false,null,null,null);
    }
 
    public static List<AmpTeam> getAllManagementWorkspaces()
    {
    	return getAllTeams(null, "management");
    }

    /**
     * 
     * @param teamId
     * @param getTabs when true - gets only reports marked as desktop tabs, when false - gets only reports NOT marked as desktop tabs, when null - gets all reports
     * @param inlcludeMemberReport
     * @param memberId
     * @return
     */
    public static int getAllTeamReportsCount(Long teamId, Boolean getTabs, Boolean inlcludeMemberReport, Long memberId) {
        Session session = null;
        int count=0;
        String tabFilter	= "";
        try {
        	if ( getTabs!=null ) {
 			   tabFilter	= "r.drilldownTab=:getTabs AND";
        	}
        	
            session = PersistenceManager.getRequestDBSession();
            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            String queryString = null;
            Query qry = null;

            if(team.getAccessType().equalsIgnoreCase(
                Constants.ACCESS_TYPE_MNGMT)) {
            	queryString	= "select count(*) from "+ AmpReports.class.getName()
                				+ " r WHERE " + tabFilter + " 1=1";
            	qry			= session.createQuery(queryString);
            	if ( getTabs!=null )
           		  	qry.setBoolean("getTabs", getTabs);
                count		= (Integer) qry.uniqueResult();
                
            } else if (!inlcludeMemberReport){
                queryString = "select r from "
                    + AmpTeamReports.class.getName()+" tr inner join tr.report r "
                    + "  where " + tabFilter + " (tr.team=:teamId) ";
                qry = session.createQuery(queryString);
                if ( getTabs!=null )
           		  qry.setBoolean("getTabs", getTabs);
                qry.setParameter("teamId", teamId, Hibernate.LONG);
               count=qry.list().size();
            }else if(inlcludeMemberReport){
         	   queryString="select distinct r from " + AmpReports.class.getName()+
  				"  r left join r.members m where " + tabFilter + 
  				" ((m.ampTeamMemId is not null and m.ampTeamMemId=:ampTeamMemId)"+ 
				" or r.id in (select r2.id from "+ AmpTeamReports.class.getName() + 
				" tr inner join  tr.report r2 where tr.team=:teamId))";
           	  qry = session.createQuery(queryString); 
           	  qry.setLong("ampTeamMemId", memberId);
           	  qry.setLong("teamId", teamId);
	           	if ( getTabs!=null )
	       		  qry.setBoolean("getTabs", getTabs);
           	  count=qry.list().size();
            }
        } catch(Exception e) {
            logger.error("Exception from getAllTeamReports()", e);
            throw new RuntimeException(e);
        }
        return count;
    }
    
   
    public static int getAllTeamReportsCount(Long teamId) {
	   return getAllTeamReportsCount(teamId, null, false,  null);
   }


   public static List getAllTeamAndMemberReports(Long teamId, Integer currentPage, Integer reportPerPage) {
       Session session = null;
       List col = new ArrayList();
       try {
           session = PersistenceManager.getRequestDBSession();
           AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

           String queryString = null;
           Query qry = null;

           if(team.getAccessType().equalsIgnoreCase(
               Constants.ACCESS_TYPE_MNGMT)) {
               queryString = "select r from " + AmpReports.class.getName()
                   + " r " + " order by r.name ";
               qry = session.createQuery(queryString);
               if (currentPage !=null){
            	   qry.setFirstResult(currentPage);
               }
               if(reportPerPage!=null && reportPerPage.intValue()>0){
            	   qry.setMaxResults(reportPerPage);
               }
               col = qry.list();
           } else {
               queryString = "select r from "
                   + AmpTeamReports.class.getName()+" tr inner join  tr.report r "
                   + "  where (tr.team=:teamId) order by r.name ";
               qry = session.createQuery(queryString);
               qry.setLong("teamId", teamId);
               if (currentPage !=null){
            	   qry.setFirstResult(currentPage);
               }
               if(reportPerPage!=null && reportPerPage.intValue()>0){
            	   qry.setMaxResults(reportPerPage);
               }
               col = qry.list();
           }
       } catch(Exception e) {
           logger.error("Exception from getAllTeamReports()", e);
           throw new RuntimeException(e);
       }
       return col;
   }

    
    
    

    public static AmpTeamReports getAmpTeamReport(Long teamId, Long reportId) {
        Session session = null;
        AmpTeamReports ampTeamRep = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select tr from "
                + AmpTeamReports.class.getName()
                + " tr where (tr.team=:teamId) and (tr.report=:reportId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            qry.setParameter("reportId", reportId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                ampTeamRep = (AmpTeamReports) itr.next();
            }
        } catch(Exception e) {
            logger.debug("Exception from getAmpTeamReport()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return ampTeamRep;
    }

    public static Collection getAllUnassignedActivities() {
        //return getAllTeamActivities(null);
    	return getAllTeamAmpActivities(null,true,null);
    }

    /**
     * returns list of all team reports which are not assigned to the team
     * @param id
     * @param tabs
     * @return
     */
    public static Collection getAllUnassignedTeamReports(Long id, Boolean tabs) {
        Session session = null;
        Collection col = null;
        Collection col1 = null;

        try {
        	session = PersistenceManager.getRequestDBSession();
        	String queryString = "select tr.report.ampReportId from "+ AmpTeamReports.class.getName()+ " tr where (tr.team=:teamId) ";            
        	Query qry = session.createQuery(queryString);
        	qry.setLong("teamId", id);
        	String excludedIds = getCommaSeparatedList(qry.list());
        
        	queryString = String.format("select r from " + AmpReports.class.getName() + " r where r.ampReportId NOT IN (%s) ", excludedIds);
        	if (tabs != null) {
        		if (tabs) {
        			queryString += " and r.drilldownTab=true ";
        		} else {
        			queryString += " and r.drilldownTab=false ";
        		}
        	};
        	qry = session.createQuery(queryString);
        	List<AmpReports> ret = qry.list();
        	return ret;
        }

        catch(Exception e)
        {
        	throw new RuntimeException(e);
        }        
    }

    public static Collection<AmpTeam> getAllTeams() {
        Session session = null;
        Query qry = null;
        Collection teams = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            String queryString = "select t from " + AmpTeam.class.getName() + " t order by name";
            qry = session.createQuery(queryString);
            qry.setCacheable(true);
            teams = qry.list();
            for (AmpTeam t: (Collection<AmpTeam>)teams){
            	t.getOrganizations().size(); //lazy init
            }
        } catch(Exception e) {
            logger.debug("cannot get All teams");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
                throw new RuntimeException(ex);
            }
        }
        return teams;
    }
    public static List<AmpTeam> getAllTeams(String keyword,String type) {
        Session session = null;
        Query qry = null;
        List<AmpTeam> teams = null;
        boolean computed = type != null && type.equals("computed");
        String accessType=null;
        if(!type.equalsIgnoreCase("all"))
        	accessType = (type != null && type.equals("management")) ? "Management" : "Team";
        
        try {
            session = PersistenceManager.getSession();
            StringBuilder queryString =new StringBuilder();
            queryString.append("select t from ");
            queryString.append(AmpTeam.class.getName());
            queryString.append(" t ");
            queryString.append(" where 1=1 ");
            String teamNameHql = AmpTeam.hqlStringForName("t");
            if(keyword!=null&&keyword.trim().length()>0){
            	queryString.append(String.format(" and lower(%s) like lower(:keyword) ", teamNameHql));
            }
            if(computed){
            	queryString.append(" and t.computation=:computation ");
            }
            if(accessType!=null){
            	queryString.append(" and t.accessType=:accessType ");
            }
            queryString.append("order by " + teamNameHql);
            qry = session.createQuery(queryString.toString());
            if(keyword!=null&&keyword.trim().length()>0){
            	 qry.setString("keyword", '%' + keyword + '%');
            }
            if(computed){
            	qry.setBoolean("computation", Boolean.TRUE);
            }
            if(accessType!=null){
            	qry.setString("accessType",accessType);
            }
            qry.setCacheable(true);
            teams = qry.list();
        } catch(Exception e) {
            logger.debug("cannot get All teams");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } 
        return teams;
    }
    /**
     * Returns Collection of the computation or non computation {@link AmpTeam} objects
     * @param computation boolean
     * @return Collection of AmpTeam
     */

     public static List<AmpTeam> getAllTeams(boolean computation) {
        Session session = null;
        Query qry = null;
        List<AmpTeam> teams = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select t from " + AmpTeam.class.getName() + " t where t.computation=:computation";
            if(!computation){
                queryString+= " or t.computation is null ";
            }
            queryString+= " order by name";
            qry = session.createQuery(queryString);
            qry.setBoolean("computation", computation);
            teams = qry.list();
        } catch(Exception e) {
            logger.debug("cannot get All  teams"+e.getMessage());
        }
        return teams;
    }

    public static Set<AmpTeam> getAmpLevel0Teams(Long ampTeamId) {
        Session session = null;
        Set teams = new TreeSet();

        try {
            logger.debug("Team Id:" + ampTeamId);
            session = PersistenceManager.getSession();
            String queryString = "select t from " + AmpTeam.class.getName()
                + " t " + "where (t.parentTeamId.ampTeamId=:ampTeamId)";
            logger.debug("Query String:" + queryString);
            Query qry = session.createQuery(queryString);
            qry.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
            Iterator itrTemp = qry.list().iterator();
            AmpTeam ampTeam = null;
            LinkedList<AmpTeam> list = new LinkedList<AmpTeam>();
            list.addAll(qry.list());
            HashSet<Long> visitedTeams = new HashSet<Long>();
            visitedTeams.add(ampTeamId); //add current team to the visited set
            while(list.size() > 0) {
                ampTeam = (AmpTeam) list.removeFirst();
                visitedTeams.add(ampTeam.getAmpTeamId());
                //if(ampTeam.getAccessType().equals("Team") || ampTeam.getAccessType().equals("Computed") )
                if(ampTeam.getAccessType().equals("Team") || 
                			(ampTeam.getComputation()!=null && ampTeam.getComputation()==true) )
                    teams.add(ampTeam);
                else {
                    queryString = "select t from " + AmpTeam.class.getName()
                        + " t " + "where (t.parentTeamId.ampTeamId="
                        + ampTeam.getAmpTeamId() + ")";
                    qry = session.createQuery(queryString);
                    List<AmpTeam> tempList = qry.list();
                    Iterator<AmpTeam> it = tempList.iterator();
                    while (it.hasNext()){
                        AmpTeam tt = it.next();
                        if (!visitedTeams.contains(tt.getAmpTeamId()))
                            list.add(tt);
                    }
                }

                // ampTeam = (AmpTeam) itrTemp.next();
                // teams.add(ampTeam.getAmpTeamId());
            }
            logger.debug("Size: " + teams.size());

        } catch(Exception e) {
            logger.debug("Exception from getAmpLevel0Team()" + e.getMessage());
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return teams;
    }

    public static Collection getAmpLevel0TeamIds(Long ampTeamId) {
        Set teams = getAmpLevel0Teams(ampTeamId);
        Collection ret = new ArrayList();
        Iterator i = teams.iterator();
        while(i.hasNext()) {
            AmpTeam element = (AmpTeam) i.next();
            ret.add(element.getAmpTeamId());
        }
        return ret;
    }

    /**
     * Retrieves current Team from request
     * @param request
     * @return currentAmpTeam
     */
    public static AmpTeam getCurrentTeam(HttpServletRequest request) {
        AmpTeam currentAmpTeam = TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam();
        return currentAmpTeam;
    }

    public static class HelperAmpTeamNameComparator
        implements Comparator {
        public int compare(Object obj1, Object obj2) {
            AmpTeam team1 = (AmpTeam) obj1;
            AmpTeam team2 = (AmpTeam) obj2;
            return team1.getName().compareTo(team2.getName());
        }

    }
    
    public static class HelperAmpTeamNameComparatorTrimmed
    implements Comparator {
    public int compare(Object obj1, Object obj2) {
        AmpTeam team1 = (AmpTeam) obj1;
        AmpTeam team2 = (AmpTeam) obj2;
        return team1.getName().trim().toLowerCase().compareTo(team2.getName().trim().toLowerCase());
    }

}
    
	public static class HelperAmpTeamNameComparatorDesc implements Comparator {
	    public int compare(Object obj1, Object obj2) {
	        AmpTeam team1 = (AmpTeam) obj1;
	        AmpTeam team2 = (AmpTeam) obj2;
	        return team2.getName().compareTo(team1.getName());
	    }
	}
	
	public static class HelperAmpTeamNameComparatorDescTrimmed implements Comparator {
	    public int compare(Object obj1, Object obj2) {
	        AmpTeam team1 = (AmpTeam) obj1;
	        AmpTeam team2 = (AmpTeam) obj2;
	        return team2.getName().trim().toLowerCase().compareTo(team1.getName().trim().toLowerCase());
	    }
	}
	    
    
    public static List<AmpTeam> getTeamByOrg(Long orgId) {
 	 	List<AmpTeam> retValue = new ArrayList<AmpTeam>();
 	 	if (orgId != null ){
 	 		Collection<AmpTeam> teams = getAllTeams();
 	 		for (AmpTeam ampTeam : teams) {
 	 			for (Iterator iterator = ampTeam.getOrganizations().iterator(); iterator.hasNext();) {
 	 				AmpOrganisation org = (AmpOrganisation) iterator.next();
 	 				if (org.getAmpOrgId().equals(orgId)){
 	 					retValue.add(ampTeam);
 	 					break;
 	 				}
 	 			}
 	 		}
 	 	}
 	 	return retValue;
    }
    
    public static boolean hasUserAccessToActivity(Long actId, HttpServletRequest request){
        Session session = null;
        Query qry = null;

        HttpSession httpSession = request.getSession();
        TeamMember tm = (TeamMember) httpSession.getAttribute("currentMember");
        
        String qryStr;
        try {
        	session = PersistenceManager.getRequestDBSession();
        	
        	
        	qryStr = "select count(*) from " + AmpActivity.class.getName() + " act " + "where (act.team=:teamId) and (act.ampActivityId=:actId)";
        	qry = session.createQuery(qryStr);
        	qry.setLong("teamId", tm.getTeamId());
        	qry.setLong("actId", actId);
        	Integer acts = (Integer) qry.list().get(0);
        	if (acts > 0)
        		return true;
        } catch(Exception e) {
        	logger.debug("cannot get acts "+e.getMessage());
        }
    	return false;
    }
}
