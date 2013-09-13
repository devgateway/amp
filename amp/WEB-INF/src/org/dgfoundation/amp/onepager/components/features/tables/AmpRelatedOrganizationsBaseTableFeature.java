/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpMaxSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.*;

import org.digijava.module.aim.util.DbUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 26, 2010
 */
public class AmpRelatedOrganizationsBaseTableFeature extends AmpFormTableFeaturePanel<AmpActivityVersion, AmpOrgRole> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final AmpComponentPanel addFundingItemAutomatically;
    protected AmpUniqueCollectionValidatorField<AmpOrgRole> uniqueCollectionValidationField;
	protected AmpPercentageCollectionValidatorField<AmpOrgRole> percentageValidationField;
    protected AmpMinSizeCollectionValidationField<AmpOrgRole> minSizeCollectionValidationField;
    protected AmpMaxSizeCollectionValidationField<AmpOrgRole> maxSizeCollectionValidationField;
	protected final IModel<ListView<AmpOrgRole>> list = new Model<ListView<AmpOrgRole>>();
	protected IModel<List<AmpOrgRole>> listModel;
	protected IModel<Set<AmpOrgRole>> setModel;
	private TransparentWebMarkupContainer updateColSpan1;
	private TransparentWebMarkupContainer updateColSpan2;
	private AmpDonorFundingFormSectionFeature donorFundingSection;
    private AmpSearchOrganizationComponent<String> searchOrganization;
	
	
	/**
	 * Override to notify of newly added roles, if you need to refresh/change other sections of the form
	 * @param target 
	 * @param ampOrgRole
	 */
	public void roleAdded(AjaxRequestTarget target, AmpOrgRole ampOrgRole) {
	    target.add(donorFundingSection);
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(donorFundingSection));
        
        changeSearchVisibility(target);
        addFundingAutomatically(target, ampOrgRole);
	}

    private void addFundingAutomatically(AjaxRequestTarget target, AmpOrgRole ampOrgRole) {
        if (addFundingItemAutomatically.isVisible()){
            donorFundingSection.getOrgRoleSelector().getOrgSelect().getModel().setObject(ampOrgRole.getOrganisation());
            donorFundingSection.getOrgRoleSelector().getRoleSelect().getModel().setObject(ampOrgRole.getRole());
            donorFundingSection.getList().addItem(ampOrgRole.getOrganisation());
            target.add(donorFundingSection);
        }
    }
    private void removeFundingAutomatically(AjaxRequestTarget target, AmpOrgRole ampOrgRole) {
        if (addFundingItemAutomatically.isVisible()){
            Set<AmpFunding> set = donorFundingSection.getFundingModel().getObject();
            Iterator<AmpFunding> it = set.iterator();
            while (it.hasNext()){
                AmpFunding funding = it.next();
                AmpOrganisation fundingOrg = funding.getAmpDonorOrgId();
                if (fundingOrg.getAmpOrgId().equals(ampOrgRole.getOrganisation().getAmpOrgId())){
                    it.remove();
                }
            }
            donorFundingSection.updateFundingGroups(ampOrgRole.getOrganisation(), target);
            target.add(donorFundingSection);
        }
    }

    private void changeSearchVisibility(AjaxRequestTarget target) {
        if (maxSizeCollectionValidationField.isVisible()){
            List<AmpOrgRole> tmpList = listModel.getObject();
            if (tmpList != null && tmpList.size() > 0){
                searchOrganization.setVisibilityAllowed(false);
            }
            else{
                searchOrganization.setVisibilityAllowed(true);
            }
            target.add(searchOrganization);
        }
    }

    /**
	 * Override to notify of newly removed roles, if you need to refresh/change other sections of the form
	 * @param ampOrgRole
	 */
	public void roleRemoved(AjaxRequestTarget target,AmpOrgRole ampOrgRole) {
	    target.add(donorFundingSection);
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(donorFundingSection));
        changeSearchVisibility(target);

        removeFundingAutomatically(target, ampOrgRole);
	}
	
	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	protected AmpRelatedOrganizationsBaseTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am, final String roleName,AmpDonorFundingFormSectionFeature donorFundingSection) throws Exception {
		super(id, am, fmName);
		setModel=new PropertyModel<Set<AmpOrgRole>>(am,"orgrole");
		this.donorFundingSection=donorFundingSection;
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpOrgRole>());
		
		
		final AmpRole specificRole = DbUtil.getAmpRole(roleName);

		listModel = new AbstractReadOnlyModel<List<AmpOrgRole>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpOrgRole> getObject() {
				ArrayList<AmpOrgRole> ret = new ArrayList<AmpOrgRole>();
				Set<AmpOrgRole> allOrgRoles = setModel.getObject();
				Set<AmpOrgRole> specificOrgRoles = new HashSet<AmpOrgRole>();  
				
				if(allOrgRoles != null){
					Iterator<AmpOrgRole> it = allOrgRoles.iterator();
					while (it.hasNext()) {
						AmpOrgRole ampOrgRole = (AmpOrgRole) it.next();
						if (ampOrgRole.getRole().getAmpRoleId().compareTo(specificRole.getAmpRoleId()) == 0)
							specificOrgRoles.add(ampOrgRole);
					}
				}
				
				if (allOrgRoles == null)
					return ret;
				
				ret = new ArrayList<AmpOrgRole>(specificOrgRoles);
				Collections.sort(ret, new Comparator<AmpOrgRole>() {
					@Override
					public int compare(AmpOrgRole o1, AmpOrgRole o2) {
						if (o1 == null || o1.getOrganisation() == null ||o1.getOrganisation().getAcronymAndName() == null)
							return 1;
						if (o2 == null || o2.getOrganisation() == null ||o2.getOrganisation().getAcronymAndName() == null)
							return -1;
						return o1.getOrganisation().getAcronymAndName().compareTo(o2.getOrganisation().getAcronymAndName());
					}
				});
				return ret;
			}
		};
		
		updateColSpan1 = new TransparentWebMarkupContainer("updateColSpan1");
		add(updateColSpan1);
		updateColSpan2 = new TransparentWebMarkupContainer("updateColSpan2");
		add(updateColSpan2);

		
		uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpOrgRole>(
				"uniqueOrgsValidator", listModel, "Unique Orgs Validator") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getIdentifier(AmpOrgRole t) {
				return t.getOrganisation().getName();
			}
		};
		add(uniqueCollectionValidationField);

        minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpOrgRole>("minSizeValidator", listModel, "Required Validator"){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                reqStar.setVisible(isVisible());
            }
        };
        add(minSizeCollectionValidationField);

        maxSizeCollectionValidationField = new AmpMaxSizeCollectionValidationField<AmpOrgRole>("maxSizeValidator", listModel, "Max Size Validator");
        maxSizeCollectionValidationField.setOutputMarkupPlaceholderTag(true);
        add(maxSizeCollectionValidationField);

        addFundingItemAutomatically = new AmpComponentPanel("addFundingItemAutomatically", "Add Funding Item Automatically"){};
        add(addFundingItemAutomatically);


        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		percentageValidationField = new AmpPercentageCollectionValidatorField<AmpOrgRole>(
				"relOrgPercentageTotal", listModel, "relOrgPercentageTotal") {
			private static final long serialVersionUID = 1L;

			@Override
			public Number getPercentage(AmpOrgRole item) {
				return item.getPercentage();
			}
		};
		percentageValidationField.setIndicatorAppender(iValidator);
		add(percentageValidationField);

		add(new AmpDividePercentageField<AmpOrgRole>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, list){
			private static final long serialVersionUID = 1L;

			@Override
			public void setPercentage(AmpOrgRole loc, int val) {
				loc.setPercentage((float) val);
			}

			@Override
			public int getPercentage(AmpOrgRole loc) {
				return (int)((double)loc.getPercentage());
			}

			@Override
			public boolean itemInCollection(AmpOrgRole item) {
				return item.getRole().getAmpRoleId().compareTo(specificRole.getAmpRoleId()) == 0;
			//	return true; //all items displayed in the same list
			}

		});

		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete","Search Organizations",true,AmpOrganisationSearchModel.class) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return DbUtil.filter(choice.getName());
			}
			
			@Override
			protected boolean showAcronyms() {
				return true;
			}
			
			@Override
			protected String getAcronym(AmpOrganisation choice) {
				return choice.getAcronym();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, AmpOrganisation choice) {
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setOrganisation(choice);
				ampOrgRole.setActivity(am.getObject());
				ampOrgRole.setRole(specificRole);
				if(list.getObject().size()>0)
					ampOrgRole.setPercentage(0f);
				else 
					ampOrgRole.setPercentage(100f);
				
				if (setModel.getObject() == null)
					setModel.setObject(new HashSet<AmpOrgRole>());
				
				setModel.getObject().add(ampOrgRole);
				uniqueCollectionValidationField.reloadValidationField(target);
                minSizeCollectionValidationField.reloadValidationField(target);
                maxSizeCollectionValidationField.reloadValidationField(target);

				list.getObject().removeAll();
				target.add(list.getObject().getParent());				
				roleAdded(target,ampOrgRole);
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};

		searchOrganization = new AmpSearchOrganizationComponent<String>("search", new Model<String> (),
				"Search Organizations",   searchOrgs );
		add(searchOrganization);

	}
	
	@Override
	public void setTitleHeaderColSpan(Integer colspan) {
		super.setTitleHeaderColSpan(colspan);
		updateColSpan1.add(new AttributeModifier("colspan", new Model<Integer>(colspan)));
		updateColSpan2.add(new AttributeModifier("colspan", new Model<Integer>(colspan)));
	}

}
