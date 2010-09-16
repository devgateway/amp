package org.digijava.module.widget.action;

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
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.form.TableWidgetCreationForm;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.util.TableWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.digijava.module.widget.util.TableWidgetUtil.ColumnOrderNoComparator;
import org.digijava.module.widget.util.TableWidgetUtil.TableWidgetColumnKeyResolver;


/**
 * Table widget administration dispatch action.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgets extends DispatchAction {

    private static Logger logger = Logger.getLogger(AdminTableWidgets.class);
    public static final String EDITING_WIDGET = "EditingWidget";
    public static final String EDITING_COLUMNS = "EditingColumns";
    public static final String DELETING_COLUMNS = "DeletingColumns";
    public static final String EDITING_WIDGET_PLACES = "EditingWidgetPlaces";

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
		tableForm.setNameAsTitle(null);
		tableForm.setShouldClose(false);
		tableForm.setCode(null);
		tableForm.setCssClass(null);
		tableForm.setHtmlStyle(null);
		tableForm.setWidth(null);
		tableForm.setColumns(null);
		setPlaces(tableForm);
		
		try {
			startEditing(new AmpDaTable(), null,new ArrayList<AmpDaColumn>(), request);
		} catch (Exception e) {
			logger.error(e);
		}finally{
			stopEditing(request);
			startEditing(new AmpDaTable(),null, new ArrayList<AmpDaColumn>(), request);
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
		wForm.setColumns(getWidgetColumnsSorted(widget));
		setPlaces(wForm);
		try{
			startEditing(widget,wForm.getSelPlaces(),wForm.getColumns(), request);
		}catch (Exception e) {
			stopEditing(request);
			startEditing(widget,wForm.getSelPlaces(),wForm.getColumns(), request);
		}

		return mapping.findForward("returnToEdit");
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
		setPlaces(wForm);
		AmpDaTable widget = getWidgetFromSession(request);
        Long[] selPlaces=getWidgetPlacesSession(request);
		if (wForm.getId()!=null && wForm.getId().longValue()==0){
			wForm.setId(null);
		}
		//widget = formToWidgetSimpleFileds(wForm,widget);
		wForm = widgetToForm(wForm, widget);
        wForm.setSelPlaces(selPlaces);
		//set columns
		List<AmpDaColumn> columns = getClumnsFromSession(request);
		Collections.sort(columns,new TableWidgetUtil.ColumnOrderNoComparator());
		wForm.setColumns(columns);
		return mapping.findForward("showEdit");
	}

	/**
	 * This method just saves changes in fields when page is refreshed.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward redraw(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetCreationForm wForm=(TableWidgetCreationForm)form;
		AmpDaTable widget = getWidgetFromSession(request);
        saveToSession(wForm.getSelPlaces(), request);
		widget = formToWidgetSimpleFileds(wForm,widget);
		return mapping.findForward("returnToEdit");
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

		AmpDaTable dbWidget = getWidgetFromSession(request);
		dbWidget = formToWidgetSimpleFileds(wForm);
		List<AmpDaColumn> newColumns = getClumnsFromSession(request);
		for (AmpDaColumn column : newColumns) {
			column.setWidget(dbWidget);
			if (column.getId().longValue() <=0){
				column.setId(null);
			}
		}
		Set<AmpDaColumn> columns = new HashSet<AmpDaColumn>(newColumns);
		dbWidget.setColumns(columns);
		
		List<AmpDaColumn> deletedColumns = getDeletedColumns(request);
		if (dbWidget.getId()!=null && dbWidget.getId().longValue()<=0){
			dbWidget.setId(null);
		}
		TableWidgetUtil.saveOrUpdateWidget(dbWidget,deletedColumns);
		
		
		//==PLACES==
		//first NULL old placess
		List<AmpDaWidgetPlace> oldPlaces = WidgetUtil.getWidgetPlaces(dbWidget.getId());
		if (oldPlaces!=null && oldPlaces.size()>0){
			for (AmpDaWidgetPlace oldPlace : oldPlaces) {
				oldPlace.setAssignedWidget(null);
			}
			WidgetUtil.savePlaces(oldPlaces);
		}
		
		//Then update selected places
		Long[] selPlaceIDs = wForm.getSelPlaces();
		if (selPlaceIDs!=null && selPlaceIDs.length>0){
			List<AmpDaWidgetPlace> selectedPlaces = WidgetUtil.getPlacesWithIDs(selPlaceIDs);
			for (AmpDaWidgetPlace place : selectedPlaces) {
				place.setAssignedWidget(dbWidget);
			}
			WidgetUtil.savePlaces(selectedPlaces);
		}
		
		stopEditing(request);
		
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
	
	/**
	 * Not Used yet....
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward details(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		TableWidgetUtil.deleteWidget(tableForm.getId());
		
		return mapping.findForward("forward");
	}

	/**
	 * Move column up or forward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward reorderUp(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		AmpDaTable widget = getWidgetFromSession(request);
		widget = formToWidgetSimpleFileds(tableForm,widget);
		if (widget!=null){
			
			formToWidgetSimpleFileds(tableForm, widget);
			
			List<AmpDaColumn> columns = getClumnsFromSession(request);
			AmpDaColumn colCur=null;
			AmpDaColumn colPrev=null;
			if (columns!=null){
				Iterator<AmpDaColumn> colIter = columns.iterator();
				while(colIter.hasNext()){
					colPrev = colCur;
					colCur = colIter.next();
					
					//find column that was clicked.
					if (colCur.getId().equals(tableForm.getColId())){
						break;
					}
				}
				if(colCur!=null && colPrev!=null){
					//just replace order numbers and ...
					Integer tmp = colCur.getOrderNo();
					colCur.setOrderNo(colPrev.getOrderNo());
					colPrev.setOrderNo(tmp);
				}
			}
		}
			
		// ... and forward to show edit, this will order columns and will update form with session widget.
		return mapping.findForward("returnToEdit");
	}
	
	/**
	 * Moves column down or backward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward reorderDown(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		TableWidgetCreationForm tableForm=(TableWidgetCreationForm)form;

		AmpDaTable widget = getWidgetFromSession(request);
		widget = formToWidgetSimpleFileds(tableForm,widget);
		if (widget!=null){
			
			formToWidgetSimpleFileds(tableForm, widget);
			
			List<AmpDaColumn> columns = getClumnsFromSession(request);
			AmpDaColumn col1=null;
			AmpDaColumn col2=null;
			if (columns!=null){
				Iterator<AmpDaColumn> colIter = columns.iterator();
				while(col1 == null  && col2 == null && colIter.hasNext()){
					AmpDaColumn col = colIter.next();
					//find column that was clicked.
					if (col.getId().equals(tableForm.getColId())){
						col1=col;
						//and the next one
						col2=colIter.next();
					}
				}
				if(col1!=null && col2!=null){
					//just replace order numbers and ...
					Integer tmp = col1.getOrderNo();
					col1.setOrderNo(col2.getOrderNo());
					col2.setOrderNo(tmp);
				}
			}
		}
			
		// ... and forward to show edit, this will order columns and will update form with session widget.
		return mapping.findForward("returnToEdit");
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

	/**
	 * Moves all simple data from widget to action form.
	 * Note that columns are not moved by this method.
	 * @param form
	 * @param widget
	 * @return
	 */
	private TableWidgetCreationForm widgetToForm(TableWidgetCreationForm form,AmpDaTable widget) throws DgException {
		form.setId(widget.getId());
		form.setName(widget.getName());
		form.setNameAsTitle(widget.getNameAsTitle());
		form.setCode(widget.getCode());
		form.setCssClass(widget.getCssClass());
		form.setHtmlStyle(widget.getHtmlStyle());
		form.setWidth(widget.getWidth());
		if (widget.getId()!=null && widget.getId()>0){
			Long[] selectedPlaces = null;
			List<AmpDaWidgetPlace> widgetPlaces=WidgetUtil.getWidgetPlaces(widget.getId());
			if (widgetPlaces!=null){
				selectedPlaces = new Long[widgetPlaces.size()];
				int c=0;
				for (AmpDaWidgetPlace place : widgetPlaces) {
					selectedPlaces[c++]=place.getId();
				}
			}
			form.setSelPlaces(selectedPlaces);
		}
		
		return form;
	}

	/**
	 * sets places label-value beans collection to form.
	 * @param form
	 * @throws DgException
	 */
	private void setPlaces(TableWidgetCreationForm form) throws DgException{
		List<AmpDaWidgetPlace> places = WidgetUtil.getAllPlaces();
		Collections.sort(places,new org.digijava.module.widget.table.util.TableWidgetUtil.AmpDaWidgetPlaceNameComparator());
		List<LabelValueBean> placesBeans = new ArrayList<LabelValueBean>();
		if (places != null){
			for (AmpDaWidgetPlace place : places) {
				placesBeans.add(new LabelValueBean(place.getName(),place.getId().toString()));
			}
		}else{
			//TODO what should go here?
		}
		form.setPlaces(placesBeans);
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
	 * Retrieves widget place from session.
	 * @param request
	 * @return
	 * @throws DgException if there is no widget in current session. Do not call this method when not in edit mode.
	 */
	private Long[]  getWidgetPlacesSession(HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		Long[] selPlaces=(Long[])session.getAttribute(EDITING_WIDGET_PLACES);
		return selPlaces;
	}

	/**
	 * Retrieves columns list saved in session.
	 * @param request
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	private List<AmpDaColumn> getClumnsFromSession(HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		List<AmpDaColumn> result=(List<AmpDaColumn>)session.getAttribute(EDITING_COLUMNS);
		if (result==null) throw new DgException("No columns in editing state");
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
	 * Save columns to session.
	 * @param columns
	 * @param request
	 * @throws DgException
	 */
	private void saveToSession(List<AmpDaColumn> columns,HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		session.setAttribute(EDITING_COLUMNS, columns);
	}

	/**
	 * Save places to session.
	 * @param columns
	 * @param request
	 * @throws DgException
	 */
	private void saveToSession(Long[] places,HttpServletRequest request) throws DgException{
		HttpSession session=request.getSession();
		session.setAttribute(EDITING_WIDGET_PLACES, places);
	}
	
	/**
	 * Saves widget in session marking it as being in edit mode.
	 * @param widget
	 * @param request
	 * @throws DgException if already in editing state. first it should be ended. 
	 */
	private void startEditing(AmpDaTable widget,Long[]places, List<AmpDaColumn> columns, HttpServletRequest request) throws DgException{
		if (isEditing(request)) throw new DgException("some widget is already in edit state");
		saveToSession(widget, request);
        saveToSession(places, request);
		saveToSession(columns, request); 
		resetDeletedColumns(request);
	}

	/**
	 * Stops editing mode by removing widget from session.
	 * Also removes columns and deleted columns from session.
	 * @param request
	 * @throws DgException if not in editing mode. should be in editing mode to stop this mode.
	 */
	private void stopEditing(HttpServletRequest request)  throws DgException{
		HttpSession session=request.getSession();
		AmpDaTable old=(AmpDaTable)session.getAttribute(EDITING_WIDGET);
		if (old==null) throw new DgException("No widget is in edit state");
		session.removeAttribute(EDITING_WIDGET);
		session.removeAttribute(EDITING_COLUMNS);
		resetDeletedColumns(request);
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
	private AmpDaTable formToWidgetSimpleFileds(TableWidgetCreationForm form) throws DgException{
		return formToWidgetSimpleFileds(form, new AmpDaTable());
	}
	
	/**
	 * Fills all simple properties of widget from action form
	 * @param form
	 * @param widget
	 * @return
	 * @throws DgException
	 */
	private AmpDaTable formToWidgetSimpleFileds(TableWidgetCreationForm form, AmpDaTable widget) throws DgException{
		widget.setId(form.getId());
		widget.setName(form.getName());
		widget.setNameAsTitle((form.getNameAsTitle()==null)?false:form.getNameAsTitle());
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
		if (form.getColSelectedType().longValue()==2||form.getColSelectedType().longValue()==3){
			return formToColumn(form,new AmpDaColumnFilter());
		}
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
		if (column instanceof AmpDaColumnFilter){
			AmpDaColumnFilter filter = (AmpDaColumnFilter) column;
			filter.setColumnType(WiColumn.FILTER);
			filter.setFilterItemProvider(form.getColSelectedType()-1);//TODO refactor !
			return filter;
		}
		column.setColumnType(WiColumn.STANDARD);
		return column;
	}
	
	/**
	 * Retrieves widget columns set, converts to list and sorts by order no.
	 * @param widget
	 * @return sorted columns by order no.
	 */
	private List<AmpDaColumn> getWidgetColumnsSorted(AmpDaTable widget) {
		if (widget.getColumns() == null){
			widget.setColumns(new HashSet<AmpDaColumn>());
		}
		List<AmpDaColumn> columns = new ArrayList<AmpDaColumn>(widget.getColumns());
		Collections.sort(columns,new ColumnOrderNoComparator());
		return columns;
	}
	
	/**
	 * Returns label-value beans for avaliable types of the columns.
	 * @return
	 * @throws DgException
	 */
	private Collection<LabelValueBean> getColumnTypes() throws DgException{
		ArrayList<LabelValueBean> result = new ArrayList<LabelValueBean>(2);
		//TODO temporary solution, improve this.
		result.add(new LabelValueBean("Standard","1"));
		result.add(new LabelValueBean("Dropdown Filter - Donors","2"));
                result.add(new LabelValueBean("Organization Groups Filter - Organization Groups(excluding government)","3"));
		return result;
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
		tableForm.setColSelectedType(null);
		tableForm.setColumnTypes(getColumnTypes());
		tableForm.setColWidth(null);
		tableForm.setShouldClose(false);
		tableForm.setColColumnEdit(false);
		return mapping.findForward("showAddColumnPopup");
	}

	/**
	 * Show column edit dialog.
	 * Only name can be edited.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showColumnEditPopup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TableWidgetCreationForm cForm=(TableWidgetCreationForm)form;
		List<AmpDaColumn> columns = getClumnsFromSession(request);
		AmpDaColumn col = AmpCollectionUtils.searchByKey(columns, cForm.getColId(), new TableWidgetColumnKeyResolver());
		cForm.setColId(col.getId());
		cForm.setColName(col.getName());
		cForm.setColCode(col.getCode());
		cForm.setColCssClass(col.getCssClass());
		cForm.setHtmlStyle(col.getHtmlStyle());
		cForm.setColWidth(col.getWidth());
		cForm.setColPattern(col.getPattern());
		cForm.setColColumnEdit(true);
		cForm.setColumnTypes(getColumnTypes());
		cForm.setColSelectedType(null);
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
		if(tableForm.getColColumnEdit()!=null && tableForm.getColColumnEdit().booleanValue()){
			return editColumn(mapping, form, request, response);
		}
		//retrieve widget from session
		AmpDaTable sessionWidget=getWidgetFromSession(request);
		//retrieve columns from session
		List<AmpDaColumn> columns = getClumnsFromSession(request);
		widgetToForm(tableForm, sessionWidget);
		//update session widget with new data from submitted form
		//sessionWidget=formToWidgetSimpleFileds(tableForm,sessionWidget);
		
		//Create new column
		AmpDaColumn newColumn = formToColumn(tableForm);
		newColumn.setId(-System.currentTimeMillis());
		newColumn.setWidget(sessionWidget);
		newColumn.setOrderNo(columns.size());

		//add to list
		columns.add(newColumn);
		//sort
		Collections.sort(columns,new ColumnOrderNoComparator());
		tableForm.setShouldClose(true);
//		return mapping.findForward("showAddColumnPopup");
		return mapping.findForward("returnToEdit");
	}

	/**
	 * Stores column changes in session.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editColumn(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TableWidgetCreationForm tForm=(TableWidgetCreationForm)form;
		//retrieve widget from session
		AmpDaTable sessionWidget=getWidgetFromSession(request);
		//retrieve columns from session
		List<AmpDaColumn> columns = getClumnsFromSession(request);
		widgetToForm(tForm, sessionWidget);
		//find column we want to edit
		AmpDaColumn column = AmpCollectionUtils.searchByKey(columns, tForm.getColId(), new TableWidgetColumnKeyResolver());
		//change field
		column.setName(tForm.getColName());
		column.setCode(tForm.getColCode());
		column.setCssClass(tForm.getColCssClass());
		column.setHtmlStyle(tForm.getColHtmlStyle());
		column.setPattern(tForm.getColPattern());
		column.setWidth(tForm.getColWidth());
		//return to edit
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
		//retrieve columns from session
		List<AmpDaColumn> columns = getClumnsFromSession(request);
		
		//update session widget with new data from submitted form
		table=formToWidgetSimpleFileds(tableForm,table);
		
		Iterator<AmpDaColumn> colIter = columns.iterator();
		while (colIter.hasNext()) {
			AmpDaColumn col = colIter.next();
			if (col.getId().equals(tableForm.getColId())){
				colIter.remove();
				addDeletedColumn(col, request);
				break;
			}
		}
		
		//sort with one missing orderNo,
		Collections.sort(columns,new ColumnOrderNoComparator());
		//and renumber to not have missing orderNo's
		int c=0;
		for (AmpDaColumn col : columns) {
			col.setOrderNo(c++);
		}
		
		return mapping.findForward("returnToEdit");
	}

	private static void resetDeletedColumns(HttpServletRequest request){
		HttpSession session=request.getSession();
		session.removeAttribute(DELETING_COLUMNS);
	}
	@SuppressWarnings("unchecked")
	private static List<AmpDaColumn> getDeletedColumns(HttpServletRequest request){
		HttpSession session=request.getSession();
		return (List<AmpDaColumn>)session.getAttribute(DELETING_COLUMNS);
	}
	@SuppressWarnings("unchecked")
	private static void addDeletedColumn(AmpDaColumn col,HttpServletRequest request){
		HttpSession session=request.getSession();
		List<AmpDaColumn> deletedColumns = (List<AmpDaColumn>)session.getAttribute(DELETING_COLUMNS);
		if (deletedColumns == null){
			deletedColumns = new ArrayList<AmpDaColumn>();
			session.setAttribute(DELETING_COLUMNS, deletedColumns);
		}
		deletedColumns.add(col);
	}
}
