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
import {OverlayTrigger, Tooltip} from "react-bootstrap";
require('../styles/less/main.less');
class DataFreezeEventView extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            showAppliedFilters: false
        };
        this.toDisplayDateFormat = this.toDisplayDateFormat.bind(this);
        this.getDisplayDateFormat = this.getDisplayDateFormat.bind(this);
        this.deleteDataFreezeEvent = this.deleteDataFreezeEvent.bind(this);
        this.toggleAppliedFilters = this.toggleAppliedFilters.bind(this);
        this.getFilterObject = this.getFilterObject.bind(this);
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

    getFilterObject(){
        var filters;
        if(this.props.dataFreezeEvent.filters){
            filters = JSON.parse(this.props.dataFreezeEvent.filters);
        }
        return filters;
    }

    showButtonWithTooltip(button, cssIcon, event) {
        let tooltip = <Tooltip id={button + '-icon-tooltip'}>{this.props.translations['amp.data-freezing:tooltip-' + button]}</Tooltip>;
        return (
            <OverlayTrigger trigger={['hover', 'focus']} placement="top" overlay={tooltip}>
                <span className={'glyphicon glyphicon-custom ' + cssIcon} onClick={event}></span>
            </OverlayTrigger>
        )
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
            let filterObject = this.getFilterObject();
            return (
                <tr >
                    <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.freezingDate)}</td>
                    <td>{this.props.dataFreezeEvent.gracePeriod}</td>
                    <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodStart)}</td>
                    <td className="date-column">{this.toDisplayDateFormat(this.props.dataFreezeEvent.openPeriodEnd)}</td>
                    <td>{this.showFreezeOption(this.props.dataFreezeEvent.freezeOption)}</td>
                    <td>
                    {this.props.dataFreezeEvent.sendNotification ? this.props.translations['amp.data-freezing:boolean-option-yes'] : this.props.translations['amp.data-freezing:boolean-option-no']}
                    </td>
                    <td>
                    {this.props.dataFreezeEvent.notificationDays}
                    </td>
                    <td>
                     {filterObject && Object.keys(filterObject.filters).length > 0 &&
                      <button className="btn btn-default filter-add" onClick={this.toggleAppliedFilters}>
                      <span className={this.state.showAppliedFilters ? 'glyphicon glyphicon-chevron-up' : 'glyphicon glyphicon-chevron-down'}></span>
                     </button>
                      }
                      {filterObject && Object.keys(filterObject.filters).length > 0 &&  this.state.showAppliedFilters &&
                          <AppliedFilters {...this.props}/>
                      }
                    </td>
                    <td> {this.props.dataFreezeEvent.enabled ? this.props.translations['amp.data-freezing:boolean-option-yes'] : this.props.translations['amp.data-freezing:boolean-option-no']}</td>
                    <td className="action-column">
                    {this.props.dataFreezeEvent.executed != true &&
                        <div>
                            {this.showButtonWithTooltip("edit", "glyphicon-pencil", this.props.edit)}
                            {this.showButtonWithTooltip("delete", "glyphicon-trash", this.deleteDataFreezeEvent)}
                        </div>
                    }

                    {this.props.dataFreezeEvent.executed == true &&
                        <div>
                            {this.showButtonWithTooltip("edit", "glyphicon-pencil glyphicon-disabled", this.props.edit)}
                            {this.showButtonWithTooltip("delete", "glyphicon-trash glyphicon-disabled", this.deleteDataFreezeEvent)}
                       </div>
                    }
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
