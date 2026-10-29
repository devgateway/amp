package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpIndicatorDisaggregationValueDto;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ThirdDimensionUpdateTest {
    private HttpServletRequest previousRequest;

    @BeforeEach
    void setUpRequest() {
        previousRequest = TLSUtils.getThreadLocalInstance().request;
        TLSUtils.getThreadLocalInstance().request = mock(HttpServletRequest.class);
    }

    @AfterEach
    void restoreRequest() {
        TLSUtils.getThreadLocalInstance().request = previousRequest;
    }

    @Test
    void allowsAddingThirdDimensionWithoutMovingUnallocatedValues() {
        AmpIndicatorDisaggregationValue existing = existing(1L, null);
        assertDoesNotThrow(() -> IndicatorManagerService.validateThirdDimensionUpdate(Collections.singleton(existing),
                new HashSet<>(Arrays.asList(submitted(1L, null), submitted(null, 30L))), true));
    }

    @Test
    void rejectsMovingAnExistingValueToTheThirdDimension() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                Collections.singleton(existing(1L, null)), Collections.singleton(submitted(1L, 30L)), true));
    }

    @Test
    void rejectsAnOldClientOmittingThirdDimensionRows() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                new HashSet<>(Arrays.asList(existing(1L, null), existing(2L, 30L))),
                Collections.singleton(submitted(1L, null)), false));
    }

    @Test
    void rejectsAnOldClientClearingAThirdCategory() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                Collections.singleton(existing(2L, 30L)), Collections.singleton(submitted(2L, null)), false));
    }

    @Test
    void retainsExistingTwoDimensionUpdateBehaviour() {
        assertDoesNotThrow(() -> IndicatorManagerService.validateThirdDimensionUpdate(
                Collections.singleton(existing(1L, null)), Collections.emptySet(), false));
    }

    @Test
    void preservesUnallocatedValuesWhenSelectingAnEmptyThirdCriterion() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                Collections.singleton(existing(1L, null)), Collections.emptySet(), true));
    }

    @Test
    void rejectsDuplicateCombinationsForAThirdCriterion() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                Collections.singleton(existing(1L, null)),
                new HashSet<>(Arrays.asList(submitted(1L, null), submitted(null, null))), true));
    }

    @Test
    void rejectsForeignIdsForAThirdCriterion() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                Collections.singleton(existing(1L, null)),
                new HashSet<>(Arrays.asList(submitted(1L, null), submitted(99L, 30L))), true));
    }

    @Test
    void rejectsAmbiguousExistingCombinationsWithoutAnId() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionUpdate(
                new HashSet<>(Arrays.asList(existing(1L, null), existing(2L, null))),
                Collections.singleton(submitted(null, null)), true));
    }

    @Test
    void rejectsMoreThanThreeCriteria() {
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateDisaggregationCriteriaCount(
                Arrays.asList(1L, 2L, 3L, 4L)));
        assertDoesNotThrow(() -> IndicatorManagerService.validateDisaggregationCriteriaCount(
                Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    void requiresOptionsFromDistinctSelectedCriteria() {
        Map<Long, AmpCategoryValue> categories = new HashMap<>();
        for (long id : Arrays.asList(1L, 2L, 3L)) {
            categories.put(id, category(id));
        }
        categories.put(10L, option(10L, 1L));
        categories.put(20L, option(20L, 2L));
        categories.put(30L, option(30L, 3L));
        categories.put(31L, option(31L, 2L));
        Session session = (Session) Proxy.newProxyInstance(Session.class.getClassLoader(),
                new Class<?>[]{Session.class}, (proxy, method, args) -> {
                    if (method.getName().equals("get") && args.length == 2 && args[0] == AmpCategoryValue.class) {
                        return categories.get(args[1]);
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
        assertDoesNotThrow(() -> IndicatorManagerService.validateThirdDimensionOptions(
                Arrays.asList(1L, 2L, 3L), Collections.singleton(submitted(null, 30L)), session));
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionOptions(
                Arrays.asList(1L, 2L, 3L), Collections.singleton(submitted(null, 31L)), session));
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionOptions(
                Arrays.asList(1L, 2L), Collections.singleton(submitted(null, 30L)), session));
        assertThrows(ApiRuntimeException.class, () -> IndicatorManagerService.validateThirdDimensionOptions(
                Arrays.asList(1L, 2L, 3L), Collections.singleton(submitted(null, 999L)), session));
    }

    private static AmpCategoryValue option(Long id, Long criterion) {
        AmpCategoryValue value = category(id);
        AmpCategoryClass optionClass = new AmpCategoryClass();
        optionClass.setKeyName("indicator_disaggregation_" + criterion);
        value.setAmpCategoryClass(optionClass);
        return value;
    }

    private static AmpIndicatorDisaggregationValue existing(Long id, Long thirdId) {
        AmpIndicatorDisaggregationValue value = new AmpIndicatorDisaggregationValue();
        value.setId(id);
        value.setParentCategory(category(10L));
        value.setChildCategory(category(20L));
        value.setThirdCategory(thirdId == null ? null : category(thirdId));
        return value;
    }

    private static AmpCategoryValue category(Long id) {
        AmpCategoryValue category = new AmpCategoryValue();
        category.setId(id);
        return category;
    }

    private static AmpIndicatorDisaggregationValueDto submitted(Long id, Long thirdId) {
        AmpIndicatorDisaggregationValueDto value = new AmpIndicatorDisaggregationValueDto();
        value.setId(id);
        value.setParentCategoryId(10L);
        value.setChildCategoryId(20L);
        value.setThirdCategoryId(thirdId);
        return value;
    }
}
