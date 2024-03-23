package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.amp.PercentagesCorrector.Snapshot;
import org.dgfoundation.amp.nireports.amp.diff.TextColumnKeyBuilder;
import org.dgfoundation.amp.nireports.behaviours.PercentageTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.kernel.request.TLSUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * a column which fetches items with percentages from a view with a "nonNulls" column and associated {@link PercentagesCorrector} instance.
 * {@link PercentagesCorrector} is used for correcting dirty AMP data (e.g. where percentages for a given activity id do not add up to 100%)
 * @author Dolghier Constantin
 *
 */
public class NormalizedPercentagesColumn extends AmpDifferentialColumn<PercentageTextCell> {

    final PercentagesCorrector percentagesCorrector;
    PercentagesCorrector.Snapshot percentagesSnapshot;
    final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100l);
    
    public NormalizedPercentagesColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, 
            String viewName, PercentagesCorrector percentagesCorrector) {
        super(columnName, levelColumn, viewName, TextColumnKeyBuilder.instance, PercentageTokenBehaviour.instance);
        this.percentagesCorrector = percentagesCorrector;
    }

    /**
     * fetches the entries corresponding to a given set of ownerIds, corrects the percentages and then returns the result cells
     * @param engine the context of the request
     * @param ids the ownerIds to fetch
     * @return
     */
    @Override
    public synchronized List<PercentageTextCell> fetch(NiReportsEngine engine, Set<Long> ids) {
        if (ids.isEmpty())
            return Collections.emptyList();
        String locale = TLSUtils.getEffectiveLangCode();
        AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
        PercentagesCorrector.Snapshot percsSnapshot = scratchpad.buildSnapshot(this.percentagesSnapshot, percentagesCorrector, ids);
        
        String queryCondition = String.format("WHERE %s IN (%s)", mainColumn, Util.toCSStringForIN(ids));
        ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView(viewName, queryCondition, locale, scratchpad.columnCachers, scratchpad.connection, "*");
        List<PercentageTextCell> res = new ArrayList<>();
        try(RsInfo rs = fetcher.fetch(null)) {
            while (rs.rs.next()) {
                PercentageTextCell cell = extractCell(engine, rs.rs, percsSnapshot);
                if (cell != null)
                    res.add(cell);
            }
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
        return res;
    }
    
    /**
     * extracts a single cell from a single row of a {@link ResultSet}, corrects the percentage and returns the result.
     * @param engine the request context
     * @param rs the correctly-position (row-wise) {@link ResultSet}
     * @param percsSnapshot the {@link Snapshot} used for correcting percentages
     * @return the result corrected cell OR null if the row contains invalid data
     * @throws SQLException
     */
    protected PercentageTextCell extractCell(NiReportsEngine engine, ResultSet rs, Snapshot percsSnapshot) throws SQLException {
        long activityId = rs.getLong(1);
        String text = rs.getString(2);
        long entityId = rs.getLong(3);
        Double rawPercentage = rs.getDouble(4);
        if (rs.wasNull()) rawPercentage = null;
        int nrNulls = rs.getInt(5);
        if (text == null || (rawPercentage != null && rawPercentage <= 0)) 
            return null;
        double percentage = percsSnapshot.correctPercentage(activityId, rawPercentage, nrNulls);
        return new PercentageTextCell(text, activityId, entityId, levelColumn, engine.schemaSpecificScratchpad.getPrecisionSetting().adjustPrecision(BigDecimal.valueOf(percentage / 100)));
    }

    @Override
    public boolean getKeptInSummaryReports() {
        return false;
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        // do nothing here, as underlying m2m tables are checked as part of AmpReportsSchema.performColumnChecks()
        return null;
    }
    
    @Override
    protected PercentageTextCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
        throw new RuntimeException("not implemented");
    }
}
