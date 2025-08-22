import React from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { Formik, Form as FormikForm, Field } from 'formik';
import * as Yup from 'yup';
import styles from './css/IndicatorModal.module.css';

interface AddNewOutcomeModalProps {
  show: boolean;
  setShow: (show: boolean) => void;
  onSubmit?: (outcome: { name: string; description?: string }) => void;
  initialName?: string;
  initialDescription?: string;
  translations?: Record<string, string>;
}

const OutcomeModal: React.FC<AddNewOutcomeModalProps> = ({ show, setShow, onSubmit, initialName = '', initialDescription = '', translations = {} }) => {
  const validationSchema = Yup.object().shape({
    name: Yup.string().required(translations['amp.outcomeoutput:errors-name-required'] || 'Name is required'),
    description: Yup.string()
  });

  return (
    <Modal show={show} onHide={() => setShow(false)} centered>
      <Formik
        initialValues={{ name: initialName, description: initialDescription }}
        validationSchema={validationSchema}
        enableReinitialize
        onSubmit={(values, { resetForm }) => {
          if (onSubmit) onSubmit(values);
          setShow(false);
          resetForm();
        }}
      >
        {({ errors, touched, handleSubmit }) => (
          <FormikForm onSubmit={handleSubmit} className={styles.indicator_modal_form}>
            <Modal.Header closeButton>
              <Modal.Title>{translations['amp.outcomeoutput:modal-title-outcome'] || 'Add New Outcome'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form.Group as={Row} controlId="outcomeName">
                <Form.Label column sm={3}>{translations['amp.outcomeoutput:outcome-name'] || 'Outcome Name'}</Form.Label>
                <Col sm={9}>
                  <Field
                    name="name"
                    as={Form.Control}
                    type="text"
                    placeholder={translations['amp.outcomeoutput:outcome-name'] || 'Outcome Name'}
                    isInvalid={!!errors.name && touched.name}
                  />
                  {errors.name && touched.name && (
                    <div className="text-danger small mt-1">{errors.name}</div>
                  )}
                </Col>
              </Form.Group>
              <Form.Group as={Row} controlId="outcomeDescription" className="mt-3">
                <Form.Label column sm={3}>{translations['amp.outcomeoutput:outcome-description'] || 'Outcome Description'}</Form.Label>
                <Col sm={9}>
                  <Field
                    name="description"
                    as={Form.Control}
                    rows={3}
                    placeholder={translations['amp.outcomeoutput:outcome-description'] || 'Outcome Description'}
                  />
                </Col>
              </Form.Group>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => setShow(false)}>
                {translations['amp.outcomeoutput:cancel'] || 'Cancel'}
              </Button>
              <Button variant="primary" type="submit">
                {translations['amp.outcomeoutput:save-outcome'] || 'Save Outcome'}
              </Button>
            </Modal.Footer>
          </FormikForm>
        )}
      </Formik>
    </Modal>
  );
};

export default OutcomeModal;
