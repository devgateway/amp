package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * Returns XML of the tree of programs.
 * Currently used in NPD page.
 * This action creates hierarchy from all programs found in DB.
 * Members of the hierarchy are objects of TreeItem class which extends HierarchyMember from kernel 
 * by adding XML functionality - every item can return its own XML. 
 * Complete XML is composed using this parts.
 * @author Irakli Kobiashvili - ikobiashvili@picktek.com
 * @see org.digijava.module.aim.helper.TreeItem
 * @see org.digijava.kernel.util.collections.HierarchyMember
 *
 */
public class GetProgramTreeNode extends DispatchAction {

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return doWork(mapping, form, request, response);
	}

	private ActionForward doWork(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml");

		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();

			// get All themes from DB
			List themes = ProgramUtil.getAllThemes(true);

			// Construct XML tree
			String xml = ProgramUtil.getThemesHierarchyXML(themes);

			// return xml
			outputStream.println(xml);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (outputStream != null) {
				try {
					outputStream.println("Error retriving tree: "
							+ e.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

}
