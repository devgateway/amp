import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
export default class UnfreezeAll extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {};        
    }
    
    componentWillMount() {        
    }  
    
    render() {       
        return (
                <div className="container-fluid">
                 <div className="text-center">
                   <img src="styles/images/alert.svg" className="system-icon"/>
                   <p className="padded">This option will allow you to unfreeze all events and activities.</p>
                   <h4 className="padded">Are you sure you want to unfreeze all activities?</h4>
                   <br/>
                   <button type="button" className="btn btn-danger">Unfreeze All</button>
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

export default connect(mapStateToProps, mapDispatchToProps)(UnfreezeAll);
