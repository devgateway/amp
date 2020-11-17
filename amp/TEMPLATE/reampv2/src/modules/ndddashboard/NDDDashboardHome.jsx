import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { NDDTranslationContext } from './components/StartUp';
import MainDashboardContainer from './components/MainDashboardContainer';
import HeaderContainer from './components/HeaderContainer';

class NDDDashboardHome extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <Container fluid className="main-container">
        <Row>
          <HeaderContainer />
        </Row>
        <Row>
          <Col md={12}>
            <MainDashboardContainer />
          </Col>
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

NDDDashboardHome.contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);
