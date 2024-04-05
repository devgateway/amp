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
 * Save new title of help or glossary topic.
 * Used to handle ajax call.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class SaveTopicTitle extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        GlossaryForm glossForm = (GlossaryForm)form;
        //check data
        if (glossForm.getNodeName() == null 
                || glossForm.getNodeName().trim().length() == 0
            ) throw new Exception("Node title value is null or empty string.");
        if (glossForm.getNodeId()==null) throw new Exception("Node id not specified.");
        //load topic
        HelpTopic topic = HelpUtil.getHelpTopic(glossForm.getNodeId());
        if (topic == null) throw new Exception("Topic with id "+glossForm.getNodeId()+" not exists");
        //do job
        topic.setTopicKey(glossForm.getNodeName());
        HelpUtil.saveOrUpdateHelpTopic(topic, request);
        return null;
    }

}
