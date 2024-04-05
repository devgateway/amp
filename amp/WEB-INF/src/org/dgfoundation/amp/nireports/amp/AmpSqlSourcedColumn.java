package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.kernel.request.TLSUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * a column which fetches its input from a view, row-by-row, filtered by mainId 
 * The way a row is interpreted as a {@link Cell} is left for the subclasses to define (see {@link #extractCell(NiReportsEngine, ResultSet)})
 * @author Dolghier Constantin
 *
 */
public abstract class AmpSqlSourcedColumn<K extends Cell> extends PsqlSourcedColumn<K> {

    private boolean sscEnabledColumn;

    public AmpSqlSourcedColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName,
            Behaviour<?> behaviour, boolean sscEnabledColumn) {
        super(columnName, levelColumn, viewName, behaviour);
        this.sscEnabledColumn = sscEnabledColumn;
    }
    
    @Override
    public List<K> fetch(NiReportsEngine engine) {
        return fetch(engine, engine.schemaSpecificScratchpad.getMainIds(engine, this));
    }
    
    public List<K> fetch(NiReportsEngine engine, Set<Long> mainIds) {
        if (mainIds.isEmpty())
            return Collections.emptyList();
        String locale = TLSUtils.getEffectiveLangCode();
        String viewName = this.viewName;
        if (sscEnabledColumn && inSSCWorkspace()) {
            viewName = viewName.replace("v_", "v_ssc_");
        }
        String queryCondition = String.format("WHERE (%s IN (%s))", mainColumn, Util.toCSStringForIN(mainIds));
        ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView(viewName, queryCondition, locale, AmpReportsScratchpad.get(engine).columnCachers, AmpReportsScratchpad.get(engine).connection, this.viewColumns.toArray(new String[0]));
        List<K> res = new ArrayList<>();
        try(RsInfo rs = fetcher.fetch(null)) {
            while (rs.rs.next()) {
                K cell = extractCell(engine, rs.rs);
                if (cell != null)
                    res.add(cell);
            }
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
        return res;
    }

    public boolean inSSCWorkspace() {
        return TLSUtils.getThreadLocalInstance().inSSCWorkspace();
    }

    /**
     * extracts a cell from the current row of an SQL {@link ResultSet}
     * @param engine the context
     * @param rs the source of the raw SQL data
     * @return the fetched Cell or null. Null is a signal that the current row contains data which does not encode a valid {@link Cell}, but the error is not bad enough to mandate a crash.
     * @throws SQLException
     */
    protected abstract K extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException;
}
