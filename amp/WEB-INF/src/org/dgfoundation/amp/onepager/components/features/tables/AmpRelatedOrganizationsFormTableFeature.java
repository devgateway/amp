/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 26, 2010
 */
public class AmpRelatedOrganizationsFormTableFeature extends AmpFormTableFeaturePanel<AmpActivity,AmpOrgRole> {

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpRelatedOrganizationsFormTableFeature(String id, String fmName,
			final IModel<AmpActivity> am, final String roleName) throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpOrgRole>> setModel=new PropertyModel<Set<AmpOrgRole>>(am,"orgrole");
		final AmpRole specificRole = DbUtil.getAmpRole(roleName);

		IModel<List<AmpOrgRole>> listModel = new AbstractReadOnlyModel<List<AmpOrgRole>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpOrgRole> getObject() {
				Set<AmpOrgRole> allOrgRoles = setModel.getObject();
				Set<AmpOrgRole> specificOrgRoles = new HashSet<AmpOrgRole>();  
				
				
				Iterator<AmpOrgRole> it = allOrgRoles.iterator();
				while (it.hasNext()) {
					AmpOrgRole ampOrgRole = (AmpOrgRole) it.next();
					if (ampOrgRole.getRole().getAmpRoleId().compareTo(specificRole.getAmpRoleId()) == 0)
						specificOrgRoles.add(ampOrgRole);
				}
				
				return new ArrayList<AmpOrgRole>(specificOrgRoles);
			}
		};

		list = new ListView<AmpOrgRole>("list", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpOrgRole> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new TextField<String>(
						"departmentDivision",
						new PropertyModel<String>(item.getModel(), "additionalInfo")));
				
				item.add(new Label("name", item.getModelObject().getOrganisation().getAcronymAndName()));			
				
				AmpDeleteLinkField delRelOrg = new AmpDeleteLinkField(
						"delRelOrg", "Delete Related Organisation") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
					}
				};
				item.add(delRelOrg);
				
			}
		};
		list.setReuseItems(true);
		add(list);

		final AbstractAmpAutoCompleteTextField<AmpOrganisation> autoComplete = new AbstractAmpAutoCompleteTextField<AmpOrganisation>(AmpOrganisationSearchModel.class) {
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpOrganisation choice)
					throws Throwable {
				return choice.getAcronymAndName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpOrganisation choice) {
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setOrganisation(choice);
				ampOrgRole.setActivity(am.getObject());
				ampOrgRole.setRole(specificRole);
				setModel.getObject().add(ampOrgRole);
				list.removeAll();
				target.addComponent(list.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};

		final AmpComboboxFieldPanel<AmpOrganisation> searchOrgs=new AmpComboboxFieldPanel<AmpOrganisation>("search", "Search Organizations", autoComplete);
		add(searchOrgs);
		
	}

}
