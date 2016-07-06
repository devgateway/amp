package org.digijava.module.aim.dbentity;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorAccessType;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


public class AmpIndicatorLayer implements Serializable, Comparable <AmpIndicatorLayer> {
	
	private Long id;
	private String name;
	private String description;
	private Set <AmpIndicatorColor> colorRamp;
	private Long numberOfClasses;
	private AmpCategoryValue admLevel;
	private Set <AmpLocationIndicatorValue> indicatorValues;
    private Set <AmpIndicatorWorkspace> sharedWorkspaces;
	private String unit;
	private IndicatorAccessType accessType;
	private Date createdOn;
	private Date updatedOn;
	private AmpTeamMember createdBy;

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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public Long getNumberOfClasses() {
		return numberOfClasses;
	}


	public void setNumberOfClasses(Long numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}
	
	@Override
	public int compareTo(AmpIndicatorLayer o) {
		return id.compareTo(o.getId());
	}


	public AmpCategoryValue getAdmLevel() {
		return admLevel;
	}


	public void setAdmLevel(AmpCategoryValue admLevel) {
		this.admLevel = admLevel;
	}


	public Set<AmpIndicatorColor> getColorRamp() {
		return colorRamp;
	}


	public void setColorRamp(Set<AmpIndicatorColor> colorRamp) {
        if (this.colorRamp == null) {
            this.colorRamp = colorRamp;
        } else {
            this.colorRamp.retainAll(colorRamp);
            this.colorRamp.addAll(colorRamp);
        }
	}


	public Set<AmpLocationIndicatorValue> getIndicatorValues() {
		return indicatorValues;
	}


	public void setIndicatorValues(Set<AmpLocationIndicatorValue> indicatorValues) {
		this.indicatorValues = indicatorValues;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}

    public IndicatorAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(IndicatorAccessType accessType) {
        this.accessType = accessType;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public AmpTeamMember getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AmpTeamMember createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn=updatedOn;
    }

    public Set<AmpIndicatorWorkspace> getSharedWorkspaces() {
        return sharedWorkspaces;
    }

    public void setSharedWorkspaces(Set<AmpIndicatorWorkspace> sharedWorkspaces) {
        if (this.sharedWorkspaces == null) {
            this.sharedWorkspaces = sharedWorkspaces;
        } else {
            this.sharedWorkspaces.retainAll(sharedWorkspaces);
            this.sharedWorkspaces.addAll(sharedWorkspaces);
        }
    }
}
