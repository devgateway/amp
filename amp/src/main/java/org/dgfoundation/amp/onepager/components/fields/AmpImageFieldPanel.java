package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AmpImageFieldPanel extends AmpFieldPanel<List<FileUpload>> {

    private final FileUploadField fileUploadField;
    private final WebMarkupContainer previewContainer;
    private final NonCachingImage previewImage;
    private final Label fileNameText;
    private final Label chooseImageLabel;

    public AmpImageFieldPanel(String id, String fmName) {
        this(id, null, fmName);
    }

    public AmpImageFieldPanel(String id, IModel<List<FileUpload>> model, String fmName) {
        this(id, model, fmName, false, false, false);
    }

    public AmpImageFieldPanel(String id, IModel<List<FileUpload>> model, String fmName,
                              boolean hideLabel, boolean hideNewLine,
                              boolean showReqStarForNotReqComp) {
        super(id, model, fmName, hideLabel, hideNewLine, showReqStarForNotReqComp);

        // File upload input
        fileUploadField = new FileUploadField("imageUpload", getModel());
        fileUploadField.add(new ImageFileValidator());
        fileUploadField.setOutputMarkupId(true);

        // Choose image label
        chooseImageLabel = new Label("chooseImage", Model.of("Choose Image"));
        chooseImageLabel.setOutputMarkupId(true);
        chooseImageLabel.setOutputMarkupPlaceholderTag(true);

        // Image preview
        previewImage = new NonCachingImage("previewImage", new LoadableDetachableModel<ByteArrayResource>() {
            @Override
            protected ByteArrayResource load() {
                FileUpload upload = getUpload();
                return upload != null ?
                        new ByteArrayResource(upload.getContentType(), upload.getBytes()) :
                        null;
            }
        });
        previewImage.setOutputMarkupPlaceholderTag(true);
        previewImage.setOutputMarkupId(true);


        previewContainer = new WebMarkupContainer("previewContainer");
        previewContainer.setOutputMarkupId(true);
        previewContainer.setOutputMarkupPlaceholderTag(true);
        previewContainer.add(previewImage);

        // File name text
        fileNameText = new Label("fileNameText", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                FileUpload upload = getUpload();
                return upload != null ? upload.getClientFileName() : "No image chosen";
            }
        });
        fileNameText.setOutputMarkupId(true);
        fileNameText.setOutputMarkupPlaceholderTag(true);

        add(chooseImageLabel, fileUploadField, previewContainer, fileNameText);

        FeedbackPanel fieldFeedback = new FeedbackPanel("imageUploadFeedback",
                new ContainerFeedbackMessageFilter(fileUploadField));
        fieldFeedback.setOutputMarkupId(true);
        add(fieldFeedback);
    }

    private FileUpload getUpload() {
        List<FileUpload> uploads = fileUploadField.getFileUploads();
        return uploads != null && !uploads.isEmpty() ? uploads.get(0) : null;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        FileUpload upload = getUpload();
        previewContainer.setVisible(upload != null);
        previewImage.setVisible(upload != null);
    }

    private static class ImageFileValidator implements IValidator<List<FileUpload>> {
        private static final long MAX_FILE_SIZE = 50 * 1024; // 50KB
        private static final int MAX_IMAGE_DIMENSION = 100; // 100px

        @Override
        public void validate(IValidatable<List<FileUpload>> validatable) {
            List<FileUpload> uploads = validatable.getValue();
            if (uploads == null || uploads.isEmpty()) {
                return;
            }

            FileUpload upload = uploads.get(0);
            String contentType = upload.getContentType();

            // Validate content type
            if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType)) {
                validatable.error(new ValidationError("Only PNG and JPG images are allowed."));
                logger.info("Error 1 ");
                return;
            }

            // Validate file size
            if (upload.getSize() > MAX_FILE_SIZE) {
                validatable.error(new ValidationError("Image size must be under 50KB."));
                logger.info("Error 2 ");

                return;
            }

            // Validate image dimensions
            try (InputStream in = upload.getInputStream()) {
                BufferedImage image = ImageIO.read(in);
                if (image == null) {
                    logger.info("Error 3 ");
                    validatable.error(new ValidationError("Invalid image file."));
                } else if (image.getWidth() > MAX_IMAGE_DIMENSION || image.getHeight() > MAX_IMAGE_DIMENSION) {
                    validatable.error(new ValidationError(
                            String.format("Image must be %dx%d pixels or smaller.",
                                    MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)));
                    logger.info("Error 4 ");
                }
            } catch (IOException e) {
                logger.error("Error reading uploaded image.", e);
                validatable.error(new ValidationError("Failed to read uploaded image."));
            }
        }
    }
}
