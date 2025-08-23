/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import React, {useEffect, useLayoutEffect, useMemo, useState} from 'react';
import {connect, useDispatch, useSelector} from 'react-redux';
import {Row} from 'react-bootstrap';
import {bindActionCreators} from 'redux';
// @ts-ignore
import {ColumnDescription} from '@musicstory/react-bootstrap-table-next';
import SkeletonTable from './Table';
import styles from './IndicatorTable.module.css';
import {DefaultComponentProps, IndicatorObjectType, ProgramObjectType, SettingsType} from '../../types';

import {getIndicators} from '../../reducers/fetchIndicatorsReducer';


// Modals
import ViewIndicatorModal from '../modals/ViewIndicatorModal';
import EditIndicatorModal from '../modals/EditIndicatorModal';
import DeleteIndicatorModal from '../modals/DeleteIndicatorModal';
import {Loading} from '../../../../../utils/components/Loading';

interface IndicatorTableProps extends DefaultComponentProps {
}

const IndicatorTable: React.FC<IndicatorTableProps> = ({ translations }) => {
  const dispatch = useDispatch();
  const globalSettings: SettingsType = useSelector((state: any) => state.fetchSettingsReducer.settings);
  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const outcomesReducer = useSelector((state: any) => state.fetchOutcomesReducer);
  const ampCategoryReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);
  const outputsReducer = useSelector((state: any) => state.fetchOutputsReducer);
  const { indicators: fetchedIndicators, loading } = useSelector((state: any) => state.fetchIndicatorsReducer);


  useLayoutEffect(() => {
    dispatch(getIndicators());
  }, []);


  const [selectedRow, setSelectedRow] = useState<any>(null);
  const [showViewIndicatorModal, setShowViewIndicatorModal] = useState<boolean>(false);
  const [showEditIndicatorModal, setShowEditIndicatorModal] = useState<boolean>(false);
  const [showDeleteIndicatorModal, setShowDeleteIndicatorModal] = useState<boolean>(false);
  const [selectedSector, setSelectedSector] = useState(0);
  const [selectedProgram, setSelectedProgram] = useState(0);
  const [selectedOutcome, setSelectedOutcome] = useState(0);
  const [selectedOutput, setSelectedOutput] = useState(0);
  const [selectedIndicatorType, setSelectedIndicatorType] = useState(0);
  const [indicatorCode, setIndicatorCode] = useState('');
  const [indicatorName, setIndicatorName] = useState('');
  const [indicators, setIndicators] = useState<IndicatorObjectType[]>(fetchedIndicators);

  useEffect(() => {
    if (fetchedIndicators) {
      setIndicators(fetchedIndicators);
    }
  }, [fetchedIndicators]);

  const viewIndicatorModalHandler = (row: any) => {
    setSelectedRow(row);
    setShowViewIndicatorModal(true);
  };

  const editIndicatorModalHandler = (row: any) => {
    setSelectedRow(row);
    setShowEditIndicatorModal(true);
  };

  const deleteIndicatorModalHandler = (row: any) => {
    setSelectedRow(row);
    setShowDeleteIndicatorModal(true);
  };

  const columns: ColumnDescription<any, any>[] = useMemo(() => [
    {
      dataField: 'id',
      text: translations['amp.indicatormanager:table-header-id'],
      sort: true,
      headerStyle: { width: '10%' },
    },
    {
      dataField: 'code',
      text: translations['amp.indicatormanager:table-header-code'],
      sort: true,
      headerStyle: { width: '10%' },
    },
    {
      dataField: 'name',
      text: translations['amp.indicatormanager:table-header-indicator-name'],
      sort: true,
      headerStyle: { width: '35%' },
    },
      ...(globalSettings["indicator-filter-by-sector"] ? [
        {
          dataField: 'sectors',
          text: translations['amp.indicatormanager:sectors'],
          sort: true,
          headerStyle: { width: '30%' },
          csvFormatter: (_cell: any, row: any) => {
            return _cell.map((sectorId: any) => {
              if (sectorId) {
                const foundSector = !sectorsReducer.loading && sectorsReducer.sectors.find((sector: any) => sector.id === sectorId);
                if (foundSector) {
                  return foundSector.name
                } else {
                  return sectorId
                }
              } else {
                return ''
              }
            });
          },
          formatter: (_cell: any, row: any) => {
            return (
                <div>
                  {
                    _cell.map((sectorId: any) => {
                      const foundSector = !sectorsReducer.loading && sectorsReducer.sectors.find((sector: any) => sector.id === sectorId);
                      if (foundSector) {
                        return <span key={sectorId}>{foundSector.name}<br /></span>
                      }
                      return (
                          <span key={sectorId}>{sectorId}<br /></span>
                      )
                    })
                  }
                </div>
            )
          },
        }
      ]: []),
    // Program column
    ...(globalSettings["indicator-filter-by-program"] ? [
      {
        dataField: 'programId',
        text: translations['amp.indicatormanager:programs'],
        sort: true,
        headerStyle: {width: '40%'},
        csvFormatter: (_cell: any, row: any) => {
          const programId = row.programId;
          if (programId) {
            const foundProgram = !programsReducer.loading && programsReducer.programs.find((program: any) => program.id === programId);
            if (foundProgram) {
              return foundProgram.name;
            } else {
              return programId;
            }
          } else {
            return '';
          }
        },
        formatter: (_cell: any, row: any) => {
          const programId = row.programId;
          const foundProgram = !programsReducer.loading && programsReducer.programs.find((program: any) => program.id === programId);
          if (foundProgram) {
            return <span key={programId}>{foundProgram.name}<br /></span>
          }
          return (
              <span key={programId}>{programId}<br /></span>
          )
        }
      }
    ]: []),
      // Outcome column
      {
        dataField: 'outcome',
        text: translations['amp.indicatormanager:outcome'],
        sort: true,
        headerStyle: { width: '20%' },
        formatter: (_cell: any, row: any) => {
          if (outcomesReducer.loading) return '';
          const foundOutcome = !outcomesReducer.loading && outcomesReducer.outcomes.find((outcome: any) => outcome.id === row.outcomeId);
          return foundOutcome ? foundOutcome.name : '';
        },
        csvFormatter: (_cell: any, row: any) => {
          if (outcomesReducer.loading) return '';
          const foundOutcome = !outcomesReducer.loading && outcomesReducer.programs.find((outcome: any) => outcome.id === row.outcomeId);
          return foundOutcome ? foundOutcome.name : '';
        }
      },
    // Output column
    {
      dataField: 'output',
      text: translations['amp.indicatormanager:output'],
      sort: true,
      headerStyle: { width: '20%' },
      formatter: (_cell: any, row: any) => {
        const foundOutput = !outputsReducer.loading && outputsReducer.outputs.find((output: any) => output.id === row.outputId);
        return foundOutput ? foundOutput.name : row.output || '';
      },
      csvFormatter: (_cell: any, row: any) => {
        const foundOutput = !outputsReducer.loading && outputsReducer.outputs.find((output: any) => output.id === row.outputId);
        return foundOutput ? foundOutput.name : row.output || '';
      }
    },
      // Indicator Type column
      {
        dataField: 'indicatorType',
        text: translations['amp.indicatormanager:indicator-type'],
        sort: true,
        headerStyle: { width: '15%' },
        formatter: (_cell: any, row: any) => {
          const foundType = !ampCategoryReducer.loading && ampCategoryReducer.categories.find((cat: any) => cat.id === row.indicatorType);
          return foundType ? foundType.value : row.indicatorType || '';
        },
        csvFormatter: (_cell: any, row: any) => {
          const foundType = !ampCategoryReducer.loading && ampCategoryReducer.categories.find((cat: any) => cat.id === row.indicatorType);
          return foundType ? foundType.value : row.indicatorType || '';
        }
      },
    {
      dataField: 'creationDate',
      text: translations['amp.indicatormanager:table-header-creation-date'],
      sort: true,
      headerStyle: { width: '10%' }
    },
    {
      dataField: 'action',
      text: translations['amp.indicatormanager:table-header-action'],
      headerStyle: { width: '30%' },
      csvExport: false,
      formatter: (_cell: any, row: any) => (
        <Row
          sm={8}
          className={styles.action_wrapper}>
          <div className={styles.action_container}>
            <i
              style={{ fontSize: 20, color: '#007bff' }}
              className="fa fa-eye"
              aria-hidden="true"
              onClick={() => viewIndicatorModalHandler(row)} />
          </div>
          <div className={styles.action_container}>
            <i
              style={{ fontSize: 20, color: '#198754' }}
              className="fa fa-pencil"
              aria-hidden="true"
              onClick={() => editIndicatorModalHandler(row)} />
          </div>
          <div className={styles.action_container}>
            <i
              style={{ fontSize: 20, color: '#dc3545' }}
              className="fa fa-trash"
              aria-hidden="true"
              onClick={() => deleteIndicatorModalHandler(row)}
            />
          </div>
        </Row>
      ),
    },

  ], []);

  // Filtering logic for all fields
  useEffect(() => {
    let filtered = fetchedIndicators;
    if (Number(selectedSector) !== 0) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.sectors.includes(Number(selectedSector)));
    }
    if (Number(selectedProgram) !== 0) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.programId === Number(selectedProgram));
    }
    if (Number(selectedOutcome) !== 0) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.outcomeId === Number(selectedOutcome));
    }
    if (Number(selectedOutput) !== 0) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.outputId === Number(selectedOutput));
    }
    if (Number(selectedIndicatorType) !== 0) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.indicatorType === Number(selectedIndicatorType));
    }

    if (indicatorCode) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.code?.toLowerCase().includes(indicatorCode.toLowerCase()));
    }
    if (indicatorName) {
      filtered = filtered.filter((indicator: IndicatorObjectType) => indicator.name?.toLowerCase().includes(indicatorName.toLowerCase()));
    }
    setIndicators(filtered);
  }, [fetchedIndicators, selectedSector, selectedProgram, selectedOutcome, selectedOutput, selectedIndicatorType]);

  return (
    <>
      {showViewIndicatorModal &&
        <ViewIndicatorModal
          show={showViewIndicatorModal}
          setShow={setShowViewIndicatorModal}
          indicator={selectedRow}
          translations={translations}
        />
      }

      {showEditIndicatorModal &&
        <EditIndicatorModal
          show={showEditIndicatorModal}
          setShow={setShowEditIndicatorModal}
          indicator={selectedRow}
          translations={translations}
          filterBySector={globalSettings["indicator-filter-by-sector"]}
          filterByProgram={globalSettings["indicator-filter-by-program"]}
        />
      }


      {showDeleteIndicatorModal &&
        <DeleteIndicatorModal
          show={showDeleteIndicatorModal}
          setShow={setShowDeleteIndicatorModal}
          indicator={selectedRow}
          translations={translations}
        />
      }

      {
        loading ? <Loading /> :
          <SkeletonTable
            title={translations['amp.indicatormanager:table-title']}
            data={indicators}
            columns={columns}
            sectors={sectorsReducer.sectors}
            programs={programsReducer.programs as ProgramObjectType[]}
            setSelectedSector={setSelectedSector}
            setSelectedProgram={setSelectedProgram}
            translations={translations}
            filterBySector={globalSettings["indicator-filter-by-sector"]}
            filterByProgram={globalSettings["indicator-filter-by-program"]}
            setSelectedOutcome={setSelectedOutcome}
            setSelectedOutput={setSelectedOutput}
            setSelectedIndicatorType={setSelectedIndicatorType}
          />
      }
    </>
  );
};

const IndicatorTableMemo = React.memo(IndicatorTable);

const mapStateToProps = (state: any) => ({
  translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(IndicatorTableMemo);
