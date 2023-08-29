/**
 * 
 */
package org.digijava.module.contentrepository.action;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.contentrepository.form.LabelManagerForm;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.jcrentity.LabelDAO;
import org.digijava.module.contentrepository.jcrentity.RootLabel;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.util.List;

/**
 * @author Alex Gartner
 *
 */
public class LabelManagerAction extends MultiAction {
    private static Logger logger    = Logger.getLogger(LabelManagerAction.class);

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return modeSelect(mapping, form, request, response);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String htmlView     = request.getParameter("htmlView");
        String addOrEdit    = request.getParameter("addOrEdit");
        String delete       = request.getParameter("delete");
        
        if ("true".equals(delete) ) {
            return modeDeleteLabel(mapping, form, request, response);
        }
        if ("true".equals(htmlView) )
            return modeGetHTMLLabels(mapping, form, request, response);
        if ("true".equals(addOrEdit) ) 
            return modeAddLabel(mapping, form, request, response);
        
        return modeGetJSONLabels(mapping, form, request, response);
    }
    
    public void modeReset(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LabelManagerForm lmForm = (LabelManagerForm) form;
        lmForm.setDeleteLabelUUID(null);
        lmForm.setEditLabelBackgroundColor(null);
        lmForm.setEditLabelColor(null);
        lmForm.setEditLabelName(null);
        lmForm.setEditLabelType(null);
        lmForm.setEditParentUUID(null);
        lmForm.setEditUUID(null);
    }
    
    public ActionForward modeGetJSONLabels(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        LabelDAO labelDAO       = new LabelDAO(request, false);
        List<Label> labelList   = labelDAO.getAllLabels();
        
        JsonConfig jsonConfig   = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"children", "node" });
        JSONArray jsonArray     = JSONArray.fromObject(labelList, jsonConfig);
        
        //System.out.println(jsonArray.toString());
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        PrintStream ps                      = new PrintStream( response.getOutputStream(), false, "UTF-8" );
        ps.print( jsonArray.toString() );
        
        return null;
    }
    public ActionForward modeGetHTMLLabels(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        LabelManagerForm lmForm = (LabelManagerForm) form;
        LabelDAO labelDAO       = new LabelDAO(request, false);
        RootLabel rootLabel         = labelDAO.getRootLabel();
        //we have to do an explicit call in orden to pre-populate the root with children, before 
        //rendering the tree
        List<Label> labels = rootLabel.getChildren();
        for (Label label:labels) {
            label.getChildren();
        }
        lmForm.setRootLabel(rootLabel);
        
        
        return mapping.findForward("showHTML");
    }
    
    
    
    public ActionForward modeAddLabel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        LabelManagerForm lmForm = (LabelManagerForm) form;
        
        if ( lmForm.getEditParentUUID() != null && lmForm.getEditParentUUID().length() > 0 ) {
            Node parentNode = DocumentManagerUtil.getWriteNode(lmForm.getEditParentUUID(), request);
            if ( parentNode != null ) {
                Label label     = new Label(lmForm.getEditLabelName(), lmForm.getEditLabelType(), 
                        lmForm.getEditLabelColor(), lmForm.getEditLabelBackgroundColor() );
                
                label.saveState(parentNode);
            }
            else
                logger.error("The node for a given uuid should not be null: " + lmForm.getEditParentUUID() );
        }
        
        if ( lmForm.getEditUUID() != null && lmForm.getEditUUID().length() > 0 ) {
            LabelDAO labelDAO   = new LabelDAO(request, true);
            Label label         = labelDAO.getLabel(lmForm.getEditUUID() );
            
            label.setName( lmForm.getEditLabelName() );
            label.setColor( lmForm.getEditLabelColor() );
            label.setBackgroundColor( lmForm.getEditLabelBackgroundColor() );
            
            label.saveState(null);
        }
        
        modeReset(mapping, lmForm, request, response);
        DocumentManagerUtil.logoutJcrSessions(request);
        return modeGetHTMLLabels(mapping, lmForm, request, response);
    }
    
    public ActionForward modeDeleteLabel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LabelManagerForm lmForm = (LabelManagerForm) form;
        
        if ( lmForm.getDeleteLabelUUID() != null && lmForm.getDeleteLabelUUID().length() > 0 ) {
            Boolean deleteSuccess=true;
            LabelDAO labelDAO   = new LabelDAO(request, true);
            deleteSuccess = labelDAO.deleteLabel(lmForm.getDeleteLabelUUID() );
            if(!deleteSuccess){
                ActionErrors errors = new ActionErrors();
                ActionMessage errorMsg =  new ActionMessage("error.contentrepository.labetIsLinkedWithResource");
                errors.add(ActionMessages.GLOBAL_MESSAGE, errorMsg);
                saveErrors(request, errors);
            }
            
        }       
        
        
        modeReset(mapping, lmForm, request, response);
        
        return modeGetHTMLLabels(mapping, lmForm, request, response);
    }
    
}

