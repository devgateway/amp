package org.dgfoundation.amp.onepager.components;

import java.util.Comparator;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeService;
import org.digijava.module.aim.dbentity.AmpActivityFrozen;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.util.FeaturesUtil;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.FUNDING_ITEM_LIST_EXPANDABLE;

/**
 * Class that extends ListEditor to be able to block edit in a funding item
 * 
 * @author JulianEduardo
 *
 * @param <T>
 */
public class FundingListEditor<T> extends ExpandableListEditor<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FundingListEditor(String id, IModel<Set<T>> model) {
        super(id, model);
    }

    public FundingListEditor(String id, IModel<Set<T>> model, Comparator<T> comparator) {
        super(id, model, comparator);
    }

    @Override
    protected void onPopulateItem(ListItem<T> item) {
        Boolean enabled = item.isEnabled();
        boolean fmMode = ((AmpAuthWebSession) getSession()).isFmMode();
        FundingInformationItem fundingDetailItem = (FundingInformationItem) item.getModel().getObject();
        AmpActivityFrozen ampActivityFrozen = org.apache.wicket.Session.get()
                .getMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION);
        if (ampActivityFrozen != null && fundingDetailItem.getDbId() != null
                && fundingDetailItem.getTransactionDate() != null && !fmMode) {
            AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
            enabled = DataFreezeService.isFundingEditable(ampActivityFrozen, s.getAmpCurrentMember(),
                    fundingDetailItem.getTransactionDate()) && enabled;
        }
        item.setEnabled(enabled);
        fundingDetailItem.setCheckSum(ActivityUtil.calculateFundingDetailCheckSum(fundingDetailItem));
    }

    @Override
    public boolean isExpandable() {
        return FeaturesUtil.getGlobalSettingValueBoolean(FUNDING_ITEM_LIST_EXPANDABLE);
    }
}
