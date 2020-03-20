import ActivityApi from '../api/ActivityApi.jsx';
import {
    FM_ROOT,
    FUNDING_INFORMATION,
    TRANSACTION_ID,
    ACTIVITY_FORM_URL,
    ACTIVITY_WORKSPACE_LEAD_DATA,
    CALENDAR_IS_FISCAL,
    IS_FISCAL,
    CALENDAR_ID,
    REORDER_FUNDING_ITEM,
    RTL_DIRECTION,
    SHOW_ACTIVITY_WORKSPACES,
    VERSION_HISTORY_URL,
    COMPARE_ACTIVITY_URL,
    PUBLIC_CHANGE_SUMMARY,
    TEAM_ID,
    PUBLIC_VERSION_HISTORY,
    HIDE_CONTACTS_PUBLIC_VIEW
} from '../common/ReampConstants.jsx';
import DateUtils from '../utils/DateUtils.jsx';
import HydratorHelper from '../utils/HydratorHelper.jsx';
import {
    FieldsManager, FieldPathConstants, ActivityConstants, FeatureManagerConstants, FeatureManager,
    FmManagerHelper, CommonActivityHelper, Constants, NumberUtils, CurrencyRatesManager, ActivityLinks
} from "amp-ui";
import processPossibleValues from '../common/PossibleValuesHelper.jsx';
import Logger from '../utils/LoggerManager' ;
import ActivityFundingTotals from '../utils/ActivityFundingTotals.jsx'
import translate from '../utils/translate.jsx';
import * as ContactAction from './ContactsAction.jsx';
import * as ResourceAction from './ResourceAction.jsx';
import {ACTIVITY_PREVIEW_URL} from "../common/ReampConstants";

export const ACTIVITY_LOAD_LOADING = 'ACTIVITY_LOAD_LOADING';
export const ACTIVITY_LOAD_LOADED = 'ACTIVITY_LOAD_LOADED';
export const ACTIVITY_LOAD_FAILED = 'ACTIVITY_LOAD_FAILED';

export const ACTIVITY_WS_INFO_LOADING = 'ACTIVITY_WS_INFO_LOADING';
export const ACTIVITY_WS_INFO_LOADED = 'ACTIVITY_WS_INFO_LOADED';
export const ACTIVITY_WS_INFO_FAILED = 'ACTIVITY_WS_INFO_FAILED';


