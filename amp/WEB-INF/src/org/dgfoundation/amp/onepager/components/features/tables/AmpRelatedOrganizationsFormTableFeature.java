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

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.util.DbUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 26, 2010
 */
public class AmpRelatedOrganizationsFormTableFeature extends AmpFormTableFeaturePanel<AmpActivityVersion,AmpOrgRole> {

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpRelatedOrganizationsFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am, final String roleName) throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpOrgRole>> setModel=new PropertyModel<Set<AmpOrgRole>>(am,"orgrole");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet());
		
		
		final AmpRole specificRole = DbUtil.getAmpRole(roleName);

		IModel<List<AmpOrgRole>> listModel = new AbstractReadOnlyModel<List<AmpOrgRole>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpOrgRole> getObject() {
				ArrayList<AmpOrgRole> ret = new ArrayList<AmpOrgRole>();
				if (setModel == null)
					return ret;
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

		
		final AmpUniqueCollectionValidatorField<AmpOrgRole> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpOrgRole>(
				"uniqueOrgsValidator", listModel, "Unique Orgs Validator") {
			@Override
			public Object getIdentifier(AmpOrgRole t) {
				return t.getOrganisation().getName();
			}
		};
		add(uniqueCollectionValidationField);
		
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		final AmpPercentageCollectionValidatorField<AmpOrgRole> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpOrgRole>(
				"relOrgPercentageTotal", listModel, "relOrgPercentageTotal") {
			@Override
			public Number getPercentage(AmpOrgRole item) {
				return item.getPercentage();
			}
		};
		percentageValidationField.setIndicatorAppender(iValidator);
		add(percentageValidationField);


		list = new ListView<AmpOrgRole>("list", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpOrgRole> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new TextField<String>(
						"departmentDivision",
						new PropertyModel<String>(item.getModel(), "additionalInfo")));
				
				AmpOrganisation o= item.getModelObject().getOrganisation();
				item.add(new Label("name", item.getModelObject().getOrganisation().getAcronymAndName()));	
				
				PropertyModel<Double> percModel = new PropertyModel<Double>(item.getModel(), "percentage");
				AmpPercentageTextField percentageField = new AmpPercentageTextField("percentage", percModel, "percentage",percentageValidationField);
				item.add(percentageField);
				
				AmpDeleteLinkField delRelOrg = new AmpDeleteLinkField(
						"delRelOrg", "Delete Related Organisation") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						uniqueCollectionValidationField.reloadValidationField(target);
						list.removeAll();
						target.addComponent(listParent);
					}
				};
				item.add(delRelOrg);
				
			}
		};
		list.setReuseItems(true);
		add(list);
		
		
		add(new AmpDividePercentageField<AmpOrgRole>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, list){
			@Override
			public void setPercentage(AmpOrgRole loc, int val) {
				loc.setPercentage((double) val);
			}

			@Override
			public int getPercentage(AmpOrgRole loc) {
				return (int)((double)loc.getPercentage());
			}

			@Override
			public boolean itemInCollection(AmpOrgRole item) {
				return true; //all items displayed in the same list
			}

		});

		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("search","Search Organizations",AmpOrganisationSearchModel.class) {
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
				if(list.size()>0)
					ampOrgRole.setPercentage(0d);
				else 
					ampOrgRole.setPercentage(100d);
				setModel.getObject().add(ampOrgRole);
				uniqueCollectionValidationField.reloadValidationField(target);
				list.removeAll();
				target.addComponent(list.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};
		
		add(searchOrgs);
	}

}
