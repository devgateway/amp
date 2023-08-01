import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {createSelector} from 'reselect'
import {ActivityPreviewUI, CurrencyRatesManager, ErrorHelper, FieldsManager} from 'amp-ui';
import * as ActivityActions from '../actions/ActivityActions';
import ActivityFundingTotals from '../utils/ActivityFundingTotals';
import Logger from '../utils/LoggerManager';
import DateUtils from '../utils/DateUtils';
import translate from '../utils/translate';
import * as ContactActions from '../actions/ContactsAction.jsx'
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {APDocumentPage} from '../containers/APDocumentPage.jsx';

const getActivityContext = createSelector(
  props => props.activityReducer,
  props => props.startUpReducer,
  (activityReducer, startUpReducer) => ({
      ...activityReducer.activityContext,
      calendar: startUpReducer.calendar,
  }));

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
        APDocumentPage: PropTypes.func.isRequired,
        resourceReducer: PropTypes.object,
        activityWsInfo: PropTypes.array.isRequired,
        globalSettings: PropTypes.object.isRequired,
        reportingTotals: PropTypes.object
    }

    constructor(props, context) {
        super(props, context);
    }

    componentWillMount() {
        this.props.actions.loadActivityForActivityPreview(this.props.activityId);
    }

    getChildContext() {
        const {activityFieldsManager, activityFundingTotals, reportingTotals} = this.props.activityReducer;
        const {contactsByIds, contactFieldsManager} = this.props.contactReducer;
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
            APDocumentPage,
            resourceReducer: this.props.resourceReducer,
            activityWsInfo: this.props.activityReducer.activityWsInfo,
            globalSettings: this.props.startUpReducer.globalSettings,
            reportingTotals
        };
    }

    render() {
        if (this.props.activityReducer.isActivityLoading) {
            return (<div className={'jumbotron'}>
                <div className={'progress'}>
                    <div className={'progress-bar progress-bar-striped bg-info'}
                         role={'progressbar'}
                         aria-valuenow={'100'}
                         aria-valuemin={'0'}
                         aria-valuemax={'100'} style={{width: '100%'}}>{translate('Loading')}
                    </div>
                </div>
            </div>)
        } else {
            const {activity, error} = this.props.activityReducer;
            const activityContext = getActivityContext(this.props);
            if (error) {
                return ErrorHelper.showErrors(error, translate);
            } else {
                return (
                    <ActivityPreviewUI
                        activity={activity}
                        activityContext={activityContext}
                        messageInformation={this.props.messageInformation}
                        isOnline={true}
                    />
                );
            }
        }
    }
}

function mapStateToProps(state, ownProps) {
    return {
        activityId: ownProps.params.activityId,
        activityReducer: state.activityReducer,
        contactReducer: state.contactReducer,
        resourceReducer: state.resourceReducer,
        startUpReducer: state.startUpReducer,
        messageInformation: state.routing.locationBeforeTransitions.query
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, ActivityActions), dispatch)};
}

export default connect(mapStateToProps, mapDispatchToProps)(App);


