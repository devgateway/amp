package org.digijava.kernel.ampapi.endpoints.security;

import com.sun.jersey.spi.container.ContainerRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
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
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.xml.sax.SAXException;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

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

	public static String getSiteConfigPath() {
		return SITE_CONFIG_PATH;
	}
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
		boolean isAdmin;

		AmpApiToken apiToken = SecurityUtil.getTokenFromSession();

		isAdmin ="yes".equals(httpRequest.getSession().getAttribute("ampAdmin"));
		
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		String username = null;
		String team = null;
		boolean addActivity = false;
		//if the user is adminn the he doesn't have a workspace assigned
 
		if (tm != null) {
			username = tm.getMemberName();
			if (!isAdmin) {
				AmpTeamMember ampTeamMember;
				ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
				team = ampTeamMember.getAmpTeam().getName();
				// if the user is logged in without a token, we generate one
				if (apiToken == null) {
					// if no token is present in session we generate one
					apiToken = SecurityUtil.generateToken();
				}
				addActivity = FeaturesUtil.isVisibleField("Add Activity Button") && ampTeamMember.getAmpTeam().getAddActivity();
			}

		}
		return createResponse(isAdmin, apiToken, username, team, addActivity);
	}

	@NotNull
	private String getPort() {
		String port="";
		//if we are in secure mode and the port is not 443 or if we are not secure and the port is not 80 we have to add the port to the url
		if( (this.httpRequest.isSecure() && this.httpRequest.getServerPort()!=443 ) ||( !this.httpRequest.isSecure() && this.httpRequest.getServerPort()!=80 )){
            port=":"+this.httpRequest.getServerPort();
        }
		return port;
	}

	private JsonBean createResponse(boolean isAdmin, AmpApiToken apiToken, String username, String team, boolean addActivity) {
		String port = getPort();
		final JsonBean authenticationResult = new JsonBean();
		authenticationResult.set("token", apiToken != null && apiToken.getToken() != null ? apiToken.getToken() : null);
		authenticationResult.set("token-expiration", apiToken != null && apiToken.getExpirationTime() != null ? apiToken.getExpirationTime().getMillis() : null);
		authenticationResult.set("url", "http"+ (TLSUtils.getRequest().isSecure()?"s":"") +"://"+ TLSUtils.getRequest().getServerName() + port +"/showLayout.do?layout=login");
		authenticationResult.set("team", team);
		authenticationResult.set("user-name", username);
		authenticationResult.set("is-admin", isAdmin);
		authenticationResult.set("add-activity", addActivity); //to check if the user can add activity in the selected ws
		authenticationResult.set("view-activity", !isAdmin); ///at this stage the user can view activities only if you are not admin
		return authenticationResult;
	}

	/**
	 * This is hack EP to avoid Digest Authentication in Activity EP.
	 *
	 * @param authentication Json bean with username/password/workspace information
	 * @return JSON with the response, error or user info
     */
	@POST
	@Path("/user/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean authenticate(final JsonBean authentication) {
		final String username = authentication.getString("username");
		final String password = authentication.getString("password");
		final Integer workspaceId = (Integer) authentication.get("workspaceId");
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || workspaceId == null) {
			ApiErrorResponse.reportError(BAD_REQUEST, SecurityErrors.INVALID_REQUEST);
		}
		try {
			final User user = UserUtils.getUserByEmail(username);
			if (user == null || !user.getPassword().equals(password)) {
				ApiErrorResponse.reportForbiddenAccess(SecurityErrors.INVALID_TOKEN);
			}

			final ApiErrorMessage result = ApiAuthentication.login(user, this.httpRequest);
			if (result != null) {
				ApiErrorResponse.reportForbiddenAccess(result);
			}
			final AmpTeam ampTeam = TeamUtil.getAmpTeam(new Long(workspaceId));
			final Collection<AmpTeamMember> allAmpTeamMembersByUser = TeamMemberUtil.getTeamMembers(user.getEmail());
			AmpTeamMember teamMember = null;
			for (final AmpTeamMember atm :
					allAmpTeamMembersByUser) {
				if (atm.getAmpTeam().getAmpTeamId().equals(ampTeam.getAmpTeamId())) {
					teamMember = atm;
				}
			}

			if (ampTeam == null || (teamMember == null && !user.isGlobalAdmin())) {
				ApiErrorResponse.reportError(BAD_REQUEST, SecurityErrors.INVALID_REQUEST);
			}
			storeInSession(username, password, teamMember, user);

			final AmpApiToken ampApiToken = SecurityUtil.generateToken();

			return createResponse(user.isGlobalAdmin(), ampApiToken, username, ampTeam.getName(), true);
		} catch (final Exception e) {
			logger.error("Error trying to login the user", e);
			ApiErrorResponse.reportError(INTERNAL_SERVER_ERROR, SecurityErrors.INVALID_REQUEST);
		}
		return null;
	}

	private void storeInSession(final String username, final String password, final AmpTeamMember teamMember, final User user) {
		final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		authRequest.setDetails(new WebAuthenticationDetails(this.httpRequest));
		SecurityContextHolder.getContext().setAuthentication(authRequest);
		final HttpSession session = this.httpRequest.getSession();
		PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
		if(teamMember != null) {
			session.setAttribute(Constants.CURRENT_MEMBER, teamMember.toTeamMember());
		}
		session.setAttribute("ampAdmin", user.isGlobalAdmin() ? "yes": "no");
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
		ApiAuthorization.authorize(containerReq);
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
