import React, { useEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col, Row
} from 'react-bootstrap';
import { Formik, FormikProps } from 'formik';
import Select from 'react-select';
import styles from './css/IndicatorModal.module.css';
import { backendDateToJavascriptDate, formatJavascriptDate, getCurrentDate } from '../../utils/dateFn';
import { indicatorValidationSchema } from '../../utils/validator';
import { useDispatch, useSelector } from 'react-redux';
import { BaseAndTargetValueType, DefaultComponentProps, ProgramSchemeType } from '../../types';
import { createIndicator } from '../../reducers/createIndicatorReducer';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';
import { extractChildrenFromProgramScheme } from '../../utils/helpers';
import useDidMountEffect from '../../utils/hooks';

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
  programId: string;
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
  const [programFieldVisible, setProgramFieldVisible] = useState(false);
  const [selectedProgramSchemeId, setSelectedProgramSchemeId] = useState<string | null>(null);

  const [sectors, setSectors] = useState<{ value: string, name: string }[]>([]);
  const [programSchemes, setProgramSchemes] = useState<{ value: string, name: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, label: string }[]>([]);

  const [baseValueOriginalDateDisabled, setBaseValueOriginalDateDisabled] = useState(false);
  const [targetValueOriginalDateFieldDisabled, setTargetValueOriginalDateDisabled] = useState(false);

  const formikRef = useRef<FormikProps<IndicatorFormValues>>(null);

  const getSectors = () => {
    const sectorData = sectorsReducer.sectors.map((sector: any) => ({
      value: sector.id,
      label: sector.name
    }));
    setSectors(sectorData);
  };

  const getProgramSchemes = () => {
    const programData = programsReducer.programs.map((program: ProgramSchemeType) => ({
      value: program.ampProgramSettingsId,
      label: program.name
    }));
    setProgramSchemes(programData);
  };

  const getProgramsForProgramScheme = () => {
    if (selectedProgramSchemeId) {
      setProgramFieldVisible(false);
      setEnableBaseValuesInput(false);
      setEnableTargetValuesInput(false);
      formikRef?.current?.setFieldValue("base.originalValueDate", "");
      formikRef?.current?.setFieldValue("target.originalValueDate", "");

      const programScheme: ProgramSchemeType = programsReducer.programs.find((program: ProgramSchemeType) => program.ampProgramSettingsId.toString() === selectedProgramSchemeId.toString());
      if (programScheme) {
        const children = extractChildrenFromProgramScheme(programScheme);
        const programData = children.map((program: any) => ({
          value: program.id.toString(),
          label: program.name
        }));

        setPrograms([]);
        setPrograms(programData);
        setProgramFieldVisible(true);


        if (programScheme.startDate) {
          formikRef.current?.setFieldValue("base.originalValueDate", "");
          formikRef?.current?.setFieldValue("base.originalValueDate", backendDateToJavascriptDate(programScheme.startDate || ''));
          setEnableBaseValuesInput(true);
          setBaseValueOriginalDateDisabled(true);
        }
    
        if (programScheme.endDate) {
          formikRef.current?.setFieldValue("target.originalValueDate", "");
          formikRef?.current?.setFieldValue("target.originalValueDate", backendDateToJavascriptDate(programScheme.endDate || ''));
          setEnableTargetValuesInput(true);
          setTargetValueOriginalDateDisabled(true);
        }
      }

    }
  }


  const handleProgramSchemeChange = (selectedOption: any, props: FormikProps<IndicatorFormValues>) => {
    setSelectedProgramSchemeId(selectedOption);
    console.log(selectedOption.value);
    props.setFieldValue("program", "");
    setProgramFieldVisible(false);
    
  

  };


  useEffect(() => {
    getProgramsForProgramScheme();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedProgramSchemeId])


  useEffect(() => {
    getSectors();
    getProgramSchemes();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sectorsReducer.sectors, programsReducer.programs])

  useDidMountEffect(() =>{
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
  }, [createIndicatorState])

  const initialValues: IndicatorFormValues = {
    name: '',
    description: '',
    code: '',
    sectors: [],
    programId: '',
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
      originalValueDate: '',
      revisedValue: 0,
      revisedValueDate: ''
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
      size='lg'
    >
      <Modal.Header closeButton>
        <Modal.Title>{translations['amp.dashboard:add-new']}</Modal.Title>
      </Modal.Header>
      <Formik
        innerRef={formikRef}
        initialValues={initialValues}
        validationSchema={indicatorValidationSchema}
        onSubmit={(values) => {
          const { name, description, code, sectors, programId, ascending, creationDate, base, target } = values;

          const indicatorData = {
            name,
            description,
            code,
            sectors,
            programId: programId ? parseInt(programId) : null,
            ascending,
            creationDate: creationDate ? formatJavascriptDate(creationDate) : null,
            base: enableBaseValuesInput ? {
              originalValue: parseInt(String(base.originalValue)),
              originalValueDate: base.originalValueDate ? formatJavascriptDate(base.originalValueDate) : null,
              revisedValue: parseInt(String(base.revisedValue)),
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
        }}
      >
        {(props) => (
          <Form noValidate onSubmit={props.handleSubmit}>
            <Modal.Body>
              <div className={styles.viewmodal_wrapper}>
                <Row className={styles.view_row}>
                  <Form.Group as={Col} className={styles.view_item} controlId="formBasicName">
                    <Form.Label>Indicator Name</Form.Label>
                    <Form.Control
                      defaultValue={props.values.name}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="name"
                      className={`${styles.input_field} ${(props.errors.name && props.touched.name) && styles.text_is_invalid}`}
                      isInvalid={!!props.errors.name}
                      required
                      aria-required type="text"
                      placeholder={translations["amp.indicatormanager:enter-indicator-name"]}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.name}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className={styles.view_item} controlId="formIndicatorCode">
                    <Form.Label>Indicator Code</Form.Label>
                    <Form.Control
                      defaultValue={props.values.code}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="code"
                      required
                      type="text"
                      className={`${styles.input_field} ${(props.errors.code && props.touched.code) && styles.text_is_invalid}`}
                      placeholder={translations["amp.indicatormanager:enter-indicator-code"]}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.code}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formBasicDescription">
                    <Form.Label>Indicator Description</Form.Label>
                    <Form.Control
                      defaultValue={props.values.description}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="description"
                      type="text"
                      className={`${styles.input_field} ${(props.errors.description && props.touched.description) && styles.text_is_invalid}`}
                      placeholder={translations["amp.indicatormanager:enter-indicator-description"]}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.description}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Row>

                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_item} controlId="Ascending">
                    <Form.Label>{translations["amp.indicatormanager:ascending"]}</Form.Label>
                    <Select
                      name="ascending"
                      options={ascendingOptions}
                      className="basic-multi-select"
                      classNamePrefix="select"
                    />
                  </Form.Group>

                  <Form.Group className={styles.view_item} controlId="formCreationDate">
                    <Form.Label>{translations["amp.indicatormanager:table-header-creation-date"]}</Form.Label>
                    <Form.Control type="date" readOnly defaultValue={getCurrentDate()} />
                  </Form.Group>
                </Row>

                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formIndicatorSectors">
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
                          className={`basic-multi-select ${styles.input_field} ${(props.errors.sectors && props.touched.sectors) && styles.text_is_invalid}`}
                          classNamePrefix="select"
                        />
                      ) : null
                    }
                  </Form.Group>
                </Row>

                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="programScheme">
                    <Form.Label>{translations["amp.indicatormanager:program-scheme"]}</Form.Label>
                    {
                      programSchemes.length > 0 ? (
                        <Select
                          name="programScheme"
                          options={programSchemes}
                          onChange={(selectedValue) => {
                            // set the formik value with the selected values and remove the label
                            if (selectedValue) {
                              handleProgramSchemeChange(selectedValue.value, props);
                            }
                          }}
                          getOptionValue={(option) => option.value}
                          onBlur={props.handleBlur}
                          className={`basic-multi-select ${styles.input_field}`}
                          classNamePrefix="select"
                        />
                      ) : null
                    }
                  </Form.Group>
                </Row>

                {programFieldVisible && (
                  <Row className={styles.view_row}>
                    <Form.Group className={styles.view_one_item} controlId="programs">
                      <Form.Label>{translations["amp.indicatormanager:programs"]}</Form.Label>
                      {
                        programs.length > 0 ? (
                          <Select
                            name="programs"
                            options={programs}
                            onChange={(selectedValue) => {
                              // set the formik value with the selected values and remove the label
                              props.setFieldValue('programId', selectedValue?.value);
                            }}
                            getOptionValue={(option) => option.value}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${styles.input_field} ${(props.errors.programId && props.touched.programId) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                          />
                        ) : null
                      }
                    </Form.Group>
                  </Row>

                )}



                <Row className={styles.view_row}>
                  <Form.Check className={styles.view_one_item}>
                    <Form.Check
                      type="switch"
                      name="baseToggle"
                      checked={enableBaseValuesInput}
                      onChange={() => setEnableBaseValuesInput(!enableBaseValuesInput)}
                      label={<h4 className={styles.checkbox_label}>{translations["amp.indicatormanager:enable-base"]}</h4>}
                    />
                  </Form.Check>
                </Row>

                {enableBaseValuesInput && (
                  <Form.Group as={Col}>
                    <Row className={styles.view_row}>
                      <Form.Label className={styles.view_one_item}>
                        <h5>{translations["amp.indicatormanager:base-values"]}</h5>
                      </Form.Label>
                    </Row>

                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item}>
                        <Form.Label>{translations['amp.indicatormanager:original-value']}</Form.Label>
                        <Form.Control
                          defaultValue={props.values.base?.originalValue}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="base.originalValue"
                          type="text"
                          className={`${styles.input_field} ${(props.errors.base?.originalValue && props.touched.base?.originalValue) && styles.text_is_invalid}`}
                          placeholder={translations["amp.indicatormanager:enter-original-value"]} />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.base?.originalValue}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group className={styles.view_item}>
                        <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
                        <Form.Control
                          defaultValue={props.values.base?.originalValueDate}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="base.originalValueDate"
                          type="date"
                          disabled={baseValueOriginalDateDisabled}
                          className={`${styles.input_field} ${(props.errors.base?.originalValueDate && props.touched.base?.originalValueDate) && styles.text_is_invalid}`}
                          placeholder="Enter Original Value Date" />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.base?.originalValueDate}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Row>

                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item}>
                        <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                        <Form.Control
                          defaultValue={props.values.base.revisedValue}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="base.revisedValue"
                          type="text"
                          className={`${styles.input_field} ${(props.errors.base?.revisedValue && props.touched.base?.revisedValue) && styles.text_is_invalid}`}
                          placeholder={translations["amp.indicatormanager:enter-revised-value"]} />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.base?.revisedValue}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group className={styles.view_item}>
                        <Form.Label>{translations['amp.indicatormanager:revised-value-date']}</Form.Label>
                        <Form.Control
                          defaultValue={props.values.base.revisedValueDate}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                          name="base.revisedValueDate"
                          type="date"
                          className={`${styles.input_field} ${(props.errors.base?.revisedValueDate && props.touched.base?.revisedValueDate) && styles.text_is_invalid}`}
                          placeholder={translations['amp.indicatormanager:enter-revised-value-date']} />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.base?.revisedValueDate}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Row>
                  </Form.Group>
                )
                }

              <Row className={styles.view_row}>
                <Form.Check className={styles.view_one_item}>
                  <Form.Check
                    type="switch"
                    name="baseToggle"
                    checked={enableTargetValuesInput}
                    onChange={() => setEnableTargetValuesInput(!enableTargetValuesInput)}
                    label={<h4 className={styles.checkbox_label}>{translations["amp.indicatormanager:enable-target"]}</h4>}
                  />
                </Form.Check>
              </Row>

              {enableTargetValuesInput && (
                <Form.Group as={Col}>
                  <Form.Label><h4>{translations["amp.indicatormanager:target-values"]}</h4></Form.Label>
                  <Row className={styles.view_row}>
                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:target-value"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.target.originalValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.originalValue"
                        type="text"
                        className={`${styles.input_field} ${(props.errors.target?.originalValue && props.touched.target?.originalValue) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-target-value"]} />
                    </Form.Group>
                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:target-value-date"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.target.originalValueDate}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.originalValueDate"
                        type="date"
                        disabled={targetValueOriginalDateFieldDisabled}
                        className={`${styles.input_field} ${(props.errors.target?.originalValueDate && props.touched.target?.originalValueDate) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-target-value-date"]} />
                    </Form.Group>
                  </Row>

                  <Row className={styles.view_row}>
                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base.revisedValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.revisedlValue"
                        type="text"
                        className={`${styles.input_field} ${(props.errors.base?.revisedValue && props.touched.base?.revisedValue) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-revised-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.base.revisedValueDate}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.revisedValueDate"
                        type="date"
                        className={`${styles.input_field} ${(props.errors.base?.revisedValueDate && props.touched.base?.revisedValueDate) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-revised-value-date"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValueDate}
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Row>
                </Form.Group>
              )}
            </div>  

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
