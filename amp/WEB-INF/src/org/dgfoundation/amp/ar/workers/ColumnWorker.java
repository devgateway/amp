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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.budgetexport.util.MappingEncoder;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sun.rowset.CachedRowSetImpl;



/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 9, 2006 
 */   
public abstract class ColumnWorker {
    
   public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	
    protected AmpColumns relatedColumn; 
    
	protected String viewName;
	
	protected boolean debugMode=false;
	
	protected boolean pledgereport = false;
	
	protected String columnName;
	
	protected String sourceName;

	protected String condition;
	
	protected String internalCondition;
	
	protected GroupColumn sourceGroup;

	protected Connection conn;
	
	protected ReportGenerator generator;
	
	
	protected ResultSetMetaData rsmd;
	
	protected HashSet<String> columnsMetaData;
	
	
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
		Column c=null;
		if(extractor) c= extractCellColumn(); else
			c=generateCellColumn();
		c.setWorker(this);
		c.setDescription(this.getRelatedColumn().getDescription());
		return c;
	}
	
	/**
	 * Returns the column instance to be used by the ColumnWorker. By default, this is the regular CellColumn. Override
	 * this in subclasses if u need a different Column instance.
	 * @param initialCapacity TODO
	 * @return the new instantiated column instance. The column name is taken from the ColumnWorker's columnName property.
	 */
	public CellColumn newColumnInstance(int initialCapacity) {
		return new CellColumn(columnName,initialCapacity);
	}
	
	protected Column generateCellColumn() {
        CellColumn dest=null;
        
        Column sourceCol=sourceGroup.getColumn(sourceName);
        dest=newColumnInstance(sourceCol.getItems().size());        
		Iterator i=sourceCol.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			Cell destCell=getCellFromCell(element);
			if (destCell!=null) dest.addCell(destCell);
		}
		return dest;
	}
	
	protected Column extractCellColumn() {
		
		Session sess=null;
		Connection conn=null;
		CellColumn cc=null;
		try {
			conn=PersistenceManager.getJdbcConnection();
			
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		
		String query = "SELECT * FROM " + viewName + " WHERE amp_activity_id IN ("
				+ condition + " ) "+(internalCondition!=null?internalCondition:"");
		PreparedStatement ps;
	
		if (debugMode){
			//if debug override the query
			 query = "SELECT * FROM TEST_"+viewName;
		}else if (pledgereport){
			query = "SELECT * FROM " + viewName + " WHERE pledge_id IN ("
			+ condition + " ) "+(internalCondition!=null?internalCondition:"");
		}
		
		try {
			
			ps = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			//add params if exist
			ArrayList<FilterParam> params=generator.getFilter().getIndexedParams();
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i).getValue(),params.get(i).getSqlType());	
			}
					
			ResultSet rs = ps.executeQuery();
			rs.setFetchSize(500);
			rsmd=rs.getMetaData();
			
			//Set parameters to query
			//generator.getFilter().getYearFrom();
			
			int colsCount=rsmd.getColumnCount()+1;
			
			columnsMetaData=new HashSet<String>();
			
			for (int i=1; i < colsCount;i++){
			    columnsMetaData.add(rsmd.getColumnName(i).toLowerCase());
			}
			rs.last();
			int rsSize=rs.getRow();			
			cc = newColumnInstance(rsSize+1);
			//rs.absolute(rsSize-500);	
			rs.beforeFirst();
			
			CachedRowSetImpl crs=new CachedRowSetImpl();
			
			crs.populate(rs);
			
			rs.close();
			
			
			
			while (crs.next()) {
				Cell c=getCellFromRow(crs);
				if(c!=null) cc.addCell(c);				
			}
			
			crs.close();
			

		} catch (SQLException e) {
			logger.error("Unable to complete extraction for column "+columnName+". Master query was "+query);
			logger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error parsing date filters");
		} 
		finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return cc;
	}

	protected abstract Cell getCellFromRow(ResultSet rs) throws SQLException;
	
	protected abstract Cell getCellFromCell(Cell src);
	
	/**
	 * If this is a ColumnWorker that encodes the strings then this method needs to be overridden to return the encoder object.
	 * @return null for standard Column workers
	 */
	public MappingEncoder getEncoder () {
		return null;
	} 

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
	
	/**
	 * Creates a new cell instance of the type. Backwards reference to Cell.getWorker
	 * @return
	 */
	public abstract Cell newCellInstance();

	public AmpColumns getRelatedColumn() {
		return relatedColumn;
	}

	public void setRelatedColumn(AmpColumns relatedColumn) {
		this.relatedColumn = relatedColumn;
	}

	public String getInternalCondition() {
		return internalCondition;
	}

	public void setInternalCondition(String internalCondition) {
		this.internalCondition = internalCondition;
	}

	public ReportGenerator getGenerator() {
		return generator;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	

	public boolean ispledgereport() {
		return pledgereport;
	}

	public void setPledge(boolean pledgereport) {
		this.pledgereport = pledgereport;	
		}
	
	public void setSession(HttpSession session) {
		;
	}

}
