import React, { useEffect, useState } from 'react';
import { Button, Table, Modal, Form, Row, Col } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import {getAmpCategories} from "../reducers/fetchAmpCategoryReducer";
import initialTranslations from "../config/initialTranslations.json";
import axios from 'axios';

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
  const [childValue, setChildValue] = useState('');
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
    setChildValue('');
    setShowModal(true);
  };

  const handleEditChild = (category: CategoryValue, child: CategoryValue) => {
    setSelectedCategory(category);
    setEditingChild(child);
    setChildValue(child.value);
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

  const handleModalSave = async () => {
    if (!selectedCategory) return;
    if (modalMode === 'add') {
      await fetch(`/rest/indicator_disaggregation/options/${selectedCategory.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ value: childValue })
      });
    } else if (modalMode === 'edit' && editingChild) {
      await fetch(`/rest/indicator_disaggregation/options/${editingChild.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ value: childValue })
      });
    }
    setShowModal(false);
    setEditingChild(null);
    setChildValue('');
    refreshCategories();
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>{translations['amp.disaggregationmanager:title'] || 'Disaggregation Manager'}</h2>
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
                      <Button variant="outline-primary" size="sm" style={{ marginLeft: 8 }} onClick={() => handleEditChild(category, child)}>
                        {translations['amp.disaggregationmanager:edit'] || 'Edit'}
                      </Button>
                      <Button variant="outline-danger" size="sm" style={{ marginLeft: 4 }} onClick={() => handleDeleteChild(category, child)}>
                        {translations['amp.disaggregationmanager:delete'] || 'Delete'}
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
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>{modalMode === 'add' ? (translations['amp.disaggregationmanager:add-option'] || 'Add Option') : (translations['amp.disaggregationmanager:edit-option-title'] || 'Edit Option')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formChildValue">
              <Form.Label>{translations['amp.disaggregationmanager:option-value'] || 'Option Value'}</Form.Label>
              <Form.Control
                type="text"
                value={childValue}
                onChange={e => setChildValue(e.target.value)}
                placeholder={translations['amp.disaggregationmanager:option-value-placeholder'] || 'Enter option value'}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            {translations['amp.disaggregationmanager:cancel'] || 'Cancel'}
          </Button>
          <Button variant="primary" onClick={handleModalSave}>
            {translations['amp.disaggregationmanager:save'] || 'Save'}
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default DisaggregationManagerPage;
