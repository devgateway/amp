package org.digijava.module.widget.helper;

/**
 * Option bean for charts.
 * @author Irakli Kobiashvili
 *
 */
public class ChartOption {
	private boolean showLegend;
	private boolean showLabels;
	private boolean showTitle;
	private String labelPattern;
	private Integer width;
	private Integer height;
	private boolean createMap;
	private String title;
    private String langCode;
    private String siteId;
    private String url;
	private Boolean monochrome ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

	public boolean isShowLegend() {
		return showLegend;
	}
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}
	public boolean isShowLabels() {
		return showLabels;
	}
	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
	}
	public boolean isShowTitle() {
		return showTitle;
	}
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	public String getLabelPattern() {
		return labelPattern;
	}
	public void setLabelPattern(String labelPattern) {
		this.labelPattern = labelPattern;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public boolean isCreateMap() {
		return createMap;
	}
	public void setCreateMap(boolean createMap) {
		this.createMap = createMap;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setMonochrome(Boolean monochromeOption) {
		this.monochrome = monochromeOption;
	}

	public Boolean isMonochrome() {
		return monochrome == null ? false : monochrome;
	}
}
