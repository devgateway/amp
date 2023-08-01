import ResourceApi from '../api/ResourceApi.jsx';
import {FieldsManager} from 'amp-ui';
import LoggerManager from '../utils/LoggerManager';
import processPossibleValues from '../common/PossibleValuesHelper.jsx';
import { ResourceConstants, ActivityConstants } from 'amp-ui';

export const RESOURCES_LOAD_LOADING = 'RESOURCES_LOAD_LOADING';
export const RESOURCES_LOAD_LOADED = 'RESOURCES_LOAD_LOADED';
export const RESOURCES_LOAD_FAILED = 'RESOURCES_LOAD_FAILED';

export const loadResourcesForActivity = (activity) => (dispatch, ownProps) => {
    if (activity.hasOwnProperty(ActivityConstants.ACTIVITY_DOCUMENTS)
     && activity[ActivityConstants.ACTIVITY_DOCUMENTS] !== null) {
        const resourcesByUuids =
            new Set(activity[ActivityConstants.ACTIVITY_DOCUMENTS].map(r => r[ResourceConstants.UUID]));
        const resourcesByUUID = Array.from(resourcesByUuids);
        dispatch({
            type: RESOURCES_LOAD_LOADING
        })
        return loadHydratedResources(resourcesByUUID)(dispatch, ownProps);
    }
}
export const loadHydratedResources = (ids) => (dispatch, ownProps) => {
    const resourcesFields = [ResourceConstants.RESOURCE_TYPE, ResourceConstants.TYPE];
    const { settings } = ownProps().startUpReducer;
    Promise.all([ResourceApi.fetchResources(ids),
        ResourceApi.getResourcesEnabledFields(), ResourceApi.fetchPossibleValues(resourcesFields)])
        .then(([resourcesByUUIDRaw, rFields, possibleValuesCollection]) => {
            const resourcesByUUID = {};
            resourcesByUUIDRaw.forEach(r => {
                resourcesByUUID[r.uuid] = r;
                r.id = r.uuid;
            });
            const resourceFieldsManager = new FieldsManager(rFields, processPossibleValues(possibleValuesCollection),
                settings.language,LoggerManager);
            dispatch({
                type: RESOURCES_LOAD_LOADED,
                payload: {
                    resourcesByUUID,
                    resourceFieldsManager
                }
            });
        }).catch(error => {
        dispatch({
            type: RESOURCES_LOAD_FAILED,
            payload: {
                error
            }
        });
    });
}
