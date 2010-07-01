package org.digijava.module.help.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.GlossaryForm;
import org.digijava.module.help.helper.HelpTopicTreeNode;
import org.digijava.module.help.util.GlossaryUtil;
import org.digijava.module.help.util.HelpTopicTreeNodeWorker;

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
		String siteId = RequestUtils.getSite(request).getSiteId();
		
		source = GlossaryUtil.getAllGlosaryTopics(moduleInstnce, siteId);
		
		HelpTopicTreeNodeWorker worker = new HelpTopicTreeNodeWorker();
		
		List<HelpTopicTreeNode> tree = AmpCollectionUtils.createTree(source, worker);
		
		glossForm.setTree(tree);
		return mapping.findForward("forward");
	}
	
	
}
