import React from 'react';
import { connect } from 'react-redux';
import MainDashboardContainer from './components/MainDashboardContainer';
import { Container, Row, Col } from 'react-bootstrap';


const MeDashboardHome = (props: any) => {
  return (
      <Container>
        <Row md={12} style={{
          paddingTop: 10,
          paddingBottom: 30,
          borderBottom: '1px solid #ccc',
        }}>
          <Col md={12}>
            <span style={{
              letterSpacing: 0.4,
              lineHeight: 1.5,
            }}>
              The National Indicators page lists the indicators which are monitored at national level by the various organizations,
              the latter being responsible for their entries and updates within the platform.
              The various graphs of this page indicate, for each indicator, a base and a target value which,
              together with the reference value, allow to measure the progress of an indicator towards its objective.
            </span>
          </Col>
        </Row>
        <MainDashboardContainer />
      </Container>

  );
}

const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => ({});

export default connect(mapStateToProps, mapDispatchToProps)(MeDashboardHome);
