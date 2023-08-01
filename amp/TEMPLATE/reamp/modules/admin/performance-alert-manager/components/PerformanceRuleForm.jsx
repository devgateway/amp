import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as performanceRuleActions from '../actions/PerformanceRuleActions';
import * as Constants from '../common/Constants';
import Utils from '../common/Utils';

require('../styles/less/main.less');

class PerformanceRuleForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};
        this.close = this.close.bind(this);
        this.onSave = this.onSave.bind(this);
        this.onInputChange = this.onInputChange.bind(this);
        this.updateAttribute = this.updateAttribute.bind(this);
        this.onEnabledChange = this.onEnabledChange.bind(this);
        this.getAttributeValue = this.getAttributeValue.bind(this);
        this.getMessage = this.getMessage.bind(this);
    }

    onInputChange(event) {
       const field = event.target.name;
       const currentPerformanceRule = Object.assign({}, this.props.currentPerformanceRule);
       if(event.target.name.indexOf('attribute_') !== -1){
           this.updateAttribute(event, currentPerformanceRule);
       } else {
           if(field === Constants.FIELD_LEVEL){
               currentPerformanceRule[field] = {};
               currentPerformanceRule[field].id = event.target.value;
           }else{
               currentPerformanceRule[field] = event.target.value;
           }
           if (field === Constants.FIELD_TYPE) {
               currentPerformanceRule[Constants.FIELD_ATTRIBUTES] = [];
               this.props.actions.getAttributeList(event.target.value);
           }
           this.props.actions.updatePerformanceRule(currentPerformanceRule);
       }
    }

    updateAttribute(event, currentPerformanceRule) {
        let attributes = currentPerformanceRule[Constants.FIELD_ATTRIBUTES] || [];
        let attribute = attributes.filter(attr => attr.name === event.target.getAttribute('data-name'))[0] || {};
        attribute.name = event.target.getAttribute('data-name');
        attribute.type = event.target.getAttribute('data-type');
        attribute.value = event.target.value;

        if (attribute.type === Constants.FIELD_TYPE_AMOUNT && attribute.value && (!Utils.isNumber(attribute.value) || attribute.value <= 0 || attribute.value > Number.MAX_SAFE_INTEGER)) return;

        if(attributes.filter(attr => attr.name === event.target.getAttribute('data-name')).length > 0){
            attributes = attributes.map(function(attr) {
                return (attr.name === attribute.name) ? attribute : attr;
            });
        } else {
            attributes.push(attribute);
        }

        currentPerformanceRule.attributes = attributes;
        this.props.actions.updatePerformanceRule(currentPerformanceRule);
    }

    onEnabledChange(event) {
        const currentPerformanceRule = Object.assign({}, this.props.currentPerformanceRule);
        currentPerformanceRule.enabled = event.target.checked;
        this.props.actions.updatePerformanceRule(currentPerformanceRule);
    }

    onSave() {
        this.props.actions.clearMessages();
        this.props.actions.savePerformanceRule(this.props.currentPerformanceRule,this.props.attributeList).then(function(){
            this.props.actions.loadPerformanceRuleList({paging: this.props.paging});
        }.bind(this));
    }

    close() {
        this.props.actions.clearMessages();
        this.props.actions.closePerformanceRule();
    }

    getAttributeValue(name) {
        const currentPerformanceRule = this.props.currentPerformanceRule;
        let result = '';
        if(currentPerformanceRule.attributes) {
            const attribute = currentPerformanceRule.attributes.filter(attr => attr.name === name)[0];
            result = attribute ? attribute.value : '';
        }
        return result;
    }

    getErrorsForField(field) {
        const errors = this.props.errors.filter(error => {
            return (error.affectedFields && error.affectedFields.includes(field))
        })
        return errors;
    }

    getMessage(){
        let message;
        const currentRuleType = this.props.typeList.filter(ruleType => ruleType.name === this.props.currentPerformanceRule['type-class-name'])[0];
        if (currentRuleType) {
            message = currentRuleType[currentRuleType.name + Constants.TRANSLATED_MESSAGE]  || '';
            for (let attr of this.props.attributeList) {
                const attribute = this.props.currentPerformanceRule.attributes.filter(a => a.name === attr.name)[0] || {};
                let value = attribute.value;
                if (attr['possible-values'].length > 0) {
                    value = this.getTranslatedValue(attr, attribute.value);
                }
                message = value ? this.replaceAttrPlaceHolder(message, attr.name, value) : message;
            }
        }

        return message;
    }

    getTranslatedValue(attr, name) {
        let value;
        const possibleValue = attr['possible-values'].filter(possibleValue => possibleValue.name === name)[0];
        if (attr.name == Constants.ATTRIBUTE_TIME_UNIT) {
            value = possibleValue ? this.props.translations['amp.performance-rule:time-unit-' + possibleValue.name] : possibleValue;
        } else {
            value = possibleValue ? possibleValue['translated-label'] : possibleValue;
        }

        return value;
    }

    replaceAttrPlaceHolder(message, attrName, value) {
        return message.replace(Constants.PLACEHOLDER_START + attrName + Constants.PLACEHOLDER_END, value);
    }

    render() {

        const message = this.getMessage();
        return (
                <div className="panel panel-default">
                <div className="panel-heading">{this.props.currentPerformanceRule.id ? this.props.translations['amp.performance-rule:heading-edit'] : this.props.translations['amp.performance-rule:heading-new']}</div>
                <div className="panel-body">

                    <table className="container-fluid data-selection-fields">
                      <tbody>
                        <tr>
                            <td className={this.getErrorsForField('type-class-name').length > 0 ? 'col-md-6 has-error': 'col-md-6'} >
                                <span className="required">*&nbsp;</span>{this.props.translations['amp.performance-rule:type']}
                                <select className="form-control performance-input" name="type-class-name" value={this.props.currentPerformanceRule['type-class-name'] ? this.props.currentPerformanceRule['type-class-name'] : '' } onChange={this.onInputChange}>
                                    <option value="">{this.props.translations['amp.performance-rule:select-type'].toLowerCase()} </option>
                                    {this.props.typeList && this.props.typeList.map(ruleType =>
                                    <option value={ruleType.name} key={ruleType.name} >{ruleType[ruleType.name + Constants.TRANSLATED_DESCRIPTION]}</option>
                                    )}
                                </select>
                            </td>
                            <td className="col-md-6 rule-parameters-cell" rowSpan="4">
                            <div className="row"><label>{this.props.translations['amp.performance-rule:rule-parameters']}</label></div>
                            {this.props.attributeList && this.props.attributeList.map((attribute, i) =>
                                <div className={this.getErrorsForField(attribute.name).length > 0 ? 'row has-error' : 'row'} key={i}>
                                <span className="required">*&nbsp;</span><span>{attribute[attribute.name + Constants.TRANSLATED_DESCRIPTION]}</span>
                                  {attribute['possible-values'] && attribute['possible-values'].length > 0 &&
                                      <select className="form-control performance-input" name={"attribute_" + attribute.name} data-type={attribute.type} data-name={attribute.name} value={this.getAttributeValue(attribute.name)} onChange={this.onInputChange}>
                                      <option value="">{this.props.translations['amp.performance-rule:select']}</option>
                                      {attribute['possible-values'].filter(possibleValue => possibleValue.visible == true).map((possibleValue) =>
                                            <option name={"attribute_" + attribute.name} data-type={attribute.type} data-name={attribute.name}  value={possibleValue.name} >{possibleValue['translated-label']}</option>
                                      )}
                                      </select>
                                  }
                                  {attribute['possible-values'] == null || attribute['possible-values'].length == 0 &&
                                      <input type="text" className="form-control performance-input" name={"attribute_" + attribute.name}  value={this.getAttributeValue(attribute.name)}  onChange={this.onInputChange} data-type={attribute.type} data-name={attribute.name}/>
                                  }
                                  <br/>
                                </div>
                            )}
                           {message &&
                             <div className="alert alert-info" role="alert">{message}</div>
                           }
                           </td>
                        </tr>
                        <tr>
                            <td className={this.getErrorsForField('name').length > 0 ? 'col-md-6 has-error': 'col-md-6'}>
                                <span className="required">*&nbsp;</span>{this.props.translations['amp.performance-rule:name']}
                                <input type="text" className="form-control performance-input" value={this.props.currentPerformanceRule.name ? this.props.currentPerformanceRule.name : '' } name="name" onChange={this.onInputChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td className={this.getErrorsForField('id').length > 0 ? 'col-md-6 has-error': 'col-md-6'}>
                                <span className="required">*&nbsp;</span>{this.props.translations['amp.performance-rule:level']}
                                <select className="form-control performance-input" name="level" value={this.props.currentPerformanceRule.level ? this.props.currentPerformanceRule.level.id : ''} onChange={this.onInputChange}>
                                    <option>{this.props.translations['amp.performance-rule:select-level']}</option>
                                    {this.props.levelList && this.props.levelList.map(level =>
                                    <option value={level.id} key={level.id} >{level.label}</option>
                                    )}
                                </select>
                            </td>
                           </tr>
                                  <tr>
                                    <td className="col-md-6">
                                    <div className="checkbox">
                                    <label>
                                    <input type="checkbox" checked={this.props.currentPerformanceRule.enabled != null ? this.props.currentPerformanceRule.enabled : false } onChange={this.onEnabledChange}/> {this.props.translations['amp.performance-rule:enabled']}
                                    </label>
                                    </div>
                                    </td>
                                </tr>
                       </tbody>
                    </table>
                    <div className="btn-toolbar">
                        <button className="btn btn-success pull-right" onClick={this.onSave}>{this.props.translations['amp.performance-rule:save-button']}</button>
                        <button className="btn btn-warning pull-right" onClick={this.close}>{this.props.translations['amp.performance-rule:close-button']}</button>
                    </div>
                </div>
            </div>
        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        attributeList: state.performanceRule.attributeList,
        paging: state.performanceRule.paging
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, performanceRuleActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceRuleForm);
