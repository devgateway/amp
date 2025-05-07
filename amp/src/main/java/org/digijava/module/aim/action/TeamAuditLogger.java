package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.form.TeamAuditForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AuditLoggerUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TeamAuditLogger extends MultiAction {
    
    private static Logger logger = Logger.getLogger(TeamAuditLogger.class);
    
    private ServletContext ampContext = null;
    
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        boolean permitted = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;
                    }
                }
            }
        }
        if (!permitted) {
            return mapping.findForward("index");
        }
        String teamname = null;
        if (session.getAttribute("currentMember") != null) {
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            teamname = tm.getTeamName();
        }
        
        TeamAuditForm vForm = (TeamAuditForm) form;
        
        Collection<AmpAuditLogger> logs=AuditLoggerUtil.getTeamLogObjects(teamname);
        
        if (request.getParameter("sortBy")!=null){
            vForm.setSortBy(request.getParameter("sortBy"));
        }
        if(vForm.getSortBy() == null){
            vForm.setSortBy("changedatedesc");
        }
        if("nameasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerNameComparator()) ;
        }
        else if("namedesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerNameComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("typeasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTypeComparator());
        }
        else if("typedesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTypeComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("teamasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTeamComparator());
        }
        else if("teamdesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTeamComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("authorasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerAuthorComparator());
        }
        else if("authordesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerAuthorComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("creationdateasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerCreationDateComparator());
        }
        else if("creationdatedesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerCreationDateComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("editorasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerEditorNameComparator());
        }
        else if("editordesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerEditorNameComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("actionasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerActionComparator());
        }
        else if("actiondesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerActionComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }
        else if("changedateasc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerActionComparator());
        }
        else if("changedatedesc".equalsIgnoreCase(vForm.getSortBy())){
          Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerActionComparator());
          Collections.reverse((List<AmpAuditLogger>)logs);
        }

        vForm.setPagesToShow(10);
        int totalrecords=20;
        int page = 0;
        if (request.getParameter("page") == null) {
            page = 1;
        } else {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int stIndex = ((page - 1) * totalrecords) + 1;
        int edIndex = page * totalrecords;
        Collection tempCol = new ArrayList();
        AmpAuditLogger[] tmplogs = (AmpAuditLogger[])logs.toArray(new AmpAuditLogger[0]);
        for (int i = (stIndex - 1); i < edIndex; i++) {
            if (logs.size() > i){
                tempCol.add(tmplogs[i]);
            }
            else{
                break;
            }
         }
        
        Collection pages = null;
        int numpages;
        numpages = logs.size() / totalrecords;
        numpages += (logs.size()  % totalrecords != 0) ? 1 : 0;
        
        if ((numpages) >= 1) {
            pages = new ArrayList();
            for (int i = 0; i < (numpages); i++) {
              Integer pageNum = new Integer(i + 1);
              pages.add(pageNum);
            }
         }else{
             pages = new ArrayList<AmpAuditLogger>();
         }
          
        vForm.setPages(pages);  
        vForm.setCurrentPage(new Integer(page));
        vForm.setLogs(tempCol);
        vForm.setPagesSize(pages.size());
        
        return  modeSelect(mapping, form, request, response);
    }

    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        //return modeNew(mapping, form, request, response);
        if(request.getParameter("action")!=null)
            {
//              if(request.getParameter("action").compareTo("add")==0) return modeAddTemplate(mapping, form, request, response);
            }
        return mapping.findForward("forward");
    }
    
}
