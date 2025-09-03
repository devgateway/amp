import React, {useEffect, useRef, useState} from 'react';
import { Button, Table, Modal } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import {getAmpCategories} from "../reducers/fetchAmpCategoryReducer";
import initialTranslations from "../config/initialTranslations.json";
import axios from 'axios';
import {useNavigate} from "react-router-dom";
import { Formik, Form as FormikForm, Field } from 'formik';
import styles from "../components/modals/css/IndicatorModal.module.css";

interface CategoryValue {
  id: number;
  value: string;
  children?: CategoryValue[];
}
const translations = initialTranslations;


const DisaggregationManagerPage: React.FC = () => {
  const [categories, setCategories] = useState<CategoryValue[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<CategoryValue | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState<'add' | 'edit'>('add');
  const [editingChild, setEditingChild] = useState<CategoryValue | null>(null);
  const [optionsMap, setOptionsMap] = useState<{ [key: number]: any[] }>({});
    const dispatch = useDispatch();

    const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);

  // Fetch indicator_disaggregation categories and their children
  useEffect(() => {
    if (categoriesReducer && categoriesReducer.categories) {
      const disaggregationCategories = categoriesReducer.categories.filter((cat: any) => cat.ampCategoryClass.keyName === 'indicator_disaggregation');
      setCategories(disaggregationCategories);
    }
  }, [categoriesReducer]);
  const navigate = useNavigate()

  useEffect(() => {
    async function fetchOptions() {
      const map: { [key: number]: any[] } = {};
      for (const category of categories) {
        try {
          const res = await axios.get(`/rest/indicator_disaggregation/options/${category.id}`);
          map[category.id] = res.data;
        } catch {
          map[category.id] = [];
        }
      }
      setOptionsMap(map);
    }
    if (categories.length > 0) fetchOptions();
  }, [categories]);

  const handleAddChild = (category: CategoryValue) => {
    setSelectedCategory(category);
    setModalMode('add');
    setShowModal(true);
  };

  const handleEditChild = (category: CategoryValue, child: CategoryValue) => {
    setSelectedCategory(category);
    setEditingChild(child);
    setModalMode('edit');
    setShowModal(true);
  };

  const refreshCategories = () => {
    dispatch(getAmpCategories());
  };

  const handleDeleteChild = async (category: CategoryValue, child: CategoryValue) => {
    await fetch(`/rest/indicator_disaggregation/options/${child.id}`, { method: 'DELETE' });
    refreshCategories();
  };

  const handleClose = () => setShowModal(false);
  const nodeRef = useRef(null);
  return (
    <div style={{ padding: '2rem' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>{translations['amp.disaggregationmanager:title'] || 'Disaggregation Manager'}</h2>
        <Button variant="secondary" onClick={() => navigate('/admin/indicator_manager')} style={{ marginLeft: '10px' }}>
          <i className="fa fa-arrow-left" /> Back
        </Button>
      </div>
      <Table bordered hover>
        <thead>
          <tr>
            <th>{translations['amp.disaggregationmanager:category'] || 'Disaggregation Category'}</th>
            <th>{translations['amp.disaggregationmanager:options'] || 'Options'}</th>
            <th>{translations['amp.disaggregationmanager:actions'] || 'Actions'}</th>
          </tr>
        </thead>
        <tbody>
          {categories.map(category => (
            <tr key={category.id}>
              <td><strong>{category.value}</strong></td>
              <td>
                <ul>
                  {optionsMap[category.id] && optionsMap[category.id].length > 0 ? optionsMap[category.id].map(child => (
                    <li key={child.id}>
                      {child.value}
                      <Button
                        variant="link"
                        size="sm"
                        style={{ marginLeft: 8, color: 'black', padding: '0 6px' }}
                        onClick={() => handleEditChild(category, child)}
                        title={translations['amp.disaggregationmanager:edit'] || 'Edit'}
                      >
                        <i className="fa fa-pencil" />
                      </Button>
                      <Button
                        variant="link"
                        size="sm"
                        style={{ marginLeft: 4, color: 'red', padding: '0 6px' }}
                        onClick={() => handleDeleteChild(category, child)}
                        title={translations['amp.disaggregationmanager:delete'] || 'Delete'}
                      >
                        <i className="fa fa-trash" />
                      </Button>
                    </li>
                  )) : <span>{translations['amp.disaggregationmanager:no-options'] || 'No options'}</span>}
                </ul>
              </td>
              <td>
                <Button variant="success" size="sm" onClick={() => handleAddChild(category)}>
                  {translations['amp.disaggregationmanager:add-option'] || 'Add Option'}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Modal
          show={showModal}
          onHide={handleClose}
          centered
          ref={nodeRef}
          animation={false}
          backdropClassName={styles.modal_backdrop}
          backdrop="static"
          keyboard={false}
          size='lg'
      >
        <Modal.Header closeButton>
          <Modal.Title>{modalMode === 'add' ? (translations['amp.disaggregationmanager:add-option'] || 'Add Option' +':' +selectedCategory?.value) : (translations['amp.disaggregationmanager:edit-option-title'] || 'Edit Option'+':' +selectedCategory?.value)}</Modal.Title>
        </Modal.Header>
        <Formik
          initialValues={{ childValue: '' }}
          enableReinitialize
          onSubmit={async (values, { setSubmitting }) => {
            if (!selectedCategory) return;
            if (modalMode === 'add') {
              await fetch(`/rest/indicator_disaggregation/options/${selectedCategory.id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ value: values.childValue })
              });
            } else if (modalMode === 'edit' && editingChild) {
              await fetch(`/rest/indicator_disaggregation/options/${editingChild.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ value: values.childValue })
              });
            }
            setShowModal(false);
            setEditingChild(null);
            refreshCategories();
            setSubmitting(false);
          }}
        >
          {({ isSubmitting, values, setFieldValue }) => (
            <FormikForm>
              <Modal.Body>
                <div className="mb-3">
                  <label htmlFor="childValue" className="form-label">
                    {translations['amp.disaggregationmanager:option-value'] || 'Option Value'}
                  </label>
                  <Field
                    id="childValue"
                    name="childValue"
                    type="text"
                    initialValue={editingChild ? editingChild.value : ''}
                    className="form-control"
                    placeholder={translations['amp.disaggregationmanager:option-value-placeholder'] || 'Enter option value'}
                  />
                </div>
              </Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isSubmitting}>
                  {translations['amp.disaggregationmanager:cancel'] || 'Cancel'}
                </Button>
                <Button variant="primary" type="submit" disabled={isSubmitting}>
                  {translations['amp.disaggregationmanager:save'] || 'Save'}
                </Button>
              </Modal.Footer>
            </FormikForm>
          )}
        </Formik>
      </Modal>
    </div>
  );
};

export default DisaggregationManagerPage;
