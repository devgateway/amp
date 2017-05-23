import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions';
require('../styles/main.less');
require('bootstrap');
import * as Constants from '../common/Constants';
import Report9b from '../components/Report9b';
import Report6 from '../components/Report6';
import Report5b from '../components/Report5b';
import Report5a from '../components/Report5a';
import Report1Output2 from '../components/Report1Output2';

export default class App extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { currentReport: null };
        this.tabChanged = this.tabChanged.bind( this );
    }

    componentDidMount() {
        this.props.actions.fetchReportVisibilityConfiguration();
    }

    tabChanged( event ) {
        this.setState( { currentReport: $( event.target ).data("indicator") });
    }

    isVisible( code ) {
        return this.props.reportVisibility[code] === true;
    }
    
   getReport(code){       
        switch (code) {               
        case Constants.INDICATOR_1_CODE: 
            return <Report1Output2/>;
        case Constants.INDICATOR_5A_CODE:
            return <Report5a/>;
        case Constants.INDICATOR_5B_CODE:
            return <Report5b/>;
        case Constants.INDICATOR_6_CODE:
            return <Report6/>;
        case Constants.INDICATOR_9B_CODE:
            return <Report9b/>;
        default:            
            return <div></div>;
        }
    }
   
    render() { 
      let visibleReports = Constants.INDICATOR_CODE_LIST.filter(indicatorCode =>{return this.props.reportVisibility[indicatorCode] == true});
      let currentReport = this.state.currentReport || (visibleReports.length > 0 ? visibleReports[0] : null)
      return (
            <div >             
             <div className="title-bar">
             <div className="container">
               <h2>{this.props.translations['amp.gpi-reports:title']}</h2>
             </div>
           </div>
             <div >
             <div>
              <ul className="nav nav-tabs indicator-tabs" role="tablist">    
               {visibleReports.map( indicatorCode =>               
                  <li role="presentation" className={currentReport == indicatorCode  ? "active" : ''} ><a href={'#indicator' + indicatorCode} aria-controls={'indicator' + indicatorCode} role="tab" data-toggle="tab" data-indicator={indicatorCode} onClick={this.tabChanged}>{this.props.translations['amp.gpi-reports:indicator'+ indicatorCode +'-title']}</a></li>
                )}             
             </ul>
             <div className="tab-content panel">               
               {visibleReports.map( indicatorCode => 
                 <div role="tabpanel" className={currentReport == indicatorCode ? "tab-pane active" : 'tab-pane'} id={'indicator'+ indicatorCode}>
                   {this.getReport(indicatorCode)}
                 </div>
               )}

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
        translate: state.startUp.translate,
        reportVisibility: state.commonLists.reportVisibility
    }
}

function mapDispatchToProps(dispatch) {
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) };
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