export function loadActivityForActivityPreview(activityId) {
    return (dispatch, ownProps) => {
        dispatch(sendingRequest());
        const paths = [...FieldPathConstants.ADJUSTMENT_TYPE_PATHS];
        const {settings} = ownProps().startUpReducer;
        Promise.all([ActivityApi.getActivity(activityId), ActivityApi.getFieldsDefinition(),
            ActivityApi.fetchFmConfiguration(FmManagerHelper.getRequestFmSyncUpBody(Object.values(FeatureManagerConstants))),
            ActivityApi.fetchActivityInfo(activityId)]
        ).then(([activity, fieldsDef, fmTree, activityInfo]) => {
            _registerSettings(settings.language, settings['default-date-format'].toUpperCase());
            if (settings[TEAM_ID]) {
                ContactAction.loadHydratedContactsForActivity(activity)(dispatch, ownProps);
                loadWsInfoForActivity(activity, dispatch);
            }
            ResourceAction.loadResourcesForActivity(activity)(dispatch, ownProps);
            //TODO find a better way to filter out non enabled paths
            const activityFieldsManagerTemp = new FieldsManager(fieldsDef, [],
                settings.language, Logger);
            const enabledPaths = paths.filter(path => activityFieldsManagerTemp.isFieldPathEnabled(path));
            Promise.all([ActivityApi.fetchPossibleValues(enabledPaths),
                ActivityApi.fetchFundingInformation(activityId, settings[Constants.EFFECTIVE_CURRENCY].id)])
                .then(([possibleValuesCollectionAPI, activityFundingInformation]) => {
                    const activityFieldsManager = new FieldsManager(fieldsDef,
                        processPossibleValues(possibleValuesCollectionAPI), settings.language, Logger);
                    _populateFMTree(fmTree);
                    _configureNumberUtils(settings);

                    ActivityApi.fetchValuesForHydration(HydratorHelper.fetchRequestDataForHydration(activity,
                        activityFieldsManager, ''))
                        .then(valuesForHydration => {
                            HydratorHelper.hydrateObject(activity, activityFieldsManager, '',
                                null, valuesForHydration);
                            activity.id = String(activity.internal_id);
                            _convertCurrency(activity, activityFundingInformation);
                            //we create an empty currency rates manager since we will be converting from same currencies,
                            // it wont be used it will just return 1.
                            const currencyRatesManager = new CurrencyRatesManager([],
                                activityFundingInformation.currency, translate, DateUtils, {});
                            return dispatch({
                                type: ACTIVITY_LOAD_LOADED,
                                payload: {
                                    activity: activity,
                                    activityFieldsManager,
                                    activityContext: _getActivityContext(settings, activityInfo, activity),
                                    activityFundingTotals: new ActivityFundingTotals(activity, activityFundingInformation),
                                    currencyRatesManager
                                }
                            })
                        })
                }).catch(error => {
                return dispatch({
                    type: ACTIVITY_LOAD_FAILED,
                    payload: {
                        error: error
                    }
                });
            })//TODO catch errors
        }).catch(error => {
            return dispatch({
                type: ACTIVITY_LOAD_FAILED,
                payload: {
                    error: error
                }
            });
        })
    };

    function _registerSettings(lang, pGSDateFormat) {
        const editLink = {url: ACTIVITY_FORM_URL, isExternal: true};
        const viewLink = {url: ACTIVITY_PREVIEW_URL, isExternal: true};
        const versionHistoryLink = {url: VERSION_HISTORY_URL, isExternal: true};
        const compareActivityLink = {url: COMPARE_ACTIVITY_URL, isExternal: true};

        ActivityLinks.registerLinks({editLink, versionHistoryLink, compareActivityLink, viewLink});
        DateUtils.registerSettings({lang, pGSDateFormat});
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
            teamMember: activityInfo.teamMember,
            reorderFundingItemId: settings[REORDER_FUNDING_ITEM],
            rtlDirection: settings[RTL_DIRECTION],
            showActivityWorkspaces: settings[SHOW_ACTIVITY_WORKSPACES],
            hideContacts: !settings[TEAM_ID] && settings[HIDE_CONTACTS_PUBLIC_VIEW],
            validation: {
                status: activityInfo['validation-status'],
                daysToAutomaticValidation: activityInfo['days-for-automatic-validation'],
            },
            versionHistoryInformation: {
                versionHistory: activityInfo['version-history'],
                activityLastVersionId: activityInfo['amp-activity-last-version-id'],
                updateCurrentVersion: activityInfo['update-current-version'],
                showChangeSummary: settings[TEAM_ID] || settings[PUBLIC_CHANGE_SUMMARY],
                showVersionHistory: settings[TEAM_ID] || settings[PUBLIC_VERSION_HISTORY],
            },
            hideEditableExportFormatsPublicView: settings['hide-editable-export-formats-public-view']

        };
        return activityContext;
    }

    function loadWsInfoForActivity(activity, dispatch) {
        return ActivityApi.fetchActivityWsInformation(activity[ActivityConstants.INTERNAL_ID]).then(activityWsInfo => {
            return dispatch({
                type: ACTIVITY_WS_INFO_LOADED,
                payload: {
                    activityWsInfo: activityWsInfo
                }
            })
        }).catch(error => {
            return dispatch({
                type: ACTIVITY_LOAD_FAILED,
                payload: {
                    error: error
                }
            });
        });
    }
}

