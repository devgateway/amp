package org.digijava.module.aim.action.dataimporter.model;


public class Sector {
    private Long id;
    private Long sector;
    private Float sector_percentage;

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

    public Float getSector_percentage() {
        return sector_percentage;
    }

    public void setSector_percentage(Float sector_percentage) {
        this.sector_percentage = sector_percentage;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "id=" + id +
                ", sector=" + sector +
                ", sector_percentage=" + sector_percentage +
                '}';
    }
}
