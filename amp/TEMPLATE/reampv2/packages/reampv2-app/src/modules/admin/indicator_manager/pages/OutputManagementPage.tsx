import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'react-bootstrap';
import BootstrapTable from '@musicstory/react-bootstrap-table-next';
import '@musicstory/react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import styles from '../components/table/Table.module.css';
import OutputModal from '../components/modals/OutputModal';
import Swal from 'sweetalert2';

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
  const [outputs, setOutputs] = useState<Output[]>([]);
  const [showAddNewOutputModal, setShowAddNewOutputModal] = useState(false);
  const [showEditOutputModal, setShowEditOutputModal] = useState(false);
  const [editingOutput, setEditingOutput] = useState<Output | null>(null);
  const [loadingEditOutput, setLoadingEditOutput] = useState(false);
  const [outcomes, setOutcomes] = useState<Outcome[]>([]);

  useEffect(() => {
    fetch('/rest/amp-outcome-output/outputs')
      .then(res => res.json())
      .then(data => setOutputs(data));
    fetch('/rest/amp-outcome-output/outcomes')
      .then(res => res.json())
      .then(data => setOutcomes(data));
  }, []);

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
            onClick={() => handleEditOutput(row)}
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

  const handleAddOutput = async (output: { name: string; description?: string; outcomeIds: number[] }) => {

    try {
      const res = await fetch('/rest/amp-outcome-output/output', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(output)
      });
      if (res.ok) {
        fetch('/rest/amp-outcome-output/outputs')
          .then(res => res.json())
          .then(data => setOutputs(data));
      } else {
        alert('Failed to add output');
      }
    } catch (e) {
      alert('Error adding output');
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
        alert('Failed to fetch output details');
      }
    } catch (e) {
      alert('Error fetching output details');
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
        fetch('/rest/amp-outcome-output/outputs')
          .then(res => res.json())
          .then(data => setOutputs(data));
      } else {
        alert('Failed to update output');
      }
    } catch (e) {
      alert('Error updating output');
    }
    setShowEditOutputModal(false);
    setEditingOutput(null);
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
          <Col sm={6}>
            <h3>Output Management</h3>
          </Col>
          <Col sm={6}>
            <Button variant="primary" onClick={() => setShowAddNewOutputModal(true)} style={{ float: 'right' }}>
              <i className="fa fa-plus" /> Add New Output
            </Button>
          </Col>
        </Row>
        <BootstrapTable
          keyField="id"
          data={outputs}
          columns={outputColumns}
          bordered={false}
          headerClasses={styles.table_header_titles}
          bodyClasses={styles.table_body}
          noDataIndication={() => (
            <div className={styles.no_data}>
              <h5>No outputs found</h5>
            </div>
          )}
        />
      </Col>
    </>
  );
};

export default OutputManagementPage;
