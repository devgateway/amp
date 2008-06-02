package org.digijava.module.gis.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.gis.dbentity.AmpDaColumn;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.form.TableWidgetCreationForm;
import org.digijava.module.gis.util.TableWidgetUtil;
import org.digijava.module.gis.util.TableWidgetUtil.ColumnOrderNoComparator;
import org.digijava.module.gis.util.TableWidgetUtil.TableWidgetColumnKeyResolver;


/**
 * Table widget administration dispatch action.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgets extends DispatchAction {

    private static Logger logger = Logger.getLogger(AdminTableWidgets.class);
    public static final String EDITING_WIDGET = "EditingWidget";

    /**
     * Show create table widget page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward create(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		logger.debug("randering add table widget page");

		tableForm.setId(null);
		tableForm.setName(null);
		tableForm.setCode(null);
		tableForm.setCssClass(null);
		tableForm.setHtmlStyle(null);
		tableForm.setWidth("100%");
		tableForm.setColumns(null);
		setPlaces(tableForm);
		
		try {
			startEditing(new AmpDaTable(), request);
		} catch (Exception e) {
			logger.error(e);
		}finally{
			stopEditing(request);
			startEditing(new AmpDaTable(), request);
		}
		
		return mapping.findForward("showAdd");
	}

    /**
     * Cancels edit and unmarks session with editing widget. 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	public ActionForward cancelEdit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		stopEditing(request);
		
		return mapping.findForward("listTableWidgets");
	}

	/**
	 * Starts edit process and marks session with editing widget.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward startEdit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm wForm=(TableWidgetCreationForm)form;
		
		AmpDaTable widget = TableWidgetUtil.getTableWidget(wForm.getId());
		wForm = widgetToForm(wForm, widget);
		setPlaces(wForm);
		startEditing(widget, request);

		return mapping.findForward("showEdit");
	}
    
    /**
     * Shows edit table widget page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	public ActionForward edit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm wForm=(TableWidgetCreationForm)form;
		if (wForm.getId()!=null && wForm.getId().longValue()==0){
			wForm.setId(null);
		}
		logger.debug("staring widget edit");
		//start of edit: load from db, update form and save in session
		AmpDaTable widget = getWidgetFromSession(request);
		if (widget!=null){
			formToWidget(wForm,widget);
		}
		//widgetToForm(wForm, widget);
		//setPlaces(wForm);
		return mapping.findForward("showEdit");
	}

	private TableWidgetCreationForm widgetToForm(TableWidgetCreationForm form,AmpDaTable widget) {
		form.setId(widget.getId());
		form.setName(widget.getName());
		form.setCode(widget.getCode());
		form.setCssClass(widget.getCssClass());
		form.setHtmlStyle(widget.getHtmlStyle());
		form.setWidth(widget.getWidth());
		if (widget.getColumns() == null){
			widget.setColumns(new HashSet<AmpDaColumn>());
		}
		List<AmpDaColumn> columns = new ArrayList<AmpDaColumn>(widget.getColumns());
		Collections.sort(columns,new ColumnOrderNoComparator());
		form.setColumns(columns);
		form.setSelectedPlaceCode("-1");
		//TODO temporary just setting one (first) place to form.
		if (widget.getPlaces()!=null){
			for (AmpDaWidgetPlace place : widget.getPlaces()) {
				form.setSelectedPlaceCode(place.getCode());
				break;
			}
		}
		
		return form;
	}
	
	private void setPlaces(TableWidgetCreationForm form) throws DgException{
		List<AmpDaWidgetPlace> places = TableWidgetUtil.getAllPlaces();
		List<LabelValueBean> placesBeans = new ArrayList<LabelValueBean>();
		if (places != null){
			for (AmpDaWidgetPlace place : places) {
				placesBeans.add(new LabelValueBean(place.getName(),place.getCode()));
			}
		}else{
			//TODO what should go here?
		}
		form.setPlaces(placesBeans);
	}
	
	/**
	 * Saves widget after edit or add page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward save(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		TableWidgetCreationForm wForm=(TableWidgetCreationForm)form;

		AmpDaTable dbWidget = null;
		
		Long id = wForm.getId();
		if (id==null || id.longValue()<=0){
			dbWidget = new AmpDaTable();
			logger.debug("creating new table widget");
		}else{
			logger.debug("loading table widget to update");
			dbWidget = TableWidgetUtil.getTableWidget(id);
		}
		
		//update db widgets simple fields from the action form.
		dbWidget = formToWidget(wForm, dbWidget);
		//get session widget
		AmpDaTable sessionWidget = getWidgetFromSession(request);
		//get columns from both db and session widgets to join them
		Set<AmpDaColumn> dbColumns = dbWidget.getColumns();
		if (dbColumns == null){
			dbColumns = new  HashSet<AmpDaColumn>();
			dbWidget.setColumns(dbColumns);
		}
		Set<AmpDaColumn> sessionColumns = sessionWidget.getColumns();
		if (sessionColumns == null){
			sessionColumns = new HashSet<AmpDaColumn>();
		}
		//join columns to get list of columns ready to just save. 
		AmpCollectionUtils.join(dbColumns, sessionColumns, new TableWidgetColumnKeyResolver());
		
		//correct new columns ID's to make hibernate persist them and not try update.
		for (AmpDaColumn col : dbColumns) {
			col.setWidget(dbWidget);
			if (col.getId() < 0 ){
				col.setId(null);
			}
		}
		
		//assign to the place if selected. 
		AmpDaWidgetPlace place =null;
		if ( ! "-1".equals(wForm.getSelectedPlaceCode())){
			place = TableWidgetUtil.getPlace(wForm.getSelectedPlaceCode());
			place.setWidget(dbWidget);
			if (null == dbWidget.getPlaces()){
				dbWidget.setPlaces(new HashSet<AmpDaWidgetPlace>());
			}
			dbWidget.getPlaces().add(place);
		}
 		
		//save or update widget with columns.
		TableWidgetUtil.saveOrUpdateWidget(dbWidget, place);
		
		stopEditing(request);
		
		logger.debug("Table widget saved");
		
		return mapping.findForward("listTableWidgets");
	}
	
	/**
	 * Removes table widget from database.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		logger.debug("deleting table widget with id="+tableForm.getId());
		TableWidgetUtil.deleteWidget(tableForm.getId());
		logger.debug("table widget with id="+tableForm.getId()+" has been deleted");
		
		return mapping.findForward("listTableWidgets");
	}

	/**
	 * Renders table widgets list page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		Collection<AmpDaTable> tables=TableWidgetUtil.getAllTableWidgets();
		tableForm.setTables(tables);
		
		return mapping.findForward("forward");
	}
	
	public ActionForward details(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		TableWidgetUtil.deleteWidget(tableForm.getId());
		
		return mapping.findForward("forward");
	}

	/**
	 * If action type is not specified
	 */
	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//just do list
		return list(mapping, form, request, response);
	}
	
	//=========Widget Session Handling=========================
	/**
	 * Retrieves widget from session.
	 * @param request
	 * @return
	 * @throws DgException if there is no widget in current session. Do not call this method when not in edit mode.
	 */
	private AmpDaTable getWidgetFromSession(HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		AmpDaTable result=(AmpDaTable)session.getAttribute(EDITING_WIDGET);
		if (result==null) throw new DgException("No widget in editing state");
		return result;
	}
	
	/**
	 * Puts widget in session.
	 * @param widget
	 * @param request
	 * @throws DgException
	 */
	private void saveToSession(AmpDaTable widget,HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		session.setAttribute(EDITING_WIDGET, widget);
	}
	
	/**
	 * Saves widget in session marking it as being in edit mode.
	 * @param widget
	 * @param request
	 * @throws DgException if already in editing state. first it should be ended. 
	 */
	private void startEditing(AmpDaTable widget, HttpServletRequest request) throws DgException{
		if (isEditing(request)) throw new DgException("some widget is already in edit state");
		saveToSession(widget, request);
	}
	
	
	/**
	 * Stops editing mode by removing widget from session.
	 * @param request
	 * @throws DgException if not in editing mode. should be in editing mode to stop this mode.
	 */
	private void stopEditing(HttpServletRequest request)  throws DgException{
		HttpSession session=request.getSession();
		AmpDaTable old=(AmpDaTable)session.getAttribute(EDITING_WIDGET);
		if (old==null) throw new DgException("No widget is in edit state");
		session.removeAttribute(EDITING_WIDGET);
	}
	
	/**
	 * Checks whether in edit mode or not.
	 * If there is an widget in session then we are in edit mode.
	 * @param request
	 * @return
	 * @throws DgException
	 */
	private boolean isEditing(HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		return null!=session.getAttribute(EDITING_WIDGET);
	}
	
	/**
	 * Creates widget and fills all simple properties of new widget from action form.
	 * @param form
	 * @return
	 * @throws DgException
	 */
	private AmpDaTable formToWidget(TableWidgetCreationForm form) throws DgException{
		return formToWidget(form, new AmpDaTable());
	}
	
	/**
	 * Fills all simple properties of widget from action form
	 * @param form
	 * @param widget
	 * @return
	 * @throws DgException
	 */
	private AmpDaTable formToWidget(TableWidgetCreationForm form, AmpDaTable widget) throws DgException{
		
		widget.setName(form.getName());
		widget.setCode(form.getCode());
		widget.setCssClass(form.getCssClass());
		widget.setHtmlStyle(form.getHtmlStyle());
		widget.setWidth(form.getWidth());
		
		return widget;
	}

	/**
	 * Creates new column and fills simple simple properties from action form
	 * @param form
	 * @return
	 * @throws DgException
	 */
	private AmpDaColumn formToColumn(TableWidgetCreationForm form) throws DgException {
		return formToColumn(form,new AmpDaColumn());
	}
	
	/**
	 * Fills column's simple properties from action form.
	 * @param form
	 * @param column
	 * @return
	 * @throws DgException
	 */
	private AmpDaColumn formToColumn(TableWidgetCreationForm form,AmpDaColumn column) throws DgException {
		column.setId(form.getColId());
		column.setName(form.getColName());
		column.setCode(form.getColCode());
		column.setCssClass(form.getCssClass());
		column.setHtmlStyle(form.getColHtmlStyle());
		column.setPattern(form.getColPattern());
		column.setWidth(form.getColWidth());
		return column;
	}
	
	private Collection<LabelValueBean> getColumnTypes() throws DgException{
		
		return null;
	}
	
	
	//============Column actions===============

	/**
	 * Render add column popup.
	 * Fills form with required data.
	 */
	public ActionForward showColumnPopup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;
		tableForm.setColCode(null);
		tableForm.setColCssClass(null);
		tableForm.setColHtmlStyle(null);
		tableForm.setColId(null);
		tableForm.setColName(null);
		tableForm.setColPattern(null);
		tableForm.setColumnTypes(getColumnTypes());
		tableForm.setColWidth(null);
		
		return mapping.findForward("showAddColumnPopup");
	}
	
	/**
	 * Adds new column to edited widget.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addColumn(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;
		//retrieve widget from session
		AmpDaTable sessionWidget=getWidgetFromSession(request);
		//update session widget with new data from submitted form
		sessionWidget=formToWidget(tableForm,sessionWidget);
		
		AmpDaColumn newColumn = formToColumn(tableForm);
		newColumn.setId(-System.currentTimeMillis());
		newColumn.setWidget(sessionWidget);
		
		//add new column to session widget
		if (sessionWidget.getColumns()==null) sessionWidget.setColumns(new HashSet<AmpDaColumn>());
		newColumn.setOrderNo(sessionWidget.getColumns().size());
		sessionWidget.getColumns().add(newColumn);
		//add new column to form column collection
		if (tableForm.getColumns()==null) tableForm.setColumns(new ArrayList<AmpDaColumn>());
		tableForm.getColumns().add(newColumn);
		Collections.sort(tableForm.getColumns(),new ColumnOrderNoComparator());
		//TODO no need for save? test
		saveToSession(sessionWidget, request);
		return mapping.findForward("returnToEdit");
	}

	/**
	 * Removes column from edited widget
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeColumn(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;
		//retrieve widget from session
		AmpDaTable table=getWidgetFromSession(request);
		//update session widget with new data from submitted form
		table=formToWidget(tableForm,table);
		
		Set<AmpDaColumn> sesColumns=table.getColumns();
		Iterator<AmpDaColumn> colIter = sesColumns.iterator();
		while (colIter.hasNext()) {
			AmpDaColumn col = colIter.next();
			if (col.getId().equals(tableForm.getColId())){
				colIter.remove();
				break;
			}
		}
		
		List<AmpDaColumn> columnList = new ArrayList<AmpDaColumn>(sesColumns);
		Collections.sort(columnList,new ColumnOrderNoComparator());
		int c=0;
		for (AmpDaColumn col : columnList) {
			col.setOrderNo(c++);
		}
		tableForm.setColumns(columnList);
		
		System.out.println(table.getCode());
		return mapping.findForward("returnToEdit");
	}
	
}
