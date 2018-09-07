import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Col, Grid, Row } from 'react-bootstrap';
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
        this.props.actions.getActivityAndFields(this.props.activityId);
    }

    _renderData() {
        const activity = this.props.activity[0];
        const translations = this.props.translations;
        const sections = AC.ACTIVITY_SECTION_IDS.map((section) => {
            return <li key={section.key}><a href={section.hash}> {translations[section.translationKey]} </a></li>;
          });
        const sectionKeys = AC.ACTIVITY_SECTION_IDS.map(section => section.key);
        const params = {
            activity: activity,
            translations : translations
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
                <div className="preview_header">
                    <span className="preview_title">
                        {activity[AC.PROJECT_TITLE].value}
                    </span>
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

	
	_hasActivity() {
        return this.props.activity !== undefined && this.props.isActivityHydrated;
    }
    
    _getMessage() {
        let message = null;
        if (this.props.isActivityError === true) {
            message = (<h1>{this.props.translations['amp.activity-preview:activityError'] + 
                this.props.errorMsg}</h1> );
        } else if (this.props.isActivityHydratedLoading === true) {
            message = (<div>
                <h1>{this.props.translations['amp.activity-preview:activityLoading']}</h1>
                <div className={'jumbotron'}>
                    <div className={'progress'}>
                        <div className={'progress-bar progress-bar-striped bg-info'}
                            role={'progressbar'}
                            aria-valuenow={'100'}
                            aria-valuemin={'0'}
                            aria-valuemax={'100'} style={{width: '100%'}}>
                        </div>
                    </div>
                </div></div>
            );
        } else if (this.props.isActivityHydrated === true) {
            if (!this.props.activity) {
                message = (<h1>{this.props.translations['amp.activity-preview:activityUnexpectedError']}</h1>);
            }
        } 
        return message === null ? '' : <div>{message}</div>;
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
        errorMsg: state.commonLists.errorMsg,
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
