/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
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
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Comment panel to be used in AjaxTabbedPanel, wrapped by a AmpCommentTab
 * 
 * @author aartimon@dginternational.org 
 * @since Oct 7, 2010
 */
public class AmpCommentPanel extends AmpFieldPanel {

    private static final long serialVersionUID = 1L;
    final ListView listView;

    public AmpCommentPanel(String id, String fmName,
            final IModel<AmpActivityVersion> activityModel) {
        super(id, fmName, true);
        super.setOutputMarkupId(true);
        this.fmType = AmpFMTypes.MODULE;

        final String trnAddComment = " <<" + TranslatorUtil.getTranslatedText("add new comment") + ">>";
        final String savedMsg = " <<" + TranslatorUtil.getTranslatedText("comment saved") + ">>";
        final String notSavedMsg = " <<" + TranslatorUtil.getTranslatedText("error, comment not saved") + ">>";
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
        addComment.add(new AttributeModifier("onclick",
                "if (this.value=='" + trnAddComment + "' || this.value=='"
                        + savedMsg + "' || this.value=='" + notSavedMsg
                        + "') this.value='';"));
        addComment.add(new AttributeModifier("onblur",
                " if (this.value=='') this.value='" + trnAddComment + "';"));

        String keypress = "var kc=event.keyCode; if (kc==27) {this.blur();} else if (kc!=13) { return true; } else {this.nextSibling.click(); this.blur(); return false;}";
        addComment.add(new AttributeModifier("onkeypress",
                "if (Wicket.Browser.isSafari()) { return; };" + keypress));
        addComment.add(new AttributeModifier("onkeydown",
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
                HashSet<AmpComments> tmp = org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_ITEMS);
                
                ArrayList<AmpComments> list = new ArrayList<AmpComments>();
                
                if (tmp == null){
                    tmp = new HashSet();
                    org.apache.wicket.Session.get().setMetaData(OnePagerConst.COMMENTS_ITEMS, tmp);
                }
                if (org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS) == null)
                    org.apache.wicket.Session.get().setMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS, new HashSet());
                
                Iterator<AmpComments> it = tmp.iterator();
                while (it.hasNext()) {
                    AmpComments comm = (AmpComments) it.next();
                    if (comm.getAmpFieldId().getAmpFieldId() == field.getAmpFieldId() &&
                            !org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS).contains(comm) )
                        list.add(comm);
                }
                
                if (list.size() == 0  && org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS).isEmpty()){
                    
                    ArrayList<AmpComments> listTmp = new ArrayList<AmpComments>();
                    try {
                        Session session = AmpActivityModel.getHibernateSession();
                        String queryString = "select o from " + AmpComments.class.getName()
                            + " o "
                            + "where (o.ampFieldId=:fid) and (o.ampActivityId=:aid)";
                        Query qry = session.createQuery(queryString);
                        qry.setParameter("fid", field.getAmpFieldId(), LongType.INSTANCE);
                        qry.setParameter("aid", activityModel.getObject()
                                .getAmpActivityId(), LongType.INSTANCE);
                        Iterator itr = qry.list().iterator();
                        while (itr.hasNext()) {
                            AmpComments com = (AmpComments) itr.next();
                            listTmp.add(com);
                        }
                    } catch (Exception e) {
                        logger.error("Unable to get all comments", e);
                    }
                    /*
                    ArrayList listTmp = DbUtil.getAllCommentsByField(
                            field.getAmpFieldId(), activityModel.getObject()
                            .getAmpActivityId());*/
                    tmp.addAll(listTmp);
                    list = listTmp;
                }
                
                return list;
            }
        }; 
        
        // list view to render the comments list
        listView = new ListView<AmpComments>("comment", comments) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<AmpComments> item) {
                final IModel<AmpComments> currentItem = PersistentObjectModel.getModel(item.getModelObject());
                item.add(new Label("userName", new PropertyModel<String>(currentItem, "memberName")));
                AmpDeleteLinkField delOrgId = new AmpDeleteLinkField("deleteComment","Delete Comment") {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        if (org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS) == null)
                            org.apache.wicket.Session.get().setMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS, new HashSet());
                    
                        if (org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_ITEMS) != null)
                        {
                            org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_ITEMS).remove(currentItem.getObject());                          
                        }
                        org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS).add(currentItem.getObject());

                        listView.removeAll();
                        target.add(listView.getParent());
                    }
                };
                item.add(delOrgId);
                AjaxEditableMultiLineLabel ae2 = new AjaxEditableMultiLineLabel(
                        "body", new PropertyModel(currentItem, "comment")) {
                    protected org.apache.wicket.markup.html.basic.MultiLineLabel newLabel(
                            MarkupContainer parent, String componentId,
                            IModel model) {
                        MultiLineLabel label = new MultiLineLabel(componentId,
                                model) {
                            private static final long serialVersionUID = 1L;
                            
                            @Override
                            public void onComponentTagBody(
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
                                            "<div class=\"comment-word-break\">"+StringEscapeUtils.escapeHtml(getDefaultModelObjectAsString())+"</div>");
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
                        //update comment
                    }

                    class LabelAjaxBehavior extends AjaxEventBehavior {
                        private static final long serialVersionUID = 1L;

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
        add(listView);
        // hidden submit link to submit the new comment
        AmpIndicatingAjaxSubmitLink asl = new AmpIndicatingAjaxSubmitLink("addCommentButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (trnAddCommentModel.getObject() != null){
                    AmpComments c = new AmpComments();
                    c.setAmpFieldId(field);
                    c.setCommentDate(new Date());
                    AmpAuthWebSession webSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
                    
                    Long memberId = webSession.getCurrentMember().getMemberId();
                    AmpTeamMember user = TeamMemberUtil.getAmpTeamMember(memberId);
                    c.setMemberName(user.getUser().getName());
                    c.setComment(trnAddCommentModel.getObject().toString());
                    String msg = savedMsg;
                    
                    if (org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_ITEMS) == null)
                        org.apache.wicket.Session.get().setMetaData(OnePagerConst.COMMENTS_ITEMS, new HashSet());
                    
                    org.apache.wicket.Session.get().getMetaData(OnePagerConst.COMMENTS_ITEMS).add(c);
                    
                    addComment.setModelObject(msg);
                    target.add(addComment);
                    target.add(listView.getParent());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
        form.add(asl);
    }

}
