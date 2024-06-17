package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class CoreIndicatorProgressDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CountryDTO country;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DonorDTO donor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProgramDTO program;
    private List<CoreIndicatorValueDTO> values;

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public DonorDTO getDonor() {
        return donor;
    }

    public void setDonor(DonorDTO donor) {
        this.donor = donor;
    }

    public List<CoreIndicatorValueDTO> getValues() {
        return values;
    }

    public void setValues(List<CoreIndicatorValueDTO> values) {
        this.values = values;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }
}

