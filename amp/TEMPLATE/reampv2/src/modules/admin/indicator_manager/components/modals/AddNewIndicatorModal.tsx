import React, { useRef, useState } from 'react';
import {
  Form, Modal, Button
} from 'react-bootstrap';
import { useFormik } from 'formik';
import Select from 'react-select';
import styles from './css/IndicatorModal.module.css';
import { getCurrentDate } from '../../utils/dateFn';
import { newIndicatorValidationSchema } from '../../utils/validator';

const options = [
  { value: 'chocolate', label: 'Chocolate' },
  { value: 'strawberry', label: 'Strawberry' },
  { value: 'vanilla', label: 'Vanilla' }
];

interface AddNewIndicatorModalProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
}

interface IndicatorFormValues {
  name: string;
  description?: string;
  code: string;
  sectors: string[];
  type: string;
  creationDate?: string;
}

const AddNewIndicatorModal: React.FC<AddNewIndicatorModalProps> = (props) => {
  const { show, setShow } = props;
  const nodeRef = useRef(null);

  const [newIndicatorFormValidated, setNewIndicatorFormValidated] = useState(false);

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
    validate: (values) => {
      // @ts-ignore
      const errors: Partial<IndicatorFormValues> = {};
      if (values.name === '') {
        errors.name = 'Please provide a valid Indicator Name.';
      }
      if (Object.keys(errors).length > 0) {
        setNewIndicatorFormValidated(false);
        return errors;
      }
      setNewIndicatorFormValidated(true);
    },
    onSubmit: (values) => {
      console.log(values);
    },
  });

  return (
    // this modal wrapper should be a separate component that can be reused since the props are the same
    <Modal
      show={show}
      onHide={handleClose}
      centered
      ref={nodeRef}
      animation={false}
      backdropClassName={styles.modal_backdrop}
    >
      <Modal.Header closeButton>
        <Modal.Title>Add New Indicator</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form noValidate validated={newIndicatorFormValidated} onSubmit={formik.handleSubmit}>
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

          <Form.Group controlId="formCreationDate">
            <Form.Label>Creation Date</Form.Label>
            <Form.Control type="date" readOnly defaultValue={getCurrentDate()} placeholder="Enter Creation Date" />
          </Form.Group>

        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button type="submit" variant="success">Save changes</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AddNewIndicatorModal;
