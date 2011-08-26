package org.digijava.module.widget.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.collections.CollectionSynchronizer;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.oldTable.DaCell;
import org.digijava.module.widget.oldTable.DaRow;
import org.digijava.module.widget.oldTable.DaTable;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiRow;
import org.digijava.module.widget.table.WiTable;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Utilities for table widgets,
 * @author Irakli Kobiashvili
 *
 */
public class TableWidgetUtil {

	/**
	 * Used tostore table widget in session during edit and preview actions.
	 */
	public static final String EDITING_WIDGET_TABLE = "Editing_Widget_Table";
	
    private static Logger logger = Logger.getLogger(TableWidgetUtil.class);
	
    /**
     * Returns ALL tables widgets.
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
	public static Collection<AmpDaTable> getAllTableWidgets() throws DgException{
    	Session session=PersistenceManager.getRequestDBSession();
    	Collection<AmpDaTable> result=null;
    	try {
			Query query=session.createQuery("from "+AmpDaTable.class.getName());
			List list=query.list();
			result=list;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * Lads table widget by ID.
     * @param id
     * @return
     * @throws DgException
     */
    public static AmpDaTable getTableWidget(Long id) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		AmpDaTable result=null;
		try {
			result=(AmpDaTable)session.load(AmpDaTable.class, id);
		} catch (Exception e) {
			throw new DgException("Cannot get Table Widget!",e);
		}
    	return result;
    }
    
    /**
     * Creates new widget in db.
     * @param widget
     * @throws DgException
     */
	public static void createWidget(AmpDaTable widget) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
//beginTransaction();
			session.save(widget);
			//tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget creation!",e1);
				}
			}
			throw new DgException("Cannot create Table Widget!",e);
		}
	}
	
	/**
	 * Updates already existing widget in db.
	 * @param widget
	 * @throws DgException
	 */
	public static void updateWidget(AmpDaTable widget) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
//beginTransaction();
			session.update(widget);
			//tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget update!",e1);
				}
			}
			throw new DgException("Cannot update Table Widget!",e);
		}
	}

	/**
	 * Saves or updates widget and also place if specified.
	 * @param widget table widget together with columns. 
	 * @throws DgException
	 */
	public static void saveOrUpdateWidget(AmpDaTable widget,List<AmpDaColumn> deletedColumns) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
