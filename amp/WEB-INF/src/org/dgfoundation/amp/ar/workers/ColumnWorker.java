/**
 * ColumnWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.filtercacher.FilterCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.budgetexport.util.MappingEncoder;


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

    protected FilterCacher filterCacher;
    
    protected ResultSetMetaData rsmd;
    
    protected HashMap<String,String> columnsMetaData;
    
    
    protected boolean extractor;

    protected static Logger logger = Logger.getLogger(ColumnWorker.class);

    
    public ColumnWorker(String condition, String viewName,String columnName, ReportGenerator generator) {
        this.condition = condition;
        this.columnName = columnName;
        this.viewName = viewName;
        this.sourceGroup = null;
        extractor = true;
        this.generator = generator;
        this.filterCacher = generator.getFilterCacher();
    }   
    
    public ColumnWorker(String destName, GroupColumn source, ReportGenerator generator) {
        this.columnName = destName;
        this.sourceGroup = source;
        extractor = false;
        this.generator = generator;
        this.filterCacher = generator.getFilterCacher();
    }
    
    public Column<Cell> populateCellColumn() {
        Column<Cell> c = null;
        if(extractor) c = extractCellColumn();
            else c = generateCellColumn();
        c.setWorker(this);
        c.setDescription(this.getRelatedColumn() == null ? null : this.getRelatedColumn().getDescription());
        this.cleanup();
        return c;
    }
    
    /**
     * called after column extraction is done to cleanup any stale used memory
     */
    protected void cleanup()
    {
        
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
        CellColumn dest = null;
        
        Column<Cell> sourceCol = sourceGroup.getColumn(sourceName);
        dest = newColumnInstance(sourceCol.getItems().size());        
        for (Cell element:sourceCol.getItems()) {
            Cell destCell = getCellFromCell(element);
            if (destCell != null) dest.addCell(destCell);
        }
        return dest;
    }
    
    protected FilterCacher getFilterCacher(){
        return this.filterCacher;
    }
    
    public void setFilterCacher(FilterCacher filterCacher) {
        this.filterCacher = filterCacher;
    }
    
    protected Column extractCellColumn() {
        
        FilterCacher filterCacher = getFilterCacher();
        
        Connection conn = filterCacher.getConnection();
        
        CellColumn cc = null;
        
        String queryCondition = "WHERE " + filterCacher.getPrimaryKeyName() + " IN (" + filterCacher.rewriteFilterQuery(condition) + " ) "+(internalCondition!=null ? internalCondition:"");
        String queryView = viewName;
        
        //PreparedStatement ps;
    
        if (debugMode){
            //if debug override the query
             queryView = "TEST_" + viewName;
             queryCondition = "";
        }
        
        try {
            // public static DatabaseViewFetcher getFetcherForView(String viewName, String condition, String locale, java.util.Map<InternationalizedPropertyDescription, ColumnValuesCacher> cachers, Connection connection, String... columnNames)
            String locale = TLSUtils.getEffectiveLangCode();
            
            ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView(queryView, queryCondition, locale, generator.getColumnCachers(), conn, "*");            
            RsInfo rs = fetcher.fetch(new ArrayList<>());
            
            rsmd = rs.rs.getMetaData();
            
            //Set parameters to query
            //generator.getFilter().getYearFrom();
            
            int colsCount = rsmd.getColumnCount()+1;
            
            
            columnsMetaData=new HashMap<String,String>();
            
            for (int i = 1; i < colsCount; i++){
                columnsMetaData.put(rsmd.getColumnLabel(i).toLowerCase(), rsmd.getColumnName(i).toLowerCase());
            }
                    
            cc = newColumnInstance(1000); // don't waste time counting nr of rows - it is more expensive than just reallocating an ArrayList in-mem
            cc.setExtractorView(this.getViewName());
            
            /* CachedRowSet -> ResultSet change argumentation: 
            /* results:
              * org.digijava.kernel.request.RequestProcessor.processActionPerform() goes down from 6900 to 5200ms
              * org.dgfoundation.amp.ar.AmpReportGenerator.retrieveData(): from 5200ms to 3500ms
              * org.dgfoundation.amp.ar.workers.ColumnWorker.extractCellColumn() 5200ms to 3500ms
              */
            while (rs.rs.next())
            {
                Cell c = getCellFromRow(rs.rs);
                if (c != null) {
                    cc.addCell(c);
                }
            }

            rs.close();

        } /*catch (SQLException e) {
        logger.error(String.format("Unable to complete extraction for column %s. Master query was run on view %s, condition was", columnName, viewName, queryCondition));
        logger.error(e);
        e.printStackTrace();
        }*/ 
        catch (Exception e) {
        //e.printStackTrace();
            throw new RuntimeException("error fetching column " + columnName + " from view " + viewName, e);
        //logger.error("Error fetching column", e);
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
    
    public String encodeUnallocatedString(String originalString) {
        return originalString;
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
        // nothing
    }
}
