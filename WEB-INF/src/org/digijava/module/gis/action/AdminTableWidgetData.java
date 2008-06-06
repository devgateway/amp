package org.digijava.module.gis.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.gis.dbentity.AmpDaColumn;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaValue;
import org.digijava.module.gis.form.AdminTableWidgetDataForm;
import org.digijava.module.gis.util.TableWidgetUtil;
import org.digijava.module.gis.widget.table.DaCell;
import org.digijava.module.gis.widget.table.DaRow;

public class AdminTableWidgetData extends DispatchAction {

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return this.showEdit(mapping, form, request, response);
	}


	public ActionForward showEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		if ( ! isEdit(request)){
			return startEdit(mapping, form, request, response);
		}
		
		String[][] sesMatrix=getFromSession(request);
		dForm.setMatrix(sesMatrix);
		
		return mapping.findForward("forward");
	}
	
	public ActionForward startEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		
		AmpDaTable dTable = TableWidgetUtil.getTableWidget(dForm.getWidgetId());
		dForm.setTableName(dTable.getName());
		
		List<AmpDaColumn> columns = new ArrayList<AmpDaColumn>(dTable.getColumns());
		dForm.setColumns(columns);
		
		List<AmpDaValue> data = TableWidgetUtil.getTableData(dForm.getWidgetId());
		List<DaRow> rows = TableWidgetUtil.dataToHelpers(data);
		
		String[][] result=result=rowsToArray(rows);
		editStart(request, result);
		
		dForm.setMatrix(result);
		return mapping.findForward("forward");
	}
	
	public ActionForward cancelEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		
		editStop(request);
		return mapping.findForward("listTableWidgets");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		
		editStop(request);
		return mapping.findForward("listTableWidgets");
	}

	public ActionForward addRow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		//updateSession(request, dForm.getMatrix());
		if (null != dForm.getRowIndex() && dForm.getRowIndex()>=0){
			int insertIndex=dForm.getRowIndex().intValue()+1;

			String[][] sesMatrix = getFromSession(request);

			String[][] result	= new String[sesMatrix.length+1][sesMatrix[0].length];
			
			System.arraycopy(sesMatrix, 0, result, 0, insertIndex);
			System.arraycopy(sesMatrix, insertIndex, result, insertIndex+1, sesMatrix.length-insertIndex);
			result[insertIndex]=setupEmptyColumn(sesMatrix[0].length);
			
			updateSession(request, result);
			dForm.setMatrix(result);
		}		
		return mapping.findForward("forward");
	}

	public ActionForward removeRow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminTableWidgetDataForm dForm = (AdminTableWidgetDataForm) form;
		if (null != dForm.getRowIndex() && dForm.getRowIndex()>=0){
			int removeIndex = dForm.getRowIndex().intValue();
			String[][] sesMatrix = getFromSession(request);
		}	
		
		return mapping.findForward("forward");
	}

	private String[] setupEmptyColumn(int numOfColumns){
		String[] result=new String[numOfColumns];
		for (int i=0;i<numOfColumns;i++) {
			result[i]="";
		}
		return result;
	}
	
	private String[][] rowsToArray(List<DaRow> rows){
		String[][] result = null;

		if (null != rows){
			int i=0;
			for (DaRow row : rows) {
				int j=0;
				if (null == result){
					result = new String[rows.size()][row.getCells().size()];
				}
				for (DaCell cell : row.getCells()) {
					result[i][j++] = cell.getValue();
				}
				i++;
			}
		}
		
		return result;
	}
	
	public static final String EDITING_WIDGET_DATA = "Editin_Widget_Data";
	private void editStart(HttpServletRequest request,String[][] matrix){
		updateSession(request, matrix);
	}
	private void editStop(HttpServletRequest request){
		request.getSession().removeAttribute(EDITING_WIDGET_DATA);
	}
	private boolean isEdit(HttpServletRequest request){
		return null!=getFromSession(request);
	}
	private String[][] getFromSession(HttpServletRequest request){
		return (String[][])request.getSession().getAttribute(EDITING_WIDGET_DATA);
	}
	private void updateSession(HttpServletRequest request,String[][] matrix){
		request.getSession().setAttribute(EDITING_WIDGET_DATA, matrix);
	}
}
