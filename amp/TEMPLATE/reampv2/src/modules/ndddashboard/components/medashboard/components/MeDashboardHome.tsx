import React from 'react';
import { Container } from 'react-bootstrap';
import { connect } from 'react-redux';
import PrintDummy from '../../../../sscdashboard/utils/PrintDummy';
import './print.css';
import MainDashboardContainer from './MainDashboardContainer';

export const SRC_DIRECT = '0';

const MeDashboardHome = (props: any) => {

  return (
    <Container fluid className="main-container" id="me-main-container">
      <MainDashboardContainer />
      <PrintDummy />
    </Container>
  );
}

const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => ({});

export default connect(mapStateToProps, mapDispatchToProps)(MeDashboardHome);
