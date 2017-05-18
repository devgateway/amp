import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import { Modal } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
export default class RemarksPopup extends Component {
    constructor( props, context ) {
        super( props, context );        
    }
    componentDidMount() {
        this.props.actions.clearRemarks(this.props.code);
        this.props.actions.fetchRemarks(this.props.code, this.props.remarksUrl);        
    }
    render() {
        console.log(this.props.remarks);    
        return (<Modal show={this.props.showRemarks} onHide={this.props.closeRemarksModal} container={this}>
                <Modal.Header closeButton>
                  <Modal.Title>{this.props.translations['amp-gpi-reports:remarks']}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  <h1> Hello World!</h1>
                </Modal.Body>
                <Modal.Footer>
                  <Button onClick={this.props.closeRemarksModal}>{this.props.translations['amp-gpi-reports:close']}</Button>
                </Modal.Footer>
              </Modal>)
    }
}

function mapStateToProps( state, ownProps ) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        remarks: state.reports['5a'].remarks
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( RemarksPopup );
