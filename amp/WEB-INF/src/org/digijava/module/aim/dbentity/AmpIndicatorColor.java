package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpIndicatorColor implements Serializable{

	private String color;
	private AmpIndicatorLayer indicatorLayer;
	private Long indicatorColorId;
	private Long payload;
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public AmpIndicatorLayer getIndicatorLayer() {
		return indicatorLayer;
	}
	public void setIndicatorLayer(AmpIndicatorLayer indicatorLayer) {
		this.indicatorLayer = indicatorLayer;
	}
	public Long getIndicatorColorId() {
		return indicatorColorId;
	}
	public void setIndicatorColorId(Long indicatorColorId) {
		this.indicatorColorId = indicatorColorId;
	}
	public Long getPayload() {
		return payload;
	}
	public void setPayload(Long payload) {
		this.payload = payload;
	}
}
