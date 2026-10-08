package org.dgfoundation.amp.onepager.components.features;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.components.upload.FileUploadPanel;

public class FileUploadModalPanel extends Panel {

    public FileUploadModalPanel(String id, ModalWindow modalWindow) {
        super(id);

        Form<?> form = new Form<>("uploadForm");
        IModel<FileItem> fileItemModel = new Model<>();
        form.add(new FileUploadPanel("fileUploadField", "new", fileItemModel));

        form.add(new AjaxSubmitLink("uploadButton", form) {
            protected void onSubmit(AjaxRequestTarget target) {
                if (fileItemModel.getObject() != null) {
                    modalWindow.close(target);
                }
            }
        });

        add(form);
    }
}
