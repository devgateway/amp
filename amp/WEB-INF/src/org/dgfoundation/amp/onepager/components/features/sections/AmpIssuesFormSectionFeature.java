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
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpMeasure;

import java.util.*;

/**
 * @author aartimon@dginternational.org 
 * @since Oct 26, 2010
 */
public class AmpIssuesFormSectionFeature extends
        AmpFormSectionFeaturePanel implements AmpRequiredComponentContainer {

    private static final long serialVersionUID = -6654390083784446344L;

    private List<FormComponent<?>> requiredFormComponents = new ArrayList<>();

    public AmpIssuesFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final PropertyModel<Set<AmpIssues>> setModel=new PropertyModel<Set<AmpIssues>>(am,"issues");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpIssues>());
        
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

        final ListEditor<AmpIssues> list = new ListEditor<AmpIssues>("list", setModel) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpIssues> item) {
                try {
                    AmpIssueTreePanel aitp = new AmpIssueTreePanel("issue", classTree, setName, labelName,
                            item.getModel(), 0, "Issue Field", AmpIssuesFormSectionFeature.this);
                    aitp.setOutputMarkupId(true);
                    item.add(aitp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        };
        add(list);
        
        AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Issue", "Add Issue") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpIssues issues = new AmpIssues();
                issues.setName(new String(""));
                issues.setIssueDate(new Date());
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
