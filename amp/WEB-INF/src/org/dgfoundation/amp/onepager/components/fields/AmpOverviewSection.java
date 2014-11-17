package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.tables.AmpOverallFundingTotalsTable;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;

public class AmpOverviewSection extends AmpComponentPanel<Void> implements AmpRequiredComponentContainer {
	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;
	private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();

	public AmpOverviewSection(String id, String fmName, IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName);
		
		//create a propose project cost section
		
		
        AmpProposedProjectCost propProjCost = new AmpProposedProjectCost(
				"propProjCost", "Proposed Project Cost", am);
        propProjCost.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
		add(propProjCost);
		getRequiredFormComponents().addAll(
				propProjCost.getRequiredFormComponents());
		
		
		AmpOverallFundingTotalsTable overallFunding = new AmpOverallFundingTotalsTable(
				"overallFunding", "Overall Funding Totals", new PropertyModel<Set<AmpFunding>>(am, "funding"));
		overallFunding.add(UpdateEventBehavior.of(OverallFundingTotalsEvents.class));

		add(overallFunding);
	}
	 
	/**
	 * Return a list of FormComponent that are marked as required for this panel
	 * 
	 * @return List<FormComponent<?>> with the FormComponent
	 */
	public List<FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}
}


