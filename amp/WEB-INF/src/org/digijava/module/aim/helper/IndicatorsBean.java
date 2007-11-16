package org.digijava.module.aim.helper;

import java.util.Collection;

public class IndicatorsBean {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String date;
    private Integer category;
    private String type;
    private Collection sector;
    private String sectorName;

    public IndicatorsBean() {
    }

    public IndicatorsBean(AllMEIndicators ind) {
        this.id=ind.getAmpMEIndId();
        this.name=ind.getName();
        this.code=ind.getCode();
        
        
    }

    public IndicatorsBean(AllPrgIndicators ind) {
        this.id=ind.getIndicatorId();
        this.name=ind.getName();
        this.category=Integer.valueOf(ind.getCategory());
        this.code=ind.getCode();
        this.date=ind.getCreationDate();
        this.description=ind.getDescription();
        this.sector= ind.getSector();
        this.sectorName = ind.getSectorName();
        
    }

    public IndicatorsBean(AmpMEIndicatorList ind) {
        this.id=ind.getAmpMEIndId();
        this.name=ind.getName();
        this.code=ind.getCode();
        this.description=ind.getDescription();
       
    }


    public Integer getCategory() {
        return category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Collection getSector() {
		return sector;
	}

	public void setSector(Collection sector) {
		this.sector = sector;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
}
