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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpIssueTreePanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;

/**
 * @author aartimon@dginternational.org 
 * @since Nov 9, 2010
 */
public class AmpRegionalObservationsFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083784446344L;

	public AmpRegionalObservationsFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		final PropertyModel<Set<AmpRegionalObservation>> setModel=new PropertyModel<Set<AmpRegionalObservation>>(am,"regionalObservations");
		final ListView<AmpRegionalObservation> list;

		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton","Add Observation", "Add Observation") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpRegionalObservation issues = new AmpRegionalObservation();
				issues.setName(new String(""));
				issues.setObservationDate(new Date());
				issues.setRegionalObservationMeasures(new HashSet());
				issues.setActivity(am.getObject());
				setModel.getObject().add(issues);
				target.addComponent(this.getParent());
			}
		};
		add(addbutton);

		IModel<List<AmpRegionalObservation>> listModel = new AbstractReadOnlyModel<List<AmpRegionalObservation>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpRegionalObservation> getObject() {
				return new ArrayList<AmpRegionalObservation>(setModel.getObject());
			}
		};
	
		final List<Class> classTree = new ArrayList<Class>();
		final Map<Class, String> setName = new HashMap<Class, String>();
		classTree.add(AmpRegionalObservation.class);
		classTree.add(AmpRegionalObservationMeasure.class);
		classTree.add(AmpRegionalObservationActor.class);
		setName.put(AmpRegionalObservation.class, "regionalObservationMeasures");
		setName.put(AmpRegionalObservationMeasure.class, "actors");
		final Map<Class, String> labelName = new HashMap<Class, String>();
		labelName.put(AmpRegionalObservation.class, "Observation");
		labelName.put(AmpRegionalObservationMeasure.class, "Measure");
		labelName.put(AmpRegionalObservationActor.class, "Actor");

		list = new ListView<AmpRegionalObservation>("list", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpRegionalObservation> item) {
				try {
					AmpIssueTreePanel aitp = new AmpIssueTreePanel("issue", classTree, setName, labelName, item.getModel(), setModel, AmpRegionalObservation.class, 0, "Regional Obsevation Field");
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
