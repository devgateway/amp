import React, { Component } from 'react';
import { Tabs, Tab, Nav } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import AdminNDDIndirectProgramApp from '../indirect';
import AdminNDDProgramApp from '../program';

// eslint-disable-next-line no-unused-vars
import styles from './css/style.css';

class NDDAdminNavigator extends Component {
  render() {
    const { translations } = this.props;
    const trnPrefix = 'amp.admin.ndd:';
    return (
      <Tabs defaultActiveKey="indirect" transition={false}>
        <Tab eventKey="indirect" title={translations[`${trnPrefix}menu-item-indirect-programs-mapping`]}>
          <AdminNDDIndirectProgramApp />
        </Tab>
        <Tab eventKey="program" title={translations[`${trnPrefix}menu-item-programs-mapping`]}>
          <AdminNDDProgramApp />
        </Tab>
      </Tabs>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
});

NDDAdminNavigator.propTypes = {
  translations: PropTypes.array.isRequired
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(NDDAdminNavigator);
