/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.ErrorLevelsFeedbackMessageFilter;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpSemanticValidatorField;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.validators.AmpSemanticValidator;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;

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
	
	private static final long serialVersionUID = 1L;
	protected Form<AmpActivityVersion> activityForm;
	private static final Integer GO_TO_DESKTOP=1;
	private static final Integer STAY_ON_PAGE=2;
	public Form<AmpActivityVersion> getActivityForm() {
		return activityForm;
	}

	public void setActivityForm(Form<AmpActivityVersion> activityForm) {
		this.activityForm = activityForm;
	}

	public ListView<AmpComponentPanel> getFeatureList() {
		return featureList;
	}
	private Integer redirected = GO_TO_DESKTOP;
	
	
	/**
	 * Toggles the validation of semantic validators. 
	 * @param enabled whether these validators are enabled
	 * @param form the form to set the validators
	 * @param target 
	 * @see AmpSemanticValidatorField
	 * @see AmpSemanticValidator
	 */
	public void toggleSemanticValidation(final boolean enabled, Form<?> form,
			final AjaxRequestTarget target) {
		//Force preupdate of the percentage fields so that sum validators can evaluate
		form.visitChildren(AmpPercentageTextField.class,
				new IVisitor<AmpPercentageTextField, Void>() {

					@Override
					public void component(AmpPercentageTextField ifs,
							IVisit<Void> visit) {
						TextField<Double> pf = ifs.getTextContainer();
						String js = String.format("$('#%s').blur();",
								pf.getMarkupId());
						target.appendJavaScript(js);
						//target.add(ifs);
					}
				});
		
		// visit all the semantic validator fields and enable/disable them
		form.visitChildren(AmpSemanticValidatorField.class,
				new IVisitor<AmpSemanticValidatorField<?>, Void>() {

					@Override
					public void component(AmpSemanticValidatorField<?> ifs,
							IVisit<Void> visit) {
						ifs.getSemanticValidator().setEnabled(enabled);
						if (ifs.isVisibleInHierarchy())
							target.add(ifs);
						visit.dontGoDeeper();
					}
				});

		// put status to not required
		form.visitChildren(AmpIdentificationFormSectionFeature.class,
				new IVisitor<AmpIdentificationFormSectionFeature, Void>() {
					@Override
					public void component(
							AmpIdentificationFormSectionFeature ifs,
							IVisit<Void> visit) {
						AbstractChoice<?, AmpCategoryValue> statusField = ifs
								.getStatus().getChoiceContainer();
						String js = String.format("$('#%s').blur();",
								statusField.getMarkupId());
						statusField.setRequired(enabled);
						target.appendJavaScript(js);
						target.add(ifs.getStatus());
						visit.stop();
					}
				});
		
	}

	private ListView<AmpComponentPanel> featureList;

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param newActivity 
	 * @param listModel 
	 * @param hideLabel
	 * @throws Exception 
	 */
	public AmpActivityFormFeature(String id, final IModel<AmpActivityVersion> am,
			String fmName, final boolean newActivity, AbstractReadOnlyModel<List<AmpComponentPanel>> listModel) throws Exception {
		super(id, am, fmName, true);
		
		this.enabledFmButton.setVisible(false);
		this.visibleFmButton.setVisible(false);
		this.ignoreFmVisibility = true;
		this.ignoreFmButtonsVisibility = true;
		this.setVisible(true);
		
		activityForm=new Form<AmpActivityVersion>("activityForm") { 
			@Override
			protected void onError() {
				// TODO Auto-generated method stub
				super.onError();
			}
		};
		activityForm.setOutputMarkupId(true);
		
		String actNameStr = am.getObject().getName();
        if (actNameStr != null && !actNameStr.trim().isEmpty()) {
            actNameStr = "(" + actNameStr + ")";
        }
        Label activityName = new Label("activityName", actNameStr);
        add(activityName);
		
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		feedbackPanel.setOutputMarkupId(true);
		
		
		//do not show errors in this feedbacklabel (they will be shown for each component)
        int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        feedbackPanel.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

		activityForm.add(feedbackPanel);
		add(activityForm);
		
		//add ajax submit button
		AmpButtonField saveAndSubmit = new AmpButtonField("saveAndSubmit","Save and Submit", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(final AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				toggleSemanticValidation(true, form,target);
				// process the form for this request
				form.process(this.getButton());
				
				if(!form.hasError()) 
					saveMethod(target, am, feedbackPanel, false);
				else
					onError(target, form);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				formSubmitErrorHandle(form, target, feedbackPanel);
			}
			
		};
		AttributePrepender updateEditors = new AttributePrepender("onclick", new Model<String>("window.onbeforeunload = null; for (instance in CKEDITOR.instances) CKEDITOR.instances[instance].updateElement(); "), "");
		
		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model<String>("sideMenuButtons")));
		saveAndSubmit.getButton().add(updateEditors);
		saveAndSubmit.getButton().setDefaultFormProcessing(false);
		activityForm.add(saveAndSubmit);
		
		AmpButtonField saveAsDraft = new AmpButtonField("saveAsDraft", "Save as Draft", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		saveAsDraft.getButton().add(new AttributeModifier("onclick", "showDraftPanel();"));
		saveAsDraft.setVisible(true);
		saveAsDraft.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		saveAsDraft.add(new Behavior(){
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.renderJavaScriptReference(new PackageResourceReference(AmpActivityFormFeature.class, "draftSaveNavigationPanel.js"));
			}
		});
		activityForm.add(saveAsDraft);
		final RadioGroup<Integer> myDraftOpts = new RadioGroup<Integer>("draftRedirectedGroup", new Model<Integer>(GO_TO_DESKTOP));
		Radio<Integer> radioDesktop=new Radio<Integer>("draftRedirectedDesktop", new Model<Integer>(GO_TO_DESKTOP));
		myDraftOpts.setOutputMarkupId(true);
		myDraftOpts.setRenderBodyOnly(false);
		radioDesktop.add(new AjaxEventBehavior("onclick") {
			private static final long serialVersionUID = 1L;

			protected void onEvent(final AjaxRequestTarget target) {
				myDraftOpts.setModelObject(GO_TO_DESKTOP);
				target.add(myDraftOpts);
			}
		});
		myDraftOpts.add(radioDesktop);
		Radio<Integer> radioStay=new Radio<Integer>("draftStayOnPage", new Model<Integer>(STAY_ON_PAGE));
		radioStay.add(new AjaxEventBehavior("onclick") {
			private static final long serialVersionUID = 1L;
			
			protected void onEvent(final AjaxRequestTarget target) {
				myDraftOpts.setModelObject(STAY_ON_PAGE);
				target.add(myDraftOpts);
			}
		});
		myDraftOpts.add(radioStay);
		activityForm.add(myDraftOpts);
		

		AmpButtonField saveAsDraftAction = new AmpButtonField("saveAsDraftAction", "Save as Draft", AmpFMTypes.MODULE, true) {
			TextField<String> titleField=null;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.appendJavaScript("hideDraftPanel();");
				am.setObject(am.getObject());
				
				redirected=myDraftOpts.getModelObject();
				toggleSemanticValidation(false, form,target);


				// process the form for this request
				form.process(this.getButton());
				//only in the eventuality that the title field is valid (is not empty) we proceed with the real save!
				if(!form.hasError())  
					saveMethod(target, am, feedbackPanel, true);
				else
					onError(target, form);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				formSubmitErrorHandle(form, target, feedbackPanel); 
			}
		};
		saveAsDraftAction.getButton().setDefaultFormProcessing(false); //disable global validation of the form
		saveAsDraftAction.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		saveAsDraftAction.getButton().add(new AttributePrepender("onclick", new Model("this.disabled='disabled';"), ""));
		saveAsDraftAction.getButton().add(updateEditors);
		activityForm.add(saveAsDraftAction);
		AmpButtonField cancelSaveAsDraft = new AmpButtonField("saveAsDraftCanceld", "Cancel", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		cancelSaveAsDraft.getButton().add(new AttributeModifier("onclick", "hideDraftPanel();"));
		cancelSaveAsDraft.setVisible(true);
		cancelSaveAsDraft.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		activityForm.add(cancelSaveAsDraft);
		
		/*
		 * 
		AmpButtonField logframe = new AmpButtonField("logframe", "Logframe", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		if (am.getObject().getAmpActivityId() == null)
			logframe.setVisible(false);
		else{
			logframe.getButton().add(new AttributeModifier("onclick", "previewLogframe(" + am.getObject().getAmpActivityId() + ");"));
			logframe.setVisible(true);
		}
		logframe.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		activityForm.add(logframe);
		 */
		
		AmpButtonField preview = new AmpButtonField("preview", "Preview", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (am.getObject().getAmpActivityId() == null)
					target.appendJavaScript("alert('" + TranslatorUtil.getTranslatedText("You need to save this activity before being able to preview it!") + "');");
				else
					target.appendJavaScript("window.location.replace(\"/aim/viewActivityPreview.do~pageId=2~activityId=" + am.getObject().getAmpActivityId() + "~isPreview=1\");");
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedbackPanel);
			}
		};
		preview.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		if (am.getObject().getAmpActivityId() == null)
			preview.setVisible(false);
		
		activityForm.add(preview);
		
		featureList = new ListView<AmpComponentPanel>("featureList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null)
					item.add(item.getModelObject());
				else{
					Label tmp = new Label("featureItem", "ERROR: Section failed to load!");
					tmp.add(new AttributeModifier("style", "font-size: medium; font-style: bold; color: red; margin: 15px;"));
					item.add(tmp);
				}
				
				String activityFormOnePager = FeaturesUtil.getGlobalSettingValue(
						GlobalSettingsConstants.ACTIVITY_FORM_ONE_PAGER);
				if ("false".equals(activityFormOnePager)){
					if (item.getIndex() > 0){
						item.add(new AttributeModifier("style", "display: none;"));
					}
				}
					
			}
		};
		featureList.setReuseItems(true);
		activityForm.add(featureList);
		
		quickMenu(am, listModel);
	}
	
	protected void formSubmitErrorHandle(Form<?> form, final AjaxRequestTarget target, FeedbackPanel feedbackPanel) {
		// visit form children and add to the ajax request the invalid ones
		form.visitChildren(FormComponent.class,
				new IVisitor<FormComponent, Void>() {
					@Override
					public void component(FormComponent component,
							IVisit<Void> visit) {
						if (!component.isValid()) {
							target.appendJavaScript("$('#"+ component.getMarkupId() +"').parents().show();");
							target.appendJavaScript("$(window).scrollTop($('#"+component.getParent().getMarkupId()+"').position().top)");
							target.add(component);
							
							//some of the fields that need to show errors are HiddenFieldS. These are cumulative error fields, that show error for groups of other fields
							//like for example a list of sectors with percentages
							//when these AmpCollectionValidatorFieldS are detected, their validation is revisited
							if (component instanceof HiddenField) {									
								if(component.getParent() instanceof AmpCollectionValidatorField<?, ?>) 
									((AmpCollectionValidatorField)component.getParent()).reloadValidationField(target);									
							} else {
								target.focusComponent(component);
								String js = null;
								
								//we simulate onClick over AmpGroupFieldS because radiochoices are treated differently they can't receive onChange.
								//For the rest of the components we use onChange
								if(component instanceof RadioChoice<?> || component instanceof CheckBoxMultipleChoice
										|| component  instanceof RadioGroup<?> || component instanceof CheckGroup) 
									js=String.format("$('#%s').click();",component.getMarkupId());										
								else 											
									js=String.format("$('#%s').change();",component.getMarkupId());
								
								target.appendJavaScript(js);
								target.add(component);
							}
						}
					}
				});
		target.add(feedbackPanel);
	}

	protected void saveMethod(AjaxRequestTarget target,
			IModel<AmpActivityVersion> am, FeedbackPanel feedbackPanel,
			boolean draft) {
		
		OnePager op = this.findParent(OnePager.class);
		//disable lock refresher
		op.getEditLockRefresher().setEnabled(false);
		
		AmpActivityModel a = (AmpActivityModel) am;
		AmpActivityVersion activity=am.getObject();
		Long oldId = activity.getAmpActivityId();
		Boolean wasDraft=activity.getDraft();
		AmpTeamMember modifiedBy = activity.getModifiedBy();
		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
		AmpTeamMember ampCurrentMember = wicketSession.getAmpCurrentMember();


		//Before starting to save check lock
		if (oldId != null && !ActivityGatekeeper.verifyLock(String.valueOf(a.getId()), a.getEditingKey())){
			//Someone else has grabbed the lock ... maybe connection slow and lock refresh timed out
			getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(ActivityGatekeeper.buildRedirectLink(String.valueOf(a.getId()))));
			return;
		}
		
		ActivityUtil.saveActivity((AmpActivityModel) am, draft);

		info("Activity saved successfully");

		/*
		 * if activity created or created as draft 
		 * and then saved the message should be sent to the list
		 */
		AmpActivityVersion newActivity=am.getObject();
		if ((oldId == null || (wasDraft != null && wasDraft))
				&& newActivity.getDraft() != null && !newActivity.getDraft()) {
			new ActivitySaveTrigger(newActivity);
		}
    	String additionalDetails="approved";
		//if validation is off in team setup no messages should be generated
		String validation = DbUtil.getValidationFromTeamAppSettings(ampCurrentMember.getAmpTeam().getAmpTeamId());
		
		if (activity.getDraft() != null&& !activity.getDraft()&&!("validationOff".equals(validation))) {
        	String approvalStatus = newActivity.getApprovalStatus();
			if(approvalStatus != null && (approvalStatus.equals(Constants.APPROVED_STATUS)||approvalStatus.equals(Constants.STARTED_APPROVED_STATUS))){
        		if(modifiedBy!=null){
        			AmpTeamMemberRoles role=modifiedBy.getAmpMemberRole();
            		boolean isTeamHead=false;
            		if(role.getTeamHead()!=null&&role.getTeamHead()){
            			isTeamHead=true;
            		}
            		if(!role.isApprover()){
            			if(oldId==null||("allEdits".equals(validation))){
            				new ApprovedActivityTrigger(newActivity,modifiedBy); //if TL or approver created activity, then no Trigger is needed
            			}
            		}
        		}
        		
        	}else{
        		if("allEdits".equals(validation)||oldId==null){
        			new NotApprovedActivityTrigger(newActivity);
            		additionalDetails="pending approval";
        		}
        	}
        }
		else{
			if (newActivity.getDraft() != null&& newActivity.getDraft()){
				additionalDetails="draft";
			}
		}
		
		HttpServletRequest hsRequest = (HttpServletRequest) getRequest().getContainerRequest();

		if (oldId != null) {
			List<String> details=new ArrayList<String>();
			details.add(additionalDetails);
			AuditLoggerUtil.logActivityUpdate(hsRequest, newActivity,details);
		} else {
			try {
				AuditLoggerUtil.logObject(hsRequest, newActivity, "add",additionalDetails);
			} catch (DgException e) {
				e.printStackTrace();
			}
		}

		//if (newActivity){
			Long actId = am.getObject().getAmpActivityId();//getAmpActivityGroup().getAmpActivityGroupId();
			String replaceStr;
			if (oldId == null)
				replaceStr = "new";
			else
				replaceStr = String.valueOf(oldId);
			if(redirected.equals(STAY_ON_PAGE)){
				target.appendJavaScript("var newLoc=window.location.href.replace(\"" + replaceStr + "\" , \"" + actId + "\");newLoc=newLoc.substr(0,newLoc.lastIndexOf('?'));window.location.replace(newLoc);");
			}
			else{
				target.appendJavaScript("window.location.replace('/');");
			}
		//}
		target.add(feedbackPanel);
	}

	private void quickMenu(IModel<AmpActivityVersion> am, AbstractReadOnlyModel<List<AmpComponentPanel>> listModel) {
		ListView<AmpComponentPanel> list = new ListView<AmpComponentPanel>("quickList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null){
					Label label = new TrnLabel("quickName", item.getModelObject().getFMName());
					String itemId = Hex.encodeHexString(item.getModelObject().getFMName().getBytes());
					label.add(new AttributeModifier("id", "qItem"+itemId));
					label.add(new AttributeModifier("onclick", "showSection('"+itemId +"'); return false;"));
					if (!item.getModelObject().isVisible())
						item.setVisible(false);
					item.add(label);
				}
				else{
					WebMarkupContainer tmp = new WebMarkupContainer("quickName");
					tmp.setVisible(false);
					item.setVisible(false);
					item.add(tmp);
					//item.add(new SimpleAttributeModifier("style", "display: none"));
				}
			}
		};
		list.setReuseItems(false);
		activityForm.add(list);
	}

}
