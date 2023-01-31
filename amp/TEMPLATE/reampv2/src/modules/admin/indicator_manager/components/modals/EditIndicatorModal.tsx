/* eslint-disable import/no-unresolved */
import React, { useRef } from 'react';
import {
  Form, Modal, Button
} from 'react-bootstrap';
import Select from 'react-select';
import { useFormik } from 'formik';
import styles from './css/IndicatorModal.module.css';
import { newIndicatorValidationSchema } from '../../utils/validator';

const options = [
  { value: 'chocolate', label: 'Chocolate' },
  { value: 'strawberry', label: 'Strawberry' },
  { value: 'vanilla', label: 'Vanilla' }
];

interface EditIndicatorModalProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
  indicator?: any;
}

interface IndicatorFormValues {
  name: string;
  description?: string;
  code: string;
  sectors: string[];
  type: string;
  creationDate?: string;
}

const EditIndicatorModal: React.FC<EditIndicatorModalProps> = (props) => {
  const { show, setShow } = props;
  const nodeRef = useRef(null);

  const handleClose = () => setShow(false);

  const initialValues: IndicatorFormValues = {
    name: '',
    description: '',
    code: '',
    sectors: [],
    type: 'ascending',
    creationDate: ''
  };

  const formik = useFormik({
    initialValues,
    validationSchema: newIndicatorValidationSchema,
    onSubmit: (values) => {
      console.log(values);
    }
  });

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
        <Modal.Title>Edit Indicator</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form validated onSubmit={formik.handleSubmit}>
          <Form.Group controlId="formBasicName">
            <Form.Label>Indicator Name</Form.Label>
            <Form.Control required aria-required type="text" placeholder="Enter Indicator Name" />
            <Form.Control.Feedback type="invalid">
              Please provide a valid Indicator Name.
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formBasicDescription">
            <Form.Label>Indicator Description</Form.Label>
            <Form.Control type="text" placeholder="Enter Indicator Description" />
          </Form.Group>

          <Form.Group controlId="formIndicatorCode">
            <Form.Label>Indicator Code</Form.Label>
            <Form.Control type="text" placeholder="Enter Indicator Code" />
          </Form.Group>

          <Form.Group controlId="formIndicatorSectors">
            <Form.Label>Indicator Sectors</Form.Label>
            <Select
              isMulti
              name="sectors"
              options={options}
              className="basic-multi-select"
              classNamePrefix="select"
            />
          </Form.Group>

          <Form.Group controlId="formIndicatorType">
            <Form.Label>Indicator Type</Form.Label>
            <Form.Control as="select">
              <option value="ascending">Ascending</option>
              <option value="descending">Descending</option>
            </Form.Control>
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

export default EditIndicatorModal;
