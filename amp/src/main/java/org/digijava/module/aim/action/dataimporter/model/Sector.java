package org.digijava.module.aim.action.dataimporter.model;


import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sector)) return false;
        Sector other = (Sector) o;
        // Two Sector objects referencing the same amp_sector entity are equal,
        // regardless of whether the activity-sector PK (id) has been populated yet.
        if (this.sector != null && other.sector != null) {
            return Objects.equals(this.sector, other.sector);
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        // Always hash by the sector entity id so hashCode is consistent with equals
        // when one object has a populated activity-sector PK and the other does not.
        return Objects.hashCode(sector);
    }
}
