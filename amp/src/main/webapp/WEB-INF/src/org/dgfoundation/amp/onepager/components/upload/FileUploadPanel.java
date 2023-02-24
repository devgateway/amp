package org.dgfoundation.amp.onepager.components.upload;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.upload.FileItem;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 *
 * @author aartimon@developmentgateway.org
 * @since 26 Mar 2013
 */
public class FileUploadPanel extends Panel {

    public FileUploadPanel(String id, String activityId, IModel<FileItem> fileItemModel) {
        super(id);
        add (new Label("chooseFileLabel", TranslatorWorker.translateText("Choose file")));
        add (new Label("noFileLabel", TranslatorWorker.translateText("No file chosen")));
        add(new FileUploadBehavior(activityId, fileItemModel));
    }
}
