package org.dgfundation.amp.support.dto;

public class ModuleDto {
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModuleDto(Long id, String a) {

		this.id = id;
		this.name = a;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
