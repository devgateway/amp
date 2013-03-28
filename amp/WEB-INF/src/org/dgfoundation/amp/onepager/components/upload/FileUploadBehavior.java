package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Contributes all CSS/JS resources needed by http://blueimp.github.com/jQuery-File-Upload/
 */
public class FileUploadBehavior extends Behavior {
    private String activityId;
    private Model<FileItem> fileItemModel;
    /**
     * The name of the request parameter used for the multipart
     * Ajax request
     */
    public static final String PARAM_NAME = "FILE-UPLOAD";

    public FileUploadBehavior(String activityId, Model<FileItem> fileItemModel) {
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

        response.render(CssHeaderItem.forReference(
                new CssResourceReference(FileUploadBehavior.class, "jquery.fileupload-ui.css")));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.ui.widget.js")));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.iframe-transport.js")));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.fileupload.js")));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.fileupload-ui.js")));

        String uploadUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(component.urlFor(new FileUploadResourceReference(activityId, fileItemModel), null).toString()));
        String markupId = component.getMarkupId();
        final Map<String, CharSequence> variables = new HashMap<String, CharSequence>();
        variables.put("componentMarkupId", markupId);
        uploadUrl +="?activityId=" + activityId;
        variables.put("url", uploadUrl);
        variables.put("paramName", PARAM_NAME);
        variables.put("uploadFailedMsg", TranslatorUtil.getTranslatedText("Upload failed! Please try again."));
        variables.put("uploadStartedMsg", TranslatorUtil.getTranslatedText("Upload started, please wait ..."));

        IModel variablesModel = new AbstractReadOnlyModel() {
            public Map getObject() {
                return variables;
            }
        };
        response.render(JavaScriptHeaderItem.forReference(
                new TextTemplateResourceReference(FileUploadBehavior.class, "FileUploadBehavior.js", variablesModel)));
    }
}