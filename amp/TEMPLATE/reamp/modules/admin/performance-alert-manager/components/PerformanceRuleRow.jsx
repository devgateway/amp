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
import * as commonListsActions from '../actions/CommonListsActions'
import * as performanceRuleActions from '../actions/PerformanceRuleActions';;
export default class PerformanceRuleRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};    
        this.edit = this.edit.bind(this);
        this.deletePerformanceRule = this.deletePerformanceRule.bind(this);
    }

    componentWillMount() {        
    }
    
    edit() {
        this.props.actions.editPerformanceRule(this.props.performanceRule);
    }
    
    deletePerformanceRule() {
        if (confirm(this.props.translations['amp.performance-rule:delete-prompt'])) {
           this.props.actions.deletePerformanceRule(this.props.performanceRule); 
        }        
    }
    
    render() {
        return (
            <tr>
                <td>{this.props.performanceRule.name}</td>
                <td>{this.props.performanceRule.typeClassName}</td>
                <td>{this.props.performanceRule.level.value}</td>
                <td>
                <span className="glyphicon glyphicon-custom glyphicon-pencil" onClick={this.edit}></span> <span className="glyphicon glyphicon-custom glyphicon-trash" onClick={this.deletePerformanceRule}></span>
                </td>
            </tr>
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
        actions: bindActionCreators(Object.assign({}, performanceRuleActions, commonListsActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceRuleRow);