/* eslint-disable import/no-unresolved */
import React, { useLayoutEffect } from 'react';
import { Modal, Row } from 'react-bootstrap';
import backdropStyles from './css/IndicatorModal.module.css';
import styles from './css/ViewIndicatorModal.module.css';
import { DefaultComponentProps, IndicatorObjectType, ProgramObjectType, SectorObjectType } from '../../types';
import { useSelector } from 'react-redux';
import { extractChildrenFromProgramScheme } from '../../utils/helpers';

interface ViewIndicatorModalProps extends DefaultComponentProps {
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
  const { show, setShow, indicator, translations } = props;
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
    const children = extractChildrenFromProgramScheme(programsReducer.programs);
    const programData = children.filter((program: any) => programIds.includes(program.id));
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
        <Modal.Title>{translations["amp.indicatormanager:view-indicator"]}</Modal.Title>
      </Modal.Header>
      {indicator ?
        <Modal.Body>
          <div className={styles.viewmodal_wrapper}>
            <Row className={styles.view_row}>
              <div className={styles.view_item}>
                <h4>{translations["amp.indicatormanager:view-indicator-id"]}</h4>
                <p className={styles.value}>{indicator.id}</p>
              </div>
              <div className={styles.view_item}>
                <h4 className={styles.label}>{translations["amp.indicatormanager:indicator-name"]}</h4>
                <p className={styles.value}>{indicator.name}</p>
              </div>
            </Row>
            <Row className={styles.view_row}>
              <div className={styles.view_item}>
                <h4>{translations["amp.indicatormanager:indicator-code"]}</h4>
                <p className={styles.value}>{indicator.code}</p>
              </div>
              <div className={styles.view_item}>
                <h4 className={styles.label}>{translations["amp.indicatormanager:indicator-description"]}</h4>
                <p className={styles.value}>{indicator.description  === "" || '' ? 'No Description available': indicator.description}</p>
              </div>
            </Row>

            <Row className={styles.view_row}>
              <div className={styles.view_item}>
                <h4 className={styles.label}>{translations["amp.indicatormanager:ascending"]}</h4>
                <p className={styles.value}>{indicator.ascending ? 'True' : 'False'}</p>
              </div>

              <div className={styles.view_item}>
                <h4 className={styles.label}>{translations["amp.indicatormanager:table-header-creation-date"]}</h4>
                <p className={styles.value}>{indicator.creationDate}</p>
              </div>
            </Row>

            <Row className={styles.view_row_full}>
              <h4 className={styles.label}>{translations["amp.indicatormanager:sectors"]}</h4>
              <div className={styles.value}>
                {sectorData.length > 0 ? sectorData.map((sector) => (
                  <p style={{
                    backgroundColor: colorOptions[sector.id % 10].color,
                  }} className={styles.array_item} key={sector.id}>{sector.name}</p>
                )) :
                  <p>{translations["amp.indicatormanager:no-data"]}</p>
                }
              </div>

            </Row>

            <Row className={styles.view_row_full}>
              <h4 className={styles.label}>{translations["amp.indicatormanager:programs"]}</h4>
              {programData.length > 0 ? programData.map((program) => (
                <p className={styles.array_item} key={program.id}>{program.name}</p>
              )) :
                <p>{translations["amp.indicatormanager:no-data"]}</p>
              }
            </Row>
          </div>
        </Modal.Body> :
        <Modal.Body>
          <h3>{translations["amp.indicatormanager:view-error"]}</h3>
        </Modal.Body>
      }

    </Modal>
  );
};

export default ViewIndicatorModal;
