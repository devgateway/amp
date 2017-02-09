// @flow
import { STATE_TYPES_LOADED, STATE_TYPES_LOADING } from '../actions/TypeListAction.jsx';

const defaultState = {
    loading: true,
    typeList: [],
};

export default function typeList(state: Object = defaultState, action: Object) {
    console.log('typeList reducer');
    switch (action.type) {
        case STATE_TYPES_LOADING:
            return Object.assign({}, state, {
                loggedIn: true
            });
        case STATE_TYPES_LOADED:
            return Object.assign({}, state, {
                    loggedIn: false,
                    typeList: action.actionData.typeList,
                }
            )
                ;

            break;
        default:
            return state;
    }
}
