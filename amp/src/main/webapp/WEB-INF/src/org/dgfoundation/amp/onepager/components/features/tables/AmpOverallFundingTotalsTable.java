package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Set;

import org.digijava.module.aim.helper.Constants;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelInformationFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOverallFundingModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

public class AmpOverallFundingTotalsTable extends AmpComponentPanel<Void> {

    public AmpOverallFundingTotalsTable(String id, String fmName, IModel<Set<AmpFunding>> funding) throws Exception {
        super(id, fmName);
        // TODO Auto-generated constructor stub
        //int COMMITMENT = 0 ;
        //int DISBURSEMENT = 1 ;
        //int EXPENDITURE = 2 ;
     //   AmpLabelFieldPanel<T>
        @SuppressWarnings({ "rawtypes", "unchecked" })
        AmpLabelInformationFieldPanel totalActualCommitments
                = new AmpLabelInformationFieldPanel("totalActualCommitments",
                new AmpOverallFundingModel(funding, null, Constants.COMMITMENT,CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),
                "Total Actual Commitments");
       add(totalActualCommitments);
        
        AmpLabelInformationFieldPanel totalActualDisbursements =
                new AmpLabelInformationFieldPanel("totalActualDisbursements",
                        new AmpOverallFundingModel(funding, null, Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),
                        "Total Actual Disbursement");
        add(totalActualDisbursements);


        AmpLabelInformationFieldPanel totalActualExpenditures =
                new AmpLabelInformationFieldPanel("totalActualExpenditures",
                        new AmpOverallFundingModel(funding, null, Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),
                        "Total Actual Expenditure");
        add(totalActualExpenditures);       
        
        AmpLabelInformationFieldPanel totalActualArrears =
                new AmpLabelInformationFieldPanel("totalActualArrears",
                        new AmpOverallFundingModel(funding, null, Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),
                        "Total Actual Arrears");
        add(totalActualArrears);            
        
        
        AmpLabelInformationFieldPanel totalPlannedCommitments =
                new AmpLabelInformationFieldPanel("totalPlannedCommitments",
                        new AmpOverallFundingModel(funding, null, Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),
                        "Total Planned Commitments");
        add(totalPlannedCommitments);
        
        AmpLabelInformationFieldPanel totalPlannedDisbursements
                = new AmpLabelInformationFieldPanel("totalPlannedDisbursements",
                new AmpOverallFundingModel(funding, null, Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),
                "Total Planned Disbursement");
        add(totalPlannedDisbursements);


        AmpLabelInformationFieldPanel totalPlannedExpenditures
                = new AmpLabelInformationFieldPanel("totalPlannedExpenditures",
                new AmpOverallFundingModel(funding, null, Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),
                "Total Planned Expenditure");
        add(totalPlannedExpenditures);
                AmpLabelInformationFieldPanel totalPlannedArrears
                = new AmpLabelInformationFieldPanel("totalPlannedArrears",
                new AmpOverallFundingModel(funding, null, Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),
                "Total Planned Arrears");
        add(totalPlannedArrears);
        
        AmpLabelInformationFieldPanel totalMtefProjectionsProjection = new AmpLabelInformationFieldPanel("totalMtefProjectionsProjection",
                new AmpOverallFundingModel(funding, null, Constants.MTEFPROJECTION, CategoryConstants.MTEF_PROJECTION_PROJECTION.getValueKey()),
                "Total MTEF Projections Projection");
        add(totalMtefProjectionsProjection);

        AmpLabelInformationFieldPanel totalMtefProjectionsPipeline = new AmpLabelInformationFieldPanel("totalMtefProjectionsPipeline",
                new AmpOverallFundingModel(funding, null, Constants.MTEFPROJECTION, CategoryConstants.MTEF_PROJECTION_PIPELINE.getValueKey()),
                "Total MTEF Projections Pipeline");
        add(totalMtefProjectionsPipeline);
        
        AmpCategoryValue planned = CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getAmpCategoryValueFromDB();
        AmpCategoryValue actual = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
        AmpCategoryValue projection = CategoryConstants.MTEF_PROJECTION_PROJECTION.getAmpCategoryValueFromDB();
        AmpCategoryValue pipeline = CategoryConstants.MTEF_PROJECTION_PIPELINE.getAmpCategoryValueFromDB();
        
        if (planned == null || !actual.isVisible()) {
            totalActualCommitments.setVisibilityAllowed(false);
            totalActualDisbursements.setVisibilityAllowed(false);
            totalActualExpenditures.setVisibilityAllowed(false);
            totalActualArrears.setVisibilityAllowed(false);
        }
        
        if (planned == null || !planned.isVisible()) {
            totalPlannedCommitments.setVisibilityAllowed(false);
            totalPlannedDisbursements.setVisibilityAllowed(false);
            totalPlannedExpenditures.setVisibilityAllowed(false);
            totalPlannedArrears.setVisibilityAllowed(false);
        }
        
        if (projection == null || !projection.isVisible()) {
            totalMtefProjectionsProjection.setVisibilityAllowed(false);
        }
        
        if (pipeline == null || !pipeline.isVisible()) {
            totalMtefProjectionsPipeline.setVisibilityAllowed(false);
        }
    }
}
