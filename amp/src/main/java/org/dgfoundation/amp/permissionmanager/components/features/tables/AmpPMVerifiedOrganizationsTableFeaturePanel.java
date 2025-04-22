/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.util.List;
import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMVerifiedOrganizationsTableFeaturePanel extends AmpFormTableFeaturePanel {



    public AmpPMVerifiedOrganizationsTableFeaturePanel(String id,final IModel<Set<AmpOrganisation>> orgsSetmodel, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, orgsSetmodel, fmName, hideLeadingNewLine);
        
        final AbstractReadOnlyModel<List<AmpOrganisation>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(orgsSetmodel);
        
        list = new PageableListView<AmpOrganisation>("verifiedOrgsList", listModel, 5) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpOrganisation> item) {
                final MarkupContainer listParent=this.getParent();
                item.add(new Label("orgName", item.getModelObject().getName()));
                item.add(new Label("orgAcronym", item.getModelObject().getAcronym()));
                
                final AmpDeleteLinkField propertyDeleteLink = new AmpDeleteLinkField("removeSelectedOrganization", "Remove Selected Organization Link") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        orgsSetmodel.getObject().remove(item.getModelObject());
                        list.removeAll();
                        target.add(AmpPMVerifiedOrganizationsTableFeaturePanel.this.getParent());
                    }
                };
                item.add(propertyDeleteLink);
                
            }
        };
        list.setReuseItems(true);
        add(list);
    }

    public AmpPMVerifiedOrganizationsTableFeaturePanel(String id, IModel<Set<AmpOrganisation>> model, String fmName) throws Exception {
        super(id, model, fmName);
        
    }

    

}
