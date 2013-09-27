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
import org.dgfoundation.amp.onepager.events.FundingOrgListUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpRelatedOrgsModel;
import org.dgfoundation.amp.onepager.models.AmpRelatedRolesModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
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

	
	
	public AmpOrgRoleSelectorComponent(String id, IModel<AmpActivityVersion> am,String [] roleFilter) {
		this(id, am, new Model<AmpRole>(), new Model<AmpOrganisation>(), false,roleFilter, true);
	}

	public AmpOrgRoleSelectorComponent(String id,
			IModel<AmpActivityVersion> am, IModel<AmpRole> roleModel,
			IModel<AmpOrganisation> orgModel,String [] roleFilter) {
		this(id, am, roleModel, orgModel, true,roleFilter, true);
	}

	public AmpOrgRoleSelectorComponent(String id,
			IModel<AmpActivityVersion> am, IModel<AmpRole> roleModel,
			IModel<AmpOrganisation> orgModel, boolean recipientMode, String [] roleFilter, boolean hideNewLine) {
		super(id, am);

		// read the list of roles from Related Organizations page, and create a
		// unique Set with the roles chosen
		AbstractReadOnlyModel<List<AmpRole>> rolesList = new AmpRelatedRolesModel(
				am,roleFilter);	
		// selector for organization role
		roleSelect = new AmpSelectFieldPanel<AmpRole>("roleSelect", roleModel,
				rolesList,(recipientMode?"Recipient ":"")+"Org Role", false, false, new TranslatedChoiceRenderer<AmpRole>(), hideNewLine);
        roleSelect.add(UpdateEventBehavior.of(FundingOrgListUpdateEvent.class));
		
		// read the list of organizations from related organizations page, and
		// create a unique set with the orgs chosen
		AbstractReadOnlyModel<List<AmpOrganisation>> orgsList = new AmpRelatedOrgsModel(
				am, roleSelect.getChoiceContainer());


		// selector for related orgs
		orgSelect = new AmpSelectFieldPanel<AmpOrganisation>("orgSelect",
				orgModel, orgsList, (recipientMode?"Recipient ":"")+"Organization", false, true, null, hideNewLine);
        orgSelect.add(UpdateEventBehavior.of(FundingOrgListUpdateEvent.class));

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
