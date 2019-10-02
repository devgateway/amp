import {ACTIVITY_LOAD_LOADING, ACTIVITY_LOAD_LOADED, ACTIVITY_LOAD_FAILED} from '../actions/ActivityActions.jsx';

const defaultState = {
    activity: undefined,
    isActivityLoading: true,
    isActivityLoaded: false,
    error: undefined
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
                isActivityLoading: false,
                isActivityLoaded: true
            };
        case ACTIVITY_LOAD_FAILED:
            return {...state, error: action.payload.error}
        default:
            return state;
    }
}
