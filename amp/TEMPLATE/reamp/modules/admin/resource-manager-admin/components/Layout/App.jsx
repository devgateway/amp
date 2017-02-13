// @flow
import React, { Component, PropTypes } from 'react';
//import styles from './App.css';


export default class App extends Component {

    constructor(props, context) {
        // This is a bit anti-pattern but still valid when you need Redux store
        // without having to connect the component (https://github.com/reactjs/react-redux/issues/108).
        // NOT for being used everywhere.
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
