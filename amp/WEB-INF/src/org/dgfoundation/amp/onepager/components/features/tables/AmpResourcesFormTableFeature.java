/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jcr.Node;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.file.File;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.dgfoundation.amp.onepager.helper.DownloadResourceStream;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author aartimon@dginternational.org
 * @since Apr 13, 2011
 */
public class AmpResourcesFormTableFeature extends AmpFormTableFeaturePanel<AmpActivityVersion,TemporaryDocument> {

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpResourcesFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, am, fmName);
		super.setTitleHeaderColSpan(10);

		final IModel<Set<AmpActivityDocument>> setModel=new PropertyModel<Set<AmpActivityDocument>>(am,"activityDocuments");
		if (am.getObject().getActivityDocuments() == null)
			am.getObject().setActivityDocuments(new HashSet<AmpActivityDocument>());
		
		IModel<List<TemporaryDocument>> listModel = new AbstractReadOnlyModel<List<TemporaryDocument>>() {

			@Override
			public List<TemporaryDocument> getObject() {
				HashSet<TemporaryDocument> newItems = getSession().getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
				HashSet<AmpActivityDocument> delItems = getSession().getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
				ArrayList<TemporaryDocument> ret = new ArrayList<TemporaryDocument>();
				
				Iterator<AmpActivityDocument> it = setModel.getObject().iterator();
				while (it.hasNext()) {
					AmpActivityDocument d = (AmpActivityDocument) it
							.next();
					//check if marked for delete
					if (delItems.contains(d))
						continue;
					
					AmpAuthWebSession session = (AmpAuthWebSession) getSession();
					
					Node node = DocumentManagerUtil.getWriteNode(d.getUuid(), session.getHttpSession());
					NodeWrapper nw = new NodeWrapper(node);
					
					if (node == null || nw == null)
						continue;
					
					/**
					 * Code to add TempDoc to list
					 */
					TemporaryDocument td = new TemporaryDocument();
					td.setExisting(true);
					td.setExistingDocument(d);
					td.setDate(nw.getCalendarDate());
					td.setDescription(nw.getDescription());
					td.setNote(nw.getNotes());
					td.setTitle(nw.getTitle());
					td.setType(CategoryManagerUtil.getAmpCategoryValueFromDb(nw.getCmDocTypeId()));
					td.setWebLink(nw.getWebLink());
					td.setYear(nw.getYearOfPublication());
					
					td.setFileSize(nw.getFileSizeInMegabytes());
					td.setFileName(nw.getName());
					td.setLabels(nw.getLabels());
					td.setContentType(nw.getContentType());
					
					ret.add(td);
					DocumentManagerUtil.logoutJcrSessions( session.getHttpSession());
				}
				
				ret.addAll(newItems);
				
				return ret;
			}
		};

		list = new ListView<TemporaryDocument>("list", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<TemporaryDocument> item) {
				if (item.getModel() == null && item.getModelObject() == null){
					logger.error("ola");
					return;
				}
				
				item.add(new AmpLabelFieldPanel<String>("title", new PropertyModel<String>(item.getModel(), "title"), "Document Title", true));
				item.add(new AmpLabelFieldPanel<String>("resourceName", new PropertyModel<String>(item.getModel(), "fileName"), "Resource Name", true));
				item.add(new AmpLabelFieldPanel<Date>("date", new PropertyModel<Date>(item.getModel(), "date.time"), "Document Date", true));
				item.add(new AmpLabelFieldPanel<String>("year", new PropertyModel<String>(item.getModel(), "year"), "Document Year", true));
				item.add(new AmpLabelFieldPanel<Double>("size", new PropertyModel<Double>(item.getModel(), "fileSize"), "Document Size", true));
				item.add(new AmpLabelFieldPanel<String>("docType", new PropertyModel<String>(item.getModelObject().getType(), "label"), "Document Type", true));
				
				final DownloadResourceStream drs;
				if (item.getModelObject().isExisting())
					drs = new DownloadResourceStream(new PersistentObjectModel<AmpActivityDocument>(item.getModelObject().getExistingDocument()));
				else
					drs = new DownloadResourceStream(item.getModelObject().getFile());
				
				String webLink = item.getModelObject().getWebLink();
				
				if (webLink!=null && webLink.length()>0 ){
					ExternalLink link = new ExternalLink("download", new Model<String>(webLink));
					item.add(link);
					WebMarkupContainer downloadLinkImg = new WebMarkupContainer("downloadImage");
					downloadLinkImg.add(new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/ico_attachment.png")));
					link.add(downloadLinkImg);
				}
				else{
					Link downloadLink = new Link("download") {
						@Override
						public void onClick() {
							getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(drs){
								public String getFileName() {
									return drs.getFileName();
								};
							});
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
						if (item.getModelObject().isExisting()){
							HashSet<AmpActivityDocument> delItems = getSession().getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
							if (delItems == null){
								delItems = new HashSet<AmpActivityDocument>();
								getSession().setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, delItems);
							}
							delItems.add(item.getModelObject().getExistingDocument());
						}
						else{
							HashSet<TemporaryDocument> newItems = getSession().getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
							newItems.remove(item.getModelObject());
						}
						target.addComponent(list.getParent());
					}
				};
				item.add(delRelOrg);
			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
