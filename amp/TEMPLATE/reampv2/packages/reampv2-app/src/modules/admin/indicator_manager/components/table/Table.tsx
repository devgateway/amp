/* eslint-disable import/no-unresolved */
import React, { useEffect, useState } from 'react';
import {
  Col, Row, Button, Form
} from 'react-bootstrap';
// @ts-ignore
import BootstrapTable, { PaginationOptions } from '@musicstory/react-bootstrap-table-next';
// @ts-ignore
import paginationFactory from '@musicstory/react-bootstrap-table2-paginator';
// @ts-ignore
import filterFactory, { FilterFactoryProps } from '@musicstory/react-bootstrap-table2-filter';
import '@musicstory/react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
// @ts-ignore
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from '@murasoftware/react-bootstrap-table2-toolkit';
import styles from './Table.module.css';
import AddNewIndicatorModal from '../modals/AddNewIndicatorModal';
import {
  DefaultComponentProps,
  GroupSelectValue,
  ProgramObjectType,
  ProgramSchemeType,
  SectorObjectType, SelectValue
} from '../../types';
import { useSelector, useDispatch } from 'react-redux';
import { setSizePerPage} from '../../reducers/fetchIndicatorsReducer';
import Select from "react-select";
import {formatProgramSchemeToSelect} from "../../utils/helpers";
import { useNavigate } from 'react-router-dom';

interface SkeletonTableProps extends DefaultComponentProps {
  columns: any;
  data: any;
  title: string;
  sectors?: SectorObjectType[];
  programs?: ProgramObjectType[];
  setSelectedSector: React.Dispatch<React.SetStateAction<number>>;
  setSelectedProgram: React.Dispatch<React.SetStateAction<number>>;
  setSelectedOutcome: React.Dispatch<React.SetStateAction<number>>;
  setSelectedOutput: React.Dispatch<React.SetStateAction<number>>;
  setSelectedIndicatorType: React.Dispatch<React.SetStateAction<number>>;
  filterBySector: boolean;
  filterByProgram: boolean;
}

const groupStyles = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
};
const groupBadgeStyles = {
  backgroundColor: '#EBECF0',
  borderRadius: '2em',
  color: '#172B4D',
  display: 'inline-block',
  fontSize: 12,
  fontWeight: 'normal',
  lineHeight: '1',
  minWidth: 1,
  padding: '0.16666666666667em 0.5em',
  textAlign: 'center',
};

const formatGroupLabel = (data: any) => (
    <div style={groupStyles}>
      <span>{data.label}</span>
      <span style={groupBadgeStyles as any}>{data.options.length}</span>
    </div>
);


