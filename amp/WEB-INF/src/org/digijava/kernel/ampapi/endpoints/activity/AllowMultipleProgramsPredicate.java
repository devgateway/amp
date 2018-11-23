package org.digijava.kernel.ampapi.endpoints.activity;


import java.util.function.Function;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * @author Octavian Ciubotaru
 */
public class AllowMultipleProgramsPredicate implements Function<String, Boolean> {

    @Override
    public Boolean apply(String programName) {
        try {
            AmpActivityProgramSettings setting = ProgramUtil.getAmpActivityProgramSettings(programName);
            return setting != null && !setting.isAllowMultiple();
        } catch (DgException e) {
            throw new RuntimeException(e);
        }
    }
}
