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
    this.state = { filters: undefined };
  }

  onApplyFilters = (data) => {
    this.setState({ filters: data });
  }

  render() {
    const { filters } = this.state;
    return (
      <Container fluid className="main-container">
        <Row>
          <HeaderContainer onApplyFilters={this.onApplyFilters} />
        </Row>
        <Row>
          <MainDashboardContainer filters={filters} />
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

NDDDashboardHome.contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);
