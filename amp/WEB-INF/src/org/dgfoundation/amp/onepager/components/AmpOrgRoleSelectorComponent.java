/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpRelatedOrgsModel;
import org.dgfoundation.amp.onepager.models.AmpRelatedRolesModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author mihai
 * @since 06.2013
 * Component wrapping an org role selector and an org selector, chained. Reading data from {@link AmpActivityVersion#getOrgrole()}
 */
public class AmpOrgRoleSelectorComponent extends Panel {

	private AmpSelectFieldPanel<AmpRole> roleSelect;
	private AmpSelectFieldPanel<AmpOrganisation> orgSelect;
	private boolean recipientMode = true;

	public AmpOrgRoleSelectorComponent(String id, IModel<AmpActivityVersion> am) {
		this(id, am, new Model<AmpRole>(), new Model<AmpOrganisation>(), false);
	}

	public AmpOrgRoleSelectorComponent(String id,
			IModel<AmpActivityVersion> am, IModel<AmpRole> roleModel,
			IModel<AmpOrganisation> orgModel) {
		this(id, am, roleModel, orgModel, true);
	}

	public AmpOrgRoleSelectorComponent(String id,
			IModel<AmpActivityVersion> am, IModel<AmpRole> roleModel,
			IModel<AmpOrganisation> orgModel, boolean recipientMode) {
		super(id, am);

		// read the list of roles from Related Organizations page, and create a
		// unique Set with the roles chosen
		AbstractReadOnlyModel<List<AmpRole>> rolesList = new AmpRelatedRolesModel(
				am);	
		// selector for organization role
		roleSelect = new AmpSelectFieldPanel<AmpRole>("roleSelect", roleModel,
				rolesList,(recipientMode?"Recipient ":"")+"Org Role", false, false, null, true);
		
		// read the list of organizations from related organizations page, and
		// create a unique set with the orgs chosen
		AbstractReadOnlyModel<List<AmpOrganisation>> orgsList = new AmpRelatedOrgsModel(
				am, roleSelect.getChoiceContainer());


		// selector for related orgs
		orgSelect = new AmpSelectFieldPanel<AmpOrganisation>("orgSelect",
				orgModel, orgsList, (recipientMode?"Recipient ":"")+"Organization", false, true, null, true);

		// when the role select changes, refresh the org selector
		roleSelect.getChoiceContainer().add(
				new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 7592988148376828926L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(orgSelect);
					}

				});

		add(roleSelect);
		add(orgSelect);

	}

	/**
	 * @return the roleSelect
	 */
	public AmpSelectFieldPanel<AmpRole> getRoleSelect() {
		return roleSelect;
	}

	/**
	 * @param roleSelect
	 *            the roleSelect to set
	 */
	public void setRoleSelect(AmpSelectFieldPanel<AmpRole> roleSelect) {
		this.roleSelect = roleSelect;
	}

	/**
	 * @return the orgSelect
	 */
	public AmpSelectFieldPanel<AmpOrganisation> getOrgSelect() {
		return orgSelect;
	}

	/**
	 * @param orgSelect
	 *            the orgSelect to set
	 */
	public void setOrgSelect(AmpSelectFieldPanel<AmpOrganisation> orgSelect) {
		this.orgSelect = orgSelect;
	}

}
