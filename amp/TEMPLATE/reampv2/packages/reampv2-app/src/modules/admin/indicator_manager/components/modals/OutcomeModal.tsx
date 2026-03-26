import React, { useRef } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { Formik, FormikProps, Form as FormikForm, Field } from 'formik';
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
  const nodeRef = useRef(null);
  const validationSchema = Yup.object().shape({
    name: Yup.string().required(translations['amp.outcomeoutput:errors-name-required'] || 'Name is required'),
    description: Yup.string()
  });

  const initialValues = {
    name: initialName,
    description: initialDescription
  };

  const handleClose = () => setShow(false);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      centered
      ref={nodeRef}
      animation={false}
      backdropClassName={styles.modal_backdrop}
      backdrop="static"
      keyboard={false}
      size='lg'
    >
      <Modal.Header closeButton>
        <Modal.Title>{translations['amp.outcomeoutput:modal-title-outcome'] || 'Add New Outcome'}</Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        enableReinitialize
        onSubmit={(values, { resetForm }) => {
          if (onSubmit) onSubmit(values);
          setShow(false);
          resetForm();
        }}
      >
        {(props: FormikProps<{ name: string; description?: string }>) => (
          <Form noValidate onSubmit={props.handleSubmit}>
            <Modal.Body>
              <div className={styles.viewmodal_wrapper}>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formOutcomeName">
                    <Form.Label>{translations['amp.outcomeoutput:outcome-name'] || 'Outcome Name'}</Form.Label>
                    <Form.Control
                      defaultValue={props.values.name}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="name"
                      className={`${styles.input_field} ${(props.errors.name && props.touched.name) && styles.text_is_invalid}`}
                      isInvalid={!!props.errors.name}
                      required
                      aria-required type="text"
                      placeholder={translations['amp.outcomeoutput:outcome-name'] || 'Outcome Name'}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.name}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formOutcomeDescription">
                    <Form.Label>{translations['amp.outcomeoutput:outcome-description'] || 'Outcome Description'}</Form.Label>
                    <Form.Control
                      defaultValue={props.values.description}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="description"
                      type="text"
                      className={`${styles.input_field} ${(props.errors.description && props.touched.description) && styles.text_is_invalid}`}
                      placeholder={translations['amp.outcomeoutput:outcome-description'] || 'Outcome Description'}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.description}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>
              </div>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={handleClose}>
                {translations['amp.outcomeoutput:cancel'] || 'Cancel'}
              </Button>
              <Button type="submit" variant="success">
                {translations['amp.outcomeoutput:save-outcome'] || 'Save Outcome'}
              </Button>
            </Modal.Footer>
          </Form>
        )}
      </Formik>
    </Modal>
  );
};

export default OutcomeModal;
