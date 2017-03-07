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
require('../styles/aid-on-budget.less');
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
export default class AidOnBudgetRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                aidOnBudget: this.props.aidOnBudget,                                
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
        const aidOnBudget = this.state.aidOnBudget;        
        aidOnBudget[field] = event.target.value;
        this.setState({aidOnBudget: aidOnBudget});
        this.props.actions.updateAidOnBudget(aidOnBudget);               
    }    
    
    onDateChange(date){
        if(date){
            const aidOnBudget = this.state.aidOnBudget; 
            const formartedDate = moment(date, this.state.displayDateFormat).format(this.state.endPointDateFormat);
            aidOnBudget['indicatorDate'] = formartedDate;
            this.setState({aidOnBudget: aidOnBudget});
            this.props.actions.updateAidOnBudget(aidOnBudget); 
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
        this.props.actions.save(this.state.aidOnBudget);                
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
    
    getCurrencyName(currencyCode) {
        var currency = this.props.currencyList.filter(currency => currency.id === currencyCode)[0];
        return currency ? currency.name : '';
    }
    
    getErrors(){
        const errors = this.props.errors.filter(error => {return (error.id && error.id === this.state.aidOnBudget.id) || (error.cid && error.cid === this.state.aidOnBudget.cid)})
        if(errors.length > 0){
            const errorPopover = (
                    <Popover
                    id="error-popover"
                    title="Errors">
                    {errors.map(error => 
                    <span>{this.props.translations[error.messageKey]}<br/></span>
                    )} 
                    </Popover>
            );
            return (<OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={errorPopover}>
                    <span className="glyphicon glyphicon-exclamation-sign error-color">                  
                    </span>
                    </OverlayTrigger>                
            );
            
        }
        
    }
    
    render() {        
        if (this.props.aidOnBudget.isEditing) {         
            return ( <tr>
                    <td className="error-column">
                    {this.getErrors()}      
                    </td>
                    <td scope="row" >                                   
                    <div className="date-container">
                    <span className="date-input-container"><input type="text" value={this.toDateDisplayFormat(this.state.aidOnBudget.indicatorDate)} readOnly className="date-input" />    
                    </span><span className = "datepicker-toggle glyphicon glyphicon-calendar " onClick={this.toggleDatePicker}> </span></div>
                    <div className="datepicker-container"> 
                    {this.state.showDatePicker &&
                        <DatePicker 
                        hideFooter={true}
                        ref="date" 
                        locale={'en'} 
                        date={this.toDateDisplayFormat(this.state.aidOnBudget.indicatorDate)} 
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
                    <td> <span className="glyphicon glyphicon-ok-circle success-color" onClick={this.save}> </span><span className="glyphicon glyphicon-remove" onClick={this.deleteAidOnBudget}></span></td>                      
            </tr>)
            
        }
        
        return (
                <tr>
                <td></td>
                <th scope="row">{this.toDateDisplayFormat(this.state.aidOnBudget.indicatorDate)}</th>
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
