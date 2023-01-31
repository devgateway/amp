import React, { useRef } from 'react';
import { Form, Modal, Button } from 'react-bootstrap';
import styles from './css/AddNewIndicatorForm.module.css';

interface IndicatorFormProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
}

const AddNewIndicatorForm: React.FC<IndicatorFormProps> = (props) => {
  const { show, setShow } = props;
  const nodeRef = useRef(null);

  const handleClose = () => setShow(false);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      centered
      ref={nodeRef}
      backdropClassName={styles.modal_backdrop}
      animation={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Add New Indicator</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formBasicName">
            <Form.Label>Indicator Name</Form.Label>
            <Form.Control required aria-required type="text" placeholder="Enter Indicator Name" />
          </Form.Group>

          <Form.Group controlId="formBasicDescription">
            <Form.Label>Indicator Description</Form.Label>
            <Form.Control type="text" placeholder="Enter Indicator Description" />
          </Form.Group>

          <Form.Group controlId="formIndicatorCode">
            <Form.Label>Indicator Code</Form.Label>
            <Form.Control type="text" placeholder="Enter Indicator Code" />
          </Form.Group>

          <Form.Group controlId="formIndicatorType">
            <Form.Label>Indicator Type</Form.Label>
            <Form.Control as="select">
              <option value="ascending">Ascending</option>
              <option value="descending">Descending</option>
            </Form.Control>
          </Form.Group>

          <Form.Group controlId="formCreationDate">
            <Form.Label>Creation Date</Form.Label>
            <Form.Control readOnly type="date" placeholder="Enter Creation Date" />
          </Form.Group>

        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary">Save changes</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AddNewIndicatorForm;
