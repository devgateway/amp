package org.digijava.kernel.ampregistry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Octavian Ciubotaru
 */
public class AmpRegistryServiceTest {

    private static final String TEST_SECRET_TOKEN = "testSecretToken";

    private static final AmpInstallation MD = new AmpInstallation(
            null,
            ImmutableMap.of("en", "Moldova"),
            "MD",
            "1",
            ImmutableList.of("https://amp.gov.md/"));

    private static final long MD_13_ID = 13L;

    private static final AmpInstallation MD_13 = new AmpInstallation(
            MD_13_ID,
            ImmutableMap.of("en", "Moldova", "fr", "Moldavie"),
            "MD",
            "1",
            ImmutableList.of("https://amp.gov.md/"));

    private static final long MD_14_ID = 14L;

    private static final AmpInstallation MD_14 = new AmpInstallation(
            MD_14_ID,
            ImmutableMap.of("en", "Moldova"),
            "MD",
            "1",
            ImmutableList.of("https://amp.gov.md/"));

    private AmpRegistryService ampRegistryService;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AmpRegistryClient ampRegistryClient;

    @Before
    public void setup() {
        ampRegistryService = new AmpRegistryService(() -> ampRegistryClient);
    }

    @Test
    public void testCreation() {
        when(ampRegistryClient.listAmpInstallations(TEST_SECRET_TOKEN)).thenReturn(ImmutableList.of());

        ampRegistryService.register(MD, TEST_SECRET_TOKEN);

        verify(ampRegistryClient).createAmpInstallation(MD, TEST_SECRET_TOKEN);
        verify(ampRegistryClient, never()).updateAmpInstallation(any(), any(), any());
    }

    @Test
    public void testUpdate() {
        when(ampRegistryClient.listAmpInstallations(TEST_SECRET_TOKEN)).thenReturn(ImmutableList.of(MD_13));

        ampRegistryService.register(MD, TEST_SECRET_TOKEN);

        verify(ampRegistryClient, never()).createAmpInstallation(any(), any());
        verify(ampRegistryClient).updateAmpInstallation(MD_13_ID, MD, TEST_SECRET_TOKEN);
    }

    @Test
    public void testAlreadyUpToDate() {
        when(ampRegistryClient.listAmpInstallations(TEST_SECRET_TOKEN)).thenReturn(ImmutableList.of(MD_14));

        ampRegistryService.register(MD, TEST_SECRET_TOKEN);

        verify(ampRegistryClient, never()).createAmpInstallation(any(), any());
        verify(ampRegistryClient, never()).updateAmpInstallation(any(), any(), any());
    }

    @Test
    public void testDestroy() {
        when(ampRegistryClient.listAmpInstallations(TEST_SECRET_TOKEN)).thenReturn(ImmutableList.of());

        ampRegistryService.register(MD, TEST_SECRET_TOKEN);

        verify(ampRegistryClient).destroy();
    }

    @Test(expected = RuntimeException.class)
    public void testDestroyOnException() {
        when(ampRegistryClient.listAmpInstallations(TEST_SECRET_TOKEN)).thenThrow(new RuntimeException());

        ampRegistryService.register(MD, TEST_SECRET_TOKEN);

        verify(ampRegistryClient).destroy();
    }
}
