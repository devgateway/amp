package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * NPD settings for each team.
 *
 * @author Dare Roinishvili
 */
import javax.persistence.*;

@Entity
@Table(name = "amp_npd_settings")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NpdSettings implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "npd_settings_id")
    private Long id;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "angle")
    private Integer angle;

    @Column(name = "activities_per_page")
    private Integer actListPageSize;

    @Column(name = "selected_years_for_team")
    private String selectedYearsForTeam;

    @OneToOne(mappedBy = "npdSettings")
    private AmpTeam team;

    
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