//beginTransaction();
			if (deletedColumns!= null && deletedColumns.size()>0){
				for (AmpDaColumn col : deletedColumns) {
					session.delete(col);
				}
			}
			session.saveOrUpdate(widget);
			//tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget save or update!",e1);
				}
			}
			throw new DgException("Cannot save or update Table Widget!",e);
		}
	}
	
	/**
	 * Removes widget from db.
	 * @param widget
	 * @throws DgException
	 */
	public static void deleteWidget(AmpDaTable widget) throws DgException{
		//first unassign from all places this widget was assigned.
		List<AmpDaWidgetPlace> places = getWidgetPlaces(widget.getId());
		if (null != places){
			WidgetUtil.updatePlacesWithWidget(places, null);
		}
		//then execute widget specific delete operations.
		//TODO it would be better if those two action could go in one transaction.
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			List<AmpDaValue> values = getTableData(widget.getId());
//beginTransaction();
			if (null != values){
				for (AmpDaValue value : values) {
					session.delete(value);
				}
			}
			session.delete(widget);
			//tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget delete!",e1);
				}
			}
			throw new DgException("Cannot delete Table Widget!",e);
		}
		
	}
	
	/**
	 * Removes widget from db.
	 * @param id
	 * @throws DgException
	 */
	public static void deleteWidget(Long id) throws DgException{
		AmpDaTable widget=getTableWidget(id);
		deleteWidget(widget);
	}

	//=======Data=======================
	
	@SuppressWarnings("unchecked")
	public static List<AmpDaValue> getTableData(Long tableId)throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		List<AmpDaValue> results=null;
		String oql="select v from ";
		oql += AmpDaValue.class.getName()+" as v, ";
		oql += AmpDaColumn.class.getName()+ " as c, ";
		oql += AmpDaTable.class.getName()+ " as t ";
		oql += " where ";
		oql += " v.column = c and c.widget = t and t.id=:tableId";
		oql += " order by v.pk";
		try {
			Query query=session.createQuery(oql);
			query.setLong("tableId", tableId);
			results = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DgException("Error loading table data for id=",e);
		}
		return results;
	}
	
	/**
	 * Converts values to helper row beans which include cells with those values.
	 * @param data
	 * @return
	 * @throws DgException
	 */
	public static List<DaRow> valuesToRows(Collection<AmpDaValue> data)throws DgException{
		List<DaRow> rows=null;
		Map<Long, DaRow> rowsByPk=new HashMap<Long, DaRow>();
		for (AmpDaValue value : data) {
			DaRow row=rowsByPk.get(value.getPk());
			if (null == row){
				row = new DaRow();
				rowsByPk.put(value.getPk(), row);
				row.setPk(value.getPk());
				row.setCells(new ArrayList<DaCell>());
			}
			DaCell cell = new DaCell(value);
			row.getCells().add(cell);
		}
		rows = new ArrayList<DaRow>(rowsByPk.values());
		for (DaRow daRow : rows) {
			Collections.sort(daRow.getCells(),new CellOrderNoComparator());
		}
		return rows;
	}

	public static List<DaRow> valuesToRows(Collection<AmpDaColumn> columns, Collection<AmpDaValue> data)throws DgException{
		List<DaRow> rows=null;
		Map<Long, DaRow> rowsByPk=new HashMap<Long, DaRow>();
		
		for (AmpDaValue value : data) {
			DaRow row=rowsByPk.get(value.getPk());
			if (null == row){
				row = new DaRow();
				rowsByPk.put(value.getPk(), row);
				row.setPk(value.getPk());
				row.setCells(new ArrayList<DaCell>());
			}
			DaCell cell = new DaCell(value);
			row.getCells().add(cell);
		}
		rows = new ArrayList<DaRow>(rowsByPk.values());
		for (DaRow daRow : rows) {
			Collections.sort(daRow.getCells(),new CellOrderNoComparator());
		}
		return rows;
	}
	
	/**
	 * Creates row with empty values according to columns.
	 * @param columns
	 * @return
	 * @throws DgException
	 * @Deprecated {@link WiTable} should create new empty row when asked, so all these logic is encapsulated in table widget
	 */
	@Deprecated
	public static WiRow createEmptyRow(List<WiColumn> columns) throws DgException{
		WiRow row = null;
//		for (WiColumn col : columns) {
//			WiCell cell = org.digijava.module.widget.table.util.TableWidgetUtil.newCell(value);
//			row.updateCell(cell);
//		}
		return row;
	}
	
	/**
	 * Converts row helper beans and their cells back to values beans for DB.
	 * This will also update pk field for each value bean according their row number.
	 * @param rows list of rows to convert
	 * @return list of value bean created from all cells of all rows.
	 * @throws DgException
	 */
	public static List<AmpDaValue> rowsToValues(List<DaRow> rows,Set<AmpDaColumn> columns) throws DgException{
		List<AmpDaValue> resultValues = new ArrayList<AmpDaValue>(columns.size() * rows.size());
		Map<Long,AmpDaColumn> columnMap = AmpCollectionUtils.createMap(columns, new TableWidgetUtil.TableWidgetColumnKeyResolver());
		long pk=0;
		for (DaRow row : rows) {
			row.setPk(new Long(pk++));
			List<AmpDaValue> values=row.getValues(columnMap);
			resultValues.addAll(values);
		}
		return resultValues;
	}

	/**
	 * Inserts, updates or deletes value beans depending on their ID.
	 * If ID is null then value is inserted, if ID is negative then value is deleted 
	 * and if ID is positive then value is updated. 
	 * @param values
	 * @throws DgException
	 */
	public static void saveTableValues(List<AmpDaValue> values)throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
