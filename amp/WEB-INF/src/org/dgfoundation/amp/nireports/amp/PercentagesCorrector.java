package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.NumberedTypedEntity;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportRenderWarningType;
import org.dgfoundation.amp.nireports.NiUtils;

/**
 * a corrector for null percentages and denormalized percentages sums 
 * @author Dolghier Constantin
 *
 */
public class PercentagesCorrector {
	public final static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100l);
	
	public final String view;
	public final String mainIdColumnName;
	public final String percentagesColumnName;
	public final Supplier<String> conditionSupplier;
	
	public PercentagesCorrector(String view, String mainIdColumnName, String percentagesColumnName, Supplier<String> conditionSupplier) {
		this.view = view;
		this.mainIdColumnName = mainIdColumnName;
		this.percentagesColumnName = percentagesColumnName;
		this.conditionSupplier = Optional.ofNullable(conditionSupplier).orElse(() -> "1 = 1");
	}
	
	/**
	 * todo: when filter-ids-caching has been implemented, replace this by a reference to FFC
	 * @param ids
	 * @return
	 */
	public Snapshot buildSnapshot(java.sql.Connection conn, Set<Long> ids) {
		String query = String.format("select %1$s, SUM(COALESCE(%2$s, 100)) AS perc FROM %3$s vz " + 
						"WHERE (%4$s) AND (vz.%1$s IN (%5$s)) " + 
						"GROUP BY %1$s " + 
						"HAVING SUM(COALESCE(%2$s, 100)) <> 100", 
						mainIdColumnName, percentagesColumnName, view, conditionSupplier.get(), Util.toCSStringForIN(ids));
		Map<Long, Double> sumOfPercentages = new HashMap<>();
		SQLUtils.forEachRow(conn, query, ExceptionConsumer.of(rs -> { 
			if (rs.getDouble("perc") > 0)
				sumOfPercentages.put(rs.getLong(mainIdColumnName), rs.getDouble("perc"));
			}));
				
//		String nullCounterQuery = String.format("select %1$s, count(*) AS cnt FROM %3$s vz " + 
//				"WHERE (%4$s) AND (vz.$1$s IN (%5$s)) AND (vz.%2$s IS NULL) " + 
//				"GROUP BY %1$s ",  
//				mainIdColumnName, percentagesColumnName, view, conditionSupplier.get(), Util.toCSStringForIN(sumOfPercentages.keySet()));
//		Map<Long, Integer> nrOfNulls = new HashMap<>();
//		SQLUtils.forEachRow(conn, nullCounterQuery, ExceptionConsumer.of(rs -> nrOfNulls.put(rs.getLong(mainIdColumnName), rs.getInt("cnt"))));
		
		return new Snapshot(sumOfPercentages);
	}
	
	public static class Snapshot {
		/** sum of percentages if each null is counted as 100 */
		public final Map<Long, Double> sumOfPercentages;
		
		public Snapshot(Map<Long, Double> sumOfPercentages) {
			this.sumOfPercentages = Collections.unmodifiableMap(sumOfPercentages);
		}
		
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
