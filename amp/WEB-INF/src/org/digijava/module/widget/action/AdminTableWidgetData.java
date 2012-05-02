package org.digijava.module.widget.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.widget.form.AdminTableWidgetDataForm;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.table.filteredColumn.WiColumnDropDownFilter;
import org.digijava.module.widget.util.TableWidgetUtil;

/**
 * Action for editing table widget data.
 * No changes are done in db until {@link #save(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} action is called.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgetData extends DispatchAction {


	/**
	 * Forwards to {@link #showEdit(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return this.showEdit(mapping, form, request, response);
	}


	/**
	 * just renders edit page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if ( ! TableWidgetUtil.isEdit(request)){
			return startEdit(mapping, form, request, response);
		}
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		WiTable wTable = TableWidgetUtil.getFromSession(request);
		dForm.setColumns(wTable.getColumns());
		if (wTable.getDataRows().size()==0){
			wTable.appendNewRow();//we should have one empty row if table has no rows (data).
		}
		dForm.setRows(wTable.getDataRows());
		dForm.setTableName(wTable.getName());
		dForm.setWidgetId(wTable.getId());
		dForm.setTable(wTable);
		dForm.setData(new String[dForm.getRows().size()][dForm.getColumns().size()]);
		dForm.setTableName(wTable.getDataRows().toString());
		return mapping.findForward("forward");
	}
	
	/**
	 * Shows preview page for table widget.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward forwardToPreview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;

		WiTable wTable = TableWidgetUtil.getFromSession(request);
		dForm.setTableName(wTable.getName());
		
		
		return mapping.findForward("forwardToPreview");
	}	
	
	
	
	
	/**
	 * Start edit process.
	 * Loads data from db and renders edit page. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward startEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		
//		AmpDaTable dTable = TableWidgetUtil.getTableWidget(dForm.getWidgetId());
//		dForm.setTableName(dTable.getName());
//		List<AmpDaColumn> columns = new ArrayList<AmpDaColumn>(dTable.getColumns());
//		dForm.setColumns(columns);
//		List<AmpDaValue> values = TableWidgetUtil.getTableData(dForm.getWidgetId());
//		List<DaRow> rows = TableWidgetUtil.valuesToRows(values);
//		if(rows==null){
//			rows = new ArrayList<DaRow>(1);
//		}
//		if(rows.size()==0){
//			rows.add(TableWidgetUtil.createEmptyRow(columns));
//		}
//		dForm.setRows(rows);
		
		
		//===NEW====
		WiTable wTable = new WiTable.TableBuilder(dForm.getWidgetId()).build();
		
		markEditStarted(request, wTable);
		
		return mapping.findForward("returnToEdit");
	}
	
	/**
	 * Cancels edit process.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cancelEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("listTableWidgets");
	}

	/**
	 * Adds new empty row at specified place.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addRow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		if (null != dForm.getRowIndex() && dForm.getRowIndex()>=0){
			int insertIndex=dForm.getRowIndex().intValue()+1;
			WiTable wTable = TableWidgetUtil.getFromSession(request);
			wTable.addNewRowAt(insertIndex);
		}		
		return mapping.findForward("returnToEdit");
	}

	/**
	 * Removes specified row.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeRow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		if (null != dForm.getRowIndex() && dForm.getRowIndex()>=0){
			int removeIndex = dForm.getRowIndex().intValue();
			WiTable wTable = TableWidgetUtil.getFromSession(request);
			wTable.deleteDataRow(removeIndex);
		}	
		
		return mapping.findForward("returnToEdit");
	}

	/**
	 * Changes state of the drop down filter.
	 * Filter column and selected item in that filter both should be specified by id's. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward filterChanged(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		Long filterColumnId = dForm.getFilterColumnId();
		Long filterItemId = dForm.getSelectedFilterItemId();
		WiTable table = TableWidgetUtil.getFromSession(request);
		WiColumnDropDownFilter filterColumn = (WiColumnDropDownFilter) table.getColumnById(filterColumnId);
		filterColumn.setFilterOn(filterItemId);
		return mapping.findForward("returnToEdit");
	}

	/**
	 * Save all changes and go back to widget list.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		System.out.println(dForm.getRows());
		WiTable wTable = TableWidgetUtil.getFromSession(request);
		org.digijava.module.widget.table.util.TableWidgetUtil.saveTable(wTable);
		
		TableWidgetUtil.markEditStopped(request);
		return mapping.findForward("listTableWidgets");
	}
	
	/**
	 * Not very useful after redesign this action.
	 * @param request
	 * @param rows
	 */
	private void markEditStarted(HttpServletRequest request,WiTable table){
		TableWidgetUtil.updateSession(request, table);
	}
}
