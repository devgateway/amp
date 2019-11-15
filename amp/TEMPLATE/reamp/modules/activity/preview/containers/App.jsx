import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {ActivityPreviewUI, FieldsManager } from 'amp-ui';
import * as ActivityActions from '../actions/ActivityActions';
import Logger from '../tempUtils/LoggerManager';
import DateUtils from '../tempUtils/DateUtils';
import translate from '../tempUtils/translate';
import ContactActions from '../tempUtils/ContactsActions';
import contactsByIds from '../jsons/ContactsByIds.json'
import { bindActionCreators } from "redux";
import { connect } from "react-redux";

/**
 *
 */
class App extends Component {
    static childContextTypes = {
        Logger: PropTypes.func.isRequired,
        translate: PropTypes.func.isRequired,
        activityFieldsManager: PropTypes.instanceOf(FieldsManager),
        DateUtils: PropTypes.func.isRequired,
        activityFundingTotals: PropTypes.object.isRequired,
        getActivityContactIds: PropTypes.func.isRequired,
        currencyRatesManager: PropTypes.func.isRequired,
        contactsByIds: PropTypes.object,

    }

    constructor(props, context) {
        super(props, context);
    }

    componentWillMount() {
        this.props.actions.loadActivityForActivityPreview(this.props.activityId);
    }

    getChildContext() {
        const { activityFieldsManager , activityFundingTotals} = this.props.activityReducer;

        return {
            activityFieldsManager,
            Logger,
            translate,
            DateUtils,
            activityFundingTotals,
            getActivityContactIds: ContactActions.getActivityContactIds,
            contactsByIds,
            currencyRatesManager: this.props.activityReducer.currencyRatesManager,
        }
    }

    render() {
        console.log(this.props);
        if (this.props.activityReducer.isActivityLoading) {
            return (<div> LOADING ACTIVITY </div>)
        } else {
            const { activity, activityContext } = this.props.activityReducer;
            return (
                <ActivityPreviewUI activity={activity} activityContext={activityContext}/>
            );
        }
    }
}

function mapStateToProps(state, ownProps) {
    return {
        activityId: ownProps.params.activityId,
        activityReducer: state.activityReducer
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, ActivityActions), dispatch)};
}

export default connect(mapStateToProps, mapDispatchToProps)(App);


