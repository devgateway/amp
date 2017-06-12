import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from  '../actions/CommonListsActions';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import DataFreezeEventEdit from '../components/DataFreezeEventEdit';
import DataFreezeEventView from '../components/DataFreezeEventView';
import DatePicker from 'react-date-picker';
import moment from 'moment';
require('react-date-picker/base.css');
require('react-date-picker/theme/hackerone.css');
import * as Constants from '../common/Constants';
require('../styles/less/main.less');
export default class DataFreezeEventRow extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {
                dataFreezeEvent: this.props.dataFreezeEvent,
        }; 
        this.edit = this.edit.bind(this);
        this.cancel = this.cancel.bind(this);
      }
       
    edit() {
        const dataFreezeEvent = this.props.dataFreezeEvent;
        var origDataFreezeEvent  = Object.assign({}, dataFreezeEvent);
        dataFreezeEvent.isEditing = true;           
        this.props.actions.updateDataFreezeEvent(dataFreezeEvent);        
    }
    
    cancel() {
        const origDataFreezeEvent = this.state.origDataFreezeEvent;
        if(origDataFreezeEvent && origDataFreezeEvent.id) {
           this.props.actions.updateDataFreezeEvent(origDataFreezeEvent);  
        } else {
            this.props.actions.removeFromState(this.props.dataFreezeEvent);
        }   
    }
    
    render() {          
        if(this.props.dataFreezeEvent.isEditing){
               return (<DataFreezeEventEdit dataFreezeEvent={this.props.dataFreezeEvent}  cancel={this.cancel}/>); 
            } else{
               return (<DataFreezeEventView dataFreezeEvent={this.props.dataFreezeEvent} edit={this.edit}/>); 
            } 
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

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventRow);
