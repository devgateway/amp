import React, { useRef } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { Formik, FormikProps, Form as FormikForm, Field } from 'formik';
import * as Yup from 'yup';
import styles from './css/IndicatorModal.module.css';
import initialTranslations from '../../config/initialTranslations.json';

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
  const t = (key: string): string => translations[key] ?? initialTranslations[key as keyof typeof initialTranslations] ?? key;
  const validationSchema = Yup.object().shape({
    name: Yup.string().required(t('amp.outcomeoutput:errors-name-required')),
    description: Yup.string()
  });

  const initialValues: { name: string; description?: string } = {
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
        <Modal.Title>{t('amp.outcomeoutput:add-new-outcome')}</Modal.Title>
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
                    <Form.Label>{t('amp.outcomeoutput:outcome-name')}</Form.Label>
                    <Form.Control
                      defaultValue={props.values.name}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="name"
                      className={`${styles.input_field} ${(props.errors.name && props.touched.name) && styles.text_is_invalid}`}
                      isInvalid={!!props.errors.name}
                      required
                      aria-required type="text"
                      placeholder={t('amp.outcomeoutput:outcome-name')}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.name}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formOutcomeDescription">
                    <Form.Label>{t('amp.outcomeoutput:outcome-description')}</Form.Label>
                    <Form.Control
                      defaultValue={props.values.description}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="description"
                      type="text"
                      className={`${styles.input_field} ${(props.errors.description && props.touched.description) && styles.text_is_invalid}`}
                      placeholder={t('amp.outcomeoutput:outcome-description')}
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
                {t('amp.outcomeoutput:cancel')}
              </Button>
              <Button type="submit" variant="success">
                {t('amp.outcomeoutput:save-outcome')}
              </Button>
            </Modal.Footer>
          </Form>
        )}
      </Formik>
    </Modal>
  );
};

export default OutcomeModal;
