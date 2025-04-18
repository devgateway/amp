/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.log4j.Logger;
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
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai
 * @since 06.2013
 * Component wrapping an org role selector and an org selector, chained. Reading data from {@link AmpActivityVersion#getOrgrole()}
 */
public class AmpOrgRoleSelectorComponent extends Panel {
    private static Logger logger = Logger.getLogger(AmpOrgRoleSelectorComponent.class);
    private final AmpComponentPanel showDonorOrganization;
    private final AmpComponentPanel showResponsibleOrganization;
    private final AmpComponentPanel showExecutingAgency;
    private final AmpComponentPanel showImplementingAgency;
    private final AmpComponentPanel showBeneficiaryAgency;
    private final AmpComponentPanel showContractingAgency;
    private final AmpComponentPanel showRegionalGroup;
    private final AmpComponentPanel showSectorGroup;
    
    private AmpSelectFieldPanel<AmpRole> roleSelect;
    private AmpSelectFieldPanel<AmpOrganisation> orgSelect;
    private boolean recipientMode = true;

    AbstractReadOnlyModel<List<AmpRole>> rolesList;
    
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

        
        
        //for the list of organizations
        showDonorOrganization = new AmpComponentPanel("showDonorOrganization", "Add Donor Organization"){};
        add(showDonorOrganization);
        showResponsibleOrganization = new AmpComponentPanel("showResponsibleOrganization", "Add Responsible Organization"){};
        add(showResponsibleOrganization);
        showExecutingAgency= new AmpComponentPanel("showExecutingAgency", "Add Executing Agency"){};
        add(showExecutingAgency);
        showImplementingAgency=new AmpComponentPanel("showImplementingAgency", "Add Implementing Agency"){};
        add(showImplementingAgency);
        showBeneficiaryAgency=new AmpComponentPanel("showBeneficiaryAgency", "Add Beneficiary Agency"){};
        add(showBeneficiaryAgency);
        showContractingAgency=new AmpComponentPanel("showContractingAgency", "Add Contracting Agency"){};
        add(showContractingAgency);
        showRegionalGroup=new AmpComponentPanel("showRegionalGroup", "Add Regional Group"){};
        add(showRegionalGroup);
        showSectorGroup=new AmpComponentPanel("showSectorGroup", "Add Sector Group"){};
        add(showSectorGroup);
        
        
        
        // read the list of roles from Related Organizations page, and create a
        // unique Set with the roles chosen
        rolesList = new AmpRelatedRolesModel(
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
    @Override
    protected void onConfigure() {
        // TODO Auto-generated method stub
        super.onAfterRender();
        List<String>orgRole=new ArrayList<String>();

        //here we are going to configure the list of available orgroles
        
        if(FMUtil.isFmVisible(showDonorOrganization)){
            orgRole.add(Constants.FUNDING_AGENCY);
        }
        if(FMUtil.isFmVisible(showResponsibleOrganization)){
            orgRole.add(Constants.RESPONSIBLE_ORGANISATION);
        }
        if(FMUtil.isFmVisible(showExecutingAgency)){
            orgRole.add(Constants.EXECUTING_AGENCY);
        }
        if(FMUtil.isFmVisible(showImplementingAgency)){
            orgRole.add(Constants.IMPLEMENTING_AGENCY);
        }               
        if(FMUtil.isFmVisible(showBeneficiaryAgency)){
            orgRole.add(Constants.BENEFICIARY_AGENCY);
        }
        if(FMUtil.isFmVisible(showContractingAgency)){
            orgRole.add(Constants.CONTRACTING_AGENCY);
        }
        if(FMUtil.isFmVisible(showRegionalGroup)){
            orgRole.add(Constants.REGIONAL_GROUP);
        }
        if(FMUtil.isFmVisible(showSectorGroup)){
            orgRole.add(Constants.SECTOR_GROUP);
        }                                       
        ((AmpRelatedRolesModel)rolesList).setRoleFilter(orgRole.toArray(new String[orgRole.size()]));
        
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
