import React from 'react';
import { Col, Button, Form, Modal } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { getShareLink } from '../actions/generateShareLink';
import { TRN_PREFIX } from '../utils/constants';

const getLink = (id) => {
  const regex = /\/\d+$/;
  const url = window.location.toString();
  const match = url.toString().match(regex);
  if (match) {
    return `${url.substring(0, match.index)}/${id}`;
  }
  if (!url.endsWith('/')) {
    return `${url}/${id}`;
  }
  return url + id;
};

const Share = (props) => {
  const {
    _getShareLink,
    filters,
    translations,
    shareLink,
    settings,
    fundingType,
    selectedPrograms
  } = props;

  const [show, setShow] = React.useState(false);

  const handleClose = () => setShow(false);

  const handleShow = () => {
    _getShareLink(filters ? filters.filters : null, settings, fundingType, selectedPrograms);
    setShow(true);
  }

  const nodeRef = React.useRef(null);

  const generateModal = () => {
    return (
      <Modal
      show={show}
      onHide={handleClose}
      centered
      ref={nodeRef}
      backdrop='static'
      backdropClassName='modal_backdrop'
      animation={false}>
        <Modal.Header
          closeButton
          style={{
            backgroundColor: '#337ab7', paddingTop: '8px', paddingBottom: '8px', color: 'white'
          }}>
          <Modal.Title>{translations[`${TRN_PREFIX}share-copy-link`]}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <span className="label-bold">{` ${translations[`${TRN_PREFIX}share-link`]}`}</span>
          <Form.Control type="text" readOnly value={shareLink ? getLink(shareLink.id) : null} />
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={handleClose}>
            {translations[`${TRN_PREFIX}close`]}
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  return (
    <Col md={3}>
      <div className="panel">
        <div className="panel-body">
          <h3 className="inline-heading">{translations[`${TRN_PREFIX}share`]}</h3>
          <button
            onClick={handleShow}
            type="button"
            className="btn btn-sm btn-default pull-right dash-share-button">
            <span className="glyphicon glyphicon-link" />
            <span>{translations['amp.ndd.dashboard:Link']}</span>
          </button>
        </div>
      </div>
      <div>
        {generateModal()}
      </div>
    </Col>
  );
};

const mapStateToProps = state => ({
  shareLink: state.shareLinkReducer.shareLink,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getShareLink: getShareLink
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Share);

Share.propTypes = {
  _getShareLink: PropTypes.func.isRequired,
  filters: PropTypes.object,
  translations: PropTypes.object.isRequired,
  shareLink: PropTypes.object,
  settings: PropTypes.object,
  fundingType: PropTypes.string.isRequired,
  selectedPrograms: PropTypes.array.isRequired
};

Share.defaultProps = {
  filters: undefined,
  shareLink: undefined,
  settings: undefined
};
