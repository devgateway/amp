import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'react-bootstrap';
import BootstrapTable, { PaginationOptions } from '@musicstory/react-bootstrap-table-next';
import '@musicstory/react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import styles from '../components/table/Table.module.css';
import OutcomeModal from '../components/modals/OutcomeModal';
import OutputModal from '../components/modals/OutputModal';
import action_style from '../components/table/IndicatorTable.module.css';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from '@murasoftware/react-bootstrap-table2-toolkit';
import paginationFactory from '@musicstory/react-bootstrap-table2-paginator';
import initialTranslations from '../config/initialTranslations.json';
import './css/ModalZIndexFix.css'; // Add z-index to modal and backdrop to ensure visibility
import Swal from 'sweetalert2';
import {useNavigate} from "react-router-dom";
import {useSelector, useDispatch} from "react-redux";
import {getOutcomes} from "../reducers/fetchOutcomesReducer";

interface Outcome {
  id: number;
  name: string;
  description?: string; // Optional description for Outcome
  outputs: Output[];
}

interface Output {
  id: number;
  name: string;
  description?: string; // Optional description for Output
}

const translations = initialTranslations;

const OutcomeOutputManagementPage: React.FC = () => {
  const [showAddNewOutcomeModal, setShowAddNewOutcomeModal] = useState(false);
  const [showEditOutcomeModal, setShowEditOutcomeModal] = useState(false);
  const [showOutputModal, setShowOutputModal] = useState(false);
  const [editingOutcome, setEditingOutcome] = useState<Outcome | null>(null);
  const [selectedOutcome, setSelectedOutcome] = useState<Outcome | null>(null);
  const [editingOutput, setEditingOutput] = useState<Output | null>(null);
  const outcomes = useSelector((state: any) => state.fetchOutcomesReducer).outcomes;
  const dispatch = useDispatch();

  const navigate = useNavigate();

  // Fetch outcomes on initial mount
  useEffect(() => {
    dispatch(getOutcomes());
  }, [dispatch]);

  const columns = [
    {
      dataField: 'name',
      text: translations['amp.outcomeoutput:outcome-name'],
    },
    {
      dataField: 'actions',
      text: translations['amp.outcomeoutput:actions'],
      formatter: (_: any, row: Outcome) => (
        <>
          <div className={action_style.action_container}
          >
            <i
               onClick={() => handleEditOutcome(row)}
               style={{ fontSize: 20, color: '#198754' }}
               className="fa fa-pencil"
               aria-hidden="true"
            />
          </div>
          <div className={action_style.action_container}
          >
            <i className="fa fa-trash"
               style={{ fontSize: 20, color: '#dc3545' }}
               aria-hidden="true"
               onClick={() => handleDeleteOutcome(row)}

            />
          </div>
        </>
      ),
      headerStyle: { width: '160px' },
      align: 'center',
    },
  ];



  const handleAddOutcome = async (outcome: { name: string; description?: string }) => {
    try {
      const res = await fetch('/rest/amp-outcome-output/outcome', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(outcome)
      });
      if (res.ok) {
        dispatch(getOutcomes());
      } else {
        alert(translations['amp.outcomeoutput:add-outcome-failed']);
      }
    } catch (e) {
      console.error('Error adding outcome', e);
      alert(translations['amp.outcomeoutput:error-adding-outcome']);
    }
  };



  const handleEditOutcome = (outcome: Outcome) => {
    setEditingOutcome(outcome);
    setShowEditOutcomeModal(true);
  };

  const handleSaveEditedOutcome = async (updated: { name: string; description?: string }) => {
    if (!editingOutcome) return;
    try {
      const res = await fetch(`/rest/amp-outcome-output/outcome/${editingOutcome.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updated)
      });
      if (res.ok) {
        dispatch(getOutcomes());
      } else {
        alert(translations['amp.outcomeoutput:update-outcome-failed']);
      }
    } catch (e) {
      console.error('Error updating outcome', e);
      alert(translations['amp.outcomeoutput:error-updating-outcome']);
    }
    setShowEditOutcomeModal(false);
    setEditingOutcome(null);
  };




  const handleDeleteOutcome = async (outcome: Outcome) => {
    // Call backend delete endpoint directly, and display any error/warning returned
    const confirm = await Swal.fire({
      icon: 'warning',
      title: translations['amp.outcomeoutput:delete-outcome'],
      html: `<div>${translations['amp.outcomeoutput:delete-outcome-confirm']} <b>${outcome.name}</b>?<br/>${translations['amp.outcomeoutput:delete-output-warning']}</div>`,
      showCancelButton: true,
      confirmButtonText: translations['amp.outcomeoutput:delete'],
      cancelButtonText: translations['amp.outcomeoutput:cancel'],
    });
    if (confirm.isConfirmed) {
      try {
        const res = await fetch(`/rest/amp-outcome-output/outcome/delete/${outcome.id}`, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' }
        });
        if (res.ok) {
          dispatch(getOutcomes());
        } else {
          // Show backend error message (alerts/warnings)
          const error = await res.json();
          let errorMsg = translations['amp.outcomeoutput:error-deleting-outcome'];
          if (error && error.error) {
            const firstKey = Object.keys(error.error)[0];
            if (firstKey && error.error[firstKey] && error.error[firstKey][0]) {
              errorMsg = error.error[firstKey][0];
            }
          }
          await Swal.fire({
            icon: 'error',
            title: translations['amp.outcomeoutput:cannot-delete-outcome'],
            html: errorMsg
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
  };

  const handleAddOutput = (outcomeId: number) => {
    const outcome = outcomes.find((o: Outcome) => o.id === outcomeId);
    setSelectedOutcome(outcome || null);
    setEditingOutput(null);
    setShowOutputModal(true);
  };

  const handleEditOutput = (output: Output, outcome: Outcome) => {
    setSelectedOutcome(outcome);
    setEditingOutput(output);
    setShowOutputModal(true);
  };

  const handleSaveOutput = async (outputData: { name: string; description?: string }) => {
    if (editingOutput && selectedOutcome) {
      // Edit existing output
      try {
        const res = await fetch(`/rest/amp-outcome-output/output/${editingOutput.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ...outputData,
            outcomeId: selectedOutcome.id
          })
        });
        if (res.ok) {
          dispatch(getOutcomes());
        } else {
          alert(translations['amp.outcomeoutput:update-output-failed']);
        }
      } catch (e) {
        console.error('Error updating output', e);
        alert(translations['amp.outcomeoutput:error-updating-output']);
      }
    } else if (selectedOutcome) {
      // Add new output
      try {
        const res = await fetch('/rest/amp-outcome-output/output', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ...outputData,
            outcomeId: selectedOutcome.id
          })
        });
        if (res.ok) {
          dispatch(getOutcomes());
        } else {
          alert(translations['amp.outcomeoutput:add-output-failed']);
        }
      } catch (e) {
        console.error('Error adding output', e);
        alert(translations['amp.outcomeoutput:error-adding-output']);
      }
    }
    setShowOutputModal(false);
    setEditingOutput(null);
    setSelectedOutcome(null);
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
                    dispatch(getOutcomes())
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
                                    dispatch(getOutcomes())
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

  const expandRow = {
    renderer: (row: Outcome) => (
      <div style={{ marginLeft: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <strong>{row.name}</strong>
          <Button
            variant="outline-success"
            size="sm"
            style={{ marginLeft: '8px' }}
            onClick={() => handleAddOutput(row.id)}
            title={translations['amp.outcomeoutput:add-outputs']}
            onMouseOver={e => {
              e.currentTarget.setAttribute('data-bs-toggle', 'tooltip');
              e.currentTarget.setAttribute('data-bs-placement', 'top');
            }}
          >
            <i className="fa fa-plus" />
          </Button>
        </div>
        {row.description && (
          <div style={{ fontStyle: 'italic', marginBottom: '0.5rem' }}>
            {row.description}
          </div>
        )}
        <strong>{translations['amp.outcomeoutput:outputs']}:</strong>
        <table style={{ marginLeft: '1.5rem', width: '100%' }}>
          <thead>
            <tr>
              <th>{translations['amp.outcomeoutput:name']}</th>
              <th>{translations['amp.outcomeoutput:description']}</th>
              <th>{translations['amp.outcomeoutput:actions']}</th>
            </tr>
          </thead>
          <tbody>
            {row.outputs && row.outputs.length > 0 ? row.outputs.map((output: Output) => (
              <tr key={output.id}>
                <td>{output.name}</td>
                <td>{output.description || ''}</td>
                <td>
                  <Button
                    variant="outline-success"
                    size="sm"
                    style={{ marginRight: '6px' }}
                    onClick={() => handleEditOutput(output, row)}
                    title={translations['amp.outcomeoutput:edit-output']}
                  >
                    <i className="fa fa-pencil" />
                  </Button>
                  <Button
                    variant="outline-danger"
                    size="sm"
                    onClick={() => handleDeleteOutput(output)}
                    title={translations['amp.outcomeoutput:delete-output']}
                  >
                    <i className="fa fa-trash" />
                  </Button>
                </td>
              </tr>
            )) : <tr><td colSpan={3}>{translations['amp.outcomeoutput:no-outputs']}</td></tr>}
          </tbody>
        </table>
      </div>
    ),
    showExpandColumn: true,
    expandByColumnOnly: true,
  };

  const { SearchBar } = Search;
  const { ExportCSVButton } = CSVExport;

  const paginationOptions: PaginationOptions = {
    paginationSize: 4,
    pageStartIndex: 1,
    alwaysShowAllBtns: true,
    sizePerPageList: [
      { text: '10', value: 10 },
      { text: '25', value: 25 },
      { text: '50', value: 50 },
      { text: translations['amp.indicatormanager:all'], value: outcomes.length }
    ],
    sizePerPage: 10
  };

  return (
    <>
      <OutcomeModal
        show={showAddNewOutcomeModal}
        setShow={setShowAddNewOutcomeModal}
        onSubmit={handleAddOutcome}
        translations={translations}
      />
      <OutcomeModal
        show={showEditOutcomeModal}
        setShow={setShowEditOutcomeModal}
        onSubmit={handleSaveEditedOutcome}
        initialName={editingOutcome?.name || ''}
        initialDescription={editingOutcome?.description || ''}
        translations={translations}
      />
      <OutputModal
        show={showOutputModal}
        setShow={setShowOutputModal}
        selectedOutcome={selectedOutcome ? selectedOutcome : undefined}
        initialName={editingOutput?.name || ''}
        initialDescription={editingOutput?.description || ''}
        onSubmit={handleSaveOutput}
        translations={translations}
      />
      <Col sm={12}>
        <Row className={styles.table_header}>
          <Col sm={6}>
            <h3>{translations['amp.outcomeoutput:outcome-management']}</h3>
          </Col>
          <Col sm={6}>
          </Col>
        </Row>
        <ToolkitProvider
          keyField="id"
          data={outcomes}
          columns={columns}
          search
          exportCSV
        >
          {(props: ToolkitContextType) => (
            <div>
              <Row sm={12} className={styles.table_header_bottom}>
                <Col sm={4}>
                  <div className={styles.table_header_bottom_left}>
                    {' '}
                    <Button variant="primary" onClick={() => setShowAddNewOutcomeModal(true)}>
                      <i className="fa fa-plus" /> {translations['amp.outcomeoutput:add-new-outcome']}
                    </Button>
                    {' '}
                    <ExportCSVButton {...props.csvProps} className={styles.export_button}>
                      <i className="fa fa-download" /> {translations['amp.outcomeoutput:export-csv']}
                    </ExportCSVButton>
                  </div>
                </Col>
                <Col sm={8}>
                    <div className={styles.search_container}>
                      <SearchBar {...props.searchProps} placeholder={translations['amp.outcomeoutput:search-placeholder']} />
                    </div>
                  <Button variant="secondary" onClick={() => navigate('/admin/indicator_manager')} style={{ float: 'right', marginLeft: '10px' }}>
                    <i className="fa fa-arrow-left" /> {translations['amp.outcomeoutput:back']}
                  </Button>
                </Col>
              </Row>
              <hr />
              <BootstrapTable
                {...props.baseProps}
                expandRow={expandRow}
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

export default OutcomeOutputManagementPage;
