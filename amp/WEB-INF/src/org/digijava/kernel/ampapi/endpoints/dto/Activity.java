package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * Class that holds the activitiy data to be serialized as a JsonObject we ignore an object if its null
 * 
 * @author jdeanquin
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Activity {
	private Long id;
	private String title;
	private String description;
	private String ampUrl;
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


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	public void addCommitments(Double amount,String transactionDate){
		this.commitments.add(new ActivityFunding(amount,transactionDate));
	}
	public void addDisbursment(Double amount,String transactionDate){
		this.disbursments.add(new ActivityFunding(amount,transactionDate));
	}
	public class ActivityFunding {
		private Double amount;
		private String transactionDate;

		public ActivityFunding( Double amount,String transactionDate) {
			this.amount=amount;
			this.transactionDate=transactionDate;
		}
		public ActivityFunding() {

		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
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
