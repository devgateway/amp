package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import org.digijava.kernel.ampapi.endpoints.activity.CategoryValueExtraInfo;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.List;

/**
 * @author Nadejda Mandrescu
 */
public class CategoryValuePossibleValuesProvider extends AbstractPossibleValuesDAOProvider {

    public CategoryValuePossibleValuesProvider(String discriminatorValue) {
        super(discriminatorValue, true);
    }

    @Override
    protected List<Object[]> getDAOItems() {
        return possibleValuesDAO.getCategoryValues(discriminatorValue);
    }

    @Override
    protected Object getExtraInfo(Object[] items) {
        Integer index = ((Number) (items[CategoryValueExtraInfo.EXTRA_INFO_START_INDEX])).intValue();
        String prefix = "" + items[CategoryValueExtraInfo.EXTRA_INFO_PREFIX_INDEX];
        return new CategoryValueExtraInfo(index, prefix);
    }

    @Override
    public boolean isAllowed(Long id) {
        return CategoryManagerUtil.isExitingAmpCategoryValue(discriminatorValue, id, isCheckDeleted);
    }

}
