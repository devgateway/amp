import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
require('../styles/less/main.less');
import * as startUp from '../actions/StartUpAction';
import DataFreezeEventForm from '../components/DataFreezeEventForm';
import DataFreezeEventsList from '../components/DataFreezeEventsList';
import UnfreezeAll from '../components/UnfreezeAll';
export default class App extends Component {
    constructor(props, context) {      
        super(props, context);
    }
    
    componentWillMount() {     
    }
   
    render() {             
        return (
            <div>
                <div className="container">
                <div className="col-md-6">
                  <h2 className="pull-left">Data Freezing Manager</h2>
                </div>
                <div className="col-md-6">
                  <span className="required pull-right">* Required Fields</span>
                </div>
                </div>
                  
                  <div className="container">
                  <ul className="nav nav-tabs indicator-tabs">
                    <li role="presentation" className="active"><a href="#data-freezing"  aria-controls="data-freezing" role="tab" data-toggle="tab">+ Add New Freezing Event</a>
                    </li>
                    <li role="presentation"><a href="#unfreeze-all" aria-controls="unfreeze-all" role="tab" data-toggle="tab">Unfreeze All</a>
                    </li>
                  </ul>
                    
                  <div className="tab-content">
                     <div id="data-freezing" className="tab-pane fade in active">                    
                       <DataFreezeEventForm /> 
                       <DataFreezeEventsList/>   
                     </div>
                    <div id="unfreeze-all" className="tab-pane fade in">
                        <UnfreezeAll/>
                    </div>
                    
                    </div>
                    </div>
                            
                    
                    
            </div>
            
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {       
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators({}, dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

