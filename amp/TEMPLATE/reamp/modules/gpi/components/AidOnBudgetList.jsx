import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import AidOnBudgetRow from './AidOnBudgetRow.jsx'
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx'
import * as commonListsActions from  '../actions/CommonListsActions.jsx'

export default class AidOnBudgetList extends Component {

    constructor(props, context) {      
        super(props, context);
        this.state = {
                addNew: false
              };
        this.addNew = this.addNew.bind(this);
    }
    
    componentWillMount() {        
        this.props.actions.loadAidOnBudgetList(); 
        this.props.actions.getCurrencyList();
        this.props.actions.getOrgList();
    }
    addNew (){
        this.setState({addNew: true});
    }
    render() {         
         return (
            <div >     
                 <div> 
                 <span className="glyphicon glyphicon-plus" onClick={this.addNew}></span>
                 </div>
                <table className="table table-striped">
                               <thead>
                                 <tr>
                                   <th>Date</th>
                                   <th>Donor Agency</th>
                                   <th>Amount</th>
                                   <th>Currency</th>
                                   <th>Action</th>
                                 </tr>
                               </thead>
                               <tbody>
                                   {this.state.addNew &&
                                       <AidOnBudgetRow aidOnBudget={{}}  currencyList={this.props.currencyList} orgList={this.props.orgList} isEditing = {true}/>
                                   }
                                   {this.props.aidOnBudgetList.map(aidOnBudget => 
                                      <AidOnBudgetRow aidOnBudget={aidOnBudget} key={aidOnBudget.id} currencyList={this.props.currencyList} orgList={this.props.orgList} isEditing = {false}/>  
                                   )}
                                                                 
                               </tbody>
                             </table>                 
            </div>
        );
    }
}

function mapStateToProps(state, ownProps) {     
    return {
        aidOnBudgetList: state.aidOnBudgetList || [],
        currencyList: state.commonLists.currencyList || [],
        orgList: state.commonLists.orgList || []
    }
  }

  function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, aidOnBudgetActions, commonListsActions), dispatch)}
  }

  export default connect(mapStateToProps, mapDispatchToProps)(AidOnBudgetList);
