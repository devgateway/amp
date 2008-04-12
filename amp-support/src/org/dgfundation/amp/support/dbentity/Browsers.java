package org.dgfundation.amp.support.dbentity;

public class Browsers {
	private String name;

	private String code;

	public Browsers(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
