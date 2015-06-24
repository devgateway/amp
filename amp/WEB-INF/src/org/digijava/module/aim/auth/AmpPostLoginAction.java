/**
 * 
 */
package org.digijava.module.aim.auth;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.um.dbentity.SuspendLogin;
import org.digijava.module.um.util.UmUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author mihai
 *
 */
public class AmpPostLoginAction extends Action {
	
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		
    	String id = request.getParameter("j_autoWorkspaceId");
    	request.getSession().setAttribute("j_autoWorkspaceId", id);
    	
    	Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        try {
            currentUser = getUser(authResult);
        } catch(DgException ex) {
            throw new RuntimeException(ex);
        }
       
        
        SiteDomain siteDomain = RequestUtils.retreiveSiteDomain(request);
        Site site = siteDomain.getSite();
        Subject subject = UserUtils.getUserSubject(currentUser);
        boolean siteAdmin = DgSecurityManager.permitted(subject, site,
            ResourcePermission.INT_ADMIN);
        /*
         * if the member is part of multiple teams the below collection contains more than one element.
         * Otherwise it will have only one element.
         * The following function will return objects of type org.digijava.module.aim.dbentity.AmpTeamMember
         * The function will return null, if the user is just a site administrator or if the user is a
         * registered user but has not yet been assigned a team
         */
        Collection members = TeamMemberUtil.getTeamMembers(currentUser.getEmail());
        if(members == null || members.size() == 0) {
            if(!siteAdmin) { // user is a site Admin
                // The user is a registered user but not a team member
            	SecurityContextHolder.getContext().setAuthentication(null);
            	out.println(getJsonResponse("noTeamMember"));
            	return null;
            }
        }

        //Suspended login
        List<SuspendLogin> su = UmUtil.getUserSuspendReasons (currentUser);
        if (su != null && !su.isEmpty()) {
            if(!siteAdmin) {
                StringBuilder suReasons = new StringBuilder("userSuspended");
                for (SuspendLogin suObject : su) {
                    suReasons.append("{").append(suObject.getReasonText()).append("}");
                }
                SecurityContextHolder.getContext().setAuthentication(null);
            	out.println(getJsonResponse(suReasons.toString()));

                return null;
            }
        }


//        /*
//         * Checking user Activity settings
//         */
//        Iterator itr = members.iterator();
//        while (itr.hasNext()){
//        	AmpTeamMember member = (AmpTeamMember) itr.next();
//        	AmpApplicationSettings ampAppSettings = DbUtil.getMemberAppSettings(member.getAmpTeamMemId());
//        	//if the user hasn't got the personalized settings
//        	if (ampAppSettings == null) {
//        		SecurityContextHolder.getContext().setAuthentication(null);
//            	out.println("invalidUser");
//            	return null;
//        	}
//            	
//        }
        AuditLoggerUtil.logUserLogin(request,currentUser, Constants.LOGIN_ACTION);
        String ampApiIntegration="\"generate_token\":";
        
		if ("true".equals(request.getParameter("generateToken"))) {
			ampApiIntegration += "true";
			ampApiIntegration += ",\"callback_url\":\"" + request.getParameter("callbackUrl") + "\" ";
		} else {
			ampApiIntegration += "false";
		}

        out.println(getJsonResponse("noError",ampApiIntegration));
		return null;
	}
	
	private String getJsonResponse(String originalMessage){
		return getJsonResponse(originalMessage,null);
	}
	/**
	 * we wrap the non json response, so we don't have to refactor all the JavaScript part
	 * @param originalMessage
	 * @param newMessage
	 * @return
	 */
	private String getJsonResponse(String originalMessage,String newMessage){ 
    	String json="{ "+  
				"\"original_result\":\""+ originalMessage +"\" ";
    	if(newMessage!=null){
    		json+=","+newMessage;
    	}
    	json+="}"; 
    	return json;
	}
	
	 protected User getUser(Authentication currentAuth) throws DgException {
	        if(currentAuth == null) {
	            return null;
	        }

	        if(currentAuth.getPrincipal() == null) {
	            return null;
	        }

	        User user;
	        Object principal = currentAuth.getPrincipal();
	        if(principal instanceof Long) {
	            Long userId = (Long) principal;
	            user = UserUtils.getUser(userId);
	        } else {
	            String userName;
	            if(principal instanceof UserDetails) {
	                UserDetails userDetails = (UserDetails) principal;
	                userName = userDetails.getUsername();
	            } else {
	                userName = principal.toString();
	            }
	            user = UserUtils.getUserByEmail(userName);
	        }

	        return user;
	    }
}
