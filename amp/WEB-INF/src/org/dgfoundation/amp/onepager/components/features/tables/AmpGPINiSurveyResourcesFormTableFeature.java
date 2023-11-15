package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.helper.DownloadResourceStream;
import org.dgfoundation.amp.onepager.helper.TemporaryGPINiDocument;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponse;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import java.util.*;
import java.util.stream.Collectors;

public class AmpGPINiSurveyResourcesFormTableFeature
        extends AmpFormTableFeaturePanel<AmpGPINiSurveyResponse, TemporaryGPINiDocument> {

    private static final long serialVersionUID = 1L;

    boolean refreshExistingDocs = false;

    public AmpGPINiSurveyResourcesFormTableFeature(String id, String fmName,
            final IModel<AmpGPINiSurveyResponse> responseModel,
            List<AmpCollectionValidatorField<AmpGPINiSurveyResponse, String>> responseValidationFields) 
                    throws Exception {
        super(id, responseModel, fmName);

        super.setTitleHeaderColSpan(10);

        final IModel<Set<AmpGPINiSurveyResponseDocument>> setModel = new PropertyModel<Set<AmpGPINiSurveyResponseDocument>>(
                responseModel, "supportingDocuments");

        if (responseModel.getObject().getSupportingDocuments() == null)
            responseModel.getObject().setSupportingDocuments(new HashSet<AmpGPINiSurveyResponseDocument>());

        IModel<List<TemporaryGPINiDocument>> listModel = new AbstractReadOnlyModel<List<TemporaryGPINiDocument>>() {
            private static final long serialVersionUID = 1L;

            private transient List<TemporaryGPINiDocument> existingTmpDocs = getExistingObject();

            private List<TemporaryGPINiDocument> getExistingObject() {
                List<TemporaryGPINiDocument> ret = new ArrayList<TemporaryGPINiDocument>();
                for (AmpGPINiSurveyResponseDocument d : setModel.getObject()) {
                    Node node = DocumentManagerUtil.getWriteNode(d.getUuid(), SessionUtil.getCurrentServletRequest());
                    NodeWrapper nw = new NodeWrapper(node);

                    if (node != null && nw != null) {
                        TemporaryGPINiDocument td = new TemporaryGPINiDocument();
                        td.setExisting(true);
                        td.setExistingDocument(d);
                        td.setTitle(nw.getTitle());
                        td.setWebLink(nw.getWebLink());
                        td.setFileName(nw.getName());

                        ret.add(td);
                    }
                }

                refreshExistingDocs = false;
                return ret;
            }

            @Override
            public List<TemporaryGPINiDocument> getObject() {
                Set<TemporaryGPINiDocument> allResourcesNewItems = 
                        getSession().getMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS);
                
                Set<TemporaryGPINiDocument> newResponseResourceItems = new HashSet<>();
                
                if (allResourcesNewItems != null) {
                    newResponseResourceItems = allResourcesNewItems.stream()
                    .filter(item -> item.getSurveyResponse().equals(responseModel.getObject()))
                    .collect(Collectors.toSet());
                }

                HashSet<AmpGPINiSurveyResponseDocument> delItems = Optional
                        .ofNullable(getSession().getMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS))
                        .orElse(new HashSet<AmpGPINiSurveyResponseDocument>());

                if (refreshExistingDocs) {
                    existingTmpDocs = getExistingObject();
                }
                if (existingTmpDocs==null){
                    existingTmpDocs=new ArrayList<>();
                }

                List<TemporaryGPINiDocument> ret = new ArrayList<>(existingTmpDocs);

                for (AmpGPINiSurveyResponseDocument d : setModel.getObject()) {
                    if (delItems.contains(d)) {
                        for (TemporaryGPINiDocument td : existingTmpDocs) {
                            if (td.getExistingDocument().equals(d)) {
                                existingTmpDocs.remove(td);
                                break;
                            }
                        }
                    }
                }

                ret.addAll(newResponseResourceItems);

                return ret;
            }
        };

        list = new ListView<TemporaryGPINiDocument>("list", listModel) {

            private static final long serialVersionUID = -3355619971591046668L;

            @Override
            protected void onAfterRender() {
                super.onAfterRender();
                DocumentManagerUtil.logoutJcrSessions(SessionUtil.getCurrentServletRequest());
            }

            @Override
            protected void populateItem(final ListItem<TemporaryGPINiDocument> item) {
                if (item.getModel() == null && item.getModelObject() == null) {
                    return;
                }

                item.add(new Label("title", item.getModel().getObject().getTitle()));

                if (item.getModel().getObject().getFileName() == null) {
                    item.add(new Label("resourceName", item.getModel().getObject().getWebLink()));
                } else {
                    item.add(new Label("resourceName", item.getModel().getObject().getFileName()));
                }

                final DownloadResourceStream<ObjectReferringDocument> drs;
                if (item.getModelObject().isExisting()) {
                    drs = new DownloadResourceStream<ObjectReferringDocument>(
                            new PersistentObjectModel<ObjectReferringDocument>(
                                    item.getModelObject().getExistingDocument()));
                } else {
                    drs = new DownloadResourceStream<ObjectReferringDocument>(item.getModelObject().getFile());
                }

                String webLink = item.getModelObject().getWebLink();

                if (webLink != null && webLink.length() > 0) {
                    if (!webLink.startsWith("http"))
                        webLink = "http://" + webLink;
                    ExternalLink link = new ExternalLink("download", new Model<String>(webLink));
                    item.add(link);
                    WebMarkupContainer downloadLinkImg = new WebMarkupContainer("downloadImage");
                    downloadLinkImg.add(
                            new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/ico_attachment.png")));
                    link.add(downloadLinkImg);
                } else {
                    Link downloadLink = new Link("download") {
                        @Override
                        public void onClick() {
                            getRequestCycle().scheduleRequestHandlerAfterCurrent(
                                    new ResourceStreamRequestHandler(drs, drs.getFileName()));
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

                    WebMarkupContainer downloadLinkImg = new WebMarkupContainer("downloadImage");
                    downloadLinkImg.add(new AttributeModifier("src", new Model(extPath)));
                    downloadLink.add(downloadLinkImg);
                }

                AmpDeleteLinkField delRelOrg = new AmpDeleteLinkField("delete", "Delete Resource") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (item.getModelObject().isExisting()) {
                            HashSet<AmpGPINiSurveyResponseDocument> delItems = getSession()
                                    .getMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS);
                            
                            if (delItems == null) {
                                delItems = new HashSet<AmpGPINiSurveyResponseDocument>();
                                getSession().setMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS, delItems);
                            }
                            
                            delItems.add((AmpGPINiSurveyResponseDocument) item.getModelObject().getExistingDocument());
                        } else {
                            HashSet<TemporaryGPINiDocument> newItems = getSession()
                                    .getMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS);
                            newItems.remove(item.getModelObject());
                        }
                        target.add(list.getParent());
                        responseValidationFields.forEach(r -> r.reloadValidationField(target, false));
                    }
                };
                item.add(delRelOrg);
            }
        };

        add(list);
    }

    public void setRefreshExistingDocs(boolean refreshExistingDocs) {
        this.refreshExistingDocs = refreshExistingDocs;
    }
}
