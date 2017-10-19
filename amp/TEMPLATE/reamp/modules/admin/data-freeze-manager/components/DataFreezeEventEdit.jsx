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
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import DatePicker from 'react-date-picker';
import moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
import * as Constants from '../common/Constants';
require('../styles/less/main.less');
export default class DataFreezeEventEdit extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            showDatePicker: {
                freezingDate: false,
                openPeriodStart: false,
                openPeriodEnd: false
            },
            currentRecord: {}
        };

        this.toggleDatePicker = this.toggleDatePicker.bind(this);
        this.onFreezingDateChange = this.onFreezingDateChange.bind(this);
        this.onOpenPeriodStartChange = this.onOpenPeriodStartChange.bind(this);
        this.onOpenPeriodEndChange = this.onOpenPeriodEndChange.bind(this);
        this.onGracePeriodChange = this.onGracePeriodChange.bind(this);
        this.onSendNotificationChange = this.onSendNotificationChange.bind(this);
        this.onFreezeOptionChange = this.onFreezeOptionChange.bind(this);
        this.save = this.save.bind(this);
        this.showFilters = this.showFilters.bind(this);
        this.restoreSavedFilters = this.restoreSavedFilters.bind(this);
        this.onEnabledChange = this.onEnabledChange.bind(this);
        this.getErrorsForField = this.getErrorsForField.bind(this);
        this.onNotificationDaysChange = this.onNotificationDaysChange.bind(this);
        this.toggleDatePickerByFieldName = this.toggleDatePickerByFieldName.bind(this);        
    }

    toggleDatePicker(event) { 
        const toggleState = this.state.showDatePicker;
        const field = event.target.getAttribute('data-field'); 
        for(const fieldName in toggleState){
            if(field != fieldName){
                toggleState[fieldName]  = false; 
            }            
        }
        
        toggleState[field] = !toggleState[field];
        this.setState({
            showDatePicker: toggleState
        });
    }
    
   toggleDatePickerByFieldName(fieldName){
        const toggleState = this.state.showDatePicker;        
        toggleState[fieldName] = !toggleState[fieldName];
        this.setState({
            showDatePicker: toggleState
        });
    }
 
   onFreezingDateChange(date) {
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.freezingDate = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
        this.setState({
            currentRecord: currentRecord
        });
        this.toggleDatePickerByFieldName('freezingDate');
    }

    onOpenPeriodStartChange(date) {
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.openPeriodStart = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
        this.setState({
            currentRecord: currentRecord
        });
        
        this.toggleDatePickerByFieldName('openPeriodStart');
    }

    onOpenPeriodEndChange(date) {
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.openPeriodEnd = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
        this.setState({
            currentRecord: currentRecord
        });
        this.toggleDatePickerByFieldName('openPeriodEnd');
    }
    
    onGracePeriodChange(event) {
        const gracePeriod = $(event.target).val();
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.gracePeriod = (gracePeriod && !isNaN(gracePeriod)) ? parseInt(gracePeriod) : '';
        this.setState({
            currentRecord: currentRecord
        });
    }
    
    onSendNotificationChange(event) {
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.sendNotification = $(event.target).val() === Constants.OPTION_YES;
        if ($(event.target).val() === Constants.OPTION_NO) {
            currentRecord.notificationDays = '';
        }
        this.setState({
            currentRecord: currentRecord
        });
    }

    onEnabledChange(event) {
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.enabled = $(event.target).val() === Constants.OPTION_YES;
        this.setState({
            currentRecord: currentRecord
        });
    }

    onFreezeOptionChange(event) {
        const currentRecord = this.props.dataFreezeEvent;
        currentRecord.freezeOption = $(event.target).val();
        this.setState({
            currentRecord: currentRecord
        });
    }

    onNotificationDaysChange(event) {
        const currentRecord = this.props.dataFreezeEvent;
        const notificationDays = $(event.target).val();
        currentRecord.notificationDays = (notificationDays && !isNaN(notificationDays)) ? parseInt(notificationDays) : '';
        this.setState({
            currentRecord: currentRecord
        }); 
    }
    
    getDisplayDateFormat() {
        return (this.props.settings && this.props.settings[Constants.DATE_FORMAT_SETTING]) ? this.props.settings[Constants.DATE_FORMAT_SETTING].toUpperCase() : Constants.DEFAULT_UI_DATE_FORMAT;
    }

    toDisplayDateFormat(date) {
        var result;
        if (date) {
            result = moment(date, Constants.EP_DATE_FORMAT).format(this.getDisplayDateFormat());
        }

        return result
    }

    save() {
        this.props.actions.save(this.props.dataFreezeEvent);
    }

    showFilters() {
        this.props.setFilterElement();
        this.restoreSavedFilters();
        this.props.filter.showFilters();
        this.props.showFilterElement();
        this.props.filter.on('cancel', function () {
            this.props.hideFilterElement();
        }.bind(this));

        this.props.filter.on('apply', function () {
            const currentRecord = this.props.dataFreezeEvent;
            const filters = this.props.filter.serialize();
            if (filters) {
                currentRecord.filters = JSON.stringify(filters);
                this.setState({
                    currentRecord: currentRecord
                });
            }
            this.props.hideFilterElement();
        }.bind(this));
    }

    restoreSavedFilters() {
        this.props.filter.reset({
            silent: true
        });
        const currentRecord = this.props.dataFreezeEvent;
        const filters = JSON.parse(currentRecord.filters || '{}')
        this.props.filter.deserialize(filters, {
            silent: true
        });
    }

    getErrorsForField(field) {
        var errors = this.props.errors.filter(error => {
            return (((error.id && error.id === this.props.dataFreezeEvent.id) || (error.cid && error.cid === this.props.dataFreezeEvent.cid)) && error.affectedFields && error.affectedFields.includes(field))
        })
        return errors;
    }

    render() {
        return (
            <tr >
                <td>
                <div className={this.getErrorsForField('freezingDate').length > 0 ? 'has-error': ''}>
                <div className="input-group date pull-right " data-provide="datepicker">
                <input type="text" className="form-control" value={this.toDisplayDateFormat(this.props.dataFreezeEvent.freezingDate)} readOnly/>
                <div className="input-group-addon">
                  <span className="glyphicon glyphicon-calendar" data-field="freezingDate" onClick={this.toggleDatePicker}></span>
                </div>                       
              </div>
              {this.state.showDatePicker.freezingDate &&
               <div className="datepicker-outer-wrapper">                      
                  <div className="datepicker-inner-wrapper">                        
                    <DatePicker data-field="freezingDate"  onChange={this.onFreezingDateChange} date={this.toDisplayDateFormat(this.props.dataFreezeEvent.freezingDate)} dateFormat={this.getDisplayDateFormat()} />
                  </div>
              </div>
              }
              </div>
              </td>
                <td >
                <div className={this.getErrorsForField('gracePeriod').length > 0 ? 'has-error input-group pull-right': 'input-group pull-right'}>
                  <input type="text" className="form-control grace-period-input" onChange={this.onGracePeriodChange} value={this.props.dataFreezeEvent.gracePeriod} />        
                
                 </div>
                </td>
                <td>
              <div className={this.getErrorsForField('openPeriodStart').length > 0 ? 'has-error': ''}>
                  <div className="input-group date pull-right " data-provide="datepicker">
                  <input type="text" className="form-control" value={this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodStart)} readOnly/>
                  <div className="input-group-addon">
                    <span className="glyphicon glyphicon-calendar" data-field="openPeriodStart" onClick={this.toggleDatePicker}></span>
                  </div>
                </div>
                {this.state.showDatePicker.openPeriodStart &&
                    <div className="datepicker-outer-wrapper">                      
                    <div className="datepicker-inner-wrapper">                      
                      <DatePicker onChange={this.onOpenPeriodStartChange} date={this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodStart)} dateFormat={this.getDisplayDateFormat()} />
                     </div>
                    </div>
                }  
                </div>
                </td>
                <td>
                <div className={this.getErrorsForField('openPeriodEnd').length > 0 ? 'has-error': ''}>
                <div className="input-group date pull-right " data-provide="datepicker">
                <input type="text" className="form-control" value={this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodEnd)} readOnly/>
                <div className="input-group-addon">
                  <span className="glyphicon glyphicon-calendar"  data-field="openPeriodEnd" onClick={this.toggleDatePicker}></span>
                </div>
              </div>
              {this.state.showDatePicker.openPeriodEnd &&
                  <div className="datepicker-outer-wrapper">                      
                  <div className="datepicker-inner-wrapper">                       
                    <DatePicker data-field="openPeriodEnd" onChange={this.onOpenPeriodEndChange} date={this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodEnd)} dateFormat={this.getDisplayDateFormat()}/>
                   </div>
                  </div>
              }
                </div>
                </td>
                
                <td>
              <div className={this.getErrorsForField('freezeOption').length > 0 ? 'input-group has-error': 'input-group'}>
              <div className="radio">
                <label>
                  <input type="radio" name={'freezeOption' + (this.props.dataFreezeEvent.id || this.props.dataFreezeEvent.cid)} value={Constants.FREEZE_OPTION_ENTIRE_ACTIVITY} onChange={this.onFreezeOptionChange} checked={this.props.dataFreezeEvent.freezeOption === Constants.FREEZE_OPTION_ENTIRE_ACTIVITY} />{this.props.translations['amp.data-freezing:freeze-option-activity']}</label>
              </div>
              <div className="radio">
                <label>
                  <input type="radio" name={'freezeOption' + (this.props.dataFreezeEvent.id || this.props.dataFreezeEvent.cid)} value={Constants.FREEZE_OPTION_FUNDING} onChange={this.onFreezeOptionChange} checked={this.props.dataFreezeEvent.freezeOption === Constants.FREEZE_OPTION_FUNDING} />{this.props.translations['amp.data-freezing:freeze-option-funding']}</label>
              </div>
            </div>
            <div>
             </div>
                
                </td>
                <td>
                  <div className={this.getErrorsForField('sendNotification').length > 0 ? 'input-group has-error': 'input-group'}>
                  <div className="radio">
                    <label>
                      <input type="radio" name={'sendNotification' + (this.props.dataFreezeEvent.id || this.props.dataFreezeEvent.cid)} value={Constants.OPTION_YES} onChange={this.onSendNotificationChange} checked={this.props.dataFreezeEvent.sendNotification}/>{this.props.translations['amp.data-freezing:boolean-option-yes']}</label>
                  </div>
                  <div className="radio">
                    <label>
                      <input type="radio" name={'sendNotification' + (this.props.dataFreezeEvent.id || this.props.dataFreezeEvent.cid)} value={Constants.OPTION_NO} onChange={this.onSendNotificationChange} checked={this.props.dataFreezeEvent.sendNotification === false}/>{this.props.translations['amp.data-freezing:boolean-option-no']}</label>
                  </div>
                </div>
                  </td>
                  <td>
                     <input type="text" className="form-control notification-days-input" value={this.props.dataFreezeEvent.notificationDays} readOnly={this.props.dataFreezeEvent.sendNotification === false} onChange={this.onNotificationDaysChange}/>
                  </td>
                <td>                  
                  <button className="btn btn-default filter-add" onClick={this.showFilters}>
                  <span className="glyphicon glyphicon-plus-sign"></span>
                 </button>
               </td>
               <td className="action-column">                  
                      <span className="glyphicon glyphicon-custom glyphicon-ok-circle success-color" onClick={this.save}> </span>
                      <span className="glyphicon glyphicon-custom glyphicon-remove-sign" onClick={this.props.cancel}></span>
                   </td>
              </tr>

        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        settings: state.commonLists.settings
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, commonListsActions, dataFreezeActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventEdit);