package org.dgfoundation.amp.reports.converters;

import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Octavian Ciubotaru
 */
public class HardcodedThemes {

    private final AmpActivityProgramSettings primaryProgramSettings;

    private final AmpActivityProgramSettings secondaryProgramSettings;

    private Map<String, AmpTheme> themes;

    public HardcodedThemes() {
        AmpTheme primaryPrograms = newTheme(1L, "Primary Programs");

        AmpTheme axe1 = newTheme(2L, "Axe 1");
        axe1.setParentThemeId(primaryPrograms);

        AmpTheme axe1p1 = newTheme(4L, "1.1");
        axe1p1.setParentThemeId(axe1);

        AmpTheme axe1p2 = newTheme(5L, "1.2");
        axe1p2.setParentThemeId(axe1);

        AmpTheme axe2 = newTheme(3L, "Axe 2");
        axe2.setParentThemeId(primaryPrograms);

        AmpTheme axe2p1 = newTheme(6L, "2.1");
        axe2p1.setParentThemeId(axe2);

        AmpTheme secondaryPrograms = newTheme(7L, "Secondary Programs");

        AmpTheme institutionalReform = new AmpTheme();
        institutionalReform.setAmpThemeId(8L);
        institutionalReform.setName("Institutional Reform");

        AmpTheme educationalReform = new AmpTheme();
        educationalReform.setAmpThemeId(9L);
        educationalReform.setName("Educational Reform");

        primaryProgramSettings = new AmpActivityProgramSettings();
        primaryProgramSettings.setName("Primary Program");
        primaryProgramSettings.setDefaultHierarchy(primaryPrograms);

        secondaryProgramSettings = new AmpActivityProgramSettings();
        secondaryProgramSettings.setName("Secondary Program");
        secondaryProgramSettings.setDefaultHierarchy(secondaryPrograms);

        themes = Stream.of(axe1, axe1p1, axe1p2, axe2, axe2p1, institutionalReform, educationalReform)
                .collect(Collectors.toMap(AmpTheme::getName, t -> t));
    }

    public AmpTheme getTheme(String name) {
        return themes.get(name);
    }

    private AmpTheme newTheme(long id, String name) {
        AmpTheme theme = new AmpTheme();
        theme.setAmpThemeId(id);
        theme.setName(name);
        return theme;
    }

    public AmpActivityProgramSettings getPrimaryProgramSettings() {
        return primaryProgramSettings;
    }

    public AmpActivityProgramSettings getSecondaryProgramSettings() {
        return secondaryProgramSettings;
    }
}
