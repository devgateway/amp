/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.digijava.module.dataExchange.engine.SourceBuilder;
import org.digijava.module.dataExchange.jaxb.Activities;

/**
 * @author dan
 *
 */
public class DEImportItem {
	private SourceBuilder sourceBuilder;
	private Activities activities;
	
	
	public Activities getActivities() {
		return activities;
	}


	public void setActivities(Activities activities) {
		this.activities = activities;
	}


	public DEImportItem(SourceBuilder sourceBuilder) {
		super();
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
	

}
