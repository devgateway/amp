package org.dgfoundation.amp.onepager.components.features.sections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.basic.Label;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpAidEffectivenessFormSectionFeature extends
		AmpFormSectionFeaturePanel 
		implements AmpRequiredComponentContainer {
	
	private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();

	public AmpAidEffectivenessFormSectionFeature(String id, String fmName,
			IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		
		AmpAuthWebSession session = (AmpAuthWebSession) getSession();
		Site site = session.getSite();
		
		String valueToTranslate= "Yes";
		String genKey = TranslatorWorker.generateTrnKey(valueToTranslate);
		String cYesValue = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), valueToTranslate, TranslatorWorker.TRNTYPE_LOCAL, null);

		valueToTranslate="No";
		genKey = TranslatorWorker.generateTrnKey(valueToTranslate);
		String cNoValue = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), valueToTranslate, TranslatorWorker.TRNTYPE_LOCAL, null);
		
		valueToTranslate="Don't Know";
		genKey = TranslatorWorker.generateTrnKey(valueToTranslate);
		String cDontKnow = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), valueToTranslate, TranslatorWorker.TRNTYPE_LOCAL, null);
		
		valueToTranslate="Partially";
		genKey = TranslatorWorker.generateTrnKey(valueToTranslate);
		String cPartially = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), valueToTranslate, TranslatorWorker.TRNTYPE_LOCAL, null);		
		
		List<String> yesNoDontKnowList = new ArrayList<String>();
		yesNoDontKnowList.add(cYesValue);
		yesNoDontKnowList.add(cNoValue);
		yesNoDontKnowList.add(cDontKnow);

		List<String> yesPartiallyNoDontKnowList = new ArrayList<String>();
		yesPartiallyNoDontKnowList.add(cYesValue);
		yesPartiallyNoDontKnowList.add(cPartially);
		yesPartiallyNoDontKnowList.add(cNoValue);
		yesPartiallyNoDontKnowList.add(cDontKnow);
		
		String projectImplementationUnitTitle = "A project implementation unit (P|U) "
				+ " is a dedicated management unit designed to support Project implementation "
				+ " and administration. The project uses a parallel P|U if the unit is created "
				+ " outside the existing structures of national implementation agencies (ministries, departments etc.), "
				+ " i.e. accountable to donors rather than national agencies, with staff appointed by the donor, "
				+ " and/or national staff salaries higher than those in the civil service.";

		addFields(yesNoDontKnowList, projectImplementationUnitTitle,
				"projectImplementationUnit", "projectImplementationUnit",
				"Project uses parallel project implementation unit",true);

		AmpCategorySelectFieldPanel projectImplementationMode = new AmpCategorySelectFieldPanel(
				"projectImplementationMode",
				CategoryConstants.PROJECT_IMPLEMENTATION_MODE_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.PROJECT_IMPLEMENTATION_MODE_KEY),
				CategoryConstants.PROJECT_IMPLEMENTATION_MODE_NAME, true,
				false, null, AmpFMTypes.MODULE);
		projectImplementationMode.getChoiceContainer().setRequired(true);
		requiredFormComponents.add(projectImplementationMode.getChoiceContainer());
		add(projectImplementationMode);

		// imacApproved
		String imacTitle = "If the total project budget is $20m or higher then "
				+ "it has to be approved by the Inter-Ministerial Approval Committee (IMAC)";

		addFields(yesNoDontKnowList, imacTitle,
				"imacApproved", "imacApproved",
				"Project has been approved by IMAC",true);
		
		// nationalOversight
		addFields(yesNoDontKnowList, null,
				"nationalOversight", "nationalOversight",
				"Government is meber of project steering committee",true);
		// onBudget
		String onBudgetTitle = "The project is on-budget if the project budget " +
				"appears en the Government’s budget documentation.";

		addFields(yesPartiallyNoDontKnowList, onBudgetTitle,
				"onBudget", "onBudget",
				"Project is on budget",true);
		// onParliament
		String onParliamentTitle = "The project is on-parliament if project financing " +
				"is included in budget appropriations approved by Parliament.";

		addFields(yesPartiallyNoDontKnowList, onParliamentTitle,
				"onParliament", "onParliament",
				"Project is on parliament",true);		
		// onTreasury
		String onTreasuryTitle = "The project is on-treasury if it disburses " +
				"funds directly into the Govermment’s single treasury account.";

		addFields(yesPartiallyNoDontKnowList, onTreasuryTitle,
				"onTreasury", "onTreasury",
				"Project disburses directly into the Goverment single treasury account",true);		
		// nationalFinancialManagement
		String nationalFinancialManagementTitle = "The project uses national financial systems, if national systems and processes foor budget " +
				"execution and reporting (for example via IFMIS/FreeBalance) are followed.";

		addFields(yesPartiallyNoDontKnowList, nationalFinancialManagementTitle,
				"nationalFinancialManagement", "nationalFinancialManagement",
				"Project uses national financial management systems",true);
		// nationalProcurement
		String nationalProcurementTitle = "The project uses national procurement system if the procurement of works, " +
				"goods and services is managed according to national procedures without donors making additional " +
				"or special requirements for such procurement. ";

		addFields(yesPartiallyNoDontKnowList, nationalProcurementTitle,
				"nationalProcurement", "nationalProcurement",
				"Project uses national procurement systems",true);
		// nationalAudit
		String nationalAuditTitle = "The project uses national audit systems if project finances are audited by National Audit Chamber.";

		addFields(yesNoDontKnowList, nationalAuditTitle,
				"nationalAudit", "nationalAudit",
				"Project uses national audit systems",true);		

	}

	private void addFields(List<String> l, String fieldTitle, String wicketId,
			String modelProperty, String label, boolean required) {
		if(fieldTitle==null){
				fieldTitle="";
		}
		IChoiceRenderer renderer = new IChoiceRenderer() {
			public Object getDisplayValue(Object object) {
				return object;
			}

			public String getIdValue(Object object, int index) {
				return object != null ? object.toString() : "";
			}
		};

		AmpGroupFieldPanel<String> yesNoDontKnowField = new AmpGroupFieldPanel<String>(
				wicketId, new PropertyModel<String>(am, modelProperty), l,
				label, false, false, renderer,fieldTitle);

		yesNoDontKnowField.getChoiceContainer().setRequired(required);
		requiredFormComponents.add(yesNoDontKnowField.getChoiceContainer());
		add(yesNoDontKnowField);
	}

	public List<FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}
}
