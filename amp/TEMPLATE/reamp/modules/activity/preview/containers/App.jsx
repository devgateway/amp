import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {ActivityPreviewUI, FieldsManager, FeatureManager} from 'amp-ui';
import Home from "./Home";
import activity from '../jsons/activity.json';
import activityContext from '../jsons/activityContext.json';
import fmTree from '../jsons/fmTree.json';
import fieldsDef from '../jsons/fieldsDef.json';
import possibleValuesCollection from '../jsons/possibleValuesCollection.json';
import Logger from '../tempUtils/LoggerManager';
import DateUtils from '../tempUtils/DateUtils';
import translate from '../tempUtils/translate';
import ActivityFundingTotals from '../tempUtils/ActivityFundingTotals';
// { getAmountsInThousandsMessage, rawNumberToFormattedString }
import NumberUtils from '../tempUtils/NumberUtils';
import  ContactActions from '../tempUtils/ContactsActions';
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
        FeatureManager.setFMTree(fmTree);
        FeatureManager.setLoggerManager(Logger);
    }

    getChildContext() {
        const activityFieldsManager = new FieldsManager(fieldsDef, possibleValuesCollection, 'en',
            Logger);
        const activityFundingTotals = new ActivityFundingTotals(null, null, null, null);
        console.log(NumberUtils.getAmountsInThousandsMessage);
        debugger;
        return {
            activityFieldsManager,
            Logger,
            translate,
            DateUtils,
            activityFundingTotals,
            getAmountsInThousandsMessage: NumberUtils.getAmountsInThousandsMessage,
            rawNumberToFormattedString:NumberUtils.rawNumberToFormattedString,
            getActivityContactIds:ContactActions.getActivityContactIds,
            contactsByIds
        }
    }

    render() {
        return (
            <ActivityPreviewUI activity={activity} activityContext={activityContext} />
        );
    }
}

function mapStateToProps(state, ownProps) {
    console.log(ownProps.params.activityId);
    return{}
    /*return {
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }*/
}

function mapDispatchToProps(dispatch) {
    //return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) };
    return {actions: bindActionCreators({}, dispatch)};
}

export default connect(mapStateToProps, mapDispatchToProps)(App);


