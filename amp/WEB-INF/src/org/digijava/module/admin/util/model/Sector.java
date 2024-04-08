package org.digijava.module.admin.util.model;


public class Sector {
    private Long id;
    private Long sector;
    private Double sector_percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSector() {
        return sector;
    }

    public void setSector(Long sector) {
        this.sector = sector;
    }

    public Double getSector_percentage() {
        return sector_percentage;
    }

    public void setSector_percentage(Double sector_percentage) {
        this.sector_percentage = sector_percentage;
    }
}
