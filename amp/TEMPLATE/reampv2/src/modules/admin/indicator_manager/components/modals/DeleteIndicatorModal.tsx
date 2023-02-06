import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import styles from './css/IndicatorModal.module.css';

interface DeleteIndicatorModalProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
    indicator?: any;
}

const DeleteIndicatorModal: React.FC<DeleteIndicatorModalProps> = (props) => {
    const { show, setShow } = props;

    const handleClose = () => setShow(false);

    return (
        <Modal
            show={show}
            onHide={handleClose}
            centered
            backdropClassName={styles.modal_backdrop}
            animation={false}
        >
            <Modal.Header closeButton>
                <Modal.Title>Delete Indicator</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Are you sure you want to delete this indicator?</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cancel
                </Button>
                <Button variant="danger" onClick={handleClose}>
                    Delete
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default DeleteIndicatorModal;
