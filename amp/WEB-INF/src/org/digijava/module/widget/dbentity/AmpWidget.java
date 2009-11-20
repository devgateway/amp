package org.digijava.module.widget.dbentity;

import java.io.Serializable;
import org.digijava.module.widget.helper.WidgetVisitor;

/**
 * Abstract widget.
 * Lets prefix all subclasses with AmpWidget.
 * @author Irakli Kobiashvili
 *
 */
public class AmpWidget implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String code;
	private Boolean nameAsTitle;
	
	public Boolean getNameAsTitle() {
		return nameAsTitle;
	}
	public void setNameAsTitle(Boolean nameAsTitle) {
		this.nameAsTitle = nameAsTitle;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
         public void accept(WidgetVisitor visitor){}

}
