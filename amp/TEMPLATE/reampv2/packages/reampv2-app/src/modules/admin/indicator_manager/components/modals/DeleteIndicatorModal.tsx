import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import styles from './css/IndicatorModal.module.css';
import { useDispatch, useSelector } from 'react-redux';
import { deleteIndicator } from '../../reducers/deleteIndicatorReducer';
import { getIndicators } from '../../reducers/fetchIndicatorsReducer';
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content';
import { DefaultComponentProps } from '../../types';
import useDidMountEffect from '../../utils/hooks';

const MySwal = withReactContent(Swal);

interface DeleteIndicatorModalProps extends DefaultComponentProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
    indicator?: any;
}

const DeleteIndicatorModal: React.FC<DeleteIndicatorModalProps> = (props) => {
    const { show, setShow, indicator, translations } = props;
    const dispatch = useDispatch();

    const deleteIndicatorState = useSelector((state: any) => state.deleteIndicatorReducer);

    const handleClose = () => setShow(false);

    useDidMountEffect(() => {
        if (!deleteIndicatorState?.loading && deleteIndicatorState?.error) {
            MySwal.fire({
                title: <p>{translations["amp.indicatormanager:table-title"]}</p>,
                text: deleteIndicatorState?.error ?? translations["amp.indicatormanager:delete-failed"],
                icon: 'error',
                timer: 3000,
                timerProgressBar: true,
            });
            handleClose();
        }

        if (!deleteIndicatorState?.loading && !deleteIndicatorState?.error) {
            MySwal.fire({
                title: <p>{translations["amp.indicatormanager:table-title"]}</p>,
                text: translations["amp.indicatormanager:delete-success"],
                icon: 'success',
                timer: 3000,
                timerProgressBar: true,
            })

            handleClose();
            dispatch(getIndicators());
            return;
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [deleteIndicatorState]);

    const handleDeleteIndicator = () => {
        dispatch(deleteIndicator(indicator?.id));
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
                    <Modal.Title>{translations["amp.indicatormanager:delete-indicator"]}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{translations["amp.indicatormanager:delete-confirm"]}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        {translations["amp.indicatormanager:cancel"]}
                    </Button>
                    <Button variant="danger" onClick={handleDeleteIndicator}>
                        {translations["amp.indicatormanager:delete"]}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>

    )
}

export default DeleteIndicatorModal;
