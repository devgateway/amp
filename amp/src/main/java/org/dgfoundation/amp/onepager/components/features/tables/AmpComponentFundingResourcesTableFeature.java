package org.dgfoundation.amp.onepager.components.features.tables;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.file.File;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.helper.DownloadResourceStream;
import org.dgfoundation.amp.onepager.helper.TemporaryComponentFundingDocument;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.models.ResourceTranslationModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpComponentFundingDocument;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

import javax.jcr.Node;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author aartimon@dginternational.org
 * @since Apr 13, 2011
 */
public class AmpComponentFundingResourcesTableFeature extends AmpFormTableFeaturePanel<AmpComponentFunding, TemporaryComponentFundingDocument> {

    boolean refreshExistingDocs = false;


    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpComponentFundingResourcesTableFeature(String id, String fmName,
                                        final IModel<AmpComponentFunding> am) throws Exception {
        super(id, am, fmName);
        super.setTitleHeaderColSpan(10);
//        getSession().setMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS, new HashSet<>());
//        getSession().setMetaData(OnePagerConst.COMPONENT_FUNDING_DELETED_ITEMS, new HashSet<>());
//        getSession().setMetaData(OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES, new HashSet<>());


        final IModel<Set<AmpComponentFundingDocument>> setModel = new PropertyModel<>(am, "componentFundingDocuments");

//        if (am.getObject().getComponentFundingDocuments() == null)
//            am.getObject().getComponentFundingDocuments().addAll(new HashSet<>());

        IModel<List<TemporaryComponentFundingDocument>> listModel = new AbstractReadOnlyModel<List<TemporaryComponentFundingDocument>>() {

            private transient List<TemporaryComponentFundingDocument> existingTmpDocs = getExistingObject();

            private List<TemporaryComponentFundingDocument> getExistingObject() {
                Iterator<AmpComponentFundingDocument> it = setModel.getObject().iterator();
                List<TemporaryComponentFundingDocument> ret = new ArrayList<>();
                HashSet<TemporaryComponentFundingDocument> existingDocTitles = new HashSet<>();

                while (it.hasNext()) {
                    AmpComponentFundingDocument d = it.next();
                    Node node = DocumentManagerUtil.getWriteNode(d.getUuid(), SessionUtil.getCurrentServletRequest());
                    NodeWrapper nw = new NodeWrapper(node);

                    if (node == null)
                        continue;

                    /**
                     * Code to add TempDoc to list
                     */
                    TemporaryComponentFundingDocument td = new TemporaryComponentFundingDocument();
                    td.setExisting(true);
                    td.setExistingDocument(d);
                    td.setDate(nw.getCalendarDate());
                    td.setDescription(nw.getDescription());
                    td.setNote(nw.getNotes());
                    td.setTitle(nw.getTitle());
//                    td.setType(CategoryManagerUtil.getAmpCategoryValueFromDb(nw.getCmDocTypeId()));
                    td.setWebLink(nw.getWebLink());
                    td.setYear(nw.getYearOfPublication());
                    td.setFileSize(nw.getFileSizeInMegabytes());
                    td.setFileName(nw.getName());
//                    td.setLabels(nw.getLabels());
                    td.setContentType(nw.getContentType());

                    ret.add(td);

                    // Existing doc titles should be populated only when multilingual is enabled
                    if (ContentTranslationUtil.multilingualIsEnabled()) {
                        TemporaryComponentFundingDocument titleHolder = new TemporaryComponentFundingDocument();
                        titleHolder.setTitle(td.getTitle());
                        titleHolder.setExistingDocument(d);
                        existingDocTitles.add(titleHolder);
                        existingDocTitles.add(td);
                    }
                }
                String justAnId= am.getObject().getJustAnId();

                MetaDataKey<HashMap<String, HashSet<TemporaryComponentFundingDocument>>> metaDataKey = OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES;

                HashMap<String, HashSet<TemporaryComponentFundingDocument>> metaData = getSession().getMetaData(metaDataKey);

                if (metaData == null) {
                    metaData = new HashMap<>();

                }
                getSession().setMetaData(metaDataKey, metaData);

                HashSet<TemporaryComponentFundingDocument> existingSet = metaData.computeIfAbsent(justAnId, k -> new HashSet<>());

                existingSet.addAll(existingDocTitles);
                refreshExistingDocs = false;
                return ret;
            }



            @Override
            public List<TemporaryComponentFundingDocument> getObject() {
                HashSet<TemporaryComponentFundingDocument> newItems = getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(am.getObject().getJustAnId());
                if (newItems == null)
                    newItems = new HashSet<>();
                HashMap<String,HashSet<AmpComponentFundingDocument>> delItemsMap = getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_DELETED_ITEMS);
                HashSet<AmpComponentFundingDocument> delItems = delItemsMap.get(am.getObject().getJustAnId());
                if (delItems == null)
                    delItems = new HashSet<>();

                if (refreshExistingDocs)
                    existingTmpDocs = getExistingObject();
                if (existingTmpDocs==null){
                    existingTmpDocs=new ArrayList<>();
                }
                List<TemporaryComponentFundingDocument> ret = new ArrayList<>(existingTmpDocs);

//                if (am.getObject().getComponentFundingDocuments() == null)
//                    am.getObject().getComponentFundingDocuments().addAll(new HashSet<>());

                for (AmpComponentFundingDocument d : setModel.getObject()) {
                    //check if marked for delete
                    if (delItems.contains(d)) {
                        for (TemporaryDocument td : existingTmpDocs) {
                            if (td.getExistingDocument().equals(d)) {
                                existingTmpDocs.remove(td);
                                break;
                            }
                        }
                    }
                }
                ret.addAll(newItems);

                return ret;
            }
        };


