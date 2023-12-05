package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.dgfoundation.amp.algo.Memoizer;
import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class AmpActivityProgramDiscriminatorConfigurer implements DiscriminationConfigurer {

    private Memoizer<Map<String, AmpActivityProgramSettings>> programSettingsByName =
            new Memoizer<>(this::loadProgramSettings);

    private Map<String, AmpActivityProgramSettings> loadProgramSettings() {
        Map<String, AmpActivityProgramSettings> programSettings = new HashMap<>();
        for (AmpActivityProgramSettings setting : ProgramUtil.getAmpActivityProgramSettingsList(true)) {
            programSettings.put(setting.getName(), setting);
        }
        return programSettings;
    }

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpActivityProgram program = (AmpActivityProgram) obj;
        program.setProgramSetting(programSettingsByName.get().get(discriminationValue));
    }
}
