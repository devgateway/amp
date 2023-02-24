/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMAjaxPagingNavigator;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMPermContentBean;

/**
 * @author dan
 *
 */
public class AmpPMViewAssignedPermissionTable extends AmpComponentPanel {
    
    protected ListView list;

    /**
     * @param id
     * @param fmName
     * @param fmType
     */
    public AmpPMViewAssignedPermissionTable(String id, String fmName,
            AmpFMTypes fmType) {
        super(id, fmName, fmType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpPMViewAssignedPermissionTable(String id, String fmName) {
        super(id, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param fmBehavior
     */
    public AmpPMViewAssignedPermissionTable(String id, IModel model,
            String fmName, AmpFMTypes fmBehavior) {
        super(id, model, fmName, fmBehavior);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpPMViewAssignedPermissionTable(String id, IModel<Set<AmpPMPermContentBean>> permContentModel, String fmName) {
        super(id, permContentModel, fmName);
        // TODO Auto-generated constructor stub
        
        AbstractReadOnlyModel<List<AmpPMPermContentBean>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(permContentModel);

        list = new PageableListView<AmpPMPermContentBean>("permContentList", listModel, 15) {
            
            @Override
            protected void populateItem(final ListItem<AmpPMPermContentBean> item) {
                AmpPMPermContentBean p = item.getModel().getObject();
                ////System.out.println(p.getName());
                item.add(new Label("label", p.getLabel()));
                //item.add(new Label("path", p.getEdit()));
                //item.add(new Label("strategy", p.get));
                WebMarkupContainer editImg = new WebMarkupContainer("editIcon");
                String noIcon = "/TEMPLATE/ampTemplate/images/erase.png";
                String okIcon = "/TEMPLATE/ampTemplate/images/check.png";
                if(p.getEdit())
                    editImg.add(new AttributeModifier("src", new Model(okIcon)));
                else editImg.add(new AttributeModifier("src", new Model(noIcon)));
                item.add(editImg);
                
                WebMarkupContainer viewImg = new WebMarkupContainer("viewIcon");
                if(p.getView())
                    viewImg.add(new AttributeModifier("src", new Model(okIcon)));
                else viewImg.add(new AttributeModifier("src", new Model(noIcon)));
                item.add(viewImg);

            }
        };
        list.setReuseItems(true);
        add(list);
        
        AmpPMAjaxPagingNavigator pager = new AmpPMAjaxPagingNavigator("navigator", (PageableListView)list);
        add(pager);
        
        
    }

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

}
