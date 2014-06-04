/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.ValidatorAdapter;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.ErrorLevelsFeedbackMessageFilter;
import org.dgfoundation.amp.onepager.components.features.sections.AmpAidEffectivenessFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityBudgetExtrasPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.components.fields.AmpSemanticValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.models.TranslationDecoratorModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.validators.AmpSemanticValidator;
import org.dgfoundation.amp.onepager.validators.StringRequiredValidator;
import org.dgfoundation.amp.onepager.validators.TranslatableValidators;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.util.AmpMessageUtil;

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

    private static final String DISBURSEMENTS_BIGGER_ERROR =
            TranslatorUtil.getTranslatedText("The sum of disbursements is greater than the sum of commitments");
    private static final String EXPENDITURES_BIGGER_ERROR =
            TranslatorUtil.getTranslatedText("The sum of expenditures is greater than the sum of disbursements");
	

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

		form.visitChildren(AmpIdentificationFormSectionFeature.class,
				new IVisitor<AmpIdentificationFormSectionFeature, Void>() {
					@Override
					public void component(
							AmpIdentificationFormSectionFeature ifs,
							IVisit<Void> visit) {
						toggleFormComponent (enabled,target,ifs,visit);					}
					});
		form.visitChildren(AmpDonorFundingFormSectionFeature.class,
				new IVisitor<AmpDonorFundingFormSectionFeature, Void>() {
					@Override
					public void component(
							AmpDonorFundingFormSectionFeature ifs,
							IVisit<Void> visit) {
						toggleFormComponent (enabled,target,ifs,visit);					}
				});
		visitChildren(AmpActivityBudgetExtrasPanel.class,
				new IVisitor<AmpActivityBudgetExtrasPanel, Void>() {
					@Override
					public void component(
							AmpActivityBudgetExtrasPanel ifs,
							IVisit<Void> visit) {
						toggleFormComponent (enabled,target,ifs,visit);
					}
				});
		visitChildren(AmpAidEffectivenessFormSectionFeature.class,
				new IVisitor<AmpAidEffectivenessFormSectionFeature, Void>() {
					@Override
					public void component(
							AmpAidEffectivenessFormSectionFeature ifs,
							IVisit<Void> visit) {
						toggleFormComponent (enabled,target,ifs,visit);
					}
				});
		visitChildren(AmpProposedProjectCost.class,
				new IVisitor<AmpProposedProjectCost, Void>() {
					@Override
					public void component(
							AmpProposedProjectCost ifs,
							IVisit<Void> visit) {
						toggleFormComponent (enabled,target,ifs,visit);
					}
				});
		
	}
	private void toggleFormComponent (boolean enabled, final AjaxRequestTarget target,
			AmpRequiredComponentContainer ifs, IVisit<Void> visit) {
		List <FormComponent<?>> requiredComponents = ifs.getRequiredFormComponents();
		for (FormComponent<?> component : requiredComponents) {
		String js = String.format("$('#%s').blur();",
				component.getMarkupId());
		component.setRequired(enabled);
		target.appendJavaScript(js);
		target.add(component);
		}
		visit.stop();
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
		//this will be use to decorate all submit buttons
		AttributePrepender isSubmit = new AttributePrepender("data-is_submit", new Model<String>("true"), "");
		
		//do not show errors in this feedbacklabel (they will be shown for each component)
        int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        feedbackPanel.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

		activityForm.add(feedbackPanel);
		add(activityForm);
        final Model<Integer> redirected = new Model<Integer>(GO_TO_DESKTOP){
            @Override
            public void setObject(Integer object) {
                super.setObject(object);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        final WebMarkupContainer warningsWrapper = new WebMarkupContainer("saveWarningsWrapper");
        warningsWrapper.setOutputMarkupId(true);
        add(warningsWrapper);

        final Model<HashMap<String,HashMap<String,String>>> saveWarningsModel =
                new Model<HashMap<String, HashMap<String, String>>>(new HashMap<String, HashMap<String, String>>());
        AbstractReadOnlyModel<List<String>> saveWarningsListModel = new AbstractReadOnlyModel<List<String>>() {
            @Override
            public List<String> getObject() {
                return new ArrayList<String>(saveWarningsModel.getObject().keySet());
            }
        };



        ListView<String> warningsList = new ListView<String>("saveWarningsList", saveWarningsListModel) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String warning = item.getModelObject();
                final HashMap<String, String> warnList = saveWarningsModel.getObject().get(warning);
                item.add(new Label("warnName", warning));

                AbstractReadOnlyModel<List<String>> keyModel = new AbstractReadOnlyModel<List<String>>() {
                    @Override
                    public List<String> getObject() {
                        return new ArrayList<String>(warnList.keySet());
                    }
                };

                ListView<String> list = new ListView<String>("warnList", keyModel) {
                    @Override
                    protected void populateItem(ListItem<String> item) {
                        String org = item.getModelObject();
                        item.add(new Label("org", org));
                        item.add(new Label("value", warnList.get(org)));
                    }
                };
                list.setReuseItems(false);
                item.add(list);
            }
        };
        warningsList.setReuseItems(false);
        warningsWrapper.add(warningsList);

        IndicatingAjaxLink cancelButton = new IndicatingAjaxLink("warnPanelCancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("hideWarningPanel();enableButtons();");
            }
        };
        cancelButton.add(new AttributeModifier("value", TranslatorUtil.getTranslatedText("Cancel")));
        add(cancelButton);


        IndicatingAjaxLink saveButton = new IndicatingAjaxLink("warnPanelSave") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("hideWarningPanel();enableButtons();");
                //TODO: fix draft param
                saveMethod(target, am, feedbackPanel, false, redirected,false);
            }
        };
        saveButton.add(new AttributeModifier("value", TranslatorUtil.getTranslatedText("Save")));
        add(saveButton);

        //add ajax submit button
		final AmpButtonField saveAndSubmit = new AmpButtonField("saveAndSubmit","Save and Submit", AmpFMTypes.MODULE, true) {
			
			
			@Override
			protected void onSubmit(final AjaxRequestTarget target, Form<?> form) {

					processAndUpdateForm(true, am, form, target, this.getButton());
	                if(!form.hasError()){
	                    HashMap<String, String> commitmentErrors = new HashMap<String, String>();
	                    HashMap<String, String> expenditureErrors = new HashMap<String, String>();
	                    boolean amountsOk = verifyAmounts(am, commitmentErrors, expenditureErrors);
	                    if (!amountsOk){
	                        //set warning
	                        HashMap<String, HashMap<String,String>> tmpMap = new HashMap<String, HashMap<String, String>>();
	                        if (!commitmentErrors.isEmpty()) {
	                            tmpMap.put(DISBURSEMENTS_BIGGER_ERROR, commitmentErrors);
	                        }
	                        if (!expenditureErrors.isEmpty()) {
	                            tmpMap.put(EXPENDITURES_BIGGER_ERROR, expenditureErrors);
	                        }
	                        saveWarningsModel.setObject(tmpMap);
	                        //show warning window
	                        target.add(warningsWrapper);
	                        target.appendJavaScript("showWarningPanel();");
	                    }
	                    else
	                        saveMethod(target, am, feedbackPanel, false, redirected,false);
	                }
					else{
						onError(target, form);
					}
					//we only remove disable on buttons tagged as submit ones
				target.appendJavaScript("enableButtons();");
			}
			

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				//if any error happens we enable again all buttons (tagged as submit
				target.appendJavaScript("enableButtons();");
				formSubmitErrorHandle(form, target, feedbackPanel);
			}


			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				generateEnableButtonsOnError(attributes);
			}
		};
		
		
		
		AttributePrepender updateEditors = new AttributePrepender("onclick", new Model<String>("window.onbeforeunload = null;  for (instance in CKEDITOR.instances) CKEDITOR.instances[instance].updateElement();disableButton();"), "");

		saveAndSubmit.getButton().add(isSubmit);
		saveAndSubmit.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons")));
		
		saveAndSubmit.getButton().add(updateEditors);
		saveAndSubmit.getButton().setDefaultFormProcessing(false);
		
		activityForm.add(saveAndSubmit);
		
		AmpAjaxLinkField saveReject=new AmpAjaxLinkField("saveReject", "Reject activity", "Reject activity") {
            @Override
            protected void onClick(AjaxRequestTarget target) {

            }
            @Override
            protected void onBeforeRender() {
            	super.onBeforeRender();
        		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
        		//if the user is aprovver of the workspace
        		//or is the teamlead of the ws
        		//and the activity is not new (make not sense to reject a newly created activity)
        		
        		this.setVisible( !newActivity && (wicketSession.getAmpCurrentMember().getAmpMemberRole().isApprover() 
        				|| wicketSession.getAmpCurrentMember().getAmpTeam().getTeamLead().equals(wicketSession.getAmpCurrentMember())));
            }
		};
		saveReject.getButton().add(isSubmit);
        
		saveReject.getButton().add(new AttributeModifier("onclick", "showRejectActivityPanel();"));
		saveReject.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons rejectButton")));
        activityForm.add(saveReject);
		
        AmpAjaxLinkField saveAsDraft = new AmpAjaxLinkField("saveAsDraft", "Save as Draft", "Save as Draft") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
            	target.appendJavaScript("enableButtons();");
            }
            //agregar el evento que reprende los botones
        };
        
        saveAsDraft.getButton().add(isSubmit);
        
		saveAsDraft.getButton().add(new AttributeModifier("onclick", "showDraftPanel();disableButton();"));
		saveAsDraft.setVisible(false);
		saveAsDraft.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons")));
        activityForm.add(saveAsDraft);
		activityForm.add(new Behavior(){
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AmpActivityFormFeature.class, "saveNavigationPanel.js")));
				response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AmpActivityFormFeature.class, "previewLogframe.js")));
				response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AmpActivityFormFeature.class, "enableDisableButtons.js")));
			}
		});
		

		final RadioGroup<Integer> myDraftOpts = new RadioGroup<Integer>("draftRedirectedGroup", new Model<Integer>(GO_TO_DESKTOP));
		Radio<Integer> radioDesktop=new Radio<Integer>("draftRedirectedDesktop", new Model<Integer>(GO_TO_DESKTOP));
		myDraftOpts.setOutputMarkupId(true);
        myDraftOpts.setRenderBodyOnly(false);
        radioDesktop.add(new AjaxEventBehavior("click") {
			private static final long serialVersionUID = 1L;
			protected void onEvent(final AjaxRequestTarget target) {
				redirected.setObject(GO_TO_DESKTOP);
                myDraftOpts.setModelObject(GO_TO_DESKTOP);
                target.add(myDraftOpts);
			}
		});
		myDraftOpts.add(radioDesktop);
		Radio<Integer> radioStay=new Radio<Integer>("draftStayOnPage", new Model<Integer>(STAY_ON_PAGE));
		radioStay.add(new AjaxEventBehavior("click") {
            private static final long serialVersionUID = 1L;

            protected void onEvent(final AjaxRequestTarget target) {
                redirected.setObject(STAY_ON_PAGE);
                myDraftOpts.setModelObject(STAY_ON_PAGE);
                target.add(myDraftOpts);
            }
        });
		myDraftOpts.add(radioStay);
		activityForm.add(myDraftOpts);

		
        final AmpAjaxLinkField cancelSaveAsDraft = new AmpAjaxLinkField("saveAsDraftCanceld", "Cancel", "Cancel") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
        };
        cancelSaveAsDraft.getButton().add(new AttributeModifier("onclick", "hideDraftPanel();enableButtons();"));
        cancelSaveAsDraft.add(isSubmit);
        cancelSaveAsDraft.setVisible(true);
        cancelSaveAsDraft.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons")));
        cancelSaveAsDraft.setOutputMarkupId(true);
        activityForm.add(cancelSaveAsDraft);

        AmpButtonField saveAsDraftAction = new AmpButtonField("saveAsDraftAction", "Save as Draft", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSubmitSaveAsDraft(am, feedbackPanel, redirected, this,
							target, form,false);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				onErrorSaveAsDraft(feedbackPanel, target, form); 
			}
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				generateEnableButtonsOnError(attributes);
			}
		};
		
	

        String onClickSaveAsDraft = "$(\"#"+ saveAsDraftAction.getButton().getMarkupId() +"\").prop('disabled', true);";
        onClickSaveAsDraft += "$(\"#"+ cancelSaveAsDraft.getButton().getMarkupId() +"\").prop('disabled', true);";

		saveAsDraftAction.getButton().setDefaultFormProcessing(false); //disable global validation of the form
		saveAsDraftAction.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons")));
		saveAsDraftAction.getButton().add(new AttributePrepender("onclick", new Model<String>(onClickSaveAsDraft+" disableButton();"), ""));
		saveAsDraftAction.getButton().add(updateEditors);
		saveAsDraftAction.add(isSubmit);
		activityForm.add(saveAsDraftAction);
		
		//text area for the message
		AmpTextAreaFieldPanel rejectMessage = new AmpTextAreaFieldPanel("rejectMessage", new PropertyModel<String>(am,"rejectMessage"), "Reject Message", false);
		activityForm.add(rejectMessage);
		//buttons for the reject activity panel
		//cancelrejectActivity
		
		AmpAjaxLinkField cancelRejectActivity=new AmpAjaxLinkField("cancelRejectActivity", "Cancel Reject activity", "Cancel") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
            	am.getObject().setRejectMessage(null);
            }
		};
		cancelRejectActivity.getButton().add(isSubmit);
		cancelRejectActivity.getButton().add(new AttributeModifier("onclick", "hideRejectActivityPanel();enableButtons();"));
		cancelRejectActivity.setVisible(true);
		cancelRejectActivity.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons")));
        activityForm.add(cancelRejectActivity);
        
        
        AmpButtonField rejectActivityAction = new AmpButtonField("rejectActivityAction", "Reject Activity", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSubmitSaveAsDraft(am, feedbackPanel, redirected, this,
							target, form,true);
			}


			
			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				onErrorSaveAsDraft(feedbackPanel, target, form); 
			}
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				generateEnableButtonsOnError(attributes);
			}
		};
		
	

