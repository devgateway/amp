/* eslint-disable import/no-unresolved */
import React from 'react';
import { Modal, Row } from 'react-bootstrap';
import backdropStyles from './css/IndicatorModal.module.css';
import styles from './css/ViewIndicatorModal.module.css';

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
    // this modal wrapper should be a separate component that can be reused since the props are the same
    <Modal
      show={show}
      onHide={handleClose}
      centered
      backdropClassName={backdropStyles.modal_backdrop}
      animation={false}
      size='lg'
    >
      <Modal.Header closeButton>
        <Modal.Title>View Indicator</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className={styles.viewmodal_wrapper}>
          <Row className={styles.view_row}>
            <div className={styles.view_item}>
              <h4>Indicator Name</h4>
              <p className={styles.value}>{indicator?.indicatorName}</p>
            </div>
            <div className={styles.view_item}>
              <h4 className={styles.label}>Indicator Code</h4>
              <p className={styles.value}>Test code</p>
            </div>
          </Row>

          <Row className={styles.view_row}>
            <div className={styles.view_item}>
              <h4 className={styles.label}>Indicator Description</h4>
              <p className={styles.value}>test description.</p>
            </div>
            <div className={styles.view_item}>
            <h4 className={styles.label}>Indicator Sectors</h4>
              <p className={styles.value}>test sector</p>
            </div>
          </Row>

          <Row className={styles.view_row}>
            <div className={styles.view_item}>
              <h4 className={styles.label}>Indicator Type</h4>
              <p className={styles.value}>test type</p>
            </div>

            <div className={styles.view_item}>
              <h4 className={styles.label}>Indicator Creation Date</h4>
              <p className={styles.value}>test date</p>
            </div>
          </Row>
        </div>
      </Modal.Body>
    </Modal>
  );
};

export default ViewIndicatorModal;
