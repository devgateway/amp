import React, { useEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col
} from 'react-bootstrap';
import { useFormik, Formik } from 'formik';
import Select from 'react-select';
import styles from './css/IndicatorModal.module.css';
import { getCurrentDate } from '../../utils/dateFn';
import { newIndicatorValidationSchema } from '../../utils/validator';
import { useSelector } from 'react-redux';
import { BaseAndTargetValueType } from '../../types';

const ascendingOptions = [
  { value: true, label: 'True' },
  { value: false, label: 'False' }
];

interface AddNewIndicatorModalProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
}

interface IndicatorFormValues {
  name: string;
  description?: string;
  code: string;
  sectors: number[];
  ascending: boolean;
  creationDate?: string;
  programs: number[];
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
}

const AddNewIndicatorModal: React.FC<AddNewIndicatorModalProps> = (props) => {
  const { show, setShow } = props;
  const nodeRef = useRef(null);

  const [newIndicatorFormValidated, setNewIndicatorFormValidated] = useState(false);

  const handleClose = () => setShow(false);

  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);

  const [sectors, setSectors] = React.useState<{ value: string, name: string }[]>([]);
  const [programs, setPrograms] = React.useState<{ value: string, name: string }[]>([]);

  const getSectors = () => {
    const sectorData = sectorsReducer.sectors.map((sector: any) => ({
      value: sector.id,
      label: sector.name.en
    }));
    setSectors(sectorData);
  };

  const getProgramData = () => {
    const programData = programsReducer.programs.map((program: any) => ({
      value: program.id,
      label: program.name.en
    }));
    setPrograms(programData);
  };


  useEffect(() => {
    getSectors();
    getProgramData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sectorsReducer.sectors, programsReducer.programs])

  const initialValues: IndicatorFormValues = {
    name: '',
    description: '',
    code: '',
    sectors: [],
    programs: [],
    ascending: false,
    creationDate: getCurrentDate(),
    base: {
      originalValue: 0,
      originalValueDate: getCurrentDate(),
      revisedlValue: 0,
      revisedValueDate: getCurrentDate(),
    },
    target: {
      originalValue: 0,
      originalValueDate: getCurrentDate(),
      revisedlValue: 0,
      revisedValueDate: getCurrentDate(),
    }
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
      backdrop="static"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Add New Indicator</Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={initialValues}
        validationSchema={newIndicatorValidationSchema}
        onSubmit={(values) => {
          console.log(values);
        }}
      >
        {(props) => (
          <Form noValidate onSubmit={formik.handleSubmit}>
            <Modal.Body>
              <Form.Group as={Col} controlId="formBasicName">
                <Form.Label>Indicator Name</Form.Label>
                <Form.Control
                  defaultValue={props.values.name}
                  onChange={props.handleChange}
                  onBlur={props.handleBlur}
                  name="name"
                  className={`${(props.errors.name && props.touched.name) && styles.text_is_invalid}`}
                  isInvalid={!!props.errors.name}
                  required
                  aria-required type="text"
                  placeholder="Enter Indicator Name"
                />
                <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                  {props.errors.name}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group controlId="formBasicDescription">
                <Form.Label>Indicator Description</Form.Label>
                <Form.Control
                  defaultValue={props.values.description}
                  onChange={props.handleChange}
                  onBlur={props.handleBlur}
                  name="description"
                  type="text"
                  placeholder="Enter Indicator Description"
                />
                <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                  {props.errors.description}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group controlId="formIndicatorCode">
                <Form.Label>Indicator Code</Form.Label>
                <Form.Control
                  defaultValue={props.values.code}
                  onChange={props.handleChange}
                  onBlur={props.handleBlur}
                  name="code"
                  required
                  type="text"
                  placeholder="Enter Indicator Code"
                />
                <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                  {props.errors.code}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group as={Col}>
                <Form.Label><h4>Base Values</h4></Form.Label>
                <Form.Row>
                  <Form.Group>
                    <Form.Label>Original Value</Form.Label>
                    <Form.Control
                      defaultValue={props.values.base.originalValue}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="base.originalValue"
                      type="text"
                      placeholder="Enter Original Value" />

                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.base?.originalValue}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group>
                    <Form.Label>Original Value Date</Form.Label>
                    <Form.Control
                      defaultValue={props.values.base.originalValueDate}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="base.originalValueDate"
                      type="date"
                      placeholder="Enter Original Value Date" />

                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.base?.originalValueDate}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Form.Row>

                <Form.Row>
                  <Form.Group>
                    <Form.Label>Revised Value</Form.Label>
                    <Form.Control
                      defaultValue={props.values.base.revisedlValue}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="base.revisedlValue"
                      type="text"
                      placeholder="Enter Revised Value" />

                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.base?.revisedlValue}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group>
                    <Form.Label>Revised Value Date</Form.Label>
                    <Form.Control
                      defaultValue={props.values.base.revisedValueDate}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="base.revisedValueDate"
                      type="date"
                      placeholder="Enter Revised Value Date" />

                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.base?.revisedValueDate}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Form.Row>
              </Form.Group>

              <Form.Group as={Col}>
                <Form.Label><h4>Target Values</h4></Form.Label>
                <Form.Row>
                  <Form.Group>
                    <Form.Label>Target Value</Form.Label>
                    <Form.Control
                      defaultValue={props.values.target.originalValue}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="target.originalValue"
                      type="text"
                      placeholder="Enter Target Value" />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Target Value Date</Form.Label>
                    <Form.Control
                      defaultValue={props.values.target.originalValueDate}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="target.originalValueDate"
                      type="date"
                      placeholder="Enter Target Value Date" />
                  </Form.Group>
                </Form.Row>

                <Form.Row>
                  <Form.Group>
                    <Form.Label>Revised Value</Form.Label>
                    <Form.Control
                      defaultValue={props.values.base.revisedlValue}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="base.revisedlValue"
                      type="text"
                      placeholder="Enter Revised Value" />

                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.base?.revisedlValue}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group>
                    <Form.Label>Revised Value Date</Form.Label>
                    <Form.Control
                      defaultValue={props.values.base.revisedValueDate}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="base.revisedValueDate"
                      type="date"
                      placeholder="Enter Revised Value Date" />

                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.base?.revisedValueDate}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Form.Row>
              </Form.Group>

              <Form.Group controlId="formIndicatorSectors">
                <Form.Label>Indicator Sectors</Form.Label>
                {
                  sectors.length > 0 ? (
                    <Select
                      isMulti
                      name="sectors"
                      options={sectors}
                      className="basic-multi-select"
                      classNamePrefix="select"
                    />
                  ) : null
                }
              </Form.Group>

              <Form.Group controlId="programs">
                <Form.Label>Programs</Form.Label>
                {
                  programs.length > 0 ? (
                    <Select
                      isMulti
                      name="programs"
                      options={programs}
                      className="basic-multi-select"
                      classNamePrefix="select"
                    />
                  ) : null
                }
              </Form.Group>

              <Form.Group controlId="Ascending">
                <Form.Label>Ascending</Form.Label>
                <Select
                  name="ascending"
                  options={ascendingOptions}
                  className="basic-multi-select"
                  classNamePrefix="select"
                />
              </Form.Group>

              <Form.Group controlId="formCreationDate">
                <Form.Label>Creation Date</Form.Label>
                <Form.Control type="date" readOnly defaultValue={getCurrentDate()} placeholder="Enter Creation Date" />
              </Form.Group>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={handleClose}>
                Close
              </Button>
              <Button type="submit" variant="success">Save changes</Button>
            </Modal.Footer>
          </Form>
        )}
      </Formik>

    </Modal>
  );
};

export default AddNewIndicatorModal;
