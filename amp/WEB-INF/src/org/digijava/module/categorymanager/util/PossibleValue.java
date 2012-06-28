

package org.digijava.module.categorymanager.util;

import java.util.List;

/**
 *
 * @author medea
 */
public class PossibleValue {
	private Long id;
	private String value;
	private boolean disable;
	private List<LabelCategory> labelCategories;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<LabelCategory> getLabelCategories() {
		return labelCategories;
	}

	public void setLabelCategories(List<LabelCategory> labelCategories) {
		this.labelCategories = labelCategories;
	}
	

}
