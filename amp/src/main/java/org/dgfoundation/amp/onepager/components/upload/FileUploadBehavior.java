package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Contributes all CSS/JS resources needed by http://blueimp.github.com/jQuery-File-Upload/
 */
public class FileUploadBehavior extends Behavior {
    private final String activityId;
    private final IModel<FileItem> fileItemModel;
    /**
     * The name of the request parameter used for the multipart
     * Ajax request
     */
    public static final String PARAM_NAME = "FILE-UPLOAD";

    public FileUploadBehavior(String activityId, IModel<FileItem> fileItemModel) {
        this.activityId = activityId;
        this.fileItemModel = fileItemModel;
    }

    /**
     * Configures the connected component to render its markup id
     * because it is needed to initialize the JavaScript widget.
     * @param component
     */
    @Override
    public void bind(Component component) {
        super.bind(component);
        component.setOutputMarkupId(true);
    }


    @Override
    public void renderHead(final Component component, IHeaderResponse response) {
        super.renderHead(component, response);

//        response.render(CssHeaderItem.forReference(
//                new CssResourceReference(FileUploadBehavior.class, "jquery.fileupload-ui.css")));
//        response.render(JavaScriptHeaderItem.forReference(
//                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.fileupload-ui.js")));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.ui.widget.js"), String.valueOf(System.currentTimeMillis())+"a", true));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.iframe-transport.js"), String.valueOf(System.currentTimeMillis())+"b", true));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.fileupload.js"), String.valueOf(System.currentTimeMillis())+"c", true));

        String uploadUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(component.urlFor(new FileUploadResourceReference(activityId, fileItemModel), null).toString()));
        String markupId = component.getMarkupId();
        
        String maxFileSizeGS = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE);
        
        final Map<String, CharSequence> variables = new HashMap<String, CharSequence>();
        variables.put("componentMarkupId", markupId);
        uploadUrl +="?activityId=" + activityId;
        variables.put("url", uploadUrl);
        variables.put("paramName", PARAM_NAME);
        variables.put("uploadFailedMsg", TranslatorUtil.getTranslatedText("Upload failed! Please try again."));
        variables.put("uploadStartedMsg", TranslatorUtil.getTranslatedText("Upload started, please wait..."));
        variables.put("uploadFailedTooBigMsg", TranslatorUtil.getTranslatedText("The file size limit is {size} MB. This file exceeds the limit.").replace("{size}", maxFileSizeGS));
        variables.put("uploadMaxFileSize", Long.toString(Bytes.megabytes(Long.parseLong(maxFileSizeGS)).bytes()));
        variables.put("uploadNoFileLabel", TranslatorWorker.translateText("No file chosen"));

        IModel variablesModel = new AbstractReadOnlyModel() {
            public Map getObject() {
                return variables;
            }
        };
        response.render(JavaScriptHeaderItem.forReference(
                new TextTemplateResourceReference(FileUploadBehavior.class, "FileUploadBehavior.js", variablesModel), String.valueOf(System.currentTimeMillis()), true));
        response.render(OnLoadHeaderItem.forScript("setupFileUpload('#" + markupId + "', '" + uploadUrl + "', '" + PARAM_NAME + "');"));
    }
}
