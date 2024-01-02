import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col, Row
} from 'react-bootstrap';
import { Formik, FormikProps } from 'formik';
import Select from 'react-select';
import styles from './css/IndicatorModal.module.css';
import { DateUtil } from '../../utils/dateFn';
import { translatedIndicatorValidationSchema } from '../../utils/validator';
import { useDispatch, useSelector } from 'react-redux';
import { BaseAndTargetValueType, DefaultComponentProps, ProgramSchemeType, SettingsType } from '../../types';
import { createIndicator } from '../../reducers/createIndicatorReducer';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';
import { checkObjectIsNull, extractChildrenFromProgramScheme } from '../../utils/helpers';
import useDidMountEffect from '../../utils/hooks';
import DateInput from '../DateInput';
import lodash from 'lodash';

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
  creationDate?: any;
  programId: string;
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
  indicatorsCategory?: string;
}

const AddNewIndicatorModal: React.FC<AddNewIndicatorModalProps> = (props) => {
  const { show, setShow, translations } = props;
  const nodeRef = useRef(null);
  const dispatch = useDispatch();
  const settingsReducer: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);

  const formatDate = (date: string | Date) => {
    return DateUtil.dateToString(date, settingsReducer['default-date-format']);
  }

  const handleClose = () => setShow(false);
  const createIndicatorState = useSelector((state: any) => state.createIndicatorReducer);

  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);

  const [programFieldVisible, setProgramFieldVisible] = useState(false);
  const [selectedProgramSchemeId, setSelectedProgramSchemeId] = useState<string | null>(null);

  const [sectors, setSectors] = useState<{ value: string, name: string }[]>([]);
  const [categories, setCategories] = useState<{ value: string, name: string }[]>([]);
  const [programSchemes, setProgramSchemes] = useState<{ value: string, name: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, label: string }[]>([]);

  const [baseOriginalValueDateDisabled, setBaseOriginalValueDateDisabled] = useState(false);
  const [targetOriginalValueDateDisabled, setTargetOriginalValueDateDisabled] = useState(false);

  const formikRef = useRef<FormikProps<IndicatorFormValues>>(null);

  const getCategories = () => {
    const categoryData = categoriesReducer.categories.map((category: any) => ({
      value: category.id,
      label: category.value
    }));
    setCategories(categoryData);
  }

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

  const getProgramSchemes = () => {
    const programData = programsReducer.programSchemes.map((program: ProgramSchemeType) => ({
      value: program.ampProgramSettingsId,
      label: program.name
    }));
    setProgramSchemes(programData);
  };

  const getProgramsForProgramScheme = () => {
    if (selectedProgramSchemeId) {
      setProgramFieldVisible(false);
      formikRef?.current?.setFieldValue("base.originalValueDate", "");
      formikRef?.current?.setFieldValue("target.originalValueDate", "");
      setBaseOriginalValueDateDisabled(false);
      setTargetOriginalValueDateDisabled(false);

      const programScheme: ProgramSchemeType = programsReducer.programSchemes.find((program: ProgramSchemeType) => program.ampProgramSettingsId.toString() === selectedProgramSchemeId.toString());
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
          formikRef?.current?.setFieldValue("base.originalValueDate", DateUtil.backendDateToJavascriptDate(programScheme.startDate || ''));
          setBaseOriginalValueDateDisabled(true);
        }

        if (programScheme.endDate) {
          formikRef.current?.setFieldValue("target.originalValueDate", "");
          formikRef?.current?.setFieldValue("target.originalValueDate", DateUtil.backendDateToJavascriptDate(programScheme.endDate || ''));
          setTargetOriginalValueDateDisabled(true);
        }
      }

    }
  }

  const getCreationDate = () => {
    const date = DateUtil.getCurrentDate();
    formikRef?.current?.setFieldValue("creationDate", date);
  }


  const handleProgramSchemeChange = (selectedOption: any, props: FormikProps<IndicatorFormValues>) => {
    setSelectedProgramSchemeId(selectedOption);
    props.setFieldValue("programId", "");
    setProgramFieldVisible(false);
  };

  useLayoutEffect(() =>{
    getCreationDate();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  useEffect(() => {
    getProgramsForProgramScheme();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedProgramSchemeId])


  useEffect(() => {
    getSectors();
    getCategories();
    getProgramSchemes();
    getPrograms();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sectorsReducer.sectors, programsReducer.programs, programsReducer.programSchemes])

  console.log("indicator===>", createIndicatorState);

  useDidMountEffect(() => {
    if (createIndicatorState.loading) {
      MySwal.fire({
        icon: 'info',
        title: 'Creating Indicator...',
        timer: 1000
      });
      return;
    }

    if (!createIndicatorState.loading && !createIndicatorState?.error && createIndicatorState?.createdIndicator?.id) {
      MySwal.fire({
        title: 'Success',
        text: 'Indicator created successfully',
        icon: 'success',
        confirmButtonText: 'Ok',
      }).then(() => {
        dispatch(getIndicators());
        handleClose();
      });
      return;
    }

    MySwal.fire({
      title: 'Error',
      text: createIndicatorState.loading ? 'Error creating indicator' : createIndicatorState.error,
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
    creationDate: DateUtil.getCurrentDate().toString(),
    ascending: false,
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
    },
    indicatorsCategory: ''
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
        validationSchema={translatedIndicatorValidationSchema(translations)}
        onSubmit={(values) => {
          const { name, description, code, sectors, programId, ascending, creationDate, base, target, indicatorsCategory } = values;

          const indicatorData = {
            name,
            description,
            code,
            sectors,
            programId: programId ? parseInt(programId) : null,
            ascending,
            creationDate: creationDate ? formatDate(new Date(creationDate)) : null,
            base: checkObjectIsNull(base) ? null : {
              originalValue: base.originalValue ? lodash.toNumber(base.originalValue): null,
              originalValueDate: base.originalValueDate ? DateUtil.formatJavascriptDate(base.originalValueDate) : null,
              revisedValue: base.revisedValue ? lodash.toNumber(base.revisedValue) : null,
              revisedValueDate: base.revisedValueDate ? DateUtil.formatJavascriptDate(base.revisedValueDate) : null,
            },
            target: checkObjectIsNull(target) ? null : {
              originalValue: target.originalValue ? lodash.toNumber(target.originalValue) : null,
              originalValueDate: target.originalValueDate ? DateUtil.formatJavascriptDate(target.originalValueDate) : null,
              revisedValue: target.revisedValue ? lodash.toNumber(target.revisedValue) : null,
              revisedValueDate: target.revisedValueDate ? DateUtil.formatJavascriptDate(target.revisedValueDate) : null,
            },
            indicatorsCategory
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
                    <Form.Label>{translations["amp.indicatormanager:indicator-name"]}</Form.Label>
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
                    <Form.Label>{translations["amp.indicatormanager:indicator-code"]}</Form.Label>
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
                    <Form.Label>{translations["amp.indicatormanager:indicator-description"]}</Form.Label>
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
                      onBlur={props.handleBlur}
                      className={`${styles.input_field} ${(props.errors.ascending && props.touched.ascending) && styles.text_is_invalid}`}
                      classNamePrefix="select"
                      onChange={(value) => {
                        if (value) props.setFieldValue('ascending', value.value)
                      }}
                      defaultValue={ascendingOptions[1]}
                    />
                    <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                      {props.errors.ascending}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className={styles.view_item} controlId="formCreationDate">
                    <Form.Label>{translations["amp.indicatormanager:table-header-creation-date"]}</Form.Label>
                    <DateInput
                      name="creationDate"
                      defaultValue={props.values.creationDate}
                      disabled
                      value={props.values.creationDate}
                      clearIcon={null}
                      calendarIcon={null}
                      className={styles.input_field} />
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
                          isClearable
                          getOptionValue={(option) => option.value}
                          onBlur={props.handleBlur}
                          className={`basic-multi-select ${(props.errors.sectors && props.touched.sectors) && styles.text_is_invalid}`}
                          classNamePrefix="select"
                        />
                      ) : (
                            <Select
                                name="sectors"
                                isDisabled={true}
                                defaultValue={{ value: 0, label: translations["amp.indicatormanager:no-data"] }}
                            />
                      )
                    }
                  </Form.Group>
                </Row>

                <Row className={styles.view_row}>
                <Form.Group className={styles.view_one_item} controlId="formIndicatorCategories">
                    <Form.Label>{translations["amp.indicatormanager:indicators-category"]}</Form.Label>
                    {
                      categories.length > 0 ? (
                        <Select
                          name="categories"
                          options={categories}
                          onChange={(value) => {
                            // set the formik value with the selected values and remove the label
                            if (value) {
                              props.setFieldValue('indicatorsCategory', parseInt(value?.value));
                            }
                          }}
                          isClearable
                          getOptionValue={(option: any) => option.value}
                          onBlur={props.handleBlur}
                          className={`basic-multi-select ${(props.errors.indicatorsCategory && props.touched.indicatorsCategory) && styles.text_is_invalid}`}
                          classNamePrefix="select"
                        />
                      ) : (
                            <Select
                                name="categories"
                                isDisabled={true}
                                defaultValue={{ value: 0, label: translations["amp.indicatormanager:no-data"] }}
                            />
                      )
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
                          isClearable
                          getOptionValue={(option) => option.value}
                          onBlur={props.handleBlur}
                          className={`basic-multi-select ${styles.input_field}`}
                          classNamePrefix="select"
                        />
                      ) : (
                          <Select
                              name="programScheme"
                              isDisabled={true}
                              defaultValue={{ value: 0, label: translations["amp.indicatormanager:no-data"] }}
                          />
                      )
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
                            isClearable
                            getOptionValue={(option) => option.value}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${styles.input_field} ${(props.errors.programId && props.touched.programId) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                          />
                        ) :
                          <Select
                            name="programs"
                            isDisabled={true}
                            defaultValue={{ value: 0, label: translations["amp.indicatormanager:no-data"] }}
                          />
                      }
                    </Form.Group>
                  </Row>

                )}

                <Form.Group as={Col}>
                  <Row className={styles.view_row}>
                    <Form.Label className={styles.view_one_item}>
                      <h4>{translations["amp.indicatormanager:base-values"]}</h4>
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
                        type="number"
                        className={`${styles.input_field} ${(props.errors.base?.originalValue && props.touched.base?.originalValue) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-original-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.originalValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
                      <DateInput
                        name="base.originalValueDate"
                        value={props.values.base.originalValueDate}
                        onChange={(value) => {
                          if (value) {
                            props.setFieldValue('base.originalValueDate', value);
                          }
                        }}
                        onClear={() => {
                          props.setFieldValue('base.originalValueDate', null);
                        }}
                        onBlur={props.handleBlur}
                        disabled={baseOriginalValueDateDisabled}
                        className={`${styles.input_field} ${(props.errors.base?.originalValueDate && props.touched.base?.originalValueDate) && styles.text_is_invalid}`}/>

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
                        type="number"
                        className={`${styles.input_field} ${(props.errors.base?.revisedValue && props.touched.base?.revisedValue) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-revised-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations['amp.indicatormanager:revised-value-date']}</Form.Label>
                      <DateInput
                        value={props.values.base.revisedValueDate}
                        onChange={(value) => {
                          if (value) {
                            props.setFieldValue('base.revisedValueDate', value);
                          }
                        }}
                        onClear={() => {
                          props.setFieldValue('base.revisedValueDate', null);
                        }}
                        onBlur={props.handleBlur}
                        name="base.revisedValueDate"
                        className={`${styles.input_field} ${(props.errors.base?.revisedValueDate && props.touched.base?.revisedValueDate) && styles.text_is_invalid}`}
                         />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.base?.revisedValueDate}
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Row>
                </Form.Group>

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
                        type="number"
                        className={`${styles.input_field} ${(props.errors.target?.originalValue && props.touched.target?.originalValue) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-target-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.target?.originalValue}
                      </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:target-value-date"]}</Form.Label>
                      <DateInput
                        name="target.originalValueDate"
                        value={props.values.target.originalValueDate}
                        onChange={(value) => {
                          if (value) {
                            props.setFieldValue('target.originalValueDate', value);
                          }
                        }}
                        onClear={() => {
                          props.setFieldValue('target.originalValueDate', null);
                        }}
                        onBlur={props.handleBlur}
                        disabled={targetOriginalValueDateDisabled}
                        className={`${styles.input_field} ${(props.errors.target?.originalValueDate && props.touched.target?.originalValueDate) && styles.text_is_invalid}`} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.target?.originalValueDate}
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Row>

                  <Row className={styles.view_row}>
                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                      <Form.Control
                        defaultValue={props.values.target.revisedValue}
                        onChange={props.handleChange}
                        onBlur={props.handleBlur}
                        name="target.revisedValue"
                        type="number"
                        className={`${styles.input_field} ${(props.errors.target?.revisedValue && props.touched.target?.revisedValue) && styles.text_is_invalid}`}
                        placeholder={translations["amp.indicatormanager:enter-revised-value"]} />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.target?.revisedValue}
                      </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className={styles.view_item}>
                      <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                      <DateInput
                        value={props.values.target.revisedValueDate}
                        onChange={(value) => {
                          if (value) {
                            props.setFieldValue('target.revisedValueDate', value);
                          }
                        }}
                        onClear={() => {
                          props.setFieldValue('target.revisedValueDate', null);
                        }}
                        onBlur={props.handleBlur}
                        name="target.revisedValueDate"
                        className={`${styles.input_field} ${(props.errors.target?.revisedValueDate && props.touched.target?.revisedValueDate) && styles.text_is_invalid}`}
                         />

                      <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                        {props.errors.target?.revisedValueDate}
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Row>
                </Form.Group>
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
