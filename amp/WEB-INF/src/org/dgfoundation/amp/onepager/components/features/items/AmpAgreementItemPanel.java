package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.models.AmpAgreementSearchModel;
import org.dgfoundation.amp.onepager.models.AutocompleteAcronymTitleModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.util.DbUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AmpAgreementItemPanel extends AmpFieldPanel<AmpFunding>{
    private static final long serialVersionUID = 1L;
    private boolean isFormOpen=false;
    public AmpAgreementItemPanel(String id, final IModel<AmpFunding> model,
            String fmName) {
        super(id, model, fmName);
        
        final Model<AmpAgreement> editAgModel = new Model<AmpAgreement>(new AmpAgreement());
        
        final PropertyModel<AmpAgreement> agreement = new PropertyModel<AmpAgreement>(model, "agreement");
        final Label agreementTextLabel = new Label("agreementText", 
                new AutocompleteAcronymTitleModel(new PropertyModel<String>(agreement, "code"),
                                                  new PropertyModel<String>(agreement, "title"), 
                                                  TranslatorUtil.getTranslation("No agreement was selected!")));
        agreementTextLabel.setOutputMarkupId(true);
        add(agreementTextLabel);

        final Form<AmpAgreement> newAgreementForm = new Form<AmpAgreement>("newAgreement", editAgModel);
        newAgreementForm.setVisibilityAllowed(false);
        AmpAgreementItemPanel.this.isFormOpen=false;

        AmpEditLinkField editAgreement = new AmpEditLinkField("editAgreement", "Edit Agreement") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (agreement.getObject() == null)
                    this.setVisibilityAllowed(false);
                else
                    this.setVisibilityAllowed(true);
            }

            @Override
            protected void onClick(AjaxRequestTarget target) {
                //prepare the editing
                editAgModel.setObject(agreement.getObject());
                newAgreementForm.setVisibilityAllowed(true);
                AmpAgreementItemPanel.this.isFormOpen=true;
                target.add(this.getParent());
            }
        };
        add(editAgreement);

        AmpDeleteLinkField deleteAgreement = new AmpDeleteLinkField("deleteAgreement", "Delete Agreement") {
            @Override
            protected void onConfigure() {
                if (agreement.getObject() == null)
                    this.setVisibilityAllowed(false);
                else
                    this.setVisibilityAllowed(true);
            }

            @Override
            protected void onClick(AjaxRequestTarget target) {
                model.getObject().setAgreement(null);
                newAgreementForm.setVisibilityAllowed(false);
                AmpAgreementItemPanel.this.isFormOpen = false;
                target.add(this.getParent());
            }
        };
        add(deleteAgreement);

        final AmpAutocompleteFieldPanel<AmpAgreement> search = new AmpAutocompleteFieldPanel<AmpAgreement>(
                "search", "Search Agreements", AmpAgreementSearchModel.class) {
            private static final long serialVersionUID = 1L;

            @Override
            protected String getChoiceValue(AmpAgreement choice) {
                return DbUtil.filter(choice.getTitle());
            }
            
            @Override
            protected AmpAgreement getSelectedChoice(Long objId) {
                if (objId.equals(-1L)){
                    AmpAgreement ag = new AmpAgreement();
                    ag.setId(-1L * (long)(Math.random() * 10000)); // creates a tmp id.
                    return ag;
                } else {
                    final Set<AmpAgreement> agItems = getAmpAgreements();
                    for(final AmpAgreement aa: agItems) {
                        if(aa.getId().equals(objId)) {
                            return aa;
                        }
                    }
                }
                return super.getSelectedChoice(objId);
            }
            
            @Override
            protected boolean showAcronyms() {
                return true;
            }
            

            @Override
            protected String getAcronym(AmpAgreement choice) {
                return DbUtil.filter(choice.getCode());
            }
            
            @Override
            public void onSelect(AjaxRequestTarget target, AmpAgreement choice) {
                    model.getObject().setAgreement(choice);
                    if (choice.getId() != null && choice.getId() < 0){
                        newAgreementForm.setVisibilityAllowed(true);
                        editAgModel.setObject(agreement.getObject());
                        AmpAgreementItemPanel.this.isFormOpen=true;
                    }
                target.add(this.getParent());
            }

            @Override
            public Integer getChoiceLevel(AmpAgreement choice) {
                return null;
            }
        };
        search.setOutputMarkupId(true);
        add(search);
        
        AmpTextFieldPanel<String> agCode = new AmpTextFieldPanel<String>("newAgCode", new PropertyModel<String>(editAgModel, "code"), "Code");
        agCode.getTextContainer().setRequired(true);
        newAgreementForm.add(agCode);
        AmpTextFieldPanel<String> agTitle = new AmpTextFieldPanel<String>("newAgTitle", new PropertyModel<String>(editAgModel, "title"), "Title");
        agTitle.getTextContainer().add(new AttributeModifier("size", new Model<String>("34")));
        agTitle.getTextContainer().setRequired(true);
        newAgreementForm.add(agTitle);
        FeedbackPanel fp = new FeedbackPanel("duplicateWarning");
        newAgreementForm.add(fp);
        newAgreementForm.add(new AmpDatePickerFieldPanel("newAgEfDate", new PropertyModel<Date>(editAgModel, "effectiveDate"), "Effective Date"));
        newAgreementForm.add(new AmpDatePickerFieldPanel("newAgSgDate", new PropertyModel<Date>(editAgModel, "signatureDate"), "Signature Date"));
        newAgreementForm.add(new AmpDatePickerFieldPanel("newAgClDate", new PropertyModel<Date>(editAgModel, "closeDate"), "Close Date"));
        newAgreementForm.add(new AmpDatePickerFieldPanel("newAgPaDate", new PropertyModel<Date>(editAgModel, "parlimentaryApprovalDate"), "Parlimentary Approval Date"));
        AmpButtonField submit = new AmpButtonField("submit", "Save", true, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                IModel<AmpAgreement> formModel = (IModel<AmpAgreement>) form.getModel();
                AmpAgreement ag = formModel.getObject();
                
                AmpAuthWebSession session = (AmpAuthWebSession) this.getSession();
                String language=session.getLocale().getLanguage();
                AmpAgreementSearchModel agr1 = new AmpAgreementSearchModel(ag.getCode(),language, null);
                Collection<AmpAgreement> ret = agr1.getObject();
                Iterator<AmpAgreement> it = ret.iterator();
                while (it.hasNext()) {
                    AmpAgreement agr = it.next();
                    if ((agr.getCode() != null && agr.getCode().compareToIgnoreCase(ag.getCode()) == 0)
                            && (ag.getId() == null || agr.getId().compareTo(ag.getId()) != 0)) {
                        error(TranslatorUtil
                                .getTranslation("Warning! The database already contains agreement with similar code"));
                        target.add(newAgreementForm.getParent());
                        return;
                    }
                }

                final Set<AmpAgreement> agItems = getAmpAgreements();
                agItems.add(ag);
                model.getObject().setAgreement(ag);
                target.add(agreementTextLabel);
                editAgModel.setObject(new AmpAgreement());
                newAgreementForm.setVisibilityAllowed(false);
                AmpAgreementItemPanel.this.isFormOpen=false;
                target.add(newAgreementForm.getParent());
                target.add(search);
            }
        };
        newAgreementForm.add(submit);

        AmpAjaxLinkField cancel = new AmpAjaxLinkField("cancel", "Cancel", "Cancel") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                newAgreementForm.setVisibilityAllowed(false);
                AmpAgreementItemPanel.this.isFormOpen = false;
                target.add(newAgreementForm.getParent());
            }
        };
        newAgreementForm.add(cancel);
        newAgreementForm.setOutputMarkupId(true);
        add(newAgreementForm);
    }

    @NotNull
    private static Set<AmpAgreement> getAmpAgreements() {
        Session wSession = Session.get();
        HashSet<AmpAgreement> agItems = wSession.getMetaData(OnePagerConst.AGREEMENT_ITEMS);
        if (agItems == null){
            agItems = new HashSet<>();
            wSession.setMetaData(OnePagerConst.AGREEMENT_ITEMS, agItems);
        }
        return agItems;
    }

    public void validateIsNewAgreementFormClosed(AjaxRequestTarget target) {
        if (this.isFormOpen) {
            // if the form is still open we reject validation
            error(TranslatorUtil.getTranslation("Warning: The agreement form should be confirmed or cancelled."));
            
            target.appendJavaScript("$('#" + this.getMarkupId() + "').parents().show();");
            target.appendJavaScript("$(window).scrollTop($('#" + this.getMarkupId() + "').position().top)");
            
            target.add(this);
        }
    }
    
}
