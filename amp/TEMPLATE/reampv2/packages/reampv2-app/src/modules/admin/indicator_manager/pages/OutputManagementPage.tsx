import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'react-bootstrap';
import BootstrapTable, {PaginationOptions} from '@musicstory/react-bootstrap-table-next';
import paginationFactory from '@musicstory/react-bootstrap-table2-paginator';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from '@murasoftware/react-bootstrap-table2-toolkit';
import '@musicstory/react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import styles from '../components/table/Table.module.css';
import action_style from '../components/table/IndicatorTable.module.css';
import OutputModal from '../components/modals/OutputModal';
import Swal from 'sweetalert2';
import { useNavigate } from "react-router-dom";
import initialTranslations from '../config/initialTranslations.json';
import {useDispatch, useSelector} from "react-redux";
import {getOutputs} from "../reducers/fetchOutputsReducer";

interface Outcome {
  // Define the properties of Outcome based on your API response
  id: number;
  name: string;
}

interface Output {
  id: number;
  name: string;
  description?: string;
  outcomes?: Outcome[];
}

const OutputManagementPage: React.FC = () => {
  const navigate = useNavigate();
  const [showAddNewOutputModal, setShowAddNewOutputModal] = useState(false);
  const [showEditOutputModal, setShowEditOutputModal] = useState(false);
  const [editingOutput, setEditingOutput] = useState<Output | null>(null);
  const [loadingEditOutput, setLoadingEditOutput] = useState(false);
  const outputs = useSelector((state: any) => state.fetchOutputsReducer).outputs;
  const outcomes = useSelector((state: any) => state.fetchOutcomesReducer).outcomes;
  const dispatch = useDispatch();

  const translations = initialTranslations;

  const { SearchBar } = Search;
  const { ExportCSVButton } = CSVExport;

  const outputColumns = [
    {
      dataField: 'name',
      text: translations['amp.outcomeoutput:output-name'],
    },
    {
      dataField: 'actions',
      text: translations['amp.outcomeoutput:actions'],
      formatter: (_: any, row: Output) => (
        <>
          <div className={action_style.action_container}
          >
            <i
               style={{ fontSize: 20, color: '#198754' }}
               className="fa fa-pencil"
               aria-hidden="true"
               onClick={() => handleEditOutput(row)}
            />
          </div>
          {' '}
          <div className={action_style.action_container}
          >
            <i className="fa fa-trash"
               style={{ fontSize: 20, color: '#dc3545' }}
               aria-hidden="true"
               onClick={() => handleDeleteOutput(row)}
            />
          </div>
        </>
      ),
      headerStyle: { width: '160px' },
      align: 'center',
    },
  ];

  const paginationOptions: PaginationOptions = {
    paginationSize: 4,
    pageStartIndex: 1,
    alwaysShowAllBtns: true,
    sizePerPageList: [
      { text: '10', value: 10 },
      { text: '25', value: 25 },
      { text: '50', value: 50 },
      { text: 'All', value: outcomes.length }
    ],
    sizePerPage: 10
  };
  const handleAddOutput = async (output: { name: string; description?: string; outcomeIds: number[] }) => {

    try {
      const res = await fetch('/rest/amp-outcome-output/output', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(output)
      });
      if (res.ok) {
        dispatch(getOutputs());
      } else {
        alert(translations['amp.outcomeoutput:add-output-failed']);
      }
    } catch (e) {
      alert(translations['amp.outcomeoutput:error-adding-output']);
    }
  };

  const handleEditOutput = async (output: Output) => {
    setLoadingEditOutput(true);
    try {
      const res = await fetch(`/rest/amp-outcome-output/output/${output.id}`);
      if (res.ok) {
        const data = await res.json();
        setEditingOutput({
          id: data.id,
          name: data.name,
          description: data.description,
          outcomes: data.outcomes
        });
        setShowEditOutputModal(true);
      } else {
        alert(translations['amp.outcomeoutput:fetch-output-details-failed']);
      }
    } catch (e) {
      alert(translations['amp.outcomeoutput:error-fetching-output-details']);
    }
    setLoadingEditOutput(false);
  };

  const handleSaveEditedOutput = async (updated: { name: string; description?: string; outcomeIds: number[] }) => {
    if (!editingOutput) return;

    try {
      const res = await fetch(`/rest/amp-outcome-output/output/${editingOutput.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updated)
      });
      if (res.ok) {
        dispatch(getOutputs());
      } else {
        alert(translations['amp.outcomeoutput:update-output-failed']);
      }
    } catch (e) {
      alert(translations['amp.outcomeoutput:error-updating-output']);
    }
    setShowEditOutputModal(false);
    setEditingOutput(null);
  };

  const handleDeleteOutput = async (output: Output) => {
    const confirm = await Swal.fire({
      icon: 'warning',
      title: translations['amp.outcomeoutput:delete-output'],
      html: `<div>${translations['amp.outcomeoutput:delete-output-confirm']} <b>${output.name}</b>?<br/>${translations['amp.outcomeoutput:delete-output-warning']}</div>`,
      showCancelButton: true,
      confirmButtonText: translations['amp.outcomeoutput:delete'],
      cancelButtonText: translations['amp.outcomeoutput:cancel'],
    });
    if (confirm.isConfirmed) {
      try {
        const res = await fetch(`/rest/amp-outcome-output/output/delete/${output.id}`, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' }
        });
        if (res.ok) {
          dispatch(getOutputs())
        } else {
          const error = await res.json();
          let errorMsg = translations['amp.outcomeoutput:error-deleting-output'];
          if (error && error.error) {
            const firstKey = Object.keys(error.error)[0];
            if (firstKey && error.error[firstKey] && error.error[firstKey][0]) {
              errorMsg = error.error[firstKey][0];
            }
          }
          if (errorMsg.includes('orphan')) {
            const forceConfirm = await Swal.fire({
              icon: 'warning',
              title: translations['amp.outcomeoutput:indicators-linked'],
              html: `${errorMsg}<br/><br/>${translations['amp.outcomeoutput:proceed-orphan-indicators']}`,
              showCancelButton: true,
              confirmButtonText: translations['amp.outcomeoutput:yes-delete-anyway'],
              cancelButtonText: translations['amp.outcomeoutput:cancel'],
            });
            if (forceConfirm.isConfirmed) {
              try {
                const forceRes = await fetch(`/rest/amp-outcome-output/output/delete/${output.id}?forceDelete=true`, {
                  method: 'DELETE',
                  headers: { 'Content-Type': 'application/json' }
                });
                if (forceRes.ok) {
                  dispatch(getOutputs())
                } else {
                  const forceError = await forceRes.json();
                  let forceErrorMsg = translations['amp.outcomeoutput:error-deleting-output'];
                  if (forceError && forceError.error) {
                    const firstKey = Object.keys(forceError.error)[0];
                    if (firstKey && forceError.error[firstKey] && forceError.error[firstKey][0]) {
                      forceErrorMsg = forceError.error[firstKey][0];
                    }
                  }
                  await Swal.fire({
                    icon: 'error',
                    title: translations['amp.outcomeoutput:cannot-delete-output'],
                    html: forceErrorMsg
                  });
                }
              } catch (e) {
                await Swal.fire({
                  icon: 'error',
                  title: translations['amp.outcomeoutput:error-deleting-output'],
                  text: translations['amp.outcomeoutput:unexpected-error']
                });
              }
            }
          } else {
            await Swal.fire({
              icon: 'error',
              title: translations['amp.outcomeoutput:cannot-delete-output'],
              html: errorMsg
            });
          }
        }
      } catch (e) {
        await Swal.fire({
          icon: 'error',
          title: translations['amp.outcomeoutput:error-deleting-output'],
          text: translations['amp.outcomeoutput:unexpected-error']
        });
      }
    }
  };

  return (
    <>
      <OutputModal
        show={showAddNewOutputModal}
        setShow={setShowAddNewOutputModal}
        onSubmit={handleAddOutput}
        outcomes={outcomes}
        translations={{}}
      />
      <OutputModal
        show={showEditOutputModal}
        setShow={setShowEditOutputModal}
        onSubmit={handleSaveEditedOutput}
        initialName={editingOutput?.name || ''}
        initialDescription={editingOutput?.description || ''}
        initialOutcomes={editingOutput?.outcomes || []}
        outcomes={outcomes}
        translations={{}}
        loading={loadingEditOutput}
      />
      <Col sm={12}>
        <Row className={styles.table_header}>
          <Col sm={3}>
            <h3>{translations['amp.outcomeoutput:output-management']}</h3>
          </Col>
          <Col sm={6}>
            <Button variant="secondary" onClick={() => navigate('/admin/indicator_manager/outcome-output-management')} style={{ float: 'right', marginLeft: '10px' }}>
              <i className="fa fa-arrow-left" /> {translations['amp.outcomeoutput:back']}
            </Button>

          </Col>
        </Row>
        <ToolkitProvider
          keyField="id"
          data={outputs}
          columns={outputColumns}
          search
          exportCSV
        >
          {(toolkitProps: ToolkitContextType) => (
            <div>
              <Row sm={12} className={styles.table_header_bottom}>
                <Col sm={3}>
                  <Button variant="primary" onClick={() => setShowAddNewOutputModal(true)} >
                    <i className="fa fa-plus" /> {translations['amp.outcomeoutput:add-new-output']}
                  </Button>
                </Col>
                <Col sm={4}>
                  <div className={styles.table_header_bottom_left}>
                    <ExportCSVButton {...toolkitProps.csvProps} className={styles.export_button}>
                      <i className="fa fa-download" /> {translations['amp.outcomeoutput:export-csv']}
                    </ExportCSVButton>
                  </div>
                </Col>
                <Col sm={8}>
                  <div className={styles.table_header_bottom_right}>
                    <div className={styles.search_container}>
                      <SearchBar
                        {...toolkitProps.searchProps}
                        placeholder={translations['amp.outcomeoutput:search-placeholder']}
                        columns={[{ dataField: 'name', text: 'Name' }]}
                      />
                    </div>
                  </div>
                </Col>
              </Row>
              <hr />
              <BootstrapTable
                {...toolkitProps.baseProps}
                bordered={false}
                headerClasses={styles.table_header_titles}
                bodyClasses={styles.table_body}
                pagination={paginationFactory(paginationOptions)}
                noDataIndication={() => (
                  <div className={styles.no_data}>
                    <h5>{translations['amp.indicatormanager:no-data']}</h5>
                  </div>
                )}
              />
            </div>
          )}
        </ToolkitProvider>
      </Col>
    </>
  );
};

export default OutputManagementPage;
