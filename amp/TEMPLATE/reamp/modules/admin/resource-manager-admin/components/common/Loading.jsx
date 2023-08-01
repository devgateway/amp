// @flow
import React, {Component} from 'react';

export default class Loading extends Component {

    constructor() {
        super();
        console.log('constructor');
    }

    render() {
        console.log('render');
        return (
            <div>
                <span>Loading...</span>
            </div>
        );
    }
}
