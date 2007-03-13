package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.util.ProgramUtil;

public class GetProgramTreeNode extends DispatchAction {

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return doWork(mapping, form, request, response);
	}

	private ActionForward doWork(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml");
		Logger logger =  Logger.getLogger(this.getClass());
		ServletOutputStream outputStream = null;
		try {
			logger.info(  "Fetching the tree..." );
			outputStream = response.getOutputStream();

			// get All themes from DB
			List themes = ProgramUtil.getAllThemes(true);

			// Construct XML tree
			String xml = ProgramUtil.getThemesHierarchyXML(themes);

			// return xml
			outputStream.println(xml);
			outputStream.close();
			logger.info(  "Finished fetching the tree!" );
		} catch (Exception e) {
			logger.info(  "Exception occured in getting TREE" );
			e.printStackTrace();
			if (outputStream != null) {
				try {
					logger.info(  "outputstream is NOT NULL" );
					outputStream.println("Error retriving tree: "
							+ e.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else {
				logger.info(  "outputstream is NULL" );
			}
		}
		return null;
	}

}
