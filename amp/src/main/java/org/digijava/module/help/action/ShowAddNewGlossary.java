package org.digijava.module.help.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.GlossaryForm;
import org.digijava.module.help.util.HelpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renders add glossary page
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class ShowAddNewGlossary extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        GlossaryForm gform = (GlossaryForm)form;
        Long nodeId = gform.getNodeId();
        if (nodeId!=null){
            HelpTopic topic = HelpUtil.getHelpTopic(nodeId);
            if (topic != null){
                gform.setNodeParentName(topic.getTopicKey());
            }
        }else{
            gform.setNodeParentName(null);      
        }
        gform.setParentNodeId(nodeId);
        return mapping.findForward("forward");
    }

    
}
