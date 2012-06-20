/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesTableFeaturePanel extends AmpFormTableFeaturePanel implements IHeaderContributor {


	
	private List<TransparentWebMarkupContainer> sliders;
	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel model,	String fmName) throws Exception {
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
		sliders = new ArrayList<TransparentWebMarkupContainer>();

		AbstractReadOnlyModel<List<AmpTeam>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
		list = new PageableListView<AmpTeam>("usersList", listModel, 5) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpTeam> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new Label("workspaceName", item.getModelObject().getName()));
				final TransparentWebMarkupContainer slider;
				slider = new TransparentWebMarkupContainer("sliderWorkspaceInfo");
				slider.setOutputMarkupId(true);
				item.add(slider);
				sliders.add(slider);
				AmpPMViewUsersTableFeaturePanel usersList = null;
				try {
						usersList = new AmpPMViewUsersTableFeaturePanel("workspaceMembers", item.getModel(), "Workspace Members", false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				item.add(usersList);

			}
		};
		list.setReuseItems(true);
		add(list);
		
	}
	
	public List<TransparentWebMarkupContainer> getSliders() {
		return sliders;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		 for (TransparentWebMarkupContainer c: sliders) {
			response.renderOnDomReadyJavaScript(OnePagerUtil.getToggleJS(c));	
		} ;
		 
	}
}

