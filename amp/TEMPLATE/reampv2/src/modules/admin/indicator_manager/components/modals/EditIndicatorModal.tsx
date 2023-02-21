/* eslint-disable import/no-unresolved */
import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col
} from 'react-bootstrap';
import Select from 'react-select';
import { Formik } from 'formik';
import styles from './css/IndicatorModal.module.css';
import { indicatorValidationSchema } from '../../utils/validator';
import { BaseAndTargetValueType, IndicatorObjectType, SectorObjectType } from '../../types';
import { useDispatch, useSelector } from 'react-redux';
import { updateIndicator } from '../../reducers/updateIndicatorReducer';
import { backendDateToJavascriptDate , formatJavascriptDate } from '../../utils/dateFn';
import { formatObjArrayToNumberArray } from '../../utils/formatter';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';

const MySwal = withReactContent(Swal);

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
  sectors: any[];
  ascending: boolean;
  creationDate?: string;
  programs: any[];
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
}

const EditIndicatorModal: React.FC<EditIndicatorModalProps> = (props) => {
  const { show, setShow, indicator } = props;
  const dispatch = useDispatch();
  const nodeRef = useRef(null);  

  const handleClose = () => setShow(false);

  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const updateIndicatorReducer = useSelector((state: any) => state.updateIndicatorReducer);

  const [enableBaseValuesInput, setEnableBaseValuesInput] = useState(false);
  const [enableTargetValuesInput, setEnableTargetValuesInput] = useState(false);

  const [sectors, setSectors] = useState<{ value: string, label: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, name: string }[]>([]);

  const getSectors = () => {
    const sectorData = sectorsReducer.sectors.map((sector: any) => ({
      value: sector.id,
      label: sector.name
    }));
    setSectors(sectorData);
  };

  const getPrograms = () => {
    const programData = programsReducer.programs.map((program: any) => ({
      value: program.id,
      label: program.name
    }));
    setPrograms(programData);
  };

  const getDefaultSectors = () => {
    if (indicator?.sectors.length === 0) {
      return [];
    }

    const indicatorSectorData = indicator?.sectors.map((sectorId: number) => {
      const foundSector: SectorObjectType = !sectorsReducer.loading && sectorsReducer.sectors.find((sector: any) => sector.id === sectorId);

      if (foundSector) {
        return {
          value: sectorId.toString(),
          label: foundSector.name
        }
      }

      return {
        value: sectorId.toString(),
        label: 'Sector not found'
      }
    });

    return indicatorSectorData;
  };

  const getDefaultPrograms = () => {
    if (indicator?.programs.length === 0) {
      return [];
    }

    const indicatorProgramData = indicator?.programs.map((programId: number) => {
      const foundProgram: any = !programsReducer.loading && programsReducer.programs.find((program: any) => program.id === programId);

      if (foundProgram) {
        return {
          value: programId.toString(),
          label: foundProgram.name.en
        }
      }

      return {
        value: programId.toString(),
        label: 'Program not found'
      }
    });

    return indicatorProgramData;
  };

  const checkIfBaseValuesAreFilled = () => {
    if(indicator) {
      const { base } = indicator;
      if (base?.originalValue || base?.originalValueDate || base?.revisedValue || base?.revisedValueDate) {
        setEnableBaseValuesInput(true);
      }
    }
  }

  const checkIfTargetValuesAreFilled = () => {
    if(indicator) {
      const { target } = indicator;
      if (target?.originalValue || target?.originalValueDate || target?.revisedValue || target?.revisedValueDate) {
        setEnableTargetValuesInput(true);
      }
    }
  }

  useLayoutEffect(() => {
    getSectors();
    getPrograms();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    checkIfBaseValuesAreFilled();
    checkIfTargetValuesAreFilled();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [indicator]);

  const initialValues: IndicatorFormValues = {
    name: indicator?.name || '',
    description: indicator?.description || '',
    code: indicator?.code || '',
    sectors: getDefaultSectors() || [],
    programs: getDefaultPrograms() || [],
    ascending: indicator?.ascending || false,
    creationDate: indicator?.creationDate && backendDateToJavascriptDate(indicator?.creationDate),
    base: {
      originalValue: indicator?.base?.originalValue,
      originalValueDate: indicator?.base?.originalValueDate && backendDateToJavascriptDate(indicator?.base?.originalValueDate),
      revisedValue: indicator?.base?.revisedValue,
      revisedValueDate: indicator?.base?.revisedValueDate && backendDateToJavascriptDate(indicator?.base?.revisedValueDate),
    },
    target: {
      originalValue: indicator?.target?.originalValue,
      originalValueDate: indicator?.target?.originalValueDate && backendDateToJavascriptDate(indicator?.target?.originalValueDate),
      revisedValue: indicator?.target?.revisedValue,
      revisedValueDate: indicator?.target?.revisedValueDate && backendDateToJavascriptDate(indicator?.target?.revisedValueDate),
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
        validationSchema={indicatorValidationSchema}
        onSubmit={(values) => {
          const { name, description, code, sectors, programs, ascending, creationDate, base, target } = values;

          const updatedIndicatorData = {
            id: indicator.id,
            name,
            description,
            code,
            sectors : formatObjArrayToNumberArray(sectors),
            programs : formatObjArrayToNumberArray(programs),
            ascending,
            creationDate: creationDate && formatJavascriptDate(creationDate),
            base: enableBaseValuesInput ? {
              originalValue: base.originalValue,
              originalValueDate: base.originalValueDate ? formatJavascriptDate(base.originalValueDate) : null,
              revisedValue: base.revisedValue,
              revisedValueDate: base.revisedValueDate ? formatJavascriptDate(base.revisedValueDate) : null,
            } : null,
            target: enableTargetValuesInput ? {
              originalValue: target.originalValue,
              originalValueDate: target.originalValueDate ? formatJavascriptDate(target.originalValueDate) : null,
              revisedValue: target.revisedValue,
              revisedValueDate: target.revisedValueDate ? formatJavascriptDate(target.revisedValueDate) : null,
            } : null
          };

          dispatch(updateIndicator(updatedIndicatorData as IndicatorObjectType));

          if (!updateIndicatorReducer.loading && !updateIndicatorReducer.error) {
            MySwal.fire({
              icon: 'success',
              title: 'Indicator updated successfully',
              timer: 3000
            }).then(() =>{
              dispatch(getIndicators());
              handleClose();
            });
            return;
          }


          MySwal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Something went wrong!',
          });
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

                <Form.Check>

                  <Form.Check
                    type="switch"
                    name="baseToggle"
                    checked={enableBaseValuesInput}
                    onChange={() => setEnableBaseValuesInput(!enableBaseValuesInput)}
                    label={<h4 className={styles.checkbox_label}>Enable Base Values Input</h4>}
                  />
                </Form.Check>

                {  enableBaseValuesInput && (
                  <Form.Group as={Col}>
                    <Form.Label><h4>Base Values</h4></Form.Label>
                    <Form.Row>
                      <Form.Group>
                        <Form.Label>Original Value</Form.Label>
                        <Form.Control
                          defaultValue={props.values.base?.originalValue}
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
                          defaultValue={props.values.base?.originalValueDate}
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
                          defaultValue={props.values.base.revisedValue}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="base.revisedlValue"
                          type="text"
                          placeholder="Enter Revised Value" />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.base?.revisedValue}
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
                )
                }


                <Form.Check>
                  <Form.Check
                    type="switch"
                    name="baseToggle"
                    checked={enableTargetValuesInput}
                    onChange={() => setEnableTargetValuesInput(!enableTargetValuesInput)}
                    label={<h4 className={styles.checkbox_label}>Enable Target Values Input</h4>}
                  />
                </Form.Check>

                { enableTargetValuesInput && (
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
                          placeholder="Enter Target Original Value" />
                      </Form.Group>
                      <Form.Group>
                        <Form.Label>Target Value Date</Form.Label>
                        <Form.Control
                          defaultValue={props.values.target.originalValueDate}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="target.originalValueDate"
                          type="date"
                          placeholder="Enter Target Original Value Date" />
                      </Form.Group>
                    </Form.Row>

                    <Form.Row>
                      <Form.Group>
                        <Form.Label>Revised Value</Form.Label>
                        <Form.Control
                          defaultValue={props.values.target.revisedValue}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="target.revisedValue"
                          type="text"
                          placeholder="Enter Target Revised Value" />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.target?.revisedValue}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group>
                        <Form.Label>Revised Value Date</Form.Label>
                        <Form.Control
                          defaultValue={props.values.target.revisedValueDate}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="target.revisedValueDate"
                          type="date"
                          placeholder="Enter Target Revised Value Date" />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.target?.revisedValueDate}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Form.Row>
                  </Form.Group>
                )}


                <Form.Group controlId="formIndicatorSectors">
                  <Form.Label>Indicator Sectors</Form.Label>
                  {
                    sectors.length > 0 ? (
                      <Select
                        isMulti
                        name="sectors"
                        options={sectors}
                        onChange={(values) => {
                          // set the formik value with the selected values and remove the label
                          const selectedValues = values.map((value: any) => parseInt(value.value))
                          props.setFieldValue('sectors', selectedValues);
                        }}
                        getOptionValue={(option) => option.value} 
                        className="basic-multi-select"
                        classNamePrefix="select"
                        defaultValue={props.values.sectors}
                      />
                    ) : <Select isDisabled defaultValue={[{ label: 'No Sectors avaliable' }]} />
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
                        onChange={(values) => {
                          // set the formik value with the selected values and remove the label
                          const selectedValues = values.map((value: any) => parseInt(value.value))
                          props.setFieldValue('programs', selectedValues);
                        }}
                        getOptionValue={(option) => option.value}
                        className="basic-multi-select"
                        classNamePrefix="select"
                        defaultValue={props.values.programs}
                      />
                    ) : <Select isDisabled defaultValue={[{ label: 'No Programs avaliable' }]} />
                  }
                </Form.Group>

                <Form.Group controlId="Ascending">
                  <Form.Label>Ascending</Form.Label>
                  <Select
                    name="ascending"
                    defaultValue={{ label: props.values.ascending ? 'True' : 'False', value: props.values.ascending }}
                    options={ascendingOptions}
                    className="basic-multi-select"
                    classNamePrefix="select"
                  />
                </Form.Group>
              
                <Form.Group controlId="formCreationDate">
                  <Form.Label>Creation Date</Form.Label>
                  <Form.Control 
                  type="date" 
                  readOnly 
                  defaultValue={props.values.creationDate} 
                  placeholder="Enter Creation Date" />
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
