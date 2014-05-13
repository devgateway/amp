package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.upload.FileItem;

/**
 *
 * @author aartimon@developmentgateway.org
 * @since 26 Mar 2013
 */
public class FileUploadPanel extends Panel {

    public FileUploadPanel(String id, String activityId, IModel<FileItem> fileItemModel) {
        super(id);
        add(new FileUploadBehavior(activityId, fileItemModel));
    }
}
