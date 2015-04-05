package org.digijava.kernel.ampapi.endpoints.security;

import java.io.IOException;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.xml.sax.SAXException;

/**
 * This class should have all security / permisions related methods
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("security")
public class Security {
	private static final Logger logger = Logger.getLogger(Security.class);
	private static String SITE_CONFIG_PATH = "TEMPLATE" + System.getProperty("file.separator") + "ampTemplate"
			+ System.getProperty("file.separator") + "site-config.xml";

	@Context
	private HttpServletRequest httpRequest;

	/**
	 * if there'is a looged user it returns
	 * 
	 * <code>
	 *  {
       	"id": "45678",
       	"email": "atl@amp.org",
       	"firstName": "ATL",
       	"lastName": "ATL",
       	"workspace": "Ministry of Finance",
       	"administratorMode": "false"
   		}   
   		</code> <code>
		if not logged it it returns 
		{
   			username: null
		}
		</code>
	 * 
	 * @return
	 */
//	@GET
//	@Path("/user")
//	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//	@ApiMethod(ui = false, id = "LoggedUser", name = "Logged user")
//	public JsonBean getUser() {
//		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
//
//		JsonBean user = new JsonBean();
//		if (tm != null) {
//			Site site = RequestUtils.getSite(TLSUtils.getRequest());
//			User u;
//			try {
//				u = UserUtils.getUserByEmail(tm.getEmail());
//			} catch (DgException e) {
//				user.set("email", null);
//				return user;
//			}
//			Subject subject = UserUtils.getUserSubject(u);
//
//			boolean siteAdmin = DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN);
//			user.set("email", u.getEmail());
//			user.set("firstName", u.getFirstNames());
//			user.set("lastName", u.getLastName());
//			user.set("administratorMode", siteAdmin);
//			if (!siteAdmin) {
//				AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
//
//				if (ampTeamMember.getAmpTeam() != null) { 
//					user.set("workspace", ampTeamMember.getAmpTeam().getName());
//					user.set("workspaceId", ampTeamMember.getAmpTeam().getIdentifier());
//				}
//				
//			} else {
//				return user;
//			}
//		} else {
//			user.set("email", null);
//		}
//		return user;
//	}

	@GET
	@Path("/menus")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Menu", name = "Menu")
	public List<JsonBean> getMenu() {
		return SecurityService.getMenu();
	}
	
	
	/**
	 * For the user response
	 * if there'is a logged user it returns
	 * 
	 * <code>
	 *  {
       	"id": "45678",
       	"email": "atl@amp.org",
       	"firstName": "ATL",
       	"lastName": "ATL",
       	"workspace": "Ministry of Finance",
       	"administratorMode": "false"
   		}   
   		</code> <code>
		if not logged it it returns 
		{
   			username: null
		}
		</code>
	 * 
	 * @return
	 */
	@GET
	@Path("/layout")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Layout", name = "Layout")
	public JsonBean getLayout() throws ParserConfigurationException, SAXException, IOException 
	{
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		String ampAdmin = (String) httpRequest.getSession().getAttribute("ampAdmin");
		boolean isAdmin = ampAdmin!=null && ampAdmin.equals("yes");
		SiteDomain currentDomain = RequestUtils.getSiteDomain(httpRequest);
		String siteUrl = SiteUtils.getSiteURL(currentDomain, httpRequest.getScheme(), httpRequest.getServerPort(), httpRequest
				.getContextPath());
		JsonBean layout = SecurityService.getFooter(httpRequest.getServletContext().getRealPath("/")+SITE_CONFIG_PATH,siteUrl,isAdmin);
		if (tm != null) {
			Site site = RequestUtils.getSite(TLSUtils.getRequest());
			User u;
			try {
				u = UserUtils.getUserByEmail(tm.getEmail());
			} catch (DgException e) {
				layout.set("email", null);
				return layout;
			}
			Subject subject = UserUtils.getUserSubject(u);

			boolean siteAdmin = DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN);
			layout.set("logged",true);
			layout.set("email", u.getEmail());
			layout.set("firstName", u.getFirstNames());
			layout.set("lastName", u.getLastName());
			layout.set("administratorMode", siteAdmin);
			if (!siteAdmin) {
				AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());

				if (ampTeamMember.getAmpTeam() != null) { 
					layout.set("workspace", ampTeamMember.getAmpTeam().getName());
					layout.set("workspaceId", ampTeamMember.getAmpTeam().getIdentifier());
				}
				
			} else {
				return layout;
			}
		} else {
			layout.set("email", null);
			if ("true".equals(TLSUtils.getRequest().getSession().getAttribute("isUserLogged"))) {
				layout.set("logged",true);
			}
			else {
				layout.set("logged",false);
			}
			
		}

		return layout;
		
	}


}