const SkeletonTable: React.FC<SkeletonTableProps> = (props) => {
  const {
    columns,
    data,
    title,
    sectors,
    setSelectedSector,
    setSelectedProgram,
      setSelectedOutcome,
      setSelectedOutput,
      setSelectedIndicatorType,
    translations,
    filterBySector,
    filterByProgram,
    programs
  } = props;
  const dispatch = useDispatch();
  const programSchemeReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const outcomesReducer = useSelector((state: any) => state.fetchOutcomesReducer);
  const indicatorTypesReducer = useSelector((state: any) => state.fetchIndicatorTypesReducer);
  const outputsReducer = useSelector((state: any) => state.fetchOutputsReducer);
  const programConfiguration: ProgramSchemeType [] = programSchemeReducer.programSchemes;

  const sizePerPage: number = useSelector((state: any) => state.fetchIndicatorsReducer.sizePerPage);

  const handleSizePerPageChange = (onSizePerPageChange: (value: number) => void, size: number) => {
    onSizePerPageChange(size);
    dispatch(setSizePerPage(size))
  }

  const { SearchBar } = Search;
  const { ExportCSVButton } = CSVExport;

  const [showAddNewIndicatorModal, setShowAddNewIndicatorModal] = useState(false);
  const [programOptions, setProgramOptions] = useState<GroupSelectValue[]>([
    {
      label: translations['amp.indicatormanager:all-programs'],
      options: [{
        value: 0,
        label: translations['amp.indicatormanager:all-programs']
      }]
    }
  ]);
  const [sectorOptions, setSectorOptions] = useState<SelectValue[]>([{
    value: 0,
    label: translations['amp.indicatormanager:all-sectors']
  }]);

  const [outcomeOptions, setOutcomeOptions] = useState<SelectValue[]>([{
    value: 0,
    label: translations['amp.indicatormanager:all-outcomes']
  }]);
  const [outputOptions, setOutputOptions] = useState<SelectValue[]>([{
    value: 0,
    label: translations['amp.indicatormanager:all-outputs']
  }]);
  const [indicatorTypeOptions, setIndicatorTypeOptions] = useState<SelectValue[]>([{
    value: 0,
    label: translations['amp.indicatormanager:all-indicator-types']
  }]);




  const showAddNewIndicatorModalHandler = () => {
    setShowAddNewIndicatorModal(true);
  };

  useEffect(() => {
    const formatPrograms = formatProgramSchemeToSelect(programConfiguration);
    setProgramOptions(prevState => [...prevState, ...formatPrograms]);

    if (sectors) {
      const formatSectors = sectors.map((sector) => ({
        value: sector.id,
        label: sector.name,
      }));
      setSectorOptions(prevState => [...prevState, ...formatSectors]);
    }

    // Set outcome options from Redux
    if (outcomesReducer && outcomesReducer.outcomes) {
      const formattedOutcomes = outcomesReducer.outcomes.map((outcome: any) => ({
        value: outcome.id,
        label: outcome.name,
      }));
      setOutcomeOptions([{ value: 0, label: translations['amp.indicatormanager:all-outcomes'] }, ...formattedOutcomes]);
    }

    if (outputsReducer && outputsReducer.outputs) {
      const formattedOutputs = outputsReducer.outputs.map((output: any) => ({
        value: output.id,
        label: output.name,
      }));
      setOutputOptions([{ value: 0, label: translations['amp.indicatormanager:all-outputs'] }, ...formattedOutputs]);
    }

    // Set indicator type options from Redux
    if (indicatorTypesReducer && indicatorTypesReducer.indicatorTypes) {
      const formattedIndicatorTypes = indicatorTypesReducer.indicatorTypes.map((indicatorType: any) => ({
        value: indicatorType.id,
        label: indicatorType.name,
      }));
      setIndicatorTypeOptions([{ value: 0, label: translations['amp.indicatormanager:all-indicator-types'] }, ...formattedIndicatorTypes]);
    }
  }, [sectors, programConfiguration, translations]);

  useEffect(() => {
    setSelectedSector(0);
  }, [setSelectedSector]);

  // create a pagination factory
  const paginationOptions: PaginationOptions = {
    paginationSize: 4,
    pageStartIndex: 1,
    alwaysShowAllBtns: true,
    sizePerPageList: [
      {
        text: '10',
        value: 10,
      },
      {
        text: '25',
        value: 25,
      },
      {
        text: '50',
        value: 50,
      },
      {
        text: '100',
        value: 100,
      },
      {
        text: translations['amp.indicatormanager:all'],
        value: data.length,
      }
    ],
    firstPageText: translations['amp.indicatormanager:first'],
    prePageText: translations['amp.indicatormanager:previous'],
    nextPageText: translations['amp.indicatormanager:next'],
    lastPageText: translations['amp.indicatormanager:last'],
    nextPageTitle: translations['amp.indicatormanager:next-page'],
    prePageTitle: translations['amp.indicatormanager:pre-page'],
    firstPageTitle: translations['amp.indicatormanager:first-page'],
    lastPageTitle: translations['amp.indicatormanager:last-page'],
    showTotal: true,
    sizePerPage: sizePerPage,
    hidePageListOnlyOnePage: true,
    sizePerPageRenderer: ({
      options,
      currSizePerPage,
      onSizePerPageChange,
    }: {
        options: any;
        currSizePerPage: number;
        onSizePerPageChange: (value: number) => void;
    }) => (
      <Row lg={4}>
        <Form.Control
          as="select"
          value={currSizePerPage}
          style={{
            width: 180,
            marginLeft: 20
          }}
          onChange={(e) => handleSizePerPageChange(onSizePerPageChange, parseInt(e.target.value))}
        >
          {
            options.map((option: any) => (
              <option key={option.text} value={option.page}>
                {option.text}
              </option>
            ))
          }
        </Form.Control>
      </Row>

    )
  };

  const filterOptions: FilterFactoryProps = {
    afterFilter: (result: any, column: any) => {
      // console.log(result, column);
    }
  };

  const navigate = useNavigate();

  return (
    <>
      <AddNewIndicatorModal
          show={showAddNewIndicatorModal}
          setShow={setShowAddNewIndicatorModal}
          filterBySector={filterBySector}
          filterByProgram={filterByProgram}
          translations={translations} />
      <Col sm={12}>
        <ToolkitProvider
          keyField="id"
          data={data}
          columns={columns}
          search
          exportCSV
        >
          {
            (props: ToolkitContextType) => (
              <div>
                <Col
                  sm={12}
                  style={{
                    paddingBottom: 15
                  }}>
                  <Row className={styles.table_header}>
                    <Col sm={6}>
                      <h3>{title}</h3>
                    </Col>
                    <Col sm={6}>
                      <hr />
                    </Col>
                  </Row>

                  <Row sm={12} className={styles.table_header_bottom}>
                    <Col sm={4}>
                      <div className={styles.table_header_bottom_left}>
                        <Button type="primary" onClick={showAddNewIndicatorModalHandler}>
                          <i className="fa fa-plus" />
                          {' '}
                          <span>{translations['amp.dashboard:add-new']}</span>
                        </Button>
                        <span style={{ float: 'right', marginLeft: '10px' }}>
                        <Button type="secondary" onClick={() => navigate('/admin/indicator_manager/outcome-output-management')}>
                          <i className="fa fa-tasks" />
                          {' '}
                          <span>{translations['amp.dashboard:outcome-output-management'] || 'Outcome and Output Management'}</span>
                        </Button>
                        </span>
                        <ExportCSVButton
                          {...props.csvProps}
                          className={styles.export_button}
                        >
                          <i className="fa fa-download" />
                          {' '}
                          <span>{translations['amp.indicatormanager:export-csv']}</span>
                        </ExportCSVButton>
                      </div>

                    </Col>
                    {/* searchbar should be far right on the col */}
                    <Col sm={8}>
                      <div className={styles.table_header_bottom_right}>

                        {filterBySector && (
                            <div className={styles.sector_filter_container}>
                              <Form.Label
                                  className={styles.filter_label}>{translations['amp.indicatormanager:sectors']}</Form.Label>
                              {
                                sectorsReducer.sectors.length > 0 ? (
                                    <Select
                                        options={sectorOptions as any}
                                        defaultValue={{
                                          value: 0,
                                          label: translations['amp.indicatormanager:all-sectors']
                                        }}
                                        className={styles.filter_select}
                                        onChange={(item: any) => {
                                          setSelectedSector(item.value)
                                        }}
                                        components={{
                                          IndicatorSeparator: () => null,
                                        }}
                                    />
                                ) : (
                                    <>
                                    {
                                      !sectorsReducer.loading && (
                                          <Select
                                              options={[]}
                                              defaultValue={{
                                                value: 0,
                                                label: translations['amp.indicatormanager:no-data']
                                              }}
                                              isDisabled
                                              components={{
                                                IndicatorSeparator: () => null,
                                              }}
                                              className={styles.filter_select}
                                          />
                                      )

                                    }
                                    </>
                                )
                              }
                            </div>
                        )}

                        {
                          filterByProgram && (
                                <div className={styles.sector_filter_container}>
                                    <Form.Label
                                        className={styles.filter_label}>{translations['amp.indicatormanager:programs']}</Form.Label>
                                  {
                                    programOptions.length > 0 ? (
                                        <Select
                                            options={programOptions}
                                            formatGroupLabel={formatGroupLabel}
                                            defaultValue={{
                                                value: 0,
                                                label: translations['amp.indicatormanager:all-programs']
                                            }}
                                            className={styles.filter_select}
                                            // Added this since it is using the optiongroup type rather than the option type
                                            // @ts-ignore
                                            onChange={(item: {value: number, label: string}) => {
                                                setSelectedProgram(item.value)
                                            }}
                                            components={{
                                              IndicatorSeparator: () => null,
                                            }}
                                        />
                                    ) : (
                                        <>
                                        {
                                          !programSchemeReducer.loading && (
                                              <Select
                                                  options={[]}
                                                  defaultValue={{
                                                    value: 0,
                                                    label: translations['amp.indicatormanager:no-data']
                                                  }}
                                                  isDisabled
                                                  components={{
                                                    IndicatorSeparator: () => null,
                                                  }}
                                                  className={styles.filter_select}
                                              />
                                          )
                                        }
                                        </>
                                    )
                                  }
                                </div>
                          )
                        }

                        {/* Outcome filter */}
                        <div className={styles.sector_filter_container}>
                          <Form.Label className={styles.filter_label}>{translations['amp.indicatormanager:outcome']}</Form.Label>
                          <Select
                            options={outcomeOptions}
                            onChange={(opt:any) =>setSelectedOutcome(opt?.value || 0)}
                            className={styles.filter_select}
                            components={{ IndicatorSeparator: () => null }}
                          />
                        </div>
                        {/* Output filter */}
                        <div className={styles.sector_filter_container}>
                          <Form.Label className={styles.filter_label}>{translations['amp.indicatormanager:output']}</Form.Label>
                          <Select
                            options={outputOptions}
                            onChange={(opt : any) => setSelectedOutput(opt?.value || 0)}
                            className={styles.filter_select}
                            components={{ IndicatorSeparator: () => null }}
                          />
                        </div>
                        {/* Indicator Type filter */}
                        <div className={styles.sector_filter_container}>
                          <Form.Label className={styles.filter_label}>{translations['amp.indicatormanager:indicator-type']}</Form.Label>
                          <Select
                            options={indicatorTypeOptions}
                            onChange={(opt:any) => setSelectedIndicatorType(opt?.value || 0)}
                            className={styles.filter_select}
                            components={{ IndicatorSeparator: () => null }}
                          />
                        </div>


                        <div className={styles.search_container}>
                          <SearchBar
                              {...props.searchProps}
                              placeholder={translations['amp.indicatormanager:search']}
                              // Only search by name and code
                              columns={[{ dataField: 'name', text: 'Name' }, { dataField: 'code', text: 'Code' }]}
                          />
                        </div>
                      </div>

                    </Col>
                  </Row>
                </Col>
                <hr/>
                <BootstrapTable
                    {...props.baseProps}
                    headerClasses={styles.table_header_titles}
                    bodyClasses={styles.table_body}
                    pagination={paginationFactory(paginationOptions)}
                    selectRow={{
                      mode: 'checkbox',
                      clickToSelect: false,
                    }}
                    filter={filterFactory(filterOptions)}
                    noDataIndication={() => (
                        <div className={styles.no_data}>
                          <h5>{translations['amp.indicatormanager:no-data']}</h5>
                        </div>
                    )}
                />
              </div>
            )
          }
        </ToolkitProvider >

      </Col >
    </>

  );
};

export default SkeletonTable;
