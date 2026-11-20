package org.digijava.kernel.ampapi.endpoints.filters;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class DisaggregationFilterListManager {
    private static final String CATEGORY_KEY = "indicator_disaggregation";
    private static final String GROUP_ID = "indicator-disaggregation-criterion";
    private static final String ITEMS = "values";

    private DisaggregationFilterListManager() {
    }

    public static FilterList getFilterList() {
        return buildFilterList(
                CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CATEGORY_KEY),
                criterion -> CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                        CATEGORY_KEY + "_" + criterion.getId()),
                TranslatorWorker::translateText);
    }

    static FilterList buildFilterList(Collection<AmpCategoryValue> criteria,
                                      Function<AmpCategoryValue, Collection<AmpCategoryValue>> optionsFor,
                                      Function<String, String> translate) {
        List<FilterListTreeNode> groups = new ArrayList<>();
        for (AmpCategoryValue criterion : criteria) {
            if (Boolean.TRUE.equals(criterion.getDeleted())) {
                continue;
            }
            FilterListTreeNode group = new FilterListTreeNode(
                    criterion.getId(), criterion.getValue(), translate.apply(criterion.getValue()));
            List<FilterListTreeNode> options = new ArrayList<>();
            for (AmpCategoryValue option : optionsFor.apply(criterion)) {
                if (!Boolean.TRUE.equals(option.getDeleted())) {
                    options.add(new FilterListTreeNode(
                            option.getId(), option.getValue(), translate.apply(option.getValue())));
                }
            }
            options.sort(Comparator.comparing(FilterListTreeNode::getName));
            if (!options.isEmpty()) {
                group.setChildren(options);
                groups.add(group);
            }
        }
        groups.sort(Comparator.comparing(FilterListTreeNode::getName));

        FilterListDefinition definition = new FilterListDefinition();
        definition.setName("Disaggregation");
        definition.setDisplayName(translate.apply("Disaggregation"));
        definition.setFilterIds(Arrays.asList(GROUP_ID, FiltersConstants.INDICATOR_DISAGGREGATION));
        definition.setFiltered(true);
        definition.setItems(ITEMS);
        definition.setSerializeSelectedLeaves(true);
        return new FilterList(definition, Collections.singletonMap(ITEMS, groups));
    }
}
