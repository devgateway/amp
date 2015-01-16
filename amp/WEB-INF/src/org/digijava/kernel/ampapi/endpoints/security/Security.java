package org.digijava.kernel.ampapi.endpoints.security;

import javax.security.auth.Subject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.ampapi.endpoints.common.Filters;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

/**
 * This class should have all security / permisions related methods
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("security")
public class Security {
	private static final Logger logger = Logger.getLogger(Security.class);

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
	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "LoggedUser", name = "Logged user")
	public JsonBean getUser() {
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);

		JsonBean user = new JsonBean();
		if (tm != null) {
			Site site = RequestUtils.getSite(TLSUtils.getRequest());
			User u = UserUtils.getUser(tm.getMemberId());
			Subject subject = UserUtils.getUserSubject(u);

			boolean siteAdmin = DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN);
			boolean badmin = DgSecurityManager.permitted(subject, site, ResourcePermission.INT_ADMIN);
			user.set("email", u.getEmail());
			user.set("firstName", u.getFirstNames());
			user.set("lastName", u.getLastName());
			user.set("administratorMode", badmin);
			if (!badmin) {
				AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());

				if (ampTeamMember.getAmpTeam() != null) { 
					user.set("workspace", ampTeamMember.getAmpTeam().getDescription());
					user.set("workspaceId", ampTeamMember.getAmpTeam().getIdentifier());
				}
				
			} else {
				return user;
			}
		} else {
			user.set("email", null);
		}
		return user;
	}

}
