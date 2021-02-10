package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.ProgramExtraInfo;
import org.digijava.module.aim.dbentity.AmpThemeMapping;

/**
 * @author Nadejda Mandrescu
 */
public class ThemePossibleValuesProvider extends AbstractPossibleValuesDAOProvider {

    private Map<Long, Long> mappedPrograms;

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

    protected Map<Long, Long> getMappedPrograms() {
        List<AmpThemeMapping> list = possibleValuesDAO.getMappedThemes();
        if (list != null) {
            /* Map<Long, Long> srcToDst = list.stream()
                    .collect(Collectors.toMap(atm -> atm.getSrcTheme().getAmpThemeId(),
                            atm -> atm.getDstTheme().getAmpThemeId())); */
            Map<Long, Long> dstToSrc = list.stream()
                    .collect(Collectors.toMap(atm -> atm.getDstTheme().getAmpThemeId(),
                            atm -> atm.getSrcTheme().getAmpThemeId()));
            // srcToDst.putAll(dstToSrc);
            return dstToSrc;
        }
        return new HashMap<>();
    }

}
