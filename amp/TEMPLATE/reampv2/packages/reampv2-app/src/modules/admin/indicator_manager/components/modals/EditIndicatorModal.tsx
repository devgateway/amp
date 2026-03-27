/* eslint-disable import/no-unresolved */
import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col, Row, Accordion, Card
} from 'react-bootstrap';
import Select from 'react-select';
import { Formik, FormikProps } from 'formik';
import styles from './css/IndicatorModal.module.css';
import { translatedIndicatorValidationSchema } from '../../utils/validator';
import { BaseAndTargetValueType, DefaultComponentProps, IndicatorObjectType, ProgramSchemeType, SectorObjectType, SettingsType } from '../../types';
import { useDispatch, useSelector } from 'react-redux';
import { updateIndicator } from '../../reducers/updateIndicatorReducer';
import { DateUtil } from '../../utils/dateFn';
import { formatObjArrayToNumberArray } from '../../utils/formatter';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';
import { checkObjectIsNull, extractChildrenFromProgramScheme, getProgamSchemeForChild } from '../../utils/helpers';
import useDidMountEffect from '../../utils/hooks';
import DateInput from '../DateInput';
import lodash from 'lodash';
import axios from 'axios';


const MySwal = withReactContent(Swal);


interface EditIndicatorModalProps extends DefaultComponentProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
  indicator: IndicatorObjectType;
  filterBySector: boolean;
  filterByProgram: boolean;
}

interface IndicatorFormValues {
  name: string;
  description?: string;
  code: string;
  relevanceForClimateChange?: string;
  indicatorType?: number;
  sectors: any[];
  logframeLinks: string[];
  data?: string;
  dataSource?: string;
  disaggregation: number[];
  unitOfMeasure?: number;
  calculationMethod?: string;
  responsibleOrganizations: number[];
  frequency?: number;
  ascending: boolean;
  creationDate?: string;
  programId: number | any;
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
  outcomeId?: number;
  outputId?: number;
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

const EditIndicatorModal: React.FC<EditIndicatorModalProps> = (props) => {
  const { show, setShow, indicator, translations, filterBySector, filterByProgram } = props;

  const ascendingOptions = [
    { value: true, label: translations["amp.indicatormanager:true"] },
    { value: false, label: translations["amp.indicatormanager:false"] }
  ];

  const dispatch = useDispatch();
  const nodeRef = useRef(null);

  const creationDateRef = useRef<HTMLInputElement>(null);
  const baseOriginalValueDateRef = useRef<HTMLInputElement>(null);
  const baseRevisedValueDateRef = useRef<HTMLInputElement>(null);
  const targetOriginalValueDateRef = useRef<HTMLInputElement>(null);
  const targetRevisedValueDateRef = useRef<HTMLInputElement>(null);

  const globalSettings: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);


  const formatDate = (date: string) => {
    return DateUtil.formatJavascriptDate(date, globalSettings['default-date-format']?.toUpperCase());
  }

  const handleClose = () => setShow(false);

  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const updateIndicatorReducer = useSelector((state: any) => state.updateIndicatorReducer);

  // Add selectors for responsible orgs and outcomes
  const responsibleOrgOptions = useSelector((state: any) => state.fetchResponsibleOrgsReducer.options || []);
  const outcomesState = useSelector((state: any) => state.fetchOutcomesReducer);
  const allOutcomes = outcomesState.outcomes || [];

  const [programFieldVisible, setProgramFieldVisible] = useState(false);
  const [selectedProgramSchemeId, setSelectedProgramSchemeId] = useState<string | null>(null);

  const [sectors, setSectors] = useState<{ value: string, label: string }[]>([]);
  const [categories, setCategories] = useState<{ value: string, label: string }[]>([]);
  const [programSchemes, setProgramSchemes] = useState<{ value: string, label: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, label: string }[]>([]);

  const [defaultCategory, setDefaultCategory] = useState<{ value: string, label: string } | null>(null);
  const [defaultSectors, setDefaultSectors] = useState<{ value: string, label: string }[]>();
  const [defaultProgram, setDefaultProgram] = useState<{ value: string, label: string } | null>(null);
  const [defaultProgramScheme, setDefaultProgramScheme] = useState<{ value: string, label: string } | null>(null);

  const [baseOriginalValueDateDisabled, setBaseOriginalValueDateDisabled] = useState(false);
  const [targetOriginalValueDateDisabled, setTargetOriginalValueDateDisabled] = useState(false);

