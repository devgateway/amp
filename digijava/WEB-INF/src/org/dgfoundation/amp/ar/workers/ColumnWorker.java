/**
 * ColumnWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

import net.sf.hibernate.HibernateException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 9, 2006 
 */
public abstract class ColumnWorker {
	protected String viewName;
	
	protected String columnName;
	
	protected String sourceName;

	protected String condition;
	
	protected GroupColumn sourceGroup;

	protected Connection conn;
	
	protected ReportGenerator generator;
	
	
	protected ResultSetMetaData rsmd;
	
	protected boolean extractor;

	protected static Logger logger = Logger.getLogger(ColumnWorker.class);

	public ColumnWorker(String condition,String viewName,String columnName,ReportGenerator generator) {
		this.condition = condition;
		this.columnName=columnName;
		this.viewName= viewName;
		this.sourceGroup=null;
		extractor=true;
		this.generator=generator;
	}	
	
	public ColumnWorker(String destName,GroupColumn source,ReportGenerator generator) {
		this.columnName=destName;
		this.sourceGroup=source;
		extractor=false;
		this.generator=generator;
	}
	
	public Column populateCellColumn() {
		if(extractor) return extractCellColumn(); else
			return generateCellColumn();
	}
	
	/**
	 * Returns the column intance to be used by the ColumnWorker. By default, this is the regular CellColumn. Override
	 * this in subclasses if u need a different Column instance.
	 * @return the new instantiated column instance. The column name is taken from the ColumnWorker's columnName property.
	 */
	public CellColumn newColumnInstance() {
		return new CellColumn(columnName);
	}
	
	protected Column generateCellColumn() {
        CellColumn dest=null;
        dest=newColumnInstance();
        Column sourceCol=sourceGroup.getColumn(sourceName);
		Iterator i=sourceCol.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			Cell destCell=getCellFromCell(element);
			if (destCell!=null) dest.addCell(destCell);
		}
		return dest;
	}
	
	protected Column extractCellColumn() {
		CellColumn cc=newColumnInstance();
		try {
			conn = PersistenceManager.getSession().connection();
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}

		String query = "SELECT * FROM " + viewName + " WHERE amp_activity_id IN ( "
				+ condition + " )";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);

			ResultSet rs = ps.executeQuery();
			rsmd=rs.getMetaData();
			
			
			while (rs.next()) {
				Cell c=getCellFromRow(rs);
				//logger.info("Added cell for ownerId="+c.getOwnerId());
				cc.addCell(c);				
			}
			
			rs.close();

		} catch (SQLException e) {
			logger.error("Unable to complete extraction for column "+columnName+". Master query was "+query);
			logger.error(e);
			e.printStackTrace();
		} 
		return cc;

	}

	protected abstract Cell getCellFromRow(ResultSet rs) throws SQLException;
	
	protected abstract Cell getCellFromCell(Cell src);

	/**
	 * @return Returns the logger.
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger The logger to set.
	 */
	public static void setLogger(Logger logger) {
		ColumnWorker.logger = logger;
	}

	/**
	 * @return Returns the columnName.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName The columnName to set.
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return Returns the condition.
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition The condition to set.
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return Returns the conn.
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * @param conn The conn to set.
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @return Returns the rsmd.
	 */
	public ResultSetMetaData getRsmd() {
		return rsmd;
	}

	/**
	 * @param rsmd The rsmd to set.
	 */
	public void setRsmd(ResultSetMetaData rsmd) {
		this.rsmd = rsmd;
	}

	/**
	 * @return Returns the viewName.
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName The viewName to set.
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @return Returns the extractor.
	 */
	public boolean isExtractor() {
		return extractor;
	}

	/**
	 * @param extractor The extractor to set.
	 */
	public void setExtractor(boolean extractor) {
		this.extractor = extractor;
	}

	/**
	 * @return Returns the sourceGroup.
	 */
	public Column getSourceGroup() {
		return sourceGroup;
	}


	/**
	 * @return Returns the sourceName.
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName The sourceName to set.
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @param sourceGroup The sourceGroup to set.
	 */
	public void setSourceGroup(GroupColumn sourceGroup) {
		this.sourceGroup = sourceGroup;
	}

}
