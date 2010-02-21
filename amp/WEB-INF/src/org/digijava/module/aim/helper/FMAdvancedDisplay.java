package org.digijava.module.aim.helper;

public class FMAdvancedDisplay {
	
	private Long id = null;
	private String name = null;
	private String type = null;
	private boolean visible = false;
	private int paddingLeft = 0;
	
	public FMAdvancedDisplay(){
		
	}

	public FMAdvancedDisplay(Long id, String name, String type, boolean visible , int paddingLeft){
		this.id = id;
		this.name = name;
		this.type = type;
		this.visible = visible;
		this.paddingLeft = paddingLeft;			
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isVisib() {
		boolean visib = visible;
		visible = false;
		return visib;
	}
	
	public void setVisib(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}
	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	
}
