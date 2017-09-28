/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.components.upload.FileUploadPanel;
import org.dgfoundation.amp.onepager.helper.ResourceTranslation;
import org.dgfoundation.amp.onepager.helper.ResourceTranslationStore;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.ResourceTranslationModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeManager;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationResponse;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationStatus;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;


/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpNewResourceFieldPanel extends AmpFeaturePanel {
    
    final String EXPRESSION = "^((https?|ftp|file|)://)?[a-zA-Z0-9\\-\\./=?,&#_-]+$";
    
    private WebMarkupContainer webLinkFeedbackContainer;
    private Label webLinkFeedbackLabel;
    boolean resourceIsURL = false;
    private boolean pathSelected;
    private boolean urlSelected;
    private boolean urlFormatValid;
    private boolean contentValid;
    
    final private String DEFAULT_MESSAGE = "*" + TranslatorUtil.getTranslatedText("Please enter title");
    final private String URL_NOT_SELECTED = "*" + TranslatorUtil.getTranslatedText("URL not selected");
    final private String FILE_PATH_NOT_SELECTED = "*" + TranslatorUtil.getTranslatedText("File not submited or upload has not finished");
    final private String WRONG_URL_FORMAT = "*" + TranslatorUtil.getTranslatedText("Wrong url format. Please enter valid url");
    final private String CONTENT_TYPE_NOT_ALLOWED = "*" + TranslatorUtil.getTranslatedText("Content type not allowed:");
    final private String CONTENT_TYPE_EXTENSION_MISMATCH = "*" + TranslatorUtil.getTranslatedText("File extension does not match the actual file format:");
    final private String CONTENT_TYPE_INTERNAL_ERROR = "*" + TranslatorUtil.getTranslatedText("Internal error during the content validation");


    boolean webLinkFormatCorrect;
    private  Model<String> newResourceIdModel = new Model <String> ();

    
    public AmpNewResourceFieldPanel(final String id,
                                    final IModel<AmpActivityVersion> model,
                                    final String fmName,
                                    final AmpResourcesFormTableFeature resourcesList,
                                    boolean newResourceIsWebLink) throws Exception {
        super(id, model, fmName, true);
        TemporaryDocument tmpDoc= new TemporaryDocument ();
        String docId =generateResourceKey("newResource");
        newResourceIdModel.setObject(docId);
        tmpDoc.setNewTemporaryDocumentId(docId);
        final IModel<TemporaryDocument> td = new Model<TemporaryDocument>(tmpDoc);
        final ResourceTranslationModel titleModel = new ResourceTranslationModel(new PropertyModel<String>(td, "title"),newResourceIdModel);
        final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("docTitle",titleModel , "Title",AmpFMTypes.MODULE,Boolean.TRUE);
        name.setTextContainerDefaultMaxSize();
        name.setOutputMarkupId(true);
        final ResourceTranslationModel descModel = new ResourceTranslationModel(new PropertyModel<String>(td, "description"),newResourceIdModel);
        final AmpTextAreaFieldPanel desc = new AmpTextAreaFieldPanel("docDesc", descModel, "Description", false, false, false);
        desc.setOutputMarkupId(true);
        final ResourceTranslationModel noteModel = new ResourceTranslationModel(new PropertyModel<String>(td, "note"),newResourceIdModel);
        final AmpTextAreaFieldPanel note = new AmpTextAreaFieldPanel("docNote",noteModel, "Note", false, false, false);
        note.setOutputMarkupId(true);
        final AmpCategorySelectFieldPanel type = new AmpCategorySelectFieldPanel("docType", CategoryConstants.DOCUMENT_TYPE_KEY, new PropertyModel<AmpCategoryValue>(td, "type"), "Type", true, true);
        //FileUploadField file = new FileUploadField("file", new AmpFileUploadModel(new PropertyModel<FileUpload>(td, "file")));
        //file.setOutputMarkupId(true);

        String activityId = "new";
        if (model.getObject().getAmpActivityId() != null)
            activityId = Long.toString(model.getObject().getAmpActivityId());
        final Model<FileItem> fileItemModel = new Model<FileItem>();
        FileUploadPanel fileUpload = new FileUploadPanel("file",activityId, fileItemModel);

        final AmpTextFieldPanel<String> webLink = new AmpTextFieldPanel<String>("webLink", new PropertyModel<String>(td, "webLink"), "Web Link", true, false);
        webLink.setTextContainerDefaultMaxSize();
        webLink.setVisibilityAllowed(false);
        webLink.setOutputMarkupId(true);
        
        
        String resourceLabelModel = "File";
        if (newResourceIsWebLink) {
            resourceLabelModel = "Web Link";
            resourceIsURL = true;
        }
            
            
        TrnLabel resourceLabel = new TrnLabel("resourceLabel", resourceLabelModel);

        // create the form
        final Form<?> form = new Form<Void>("form") {
            
            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit() {
                TemporaryDocument tmp = td.getObject();
                if (fileItemModel.getObject() != null)
                    tmp.setFile(new FileUpload(fileItemModel.getObject()));
//              if (tmp.getFile() == null && tmp.getWebLink() == null)
//                  return;
//              
                if (updateVisibility(td, resourceIsURL)) {
                    if (tmp.getFile() != null){
                        double fSize = tmp.getFile().getSize()*100/(1024*1024);
                        fSize = fSize/100;
                        tmp.setFileSize(fSize);
                        tmp.setFileName(tmp.getFile().getClientFileName());
                        tmp.setContentType(tmp.getFile().getContentType());
                    }
                    
                    if (tmp.getWebLink() != null) {
                        tmp.setWebLink(DocumentManagerUtil.processUrl(tmp.getWebLink(), null));
                        tmp.setFileName(tmp.getWebLink());
                    }               
                    
                    tmp.setDate(Calendar.getInstance());
                    tmp.setYear(String.valueOf((tmp.getDate()).get(Calendar.YEAR)));
                    HashSet<TemporaryDocument> newItemsSet = getSession().getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
                    if (newItemsSet == null) {
                        newItemsSet = new HashSet<TemporaryDocument>();
                        getSession().setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, newItemsSet);
                    }

                    tmp.setTranslatedDescriptionList(getTranslationsForField(tmp.getNewTemporaryDocumentId(),"description"));
                    tmp.setTranslatedTitleList(getTranslationsForField(tmp.getNewTemporaryDocumentId(),"title"));
                    tmp.setTranslatedNoteList(getTranslationsForField(tmp.getNewTemporaryDocumentId(),"description"));
                    newItemsSet.add(tmp);
                    TemporaryDocument tmpDoc = new TemporaryDocument ();
                    String docId = generateResourceKey("newResource");
                    newResourceIdModel.setObject(docId);
                    tmpDoc.setNewTemporaryDocumentId(docId);
                    td.setObject(tmpDoc);
                    fileItemModel.setObject(null);
                }
                
                //panel.setVisibilityAllowed(false);
            }
            
        };

        final String newDocumentGenKey = TranslatorWorker.generateTrnKey(fmName);
        final AjaxLink addNewLink = new AjaxLink("panelLink"){
            public void onClick(AjaxRequestTarget target) {
                target.prependJavaScript(OnePagerUtil.getToggleChildrenJS(this));
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                configureTranslationMode(this, newDocumentGenKey, id + "H");
            }
        };
        addNewLink.add(new Label("linkText", TranslatorWorker.translateText(fmName)));

        add(addNewLink);

        WebMarkupContainer rc = new WebMarkupContainer("resourcePanel");
        rc.setOutputMarkupId(true);
        add(rc);
        rc.add(new AttributeModifier("id", "id_" + this.getId()));

        //form.setMaxSize(Bytes.megabytes(1));
        //add(form);
        rc.add(form);
        rc.add(name);
        rc.add(fileUpload);

        if (newResourceIsWebLink){
            fileUpload.setVisible(false);
            webLink.setVisibilityAllowed(true);
        }
        
        form.add(name);
        form.add(desc);
        form.add(note);
        form.add(type);
        form.add(fileUpload);
        //form.add(new UploadProgressBar("progressBar", form, file));
        form.add(resourceLabel);
        form.add(webLink);
        
        // create the ajax button used to submit the form
        AmpButtonField submit = new AmpButtonField("ajaxSubmit", "Add", true){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                TemporaryDocument tmp = td.getObject();
                if (fileItemModel.getObject() != null)
                    tmp.setFile(new FileUpload(fileItemModel.getObject()));
                target.add(name);
                target.add(desc);
                target.add(note);
                target.add(type);
                target.add(webLink);
                target.add(resourcesList);
                target.add(webLinkFeedbackContainer);
                if (updateVisibility(td, resourceIsURL)){
                    target.appendJavaScript("$('#" + id + "H').hide();");
                    target.appendJavaScript("$('#" + id + "H').find('[role=fileUploadedMsg]').html('');");
                    target.appendJavaScript("$('#uploadLabel').text('" + TranslatorWorker.translateText("No file chosen") + "');");
                    
                }
//                target.add(form);
//                AmpNewResourceFieldPanel panel = this.findParent(AmpNewResourceFieldPanel.class);
//                target.add(panel.getParent());
            }
        };
        form.add(submit);
        AmpAjaxLinkField cancel = new AmpAjaxLinkField("cancel", "Cancel", "Cancel") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("$('#" + id + "H').hide();");
                target.appendJavaScript("$('#" + id + "H').find('[role=fileUploadedMsg]').html('');");
                target.appendJavaScript("$('#uploadLabel').text('" + TranslatorWorker.translateText("No file chosen") + "');");
                webLinkFeedbackContainer.setVisible(false);
            }
        };
        form.add(cancel);
        
        webLinkFeedbackContainer = new WebMarkupContainer("webLinkFeedbackContainer");
        webLinkFeedbackLabel = new Label("webLinkFeedbackLabel", new Model<String>(DEFAULT_MESSAGE));
        webLinkFeedbackContainer.setOutputMarkupId(true);
        webLinkFeedbackContainer.setOutputMarkupPlaceholderTag(true);
        webLinkFeedbackContainer.setVisible(false);
        webLinkFeedbackContainer.add(webLinkFeedbackLabel);
        form.add(webLinkFeedbackContainer);
    }

    
    protected boolean updateVisibility(IModel<TemporaryDocument> tempDocModel,boolean newResourceIsWebLink){
        boolean noErrors = true;
        TemporaryDocument resource = tempDocModel.getObject();
        boolean titleSelected = !(resource.getTitle() == null || resource.getTitle().length() == 0);
        
        String conentValidationMessage = "";
                
        if (newResourceIsWebLink){
            if (resource.getWebLink() == null || resource.getWebLink().length() == 0) {
                urlSelected = false;
            } else {
                urlSelected =true;
                
                Pattern pattern = Pattern.compile(EXPRESSION,Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(resource.getWebLink());
                if (!matcher.find()){
                    urlFormatValid = false;
                } else {
                    if (!resource.getWebLink().contains("://")) {
                        resource.setWebLink("http://" + resource.getWebLink());
                    }
                    urlFormatValid = true;
                }
            }           
        } else {
            pathSelected = resource.getFile() != null;
            contentValid = true;
            if (pathSelected) {
                // validate the content of the file AMP-24920
                if (isEnabledMimeTypeValidation()) {
                    try {
                        FileTypeManager mimeTypeManager = FileTypeManager.getInstance();
                        InputStream is = new BufferedInputStream(resource.getFile().getInputStream());
                        FileTypeValidationResponse validationResponse = mimeTypeManager.validateFileType(is, resource.getFile().getClientFileName());
                        if (validationResponse.getStatus() != FileTypeValidationStatus.ALLOWED) {
                            if (validationResponse.getStatus() == FileTypeValidationStatus.NOT_ALLOWED) {
                                conentValidationMessage = CONTENT_TYPE_NOT_ALLOWED + " "
                                        + validationResponse.getDescription();

                            } else if (validationResponse.getStatus() == FileTypeValidationStatus.CONTENT_EXTENSION_MISMATCH) {
                                conentValidationMessage = CONTENT_TYPE_EXTENSION_MISMATCH + " "
                                        + resource.getFile().getClientFileName() + " "
                                        + TranslatorUtil.getTranslatedText("is a ") + " "
                                        + validationResponse.getDescription() + " ("
                                        + validationResponse.getContentName() + ")";
                            } else {
                                conentValidationMessage = CONTENT_TYPE_INTERNAL_ERROR;
                            }
                            contentValid = false;
                        } 
                    } catch (IOException e) {
                        conentValidationMessage = CONTENT_TYPE_INTERNAL_ERROR;
                        contentValid = false;
                    }
                } 
            }
        }

        if (titleSelected && ((pathSelected && contentValid && !newResourceIsWebLink)
                || (newResourceIsWebLink && urlSelected && urlFormatValid))) {
            webLinkFeedbackContainer.setVisible(false);
        } else {
            webLinkFeedbackContainer.setVisible(true);
            noErrors = false;
            if (!titleSelected) {
                webLinkFeedbackLabel.setDefaultModelObject(DEFAULT_MESSAGE);
            } else if (!pathSelected && !newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(FILE_PATH_NOT_SELECTED);
            } else if (!contentValid && !newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(conentValidationMessage);
            } else if (!urlSelected && newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(URL_NOT_SELECTED);
            } else if (!urlFormatValid && newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(WRONG_URL_FORMAT);
            }
        }
        
        return noErrors;
    }


    /**
     * 
     */
    private boolean isEnabledMimeTypeValidation() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.LIMIT_FILE_TYPE_FOR_UPLOAD);
    }
    
    private String generateResourceKey(String id) {
        AmpAuthWebSession session = ((AmpAuthWebSession)Session.get());
        String eKey = id + "-" + session.getCurrentMember().getMemberId() + "-";
        eKey = eKey + System.currentTimeMillis();
        return eKey;
    }
    
    private List <ResourceTranslation> getTranslationsForField (String id, String field) {
        List<ResourceTranslation> translationsList = null;
        HashMap<String, ResourceTranslationStore> translationMap = Session.get().getMetaData(
                OnePagerConst.RESOURCES_TRANSLATIONS);
        if (translationMap != null) {
            ResourceTranslationStore store = translationMap.get(id);
            if (store != null) {
                translationsList = store.getResourceFieldTranslations().get(field);
                return translationsList;
            }
        }
        return null;
    }

    private void configureTranslationMode (AjaxLink link, String key, String id) {
        if (TranslatorUtil.isTranslatorMode(getSession())){
            link.setOutputMarkupId(true);
            link.add(new AttributeAppender("style", new Model<String>("text-decoration: underline; color: #0CAD0C;"), ""));
            link.add(new AttributeModifier("key", key));
            link.add(new AttributeModifier("onclick", "$('#"+id+"').slideToggle();spawnEditBox(this.id)"));
        }
        else{
            link.add(AttributeModifier.remove("key"));
            link.add(AttributeModifier.remove("style"));
            link.add(AttributeModifier.remove("onclick"));
            link.add(new AttributeModifier("onclick", "$('#id_" + this.getId() + "').slideToggle();"));

        }
    }
    
}
