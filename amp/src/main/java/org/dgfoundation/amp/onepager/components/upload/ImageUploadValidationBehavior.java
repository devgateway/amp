package org.dgfoundation.amp.onepager.components.upload;

import net.sf.json.JSONObject;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpProjectThumbnail;

import java.util.Base64;

public class ImageUploadValidationBehavior extends Behavior {
    private static final long serialVersionUID = 1L;

    // Configurable properties with defaults
    private String chooseImageText = TranslatorUtil.getTranslatedText("Choose Image");
    private String noImageText = TranslatorUtil.getTranslatedText("No image chosen");
    private String invalidTypeMessage = TranslatorUtil.getTranslatedText("Only PNG or JPG images are allowed.");
    private String maxSizeMessage = TranslatorUtil.getTranslatedText("Maximum file size is 50KB.");
    private String dimensionsMessage = TranslatorUtil.getTranslatedText("Image must not exceed 100x100 pixels.");
    private long maxSize = 50 * 1024; // 50KB
    private int maxWidth = 100;
    private int maxHeight = 100;
    private String[] validTypes = {"image/png", "image/jpeg"};
    private final String inputId;
    private final String previewId;
    private final String noImageId;
    private final String errorId;
    private String activityId ;
    private IModel<FileItem> fileItemModel;
    private AmpProjectThumbnail projectThumbnail;
    public ImageUploadValidationBehavior(String inputId, String previewId, String noImageId, String errorId,String activityId, IModel<FileItem> fileItemModel, AmpProjectThumbnail projectThumbnail) {
    this.inputId = inputId;
    this.previewId = previewId;
    this.noImageId=noImageId;
    this.errorId=errorId;
    this.activityId=activityId;
    this.fileItemModel= fileItemModel;
    this.projectThumbnail=projectThumbnail;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        // Reference the external JS file

        String uploadUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(component.urlFor(new FileUploadResourceReference(activityId, fileItemModel), null).toString()));
        String markupId = component.getMarkupId();

//        response.render(JavaScriptHeaderItem.forReference(
//                new JavaScriptResourceReference(this.getClass(), "jquery.ui.widget.js"), System.currentTimeMillis() +"a", true));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(this.getClass(), "jquery.iframe-transport.js"), System.currentTimeMillis() +"b", true));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(this.getClass(), "jquery.fileupload.js"), System.currentTimeMillis() +"c", true));

        // Create JSON object with all options
        JSONObject options = new JSONObject();
        options.put("inputId", inputId);
        options.put("previewId", previewId );
        options.put("noImageId", noImageId);
        options.put("errorId",errorId);
        options.put("chooseImageText", chooseImageText);
        options.put("noImageText", noImageText);
        options.put("invalidTypeMessage", invalidTypeMessage);
        options.put("maxSizeMessage", maxSizeMessage);
        options.put("dimensionsMessage", dimensionsMessage);
        options.put("maxSize", maxSize);
        options.put("maxWidth", maxWidth);
        options.put("maxHeight", maxHeight);
        options.put("validTypes", validTypes);
        options.put("markupId", markupId);
        options.put("uploadUrl", uploadUrl);

        // Generate and render the initialization script
        response.render(JavaScriptHeaderItem.forReference(
                new PackageResourceReference(this.getClass(), "image-upload-validation.js")));
        String script = String.format("jQuery(function() { setupImageUploadValidation(%s); });",
                options.toString());
        response.render(OnDomReadyHeaderItem.forScript(script));
        if (projectThumbnail!=null)
        {
            String base64Data = Base64.getEncoder().encodeToString(projectThumbnail.getImgFile());
            String dataUrl = "data:" + projectThumbnail.getContentType() + ";base64," + base64Data;

            String script1 = String.format(
                    "displayExistingImage('%s', '%s', '%s', '%s', '%s', '%s');",
                    dataUrl,
                    projectThumbnail.getContentType(),
                    previewId ,
                   noImageId,
                    inputId,
                    projectThumbnail.getImgFileName()
            );
            response.render(OnDomReadyHeaderItem.forScript(script1));
        }
    }

}
