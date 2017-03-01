import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import AidOnBudgetRow from './AidOnBudgetRow.jsx';
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as commonListsActions from  '../actions/CommonListsActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import { Alert } from 'react-bootstrap';
export default class AidOnBudgetList extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {                
                errors: [],
                infoMessages:[]
        };
        
        this.addNew = this.addNew.bind(this);
        this.onError = this.onError.bind(this);
        this.showErrors = this.showErrors.bind(this);
        this.showInfoMessages = this.showInfoMessages.bind(this);
    }
    
    componentWillMount() {        
        this.props.actions.loadAidOnBudgetList(); 
        this.props.actions.getCurrencyList();
        this.props.actions.getOrgList();
    }
    
    componentWillReceiveProps(nextProps) { 
        this.setState({errors: nextProps.errors, infoMessages:  nextProps.infoMessages});        
    }
    
    addNew() {
        this.props.actions.addNewAidOnBudget();       
    }
    
    onError(errors) {
        this.setState({errors: errors, infoMessages: []});
    }   
    
    showErrors() {              
         return (this.state.errors.length > 0 && <div className="alert alert-danger" role="alert">
                  {this.state.errors.map(error => 
                      <span>{this.props.translations[error.messageKey]}<br/></span>
                    )}
              </div>) 
    }
    
    showInfoMessages() {
        return (this.state.infoMessages.length > 0 &&
               <div className="alert alert-info" role="alert">
                   {this.state.infoMessages.map(info =>
                      <span>{this.props.translations[info.messageKey]} <br/></span>
              )}
             </div>) 
    }
    
    render() {       
        return (
                <div >                
                <h2>{this.props.translations['amp.gpi-data-aid-on-budget:title']}</h2>
                <div className="panel panel-default">                 
                <div className="panel-body custom-panel">
                <span className="glyphicon glyphicon-plus" onClick={this.addNew}></span>
                <span  onClick={this.addNew}> Add Data</span>
                <span className="success-green"> (insert data to the new field)</span>
                <span> / </span> <span className="glyphicon glyphicon-ok-circle success-green"> </span> <span > Click the Save Symbol to save the added data row</span>
                <span className="float-right"> <button type="button" className="btn btn-success">Save all Edits</button></span>
                </div>                 
                </div>  
                {this.showErrors()}
                {this.showInfoMessages()} 
                <table className="table table-striped">
                <thead>
                <tr>
                <th>{this.props.translations['amp.gpi-data-aid-on-budget:date']}</th>
                <th>{this.props.translations['amp.gpi-data-aid-on-budget:donor-agency']}</th>
                <th>{this.props.translations['amp.gpi-data-aid-on-budget:amount']}</th>
                <th>{this.props.translations['amp.gpi-data-aid-on-budget:currency']}</th>
                <th>{this.props.translations['amp.gpi-data-aid-on-budget:action']}</th>
                </tr>
                </thead>
                <tbody>
               
                {this.props.aidOnBudgetList.map(aidOnBudget => 
                <AidOnBudgetRow aidOnBudget={aidOnBudget} key={aidOnBudget.id} currencyList={this.props.currencyList} orgList={this.props.orgList} isEditing = {!aidOnBudget.id} onError={this.onError} key={aidOnBudget.id || 'c' + aidOnBudget.cid}/>  
                )}                
                </tbody>
                </table>                 
                </div>
        );
    }
}

function mapStateToProps(state, ownProps) {         
        return {
        aidOnBudgetList: state.aidOnBudget.aidOnBudgetList || [],
        errors: state.aidOnBudget.errors || [],
        infoMessages: state.aidOnBudget.infoMessages || [],        
        currencyList: state.commonLists.currencyList || [],
        orgList: state.commonLists.orgList || [],
        translations: state.startUp.translations
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, aidOnBudgetActions, commonListsActions), dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(AidOnBudgetList);
