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
                <Row className={styles.view_row}><Col><h5>Core Indicator Information</h5></Col></Row>
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
                      defaultValue={props.values.creationDate}
                      disabled
                      value={props.values.creationDate}
                      clearIcon={null}
                      calendarIcon={null}
                      className={styles.input_field} />
                  </Form.Group>
                </Row>

                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formRelevanceForClimateChange">
                    <Form.Label>Relevance for Climate Change Adaptation</Form.Label>
                    <Form.Control
                      defaultValue={props.values.relevanceForClimateChange}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="relevanceForClimateChange"
                      type="text"
                      className={styles.input_field}
                      placeholder="Describe relevance for climate change adaptation"
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formIndicatorType">
                    <Form.Label>Type</Form.Label>
                    <Select
                      name="indicatorType"
                      options={indicatorTypeOptions}
                      onChange={(value: { value: number; label: string } | null) => {
                        if (value) props.setFieldValue('indicatorType', value.value)
                      }}
                      isClearable
                      placeholder="Select type"
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                {/* Categorization and Linkage */}
                <Row className={styles.view_row}><Col><h5>Categorization and Linkage</h5></Col></Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formIndicatorOutcomes">
                    <Form.Label>Outcome</Form.Label>
                    <Select
                      name="outcomeId"
                      options={allOutcomes.map(outcome => ({ value: outcome.id, label: outcome.name }))}
                      placeholder="Select outcome"
                      onChange={(selectedValue) => {
                        setSelectedOutcomeId(selectedValue ? (selectedValue as { value: number }).value : null);
                        props.setFieldValue('outcomeId', selectedValue ? (selectedValue as { value: number }).value : null);
                      }}
                      isClearable
                      getOptionValue={(option) => String((option as { value: any }).value)}
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formIndicatorOutputs">
                    <Form.Label>Output</Form.Label>
                    <Select
                      name="outputId"
                      options={filteredOutputs.map(output => ({ value: output.id, label: output.name }))}
                      placeholder="Select output"
                      onChange={(selectedValue) => {
                        props.setFieldValue('outputId', selectedValue ? selectedValue.value : null);
                      }}
                      isClearable
                      getOptionValue={(option) => String((option as { value: any }).value)}
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                      isDisabled={filteredOutputs.length === 0}
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formLogframeLinks">
                    <Form.Label>Link to Logframe (Program Scheme)</Form.Label>
                    <Select
                      isMulti
                      name="logframeLinks"
                      options={programSchemes}
                      onChange={(values) => {
                        const selectedValues = values.map((value: any) => value.value)
                        props.setFieldValue('logframeLinks', selectedValues);
                      }}
                      isClearable
                      getOptionValue={(option) => String(option.value)}
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                {/* Sector (multi, mandatory) */}
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
                      isClearable
                      getOptionValue={(option) => String(option.value)}
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                {/* Data Definition and Sourcing */}
                <Row className={styles.view_row}><Col><h5>Data Definition and Sourcing</h5></Col></Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formData">
                    <Form.Label>Data</Form.Label>
                    <Form.Control
                      defaultValue={props.values.data}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="data"
                      type="text"
                      className={styles.input_field}
                      placeholder="Describe the data to be collected"
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formDataSource">
                    <Form.Label>Data Source</Form.Label>
                    <Form.Control
                      defaultValue={props.values.dataSource}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="dataSource"
                      type="text"
                      className={styles.input_field}
                      placeholder="Specify the data source"
                    />
                  </Form.Group>
                </Row>
                {/* Disaggregation, Unit of Measure, Calculation Method */}
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formDisaggregation">
                    <Form.Label>Disaggregation</Form.Label>
                    <Select
                      isMulti
                      name="disaggregation"
                      options={disaggregationOptions}
                      onChange={(values) => {
                        const selectedValues = values.map((value: any) => parseInt(value.value))
                        props.setFieldValue('disaggregation', selectedValues);
                      }}
                      isClearable
                      getOptionValue={(option: { value: number; label: string } | null) => String(option?.value)}
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formUnitOfMeasure">
                    <Form.Label>Unit of Measure</Form.Label>
                    <Select
                      name="unitOfMeasure"
                      options={unitOfMeasureOptions}
                      onChange={(value: { value: number; label: string } | null) => {
                        if (value) props.setFieldValue('unitOfMeasure', value.value)
                      }}
                      isClearable
                      placeholder="Select unit of measure"
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formCalculationMethod">
                    <Form.Label>Calculation Method</Form.Label>
                    <Form.Control
                      defaultValue={props.values.calculationMethod}
                      onChange={props.handleChange}
                      onBlur={props.handleBlur}
                      name="calculationMethod"
                      type="text"
                      className={styles.input_field}
                      placeholder="Describe calculation method"
                    />
                  </Form.Group>
                </Row>
                {/* Responsibility and Frequency */}
                <Row className={styles.view_row}><Col><h5>Responsibility and Frequency</h5></Col></Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formResponsibleOrganizations">
                    <Form.Label>Responsible Organization(s)</Form.Label>
                    <Select
                      isMulti
                      name="responsibleOrganizations"
                      options={responsibleOrgOptions}
                      onChange={(selected) => {
                        const selectedValues = Array.isArray(selected)
                          ? selected.map((option) => option.value)
                          : [];
                        props.setFieldValue('responsibleOrganizations', selectedValues);
                      }}
                      isClearable
                      getOptionValue={(option) => String((option as { value: any }).value)}
                      getOptionLabel={(option) => (option as { label: string }).label}
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_one_item} controlId="formFrequency">
                    <Form.Label>Frequency</Form.Label>
                    <Select
                      name="frequency"
                      options={frequencyOptions}
                      onChange={(value: { value: number; label: string } | null) => {
                        if (value) props.setFieldValue('frequency', value.value)
                      }}
                      isClearable
                      placeholder="Select frequency"
                      onBlur={props.handleBlur}
                      className={styles.input_field}
                      classNamePrefix="select"
                    />
                  </Form.Group>
                </Row>
                {/* Value Tracking - New Section */}
                <Row className={styles.view_row}><Col><h5>Value Tracking</h5></Col></Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_item} controlId="valueTracking">
                    <Form.Label><b>Value Tracking</b></Form.Label>
                    <div style={{ marginLeft: '1rem' }}>
                      <Form.Label><u>Base Value</u></Form.Label>
                      <Row>
                        <Col>
                          <Form.Label>Original Value (numeric)</Form.Label>
                          <Form.Control
                            type="number"
                            name="base.originalValue"
                            value={props.values.base.originalValue || ''}
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            isInvalid={!!props.errors.base?.originalValue}
                          />
                        </Col>
                        <Col>
                          <Form.Label>Date</Form.Label>
                          <DateInput
                              translations={translations}
                            name="base.originalValueDate"
                            value={props.values.base.originalValueDate || ''}
                            onChange={val => props.setFieldValue('base.originalValueDate', val)}
                            onBlur={props.handleBlur}
                          />
                        </Col>
                        <Col>
                          <Form.Label>Revised Value (numeric)</Form.Label>
                          <Form.Control
                            type="number"
                            name="base.revisedValue"
                            value={props.values.base.revisedValue || ''}
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            isInvalid={!!props.errors.base?.revisedValue}
                          />
                        </Col>
                        <Col>
                          <Form.Label>Date</Form.Label>
                          <DateInput
                              translations={translations}
                            name="base.revisedValueDate"
                            value={props.values.base.revisedValueDate || ''}
                            onChange={val => props.setFieldValue('base.revisedValueDate', val)}
                            onBlur={props.handleBlur}
                          />
                        </Col>
                      </Row>
                      <Form.Label style={{ marginTop: '1rem' }}><u>Target Value</u></Form.Label>
                      <Row>
                        <Col>
                          <Form.Label>Original Value (numeric)</Form.Label>
                          <Form.Control
                            type="number"
                            name="target.originalValue"
                            value={props.values.target.originalValue || ''}
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            isInvalid={!!props.errors.target?.originalValue}
                          />
                        </Col>
                        <Col>
                          <Form.Label>Date</Form.Label>
                          <DateInput
                              translations={translations}
                            name="target.originalValueDate"
                            value={props.values.target.originalValueDate || ''}
                            onChange={val => props.setFieldValue('target.originalValueDate', val)}
                            onBlur={props.handleBlur}
                          />
                        </Col>
                        <Col>
                          <Form.Label>Revised Value (numeric)</Form.Label>
                          <Form.Control
                            type="number"
                            name="target.revisedValue"
                            value={props.values.target.revisedValue || ''}
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            isInvalid={!!props.errors.target?.revisedValue}
                          />
                        </Col>
                        <Col>
                          <Form.Label>Date</Form.Label>
                          <DateInput
                              translations={translations}
                            name="target.revisedValueDate"
                            value={props.values.target.revisedValueDate || ''}
                            onChange={val => props.setFieldValue('target.revisedValueDate', val)}
                            onBlur={props.handleBlur}
                          />
                        </Col>
                      </Row>
                    </div>
                  </Form.Group>
                </Row>
                {/* Other Considerations - Separate Group */}
                <Row className={styles.view_row}><Col><h5>Other Considerations</h5></Col></Row>
                <Row className={styles.view_row}>
                  <Form.Group className={styles.view_item} controlId="otherConsiderations">
                    <Row>
                      <Col>
                        <Form.Label>Creation Date</Form.Label>
                        <DateInput
                            translations={translations}
                          name="creationDate"
                          value={props.values.creationDate || ''}
                          onChange={val => props.setFieldValue('creationDate', val)}
                          onBlur={props.handleBlur}
                        />
                      </Col>
                      <Col>
                        <Form.Label>Ascending <span style={{ fontStyle: 'italic', color: 'gray' }}>(review necessity)</span></Form.Label>
                        <Form.Check
                          type="checkbox"
                          name="ascending"
                          checked={!!props.values.ascending}
                          onChange={props.handleChange}
                          onBlur={props.handleBlur}
                        />
                      </Col>
                    </Row>
                  </Form.Group>
                </Row>
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

