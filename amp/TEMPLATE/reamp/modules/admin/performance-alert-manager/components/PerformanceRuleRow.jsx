import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as performanceRuleActions from '../actions/PerformanceRuleActions';
import * as Constants from '../common/Constants';

require('../styles/less/main.less');

class PerformanceRuleRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};
        this.edit = this.edit.bind(this);
        this.deletePerformanceRule = this.deletePerformanceRule.bind(this);
    }

    componentWillMount() {
    }

    edit() {
        this.props.actions.clearMessages();
        this.props.actions.editPerformanceRule(this.props.performanceRule);
        if(this.props.performanceRule['type-class-name']) {
            this.props.actions.getAttributeList(this.props.performanceRule['type-class-name']);
        }
        this.props.focusOnForm();
    }

    deletePerformanceRule() {
        this.props.actions.clearMessages();
        if (confirm(this.props.translations['amp.performance-rule:delete-prompt'])) {
           this.props.actions.deletePerformanceRule(this.props.performanceRule).then(function(){
               this.props.actions.loadPerformanceRuleList({paging: this.props.paging});
           }.bind(this));
        }
    }

    getTypeDescription(name) {
        const ruleType = this.props.typeList.filter(ruleType => ruleType.name === name)[0];
        return ruleType ? ruleType[ruleType.name + Constants.TRANSLATED_DESCRIPTION] : '';
    }

    getLevelName(id) {
        const level = this.props.levelList.filter(level => level.id === id)[0];
        return level.label;
    }

    render() {
        return (
            <tr>
                <td>{this.props.performanceRule.name}</td>
                <td>{this.getTypeDescription(this.props.performanceRule['type-class-name'])}</td>
                <td>{this.getLevelName(this.props.performanceRule.level.id)}</td>
                <td>{this.props.performanceRule.enabled ? this.props.translations['amp.performance-rule:enabled-yes'] : this.props.translations['amp.performance-rule:enabled-no']}</td>
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
        translate: state.startUp.translate,
        typeList: state.performanceRule.typeList,
        levelList: state.performanceRule.levelList,
        paging: state.performanceRule.paging
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, performanceRuleActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceRuleRow);
