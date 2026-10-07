package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
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
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Contributes all CSS/JS resources needed by http://blueimp.github.com/jQuery-File-Upload/
 */
public class FileUploadBehavior extends Behavior {
    private final String activityId;
    private final IModel<FileItem> fileItemModel;
    private final boolean deferUpload;
    private final String importButtonMarkupId;
    private final String importedRowsMarkupId;
    private final String uploadUrlOverride;
    /**
     * The name of the request parameter used for the multipart
     * Ajax request
     */
    public static final String PARAM_NAME = "FILE-UPLOAD";

    public FileUploadBehavior(String activityId, IModel<FileItem> fileItemModel) {
        this(activityId, fileItemModel, null, null, null);
    }

    public FileUploadBehavior(String activityId, IModel<FileItem> fileItemModel, String uploadUrlOverride,
            String importButtonMarkupId, String importedRowsMarkupId) {
        this.activityId = activityId;
        this.fileItemModel = fileItemModel;
        this.uploadUrlOverride = uploadUrlOverride;
        this.deferUpload = importButtonMarkupId != null;
        this.importButtonMarkupId = importButtonMarkupId;
        this.importedRowsMarkupId = importedRowsMarkupId;
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
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.ui.widget.js"), System.currentTimeMillis() +"a", true));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.iframe-transport.js"), System.currentTimeMillis() +"b", true));
        response.render(JavaScriptHeaderItem.forReference(
                new JavaScriptResourceReference(FileUploadBehavior.class, "jquery.fileupload.js"), System.currentTimeMillis() +"c", true));

        String uploadUrl = uploadUrlOverride;
        if (uploadUrl == null) {
            uploadUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(component.urlFor(new FileUploadResourceReference(activityId, fileItemModel), null).toString()));
            uploadUrl = appendQueryParameter(uploadUrl, "activityId", activityId);
            uploadUrl = appendSpringCsrfToken(uploadUrl);
        } else {
            HttpServletRequest request = ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest();
            uploadUrl = request.getContextPath() + uploadUrl;
            uploadUrl = appendSpringCsrfToken(uploadUrl);
        }
        String markupId = component.getMarkupId();
        
        String maxFileSizeGS = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE);
        String uploadParamName = uploadUrlOverride == null ? PARAM_NAME : "file";
        
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("componentMarkupId", markupId);
        variables.put("url", uploadUrl);
        variables.put("paramName", uploadParamName);
        variables.put("uploadFailedMsg", TranslatorUtil.getTranslatedText("Upload failed! Please try again."));
        variables.put("uploadStartedMsg", TranslatorUtil.getTranslatedText("Upload started, please wait..."));
        variables.put("uploadFailedTooBigMsg", TranslatorUtil.getTranslatedText("The file size limit is {size} MB. This file exceeds the limit.").replace("{size}", maxFileSizeGS));
        variables.put("uploadMaxFileSize", Long.toString(Bytes.megabytes(Long.parseLong(maxFileSizeGS)).bytes()));
        variables.put("uploadNoFileLabel", TranslatorWorker.translateText("No file chosen"));

        IModel<Map<String, Object>> variablesModel = new AbstractReadOnlyModel<Map<String, Object>>() {
            @Override
            public Map<String, Object> getObject() {
                return variables;
            }
        };
        response.render(JavaScriptHeaderItem.forReference(
                new TextTemplateResourceReference(FileUploadBehavior.class, "FileUploadBehavior.js", variablesModel), String.valueOf(System.currentTimeMillis()), true));
        response.render(OnLoadHeaderItem.forScript("setupFileUpload('#" + markupId + "', '" + uploadUrl + "', '"
            + uploadParamName + "', " + deferUpload + ", '"
            + (importButtonMarkupId == null ? "" : importButtonMarkupId) + "', '"
            + (importedRowsMarkupId == null ? "" : importedRowsMarkupId) + "');"));
    }

    static String appendSpringCsrfToken(String url) {
        RequestCycle requestCycle = RequestCycle.get();
        if (requestCycle == null || !(requestCycle.getRequest() instanceof ServletWebRequest)) {
            return url;
        }

        HttpServletRequest request = ((ServletWebRequest) requestCycle.getRequest()).getContainerRequest();
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            return url;
        }

        return appendQueryParameter(url, token.getParameterName(), token.getToken());
    }

    private static String appendQueryParameter(String url, String name, String value) {
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + urlEncode(name) + "=" + urlEncode(value);
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
