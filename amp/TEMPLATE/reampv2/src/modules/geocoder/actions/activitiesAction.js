import {fetchApiData} from "../../../utils/apiOperations";
import {
    API_REPORTS_URL,
    FIELD_ACTIVITY_DATE, FIELD_LOCATION, FIELD_PROJECT_DESCRIPTION,
    FIELD_PROJECT_NUMBER,
    FIELD_PROJECT_TITLE
} from "../utils/geocoder-constants";

export const FETCH_ACTIVITIES_PENDING = 'FETCH_ACTIVITIES_PENDING';
export const FETCH_ACTIVITIES_SUCCESS = 'FETCH_ACTIVITIES_SUCCESS';
export const FETCH_ACTIVITIES_ERROR = 'FETCH_ACTIVITIES_ERROR';

export const SELECT_ACTIVITY_FOR_GEOCODING = 'SELECT_ACTIVITY_FOR_GEOCODING';

export function fetchActivitiesPending() {
    return {
        type: FETCH_ACTIVITIES_PENDING
    }
}

export function fetchActivitiesSuccess(activities) {
    function extractActivity(activity) {
        return {
            id: activity.contents['[' + FIELD_PROJECT_TITLE + ']'].entityId,
            col1: activity.contents['[' + FIELD_ACTIVITY_DATE + ']'] ? activity.contents['[' + FIELD_ACTIVITY_DATE + ']'].displayedValue : "",
            col2: activity.contents['[' + FIELD_PROJECT_NUMBER + ']'] ? activity.contents['[' + FIELD_PROJECT_NUMBER + ']'].displayedValue : "",
            col3: activity.contents['[' + FIELD_PROJECT_TITLE + ']'].displayedValue,
            col4: activity.contents['[' + FIELD_LOCATION + ']'].displayedValue ? activity.contents['[' + FIELD_LOCATION + ']'].displayedValue : "---"
        };
    }

    return {
        type: FETCH_ACTIVITIES_SUCCESS,
        payload: activities.page.pageArea.children.map(extractActivity)
    }
}

export function fetchActivitiesError(error) {
    return {
        type: FETCH_ACTIVITIES_ERROR,
        error: error
    }
}

export function selectActivityForGeocoding(currentSelectedActivities, isSelected, activityId) {
    let selectedActivities = Array.from(currentSelectedActivities);

    if (isSelected) {
        selectedActivities.push(activityId);
    } else {
        selectedActivities = selectedActivities.filter(id => id !== activityId);
    }

    return {
        type: SELECT_ACTIVITY_FOR_GEOCODING,
        payload: selectedActivities
    }
}

export function selectAllActivitiesForGeocoding(currentSelectedActivities, isSelected, activityIds) {
    let selectedActivities = Array.from(currentSelectedActivities);

    if (isSelected) {
        activityIds.forEach(activityId => selectedActivities.push(activityId));
    } else {
        selectedActivities = selectedActivities.filter(id => !activityIds.includes(id));
    }

    return {
        type: SELECT_ACTIVITY_FOR_GEOCODING,
        payload: selectedActivities
    }
}

export const loadActivities = () => {
    let queryModel = {
        name: "Geocoding",
        add_columns : [
            FIELD_PROJECT_TITLE,
            FIELD_ACTIVITY_DATE,
            FIELD_PROJECT_NUMBER,
            FIELD_LOCATION,
            FIELD_PROJECT_DESCRIPTION]
    }

    return dispatch => {
        dispatch(fetchActivitiesPending());
        return fetchApiData({url: API_REPORTS_URL, body: queryModel})
            .then(activities => {
                return dispatch(fetchActivitiesSuccess(activities));
            })
            .catch(error => {
                console.error(error);
                return dispatch(fetchActivitiesError(error))
            });
    }
};