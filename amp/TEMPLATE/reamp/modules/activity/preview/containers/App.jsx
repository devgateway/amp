import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {ActivityPreviewUI, FieldsManager, CurrencyRatesManager, ErrorHelper} from 'amp-ui';
import * as ActivityActions from '../actions/ActivityActions';
import ActivityFundingTotals from '../utils/ActivityFundingTotals';
import Logger from '../utils/LoggerManager';
import DateUtils from '../utils/DateUtils';
import translate from '../utils/translate';
import * as ContactActions from '../actions/ContactsAction.jsx'
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {APDocumentPage} from '../containers/APDocumentPage.jsx';

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
        activityWsInfo: PropTypes.array.isRequired

    }

    constructor(props, context) {
        super(props, context);
    }

    componentWillMount() {
        this.props.actions.loadActivityForActivityPreview(this.props.activityId);
    }

    getChildContext() {
        const {activityFieldsManager, activityFundingTotals} = this.props.activityReducer;
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
            activityWsInfo: this.props.activityReducer.activityWsInfo
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
            const {activity, activityContext, error} = this.props.activityReducer;
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


