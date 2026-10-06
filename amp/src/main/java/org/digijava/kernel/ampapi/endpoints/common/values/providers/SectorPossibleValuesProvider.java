package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.SectorExtraInfo;

import java.util.List;

/**
 * @author Nadejda Mandrescu
 */
public class SectorPossibleValuesProvider extends AbstractPossibleValuesDAOProvider {

    public SectorPossibleValuesProvider(String discriminatorValue) {
        super(discriminatorValue, false);
    }

    @Override
    protected List<Object[]> getDAOItems() {
        return possibleValuesDAO.getSectors(discriminatorValue);
    }

    @Override
    protected Object getExtraInfo(Object[] item) {
        Long parentSectorId = item.length > 2 ? (Long) item[PossibleValuesDAO.SECTOR_PARENT_ID_POS] : null;
        return new SectorExtraInfo(parentSectorId);
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isSectorValid(discriminatorValue, id);
    }
}
