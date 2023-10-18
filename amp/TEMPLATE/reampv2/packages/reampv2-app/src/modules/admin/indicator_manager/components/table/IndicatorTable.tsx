/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import React, { useState, useMemo, useLayoutEffect, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Row } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
// @ts-ignore
import { ColumnDescription } from '@musicstory/react-bootstrap-table-next';
import SkeletonTable from './Table';
import styles from './IndicatorTable.module.css';
import { DefaultComponentProps, IndicatorObjectType } from '../../types';

import { getIndicators } from '../../reducers/fetchIndicatorsReducer';

// Modals
import ViewIndicatorModal from '../modals/ViewIndicatorModal';
import EditIndicatorModal from '../modals/EditIndicatorModal';
import DeleteIndicatorModal from '../modals/DeleteIndicatorModal';
import { Loading } from '../../../../../utils/components/Loading';

interface IndicatorTableProps extends DefaultComponentProps {
}

const IndicatorTable: React.FC<IndicatorTableProps> = ({ translations }) => {
  const dispatch = useDispatch();
  const { indicators: fetchedIndicators, loading } = useSelector((state: any) => state.fetchIndicatorsReducer);
  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);

  useLayoutEffect(() => {
    dispatch(getIndicators());
  }, []);


  const [selectedRow, setSelectedRow] = useState<any>(null);
  const [showViewIndicatorModal, setShowViewIndicatorModal] = useState<boolean>(false);
  const [showEditIndicatorModal, setShowEditIndicatorModal] = useState<boolean>(false);
  const [showDeleteIndicatorModal, setShowDeleteIndicatorModal] = useState<boolean>(false);
  const [selectedSector, setSelectedSector] = useState(0);
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
    {
      dataField: 'sectors',
      text: translations['amp.indicatormanager:sectors'],
      sort: true,
      headerStyle: { width: '30%' },
      formatter: (_cell: any, row: any) => {
        return (
          <div>
            {
              _cell.map((sectorId: any) => {
                const foundSector = !sectorsReducer.loading && sectorsReducer.sectors.find((sector: any) => sector.id === sectorId);

                if (foundSector) {
                  return <span key={sectorId}>{foundSector.name}
                    <br />

                  </span>
                }

                return (
                  <span key={sectorId}>
                    {sectorId}
                    <br />
                  </span>
                )
              })
            }
          </div>
        )
      },
    },
    {
      dataField: 'creationDate',
      text: translations['amp.indicatormanager:table-header-creation-date'],
      sort: true,
      headerStyle: { width: '10%' },
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

  const handleFilterIndicators = () => {
    if (Number(selectedSector) === 0) {
      setIndicators(fetchedIndicators);
      return;
    }
    const filteredIndicators = fetchedIndicators.filter((indicator: IndicatorObjectType) => {
      return indicator.sectors.includes(Number(selectedSector));
    });
    setIndicators([]);
    setIndicators(filteredIndicators);
  }

  useEffect(() => {
    handleFilterIndicators();
  }, [selectedSector])

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
            setSelectedSector={setSelectedSector}
            translations={translations}
          />
      }
    </>
  );
};

const InidcatorTableMemo = React.memo(IndicatorTable);

const mapStateToProps = (state: any) => ({
  translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(InidcatorTableMemo);
