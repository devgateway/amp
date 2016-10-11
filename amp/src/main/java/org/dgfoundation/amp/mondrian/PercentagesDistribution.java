package org.dgfoundation.amp.mondrian;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.dgfoundation.amp.newreports.NumberedTypedEntity;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportRenderWarningType;

/**
 * class which holds and processes percentages distribution for a certain entity, certain class (for example, primary sectors for an activity or pledge)
 * @author Dolghier Constantin
 *
 */
public class PercentagesDistribution {
	
	/**
	 * before postprocessing - raw data, possible containing NULLs and unnormalized data (adding up to != 100)
	 * after postprocessing - strictly non-null data adding up to 100%
	 */
	protected Map<Long, Double> percentages = new TreeMap<>();
		
	protected boolean postprocessed = false;
	
	protected final NumberedTypedEntity entity;
	protected final String columnName;
	
	protected final Set<ReportRenderWarning> errors = new TreeSet<>();
	
	public PercentagesDistribution(NumberedTypedEntity entity, String columnName) {
		this.entity = entity;
		this.columnName = columnName;
	}
	
	/**
	 * 
	 * @param rs should have the rows of the form <activityId, sectorId, percentage>
	 * @return
	 * @throws SQLException
	 */
	public static Map<Long, PercentagesDistribution> readInput(ResultSet rs) throws SQLException {
		Map<Long, PercentagesDistribution> res = new TreeMap<>();
		String columnName = rs.getMetaData().getColumnLabel(2);		
		while (rs.next()) { 
			long activityId = rs.getLong(1);
			long fieldId = rs.getLong(2);
			Double percentage = rs.getDouble(3);
			if (rs.getObject(3) == null) percentage = null;
			
			if (percentage != null && percentage.doubleValue() < 0.001)
				continue; // ignore zero-values
			
			NumberedTypedEntity entity = new NumberedTypedEntity(activityId);
			if (!res.containsKey(activityId))
				res.put(activityId, new PercentagesDistribution(entity, columnName));
			res.get(activityId).add(fieldId, percentage);
		}
		return res;
	}
	
	/**
	 * adds a percentage to a fieldId (e.g. sectorId, for example). Does all the necessary checks
	 * @param entity
	 * @param fieldId
	 * @param percentage
	 */
	public void add(long fieldId, Double percentage) {
		if (this.postprocessed)
			throw new IllegalStateException("not allowed to modify a PercentageDistribution instance after having postprocessed it");
		
		if (percentages.containsKey(fieldId)) {			
			errors.add(new ReportRenderWarning(entity, this.columnName, fieldId, ReportRenderWarningType.WARNING_TYPE_MULTIPLE_ENTRIES_FOR_SAME_CART_ID));
		}
		
		if (percentage == null) {
			errors.add(new ReportRenderWarning(entity, this.columnName, fieldId, ReportRenderWarningType.WARNING_TYPE_ENTRY_WITH_NULL));
		}
		
		if (percentages.containsKey(fieldId))
			percentage = combinePercentages(percentage, percentages.get(fieldId));
		
		if (percentage != null && percentage.doubleValue() < 0.0001)
			return;

		percentages.put(fieldId, percentage);
	}

	/**
	 * postprocesses percentages in {@link #percentages} so that they sum up to 100%
	 * @param idToAddIfEmpty - if not null, then a dummy entry will be added with the said id for activities without entries (e.g., a project with no Secondary Sector will have this dummy Secondary Sector added)
	 */
	public void postProcess(Long idToAddIfEmpty) {
		
		if (this.postprocessed)
			return;
		
		this.postprocessed = true;
				
		if (percentages.isEmpty()) {
			if (idToAddIfEmpty != null) {
				// add a dummy entry with 100% if has been specified
				percentages.put(idToAddIfEmpty, 100.0);
			}
			return; // nothing to do
		}
		
		double sum = 0.0; // sum of percentages
		int numberOfNulls = 0; // number of entries with null
		for(Double percentage:percentages.values()) {
			if (percentage == null)
				numberOfNulls ++;
			else
				sum += percentage;
		}
		
		double nullReplacementValue;
		
		if (numberOfNulls > 0 && numberOfNulls != percentages.size()) {
			errors.add(new ReportRenderWarning(entity, this.columnName, ReportRenderWarning.UNDEFINED_CART_ENTITY, ReportRenderWarningType.WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL));
		}
		
		if (numberOfNulls == percentages.size()) {
			// if only null activities found, then sum will be zero, so we need to override that
			nullReplacementValue = 100.0;
			sum = 0.0;
		} else
			nullReplacementValue = sum;
		
		double truePercentageSum = sum + numberOfNulls * nullReplacementValue;
		double percentagesSum = 0; // sum of percentages written by postprocess()
		
		for (Map.Entry<Long, Double> entry:percentages.entrySet()) {
			Double newValue = (entry.getValue() == null ? nullReplacementValue : entry.getValue()) * 100.0 / truePercentageSum;
			entry.setValue(newValue);
			percentagesSum += entry.getValue();
		}
		boolean isOutputOk = Math.abs(percentagesSum - 100.0) < 0.001;
		if (!isOutputOk)
			throw new RuntimeException("BUG while distributing percentages, sum is " + percentagesSum + ", had numberOfNulls: " + numberOfNulls + " and percentages is now " + percentages.toString());
	}
	
	/**
	 * null + X = X, else
	 * X + Y -> X + Y
	 * @param a
	 * @param b
	 * @return
	 */
	public final static Double combinePercentages(Double a, Double b) {
		if (a == null) return b;
		if (b == null) return a;
		return a + b;
	}
	
	/**
	 * gets an unmodifiable view of the errors
	 * @return
	 */
	public Set<ReportRenderWarning> getErrors() {
		return Collections.unmodifiableSet(this.errors);
	}
	
	/**
	 * gets an unmodifiable view of the percentages
	 * @return
	 */
	public Map<Long, Double> getPercentages() {
		return Collections.unmodifiableMap(this.percentages);
	}
	
	@Override
	public String toString() {
		return this.percentages.toString();
	}
}
