package org.dgfoundation.amp.aitranslation;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;
import java.util.function.Supplier;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.translate.v3.BatchTranslateTextRequest;
import com.google.cloud.translate.v3.GcsDestination;
import com.google.cloud.translate.v3.GcsSource;
import com.google.cloud.translate.v3.InputConfig;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.OutputConfig;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.dgfoundation.amp.nireports.NiUtils;

/**
 * This is a translation service that uses Google Translation API to do the actual translation. Additionally this
 * service uses Google Cloud Storage API since this is required for using the batch translation calls.
 *
 * The corresponding google project must have the following APIs enabled:
 *  Cloud Translation API
 *
 * For this client to work, a Service Account has to be created in Google Cloud Console with the following roles:
 *  Project / Editor (roles/editor)
 *
 * @author Octavian Ciubotaru
 */
public class GoogleMachineTranslationService implements MachineTranslationService {

    /**
     * Batch translation works only in us-central1 location. Using this location for Google Cloud Storage as well.
     */
    private static final String LOCATION = "us-central1";

    private static final int MAX_RETRIES = 10;

    private static final int FAST_TRANSLATE_CODE_POINT_LIMIT = 30000;

    private String projectId;

    private Storage storage;
    private String bucketName;
    private TranslationServiceClient client;
    private Boolean errorOnInit;

    /**
     * Initialize service is not yet initialized. Will not attempt to initialize second time if the first
     * initialization was unsuccessful.
     */
    private synchronized void initIfNeeded() {
        if (errorOnInit == null) {
            try {
                init();
                errorOnInit = false;
            } catch (RuntimeException e) {
                errorOnInit = true;
                throw e;
            }
        } else if (errorOnInit) {
            throw new RuntimeException("Failed to initialize translation service.");
        }
    }

    /**
     * Initialize the service.
     *
     * Search translations bucket and if not yet present creates one. Bucket name must be unique and it's scope is
     * global. Using retry mechanism to create a bucket until one succeeds.
     */
    private void init() {
        try {
            projectId = Objects.requireNonNull(System.getenv("GOOGLE_PROJECT_ID"),
                    "GOOGLE_PROJECT_ID environment variable was not set");

            storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

            String trnBucketKey = "translations";
            String trnBucketValue = "true";

            Page<Bucket> buckets = storage.list();
            for (Bucket b : buckets.iterateAll()) {
                if (trnBucketValue.equals(b.getLabels().get(trnBucketKey))) {
                    bucketName = b.getName();
                    break;
                }
            }
            while (bucketName == null) {
                bucketName = withRetry(() -> storage.create(BucketInfo.newBuilder(UUID.randomUUID().toString())
                        .setLocation(LOCATION)
                        .setLabels(ImmutableMap.of(trnBucketKey, trnBucketValue))
                        .setStorageClass(StorageClass.STANDARD)
                        .build()).getName(),
                        e -> e.getCode() == HttpStatus.SC_CONFLICT && "conflict".equals(e.getReason()));
            }

            client = TranslationServiceClient.create();
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException("Failed to initialize translation service. " + e.getMessage(), e);
        }
    }

    /**
     * Translates a large amount of text asynchronously.
     *
     * When a content cannot be translated, then it will not be present in the resulting map.
     *
     * @param srcLang source language, required, 2 letter code
     * @param destLang destination language, required, 2 letter code
     * @param contents all contents to be translated, cannot have null or empty values
     * @return future with the result of the translation
     */
    @Override
    public CompletableFuture<Map<String, String>> translate(String srcLang, String destLang, List<String> contents) {
        Objects.requireNonNull(srcLang);
        Objects.requireNonNull(destLang);
        Objects.requireNonNull(contents);
        NiUtils.failIf(contents.stream().anyMatch(StringUtils::isEmpty),
                "contents cannot have null or empty strings");

        initIfNeeded();

        if (countCodePoints(contents) < FAST_TRANSLATE_CODE_POINT_LIMIT) {
            return CompletableFuture
                    .supplyAsync(() -> translateText(srcLang, destLang, contents));
        } else {
            return CompletableFuture
                    .supplyAsync(() -> uploadSourceFile(contents))
                    .thenCompose(id -> batchTranslateText(srcLang, destLang, id)
                            .whenCompleteAsync((r, e) -> deleteSourceFile(id)))
                    .thenApplyAsync(this::readTranslatedContent);
        }
    }

    private int countCodePoints(List<String> contents) {
        return contents.stream()
                .mapToInt(s -> s.codePointCount(0, s.length()))
                .sum();
    }

    /**
     * Invoke Google API's translateText operation. Much faster than batch translation.
     */
    private Map<String, String> translateText(String srcLang, String destLang, List<String> contents) {

        LocationName parent = LocationName.of(projectId, LOCATION);

        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setSourceLanguageCode(srcLang)
                .setTargetLanguageCode(destLang)
                .addAllContents(contents)
                .build();

        TranslateTextResponse response = client.translateText(request);

        Map<String, String> translated = new HashMap<>();
        for (int i = 0; i < contents.size(); i++) {
            translated.put(contents.get(i), response.getTranslations(i).getTranslatedText());
        }
        return translated;
    }

    /**
     * Find the file with the translated content and read the result of the translation. If some of the content
     * couldn't be translated, then it will be missing in resulting map.
     *
     * @param id unique id used for one batch translation invocation
     * @return an array of translated content items, has the same size as the input
     */
    private Map<String, String> readTranslatedContent(String id) {
        Map<String, String> res = ImmutableMap.of();

        Page<Blob> outBlobs = storage.list(bucketName, Storage.BlobListOption.prefix(id));
        for (Blob b : outBlobs.iterateAll()) {
            if (b.getName().endsWith("_translations.tsv")) {
                List<String[]> strings = readAllFromTSV(b.getContent());
                res = strings.stream()
                        .filter(r -> StringUtils.isNotEmpty(r[2]))
                        .collect(toMap(r -> r[1], r -> r[2]));
            }
            b.delete();
        }

        return res;
    }

    /**
     * Parse the whole content of the TSV represented by the bytes. Reading with UTF-8 encoding.
     *
     * @param bytes bytes to be parsed, UTF-8 encoded
     * @return all lines
     */
    private List<String[]> readAllFromTSV(byte[] bytes) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                new ByteArrayInputStream(bytes), StandardCharsets.UTF_8), '\t')) {
            return csvReader.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file.", e);
        }
    }

    /**
     * Upload content to be translated to Google Cloud Storage. Since each file name must be unique using random UUID
     * as file name. Retry logic is used to create another file if a file with such UUID is already present.
     *
     * @param contents content to be translated
     * @return unique id of the translation
     */
    private String uploadSourceFile(List<String> contents) {
        byte[] bytes = getContentsAsTSVBytes(new HashSet<>(contents));

        return withRetry(() -> {
            String newId = UUID.randomUUID().toString();
            BlobInfo inBlobInfo = BlobInfo.newBuilder(bucketName, newId + ".tsv").build();
            storage.create(inBlobInfo, bytes, Storage.BlobTargetOption.doesNotExist());
            return newId;
        }, e -> e.getCode() == HttpStatus.SC_PRECONDITION_FAILED && "conditionNotMet".equals(e.getReason()));
    }

    /**
     * Try to compute a value using a retry logic. Will try up to 10 times and if all attepmts failed will raise an
     * exception with last known exception as the cause.
     *
     * @param supplier supplier for the value
     * @param predicate retry to compute value only if exception matches this predicate
     * @param <T> value type
     * @return computed value
     */
    private <T> T withRetry(Supplier<T> supplier, Predicate<StorageException> predicate) {
        StorageException last = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                return supplier.get();
            } catch (StorageException e) {
                last = e;
                if (!predicate.test(e)) {
                    throw e;
                }
            }
        }
        throw new RuntimeException("Failed to execute logic with retry after 10 times.", last);
    }

    /**
     * Convert contents list to a TSV format accepted by Google's batch translation operation.
     *
     * @param contents array of contents
     * @return content to be translated in TSV format
     */
    private byte[] getContentsAsTSVBytes(Collection<String> contents) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8), '\t');
            writer.writeAll(contents.stream().map(c -> new String[]{c}).collect(toList()));
            writer.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Invoke Google API's batchTranslateText operation. Once completed, the corresponding TSV file with translated
     * text will be present in Google Cloud Storage.
     *
     * @param sourceLanguage source language, required, 2 letter code
     * @param targetLanguage target language, required, 2 letter code
     * @param id unique id of this translation request, determines the source and target paths
     * @return a completable future that once complete will return translated texts
     */
    private CompletableFuture<String> batchTranslateText(String sourceLanguage, String targetLanguage, String id) {
        try {
            String inputUri = String.format("gs://%s/%s.tsv", bucketName, id);
            String outputUri = String.format("gs://%s/%s/", bucketName, id);

            // Supported Locations: `us-central1`
            LocationName parent = LocationName.of(projectId, LOCATION);

            GcsSource gcsSource = GcsSource.newBuilder().setInputUri(inputUri).build();

            InputConfig inputConfig =
                    InputConfig.newBuilder().setGcsSource(gcsSource).setMimeType("text/plain").build();

            GcsDestination gcsDestination =
                    GcsDestination.newBuilder().setOutputUriPrefix(outputUri).build();
            OutputConfig outputConfig =
                    OutputConfig.newBuilder().setGcsDestination(gcsDestination).build();

            BatchTranslateTextRequest request = BatchTranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setSourceLanguageCode(sourceLanguage)
                    .addTargetLanguageCodes(targetLanguage)
                    .addInputConfigs(inputConfig)
                    .setOutputConfig(outputConfig)
                    .build();

            return apiToCompletableFuture(client.batchTranslateTextAsync(request), id);
        } catch (RuntimeException e) {
            deleteSourceFile(id);
            throw e;
        }
    }

    /**
     * Delete file used to hold text to be translated.
     *
     * @param id unique id of the translation request
     */
    private void deleteSourceFile(String id) {
        storage.delete(bucketName, id + ".tsv");
    }

    /**
     * Convert ApiFuture to CompletableFuture. Resulting future will return the id in case the original future
     * completes successfully.
     *
     * @param apiFuture api future to convert
     * @param id used as return value for completable future
     * @return completable future
     */
    private CompletableFuture<String> apiToCompletableFuture(ApiFuture<?> apiFuture, String id) {
        CompletableFuture<String> cf = new CompletableFuture<>();

        apiFuture.addListener(() -> {
            try {
                apiFuture.get();
                cf.complete(id);
            } catch (CancellationException e) {
                cf.cancel(false);
            } catch (InterruptedException e) {
                cf.completeExceptionally(e);
            } catch (ExecutionException e) {
                cf.completeExceptionally(e.getCause());
            }
        }, ForkJoinPool.commonPool());

        return cf;
    }
}
