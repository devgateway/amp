import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import * as Constants from '../common/Constants';
import DataFreezeEventList from '../components/DataFreezeEventList';
export default class UnfreezeAll extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {}; 
        this.unfreezeAll = this.unfreezeAll.bind(this);
    }
    
    componentWillMount() {       
    }  
    
    unfreezeAll(){
        if(confirm(this.props.translations['amp.data-freeze-event:unfreeze-all-confirm'])){
           this.props.actions.unfreezeAll();
        }
    }
    
    render() {      
        const pages = ([...Array(this.props.paging.totalPageCount + 1).keys()]).slice(1);    
        return (
                <div className="container-fluid">
                 <div className="text-center">
                   <img src="styles/images/alert.svg" className="system-icon"/>
                   <p className="padded">{this.props.translations['amp.data-freezing:unfreeze-all-desc']}</p>
                   <br/>
                   <button type="button" className="btn btn-danger" onClick={this.unfreezeAll}>{this.props.translations['amp.data-freezing:unfreeze-all']}</button>                   
                   <DataFreezeEventList context={Constants.UNFREEZE_ALL}/>
                </div>
               </div>            
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {        
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        errors: state.dataFreeze.errors || [],
        infoMessages: state.dataFreeze.infoMessages || [],
        paging: state.dataFreeze.paging
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, dataFreezeActions), dispatch)}  
}

export default connect(mapStateToProps, mapDispatchToProps)(UnfreezeAll);
