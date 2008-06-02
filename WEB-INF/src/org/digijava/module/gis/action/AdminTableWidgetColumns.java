package org.digijava.module.gis.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.gis.dbentity.AmpDaColumn;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.form.WidgetColumnCreationForm;
import org.digijava.module.gis.util.TableWidgetUtil;

public class AdminTableWidgetColumns extends DispatchAction {

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		WidgetColumnCreationForm colForm=(WidgetColumnCreationForm)form;
		
		AmpDaTable table=TableWidgetUtil.getTableWidget(colForm.getWidgetId());
		
		Collection<AmpDaColumn> columns= table.getColumns(); 
		
		colForm.setColumns(columns);
		
		return mapping.findForward("forward");
	}
	
}
