/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.ErrorLevelsFeedbackMessageFilter;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

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
		
		activityForm=new Form<AmpActivityVersion>("activityForm") { 
			@Override
			protected void onError() {
				// TODO Auto-generated method stub
				super.onError();
			}
		};
		activityForm.setOutputMarkupId(true);
		
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
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				saveMethod(target, am, feedbackPanel, false);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
			
		};
		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		saveAndSubmit.getButton().add(new AttributePrepender("onclick", new Model("for (instance in CKEDITOR.instances) CKEDITOR.instances[instance].updateElement(); "), ""));
		activityForm.add(saveAndSubmit);

		AmpButtonField saveAsDraft = new AmpButtonField("saveAsDraft", "Save as Draft", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				saveMethod(target, am, feedbackPanel, true);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
		};
		saveAsDraft.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		activityForm.add(saveAsDraft);

		AmpButtonField logframe = new AmpButtonField("logframe", "Logframe", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		if (am.getObject().getAmpActivityId() == null)
			logframe.setVisible(false);
		else{
			logframe.add(new SimpleAttributeModifier("onclick", "previewLogframe(" + am.getObject().getAmpActivityId() + ");"));
			logframe.setVisible(true);
		}
		logframe.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		activityForm.add(logframe);
		
		AmpButtonField preview = new AmpButtonField("preview", "Preview", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.appendJavascript("window.location.replace(\"/aim/viewActivityPreview.do~pageId=2~activityId=" + am.getObject().getAmpActivityId() + "~isPreview=1\");");
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
		};
		preview.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		if (am.getObject().getAmpActivityId() == null)
			preview.setVisible(false);
		
		activityForm.add(preview);
		
		ListView<AmpComponentPanel> list = new ListView<AmpComponentPanel>("featureList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null)
					item.add(item.getModelObject());
				else{
					Label tmp = new Label("featureItem", "ERROR: Section failed to load!");
					tmp.add(new SimpleAttributeModifier("style", "font-size: medium; font-style: bold; color: red; margin: 15px;"));
					item.add(tmp);
				}
					
			}
		};
		list.setReuseItems(true);
		activityForm.add(list);
		
		quickMenu(am, listModel);
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

	private void quickMenu(IModel<AmpActivityVersion> am, AbstractReadOnlyModel<List<AmpComponentPanel>> listModel) {
		ListView<AmpComponentPanel> list = new ListView<AmpComponentPanel>("quickList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null){
					Label label = new TrnLabel("quickName", item.getModelObject().getFMName());
					String itemId = item.getModelObject().getFMName().replaceAll(" ", "");
					label.add(new SimpleAttributeModifier("onclick", "$('#" + itemId + "').parent().parent().siblings('div:first').show();$('html, body').animate({scrollTop: $('#" + itemId + "').offset().top}, 1200); return false;"));
					if (!item.getModelObject().isVisible())
						item.setVisible(false);
					item.add(label);
				}
				else{
					WebMarkupContainer tmp = new WebMarkupContainer("quickName");
					tmp.setVisible(false);
					item.add(tmp);
				}
			}
		};
		list.setReuseItems(true);
		activityForm.add(list);
	}

}
