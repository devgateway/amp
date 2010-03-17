/**
 * 
 */
package org.dgfoundation.amp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.UserUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.module.aim.action.EditActivity;
import org.digijava.module.aim.action.SaveActivity;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockActionServlet;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;

/**
 * @author mihai
 * This is a standalone initializer for digi/hibernate, that can be use to patch/inspect/query
 * the amp database without jboss/tomcat running.
 */
public class StandaloneAMPStartup {
	private static Logger logger = Logger.getLogger(StandaloneAMPStartup.class);
	
	
	  public static AmpActivity loadActivity(Long id, Session session) throws DgException {
		AmpActivity result = null;

		try {
			session.flush();
			result = (AmpActivity) session.get(AmpActivity.class, id);
			session.evict(result);
			result = (AmpActivity) session.get(AmpActivity.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivity with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Canno load AmpActivity with id" + id, e);
		}
		return result;
	}

	/**
	 * Generate 200,000 activity copies when AMP Starts, using XML Import/Export.
	 * @param session the Hibernate Session
	 * @param ampActivityId the id of the source AmpActivity
	 * @throws DgException
	 * @throws JAXBException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private static void generate200k(Long ampActivityId,Session session) throws DgException, JAXBException, HibernateException, SQLException {
/*  AmpHarvester was removed from AMP
		AmpActivity activity = loadActivity(ampActivityId,session);				
		ActivityType xmlActivity = ExportManager.getXmlActivity(activity, session);
		
		
		ObjectFactory objFactory = new ObjectFactory();
		Activities aList=objFactory.createActivities();
		aList.getActivity().add(xmlActivity);
		
		xmlActivity.setAmpId("200K ");
		xmlActivity.getTitle().setValue("Generated Activity ");
		byte[] toByte = XmlTransformerHelper.marshalToByte(aList);
		
		String title=xmlActivity.getTitle().getValue();
		ImportManager im=new ImportManager(toByte);
		for(long i=1;i<200000;i++){ 
		im.startImportHttp(new String("#"+i), activity.getTeam());
		if((i % 100)==0) logger.info("Completed #"+i);
		}
		PersistenceManager.releaseSession(session);
*/				
	}
	
	private static void replicateActivity(Long ampActivityId,String newName, Session session) throws Exception {
		//mock http servlet request
		MockHttpServletRequest request=new MockHttpServletRequest();
		MockActionMapping mam=new MockActionMapping();
		MockHttpServletResponse msrep=new MockHttpServletResponse();
		request.setSession(new MockHttpSession());
		HttpSession httpsess = request.getSession();
		EditActivityForm eaf=new EditActivityForm();
		eaf.setActivityId(ampActivityId);

		EditActivity eas=new EditActivity();
		MockActionServlet servlet = new MockActionServlet();
		servlet.setServletContext(new MockServletContext());
		eas.setServlet(servlet);
		mam.setPath("");
		httpsess.setAttribute("teamLeadFlag", new String("true"));
		
		//simulate team member
		
		User usr = UserUtils.getUserByEmail("atl@amp.org");
		
		
		Locale locale = new Locale();
		locale.setCode("en");
		request.setAttribute(org.digijava.kernel.Constants.NAVIGATION_LANGUAGE, locale);

		SiteDomain siteDomain = new SiteDomain();
		Site site = new Site("newdemo", "newdemo");
		site.setId(new Long(3));

		SiteCache siteCache = SiteCache.getInstance();

		siteDomain.setSite(site);
		request.setAttribute(org.digijava.kernel.Constants.CURRENT_SITE, siteDomain);
		
		AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(usr); 
		ServletContext ampContext = servlet.getServletContext();
		
		
		TeamMember tm = new TeamMember();
		tm.setMemberName(usr.getName());
		tm.setMemberId(teamMember.getAmpTeamMemId());
		AmpApplicationSettings ampAppSettings = DbUtil.getMemberAppSettings(teamMember.getAmpTeamMemId());
		
		ApplicationSettings appSettings = new ApplicationSettings();
		appSettings.setAppSettingsId(ampAppSettings
				.getAmpAppSettingsId());
		appSettings.setDefRecsPerPage(ampAppSettings
				.getDefaultRecordsPerPage());
		appSettings.setCurrencyId(ampAppSettings.getCurrency()
				.getAmpCurrencyId());
		appSettings.setFisCalId(ampAppSettings.getFiscalCalendar()
				.getAmpFiscalCalId());
		appSettings.setValidation(ampAppSettings.getValidation());

		//appSettings.setLanguage(ampAppSettings.getLanguage());

		String langCode = UserUtils.getUserLangPreferences(
				usr,RequestUtils.getSite(request)).getAlertsLanguage().getCode();

		appSettings.setLanguage(langCode);
		tm.setAppSettings(appSettings);
	
		AmpTeam team = TeamUtil.getAmpTeam(1L);
		
		tm.setTeamName(team.getName());
		tm.setTeamId(team.getAmpTeamId());
		tm.setWrite(true);
		httpsess.setAttribute(Constants.CURRENT_MEMBER, tm);

		AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
		// get the default amp template!!!
		AmpTreeVisibility ampTreeVisibilityAux = new AmpTreeVisibility();
		AmpTreeVisibility ampTreeVisibilityAux2 = new AmpTreeVisibility();
	

		AmpTemplatesVisibility currentTemplate = null;
		try {
			currentTemplate = FeaturesUtil
					.getTemplateVisibility(
							FeaturesUtil
									.getGlobalSettingValueLong("Visibility Template"),
							session);
			ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
		} finally {
			PersistenceManager.releaseSession(session);
		}
		ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
		ampContext.setAttribute("FMcache", "read");
		
		eas.execute(mam, eaf, request, msrep);
		
		eaf.getIdentification().setDraft(new Boolean(false));
		eaf.getIdentification().setTitle(newName);
		
		AmpClassificationConfiguration auxClassification = new AmpClassificationConfiguration();
		int i=0;
		auxClassification.setClassification((AmpSectorScheme) ((List) SectorUtil.getAllSectorSchemes()).get(i));
		auxClassification.setId(new Long(i));
		auxClassification.setName(i == 0 ? "Primary" : "Secondary");
		auxClassification.setPrimary(i == 0 ? true : false);
		eaf.getSectors().setClassificationConfigs(new ArrayList<AmpClassificationConfiguration>());
		eaf.getSectors().getClassificationConfigs().add(auxClassification);
		
		eaf.setActivityId(null);
		List steps = ActivityUtil.getSteps();
		eaf.setSteps(steps);
		eaf.setEditAct(false);
	
		
		SaveActivity saveAct=new SaveActivity();
		saveAct.setServlet(servlet);
		saveAct.execute(mam, eaf, request, msrep);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceStreamHandlerFactory.installIfNeeded();
		try {
			
			
			
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
			
			DigiConfigManager.initialize("repository");
			PersistenceManager.initialize(false);
		
			//BELOW THIS LINE, HIBERNATE IS AVAILABLE, ADD YOUR SCRIPT INVOCATION HERE			
			
			
			
			try {
				//EXAMPLE OF A WORKING HIBERNATE SESSION OBJECT:

				
				for (int i=0; i<1000;i++) {
					Session session = PersistenceManager.getSession();
					logger.info("Creating activity replica "+i);
					replicateActivity(32L,"Monrovia Water Treatment "+i, session);
					PersistenceManager.releaseSession(session);
				}
				
				
				
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}

	}

}
