package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * NPD settings for each team.
 *
 * @author Dare Roinishvili
 */
public class NpdSettings implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Integer height;
    private Integer width;
    private Integer angle;
    private AmpTeam team;
    private Integer actListPageSize;
    private String selectedYearsForTeam; //used in npd to display graph by years;
    
    public Integer getActListPageSize() {
        return actListPageSize;
    }
    
    public void setActListPageSize(Integer actListPageSize) {
        this.actListPageSize = actListPageSize;
    }
    
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getSelectedYearsForTeam() {
        return selectedYearsForTeam;
    }
    
    public void setSelectedYearsForTeam(String selectedYearsForTeam) {
        this.selectedYearsForTeam = selectedYearsForTeam;
    }
    
    
}
