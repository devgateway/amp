package org.digijava.kernel.ampapi.endpoints.ndd.utils;

import org.digijava.kernel.ampapi.endpoints.ndd.MappingConfiguration;
import org.digijava.kernel.ampapi.endpoints.ndd.ProgramMappingConfiguration;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DashboardUtils {

    /**
     * Given a program get its parent from level @lvl.
     *
     * @param program
     * @param lvl
     * @return
     */
    public static AmpTheme getProgramByLvl(AmpTheme program, int lvl) {
        AmpTheme root = program;
        while (root.getIndlevel() > lvl) {
            root = root.getParentThemeId();
        }
        return root;
    }

    /**
     * Since we can change the assigned program for PP, NPO, SP, etc then we will have old programs assigned
     * in activities and that will break the donut charts.
     *
     * @param program
     * @param settings
     * @return
     */
    public static boolean isProgramValid(AmpTheme program, Set<AmpActivityProgramSettings> settings) {
        AtomicBoolean ret = new AtomicBoolean(false);
        AmpTheme root = getProgramByLvl(program, 0);
        settings.forEach(i -> {
            root.getProgramSettings().forEach(j -> {
                if (i.getAmpProgramSettingsId().equals(j.getAmpProgramSettingsId())) {
                    ret.set(true);
                }
            });
        });
        return ret.get();
    }

    /**
     * This function is way faster than ProgramUtil.getTheme().
     *
     * @param id
     * @return
     */
    public static AmpTheme getThemeById(Long id) {
        try {
            if (id > 0) {
                return ProgramUtil.getThemeById(id);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isMapped(MappingConfiguration mapping, AmpTheme outer, AmpTheme inner) {
        boolean ret = false;
        if (outer != null && inner != null
                && ((ProgramMappingConfiguration) mapping).getProgramMapping().stream().filter(i -> {
            return i.getSrcTheme().getAmpThemeId().equals(outer.getAmpThemeId())
                    && i.getDstTheme().getAmpThemeId().equals(inner.getAmpThemeId());
        }).collect(Collectors.toList()).size() > 0) {
            ret = true;
        }
        return ret;
    }
}
