import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import styles from './css/IndicatorModal.module.css';
import { useDispatch, useSelector } from 'react-redux';
import { deleteIndicator } from '../../reducers/deleteIndicatorReducer';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';

const MySwal = withReactContent(Swal);

interface DeleteIndicatorModalProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
    indicator?: any;
}

const DeleteIndicatorModal: React.FC<DeleteIndicatorModalProps> = (props) => {
    const { show, setShow, indicator } = props;
    const dispatch = useDispatch();

    const deleteIndicatorState = useSelector((state: any) => state.deleteIndicator);

    const handleClose = () => setShow(false);

    const handleDeleteIndicator = () => {
        dispatch(deleteIndicator(indicator?.id));

        if (!deleteIndicatorState?.error) {
            MySwal.fire({
                title: <p>AMP Indicator Manager</p>,
                text: 'Indicator Deleted Successfully',
                icon: 'success',
                timer: 3000,
                timerProgressBar: true,
            });

            handleClose();
            dispatch(getIndicators());
            return;
        }

        MySwal.fire({
            title: <p>AMP Indicator Manager</p>,
            text: 'Indicator Delete Failed',
            icon: 'error',
            timer: 3000,
            timerProgressBar: true,
        });
        handleClose();
    };

    return (
        <>
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
                    <Button variant="danger" onClick={handleDeleteIndicator}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </>

    )
}

export default DeleteIndicatorModal;
