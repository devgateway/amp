import React, { Component } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap-modal';
import PropTypes from 'prop-types';
import { TRN_PREFIX } from '../../utils/constants';

export default class YearDetail extends Component {
  render() {
    const {
      translations, show, handleClose, data, loading, error
    } = this.props;
    return (
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>{translations[`${TRN_PREFIX}modal-details-title`]}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {loading
            ? <div className="loading" style={{ top: '5%', marginTop: '5px' }} />
            : null}
          {!loading && !data && !error ? <span>No Data</span> : null}
          {data ? <span>{JSON.stringify(data)}</span> : null}
          {error ? <span className="error">{JSON.stringify(error)}</span> : null}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={handleClose}>
            {translations[`${TRN_PREFIX}close`]}
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

YearDetail.propTypes = {
  translations: PropTypes.array.isRequired,
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  data: PropTypes.array.isRequired,
  loading: PropTypes.bool.isRequired,
  error: PropTypes.object.isRequired
};
