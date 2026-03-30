import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import { Card, Form, Modal, Button, Col, Row } from 'react-bootstrap';
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
import axios from 'axios';
import Accordion from 'react-bootstrap/Accordion';
import initialTranslations from '../../config/initialTranslations.json';


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
  indicatorType?: number;
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
  programId?: number;
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
  outputId?: number;
  outcomeId?: number;
  indicatorsCategory?: number;
  // Add editable disaggregation values
  disaggregationValues?: Array<{
    parentCategoryId: number;
    childCategoryId: number | null;
    base: {
      originalValue: string | number;
      originalValueDate: string;
      revisedValue: string | number;
      revisedValueDate: string;
    };
    target: {
      originalValue: string | number;
      originalValueDate: string;
      revisedValue: string | number;
      revisedValueDate: string;
    };
  }>;
}

const AddNewIndicatorModal: React.FC<AddNewIndicatorModalProps> = (props) => {
  const { show, setShow, translations } = props;
  const t = (key: string): string => translations[key] ?? initialTranslations[key as keyof typeof initialTranslations] ?? key;

  const ascendingOptions = [
    { value: true, label: t("amp.indicatormanager:true") },
    { value: false, label: t("amp.indicatormanager:false") }
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
  const outcomesReducer = useSelector((state: any) => state.fetchOutcomesReducer);
  const allOutcomes = outcomesReducer.outcomes || [];

  const [programFieldVisible, setProgramFieldVisible] = useState(false);
  const [selectedProgramSchemeId, setSelectedProgramSchemeId] = useState<string | null>(null);

  const [sectors, setSectors] = useState<{ value: string, name: string }[]>([]);
  const [programSchemes, setProgramSchemes] = useState<{ value: string, name: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, label: string }[]>([]);
  const [categories, setCategories] = useState<{ value: number, label: string }[]>([]);

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
    getOutcomes();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sectorsReducer.sectors, programsReducer.programs, programsReducer.programSchemes, outcomesReducer.outcomes])


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
        title: `${t("amp.indicatormanager:creating-indicator")}...`,
        timer: 1000
      });
      return;
    }

    if (!createIndicatorState.loading && !createIndicatorState?.error && createIndicatorState?.createdIndicator?.id) {
      MySwal.fire({
        title: t("amp.indicatormanager:success"),
        text: t("amp.indicatormanager:save-success"),
        icon: 'success',
        confirmButtonText: t("amp.indicatormanager:ok"),
      }).then(() => {
        dispatch(getIndicators());
        handleClose();
      });
      return;
    }

    if (createIndicatorState.error && !createIndicatorState.loading && !createIndicatorState.createdIndicator) {
      MySwal.fire({
        title: t("amp.indicatormanager:error"),
        text: createIndicatorState.loading ? t("Error creating indicator") : createIndicatorState.error,
        icon: 'error',
        confirmButtonText: t("amp.indicatormanager:ok"),
      });
    }

  }, [createIndicatorState])

  const initialValues: IndicatorFormValues = {
    name: '',
    description: '',
    code: '',
    relevanceForClimateChange: '',
    indicatorType: undefined,
    sectors: [],
    programId: undefined,
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
    disaggregationValues: [],
  };

  // --- Dynamic category options from fetchAmpCategoryReducer ---
  const getCategoryOptions = (keyName: string) => {
    return categoriesReducer.categories
      .filter((cat: any) => cat.ampCategoryClass && cat.ampCategoryClass.keyName === keyName)
      .map((cat: any) => ({ value: cat.id, label: cat.value }));
  };

  const indicatorTypeOptions = getCategoryOptions('indicator_type');
  const disaggregationOptions = getCategoryOptions('indicator_disaggregation');
  const unitOfMeasureOptions = getCategoryOptions('indicator_unit_of_measure');
  const frequencyOptions = getCategoryOptions('indicator_frequency');

  const [disaggregationChildren, setDisaggregationChildren] = useState<{[key: number]: any[]}>({});

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
        <Modal.Title>{t('amp.dashboard:add-new')}</Modal.Title>
      </Modal.Header>
      <Formik
        innerRef={formikRef}
        initialValues={initialValues}
        validationSchema={translatedIndicatorValidationSchema(translations)}
        onSubmit={(values) => {
          const { name, description, code, sectors, programId, ascending, creationDate, base, target, indicatorsCategory } = values;
          if (selectedProgramSchemeId && !programId) {
            MySwal.fire({
              title: t('amp.indicatormanager:error'),
              text: t('amp.indicatormanager:errors-program-is-required'),
              icon: 'error',
              confirmButtonText: t('amp.indicatormanager:ok'),
            })

            return;
          }


          // Format disaggregationValues date fields
          const formattedDisaggregationValues = (values.disaggregationValues || []).map(dv => ({
            ...dv,
            base: {
              ...dv.base,
              originalValueDate: dv.base?.originalValueDate ? formatDate(dv.base.originalValueDate) : null,
              revisedValueDate: dv.base?.revisedValueDate ? formatDate(dv.base.revisedValueDate) : null,
            },
            target: {
              ...dv.target,
              originalValueDate: dv.target?.originalValueDate ? formatDate(dv.target.originalValueDate) : null,
              revisedValueDate: dv.target?.revisedValueDate ? formatDate(dv.target.revisedValueDate) : null,
            }
          }));

          const indicatorData = {
            name,
            description,
            code,
            sectors,
            programId: programId ? programId : null,
            ascending,
            creationDate: creationDate ? formatDate(new Date(creationDate)) : null,
            base: checkObjectIsNull(base) ? null : {
              originalValue: base.originalValue ? lodash.toNumber(base.originalValue): null,
              originalValueDate: base.originalValueDate ? formatDate(base.originalValueDate) : null,
              revisedValue: base.revisedValue ? lodash.toNumber(base.revisedValue) : null,
              revisedValueDate: base.revisedValueDate ? formatDate(base.revisedValueDate) : null,
            },
            target: checkObjectIsNull(target) ? null : {
              originalValue: target.originalValue ? lodash.toNumber(target.originalValue) : null,
              originalValueDate: target.originalValueDate ? formatDate(target.originalValueDate) : null,
              revisedValue: target.revisedValue ? lodash.toNumber(target.revisedValue) : null,
              revisedValueDate: target.revisedValueDate ? formatDate(target.revisedValueDate) : null,
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
            disaggregationValues: formattedDisaggregationValues,
          };

          dispatch(createIndicator(indicatorData));
        }}
      >
        {(props) => {
          // Move disaggregation children-fetching logic here
          useEffect(() => {
            const selected = props.values.disaggregation;
            if (selected && (selected.length === 1 || selected.length === 2)) {
              Promise.all(selected.map(id => axios.get(`/rest/indicator_disaggregation/options/${id}`)))
                .then((responses) => {
                  const childrenMap = {};
                  selected.forEach((id, idx) => {
                    childrenMap[id] = responses[idx].data;
                  });
                  setDisaggregationChildren(childrenMap);
                });
            } else {
              setDisaggregationChildren({});
            }
          }, [props.values.disaggregation]);

          // Ensure all required disaggregation entries exist in Formik state before rendering Accordion
          useEffect(() => {
            if (!Array.isArray(props.values.disaggregationValues)) return;
            let updated = [...props.values.disaggregationValues];
            if (props.values.disaggregation.length === 1) {
              const parentId = props.values.disaggregation[0];
              (disaggregationChildren[parentId] || []).forEach((child) => {
                const entryIdx = updated.findIndex((v: any) => v.parentCategoryId === child.id && v.childCategoryId === null);
                if (entryIdx === -1) {
                  updated.push({
                    parentCategoryId: child.id,
                    childCategoryId: null,
                    base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                    target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                  });
                }
              });
            } else if (props.values.disaggregation.length === 2) {
              const parentArr = disaggregationChildren[props.values.disaggregation[0]] || [];
              const childArr = disaggregationChildren[props.values.disaggregation[1]] || [];
              parentArr.forEach((parentChild) => {
                childArr.forEach((child) => {
                  const entryIdx = updated.findIndex((v: any) => v.parentCategoryId === parentChild.id && v.childCategoryId === child.id);
                  if (entryIdx === -1) {
                    updated.push({
                      parentCategoryId: parentChild.id,
                      childCategoryId: child.id,
                      base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                      target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                    });
                  }
                });
              });
              // Remove any entries with childCategoryId: null
              updated = updated.filter((v: any) => v.childCategoryId !== null);
            }
            if (updated.length !== props.values.disaggregationValues.length) {
              props.setFieldValue('disaggregationValues', updated);
            }
          }, [props.values.disaggregation, disaggregationChildren]);

          // Helper to update a field in disaggregationValues
          const updateDisaggregationField = (entryIdx: number, fieldPath: string[], value: any) => {
            let updated = Array.isArray(props.values.disaggregationValues) ? [...props.values.disaggregationValues] : [];
            let entry = updated[entryIdx];
            // If entry does not exist, create it and push to array
            if (!entry) {
              // Find parent/child ids from context
              let parentCategoryId, childCategoryId;
              if (fieldPath[0] === 'base' || fieldPath[0] === 'target') {
                // Try to get from Accordion context
                if (props.values.disaggregation.length === 1) {
                  parentCategoryId = props.values.disaggregation[0];
                  childCategoryId = disaggregationChildren[parentCategoryId]?.[entryIdx]?.id;
                } else if (props.values.disaggregation.length === 2) {
                  parentCategoryId = disaggregationChildren[props.values.disaggregation[0]]?.[Math.floor(entryIdx / disaggregationChildren[props.values.disaggregation[1]].length)]?.id;
                  childCategoryId = disaggregationChildren[props.values.disaggregation[1]]?.[entryIdx % disaggregationChildren[props.values.disaggregation[1]].length]?.id;
                }
              }
              entry = {
                parentCategoryId: parentCategoryId,
                childCategoryId: childCategoryId,
                base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
              };
              updated.push(entry);
              entryIdx = updated.length - 1;
            }
            let obj = entry;
            for (let i = 0; i < fieldPath.length - 1; i++) {
              obj = obj[fieldPath[i]];
            }
            obj[fieldPath[fieldPath.length - 1]] = value;
            props.setFieldValue('disaggregationValues', updated);
          };

          return (
            <Form noValidate onSubmit={props.handleSubmit}>
              <Modal.Body>
                <div className={styles.viewmodal_wrapper}>
                  {/* Core Indicator Information */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{t("amp.indicatormanager:core-info")}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formBasicName">
                        <Form.Label>{t("amp.indicatormanager:indicator-name")}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="name"
                            className={`${styles.input_field} ${(props.errors.name && props.touched.name) && styles.text_is_invalid}`}
                            isInvalid={!!props.errors.name}
                            required
                            aria-required type="text"
                            placeholder={t("amp.indicatormanager:enter-indicator-name")}
                        />
                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.name && <span>{props.errors.name}</span>}
                        </Form.Control.Feedback>
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formIndicatorCode">
                        <Form.Label>{t("amp.indicatormanager:indicator-code")}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="code"
                            required
                            type="text"
                            className={`${styles.input_field} ${(props.errors.code && props.touched.code) && styles.text_is_invalid}`}
                            placeholder={t("amp.indicatormanager:enter-indicator-code")}
                        />
                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.code && <span>{props.errors.code}</span>}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group as={Col} className={styles.view_one_item} controlId="formBasicDescription">
                        <Form.Label>{t("amp.indicatormanager:indicator-description")}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="description"
                            as="textarea"
                            rows={2}
                            className={`${styles.input_field} ${(props.errors.description && props.touched.description) && styles.text_is_invalid}`}
                            placeholder={t("amp.indicatormanager:enter-indicator-description")}
                        />
                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.description && <span>{props.errors.description}</span>}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group as={Col} className={styles.view_one_item} controlId="formRelevanceForClimateChange">
                        <Form.Label>{t("amp.indicatormanager:relevance-for-climate-change")}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="relevanceForClimateChange"
                            as="textarea"
                            rows={2}
                            className={styles.input_field}
                            placeholder={t("amp.indicatormanager:relevance-for-climate-change")}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formIndicatorType">
                        <Form.Label>{t("amp.indicatormanager:type")}</Form.Label>
                        <Select
                            name="indicatorType"
                            options={indicatorTypeOptions}
                            onChange={(selectedValue) => {
                              props.setFieldValue('indicatorType', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.indicatorType && props.touched.indicatorType) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            placeholder={t("amp.indicatormanager:select-indicator-type")}
                            value={indicatorTypeOptions.find(opt => opt.value === props.values.indicatorType) || null}
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Categorization and Linkage */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{t("amp.indicatormanager:categorization-linkage-info")}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formOutcome">
                        <Form.Label>{t("amp.indicatormanager:outcome")}</Form.Label>
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
                            placeholder={t("amp.indicatormanager:select-outcome")}
                            value={allOutcomes.find(outcome => outcome.id === selectedOutcomeId) ? { value: selectedOutcomeId, label: allOutcomes.find(outcome => outcome.id === selectedOutcomeId)?.name } : null}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formOutput">
                        <Form.Label>{t("amp.indicatormanager:output")}</Form.Label>
                        <Select
                            name="outputId"
                            options={filteredOutputs.map(output => ({ value: output.id, label: output.name }))}
                            onChange={(selectedValue) => {
                              props.setFieldValue('outputId', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.outputId && props.touched.outputId) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            placeholder={t("amp.indicatormanager:select-output")}
                            value={filteredOutputs.find(output => output.id === props.values.outputId) ? { value: props.values.outputId, label: filteredOutputs.find(output => output.id === props.values.outputId)?.name } : null}
                            isDisabled={!selectedOutcomeId}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="programScheme">
                        <Form.Label>{t("amp.indicatormanager:link-logframe")}</Form.Label>
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
                            placeholder={t("amp.indicatormanager:select-program-scheme")}
                        />
                      </Form.Group>
                      {programFieldVisible && (
                          <Form.Group className={styles.view_item} controlId="programs">
                            <Form.Label>{t("amp.indicatormanager:programs")}</Form.Label>
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
                                placeholder={t("amp.indicatormanager:select-program")}
                            />
                          </Form.Group>
                      )}
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_one_item} controlId="formIndicatorSectors">
                        <Form.Label>{t("amp.indicatormanager:sectors")}</Form.Label>
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
                            placeholder={t("amp.indicatormanager:select-sectors")}
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Data Definition and Sourcing */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{t("amp.indicatormanager:data-definition-sourcing-info")}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formData">
                        <Form.Label>{t("amp.indicatormanager:data")}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="data"
                            type="text"
                            className={styles.input_field}
                            placeholder={t("amp.indicatormanager:enter-data")}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formDataSource">
                        <Form.Label>{t("amp.indicatormanager:data-source")}</Form.Label>
                        <Form.Control
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="dataSource"
                            type="text"
                            className={styles.input_field}
                            placeholder={t("amp.indicatormanager:enter-data-source")}
                        />
                      </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>

                      <Form.Group className={styles.view_item} controlId="formUnitOfMeasure">
                        <Form.Label>{t("amp.indicatormanager:unit-of-measure")}</Form.Label>
                        <Select
                            name="unitOfMeasure"
                            options={unitOfMeasureOptions}
                            onChange={(selectedValue) => {
                              props.setFieldValue('unitOfMeasure', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.unitOfMeasure && props.touched.unitOfMeasure) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            placeholder={t("amp.indicatormanager:select-unit-of-measure")}
                            value={unitOfMeasureOptions.find(opt => opt.value === props.values.unitOfMeasure) || null}
                        />
                      </Form.Group>
                        <Form.Group className={styles.view_one_item} controlId="formCalculationMethod">
                            <Form.Label>{t("amp.indicatormanager:calculation-method")}</Form.Label>
                            <Form.Control
                                onChange={props.handleChange}
                                onBlur={props.handleBlur}
                                name="calculationMethod"
                                type="text"
                                className={styles.input_field}
                                placeholder={t("amp.indicatormanager:calculation-method-placeholder")}
                            />
                        </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>
                        <Form.Group className={styles.view_item} controlId="formDisaggregation">
                            <Form.Label>{t("amp.indicatormanager:disaggregation")}</Form.Label>
                            <Select
                              isMulti
                              name="disaggregation"
                              options={disaggregationOptions}
                              onChange={(selectedValues) => {
                                // Limit selection to max 2
                                const limitedValues = selectedValues.slice(0, 2);
                                props.setFieldValue('disaggregation', limitedValues.map((v: any) => v.value));
                              }}
                              onBlur={props.handleBlur}
                              className={`basic-multi-select ${(props.errors.disaggregation && props.touched.disaggregation) && styles.text_is_invalid}`}
                              classNamePrefix="select"
                              placeholder={t("amp.indicatormanager:select-disaggregation")}
                              value={disaggregationOptions.filter(opt => props.values.disaggregation?.includes(opt.value))}
                            />
                        </Form.Group>
                    </Row>
                    {/* Accordion for disaggregation values, always in a new row below the select */}
                    {props.values.disaggregation?.length === 1 && (
                      <Row className={styles.view_row}>
                        <Col>
                          <div style={{marginTop: '1rem'}}>
                            <h6>{t("amp.indicatormanager:disaggregation-values")}</h6>
                            <Accordion defaultActiveKey="0">
                              {props.values.disaggregation.map((parentId, parentIdx) => (
                                <Card key={parentId}>
                                  <Accordion.Toggle
                                    as={Card.Header}
                                    eventKey={String(parentIdx)}
                                    className={styles.accordionHeader}
                                    style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', background: '#f7f7f7', fontWeight: 'bold' }}
                                    aria-label={t("amp.indicatormanager:click-to-expand-collapse")}
                                  >
                                    <div className={styles.accordionHeaderTitle} style={{ flex: 1 }}>
                                      {disaggregationOptions.find(opt => opt.value === parentId)?.label || `Disaggregation ${parentId}`}
                                    </div>
                                    <Accordion.Collapse eventKey={String(parentIdx)}>
                                      <span style={{ marginLeft: 8 }}>▼</span>
                                    </Accordion.Collapse>
                                  </Accordion.Toggle>
                                  <Accordion.Collapse eventKey={String(parentIdx)}>
                                    <Card.Body>
                                      {disaggregationChildren[parentId] && disaggregationChildren[parentId].length > 0 ? (
                                        <div style={{maxHeight: '300px', overflowY: 'auto'}}>
                                          {disaggregationChildren[parentId].map((child) => {
                                            const disaggArr = Array.isArray(props.values.disaggregationValues) ? props.values.disaggregationValues : [];
                                            // For single disaggregation, childCategoryId is null
                                            const entryIdx = disaggArr.findIndex((v: any) => v.parentCategoryId === child.id && v.childCategoryId === null);
                                            const entry = entryIdx !== -1 ? disaggArr[entryIdx] : {
                                              parentCategoryId: child.id,
                                              childCategoryId: null,
                                              base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                                              target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                                            };
                                            return (
                                              <Card key={child.id} style={{marginBottom: '8px'}}>
                                                <Card.Body>
                                                  <Card.Title className={styles.accordionChildTitle}>{child.value}</Card.Title>
                                                  <div style={{display: 'flex', flexWrap: 'wrap', gap: '32px'}}>
                                                    <div style={{minWidth: '300px'}}>
                                                      <h6 color={"red"}>{t("amp.indicatormanager:base-values")}</h6>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.base.originalValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'originalValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-original-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.base.originalValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'originalValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-original-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.base.revisedValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'revisedValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-revised-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.base.revisedValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'revisedValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-revised-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                    </div>
                                                    <div style={{minWidth: '300px'}}>
                                                      <h6 color={"red"}>{t("amp.indicatormanager:target-values")}</h6>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.target.originalValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'originalValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-original-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.target.originalValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'originalValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-original-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.target.revisedValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'revisedValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-revised-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.target.revisedValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'revisedValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-revised-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                    </div>
                                                  </div>
                                                </Card.Body>
                                              </Card>
                                            );
                                          })}
                                        </div>
                                      ) : (
                                        <div style={{color: '#888', padding: '1rem', textAlign: 'center', border: '1px solid #eee', borderRadius: '4px'}}>
                                          {t("amp.indicatormanager:no-disaggregation-children")}
                                        </div>
                                      )}
                                    </Card.Body>
                                  </Accordion.Collapse>
                                </Card>
                              ))}
                            </Accordion>
                          </div>
                        </Col>
                      </Row>
                    )}
                    {props.values.disaggregation?.length === 2 && (
                      <Row className={styles.view_row}>
                        <Col>
                          <div style={{marginTop: '1rem'}}>
                            <h6>{t("amp.indicatormanager:disaggregation-values")}</h6>
                            <Accordion defaultActiveKey="0">
                              {disaggregationChildren[props.values.disaggregation[0]]?.map((parentChild: any, parentIdx: number) => (
                                <Card key={parentChild.id}>
                                  <Accordion.Toggle
                                    as={Card.Header}
                                    eventKey={String(parentIdx)}
                                    className={styles.accordionHeader}
                                    style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', background: '#f7f7f7', fontWeight: 'bold' }}
                                    aria-label={t("amp.indicatormanager:click-to-expand-collapse")}
                                  >
                                    <div className={styles.accordionHeaderTitle} style={{ flex: 1 }}>
                                      {parentChild.value}
                                    </div>
                                    <Accordion.Collapse eventKey={String(parentIdx)}>
                                      <span style={{ marginLeft: 8 }}>▼</span>
                                    </Accordion.Collapse>
                                  </Accordion.Toggle>
                                  <Accordion.Collapse eventKey={String(parentIdx)}>
                                    <Card.Body>
                                      {disaggregationChildren[props.values.disaggregation[1]]?.length > 0 ? (
                                        <div style={{maxHeight: '300px', overflowY: 'auto'}}>
                                          {disaggregationChildren[props.values.disaggregation[1]].map((child: any) => {
                                            const disaggArr = Array.isArray(props.values.disaggregationValues) ? props.values.disaggregationValues : [];
                                            const entryIdx = disaggArr.findIndex((v: any) => v.parentCategoryId === parentChild.id && v.childCategoryId === child.id);
                                            const entry = entryIdx !== -1 ? disaggArr[entryIdx] : {
                                              parentCategoryId: parentChild.id,
                                              childCategoryId: child.id,
                                              base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                                              target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                                            };
                                            return (
                                              <Card key={child.id} style={{marginBottom: '8px'}}>
                                                <Card.Body>
                                                  <Card.Title className={styles.accordionChildTitle}>{child.value}</Card.Title>
                                                  <div style={{display: 'flex', flexWrap: 'wrap', gap: '32px'}}>
                                                    <div style={{minWidth: '300px'}}>
                                                      <h6 color={"red"}>{t("amp.indicatormanager:base-values")}</h6>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.base.originalValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'originalValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-original-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.base.originalValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'originalValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-original-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.base.revisedValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'revisedValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-revised-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.base.revisedValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['base', 'revisedValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:base-revised-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                    </div>
                                                    <div style={{minWidth: '300px'}}>
                                                      <h6 color={"red"}>{t("amp.indicatormanager:target-values")}</h6>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.target.originalValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'originalValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-original-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:original-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.target.originalValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'originalValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-original-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value")}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.target.revisedValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'revisedValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-revised-value")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{t("amp.indicatormanager:revised-value-date")}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.target.revisedValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx === -1 ? disaggArr.length : entryIdx, ['target', 'revisedValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={t("amp.indicatormanager:target-revised-value-date")}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                    </div>
                                                  </div>
                                                </Card.Body>
                                              </Card>
                                            );
                                          })}
                                        </div>
                                      ) : (
                                        <div style={{color: '#888', padding: '1rem', textAlign: 'center', border: '1px solid #eee', borderRadius: '4px'}}>
                                          {t("amp.indicatormanager:no-disaggregation-children")}
                                        </div>
                                      )}
                                    </Card.Body>
                                  </Accordion.Collapse>
                                </Card>
                              ))}
                            </Accordion>
                          </div>
                        </Col>
                      </Row>
                    )}
                  </div>
                  {/* Responsibility and Frequency */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{t("amp.indicatormanager:responsibility-frequency-info")}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formResponsibleOrganizations">
                        <Form.Label>{t("amp.indicatormanager:responsible-organizations")}</Form.Label>
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
                            placeholder={t("amp.indicatormanager:select-responsible-organizations")}
                            value={responsibleOrgOptions.filter(opt => props.values.responsibleOrganizations?.includes(opt.value))}
                        />
                      </Form.Group>
                      <Form.Group className={styles.view_item} controlId="formFrequency">
                        <Form.Label>{t("amp.indicatormanager:frequency")}</Form.Label>
                        <Select
                            name="frequency"
                            options={frequencyOptions}
                            onChange={(selectedValue) => {
                              props.setFieldValue('frequency', selectedValue?.value);
                            }}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.frequency && props.touched.frequency) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            placeholder={t("amp.indicatormanager:select-frequency")}
                            value={frequencyOptions.find(opt => opt.value === props.values.frequency) || null}
                        />
                      </Form.Group>
                    </Row>
                  </div>
                  {/* Value Tracking */}
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{t("amp.indicatormanager:value-tracking")}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Form.Group as={Col}>
                      <Form.Label>
                        <h4>{t("amp.indicatormanager:base-values")}</h4>
                      </Form.Label>
                      {/* Original Value and Date in one row */}
                      <Row className={styles.view_row}>
                        <Form.Group className={styles.view_item}>
                          <Form.Label>{t('amp.indicatormanager:original-value')}</Form.Label>
                          <Form.Control
                              defaultValue={props.values.base?.originalValue}
                              onChange={props.handleChange}
                              onBlur={props.handleBlur}
                              name="base.originalValue"
                              type="number"
                              className={`${styles.input_field} ${(props.errors.base?.originalValue && props.touched.base?.originalValue) && styles.text_is_invalid}`}
                              placeholder={t("amp.indicatormanager:enter-original-value")} />

                          <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                            {props.errors.base?.originalValue}
                          </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className={styles.view_item}>
                          <Form.Label>{t("amp.indicatormanager:original-value-date")}</Form.Label>
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
                          <Form.Label>{t("amp.indicatormanager:revised-value")}</Form.Label>
                          <Form.Control
                              defaultValue={props.values.base.revisedValue}
                              onChange={props.handleChange}
                              onBlur={props.handleBlur}
                              name="base.revisedValue"
                              type="number"
                              className={`${styles.input_field} ${(props.errors.base?.revisedValue && props.touched.base?.revisedValue) && styles.text_is_invalid}`}
                              placeholder={t("amp.indicatormanager:enter-revised-value")} />

                          <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                            {props.errors.base?.revisedValue}
                          </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className={styles.view_item}>
                          <Form.Label>{t('amp.indicatormanager:revised-value-date')}</Form.Label>
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
                    <Form.Label><h4>{t("amp.indicatormanager:target-values")}</h4></Form.Label>
                    {/* Original Value and Date in one row */}
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item}>
                        <Form.Label>{t("amp.indicatormanager:target-value")}</Form.Label>
                        <Form.Control
                            defaultValue={props.values.target.originalValue}
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="target.originalValue"
                            type="number"
                            className={`${styles.input_field} ${(props.errors.target?.originalValue && props.touched.target?.originalValue) && styles.text_is_invalid}`}
                            placeholder={t("amp.indicatormanager:enter-target-value")} />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.target?.originalValue}
                        </Form.Control.Feedback>
                      </Form.Group>
                      <Form.Group className={styles.view_item}>
                        <Form.Label>{t("amp.indicatormanager:target-value-date")}</Form.Label>
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
                        <Form.Label>{t("amp.indicatormanager:revised-value")}</Form.Label>
                        <Form.Control
                            defaultValue={props.values.target.revisedValue}
                            onChange={props.handleChange}
                            onBlur={props.handleBlur}
                            name="target.revisedValue"
                            type="number"
                            className={`${styles.input_field} ${(props.errors.target?.revisedValue && props.touched.target?.revisedValue) && styles.text_is_invalid}`}
                            placeholder={t("amp.indicatormanager:enter-revised-value")} />

                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.target?.revisedValue}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group className={styles.view_item}>
                        <Form.Label>{t("amp.indicatormanager:revised-value-date")}</Form.Label>
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
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{t("amp.indicatormanager:other-considerations")}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="Ascending">
                        <Form.Label>{t("amp.indicatormanager:ascending")}</Form.Label>
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
                              label: t("amp.indicatormanager:true")
                            }}
                        />
                        <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                          {props.errors.ascending && <span>{props.errors.ascending}</span>}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group className={styles.view_item} controlId="formCreationDate">
                        <Form.Label>{t("amp.indicatormanager:table-header-creation-date")}</Form.Label>
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
                  {t("amp.indicatormanager:close")}
                </Button>
                <Button type="submit" variant="success" >
                  {t("amp.indicatormanager:save")}
                </Button>
              </Modal.Footer>
            </Form>
        )}}
      </Formik>
    </Modal>
  );
};

export default AddNewIndicatorModal;

