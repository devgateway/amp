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
    private final PossibleValue srcProgram;

    @JsonProperty("dst-program")
    private final PossibleValue dstProgram;

    public MappingConfiguration(List<AmpIndirectTheme> programMapping,
            PossibleValue srcProgram, PossibleValue dstProgram) {
        this.programMapping = programMapping;
        this.srcProgram = srcProgram;
        this.dstProgram = dstProgram;
    }

    public List<AmpIndirectTheme> getProgramMapping() {
        return programMapping;
    }

    public PossibleValue getSrcProgram() {
        return srcProgram;
    }

    public PossibleValue getDstProgram() {
        return dstProgram;
    }
}
