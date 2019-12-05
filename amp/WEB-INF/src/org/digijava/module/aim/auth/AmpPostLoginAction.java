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
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
        
        Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        try {
            currentUser = getUser(authResult);
        } catch(DgException ex) {
            throw new RuntimeException(ex);
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
