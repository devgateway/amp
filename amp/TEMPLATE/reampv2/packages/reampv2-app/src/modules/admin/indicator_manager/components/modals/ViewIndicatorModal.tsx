/* eslint-disable import/no-unresolved */
import React, { useLayoutEffect } from 'react';
import { Modal, Row, Col, Badge } from 'react-bootstrap';
import backdropStyles from './css/IndicatorModal.module.css';
import styles from './css/ViewIndicatorModal.module.css';
import { DefaultComponentProps, IndicatorObjectType, ProgramObjectType, SectorObjectType } from '../../types';
import { useSelector } from 'react-redux';
import initialTranslations from '../../config/initialTranslations.json';

interface ViewIndicatorModalProps extends DefaultComponentProps {
    show: boolean;
    setShow: React.Dispatch<React.SetStateAction<boolean>>;
    indicator: IndicatorObjectType;
}

const ViewIndicatorModal: React.FC<ViewIndicatorModalProps> = (props) => {
    const { show, setShow, indicator, translations } = props;
    const t = (key: string): string => translations[key] ?? initialTranslations[key as keyof typeof initialTranslations] ?? key;
    const sectorsReducer = useSelector((state: any) => state.fetchSectorsReducer);
    const programsReducer = useSelector((state: any) => state.fetchProgramsReducer);
    const categoriesReducer = useSelector((state: any) => state.fetchAmpCategoryReducer);
    const outcomesReducer = useSelector((state: any) => state.fetchOutcomesReducer);
    const outputsReducer = useSelector((state: any) => state.fetchOutputsReducer);
    const responsibleOrgsReducer = useSelector((state: any) => state.fetchResponsibleOrgsReducer);

    const handleClose = () => setShow(false);

    const [sectorData, setSectorData] = React.useState<SectorObjectType[]>([]);

    const getSectorData = () => {
        if (!indicator) return;
        const sectorIds = indicator.sectors;
        const sectorData = sectorsReducer.sectors.filter((sector: any) => sectorIds.includes(sector.id));
        setSectorData(sectorData);
    };

    useLayoutEffect(() => {
        getSectorData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [indicator]);

    // Helper functions for lookups
    const getCategoryLabel = (id: number | undefined) => {
        if (!id) return t("amp.indicatormanager:no-data");
        const found = categoriesReducer.categories.find((cat: any) => cat.id === id);
        return found ? found.value : id;
    };
    const getOutcomeLabel = (id: number | undefined) => {
        if (!id) return t("amp.indicatormanager:no-data");
        const found = outcomesReducer.outcomes.find((o: any) => o.id === id);
        return found ? found.name : id;
    };
    const getOutputLabel = (id: number | undefined) => {
        if (!id) return t("amp.indicatormanager:no-data");
        const found = outputsReducer.outputs.find((o: any) => o.id === id);
        return found ? found.name : id;
    };
    const getResponsibleOrgLabels = (ids: number[] = []) => {
        if (!ids.length) return [t("amp.indicatormanager:no-data")];
        return ids.map(id => {
            const found = responsibleOrgsReducer.options.find((org: any) => org.value === id);
            return found ? found.label : id;
        });
    };
    const getProgramLabel = (id: number | null) => {
        if (!id) return t("amp.indicatormanager:no-data");
        const found = programsReducer.programs.find((p: any) => p.id === id);
        return found ? found.name : id;
    };

    return (
        <Modal
            show={show}
            onHide={handleClose}
            centered
            backdropClassName={styles.modal_backdrop}
            animation={false}
            backdrop="static"
            keyboard={false}
            size="lg"
        >
            <Modal.Header closeButton className={styles.modal_header}>
                <Modal.Title className={styles.modal_title}>
                    <i className="fas fa-info-circle me-2"></i>
                    {t("amp.indicatormanager:view-indicator")}
                </Modal.Title>
            </Modal.Header>
            {indicator ?
                <Modal.Body className={styles.modal_body}>
                    <div className={styles.viewmodal_wrapper}>
                        {/* Core Indicator Information Section */}
                        <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                            <h4 className={styles.section_title} style={{color: '#0052CC', fontWeight: 'bold'}}>
                                <i className="fas fa-info-circle me-2"></i>
                                {t("amp.indicatormanager:core-info")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:indicator-name")}</b></div>
                                    <div className={`${styles.value} ${styles.important}`}>{indicator.name}</div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:indicator-code")}</b></div>
                                    <div className={styles.value}>{indicator.code}</div>
                                </Col>
                            </Row>
                            <Row className={styles.view_row}>
                                <Col md={12} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:indicator-description")}</b></div>
                                    <div className={styles.value} style={{fontStyle: 'italic'}}>
                                        {indicator.description === "" || !indicator.description ?
                                            <span className={styles.no_data}>{t("amp.indicatormanager:no-description-available")}</span> :
                                            indicator.description
                                        }
                                    </div>
                                </Col>
                            </Row>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:relevance-for-climate-change")}</b></div>
                                    <div className={styles.value}>{indicator.relevanceForClimateChange || <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:type")}</b></div>
                                    <div className={styles.value}>{getCategoryLabel(indicator.indicatorType)}</div>
                                </Col>
                            </Row>
                        </div>

                        {/* Categorization and Linkage Section */}
                        <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                            <h4 className={styles.section_title} style={{color: '#FF5630', fontWeight: 'bold'}}>
                                <i className="fas fa-link me-2"></i>
                                {t("amp.indicatormanager:categorization-linkage-info")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:outcome")}</b></div>
                                    <div className={styles.value}>{getOutcomeLabel(indicator.outcomeId)}</div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:output")}</b></div>
                                    <div className={styles.value}>{getOutputLabel(indicator.outputId)}</div>
                                </Col>
                            </Row>
                            <Row className={styles.view_row}>
                                <Col md={12} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:logframe-links")}</b></div>
                                    <div className={styles.value}>{getProgramLabel(indicator.programId)}</div>
                                </Col>
                            </Row>
                            <Row className={styles.view_row}>
                                <Col md={12} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:sectors")}</b></div>
                                    <div className={styles.value}>
                                        {sectorData.length > 0 ? (
                                            <ul style={{ paddingLeft: '1.2em', marginBottom: 0 }}>
                                                {sectorData.map((sector) => (
                                                    <li key={sector.id}>{sector.name}</li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>
                                        )}
                                    </div>
                                </Col>
                            </Row>
                        </div>

                        {/* Data Definition and Sourcing Section */}
                        <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                            <h4 className={styles.section_title} style={{color: '#36B37E', fontWeight: 'bold'}}>
                                <i className="fas fa-database me-2"></i>
                                {t("amp.indicatormanager:data")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:data")}</b></div>
                                    <div className={styles.value}>{indicator.data || <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:data-source")}</b></div>
                                    <div className={styles.value}>{indicator.dataSource || <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                </Col>
                            </Row>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:disaggregation")}</b></div>
                                    <div className={styles.value}>
                                        {indicator.disaggregation && indicator.disaggregation.length > 0 ? (
                                            <ul style={{ paddingLeft: '1.2em', marginBottom: 0 }}>
                                                {indicator.disaggregation.map((item: any, idx: number) => (
                                                    <li key={idx}>{getCategoryLabel(item)}</li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>
                                        )}
                                    </div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:unit-of-measure")}</b></div>
                                    <div className={styles.value}>{getCategoryLabel(indicator.unitOfMeasure)}</div>
                                </Col>
                            </Row>
                            <Row className={styles.view_row}>
                                <Col md={12} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:calculation-method")}</b></div>
                                    <div className={styles.value}>{indicator.calculationMethod || <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                </Col>
                            </Row>
                        </div>

                        {/* Disaggregation Values Section */}
                        <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                            <h4 className={styles.section_title} style={{color: '#FF8B00', fontWeight: 'bold'}}>
                                <i className="fas fa-table me-2"></i>
                                {t("amp.indicatormanager:disaggregation-values")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={12} style={{ width: '100%' }}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:disaggregation-values")}</b></div>
                                    <div className={styles.value} style={{ width: '100%' }}>
                                        {indicator.disaggregationValues && indicator.disaggregationValues.length > 0 ? (
                                            <div style={{ maxHeight: '40vh', overflow: 'auto', width: '100%' }}>
                                                {/* Transposed Table */}
                                                {(() => {
                                                  // Group disaggregationValues by parentCategoryId
                                                  const parentGroups: Record<number, any[]> = {};
                                                  (indicator.disaggregationValues || []).forEach((dv: any) => {
                                                    if (!parentGroups[dv.parentCategoryId]) parentGroups[dv.parentCategoryId] = [];
                                                    parentGroups[dv.parentCategoryId].push(dv);
                                                  });
                                                  // Build ordered parent list
                                                  const parentIds = Object.keys(parentGroups).map(Number);
                                                  // Helper to get child list for a parent (exclude childCategoryId=null)
                                                  const getChildren = (parentId: number) => parentGroups[parentId].filter(dv => dv.childCategoryId !== null);
                                                  // Helper to get dv for parent/child
                                                  const getDV = (parentId: number, childId: number|null) => parentGroups[parentId].find(dv => dv.childCategoryId === childId);
                                                  // Table header
                                                  return (
                                                    <table className={styles.hierarchical_table} style={{ width: '100%', borderCollapse: 'collapse', marginBottom: '1em' }}>
                                                      <thead>
                                                        {/* Row 1: Parent categories */}
                                                        <tr>
                                                          <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                            {/* Parent disaggregation label for first column */}
                                                            {parentIds.length > 0 ? getCategoryLabel(parentGroups[parentIds[0]][0].parentDisaggregationId) : t("amp.indicatormanager:no-parent-category")}
                                                          </th>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return (
                                                                <th key={parentId} colSpan={children.length * 2} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                                  {getCategoryLabel(parentId) || t("amp.indicatormanager:no-parent-category")}
                                                                </th>
                                                              );
                                                            } else {
                                                              // No children, colSpan=2
                                                              return (
                                                                <th key={parentId} colSpan={2} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                                  {getCategoryLabel(parentId) || t("amp.indicatormanager:no-parent-category")}
                                                                </th>
                                                              );
                                                            }
                                                          })}
                                                        </tr>
                                                        {/* Row 2: Child categories */}
                                                        {parentIds.length > 0 && getChildren(parentIds[0]).length > 0?(
                                                        <tr>
                                                          <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                            {/* Child category label for first column */}
                                                            {parentIds.length > 0 && getChildren(parentIds[0]).length > 0
                                                              ? getCategoryLabel(parentGroups[parentIds[0]][0].childDisaggregationId)
                                                              : t("amp.indicatormanager:no-child-category")}
                                                          </th>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return children.map(child => (
                                                                <th key={`child-label-${parentId}-${child.childCategoryId}`} colSpan={2} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                                  {getCategoryLabel(child.childCategoryId)}
                                                                </th>
                                                              ));
                                                            } else {
                                                              // No children, just Value/Date
                                                              return [
                                                                <th key={`parent-value-label-${parentId}`} colSpan={2} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                                  {getCategoryLabel(parentId)}
                                                                </th>
                                                              ];
                                                            }
                                                          })}
                                                        </tr>):
                                                            (<tr></tr>)}
                                                        {/* Row 3: Value/Date subcolumns */}
                                                        <tr>
                                                          <th style={{ border: '1px solid #ddd', padding: '8px' }}></th>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return children.map(child => [
                                                                <th key={`child-value-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                                  <div style={{ fontSize: '0.9em', color: '#888' }}>{t("amp.indicatormanager:value")}</div>
                                                                </th>,
                                                                <th key={`child-date-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                                                                  <div style={{ fontSize: '0.9em', color: '#888' }}>{t("amp.indicatormanager:date")}</div>
                                                                </th>
                                                              ]);
                                                            } else {
                                                              // No children, just Value/Date
                                                              return [
                                                                <th key={`parent-value-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>{t("amp.indicatormanager:value")}</th>,
                                                                <th key={`parent-date-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>{t("amp.indicatormanager:date")}</th>
                                                              ];
                                                            }
                                                          })}
                                                        </tr>
                                                      </thead>
                                                      <tbody>
                                                        {/* Original Base Value */}
                                                        <tr>
                                                          <td style={{ border: '1px solid #ddd', padding: '8px', fontWeight: 'bold' }}>{t("amp.indicatormanager:original-base-value")}</td>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return children.map(child => [
                                                                <td key={`base-value-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.base.originalValue}</td>,
                                                                <td key={`base-date-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.base.originalValueDate}</td>
                                                              ]);
                                                            } else {
                                                              // No children, use parent only
                                                              const dv = getDV(parentId, null);
                                                              return [
                                                                <td key={`base-value-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.base.originalValue}</td>,
                                                                <td key={`base-date-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.base.originalValueDate}</td>
                                                              ];
                                                            }
                                                          })}
                                                        </tr>
                                                        {/* Revised Base Value */}
                                                        <tr>
                                                          <td style={{ border: '1px solid #ddd', padding: '8px', fontWeight: 'bold' }}>{t("amp.indicatormanager:revised-base-value")}</td>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return children.map(child => [
                                                                <td key={`base-revised-value-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.base.revisedValue}</td>,
                                                                <td key={`base-revised-date-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.base.revisedValueDate}</td>
                                                              ]);
                                                            } else {
                                                              const dv = getDV(parentId, null);
                                                              return [
                                                                <td key={`base-revised-value-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.base.revisedValue}</td>,
                                                                <td key={`base-revised-date-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.base.revisedValueDate}</td>
                                                              ];
                                                            }
                                                          })}
                                                        </tr>
                                                        {/* Original Target Value */}
                                                        <tr>
                                                          <td style={{ border: '1px solid #ddd', padding: '8px', fontWeight: 'bold' }}>{t("amp.indicatormanager:original-target-value")}</td>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return children.map(child => [
                                                                <td key={`target-value-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.target.originalValue}</td>,
                                                                <td key={`target-date-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.target.originalValueDate }</td>
                                                              ]);
                                                            } else {
                                                              const dv = getDV(parentId, null);
                                                              return [
                                                                <td key={`target-value-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.target.originalValue}</td>,
                                                                <td key={`target-date-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.target.originalValueDate}</td>
                                                              ];
                                                            }
                                                          })}
                                                        </tr>
                                                        {/* Revised Target Value */}
                                                        <tr>
                                                          <td style={{ border: '1px solid #ddd', padding: '8px', fontWeight: 'bold' }}>{t("amp.indicatormanager:revised-target-value")}</td>
                                                          {parentIds.map(parentId => {
                                                            const children = getChildren(parentId);
                                                            if (children.length > 0) {
                                                              return children.map(child => [
                                                                <td key={`target-revised-value-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.target.revisedValue}</td>,
                                                                <td key={`target-revised-date-${parentId}-${child.childCategoryId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{child.target.revisedValueDate}</td>
                                                              ]);
                                                            } else {
                                                              const dv = getDV(parentId, null);
                                                              return [
                                                                <td key={`target-revised-value-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.target.revisedValue}</td>,
                                                                <td key={`target-revised-date-${parentId}`} style={{ border: '1px solid #ddd', padding: '8px' }}>{dv?.target.revisedValueDate}</td>
                                                              ];
                                                            }
                                                          })}
                                                        </tr>
                                                      </tbody>
                                                    </table>
                                                  );
                                                })()}
                                            </div>
                                        ) : (
                                            <div className={styles.no_data}>{t("amp.indicatormanager:no-disaggregation-values")}</div>
                                        )}
                                    </div>
                                </Col>
                            </Row>
                        </div>

                        {/* Responsibility and Frequency Section */}
                        <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                            <h4 className={styles.section_title} style={{color: '#5243AA', fontWeight: 'bold'}}>
                                <i className="fas fa-users me-2"></i>
                                {t("amp.indicatormanager:responsibility-frequency-info")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:responsible-organizations")}</b></div>
                                    <div className={styles.value}>
                                        {indicator.responsibleOrganizations && indicator.responsibleOrganizations.length > 0 ? (
                                            <ul style={{ paddingLeft: '1.2em', marginBottom: 0 }}>
                                                {getResponsibleOrgLabels(indicator.responsibleOrganizations).map((org, idx) => (
                                                    <li key={idx}>{org}</li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>
                                        )}
                                    </div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:frequency")}</b></div>
                                    <div className={styles.value}>{getCategoryLabel(indicator.frequency)}</div>
                                </Col>
                            </Row>
                        </div>

                        {/* Value Tracking Section */}
                        <div className={styles.section} style={{borderBottom: '2px solid #e0e0e0', marginBottom: 24, paddingBottom: 16}}>
                            <h4 className={styles.section_title} style={{color: '#36B37E', fontWeight: 'bold'}}>
                                <i className="fas fa-chart-line me-2"></i>
                                {t("amp.indicatormanager:value-tracking")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.section} style={{borderRight: '1px solid #e0e0e0', paddingRight: 16}}>
                                    <h5 className={styles.section_title} style={{color: '#36B37E', fontWeight: 'bold'}}>
                                        <i className="fas fa-chart-line me-2"></i>
                                        {t("amp.indicatormanager:base-values")}
                                    </h5>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:original-base-value")}</div>
                                        <div className={styles.value}>{indicator.base?.originalValue ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:original-value-date")}</div>
                                        <div className={styles.value}>{indicator.base?.originalValueDate ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:revised-value")}</div>
                                        <div className={styles.value}>{indicator.base?.revisedValue ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:revised-value-date")}</div>
                                        <div className={styles.value}>{indicator.base?.revisedValueDate ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                </Col>
                                <Col md={6} className={styles.section} style={{paddingLeft: 16}}>
                                    <h5 className={styles.section_title} style={{color: '#FF8B00', fontWeight: 'bold'}}>
                                        <i className="fas fa-bullseye me-2"></i>
                                        {t("amp.indicatormanager:target-values")}
                                    </h5>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:target-value")}</div>
                                        <div className={styles.value}>{indicator.target?.originalValue ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:target-value-date")}</div>
                                        <div className={styles.value}>{indicator.target?.originalValueDate ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:revised-value")}</div>
                                        <div className={styles.value}>{indicator.target?.revisedValue ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                    <div className={styles.view_item}>
                                        <div className={styles.label}>{t("amp.indicatormanager:revised-value-date")}</div>
                                        <div className={styles.value}>{indicator.target?.revisedValueDate ?? <span className={styles.no_data}>{t("amp.indicatormanager:no-data")}</span>}</div>
                                    </div>
                                </Col>
                            </Row>
                        </div>

                        {/* Other Considerations Section */}
                        <div className={styles.section} style={{marginTop: 24}}>
                            <h4 className={styles.section_title} style={{color: '#FF5630', fontWeight: 'bold'}}>
                                <i className="fas fa-ellipsis-h me-2"></i>
                                {t("amp.indicatormanager:other-considerations")}
                            </h4>
                            <Row className={styles.view_row}>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:table-header-creation-date")}</b></div>
                                    <div className={styles.value}>{indicator.creationDate}</div>
                                </Col>
                                <Col md={6} className={styles.view_item}>
                                    <div className={styles.label}><b>{t("amp.indicatormanager:ascending")}</b></div>
                                    <div className={styles.value}>
                                        <Badge content={indicator.ascending ? "success" : "secondary"} style={{fontWeight: 'bold'}}>
                                            {indicator.ascending ? t("amp.indicatormanager:yes") : t("amp.indicatormanager:no")}
                                        </Badge>
                                    </div>
                                </Col>
                            </Row>
                        </div>
                    </div>
                </Modal.Body> :
                <Modal.Body>
                    <div className={styles.error_state}>
                        <i className="fas fa-exclamation-triangle"></i>
                        <h3>{t("amp.indicatormanager:view-error")}</h3>
                    </div>
                </Modal.Body>
            }
        </Modal>
    );
};

export default ViewIndicatorModal;
