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
import * as donorNotesActions from '../actions/DonorNotesActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/constants.jsx';
export default class DonorNotesRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                donorNotes: this.props.donorNotes,                                
                showDatePicker: false                
        };
        this.toggleEdit = this.toggleEdit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.save = this.save.bind(this);
        this.toggleDatePicker =  this.toggleDatePicker.bind(this);
        this.onDateChange = this.onDateChange.bind(this);  
        this.deleteDonorNotes = this.deleteDonorNotes.bind(this); 
        this.getErrorsForField = this.getErrorsForField.bind(this);
    }
    
    toggleEdit() {
        const donorNotes = this.state.donorNotes;
        donorNotes.isEditing = true;
        this.setState({donorNotes: donorNotes});
        this.props.actions.updateDonorNotes(donorNotes);        
    }
    
    toggleDatePicker(){
        this.setState({showDatePicker: !this.state.showDatePicker});
    }
    
    onChange(event){
        const errors = [];
        const field = event.target.name;
        const value = event.target.value;        
        const donorNotes = this.state.donorNotes;        
        donorNotes[field] = event.target.value;
        this.setState({donorNotes: donorNotes});
        this.props.actions.updateDonorNotes(donorNotes);               
    }    
    
    onDateChange(date){
        if(date){
            const donorNotes = this.state.donorNotes; 
            const formartedDate = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
            donorNotes['notesDate'] = formartedDate;
            this.setState({donorNotes: donorNotes});
            this.props.actions.updateDonorNotes(donorNotes); 
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
        this.props.actions.save(this.state.donorNotes);                
    }
    
    deleteDonorNotes() {        
        if(confirm(this.props.translations['amp.gpi-data:delete-prompt'])){
            this.props.actions.deleteDonorNotes(this.state.donorNotes); 
        }        
    }
    
    getOrgName(id) {
        var org = this.props.orgList.filter(org => org.id === id)[0];
        return org ? org.name : '';
    }
    
    getErrorsForField(field){
        var errors = this.props.errors.filter(error => {return ((error.id && error.id === this.state.donorNotes.id) || (error.cid && error.cid === this.state.donorNotes.cid) && error.affectedFields && error.affectedFields.includes(field) )})
        return  errors; 
    }
    
    render() {        
        if (this.props.donorNotes.isEditing) {         
            return ( <tr>
                    <td className="error-column">                        
                    </td>
                    <td scope="row" >                                   
                    <div className={this.getErrorsForField('notesDate').length > 0 ? 'form-group date-container has-error' : 'form-group date-container' }>
                    <span className="date-input-container"><input type="text" value={this.toDisplayDateFormat(this.state.donorNotes.notesDate)} readOnly className="date-input form-control" />    
                    </span><span className = "datepicker-toggle glyphicon glyphicon-calendar " onClick={this.toggleDatePicker}> </span></div>
                    <div className="datepicker-container"> 
                    {this.state.showDatePicker &&
                        <DatePicker 
                        hideFooter={true}
                        ref="date" 
                        locale={'en'} 
                        date={this.toDisplayDateFormat(this.state.donorNotes.notesDate)} 
                        onChange={this.onDateChange} 
                        expanded={false}
                        dateFormat={this.getDisplayDateFormat()}
                        />  
                    }
                    </div>
                    </td>
                    <td>
                    
                    <div className={this.getErrorsForField('donorId').length > 0 ? 'form-group has-error' : 'form-group' }>
                    <select name="donorId" className="form-control" value={this.state.donorNotes.donorId} onChange={this.onChange}>
                    <option value="">{this.props.translations['amp.gpi-data:select-donor']}</option>
                    {this.props.orgList.map(org => 
                    <option value={org.id}  key={org.id} >{org.name}</option>
                    )}
                    </select> 
                    </div>
                    
                    </td> 
                    <td className="notes-column">
                    <div className={this.getErrorsForField('notes').length > 0 ? 'form-group has-error' : 'form-group' }>                    
                    <textarea name="notes" className="form-control" rows="5" onChange={this.onChange}>{this.state.donorNotes.notes}</textarea>
                    </div>
                    </td>
                    <td> <span className="glyphicon glyphicon-ok-circle success-color" onClick={this.save}> </span><span className="glyphicon glyphicon-remove" onClick={this.deleteDonorNotes}></span></td>                      
            </tr>)
            
            }
            
            return (
                    <tr>
                    <td></td>
                    <th scope="row">{this.toDisplayDateFormat(this.state.donorNotes.notesDate)}</th>
                    <td>{this.getOrgName(this.state.donorNotes.donorId)}</td>
                    <td className="notes-column">{this.state.donorNotes.notes}</td>
                    <td><span className="glyphicon glyphicon-pencil" onClick={this.toggleEdit}></span> <span className="glyphicon glyphicon-remove" onClick={this.deleteDonorNotes}></span></td>                
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
        return {actions: bindActionCreators(donorNotesActions, dispatch)}
    }
    
    export default connect(mapStateToProps, mapDispatchToProps)(DonorNotesRow);
