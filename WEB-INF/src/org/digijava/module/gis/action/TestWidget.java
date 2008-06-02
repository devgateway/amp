package org.digijava.module.gis.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.TableWidgetTeaserForm;
import org.digijava.module.gis.widget.table.DaRow;
import org.digijava.module.gis.widget.table.DaTable;

public class TestWidget extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TableWidgetTeaserForm fTeaser=(TableWidgetTeaserForm)form;
		
		DaTable table=new DaTable();
		table.setWidth("50%");
		table.setDataRows(new ArrayList<DaRow>(10));
		for (int i = 0; i < 10; i++) {
			DaRow row=new DaRow();
			row.setId((long)i);
			row.setPk("PK"+i);
			table.getDataRows().add(row);
		}
		
		fTeaser.setTable(table);
		return mapping.findForward("forward");
	}

}
