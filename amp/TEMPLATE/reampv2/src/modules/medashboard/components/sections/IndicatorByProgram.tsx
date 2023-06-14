import React from 'react';
import Select from 'react-select';
import { Row, Col } from 'react-bootstrap';
import styles from './css/Styles.module.css';
import Gauge from '../charts/GaugesChart';

const options = [
    { value: 'chocolate', label: 'Chocolate' },
    { value: 'strawberry', label: 'Strawberry' },
    { value: 'vanilla', label: 'Vanilla' }
];

const sampleData = [
    {
        id: '1',
        value: 40,
    },
    {
        id: '2',
        value: 60,
    }
]

const IndicatorByProgram = () => {
    return (
        <div>
            <Col md={12}>
                <Row md={12} style={{
                    width: '100%',
                    marginLeft: 0,
                    paddingBottom: 20,
                    alignItems: 'center',
                    justifyContent: 'center',
                }}>
                    <Col md={9}>
                        <Select components={{
                            IndicatorSeparator: () => null
                        }}
                        className={styles.dropdown}
                        styles={{
                            control: (base, state) => ({
                                ...base,
                                backgroundColor: '#f5f7fb',
                            })
                        }}
                        options={options} />
                    </Col>
                    <Col md={3} style={{
                    }}>
                        <div style={{
                            border: '1px solid #ccc',
                        }}>
                        <div className="export-wrapper">
                            <div
                                className="download-image"
                            >
                                <span
                                    className="glyphicon glyphicon-cloud-download download-image-img" />
                            </div>
                        </div>
                        </div>

                    </Col>
                </Row>
                <Row style={{
                    paddingTop: 10,
                    paddingBottom: 30,
                    marginLeft: 0,
                    marginRight: 0,
                    borderTop: '1px solid #ccc',
                }}>
                    <Col md={12}>
                        <Select components={{
                            IndicatorSeparator: () => null
                        }}
                        className={styles.dropdown}
                        styles={{
                            control: (base, state) => ({
                                ...base,
                                backgroundColor: '#f5f7fb',
                            })
                        }}
                        options={options} />
                    </Col>
                </Row>
                <Row>
                    <Col md={8} style={{
                        width: 500,
                        height: 500,
                    }}>
                        <Gauge data={sampleData} height={300} width={400} innerValue={40} innerColor={''} />
                    </Col>
                </Row>
            </Col>
        </div>
    )
}

export default IndicatorByProgram;
