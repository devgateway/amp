package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * 
 * @author Viorel Chihai
 *
 * @param <T>
 */
@JsonPropertyOrder({"data", "totalRecords"})
public class PerformanceRulesAdminPage<T> {
	
	@JsonProperty(value = "data")
	private List<T> performanceRules = new ArrayList<>();
	
	@JsonProperty(value = "totalRecords")
	private int totalRecords = 0;
	
	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<T> getPerformanceRules() {
		return performanceRules;
	}

	public void setPerformanceRules(List<T> performanceRules) {
		this.performanceRules = performanceRules;
	}
}
