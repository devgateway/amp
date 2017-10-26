export const STATE_LOADING_OK = 'STATE_LOADING_OK';

export function loadAction() {
    console.log('loadAction');
    return (dispatch) => {
        return {
            type: STATE_LOADING_OK
        };
    };
}