import React, {useEffect} from "react";
import styles from './css/Styles.module.css';
import {Col, Row} from "react-bootstrap";
import ChartUtils from "../../utils/chart";
import {IndicatorObjectType, SectorObjectType} from "../../../../admin/indicator_manager/types";
import {DefaultTranslations} from "../../types";
import IndicatorProgressChart from "../IndicatorProgressChart";
import {useSelector} from "react-redux";

interface IndicatorBySectorProps {
    translations: DefaultTranslations;
    filters: any;
    settings: any;
    index: number;
    indicators: IndicatorObjectType[];
}

const IndicatorBySector: React.FC<IndicatorBySectorProps> = (props) => {
    const { translations, filters, settings, index, indicators } = props;

    // @ts-ignore
    const globalSettings = useSelector(state => state.fetchSettingsReducer.settings);
    const selectedSector: SectorObjectType = useSelector((state: any) => state.fetchSectorClassificationReducer.selectedSector);

    const [selectedIndicatorId, setSelectedIndicatorId] = React.useState<number | null>(null);
    const [selectedIndicator, setSelectedIndicator] = React.useState<IndicatorObjectType | null>(null);

    const handleIndicatorChange = (value: number) => {
        const indicator = indicators.find((item: IndicatorObjectType) => item.id === value);
        if (indicator) setSelectedIndicator(indicator);
    }


    useEffect(() => {
        if (indicators.length > 0) {
            setSelectedIndicatorId(indicators[0].id);
            setSelectedIndicator(indicators[0])
        }
    }, [indicators]);

    useEffect(() => {
        if (selectedIndicatorId) {
            handleIndicatorChange(selectedIndicatorId);
        }
    }, [selectedIndicatorId]);

    return (
        <div>
            <Col md={12} style={{
                borderBottom: '1px solid #ddd',
            }}>
                <div id={`indicators-by-sector-${index}`}>
                    <Row md={12} style={{
                        width: '100%',
                        marginLeft: 0,
                        paddingBottom: 10,
                        paddingTop: 20,
                        alignItems: 'center',
                        justifyContent: 'center',
                    }}>

                        <Col md={11} style={{
                            paddingRight: 10
                        }}>
                            {indicators.length === 0 ? (
                                <select
                                    style={{
                                        backgroundColor: '#f3f5f8',
                                        boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                    }}
                                    className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                    <option>{translations['amp.ndd.dashboard:me-no-data']}</option>
                                </select>
                            ) : (
                                <select
                                    defaultValue={indicators[0].id}
                                    onChange={(e) => {
                                        setSelectedIndicator(null);
                                        setSelectedIndicatorId(parseInt(e.target.value))
                                    }}
                                    style={{
                                        backgroundColor: '#f3f5f8',
                                        boxShadow: 'rgba(0, 0, 0, 0.16) 0px 1px 4px'
                                    }}
                                    className={`form-control like-btn-sm ftype-options ${styles.dropdown}`}>
                                    {indicators.map((item, index: number) => (<option key={index} value={item.id}>{item.name.length > 80 ? item.name.slice(0, 80) + '...' : item.name}</option>))}
                                </select>
                            )}
                            <span className="cheat-lineheight" />
                        </Col>
                        <Col md={1} style={{
                            padding: 0,
                            cursor: 'pointer'
                        }}>
                            <div className={styles.download_btn_wrapper} onClick={() => ChartUtils.downloadChartImage(
                                `${translations['amp.ndd.dashboard:me-dashboard']}-sector`, `indicators-by-sector-${index}`)}>
                                <span className="glyphicon glyphicon-cloud-download" />
                            </div>
                        </Col>
                    </Row>

                    {
                        (selectedIndicator) ? (
                            <IndicatorProgressChart
                                title={translations['amp.ndd.dashboard:sector-progress']}
                                section="right"
                                translations={translations}
                                filters={filters}
                                settings={settings}
                                index={index}
                                indicator={selectedIndicator}
                                globalSettings={globalSettings}
                                sectorName={selectedSector.name}
                                showSectorName={true}
                            />
                        ) : (
                            <div className="loading"></div>
                        )
                    }
                </div>
            </Col>
        </div>
    )
};

export default React.memo(IndicatorBySector);
