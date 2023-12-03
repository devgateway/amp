/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpIssueTreePanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservation;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationActor;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationMeasure;

import java.util.*;

/**
 * @author aartimon@dginternational.org 
 * @since Nov 9, 2010
 */
public class AmpLineMinistryObservationsFormSectionFeature extends
        AmpFormSectionFeaturePanel implements AmpRequiredComponentContainer {

    private static final long serialVersionUID = -6654390083784446344L;

    private List<FormComponent<?>> requiredFormComponents = new ArrayList<>();

    public AmpLineMinistryObservationsFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final PropertyModel<Set<AmpLineMinistryObservation>> setModel=new PropertyModel<Set<AmpLineMinistryObservation>>(am,"lineMinistryObservations");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpLineMinistryObservation>());
        
        final List<Class> classTree = new ArrayList<Class>();
        final Map<Class, String> setName = new HashMap<Class, String>();
        classTree.add(AmpLineMinistryObservation.class);
        classTree.add(AmpLineMinistryObservationMeasure.class);
        classTree.add(AmpLineMinistryObservationActor.class);
        setName.put(AmpLineMinistryObservation.class, "lineMinistryObservationMeasures");
        setName.put(AmpLineMinistryObservationMeasure.class, "actors");
        final Map<Class, String> labelName = new HashMap<Class, String>();
        labelName.put(AmpLineMinistryObservation.class, "Observation");
        labelName.put(AmpLineMinistryObservationMeasure.class, "Measure");
        labelName.put(AmpLineMinistryObservationActor.class, "Actor");

        final ListEditor<AmpLineMinistryObservation> list = new ListEditor<AmpLineMinistryObservation>("list", setModel) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpLineMinistryObservation> item) {
                try {
                    AmpIssueTreePanel aitp = new AmpIssueTreePanel("issue", classTree, setName, labelName,
                            item.getModel(), 0, "Regional Obsevation Field",
                            AmpLineMinistryObservationsFormSectionFeature.this);
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
                AmpLineMinistryObservation issues = new AmpLineMinistryObservation();
                issues.setName(new String(""));
                issues.setObservationDate(new Date());
                issues.setLineMinistryObservationMeasures(new HashSet());
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
