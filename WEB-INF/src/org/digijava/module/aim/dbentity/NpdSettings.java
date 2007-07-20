package org.digijava.module.aim.dbentity;
import java.io.Serializable;
import org.digijava.module.aim.dbentity.AmpTeam;

public class NpdSettings implements Serializable{
	private Long npdSettingsId;
	private Integer height;
	private Integer width;
	private Integer angle;
	private AmpTeam team;
	public Integer getAngle() {
		return angle;
	}
	public void setAngle(Integer angle) {
		this.angle = angle;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Long getNpdSettingsId() {
		return npdSettingsId;
	}
	public void setNpdSettingsId(Long npdSettingsId) {
		this.npdSettingsId = npdSettingsId;
	}
	public AmpTeam getTeam() {
		return team;
	}
	public void setTeam(AmpTeam team) {
		this.team = team;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	
}
