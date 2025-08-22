import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'react-bootstrap';
import BootstrapTable, { PaginationOptions } from '@musicstory/react-bootstrap-table-next';
import '@musicstory/react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import styles from '../components/table/Table.module.css';
import OutcomeModal from '../components/modals/OutcomeModal';
import OutputModal from '../components/modals/OutputModal';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from '@murasoftware/react-bootstrap-table2-toolkit';
import paginationFactory from '@musicstory/react-bootstrap-table2-paginator';
import initialTranslations from '../config/initialTranslations.json';
import './css/ModalZIndexFix.css'; // Add z-index to modal and backdrop to ensure visibility

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
  const [editingOutput, setEditingOutput] = useState<Output & { outcomeIds?: number[] } | null>(null);
  const [outcomes, setOutcomes] = useState<Outcome[]>([]);

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
            Edit
          </Button>
          {' '}
          <Button
            size="sm"
            variant="outline-danger"
            onClick={() => handleDeleteOutcome(row)}
          >
            Delete
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

  const handleEditOutput = (output: Output, parentOutcomeIds: number[]) => {
    setEditingOutput({ ...output, outcomeIds: parentOutcomeIds });
    setShowEditOutputModal(true);
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

  const handleDeleteOutcome = (outcome: Outcome) => {
    // TODO: Implement delete logic (e.g., update state or call backend)
    if (window.confirm(`Are you sure you want to delete outcome: ${outcome.name}?`)) {
      console.log('Delete Outcome:', outcome);
      // Implement actual delete logic here
    }
  };

  // For linking outputs, pass only id and name of outcomes
  const outcomeOptions = outcomes.map(o => ({ id: o.id, name: o.name }));

  const expandRow = {
    renderer: (row: Outcome) => (
      <div style={{ marginLeft: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <strong>{row.name}</strong>
          <Button
            size="sm"
            variant="outline-primary"
            style={{ marginLeft: '1rem' }}
            onClick={() => handleEditOutcome(row)}
          >
            Edit
          </Button>
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
                <Button
                  size="sm"
                  variant="outline-primary"
                  style={{ marginLeft: '1rem' }}
                  onClick={() => handleEditOutput(output, [row.id])}
                >
                  Edit
                </Button>
                <Button
                  size="sm"
                  variant="outline-danger"
                  style={{ marginLeft: '0.5rem' }}
                  onClick={() => {/* TODO: Implement delete output logic */}}
                >
                  Delete
                </Button>
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
        initialOutcomeIds={editingOutput?.outcomeIds || []}
        translations={translations}
      />
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
                    <Button type="button" variant="primary" onClick={() => setShowAddNewOutcomeModal(true)}>
                      <i className="fa fa-plus" /> {translations['amp.outcomeoutput:add-new-outcome']}
                    </Button>
                    {' '}
                    <Button type="button" variant="primary" onClick={() => setShowAddNewOutputModal(true)}>
                      <i className="fa fa-plus" /> {translations['amp.outcomeoutput:add-new-output']}
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
              />
            </div>
          )}
        </ToolkitProvider>
      </Col >
    </>
  );
};

export default OutcomeOutputManagementPage;
