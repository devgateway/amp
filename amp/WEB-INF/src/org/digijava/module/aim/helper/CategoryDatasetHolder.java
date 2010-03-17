package org.digijava.module.aim.helper;

import org.jfree.data.category.CategoryDataset;

public class CategoryDatasetHolder {
	private CategoryDataset dataset;
	private boolean fixedRange; //defines whether chart range for this dataset should be fixed or not
	
	public CategoryDataset getDataset() {
		return dataset;
	}
	public void setDataset(CategoryDataset dataset) {
		this.dataset = dataset;
	}
	public boolean isFixedRange() {
		return fixedRange;
	}
	public void setFixedRange(boolean fixedRange) {
		this.fixedRange = fixedRange;
	}
	
}
