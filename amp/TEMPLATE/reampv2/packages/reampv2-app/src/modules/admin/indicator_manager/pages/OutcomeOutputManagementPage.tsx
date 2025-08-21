import React, { useState } from 'react';
import { Col, Row, Button } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import styles from '../components/table/Table.module.css';
import AddNewOutcomeModal from '../components/modals/AddNewOutcomeModal';
import AddNewOutputModal from '../components/modals/AddNewOutputModal';

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

const dummyOutcomes: Outcome[] = [
  {
    id: 1,
    name: 'Outcome 1',
    description: 'Description for Outcome 1',
    outputs: [
      { id: 101, name: 'Output A', description: 'Description for Output A' },
      { id: 102, name: 'Output B' }
    ]
  },
  {
    id: 2,
    name: 'Outcome 2',
    outputs: [
      { id: 201, name: 'Output C' }
    ]
  }
];



const OutcomeOutputManagementPage: React.FC = () => {
  const [showAddNewOutcomeModal, setShowAddNewOutcomeModal] = useState(false);
  const [showEditOutcomeModal, setShowEditOutcomeModal] = useState(false);
  const [showAddNewOutputModal, setShowAddNewOutputModal] = useState(false);
  const [showEditOutputModal, setShowEditOutputModal] = useState(false);
  const [editingOutcome, setEditingOutcome] = useState<Outcome | null>(null);
  const [editingOutput, setEditingOutput] = useState<Output & { outcomeIds?: number[] } | null>(null);


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

  const handleAddOutcome = (outcome: { name: string; description?: string }) => {
    // TODO: Add logic to save outcome
    // For now, just log
    console.log('New Outcome:', outcome);
  };

  const handleAddOutput = (output: { name: string; description?: string; outcomeIds: number[] }) => {
    if (!output.outcomeIds || output.outcomeIds.length === 0) {
      alert('You must associate the output with at least one outcome.');
      return;
    }
    // TODO: Add logic to save output
    // For now, just log
    console.log('New Output:', output);
  };

  const handleEditOutcome = (outcome: Outcome) => {
    setEditingOutcome(outcome);
    setShowEditOutcomeModal(true);
  };

  const handleSaveEditedOutcome = (updated: { name: string; description?: string }) => {
    // TODO: Update outcome in state/backend
    console.log('Edited Outcome:', { ...editingOutcome, ...updated });
    setShowEditOutcomeModal(false);
    setEditingOutcome(null);
  };

  const handleEditOutput = (output: Output, parentOutcomeIds: number[]) => {
    setEditingOutput({ ...output, outcomeIds: parentOutcomeIds });
    setShowEditOutputModal(true);
  };

  const handleSaveEditedOutput = (updated: { name: string; description?: string; outcomeIds: number[] }) => {
    if (!updated.outcomeIds || updated.outcomeIds.length === 0) {
      alert('You must associate the output with at least one outcome.');
      return;
    }
    // TODO: Update output in state/backend
    console.log('Edited Output:', { ...editingOutput, ...updated });
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
  const outcomeOptions = dummyOutcomes.map(o => ({ id: o.id, name: o.name }));

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

  return (
    <Col sm={12}>
      <AddNewOutcomeModal
        show={showAddNewOutcomeModal}
        setShow={setShowAddNewOutcomeModal}
        onSubmit={handleAddOutcome}
      />
      <AddNewOutcomeModal
        show={showEditOutcomeModal}
        setShow={setShowEditOutcomeModal}
        onSubmit={handleSaveEditedOutcome}
        initialName={editingOutcome?.name || ''}
        initialDescription={editingOutcome?.description || ''}
      />
      <AddNewOutputModal
        show={showAddNewOutputModal}
        setShow={setShowAddNewOutputModal}
        outcomes={outcomeOptions}
        onSubmit={handleAddOutput}
      />
      <AddNewOutputModal
        show={showEditOutputModal}
        setShow={setShowEditOutputModal}
        outcomes={outcomeOptions}
        onSubmit={handleSaveEditedOutput}
        initialName={editingOutput?.name || ''}
        initialDescription={editingOutput?.description || ''}
        initialOutcomeIds={editingOutput?.outcomeIds || []}
      />
      <Row className={styles.table_header}>
        <Col sm={6}>
          <h3>Outcome and Output Management</h3>
        </Col>
        <Col sm={6}>
          <hr />
        </Col>
      </Row>
      <Row className={styles.table_header_bottom}>
        <Col sm={4}>
          <div className={styles.table_header_bottom_left}>
            <Button type="primary" onClick={() => setShowAddNewOutcomeModal(true)}>
              <i className="fa fa-plus" /> Add New Outcome
            </Button>
            {' '}
            <Button type="primary" onClick={() => setShowAddNewOutputModal(true)}>
              <i className="fa fa-plus" /> Add New Output
            </Button>
            {' '}
            <Button type="secondary" disabled>
              <i className="fa fa-download" /> Export CSV
            </Button>
          </div>
        </Col>
      </Row>
      <hr />
      <BootstrapTable
        keyField="id"
        data={dummyOutcomes}
        columns={columns}
        expandRow={expandRow}
        bordered={false}
        headerClasses={styles.table_header_titles}
        bodyClasses={styles.table_body}
      />
    </Col>
  );
};

export default OutcomeOutputManagementPage;
