package org.digijava.module.esrigis.dbentity;

import java.util.Date;
/**
 * 
 * @author jdeanquin
 *
 */
public class AmpApiState {
	private Long id;
	private String title;
	private String description;
	private String type;
	private String stateBlob;
	private Date createdDate;
	private Date updatedDate;
	private Date lastAccesedDate;
	public AmpApiState(){
		
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
	public String getStateBlob() {
		return stateBlob;
	}
	public void setStateBlob(String stateBlob) {
		this.stateBlob = stateBlob;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public Date getLastAccesedDate() {
		return lastAccesedDate;
	}
	public void setLastAccesedDate(Date lastAccesedDate) {
		this.lastAccesedDate = lastAccesedDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastAccesedDate == null) ? 0 : lastAccesedDate.hashCode());
		result = prime * result
				+ ((stateBlob == null) ? 0 : stateBlob.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((updatedDate == null) ? 0 : updatedDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmpApiState other = (AmpApiState) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastAccesedDate == null) {
			if (other.lastAccesedDate != null)
				return false;
		} else if (!lastAccesedDate.equals(other.lastAccesedDate))
			return false;
		if (stateBlob == null) {
			if (other.stateBlob != null)
				return false;
		} else if (!stateBlob.equals(other.stateBlob))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		return true;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "AmpApiState [id=" + id + ", title=" + title + ", description="
				+ description + ", type=" + type + ", stateBlob=" + stateBlob
				+ ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", lastAccesedDate=" + lastAccesedDate + "]";
	}
	
	
}
