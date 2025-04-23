package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AmpImageFieldPanel extends AmpFieldPanel<List<FileUpload>>{

    private FileUploadField fileUploadField;
    private WebMarkupContainer previewContainer;
    private NonCachingImage previewImage;
    private Label noImageLabel;

    public AmpImageFieldPanel(String id, String fmName){
        super(id, fmName);
    }
    public AmpImageFieldPanel(String id, IModel<List<FileUpload>> model, String fmName){
        super(id, model, fmName);
        initComponents();
    }
    public AmpImageFieldPanel(String id, IModel<List<FileUpload>> model, String fmName,
                         boolean hideLabel, boolean hideNewLine,
                         final boolean showReqStarForNotReqComp) {
    super(id, model, fmName, hideLabel, hideNewLine, showReqStarForNotReqComp);
    initComponents();
    }

    private void initComponents() {
        // File upload input
        fileUploadField = new FileUploadField("imageUpload", getModel());
//        fileUploadField.setRequired(isRequired);
        fileUploadField.add(new ImageFileValidator());

        // Image preview
        previewImage = new NonCachingImage("previewImage", Model.of((IResourceStream) null));
        previewImage.setOutputMarkupPlaceholderTag(true);
        previewImage.setVisible(false);

        previewContainer = new WebMarkupContainer("previewContainer");
        previewContainer.setOutputMarkupId(true);
        previewContainer.setOutputMarkupPlaceholderTag(true);
        previewContainer.add(previewImage);
        previewContainer.setVisible(false);

        // No-image label
        noImageLabel = new Label("noImageMsg","No Image Chosen" );
        noImageLabel.setOutputMarkupId(true);
        noImageLabel.setOutputMarkupPlaceholderTag(true);

        add(fileUploadField, previewContainer, noImageLabel);
        FeedbackPanel fieldFeedback = new FeedbackPanel("imageUploadFeedback", new ContainerFeedbackMessageFilter(fileUploadField));
        fieldFeedback.setOutputMarkupId(true);
        add(fieldFeedback);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        FileUpload upload = fileUploadField.getFileUpload();
        if (upload != null) {
            previewImage.setImageResource(new ByteArrayResource(
                    upload.getContentType(),
                    upload.getBytes()
            ));
            previewContainer.setVisible(true);
            previewImage.setVisible(true);
            noImageLabel.setVisible(false);
        } else {
            previewContainer.setVisible(false);
            noImageLabel.setVisible(true);
        }
    }


    private static class ImageFileValidator implements IValidator<List<FileUpload>> {
        @Override
        public void validate(IValidatable<List<FileUpload>> validatable) {
            List<FileUpload> uploads = validatable.getValue();
            if (uploads == null || uploads.isEmpty()) {
                return;
            }

            FileUpload upload = uploads.get(0);
            String contentType = upload.getContentType();

            if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType)) {
                validatable.error(new ValidationError("Only PNG and JPG images are allowed."));            }

            if (upload.getSize() > 50 * 1024) {
                validatable.error(new ValidationError("Image size must be under 50KB."));
            }

            try (InputStream in = upload.getInputStream()) {
                BufferedImage image = ImageIO.read(in);
                if (image == null || image.getWidth() > 100 || image.getHeight() > 100) {
                    validatable.error(new ValidationError("Image must be 100x100 pixels or smaller."));

                }
            } catch (IOException e) {
                validatable.error(new ValidationError("Failed to read uploaded image."));

            }
        }
    }

}
