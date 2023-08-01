package org.digijava.module.help.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.util.GlossaryUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Deletes help topic.
 * Used for ajax call
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class DeleteGlossaryTopic extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HelpForm hform = (HelpForm) form;
        //get topic by received ID
        HelpTopic topic = GlossaryUtil.getGlosaryTopic(hform.getHelpTopicId());
        //save key and siteId
        String editorKey = topic.getBodyEditKey();
        //delete topic
        GlossaryUtil.deleteGlossaryTopic(topic);
        //if no errors thrown, delete all editors for deleted topic.
        List<Editor> editors = DbUtil.getEditorList(editorKey, topic.getSite());
        if (editors!=null && editors.size()>0){
            for (Editor editor : editors) {
                DbUtil.deleteEditor(editor);
            }
        }
        return null;
    }

}
