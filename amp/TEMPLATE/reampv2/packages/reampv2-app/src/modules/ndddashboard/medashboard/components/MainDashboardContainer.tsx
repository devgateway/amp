import React, { Suspense } from 'react'
import { Col, Row } from 'react-bootstrap';

const LazyLeftSection = React.lazy(() => import('./sections/LeftSection'));
const LazyRightSection = React.lazy(() => import('./sections/RightSection'));


const MainDashboardContainer = (props: any) => {
    const { filters, settings } = props;

    return (
        <>
            <Row md={6} lg={12} style={{
                display: 'flex',
                borderTop: 'none'
            }}>
                <Col md={12} lg={6} style={{paddingRight: 0, paddingLeft: 0, paddingTop: 20, borderRight: '1px solid #ddd'}}>
                    <Suspense fallback={<div className="loading" />}>
                        <LazyLeftSection filters={filters} settings={settings}/>
                    </Suspense>
                </Col>
                <Col md={12} lg={6} style={{ paddingRight: 0, paddingLeft: 0,  paddingTop: 20, borderRight: '1px solid #ddd' }}>
                    <Suspense fallback={<div />}>
                        <LazyRightSection filters={filters} settings={settings}/>
                    </Suspense>
                </Col>
            </Row>
        </>
    )
}

export default MainDashboardContainer;
