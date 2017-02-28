import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import { FormControl } from 'react-bootstrap';
import DatePicker from 'react-date-picker';
import moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
require('../styles/aid-on-budget.less');
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
export default class AidOnBudgetRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                aidOnBudget: this.props.aidOnBudget,          
                saving: false,
                isEditing: this.props.isEditing,
                showDatePicker:false,
                displayDateFormat: 'DD/MMM/YYYY',
                endPointDateFormat: 'YYYY-MM-DD'
        };
        this.toggleEdit = this.toggleEdit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.save = this.save.bind(this);
        this.toggleDatePicker =  this.toggleDatePicker.bind(this);
        this.onDateChange = this.onDateChange.bind(this);  
        this.deleteAidOnBudget = this.deleteAidOnBudget.bind(this); 
    }
    toggleEdit() {
        this.setState({isEditing: true});
    }
    toggleDatePicker(){
        this.setState({showDatePicker: !this.state.showDatePicker});
    }
    onChange(event){
        const errors = [];
        const field = event.target.name;
        const value = event.target.value;        
        const aidOnBudget = this.state.aidOnBudget;        
        aidOnBudget[field] = event.target.value;
        this.setState({aidOnBudget: aidOnBudget});       
    }
    isValid() {
        const aidOnBudget = this.state.aidOnBudget;
        const errors = [];
        if (!this.isNumber(aidOnBudget['amount'] )){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-amount-invalid'})            
        } 
        
        if(this.isUndefinedOrBlank('donorId')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-donor-agency-required'})
        }
        
        if(this.isUndefinedOrBlank('currencyCode')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-currency-required'})
        }
        
        if(this.isUndefinedOrBlank('date')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-date-required'}) 
        }
               
        if(errors.length > 0){
            this.props.onError(errors);
        }
        
        return errors.length == 0;
    }
    isNumber(input) {
        return typeof(input) != "boolean" && !isNaN(input);
    }
    
    isUndefinedOrBlank(field) {
        var result = false;
        const aidOnBudget = this.state.aidOnBudget;
        if(aidOnBudget[field] === '' || !aidOnBudget[field]){
            result = true;
        }
        
        return result;        
    }
    
    onDateChange(date){
        if(date){
            const aidOnBudget = this.state.aidOnBudget; 
            const formartedDate = moment(date, this.state.displayDateFormat).format(this.state.endPointDateFormat);
            aidOnBudget['date'] = formartedDate;
            this.setState({aidOnBudget: aidOnBudget});
            this.toggleDatePicker(); 
        }        
    }
    
    toDateDisplayFormat(date) {
        var result;
        if(date) {
            result = moment(date, this.state.endPointDateFormat).format(this.state.displayDateFormat);           
        }       
        return result
        
    }
    
    save() {
        if (this.isValid()) {
            this.props.actions.save(this.state.aidOnBudget); 
        }       
    }
    deleteAidOnBudget() {
        if(confirm("This will delete the row. Do you want to proceed?")){
            this.props.actions.deleteAidOnBudget(this.state.aidOnBudget); 
        }        
    }
    getOrgName(id) {
        var org = this.props.orgList.filter(org => org.id === id)[0];
        return org ? org.name : '';
    }
    render() {     
        if (this.state.isEditing) {
            return ( <tr >
                    <td scope="row" >                                   
                    <div className="date-container">
                    <span className="date-input-container"><input type="text" value={this.toDateDisplayFormat(this.state.aidOnBudget.date)} readOnly className="date-input" />    
                    </span><span className = "datepicker-toggle glyphicon glyphicon-calendar " onClick={this.toggleDatePicker}> </span></div>
                    <div className="datepicker-container"> 
                    {this.state.showDatePicker &&
                        <DatePicker 
                        hideFooter={true}
                        ref="date" 
                        locale={'en'} 
                        date={this.toDateDisplayFormat(this.state.aidOnBudget.date)} 
                        onChange={this.onDateChange} 
                        expanded={false}
                        dateFormat={this.state.displayDateFormat}
                        />  
                    }
                    </div>
                    </td>
                    <td>
                    
                    <select name="donorId" className="form-control" value={this.state.aidOnBudget.donorId} onChange={this.onChange}>
                    <option value="">Select Donor</option>
                    {this.props.orgList.map(org => 
                    <option value={org.id}  key={org.id} >{org.name}</option>
                    )}
                    </select>
                    
                    </td>
                    <td><input type="text" name="amount" className="form-control" placeholder="" value={this.state.aidOnBudget.amount} onChange={this.onChange} /></td>
                    <td><select name="currencyCode" value={this.state.aidOnBudget.currencyCode} className="form-control" onChange={this.onChange}>
                    <option value="">Select Currency</option>
                    {this.props.currencyList.map(currency => 
                    <option value={currency.id} key={currency.id} >{currency.name}</option>
                    )}
                    </select>
                    </td>
                    <td> <span className="glyphicon glyphicon-ok-circle success-green" onClick={this.save}> </span></td>                      
            </tr>)
            
        }
        return (
                <tr>
                <th scope="row">{this.toDateDisplayFormat(this.state.aidOnBudget.date)}</th>
                <td>{this.getOrgName(this.state.aidOnBudget.donorId)}</td>
                <td>{this.state.aidOnBudget.amount}</td>
                <td>{this.state.aidOnBudget.currencyCode} </td>
                <td><span className="glyphicon glyphicon-pencil" onClick={this.toggleEdit}></span></td>
                <td><span className="glyphicon glyphicon-remove" onClick={this.deleteAidOnBudget}></span></td>
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

