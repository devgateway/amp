package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
/**
 * Class that holds the activitiy data to be serialized as a JsonObject we ignore an object if its null
 * 
 * @author jdeanquin
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Activity {
	private Long id;
	private String name;
	private String description;
	private String ampUrl;
	private JsonBean matchesFilters;
	private List<ActivityFunding> commitments;
	private List<ActivityFunding> disbursments;

	public Activity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmpUrl() {
		return ampUrl;
	}

	public void setAmpUrl(String ampUrl) {
		this.ampUrl = ampUrl;
	}

	public List<ActivityFunding> getCommitments() {
		return commitments;
	}

	public void setCommitments(List<ActivityFunding> commitments) {
		this.commitments = commitments;
	}

	public List<ActivityFunding> getDisbursments() {
		return disbursments;
	}

	public void setDisbursments(List<ActivityFunding> disbursments) {
		this.disbursments = disbursments;
	}
	public void addCommitments(String amount,String transactionDate){
		if(commitments==null){
			commitments=new ArrayList<Activity.ActivityFunding>();
		}
		this.commitments.add(new ActivityFunding(amount,transactionDate));
	}
	public void addDisbursment(String amount,String transactionDate){
		if(disbursments==null){
			disbursments=new ArrayList<Activity.ActivityFunding>();
		}
		this.disbursments.add(new ActivityFunding(amount,transactionDate));
	}
	
	
	public JsonBean getMatchesFilters() {
		return matchesFilters;
	}

	public void setMatchesFilters(JsonBean matchesFilters) {
		this.matchesFilters = matchesFilters;
	}


	public class ActivityFunding {
		private String amount;
		private String transactionDate;

		public ActivityFunding( String amount,String transactionDate) {
			this.amount=amount;
			this.transactionDate=transactionDate;
		}
		public ActivityFunding() {

		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
		}

	}

}
