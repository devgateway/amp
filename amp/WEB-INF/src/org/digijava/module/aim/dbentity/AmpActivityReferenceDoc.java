package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpActivityReferenceDoc implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private AmpActivityVersion activity;
	private AmpCategoryValue categoryValue;
	private String comment;
	private Date created;
	private Date lastEdited;
	
	public AmpActivityVersion getActivity() {
		return activity;
	}
	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getLastEdited() {
		return lastEdited;
	}
	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}
	public AmpCategoryValue getCategoryValue() {
		return categoryValue;
	}
	public void setCategoryValue(AmpCategoryValue referenceDoc) {
		this.categoryValue = referenceDoc;
	}
	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (! (obj instanceof AmpActivityReferenceDoc)) return false;
		AmpActivityReferenceDoc other=(AmpActivityReferenceDoc)obj;
		if (other.getId()!=null && this.id!=null){
			return other.getId().equals(this.id);
		}
		return false;
	}
	@Override
	public int hashCode() {
		int hash=0;
		hash += (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}
	
	
}
