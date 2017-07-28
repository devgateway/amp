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
import {
    OverlayTrigger
} from 'react-bootstrap';
import {
    Tooltip
} from 'react-bootstrap';
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions';
import * as Constants from '../common/Constants';
require('../styles/less/main.less');

export default class PerformanceAlertRuleList extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};        
    }

    componentWillMount() {        
    }  
    
    render() {
        return (
            <div>
                Some Content Here
            </div>
        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        paging: state.performanceAlertRule.paging,
        sorting: state.performanceAlertRule.sorting,
        errors: state.performanceAlertRule.errors || [],
        infoMessages: state.performanceAlertRule.infoMessages || [],
        settings: state.commonLists.settings || {},
        PerformanceAlertRuleList: state.performanceAlertRule.performanceAlertRuleList
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, performanceAlertRuleActions, commonListsActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceAlertRuleList);