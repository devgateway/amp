/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Calendar;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import org.dgfoundation.amp.onepager.models.AmpFileUploadModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpNewResourceFieldPanel extends AmpFeaturePanel {
	
	final String EXPRESSION = "^((https?|ftp|file|)://)?[a-zA-Z0-9\\-\\./=?]+$";
	
	private WebMarkupContainer webLinkFeedbackContainer;
	private Label webLinkFeedbackLabel;
	boolean resourceIsURL = false;
	private boolean titleSelected;
	private boolean pathSelected;
	private boolean urlSelected;
	private boolean urlFormatValid;
	
	static final private String defaultMsg = "*" + TranslatorUtil.getTranslatedText("Please enter title");
	static final private String urlNotSelected = "*" + TranslatorUtil.getTranslatedText("URL not selected");
	static final private String filePathNotSelected = "*" + TranslatorUtil.getTranslatedText("File path not selected");
	static final private String wrongUrlFormat = "*" + TranslatorUtil.getTranslatedText("Wrong url format. Please enter valid url");
	boolean webLinkFormatCorrect;

	public AmpNewResourceFieldPanel(String id, IModel model, String fmName, final AmpResourcesFormTableFeature resourcesList, boolean newResourceIsWebLink) throws Exception {
		super(id, model, fmName, true);
		
		final IModel<TemporaryDocument> td = new Model(new TemporaryDocument());
		
		AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("docTitle", new PropertyModel<String>(td, "title"), "Title",AmpFMTypes.MODULE,Boolean.TRUE);
		name.setTextContainerDefaultMaxSize();
		name.setOutputMarkupId(true);
		AmpTextAreaFieldPanel<String> desc = new AmpTextAreaFieldPanel<String>("docDesc", new PropertyModel<String>(td, "description"), "Description", false, false, false);
		AmpTextAreaFieldPanel<String> note = new AmpTextAreaFieldPanel<String>("docNote", new PropertyModel<String>(td, "note"), "Note", false, false, false);
		AmpCategorySelectFieldPanel type = new AmpCategorySelectFieldPanel("docType", CategoryConstants.DOCUMENT_TYPE_KEY, new PropertyModel<AmpCategoryValue>(td, "type"), "Type", true, true);
		FileUploadField file = new FileUploadField("file", new AmpFileUploadModel(new PropertyModel<FileUpload>(td, "file")));
		file.setOutputMarkupId(true);
		AmpTextFieldPanel<String> webLink = new AmpTextFieldPanel<String>("webLink", new PropertyModel<String>(td, "webLink"), "Web Link", true, false);
		webLink.setTextContainerDefaultMaxSize();
		webLink.setVisible(false);
		webLink.setOutputMarkupId(true);
		
		
		String resourceLabelModel = "File";
		if (newResourceIsWebLink) {
			resourceLabelModel = "Web Link";
			resourceIsURL = true;
		}
			
			
		TrnLabel resourceLabel = new TrnLabel("resourceLabel", resourceLabelModel);

		// create the form
        Form<?> form = new Form<Void>("form") {
        	
            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit() {
            	boolean addTmp = true;
            	TemporaryDocument tmp = td.getObject();
            	
//            	if (tmp.getFile() == null && tmp.getWebLink() == null)
//            		return;
//            	
            	if (updateVisibility(td, resourceIsURL)) {
            		if (tmp.getFile() != null){
                		double fSize = tmp.getFile().getSize()/(1024*1024);
                		tmp.setFileSize(fSize);
                		tmp.setFileName(tmp.getFile().getClientFileName());
                		tmp.setContentType(tmp.getFile().getContentType());
                	}
            		
            		if(tmp.getWebLink() != null){
            			tmp.setWebLink(DocumentManagerUtil.processUrl(tmp.getWebLink(),null));
            			tmp.setFileName(tmp.getWebLink());
                	}            	
                	
                	
                	if(addTmp){
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
            	}
            	
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
		AmpButtonField submit = new AmpButtonField("ajaxSubmit", "Add", true){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
            	target.add(form);
            	target.add(resourcesList);
            	target.add(webLinkFeedbackContainer);
            }
        };
        form.add(submit);
        
        webLinkFeedbackContainer = new WebMarkupContainer("webLinkFeedbackContainer");
        webLinkFeedbackLabel = new Label("webLinkFeedbackLabel", new Model(defaultMsg));
		webLinkFeedbackContainer.setOutputMarkupId(true);
		webLinkFeedbackContainer.setOutputMarkupPlaceholderTag(true);
		webLinkFeedbackContainer.setVisible(false);
		webLinkFeedbackContainer.add(webLinkFeedbackLabel);
		form.add(webLinkFeedbackContainer);
	}

	
	protected boolean updateVisibility(IModel<TemporaryDocument> tempDocModel,boolean newResourceIsWebLink){
		boolean noErrors = true;
		TemporaryDocument resource = tempDocModel.getObject();
		if(resource.getTitle() == null || resource.getTitle().length() == 0){
			titleSelected = false;
		}else{
			titleSelected = true;
		}
                
		if (newResourceIsWebLink){
			if(resource.getWebLink() == null || resource.getWebLink().length() == 0){
				urlSelected = false;
			}else{
				urlSelected =true;
				
				Pattern pattern = Pattern.compile(EXPRESSION,Pattern.MULTILINE);
				Matcher matcher = pattern.matcher(resource.getWebLink());
				if (!matcher.find()){
					urlFormatValid = false;
				}else{
					if(!resource.getWebLink().contains("://"))
						resource.setWebLink("http://" + resource.getWebLink());
					urlFormatValid = true;
				}
			}			
		}else{
			if(resource.getFile() == null){
				pathSelected = false;
			}else{
				pathSelected =true;
			}
		}
			
		
		if(titleSelected && ((pathSelected && ! newResourceIsWebLink) || (newResourceIsWebLink && urlSelected && urlFormatValid)) ){
			webLinkFeedbackContainer.setVisible(false);
		}else{
			webLinkFeedbackContainer.setVisible(true);
			if(! titleSelected){
				webLinkFeedbackLabel.setDefaultModelObject(defaultMsg);
				noErrors = false;
			}else if( ! pathSelected && ! newResourceIsWebLink){
				webLinkFeedbackLabel.setDefaultModelObject(filePathNotSelected);
				noErrors = false;
			}else if (! urlSelected && newResourceIsWebLink) {
				webLinkFeedbackLabel.setDefaultModelObject(urlNotSelected);
				noErrors = false;
			}else if ( ! urlFormatValid && newResourceIsWebLink){
				webLinkFeedbackLabel.setDefaultModelObject(wrongUrlFormat);
				noErrors = false;
			}
		}
		return noErrors;
	}
	
}
