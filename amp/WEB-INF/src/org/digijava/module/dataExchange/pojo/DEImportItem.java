/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.digijava.module.dataExchange.engine.SourceBuilder;
import org.digijava.module.dataExchange.jaxb.Activities;

import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiActivities;

/**
 * @author dan
 * this class implements the execution of a source builder (url, ws, file upload)
 * DEImportBuilder.java class is building the import: check, xml validation, content validation, save
 */
public class DEImportItem {
	private SourceBuilder sourceBuilder;

    /**
     * This is IDML2.0.xsd activities that should be eventually removed
     */
	private Activities activities;
	private IatiActivities iatiActivities;
	private IatiActivities previousIatiActivities;


	public IatiActivities getPreviousIatiActivities() {
		return previousIatiActivities;
	}


	public void setPreviousIatiActivities(IatiActivities previousIatiActivities) {
		this.previousIatiActivities = previousIatiActivities;
	}


	public IatiActivities getIatiActivities() {
		return iatiActivities;
	}


	public void setIatiActivities(IatiActivities iatiActivities) {
		this.iatiActivities = iatiActivities;
	}

	private HashMap<String,Boolean> hashFields;
	
	
	public HashMap<String, Boolean> getHashFields() {
		return hashFields;
	}


	public void setHashFields(HashMap<String, Boolean> hashFields) {
		this.hashFields = hashFields;
	}


	public Activities getActivities() {
		return activities;
	}


	public void setActivities(Activities activities) {
		this.activities = activities;
	}


	public DEImportItem(SourceBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
	}


	public DEImportItem() {
		// TODO Auto-generated constructor stub
	}


	public SourceBuilder getSourceBuilder() {
		return sourceBuilder;
	}


	public void setSourceBuilder(SourceBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
	}
	
	public InputStream getInputStream(){
		return new ByteArrayInputStream(this.sourceBuilder.getInputString().getBytes());
	}
	
	public InputStream getPreviousInputStream(){
		return new ByteArrayInputStream(this.sourceBuilder.getPreviousInputStream().getBytes());
	}

    public InputStream getRawStream() {
        return this.sourceBuilder.getRawStream();
    }

}
