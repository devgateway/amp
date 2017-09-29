/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
     * @param addOrigName true if also to add original untranslated category value
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
    public static List<JsonBean> getCategoryValues(String categoryKey, boolean addOrigName) {
        List<JsonBean> types = new ArrayList<JsonBean>();
        Collection<AmpCategoryValue> categValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                categoryKey);
        if (categValues == null || categValues.isEmpty()) {
            LOGGER.warn(String.format("No category values found for '%s' key", categoryKey));
            return Collections.EMPTY_LIST;
        }
        for (AmpCategoryValue acv : categValues) {
            JsonBean jsonType = new JsonBean();
            jsonType.set(EPConstants.ID, acv.getId());
            if (addOrigName) {
                jsonType.set(EPConstants.ORIGINAL_NAME, acv.getValue());
            }
            jsonType.set(EPConstants.NAME, TranslatorWorker.translateText(acv.getValue()));
            types.add(jsonType);
        }
        return types;
    }

}
