import {RESOURCES_LOAD_FAILED, RESOURCES_LOAD_LOADED, RESOURCES_LOAD_LOADING} from '../actions/ResourceAction';

const defaultState = {
    isResourceLoading: false,
    resourcesByUuids: {},
    isResourcesLoaded: false,
    isResourceManagersLoaded: false,
    resourceFieldsManager: undefined,
    errors: []
};
export default function resourceReducer(state: Object = defaultState, action: Object) {
    switch (action.type) {
        case RESOURCES_LOAD_LOADING:
            return {...state, isResourceLoading: true};
        case RESOURCES_LOAD_LOADED:
            return {
                ...state, isResourceLoading: false,
                resourcesByUuids: action.payload.resourcesByUUID,
                resourceFieldsManager: action.payload.resourceFieldsManager,
                isResourcesLoaded: true,
                isResourceManagersLoaded: true
            };
        case RESOURCES_LOAD_FAILED:
            return {
                ...state, isResourceLoading: false,
                isResourcesLoaded: false,
                isResourceManagersLoaded: false,
                errors: action.payload.error
            };
            break;
        default:
            return state;
    }
}
