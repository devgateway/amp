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

        // Existing sectors are uniquely identified by activity-sector PK id.
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }

        // New import sectors have null id until persisted; use sector id for de-duplication.
        if (this.sector != null && other.sector != null) {
            return Objects.equals(this.sector, other.sector);
        }

        return Objects.equals(this.id, other.id) && Objects.equals(this.sector, other.sector);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        if (sector != null) {
            return Objects.hash(sector);
        }
        return 0;
    }
}
