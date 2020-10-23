package org.digijava.kernel.ampapi.endpoints.ndd;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;

/**
 * @author Octavian Ciubotaru
 */
public class MappingConfiguration {

    @JsonProperty("program-mapping")
    private final List<AmpIndirectTheme> programMapping;

    @JsonProperty("src-program")
    private final NDDService.SingleProgramData srcProgram;

    @JsonProperty("dst-program")
    private final NDDService.SingleProgramData dstProgram;

    @JsonProperty("all-programs")
    private final List<PossibleValue> allPrograms;

    public MappingConfiguration(List<AmpIndirectTheme> programMapping,
                                NDDService.SingleProgramData srcProgram, NDDService.SingleProgramData dstProgram,
                                List<PossibleValue> allPrograms) {
        this.programMapping = programMapping;
        this.srcProgram = srcProgram;
        this.dstProgram = dstProgram;
        this.allPrograms = allPrograms;
    }

    public List<AmpIndirectTheme> getProgramMapping() {
        return programMapping;
    }

    public NDDService.SingleProgramData getSrcProgram() {
        return srcProgram;
    }

    public NDDService.SingleProgramData getDstProgram() {
        return dstProgram;
    }
}
