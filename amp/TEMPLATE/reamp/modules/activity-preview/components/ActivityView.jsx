import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as commonListsActions from '../actions/CommonListsActions';
import styles from './ActivityView.css';
import * as AC from '../utils/ActivityConstants';

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
        this.props.actions.getFields();
        this.props.actions.getActivity(this.props.activityId);
    }

    _renderData() {
        const activity = this.props.activity;
        return (
            <div className={styles.preview_container}>
                <div className={styles.preview_header} >
                    <span className={styles.preview_title} >{activity[AC.PROJECT_TITLE]}</span>
                </div>
            </div>
        );
    }

	
	_hasActivity() {
        return this.props.activity !== undefined && this.props.activity !== null;
    }
    
    _getMessage() {
        let message = null;
        if (this.props.isActivityLoading === true) {
            message = this.props.translations['amp.activity-preview:activityLoading'];
        } else if (this.props.isActivityLoaded === true) {
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
        activity: state.commonLists.activity,
        isActivityLoading: state.commonLists.isActivityLoading,
        isActivityLoaded: state.commonLists.isActivityLoaded,
        fields: state.commonLists.fields,
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( ActivityView );
