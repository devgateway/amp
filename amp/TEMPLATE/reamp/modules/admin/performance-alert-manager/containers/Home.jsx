import React, {
    Component,
    PropTypes
} from 'react';
import {
    connect
} from 'react-redux';
import {
    bindActionCreators
} from 'redux';
require('../styles/less/main.less');
import * as startUp from '../actions/StartUpAction';
export default class App extends Component {
    constructor(props, context) {
        super(props, context);        
    }

    componentWillMount() {}

    render() {
        return (
            <div>
             <h1>Hello World!</h1>
            </div>

        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators({}, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);