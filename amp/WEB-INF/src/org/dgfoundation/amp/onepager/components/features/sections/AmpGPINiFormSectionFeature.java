/**
 * Copyright (c) 2017 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpGPINiOrgRoleItemFeaturePanel;
import org.dgfoundation.amp.onepager.events.OrganisationUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.FilteredListModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;

/**
 * GPI NI section
 * 
 * @author Viorel Chihai
 * @since Mar 01, 2017
 */
public class AmpGPINiFormSectionFeature extends AmpFormSectionFeaturePanel {
	private static final long serialVersionUID = 1L;

	public AmpGPINiFormSectionFeature(String id, String fmName, final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);

		PropertyModel<Set<AmpOrgRole>> orgRoles = new PropertyModel<Set<AmpOrgRole>>(am, "orgrole");
		AbstractReadOnlyModel<List<AmpOrgRole>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(orgRoles, AmpOrgRole.BY_ACRONYM_AND_NAME_COMPARATOR);
		FilteredListModel<AmpOrgRole> filteredListModel = new FilteredListModel<AmpOrgRole>(listModel.getObject()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean accept(AmpOrgRole o) {
				return hasDonorFundings(o);
			}
		};
		
		final ListView<AmpOrgRole> list = new ListView<AmpOrgRole>("list", filteredListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AmpOrgRole> item) {
				AmpGPINiOrgRoleItemFeaturePanel orgRoleItem = new AmpGPINiOrgRoleItemFeaturePanel("item", "GPI NI Survey", item.getModel(), am, AmpGPINiFormSectionFeature.this);
				item.add(orgRoleItem);
			}
		};
		
		list.setOutputMarkupId(true);
		setOutputMarkupId(true);
		add(list);
		add(UpdateEventBehavior.of(OrganisationUpdateEvent.class));
	}
	
	private boolean hasDonorFundings(AmpOrgRole donor) {
		AmpActivityVersion activity = donor.getActivity();
		
		for (AmpFunding f : activity.getFunding()) {
			int orgCompareResult = ComparatorUtils.nullLowComparator(null)
					.compare(f.getAmpDonorOrgId(), donor.getOrganisation());
			
			int roleCompareResult = ComparatorUtils.nullLowComparator(null)
					.compare(f.getSourceRole(), donor.getRole());
			
			if (orgCompareResult == 0 && roleCompareResult == 0) {
				return true;
			}
		}
		
		return false;
	}
}