//beginTransaction();
			for (AmpDaValue value : values) {
				if (value.getId()!=null){
					if(value.getId().longValue()<0){
						value.setId(new Long( - value.getId().longValue()));
						AmpDaValue val = (AmpDaValue)session.load(AmpDaValue.class, value.getId());
						session.delete(val);
					}else{
						AmpDaValue val = (AmpDaValue)session.load(AmpDaValue.class, value.getId());
						val.replaceValues(value);
						session.update(val);
					}
				} else{
					session.save(value);
				}
			}
			//tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new DgException("Cannot rollback values save,update,delete operations!",e1);
				}
			}
			e.printStackTrace();
			throw new DgException("Cannot save,update,delete values!",e);
		}
		
	}
	
	//=======PLACES=====================
	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getWidgetPlaces(Long id) throws DgException{
		List<AmpDaWidgetPlace> places = null;
		String oql = "select p from "+AmpDaWidgetPlace.class.getName()+" as p where p.assignedWidget = :widId";
		Session session = PersistenceManager.getRequestDBSession();
		try {
			Query query = session.createQuery(oql);
			query.setLong("widId", id);
			places = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DgException("Cannot load widget places!",e);
		}
		return places;
	}
	
	//=======session====================

	
	public static DaTable getTableHelperFromSession(HttpServletRequest request)throws DgException{
		
		return null;
	}
	
	//=======COLUMNS====================

	/**
	 * Returns table columns with ID.
	 * @param id
	 * @return
	 * @throws DgException
	 */
	public static AmpDaColumn getColumn(Long id)throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		AmpDaColumn result=null;
		try {
			result=(AmpDaColumn)session.load(AmpDaColumn.class, id);
		} catch (Exception e) {
			throw new DgException("Cannot get Column Widget!",e);
		}
    	return result;
	}
	
	
	public static void markEditStopped(HttpServletRequest request){
		request.getSession().removeAttribute(EDITING_WIDGET_TABLE);
	}
	public static boolean isEdit(HttpServletRequest request){
		return null!=getFromSession(request);
	}

	public static WiTable getFromSession(HttpServletRequest request){
		return (WiTable)request.getSession().getAttribute(EDITING_WIDGET_TABLE);
	}
	public static void updateSession(HttpServletRequest request,WiTable table){
		request.getSession().setAttribute(EDITING_WIDGET_TABLE, table);
	}

	
	//=======key resolvers==============
	public static class TableWidgetKeyResolver implements KeyResolver<Long, AmpDaTable>{
		public Long resolveKey(AmpDaTable element) {
			return element.getId();
		}
	}
	public static class TableWidgetColumnKeyResolver implements KeyResolver<Long, AmpDaColumn>{
		public Long resolveKey(AmpDaColumn element) {
			return element.getId();
		}
	}
	
	public static class WidgetPlaceKeyResolver implements KeyResolver<Long, AmpDaWidgetPlace>{
		public Long resolveKey(AmpDaWidgetPlace element) {
			return element.getId();
		}
		
	}
	
	public static class AmpDaValueKeyWorker implements KeyWorker<Long, AmpDaValue>{
		public Long resolveKey(AmpDaValue value) {
			return value.getId();
		}

		public void markKeyForRemoval(AmpDaValue element) {
			element.setId(new Long( - element.getId().longValue()));
		}

		public void updateKey(AmpDaValue element, Long newKey) {
			element.setId(newKey);
		}
		
	}
	
	//========comparators=============
	/**
	 * Compares {@link AmpDaColumn} with its orderNo field.
	 *
	 */
	public static class ColumnOrderNoComparator implements Comparator<AmpDaColumn>{

		public int compare(AmpDaColumn col1, AmpDaColumn col2) {
			return col1.getOrderNo().compareTo(col2.getOrderNo());
		}
	}

	public static class RowPkComparator implements Comparator<DaRow>{
		public int compare(DaRow r1, DaRow r2) {
			return r1.getPk().compareTo(r2.getPk());
		}
	}
	
	/**
	 * Compares DaCell helpers by its column order number.
	 *
	 */
	public static class CellOrderNoComparator implements Comparator<DaCell>{
		public int compare(DaCell c1, DaCell c2) {
			return c1.getColumnOrderNo().compareTo(c2.getColumnOrderNo());
		}
	}
	
	//not used yet. will reimplement in AmpCollectionUtils with java generics.
	public static class ColumnSynzchronizer implements CollectionSynchronizer{

		public boolean removeEvent(Object arg0) {
			return false;
		}

		public boolean synchronizeEvent(Object arg0, Object arg1) {
			return false;
		}

		public int compare(Object arg0, Object arg1) {
			return 0;
		}
		
	}

	public static DaTable widgetToHelper(AmpDaTable widget) throws DgException{
		DaTable table=null;
		table = new DaTable(widget);
		return table;
	}

	public Long resolveKey(AmpDaValue element) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
