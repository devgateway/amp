import React, { Component } from 'react';
import { Col, Button, Form } from 'react-bootstrap';
import Modal from 'react-bootstrap-modal';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { getShareLink } from '../actions/generateShareLink';
import { SHARE_LINK } from '../utils/constants';

const getLink = (id) => window.location + SHARE_LINK + id;

class Share extends Component {
  constructor(props) {
    super(props);
    this.state = { show: false };
    this.generateModal = this.generateModal.bind(this);
  }

   handleClose = () => this.setState({ show: false });

   handleShow = () => this.prepareDataToSave().then(() => this.setState({ show: true }));

   prepareDataToSave = () => {
     // TODO: prepare data from filters and chart settings.
     // eslint-disable-next-line no-shadow
     const { getShareLink } = this.props;
     return getShareLink();
   }

   generateModal() {
     const { show } = this.state;
     const { shareLink } = this.props;
     return (
       <Modal show={show} onHide={this.handleClose}>
         <Modal.Header closeButton>
           <Modal.Title>Copy this link to share the dashboard</Modal.Title>
         </Modal.Header>
         <Modal.Body>
           <span className="label-bold">Link</span>
           <Form.Control type="text" value={shareLink ? getLink(shareLink.id) : null} />
         </Modal.Body>
         <Modal.Footer>
           <Button variant="secondary" onClick={this.handleClose}>
             Close
           </Button>
         </Modal.Footer>
       </Modal>
     );
   }

   render() {
     return (
       <Col md={3}>
         <div className="panel">
           <div className="panel-body">
             <h3 className="inline-heading">Share</h3>
             <button
               onClick={this.handleShow}
               type="button"
               className="btn btn-sm btn-default pull-right dash-share-button">
               <span className="glyphicon glyphicon-link" />
               <span>link</span>
             </button>
           </div>
         </div>
         <div>
           {this.generateModal()}
         </div>
       </Col>
     );
   }
}

const mapStateToProps = state => ({
  shareLink: state.shareLinkReducer.shareLink
});

const mapDispatchToProps = dispatch => bindActionCreators({
  getShareLink
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Share);

Share.propTypes = {
  getShareLink: PropTypes.func.isRequired
};
