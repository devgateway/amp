import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Container } from 'semantic-ui-react';
import { ReportGeneratorContext } from './StartUp';
import MainHeader from './MainHeader';

class ReportGeneratorHome extends Component {
  render() {
    return (
      <Container>
        <MainHeader />
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

ReportGeneratorHome.propTypes = {};

ReportGeneratorHome.defaultProps = {};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
