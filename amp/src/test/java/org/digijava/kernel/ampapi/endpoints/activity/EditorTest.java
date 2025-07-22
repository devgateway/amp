package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.activity.utils.AIHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author Julian de Anquin
 */
public class EditorTest {

    @Test
    public void testParallel() {

        List<CompletableFuture<String>> s = new ArrayList<>(
                Arrays.asList(getEditorKeyAsync(), getEditorKeyAsync())
        );

        List<String> result =
                s.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        assertThat(result.get(0), not(result.get(1)));

    }

    private CompletableFuture<String> getEditorKeyAsync() {
        return CompletableFuture.supplyAsync(() -> AIHelper.getEditorKey("description"));
    }
}