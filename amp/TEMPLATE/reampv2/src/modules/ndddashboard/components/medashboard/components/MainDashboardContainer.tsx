import React from 'react'
import { Col, Row } from 'react-bootstrap';
import LeftSection from './sections/LeftSection';

const MainDashboardContainer = () => {
    return (
        <>
            <Row style={{
                marginRight: '-15px', marginLeft: '-15px', border: '1px solid #ddd', borderBottom: 'none'
            }}>
                <Col md={12} style={{ paddingRight: 0, paddingLeft: 0 }}>
                    <div className="section_title">
                        <h3>M&E Dashboard</h3>
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
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0, borderRight: '1px solid #ddd' }}>
                    <LeftSection />
                </Col>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0 }}>
                </Col>
            </Row>
        </>
    )
}

export default MainDashboardContainer;
