package org.dgfoundation.amp.onepager.components.features;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import java.io.File;

public class FileUploadModalPanel extends Panel {

    private FileUploadField fileUploadField;

    public FileUploadModalPanel(String id, ModalWindow modalWindow) {
        super(id);

        // Create the form and mark it as multi-part for file uploads
        Form<?> form = new Form<>("uploadForm");
        form.setMultiPart(true);  // Necessary for file upload

        // Add file upload field
        fileUploadField = new FileUploadField("fileUploadField");
        form.add(fileUploadField);

        // Add AjaxSubmitLink to handle file upload via Ajax
        form.add(new AjaxSubmitLink("uploadButton", form) {
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload upload = fileUploadField.getFileUpload();
                if (upload != null) {
                    try {
                        // Save the uploaded file or process it
                        File file = new File("/path/to/save/" + upload.getClientFileName());
                        upload.writeTo(file);
                        System.out.println("File uploaded: " + upload.getClientFileName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Close the modal window after successful upload
                modalWindow.close(target);
            }

            protected void onError(AjaxRequestTarget target) {
                // Handle error scenario here
                System.out.println("File upload failed");
            }
        });

        add(form);
    }
}
