package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.upload.ImageUploadValidationBehavior;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpProjectThumbnail;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.List;

public class AmpImageFieldPanel extends AmpFieldPanel<AmpProjectThumbnail> {
    public AmpImageFieldPanel(String id, IModel<AmpProjectThumbnail> model, String fmName,
                              boolean hideLabel, boolean hideNewLine,
                              boolean showReqStarForNotReqComp, IModel<AmpActivityVersion> am ) {
        super(id, model, fmName, hideLabel, hideNewLine, showReqStarForNotReqComp);

        setOutputMarkupId(true);


        // Label
        Label label = new Label("label", Model.of("Choose Image"));
        add(label);


        final String[] activityId = {"new"};
        if (am.getObject().getAmpActivityId() != null)
            activityId[0] = Long.toString(am.getObject().getAmpActivityId());
        final IModel<FileItem> fileItemModel = new Model<FileItem>();



        AmpProjectThumbnail existingThumbnail = am.getObject().getProjectThumbnail();



        add(new ImageUploadValidationBehavior(
                "projectThumbnailInput",
                "projectThumbnailInputPreview",
                "projectThumbnailInputNoImage",
                "projectThumbnailInputError",
                activityId[0], fileItemModel,existingThumbnail
        ));


        // Preview image
        Image previewImage = new Image("previewImage", Model.of(""));
        previewImage.setOutputMarkupId(true);
        previewImage.setMarkupId("projectThumbnailInputPreview");
        previewImage.add(new AttributeModifier("style", "display:none;"));
        add(previewImage);

        Label noImage = new Label("noImage", Model.of("No Image Chosen"));
        noImage.setOutputMarkupId(true);
        noImage.setMarkupId("projectThumbnailInputNoImage");
        add(noImage);

        Button button = new Button("submitButton");
        button.add(new AjaxEventBehavior("click") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                System.out.println("Button was clicked via AJAX!");
                System.out.println("FileUploadField: " + fileItemModel.getObject());
                if (fileItemModel.getObject() != null) {
                    System.out.println("FileUploadField: " + fileItemModel.getObject().getName());
                    AmpProjectThumbnail ampProjectThumbnail = new AmpProjectThumbnail();
                    ampProjectThumbnail.setContentType(fileItemModel.getObject().getContentType());
                    ampProjectThumbnail.setImgFile(fileItemModel.getObject().get());
                    ampProjectThumbnail.setImgFileName(fileItemModel.getObject().getName());
                    am.getObject().setProjectThumbnail(ampProjectThumbnail);
                }

            }
        });
        button.setOutputMarkupId(true);
        button.setVisibilityAllowed(true);
        button.add(AttributeAppender.append("style", "display:none;"));
        add(button);


    }


}