//        String onClickSaveAsDraft = "$(\"#"+ saveAsDraftAction.getButton().getMarkupId() +"\").prop('disabled', true);";
//        onClickSaveAsDraft += "$(\"#"+ cancelSaveAsDraft.getButton().getMarkupId() +"\").prop('disabled', true);";

		rejectActivityAction.getButton().setDefaultFormProcessing(false); //disable global validation of the form
		rejectActivityAction.getButton().add(new AttributeModifier("class", new Model<String>("sideMenuButtons")));
		rejectActivityAction.getButton().add(new AttributePrepender("onclick", new Model<String>("disableButton();"), ""));
		rejectActivityAction.getButton().add(updateEditors);
		rejectActivityAction.add(isSubmit);
		activityForm.add(rejectActivityAction);        
        
        
        
        
        
		
		// this div will be "submitted" by the autoSaveTimer
		final WebMarkupContainer autoSaveDiv = new WebMarkupContainer(
				"autoSaveDiv");
		autoSaveDiv.setOutputMarkupId(true);
		// it implements an ajaxformsubmitbehavior, just like an AjaxButton, and
		// hooked on the "click" event
		autoSaveDiv.add(new AjaxFormSubmitBehavior(activityForm, "click") {
			// we do something very similar during Save Draft: disable semantic
			// validation, and process the form
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
                am.setObject(am.getObject());
				toggleSemanticValidation(false, activityForm, target);
				// process the form for this request
				activityForm.process(null);
				// only in the eventuality that the title field is valid (is not
				// empty) we proceed with the real save!
				if (!activityForm.hasError())
					saveMethod(target, am, feedbackPanel, true, redirected,false);
				else {
					formSubmitErrorHandle(activityForm, target, feedbackPanel);
				}
			}

			// we disable the normal form processing, just like the save buttons
			// do
			@Override
			public boolean getDefaultProcessing() {
				return false;
			}
		});
		activityForm.add(autoSaveDiv);

		// this timer will be invoked every X minutes and the onTimer method
		// will fire
		int autoSaveSeconds = Integer.parseInt(FeaturesUtil
		.getGlobalSettingValue(GlobalSettingsConstants.ACTIVITY_AUTO_SAVE_SECONDS));
		
		AbstractAjaxTimerBehavior autoSaveTimer = null;
		if (autoSaveSeconds != 0) {
			autoSaveTimer = new AbstractAjaxTimerBehavior(
					Duration.seconds(autoSaveSeconds)) {
				@Override
				protected void onTimer(AjaxRequestTarget target) {
					// we send a javascript event to the autoSaveDiv
					target.appendJavaScript(String.format("$('#%s').click()",
							autoSaveDiv.getMarkupId()));
					target.add(autoSaveDiv);
				}
			};
			activityForm.add(autoSaveTimer);
		}

		AmpButtonField logframe = new AmpButtonField("logframe", "Logframe", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.appendJavaScript("enableButtons();");
			}
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				generateEnableButtonsOnError(attributes);
			}
		};
		if (am.getObject().getAmpActivityId() == null)
			logframe.setVisible(false);
		else{
			logframe.getButton().add(new AttributeModifier("onclick", "previewLogframe(" + am.getObject().getAmpActivityId() + ");disableButton();"));
			logframe.setVisible(true);
		}
		logframe.getButton().add(isSubmit);
		logframe.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		activityForm.add(logframe);
		
		AmpButtonField preview = new AmpButtonField("preview", "Preview", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					//reenable buttons
					if (am.getObject().getAmpActivityId() == null){
						target.appendJavaScript("alert('" + TranslatorUtil.getTranslatedText("You need to save this activity before being able to preview it!") + "');");
					}
					else{
						target.appendJavaScript("window.location.replace(\"/aim/viewActivityPreview.do~pageId=2~activityId=" + am.getObject().getAmpActivityId() + "~isPreview=1\");");
					}
					target.appendJavaScript("enableButtons();");
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);

					target.add(feedbackPanel);
					target.appendJavaScript("enableButtons();");
			}
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				generateEnableButtonsOnError(attributes);
			}
		};
		//disable on click preview
		preview.getButton().add(new AttributeModifier("class", new Model("sideMenuButtons")));
		preview.getButton().add(new AttributePrepender("onclick", new Model<String>( " disableButton();"), ""));
		if (am.getObject().getAmpActivityId() == null)
			preview.setVisible(false);
		preview.getButton().add(isSubmit);
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


    private void processAndUpdateForm(boolean notDraft, IModel<AmpActivityVersion> am, final Form<?> form, final AjaxRequestTarget target, IndicatingAjaxButton button) {
        am.setObject(am.getObject());
        toggleSemanticValidation(notDraft, form, target);
        form.process(button);
        form.visitChildren(AbstractTextComponent.class,
                new IVisitor<Component, Object>() {
                    @Override
                    public void component(Component component, IVisit<Object> objectIVisit) {
                        IModel<?> model = component.getDefaultModel();
                        AbstractTextComponent atc = (AbstractTextComponent) component;
                        //logger.error(component.getParent().getId());	
                        boolean required = false;
                        List<IValidator> validators = atc.getValidators();
                        for (IValidator validator : validators) {
                            if (validator instanceof ValidatorAdapter && ((ValidatorAdapter) validator).getValidator() instanceof TranslatableValidators) {
                                List<IValidator<? super String>> nestedValidators = ((TranslatableValidators) ((ValidatorAdapter) validator).getValidator()).getValidators();
                                for (IValidator nValidator : nestedValidators)
                                    if (nValidator instanceof ValidatorAdapter && ((ValidatorAdapter) nValidator).getValidator() instanceof StringRequiredValidator) {
                                        required = true;
                                        break;
                                    }
                                break;
                            }
                        }
                        /* Validator for date will make the date field mandatory only when there is an amount entered
                         *  in proposed project code field AMP-17234
                         *  Review to make is more readable 
                         */
                        if(component.getParent().getParent().getId().equalsIgnoreCase("proposedDate") && 
                        		component.getParent().getParent().getParent().get("proposedAmount").getDefaultModel().getObject()!=null ){
                        	((AmpDatePickerFieldPanel)component.getParent().getParent()).getDate().setRequired(true);
                        	if(((AmpDatePickerFieldPanel)component.getParent().getParent()).getDate().getDefaultModel().getObject()==null){
                        		component.error(new ValidationError().addKey("RequiredProposedAmount"));
                        		target.add(component.getParent());
                        	}
                    	}else if(component.getParent().getParent().getId().equalsIgnoreCase("proposedDate") && 
                        		component.getParent().getParent().getParent().get("proposedAmount").getDefaultModel().getObject()==null){
                    		((AmpDatePickerFieldPanel)component.getParent().getParent()).getDate().setRequired(false);
                    	}
                        
                        if (model instanceof TranslationDecoratorModel && required) {
                            TranslationDecoratorModel tdm = (TranslationDecoratorModel) model;

                            if (tdm.getOriginalModel().getObject() == null || tdm.getOriginalModel().getObject().trim().isEmpty()) {
                                ((AbstractTextComponent) component).error(new ValidationError().addKey("Required"));
                                TranslatableValidators.onError(target, atc, null);
                                target.add(component.getParent());
                            }
                        }
                    }
                });

    }


    /**
     * Check for disbursements bigger than commitments or expenditures bigger than disbursements
     * in the funding items
     *
     * @param commitmentErrors map between org acronym&name and string disbursement_sum > commitment_sum
     * @param expenditureErrors map between org acronym&name and string expenditure_sum > disbursement_sum
     *
     * @return true if no errors occurred
     */
    private boolean verifyAmounts(IModel<AmpActivityVersion> am, HashMap<String, String> commitmentErrors,
                                  HashMap<String, String> expenditureErrors) {
        boolean alertIfExpenditureBiggerDisbursement = false;
        if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_EXPENDITURE_BIGGER_DISBURSMENT)))
            alertIfExpenditureBiggerDisbursement = true;
        boolean alertIfDisbursementBiggerCommitments = false;
        if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS)))
            alertIfDisbursementBiggerCommitments = true;

        commitmentErrors.clear();
        expenditureErrors.clear();

        if (alertIfDisbursementBiggerCommitments || alertIfExpenditureBiggerDisbursement){
            verifySections(am, alertIfDisbursementBiggerCommitments, alertIfExpenditureBiggerDisbursement,
                    commitmentErrors, expenditureErrors);
        }

        //Return true if no errors
        return commitmentErrors.isEmpty() && expenditureErrors.isEmpty();
    }

    private void verifySections(IModel<AmpActivityVersion> am, boolean alertIfDisbursementBiggerCommitments,
                                boolean alertIfExpenditureBiggerDisbursement, HashMap<String, String> commitmentErrors,
                                HashMap<String, String> expenditureErrors){
        AmpActivityVersion activity = am.getObject();

        //DonorFunding
        Set fundingSet = activity.getFunding();
        if (fundingSet != null){
            for (Iterator<AmpFunding> iterator = fundingSet.iterator(); iterator.hasNext(); ) {
                AmpFunding funding = iterator.next();
                verifySet(new PropertyModel<Set>(funding, "fundingDetails"),  alertIfDisbursementBiggerCommitments,
                        alertIfExpenditureBiggerDisbursement, commitmentErrors, expenditureErrors, funding,
                        TranslatorUtil.getTranslatedText(OnePager.DONOR_FUNDING_SECTION_NAME) + ": " +
                        funding.getAmpDonorOrgId().getAcronymAndName()+" ["+funding.getGroupVersionedFunding()+"]");
            }
        }

        //Regional Funding
        Set regionalSet = activity.getRegionalFundings();
        if (regionalSet != null){
            HashSet<Long> verifiedRegions = new HashSet<Long>();
            for (Iterator<AmpRegionalFunding> iterator = regionalSet.iterator(); iterator.hasNext(); ){
                AmpRegionalFunding funding = iterator.next();
                if (funding.getRegionLocation() == null || verifiedRegions.contains(funding.getRegionLocation().getId()))
                    continue;
                verifiedRegions.add(funding.getRegionLocation().getId());
                verifySet(new PropertyModel<Set>(am, "regionalFundings"), alertIfDisbursementBiggerCommitments,
                        alertIfExpenditureBiggerDisbursement, commitmentErrors, expenditureErrors, funding.getRegionLocation(),
                        TranslatorUtil.getTranslatedText(OnePager.REGIONAL_FUNDING_SECTION_NAME) + ": " +
                        funding.getRegionLocation().getAutoCompleteLabel());
            }
        }

        //Components Funding
        Set componentSet = activity.getComponentFundings();
        if (componentSet != null){
            HashSet<String> verifiedComponents = new HashSet<String>();
            for (Iterator<AmpComponentFunding> iterator = componentSet.iterator(); iterator.hasNext();){
                AmpComponentFunding funding = iterator.next();
                if (funding.getComponent() == null || verifiedComponents.contains(funding.getComponent().getTitle()))
                    continue;
                verifiedComponents.add(funding.getComponent().getTitle());
                verifySet(new PropertyModel<Set>(am, "componentFundings"), alertIfDisbursementBiggerCommitments,
                        alertIfExpenditureBiggerDisbursement, commitmentErrors, expenditureErrors, funding.getComponent(),
                        TranslatorUtil.getTranslatedText(OnePager.COMPONENTS_SECTION_NAME) + ": " +
                        funding.getComponent().getTitle());
            }
        }
    }

    private void verifySet(PropertyModel<Set> detailsModel, boolean alertIfDisbursementBiggerCommitments,
                           boolean alertIfExpenditureBiggerDisbursement, HashMap<String, String> commitmentErrors,
                           HashMap<String, String> expenditureErrors, Object parent, String itemIdentifier){
        Set details = detailsModel.getObject();
        //always calculate
        double disbursementSum = sumUp(details, Constants.DISBURSEMENT, parent);
        if (alertIfDisbursementBiggerCommitments){
            double commitmentSum = sumUp(details, Constants.COMMITMENT, parent);
            if (disbursementSum > commitmentSum)
                commitmentErrors.put(itemIdentifier, Double.toString(disbursementSum) + " > " + Double.toString(commitmentSum));
        }
        if (alertIfExpenditureBiggerDisbursement){
            double expenditureSum = sumUp(details, Constants.EXPENDITURE, parent);
            if (expenditureSum > disbursementSum)
                expenditureErrors.put(itemIdentifier, Double.toString(expenditureSum) + " > " + Double.toString(disbursementSum));
        }
    }

    private double sumUp(Collection collection, int transactionType, Object parent){
        double total = 0;
        for(Object item : collection){
            AmpCurrency currency = null;
            java.sql.Date currencyDate = null;
            Double exchangeRate = null;
            Double amount = null;
            int itemTransactionType = -1;

            //extract needed information from different funding detail types
            if (item instanceof AmpFundingDetail) {
                AmpFundingDetail fundItem = (AmpFundingDetail) item;
                itemTransactionType = fundItem.getTransactionType();
                amount = fundItem.getTransactionAmount();
                exchangeRate = fundItem.getFixedExchangeRate();
                currency = fundItem.getAmpCurrencyId();
                currencyDate = new java.sql.Date(fundItem.getTransactionDate().getTime());
                //no necessary parent verification for Donor funding since
                //funding details are extracted from AmpFunding
            } else if (item instanceof AmpComponentFunding) {
                AmpComponentFunding compFundItem = (AmpComponentFunding) item;
                itemTransactionType = compFundItem.getTransactionType();
                amount = compFundItem.getTransactionAmount();
                exchangeRate = (compFundItem.getExchangeRate() == null ? null : compFundItem.getExchangeRate().doubleValue());
                currency = compFundItem.getCurrency();
                currencyDate = new java.sql.Date(compFundItem.getTransactionDate().getTime());
                if (!compFundItem.getComponent().equals(parent))
                    continue;
            } else if (item instanceof AmpRegionalFunding) {
                AmpRegionalFunding regFundItem = (AmpRegionalFunding) item;
                itemTransactionType = regFundItem.getTransactionType();
                amount = regFundItem.getTransactionAmount();
                currency = regFundItem.getCurrency();
                currencyDate = new java.sql.Date(regFundItem.getTransactionDate().getTime());
                if (!regFundItem.getRegionLocation().equals(parent))
                    continue;
            }

            //we're only summing up certain funding details
            if (itemTransactionType != transactionType)
                continue;
            if (amount == null)
                continue;
            //if we don't have a fixed exchange rate
            if (exchangeRate == null)
                exchangeRate = Util.getExchange(currency.getCurrencyCode(), currencyDate);

            total += amount/exchangeRate;
        }
        return total;
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
                            logger.error("Component is invalid, adding to target: " + component.getLabel().getObject());
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
                              boolean draft, Model<Integer> redirected,boolean rejected) {
		
		OnePager op = this.findParent(OnePager.class);
		//disable lock refresher
		op.getEditLockRefresher().setEnabled(false);
		
		AmpActivityModel a = (AmpActivityModel) am;
		AmpActivityVersion activity=am.getObject();
		Long oldId = activity.getAmpActivityId();
		Boolean wasDraft=activity.getDraft();
		AmpTeamMember modifiedBy = activity.getModifiedBy();
		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get(); 
		long currentUserId = wicketSession.getCurrentMember().getMemberId();
		
		AmpTeamMember ampCurrentMember = wicketSession.getAmpCurrentMember();


		//Before starting to save check lock
		if (oldId != null && !ActivityGatekeeper.verifyLock(String.valueOf(a.getId()), a.getEditingKey())){
			//Someone else has grabbed the lock ... maybe connection slow and lock refresh timed out
            throw new RedirectToUrlException(ActivityGatekeeper.buildRedirectLink(String.valueOf(a.getId()), currentUserId));
		}
		
		ActivityUtil.saveActivity((AmpActivityModel) am, draft,rejected);

		info(TranslatorUtil.getTranslatedText("Activity saved successfully"));

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

        Long actId = am.getObject().getAmpActivityId();//getAmpActivityGroup().getAmpActivityGroupId();
        String replaceStr;
        if (oldId == null) {
            replaceStr = "new";
        }
        else {
            replaceStr = String.valueOf(oldId);
        }
        if(draft && redirected.getObject().equals(STAY_ON_PAGE)){
				target.appendJavaScript("var newLoc=window.location.href.replace(\"" + replaceStr + "\" , \"" + actId + "\");newLoc=newLoc.substr(0,newLoc.lastIndexOf('?'));window.location.replace(newLoc);");
        }
        else{
            target.appendJavaScript("window.location.replace('/aim/');");
        }
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
					//item.add(new AttributeModifier("style", "display: none"));
				}
			}
		};
		list.setReuseItems(false);
		activityForm.add(list);
	}

	private void generateEnableButtonsOnError(AjaxRequestAttributes attributes) {
		AjaxCallListener listener = new AjaxCallListener() {
			@Override
			public CharSequence getFailureHandler(Component component) {
				// if the ajax call failed we enable the buttons may be it make
				// no sense but not to leave the
				// buttons useless (although if we had an ajax failure the may
				// already be useless)
				return "enableButtons();";
			}
		};
		attributes.getAjaxCallListeners().add(listener);
	}

	/**
	 * Method used from saveAsDraft and Reject activity(the only difference
	 * between them is that reject prior to saving the activity changes the
	 * status of the activity and sends the messages if the user has chosen to
	 * 
	 * @param am
	 * @param feedbackPanel
	 * @param redirected
	 * @param cancelSaveAsDraft
	 * @param target
	 * @param form
	 */
	protected void onSubmitSaveAsDraft(final IModel<AmpActivityVersion> am,
			final FeedbackPanel feedbackPanel, final Model<Integer> redirected,
			final AmpButtonField cancelSaveAsDraft, AjaxRequestTarget target,
			Form<?> form, boolean isReject) {
		// reenable buttons
		if(!isReject){
			target.appendJavaScript("hideDraftPanel();");	
		}else{
			target.appendJavaScript("hideRejectActivityPanel();");
		}
		
		processAndUpdateForm(false, am, form, target,cancelSaveAsDraft.getButton());

		// only in the eventuality that the title field is valid (is not empty)
		// we proceed with the real save!
		if (!form.hasError()){
			//if no error happend and we are rejecting we
			//* change the approval status to rejected
			//* send a message to the creator of the activity
			saveMethod(target, am, feedbackPanel, true, redirected,isReject);
			if(isReject){ //is is reject we send the message
				try {
					AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
					System.out.println(getRequest().getContainerRequest());
					
					sendRejectMessage("activity rejected",am.getObject().getActivityCreator(),wicketSession.getCurrentMember(),am.getObject());
				} catch (AimException e) {
					logger.error("Cannot create reject message",e);
				}
			}
		}
		else {
			target.add(this);
			target.add(cancelSaveAsDraft);
			onErrorSaveAsDraft(feedbackPanel, target, form);
		}
		target.appendJavaScript("enableButtons();");
	}

	protected void onErrorSaveAsDraft(final FeedbackPanel feedbackPanel,
			final AjaxRequestTarget target, Form<?> form) {
		target.appendJavaScript("enableButtons();");
		formSubmitErrorHandle(form, target, feedbackPanel);
	}

	private void sendRejectMessage(String messageToSend,AmpTeamMember tmTo,TeamMember tmFrom,AmpActivityVersion linkedActivity) throws AimException {
		AmpMessage message = new AmpAlert();
		String senderName;
		Long activityId;
		User user;
		activityId=(Long)linkedActivity.getIdentifier();
		user=TeamMemberUtil.getAmpTeamMember(tmFrom.getMemberId()).getUser();
		message.setName("Activity Rejected");
    	message.setSenderType(MessageConstants.SENDER_TYPE_USER);
    	message.setSenderId(tmFrom.getMemberId());
        
        senderName=user.getFirstNames()+" "+user.getLastName()+"<"+user.getEmail()+">;"+tmFrom.getTeamName();
        message.setSenderName(senderName);
        
        message.setRelatedActivityId(activityId);
        
        /*String fullModuleURL=RequestUtils.getFullModuleUrl(request);*/
        String objUrl="/aim/viewActivityPreview.do~public=true~pageId=2~activityId="+activityId;
        message.setObjectURL(objUrl);
        
		
		message.setPriorityLevel(MessageConstants.PRIORITY_LEVEL_CRITICAL);
		if(messageToSend==null){
			messageToSend="";
		}
			message.setDescription(messageToSend);
		
		message.setCreationDate(new Date(System.currentTimeMillis()));

		message.setDraft(false);
		message.setMessageType(0L);
		AmpMessageUtil.saveOrUpdateMessage(message);

		AmpMessageState state = new AmpMessageState();
		state.setMessage(message);
		state.setSender(tmFrom.getMemberName()+";"+tmFrom.getTeamName());
		AmpMessageUtil.saveOrUpdateMessageState(state);
		try{ 
		AmpMessageUtil.createMessageState(message, tmTo);
		}catch(Exception e){
			throw new AimException("cannot create message state",e);
		}
		AmpMessageUtil.saveOrUpdateMessage(message);

	}
	
}
