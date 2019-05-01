package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.ProgramExtraInfo;

/**
 * @author Nadejda Mandrescu
 */
public class ThemePossibleValuesProvider extends AbstractPossibleValuesDAOProvider {

    public ThemePossibleValuesProvider(String discriminatorValue) {
        super(discriminatorValue, false);
    }

    @Override
    protected List<Object[]> getDAOItems() {
        return possibleValuesDAO.getThemes(discriminatorValue);
    }

    @Override
    protected Object getExtraInfo(Object[] item) {
        Long parentProgramId = item.length > 2 ? (Long) item[PossibleValuesDAO.THEME_PARENT_ID_POS] : null;
        return new ProgramExtraInfo(parentProgramId);
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isThemeValid(discriminatorValue, id);
    }

}
