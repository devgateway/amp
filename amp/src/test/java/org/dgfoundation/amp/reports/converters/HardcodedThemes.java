package org.dgfoundation.amp.reports.converters;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.digijava.module.aim.dbentity.AmpTheme;

/**
 * @author Octavian Ciubotaru
 */
public class HardcodedThemes {

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

        themes = Stream.of(axe1, axe1p1, axe1p2, axe2, axe2p1).collect(Collectors.toMap(AmpTheme::getName, t -> t));
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
}
