package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.NumberedTypedEntity;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportRenderWarningType;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.amp.diff.TextColumnKeyBuilder;
import org.dgfoundation.amp.nireports.behaviours.PercentageTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * 
 * a text column which fetches its input from a normalized view (sum of percentages over an entity id = 100, no nulls, else the output will be denormalized) which contains 4 or more columns: <br /><ol>
 *  <li>amp_activity_id (or pledge_id)</li>
 *  <li>payload (text)</li>
 *  <li>entity_id (e.g. sector_id, donor_id etc)</li>
 *  <li>percentage</li>
 *  </ol>  
 *  All the extra columns are ignored
 * @author Dolghier Constantin
 *
 */
public class PercentageTextColumn extends AmpDifferentialColumn<PercentageTextCell> {
    
    public final static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    public PercentageTextColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName) {
        super(columnName, levelColumn, viewName, TextColumnKeyBuilder.instance, PercentageTokenBehaviour.instance);
    }

    @Override
    protected PercentageTextCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
        String text = rs.getString(2);
        if (text == null)
            return null;
        BigDecimal percentage = rs.getBigDecimal(4);
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) <= 0 || percentage.compareTo(ONE_HUNDRED) > 0)
            return null;
        return new PercentageTextCell(text, rs.getLong(1), rs.getLong(3), this.levelColumn, percentage.divide(ONE_HUNDRED));
    }
        
    public static PercentageTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
        return new PercentageTextColumn(columnName, levelColumn, viewName);
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        List<ReportRenderWarning> res = new ArrayList<ReportRenderWarning>();
        String actFilteringQuery = "amp_activity_id IN (SELECT amp_activity_id FROM v_activity_latest_and_validated)";
        Set<Long> activitiesWithNonNull = new HashSet<Long>(PersistenceManager.getSession().doReturningWork(conn -> SQLUtils.fetchLongs(conn, String.format("SELECT DISTINCT %s FROM %s WHERE (%s) AND (%s IS NOT NULL)", "amp_activity_id", this.viewName, actFilteringQuery, "percentage"))));
        Set<Long> activitiesWithNull = new HashSet<Long>(PersistenceManager.getSession().doReturningWork(conn -> SQLUtils.fetchLongs(conn, String.format("SELECT DISTINCT %s FROM %s WHERE (%s) AND (%s IS NULL)", "amp_activity_id", this.viewName, actFilteringQuery, "percentage"))));
        
        for(long actWithNull:activitiesWithNull) {
            ReportRenderWarningType tp = activitiesWithNonNull.contains(actWithNull) ? ReportRenderWarningType.WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL : ReportRenderWarningType.WARNING_TYPE_ENTRY_WITH_NULL;
            res.add(new ReportRenderWarning(new NumberedTypedEntity(actWithNull), this.name, null, tp));
        }
        String query = String.format("SELECT %s, SUM(percentage) AS percentage FROM %s WHERE %s GROUP BY 1 HAVING sum(percentage) < 99.99 OR sum(percentage) > 100.01", "amp_activity_id", this.viewName, actFilteringQuery);
        List<Object[]> rows = PersistenceManager.getSession().createNativeQuery(query).list();
        for(Object[] row:rows)
        {
            long activityId = PersistenceManager.getLong(row[AMP_ACTIVITY_ID]);
            Double percentage = PersistenceManager.getDouble(row[PERCENTAGE]);
            NumberedTypedEntity entity = new NumberedTypedEntity(activityId);
            
            if (percentage != null && percentage > 100) {
                res.add(new ReportRenderWarning(entity, this.name, null, ReportRenderWarningType.WARNING_TYPE_PERCENTAGE_SUM_EXCEEDS_100));
            }

            if (percentage != null && percentage < 100) {
                res.add(new ReportRenderWarning(entity, this.name, null, ReportRenderWarningType.WARNING_TYPE_PERCENTAGE_SUM_LESS_100));
            }
        }
        return res;
    }
    
    @Override
    public boolean getKeptInSummaryReports() {
        return false;
    }

    private static final int AMP_ACTIVITY_ID = 0;
    private static final int PERCENTAGE = 1;
}
