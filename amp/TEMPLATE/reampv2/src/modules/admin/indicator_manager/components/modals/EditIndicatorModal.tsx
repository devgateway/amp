/* eslint-disable import/no-unresolved */
import React, { useRef, useState } from 'react';
import {
  Form, Modal, Button, Col
} from 'react-bootstrap';
import Select from 'react-select';
import { Formik } from 'formik';
import styles from './css/IndicatorModal.module.css';
// import bootstrapStyles from '../bootstrap.module.css';
import { newIndicatorValidationSchema } from '../../utils/validator';
import { BaseAndTargetValueType, IndicatorObjectType } from '../../types';
import { useSelector } from 'react-redux';
import { getCurrentDate } from '../../utils/dateFn';

const ascendingOptions = [
  { value: true, label: 'True' },
  { value: false, label: 'False' }
];

interface EditIndicatorModalProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
  indicator: IndicatorObjectType;
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

const EditIndicatorModal: React.FC<EditIndicatorModalProps> = (props) => {
  const { show, setShow, indicator } = props;
  const nodeRef = useRef(null);

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

  const getInitialSectorsData = () => { }

  const initialValues: IndicatorFormValues = {
    name: indicator?.name.en || '',
    description: indicator?.description.en || '',
    code: indicator?.code || '',
    sectors: [],
    programs: [],
    ascending: indicator?.ascending || false,
    creationDate: indicator?.creationDate || '',
    base: {
      originalValue: indicator?.base?.originalValue,
      originalValueDate: indicator?.base?.originalValueDate,
      revisedlValue: indicator?.base?.revisedlValue,
      revisedValueDate: indicator?.base?.revisedValueDate,
    },
    target: {
      originalValue: indicator?.target?.originalValue,
      originalValueDate: indicator?.target?.originalValueDate,
      revisedlValue: indicator?.target?.revisedlValue,
      revisedValueDate: indicator?.target?.revisedValueDate,
    }
  };

  return (
    // this modal wrapper should be a separate component that can be reused since the props are the same
    <Modal
      show={show}
      onHide={handleClose}
      centered
      ref={nodeRef}
      backdropClassName={styles.modal_backdrop}
      animation={false}
      backdrop="static"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Edit Indicator</Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={initialValues}
        validationSchema={newIndicatorValidationSchema}
        onSubmit={(values) => {
          console.log(values);
        }}
      >
        {(props) => (
          <>
            <Form noValidate onSubmit={props.handleSubmit}>
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
                <Button type="submit" variant="primary">Save changes</Button>
              </Modal.Footer>
            </Form>
          </>
        )}

      </Formik>
    </Modal>
  );
};

export default EditIndicatorModal;
