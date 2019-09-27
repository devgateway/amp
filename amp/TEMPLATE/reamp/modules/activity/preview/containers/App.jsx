import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {ActivityPreviewUI, FieldsManager, FeatureManager} from 'amp-ui';
import Home from "./Home";
import * as ActivityActions from '../actions/ActivityActions';
import activityContext from '../jsons/activityContext.json';
import Logger from '../tempUtils/LoggerManager';
import DateUtils from '../tempUtils/DateUtils';
import translate from '../tempUtils/translate';
import ActivityFundingTotals from '../tempUtils/ActivityFundingTotals';
// { getAmountsInThousandsMessage, rawNumberToFormattedString }
import NumberUtils from '../tempUtils/NumberUtils';
import ContactActions from '../tempUtils/ContactsActions';
import contactsByIds from '../jsons/ContactsById.json'
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

/**
 *
 */
class App extends Component {
    static childContextTypes = {
        Logger: PropTypes.func.isRequired,
        translate: PropTypes.func.isRequired,
        activityFieldsManager: PropTypes.instanceOf(FieldsManager),
        DateUtils: PropTypes.func.isRequired,
        activityFundingTotals: PropTypes.instanceOf(ActivityFundingTotals),
        getAmountsInThousandsMessage: PropTypes.func.isRequired,
        rawNumberToFormattedString: PropTypes.func.isRequired,
        getActivityContactIds: PropTypes.func.isRequired,
        contactsByIds: PropTypes.object,

    }

    constructor(props, context) {
        super(props, context);
    }

    componentWillMount() {
        this.props.actions.loadActivityForActivityPreview(this.props.activityId);
    }

    getChildContext() {
        const activityFieldsManager = this.props.activityReducer.activityFieldsManager;
        const activityFundingTotals = new ActivityFundingTotals(null, null, null, null);
        console.log(NumberUtils.getAmountsInThousandsMessage);
        return {
            activityFieldsManager,
            Logger,
            translate,
            DateUtils,
            activityFundingTotals,
            getAmountsInThousandsMessage: NumberUtils.getAmountsInThousandsMessage,
            rawNumberToFormattedString: NumberUtils.rawNumberToFormattedString,
            getActivityContactIds: ContactActions.getActivityContactIds,
            contactsByIds
        }
    }

    render() {
        console.log(this.props);
        if (this.props.activityReducer.isActivityLoading) {
            return (<div> LOADING ACTIVITY </div>)
        } else {
            const activity = this.props.activityReducer.activity;
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


