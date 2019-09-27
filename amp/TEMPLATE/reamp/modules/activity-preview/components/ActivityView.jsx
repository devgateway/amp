import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Col, Grid, Row, Alert } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import Scrollspy from 'react-scrollspy';
import * as commonListsActions from '../actions/CommonListsActions';
import * as AC from '../utils/ActivityConstants';
import StatusBar from './sections/StatusBar';
import MainGroup from './MainGroup'
import SummaryGroup from './SummaryGroup';
import IconGroup from './IconGroup';
require('../styles/ActivityView.css');

/**
 *    
 */
export default class ActivityView extends Component { 
    
    constructor( props, context ) {
        super( props, context );
    }

	
	componentDidMount() {
        this.initializeFieldsAndActivity();
    }

    initializeFieldsAndActivity() {
        this.props.actions.getSettingsAndActivity(this.props.activityId);
    }

    _renderData() {
        const activity = this.props.activity[0];
        const activityInfo = this.props.activityInfo;
        const settings = this.props.settings;
        const translations = this.props.translations;
        const sections = AC.ACTIVITY_SECTION_IDS.map((section) => {
            return <li key={section.key}><a href={'javascript:openSection("'+section.hash+'")'}> 
                {translations[section.translationKey]} </a></li>;
          });
        const sectionKeys = AC.ACTIVITY_SECTION_IDS.map(section => section.key);
        const params = {
            activity: activity,
            translations: translations,
            settings: settings,
            activityInfo: activityInfo
        }
        const statusBarStyles = {
            inline : true,         
            fieldNameClass: 'preview_status_title',
            fieldValueClass: 'preview_status_detail',
            titleClass: 'status_title_class',
            groupClass: 'status_group_class'
        }
        return (
            <div className="preview_container">
                <div>
                    {this._getLegends(translations, activityInfo)}
                </div>
                <div className="l_divright">
                   <IconGroup activity={activity} translations={translations} 
                   settings={settings} activityInfo={activityInfo} />
                </div>
                <div className="preview_header">
                    <span className="preview_title">
                        {activity[AC.PROJECT_TITLE].value}
                    </span>
                    <div>
                        {this._checkDraft(activity, translations)}
                    </div>
                    <div>
                        {this._getValidations(translations, activityInfo)}
                    </div>
                    <div className="preview_status_container" >
                        <StatusBar params={params} styles={statusBarStyles} />
                    </div>
                    <div className={'preview_categories'} >
                        <Scrollspy items={sectionKeys} currentClassName={'preview_category_selected'}>
                        {sections}
                        </Scrollspy>
                    </div>
                </div>
                <div className={'preview_content'}>
                    <Grid fluid>
                        <Row>
                        <Col md={9} >
                            <MainGroup params={params} />
                        </Col>
                        <Col mdOffset={9} className={'preview_summary'} >
                            <SummaryGroup params={params}/>
                        </Col>
                        </Row>
                    </Grid>
                </div>
            </div>
        );
    }

	_hasSettings() {
        return this.props.isSettingsLoaded && this.props.settings;
    }
	
	_hasActivity() {
        return this.props.isActivityHydrated && this.props.activity;
    }
	
	_hasActivityInfo() {
        return this.props.isActivityInfoLoaded && this.props.activityInfo;
    }
    
    _getMessage() {
        let message = null;
        if (this.props.isActivityError === true) {
            message = (
                <Alert bsStyle="danger">
                    <strong>{this.props.translations['amp.activity-preview:activityError'] + this.props.errorMsg}</strong>
                </Alert>);
        } else if (this.props.isActivityHydratedLoading === true) {
            message = (
                <div className={'jumbotron'}>
                    <div className={'progress'}>
                        <div className={'progress-bar progress-bar-striped bg-info'}
                            role={'progressbar'}
                            aria-valuenow={'100'}
                            aria-valuemin={'0'}
                            aria-valuemax={'100'} style={{width: '100%'}}>
                        </div>
                    </div>
                </div>
            );
        } else if (this.props.isActivityHydrated === true) {
            if (!this.props.activity) {
                message = ( 
                    <Alert bsStyle="danger">
                        <strong>{this.props.translations['amp.activity-preview:activityUnexpectedError']}</strong>
                    </Alert>);
            }
        } 
        return message === null ? '' : <div>{message}</div>;
    }

    _getLegends(translations, activityInfo) {
        let alertMsg = activityInfo[AC.INFO_ACTIVITY_TEAM] && !activityInfo[AC.INFO_ACTIVITY_TEAM][AC.INFO_IS_PRIVATE] ? '' : 
        (
            <Alert bsStyle="warning">
                <strong>{translations['amp.activity-preview:isPrivate']}</strong>
            </Alert>
        );
        let ret = (
            <div>{alertMsg}</div>
        );
    return ret;
    }

    _checkDraft(activity, translations) {
        if (activity[AC.IS_DRAFT] && activity[AC.IS_DRAFT].value) {
            return (<Alert bsStyle="danger">
                <strong><li>{translations['draft_activity']}</li></strong>
            </Alert>);
        }
    }

    _getValidations(translations, activityInfo) {
        let msg = '';
        let lastVersionMsg;
        let addLink = false;

        switch (activityInfo[AC.INFO_VALIDATION_STATUS]) {
            case AC.AUTOMATIC_VALIDATION:
              msg = translations['automatic_validation'];
              break;
            case AC.AWAITING_VALIDATION:
              msg = translations['awaiting_validation'];
              break;
            case AC.CANNOT_BE_VALIDATE:
              if (activityInfo[AC.INFO_ACTIVITY_ID] !== activityInfo[AC.INFO_LAST_VERSION]) {
                lastVersionMsg = translations['not_latest_version'];
                addLink = true;
              }
              msg = translations['cannot_be_validated'];
              break;
        }

        let alertMsg = msg.length < 1 ? '' : 
        (
            <Alert bsStyle="danger">
                <strong><li>{msg}</li></strong>
                {lastVersionMsg &&
                    <div><strong><li>{lastVersionMsg}</li></strong></div>
                }
                {addLink && 
                    <div><li><a href={'/TEMPLATE/reamp/modules/activity-preview/index.html#/activity/' + 
                        activityInfo[AC.INFO_LAST_VERSION]}>{translations['click_latest_version']}
                    </a></li></div>
                }
            </Alert>
        );
        let ret = (
            <div>{alertMsg}</div>
        );
        return ret;
    }
    
    render() {
        const activityPreview = this._hasActivity() && this._hasSettings() && this._hasActivityInfo() ? this._renderData() : '';
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
        activityInfo: state.commonLists.activityInfo,
        settings: state.commonLists.settings,
        errorMsg: state.commonLists.errorMsg,
        isSettingsLoaded: state.commonLists.isSettingsLoaded,
        isActivityInfoLoaded: state.commonLists.isActivityInfoLoaded,
        isActivityError: state.commonLists.isActivityError,
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
