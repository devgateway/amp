/* eslint-disable import/no-unresolved */
import React, { useLayoutEffect } from 'react';
import { Modal, Row } from 'react-bootstrap';
import backdropStyles from './css/IndicatorModal.module.css';
import styles from './css/ViewIndicatorModal.module.css';
import { IndicatorObjectType, ProgramObjectType, SectorObjectType } from '../../types';
import { useSelector } from 'react-redux';

interface ViewIndicatorModalProps {
  show: boolean;
  setShow: React.Dispatch<React.SetStateAction<boolean>>;
  indicator: IndicatorObjectType;
}

const colorOptions = [
  { value: 'ocean', label: 'Ocean', color: '#00B8D9' },
  { value: 'blue', label: 'Blue', color: '#0052CC' },
  { value: 'purple', label: 'Purple', color: '#5243AA' },
  { value: 'red', label: 'Red', color: '#FF5630' },
  { value: 'orange', label: 'Orange', color: '#FF8B00' },
  { value: 'yellow', label: 'Yellow', color: '#FFC400' },
  { value: 'green', label: 'Green', color: '#36B37E' },
  { value: 'forest', label: 'Forest', color: '#00875A' },
  { value: 'slate', label: 'Slate', color: '#253858' },
  { value: 'silver', label: 'Silver', color: '#666666' },
];

const ViewIndicatorModal: React.FC<ViewIndicatorModalProps> = (props) => {
  const { show, setShow, indicator } = props;
  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);

  const handleClose = () => setShow(false);

  const [sectorData, setSectorData] = React.useState<SectorObjectType[]>([]);
  const [programData, setProgramData] = React.useState<ProgramObjectType[]>([]);

  const getSectorData = () => {
    if (!indicator) return;
    const sectorIds = indicator.sectors;
    const sectorData = sectorsReducer.sectors.filter((sector: any) => sectorIds.includes(sector.id));
    setSectorData(sectorData);
  };

  const getProgramData = () => {
    if (!indicator) return;
    const programIds = indicator.programs;
    const programData = programsReducer.programs.filter((program: any) => programIds.includes(program.id));
    setProgramData(programData);
  };

  useLayoutEffect(() => {
    getSectorData();
    getProgramData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [indicator]);

  return (
    // this modal wrapper should be a separate component that can be reused since the props are the same
    <Modal
      show={show}
      onHide={handleClose}
      centered
      backdropClassName={backdropStyles.modal_backdrop}
      animation={false}
      size='lg'
    >
      <Modal.Header closeButton>
        <Modal.Title>View Indicator</Modal.Title>
      </Modal.Header>
      {indicator ?
        <Modal.Body>
          <div className={styles.viewmodal_wrapper}>
            <Row className={styles.view_row}>
              <div className={styles.view_item}>
                <h4>Indicator ID</h4>
                <p className={styles.value}>{indicator.id}</p>
              </div>
              <div className={styles.view_item}>
                <h4 className={styles.label}>Indicator Name</h4>
                <p className={styles.value}>{indicator.name}</p>
              </div>
            </Row>
            <Row className={styles.view_row}>
              <div className={styles.view_item}>
                <h4>Indicator Code</h4>
                <p className={styles.value}>{indicator.code}</p>
              </div>
              <div className={styles.view_item}>
                <h4 className={styles.label}>Indicator Description</h4>
                <p className={styles.value}>{indicator.description  === "" || '' ? 'No Description available': indicator.description}</p>
              </div>
            </Row>

            <Row className={styles.view_row}>
              <div className={styles.view_item}>
                <h4 className={styles.label}>Ascending</h4>
                <p className={styles.value}>{indicator.ascending ? 'True' : 'False'}</p>
              </div>

              <div className={styles.view_item}>
                <h4 className={styles.label}>Indicator Creation Date</h4>
                <p className={styles.value}>{indicator.creationDate}</p>
              </div>
            </Row>

            <Row className={styles.view_row_full}>
              <h4 className={styles.label}>Indicator Sectors</h4>
              <div className={styles.value}>
                {sectorData.length > 0 ? sectorData.map((sector) => (
                  <p style={{
                    backgroundColor: colorOptions[sector.id % 10].color,
                  }} className={styles.array_item} key={sector.id}>{sector.name}</p>
                )) :
                  <p>No Sectors avaliable</p>
                }
              </div>

            </Row>

            <Row className={styles.view_row_full}>
              <h4 className={styles.label}>Programs</h4>
              {programData.length > 0 ? programData.map((program) => (
                <p className={styles.array_item} key={program.id}>{program.name}</p>
              )) :
                <p>No Programs avaliable</p>
              }
            </Row>
          </div>
        </Modal.Body> :
        <Modal.Body>
          <h3>Error loading indicator</h3>
        </Modal.Body>
      }

    </Modal>
  );
};

export default ViewIndicatorModal;
