/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.PagingListEditor;
import org.dgfoundation.amp.onepager.components.PagingListNavigator;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMInfo;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.util.FMUtil.PathException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.util.StructuresUtil;

public class AmpStructuresFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083754446344L;
	private final String FM_NAMES [] = {"Structure Type","Structure Title","Structure Description","Structure Latitude","Structure Longitude","Delete Structure"};
	protected Collection<AmpStructureType> structureTypes;
	
	public AmpStructuresFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final PropertyModel<Set<AmpStructure>> setModel=new PropertyModel<Set<AmpStructure>>(am,"structures");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpStructure>());
		final PagingListEditor<AmpStructure> list;
		this.structureTypes	= 	StructuresUtil.getAmpStructureTypes();
		
		
		IModel<List<AmpStructure>> listModel = new AbstractReadOnlyModel<List<AmpStructure>>() {
			private static final long serialVersionUID = 3706184421459839220L;

			@Override
			public ArrayList<AmpStructure> getObject() {
				if (setModel != null && setModel.getObject() != null)
					return new ArrayList<AmpStructure>(setModel.getObject());
				else
					return null;
			}
		};
		
		
		list = new PagingListEditor<AmpStructure>("list", setModel) {
			
			
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpStructure> item) {
				IModel<AmpStructure> structureModel = item.getModel();
				AmpSelectFieldPanel<AmpStructureType> structureTypes = new  AmpSelectFieldPanel<AmpStructureType>("structureTypes", new PropertyModel<AmpStructureType>(structureModel, "type"),
						new LoadableDetachableModel<List<AmpStructureType>>() {
							private static final long serialVersionUID = 1L;
		
							@Override
							protected List<AmpStructureType> load() {
								return new ArrayList<AmpStructureType>(AmpStructuresFormSectionFeature.this.structureTypes);
							}		
						}, 
						"Structure Type",true, false,  new ChoiceRenderer<AmpStructureType>("name","typeId")) ;

				structureTypes.getChoiceContainer().setRequired(true);
				structureTypes.setOutputMarkupId(true);
                structureTypes.getChoiceContainer().add(new AttributeModifier("style", "max-width: 100px;margin-bottom:20px;"));
                item.add(structureTypes);
				
				final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("name", new PropertyModel<String>(structureModel, "title"), "Structure Title",true, true);
				name.setOutputMarkupId(true);
				name.getTextContainer().add(new AttributeAppender("size", new Model("10px"), ";"));
				name.setTextContainerDefaultMaxSize();
				name.getTextContainer().setRequired(true);
				if (name.isComponentMultilingual()) {
					name.getTextContainer().add(new AttributeAppender("style", "margin-bottom:40px;"));
				}
				item.add(name);
				
				
				final AmpTextAreaFieldPanel description = new AmpTextAreaFieldPanel("description", new PropertyModel<String>(structureModel, "description"),"Structure Description",false, true, true);
				description.setOutputMarkupId(true);

				String descriptionStyle;
				if (description.isComponentMultilingual()) {
					descriptionStyle ="margin-bottom: 55px;"; 
				}
				else {
					descriptionStyle ="margin-bottom: 20px;";
				}
				description.getTextAreaContainer().add(new AttributeModifier("style",descriptionStyle ));
				item.add(description);		
				
				final AmpTextFieldPanel<String> longitude = new AmpTextFieldPanel<String>("longitude", new PropertyModel<String>(structureModel, "longitude"),"Structure Longitude", true, true);
				longitude.setOutputMarkupId(true);
				longitude.setTextContainerDefaultMaxSize();

				longitude.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
				item.add(longitude);
				

				final AmpTextFieldPanel<String> latitude = new AmpTextFieldPanel<String>("latitude", new PropertyModel<String>(structureModel, "latitude"),"Structure Latitude", true, true);
				latitude.setTextContainerDefaultMaxSize();
				latitude.setOutputMarkupId(true);

				latitude.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
				item.add(latitude);
				
				final AmpTextFieldPanel<String> shape = new AmpTextFieldPanel<String>("shape", new PropertyModel<String>(structureModel, "shape"),"Structure Shape", true, true);
				shape.setOutputMarkupId(true);
				
				shape.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
				item.add(shape);
				
				//final AmpStructureImgListComponent<AmpStructure> imgList = new AmpStructureImgListComponent<AmpStructure>("structureImgList", "", structureModel, am);
				//item.add(imgList);
				
				
				ListEditorRemoveButton delbutton = new ListEditorRemoveButton("deleteStructure", "Delete Structure");
				item.add(delbutton);
				
			}
		};
		add(list);

        PagingListNavigator<AmpStructure> pln = new PagingListNavigator<AmpStructure>("paging", list);
        add(pln);


		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Structure", "Add Structure") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpStructure stru = new AmpStructure();
				list.addItem(stru);
				target.add(this.getParent());
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
				for (String fmName : FM_NAMES) {
					if (((AmpAuthWebSession)Session.get()).isFmMode())
					{
						break;	
					}
					LinkedList<FMInfo> path;
					try {
						path = FMUtil.getFmPath(this.getParent());
						path.add(new FMInfo(AmpFMTypes.MODULE, fmName));
						String pathString = FMUtil.getFmPathString(path);
						if (!FMUtil.isFmVisible(pathString, AmpFMTypes.MODULE)) {
							String javascript = "if ($('[structure-id=\"" + fmName + "\"]').length==0) {"
									+ "$('[column-id=\"" + fmName + "\"]').hide ();}";
							target.appendJavaScript(javascript);
						}
					} catch (PathException e) {
						logger.error("Error getting the path for "+this.getParent().getPath(),e);
					}

				}
				
				list.goToLastPage();
			}
		};
		add(addbutton);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
	   // response.render(arg0)OnDomReadyJavascript("some JS that will run when the page is loaded");
	} 

}
