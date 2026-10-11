/**
 * 
 */
package org.digijava.module.aim.auth;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.security.ApiAuthentication;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.security.SecurityService;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.AuditLoggerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

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

        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");

        SecurityService securityService = SecurityService.getInstance();
        User currentUser = securityService.verifyCredentials(username, password);
        if (currentUser == null) {
            out.println(getJsonResponse(toLoginWidgetErrorCode(SecurityErrors.INVALID_USER_PASSWORD)));
            return null;
        }

        ApiErrorMessage res = ApiAuthentication.performSecurityChecks(currentUser, request);
        if (res != null) {
            out.println(getJsonResponse(toLoginWidgetErrorCode(res)));
        } else {
            securityService.invalidateExistingSession();
            securityService.storeInSession(username, null, currentUser);
            // re-apply: invalidateExistingSession() discarded the session it was written to above
            request.getSession().setAttribute("j_autoWorkspaceId", id);
            AuditLoggerUtil.logUserLogin(request, currentUser, Constants.LOGIN_ACTION);
            out.println(getJsonResponse("noError", null));
        }

        return null;
    }

    /**
     * The login widget's JavaScript (digest-auth.js) matches "original_result" against fixed short codes
     * (e.g. "userBanned") rather than the translatable {@link ApiErrorMessage#description}, so it must be
     * translated back here. Any other string ends up mishandled by that script.
     */
    private String toLoginWidgetErrorCode(ApiErrorMessage res) {
        if (SecurityErrors.USER_BANNED.id.equals(res.id)) {
            return "userBanned";
        }
        if (SecurityErrors.NO_TEAM.id.equals(res.id)) {
            return "noTeamMember";
        }
        if (SecurityErrors.USER_SUSPENDED.id.equals(res.id)) {
            StringBuilder code = new StringBuilder("userSuspended");
            if (res.getValues() != null) {
                for (String reason : res.getValues()) {
                    code.append('{').append(reason).append('}');
                }
            }
            return code.toString();
        }
        return "invalidUser";
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
    private String getJsonResponse(String originalMessage, String newMessage) {
        String json="{ "+  
                "\"original_result\":\""+ originalMessage +"\" ";
        if(newMessage!=null){
            json+=","+newMessage;
        }
        json+="}"; 
        return json;
    }
}
