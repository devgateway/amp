/* eslint-disable import/no-unresolved */
import React, { useLayoutEffect } from 'react';
import { Modal, Row, Col, Badge } from 'react-bootstrap';
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
  { value: 'ocean', label: 'Ocean', color: '#00B8D9', textColor: '#ffffff' },
  { value: 'blue', label: 'Blue', color: '#0052CC', textColor: '#ffffff' },
  { value: 'purple', label: 'Purple', color: '#5243AA', textColor: '#ffffff' },
  { value: 'red', label: 'Red', color: '#FF5630', textColor: '#ffffff' },
  { value: 'orange', label: 'Orange', color: '#FF8B00', textColor: '#000000' },
  { value: 'yellow', label: 'Yellow', color: '#FFC400', textColor: '#000000' },
  { value: 'green', label: 'Green', color: '#36B37E', textColor: '#ffffff' },
  { value: 'forest', label: 'Forest', color: '#00875A', textColor: '#ffffff' },
  { value: 'slate', label: 'Slate', color: '#96A0A8', textColor: '#ffffff' },
  { value: 'silver', label: 'Silver', color: '#666666', textColor: '#ffffff' },
];

const ViewIndicatorModal: React.FC<ViewIndicatorModalProps> = (props) => {
  const { show, setShow, indicator, translations } = props;
  const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
  const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
  const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);
  const outcomesReducer = useSelector((state: any) => state.fetchOutcomesReducer);
  const outputsReducer = useSelector((state: any) => state.fetchOutputsReducer);
  const responsibleOrgsReducer = useSelector((state: any) => state.fetchResponsibleOrgsReducer);

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
    const programId = indicator.programId;
    const children = extractChildrenFromProgramScheme(programsReducer.programs);
    const programData = children.filter((program: any) => programId === program.id);
    setProgramData(programData);
  };

  useLayoutEffect(() => {
    getSectorData();
    getProgramData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [indicator]);

  // Helper functions for lookups
  const getCategoryLabel = (id: number | undefined) => {
    if (!id) return translations["amp.indicatormanager:no-data"];
    const found = categoriesReducer.categories.find((cat: any) => cat.id === id);
    return found ? found.value : id;
  };
  const getOutcomeLabel = (id: number | undefined) => {
    if (!id) return translations["amp.indicatormanager:no-data"];
    const found = outcomesReducer.outcomes.find((o: any) => o.id === id);
    return found ? found.name : id;
  };
  const getOutputLabel = (id: number | undefined) => {
    if (!id) return translations["amp.indicatormanager:no-data"];
    const found = outputsReducer.outputs.find((o: any) => o.id === id);
    return found ? found.name : id;
  };
  const getResponsibleOrgLabels = (ids: number[] = []) => {
    if (!ids.length) return [translations["amp.indicatormanager:no-data"]];
    return ids.map(id => {
      const found = responsibleOrgsReducer.options.find((org: any) => org.value === id);
      return found ? found.label : id;
    });
  };

  return (
      <Modal
          show={show}
          onHide={handleClose}
          centered
          backdropClassName={backdropStyles.modal_backdrop}
          animation={false}
          size='xl'
          className={styles.view_modal}
      >
        <Modal.Header closeButton className={styles.modal_header}>
          <Modal.Title className={styles.modal_title}>
            <i className="fas fa-info-circle me-2"></i>
            {translations["amp.indicatormanager:view-indicator"]}
          </Modal.Title>
        </Modal.Header>
        {indicator ?
            <Modal.Body className={styles.modal_body}>
              <div className={styles.viewmodal_wrapper}>
                {/* Core Information Section */}
                <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                  <h4 className={styles.section_title} style={{color: '#0052CC', fontWeight: 'bold'}}>
                    <i className="fas fa-info-circle me-2"></i>
                    {translations["amp.indicatormanager:core-info"] || "Core Information"}
                  </h4>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:view-indicator-id"]}</b></div>
                      <div className={styles.value}>{indicator.id}</div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:indicator-code"]}</b></div>
                      <div className={styles.value}>{indicator.code}</div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={12} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:indicator-name"]}</b></div>
                      <div className={`${styles.value} ${styles.important}`} style={{fontWeight: 'bold', color: '#36B37E'}}>{indicator.name}</div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={12} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:indicator-description"]}</b></div>
                      <div className={styles.value} style={{fontStyle: 'italic'}}>
                        {indicator.description === "" || !indicator.description ?
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-description-available"]}</span> :
                            indicator.description
                        }
                      </div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:ascending"]}</b></div>
                      <div className={styles.value}>
                        <Badge content={indicator.ascending ? "success" : "secondary"} style={{fontWeight: 'bold'}}>
                          {indicator.ascending ? translations["amp.indicatormanager:yes"] : translations["amp.indicatormanager:no"]}
                        </Badge>
                      </div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:table-header-creation-date"]}</b></div>
                      <div className={styles.value}>{indicator.creationDate}</div>
                    </Col>
                  </Row>
                </div>

                {/* Classification Section */}
                <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                  <h4 className={styles.section_title} style={{color: '#FF5630', fontWeight: 'bold'}}>
                    <i className="fas fa-tags me-2"></i>
                    {translations["amp.indicatormanager:classification"] || "Classification"}
                  </h4>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:sectors"]}</b></div>
                      <div className={styles.value}>
                        {sectorData.length > 0 ? (
                            <div className={styles.tags_container}>
                              {sectorData.map((sector) => {
                                const colorIndex = sector.id % 10;
                                return (
                                    <span
                                        key={sector.id}
                                        className={styles.tag}
                                        style={{backgroundColor: colorOptions[colorIndex].color, color: colorOptions[colorIndex].textColor, fontWeight: 'bold'}}>
                                      {sector.name}
                                    </span>
                                );
                              })}
                            </div>
                        ) : (
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>
                        )}
                      </div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:programs"]}</b></div>
                      <div className={styles.value}>
                        {programData.length > 0 ? (
                            <div className={styles.tags_container}>
                              {programData.map((program) => (
                                  <span key={program.id} className={styles.tag} style={{backgroundColor: '#5243AA', color: '#fff', fontWeight: 'bold'}}>
                                    {program.name}
                                  </span>
                              ))}
                            </div>
                        ) : (
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>
                        )}
                      </div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:outcome"]}</b></div>
                      <div className={styles.value}>{getOutcomeLabel(indicator.outcomeId)}</div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:output"]}</b></div>
                      <div className={styles.value}>{getOutputLabel(indicator.outputId)}</div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:indicator-type"]}</b></div>
                      <div className={styles.value}>{getCategoryLabel(indicator.indicatorType)}</div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:frequency"]}</b></div>
                      <div className={styles.value}>{getCategoryLabel(indicator.frequency)}</div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={12} className={styles.view_item}>
                      <div className={styles.label}><b>{translations["amp.indicatormanager:responsible-organizations"]}</b></div>
                      <div className={styles.value}>
                        {getResponsibleOrgLabels(indicator.responsibleOrganizations).join(", ") ||
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}
                      </div>
                    </Col>
                  </Row>
                </div>

                {/* Values Section */}
                <Row className={styles.view_row}>
                  <Col md={6} className={styles.section} style={{borderRight: '1px solid #e0e0e0', paddingRight: 16}}>
                    <h4 className={styles.section_title} style={{color: '#36B37E', fontWeight: 'bold'}}>
                      <i className="fas fa-chart-line me-2"></i>
                      {translations["amp.indicatormanager:base-values"]}
                    </h4>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:original-base-value"]}</div>
                      <div className={styles.value}>{indicator.base?.originalValue ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:original-value-date"]}</div>
                      <div className={styles.value}>{indicator.base?.originalValueDate ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:revised-value"]}</div>
                      <div className={styles.value}>{indicator.base?.revisedValue ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:revised-value-date"]}</div>
                      <div className={styles.value}>{indicator.base?.revisedValueDate ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                  </Col>
                  <Col md={6} className={styles.section} style={{paddingLeft: 16}}>
                    <h4 className={styles.section_title} style={{color: '#FF8B00', fontWeight: 'bold'}}>
                      <i className="fas fa-bullseye me-2"></i>
                      {translations["amp.indicatormanager:target-values"]}
                    </h4>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:target-value"]}</div>
                      <div className={styles.value}>{indicator.target?.originalValue ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:target-value-date"]}</div>
                      <div className={styles.value}>{indicator.target?.originalValueDate ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:revised-value"]}</div>
                      <div className={styles.value}>{indicator.target?.revisedValue ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                    <div className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:revised-value-date"]}</div>
                      <div className={styles.value}>{indicator.target?.revisedValueDate ?? <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}</div>
                    </div>
                  </Col>
                </Row>

                {/* Additional Information Section */}
                <div className={styles.section} style={{marginTop: 24}}>
                  <h4 className={styles.section_title} style={{color: '#5243AA', fontWeight: 'bold'}}>
                    <i className="fas fa-ellipsis-h me-2"></i>
                    {translations["amp.indicatormanager:additional-information"] || "Additional Information"}
                  </h4>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:unit-of-measure"]}</div>
                      <div className={styles.value}>{getCategoryLabel(indicator.unitOfMeasure)}</div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:disaggregation"]}</div>
                      <div className={styles.value}>
                        {indicator.disaggregation && indicator.disaggregation.length > 0 ?
                            indicator.disaggregation.map(getCategoryLabel).join(", ") :
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}
                      </div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:logframe-links"]}</div>
                      <div className={styles.value}>
                        {indicator.logframeLinks && indicator.logframeLinks.length > 0 ?
                            indicator.logframeLinks.join(", ") :
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}
                      </div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:data-source"]}</div>
                      <div className={styles.value}>
                        {indicator.dataSource ||
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}
                      </div>
                    </Col>
                    <Col md={6} className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:calculation-method"]}</div>
                      <div className={styles.value}>
                        {indicator.calculationMethod ||
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}
                      </div>
                    </Col>
                  </Row>
                  <Row className={styles.view_row}>
                    <Col md={12} className={styles.view_item}>
                      <div className={styles.label}>{translations["amp.indicatormanager:relevance-for-climate-change"]}</div>
                      <div className={styles.value}>
                        {indicator.relevanceForClimateChange ||
                            <span className={styles.no_data}>{translations["amp.indicatormanager:no-data"]}</span>}
                      </div>
                    </Col>
                  </Row>
                </div>
              </div>
            </Modal.Body> :
            <Modal.Body>
              <div className={styles.error_state}>
                <i className="fas fa-exclamation-triangle"></i>
                <h3>{translations["amp.indicatormanager:view-error"]}</h3>
              </div>
            </Modal.Body>
        }
      </Modal>
  );
};

export default ViewIndicatorModal;
