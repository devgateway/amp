package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.form.AdminTableWidgetDataForm;
import org.digijava.module.widget.table.DaRow;
import org.digijava.module.widget.table.DaTable;
import org.digijava.module.widget.util.TableWidgetUtil;

/**
 * Action for editing table widget data.
 * No changes are done in db until {@link #save(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} action is called.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgetData extends DispatchAction {

	public static final String EDITING_WIDGET_DATA = "Editin_Widget_Data";

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
		if ( ! isEdit(request)){
			return startEdit(mapping, form, request, response);
		}
		return mapping.findForward("forward");
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
		
		AmpDaTable dTable = TableWidgetUtil.getTableWidget(dForm.getWidgetId());
		dForm.setTableName(dTable.getName());
		
		List<AmpDaColumn> columns = new ArrayList<AmpDaColumn>(dTable.getColumns());
		dForm.setColumns(columns);
		
		List<AmpDaValue> values = TableWidgetUtil.getTableData(dForm.getWidgetId());
		List<DaRow> rows = TableWidgetUtil.valuesToRows(values);
		if(rows==null){
			rows = new ArrayList<DaRow>(1);
		}
		if(rows.size()==0){
			rows.add(TableWidgetUtil.createEmptyRow(columns));
		}
		
		dForm.setRows(rows);
		DaTable table = new DaTable(dTable);
		table.generateHtml();
		markEditStarted(request, rows);
		
		return mapping.findForward("forward");
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
			
			List<DaRow> rows = dForm.getRows();
			DaRow newRow = TableWidgetUtil.createEmptyRow(dForm.getColumns());
			rows.add(insertIndex, newRow);
			updateSession(request, rows);
		}		
		return mapping.findForward("forward");
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
			List<DaRow> rows = dForm.getRows();
			rows.remove(removeIndex);
			updateSession(request, rows);
		}	
		
		return mapping.findForward("forward");
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
		AmpDaTable widget = TableWidgetUtil.getTableWidget(dForm.getWidgetId());
		List<DaRow> rows = dForm.getRows();
		List<AmpDaValue> newValues = TableWidgetUtil.rowsToValues(rows,widget.getColumns());
		List<AmpDaValue> oldValues = TableWidgetUtil.getTableData(dForm.getWidgetId());
		AmpCollectionUtils.joinAndMarkRemoved(oldValues, newValues, new TableWidgetUtil.AmpDaValueKeyWorker());
		TableWidgetUtil.saveTableValues(oldValues);
		markEditStopped(request);
		return mapping.findForward("listTableWidgets");
	}
	
	/**
	 * Not very useful after redesign this action.
	 * @param request
	 * @param rows
	 */
	private void markEditStarted(HttpServletRequest request,List<DaRow> rows){
		updateSession(request, rows);
	}
	private void markEditStopped(HttpServletRequest request){
		request.getSession().removeAttribute(EDITING_WIDGET_DATA);
	}
	private boolean isEdit(HttpServletRequest request){
		return null!=getFromSession(request);
	}
	private List<DaRow> getFromSession(HttpServletRequest request){
		return (List<DaRow>)request.getSession().getAttribute(EDITING_WIDGET_DATA);
	}
	private void updateSession(HttpServletRequest request,List<DaRow> rows){
		request.getSession().setAttribute(EDITING_WIDGET_DATA, rows);
	}
}
