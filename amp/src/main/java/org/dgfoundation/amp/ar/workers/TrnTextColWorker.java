package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.cell.TrnTextCell;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.I18nDatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.action.GetReports;

public class TrnTextColWorker extends TextColWorker {

    /**
     * @param condition
     * @param viewName
     * @param columnName
     * @param generator
     */
    public TrnTextColWorker(String condition, String viewName,
            String columnName, ReportGenerator generator) {
        super(condition, viewName, columnName, generator);
        // TODO Auto-generated constructor stub
    }
    
    protected Cell getCellFromRow(ResultSet rs) throws SQLException
    {
        Long ownerId=new Long(rs.getLong(1));
        String value=rs.getString(2);
        Long id=null;
        if (rsmd.getColumnCount()>2) {
            id=new Long(rs.getLong(3));
        }

        /**
         *  UGLY HACK! TrnTextCell and TrnTextColWorker should disappear totally in some future version of AMP
         *  Because this implies changing the amp_columns table to update all celltype's from TrnTextCell to TextCell + adding ALL the involved views to the i18nviewsrepository + deleting all the involved JSPs, ExcelExporters etc etc, this is too big of a task for a dotRelease
         *  so, doing a hack which says "if value is already translated by view fetcher, do not translate it once again"
         */
        boolean valueAlreadyTranslated = DatabaseViewFetcher.isInternationalized(this.viewName, rs); 
        if (!valueAlreadyTranslated)
        {
            value = TranslatorWorker.translateText(value); 
        }
        
        TrnTextCell ret = new TrnTextCell(ownerId);
        ret.setId(id);
        ret.setValue(value);
        return ret;
    }
    
    public Cell newCellInstance() {
        TrnTextCell tx= new TrnTextCell();
        tx.setValue(ArConstants.UNALLOCATED);
        return tx;
    }

}
