import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Container, Header, Loader } from 'semantic-ui-react';
import { ReportGeneratorContext } from './StartUp';

class ReportGeneratorHome extends Component {
  render() {
    return (
      <Container>
        <Header>
          Fomantic-UI & Semantic-UI-React
          <Loader active inline className="slow red" />
          <Loader active inline className="fast green" />
        </Header>
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