  // --- Outcome/Output dropdown logic ---
  const [allOutcomesData, setAllOutcomes] = useState<{ id: number, name: string, outputs: { id: number, name: string }[] }[]>([]);
  const [selectedOutcomeId, setSelectedOutcomeId] = useState<number | null>(indicator?.outcomeId ?? null);
  const [filteredOutputs, setFilteredOutputs] = useState<{ id: number, name: string }[]>([]);

  // --- Add disaggregationChildren state ---
  const [disaggregationChildren, setDisaggregationChildren] = useState<{[key: number]: any[]}>({});

  // Utility to convert any date string to ISO format
  const convertDateToISO = (date?: string) => {
    if (!date) return '';
    // Try to parse as ISO first, fallback to parsing with default format
    if (/^\d{4}-\d{2}-\d{2}/.test(date)) return date;
    return DateUtil.toISO8601(date, globalSettings['default-date-format']);
  };

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
      value: sector.id.toString(),
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
          formikRef?.current?.setFieldValue("base.originalValueDate", convertDateToISO(programScheme.startDate));
        }

        if (programScheme.endDate) {
          formikRef?.current?.setFieldValue("target.originalValueDate", convertDateToISO(programScheme.endDate));
        }
      }

    }
  }


  const handleProgramSchemeChange = (selectedOption: any, props: FormikProps<IndicatorFormValues>) => {
    setSelectedProgramSchemeId(selectedOption);
    props.setFieldValue("programId", null);
    setProgramFieldVisible(false);
  };


  useEffect(() => {
    getProgramsForProgramScheme();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedProgramSchemeId]);

  const getDefaultCategory = () => {
    if (indicator?.indicatorsCategory) {
      const foundCategory = categoriesReducer.categories.find((category: any) => category.id === indicator?.indicatorsCategory);
      if (foundCategory) {
        setDefaultCategory({
            value: foundCategory.id,
            label: foundCategory.value
        });

        formikRef?.current?.setFieldValue("indicatorsCategory", foundCategory.id);
      }
    }
  }

  const getDefaultSectors = () => {
    if (indicator?.sectors === null || indicator?.sectors.length === 0) {
      setDefaultSectors([]);
    }

    const indicatorSectorData = indicator?.sectors.map((sectorId: number) => {
      const foundSector: SectorObjectType = !sectorsReducer.loading && sectorsReducer.sectors.find((sector: SectorObjectType) => sector.id === sectorId);

      if (foundSector) {
        return {
          value: sectorId.toString(),
          label: foundSector.name
        }
      }

      return {
        value: sectorId.toString(),
        label: translations["amp.indicatormanager:sector-not-found"]
      }
    });

    setDefaultSectors(indicatorSectorData);
  };


  const getDefaultPropgramScheme = () => {
    if (indicator?.programId) {
      const foundProgramScheme = getProgamSchemeForChild(programsReducer.programSchemes, indicator?.programId);
      if (foundProgramScheme) {
        setDefaultProgramScheme({
          value: foundProgramScheme.ampProgramSettingsId.toString(),
          label: foundProgramScheme.name
        })

        if (foundProgramScheme.startDate) {
          formikRef?.current?.setFieldValue("base.originalValueDate", DateUtil.formatJavascriptDate(foundProgramScheme.startDate || ''));
          setBaseOriginalValueDateDisabled(true);
        }

        if (foundProgramScheme.endDate) {
          formikRef?.current?.setFieldValue("target.originalValueDate", DateUtil.formatJavascriptDate(foundProgramScheme.endDate || ''));
          setTargetOriginalValueDateDisabled(true);
        }
      }
    }
  }

  const getDefaultProgram = () => {
    if (indicator?.programId !== null) {
      const getProgram = programsReducer.programs.find((program: any) => program.id === indicator?.programId);
      if (getProgram) {
        formikRef?.current?.setFieldValue("programId", getProgram.id.toString());
        setProgramFieldVisible(true);
        setDefaultProgram({
          value: getProgram.id.toString(),
          label: getProgram.name
        });
      }
    }
  }


  useLayoutEffect(() => {
    getSectors();
    getCategories();
    getProgramSchemes();
    getPrograms();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);




  useEffect(() => {
    getDefaultSectors();
    getDefaultCategory();
    getDefaultProgram();
    getDefaultPropgramScheme();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [indicator]);

  useDidMountEffect(() => {
    if (updateIndicatorReducer.loading) {
      MySwal.fire({
        icon: 'info',
        title: `${translations["amp.indicatormanager:updating-indicator"]}...`,
        timer: 18000
      });
      return;
    }

    if (!updateIndicatorReducer.loading && !updateIndicatorReducer.error && updateIndicatorReducer?.indicator?.id) {
      MySwal.fire({
        icon: 'success',
        title: translations["amp.indicatormanager:indicator-updated-successfully"],
        timer: 3000
      }).then(() => {
        handleClose();
        dispatch(getIndicators());
      });
      return;
    }

    MySwal.fire({
      icon: 'error',
      title: translations["amp.indicatormanager:error"],
      text: updateIndicatorReducer.loading ? translations["amp.indicatormanager:save-failed"] : updateIndicatorReducer.error,
    });
  }, [updateIndicatorReducer]);
    const convertDisaggregationDatesToISO = (disaggregationValues: any[] = []) => {
        return disaggregationValues.map(dv => ({
            ...dv,
            base: {
                ...dv.base,
                originalValueDate: dv.base?.originalValueDate ? convertDateToISO(dv.base.originalValueDate) : '',
                revisedValueDate: dv.base?.revisedValueDate ? convertDateToISO(dv.base.revisedValueDate) : '',
            },
            target: {
                ...dv.target,
                originalValueDate: dv.target?.originalValueDate ? convertDateToISO(dv.target.originalValueDate) : '',
                revisedValueDate: dv.target?.revisedValueDate ? convertDateToISO(dv.target.revisedValueDate) : '',
            }
        }));
    };

  const initialValues: IndicatorFormValues = {
    name: indicator?.name || '',
    description: indicator?.description || '',
    code: indicator?.code || '',
    relevanceForClimateChange: indicator?.relevanceForClimateChange || '',
    indicatorType: indicator?.indicatorType || undefined,
    sectors: indicator?.sectors || [],
    logframeLinks: indicator?.logframeLinks || [],
    data: indicator?.data || '',
    dataSource: indicator?.dataSource || '',
    disaggregation: indicator?.disaggregation || [],
    unitOfMeasure: indicator?.unitOfMeasure || undefined,
    calculationMethod: indicator?.calculationMethod || '',
    responsibleOrganizations: indicator?.responsibleOrganizations || [],
    frequency: indicator?.frequency || undefined,
    programId:indicator?.programId || undefined,
    ascending: indicator?.ascending || false,
    creationDate: indicator?.creationDate ? convertDateToISO(indicator?.creationDate) : '',
    base: {
      originalValue: indicator?.base?.originalValue,
      originalValueDate: indicator?.base?.originalValueDate ? convertDateToISO(indicator?.base?.originalValueDate) : '',
      revisedValue: indicator?.base?.revisedValue,
      revisedValueDate: indicator?.base?.revisedValueDate ? convertDateToISO(indicator?.base?.revisedValueDate) : '',
    },
    target: {
      originalValue: indicator?.target?.originalValue,
      originalValueDate: indicator?.target?.originalValueDate ? convertDateToISO(indicator?.target?.originalValueDate) : '',
      revisedValue: indicator?.target?.revisedValue,
      revisedValueDate: indicator?.target?.revisedValueDate ? convertDateToISO(indicator?.target?.revisedValueDate) : '',
    },
    outcomeId: indicator?.outcomeId || undefined,
    outputId: indicator?.outputId || undefined,
    // Add editable disaggregation values
    disaggregationValues: convertDisaggregationDatesToISO(indicator?.disaggregationValues || []),
  };


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

  useEffect(() => {
    fetch('/rest/amp-outcome-output/outcomes')
      .then(res => res.json())
      .then (data => setAllOutcomes(data));
  }, []);

  useEffect(() => {
    if (selectedOutcomeId) {
      const found = allOutcomes.find(o => o.id === selectedOutcomeId);
      setFilteredOutputs(found ? found.outputs : []);
      // Set initial outputId if editing and outputId matches a filtered output
      if (formikRef.current && indicator?.outputId) {
        const match = found?.outputs.find(out => out.id === indicator.outputId);
        if (match) {
          formikRef.current.setFieldValue('outputId', indicator.outputId);
        } else {
          formikRef.current.setFieldValue('outputId', undefined);
        }
      }
    } else {
      setFilteredOutputs([]);
      if (formikRef.current) {
        formikRef.current.setFieldValue('outputId', undefined);
      }
    }
  }, [selectedOutcomeId, allOutcomes]);

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
      size="lg"
    >
      <Modal.Header closeButton>
        <Modal.Title>{translations["amp.indicatormanager:edit-indicator"]}</Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={initialValues}
        validationSchema={translatedIndicatorValidationSchema(translations)}
        innerRef={formikRef}
        onSubmit={(values) => {
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
          const hasDisagg = values.disaggregation && values.disaggregation.length > 0;
          const updatedIndicatorData = {
            id: indicator.id,
            name: values.name,
            description: values.description,
            code: values.code,
            relevanceForClimateChange: values.relevanceForClimateChange,
            indicatorType: values.indicatorType,
            sectors: formatObjArrayToNumberArray(values.sectors),
            logframeLinks: values.logframeLinks,
            data: values.data,
            dataSource: values.dataSource,
            disaggregation: values.disaggregation,
            unitOfMeasure: values.unitOfMeasure,
            calculationMethod: values.calculationMethod,
            responsibleOrganizations: values.responsibleOrganizations,
            frequency: values.frequency,
            programId: values.programId ? values.programId: null,
            ascending: values.ascending,
            creationDate: values.creationDate && formatDate(values.creationDate),
            base: hasDisagg ? null : (checkObjectIsNull(values.base) ? null : {
              originalValue: values.base.originalValue ? lodash.toNumber(values.base.originalValue) : null,
              originalValueDate: values.base.originalValueDate ? formatDate(values.base.originalValueDate) : null,
              revisedValue: values.base.revisedValue ? lodash.toNumber(values.base.revisedValue) : null,
              revisedValueDate: values.base.revisedValueDate ? formatDate(values.base.revisedValueDate) : null,
            }),
            target: hasDisagg ? null : (checkObjectIsNull(values.target) ? null : {
              originalValue: values.target.originalValue ? lodash.toNumber(values.target.originalValue) : null,
              originalValueDate: values.target.originalValueDate ? formatDate(values.target.originalValueDate) : null,
              revisedValue: values.target.revisedValue ? lodash.toNumber(values.target.revisedValue) : null,
              revisedValueDate: values.target.revisedValueDate ? formatDate(values.target.revisedValueDate) : null,
            }),
            outcomeId: values.outcomeId,
            outputId: values.outputId,
            disaggregationValues: hasDisagg ? formattedDisaggregationValues : [],
            indicatorsCategory: indicator.indicatorsCategory || undefined,
          };

          if (selectedProgramSchemeId && !values.programId) {
            MySwal.fire({
              title: translations['amp.indicatormanager:error'],
              text: translations['amp.indicatormanager:errors-program-is-required'],
              icon: 'error',
              confirmButtonText: translations['amp.indicatormanager:ok'],
            })
            return;
          }

          dispatch(updateIndicator(updatedIndicatorData as IndicatorObjectType));
        }}
      >
        {(props) => {
          // Fetch disaggregation children when disaggregation changes
          useEffect(() => {
            const selected = props.values.disaggregation;
            if (selected && (selected.length === 1 || selected.length === 2)) {
              Promise.all(selected.map(id => axios.get(`/rest/indicator_disaggregation/options/${id}`)))
                .then((responses) => {
                  const childrenMap: {[key: number]: any[]} = {};
                  selected.forEach((id, idx) => {
                    childrenMap[id] = responses[idx].data;
                  });
                  setDisaggregationChildren(childrenMap);

                  // --- Rebuild disaggregationValues to match current selection ---
                  let newDisaggValues: any[] = [];
                  if (selected.length === 1) {
                    // For single disaggregation, childCategoryId is null
                    const children = childrenMap[selected[0]] || [];
                    newDisaggValues = children.map((child: any) => {
                      // Try to find existing entry for this child
                      const existing = (props.values.disaggregationValues || []).find((v: any) => v.parentCategoryId === child.id && v.childCategoryId === null);
                      return existing || {
                        parentCategoryId: child.id,
                        childCategoryId: null,
                        base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                        target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                      };
                    });
                  } else if (selected.length === 2) {
                    // For double disaggregation, cross product of children
                    const parents = childrenMap[selected[0]] || [];
                    const children = childrenMap[selected[1]] || [];
                    parents.forEach((parent: any) => {
                      children.forEach((child: any) => {
                        const existing = (props.values.disaggregationValues || []).find((v: any) => v.parentCategoryId === parent.id && v.childCategoryId === child.id);
                        newDisaggValues.push(existing || {
                          parentCategoryId: parent.id,
                          childCategoryId: child.id,
                          base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                          target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                        });
                      });
                    });
                  }
                  props.setFieldValue('disaggregationValues', newDisaggValues);
                });
            } else {
              setDisaggregationChildren({});
              props.setFieldValue('disaggregationValues', []);
            }
          }, [props.values.disaggregation]);

          // Helper to update a field in disaggregationValues
          const updateDisaggregationField = (entryIdx: number, fieldPath: string[], value: any) => {
            let updated = Array.isArray(props.values.disaggregationValues) ? [...props.values.disaggregationValues] : [];
            if (!updated[entryIdx]) return; // Only update if entry exists
            let obj = updated[entryIdx];
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
                  <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{translations["amp.indicatormanager:core-info"]}</h5></Col></Row>
                  <div className={styles.sectionContainer}>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_item} controlId="formBasicName">
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
                      <Form.Group as={Col} className={styles.view_one_item} controlId="formBasicDescription">
                        <Form.Label>{translations["amp.indicatormanager:indicator-description"]}</Form.Label>
                        <Form.Control
                          defaultValue={props.values.description}
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
                          defaultValue={props.values.relevanceForClimateChange}
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
                        <Form.Label>{translations["amp.indicatormanager:type"]}</Form.Label>
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
                        <Form.Label>{translations["amp.indicatormanager:link-logframe"]}</Form.Label>
                        <Select
                          name="programScheme"
                          options={programSchemes}
                          onChange={(selectedValue) => {
                            if (selectedValue) {
                              setDefaultProgramScheme(selectedValue);
                              handleProgramSchemeChange(selectedValue.value, props);
                            }
                          }}
                          isClearable
                          getOptionValue={(option) => option.value}
                          onBlur={props.handleBlur}
                          className={`basic-multi-select ${styles.input_field}`}
                          classNamePrefix="select"
                          value={defaultProgramScheme}
                        />
                      </Form.Group>
                      {programFieldVisible && (
                        <Form.Group className={styles.view_item} controlId="programs">
                          <Form.Label>{translations["amp.indicatormanager:programs"]}</Form.Label>
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
                            defaultValue={defaultProgram}
                          />
                        </Form.Group>
                        )}
                    </Row>
                    <Row className={styles.view_row}>
                      <Form.Group className={styles.view_one_item} controlId="formIndicatorSectors">
                        <Form.Label>{translations["amp.indicatormanager:sectors"]}</Form.Label>
                        <Select
                          isMulti
                          name="sectors"
                          options={sectors}
                          onChange={(values) => {
                            const selectedValues = values.map((value: any) => parseInt(value.value))
                            setDefaultSectors(values as any);
                            props.setFieldValue('sectors', selectedValues);
                          }}
                          onBlur={props.handleBlur}
                          className={`basic-multi-select ${(props.errors.sectors && props.touched.sectors) && styles.text_is_invalid}`}
                          classNamePrefix="select"
                          value={defaultSectors}
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
                          defaultValue={props.values.data}
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
                          defaultValue={props.values.dataSource}
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

                      <Form.Group className={styles.view_item} controlId="formUnitOfMeasure">
                        <Form.Label>{translations["amp.indicatormanager:unit-of-measure"]}</Form.Label>
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

                        <Form.Group className={styles.view_one_item} controlId="formCalculationMethod">
                            <Form.Label>{translations["amp.indicatormanager:calculation-method"]}</Form.Label>
                            <Form.Control
                                defaultValue={props.values.calculationMethod}
                                onChange={props.handleChange}
                                onBlur={props.handleBlur}
                                name="calculationMethod"
                                type="text"
                                className={styles.input_field}
                                placeholder={translations["amp.indicatormanager:calculation-method-placeholder"]}
                        />
                        </Form.Group>
                    </Row>
                    <Row className={styles.view_row}>

                        <Form.Group className={styles.view_item} controlId="formDisaggregation">
                            <Form.Label>{translations["amp.indicatormanager:disaggregation"]}</Form.Label>
                            <Select
                                isMulti
                                name="disaggregation"
                                options={disaggregationOptions}
                                onChange={(selectedValues) => {
                                  const limitedValues = selectedValues.slice(0, 2);
                                  props.setFieldValue('disaggregation', limitedValues.map((v: any) => v.value));
                                }}
                                onBlur={props.handleBlur}
                                className={`basic-multi-select ${(props.errors.disaggregation && props.touched.disaggregation) && styles.text_is_invalid}`}
                                classNamePrefix="select"
                                value={(props.values.disaggregation || []).map(id => disaggregationOptions.find(opt => opt.value === id)).filter(Boolean)}
                            />
                        </Form.Group>
                    </Row>
                    {/* Accordion for disaggregation values, always in a new row below the select */}
                    {props.values.disaggregation?.length === 1 && (
                      <Row className={styles.view_row}>
                        <Col>
                          <div style={{marginTop: '1rem'}}>
                            <h6>{translations["amp.indicatormanager:disaggregation-values"]}</h6>
                            <Accordion defaultActiveKey="0">
                              {props.values.disaggregation.map((parentId, parentIdx) => (
                                <Card key={parentId}>
                                  <Accordion.Toggle
                                    as={Card.Header}
                                    eventKey={String(parentIdx)}
                                    className={styles.accordionHeader}
                                    style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', background: '#f7f7f7', fontWeight: 'bold' }}
                                    aria-label={translations["amp.indicatormanager:click-to-expand-collapse"]}
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
                                            let entryIdx = disaggArr.findIndex((v: any) => v.parentCategoryId === child.id && v.childCategoryId === null);
                                            // If not found, create a new entry and push it
                                            if (entryIdx === -1) {
                                              disaggArr.push({
                                                parentCategoryId: child.id,
                                                childCategoryId: null,
                                                base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                                                target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                                              });
                                              entryIdx = disaggArr.length - 1;
                                              props.setFieldValue('disaggregationValues', disaggArr);
                                            }
                                            const entry = disaggArr[entryIdx];
                                            return (
                                              <Card key={child.id} style={{marginBottom: '8px'}}>
                                                <Card.Body>
                                                  <Card.Title className={styles.accordionChildTitle}>{child.value}</Card.Title>
                                                  <div style={{display: 'flex', flexWrap: 'wrap', gap: '32px'}}>
                                                    <div style={{minWidth: '300px'}}>
                                                      <h6 color={"red"}>{translations["amp.indicatormanager:base-values"]}</h6>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:original-value"]}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.base.originalValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx, ['base', 'originalValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-base-original-value"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.base.originalValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx, ['base', 'originalValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-base-original-value-date"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.base.revisedValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx, ['base', 'revisedValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-base-revised-value"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.base.revisedValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx, ['base', 'revisedValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-base-revised-value-date"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                    </div>
                                                    <div style={{minWidth: '300px'}}>
                                                      <h6 color={"red"}>{translations["amp.indicatormanager:target-values"]}</h6>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:original-value"]}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.target.originalValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx, ['target', 'originalValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-target-original-value"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.target.originalValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx, ['target', 'originalValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-target-original-value-date"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                                                        <Form.Control
                                                          type="number"
                                                          value={entry.target.revisedValue || ''}
                                                          onChange={e => updateDisaggregationField(entryIdx, ['target', 'revisedValue'], e.target.value)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-target-revised-value"]}
                                                          disabled={false}
                                                        />
                                                      </Form.Group>
                                                      <Form.Group>
                                                        <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                                                        <DateInput
                                                          translations={translations}
                                                          value={entry.target.revisedValueDate || ''}
                                                          onChange={val => updateDisaggregationField(entryIdx, ['target', 'revisedValueDate'], val)}
                                                          className={styles.input_field}
                                                          aria-label={translations["amp.indicatormanager:aria-target-revised-value-date"]}
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
                                          {translations["amp.indicatormanager:no-disaggregation-children"]}
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
                              <h6>{translations["amp.indicatormanager:disaggregation-values"]}</h6>
                              <Accordion defaultActiveKey="0">
                                {disaggregationChildren[props.values.disaggregation[0]]?.map((parentChild: any, parentIdx: number) => (
                                  <Card key={parentChild.id}>
                                    <Accordion.Toggle
                                      as={Card.Header}
                                      eventKey={String(parentIdx)}
                                      className={styles.accordionHeader}
                                      style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', background: '#f7f7f7', fontWeight: 'bold' }}
                                      aria-label={translations["amp.indicatormanager:click-to-expand-collapse"]}
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
                                              let entryIdx = disaggArr.findIndex((v: any) => v.parentCategoryId === parentChild.id && v.childCategoryId === child.id);
                                              let entry = entryIdx !== -1 ? disaggArr[entryIdx] : {
                                                parentCategoryId: parentChild.id,
                                                childCategoryId: child.id,
                                                base: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' },
                                                target: { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' }
                                              };
                                              // If not found, create a new entry and push it
                                              if (entryIdx === -1) {
                                                disaggArr.push(entry);
                                                entryIdx = disaggArr.length - 1;
                                                props.setFieldValue('disaggregationValues', disaggArr);
                                              }
                                              return (
                                                <Card key={child.id} style={{marginBottom: '8px'}}>
                                                  <Card.Body>
                                                    <Card.Title className={styles.accordionChildTitle}>{child.value}</Card.Title>
                                                    <div style={{display: 'flex', flexWrap: 'wrap', gap: '32px'}}>
                                                      <div style={{minWidth: '300px'}}>
                                                        <h6 color={"red"}>{translations["amp.indicatormanager:base-values"]}</h6>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:original-value"]}</Form.Label>
                                                          <Form.Control
                                                            type="number"
                                                            value={entry.base.originalValue || ''}
                                                            onChange={e => updateDisaggregationField(entryIdx, ['base', 'originalValue'], e.target.value)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-base-original-value"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
                                                          <DateInput
                                                            translations={translations}
                                                            value={entry.base.originalValueDate || ''}
                                                            onChange={val => updateDisaggregationField(entryIdx, ['base', 'originalValueDate'], val)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-base-original-value-date"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                                                          <Form.Control
                                                            type="number"
                                                            value={entry.base.revisedValue || ''}
                                                            onChange={e => updateDisaggregationField(entryIdx, ['base', 'revisedValue'], e.target.value)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-base-revised-value"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                                                          <DateInput
                                                            translations={translations}
                                                            value={entry.base.revisedValueDate || ''}
                                                            onChange={val => updateDisaggregationField(entryIdx, ['base', 'revisedValueDate'], val)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-base-revised-value-date"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                      </div>
                                                      <div style={{minWidth: '300px'}}>
                                                        <h6 color={"red"}>{translations["amp.indicatormanager:target-values"]}</h6>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:original-value"]}</Form.Label>
                                                          <Form.Control
                                                            type="number"
                                                            value={entry.target.originalValue || ''}
                                                            onChange={e => updateDisaggregationField(entryIdx, ['target', 'originalValue'], e.target.value)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-target-original-value"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:original-value-date"]}</Form.Label>
                                                          <DateInput
                                                            translations={translations}
                                                            value={entry.target.originalValueDate || ''}
                                                            onChange={val => updateDisaggregationField(entryIdx, ['target', 'originalValueDate'], val)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-target-original-value-date"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:revised-value"]}</Form.Label>
                                                          <Form.Control
                                                            type="number"
                                                            value={entry.target.revisedValue || ''}
                                                            onChange={e => updateDisaggregationField(entryIdx, ['target', 'revisedValue'], e.target.value)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-target-revised-value"]}
                                                            disabled={false}
                                                          />
                                                        </Form.Group>
                                                        <Form.Group>
                                                          <Form.Label>{translations["amp.indicatormanager:revised-value-date"]}</Form.Label>
                                                          <DateInput
                                                            translations={translations}
                                                            value={entry.target.revisedValueDate || ''}
                                                            onChange={val => updateDisaggregationField(entryIdx, ['target', 'revisedValueDate'], val)}
                                                            className={styles.input_field}
                                                            aria-label={translations["amp.indicatormanager:aria-target-revised-value-date"]}
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
                                            {translations["amp.indicatormanager:no-disaggregation-children"]}
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
                    <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{translations["amp.indicatormanager:responsibility-frequency-info"]}</h5></Col></Row>
                    <div className={styles.sectionContainer}>
                      <Row className={styles.view_row}>
                        <Form.Group className={styles.view_item} controlId="formResponsibleOrganizations">
                          <Form.Label>{translations["amp.indicatormanager:responsible-organizations"]}</Form.Label>
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
                          <Form.Label>{translations["amp.indicatormanager:frequency"]}</Form.Label>
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
                    <Row className={styles.view_row}><Col><h5 className={styles.sectionTitle}>{translations["amp.indicatormanager:value-tracking"]}</h5></Col></Row>
                    <div className={styles.sectionContainer}>
                      {props.values.disaggregation?.length > 0 && (
                        <Row className={styles.view_row}>
                          <Col>
                            <div style={{color: '#856404', background: '#fff3cd', border: '1px solid #ffc107', borderRadius: '4px', padding: '0.5rem 1rem', marginBottom: '0.5rem'}}>
                              {translations["amp.indicatormanager:value-tracking-disabled-disaggregation"] || "Regular base/target values are disabled when disaggregation is active and will be cleared on save."}
                            </div>
                          </Col>
                        </Row>
                      )}
                      <Form.Group as={Col}>
                        <Form.Label className={styles.view_one_item}>
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
                              disabled={props.values.disaggregation?.length > 0}
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
                                value={props.values.base?.originalValueDate}
                                onChange={(value) => {
                                  if (value) {
                                    props.setFieldValue("base.originalValueDate", value);
                                  }
                                }}
                                onClear={() => {
                                  props.setFieldValue("base.originalValueDate", null);
                            }}
                            onBlur={props.handleBlur}
                            name="base.originalValueDate"
                            disabled={props.values.disaggregation?.length > 0 || baseOriginalValueDateDisabled}
                            className={`${styles.input_field} ${(props.errors.base?.originalValueDate && props.touched.base?.originalValueDate) && styles.text_is_invalid}`}
                            id="baseOriginalValueDate"
                            inputRef={baseOriginalValueDateRef}
                        />

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
                              disabled={props.values.disaggregation?.length > 0}
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
                              onChange={(value) =>{
                                if (value) {
                                  props.setFieldValue("base.revisedValueDate", value);
                                }
                            }}
                            onClear={() => {
                              props.setFieldValue("base.revisedValueDate", null);
                            }}
                            onBlur={props.handleBlur}
                            name="base.revisedValueDate"
                            disabled={props.values.disaggregation?.length > 0}
                            className={`${styles.input_field} ${(props.errors.base?.revisedValueDate && props.touched.base?.revisedValueDate) && styles.text_is_invalid}`}
                            id="baseRevisedValueDate"
                            inputRef={baseRevisedValueDateRef}
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
                              disabled={props.values.disaggregation?.length > 0}
                              className={`${styles.input_field} ${(props.errors.target?.originalValue && props.touched.target?.originalValue) && styles.text_is_invalid}`}
                              placeholder={translations["amp.indicatormanager:enter-target-value"]} />

                          <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                            {props.errors.target?.originalValue}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className={styles.view_item}>
                          <Form.Label>{translations["amp.indicatormanager:target-value-date"]}</Form.Label>
                          <DateInput
                              translations={translations}
                              value={props.values.target.originalValueDate}
                              onChange={(value) => {
                                if (value) {
                                  props.setFieldValue("target.originalValueDate", value);
                                }
                              }}
                              onClear={() => {
                                props.setFieldValue("target.originalValueDate", null);
                            }}
                            disabled={props.values.disaggregation?.length > 0 || targetOriginalValueDateDisabled}
                            onBlur={props.handleBlur}
                            name="target.originalValueDate"
                            className={`${styles.input_field} ${(props.errors.target?.originalValueDate && props.touched.target?.originalValueDate) && styles.text_is_invalid}`}
                            id="targetOriginalValueDate"
                            inputRef={targetOriginalValueDateRef}
                        />
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
                              disabled={props.values.disaggregation?.length > 0}
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
                                  props.setFieldValue("target.revisedValueDate", value);
                                }
                              }}
                              onClear={() => {
                                props.setFieldValue("target.revisedValueDate", null);
                            }}
                            onBlur={props.handleBlur}
                            name="target.revisedValueDate"
                            disabled={props.values.disaggregation?.length > 0}
                            className={`${styles.input_field} ${(props.errors.target?.revisedValueDate && props.touched.target?.revisedValueDate) && styles.text_is_invalid}`}
                            id="targetRevisedValueDate"
                            inputRef={targetRevisedValueDateRef}
                        />

                          <Form.Control.Feedback type="invalid" className={styles.text_is_invalid}>
                            {props.errors.target?.revisedValueDate}
                          </Form.Control.Feedback>
                        </Form.Group>
                      </Row>
                    </Form.Group>
                  </div>
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
                            defaultValue={props.values.creationDate}
                            disabled
                            value={props.values.creationDate}
                            clearIcon={null}
                            calendarIcon={null}
                            className={styles.input_field} />
                      </Form.Group>
                    </Row>
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
        )}}
      </Formik>
    </Modal>
  );
};

export default EditIndicatorModal;

