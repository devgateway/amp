package org.digijava.module.help.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.util.GlossaryUtil;
import org.digijava.module.help.util.HelpUtil;

/**
 * Gets help topic body and returns as HTML.
 * Used for ajax calls.
 * This may not be safe if bad user gets access to help topic edit.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class GetHelpBodyHtml extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        HelpForm hform = (HelpForm) form;
        
        Long topicId = hform.getHelpTopicId();
        //Load topic by ID
        HelpTopic topic = HelpUtil.getHelpTopic(topicId);
        //get current site
        Site site = RequestUtils.getSite(request);
        //get current language
        String language = RequestUtils.getNavigationLanguage(request).getCode();
        //get editor key
        String editorKey = topic.getBodyEditKey();
        if (editorKey == null){
            String moduleInstance = RequestUtils.getModuleInstance(request).getInstanceName();
            editorKey="glossary-" + moduleInstance + "-"+request.getSession().getId().hashCode() + "-" + String.valueOf(System.currentTimeMillis());
            topic.setBodyEditKey(editorKey);
            GlossaryUtil.createOrUpdateGlossaryTopic(topic, request);
        }
        //get body for help topic
        String body = DbUtil.getEditorBodyEmptyInclude(site, editorKey, language);
        if (body==null){
            body="No text. Please edit and enter text.";
            Editor editor = new Editor();
            editor.setBody(body);
            editor.setEditorKey(editorKey);
            editor.setLanguage(language);
            editor.setLastModDate(new Date());
            editor.setSite(site);
            editor.setTitle(editorKey);
            editor.setUser(RequestUtils.getUser(request));
            DbUtil.saveEditor(editor);
        }

        //setup response for UTF-8
        response.setContentType("text/html");
        OutputStreamWriter outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
        PrintWriter out = new PrintWriter(outputStream, true);
        //write o responce and close.
        out.println(body);
        out.close();
        
        return null;
    }

    
}
