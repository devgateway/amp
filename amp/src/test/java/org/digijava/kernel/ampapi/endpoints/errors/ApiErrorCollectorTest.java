package org.digijava.kernel.ampapi.endpoints.errors;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_TEST_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Octavian Ciubotaru
 */
public class ApiErrorCollectorTest {

    @Test
    public void testNoErrors() {
        assertTrue(errorsFor(Void.class).isEmpty());
    }

    @Test
    public void testOneError() {
        assertEquals(errorsFor(OneError.class), singletonList(OneError.ONE_ERROR));
    }

    public static class OneError {
        public static final ApiErrorMessage ONE_ERROR = new ApiErrorMessage(ERROR_CLASS_TEST_ID, 1, "First error");
    }

    @Test
    public void testTwoErrors() {
        assertEquals(errorsFor(TwoErrors.class),
                Arrays.asList(TwoErrors.FIRST_ERROR, TwoErrors.SECOND_ERROR));
    }

    public static class TwoErrors {
        public static final ApiErrorMessage FIRST_ERROR = new ApiErrorMessage(ERROR_CLASS_TEST_ID, 1, "First error");
        public static final ApiErrorMessage SECOND_ERROR = new ApiErrorMessage(ERROR_CLASS_TEST_ID, 2, "Second error");
    }

    @Test
    public void testOtherFieldsDoNotInterfere() {
        assertTrue(errorsFor(DoNotInterfereErrors.class).isEmpty());
    }

    public static class DoNotInterfereErrors {
        private static final ApiErrorMessage PRIVATE_ERROR = new ApiErrorMessage(ERROR_CLASS_TEST_ID, 1, "First error");
        public ApiErrorMessage INSTANCE_ERROR = new ApiErrorMessage(ERROR_CLASS_TEST_ID, 2, "Instance error");
        public static final ApiErrorMessage NULL_ERROR = null;
        public static final Object SIMPLE_OBJECT = new Object();
    }

    @Test
    public void testPickupErrorsFromSuper() {
        assertEquals(errorsFor(ErrorsSubclass.class), singletonList(ErrorsSuper.ONE_ERROR));
    }

    public static class ErrorsSubclass extends ErrorsSuper {
    }

    public static class ErrorsSuper {
        public static final ApiErrorMessage ONE_ERROR = new ApiErrorMessage(ERROR_CLASS_TEST_ID, 1, "First error");
    }

    private List<ApiErrorMessage> errorsFor(Class errorsClass) {
        return new ApiErrorCollector().collect(errorsClass);
    }
}
