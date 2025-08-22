import React from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { Formik, Form as FormikForm, Field } from 'formik';
import * as Yup from 'yup';
import Select from 'react-select';
import styles from './css/IndicatorModal.module.css';

interface Outcome {
  id: number;
  name: string;
}

interface AddNewOutputModalProps {
  show: boolean;
  setShow: (show: boolean) => void;
  outcomes: Outcome[];
  onSubmit?: (output: { name: string; description?: string; outcomeIds: number[] }) => void;
  initialName?: string;
  initialDescription?: string;
  initialOutcomeIds?: number[];
}

const OutputModal: React.FC<AddNewOutputModalProps> = ({ show, setShow, outcomes, onSubmit, initialName = '', initialDescription = '', initialOutcomeIds = [] }) => {
  const validationSchema = Yup.object().shape({
    name: Yup.string().required('Output name is required'),
    description: Yup.string(),
    outcomeIds: Yup.array().min(1, 'Select at least one outcome')
  });

  const outcomeOptions = outcomes.map(o => ({ value: o.id, label: o.name }));

  return (
    <Modal show={show} onHide={() => setShow(false)} centered>
      <Formik
        initialValues={{ name: initialName, description: initialDescription, outcomeIds: initialOutcomeIds }}
        validationSchema={validationSchema}
        enableReinitialize
        onSubmit={(values, { resetForm }) => {
          if (onSubmit) onSubmit(values);
          setShow(false);
          resetForm();
        }}
      >
        {({ errors, touched, handleSubmit, setFieldValue, values }) => (
          <FormikForm onSubmit={handleSubmit} className={styles.indicator_modal_form}>
            <Modal.Header closeButton>
              <Modal.Title>Add New Output</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form.Group as={Row} controlId="outputName">
                <Form.Label column sm={3}>Output Name</Form.Label>
                <Col sm={9}>
                  <Field
                    name="name"
                    as={Form.Control}
                    type="text"
                    placeholder="Enter output name"
                    isInvalid={!!errors.name && touched.name}
                  />
                  {errors.name && touched.name && (
                    <div className="text-danger small mt-1">{errors.name}</div>
                  )}
                </Col>
              </Form.Group>
              <Form.Group as={Row} controlId="outputDescription" className="mt-3">
                <Form.Label column sm={3}>Description</Form.Label>
                <Col sm={9}>
                  <Field
                    name="description"
                    as={Form.Control}
                    rows={3}
                    placeholder="Enter output description"
                  />
                </Col>
              </Form.Group>
              <Form.Group as={Row} controlId="outputOutcomes" className="mt-3">
                <Form.Label column sm={3}>Linked Outcomes</Form.Label>
                <Col sm={9}>
                  <Select
                    isMulti
                    options={outcomeOptions}
                    value={outcomeOptions.filter(opt => values.outcomeIds.includes(opt.value))}
                    onChange={selected => setFieldValue('outcomeIds', selected.map((opt: any) => opt.value))}
                    placeholder="Select outcomes..."
                  />
                  {errors.outcomeIds && touched.outcomeIds && (
                    <div className="text-danger small mt-1">{errors.outcomeIds}</div>
                  )}
                </Col>
              </Form.Group>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => setShow(false)}>
                Cancel
              </Button>
              <Button variant="primary" type="submit">
                Save Output
              </Button>
            </Modal.Footer>
          </FormikForm>
        )}
      </Formik>
    </Modal>
  );
};

export default OutputModal;
