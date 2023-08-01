import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PerformanceRuleList from '../components/PerformanceRuleList.jsx';

require('../styles/less/main.less');

class App extends Component {
    constructor(props, context) {
        super(props, context);
    }

    componentWillMount() {}

    render() {
        return (
            <div className="container">
              <h2>{this.props.translations['amp.performance-rule:performance-rules']}</h2>
               <PerformanceRuleList/>
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
