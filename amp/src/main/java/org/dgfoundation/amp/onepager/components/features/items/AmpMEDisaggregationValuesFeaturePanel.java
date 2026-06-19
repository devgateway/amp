package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Panel listing disaggregation base/target values and nested actual values panels grouped by parent category.
 */
public class AmpMEDisaggregationValuesFeaturePanel extends AmpFeaturePanel<AmpIndicator> {
    private static final Logger logger = Logger.getLogger(AmpMEDisaggregationValuesFeaturePanel.class);

    private final IModel<AmpIndicator> indicatorModel;

    @Override
    protected void onConfigure() {
        super.onConfigure();
        AmpIndicator indicator = indicatorModel.getObject();
        if (indicator == null || indicator.getDisaggregationValues() == null || indicator.getDisaggregationValues().isEmpty()) {
            setVisible(false);
        }
    }

    public AmpMEDisaggregationValuesFeaturePanel(String id, String fmName, IModel<AmpIndicator> indicatorModel) {
        super(id, fmName, true);
        this.indicatorModel = indicatorModel;
        logger.info("Initializing AmpMEDisaggregationValuesFeaturePanel for indicator: " + (indicatorModel.getObject() != null ? indicatorModel.getObject().getName() : "null"));
        final Label panelLabel = new Label("panelLabel", Model.of("Disaggregation Values"));
        add(panelLabel);
        IModel<List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>>> parentsModel = new LoadableDetachableModel<List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>>>() {
            @Override
            protected List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> load() {
                AmpIndicator indicator = indicatorModel.getObject();
                if (indicator == null || indicator.getDisaggregationValues() == null) return Collections.emptyList();
                Map<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>> grouped = new LinkedHashMap<>();
                for (AmpIndicatorDisaggregationValue v : indicator.getDisaggregationValues()) {
                    grouped.computeIfAbsent(v.getParentCategory(), k -> new ArrayList<>()).add(v);
                }
                List<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> list = new ArrayList<>(grouped.entrySet());
                // sort parents by their category value (case-insensitive)
                list.sort(Comparator.comparing(e -> {
                    String name = e.getKey() != null && e.getKey().getValue() != null ? e.getKey().getValue() : "";
                    return name.toLowerCase(Locale.ROOT);
                }));
                return list;
            }
        };

        ListView<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> parentList = new ListView<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>>("parentList", parentsModel) {
            @Override
            protected void populateItem(ListItem<Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>>> parentItem) {
                Map.Entry<AmpCategoryValue, List<AmpIndicatorDisaggregationValue>> entry = parentItem.getModelObject();
                String parentName = entry.getKey() != null ? entry.getKey().getValue() : "N/A";
                AmpCategoryClass parentDisaggregationClass = entry.getKey()!= null ? entry.getKey().getAmpCategoryClass() :null;
                String parentDisaggregationName="Parent";
                if (parentDisaggregationClass!=null) {
                    AmpCategoryValue parentDisaggregation;
                    Long classId = Long.valueOf(parentDisaggregationClass.getKeyName().split("_")[parentDisaggregationClass.getKeyName().split("_").length - 1]);
                    parentDisaggregation = PersistenceManager.getSession().get(AmpCategoryValue.class, classId);
                    if (parentDisaggregation!=null) parentDisaggregationName=parentDisaggregation.getLabel();

                }
                // make parentDisaggregationName bold
                Label parentNameLabel = new Label("parentName", Model.of("<b>" + parentDisaggregationName + "</b>: " + parentName));
                parentNameLabel.setEscapeModelStrings(false);
                parentNameLabel.setOutputMarkupId(true);
                parentItem.add(parentNameLabel);

                WebMarkupContainer childrenContainer = new WebMarkupContainer("childrenContainer");
                childrenContainer.setOutputMarkupId(true);
                // keep always visible for CSS sliding (no placeholder toggling needed)
                parentItem.add(childrenContainer);

                parentNameLabel.add(new AjaxEventBehavior("click") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        target.appendJavaScript("(function(){var el=document.getElementById('" + childrenContainer.getMarkupId() + "');if(el){el.classList.toggle('open');}})();");
                    }
                });

                logger.info("Rendering disaggregation values for parent category: " + parentName);
                List<AmpIndicatorDisaggregationValue> children = entry.getValue();
                // ensure child list is sorted case-insensitively by child category value
                List<AmpIndicatorDisaggregationValue> sortedChildren = children.stream()
                        .sorted(Comparator.comparing(a -> {
                            String v = a.getChildCategory() != null && a.getChildCategory().getValue() != null ? a.getChildCategory().getValue() : "";
                            return v.toLowerCase(Locale.ROOT);
                        }))
                        .collect(Collectors.toList());
                ListView<AmpIndicatorDisaggregationValue> childListView = new ListView<AmpIndicatorDisaggregationValue>("childList", sortedChildren) {
                    @Override
                    protected void populateItem(ListItem<AmpIndicatorDisaggregationValue> childItem) {
                        AmpIndicatorDisaggregationValue disaggVal = childItem.getModelObject();
                        String childName = disaggVal.getChildCategory() != null ? disaggVal.getChildCategory().getValue() : "N/A";
                        String childDisaggregationName="Child";
                        AmpCategoryClass childDisaggregationClass = disaggVal.getChildCategory()!= null ? disaggVal.getChildCategory().getAmpCategoryClass() :null;
                        if (childDisaggregationClass!=null) {
                            AmpCategoryValue childDisaggregation;
                            Long classId = Long.valueOf(childDisaggregationClass.getKeyName().split("_")[childDisaggregationClass.getKeyName().split("_").length - 1]);
                            childDisaggregation = PersistenceManager.getSession().get(AmpCategoryValue.class, classId);
                            if (childDisaggregation != null) childDisaggregationName = childDisaggregation.getLabel();
                        }
                        String childHeaderText = "<b>" + childDisaggregationName + "</b>: " + childName;
                        Label childHeader = new Label("childHeader", Model.of(childHeaderText));
                        childHeader.setEscapeModelStrings(false);
                        childHeader.setOutputMarkupId(true);
                        childItem.add(childHeader);
                        logger.info("  Child category: " + childName);

                        AmpIndicatorGlobalValue base = disaggVal.getBaseValue();
                        AmpIndicatorGlobalValue target = disaggVal.getTargetValue();
                        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

                        // choose revised (non-null & non-zero) else original
                        Double baseDisplayVal = (base != null && base.getRevisedValue() != null && base.getRevisedValue() != 0d) ? base.getRevisedValue() : (base != null ? base.getOriginalValue() : null);
                        Date baseDisplayDate = (base != null && base.getRevisedValue() != null && base.getRevisedValue() != 0d && base.getRevisedValueDate() != null)
                                ? base.getRevisedValueDate()
                                : (base != null ? base.getOriginalValueDate() : null);

                        Double targetDisplayVal = (target != null && target.getRevisedValue() != null && target.getRevisedValue() != 0d) ? target.getRevisedValue() : (target != null ? target.getOriginalValue() : null);
                        Date targetDisplayDate = (target != null && target.getRevisedValue() != null && target.getRevisedValue() != 0d && target.getRevisedValueDate() != null)
                                ? target.getRevisedValueDate()
                                : (target != null ? target.getOriginalValueDate() : null);

                        childItem.add(new Label("baseValue", Model.of(baseDisplayVal != null ? String.valueOf(baseDisplayVal) : "N/A")));
                        childItem.add(new Label("baseDate", Model.of(baseDisplayDate != null ? fmt.format(baseDisplayDate) : "N/A")));
                        childItem.add(new Label("targetValue", Model.of(targetDisplayVal != null ? String.valueOf(targetDisplayVal) : "N/A")));
                        childItem.add(new Label("targetDate", Model.of(targetDisplayDate != null ? fmt.format(targetDisplayDate) : "N/A")));

                        WebMarkupContainer actualValuesContainer = new WebMarkupContainer("actualValuesContainer");
                        actualValuesContainer.setOutputMarkupId(true);
                        childItem.add(actualValuesContainer);

                        AmpMEDisaggregationActualValuesPanel actualPanel = new AmpMEDisaggregationActualValuesPanel("actualValuesPanel", Model.of(disaggVal));
                        actualPanel.setOutputMarkupId(true);
                        actualPanel.setOutputMarkupPlaceholderTag(true);
                        actualValuesContainer.add(actualPanel);

                        childHeader.add(new AjaxEventBehavior("click") {
                            @Override
                            protected void onEvent(AjaxRequestTarget target) {
                                target.appendJavaScript("(function(){var el=document.getElementById('" + actualValuesContainer.getMarkupId() + "');if(el){el.classList.toggle('open');}})();");
                            }
                        });
                    }
                };
                childListView.setOutputMarkupId(true);
                childrenContainer.add(childListView);
            }
        };
        parentList.setOutputMarkupId(true);
        add(parentList);
    }
}
