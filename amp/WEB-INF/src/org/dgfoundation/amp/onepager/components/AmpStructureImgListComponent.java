/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components;

import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.lang.Bytes;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureImg;

/**
 * @author mmoras
 * @since Jan 2, 2013
 */
public class AmpStructureImgListComponent<T> extends AmpComponentPanel {
	private static final long serialVersionUID = 1L;

	public AmpStructureImgListComponent(String id, String fmName,
			final IModel<AmpStructure> model) {
		super(id, model, fmName);
		init(model);
	}
	
	private void init(final IModel<AmpStructure> model){
		
		final Component feedback = new FeedbackPanel("feedback").setOutputMarkupPlaceholderTag(true);
		add(feedback);
		final FileUploadField file = new FileUploadField("imgFile");
        Form<?> form = new Form<Void>("addimgform")
        {
            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit()
            {
            }
        };
        form.setMaxSize(Bytes.megabytes(1));
        add(form);

        form.add(file);

		final ListEditor<AmpStructureImg> imgList = createImgList(model);
		add(imgList);        
        
        form.add(new AjaxButton("addimgbutton")
        {
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {	
				//TODO add translations for errors
            	if (file.getFileUploads() == null || file.getFileUploads().isEmpty()){
            		error("You should select an image to add.");
            		target.add(feedback);
            		return;
            	}
            	
				for (FileUpload fileUp : file.getFileUploads()){
					if(fileUp.getContentType() == null || !fileUp.getContentType().startsWith("image/")){
	            		error("The selected file is not an image. Please select an image file");
	            		target.add(feedback);
	            		return;
					}
					
					AmpStructureImg img = new AmpStructureImg();
					img.setImgFile(fileUp.getBytes());
					img.setImgFileName(fileUp.getClientFileName());
					img.setContentType(fileUp.getContentType());
					imgList.addItem(img);
				}
				
				target.add(imgList.getParent());
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(imgList.getParent()));
            }
        });
	}
	
	private ListEditor<AmpStructureImg> createImgList(IModel<AmpStructure> structureModel){
		
		final PropertyModel<Set<AmpStructureImg>> setModel=new PropertyModel<Set<AmpStructureImg>>(structureModel,"images");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpStructureImg>());
		
		final ListEditor<AmpStructureImg> imgList = new ListEditor<AmpStructureImg>("imgList", setModel) {
			
		@Override
		protected void onPopulateItem(ListItem<AmpStructureImg> item) {
			
			final AmpStructureImg modelObject = item.getModelObject();
			DynamicImageResource imgResource = new DynamicImageResource() {
				@Override
				protected byte[] getImageData(Attributes attributes) {
					return modelObject.getImgFile();
				}
			};
			Image img = new Image("imgFileDisplay", imgResource);
			item.add(img);

			PropertyModel<AmpStructureImg> imgModel = new PropertyModel<AmpStructureImg>(item.getModelObject(), "imgFileName");
			AmpLabelFieldPanel<AmpStructureImg> label = new AmpLabelFieldPanel<AmpStructureImg>("imgFileNameLbl", imgModel, "");
			item.add(label);
			
			ListEditorRemoveButton delbutton = new ListEditorRemoveButton("deleteStructureImg", "Delete Image");
			item.add(delbutton);

		}

	};
	return imgList;
	}	
}
