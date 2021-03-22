package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.ProgramExtraInfo;
import org.digijava.module.aim.dbentity.AmpThemeMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

/**
 * @author Nadejda Mandrescu
 */
public class ThemePossibleValuesProvider extends AbstractPossibleValuesDAOProvider {

    private Map<Long, Set<Long>> mappedPrograms;

    public ThemePossibleValuesProvider(String discriminatorValue) {
        super(discriminatorValue, false);
    }

    @Override
    protected List<Object[]> getDAOItems() {
        return possibleValuesDAO.getThemes(discriminatorValue);
    }

    @Override
    protected Object getExtraInfo(Object[] item) {
        if (mappedPrograms == null) {
            mappedPrograms = getMappedPrograms();
        }
        Long parentProgramId = item.length > 2 ? (Long) item[PossibleValuesDAO.THEME_PARENT_ID_POS] : null;
        return new ProgramExtraInfo(parentProgramId, mappedPrograms.get(item[0]));
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isThemeValid(discriminatorValue, id);
    }

    protected Map<Long, Set<Long>> getMappedPrograms() {
        List<AmpThemeMapping> list = possibleValuesDAO.getMappedThemes();
        if (list != null) {
            Map<Long, Set<Long>> srcToDst = list.stream().collect(groupingBy(atm -> atm.getSrcTheme().getAmpThemeId(),
                    mapping(atm -> atm.getDstTheme().getAmpThemeId(), toSet())));
            Map<Long, Set<Long>> dstToSrc = list.stream().collect(groupingBy(atm -> atm.getDstTheme().getAmpThemeId(),
                    mapping(atm -> atm.getSrcTheme().getAmpThemeId(), toSet())));
            srcToDst.putAll(dstToSrc);
            return srcToDst;
        }
        return new HashMap<>();
    }

}
