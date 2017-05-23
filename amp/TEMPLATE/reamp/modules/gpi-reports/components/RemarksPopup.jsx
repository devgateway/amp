import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import { Modal } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
import moment from 'moment';
import * as Constants from '../common/Constants';
export default class RemarksPopup extends Component {
    constructor( props, context ) {
        super( props, context );        
    }
    componentDidMount() {
        this.props.actions.clearRemarks(this.props.code);
        this.props.actions.fetchRemarks(this.props.code, this.props.remarksUrl);        
    }
    getDateFormat() {
        return (this.props.settings && this.props.settings[Constants.DATE_FORMAT_SETTING]) ? this.props.settings[Constants.DATE_FORMAT_SETTING].toUpperCase() : Constants.DEFAULT_UI_DATE_FORMAT;  
    }
    
    formatDate(date) {
        var result;
        if(date) {
            result = moment(date, Constants.EP_DATE_FORMAT).format(this.getDateFormat());           
        }        
        return result        
    }
    
    render() {          
        return (<Modal show={this.props.showRemarks} onHide={this.props.closeRemarksModal} container={this}>
                <Modal.Header closeButton>
                  <Modal.Title>{this.props.translations['amp-gpi-reports:remarks']}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  <div>
                    {this.props.remarks.map((remark, i ) =>
                    <div className="row remark-row" key={i}> 
                       <div >
                        <span className="glyphicon glyphicon-comment comment-icon"> </span>
                        <span className="remark-date"> {this.formatDate(remark.date)}</span>
                        </div>
                       <div className="remark"> {remark.remark}</div>
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
        remarks: state.reports['5a'].remarks
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( RemarksPopup );
