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
export default class PerformanceRuleForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                currentPerformanceRule: this.props.currentPerformanceRule
        };    
        this.close = this.close.bind(this);
        this.onSave = this.onSave.bind(this);
        this.onInputChange = this.onInputChange.bind(this);
        this.updateAttribute = this.updateAttribute.bind(this);
        this.onEnabledChange = this.onEnabledChange.bind(this);
        this.getAttributeValue = this.getAttributeValue.bind(this);
    }

    componentWillMount() {        
        if(this.props.currentPerformanceRule.typeClassName) {
            this.props.actions.getAttributeList(this.props.currentPerformanceRule.typeClassName);
        }
    }
    
    close() {
        this.props.actions.closePerformanceRule();
    }
    
    onInputChange(event) {
       const field = event.target.name;
       const currentPerformanceRule = this.state.currentPerformanceRule;
       if(event.target.name.indexOf('attribute_') !== -1){
           this.updateAttribute(event, currentPerformanceRule);
       } else {
           if(field === 'level'){
               currentPerformanceRule[field] = {};
               currentPerformanceRule[field].id = event.target.value;
           }else{
               currentPerformanceRule[field] = event.target.value;
           }                      
           if(field === 'typeClassName') {
               currentPerformanceRule['attributes'] = [];
               this.props.actions.getAttributeList(event.target.value);
           } 
           this.setState({currentPerformanceRule: currentPerformanceRule});
       }      
    }
    
    updateAttribute(event, currentPerformanceRule) {
        let attributes = currentPerformanceRule['attributes'] || [];    
        let attribute = attributes.filter(attr => attr.name === event.target.getAttribute('data-name'))[0] || {};
        attribute.name = event.target.getAttribute('data-name');
        attribute.type = event.target.getAttribute('data-type');
        attribute.value = event.target.value;            
        if(attributes.filter(attr => attr.name === event.target.getAttribute('data-name')).length > 0){
            attributes = attributes.map(function(attr) { 
                return (attr.name === attribute.name) ? attribute : attr;
            });
        } else {
            attributes.push(attribute);   
        }                              
        currentPerformanceRule.attributes = attributes;
        this.setState({currentPerformanceRule: currentPerformanceRule});
    }
    
    onEnabledChange(event) {
        const currentPerformanceRule = this.state.currentPerformanceRule;        
        currentPerformanceRule.enabled = event.target.checked;
        this.setState({currentPerformanceRule: currentPerformanceRule});
    }
    
    onSave() {
        this.props.actions.savePerformanceRule(this.state.currentPerformanceRule);  
    }
    
    getAttributeValue(name) {
        const currentPerformanceRule = this.state.currentPerformanceRule;
        let result = '';
        if(currentPerformanceRule.attributes) {
            const attribute = currentPerformanceRule.attributes.filter(attr => attr.name === name)[0];
            result = attribute ? attribute.value : ''; 
        }
        return result;      
    }
    
    render() {         
       console.log(this.state.currentPerformanceRule);
        return (
                <div className="panel panel-default">
                <div className="panel-heading">{this.state.currentPerformanceRule.id ? this.props.translations['amp.performance-rule:heading-edit'] : this.props.translations['amp.performance-rule:heading-new']}</div>
                <div className="panel-body custom-panel">

                    <table className="container-fluid data-selection-fields">  
                      <tbody>
                        <tr>
                            <td className="col-md-6">                        
                                <span className="required">*</span>{this.props.translations['amp.performance-rule:type']}
                                <select className="form-control" name="typeClassName" value={this.state.currentPerformanceRule.typeClassName} onChange={this.onInputChange}>
                                    <option>{this.props.translations['amp.performance-rule:select-type']} </option>
                                    {this.props.typeList && this.props.typeList.map(ruleType => 
                                    <option value={ruleType.name} key={ruleType.name} >{ruleType.description}</option>
                                    )}
                                </select>
                            </td>
                            <td className="col-md-6 rule-parameters-cell" rowSpan="4">
                           <div className="row"><label>{this.props.translations['amp.performance-rule:rule-parameters']}</label></div>                             
                            {this.props.attributeList && this.props.attributeList.map((attribute, i) =>
                                <div className="row" key={i}>
                                  <span>{attribute.description}</span>  
                                  {attribute.possibleValues && 
                                      <select name={"attribute_" + attribute.name} data-type={attribute.type} data-name={attribute.name} value={this.getAttributeValue(attribute.name)} onChange={this.onInputChange}>
                                      {attribute.possibleValues.map((possibleValue) =>
                                       <option name={"attribute_" + attribute.name} data-type={attribute.type} data-name={attribute.name}   >{possibleValue}</option>     
                                      )}
                                      </select>                                         
                                  }  
                                  {attribute.possibleValues == null &&
                                      <input type="text" className="form-control" name={"attribute_" + attribute.name}  value={this.getAttributeValue(attribute.name)}  onChange={this.onInputChange} data-type={attribute.type} data-name={attribute.name}/>
                                  }
                                </div>
                            )}                           
                            </td>
                        </tr>
                        <tr>
                            <td className="col-md-6">                        
                                <span className="required">*</span>{this.props.translations['amp.performance-rule:name']}
                                <input type="text" className="form-control" value={this.state.currentPerformanceRule.name} name="name" onChange={this.onInputChange}/>                        
                            </td>                           
                        </tr>
                        <tr>
                            <td className="col-md-6">                       
                                <span className="required">*</span>{this.props.translations['amp.performance-rule:level']}
                                <select className="form-control" name="level" value={this.state.currentPerformanceRule.level ? this.state.currentPerformanceRule.level.id : ''} onChange={this.onInputChange}>
                                    <option>{this.props.translations['amp.performance-rule:select-level']}</option>
                                    {this.props.levelList && this.props.levelList.map(level => 
                                    <option value={level.id} key={level.id} >{level.name}</option>
                                    )}
                                </select>
                            </td>         
                           </tr>
                                  <tr>
                                    <td className="col-md-6">                                       
                                          <input type="checkbox" checked={this.state.currentPerformanceRule.enabled} onChange={this.onEnabledChange}/>{this.props.translations['amp.performance-rule:enabled']}<label className="checkbox-inline"></label>                                        
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
        attributeList: state.performanceRule.attributeList
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, performanceRuleActions, commonListsActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceRuleForm);