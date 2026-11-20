package org.dgfoundation.amp.nireports.amp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndicatorDisaggregationFilterPersistenceTest {
    @Test
    void mapsWidgetIdsAndLegacyOptionsToTheSameReportFilter() throws Exception {
        AmpReportFilters fromWidget = FilterUtils.getFilters(
                Collections.singletonMap(FiltersConstants.INDICATOR_DISAGGREGATION, Arrays.asList(11, 21)),
                new AmpReportFilters((AmpFiscalCalendar) null));
        FilterRule widgetRule = rule(fromWidget);
        assertEquals(new HashSet<>(Arrays.asList("11", "21")), new HashSet<>(widgetRule.values));
        assertEquals(FiltersConstants.INDICATOR_DISAGGREGATION,
                FilterUtils.INSTANCE.idFromColumnName(ColumnConstants.INDICATOR_DISAGGREGATION_FILTER));

        AmpARFilter legacy = new AmpARFilter();
        legacy.setIndicatorDisaggregationOptions(new HashSet<>(Arrays.asList(option(11, "Female"), option(21, "Adult"))));
        FilterRule restoredRule = rule(new AmpARFilterConverter(legacy).buildFilters());
        assertEquals(new HashSet<>(widgetRule.values), new HashSet<>(restoredRule.values));
        JsonNode restoredJson = new ObjectMapper().valueToTree(new AmpARFilterConverter(legacy).buildFilters());
        Set<Integer> restoredIds = new HashSet<>();
        restoredJson.get(FiltersConstants.INDICATOR_DISAGGREGATION).forEach(id -> restoredIds.add(id.intValue()));
        assertEquals(new HashSet<>(Arrays.asList(11, 21)), restoredIds);

        Set<AmpFilterData> records = AmpFilterData.createFilterDataSet(new FilterRecords(), legacy).stream()
                .filter(record -> "indicatorDisaggregationOptions".equals(record.getPropertyName()))
                .collect(Collectors.toSet());
        assertEquals(new HashSet<>(Arrays.asList("11", "21")),
                records.stream().map(AmpFilterData::getValue).collect(Collectors.toSet()));
        assertEquals(Collections.singleton(AmpCategoryValue.class.getName()),
                records.stream().map(AmpFilterData::getElementClassName).collect(Collectors.toSet()));
    }

    private FilterRule rule(AmpReportFilters filters) {
        return filters.getAllFilterRules().get(
                new ReportElement(new ReportColumn(ColumnConstants.INDICATOR_DISAGGREGATION_FILTER)));
    }

    private AmpCategoryValue option(long id, String label) {
        AmpCategoryValue option = new AmpCategoryValue();
        option.setId(id);
        option.setValue(label);
        return option;
    }

    private static class FilterRecords implements FilterDataSetInterface<AmpFilterData> {
        private Set<AmpFilterData> records;
        @Override public void setFilterDataSet(Set<AmpFilterData> filterDataSet) { records = filterDataSet; }
        @Override public Set<AmpFilterData> getFilterDataSet() { return records; }
        @Override public AmpFilterData newAmpFilterData(FilterDataSetInterface<AmpFilterData> owner,
                String property, String propertyClass, String elementClass, String value) {
            return new AmpFilterData(owner, property, propertyClass, elementClass, value);
        }
    }
}
