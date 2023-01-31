/* eslint-disable import/no-unresolved */
import React from 'react';
import { Modal } from 'react-bootstrap';
import styles from './css/ViewIndicatorModal.module.css';

interface ViewIndicatorProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
}

const ViewIndicatorModal = () => {
  console.log('ViewIndicatorModal');
  return (
    <div>ViewIndicatorModal</div>
  );
};

export default ViewIndicatorModal;
