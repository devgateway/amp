/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import org.apache.log4j.Logger;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Category Value Services
 * 
 * @author Nadejda Mandrescu
 */
public class CategoryValueService {
    private static Logger LOGGER = Logger.getLogger(CategoryValueService.class);
            
    /**
     * Simple list of Category Values:
     * @param categoryKey the category key
     * <pre>
     * @return 
     * [
     *  { 
     *    "id" : 123,
     *    "orig-name" : "Ration (% of Total Population)", // (optional) if requested, is not translated
     *    "name" : “Ration (% of Total Population)” // translated
     *  }, 
     *  ...
     * ]
     * </pre>
     */
    public static List<CategoryValueLabel> getCategoryValues(String categoryKey) {
        Collection<AmpCategoryValue> categValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                categoryKey);
        if (categValues == null || categValues.isEmpty()) {
            LOGGER.warn(String.format("No category values found for '%s' key", categoryKey));
            return Collections.EMPTY_LIST;
        }
        
        List<CategoryValueLabel> types = categValues.stream()
                .map(acv -> new CategoryValueLabel(acv.getId(), acv.getValue(),
                        TranslatorWorker.translateText(acv.getValue())))
                .collect(Collectors.toList());
        
        return types;
    }

}
