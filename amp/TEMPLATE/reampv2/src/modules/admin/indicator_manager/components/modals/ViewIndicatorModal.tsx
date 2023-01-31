/* eslint-disable import/no-unresolved */
import React from 'react';
import { Modal, Row, Col } from 'react-bootstrap';
import styles from './css/IndicatorModal.module.css';

interface ViewIndicatorModalProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
    indicator?: any;
}

const ViewIndicatorModal: React.FC<ViewIndicatorModalProps> = (props) => {
  const { show, setShow, indicator } = props;
  console.log('indicator', indicator);

  const handleClose = () => setShow(false);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      centered
      backdropClassName={styles.modal_backdrop}
      animation={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>View Indicator</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Row>
          <Col>
            <h4>Indicator Name</h4>
            <p className={styles.value}>{indicator?.indicatorName}</p>
          </Col>
          <Col>

            <p className={styles.label}>Indicator Code</p>
            <p className={styles.value}>Test code</p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className={styles.label}>Indicator Description</p>
            <p className={styles.value}>test soos</p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className={styles.label}>Indicator Sectors</p>
            <p className={styles.value}>test sector</p>
          </Col>
        </Row>
      </Modal.Body>
    </Modal>
  );
};

export default ViewIndicatorModal;
