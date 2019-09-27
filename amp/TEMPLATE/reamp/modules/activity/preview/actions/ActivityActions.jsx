import activityJson from '../jsons/activity.json';
import ActivityApi from '../api/ActivityApi.jsx';
import { FM_ROOT } from '../common/Constants.jsx';
import {FieldsManager, FieldPathConstants, ActivityConstants, FeatureManagerConstants, FeatureManager} from "amp-ui";
//TODO move this helper to AMP-UI since we have DUPLICATE code with AMP-ONLINE
//TODO Create an abstract class and make them both inherit the abstract class
import processPossibleValues from '../common/PossibleValuesHelper.jsx';
import Logger from "amp/modules/activity/preview/tempUtils/LoggerManager";
import fmTree from "amp/modules/activity/preview/jsons/fmTree.json";

export const ACTIVITY_LOAD_LOADING = 'ACTIVITY_LOAD_LOADING';
export const ACTIVITY_LOAD_LOADED = 'ACTIVITY_LOAD_LOADED';
export const ACTIVITY_LOAD_FAILED = 'ACTIVITY_LOAD_FAILED';

export function loadActivityForActivityPreview(activityId) {
    return (dispatch, ownProps) => {
        dispatch(sendingRequest());
        const paths = [...FieldPathConstants.ADJUSTMENT_TYPE_PATHS, ActivityConstants.CREATED_BY, ActivityConstants.TEAM,
            ActivityConstants.MODIFIED_BY];

        Promise.all([ActivityApi.getActivity(activityId), ActivityApi.getFieldsDefinition(),
            ActivityApi.fetchFmConfiguration(Object.values(FeatureManagerConstants))]
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

