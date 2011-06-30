/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpIssueTreePanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpMeasure;

/**
 * @author aartimon@dginternational.org 
 * @since Oct 26, 2010
 */
public class AmpIssuesFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083784446344L;

	public AmpIssuesFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final PropertyModel<Set<AmpIssues>> setModel=new PropertyModel<Set<AmpIssues>>(am,"issues");
		final ListView<AmpIssues> list;

		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpIssues>());
		
		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Issue", "Add Issue") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpIssues issues = new AmpIssues();
				issues.setName(new String(""));
				issues.setIssueDate(new Date());
				issues.setMeasures(new HashSet());
				issues.setActivity(am.getObject());
				
				setModel.getObject().add(issues);
				target.addComponent(this.getParent());
			}
		};
		add(addbutton);

		IModel<List<AmpIssues>> listModel = new AbstractReadOnlyModel<List<AmpIssues>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpIssues> getObject() {
				return new ArrayList<AmpIssues>(setModel.getObject());
			}
		};
	
		final List<Class> classTree = new ArrayList<Class>();
		final Map<Class, String> setName = new HashMap<Class, String>();
		classTree.add(AmpIssues.class);
		classTree.add(AmpMeasure.class);
		classTree.add(AmpActor.class);
		setName.put(AmpIssues.class, "measures");
		setName.put(AmpMeasure.class, "actors");
		final Map<Class, String> labelName = new HashMap<Class, String>();
		labelName.put(AmpIssues.class, "Issue");
		labelName.put(AmpMeasure.class, "Measure");
		labelName.put(AmpActor.class, "Actor");

		list = new ListView<AmpIssues>("list", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpIssues> item) {
				try {
					AmpIssueTreePanel aitp = new AmpIssueTreePanel("issue", classTree, setName, labelName, item.getModel(), setModel, AmpIssues.class, 0, "Issue Field");
					aitp.setOutputMarkupId(true);
					item.add(aitp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		list.setReuseItems(true);
		add(list);
	}

}
