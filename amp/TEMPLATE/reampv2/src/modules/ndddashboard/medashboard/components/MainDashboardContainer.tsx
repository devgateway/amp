import React from 'react'
import { Col, Row } from 'react-bootstrap';
import LeftSection from './sections/LeftSection';

const MainDashboardContainer = (props: any) => {
    const { filters } = props;

    return (
        <>
            <Row style={{
                display: 'flex',
                borderTop: 'none'
            }}>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0,  paddingTop: 20, borderRight: '1px solid #ddd' }}>
                    <LeftSection filters={filters}/>
                </Col>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0 }}>
                </Col>
            </Row>
        </>
    )
}

export default MainDashboardContainer;
