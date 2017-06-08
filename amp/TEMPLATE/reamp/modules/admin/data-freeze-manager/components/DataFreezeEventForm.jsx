import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from  '../actions/CommonListsActions';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import DatePicker from 'react-date-picker';
import moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
import * as Constants from '../common/Constants';
require('../styles/less/main.less');
export default class DataFreezeEventForm extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {
           showDatePicker: {freezingDate: false, openPeriodStart: false, openPeriodEnd: false},
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
    }
        
    toggleDatePicker(event) {
        let field = event.target.getAttribute('data-field');
        let toggleState = this.state.showDatePicker;
        toggleState[field] = !toggleState[field];
        this.setState({showDatePicker: toggleState});
    }
    
    onFreezingDateChange(date) {
        let currentRecord = this.state.currentRecord;
        currentRecord.freezingDate = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
        this.setState({currentRecord: currentRecord}); 
    }
    
    onOpenPeriodStartChange(date){
       let currentRecord = this.state.currentRecord;
       currentRecord.openPeriodStart = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
       this.setState({currentRecord: currentRecord});        
    }
    
    onOpenPeriodEndChange(date){
        let currentRecord = this.state.currentRecord;
        currentRecord.openPeriodEnd = moment(date, this.getDisplayDateFormat()).format(Constants.EP_DATE_FORMAT);
        this.setState({currentRecord: currentRecord});    
    }

    onGracePeriodChange(event) {
        let gracePeriod = $(event.target).val();    
        if(gracePeriod) {
            let currentRecord = this.state.currentRecord;
            currentRecord.gracePeriod = parseInt(gracePeriod);
            this.setState({currentRecord: currentRecord});  
        }          
    }
    
    onSendNotificationChange(event) {
        let currentRecord = this.state.currentRecord;
        currentRecord.sendNotification = $(event.target).val() === Constants.OPTION_YES;
        this.setState({currentRecord: currentRecord}); 
    }
    
    onFreezeOptionChange(event) {
        let currentRecord = this.state.currentRecord;
        currentRecord.freezeOption = $(event.target).val();
        this.setState({currentRecord: currentRecord}); 
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
    
    save(){
        this.props.actions.save(this.state.currentRecord);
    }
   
    render() {          
        return (                  
                <div>
                <table className="container-fluid data-selection-fields">
                <tbody>
                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>Sed maximus eros eget risus vehicula handrerit.

                    </td>
                    <td className="col-md-6">

                    </td>
                  </tr>

                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> {this.props.translations['amp.data-freezing:data-freeze-date']}
                     
                      <div className="input-group date pull-right col-md-7" data-provide="datepicker">
                        <input type="text" className="form-control" value={this.toDisplayDateFormat(this.state.currentRecord.freezingDate)} readOnly/>
                        <div className="input-group-addon">
                          <span className="glyphicon glyphicon-calendar" data-field="freezingDate" onClick={this.toggleDatePicker}></span>
                        </div>                       
                      </div>
                      {this.state.showDatePicker.freezingDate &&
                       <div className="input-group date pull-right col-md-7">                      
                          <div className="datepicker-container">                        
                            <DatePicker data-field="freezingDate"  onChange={this.onFreezingDateChange} date={this.toDisplayDateFormat(this.state.currentRecord.freezingDate)} dateFormat={this.getDisplayDateFormat()}/>
                          </div>
                      </div>
                      }                      
                    </td>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <div className="input-group pull-right col-md-7">
                        <input type="text" className="form-control" aria-describedby="basic-addon2" onChange={this.onGracePeriodChange}/>
                        <span className="input-group-addon" id="basic-addon2">{this.props.translations['amp.data-freezing:days']}                     
                        </span>
                      </div>
                       {this.props.translations['amp.data-freezing:grace-period']}
                    </td>
                  </tr>


                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> {this.props.translations['amp.data-freezing:open-period-start']}
                      <div className="input-group date pull-right col-md-7" data-provide="datepicker">
                        <input type="text" className="form-control" value={this.toDisplayDateFormat(this.state.currentRecord.openPeriodStart)} readOnly/>
                        <div className="input-group-addon">
                          <span className="glyphicon glyphicon-calendar" data-field="openPeriodStart" onClick={this.toggleDatePicker}></span>
                        </div>
                      </div>
                      {this.state.showDatePicker.openPeriodStart &&
                          <div className="input-group date pull-right col-md-7">                      
                            <div className="datepicker-container">                       
                            <DatePicker onChange={this.onOpenPeriodStartChange} date={this.toDisplayDateFormat(this.state.currentRecord.openPeriodStart)} dateFormat={this.getDisplayDateFormat()}/>
                           </div>
                          </div>
                      }
                    </td>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <div className="input-group pull-right col-md-7">
                        <div className="radio pull-left col-md-3">
                          <label>
                            <input type="radio" name="sendNotification" value={Constants.OPTION_YES} onChange={this.onSendNotificationChange} checked={this.state.currentRecord.sendNotification}/>Yes</label>
                        </div>
                        <div className="radio pull-left col-md-3">
                          <label>
                            <input type="radio" name="sendNotification" value={Constants.OPTION_NO} onChange={this.onSendNotificationChange} checked={this.state.currentRecord.sendNotification === false}/>No</label>
                        </div>
                      </div>
                        {this.props.translations['amp.data-freezing:notification-email']}
                    </td>
                  </tr>


                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span>{this.props.translations['amp.data-freezing:open-period-end']}
                      <div className="input-group date pull-right col-md-7" data-provide="datepicker">
                        <input type="text" className="form-control" value={this.toDisplayDateFormat(this.state.currentRecord.openPeriodEnd)} readOnly/>
                        <div className="input-group-addon">
                          <span className="glyphicon glyphicon-calendar"  data-field="openPeriodEnd" onClick={this.toggleDatePicker}></span>
                        </div>
                      </div>
                      {this.state.showDatePicker.openPeriodEnd &&
                          <div className="input-group date pull-right col-md-7">                      
                            <div className="datepicker-container">                       
                            <DatePicker data-field="openPeriodEnd" onChange={this.onOpenPeriodEndChange} date={this.toDisplayDateFormat(this.state.currentRecord.openPeriodEnd)} dateFormat={this.getDisplayDateFormat()}/>
                           </div>
                          </div>
                      }
                    </td>
                    <td className="col-md-6">
                      <div className="input-group pull-right col-md-7">
                        <div className="radio pull-left col-md-6">
                          <label>
                            <input type="radio" name="freezeOption" value={Constants.FREEZE_OPTION_ENTIRE_ACTIVITY} onChange={this.onFreezeOptionChange} checked={this.state.currentRecord.freezeOption === Constants.FREEZE_OPTION_ENTIRE_ACTIVITY} />Entire Activity</label>
                        </div>
                        <div className="radio pull-left col-md-3">
                          <label>
                            <input type="radio" name="freezeOption" value={Constants.FREEZE_OPTION_FUNDING} onChange={this.onFreezeOptionChange} checked={this.state.currentRecord.freezeOption === Constants.FREEZE_OPTION_FUNDING} />Funding</label>
                        </div>
                      </div>
                      <div>
                        <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                         {this.props.translations['amp.data-freezing:freeze-options']}</div>

                    </td>
                  </tr>
                  <tr>
                    <td className="col-md-6" colSpan="2">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> {this.props.translations['amp.data-freezing:filters']}
                      <button className="btn btn-default filter-add">
                        <span className="glyphicon glyphicon-plus-sign"></span>
                      </button>
                    </td>                        
                  </tr>
                  </tbody>
                </table>                          
                <button className="btn btn-success pull-right" onClick={this.save}>{this.props.translations['amp.data-freezing:save-event']}</button>
              </div>
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
    return {actions: bindActionCreators(Object.assign({}, commonListsActions, dataFreezeActions), dispatch)}   
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventForm);
