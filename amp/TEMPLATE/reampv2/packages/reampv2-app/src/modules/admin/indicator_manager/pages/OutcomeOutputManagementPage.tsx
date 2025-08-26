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
import Swal from 'sweetalert2';
import OutputManagementPage from './OutputManagementPage';
import {useNavigate} from "react-router-dom";

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
  const [editingOutcome, setEditingOutcome] = useState<Outcome | null>(null);
  const [outcomes, setOutcomes] = useState<Outcome[]>([]);
  const navigate = useNavigate();

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
          <div className={styles.action_container}
          >
            <i
               onClick={() => handleEditOutcome(row)}
               style={{ fontSize: 20, color: '#198754' }}
               className="fa fa-pencil"
               aria-hidden="true"
            />
          </div>
          <div className={styles.action_container}
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
      <Col sm={12}>
        <Row className={styles.table_header}>
          <Col sm={6}>
            <h3>Outcome Management</h3>
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
                      <i className="fa fa-plus" /> Add New Outcome
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
                  <Button variant="info" onClick={() => navigate('/admin/indicator_manager/output-management')} style={{ float: 'right', marginLeft: '10px' }}>
                    <i className="fa fa-share" /> Output Management
                  </Button>
                  <Button variant="secondary" onClick={() => navigate('/admin/indicator_manager')} style={{ float: 'right', marginLeft: '10px' }}>
                    <i className="fa fa-arrow-left" /> Back
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
