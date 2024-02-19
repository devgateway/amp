import React from 'react';
import { connect } from 'react-redux';
import MainDashboardContainer from './components/MainDashboardContainer';
import { Container, Row, Col } from 'react-bootstrap';
import PrintDummy from "../../sscdashboard/utils/PrintDummy";

interface MeDashboardHomeProps {
  filters: any;
  dashboardSettings?: any;
  settings?: any;
  globalSettings?: any;
  translations?: any;
}

const MeDashboardHome: React.FC<MeDashboardHomeProps> = (props) => {
  const { filters, settings, translations } = props;

  return (
      <Container style={{
        marginRight: '-15px', marginLeft: '-15px', border: '1px solid #ddd', borderBottom: 'none', backgroundColor: '#ffffff'
      }}>
        <Row md={12} style={{
          paddingTop: 10,
          paddingBottom: 30,
          borderBottom: '1px solid #ccc',
          backgroundColor: '#ffffff !important',
        }}>
          <Col md={12}>
            <span style={{
              letterSpacing: 0.4,
              lineHeight: 1.5,
            }}>
             {translations["amp.ndd.dashboard:me-header-text"]}
            </span>
          </Col>
        </Row>
        <MainDashboardContainer filters={filters} settings={settings} translations={translations} />
        <PrintDummy/>
      </Container>

  );
}

const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});

const mapDispatchToProps = (dispatch: any) => ({});

export default connect(mapStateToProps, mapDispatchToProps)(MeDashboardHome);
