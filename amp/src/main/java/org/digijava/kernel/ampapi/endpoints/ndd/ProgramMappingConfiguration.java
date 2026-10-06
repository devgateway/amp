package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.module.aim.dbentity.AmpThemeMapping;

import java.util.List;

/**
 * @author Viorel Chihai
 */
public class ProgramMappingConfiguration extends MappingConfiguration {

    @JsonProperty("program-mapping")
    private final List<AmpThemeMapping> programMapping;

    public ProgramMappingConfiguration(List<AmpThemeMapping> programMapping,
                                       NDDService.SingleProgramData srcProgram, NDDService.SingleProgramData dstProgram,
                                       List<PossibleValue> allPrograms) {
        super(srcProgram, dstProgram, allPrograms);
        this.programMapping = programMapping;
    }

    public List<AmpThemeMapping> getProgramMapping() {
        return programMapping;
    }

}
