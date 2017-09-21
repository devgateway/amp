import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import AidOnBudgetList from "../components/AidOnBudgetList.jsx";
import DonorNotesList from "../components/DonorNotesList.jsx"; 
import * as commonListsActions from  '../actions/CommonListsActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';

export default class App extends Component {

    constructor(props, context) {      
        super(props, context);
    }
    
    componentWillMount() {       
        this.props.actions.getOrgList(true);
        this.props.actions.getUserInfo();  
    }
   
    render() {             
        return (
            <div >
                <div>
                 <ul className="nav nav-tabs indicator-tabs" role="tablist">                  
                {this.props.verifiedOrgList.length > 0 && <li role="presentation" className="active"><a href="#indicator1" aria-controls="indicator1" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-data-donor-notes-indicator1:title']}</a></li>}
                {this.props.verifiedOrgList.length > 0 && <li role="presentation" ><a href="#indicator5a" aria-controls="indicator1" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-data-donor-notes-indicator5a:title']}</a></li>}                
                {this.props.userInfo['national-coordinator'] &&  <li role="presentation" className = {this.props.verifiedOrgList.length == 0 ? 'active' : ''} ><a href="#indicator6" aria-controls="indicator6" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-data-aid-on-budget:title']}</a></li>}
                </ul>

                <div className="tab-content panel">
                  {this.props.verifiedOrgList.length > 0 &&
                      <div role="tabpanel" className="tab-pane active" id="indicator1"><DonorNotesList indicatorCode="1"/></div>
                  }   
                  {this.props.verifiedOrgList.length > 0 &&
                      <div role="tabpanel" className="tab-pane" id="indicator5a"><DonorNotesList indicatorCode="5a"/></div>
                  }
                  {this.props.userInfo['national-coordinator'] && 
                      <div role="tabpanel" className = {this.props.verifiedOrgList.length == 0 ? 'tab-pane active' : 'tab-pane'} id="indicator6">
                       <AidOnBudgetList/>
                     </div>   
                  }
                </div>

              </div>
                 
            </div>
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {
        verifiedOrgList: state.commonLists.verifiedOrgList || [],
        userInfo: state.commonLists.userInfo || {},
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, commonListsActions), dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

