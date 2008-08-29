package org.digijava.module.widget.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.action.ShowWidgetChart;

/**
 * Form for {@link ShowWidgetChart} action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowWidgetChartForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	private Integer imageWidth;
	private Integer imageHeight;
	private Long widgetId;
	private Long objectId; 
	private Long donorId;
	private String selectedYear;
	private Boolean showLegend;
	private Boolean showLabels;
	
	public String getSelectedYear() {
		return selectedYear;
	}
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}
	public Long getDonorId() {
		return donorId;
	}
	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	public void setShowLegend(Boolean showLegend) {
		this.showLegend = showLegend;
	}
	public Boolean getShowLegend() {
		return showLegend;
	}
	public void setShowLabels(Boolean showLabels) {
		this.showLabels = showLabels;
	}
	public Boolean getShowLabels() {
		return showLabels;
	}
	
}
