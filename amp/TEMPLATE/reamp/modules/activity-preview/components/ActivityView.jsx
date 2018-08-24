import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as commonListsActions from '../actions/CommonListsActions';
import * as AC from '../utils/ActivityConstants';
import StatusBar from './sections/StatusBar';
require('../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
export default class ActivityView extends Component {
	
	constructor( props, context ) {
        super( props, context );
    }
	
	componentDidMount() {
        this.initializeFieldsAndActivity();
    }

    initializeFieldsAndActivity() {
        this.props.actions.getActivityAndFields(this.props.activityId);
    }

    _renderData() {
        const activity = this.props.activity;
        debugger;
        const translations = this.props.translations;
        return (
            <div className="preview_container">
                <div className="preview_header">
                    <span className="preview_title">
                        {activity[0][AC.PROJECT_TITLE].value}
                    </span>
                    <div className="preview_status_container" >
                        <StatusBar activity={activity}
                            translations={translations}
                            fieldNameClass="preview_status_title" 
                            fieldValueClass="preview_status_detail"
                            inline titleClass="status_title_class" 
                            groupClass="status_group_class" />
                    </div>
                </div>
            </div>
        );
    }

	
	_hasActivity() {
        return this.props.activity !== undefined && this.props.isActivityHydrated;
    }
    
    _getMessage() {
        let message = null;
        if (this.props.isActivityHydratedLoading === true) {
            message = this.props.translations['amp.activity-preview:activityLoading'];
        } else if (this.props.isActivityHydrated === true) {
            if (!this.props.activity) {
                message = this.props.translations['amp.activity-preview:activityUnexpectedError'];
            }
        } 
        return message === null ? '' : <h1>{message}</h1>;
    }
    
    render() {
        const activityPreview = this._hasActivity() ? this._renderData() : '';
        return (
            <div>
                {this._getMessage()}
                {activityPreview}
            </div>
        );
    }
}

function mapStateToProps( state, ownProps ) {
    return {
        activityId: ownProps.params.id,
        activity: state.commonLists.hydratedActivity,
        isActivityHydratedLoading: state.commonLists.isActivityHydratedLoading,
        isActivityHydrated: state.commonLists.isActivityHydrated,
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( ActivityView );
