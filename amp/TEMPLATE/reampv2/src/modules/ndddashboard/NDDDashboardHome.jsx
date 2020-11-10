import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { NDDTranslationContext } from './components/StartUp';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import MainDashboardContainer from './components/MainDashboardContainer';

class NDDDashboardHome extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (<Container fluid>
      <Row>
        <Col>top area for filters/settings/share</Col>
      </Row>
      <Row>
        <Col>
          <MainDashboardContainer/>
        </Col>
      </Row>
    </Container>);
  }
}

const mapStateToProps = state => {
  return {};
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

NDDDashboardHome.contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);

