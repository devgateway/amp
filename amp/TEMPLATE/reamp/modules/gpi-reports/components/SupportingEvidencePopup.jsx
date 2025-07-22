import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import { Modal } from 'react-bootstrap';
import * as Constants from '../common/Constants';
class SupportingEvidencePopup extends Component {
    constructor( props, context ) {
        super( props, context );
    }
    componentDidMount() {
        this.props.actions.clearSupportingEvidence(this.props.code);
        let data = [];
        if(this.props.rowData && this.props.rowData[Constants.DONOR_ID] && this.props.rowData[Constants.ACTIVITY_ID]) {
            data.push({donorId: this.props.rowData[Constants.DONOR_ID], activityId: this.props.rowData[Constants.ACTIVITY_ID]});
            this.props.actions.fetchSupportingEvidence(this.props.code, data);
        }

    }

    openDocument(event) {
        window.open($(event.target).data("url"), '_blank');
    }

    render() {
        return (<Modal  container={this} show={true} onHide={this.props.toggleSupportingEvidence}>
                <Modal.Header closeButton>
                <Modal.Title>{this.props.translations['amp-gpi-reports:supporting-evidence']}</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                <div >
                  {this.props.supportingEvidence.documents && this.props.supportingEvidence.documents.map((document, i ) =>
                  <div className="row document-row" key={i}>
                     <div className="document">
                      <span className= {document.type === 'link' ? 'glyphicon glyphicon-link document-icon' : 'glyphicon glyphicon-file document-icon'}> </span>
                       <span > <a data-url={document.url} onClick={this.openDocument} title={document.description}>{document.title}</a></span>
                      </div>
                      <div className="row-divider"></div>
                   </div>

                  )}
                </div>
              </Modal.Body>
            </Modal>)
    }
}

function mapStateToProps( state, ownProps ) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        supportingEvidence: state.reports[ownProps.code].supportingEvidence
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( SupportingEvidencePopup );
