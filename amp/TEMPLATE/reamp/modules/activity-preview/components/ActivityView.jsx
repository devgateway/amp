import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Col, Grid, Row, Button, Alert } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import Scrollspy from 'react-scrollspy';
import * as commonListsActions from '../actions/CommonListsActions';
import * as AC from '../utils/ActivityConstants';
import StatusBar from './sections/StatusBar';
import MainGroup from './MainGroup'
import SummaryGroup from './SummaryGroup';
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
        this.props.actions.getSettings();
        this.props.actions.getActivityAndFields(this.props.activityId);
    }

    _renderData() {
        const activity = this.props.activity[0];
        const activityInfo = this.props.activityInfo;
        const settings = this.props.settings;
        const translations = this.props.translations;
        const sections = AC.ACTIVITY_SECTION_IDS.map((section) => {
            return <li key={section.key}><a href={section.hash}> {translations[section.translationKey]} </a></li>;
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
                    {this._getExportOptions(activity, translations, settings)}
                </div>
                <div className="l_divright">
                    {this._getEditOptions(activity, translations, activityInfo)}
                </div>
                <div className="preview_header">
                    <span className="preview_title">
                        {activity[AC.PROJECT_TITLE].value}
                    </span>
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

    _getExportOptions(activity, translations, settings) {
        let word = settings && settings[AC.HIDE_EXPORT] ? '' : 
            (
                <a href={'/aim/exportActToWord.do?activityid=' + activity[AC.INTERNAL_ID].value} className="l_sm"
                    title={translations['amp.activity-preview:exportWord']} target="_blank">
                    <img src="/TEMPLATE/ampTemplate/img_2/ico_word.gif"/>{translations['amp.activity-preview:exportWord']}
                </a>
            );
        let ret = (
            <div>
                <a href={'/aim/exportActToPDF.do?activityid='+ activity[AC.INTERNAL_ID].value} className="l_sm"
                    title={translations['amp.activity-preview:exportPDF']} target="_blank">
                    <img src="/TEMPLATE/ampTemplate/img_2/ico_pdf.gif"/>{translations['amp.activity-preview:exportPDF']}
                </a>
                {word}
                <a href={'/showPrinterFriendlyPage.do?edit=true&activityid='+ activity[AC.INTERNAL_ID].value} className="l_sm" 
                    title={translations['amp.activity-preview:print']} target="_blank">
                    <img src="/TEMPLATE/ampTemplate/img_2/ico_print.gif"/>{translations['amp.activity-preview:print']}
                </a>
            </div>
        );        
        return ret;
    }

    _getEditOptions(activity, translations, activityInfo) {
        let msg = activityInfo && activityInfo[AC.INFO_VALIDATE] ? translations['amp.activity-preview:validate'] : translations['amp.activity-preview:edit'];
        let edit = activityInfo && !activityInfo[AC.INFO_EDIT] ? '' : 
            (
                <Button href={'/wicket/onepager/activity/' + activity[AC.INTERNAL_ID].value} 
                    bsStyle="primary" bsSize="small">{msg}
                </Button>
            );
        let ret = (
            <div>{edit}</div>
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
