package org.digijava.module.aim.helper;

public class CategoryCustomField extends CustomField<Long> {
	private String categoryName;

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}
}
