import React, {
    Component,
    PropTypes
} from 'react';
import {
    connect
} from 'react-redux';
import {
    bindActionCreators
} from 'redux';
require('bootstrap');
require('../styles/less/main.less');
import * as startUp from '../actions/StartUpAction';
import DataFreezeEventList from '../components/DataFreezeEventList';
import UnfreezeAll from '../components/UnfreezeAll';
import * as Constants from '../common/Constants';
import * as commonListsActions from  '../actions/CommonListsActions.jsx';
class App extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            currentTab: 'data-freezing'
        }
        this.tabChanged = this.tabChanged.bind(this);
    }

    componentWillMount() {
        this.props.actions.getUserInfo().then(function() {
          if (this.props.user['is-admin'] != true) {
              window.location.href = '/';
          }
        }.bind(this));
    }

    tabChanged(event) {
        this.setState({
            currentTab: $(event.target).data("tab")
        });
    }
    render() {
        return (
            <div>
                <div className="data-freeze-container">
                <div className="container title-container" >
                <h2 >{this.props.translations['amp.data-freezing:data-freeze-manager']}</h2>
                </div>
                <div className="container">
                </div>
                  <div className="container" style={{width:'90%'}}>
                  <ul className="nav nav-tabs indicator-tabs" data-tabs="tabs">
                    <li role="presentation" className={this.state.currentTab == 'data-freezing' ? 'active' : ''}><a data-tab="data-freezing" role="tab" data-toggle="tab" onClick={this.tabChanged}>{this.props.translations['amp.data-freezing:add-freezing-event']}</a>
                    </li>
                    <li role="presentation" className={this.state.currentTab == 'unfreeze-all' ? 'active' : ''}><a  data-tab="unfreeze-all" role="tab" data-toggle="tab" onClick={this.tabChanged}>
                    {this.props.translations['amp.data-freezing:unfreeze-all']}</a>
                    </li>
                  </ul>

                  <div className="tab-content panel">
                     <div id="data-freezing" className={this.state.currentTab === 'data-freezing' ? 'tab-pane fade in active' : 'tab-pane fade in' }>
                        <DataFreezeEventList context={Constants.DATA_FREEZE_EVENTS}/>
                     </div>
                    <div id="unfreeze-all" className={this.state.currentTab === 'unfreeze-all' ? 'tab-pane fade in active' : 'tab-pane fade in' }>
                        <UnfreezeAll/>
                    </div>

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
        user: state.commonLists.user || {}
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, commonListsActions), dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
