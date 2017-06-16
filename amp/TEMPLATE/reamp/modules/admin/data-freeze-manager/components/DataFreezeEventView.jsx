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
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import AppliedFilters from './AppliedFilters';
import moment from 'moment';
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
        this.toggleAppliedFilters = this.toggleAppliedFilters.bind(this);
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
        if (date) {
            result = moment(date, Constants.EP_DATE_FORMAT).format(this.getDisplayDateFormat());
        }

        return result
    }

    deleteDataFreezeEvent() {
        if (confirm(this.props.translations['amp.data-freezing:delete-prompt'])) {
            this.props.actions.deleteDataFreezeEvent(this.props.dataFreezeEvent);
        }
    }

    toggleAppliedFilters(event) {
        this.setState({
            showAppliedFilters: !this.state.showAppliedFilters
        });
    }

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
                      {this.props.dataFreezeEvent.filters && this.state.showAppliedFilters &&
                          <AppliedFilters {...this.props}/>
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
    return {
        actions: bindActionCreators(Object.assign({}, commonListsActions, dataFreezeActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventView);