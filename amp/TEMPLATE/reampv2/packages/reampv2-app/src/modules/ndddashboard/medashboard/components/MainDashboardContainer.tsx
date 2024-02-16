import React, {useEffect} from 'react'
import { Col, Row } from 'react-bootstrap';
import LeftSection from './sections/LeftSection';
import RightSection from './sections/RightSection';


const MainDashboardContainer = (props: any) => {
    const { filters, settings } = props;

    return (
        <>
            <Row sm={12} style={{
                display: 'flex',
                borderTop: 'none'
            }}>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0,  paddingTop: 20, borderRight: '1px solid #ddd' }}>
                    <LeftSection filters={filters} settings={settings}/>
                </Col>
                <Col md={6} style={{ paddingRight: 0, paddingLeft: 0,  paddingTop: 20, borderRight: '1px solid #ddd' }}>
                    <RightSection filters={filters} settings={settings}/>
                </Col>
            </Row>
        </>
    )
}

export default MainDashboardContainer;
