import React, { useRef } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { Formik, FormikProps } from 'formik';
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
  initialOutcomes?: Outcome[];
  translations?: Record<string, string>;
  loading?: boolean;
}

const OutputModal: React.FC<AddNewOutputModalProps> = ({ show, setShow, outcomes, onSubmit, initialName = '', initialDescription = '', initialOutcomes = [], translations = {}, loading = false }) => {
  const nodeRef = useRef(null);
  const validationSchema = Yup.object().shape({
    name: Yup.string().required(translations['amp.outcomeoutput:errors-name-required'] || 'Name is required'),
    description: Yup.string(),
  });

  const initialValues = {
    name: initialName,
    description: initialDescription,
    outcomeIds: initialOutcomes.map(o => o.id)
  };

  const outcomeOptions = outcomes.map(o => ({ value: o.id, label: o.name }));
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
        <Modal.Title>{translations['amp.outcomeoutput:modal-title-output'] || 'Add New Output'}</Modal.Title>
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
        {(props: FormikProps<{ name: string; description?: string; outcomeIds: number[] }>) => (
          <Form noValidate onSubmit={props.handleSubmit}>
            <Modal.Body>
              <div className={styles.viewmodal_wrapper}>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formOutputName">
                    <Form.Label>{translations['amp.outcomeoutput:output-name'] || 'Output Name'}</Form.Label>
                    <Form.Control
                      defaultValue={props.values.name}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="name"
                      className={`${styles.input_field} ${(props.errors.name && props.touched.name) && styles.text_is_invalid}`}
                      isInvalid={!!props.errors.name}
                      required
                      aria-required type="text"
                      placeholder={translations['amp.outcomeoutput:output-name'] || 'Output Name'}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.name}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formOutputDescription">
                    <Form.Label>{translations['amp.outcomeoutput:output-description'] || 'Output Description'}</Form.Label>
                    <Form.Control
                      defaultValue={props.values.description}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="description"
                      type="text"
                      className={`${styles.input_field} ${(props.errors.description && props.touched.description) && styles.text_is_invalid}`}
                      placeholder={translations['amp.outcomeoutput:output-description'] || 'Output Description'}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.description}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formOutputOutcomes">
                    <Form.Label>{translations['amp.outcomeoutput:linked-outcomes'] || 'Linked Outcomes'}</Form.Label>
                    <Select
                      isMulti
                      options={outcomeOptions}
                      value={outcomeOptions.filter(opt => props.values.outcomeIds.includes(opt.value))}
                      onChange={selected => props.setFieldValue('outcomeIds', selected.map((opt: any) => opt.value))}
                      placeholder={translations['amp.outcomeoutput:linked-outcomes'] || 'Linked Outcomes'}
                      isDisabled={loading}
                    />
                    {props.errors.outcomeIds && props.touched.outcomeIds && (
                      <div className="text-danger small mt-1">{props.errors.outcomeIds}</div>
                    )}
                  </Form.Group>
                </Row>
              </div>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={handleClose} disabled={loading}>
                {translations['amp.outcomeoutput:cancel'] || 'Cancel'}
              </Button>
              <Button type="submit" variant="success" disabled={loading}>
                {translations['amp.outcomeoutput:save-output'] || 'Save Output'}
              </Button>
            </Modal.Footer>
          </Form>
        )}
      </Formik>
    </Modal>
  );
};

export default OutputModal;
