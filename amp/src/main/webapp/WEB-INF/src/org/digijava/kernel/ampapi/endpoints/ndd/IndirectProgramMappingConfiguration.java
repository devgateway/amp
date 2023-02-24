package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class IndirectProgramMappingConfiguration extends MappingConfiguration {

    @JsonProperty("program-mapping")
    private final List<AmpIndirectTheme> programMapping;

    public IndirectProgramMappingConfiguration(List<AmpIndirectTheme> programMapping,
                                               NDDService.SingleProgramData srcProgram,
                                               NDDService.SingleProgramData dstProgram,
                                               List<PossibleValue> allPrograms) {
        super(srcProgram, dstProgram, allPrograms);
        this.programMapping = programMapping;
    }

    public List<AmpIndirectTheme> getProgramMapping() {
        return programMapping;
    }

}
