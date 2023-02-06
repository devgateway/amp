/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import React, { useState, useMemo, useEffect } from 'react';
import { Row } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { ColumnDescription } from 'react-bootstrap-table-next';
import SkeletonTable from './Table';
import styles from './IndicatorTable.module.css';
import { DefaultComponentProps } from '../../types';
import sampleData from './test_data.json';

// Modals
import ViewIndicatorModal from '../modals/ViewIndicatorModal';
import EditIndicatorModal from '../modals/EditIndicatorModal';
import DeleteIndicatorModal from '../modals/DeleteIndicatorModal';

interface IndicatorTableProps extends DefaultComponentProps {
}

const IndicatorTable: React.FC<IndicatorTableProps> = ({ translations }) => {
  const [selectedRow, setSelectedRow] = useState<any>(null);
  const [showViewIndicatorModal, setShowViewIndicatorModal] = useState<boolean>(false);
  const [showEditIndicatorModal, setShowEditIndicatorModal] = useState<boolean>(false);
  const [showDeleteIndicatorModal, setShowDeleteIndicatorModal] = useState<boolean>(false);

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
      text: 'ID',
      sort: true,
      headerStyle: { width: '10%' },
    },
    {
      dataField: 'indicatorName',
      text: 'Indicator Name',
      sort: true,
      headerStyle: { width: '50%' },
    },
    {
      dataField: 'sector',
      text: 'Sector',
      sort: true,
      headerStyle: { width: '30%' },
    },
    {
      dataField: 'action',
      text: 'Action',
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

  const [inidcatorsData, setIndicatorsData] = useState<any>(useMemo(() => [], []));

  const fetchData = async () => {
    setIndicatorsData(sampleData);
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <>
      <ViewIndicatorModal
        show={showViewIndicatorModal}
        setShow={setShowViewIndicatorModal}
        indicator={selectedRow}
            />

      <EditIndicatorModal
        show={showEditIndicatorModal}
        setShow={setShowEditIndicatorModal}
        indicator={selectedRow}
            />
      
      <DeleteIndicatorModal
        show={showDeleteIndicatorModal}
        setShow={setShowDeleteIndicatorModal}
        indicator={selectedRow}
      />

      <SkeletonTable
        title={translations['amp.indicatormanager:table-title']}
        data={inidcatorsData}
        columns={columns}
            />
    </>
  );
};

const InidcatorTableMemo = React.memo(IndicatorTable);

const mapStateToProps = (state: any) => ({
  translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(InidcatorTableMemo);
