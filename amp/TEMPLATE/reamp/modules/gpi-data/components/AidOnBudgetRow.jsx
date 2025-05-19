import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import { FormControl } from 'react-bootstrap';
import { OverlayTrigger } from 'react-bootstrap';
import {Tooltip } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
import DatePicker from 'react-date-picker';
import moment from 'moment';
import {Typeahead} from 'react-bootstrap-typeahead';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
require('../styles/main.less');
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/constants.jsx';
class AidOnBudgetRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
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
        this.cancel = this.cancel.bind(this);
    }

    toggleEdit() {
        const aidOnBudget = this.props.aidOnBudget;
        var origAidOnBudget = Object.assign({}, aidOnBudget);
        aidOnBudget.isEditing = true;
        this.setState({origAidOnBudget: origAidOnBudget});
        this.props.actions.updateAidOnBudget(aidOnBudget);
    }

    cancel() {
        const origAidOnBudget = this.state.origAidOnBudget;
        if(origAidOnBudget && origAidOnBudget.id) {
           this.props.actions.updateAidOnBudget(origAidOnBudget);
        } else {
            this.props.actions.removeFromState(this.props.aidOnBudget);
        }
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
        const aidOnBudget = this.props.aidOnBudget;
        if(field === 'donorId'){
            aidOnBudget[field] = value ? parseInt(value) : '';
        } else {
            aidOnBudget[field] = value;
        }
        this.props.actions.updateAidOnBudget(aidOnBudget);
    }

    onDateChange(date){
        if(date){
            const aidOnBudget = this.props.aidOnBudget;
            const formatedDate = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
            aidOnBudget['indicatorDate'] = formatedDate;
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
        this.props.actions.save(this.props.aidOnBudget);
    }

    deleteAidOnBudget() {
        if(confirm(this.props.translations['amp.gpi-data:delete-prompt'])){
            this.props.actions.deleteAidOnBudget(this.props.aidOnBudget);
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
        var errors = this.props.errors.filter(error => {return (((error.id && error.id === this.props.aidOnBudget.id) || (error.cid && error.cid === this.props.aidOnBudget.cid)) && error.affectedFields && error.affectedFields.includes(field) )})
        return  errors;
    }

    onOrgChange(selected){
        if (selected.length > 0) {
           this.onValueChange('donorId', selected[0].id);
        } else {
            this.onValueChange('donorId', null);
        }
    }

    formatAmount(amount){
        if(amount == 0) {
            return amount;
        } else {
           var formatted = this.props.numberFormatter.format(amount);
           //return 0 instead of formatted zero e.g 0.000
           if(formatted == 0){
              return 0;
           }
           return formatted;
        }
    }

    render() {
        if (this.props.aidOnBudget.isEditing) {

            let dateToolTip = (
                    <Tooltip id="date-icon-tooltip">{this.props.translations['amp.gpi-data:date-icon:tooltip']}</Tooltip>
            );

            let datePickerIcon = (
                    <OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={dateToolTip}>
                    <span className = "datepicker-toggle glyphicon glyphicon-custom glyphicon-calendar " onClick={this.toggleDatePicker}> </span>
                    </OverlayTrigger>
            )

            return ( <tr>
                    <td scope="row" >
                    <div className={this.getErrorsForField('indicatorDate').length > 0 ? 'form-group date-container has-error' : 'form-group date-container' }>
                    <br/>
                    <span className="date-input-container"><input type="text" value={this.toDisplayDateFormat(this.props.aidOnBudget.indicatorDate)} readOnly className="date-input form-control" />
                    </span>{datePickerIcon}
                    </div>
                    <div className="datepicker-container">
                    {this.state.showDatePicker &&
                        <DatePicker
                        hideFooter={true}
                        ref="date"
                        locale={'en'}
                        date={this.toDisplayDateFormat(this.props.aidOnBudget.indicatorDate)}
                        onChange={this.onDateChange}
                        expanded={false}
                        dateFormat={this.getDisplayDateFormat()}
                        onClickOutside = {this.toggleDatePicker}
                        />
                    }
                    </div>
                    </td>
                    <td>
                    <div className={this.getErrorsForField('donorId').length > 0 ? 'form-group has-error' : 'form-group ' }>
                    <br/>
                    <Typeahead
                    bodyContainer={false}
                    labelKey="name"
                    options={this.props.orgList}
                    placeholder={this.props.translations['amp.gpi-data:select-donor']}
                    onChange={this.onOrgChange}
                    selected={this.props.orgList.filter(org => {return org.id === this.props.aidOnBudget.donorId})}
                    clearButton={true}
                    />
                    </div>
                    </td>
                    <td>
                    <div className={this.getErrorsForField('amount').length > 0 ? 'form-group has-error amount-input' : 'form-group amount-input' }>
                    <br/>
                    <input type="text" name="amount" className="form-control amount-input" placeholder="" value={this.props.aidOnBudget.amount} onChange={this.onChange} />
                    </div>
                    </td>
                    <td>
                    <div className={this.getErrorsForField('currencyCode').length > 0 ? 'form-group has-error' : 'form-group' }>
                    <br/>
                    <select name="currencyCode" value={this.props.aidOnBudget.currencyCode} className="form-control" onChange={this.onChange}>
                    <option value="" >{this.props.translations['amp.gpi-data:select-currency'].toLowerCase()}</option>
                    {this.props.currencyList.map(currency =>
                    <option value={currency.id} key={currency.id} >{currency.id}</option>
                    )}
                    </select>
                    </div>
                    </td>
                    <td className="actions-column"><br/>
                    <span className="glyphicon glyphicon-custom glyphicon-ok-circle success-color" onClick={this.save}> </span>
                    <span className="glyphicon glyphicon-custom glyphicon-remove-sign" onClick={this.cancel}></span>
                    </td>
            </tr>)

            }

            return (
                    <tr>
                    <td scope="row">{this.toDisplayDateFormat(this.props.aidOnBudget.indicatorDate)}</td>
                    <td>{this.getOrgName(this.props.aidOnBudget.donorId)}</td>
                    <td className="amount-column">{this.formatAmount(this.props.aidOnBudget.amount)}</td>
                    <td>{this.props.aidOnBudget.currencyCode} </td>
                    <td className="actions-column"><span className="glyphicon glyphicon-custom glyphicon-pencil" onClick={this.toggleEdit}></span> <span className="glyphicon glyphicon-custom glyphicon-trash" onClick={this.deleteAidOnBudget}></span></td>
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
