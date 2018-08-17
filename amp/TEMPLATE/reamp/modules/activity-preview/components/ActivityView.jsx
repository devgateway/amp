import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as commonListsActions from '../actions/CommonListsActions';

export default class ActivityView extends Component {
	
	constructor( props, context ) {
        super( props, context );
    }
	
	componentDidMount() {
        this.initializeFieldsAndActivity();
    }

    initializeFiltersAndSettings() {
        this.props.actions.getFields();
        this.props.actions.getActivity(activityId);
    }

	
	render() {
	}
}

function mapStateToProps( state, ownProps ) {
    return {
        activity: state.commonLists.activity,
        fields: state.commonLists.fields,
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( ActivityView );
