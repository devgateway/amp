package org.digijava.module.help.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.GlossaryForm;
import org.digijava.module.help.helper.HelpTopicTreeNode;
import org.digijava.module.help.util.GlossaryUtil;
import org.digijava.module.help.util.HelpTopicTreeNodeWorker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows glossary page.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class ShowGlossary extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        GlossaryForm glossForm = (GlossaryForm)form;

        List<HelpTopic> source = new ArrayList<HelpTopic>();
        
        String moduleInstnce = RequestUtils.getModuleInstance(request).getInstanceName();
        Site site = RequestUtils.getSite(request);
        //String siteIdNo = RequestUtils.getSite(request).getId().toString();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();
        
        source = GlossaryUtil.getAllGlosaryTopics(moduleInstnce, site);             //Flat list of all glossary items.
        HelpTopicTreeNodeWorker worker = new HelpTopicTreeNodeWorker(site,locale);  //Node worker to build tree
        List<HelpTopicTreeNode> tree = AmpCollectionUtils.createTree(source, worker);   //Build tree
        
        glossForm.setTree(tree);
        return mapping.findForward("forward");
    }
    
    
}
