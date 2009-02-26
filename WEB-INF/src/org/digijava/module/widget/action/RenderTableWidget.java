package org.digijava.module.widget.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.widget.form.TableWidgetTeaserForm;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.table.filteredColumn.WiColumnDropDownFilter;

/**
 * Renders table widget teaser.
 * Uses {@link WiTable} HTML generation result as output.
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
		
		Long tableId 	= fTable.getTableId();
		Long columnId 	= fTable.getColumnId();
		Long itemId 	= fTable.getItemId();
		try{
			WiTable table = new WiTable.TableBuilder(tableId).build();
			if (columnId!=null && itemId!=null && columnId.longValue()>0 && itemId.longValue()>0){
				WiColumnDropDownFilter filter = (WiColumnDropDownFilter) table.getColumnById(columnId);
				//TODO this is not correct, check why columnId and itemId are not null when table is normal table.
				if (filter!=null){
					filter.setActiveItemId(itemId);
				}
			}
			String html = table.generateHtml();	
			OutputStreamWriter outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
			PrintWriter out = new PrintWriter(outputStream, true);
			out.println(html);
			response.getOutputStream().close();
		}catch(Exception e){
			logger.error("RenderTableWidget::execute()", e);
		}
		return null;
	}
	
	
}
