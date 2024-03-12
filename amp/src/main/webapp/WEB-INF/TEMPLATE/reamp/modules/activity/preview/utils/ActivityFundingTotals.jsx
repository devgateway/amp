import { FieldPathConstants, NumberUtils } from 'amp-ui';

export default class ActivityFundingTotals {
    constructor(activity, activityFunding) {
        this._activity = activity;
        this._activityFunding = activityFunding;
    }
    getTotals(adjType, trnType, filter = {}) {
        const total =
            this._activityFunding['funding_information'].totals.find(t => {
                return t['transaction_type'] === trnType && t['adjustment_type'] === adjType;
            })

        return total ? total.amount : 0;
    }
    getMTEFTotal() {
        const mtefTotal =
            this._activityFunding['funding_information'].totals.find(t => {
                return t['transaction_type'] === 'mtef_projections'
            });
        return mtefTotal ? mtefTotal.amount : 0;
    }
    formatAmount(amount, isPercentage = false) {
        // TODO
        const value = NumberUtils.rawNumberToFormattedString(amount);
        if (isPercentage) {
            return `${value}%`;
        }
        return value;
    }
}
