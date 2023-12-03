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

import * as Constants from '../common/Constants';
import Utils from '../common/Utils'
require('../styles/less/main.less');
import * as performanceRuleActions from '../actions/PerformanceRuleActions';

class ToolBar extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};
        this.addNew = this.addNew.bind(this);
    }

    componentWillMount() {
    }

    addNew() {
        this.props.actions.clearMessages();
        this.props.actions.addNewPerformanceRule();
    }

    render() {
        return (
            <div>
                <div className="panel panel-default">
                <div className="panel-body custom-panel">
                <span className="glyphicon glyphicon-plus" onClick={this.addNew}></span>
                <span  onClick={this.addNew} className="add-new-text">{ Utils.capitalizeFirst(this.props.translations['amp.performance-rule:add-new']) } </span>
                <span className="insert-data-text">{this.props.translations['amp.performance-rule:insert-data']}</span>
                <span> / </span><span className="required-fields">{this.props.translations['amp.performance-rule:required-fields']}</span>
                </div>
                </div>
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
        actions: bindActionCreators(Object.assign({}, performanceRuleActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ToolBar);
