package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;

import java.util.List;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 * A resource reference provides default implementation of AbstractFileUploadResource.
 * The implementation generates JSON response as expected by the demo at
 * <a href="http://blueimp.github.com/jQuery-File-Upload/">http://blueimp.github.com/jQuery-File-Upload</a>
 */
public class FileUploadResourceReference extends ResourceReference
{

    private FileManager fileManager;

    public FileUploadResourceReference(String activityId, IModel<FileItem> fileItemModel) {
        super(FileUploadResourceReference.class, "file-upload-"+System.currentTimeMillis());

        this.fileManager = new FileManager(activityId, fileItemModel);
    }

    @Override
    public IResource getResource() {
        return new AbstractFileUploadResource(fileManager) {
            @Override
            protected String generateJsonResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest, List<FileItem> files) {
                JSONArray json = new JSONArray();

                for (FileItem fileItem : files) {
                    JSONObject fileJson = new JSONObject();

                    try {
                        fileJson.put("name", fileItem.getName());
                        fileJson.put("size", fileItem.getSize());
                        fileJson.put("uploadTxt", TranslatorUtil.getTranslatedText("File") + " '"
                                + fileItem.getName() + "' " +  TranslatorUtil.
                                getTranslatedText("was uploaded, with size") + " '" + fileItem.getSize()
                                + "'" + TranslatorUtil.getTranslatedText("bytes"));

                    } catch (JSONException e) {
                        try {
                            fileJson.put("error", e.getMessage());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    json.put(fileJson);
                }
                return json.toString();
            }

            @Override
            protected String generateHtmlResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest, List<FileItem> files) {
                String jsonResponse = generateJsonResponse(resourceResponse, webRequest, files);
                String escapedJson = escapeHtml(jsonResponse);
                return escapedJson;
            }
        };
    }
}
