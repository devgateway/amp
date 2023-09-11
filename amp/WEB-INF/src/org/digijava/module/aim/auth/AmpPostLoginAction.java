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
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.trubudget.util.ProjectUtil;
import org.digijava.module.um.model.TruLoginRequest;
import org.digijava.module.um.model.TruLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

import static org.digijava.module.um.util.DbUtil.*;

/**
 * @author mihai
 *
 */
public class AmpPostLoginAction extends Action {
    private static Logger logger = LoggerFactory.getLogger(AmpPostLoginAction.class);

    
    
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
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        if (currentUser.getTruBudgetEnabled()) {


            //login into TruBudget
            TruLoginRequest truLoginRequest = new TruLoginRequest();
            truLoginRequest.setApiVersion(getSettingValue(settings, "apiVersion"));
            TruLoginRequest.Data data = new TruLoginRequest.Data();
            TruLoginRequest.User user1 = new TruLoginRequest.User();
            user1.setPassword(currentUser.getEmail());
            user1.setId(currentUser.getEmail().split("@")[0]);
            data.setUser(user1);
            truLoginRequest.setData(data);
            Mono<TruLoginResponse> truResp = loginToTruBudget(truLoginRequest, settings);
            try {
                TruLoginResponse truLoginResponse = truResp.block();
                // TODO: 8/29/23 -- cache this token to be used for TruBudget requests in Login.java and AmpPostLoginAction.java
                logger.info("Trubudget login response: " + Objects.requireNonNull(truLoginResponse).getData());
                AbstractCache myCache = new EhCacheWrapper("trubudget");
                myCache.put("truBudgetToken",truLoginResponse.getData().getUser().getToken());
                myCache.put("truBudgetUser",currentUser.getEmail().split("@")[0]);
                myCache.put("truBudgetPassword",currentUser.getEmail());
            } catch (Exception e) {
                logger.info("Error during login: " + e.getMessage(), e);
            }
            ProjectUtil.createProject(null);
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
