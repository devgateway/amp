package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a <i>ni_all_programs_with_levels</i>-backed <strong>continuum</strong> dimension consisting of (TOP_PROGRAM[level=0], PROGRAM[level=1], SUB_PROGRAM[level=2], SUB_SUB_PROGRAM[level=3] and so upto level 8) 
 * @author Dolghier Constantin
 *
 */
public final class ProgramsDimension extends SqlSourcedNiDimension {
    
    public final static ProgramsDimension instance = new ProgramsDimension("progs");
    
    private ProgramsDimension(String name) {
        super(name, "ni_all_programs_with_levels", Arrays.asList("id0", "id1", "id2", "id3", "id4", "id5", "id6", "id7", "id8"));
    }

    @Override
    protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg, boolean pledgeColumn) {
        String schemeName = dimUsg.instanceName;

        if (pledgeColumn)
            return new PercentagesCorrector("amp_funding_pledges_program", "pledge_id", "program_percentage", () -> String.format("amp_program_id IN (select amp_theme_id FROM all_programs_with_levels WHERE program_setting_name='%s')", schemeName));
        
        return new PercentagesCorrector("amp_activity_program", "amp_activity_id", "program_percentage", () -> String.format("program_setting = (SELECT amp_program_settings_id FROM amp_program_settings WHERE name='%s')", schemeName));
    }
}
