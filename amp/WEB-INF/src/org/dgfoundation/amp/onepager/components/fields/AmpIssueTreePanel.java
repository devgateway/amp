/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservation;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationActor;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationMeasure;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;

/**
 * @author aartimon@dginternational.org
 * since Oct 26, 2010
 */
public class AmpIssueTreePanel extends AmpFieldPanel{

	private static final long serialVersionUID = 0L;
	
	public AmpIssueTreePanel(String id, final List<Class> tree, final Map<Class, String> setName, final Map<Class, String> labelName, final IModel objModel, final IModel parentSet, final Class parentClass, final int level, final String fmName) throws Exception{
		super(id,fmName, true);
		this.fmType = AmpFMTypes.MODULE;
		
		final Class levelClass = tree.get(level);
		final String levelLabel = TranslatorUtil.getTranslation(labelName.get(levelClass));
		String levelChildrenName = setName.get(levelClass);
		final PropertyModel<Set<?>> levelChildren = new PropertyModel<Set<?>>(objModel, levelChildrenName);
	
		final TextArea name =new TextArea("name", new PropertyModel(objModel,"name"));
		addFormComponent(name);
		Label label = new Label("label", levelLabel);
		add(label);
		
		if (levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpIssues") == 0 || 
			levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpRegionalObservation") == 0 || 
			levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpLineMinistryObservation") == 0){
			final AmpDatePickerFieldPanel date;
			
			if (levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpIssues") == 0)
				date = new AmpDatePickerFieldPanel("date", new PropertyModel(objModel,"issueDate"), "Date", true);
			else 
				date = new AmpDatePickerFieldPanel("date", new PropertyModel(objModel,"observationDate"), "Date", true);
					
			date.setOutputMarkupId(true);
			add(date);
		}
		else{
			WebMarkupContainer wmc = new WebMarkupContainer("date");
			wmc.setVisible(false);
			add(wmc);
		}
		ListEditorRemoveButton deleteLink = new ListEditorRemoveButton("delete", "Delete Item");
		add(deleteLink);

		if (tree.size() > level + 1){
			final ListEditor list = new ListEditor("list", levelChildren) {
				private static final long serialVersionUID = 7218457979728871528L;
				@Override
				protected void onPopulateItem(
						org.dgfoundation.amp.onepager.components.ListItem item) {
					try {
						Class childClass = tree.get(level + 1);
						AmpIssueTreePanel aitp = new AmpIssueTreePanel("item", tree, setName, labelName, item.getModel(), levelChildren, levelClass, level + 1, fmName);
						aitp.setOutputMarkupId(true);
						item.add(aitp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			add(list);
			
			AmpAddLinkField addLink = new AmpAddLinkField("add","Add Item") {
				@Override
				protected void onClick(AjaxRequestTarget target) {
					//Set children = (Set) levelChildren.getObject();
					Class childClass = tree.get(level + 1);
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpMeasure") == 0){
						AmpMeasure m = new AmpMeasure();
						m.setActors(new HashSet());
						m.setIssue((AmpIssues) objModel.getObject());
						m.setName(new String(""));
						list.addItem(m);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpActor") == 0){
						AmpActor a = new AmpActor();
						a.setMeasure((AmpMeasure) objModel.getObject());
						a.setName(new String(""));
						list.addItem(a);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure") == 0){
						AmpRegionalObservationMeasure a = new AmpRegionalObservationMeasure();
						a.setActors(new HashSet());
						a.setName(new String(""));
						a.setRegionalObservation((AmpRegionalObservation)objModel.getObject());
						a.setName(new String(""));
						list.addItem(a);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpRegionalObservationActor") == 0){
						AmpRegionalObservationActor a = new AmpRegionalObservationActor();
						a.setMeasure((AmpRegionalObservationMeasure) objModel.getObject());
						a.setName(new String(""));
						list.addItem(a);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpLineMinistryObservationMeasure") == 0){
						AmpLineMinistryObservationMeasure a = new AmpLineMinistryObservationMeasure();
						a.setActors(new HashSet());
						a.setName(new String(""));
						a.setLineMinistryObservation((AmpLineMinistryObservation)objModel.getObject());
						a.setName(new String(""));
						list.addItem(a);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpLineMinistryObservationActor") == 0){
						AmpLineMinistryObservationActor a = new AmpLineMinistryObservationActor();
						a.setMeasure((AmpLineMinistryObservationMeasure) objModel.getObject());
						a.setName(new String(""));
						list.addItem(a);
					}
					target.add(this.getParent());
				}
			};
			
			
			add(addLink);
			
			// AMP-11750
			String title ="";
			if (tree.size() > level + 1){				
				title = labelName.get(tree.get(level+ 1));
			}
			title = TranslatorUtil.getTranslation("Add "+  title + ":");
			Label addTitle = new TrnLabel("addTitle", title);						
			add(addTitle);
			
		}
		else{
			WebMarkupContainer addImg = new WebMarkupContainer("add");
			addImg.setVisible(false);
			add(addImg);
			
			WebMarkupContainer wmc = new WebMarkupContainer("list");
			wmc.add(new WebMarkupContainer("issue"));
			wmc.setVisible(false);
			add(wmc);
			
			WebMarkupContainer addTitle = new WebMarkupContainer("addTitle");			
			addTitle.setVisible(false);
			add(addTitle);
		}
		

	}
}
