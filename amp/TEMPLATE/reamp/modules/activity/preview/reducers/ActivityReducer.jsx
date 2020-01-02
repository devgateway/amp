import {
    ACTIVITY_LOAD_LOADING,
    ACTIVITY_LOAD_LOADED,
    ACTIVITY_LOAD_FAILED,
    ACTIVITY_WS_INFO_LOADED
} from '../actions/ActivityActions.jsx';
import {ACTIVITY_WS_INFO_LOADING} from "../actions/ActivityActions";

const defaultState = {
    activity: undefined,
    isActivityLoading: true,
    isActivityLoaded: false,
    error: undefined,
    activityWsInfo: []
};

export default function activityReducer(state: Object = defaultState, action: Object) {
    switch (action.type) {

        case ACTIVITY_LOAD_LOADING:
            return {...state, isActivityLoading: true};
        case ACTIVITY_LOAD_LOADED:
            return {
                ...state,
                activity: action.payload.activity,
                activityFieldsManager: action.payload.activityFieldsManager,
                activityContext: action.payload.activityContext,
                fmTree: action.payload.fmTree,
                activityFundingTotals: action.payload.activityFundingTotals,
                currencyRatesManager: action.payload.currencyRatesManager,
                isActivityLoading: false,
                isActivityLoaded: true
            };
        case ACTIVITY_LOAD_FAILED:
            return {...state, error: action.payload.error};
        case ACTIVITY_WS_INFO_LOADED:
            return {...state, activityWsInfo: action.payload.activityWsInfo};
        default:
            return state;
    }
}
