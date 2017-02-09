export const STATE_TYPES_LOADING = 'STATE_TYPES_LOADING';
export const STATE_TYPES_LOADED = 'STATE_TYPES_LOADED';

export function loadAvailableTypes(param) {
    return (dispatch, ownProps) => {
        dispatch(_sendingRequest());

        setTimeout(dispatch(_loadAvailableTypes()), 5000);

    }

    console.log('loadAvailableTypes invoked ' + param);
    return {
        type: STATE_TYPES_LOADED
    };
}
function _loadAvailableTypes(params) {
    console.log('sending request loadAvailableTypes');
    return {
        type: STATE_TYPES_LOADED,
        actionData: {
            options: [
                { name: 'Foo', value: 0 },
                { name: 'Bar', value: 1 },
                { name: 'Baz', value: 2, disabled: true },
                { name: 'Qux', value: 3 },
                { name: 'Quux', value: 4 },
                { name: 'Corge', value: 5 },
                { name: 'Grault', value: 6 },
                { name: 'Garply', value: 7 },
                { name: 'Waldo', value: 8 },
                { name: 'Fred', value: 9 },
                { name: 'Plugh', value: 10 },
                { name: 'Xyzzy', value: 11 },
                { name: 'Thud', value: 12 }
            ],
            value: [0, 3, 9]
        }
    }
}

