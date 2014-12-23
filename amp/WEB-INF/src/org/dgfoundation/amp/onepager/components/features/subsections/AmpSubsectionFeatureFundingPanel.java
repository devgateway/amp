package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFundingItemSummaryFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelInformationFieldPanel;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpOverallFundingModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpSubsectionFeatureFundingPanel<T> extends AmpSubsectionFeaturePanel<T> {

	public AmpSubsectionFeatureFundingPanel(String id, String fmName, IModel<T> model,Integer transactionType) {
		this(id, fmName, model, false, false, false,transactionType);
	}

	public AmpSubsectionFeatureFundingPanel(String id, String fmName, IModel<T> model, boolean hideLabel,
			boolean hideAmountsInThousandsWarning, boolean showSummary,Integer transactionType) {
		super(id, fmName, model, hideLabel, hideAmountsInThousandsWarning, false);
		AmpLabelFundingItemSummaryFieldPanel<AmpFunding> s = new AmpLabelFundingItemSummaryFieldPanel<AmpFunding>(
				"featureSummary", fmName, (IModel<AmpFunding>) model, transactionType);
		s.add(UpdateEventBehavior.of(OverallFundingTotalsEvents.class));
		add(s);
	}

}
