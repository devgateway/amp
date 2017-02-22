import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import { FormControl } from 'react-bootstrap';
import DatePicker from 'react-date-picker';
import Moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
require('../styles/aid-on-budget.less');
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx'
export default class AidOnBudgetRow extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
          aidOnBudget: this.props.aidOnBudget,          
          saving: false,
          isEditing: this.props.isEditing,
          showDatePicker:false
        };
        this.toggleEdit = this.toggleEdit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.save = this.save.bind(this);
        this.toggleDatePicker =  this.toggleDatePicker.bind(this);
        this.onDateChange = this.onDateChange.bind(this);        
    }
    toggleEdit() {
        this.setState({isEditing: true});
    }
    toggleDatePicker(){
        this.setState({showDatePicker: !this.state.showDatePicker});
    }
    onChange(event){
        let field = event.target.name;
        let value = event.target.value;
        let aidOnBudget = this.state.aidOnBudget;        
        aidOnBudget[field] = event.target.value;
        this.setState({aidOnBudget: aidOnBudget});       
    }
    onDateChange(date){
        let aidOnBudget = this.state.aidOnBudget;        
        aidOnBudget['date'] = date;
        this.setState({aidOnBudget: aidOnBudget});
        this.toggleDatePicker();
    }
    save(){
       this.props.actions.save(this.state.aidOnBudget); 
    }
    render() {        
        if (this.state.isEditing) {
           return ( <tr >
            <td scope="row" >                                   
                  <div className="date-container">
                   <span className="date-input-container"><input type="text" value={this.state.aidOnBudget.date} readOnly className="date-input" />    
                   </span><span className = "datepicker-toggle glyphicon glyphicon-calendar " onClick={this.toggleDatePicker}> </span></div>
                  <div className="datepicker-container"> 
                  {this.state.showDatePicker &&
                      <DatePicker 
                      hideFooter={true}
                      ref="date" 
                      locale={'en'} 
                      date={this.state.aidOnBudget.date} 
                      onChange={this.onDateChange} 
                      expanded={false}
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
            <td> <span className="glyphicon glyphicon-floppy-disk" onClick={this.save}> </span></td>                      
            </tr>)
            
        }
        return (
                <tr>
                <th scope="row">{this.state.aidOnBudget.date}</th>
                <td>{this.state.aidOnBudget.donorId}</td>
                <td>{this.state.aidOnBudget.amount}</td>
                <td>{this.state.aidOnBudget.currencyCode} </td>
                <td><span className="glyphicon glyphicon-pencil" onClick={this.toggleEdit}></span></td>                      
                </tr>
                                  
        );
    }
}

function mapStateToProps(state, ownProps) {     
    return {
        
    }
  }


function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(aidOnBudgetActions, dispatch)}
 }

  export default connect(mapStateToProps, mapDispatchToProps)(AidOnBudgetRow);

