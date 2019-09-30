import activityJson from '../jsons/activity.json';
import ActivityApi from '../api/ActivityApi.jsx';
import { FM_ROOT } from '../common/Constants.jsx';
import {FieldsManager, FieldPathConstants, ActivityConstants, FeatureManagerConstants, FeatureManager,
    FmManagerHelper} from "amp-ui";
import processPossibleValues from '../common/PossibleValuesHelper.jsx';
import Logger from "amp/modules/activity/preview/tempUtils/LoggerManager";
export const ACTIVITY_LOAD_LOADING = 'ACTIVITY_LOAD_LOADING';
export const ACTIVITY_LOAD_LOADED = 'ACTIVITY_LOAD_LOADED';
export const ACTIVITY_LOAD_FAILED = 'ACTIVITY_LOAD_FAILED';

export function loadActivityForActivityPreview(activityId) {
    return (dispatch, ownProps) => {
        dispatch(sendingRequest());
        const paths = [...FieldPathConstants.ADJUSTMENT_TYPE_PATHS, ActivityConstants.CREATED_BY, ActivityConstants.TEAM,
            ActivityConstants.MODIFIED_BY];
        Promise.all([ActivityApi.getActivity(activityId), ActivityApi.getFieldsDefinition(),
            ActivityApi.fetchFmConfiguration(FmManagerHelper.getRequestFmSyncUpBody(Object.values(FeatureManagerConstants)))]
        ).then(([activity, fieldsDef, fmTree] ) => {
            //TODO find a better way to filter out non enabled paths
            const activityFieldsManagerTemp = new FieldsManager(fieldsDef, [], 'en', Logger);
            const enabledPaths = paths.filter(path => activityFieldsManagerTemp.isFieldPathEnabled(path));
            ActivityApi.fetchPossibleValues(enabledPaths).then(possibleValuesCollectionAPI=>{
                const activityFieldsManager = new FieldsManager(fieldsDef, processPossibleValues(possibleValuesCollectionAPI), 'en', Logger);
                _populateFMTree(fmTree);
                return dispatch({
                    type: ACTIVITY_LOAD_LOADED,
                    payload: {
                        activity: activityJson,
                        activityFieldsManager
                    }
                })
            })
        }).catch(error=>{
            return dispatch({
                type: ACTIVITY_LOAD_FAILED,
                payload: {
                    error:error
                }
            })
        })
    }

    function sendingRequest() {
        return {
            type: ACTIVITY_LOAD_LOADING
        };
    }

    function _populateFMTree(fmTree){
        FeatureManager.setFMTree(fmTree[FM_ROOT]);
        FeatureManager.setLoggerManager(Logger);
    }

}

