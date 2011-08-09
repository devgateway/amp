/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpFormSectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.helper.OnepagerSection;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import com.rc.retroweaver.runtime.Arrays;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Main component hub for all activity form subcomponents.
 * This is {@link AmpFeaturePanel}, as such, it supports
 * {@link OnePagerUtil#cascadeFmEnabled(AjaxRequestTarget, boolean, org.apache.wicket.Component)}
 * and
 * {@link OnePagerUtil#cascadeFmVisible(AjaxRequestTarget, boolean, org.apache.wicket.Component)}
 * @author mpostelnicu@dgateway.org
 * @since Jun 7, 2011
 */
public class AmpActivityFormFeature extends AmpFeaturePanel<AmpActivityVersion> {
	protected Form<AmpActivityVersion> activityForm;
	private AmpIdentificationFormSectionFeature identificationFeature;
	private AmpInternalIdsFormSectionFeature internalIdsFeature;
	private AmpPlanningFormSectionFeature planningFeature;
	private AmpRegionalFundingFormSectionFeature regionalFundingFeature;
	private AmpLocationFormSectionFeature locationFeature;
	private AmpProgramFormSectionFeature programFeature;
	private AmpCrossCuttingIssuesFormSectionFeature crossCuttingIssues;
	private AmpSectorsFormSectionFeature sectorsFeature;
	private AmpDonorFundingFormSectionFeature donorFundingFeature;
	private AmpRelatedOrganizationsFormSectionFeature relatedOrganizations;
	private AmpComponentsFormSectionFeature components;
	private AmpStructuresFormSectionFeature structures;
	private AmpIssuesFormSectionFeature issues;
	private AmpRegionalObservationsFormSectionFeature regionalObs;
	private AmpContactsFormSectionFeature contacts;
	private AmpContractingFormSectionFeature contracts;
	private AmpMEFormSectionFeature me;
	private AmpPIFormSectionFeature pi;
	private AmpResourcesFormSectionFeature resources;
	
	static OnepagerSection[] test = {
	new OnepagerSection("Identification", "org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature", 1, false),
	new OnepagerSection("Activity Internal IDs", "org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature", 2, false),
	new OnepagerSection("Planning", "org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature", 3, false),
	new OnepagerSection("Location", "org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature", 4, false, true, "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature"),
	new OnepagerSection("Program", "org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature", 5, false),
	new OnepagerSection("Cross Cutting Issues", "org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature", 6, false),
	new OnepagerSection("Sectors", "org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature", 7, false),
	new OnepagerSection("Donor Funding", "org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature", 8, false),
	new OnepagerSection("Regional Funding", "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature", 9, false),
	new OnepagerSection("Related Organizations", "org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature", 10, false),
	new OnepagerSection("Components", "org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature", 11, false),
	new OnepagerSection("Structures", "org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature", 12, false),
	new OnepagerSection("Issues Section", "org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature", 13, false),
	new OnepagerSection("Regional Observations", "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature", 14, false),
	new OnepagerSection("Contacts", "org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature", 15, false),
    new OnepagerSection("Contracts", "org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature", 16, false),
	new OnepagerSection("M&E", "org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature", 17, false),
	new OnepagerSection("Paris Indicators", "org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature", 18, false),
	new OnepagerSection("Related Documents", "org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature", 19, false)
	};
	public static final LinkedList<OnepagerSection> flist = new LinkedList<OnepagerSection>(Arrays.asList(test));
	
	public static OnepagerSection findByName(String name){
		Iterator<OnepagerSection> it = flist.iterator();
		while (it.hasNext()) {
			OnepagerSection os = (OnepagerSection) it.next();
			if (os.getClassName().compareTo(name) == 0)
				return os;
		}
		return null;
	}
	
	public static OnepagerSection findByPosition(int pos){
		Iterator<OnepagerSection> it = flist.iterator();
		while (it.hasNext()) {
			OnepagerSection os = (OnepagerSection) it.next();
			if (os.getPosition() == pos)
				return os;
		}
		return null;
	}
	
	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param newActivity 
	 * @param hideLabel
	 * @throws Exception 
	 */
	public AmpActivityFormFeature(String id, final IModel<AmpActivityVersion> am,
			String fmName, final boolean newActivity) throws Exception {
		super(id, am, fmName, true);
		
		activityForm=new Form<AmpActivityVersion>("activityForm") ;
		activityForm.setOutputMarkupId(true);
		
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		
        //int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        //feedbackPanel.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

		activityForm.add(feedbackPanel);
		add(activityForm);
		
		//add ajax submit button
		AmpButtonField saveAndSubmit = new AmpButtonField("saveAndSubmit","Save and Submit", AmpFMTypes.FEATURE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				saveMethod(target, am, feedbackPanel, false);
			}
		};
		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		saveAndSubmit.getButton().add(new AttributePrepender("onclick", new Model("for (instance in CKEDITOR.instances) CKEDITOR.instances[instance].updateElement(); "), ""));
		activityForm.add(saveAndSubmit);

		AmpButtonField saveAsDraft = new AmpButtonField("saveAsDraft", "Save as Draft", AmpFMTypes.FEATURE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				saveMethod(target, am, feedbackPanel, true);
			}
		};
		saveAsDraft.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		activityForm.add(saveAsDraft);

		AmpButtonField logframe = new AmpButtonField("logframe", "Logframe", AmpFMTypes.FEATURE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		if (am.getObject().getAmpActivityId() == null)
			logframe.setEnabled(false);
		else{
			logframe.add(new SimpleAttributeModifier("onclick", "previewLogframe(" + am.getObject().getAmpActivityId() + ");"));
			logframe.setEnabled(true);
		}
		activityForm.add(logframe);
		
		AmpButtonField preview = new AmpButtonField("preview", "Preview", AmpFMTypes.FEATURE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.appendJavascript("window.location.replace(\"/aim/viewActivityPreview.do~pageId=2~activityId=" + am.getObject().getAmpActivityId() + "~isPreview=1\");");
			}
		};
		preview.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		if (am.getObject().getAmpActivityId() == null)
			preview.setEnabled(false);
		
		activityForm.add(preview);
		
		
		initializeFormComponents(am);
	}
	
	protected void saveMethod(AjaxRequestTarget target,
			IModel<AmpActivityVersion> am, FeedbackPanel feedbackPanel,
			boolean draft) {
		Long oldId = am.getObject().getAmpActivityId();
		
		ActivityUtil.saveActivity((AmpActivityModel) am, draft);

		info("Activity saved successfully");
		//if (newActivity){
			Long actId = am.getObject().getAmpActivityId();//getAmpActivityGroup().getAmpActivityGroupId();
			String replaceStr;
			if (oldId == null)
				replaceStr = "new";
			else
				replaceStr = String.valueOf(oldId);
			target.appendJavascript("window.location.replace(window.location.href.replace(\"" + replaceStr + "\" , \"" + actId + "\"));");
		//}
		target.addComponent(feedbackPanel);
	}

	public void initializeFormComponents(final IModel<AmpActivityVersion> am) throws Exception {

		Collections.sort(flist, new Comparator<OnepagerSection>() {
			@Override
			public int compare(OnepagerSection o1, OnepagerSection o2) {
				return o1.getPosition() - o2.getPosition();
			}
		});
		AbstractReadOnlyModel<List<AmpComponentPanel>> listModel = new AbstractReadOnlyModel<List<AmpComponentPanel>>() {
			private List<AmpComponentPanel> list = null;
			private AmpComponentPanel initObject(OnepagerSection os, LinkedList<OnepagerSection> features, HashMap<String, AmpComponentPanel> temp){
				AmpComponentPanel dep = null;
				if (os.isDependent()){
					Iterator<OnepagerSection> it = features.iterator();
					OnepagerSection depOs = null;
					while (it.hasNext()) {
						OnepagerSection tmpos = (OnepagerSection) it
								.next();
						if (tmpos.getClassName().compareTo(os.getDependentClassName()) == 0){
							depOs = tmpos;
							break;
						}
					}
					dep = initObject(depOs, features, temp);
				}
				
				Class clazz = null;
				try {
					clazz = Class.forName(os.getClassName());
				} catch (ClassNotFoundException e) {
					logger.error("Can't find class for section:" + os.getName(), e);
				}
				Constructor constructor = null;
				try {
					if (os.isDependent())
						constructor = clazz.getConstructor(String.class, String.class, IModel.class, AmpComponentPanel.class);
					else
						constructor = clazz.getConstructor(String.class, String.class, IModel.class);
					
					AmpComponentPanel feature = null;
					if (os.isDependent())
						feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am, dep);
					else
						feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am);
					
					if (AmpFormSectionFeaturePanel.class.isAssignableFrom(feature.getClass())){
						AmpFormSectionFeaturePanel fsfp = (AmpFormSectionFeaturePanel) feature;
						fsfp.setFolded(os.isFolded());
					}
					
					temp.put(os.getClassName(), feature);
					return feature;
				} catch (Exception e) {
					logger.error("Can't init constructor for section:" + os.getName(), e);
					return null;
				}
				
			}
			
			public List<AmpComponentPanel> initObjects(){
				Iterator<OnepagerSection> it = flist.iterator();
				LinkedList<AmpComponentPanel> ret = new LinkedList<AmpComponentPanel>();
				HashMap<String, AmpComponentPanel> temp = new HashMap<String, AmpComponentPanel>();
				while (it.hasNext()) {
					OnepagerSection os = (OnepagerSection) it
							.next();
					AmpComponentPanel fet = initObject(os, flist, temp);
					ret.add(fet);
				}
				return ret;
			}
			
			@Override
			public List<AmpComponentPanel> getObject() {
				if (list == null)
					list = initObjects();
				
				return list;
			}
		};

		ListView<AmpComponentPanel> list = new ListView<AmpComponentPanel>("featureList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				item.add(item.getModelObject());
			}
		};
		list.setReuseItems(true);
		activityForm.add(list);

	}
	
	public void oldInitializeFormComponents(IModel<AmpActivityVersion> am) throws Exception {
		identificationFeature = new AmpIdentificationFormSectionFeature(
				"identification", "Identification", am);
		activityForm.add(identificationFeature);

		internalIdsFeature = new AmpInternalIdsFormSectionFeature(
				"internalIds", "Activity Internal IDs", am);
		activityForm.add(internalIdsFeature);

		planningFeature = new AmpPlanningFormSectionFeature(
				"planning", "Planning", am);
		activityForm.add(planningFeature);
		
		regionalFundingFeature = new AmpRegionalFundingFormSectionFeature(
				"regionalFunding", "Regional Funding", am);
		activityForm.add(regionalFundingFeature);
		
		locationFeature = new AmpLocationFormSectionFeature(
				"location", "Location", am,regionalFundingFeature);
		activityForm.add(locationFeature);

		programFeature = new AmpProgramFormSectionFeature(
				"program", "Program", am);
		activityForm.add(programFeature);

		crossCuttingIssues = new AmpCrossCuttingIssuesFormSectionFeature(
				"crossCuttingIssues", "Cross Cutting Issues", am);
		activityForm.add(crossCuttingIssues);

		sectorsFeature = new AmpSectorsFormSectionFeature(
				"sectors", "Sectors", am);
		activityForm.add(sectorsFeature);

		donorFundingFeature = new AmpDonorFundingFormSectionFeature(
				"donorFunding", "Donor Funding", am);
		activityForm.add(donorFundingFeature);
        
		relatedOrganizations = new AmpRelatedOrganizationsFormSectionFeature(
				"relatedOrganizations", "Related Organizations", am);
		activityForm.add(relatedOrganizations);
		
		components = new AmpComponentsFormSectionFeature("components", "Components", am);
		activityForm.add(components);

		structures = new AmpStructuresFormSectionFeature("structures", "Structures", am);
		activityForm.add(structures);
		
		issues = new AmpIssuesFormSectionFeature("issues", "Issues Section", am);
		activityForm.add(issues);
		
		regionalObs = new AmpRegionalObservationsFormSectionFeature("regionalObs", "Regional Observations", am);
		activityForm.add(regionalObs);

		contacts = new AmpContactsFormSectionFeature("contacts", "Contacts", am);
		activityForm.add(contacts);
		
		contracts = new AmpContractingFormSectionFeature("contracts", "Contracts", am);
		activityForm.add(contracts);
		
		me = new AmpMEFormSectionFeature("me", "M&E", am);
		activityForm.add(me);

		pi = new AmpPIFormSectionFeature("pi", "Paris Indicators", am);
		activityForm.add(pi);
		
		resources = new AmpResourcesFormSectionFeature("resources", "Related Documents", am);
		activityForm.add(resources);

	}

}
