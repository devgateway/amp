import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Container } from 'semantic-ui-react';
import { ReportGeneratorContext } from './StartUp';
import MainHeader from './MainHeader';
import MainContent from './MainContent';
import FiltersAndSettings from './FiltersAndSettings';
import {
  getMetadata
} from '../actions/stateUIActions';

class ReportGeneratorHome extends Component {
  componentDidMount() {
    const { _getMetadata } = this.props;
    _getMetadata();
  }

  render() {
    return (
      <Container>
        <MainHeader />
        <FiltersAndSettings />
        <MainContent />
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getMetadata: (data) => getMetadata(data),
}, dispatch);

ReportGeneratorHome.propTypes = {
  _getMetadata: PropTypes.func.isRequired,
};

ReportGeneratorHome.defaultProps = {};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
