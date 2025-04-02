/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMSearchOrganizationsFeaturePanel;
import org.digijava.kernel.user.User;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersTableFeaturePanel extends AmpFormTableFeaturePanel implements IHeaderContributor{

    private List<TransparentWebMarkupContainer> sliders;
    
    public AmpPMManageUsersTableFeaturePanel(String id, IModel<Set<User>> model, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);
        // TODO Auto-generated constructor stub
    }

    public AmpPMManageUsersTableFeaturePanel(String id, IModel<Set<User>> model, String fmName) throws Exception {
        super(id, model, fmName);
        sliders = new ArrayList<TransparentWebMarkupContainer>();

        AbstractReadOnlyModel<List<User>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
        
        list = new PageableListView<User>("usersList", listModel, 5) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("userLabel", item.getModelObject().getName()));
                item.add(new Label("userEmailLabel", item.getModelObject().getEmail()));

                final TransparentWebMarkupContainer slider;
                slider = new TransparentWebMarkupContainer("sliderUserInfo");
                slider.setOutputMarkupId(true);
                item.add(slider);
                sliders.add(slider);
                try {
                    item.add(new AmpPMSearchOrganizationsFeaturePanel("assignedOrgsPerUser", item.getModel(), "Assigning Organizations", true));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }   
            }
        };
        list.setReuseItems(true);
        add(list);
        
    }
    
    public List<TransparentWebMarkupContainer> getSliders() {
        return sliders;
    }

    public void setSliders(List<TransparentWebMarkupContainer> sliders) {
        this.sliders = sliders;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
         for (TransparentWebMarkupContainer c: sliders) {
             response.render(OnDomReadyHeaderItem.forScript(OnePagerUtil.getToggleJS(c)));
        };
         
    }

    

}
