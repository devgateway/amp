/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMAjaxPagingNavigator;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMPermContentBean;
import org.digijava.module.gateperm.core.Permission;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author dan
 *
 */
public class AmpPMViewFieldPermissionTableFeaturePanel extends AmpFormTableFeaturePanel implements IHeaderContributor{

    
    public AmpPMViewFieldPermissionTableFeaturePanel(String id, IModel<Set<Permission>> model, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);
        // TODO Auto-generated constructor stub
    }

    public AmpPMViewFieldPermissionTableFeaturePanel(String id, IModel<Set<AmpPMFieldPermissionViewer>> permsModel, String fmName) throws Exception {
        super(id, permsModel, fmName);

        //AbstractReadOnlyModel<List<Permission>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
        
        AbstractReadOnlyModel<List<AmpPMFieldPermissionViewer>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(permsModel);

        list = new PageableListView<AmpPMFieldPermissionViewer>("permList", listModel, 10) {
            
            @Override
            protected void populateItem(final ListItem<AmpPMFieldPermissionViewer> item) {
                AmpPMFieldPermissionViewer p = item.getModel().getObject();
                ////System.out.println(p.getName());
                item.add(new Label("name", p.getName()));
                item.add(new Label("path", p.getPath()));
                item.add(new Label("strategy", p.getStrategy()));
                final IModel<Set<AmpPMPermContentBean>> permMapModel = new Model((Serializable)p.getAssignedPerms());
                AmpPMViewAssignedPermissionTable content = new AmpPMViewAssignedPermissionTable("description", permMapModel, "Permission Content Table");
                item.add(content);
            }
        };
        list.setReuseItems(true);
        add(list);
        
        AmpPMAjaxPagingNavigator pager = new AmpPMAjaxPagingNavigator("navigatorList", (PageableListView)list);
        add(pager);
        
    }
    

}
