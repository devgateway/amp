package org.digijava.kernel.ampapi.endpoints.security;

import java.io.IOException;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.AmpApiToken;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
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

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * This class should have all security / permissions related methods
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
	 * 
	 * @return
	 */
	@GET
	@Path("/user/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean user() {
		JsonBean authenticationResult = new JsonBean();
		

		AmpApiToken apiToken= SecurityUtil.getTokenFromSession();

		boolean isAdmin ="yes".equals(httpRequest.getSession().getAttribute("ampAdmin"));
		
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		String username=null;
		String team=null;
		
		//if the user is admin the he doesn't have a workspace assigned
		if (tm != null && !isAdmin ) {
			User u;
			AmpTeamMember ampTeamMember;
			try {
				u = UserUtils.getUserByEmail(tm.getEmail());
				username=u.getName();
				ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
				team=ampTeamMember.getAmpTeam().getName();

			} catch (DgException e) {
				// TODO return error 500 with description
				e.printStackTrace();
			}

		}
	    String port="";
	    //if we are in secure mode and the port is not 443 or if we are not secure and the port is not 80 we have to add the port to the url
	    if( (TLSUtils.getRequest().isSecure() && TLSUtils.getRequest().getServerPort()!=443 ) ||( !TLSUtils.getRequest().isSecure() && TLSUtils.getRequest().getServerPort()!=80 )){
	    	port=":"+TLSUtils.getRequest().getServerPort();
	    }
		authenticationResult.set("token", apiToken!=null && apiToken.getToken()!=null?apiToken.getToken():null);
		authenticationResult.set("url", "http"+ (TLSUtils.getRequest().isSecure()?"S":"") +"://"+ TLSUtils.getRequest().getServerName() + port +"/showLayout.do?layout=login");
		authenticationResult.set("team", team);
		authenticationResult.set("user-name", username);
		authenticationResult.set("add-activity", false); //to check if the user can add activity in the selected ws
		authenticationResult.set("view-activity", true); //to check if the user can edit activity in the selected ws

		return authenticationResult;
	}
	/**
	 * @return menu structure for the current view, user and state
	 */
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
			layout.set("userId",u.getId());
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

	/**
	 * Authorizes Container Request
	 * @param containerReq
	 */
	public static void authorize(ContainerRequest containerReq) {
		/* disabling for now, since cannot commit yet ApiAuthorization class due to some pom.xml changes needed and we are in 2.10 code freeze 
		ApiAuthorization.authorize(containerReq);
		*/
	}
	
	/**
	 * THIS IS FOR DEBUG ONLY. Must be disabled on production.
	 * @param token
	 * @return
	 */
	/*
	@GET
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String echo(@QueryParam("amp_api_token") String token) {
		token = SecurityUtil.generateToken();
		return "Token: " + token;
	}
	*/
}
