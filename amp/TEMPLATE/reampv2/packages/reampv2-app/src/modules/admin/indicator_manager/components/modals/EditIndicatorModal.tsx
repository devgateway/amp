/* eslint-disable import/no-unresolved */
import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import {
  Form, Modal, Button, Col, Row
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


const MySwal = withReactContent(Swal);

const ascendingOptions = [
  { value: true, label: 'True' },
  { value: false, label: 'False' }
];

interface EditIndicatorModalProps extends DefaultComponentProps {
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
  programId: string | any;
  base: BaseAndTargetValueType;
  target: BaseAndTargetValueType;
  indicatorsCategory?: string;
}

const EditIndicatorModal: React.FC<EditIndicatorModalProps> = (props) => {
  const { show, setShow, indicator, translations } = props;
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

  const [programFieldVisible, setProgramFieldVisible] = useState(false);
  const [selectedProgramSchemeId, setSelectedProgramSchemeId] = useState<string | null>(null);

  const [sectors, setSectors] = useState<{ value: string, label: string }[]>([]);
  const [categories, setCategories] = useState<{ value: string, label: string }[]>([]);
  const [programSchemes, setProgramSchemes] = useState<{ value: string, label: string }[]>([]);
  const [programs, setPrograms] = useState<{ value: string, label: string }[]>([]);

  const [defaultCategory, setDefaultCategory] = useState<{ value: string, label: string } | null>(null);
  const [defaultSectors, setDefaultSectors] = useState<{ value: string, label: string }[] | null>(null);
  const [defaultProgram, setDefaultProgram] = useState<{ value: string, label: string } | null>(null);
  const [defaultProgramScheme, setDefaultProgramScheme] = useState<{ value: string, label: string } | null>(null);

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
          formikRef?.current?.setFieldValue("base.originalValueDate", DateUtil.formatJavascriptDate(programScheme.startDate || ''));
          setBaseOriginalValueDateDisabled(true);
        }

        if (programScheme.endDate) {
          formikRef?.current?.setFieldValue("target.originalValueDate", DateUtil.formatJavascriptDate(programScheme.endDate || ''));
          setTargetOriginalValueDateDisabled(true);
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
    if (indicator?.sectors.length === 0) {
      setDefaultSectors([]);
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
    getDefaultCategory();
    getDefaultProgram();
    getDefaultPropgramScheme();
    getDefaultSectors();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [indicator]);

  useDidMountEffect(() => {
    if (updateIndicatorReducer.loading) {
      MySwal.fire({
        icon: 'info',
        title: 'Updating Indicator...',
        timer: 18000
      });
      return;
    }

    if (!updateIndicatorReducer.loading && !updateIndicatorReducer.error) {
      MySwal.fire({
        icon: 'success',
        title: 'Indicator updated successfully',
        timer: 3000
      }).then(() => {
        handleClose();
        dispatch(getIndicators());
      });
      return;
    }

    MySwal.fire({
      icon: 'error',
      title: 'Oops...',
      text: updateIndicatorReducer.loading ? translations["amp.indicatormanager:save-failed"] : updateIndicatorReducer.error,
    });
  }, [updateIndicatorReducer]);

  const initialValues: IndicatorFormValues = {
    name: indicator?.name || '',
    description: indicator?.description || '',
    code: indicator?.code || '',
    sectors: [],
    programId: '',
    ascending: indicator?.ascending || false,
    creationDate: indicator?.creationDate,
    base: {
      originalValue: indicator?.base?.originalValue,
      originalValueDate: indicator?.base?.originalValueDate,
      revisedValue: indicator?.base?.revisedValue,
      revisedValueDate: indicator?.base?.revisedValueDate,
    },
    target: {
      originalValue: indicator?.target?.originalValue,
      originalValueDate: indicator?.target?.originalValueDate,
      revisedValue: indicator?.target?.revisedValue,
      revisedValueDate: indicator?.target?.revisedValueDate,
    },
    indicatorsCategory: indicator?.indicatorsCategory?.toString() || ''
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
          const { name, description, code, sectors, ascending, programId, creationDate, base, target, indicatorsCategory } = values;
          const updatedIndicatorData = {
            id: indicator.id,
            name,
            description,
            code,
            sectors: formatObjArrayToNumberArray(sectors),
            programId: programId ? parseInt(programId) : null,
            ascending,
            creationDate: creationDate && formatDate(creationDate),
            base: checkObjectIsNull(base) ? null : {
              originalValue: base.originalValue ? lodash.toNumber(base.originalValue) : null,
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
            indicatorsCategory : indicatorsCategory ? parseInt(indicatorsCategory) : null
          };

          dispatch(updateIndicator(updatedIndicatorData as IndicatorObjectType));
        }}
      >
        {(props) => (
          <>
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
                        className="basic-multi-select"
                        classNamePrefix="select"
                        onBlur={props.handleBlur}
                        onChange={(value) => {
                          props.setFieldValue("ascending", value?.value);
                        }}
                        defaultValue={{ value: props.values.ascending, label: props.values.ascending ? "True" : "False" }}
                      />
                    </Form.Group>

                    <Form.Group className={styles.view_item} controlId="formCreationDate">
                      <Form.Label>{translations["amp.indicatormanager:table-header-creation-date"]}</Form.Label>
                      <DateInput
                        name="creationDate"
                        value={props.values.creationDate}
                        disabled
                        defaultValue={new Date()}
                        clearIcon={null}
                        calendarIcon={null}
                        className={styles.input_field}
                       id="creationDate"
                      inputRef={creationDateRef}/>
                    </Form.Group>
                  </Row>

                  <Row className={styles.view_row}>
                    <Form.Group className={styles.view_one_item} controlId="formIndicatorSectors">
                      <Form.Label>{translations["amp.indicatormanager:sectors"]}</Form.Label>
                      {
                        (sectors.length > 0 && defaultSectors)? (
                          <Select
                            isMulti
                            name="sectors"
                            isClearable
                            options={sectors}
                            onChange={(values) => {
                              // set the formik value with the selected values and remove the label
                              const selectedValues = values.map((value: any) => parseInt(value.value))
                              props.setFieldValue('sectors', selectedValues);
                            }}
                            getOptionValue={(option) => option.value}
                            onBlur={props.handleBlur}
                            className={`basic-multi-select ${(props.errors.sectors && props.touched.sectors) && styles.text_is_invalid}`}
                            classNamePrefix="select"
                            defaultValue={defaultSectors}
                          />
                        ) : (
                            <Select
                                isDisabled={true}
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
                                name="indicatorsCategory"
                                options={categories}
                                onChange={(selectedValue) => {
                                  // set the formik value with the selected values and remove the label
                                  if (selectedValue) {
                                    setDefaultCategory(selectedValue)
                                    props.setFieldValue('indicatorsCategory', selectedValue?.value);
                                  }
                                }}
                                isClearable
                                getOptionValue={(option) => option.value}
                                onBlur={props.handleBlur}
                                className={`basic-multi-select ${(props.errors.indicatorsCategory && props.touched.indicatorsCategory) && styles.text_is_invalid}`}
                                classNamePrefix="select"
                                value={defaultCategory}
                            />
                        ) : (
                            <Select
                                name="categories"
                                isDisabled={true}
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
                        ) : (
                            <Select
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
                          (programs.length > 0 && defaultProgram) ? (
                            <Select
                              name="programs"
                              options={programs}
                              onChange={(selectedValue) => {
                                // set the formik value with the selected values and remove the label
                                props.setFieldValue("programId", selectedValue?.value);
                              }}
                              isClearable
                              getOptionValue={(option) => option.value}
                              onBlur={props.handleBlur}
                              className={`basic-multi-select ${styles.input_field} ${(props.errors.programId && props.touched.programId) && styles.text_is_invalid}`}
                              classNamePrefix="select"
                              defaultValue={defaultProgram}
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
                          disabled={baseOriginalValueDateDisabled}
                          className={`${styles.input_field} ${(props.errors.base?.originalValueDate && props.touched.base?.originalValueDate) && styles.text_is_invalid}`}
                          id="baseOriginalValueDate"
                          inputRef={baseOriginalValueDateRef}
                        />

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
                          value={props.values.target.originalValueDate}
                          onChange={(value) => {
                            if (value) {
                              props.setFieldValue("target.originalValueDate", value);
                            }
                          }}
                          onClear={() => {
                            props.setFieldValue("target.originalValueDate", null);
                          }}
                          disabled={targetOriginalValueDateDisabled}
                          onBlur={props.handleBlur}
                          name="target.originalValueDate"
                          className={`${styles.input_field} ${(props.errors.target?.originalValueDate && props.touched.target?.originalValueDate) && styles.text_is_invalid}`}
                          id="targetOriginalValueDate"
                          inputRef={targetOriginalValueDateRef}
                        />
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
                              props.setFieldValue("target.revisedValueDate", value);
                            }
                          }}
                          onClear={() => {
                            props.setFieldValue("target.revisedValueDate", null);
                          }}
                          onBlur={props.handleBlur}
                          name="target.revisedValueDate"
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
          </>
        )}

      </Formik>
    </Modal>
  );
};

export default EditIndicatorModal;
