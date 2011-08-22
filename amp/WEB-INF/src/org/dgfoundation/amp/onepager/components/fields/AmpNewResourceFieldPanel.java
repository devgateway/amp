/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Calendar;
import java.util.HashSet;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpNewResourceFieldPanel extends AmpFeaturePanel {

	public AmpNewResourceFieldPanel(String id, IModel model, String fmName, final AmpResourcesFormTableFeature resourcesList, boolean newResourceIsWebLink) throws Exception {
		super(id, model, fmName, true);
		
		final IModel<TemporaryDocument> td = new Model(new TemporaryDocument());
		
		AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("docTitle", new PropertyModel<String>(td, "title"), "Title");
		AmpTextAreaFieldPanel<String> desc = new AmpTextAreaFieldPanel<String>("docDesc", new PropertyModel<String>(td, "description"), "Description", false);
		AmpTextAreaFieldPanel<String> note = new AmpTextAreaFieldPanel<String>("docNote", new PropertyModel<String>(td, "note"), "Note", false);
		AmpCategorySelectFieldPanel type = new AmpCategorySelectFieldPanel("docType", CategoryConstants.DOCUMENT_TYPE_KEY, new PropertyModel<AmpCategoryValue>(td, "type"), "Type", true, true);
		FileUploadField file = new FileUploadField("file", new PropertyModel<FileUpload>(td, "file"));
		AmpTextFieldPanel<String> webLink = new AmpTextFieldPanel<String>("webLink", new PropertyModel<String>(td, "webLink"), "Web Link", true, true);
		webLink.setVisible(false);
		String resourceLabelModel = "File";
		if (newResourceIsWebLink)
			resourceLabelModel = "Web Link";
			
		TrnLabel resourceLabel = new TrnLabel("resourceLabel", resourceLabelModel);

		// create the form
        Form<?> form = new Form<Void>("form")
        {
            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit()
            {
            	TemporaryDocument tmp = td.getObject();
            	if (tmp.getFile() == null && tmp.getWebLink() == null)
            		return;
 
            	if (tmp.getFile() != null){
            		double fSize = tmp.getFile().getSize()/(1024*1024);
            		tmp.setFileSize(fSize);
            		tmp.setFileName(tmp.getFile().getClientFileName());
            		tmp.setContentType(tmp.getFile().getContentType());
            	}
            	
            	if (tmp.getWebLink() != null){
            		tmp.setFileName(tmp.getWebLink());
            	}
            	
            	tmp.setDate(Calendar.getInstance());
            	tmp.setYear(String.valueOf((tmp.getDate()).get(Calendar.YEAR)));
            	HashSet<TemporaryDocument> newItemsSet = getSession().getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
            	if (newItemsSet == null){
            		newItemsSet = new HashSet<TemporaryDocument>();
            		getSession().setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, newItemsSet);
            	}
            	newItemsSet.add(tmp);
            	td.setObject(new TemporaryDocument());
            }
        };
        //form.setMaxSize(Bytes.megabytes(1));
        add(form);

        if (newResourceIsWebLink){
        	file.setVisible(false);
        	webLink.setVisible(true);
        }
        
		form.add(name);
		form.add(desc);
		form.add(note);
		form.add(type);
		form.add(file);
		form.add(resourceLabel);
		form.add(webLink);
		
        // create the ajax button used to submit the form
        form.add(new AjaxButton("ajaxSubmit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	target.addComponent(form);
            	target.addComponent(resourcesList);
            }
        });
	}
}
