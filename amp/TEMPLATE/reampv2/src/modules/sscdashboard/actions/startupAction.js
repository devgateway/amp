export const startupAction = () => dispatch => {
    debugger;
    dispatch({
        type: 'SIMPLE_ACTION',
        payload: 'result_of_simple_action'
    })
};
