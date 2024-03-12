import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import { FormControl } from 'react-bootstrap';
import { OverlayTrigger } from 'react-bootstrap';
import {Tooltip } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
import DatePicker from 'react-date-picker';
import {Typeahead} from 'react-bootstrap-typeahead';
import moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
require('../styles/main.less');
import * as donorNotesActions from '../actions/DonorNotesActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/constants.jsx';

class DonorNotesRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                donorNotes: this.props.donorNotes,
                showDatePicker: false,
                showFullText: false,
                shortTextLength: 55
        };
        this.toggleEdit = this.toggleEdit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.save = this.save.bind(this);
        this.toggleDatePicker =  this.toggleDatePicker.bind(this);
        this.onDateChange = this.onDateChange.bind(this);
        this.deleteDonorNotes = this.deleteDonorNotes.bind(this);
        this.getErrorsForField = this.getErrorsForField.bind(this);
        this.cancel = this.cancel.bind(this);
        this.onOrgChange = this.onOrgChange.bind(this);
        this.toggleNotes = this.toggleNotes.bind(this);
     }

    toggleEdit() {
        const donorNotes = this.props.donorNotes;
        var origDonorNotes = Object.assign({}, donorNotes);
        donorNotes.isEditing = true;
        this.setState({origDonorNotes: origDonorNotes});
        this.props.actions.updateDonorNotes(donorNotes, this.props.indicatorCode);
    }

    cancel() {
        const origDonorNotes = this.state.origDonorNotes;
        if(origDonorNotes && origDonorNotes.id) {
           this.props.actions.updateDonorNotes(origDonorNotes, this.props.indicatorCode);
        } else {
            this.props.actions.removeFromState(this.props.donorNotes, this.props.indicatorCode);
        }
    }

    toggleDatePicker(){
        this.setState({showDatePicker: !this.state.showDatePicker});
    }

    onChange(event){
        const errors = [];
        const field = event.target.name;
        const value = event.target.value;
        const donorNotes = this.props.donorNotes;
        donorNotes[field] = event.target.value;
        this.props.actions.updateDonorNotes(donorNotes, this.props.indicatorCode);
    }

    toggleNotes() {
        this.setState({showFullText: !this.state.showFullText});
    }

    onDateChange(date){
        if(date){
            const donorNotes = this.props.donorNotes;
            const formartedDate = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
            donorNotes['notesDate'] = formartedDate;
            this.props.actions.updateDonorNotes(donorNotes, this.props.indicatorCode);
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
        this.props.actions.save(this.props.donorNotes, this.props.indicatorCode);
    }

    deleteDonorNotes() {
        if(confirm(this.props.translations['amp.gpi-data:delete-prompt'])){
            this.props.actions.deleteDonorNotes(this.props.donorNotes, this.props.indicatorCode);
        }
    }

    getOrgName(id) {
        var org = this.props.verifiedOrgList.filter(org => org.id === id)[0];
        return org ? org.name : '';
    }

    getErrorsForField(field){
        var errors = this.props.errors.filter(error => {return (((error.id && error.id === this.props.donorNotes.id) || (error.cid && error.cid === this.props.donorNotes.cid)) && error.affectedFields && error.affectedFields.includes(field) )})
        return  errors;
    }

    onOrgChange(selected){
        let donorNotes = this.props.donorNotes;
        if (selected.length > 0) {
            donorNotes.donorId = selected[0].id;
        } else {
            donorNotes.donorId = null;
        }
        this.props.actions.updateDonorNotes(donorNotes, this.props.indicatorCode);
    }

    showTextToggle() {
        var notes = this.props.donorNotes.notes || '';
        var toggleTitle = this.state.showFullText ? this.props.translations['amp.gpi-data-donor-notes:collapse-text'] : this.props.translations['amp.gpi-data-donor-notes:expand-text'];
        const tooltip = (<Tooltip id="notes-toggle-tooltip">{toggleTitle}</Tooltip>);
       if(notes.length > this.state.shortTextLength && this.state.showFullText == false) {
            return (<OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={tooltip}>
            <span className= {this.state.showFullText ? 'notes-toggle glyphicon glyphicon glyphicon-chevron-up' : 'notes-toggle glyphicon glyphicon-chevron-down'} onClick = {this.toggleNotes}></span>
            </OverlayTrigger>);
        }

       if(this.state.showFullText){
           return (<OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={tooltip}>
           <span className= {this.state.showFullText ? 'notes-toggle glyphicon glyphicon glyphicon-chevron-up' : 'notes-toggle glyphicon glyphicon-chevron-down'} onClick = {this.toggleNotes}></span>
           </OverlayTrigger>);
       }

    }

    render() {
        if (this.props.donorNotes.isEditing) {

            let dateToolTip = (
                    <Tooltip id="date-icon-tooltip">{this.props.translations['amp.gpi-data:date-icon:tooltip']}</Tooltip>
            );

            let datePickerIcon = (
                    <OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={dateToolTip}>
                    <span className = "datepicker-toggle glyphicon glyphicon-custom glyphicon-calendar " onClick={this.toggleDatePicker}> </span>
                    </OverlayTrigger>
            )

            return ( <tr>
                    <td className="error-column">
                    </td>
                    <td scope="row" >
                    <div className={this.getErrorsForField('notesDate').length > 0 ? 'form-group date-container has-error' : 'form-group date-container' }>
                    <span className="date-input-container"><input type="text" value={this.toDisplayDateFormat(this.props.donorNotes.notesDate)} readOnly className="date-input form-control" />
                    </span>{datePickerIcon}</div>
                    <div className="datepicker-container">
                    {this.state.showDatePicker &&
                        <DatePicker
                        hideFooter={true}
                        ref="date"
                        locale={'en'}
                        date={this.toDisplayDateFormat(this.props.donorNotes.notesDate)}
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
                    options={this.props.verifiedOrgList}
                    placeholder={this.props.translations['amp.gpi-data:select-donor']}
                    onChange={this.onOrgChange}
                    selected={this.props.verifiedOrgList.filter(org => {return org.id === this.props.donorNotes.donorId})}
                    clearButton={true}
                    />
                    </div>
                    </td>
                    <td className="notes-column">
                    <div className={this.getErrorsForField('notes').length > 0 ? 'form-group has-error' : 'form-group' }>
                    <textarea name="notes" className="form-control" rows="5" onChange={this.onChange}>{this.props.donorNotes.notes}</textarea>
                    </div>
                    </td>
                    <td className="actions-column">
                       <span className="glyphicon glyphicon-custom glyphicon-ok-circle success-color" onClick={this.save}> </span>
                       <span className="glyphicon glyphicon-custom glyphicon-remove-sign" onClick={this.cancel}></span>
                    </td>
            </tr>)

            }
            var notes = this.props.donorNotes.notes || '';
            var toggleTitle = this.state.showFullText ? this.props.translations['amp.gpi-data-donor-notes:collapse-text'] : this.props.translations['amp.gpi-data-donor-notes:expand-text'];


            return (
                    <tr>
                    <td></td>
                    <td scope="row">{this.toDisplayDateFormat(this.props.donorNotes.notesDate)}</td>
                    <td>{this.getOrgName(this.props.donorNotes.donorId)}</td>
                    <td className={this.state.showFullText ? 'notes-column' : 'notes-column notes-column-short'}>
                    {notes.length > this.state.shortTextLength && this.state.showFullText == false &&
                        <div className="notes-container"> { notes.substring(0, this.state.shortTextLength - 1) } {this.showTextToggle()}</div>
                    }

                    {(notes.length <= this.state.shortTextLength || this.state.showFullText) &&
                        <div className="notes-container"> { notes} {this.showTextToggle()}</div>
                    }
                    </td>
                    <td className="actions-column"><span className="glyphicon glyphicon-custom glyphicon-pencil" onClick={this.toggleEdit}></span> <span className="glyphicon glyphicon-custom glyphicon-trash" onClick={this.deleteDonorNotes}></span></td>
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
