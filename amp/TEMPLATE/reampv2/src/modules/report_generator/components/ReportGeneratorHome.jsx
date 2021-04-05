import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { ReportGeneratorContext } from './StartUp';

class ReportGeneratorHome extends Component {
  render() {
    return <div>TO BE IMPLEMENTED</div>;
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({ }, dispatch);

ReportGeneratorHome.propTypes = {};

ReportGeneratorHome.defaultProps = {};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
