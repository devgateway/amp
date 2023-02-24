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
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
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
        AmpFormSectionFeaturePanel implements AmpRequiredComponentContainer {

    private static final long serialVersionUID = -6654390083784446344L;

    private List<FormComponent<?>> requiredFormComponents = new ArrayList<>();

    public AmpRegionalObservationsFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final PropertyModel<Set<AmpRegionalObservation>> setModel=new PropertyModel<Set<AmpRegionalObservation>>(am,"regionalObservations");
        final ListEditor<AmpRegionalObservation> list;
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpRegionalObservation>());
        
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

        list = new ListEditor<AmpRegionalObservation>("list", setModel, new AmpRegionalObservation.RegionalObservationComparator()) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpRegionalObservation> item) {
                try {
                    AmpIssueTreePanel aitp = new AmpIssueTreePanel("issue", classTree, setName, labelName,
                            item.getModel(), 0, "Regional Obsevation Field",
                            AmpRegionalObservationsFormSectionFeature.this);
                    aitp.setOutputMarkupId(true);
                    item.add(aitp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        add(list);

        AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton","Add Observation", "Add Observation") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpRegionalObservation issues = new AmpRegionalObservation();
                issues.setName(new String(""));
                issues.setObservationDate(new Date());
                issues.setRegionalObservationMeasures(new HashSet());
                issues.setActivity(am.getObject());
                list.addItem(issues);
                target.add(this.getParent());
            }
        };
        add(addbutton);
    }

    @Override
    public List<FormComponent<?>> getRequiredFormComponents() {
        return requiredFormComponents;
    }
}
