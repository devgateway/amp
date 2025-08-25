import React, { useState, useEffect } from 'react';
import { Col, Row, Button, Tabs, Tab } from 'react-bootstrap';
import BootstrapTable, { PaginationOptions } from '@musicstory/react-bootstrap-table-next';
import '@musicstory/react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import styles from '../components/table/Table.module.css';
import OutcomeModal from '../components/modals/OutcomeModal';
import OutputModal from '../components/modals/OutputModal';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from '@murasoftware/react-bootstrap-table2-toolkit';
import paginationFactory from '@musicstory/react-bootstrap-table2-paginator';
import initialTranslations from '../config/initialTranslations.json';
import './css/ModalZIndexFix.css'; // Add z-index to modal and backdrop to ensure visibility
import Swal from 'sweetalert2';
import 'bootstrap/dist/css/bootstrap.min.css';

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
  const [showAddNewOutputModal, setShowAddNewOutputModal] = useState(false);
  const [showEditOutputModal, setShowEditOutputModal] = useState(false);
  const [editingOutcome, setEditingOutcome] = useState<Outcome | null>(null);
  const [editingOutput, setEditingOutput] = useState<Output & { outcomes?: Outcome[] } | null>(null);
  const [loadingEditOutput, setLoadingEditOutput] = useState(false);
  const [outcomes, setOutcomes] = useState<Outcome[]>([]);
  const [outputs, setOutputs] = useState<Output[]>([]);
  const [activeTab, setActiveTab] = useState<string>('outcomes');

  useEffect(() => {
    console.log('Add Outcome Modal State:', showAddNewOutcomeModal);
  }, [showAddNewOutcomeModal]);

  useEffect(() => {
    console.log('Edit Outcome Modal State:', showAddNewOutputModal);
  }, [showAddNewOutputModal]);
  useEffect(() => {
    fetch('/rest/amp-outcome-output/outcomes')
      .then(res => res.json())
      .then(data => setOutcomes(data));
  }, []);

  useEffect(() => {
    fetch('/rest/amp-outcome-output/outputs')
      .then(res => res.json())
      .then(data => setOutputs(data));
  }, []);

  const columns = [
    {
      dataField: 'name',
      text: 'Outcome Name',
    },
    {
      dataField: 'actions',
      text: 'Actions',
      formatter: (_: any, row: Outcome) => (
        <>
          <Button
            size="sm"
            variant="outline-primary"
            onClick={() => handleEditOutcome(row)}
          >
            <i className="fa fa-edit" />
          </Button>
          {' '}
          <Button
            size="sm"
            variant="outline-danger"
            onClick={() => handleDeleteOutcome(row)}
          >
            <i className="fa fa-trash" />
          </Button>
        </>
      ),
      headerStyle: { width: '160px' },
      align: 'center',
    },
  ];

  const outputColumns = [
    {
      dataField: 'name',
      text: 'Output Name',
    },
    {
      dataField: 'actions',
      text: 'Actions',
      formatter: (_: any, row: Output) => (
        <>
          <Button
            size="sm"
            variant="outline-primary"
            onClick={() => handleEditOutput(row, [])}
          >
            <i className="fa fa-edit" />
          </Button>
          {' '}
          <Button
            size="sm"
            variant="outline-danger"
            onClick={() => handleDeleteOutput(row)}
          >
            <i className="fa fa-trash" />
          </Button>
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
        fetch('/rest/amp-outcome-output/outcomes')
          .then(res => res.json())
          .then(data => setOutcomes(data));
      } else {
        alert('Failed to add outcome');
      }
    } catch (e) {
      console.error('Error adding outcome', e);
      alert('Error adding outcome');
    }
  };

  const handleAddOutput = async (output: { name: string; description?: string; outcomeIds: number[] }) => {
    if (!output.outcomeIds || output.outcomeIds.length === 0) {
      alert('You must associate the output with at least one outcome.');
      return;
    }
    try {
      const res = await fetch('/rest/amp-outcome-output/output', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(output)
      });
      if (res.ok) {
        fetch('/rest/amp-outcome-output/outcomes')
          .then(res => res.json())
          .then(data => setOutcomes(data));
      } else {
        alert('Failed to add output');
      }
    } catch (e) {
      console.error('Error adding output', e);
      alert('Error adding output');
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
        fetch('/rest/amp-outcome-output/outcomes')
          .then(res => res.json())
          .then(data => setOutcomes(data));
      } else {
        alert('Failed to update outcome');
      }
    } catch (e) {
      console.error('Error updating outcome', e);
      alert('Error updating outcome');
    }
    setShowEditOutcomeModal(false);
    setEditingOutcome(null);
  };

  const handleEditOutput = async (output: Output, parentOutcomeIds: number[]) => {
    setLoadingEditOutput(true);
    try {
      const res = await fetch(`/rest/amp-outcome-output/output/${output.id}`);
      if (res.ok) {
        const data = await res.json();
        setEditingOutput({
          id: data.id,
          name: data.name,
          description: data.description,
          outcomes: data.outcomes || []
        });
        setShowEditOutputModal(true);
      } else {
        alert('Failed to fetch output details');
      }
    } catch (e) {
      alert('Error fetching output details');
    }
    setLoadingEditOutput(false);
  };

  const handleSaveEditedOutput = async (updated: { name: string; description?: string; outcomeIds: number[] }) => {
    if (!editingOutput) return;
    if (!updated.outcomeIds || updated.outcomeIds.length === 0) {
      alert('You must associate the output with at least one outcome.');
      return;
    }
    try {
      const res = await fetch(`/rest/amp-outcome-output/output/${editingOutput.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updated)
      });
      if (res.ok) {
        fetch('/rest/amp-outcome-output/outcomes')
          .then(res => res.json())
          .then(data => setOutcomes(data));
      } else {
        alert('Failed to update output');
      }
    } catch (e) {
      alert('Error updating output');
    }
    setShowEditOutputModal(false);
    setEditingOutput(null);
  };

  const handleDeleteOutcome = async (outcome: Outcome) => {
    // Call backend delete endpoint directly, and display any error/warning returned
    const confirm = await Swal.fire({
      icon: 'warning',
      title: 'Delete Outcome?',
      html: `<div>Are you sure you want to delete outcome: <b>${outcome.name}</b>?<br/>This action cannot be undone.</div>`,
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
    });
    if (confirm.isConfirmed) {
      try {
        const res = await fetch(`/rest/amp-outcome-output/outcome/delete/${outcome.id}`, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' }
        });
        if (res.ok) {
          fetch('/rest/amp-outcome-output/outcomes')
            .then(res => res.json())
            .then(data => setOutcomes(data));
        } else {
          // Show backend error message (alerts/warnings)
          const error = await res.json();
          let errorMsg = 'An error occurred while deleting the outcome.';
          if (error && error.error) {
            const firstKey = Object.keys(error.error)[0];
            if (firstKey && error.error[firstKey] && error.error[firstKey][0]) {
              errorMsg = error.error[firstKey][0];
            }
          }
          await Swal.fire({
            icon: 'error',
            title: 'Cannot Delete Outcome',
            html: errorMsg
          });
        }
      } catch (e) {
        await Swal.fire({
          icon: 'error',
          title: 'Error deleting outcome',
          text: 'An unexpected error occurred.'
        });
      }
    }
  };

  const handleDeleteOutput = async (output: Output) => {
    const confirm = await Swal.fire({
      icon: 'warning',
      title: 'Delete Output?',
      html: `<div>Are you sure you want to delete output: <b>${output.name}</b>?<br/>This action cannot be undone.</div>`,
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
    });
    if (confirm.isConfirmed) {
      try {
        const res = await fetch(`/rest/amp-outcome-output/output/delete/${output.id}`, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' }
        });
        if (res.ok) {
          fetch('/rest/amp-outcome-output/outputs')
            .then(res => res.json())
            .then(data => setOutputs(data));
        } else {
          const error = await res.json();
          let errorMsg = 'An error occurred while deleting the output.';
          if (error && error.error) {
            const firstKey = Object.keys(error.error)[0];
            if (firstKey && error.error[firstKey] && error.error[firstKey][0]) {
              errorMsg = error.error[firstKey][0];
            }
          }
          // If error message contains orphan warning, prompt for confirmation
          if (errorMsg.includes('orphan')) {
            const forceConfirm = await Swal.fire({
              icon: 'warning',
              title: 'Indicators Linked',
              html: `${errorMsg}<br/><br/>Do you want to proceed and orphan these indicators?`,
              showCancelButton: true,
              confirmButtonText: 'Yes, delete anyway',
              cancelButtonText: 'Cancel',
            });
            if (forceConfirm.isConfirmed) {
              try {
                const forceRes = await fetch(`/rest/amp-outcome-output/output/delete/${output.id}?forceDelete=true`, {
                  method: 'DELETE',
                  headers: { 'Content-Type': 'application/json' }
                });
                if (forceRes.ok) {
                  fetch('/rest/amp-outcome-output/outputs')
                    .then(res => res.json())
                    .then(data => setOutputs(data));
                } else {
                  const forceError = await forceRes.json();
                  let forceErrorMsg = 'An error occurred while deleting the output.';
                  if (forceError && forceError.error) {
                    const firstKey = Object.keys(forceError.error)[0];
                    if (firstKey && forceError.error[firstKey] && forceError.error[firstKey][0]) {
                      forceErrorMsg = forceError.error[firstKey][0];
                    }
                  }
                  await Swal.fire({
                    icon: 'error',
                    title: 'Cannot Delete Output',
                    html: forceErrorMsg
                  });
                }
              } catch (e) {
                await Swal.fire({
                  icon: 'error',
                  title: 'Error deleting output',
                  text: 'An unexpected error occurred.'
                });
              }
            }
          } else {
            await Swal.fire({
              icon: 'error',
              title: 'Cannot Delete Output',
              html: errorMsg
            });
          }
        }
      } catch (e) {
        await Swal.fire({
          icon: 'error',
          title: 'Error deleting output',
          text: 'An unexpected error occurred.'
        });
      }
    }
  };

  // For linking outputs, pass only id and name of outcomes
  const outcomeOptions = outcomes.map(o => ({ id: o.id, name: o.name }));

  const expandRow = {
    renderer: (row: Outcome) => (
      <div style={{ marginLeft: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <strong>{row.name}</strong>
        </div>
        {row.description && (
          <div style={{ fontStyle: 'italic', marginBottom: '0.5rem' }}>
            {row.description}
          </div>
        )}
        <strong>Outputs:</strong>
        <ul style={{ marginLeft: '1.5rem' }}>
          {row.outputs && row.outputs.length > 0 ? row.outputs.map((output: Output) => (
            <li key={output.id} style={{ marginBottom: '0.5rem' }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <strong>{output.name}</strong>
              </div>
              {output.description && (
                <div style={{ fontStyle: 'italic', marginBottom: '0.5rem', marginLeft: '1rem' }}>
                  {output.description}
                </div>
              )}
            </li>
          )) : <li>No outputs</li>}
        </ul>
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
      { text: 'All', value: outcomes.length }
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
        show={showAddNewOutputModal}
        setShow={setShowAddNewOutputModal}
        outcomes={outcomeOptions}
        onSubmit={handleAddOutput}
        translations={translations}
      />
      <OutputModal
        show={showEditOutputModal}
        setShow={setShowEditOutputModal}
        outcomes={outcomeOptions}
        onSubmit={handleSaveEditedOutput}
        initialName={editingOutput?.name || ''}
        initialDescription={editingOutput?.description || ''}
        initialOutcomes={editingOutput?.outcomes || []}
        translations={translations}
        loading={loadingEditOutput}
      />
      <Tabs activeKey={activeTab} onSelect={k => setActiveTab(k || 'outcomes')} className="mb-3">
        <Tab eventKey="outcomes" title={translations['amp.outcomeoutput:management-title'] || 'Manage Outcomes'}>
          <Col sm={12}>
            <ToolkitProvider
              keyField="id"
              data={outcomes}
              columns={columns}
              search
              exportCSV
            >
              {(props: ToolkitContextType) => (
                <div>
                  <Row className={styles.table_header}>
                    <Col sm={6}>
                      <h3>{translations['amp.outcomeoutput:management-title']}</h3>
                    </Col>
                    <Col sm={6}>
                      <hr />
                    </Col>
                  </Row>
                  <Row sm={12} className={styles.table_header_bottom}>
                    <Col sm={4}>
                      <div className={styles.table_header_bottom_left}>
                        <Button variant="primary" onClick={() => {console.log('Add Outcome Clicked'); setShowAddNewOutcomeModal(true);}}>
                          <i className="fa fa-plus" /> {translations['amp.outcomeoutput:add-new-outcome']}
                        </Button>
                        {' '}
                        <ExportCSVButton {...props.csvProps} className={styles.export_button}>
                          <i className="fa fa-download" /> {translations['amp.outcomeoutput:export-csv']}
                        </ExportCSVButton>
                      </div>
                    </Col>
                    <Col sm={8}>
                      <div className={styles.table_header_bottom_right}>
                        <div className={styles.search_container}>
                          <SearchBar {...props.searchProps} placeholder={translations['amp.outcomeoutput:search-placeholder']} />
                        </div>
                      </div>
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
        </Tab>
        <Tab eventKey="outputs" title={translations['amp.outcomeoutput:outputs-management-title'] || 'Manage Outputs'}>
          <Col sm={12}>
            <Button variant="primary" onClick={() => setShowAddNewOutputModal(true)} style={{ marginBottom: '1rem' }}>
              <i className="fa fa-plus" /> {translations['amp.outcomeoutput:add-new-output']}
            </Button>
            <BootstrapTable
              keyField="id"
              data={outputs}
              columns={outputColumns}
              bordered={false}
              headerClasses={styles.table_header_titles}
              bodyClasses={styles.table_body}
              noDataIndication={() => (
                <div className={styles.no_data}>
                  <h5>{translations['amp.indicatormanager:no-data']}</h5>
                </div>
              )}
            />
          </Col>
        </Tab>
      </Tabs>
    </>
  );
};

export default OutcomeOutputManagementPage;
