/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesTableFeaturePanel extends AmpFormTableFeaturePanel implements IHeaderContributor {


    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel model,  String fmName) throws Exception {
        super(id, model, fmName);
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLeadingNewLine
     * @throws Exception
     */
    public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel<Set<AmpTeam>> model, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);

        AbstractReadOnlyModel<List<AmpTeam>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
        list = new PageableListView<AmpTeam>("usersList", listModel, 5) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpTeam> item) {
                item.add(new Label("workspaceName", item.getModelObject().getName()));
                
                DropDownChoice<String> strategies = new DropDownChoice<String>("permStrategies", new PropertyModel<String>(item.getModelObject(), "permissionStrategy"),PMUtil.PERM_STRATEGIES);
                item.add(strategies);
                strategies.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    protected void onUpdate(AjaxRequestTarget target) {
                        PMUtil.updateAmpObj(item.getModelObject());
                        target.add(AmpPMManageWorkspacesTableFeaturePanel.this);
                    }
                });
            }
        };
        list.setReuseItems(true);
        add(list);
        
    }
    
}

