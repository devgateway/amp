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
export default class DataFreezeEventView extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {           
        };    
        this.toDisplayDateFormat = this.toDisplayDateFormat.bind(this);
        this.getDisplayDateFormat = this.getDisplayDateFormat.bind(this);
        this.deleteDataFreezeEvent = this.deleteDataFreezeEvent.bind(this);
    }
    
    showFreezeOption(freezeOption) {
        let result = '';
        if (freezeOption === Constants.FREEZE_OPTION_ENTIRE_ACTIVITY) {             
            result = this.props.translations['amp.data-freezing:freeze-option-activity'];
        } else if (freezeOption === Constants.FREEZE_OPTION_FUNDING) {
            result = this.props.translations['amp.data-freezing:freeze-option-funding'];
        }
        
        return result;      
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
    
    deleteDataFreezeEvent() {
        if(confirm(this.props.translations['amp.data-freezing:delete-prompt'])){
            this.props.actions.deleteDataFreezeEvent(this.props.dataFreezeEvent); 
        }        
    }
    
    render() {          
         return (                  
                <tr >
                <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.freezingDate)}</td>
                <td>{this.props.dataFreezeEvent.gracePeriod}</td>
                <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodStart)}</td>
                <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodEnd)}</td>
                <td>
                 {this.props.dataFreezeEvent.count}
                </td>
                <td>{this.showFreezeOption(this.props.dataFreezeEvent.freezeOption)}</td>
                <td>
                {this.props.dataFreezeEvent.sendNotification ? this.props.translations['amp.data-freezing:boolean-option-yes'] : this.props.translations['amp.data-freezing:boolean-option-no']}
                </td>
                <td>
                  <span className="filter">Filter name 1, Filter name 2</span>
                </td>  
                <td> {this.props.dataFreezeEvent.enabled ? this.props.translations['amp.data-freezing:boolean-option-yes'] : this.props.translations['amp.data-freezing:boolean-option-no']}</td>
                <td className="action-column">
                  <span className="glyphicon glyphicon-custom glyphicon-pencil" onClick={this.props.edit}></span> <span className="glyphicon glyphicon-custom glyphicon-trash" onClick={this.deleteDataFreezeEvent}></span>
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
    return {actions: bindActionCreators(Object.assign({}, commonListsActions, dataFreezeActions), dispatch)}   
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventView);
