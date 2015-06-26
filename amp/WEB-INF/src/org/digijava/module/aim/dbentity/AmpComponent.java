/*
 * AmpComponent.java
 * Created : 9th March, 2005
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Output;

/**
 * Persister class for Components
 * @author Priyajith
 */
@TranslatableClass (displayName = "Component")
public class AmpComponent implements Serializable,Comparable<AmpComponent>, Versionable, Cloneable {
	
	private static Logger logger = Logger.getLogger(AmpComponent.class);
	@Interchangeable(fieldTitle="ID", id = true)
	private Long ampComponentId;
	@Interchangeable(fieldTitle="Title",fmPath="/Activity Form/Components/Component/Component Information/Component Title", value = true)
	@TranslatableField
	private String title;
	@Interchangeable(fieldTitle="Description",fmPath="/Activity Form/Components/Component/Component Information/Description")
	@TranslatableField
	private String description;
//	@Interchangeable(fieldTitle="") //I gladly would export this, if I could
	private java.sql.Timestamp creationdate;
	@Interchangeable(fieldTitle="Code")
	private String code;
	
	public static class AmpComponentComparator implements Comparator<AmpComponent>{
		@Override
		public int compare(AmpComponent o1, AmpComponent o2) {
			return staticCompare(o1, o2);
		}
		
		public static int staticCompare(AmpComponent o1, AmpComponent o2) {
			if (o1 == null)
				return 1;
			if (o2 == null)
				return -1;
			if (o2.getTitle() == null)
				return -1;
			if (o1.getTitle() == null)
				return 1;
			int ret = o1.getTitle().compareTo(o2.getTitle());
			return ret;
		}
	}
	
	//private String type;
	private AmpComponentType type;
	
	private Set activities;
	private String Url;
	
	public Set getActivities() {
		return activities;
	}
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	public Long getAmpComponentId() {
		return ampComponentId;
	}
	public void setAmpComponentId(Long ampComponentId) {
		this.ampComponentId = ampComponentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public AmpComponentType getType() {
		return type;
	}
	public void setType(AmpComponentType type) {
		this.type = type;
	}
	
	public void setUrl(String url) {
		Url = url;
	}
	public String getUrl() {
		return Url;
	}
	
	/**
	 * A simple string comparison to sort components by title
	 */
	public int compareTo(AmpComponent o) {
		return AmpComponentComparator.staticCompare(this, o);
	}
    /**
     * If overriding is really needed please fully comment on the reason!
     *
	@Override
	public boolean equals(Object obj) {
        if (!(obj instanceof AmpComponent))
            return false;

		AmpComponent target=(AmpComponent) obj;
		if (this.ampComponentId == null)
			return super.equals(obj);
		
		if (target!=null && this.ampComponentId!=null){
			return (this.getAmpComponentId().equals(target.getAmpComponentId()));
		}
		return false;
	}
	@Override
	public int hashCode() {
		if( this.ampComponentId ==null) return 0;
		return this.ampComponentId.hashCode();
	}
	 */
	
	public java.sql.Timestamp getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(java.sql.Timestamp creationdate) {
		this.creationdate = creationdate;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpComponent aux = (AmpComponent) obj;
		String original = this.title != null ? this.title : "";
		String copy = aux.title != null ? aux.title : "";
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "Title" }, new Object[] { this.title != null ? this.title
						: "Empty Title" }));
		if (this.description != null && !this.description.trim().equals("")) {
			out.getOutputs()
					.add(new Output(null, new String[] { "Description" }, new Object[] { this.description }));
		}
		if (this.code != null && !this.code.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { "Code" }, new Object[] { this.code }));
		}
		if (this.creationdate != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Creation Date" }, new Object[] { this.creationdate }));
		}
		if (this.Url != null && !this.Url.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { "URL" }, new Object[] { this.Url }));
		}
		/*if (this.activity != null ) {
			out.getOutputs().add(new Output(null, new String[] { " Activity: " }, new Object[] { this.activity }));
		}*/
		return out;
	}
	@Override
	public Object getValue() {
		String value = " " + this.creationdate + this.description + this.Url + this.code /*+ this.activity*/;
		return value;
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpComponent aux = (AmpComponent) clone();
		aux.activities = new HashSet();
		aux.activities.add(newActivity);
		aux.ampComponentId = null;
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	@Override
	public String toString() {
		return title;
	}
}
