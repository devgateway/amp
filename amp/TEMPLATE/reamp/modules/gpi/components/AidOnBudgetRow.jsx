import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import { FormControl } from 'react-bootstrap';
import { Popover } from 'react-bootstrap';
import { OverlayTrigger } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
import DatePicker from 'react-date-picker';
import moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
require('../styles/main.less');
import {Typeahead} from 'react-bootstrap-typeahead'; 
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/constants.jsx';
export default class AidOnBudgetRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                aidOnBudget: this.props.aidOnBudget,                                
                showDatePicker:false 
        };
        this.toggleEdit = this.toggleEdit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.save = this.save.bind(this);
        this.toggleDatePicker =  this.toggleDatePicker.bind(this);
        this.onDateChange = this.onDateChange.bind(this);  
        this.deleteAidOnBudget = this.deleteAidOnBudget.bind(this); 
        this.getErrorsForField = this.getErrorsForField.bind(this);
        this.onOrgChange = this.onOrgChange.bind(this);
    }
    
    toggleEdit() {
        const aidOnBudget = this.state.aidOnBudget;
        aidOnBudget.isEditing = true;
        this.setState({aidOnBudget: aidOnBudget});
        this.props.actions.updateAidOnBudget(aidOnBudget);        
    }
    
    toggleDatePicker(){
        this.setState({showDatePicker: !this.state.showDatePicker});
    }
    
    onChange(event){
        const errors = [];
        const field = event.target.name;
        const value = event.target.value;        
        this.onValueChange(field, value);   
    }    
    
    onValueChange(field, value) {
        const aidOnBudget = this.state.aidOnBudget;   
        if(field === 'donorId'){
            aidOnBudget[field] = parseInt(value);
        }else{
            aidOnBudget[field] = value;   
        }
        
        this.setState({aidOnBudget: aidOnBudget});
        this.props.actions.updateAidOnBudget(aidOnBudget);
    }
    
    onDateChange(date){
        if(date){
            const aidOnBudget = this.state.aidOnBudget;                               
            const formatedDate = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
            aidOnBudget['indicatorDate'] = formatedDate;
            this.setState({aidOnBudget: aidOnBudget});
            this.props.actions.updateAidOnBudget(aidOnBudget); 
            this.toggleDatePicker(); 
        }        
    }
    
    getDisplayDateFormat() {
        return (this.props.settings && this.props.settings[Constants.DATE_FORMAT_SETTING]) ? this.props.settings[Constants.DATE_FORMAT_SETTING].toUpperCase() : Constants.DEFAULT_UI_DATE_FORMAT;  
    }
    
    toDisplayDateFormat(date) {
        var result;
        if(date) {
            result = moment(date, Constants.EP_DATE_FORMAT).format(this.getDisplayDateFormat());           
        }  
        
        return result        
    }
    
    save() {
        this.props.actions.save(this.state.aidOnBudget);                
    }
    
    deleteAidOnBudget() {
        if(confirm(this.props.translations['amp.gpi-data:delete-prompt'])){
            this.props.actions.deleteAidOnBudget(this.state.aidOnBudget); 
        }        
    }
    
    getOrgName(id) {
        var org = this.props.orgList.filter(org => org.id === id)[0];
        return org ? org.name : '';
    }
    
    getCurrencyName(currencyCode) {
        var currency = this.props.currencyList.filter(currency => currency.id === currencyCode)[0];
        return currency ? currency.name : '';
    }
    
    getErrorsForField(field){
        var errors = this.props.errors.filter(error => {return ((error.id && error.id === this.state.aidOnBudget.id) || (error.cid && error.cid === this.state.aidOnBudget.cid) && error.affectedFields && error.affectedFields.includes(field) )})
        return  errors; 
    }
    
    onOrgChange(selected){        
        if (selected.length > 0) {           
           this.onValueChange('donorId', selected[0].id); 
        } else {
            this.onValueChange('donorId', null);
        }   
    }
    
    render() {        
        if (this.props.aidOnBudget.isEditing) {           
            return ( <tr>                    
                    <td scope="row" >                                   
                    <div className={this.getErrorsForField('indicatorDate').length > 0 ? 'form-group date-container has-error' : 'form-group date-container' }>
                    <span className="date-input-container"><input type="text" value={this.toDisplayDateFormat(this.state.aidOnBudget.indicatorDate)} readOnly className="date-input form-control" />    
                    </span><span className = "datepicker-toggle glyphicon glyphicon-calendar " onClick={this.toggleDatePicker}> </span></div>
                    <div className="datepicker-container"> 
                    {this.state.showDatePicker &&
                        <DatePicker 
                        hideFooter={true}
                        ref="date" 
                        locale={'en'} 
                        date={this.toDisplayDateFormat(this.state.aidOnBudget.indicatorDate)} 
                        onChange={this.onDateChange} 
                        expanded={false}
                        dateFormat={this.getDisplayDateFormat()}
                        />  
                    }
                    </div>
                    </td>
                    <td>
                    <div className={this.getErrorsForField('donorId').length > 0 ? 'form-group has-error' : 'form-group' }>                    
                    <Typeahead
                    bodyContainer={false}
                    labelKey="name"
                    options={this.props.orgList}
                    placeholder={this.props.translations['amp.gpi-data:select-donor']}
                    onChange={this.onOrgChange} 
                    selected={this.props.orgList.filter(org => {return org.id === this.state.aidOnBudget.donorId})}
                    clearButton={true}
                    />                    
                    </div>
                    </td>
                    <td>
                    <div className={this.getErrorsForField('amount').length > 0 ? 'form-group has-error' : 'form-group' }>
                    <input type="text" name="amount" className="form-control" placeholder="" value={this.state.aidOnBudget.amount} onChange={this.onChange} />
                    </div>
                    </td>
                    <td>
                    <div className={this.getErrorsForField('currencyCode').length > 0 ? 'form-group has-error' : 'form-group' }>                    
                    <select name="currencyCode" value={this.state.aidOnBudget.currencyCode} className="form-control" onChange={this.onChange}>
                    <option value="">{this.props.translations['amp.gpi-data:select-currency']}</option>
                    {this.props.currencyList.map(currency => 
                    <option value={currency.id} key={currency.id} >{currency.name}</option>
                    )}
                    </select>
                    </div>                    
                    </td>
                    <td> <span className="glyphicon glyphicon-ok-circle success-color" onClick={this.save}> </span><span className="glyphicon glyphicon-remove" onClick={this.deleteAidOnBudget}></span></td>                      
            </tr>)
            
            }
            
            return (
                    <tr>                
                    <th scope="row">{this.toDisplayDateFormat(this.state.aidOnBudget.indicatorDate)}</th>
                    <td>{this.getOrgName(this.state.aidOnBudget.donorId)}</td>
                    <td>{this.state.aidOnBudget.amount}</td>
                    <td>{this.getCurrencyName(this.state.aidOnBudget.currencyCode)} </td>
                    <td><span className="glyphicon glyphicon-pencil" onClick={this.toggleEdit}></span> <span className="glyphicon glyphicon-remove" onClick={this.deleteAidOnBudget}></span></td>                
                    </tr>
                    
            );
        }
    }
    
    function mapStateToProps(state, ownProps) {     
        return {
            translations: state.startUp.translations     
        }
    }
    
    function mapDispatchToProps(dispatch) {
        return {actions: bindActionCreators(aidOnBudgetActions, dispatch)}
    }
    
    export default connect(mapStateToProps, mapDispatchToProps)(AidOnBudgetRow);
