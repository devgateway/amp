package org.digijava.kernel.ampapi.endpoints.common.print;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.print.phantom.PhantomService;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by esoliani on 22/07/16.
 */
public class PrintImageService {

    private static final Logger LOGGER = Logger.getLogger(PrintImageService.class);
    private static final Integer DEFAULT_HEIGHT = 768;
    private static final Integer DEFAULT_WIDTH = 1366;
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_FILENAME_GIS_IMAGE_PNG = "attachment; filename=gis-image.png";
    private static final String NO_CACHE_NO_STORE_MUST_REVALIDATE = "no-cache, no-store, must-revalidate";
    private static final String PNG_EXTENSION = ".png";
    private static final String HTML_EXTENSION = ".html";
    private static final String JS_EXTENSION = ".js";
    private static final int FILE_NAME_LENGTH = 10;
    private static final String ERROR_CREATING_IMAGE = "Error Creating image";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String PRAGMA = "Pragma";
    private static final String NO_CACHE = "no-cache";
    private static final String EXPIRES = "Expires";
    private static final String ZERO = "0";
    private static final String IMAGE = "image";
    private static final String PNG = "png";

    public static final Response createImage(final HtmlContent htmlContent) {
        return internalCreateImage(htmlContent);
    }

    private static Response internalCreateImage(final HtmlContent htmlContent) {
        File tmpContentFile = null;
        File tmpImageFile = null;
        File tmpJsFile = null;
        String errorMessage = ERROR_CREATING_IMAGE;
        try (
            final ByteArrayOutputStream output = new ByteArrayOutputStream()
        ){
            tmpContentFile = createTmpFile(htmlContent.getContent(), HTML_EXTENSION);
            final Integer height = getHeight(htmlContent);
            final Integer width = getWidth(htmlContent);
            tmpImageFile = File.createTempFile(RandomStringUtils.randomAlphabetic(FILE_NAME_LENGTH), PNG_EXTENSION);
            tmpJsFile = createTmpFile(htmlContent.getJavascript(), JS_EXTENSION);
            final String scriptPath = tmpJsFile != null ? tmpJsFile.getAbsolutePath(): "";
            PhantomService.createImage(tmpContentFile.toURI().toString(), tmpImageFile.getAbsolutePath(), width, height, scriptPath);
            Files.copy(tmpImageFile.toPath(), output);
            return createImageBase64Response(output); // this also should change for PDF
        } catch (final Exception e) {
            LOGGER.error(errorMessage, e);
            errorMessage = e.getMessage();
        } finally {
            safeDelete(tmpContentFile);
            safeDelete(tmpImageFile);
            safeDelete(tmpJsFile);
        }
        return Response.serverError().entity(errorMessage).build();
    }

    private static Response createImageBase64Response(final ByteArrayOutputStream output) {
        return Response.ok(Base64.encodeBase64String(output.toByteArray()))
                .type(new MediaType(IMAGE, PNG))
                .header(CONTENT_DISPOSITION, ATTACHMENT_FILENAME_GIS_IMAGE_PNG)
                .header(CACHE_CONTROL, NO_CACHE_NO_STORE_MUST_REVALIDATE)
                .header(PRAGMA, NO_CACHE)
                .header(EXPIRES, ZERO)
                .build();
    }

    private static void safeDelete(final File tmpFile) {
        if(tmpFile != null && tmpFile.exists()) {
            tmpFile.delete();
        }
    }

    private static Integer getWidth(final HtmlContent htmlContent) {
        return htmlContent.getWidth() != null && htmlContent.getWidth() > 0 ? htmlContent.getWidth(): DEFAULT_WIDTH;
    }

    private static Integer getHeight(final HtmlContent htmlContent) {
        return htmlContent.getHeight() != null && htmlContent.getHeight() > 0 ? htmlContent.getHeight(): DEFAULT_HEIGHT;
    }

    private static File createTmpFile(final String content, String extension) throws IOException {
        if (StringUtils.isNotEmpty(content)) {
            final File file = File.createTempFile(RandomStringUtils.randomAlphabetic(FILE_NAME_LENGTH), extension);
            FileUtils.writeStringToFile(file, content, Charsets.UTF_8.displayName());
            return file;
        }
        return null;
    }
}
