package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MetaDataKey;
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
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentFundingResourcesTableFeature;
import org.dgfoundation.amp.onepager.components.upload.FileUploadPanel;
import org.dgfoundation.amp.onepager.helper.ResourceTranslation;
import org.dgfoundation.amp.onepager.helper.ResourceTranslationStore;
import org.dgfoundation.amp.onepager.helper.TemporaryComponentFundingDocument;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.ResourceTranslationModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeManager;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationResponse;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationStatus;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AmpComponentFundingNewResourceFieldPanel extends AmpFeaturePanel {


    private static final long serialVersionUID = 1L;

    protected WebMarkupContainer webLinkFeedbackContainer;
    protected Label webLinkFeedbackLabel;
    boolean resourceIsURL = false;
    protected boolean pathSelected;
    protected boolean urlSelected;
    protected boolean contentValid;

    final protected String DEFAULT_MESSAGE = "*" + TranslatorUtil.getTranslatedText("Please enter title");
    final protected String URL_NOT_SELECTED = "*" + TranslatorUtil.getTranslatedText("URL not selected");
    final protected String FILE_PATH_NOT_SELECTED = "*" + TranslatorUtil.getTranslatedText("File not submitted or upload has not finished");
    final protected String CONTENT_TYPE_NOT_ALLOWED = "*" + TranslatorUtil.getTranslatedText("Content type not allowed:");
    final protected String CONTENT_TYPE_EXTENSION_MISMATCH = "*" + TranslatorUtil.getTranslatedText("File extension does not match the actual file format:");
    final protected String CONTENT_TYPE_INTERNAL_ERROR = "*" + TranslatorUtil.getTranslatedText("Internal error during the content validation");

    boolean webLinkFormatCorrect;
    protected Model<String> newResourceIdModel = new Model<>();

    public AmpComponentFundingNewResourceFieldPanel(final String id,
                                    final IModel<AmpComponentFunding> model,
                                    final String fmName,
                                    final AmpComponentFundingResourcesTableFeature resourcesList) {

        super(id, model, fmName, true);

        TemporaryComponentFundingDocument tmpDoc = new TemporaryComponentFundingDocument ();
        String docId =generateResourceKey("newResource");
        newResourceIdModel.setObject(docId);
        tmpDoc.setNewTemporaryDocumentId(docId);
        final IModel<TemporaryComponentFundingDocument> td = new Model<>(tmpDoc);
        final ResourceTranslationModel titleModel = new ResourceTranslationModel(new PropertyModel<String>(td, "title"),newResourceIdModel);
        final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<>("componentFundingDocumentDocTitle", titleModel, "Title", AmpFMTypes.MODULE, Boolean.TRUE);
        name.setTextContainerDefaultMaxSize();
        name.setOutputMarkupId(true);
        final ResourceTranslationModel descModel = new ResourceTranslationModel(new PropertyModel<String>(td, "description"),newResourceIdModel);
        final AmpTextAreaFieldPanel desc = new AmpTextAreaFieldPanel("componentFundingDocumentDocDesc", descModel, "Description", false, false, false);
        desc.setOutputMarkupId(true);
        final ResourceTranslationModel noteModel = new ResourceTranslationModel(new PropertyModel<String>(td, "note"),newResourceIdModel);
        final AmpTextAreaFieldPanel note = new AmpTextAreaFieldPanel("componentFundingDocumentDocNote",noteModel, "Note", false, false, false);
        note.setOutputMarkupId(true);
//        final AmpCategorySelectFieldPanel type = new AmpCategorySelectFieldPanel("docType", CategoryConstants.DOCUMENT_TYPE_KEY, new PropertyModel<AmpCategoryValue>(td, "type"), "Type", true, true);
        //FileUploadField file = new FileUploadField("file", new AmpFileUploadModel(new PropertyModel<FileUpload>(td, "file")));
        //file.setOutputMarkupId(true);

        String componentId = "new";
        if (model.getObject().getAmpComponentFundingId() != null)
            componentId = Long.toString(model.getObject().getAmpComponentFundingId());
        final Model<FileItem> fileItemModel = new Model<FileItem>();
        FileUploadPanel fileUpload = new FileUploadPanel("componentFundingDocumentFile",componentId, fileItemModel);

//        final AmpTextFieldPanel<String> webLink = new AmpTextFieldPanel<String>("webLink",
//                new PropertyModel<String>(td, "webLink"), "Web Link", true, true);
//        webLink.setTextContainerDefaultMaxSize();
//        webLink.setVisibilityAllowed(false);
//        webLink.setOutputMarkupId(true);

        String resourceLabelModel = "File";
//        if (newResourceIsWebLink) {
//            resourceLabelModel = "Web Link";
//            resourceIsURL = true;
//        }

        TrnLabel resourceLabel = new TrnLabel("componentFundingDocumentResourceLabel", resourceLabelModel);

        // create the form
        final Form<?> form = new Form<Void>("componentFundingDocumentForm") {

            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit() {
                TemporaryComponentFundingDocument tmp = td.getObject();
                if (fileItemModel.getObject() != null)
                    tmp.setFile(new FileUpload(fileItemModel.getObject()));

                if (updateVisibility(td.getObject(), resourceIsURL)) {
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
//                    HashSet<TemporaryComponentFundingDocument> newItemsSet = getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(model.getObject().getJustAnId());
//                    if (newItemsSet == null) {
//                        newItemsSet = new HashSet<>();
////                        getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(model.getObject().getJustAnId()).addAll(newItemsSet);
//                    }

                    MetaDataKey<HashMap<String, HashSet<TemporaryComponentFundingDocument>>> metaDataKey = OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS;

                    HashMap<String, HashSet<TemporaryComponentFundingDocument>> metaData = getSession().getMetaData(metaDataKey);

                    if (metaData == null) {
                        metaData = new HashMap<>();
                    }
                    getSession().setMetaData(metaDataKey, metaData);

                    HashSet<TemporaryComponentFundingDocument> newItemsSet = metaData.computeIfAbsent(model.getObject().getJustAnId(), k -> new HashSet<>());


//                    existingSet.addAll(newItemsSet);

                    tmp.setTranslatedDescriptionList(getTranslationsForField(tmp.getNewTemporaryDocumentId(),"description"));
                    tmp.setTranslatedTitleList(getTranslationsForField(tmp.getNewTemporaryDocumentId(),"title"));
                    tmp.setTranslatedNoteList(getTranslationsForField(tmp.getNewTemporaryDocumentId(),"description"));
                    newItemsSet.add(tmp);
                    TemporaryComponentFundingDocument tmpDoc = new TemporaryComponentFundingDocument();
                    String docId = generateResourceKey("newResource");
                    newResourceIdModel.setObject(docId);
                    tmpDoc.setNewTemporaryDocumentId(docId);
                    td.setObject(tmpDoc);
                    fileItemModel.setObject(null);
                }
            }
        };

        add(createAddNewLink(fmName));

        WebMarkupContainer rc = new WebMarkupContainer("componentFundingDocumentResourcePanel");
        rc.add(new AttributeModifier("id", getToggleId()));
        rc.add(form);
        rc.add(name);
        rc.add(fileUpload);
        rc.setOutputMarkupId(true);
        add(rc);

//        if (newResourceIsWebLink){
//            fileUpload.setVisible(false);
//            webLink.setVisibilityAllowed(true);
//        }

        form.add(name);
        form.add(desc);
        form.add(note);
//        form.add(type);
        form.add(fileUpload);
        form.add(resourceLabel);
//        form.add(webLink);

        // create the ajax button used to submit the form
        AmpButtonField submit = new AmpButtonField("componentFundingDocumentAjaxSubmit", "Add", true){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                TemporaryDocument tmp = td.getObject();
                if (fileItemModel.getObject() != null)
                    tmp.setFile(new FileUpload(fileItemModel.getObject()));
                target.add(name);
                target.add(desc);
                target.add(note);
//                target.add(type);

//                if (webLink.isVisibleInHierarchy()) {
//                    target.add(webLink);
//                }

                target.add(resourcesList);
//                target.add(webLinkFeedbackContainer);
                if (updateVisibility(td.getObject(), resourceIsURL)){
                    target.appendJavaScript("$('#" + getToggleId() + "').hide();");
                    target.appendJavaScript("$('#" + getToggleId() + "').find('[role=fileUploadedMsg]').html('');");
                    target.appendJavaScript("$('#uploadLabel').text('" + TranslatorWorker.translateText("No file chosen") + "');");

                }
            }
        };

        form.add(submit);
        form.add(createCancelButton());

        createWebLinkFeedbackContainer();
        form.add(webLinkFeedbackContainer);
    }

    protected AjaxLink createAddNewLink(final String fmName) {
        final String newDocumentGenKey = TranslatorWorker.generateTrnKey(fmName);
        final AjaxLink addNewLink = new AjaxLink("componentFundingDocumentPanelLink"){
            public void onClick(AjaxRequestTarget target) {
//                target.prependJavaScript(OnePagerUtil.getToggleChildrenJS(this));
                target.prependJavaScript(OnePagerUtil.getToggleChildrenJSComponentFunding(this, getToggleId()));
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                configureTranslationMode(this, newDocumentGenKey);
            }
        };
        addNewLink.add(new Label("componentFundingDocumentLinkText", TranslatorWorker.translateText(fmName)));

        return addNewLink;
    }

    protected AmpAjaxLinkField createCancelButton() {

        return new AmpAjaxLinkField("componentFundingDocumentCancel", "Cancel", "Cancel") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("$('#" + getToggleId() + "').hide();");
                target.appendJavaScript("$('#" + getToggleId() + "').find('[role=fileUploadedMsg]').html('');");
                target.appendJavaScript("$('#uploadLabel').text('" + TranslatorWorker.translateText("No file chosen") + "');");
//                webLinkFeedbackContainer.setVisible(false);
            }
        };
    }

    protected void createWebLinkFeedbackContainer() {
        webLinkFeedbackContainer = new WebMarkupContainer("webLinkFeedbackContainer");
        webLinkFeedbackContainer.setOutputMarkupId(true);
        webLinkFeedbackContainer.setOutputMarkupPlaceholderTag(true);
        webLinkFeedbackContainer.setVisible(false);

        webLinkFeedbackLabel = new Label("webLinkFeedbackLabel", new Model<String>(DEFAULT_MESSAGE));
        webLinkFeedbackContainer.add(webLinkFeedbackLabel);
    }

    protected boolean updateVisibility(TemporaryDocument resource, boolean newResourceIsWebLink) {
        boolean noErrors = true;
        boolean titleSelected = !(resource.getTitle() == null || resource.getTitle().length() == 0);

        String conentValidationMessage = "";

        if (newResourceIsWebLink){
            urlSelected = resource.getWebLink() != null && resource.getWebLink().length() != 0;
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

        if (!titleSelected && !((pathSelected && contentValid))) {
            noErrors = false;
            if (!titleSelected) {
                webLinkFeedbackLabel.setDefaultModelObject(DEFAULT_MESSAGE);
            } else if (!pathSelected && !newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(FILE_PATH_NOT_SELECTED);
            } else if (!contentValid && !newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(conentValidationMessage);
            } else if (!urlSelected && newResourceIsWebLink) {
                webLinkFeedbackLabel.setDefaultModelObject(URL_NOT_SELECTED);
            }
        }

        return noErrors;
    }

    private boolean isEnabledMimeTypeValidation() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.LIMIT_FILE_TYPE_FOR_UPLOAD);
    }

    protected String generateResourceKey(String id) {
        AmpAuthWebSession session = ((AmpAuthWebSession) Session.get());
        String eKey = id + "-" + session.getCurrentMember().getMemberId() + "-";
        eKey = eKey + System.currentTimeMillis();

        return eKey;
    }

    private List<ResourceTranslation> getTranslationsForField (String id, String field) {
        List<ResourceTranslation> translationsList;
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

    protected void configureTranslationMode(AjaxLink link, String key) {
        if (TranslatorUtil.isTranslatorMode(getSession())){
            link.setOutputMarkupId(true);
            link.add(new AttributeAppender("style", new Model<String>("text-decoration: underline; color: #0CAD0C;"), ""));
            link.add(new AttributeModifier("key", key));
//            link.add(new AttributeModifier("onclick", "$('#" + getToggleId() + "').slideToggle();spawnEditBox(this.id)"));
            link.add(new AttributeModifier("onclick", String.format("$(this).closest('tr.wicketFundingRowItem').find('#%s').slideToggle();spawnEditBox(this.id)",getToggleId())));
//            link.add(new AttributeModifier("onclick", "toggleElement(this, '" + getToggleId() + "');spawnEditBox(this.id)"));
//            link.add(new AttributeModifier("onclick", "var row = $(this).closest('tr'); var element = row.find('#" + getToggleId() + "'); element.slideToggle(); return false;spawnEditBox(this.id)"));

        } else {
            link.add(AttributeModifier.remove("key"));
            link.add(AttributeModifier.remove("style"));
            link.add(AttributeModifier.remove("onclick"));
//            link.add(new AttributeModifier("onclick", "$('#" + getToggleId()  + "').slideToggle();"));
            link.add(new AttributeModifier("onclick", String.format("$(this).closest('tr.wicketFundingRowItem').find('#%s').slideToggle();",getToggleId())));
//            link.add(new AttributeModifier("onclick", "console.log('Link clicked'); var row = $(this).closest('tr'); var element = row.find('#" + getToggleId() + "'); console.log(element); element.slideToggle(); return false;"));

//            link.add(new AttributeModifier("onclick", "toggleElement(this, '" + getToggleId() + "');"));

        }
    }

    protected String getToggleId() {
        if (TranslatorUtil.isTranslatorMode(getSession())) {
            return getTranlationToggleId();
        }

        return "id_" + getId();
    }

    protected String getTranlationToggleId() {
        return getId() + "H";
    }
}
