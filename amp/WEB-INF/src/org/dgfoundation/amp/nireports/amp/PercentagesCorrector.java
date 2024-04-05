package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.NumberedTypedEntity;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportRenderWarningType;
import org.dgfoundation.amp.nireports.NiUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

/**
 * a corrector for null percentages and denormalized percentages sums.
 * Its way of working is akin to the one employed by dimensions: a {@link PercentagesCorrector} instance contains info regarding ways of fetching the correcting data. 
 * Since that information varies with time, one uses a {@link Snapshot} to postprocess live data fetched from the database. Hence, the process of correcting percentages is two-step: <br />
 * 1. make a snapshot by calling {@link PercentagesCorrector#buildSnapshot(java.sql.Connection, Set)} <br />
 * 2. use that snapshot in {@link NormalizedPercentagesColumn#extractCell(org.dgfoundation.amp.nireports.NiReportsEngine, java.sql.ResultSet, Snapshot)} <br />
 * 
 * Please note that this class does not operate on the AMP column view; instead it operates on the "basic view". The basic view is the natural-data table which generates the column-views
 * via JOINs (e.g:, v_adm_level_1 is a result of joining amp_activity_location with ni_all_locations_with_levels).
 * Since joining with a dimension definition table does
 * not change percentages' sums, computing the sums for the basic view is enough to be able to correct the percentages for any derived view.
 * @author Dolghier Constantin
 *
 */
public class PercentagesCorrector {
    public final static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100l);
    
    /**
     * the "basic" view to fetch data from. For example, v_region's basic view is amp_activity_locations
     */
    public final String view;
    
    /**
     * the view-column name containing the ownerId (usually amp_activity_id or pledge_id)
     */
    public final String mainIdColumnName;
    
    /**
     * the view-column name containing the percentages
     */
    public final String percentagesColumnName;
    
    /**
     * an optional SQL statement to add to the WHERE of the SELECT. Used for cases when the same basic view contains data for multiple families of columns 
     * (for example, amp_activity_sector)
     */
    public final Supplier<String> conditionSupplier;
    
    public PercentagesCorrector(String view, String mainIdColumnName, String percentagesColumnName, Supplier<String> conditionSupplier) {
        this.view = view;
        this.mainIdColumnName = mainIdColumnName;
        this.percentagesColumnName = percentagesColumnName;
        this.conditionSupplier = Optional.ofNullable(conditionSupplier).orElse(() -> "1 = 1");
    }
    
    /**
     * builds a snapshot containing correction information for certain ids only; thus it enabled differential fetching for percentages-corrector data
     * @param ids
     * @return
     */
    public Snapshot buildSnapshot(java.sql.Connection conn, Set<Long> ids) {
        String query = String.format("select %1$s, SUM(COALESCE(%2$s, 100)) AS perc FROM %3$s vz " + 
                        "WHERE (%4$s) AND (vz.%1$s IN (%5$s)) " + 
                        "GROUP BY %1$s " + 
                        "HAVING SUM(COALESCE(%2$s, 100)) <> 100", // minimize SQL traffic by only fetching denormal-entity entries
                        mainIdColumnName, percentagesColumnName, view, conditionSupplier.get(), Util.toCSStringForIN(ids));
        Map<Long, Double> sumOfPercentages = new HashMap<>();
        SQLUtils.forEachRow(conn, query, ExceptionConsumer.of(rs -> { 
            if (rs.getDouble("perc") > 0)
                sumOfPercentages.put(rs.getLong(mainIdColumnName), rs.getDouble("perc"));
            }));
                
//      String nullCounterQuery = String.format("select %1$s, count(*) AS cnt FROM %3$s vz " + 
//              "WHERE (%4$s) AND (vz.$1$s IN (%5$s)) AND (vz.%2$s IS NULL) " + 
//              "GROUP BY %1$s ",  
//              mainIdColumnName, percentagesColumnName, view, conditionSupplier.get(), Util.toCSStringForIN(sumOfPercentages.keySet()));
//      Map<Long, Integer> nrOfNulls = new HashMap<>();
//      SQLUtils.forEachRow(conn, nullCounterQuery, ExceptionConsumer.of(rs -> nrOfNulls.put(rs.getLong(mainIdColumnName), rs.getInt("cnt"))));
        
        return new Snapshot(sumOfPercentages);
    }
    
    /**
     * a {@link PercentagesCorrector} snapshot containing data for the denormal entities only
     * @author Dolghier Constantin
     *
     */
    public static class Snapshot {
        /** sum of percentages if each null is counted as 100 */
        public final Map<Long, Double> sumOfPercentages;
        
        public Snapshot(Map<Long, Double> sumOfPercentages) {
            this.sumOfPercentages = Collections.unmodifiableMap(sumOfPercentages);
        }

        
        /**
         * given a raw data (percentage as it comes from the AMP view) and its ownerId, return the percentage which should be output in the engine-visible Cell instead
         * @param mainId the ownerId
         * @param raw the Double percentage as it appears in the AMp view
         * @param nrNulls the number of nulls in the AMP view
         * @return
         */
        public Double correctPercentage(long mainId, Double raw, int nrNulls) {
            if (!sumOfPercentages.containsKey(mainId)) {
                // common case: no correction needed, make it FAST
                NiUtils.failIf(nrNulls > 1, () -> String.format("for fast-track entity %d, nrNulls is %d while raw is %s", mainId, nrNulls, raw));
                return raw == null ? 100 : raw;
            }
            double cellValue = 100 * nrNulls + (raw == null ? 0 : raw.doubleValue());
            return 100 * cellValue / sumOfPercentages.get(mainId);
        }
        
        /**
         * returns an another snapshot, containing old entries + new entries. New entries overwrite the existing once, in case of same-key overlap
         * @param newer
         * @return
         */
        public Snapshot mergeWith(Snapshot newer) {
            Map<Long, Double> resSop = new HashMap<>(this.sumOfPercentages);
            resSop.putAll(newer.sumOfPercentages);
            return new Snapshot(resSop);
        }
        
        @Override
        public String toString() {
            return String.format("sumOfPercs: %s", sumOfPercentages.toString());
        }
    }
    
    public List<ReportRenderWarning> validateDb(String entityName, java.sql.Connection conn) {
        Snapshot snapshot = buildSnapshot(conn, new HashSet<>(SQLUtils.fetchLongs(conn, "SELECT amp_activity_id FROM v_activity_latest_and_validated")));
        List<ReportRenderWarning> res = new ArrayList<>();
        snapshot.sumOfPercentages.forEach((id, perc) -> {
            if (perc > 100)
                res.add(new ReportRenderWarning(new NumberedTypedEntity(id), entityName, null, ReportRenderWarningType.WARNING_TYPE_PERCENTAGE_SUM_EXCEEDS_100));
            else
                res.add(new ReportRenderWarning(new NumberedTypedEntity(id), entityName, null, ReportRenderWarningType.WARNING_TYPE_PERCENTAGE_SUM_LESS_100));
        });
        return res;
    }
    
    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    public final boolean equals(Object oth) {
        return this == oth;
    }
}
