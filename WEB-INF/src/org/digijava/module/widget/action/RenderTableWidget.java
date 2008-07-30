package org.digijava.module.widget.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.widget.form.TableWidgetTeaserForm;
import org.digijava.module.widget.table.WiTable;

/**
 * Renders table widget teaser.
 * @author Irakli Kobiashvili
 *
 */
public class RenderTableWidget extends Action {

    private static Logger logger = Logger.getLogger(RenderTableWidget.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetTeaserForm fTable=(TableWidgetTeaserForm)form;
		logger.debug("Building table Widget id = "+fTable.getTableId());
		
		Long tableId = fTable.getTableId();
		WiTable table = new WiTable.TableBuilder(tableId).build();
		String html = table.generateHtml();	
		response.getOutputStream().print(html);
		response.getOutputStream().close();
		return null;
	}
	
	
}