        list = new ListView<TemporaryComponentFundingDocument>("componentFundingDocumentList", listModel) {
            private static final long serialVersionUID = 7218457979728871528L;

            @Override
            protected void onAfterRender() {
                super.onAfterRender();
                DocumentManagerUtil.logoutJcrSessions(SessionUtil.getCurrentServletRequest());
            }

            @Override
            protected void populateItem(final ListItem<TemporaryComponentFundingDocument> item) {
                if (item.getModel() == null && item.getModelObject() == null) {
                    logger.info("yoh here");
                    return;
                }

                TemporaryDocument document = item.getModelObject();

                if (!ContentTranslationUtil.multilingualIsEnabled()) {
                    item.add(new Label("componentFundingDocumentTitle", item.getModel().getObject().getTitle()));
                } else {
                    String id;
                    if (document.getExistingDocument() != null) {
                        id = document.getExistingDocument().getUuid();
                    } else {
                        id = document.getNewTemporaryDocumentId();
                    }
                    Model<String> newResourceIdModel = new Model<String>(id);
                    final ResourceTranslationModel titleModel = new ResourceTranslationModel(new PropertyModel<>(item.getModel().getObject(), "title"), newResourceIdModel);
                    final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("componentFundingDocumentTitle", titleModel, "Title", AmpFMTypes.MODULE, Boolean.TRUE);
                    name.setEnabled(false);
                    name.getTextContainer().setRequired(true);
                    item.add(name);


                }

                if (item.getModel().getObject().getFileName() == null) {
                    item.add(new Label("componentFundingDocumentResourceName", item.getModel().getObject().getWebLink()));
                } else {
                    item.add(new Label("componentFundingDocumentResourceName", item.getModel().getObject().getFileName()));
                }

                AjaxLink delComponentDoc = new AjaxLink("componentFundingDocumentDelete", setModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (item.getModelObject().isExisting()) {
                            MetaDataKey<HashMap<String, HashSet<AmpComponentFundingDocument>>> metaDataKey = OnePagerConst.COMPONENT_FUNDING_DELETED_ITEMS;

                            HashMap<String, HashSet<AmpComponentFundingDocument>> metaData = getSession().getMetaData(metaDataKey);

                            if (metaData == null) {
                                metaData = new HashMap<>();

                            }
                            getSession().setMetaData(metaDataKey, metaData);
                            HashSet<AmpComponentFundingDocument> delItems = metaData.computeIfAbsent(am.getObject().getJustAnId(), k -> new HashSet<>());
                            delItems.add(item.getModelObject().getExistingDocument());
                        } else {
                            HashSet<TemporaryComponentFundingDocument> newItems = getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(am.getObject().getJustAnId());
                            newItems.remove(item.getModelObject());
                        }
                        target.add(list.getParent());
                    }
                };
                // TODO: 10/2/23 add some style to
                delComponentDoc.add(new Button("deleteComponentFundingDocButton").add(new Label("deleteComponentFundingDocLabel", TranslatorWorker.translateText("Del"))));
                item.add(delComponentDoc);
//                item.add(new ListEditorRemoveButton("componentFundingDocumentDelete", "Component Funding Document Delete Resource"));

                PropertyModel<Date> dateModel = new PropertyModel<>(item.getModel(), "date.time");
                String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
                pattern = pattern.replace('m', 'M');
                SimpleDateFormat formater = new SimpleDateFormat(pattern);
                String formatedDate = formater.format(dateModel.getObject());

                item.add(new Label("componentFundingDocumentDate", formatedDate));
                item.add(new Label("componentFundingDocumentYear",item.getModel().getObject().getYear()));
                item.add(new Label("componentFundingDocumentSize", item.getModel().getObject().getFileSize()));
//                item.add(new AmpLabelFieldPanel<String>("docType", new PropertyModel<String>(item.getModel(), "type.label"), "Document Type", true));

                final DownloadResourceStream drs;
                if (item.getModelObject().isExisting())
                    drs = new DownloadResourceStream(new PersistentObjectModel<>
                            (item.getModelObject().getExistingDocument()), item.getModelObject().getFileName());
                else
                    drs = new DownloadResourceStream(item.getModelObject().getFile(), item.getModelObject().getFileName());

                String webLink = item.getModelObject().getWebLink();

                if (webLink != null && webLink.length() > 0) {
                    if (!webLink.startsWith("http"))
                        webLink = "http://" + webLink;
                    ExternalLink link = new ExternalLink("componentFundingDocumentDownload", new Model<>(webLink));
                    item.add(link);
                    WebMarkupContainer downloadLinkImg = new WebMarkupContainer("componentFundingDocumentDownloadImage");
                    downloadLinkImg.add(new AttributeModifier("src", new Model<>("/TEMPLATE/ampTemplate/img_2/ico_attachment.png")));
                    link.add(downloadLinkImg);
                } else {
                    Link downloadLink = new Link("componentFundingDocumentDownload") {
                        @Override
                        public void onClick() {
                            getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(drs, drs.getFileName()));
                        }
                    };
                    item.add(downloadLink);

                    String contentType = item.getModelObject().getFileName();
                    int index = contentType.lastIndexOf('.');

                    String extension = contentType.substring(index + 1, contentType.length());
                    String extPath = "/TEMPLATE/ampTemplate/images/icons/" + extension + ".gif";
                    File extImgFile = new File(WebApplication.get().getServletContext().getRealPath(extPath));
                    if (!extImgFile.exists())
                        extPath = "/TEMPLATE/ampTemplate/images/icons/default.icon.gif";
                    else
                        extPath = "/TEMPLATE/ampTemplate/images/icons/" + extension + ".gif";

                    WebMarkupContainer downloadLinkImg = new WebMarkupContainer("componentFundingDocumentDownloadImage");
                    downloadLinkImg.add(new AttributeModifier("src", new Model<>(extPath)));
                    downloadLink.add(downloadLinkImg);
                }


            }
        };

        add(list);

    }


    public void setRefreshExistingDocs(boolean refreshExistingDocs) {
        this.refreshExistingDocs = refreshExistingDocs;
    }

}

