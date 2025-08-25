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
import { getOutcomes } from '../../reducers/fetchOutcomesReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';
import { checkObjectIsNull, extractChildrenFromProgramScheme } from '../../utils/helpers';
import useDidMountEffect from '../../utils/hooks';
import DateInput from '../DateInput';
import lodash from 'lodash';
import { getResponsibleOrgs } from '../../reducers/fetchResponsibleOrgsReducer';

const MySwal = withReactContent(Swal);

interface AddNewIndicatorModalProps extends DefaultComponentProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
  filterBySector: boolean;
  filterByProgram: boolean;
}

interface IndicatorFormValues {
  name: string;
  description?: string;
  code: string;
  relevanceForClimateChange?: string;
  indicatorType?: string;
  sectors: number[];
  logframeLinks: string[];
  data?: string;
  dataSource?: string;
  disaggregation: number[];
  unitOfMeasure?: number;
  calculationMethod?: string;
  responsibleOrganizations: number[];
  frequency?: number;
  ascending: boolean;
  creationDate?: any;
  programId: string;
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
  outputId?: number;
  outcomeId?: number;
  indicatorsCategory?: number;
}

const AddNewIndicatorModal: React.FC<AddNewIndicatorModalProps> = (props) => {
  const { show, setShow, translations, filterBySector, filterByProgram } = props;

  const ascendingOptions = [
    { value: true, label: translations["amp.indicatormanager:true"] },
    { value: false, label: translations["amp.indicatormanager:false"] }
  ];

  const nodeRef = useRef(null);
  const dispatch = useDispatch();
  const settingsReducer: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);

  const formatDate = (date: string | Date) => {
    return DateUtil.dateToString(date, settingsReducer['default-date-format']);
  }

  const convertDateToISO = (date?: string) => {
    if (!date) {
      return '';
    }
    return DateUtil.toISO8601(date, settingsReducer['default-date-format']);
  };

  const handleClose = () => setShow(false);
  const createIndicatorState = useSelector((state: any) => state.createIndicatorReducer);

  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);
  const outcomesState = useSelector((state: any) => state.fetchOutcomesReducer);
  const allOutcomes = outcomesState.outcomes || [];

  const [programFieldVisible, setProgramFieldVisible] = useState(false);
  const [selectedProgramSchemeId, setSelectedProgramSchemeId] = useState<string | null>(null);

  const [sectors, setSectors] = useState<{ value: string, name: string }[]>([]);
  const [categories, setCategories] = useState<{ value: string, name: string }[]>([]);
  const [programSchemes, setProgramSchemes] = useState<{ value: string, name: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, label: string }[]>([]);

  const [baseOriginalValueDateDisabled, setBaseOriginalValueDateDisabled] = useState(false);
  const [targetOriginalValueDateDisabled, setTargetOriginalValueDateDisabled] = useState(false);

  const formikRef = useRef<FormikProps<IndicatorFormValues>>(null);

  // --- Outcome/Output dropdown logic ---
  const [filteredOutputs, setFilteredOutputs] = useState<{ id: number, name: string }[]>([]);

  const responsibleOrgOptions = useSelector((state: any) => state.fetchResponsibleOrgsReducer.options || []);

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
          formikRef?.current?.setFieldValue("base.originalValueDate", convertDateToISO(programScheme.startDate || ''));
        }

        if (programScheme.endDate) {
          formikRef.current?.setFieldValue("target.originalValueDate", "");
          formikRef?.current?.setFieldValue("target.originalValueDate", convertDateToISO(programScheme.endDate || ''));
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

  useEffect(() => {
    if (!allOutcomes.length) {
      dispatch(getOutcomes());
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, allOutcomes.length]);

  const [selectedOutcomeId, setSelectedOutcomeId] = useState<number | null>(null);

  useEffect(() => {
    if (selectedOutcomeId) {
      const found = allOutcomes.find(o => o.id === selectedOutcomeId);
      setFilteredOutputs(found ? found.outputs : []);
    } else {
      setFilteredOutputs([]);
    }
  }, [selectedOutcomeId, allOutcomes]);

  useEffect(() => {
    if (!responsibleOrgOptions.length) {
      dispatch(getResponsibleOrgs());
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, responsibleOrgOptions.length]);

  console.log("indicator===>", createIndicatorState);

  useDidMountEffect(() => {
    if (createIndicatorState.loading) {
      MySwal.fire({
        icon: 'info',
        title: `${translations["amp.indicatormanager:creating-indicator"]}...`,
        timer: 1000
      });
      return;
    }

    if (!createIndicatorState.loading && !createIndicatorState?.error && createIndicatorState?.createdIndicator?.id) {
      MySwal.fire({
        title: translations["amp.indicatormanager:success"],
        text: translations["amp.indicatormanager:save-success"],
        icon: 'success',
        confirmButtonText: translations["amp.indicatormanager:ok"],
      }).then(() => {
        dispatch(getIndicators());
        handleClose();
      });
      return;
    }

    if (createIndicatorState.error && !createIndicatorState.loading && !createIndicatorState.createdIndicator) {
      MySwal.fire({
        title: translations["amp.indicatormanager:error"],
        text: createIndicatorState.loading ? translations["Error creating indicator"] : createIndicatorState.error,
        icon: 'error',
        confirmButtonText: translations["amp.indicatormanager:ok"],
      });
    }

  }, [createIndicatorState])

  const initialValues: IndicatorFormValues = {
    name: '',
    description: '',
    code: '',
    relevanceForClimateChange: '',
    indicatorType: '',
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
    outputId: undefined,
    outcomeId: undefined,
    logframeLinks: [],
    data: '',
    dataSource: '',
    disaggregation: [],
    unitOfMeasure: undefined,
    calculationMethod: '',
    responsibleOrganizations: [],
    frequency: undefined,
  };

  // --- Dynamic category options from fetchAmpCategoryReducer ---
  const getCategoryOptions = (keyName: string, isMulti = false) => {
    // Filter only category values with the correct keyName
    return categoriesReducer.categories
      .filter((cat: any) => cat.ampCategoryClass && cat.ampCategoryClass.keyName === keyName)
      .map((cat: any) => ({ value: cat.id, label: cat.value }));
  };

  const indicatorTypeOptions = getCategoryOptions('indicator_type');
  const disaggregationOptions = getCategoryOptions('indicator_disaggregation', true);
  const unitOfMeasureOptions = getCategoryOptions('indicator_unit_of_measure');
  const frequencyOptions = getCategoryOptions('indicator_frequency');

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
          if (selectedProgramSchemeId && !programId) {
            MySwal.fire({
              title: translations['amp.indicatormanager:error'],
              text: translations['amp.indicatormanager:errors-program-is-required'],
              icon: 'error',
              confirmButtonText: translations['amp.indicatormanager:ok'],
            })

            return;
          }

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
            indicatorsCategory,
            outputId: values.outputId,
            outcomeId: values.outcomeId,
            relevanceForClimateChange: values.relevanceForClimateChange,
            indicatorType: values.indicatorType,
            logframeLinks: values.logframeLinks,
            data: values.data,
            dataSource: values.dataSource,
            disaggregation: values.disaggregation,
            unitOfMeasure: values.unitOfMeasure,
            calculationMethod: values.calculationMethod,
            responsibleOrganizations: values.responsibleOrganizations,
            frequency: values.frequency,
          };

          dispatch(createIndicator(indicatorData));
        }}
      >
        {(props) => (
            <Form noValidate onSubmit={props.handleSubmit}>
              <Modal.Body>
                <div className={styles.viewmodal_wrapper}>
                  {/* Core Indicator Information */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{translations["amp.indicatormanager:core-info"]}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formBasicName">
                        <Form.Label>{translations["amp.indicatormanager:indicator-name"]}</Form.Label>
                        <Form.Control
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
                      <Form.Group as={Col} className={styles.view_one_item} controlId="formBasicDescription">
                        <Form.Label>{translations["amp.indicatormanager:indicator-description"]}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="description"
                            as="textarea"
                            rows={2}
                            className={`${styles.input_field} ${(props.errors.description && props.touched.description) && styles.text_is_invalid}`}
                            placeholder={translations["amp.indicatormanager:enter-indicator-description"]}
                        />
                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.description}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group as={Col} className={styles.view_one_item} controlId="formRelevanceForClimateChange">
                        <Form.Label>{translations["amp.indicatormanager:relevance-for-climate-change"]}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="relevanceForClimateChange"
                            as="textarea"
                            rows={2}
                            className={styles.input_field}
                            placeholder={translations["amp.indicatormanager:relevance-for-climate-change"]}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formIndicatorType">
                        <Form.Label>Type</Form.Label>
                        <Select
                            name="indicatorType"
                            options={indicatorTypeOptions}
                            onChange={(selectedValue) => {
                              props.setFieldValue('indicatorType', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.indicatorType && props.touched.indicatorType) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={indicatorTypeOptions.find(opt => opt.value === props.values.indicatorType) || null}
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Categorization and Linkage */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{translations["amp.indicatormanager:categorization-linkage-info"] || "Categorization and Linkage"}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formOutcome">
                        <Form.Label>{translations["amp.indicatormanager:outcome"]}</Form.Label>
                        <Select
                            name="outcomeId"
                            options={allOutcomes.map(outcome => ({ value: outcome.id, label: outcome.name }))}
                            onChange={(selectedValue) => {
                              setSelectedOutcomeId(selectedValue?.value ?? null);
                              props.setFieldValue('outcomeId', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.outcomeId && props.touched.outcomeId) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={allOutcomes.find(outcome => outcome.id === selectedOutcomeId) ? { value: selectedOutcomeId, label: allOutcomes.find(outcome => outcome.id === selectedOutcomeId)?.name } : null}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formOutput">
                        <Form.Label>{translations["amp.indicatormanager:output"]}</Form.Label>
                        <Select
                            name="outputId"
                            options={filteredOutputs.map(output => ({ value: output.id, label: output.name }))}
                            onChange={(selectedValue) => {
                              props.setFieldValue('outputId', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.outputId && props.touched.outputId) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={filteredOutputs.find(output => output.id === props.values.outputId) ? { value: props.values.outputId, label: filteredOutputs.find(output => output.id === props.values.outputId)?.name } : null}
                            isDisabled={!selectedOutcomeId}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="programScheme">
                        <Form.Label>Link to Logframe (Program Scheme)</Form.Label>
                        <Select
                            name="programScheme"
                            options={programSchemes}
                            onChange={(selectedValue) => {
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
                      </Form.Group>
                      {programFieldVisible && (
                          <Form.Group className={styles.view_item} controlId="programs">
                            <Form.Label>Program</Form.Label>
                            <Select
                                name="programs"
                                options={programs}
                                onChange={(selectedValue) => {
                                  props.setFieldValue("programId", selectedValue?.value);
                                }}
                                isClearable
                                getOptionValue={(option) => option.value}
                                onBlur={props.handleBlur}
                                className={`basic-multi-select ${styles.input_field} ${(props.errors.programId && props.touched.programId) && styles.text_is_invalid}`}
                                classNamePrefix="select"
                            />
                          </Form.Group>
                      )}
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_one_item} controlId="formIndicatorSectors">
                        <Form.Label>Sector</Form.Label>
                        <Select
                            isMulti
                            name="sectors"
                            options={sectors}
                            onChange={(values) => {
                              const selectedValues = values.map((value: any) => parseInt(value.value))
                              props.setFieldValue('sectors', selectedValues);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.sectors && props.touched.sectors) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Data Definition and Sourcing */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{translations["amp.indicatormanager:data-definition-sourcing-info"] || "Data Definition and Sourcing"}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formData">
                        <Form.Label>{translations["amp.indicatormanager:data"]}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="data"
                            type="text"
                            className={styles.input_field}
                            placeholder={translations["amp.indicatormanager:enter-data"]}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formDataSource">
                        <Form.Label>{translations["amp.indicatormanager:data-source"]}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="dataSource"
                            type="text"
                            className={styles.input_field}
                            placeholder={translations["amp.indicatormanager:enter-data-source"]}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formDisaggregation">
                        <Form.Label>Disaggregation</Form.Label>
                        <Select
                            isMulti
                            name="disaggregation"
                            options={disaggregationOptions}
                            onChange={(selectedValues) => {
                              props.setFieldValue('disaggregation', selectedValues.map((v: any) => v.value));
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.disaggregation && props.touched.disaggregation) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={disaggregationOptions.filter(opt => props.values.disaggregation?.includes(opt.value))}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formUnitOfMeasure">
                        <Form.Label>Unit of Measure</Form.Label>
                        <Select
                            name="unitOfMeasure"
                            options={unitOfMeasureOptions}
                            onChange={(selectedValue) => {
                              props.setFieldValue('unitOfMeasure', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.unitOfMeasure && props.touched.unitOfMeasure) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={unitOfMeasureOptions.find(opt => opt.value === props.values.unitOfMeasure) || null}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_one_item} controlId="formCalculationMethod">
                        <Form.Label>Calculation Method</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="calculationMethod"
                            type="text"
                            className={styles.input_field}
                            placeholder="Describe how the indicator's value is calculated"
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Responsibility and Frequency */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>Responsibility and Frequency</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formResponsibleOrganizations">
                        <Form.Label>Responsible Organization(s)</Form.Label>
                        <Select
                            isMulti
                            name="responsibleOrganizations"
                            options={responsibleOrgOptions}
                            onChange={(selectedValues) => {
                              props.setFieldValue('responsibleOrganizations', selectedValues.map((v: any) => v.value));
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.responsibleOrganizations && props.touched.responsibleOrganizations) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={responsibleOrgOptions.filter(opt => props.values.responsibleOrganizations?.includes(opt.value))}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formFrequency">
                        <Form.Label>Frequency</Form.Label>
                        <Select
                            name="frequency"
                            options={frequencyOptions}
                            onChange={(selectedValue) => {
                              props.setFieldValue('frequency', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.frequency && props.touched.frequency) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            value={frequencyOptions.find(opt => opt.value === props.values.frequency) || null}
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Value Tracking */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>Value Tracking</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Form.Group as={Col}>
                      <Form.Label>
                        <h4>{translations["amp.indicatormanager:base-values"]}</h4>
                      </Form.Label>
                      {/* Original Value and Date in one row */}
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
                              translations={translations}
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
                      {/* Revised Value and Date in one row */}
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
                              translations={translations}
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
                    {/* Original Value and Date in one row */}
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
                        <DateInput translations={translations}
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
                    {/* Revised Value and Date in one row */}
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
                            translations={translations}
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
                  {/* Other Considerations */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>Other Considerations</h5></Col></Row>
                  <div className={styles.sectionContainer}>
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
                            defaultValue={{
                              value: false,
                              label: translations["amp.indicatormanager:true"]
                            }}
                        />
                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.ascending}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group className={styles.view_item} controlId="formCreationDate">
                        <Form.Label>{translations["amp.indicatormanager:table-header-creation-date"]}</Form.Label>
                        <DateInput
                            translations={translations}
                            name="creationDate"
                            disabled
                            value={props.values.creationDate}
                            clearIcon={null}
                            calendarIcon={null}
                            className={styles.input_field} />
                      </Form.Group>
                    </Row>
                  </div>
                </div>
                </div>

              </Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                  {translations["amp.indicatormanager:close"]}
                </Button>
                <Button type="submit" variant="success" >
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

