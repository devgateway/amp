package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Date;
import java.util.HashSet;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpAgreementSearchModel;
import org.dgfoundation.amp.onepager.models.AutocompleteAcronymTitleModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.util.DbUtil;

public class AmpAgreementItemPanel extends AmpFieldPanel<AmpFunding>{
	private static final long serialVersionUID = 1L;

	public AmpAgreementItemPanel(String id, final IModel<AmpFunding> model,
			String fmName) {
		super(id, model, fmName);
		
		final Model<AmpAgreement> newAgModel = new Model<AmpAgreement>(new AmpAgreement());
		
		PropertyModel<AmpAgreement> agreement = new PropertyModel<AmpAgreement>(model, "agreement");
		final Label agreementTextLabel = new Label("agreementText", 
				new AutocompleteAcronymTitleModel(new PropertyModel<String>(agreement, "code"),
												  new PropertyModel<String>(agreement, "title"), 
												  TranslatorUtil.getTranslation("No agreement was selected!")));
		agreementTextLabel.setOutputMarkupId(true);
		add(agreementTextLabel);

		final Form<AmpAgreement> newAgreementForm = new Form<AmpAgreement>("newAgreement", newAgModel);
		newAgreementForm.setVisibilityAllowed(false);
		final AmpAutocompleteFieldPanel<AmpAgreement> search = new AmpAutocompleteFieldPanel<AmpAgreement>(
				"search", "Search Agreements", AmpAgreementSearchModel.class) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getChoiceValue(AmpAgreement choice) {
				return DbUtil.filter(choice.getTitle());
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
				if (choice.getId() != null && choice.getId() < 0){
					newAgreementForm.setVisibilityAllowed(true);
				}
				else{
					model.getObject().setAgreement(choice);
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
		
		AmpTextFieldPanel<String> agCode = new AmpTextFieldPanel<String>("newAgCode", new PropertyModel<String>(newAgModel, "code"), "Code");
		agCode.getTextContainer().setRequired(true);
		newAgreementForm.add(agCode);
		AmpTextFieldPanel<String> agTitle = new AmpTextFieldPanel<String>("newAgTitle", new PropertyModel<String>(newAgModel, "title"), "Title");
		agTitle.getTextContainer().setRequired(true);
		newAgreementForm.add(agTitle);
		
		newAgreementForm.add(new AmpDatePickerFieldPanel("newAgEfDate", new PropertyModel<Date>(newAgModel, "effectiveDate"), "Effective Date"));
		newAgreementForm.add(new AmpDatePickerFieldPanel("newAgSgDate", new PropertyModel<Date>(newAgModel, "signatureDate"), "Signature Date"));
		newAgreementForm.add(new AmpDatePickerFieldPanel("newAgClDate", new PropertyModel<Date>(newAgModel, "closeDate"), "Close Date"));
		AmpButtonField submit = new AmpButtonField("submit", "Add Agreement", true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				IModel<AmpAgreement> formModel = (IModel<AmpAgreement>) form.getModel();
				AmpAgreement ag = formModel.getObject();
				Session wSession = Session.get();
				HashSet<AmpAgreement> agItems = wSession.getMetaData(OnePagerConst.AGREEMENT_ITEMS);
				if (agItems == null){
					agItems = new HashSet<AmpAgreement>();
					wSession.setMetaData(OnePagerConst.AGREEMENT_ITEMS, agItems);
				}
				agItems.add(ag);
				model.getObject().setAgreement(ag);
				target.add(agreementTextLabel);
				newAgModel.setObject(new AmpAgreement());
				newAgreementForm.setVisibilityAllowed(false);
				target.add(newAgreementForm.getParent());
				target.add(search);
			}
		};
		newAgreementForm.add(submit);
		newAgreementForm.setOutputMarkupId(true);
		add(newAgreementForm);

	}

}
