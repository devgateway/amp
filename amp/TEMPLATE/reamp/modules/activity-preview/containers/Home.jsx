import React, { Component } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as commonListsActions from '../actions/CommonListsActions';
require('../styles/main.less');

/**
 *    
 */
export default class App extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { currentActivity: null};
    }

   
    render() { 
      
      return (
            <div>             
                <div className="title-bar">
                    <h2>{this.props.translations['amp.activity-preview:main-title']}</h2>
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
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) };
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

