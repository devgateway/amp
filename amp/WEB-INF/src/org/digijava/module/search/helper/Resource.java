package org.digijava.module.search.helper;

import org.digijava.module.aim.util.LoggerIdentifiable;

public class Resource implements LoggerIdentifiable {

	private String name;
	private String uuid;
	private String webLink;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	public String getWebLink(){
		return this.webLink;
	}

}
