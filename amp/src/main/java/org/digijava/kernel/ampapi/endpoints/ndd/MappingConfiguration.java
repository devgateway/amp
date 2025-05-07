package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class MappingConfiguration {

    @JsonProperty("src-program")
    private final NDDService.SingleProgramData srcProgram;

    @JsonProperty("dst-program")
    private final NDDService.SingleProgramData dstProgram;

    @JsonProperty("all-programs")
    private final List<PossibleValue> allPrograms;

    public MappingConfiguration(NDDService.SingleProgramData srcProgram, NDDService.SingleProgramData dstProgram,
                                List<PossibleValue> allPrograms) {
        this.srcProgram = srcProgram;
        this.dstProgram = dstProgram;
        this.allPrograms = allPrograms;
    }

    public NDDService.SingleProgramData getSrcProgram() {
        return srcProgram;
    }

    public NDDService.SingleProgramData getDstProgram() {
        return dstProgram;
    }
}
