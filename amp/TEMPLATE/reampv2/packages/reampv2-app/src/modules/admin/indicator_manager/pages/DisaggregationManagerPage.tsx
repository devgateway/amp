import React, {useEffect, useRef, useState} from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import {getAmpCategories} from "../reducers/fetchAmpCategoryReducer";
import initialTranslations from "../config/initialTranslations.json";
import axios from 'axios';
import {useNavigate} from "react-router-dom";
import styles from "../components/modals/css/IndicatorModal.module.css";

interface CategoryValue {
  id: number;
  value: string;
  children?: CategoryValue[];
}
const DisaggregationManagerPage: React.FC = () => {
  const reduxTranslations = useSelector((state: any) => state.translationsReducer.translations);
  const translations = (reduxTranslations && Object.keys(reduxTranslations).length > 0) ? reduxTranslations : initialTranslations;
  const t = (key: string): string => translations[key] ?? initialTranslations[key as keyof typeof initialTranslations] ?? key;
  const [categories, setCategories] = useState<CategoryValue[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<CategoryValue | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState<'add' | 'edit'>('add');
  const [editingChild, setEditingChild] = useState<CategoryValue | null>(null);
  const [optionsMap, setOptionsMap] = useState<{ [key: number]: any[] }>({});
  const [optionValueError, setOptionValueError] = useState<string>('');
  const [deleteCandidate, setDeleteCandidate] = useState<{ category: CategoryValue; child: CategoryValue } | null>(null);
  const [showDeleteConfirmModal, setShowDeleteConfirmModal] = useState(false);
  const [showDeleteBlockedModal, setShowDeleteBlockedModal] = useState(false);
  const [deleteErrorMessage, setDeleteErrorMessage] = useState('');
  const [isDeleting, setIsDeleting] = useState(false);
  const dispatch = useDispatch();

  const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);

  useEffect(() => {
    dispatch(getAmpCategories());
  }, [dispatch]);

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

  const extractApiErrorMessage = async (response: Response, fallback: string): Promise<string> => {
    try {
      const error = await response.json();
      if (error?.error) {
        const firstKey = Object.keys(error.error)[0];
        if (firstKey && error.error[firstKey] && error.error[firstKey][0]) {
          return error.error[firstKey][0];
        }
      }
      if (error?.message) {
        return error.message;
      }
    } catch {
      return fallback;
    }
    return fallback;
  };

  const handleDeleteChild = (category: CategoryValue, child: CategoryValue) => {
    setDeleteCandidate({ category, child });
    setShowDeleteConfirmModal(true);
  };

  const handleCloseDeleteConfirm = () => {
    if (!isDeleting) {
      setShowDeleteConfirmModal(false);
      setDeleteCandidate(null);
    }
  };

  const handleConfirmDeleteChild = async () => {
    if (!deleteCandidate) return;

    const candidate = deleteCandidate;
    setIsDeleting(true);
    try {
      const response = await fetch(`/rest/indicator_disaggregation/options/${candidate.child.id}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
      });
      if (response.ok) {
        setOptionsMap(previousOptionsMap => ({
          ...previousOptionsMap,
          [candidate.category.id]: (previousOptionsMap[candidate.category.id] || [])
            .filter(option => option.id !== candidate.child.id)
        }));
        setShowDeleteConfirmModal(false);
        setDeleteCandidate(null);
        refreshCategories();
      } else {
        const errorMessage = await extractApiErrorMessage(response, t('amp.disaggregationmanager:delete-failed'));
        setShowDeleteConfirmModal(false);
        setDeleteCandidate(null);
        setDeleteErrorMessage(errorMessage);
        setShowDeleteBlockedModal(true);
      }
    } catch {
      setShowDeleteConfirmModal(false);
      setDeleteCandidate(null);
      setDeleteErrorMessage(t('amp.disaggregationmanager:delete-unexpected-error'));
      setShowDeleteBlockedModal(true);
    } finally {
      setIsDeleting(false);
    }
  };

  const handleClose = () => {
    setShowModal(false);
    setOptionValueError('');
  };

  const stripHtml = (input: string): string => input.replace(/<[^>]*>/g, '').trim();
  const nodeRef = useRef(null);
  return (
    <div style={{ padding: '2rem' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>{t('amp.disaggregationmanager:title')}</h2>
        <Button variant="secondary" onClick={() => navigate('/admin/indicator_manager')} style={{ marginLeft: '10px' }}>
          <i className="fa fa-arrow-left" /> {t('amp.disaggregationmanager:back')}
        </Button>
      </div>
      <Table bordered hover>
        <thead>
          <tr>
            <th>{t('amp.disaggregationmanager:category')}</th>
            <th>{t('amp.disaggregationmanager:options')}</th>
            <th>{t('amp.disaggregationmanager:actions')}</th>
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
                        title={t('amp.disaggregationmanager:edit')}
                      >
                        <i className="fa fa-pencil" />
                      </Button>
                      <Button
                        variant="link"
                        size="sm"
                        style={{ marginLeft: 4, color: 'red', padding: '0 6px' }}
                        onClick={() => handleDeleteChild(category, child)}
                        title={t('amp.disaggregationmanager:delete')}
                      >
                        <i className="fa fa-trash" />
                      </Button>
                    </li>
                  )) : <span>{t('amp.disaggregationmanager:no-options')}</span>}
                </ul>
              </td>
              <td>
                <Button variant="success" size="sm" onClick={() => handleAddChild(category)}>
                  {t('amp.disaggregationmanager:add-option')}
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
          <Modal.Title>{modalMode === 'add' ? (t('amp.disaggregationmanager:add-option') + ':' + selectedCategory?.value) : (t('amp.disaggregationmanager:edit-option-title') + ':' + selectedCategory?.value)}</Modal.Title>
        </Modal.Header>
        <Form
            onSubmit={async (e) => {
              e.preventDefault();
              const form = e.currentTarget;
              const formData = new FormData(form);
              const childValue = formData.get('childValue') as string;

              const sanitizedValue = stripHtml(childValue);
              if (!sanitizedValue) {
                setOptionValueError(t('amp.disaggregationmanager:option-value-invalid'));
                return;
              }
              setOptionValueError('');

              if (!selectedCategory) return;

              if (modalMode === 'add') {
                await fetch(`/rest/indicator_disaggregation/options/${selectedCategory.id}`, {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ value: sanitizedValue })
                });
              } else if (modalMode === 'edit' && editingChild) {
                await fetch(`/rest/indicator_disaggregation/options/${editingChild.id}`, {
                  method: 'PUT',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ value: sanitizedValue })
                });
              }
              setShowModal(false);
              setEditingChild(null);
              setOptionValueError('');
              refreshCategories();
            }}
        >
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>
                {t('amp.disaggregationmanager:option-value')}
              </Form.Label>
              <Form.Control
                  name="childValue"
                  type="text"
                  defaultValue={editingChild ? editingChild.value : ''}
                  placeholder={t('amp.disaggregationmanager:option-value-placeholder')}
                  onChange={() => setOptionValueError('')}
                  isInvalid={!!optionValueError}
              />
              <Form.Control.Feedback type="invalid">
                {optionValueError}
              </Form.Control.Feedback>
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              {t('amp.disaggregationmanager:cancel')}
            </Button>
            <Button variant="primary" type="submit">
              {t('amp.disaggregationmanager:save')}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
      <Modal
          show={showDeleteConfirmModal}
          onHide={handleCloseDeleteConfirm}
          centered
          animation={false}
          backdropClassName={styles.modal_backdrop}
          backdrop="static"
          keyboard={!isDeleting}
      >
        <Modal.Header closeButton={!isDeleting}>
          <Modal.Title>{t('amp.disaggregationmanager:delete-option')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            {t('amp.disaggregationmanager:delete-option-confirm')} <strong>{deleteCandidate?.child.value}</strong>?
          </p>
          <p>{t('amp.disaggregationmanager:delete-option-warning')}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseDeleteConfirm} disabled={isDeleting}>
            {t('amp.disaggregationmanager:cancel')}
          </Button>
          <Button variant="danger" onClick={handleConfirmDeleteChild} disabled={isDeleting}>
            {isDeleting ? t('amp.disaggregationmanager:deleting') : t('amp.disaggregationmanager:delete')}
          </Button>
        </Modal.Footer>
      </Modal>
      <Modal
          show={showDeleteBlockedModal}
          onHide={() => setShowDeleteBlockedModal(false)}
          centered
          animation={false}
          backdropClassName={styles.modal_backdrop}
      >
        <Modal.Header closeButton>
          <Modal.Title>{t('amp.disaggregationmanager:cannot-delete-option')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{deleteErrorMessage || t('amp.disaggregationmanager:delete-linked-option')}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowDeleteBlockedModal(false)}>
            {t('amp.disaggregationmanager:ok')}
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default DisaggregationManagerPage;
