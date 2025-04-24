package org.dgfoundation.amp.onepager.components.fields;

import net.sf.json.JSONObject;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;

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
    public ImageUploadValidationBehavior(String inputId, String previewId, String noImageId, String errorId) {
    this.inputId = inputId;
    this.previewId = previewId;
    this.noImageId=noImageId;
    this.errorId=errorId;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        // Reference the external JS file
        response.render(JavaScriptHeaderItem.forReference(
                new PackageResourceReference(this.getClass(), "image-upload-validation.js")));

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

        // Generate and render the initialization script
        String script = String.format("jQuery(function() { setupImageUploadValidation(%s); });",
                options.toString());
        response.render(OnDomReadyHeaderItem.forScript(script));
    }

}
