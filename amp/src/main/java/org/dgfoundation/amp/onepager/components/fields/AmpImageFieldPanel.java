package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class AmpImageFieldPanel extends AmpFieldPanel<List<FileUpload>> {

    private FileUploadField fileUploadField;

    public AmpImageFieldPanel(String id, IModel<List<FileUpload>> model, String fmName,
                              boolean hideLabel, boolean hideNewLine,
                              boolean showReqStarForNotReqComp) {
        super(id, model, fmName, hideLabel, hideNewLine, showReqStarForNotReqComp);

        setOutputMarkupId(true);


        // Label
        Label label = new Label("label", Model.of("Choose Image"));
        add(label);

        // File input
        fileUploadField = new FileUploadField("fileUpload", model);
        fileUploadField.setOutputMarkupId(true);
        fileUploadField.setMarkupId("projectThumbnailInput");

        fileUploadField.add(new ImageUploadValidationBehavior(
                "projectThumbnailInput",
                "projectThumbnailInputPreview",
                "projectThumbnailInputNoImage",
                "projectThumbnailInputError"
        ));

        add(fileUploadField);

        // Preview image
        Image previewImage = new Image("previewImage", Model.of(""));
        previewImage.setOutputMarkupId(true);
        previewImage.setMarkupId("projectThumbnailInputPreview");
        previewImage.add(new AttributeModifier("style", "display:none;"));
        add(previewImage);

        // "No image chosen"
        Label noImage = new Label("noImage", Model.of("No Image Chosen"));
        noImage.setOutputMarkupId(true);
        noImage.setMarkupId("projectThumbnailInputNoImage");
        add(noImage);
    }

    public FileUpload getSelectedFileUpload() {
        return fileUploadField.getFileUpload();
    }

}
