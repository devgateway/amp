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
import DataFreezeEventEdit from '../components/DataFreezeEventEdit';
import DataFreezeEventView from '../components/DataFreezeEventView';
import * as Constants from '../common/Constants';
export default class DataFreezeEventRow extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            dataFreezeEvent: this.props.dataFreezeEvent,
        };
        this.edit = this.edit.bind(this);
        this.cancel = this.cancel.bind(this);
    }

    edit() {
        var inEdit = this.props.dataFreezeEventList.filter(dataFreezeEvent => {
            return dataFreezeEvent.isEditing
        });
        if (inEdit.length == 0) {
            const dataFreezeEvent = this.props.dataFreezeEvent;
            var origDataFreezeEvent = Object.assign({}, dataFreezeEvent);
            dataFreezeEvent.isEditing = true;
            this.setState({
                origDataFreezeEvent: origDataFreezeEvent
            });
            this.props.actions.updateDataFreezeEvent(dataFreezeEvent);
        }
    }

    cancel() {
        const origDataFreezeEvent = this.state.origDataFreezeEvent;
        if (origDataFreezeEvent && origDataFreezeEvent.id) {
            this.props.actions.updateDataFreezeEvent(origDataFreezeEvent);
        } else {
            this.props.actions.removeFromState(this.props.dataFreezeEvent);
        }
    }

    render() {
        if (this.props.dataFreezeEvent.isEditing && this.props.context === Constants.DATA_FREEZE_EVENTS) {
            return (<DataFreezeEventEdit cancel={this.cancel} {...this.props}/>);
        } else {
            return (<DataFreezeEventView edit={this.edit} {...this.props}/>);
        }
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        settings: state.commonLists.settings,
        dataFreezeEventList: state.dataFreeze.dataFreezeEventList
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, commonListsActions, dataFreezeActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventRow);