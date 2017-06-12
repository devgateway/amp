// @flow
import React, { Component, PropTypes } from 'react';
import styles from './App.less';


export default class App extends Component {

    constructor(props, context) {
        super(props, context);
    }
    /*

        static propTypes = {
        children: PropTypes.element.isRequired
    };

    static contextTypes = {
        store: React.PropTypes.object.isRequired
    };
*/
    render() {
        console.log('render');
        return (
            <div >
                {this.props.children}
            </div>
        );
    }
}
