import React, { useEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col
} from 'react-bootstrap';
import { Formik } from 'formik';
import Select from 'react-select';
import styles from './css/IndicatorModal.module.css';
import { formatJavascriptDate, getCurrentDate } from '../../utils/dateFn';
import { indicatorValidationSchema } from '../../utils/validator';
import { useDispatch, useSelector } from 'react-redux';
import { BaseAndTargetValueType, DefaultComponentProps } from '../../types';
import { createIndicator } from '../../reducers/createIndicatorReducer';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';

const MySwal = withReactContent(Swal);

const ascendingOptions = [
  { value: true, label: 'True' },
  { value: false, label: 'False' }
];

interface AddNewIndicatorModalProps extends DefaultComponentProps {
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
  const { show, setShow, translations } = props;
  const nodeRef = useRef(null);
  const dispatch = useDispatch();

  const handleClose = () => setShow(false);
  const createIndicatorState = useSelector((state: any) => state.createIndicatorReducer);

  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);

  const [enableBaseValuesInput, setEnableBaseValuesInput] = useState(false);
  const [enableTargetValuesInput, setEnableTargetValuesInput] = useState(false);

  const [sectors, setSectors] = useState<{ value: string, name: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, name: string }[]>([]);

  const getSectors = () => {
    const sectorData = sectorsReducer.sectors.map((sector: any) => ({
      value: sector.id,
      label: sector.name
    }));
    setSectors(sectorData);
  };

  const getProgramData = () => {
    const programData = programsReducer.programs.map((program: any) => ({
      value: program.id,
      label: program.name
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
      originalValueDate: '',
      revisedValue: 0,
      revisedValueDate: '',
    },
    target: {
      originalValue: 0,
      originalValueDate: getCurrentDate(),
      revisedValue: 0,
      revisedValueDate: getCurrentDate(),
    }
  };

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
        <Modal.Title>{translations['amp.dashboard:add-new']}</Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={initialValues}
        validationSchema={indicatorValidationSchema}
        onSubmit={(values) => {
          const { name, description, code, sectors, programs, ascending, creationDate, base, target } = values;

          const indicatorData = {
            name,
            description,
            code,
            sectors,
            programs,
            ascending,
            creationDate: creationDate ? formatJavascriptDate(creationDate) : null,
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

          dispatch(createIndicator(indicatorData));

          if (!createIndicatorState.loading && !createIndicatorState?.error) {
            MySwal.fire({
              title: 'Success',
              text: 'Indicator created successfully',
              icon: 'success',
              confirmButtonText: 'Ok',
            }).then(() => {
              handleClose();
              dispatch(getIndicators());
            });
            return;
          }

          MySwal.fire({
            title: 'Error',
            text: createIndicatorState.loading ? 'Creating Indicator...' : createIndicatorState.error,
            icon: 'error',
            confirmButtonText: 'Ok',
          });


        }}
      >
        {(props) => (
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
                  placeholder={ translations["amp.indicatormanager:enter-indicator-name"]}
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
                  placeholder={ translations["amp.indicatormanager:enter-indicator-description"]}
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
                  placeholder={translations["amp.indicatormanager:enter-indicator-code"]}
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
                  label={<h4 className={styles.checkbox_label}>{translations["amp.indicatormanager:enable-base"]}</h4>}
                />
              </Form.Check>

              {enableBaseValuesInput && (
                <Form.Group as={Col}>
                  <Form.Label><h4>{translations["amp.indicatormanager:base-values"]}</h4></Form.Label>
                  <Form.Row>
                    <Form.Group>
                      <Form.Label>{translations['amp.indicatormanager:original-value']}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base?.originalValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="base.originalValue"
                        type="text"
                        placeholder={ translations["amp.indicatormanager:enter-original-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.originalValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group>
                      <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
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
                      <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base.revisedValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="base.revisedlValue"
                        type="text"
                        placeholder={translations["amp.indicatormanager:enter-revised-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group>
                      <Form.Label>{translations['amp.indicatormanager:revised-value-date']}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base.revisedValueDate}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="base.revisedValueDate"
                        type="date"
                        placeholder={translations['amp.indicatormanager:enter-revised-value-date']} />

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
                  label={<h4 className={styles.checkbox_label}>{translations["amp.indicatormanager:enable-target"]}</h4>}
                />
              </Form.Check>

              {enableTargetValuesInput && (
                <Form.Group as={Col}>
                  <Form.Label><h4>{translations["amp.indicatormanager:target-values"]}</h4></Form.Label>
                  <Form.Row>
                    <Form.Group>
                      <Form.Label>{translations["amp.indicatormanager:target-value"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.target.originalValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.originalValue"
                        type="text"
                        placeholder={translations["amp.indicatormanager:enter-target-value"]} />
                    </Form.Group>
                    <Form.Group>
                      <Form.Label>{translations["amp.indicatormanager:target-value-date"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.target.originalValueDate}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.originalValueDate"
                        type="date"
                        placeholder={translations["amp.indicatormanager:enter-target-value-date"]} />
                    </Form.Group>
                  </Form.Row>

                  <Form.Row>
                    <Form.Group>
                      <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base.revisedValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="base.revisedlValue"
                        type="text"
                        placeholder={translations["amp.indicatormanager:enter-revised-value"]}/>

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group>
                      <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base.revisedValueDate}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="base.revisedValueDate"
                        type="date"
                        placeholder={translations["amp.indicatormanager:enter-revised-value-date"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValueDate}
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Form.Row>
                </Form.Group>
              )}

              <Form.Group controlId="formIndicatorSectors">
                <Form.Label>{translations["amp.indicatormanager:sectors"]}</Form.Label>
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
                      onBlur={props.handleBlur}
                      className="basic-multi-select"
                      classNamePrefix="select"
                    />
                  ) : null
                }
              </Form.Group>

              <Form.Group controlId="programs">
                <Form.Label>{translations["amp.indicatormanager:programs"]}</Form.Label>
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
                      onBlur={props.handleBlur}
                      className="basic-multi-select"
                      classNamePrefix="select"
                    />
                  ) : null
                }
              </Form.Group>

              <Form.Group controlId="Ascending">
                <Form.Label>{translations["amp.indicatormanager:ascending"]}</Form.Label>
                <Select
                  name="ascending"
                  options={ascendingOptions}
                  className="basic-multi-select"
                  classNamePrefix="select"
                />
              </Form.Group>

              <Form.Group controlId="formCreationDate">
                <Form.Label>{translations["amp.indicatormanager:table-header-creation-date"]}</Form.Label>
                <Form.Control type="date" readOnly defaultValue={getCurrentDate()} />
              </Form.Group>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={handleClose}>
                {translations["amp.indicatormanager:close"]}
              </Button>
              <Button type="submit" variant="success">
                {translations["amp.indicatormanager:save"]}
              </Button>
            </Modal.Footer>
          </Form>
        )}
      </Formik>
    </Modal>
  );
};

export default AddNewIndicatorModal;
