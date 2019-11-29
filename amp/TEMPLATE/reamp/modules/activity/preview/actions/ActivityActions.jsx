import activityJson from '../jsons/activity.json';
import ActivityApi from '../api/ActivityApi.jsx';
import { FM_ROOT, FUNDING_INFORMATION, TRANSACTION_ID,ACTIVITY_FORM_URL } from '../common/ReampConstants.jsx';
import DateUtils from '../tempUtils/DateUtils.jsx';
import {ACTIVITY_WORKSPACE_LEAD_DATA, CALENDAR_IS_FISCAL, IS_FISCAL, CALENDAR_ID} from '../common/ReampConstants';
import {
    FieldsManager, FieldPathConstants, ActivityConstants, FeatureManagerConstants, FeatureManager,
    FmManagerHelper, CommonActivityHelper, Constants, NumberUtils, CurrencyRatesManager, ActivityLinks
} from "amp-ui";
import processPossibleValues from '../common/PossibleValuesHelper.jsx';
import Logger from 'amp/modules/activity/preview/tempUtils/LoggerManager' ;
import ActivityFundingTotals from '../utils/ActivityFundingTotals.jsx'
import translate from '../tempUtils/translate.jsx';

export const ACTIVITY_LOAD_LOADING = 'ACTIVITY_LOAD_LOADING';
export const ACTIVITY_LOAD_LOADED = 'ACTIVITY_LOAD_LOADED';
export const ACTIVITY_LOAD_FAILED = 'ACTIVITY_LOAD_FAILED';

export function loadActivityForActivityPreview(activityId) {
    return (dispatch, ownProps) => {
        // register links
        const editLink = { url:ACTIVITY_FORM_URL, isExternal:true };
        ActivityLinks.registerLinks({editLink });
        dispatch(sendingRequest());
        const paths = [...FieldPathConstants.ADJUSTMENT_TYPE_PATHS, ActivityConstants.CREATED_BY, ActivityConstants.TEAM,
            ActivityConstants.MODIFIED_BY];
        Promise.all([ActivityApi.getActivity(activityId), ActivityApi.getFieldsDefinition(),
            ActivityApi.fetchFmConfiguration(FmManagerHelper.getRequestFmSyncUpBody(Object.values(FeatureManagerConstants))),
            ActivityApi.fetchSettings(), ActivityApi.fetchActivityInfo(activityId)]
        ).then(([activity, fieldsDef, fmTree, settings, activityInfo]) => {
            //TODO activity is still the JSON file since we dont have yet the hydrated version
            //TODO find a better way to filter out non enabled paths
            const activityFieldsManagerTemp = new FieldsManager(fieldsDef, [], 'en', Logger);
            const enabledPaths = paths.filter(path => activityFieldsManagerTemp.isFieldPathEnabled(path));
            Promise.all([ActivityApi.fetchPossibleValues(enabledPaths),
                ActivityApi.fetchFundingInformation(activityId, settings[Constants.EFFECTIVE_CURRENCY].id)]).then(([possibleValuesCollectionAPI, activityFundingInformation]) => {
                const activityFieldsManager = new FieldsManager(fieldsDef, processPossibleValues(possibleValuesCollectionAPI), 'en', Logger);
                _populateFMTree(fmTree);
                _configureNumberUtils(settings);
                //this has to be done _after_ activity Hydration
                _convertCurrency(activityJson, activityFundingInformation);
                //we create an empty currency rates manager since we will be converting from same currencies, it wont
                //be used it will just return 1.
                const currencyRatesManager = new CurrencyRatesManager([],
                    activityFundingInformation.currency, translate, DateUtils, {});
                return dispatch({
                    type: ACTIVITY_LOAD_LOADED,
                    payload: {
                        activity: activityJson,
                        activityFieldsManager,
                        activityContext: _getActivityContext(settings, activityInfo, activityJson),
                        activityFundingTotals: new ActivityFundingTotals(activityJson, activityFundingInformation),
                        currencyRatesManager
                    }
                })
            })//TODO catch errors
        }).catch(error => {
            return dispatch({
                type: ACTIVITY_LOAD_FAILED,
                payload: {
                    error: error
                }
            })
        })
    }

    function sendingRequest() {
        return {
            type: ACTIVITY_LOAD_LOADING
        };
    }

    function _populateFMTree(fmTree) {
        FeatureManager.setFMTree(fmTree[FM_ROOT]);
        FeatureManager.setLoggerManager(Logger);
    }

    function _configureNumberUtils(settings) {
        NumberUtils.registerSettings({
            gsDefaultGroupSeparator: settings['number-group-separator'],
            gsDefaultDecimalSeparator: settings,
            gsDefaultNumberFormat: settings['gs-number-format'],
            gsAmountInThousands: settings['number-divider'] + '',
            Translate: translate,
            Logger,
            shouldForceUnits: true
        });
        NumberUtils.createLanguage();
    }

    function _convertCurrency(activity, activityFundingInformation) {
        const currencyCode = activityFundingInformation.currency;
        const fundings = activity[ActivityConstants.FUNDINGS];
        if (activity[ActivityConstants.PPC_AMOUNT] && activityFundingInformation[ActivityConstants.PPC_AMOUNT]) {
            activity[ActivityConstants.PPC_AMOUNT].amount = activityFundingInformation[ActivityConstants.PPC_AMOUNT];
            activity[ActivityConstants.PPC_AMOUNT].currency.value = currencyCode;
        }
        const transactions = [...FieldPathConstants.TRANSACTION_TYPES,
            ActivityConstants.ESTIMATED_DISBURSEMENTS, ActivityConstants.MTEF_PROJECTIONS];
        if (fundings) {
            fundings.forEach(funding => {
                const fundingFromConverted =
                    activityFundingInformation[FUNDING_INFORMATION][ActivityConstants.FUNDINGS].find(fundingInWsCurrency =>
                        funding[ActivityConstants.FUNDING_ID] === fundingInWsCurrency[ActivityConstants.FUNDING_ID]);
                if (activityFundingInformation) {
                    transactions.forEach(tt => {
                        const rawTransactionsList = funding[tt];
                        const transactionListInWsCurrency =
                            fundingFromConverted[ActivityConstants.FUNDING_DETAILS][tt];
                        if (rawTransactionsList && rawTransactionsList.length > 0
                            && transactionListInWsCurrency && transactionListInWsCurrency.length > 0) {
                            rawTransactionsList.forEach(t => {
                                const transactionInWsCurrency =
                                    transactionListInWsCurrency.find(tiwc => tiwc[TRANSACTION_ID] === t[TRANSACTION_ID]);
                                t[ActivityConstants.TRANSACTION_AMOUNT] = transactionInWsCurrency[ActivityConstants.TRANSACTION_AMOUNT];
                                t[ActivityConstants.CURRENCY].value = currencyCode;
                            })
                        }
                    });
                }
            });
        }
    }

    function _getActivityContext(settings, activityInfo, activity) {
        const activityContext = {
            activityStatus: CommonActivityHelper.getActivityStatus(activity),
            activityWorkspace: activityInfo.activityWorkspace,
            calendar: {id: settings[CALENDAR_ID], [IS_FISCAL]: settings[CALENDAR_IS_FISCAL]},
            workspaceLeadData: activityInfo[ACTIVITY_WORKSPACE_LEAD_DATA],
            effectiveCurrency: settings[Constants.EFFECTIVE_CURRENCY].code,
            teamMember: activityInfo.teamMember
        };
        return activityContext;
    }

}

