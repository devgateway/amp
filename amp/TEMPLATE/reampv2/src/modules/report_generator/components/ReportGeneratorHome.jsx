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
  getMetadata, fetchReport
} from '../actions/stateUIActions';

class ReportGeneratorHome extends Component {
  componentDidMount() {
    const { _getMetadata, _fetchReport, location } = this.props;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    const type = new URLSearchParams(location.search).get('type');
    if (id) {
      return _getMetadata(type).then(() => _fetchReport(id));
    } else {
      _getMetadata(type);
    }
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
  _fetchReport: (id) => fetchReport(id),
}, dispatch);

ReportGeneratorHome.propTypes = {
  _getMetadata: PropTypes.func.isRequired,
  _fetchReport: PropTypes.func.isRequired,
  location: PropTypes.object.isRequired,
};

ReportGeneratorHome.defaultProps = {};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
