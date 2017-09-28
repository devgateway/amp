package org.digijava.module.exception.action;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.swizzle.confluence.Confluence;
import org.digijava.kernel.exception.ExceptionHelper;
import org.digijava.kernel.exception.ExceptionInfo;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.user.User;

/**
 *
 * @author Arty
 * 
 */
public final class GetConfluenceDocs
    extends Action {

    private static final int MAX_RESULTS = 5;
    private static Logger logger = Logger.getLogger(GetConfluenceDocs.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DigiExceptionReportForm formReport = (DigiExceptionReportForm) form;

        ExceptionInfo exceptionInfo = null;
        try {
            String rand = request.getParameter("rand");
            exceptionInfo = (ExceptionInfo) request.getSession().getAttribute(ExceptionInfo.EXCEPTION_INFO + rand);
            request.getSession().removeAttribute(ExceptionInfo.EXCEPTION_INFO + rand);
        } catch (Exception e) {
        }

        String content = "<b>No documents were found!</b>";
        LinkedList<String> tags = null;
        if (exceptionInfo != null)
            tags = exceptionInfo.getTags();
        
        if (tags != null){
            int noResults = 0;
            HashSet<String> addedDocs = new HashSet<String>();
            Iterator<String> it = tags.iterator();
            try {
                //establish the connection first
                String username = "amprpc";
                String password = ".Very.Secret.25.Pazzword;;";
                String endpoint = "http://docs.ampdev.net/confluence/rpc/xmlrpc";
                
                Confluence confluence = new Confluence(endpoint);
                confluence.login(username, password);
                
                //do the actual searching
                while (it.hasNext() && noResults < MAX_RESULTS) {
                    String tag = (String) it.next();
                    
                    try {
                        
                        List labelContent = confluence.getLabelContentByName(tag);
                        
                        Iterator it1 = labelContent.iterator();
                        if (it1.hasNext() && noResults == 0){
                            content = "<font color=\"black\"><b><ol> ";
                        }
                        while (it1.hasNext() && noResults < MAX_RESULTS) {
                            Hashtable ht = (Hashtable) it1.next();
                            
                            String title = (String) ht.get("title");
                            String url = (String) ht.get("url");
                            String excerpt = (String) ht.get("excerpt");
                            
                            if (addedDocs.contains(url))
                                continue;

                            content = content + "<li><font color=\"green\" style=\"font-weight: normal\">" +
                            "<a href=\"" + url + "\">" + title + "</a><br/>" +
                            "" + excerpt + "" +
                            "</font>" +
                            "</li>";
                            addedDocs.add(url);
                            noResults++;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                if (noResults > 0)
                    content = content + "</ol></b></font>";
                //Logout of Confluence
                confluence.logout();
                
            } catch (Exception e) {
                // Can't connect or auth
            }
            
        }
        
        
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.write(content);
        writer.flush();
        
        return null;
    }
}
