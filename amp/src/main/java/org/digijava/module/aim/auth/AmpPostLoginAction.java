/**
 * 
 */
package org.digijava.module.aim.auth;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.security.ApiAuthentication;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.trubudget.util.TruBudgetAuthUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author mihai
 *
 */
public class AmpPostLoginAction extends Action {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmpPostLoginAction.class);
    
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

        try {
            logger.info("Starting TruBudget post-login authentication for user: " + (currentUser != null ? currentUser.getEmail() : "null"));
            TruBudgetAuthUtil.doActualTruBudgetLogin(currentUser);
            logger.info("Completed TruBudget post-login authentication attempt for user: " + (currentUser != null ? currentUser.getEmail() : "null"));
        } catch (Exception e) {
            logger.error("TruBudget post-login authentication failed for user: " + (currentUser != null ? currentUser.getEmail() : "null"), e);
        }
        ApiErrorMessage res = ApiAuthentication.login(currentUser, request);
        if(res != null) {
            out.println(getJsonResponse(res.description));
        } else {
            out.println(getJsonResponse("noError", null));
        }

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
    private String getJsonResponse(String originalMessage, String newMessage) {
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
                user = UserUtils.getUserByEmailAddress(userName);
            }

            return user;
        }
}
