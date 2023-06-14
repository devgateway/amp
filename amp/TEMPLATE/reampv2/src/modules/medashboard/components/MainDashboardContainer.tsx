import React from 'react'
import { Col, Row } from 'react-bootstrap';
import Gauge from './charts/GaugesChart';
import LeftSection from './sections/LeftSection';

const MainDashboardContainer = () => {
    return (
        <>
            <Row style={{
                marginRight: '-15px', marginLeft: '-15px', border: '1px solid #ddd', borderBottom: 'none'
            }}>
                <Col md={12} style={{ paddingRight: 0, paddingLeft: 0 }}>
                    <div className="section_title">
                        <h3>Dashboard</h3>
                    </div>
                </Col>
            </Row>
            <Row style={{
                marginRight: '-15px',
                marginLeft: '-15px',
                border: '1px solid #ddd',
                display: 'flex',
                borderTop: 'none',
                backgroundColor: 'white',
                paddingTop: 20
            }}>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0 }}>
                    <LeftSection />
                    {/* <Gauge data={[]} height={300} width={500} innerValue={60} innerColor={''} /> */}
                </Col>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0 }}>
                    right section
                </Col>
            </Row>
        </>
    )
}

export default MainDashboardContainer;
