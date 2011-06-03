/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Date;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Comment panel to be used in AjaxTabbedPanel, wrapped by a AmpCommentTab
 * 
 * @author aartimon@dginternational.org since Oct 7, 2010
 */
public class AmpCommentPanel extends AmpFieldPanel {

	private static final long serialVersionUID = 1L;
	final ListView listView;

	public AmpCommentPanel(String id, String fmName,
			final IModel<AmpActivityVersion> activityModel) {
		super(id, fmName, true);
		super.setOutputMarkupId(true);
		this.fmType = AmpFMTypes.FEATURE;

		final String trnAddComment = TranslatorUtil
				.getTranslatedText(" <<add new comment>>");
		final String savedMsg = TranslatorUtil
				.getTranslatedText(" <<comment saved>>");
		final String notSavedMsg = TranslatorUtil
				.getTranslatedText(" <<error, comment not saved>>");
		final Model trnAddCommentModel = new Model(trnAddComment);
		Form<String> form = new Form<String>("form", trnAddCommentModel);
		add(form);
		// Add the textbox used to add new comments
		final TextField addComment = new TextField("addComment",
				trnAddCommentModel);
		addComment.setOutputMarkupId(true);
		// addComment.add(new
		// BeautyTipBehavior(TranslatorUtil.getTranslatedText("Add new comment. Submit by pressing [ENTER] or cancel by pressing [ESC]")));
		addComment.setModelObject(trnAddComment);
		addComment.add(new SimpleAttributeModifier("onclick",
				"if (this.value=='" + trnAddComment + "' || this.value=='"
						+ savedMsg + "' || this.value=='" + notSavedMsg
						+ "') this.value='';"));
		addComment.add(new SimpleAttributeModifier("onblur",
				" if (this.value=='') this.value='" + trnAddComment + "';"));

		// Behaviour to click the hidden AjaxSubmitLink so that it submits the
		// new comment
		//AmpAjaxBehavior commentBehavior = new AmpAjaxBehavior();
		//addComment.add(commentBehavior);
		String keypress = "var kc=wicketKeyCode(event); if (kc==27) {this.blur();} else if (kc!=13) { return true; } else {this.nextSibling.onclick(); this.blur(); return false;}";
		addComment.add(new SimpleAttributeModifier("onkeypress",
				"if (Wicket.Browser.isSafari()) { return; };" + keypress));
		addComment.add(new SimpleAttributeModifier("onkeydown",
				"if (!Wicket.Browser.isSafari()) { return; };" + keypress));

		form.add(addComment);

		final AmpField field = DbUtil.getAmpFieldByName(fmName);
		if (field == null)
			throw new IllegalArgumentException(
					"Can't find AMP field with name:" + fmName);

		// load the comments, for this activity and the specified field, from
		// the db
		IModel<ArrayList<AmpComments>> comments = new LoadableDetachableModel<ArrayList<AmpComments>>() {
			@Override
			protected ArrayList<AmpComments> load() {
				return DbUtil.getAllCommentsByField(
				field.getAmpFieldId(), activityModel.getObject()
						.getAmpActivityId());
			}
		}; 
		
		// list view to render the comments list
		listView = new ListView<AmpComments>("comment", comments) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AmpComments> item) {
				item.add(new Label("userName", item.getModelObject().getMemberName()));
				AmpDeleteLinkField delOrgId = new AmpDeleteLinkField("deleteComment","Delete Comment") {
					@Override
					protected void onClick(AjaxRequestTarget target) {
						Transaction tx = null;
						try {
							Session session = PersistenceManager.getSession();
							tx = session.beginTransaction();
							session.delete(item.getModelObject());
							tx.commit();
							session.flush();
							session.close();
						} catch (Exception e) {
							logger.error("Error while deleting comment", e);
							tx.rollback();
						}
						target.addComponent(this.getParent().getParent()
								.getParent());
					}
				};
				item.add(delOrgId);
				AjaxEditableMultiLineLabel ae2 = new AjaxEditableMultiLineLabel(
						"body", new PropertyModel(PersistentObjectModel.getModel(item.getModelObject()), "comment")) {
					protected org.apache.wicket.markup.html.basic.MultiLineLabel newLabel(
							MarkupContainer parent, String componentId,
							IModel model) {
						MultiLineLabel label = new MultiLineLabel(componentId,
								model) {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected void onComponentTagBody(
									MarkupStream markupStream,
									ComponentTag openTag) {
								Object modelObject = getDefaultModelObject();
								if (modelObject == null
										|| "".equals(modelObject)) {
									replaceComponentTagBody(markupStream,
											openTag, defaultNullLabel());
								} else {
									replaceComponentTagBody(markupStream,
											openTag,
											"<p style=\"white-space: normal; margin-top: 0px; margin-bottom: 0px;\">"+getDefaultModelObjectAsString()+"</p>");
								}
							}
						};
						label.setEscapeModelStrings(false);
						label.setOutputMarkupId(true);
						label.add(new LabelAjaxBehavior(getLabelAjaxEvent()));
						return label;
					}

					@Override
					protected FormComponent newEditor(MarkupContainer parent,
							String componentId, IModel model) {
						FormComponent a = super.newEditor(parent, componentId,
								model);
						a.add(new AttributeAppender("style", new Model(
								"font-size: 10px;"), ""));
						return a;
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						super.onSubmit(target);
						Session session;
						Transaction tx = null;
						try {
							session = PersistenceManager.getSession();
							tx = session.beginTransaction();
							session.update(item.getModelObject());
							tx.commit();
							session.flush();
							session.close();
						} catch (Exception e) {
							logger.error("Comment edit error:", e);
							tx.rollback();
						}
					}

					class LabelAjaxBehavior extends AjaxEventBehavior {
						private static final long serialVersionUID = 1L;

						/**
						 * Construct.
						 * 
						 * @param event
						 */
						public LabelAjaxBehavior(String event) {
							super(event);
						}

						@Override
						protected void onEvent(AjaxRequestTarget target) {
							onEdit(target);
						}
					}

				};
				ae2.setCols(39);
				ae2.setRows(3);
				item.add(ae2);
			}
		};
		listView.setOutputMarkupId(true);
		listView.setReuseItems(false);
		add(listView);

		// hidden submit link to submit the new comment
		AjaxSubmitLink asl = new AjaxSubmitLink("addCommentButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpComments c = new AmpComments();
				c.setAmpActivityId(activityModel.getObject());
				c.setAmpFieldId(field);
				c.setCommentDate(new Date());
				AmpAuthWebSession webSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
				
				Long memberId = webSession.getCurrentMember().getMemberId();
				AmpTeamMember user = TeamMemberUtil.getAmpTeamMember(memberId);
				c.setMemberId(user);
				c.setMemberName(user.getUser().getName());
				c.setComment(trnAddCommentModel.getObject().toString());
				String msg = savedMsg;
				Transaction tx = null;
				try {
					Session session = PersistenceManager.getSession();
					tx = session.beginTransaction();
					session.save(c);
					tx.commit();
					//session.flush();
					session.close();
				} catch (Exception e) {
					error(e);
					tx.rollback();
					e.printStackTrace();
					msg = notSavedMsg;
				}

				addComment.setModelObject(msg);
				target.addComponent(addComment);
				target.addComponent(this.getParent().getParent());
			}
		};
		form.add(asl);
	}

}
