package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

public class DonorDTO {
    public DonorDTO() {
    }

    public DonorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
