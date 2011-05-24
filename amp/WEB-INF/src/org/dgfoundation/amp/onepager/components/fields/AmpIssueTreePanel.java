/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpIssues;
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
		
		final Class levelClass = tree.get(level);
		final String levelLabel = labelName.get(levelClass);
		String levelChildrenName = setName.get(levelClass);
		final PropertyModel levelChildren = new PropertyModel(objModel, levelChildrenName);
		
		
		final TextField name = new TextField("name", new PropertyModel(objModel.getObject(),"name"));
		addFormComponent(name);
		Label label = new Label("label", levelLabel);
		add(label);
		
		if (levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpIssues") == 0 || 
				levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpRegionalObservation") == 0 ){
			final DateTextField date;
			
			if (levelClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpIssues") == 0)
				date = new DateTextField("date", new PropertyModel(objModel.getObject(),"issueDate"));
			else 
				date = new DateTextField("date", new PropertyModel(objModel.getObject(),"observationDate"));
					
			date.setOutputMarkupId(true);
			
			DatePicker dp = new DatePicker() {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected boolean enableMonthYearSelection() {
					return true;
				}
			};
			dp.setShowOnFieldClick(true);
			date.add(dp);
			addFormComponent(date);
		}
		else{
			WebMarkupContainer wmc = new WebMarkupContainer("date");
			wmc.setVisible(false);
			add(wmc);
		}
		
		AmpDeleteLinkField deleteLink = new AmpDeleteLinkField("delete", "Delete Item") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				Object obj = objModel.getObject();
				Set<AmpIssues> tmpset = ((Set<AmpIssues>)parentSet.getObject());
				tmpset.remove(obj);
				/*
				 * Ugly! Can be replaced only by reloading the whole tree with
				 * ...(this.findParent(AmpIssuesFormSectionFeature.class);
				 */
				target.addComponent((Component) this.getParent().getParent().getParent().getParent());
			}	
		};
		add(deleteLink);
		
		
		
		if (tree.size() > level + 1){
			AmpAddLinkField addLink = new AmpAddLinkField("add","Add Item") {
				@Override
				protected void onClick(AjaxRequestTarget target) {
					Set children = (Set) levelChildren.getObject();
					Class childClass = tree.get(level + 1);
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpMeasure") == 0){
						AmpMeasure m = new AmpMeasure();
						m.setActors(new HashSet());
						m.setIssue((AmpIssues) objModel.getObject());
						m.setName(new String(""));
						children.add(m);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpActor") == 0){
						AmpActor a = new AmpActor();
						a.setMeasure((AmpMeasure) objModel.getObject());
						a.setName(new String(""));
						children.add(a);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure") == 0){
						AmpRegionalObservationMeasure a = new AmpRegionalObservationMeasure();
						a.setActors(new HashSet());
						a.setName(new String(""));
						a.setRegionalObservation((AmpRegionalObservation)objModel.getObject());
						a.setName(new String(""));
						children.add(a);
					}
					if (childClass.getCanonicalName().compareTo("org.digijava.module.aim.dbentity.AmpRegionalObservationActor") == 0){
						AmpRegionalObservationActor a = new AmpRegionalObservationActor();
						a.setMeasure((AmpRegionalObservationMeasure) objModel.getObject());
						a.setName(new String(""));
						children.add(a);
					}
					target.addComponent(this.getParent());
				}
			};
			add(addLink);

			IModel<List<?>> listModel = new AbstractReadOnlyModel<List<?>>() {
				private static final long serialVersionUID = 3706184421459839210L;
				
				@Override
				public List<?> getObject() {
					return new ArrayList((Set)levelChildren.getObject());
				}
			};
			
			ListView list = new ListView("list", listModel) {
				private static final long serialVersionUID = 7218457979728871528L;
				@Override
				protected void populateItem(final ListItem item) {
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
			list.setReuseItems(false);
			add(list);
		}
		else{
			WebMarkupContainer addImg = new WebMarkupContainer("add");
			addImg.setVisible(false);
			add(addImg);
			
			WebMarkupContainer wmc = new WebMarkupContainer("list");
			wmc.add(new WebMarkupContainer("issue"));
			wmc.setVisible(false);
			add(wmc);
		}
	}
}
