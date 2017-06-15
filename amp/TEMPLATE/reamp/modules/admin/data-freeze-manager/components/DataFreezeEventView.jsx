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
                showAppliedFilters: false
        };    
        this.toDisplayDateFormat = this.toDisplayDateFormat.bind(this);
        this.getDisplayDateFormat = this.getDisplayDateFormat.bind(this);
        this.deleteDataFreezeEvent = this.deleteDataFreezeEvent.bind(this);
        this.showAppliedFilters = this.showAppliedFilters.bind(this);
        this.toggleAppliedFilters = this.toggleAppliedFilters.bind(this);
        this.filtersToHtml = this.filtersToHtml.bind(this);
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
    
    toggleAppliedFilters(event) {
       this.setState({showAppliedFilters: !this.state.showAppliedFilters});
    }
    
    showAppliedFilters() {
        var html = [];
        this.props.filter.reset({silent : true });
        if (this.props.dataFreezeEvent.filters) {
            let filters = JSON.parse(this.props.dataFreezeEvent.filters || '{}');
            this.props.filter.deserialize(filters, {silent : true });
            let modelFilters = this.props.filter.serializeToModels();
            html = this.filtersToHtml(modelFilters.filters);
        }
        return html;
        
    }
    
    filtersToHtml(filters) {
        var html = [];
        if (filters != undefined) {
            for ( var propertyName in filters) {
                var auxProperty = filters[propertyName];
                var content = [];
                if(auxProperty.modelType === 'YEAR-SINGLE-VALUE' || auxProperty.modelType === 'DATE-RANGE-VALUES'){
                    var filter = this.createDateFilterObject(filters, propertyName);
                    if(filter && filter.values.length > 0){
                        html.push(<div className='round-filter-group'><b className='i18n'>{ filter.trnName.replace(/-/g, " ") }</b><br/> { this.filterContentToHtml(filter.values) }</div>);                        
                    }
                    
                } else {
                    auxProperty.forEach(function(item) {
                        var auxItem = {};               
                        if(item.get !== undefined) {
                            auxItem.id = item.get('id');
                            auxItem.name = item.get('name');
                            
                            if (item.get('name') === "true" || item.get('name') === "false") {                      
                                auxItem.trnName = TranslationManager.getTranslated(item.get('name'));                       
                             }
                            else {
                                auxItem.trnName = item.get('name');
                            }
                            content.push(auxItem);
                        } else {
                            console.error(JSON.stringify(auxItem) + " not mapped, we need to check why is not a model.");
                        }
                    });
                    
                    var trnName = auxProperty.filterName || propertyName;
                    html.push(<div className='round-filter-group'><b className='i18n'>{ trnName.replace(/-/g, " ") }</b><br/>{this.filterContentToHtml(content)}</div>);
                }   
                
            }
        }       
        return html;
    }
    
    filterContentToHtml(content) {        
        var html = [];
        content.forEach(function(item) {
            html.push(<div className='round-filter'>{item.trnName}</div>);
        });
        return html;
    }
    
    createDateFilterObject(filters, propertyName) {    
        var auxProperty = filters[propertyName];
        var filter; 
        if (auxProperty != undefined) {
            filter = {
                    trnName : TranslationManager.getTranslated(propertyName), 
                    name : propertyName,
                    values:[]
            };  
            if (auxProperty.modelType === 'DATE-RANGE-VALUES') {
                auxProperty.start = auxProperty.start || "";
                auxProperty.end = auxProperty.end || "";

                var startDatePrefix = TranslationManager.getTranslated((auxProperty.start.length > 0 && auxProperty.end.length === 0) ? "From" : "");
                var endDatePrefix = TranslationManager.getTranslated((auxProperty.start.length === 0 && auxProperty.end.length > 0) ? "Until" : "");
                var trnName = "";
                
                if(auxProperty.start.length > 0 ){
                    trnName = startDatePrefix + " " + window.currentFilter.formatDate(auxProperty.start);               
                }           
                            
                if(auxProperty.end.length > 0){
                    if(auxProperty.start.length > 0){
                        trnName += " - ";
                    }
                    trnName += endDatePrefix + " " + window.currentFilter.formatDate(auxProperty.end);                  
                }   
                
                filter.values.push({
                    id : auxProperty.end + auxProperty.start ,
                    name : auxProperty.end + auxProperty.start,
                    trnName : trnName                   
                }); 
                
                
            } else if (auxProperty.modelType === 'YEAR-SINGLE-VALUE') {
                if(auxProperty.year){               
                    filter.values.push({
                        id : auxProperty.year,
                        name : auxProperty.year,
                        trnName : auxProperty.year
                    });
                }           
                filter.trnName = auxProperty.displayName;
            }
        }
        return filter;
    };
    
    render() { 
        if (this.props.context === Constants.UNFREEZE_ALL) {
            return (                   
                    <tr >
                    <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.freezingDate)}</td>                    
                    <td className="text-left">
                     {this.props.dataFreezeEvent.count}
                    </td>                            
                  </tr>                
         ); 
        } else {
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
                      <button className="btn btn-default filter-add" onClick={this.toggleAppliedFilters}>
                      <span className={this.state.showAppliedFilters ? 'glyphicon glyphicon-chevron-up' : 'glyphicon glyphicon-chevron-down'}></span>
                     </button>
                      {this.state.showAppliedFilters &&
                          <div className="applied-filters-outer">
                            <div className="applied-filters-inner">
                              {this.showAppliedFilters()}
                            </div>
                          </div>
                      }
                    </td>  
                    <td> {this.props.dataFreezeEvent.enabled ? this.props.translations['amp.data-freezing:boolean-option-yes'] : this.props.translations['amp.data-freezing:boolean-option-no']}</td>
                    <td className="action-column">
                      <span className="glyphicon glyphicon-custom glyphicon-pencil" onClick={this.props.edit}></span> <span className="glyphicon glyphicon-custom glyphicon-trash" onClick={this.deleteDataFreezeEvent}></span>
                    </td>               
                  </tr>                
         ); 
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

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventView);
