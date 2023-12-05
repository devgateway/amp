package org.digijava.module.help.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.GlossaryForm;
import org.digijava.module.help.helper.HelpTopicTreeNode;
import org.digijava.module.help.util.GlossaryUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Does search for glossary and return ajax results.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class SearchGlossary extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        GlossaryForm gform = (GlossaryForm)form;
        
        String term = gform.getSearchTerm();
        String moduleInstance = RequestUtils.getModuleInstance(request).getInstanceName();
        Site site = RequestUtils.getSite(request);
//      String siteIdNo = RequestUtils.getSite(request).getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();
        
        List<String> terms = null;
        if (term != null){
            String[] arr = term.split(" ");
            List<String> list = Arrays.asList(arr);
            terms = list;
        }
        List<HelpTopic> helpTopics = GlossaryUtil.searchGlossary(terms, moduleInstance, site, locale);
        
        String result = TranslatorWorker.translateText("Nothing found");
        
        if (helpTopics!=null && helpTopics.size()>0){
            StringBuffer buf = new StringBuffer();
            for (HelpTopic helpTopic : helpTopics) {
                HelpTopicTreeNode node = new HelpTopicTreeNode(helpTopic, site, locale);
                buf.append(node.getSearchResultLink());
            }
            result = buf.toString();
        }
        
        //setup response for UTF-8
        response.setContentType("text/html");
        OutputStreamWriter outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
        PrintWriter out = new PrintWriter(outputStream, true);
        //write to response and close.
        out.println(result);
        out.close();
        
        return null;
    }

    
}
