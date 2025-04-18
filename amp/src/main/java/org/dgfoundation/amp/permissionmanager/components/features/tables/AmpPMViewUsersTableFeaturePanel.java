/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author dan
 *
 */
public class AmpPMViewUsersTableFeaturePanel extends AmpFormTableFeaturePanel {

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMViewUsersTableFeaturePanel(String id, IModel model, String fmName) throws Exception {
        super(id, model, fmName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLeadingNewLine
     * @throws Exception
     */
    public AmpPMViewUsersTableFeaturePanel(String id, IModel<AmpTeam> model, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);

        Set<TeamMember> col = new TreeSet<TeamMember>(TeamMemberUtil.getAllTeamMembers(model.getObject().getAmpTeamId()));
        final IModel<Set<TeamMember>> setModel = new Model((Serializable) col);
        AbstractReadOnlyModel<List<TeamMember>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);
        
        list = new PageableListView<TeamMember>("usersListWorkspace", listModel, 5) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<TeamMember> item) {
                final MarkupContainer listParent=this.getParent();
                TransparentWebMarkupContainer userImgSrc = new TransparentWebMarkupContainer("userImgSrc");
                userImgSrc.setOutputMarkupId(true);
                AttributeModifier srcModifier = null;
                TeamMember t = item.getModelObject();
                if(item.getModelObject().getTeamHead()) 
                    userImgSrc.add(new AttributeModifier("src",new Model<>(PMUtil.WORKSPACE_MANAGER_IMG_SRC)));
                else userImgSrc.add(new AttributeModifier("src",new Model<>(PMUtil.WORKSPACE_MEMBER_IMG_SRC)));
                item.add(userImgSrc);
                item.add(new Label("userLabel", item.getModelObject().getMemberName()));
                item.add(new Label("userEmailLabel", item.getModelObject().getEmail()));
                item.add(new Label("userRole", item.getModelObject().getRoleName()));
            }
        };
        list.setReuseItems(true);
        add(list);
        
    
    }

}
