package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Panel listing disaggregation base/target values and nested actual values panels grouped by parent category.
 */
public class AmpMEDisaggregationValuesFeaturePanel extends AmpFeaturePanel<AmpIndicator> {
    private static final Logger logger = Logger.getLogger(AmpMEDisaggregationValuesFeaturePanel.class);

    public AmpMEDisaggregationValuesFeaturePanel(String id, IModel<AmpIndicator> indicatorModel) {
        super(id, indicatorModel, "Disaggregation Values", true);
        setOutputMarkupId(true);

        IModel<List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>>> parentsModel = new LoadableDetachableModel<List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>>>() {
            @Override
            protected List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> load() {
                AmpIndicator indicator = indicatorModel.getObject();
                if (indicator == null || indicator.getDisaggregationValues() == null) return Collections.emptyList();
                Map<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>> grouped = new LinkedHashMap<>();
                for (AmpIndicatorDisaggregationValue v : indicator.getDisaggregationValues()) {
                    grouped.computeIfAbsent(v.getParentCategory(), k -> new ArrayList<>()).add(v);
                }
                return new ArrayList<>(grouped.entrySet());
            }
        };

        ListView<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> parentList = new ListView<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>>("parentList", parentsModel) {
            @Override
            protected void populateItem(ListItem<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> parentItem) {
                Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>> entry = parentItem.getModelObject();
                String parentName = entry.getKey() != null ? entry.getKey().getValue() : "N/A";
                parentItem.add(new Label("parentName", Model.of(parentName)));

                List<AmpIndicatorDisaggregationValue> children = entry.getValue();
                parentItem.add(new ListView<AmpIndicatorDisaggregationValue>("childList", children.stream().sorted(Comparator.comparing(a -> a.getChildCategory() != null ? a.getChildCategory().getValue() : "" )).collect(Collectors.toList())) {
                    @Override
                    protected void populateItem(ListItem<AmpIndicatorDisaggregationValue> childItem) {
                        AmpIndicatorDisaggregationValue disaggVal = childItem.getModelObject();
                        String childName = disaggVal.getChildCategory() != null ? disaggVal.getChildCategory().getValue() : "N/A";
                        childItem.add(new Label("childName", Model.of(childName)));

                        AmpIndicatorGlobalValue base = disaggVal.getBaseValue();
                        AmpIndicatorGlobalValue target = disaggVal.getTargetValue();
                        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

                        childItem.add(new Label("baseValue", Model.of(base != null && base.getOriginalValue() != null ? String.valueOf(base.getOriginalValue()) : "N/A")));
                        childItem.add(new Label("baseDate", Model.of(base != null && base.getOriginalValueDate() != null ? fmt.format(base.getOriginalValueDate()) : "N/A")));
                        childItem.add(new Label("targetValue", Model.of(target != null && target.getOriginalValue() != null ? String.valueOf(target.getOriginalValue()) : "N/A")));
                        childItem.add(new Label("targetDate", Model.of(target != null && target.getOriginalValueDate() != null ? fmt.format(target.getOriginalValueDate()) : "N/A")));

                        AmpMEDisaggregationActualValuesPanel actualPanel = new AmpMEDisaggregationActualValuesPanel("actualValuesPanel", Model.of(disaggVal));
                        childItem.add(actualPanel);
                    }
                });
            }
        };
        parentList.setOutputMarkupId(true);
        add(parentList);
    }
}
