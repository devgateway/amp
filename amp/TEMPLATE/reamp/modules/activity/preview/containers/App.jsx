import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {ActivityPreviewUI, FieldsManager, CurrencyRatesManager } from 'amp-ui';
import * as ActivityActions from '../actions/ActivityActions';
import ActivityFundingTotals from '../utils/ActivityFundingTotals';
import Logger from '../tempUtils/LoggerManager';
import DateUtils from '../utils/DateUtils';
import translate from '../utils/translate';
import * as ContactActions from '../actions/ContactsAction.jsx'
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
        contactFieldsManager: PropTypes.instanceOf(FieldsManager),
        DateUtils: PropTypes.func.isRequired,
        activityFundingTotals: PropTypes.instanceOf(ActivityFundingTotals),
        getActivityContactIds: PropTypes.func.isRequired,
        currencyRatesManager: PropTypes.instanceOf(CurrencyRatesManager),
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
        const { contactsByIds, contactFieldsManager } = this.props.contactReducer;
        return {
            activityFieldsManager,
            Logger,
            translate,
            DateUtils,
            activityFundingTotals,
            getActivityContactIds: ContactActions.getActivityContactsId,
            contactsByIds,
            contactFieldsManager,
            currencyRatesManager: this.props.activityReducer.currencyRatesManager,
        }
    }

    render() {
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
        activityReducer: state.activityReducer,
        contactReducer: state.contactReducer
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, ActivityActions), dispatch)};
}

export default connect(mapStateToProps, mapDispatchToProps)(App);


