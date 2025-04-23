package org.digijava.module.help.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.GlossaryForm;
import org.digijava.module.help.util.GlossaryUtil;
import org.digijava.module.help.util.HelpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds new glossary topic to db
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class AddGlossaryTopic extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        GlossaryForm gform = (GlossaryForm)form;
        String nodeName = gform.getNodeName();
        Long parentId = gform.getParentNodeId();
        String moduleInstance = RequestUtils.getModuleInstance(request).getInstanceName();
        String siteId = RequestUtils.getSite(request).getSiteId();
        HelpTopic parentTopic = null;
        if (parentId != null && !parentId.equals(0L)){
            parentTopic = HelpUtil.getHelpTopic(parentId);
        }
        String editorKey="glossary-" + moduleInstance + "-"+request.getSession().getId().hashCode() + "-" + String.valueOf(System.currentTimeMillis());
        HelpTopic topic = new HelpTopic();
        topic.setBodyEditKey(editorKey);
        topic.setModuleInstance(moduleInstance);
        topic.setSiteId(siteId);
        topic.setParent(parentTopic);
        topic.setTopicKey(nodeName);
        GlossaryUtil.createOrUpdateGlossaryTopic(topic, request);
        
        return mapping.findForward("forward");
    }


}
