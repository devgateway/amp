import React, { useState } from 'react';
import { Tabs, Tab } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import AdminNDDIndirectProgramApp from '../indirect';
import AdminNDDProgramApp from '../program';

// eslint-disable-next-line no-unused-vars
import styles from './css/style.css';

const NDDAdminNavigator = ({ translations }) => {
  const [key, setKey] = useState('indirect');
  const trnPrefix = 'amp.admin.ndd:';
  return (
    <Tabs
      id="programs-tab"
      activeKey={key}
      transition={false}
      onSelect={(k) => setKey(k)}
    >
      <Tab eventKey="indirect" title={translations[`${trnPrefix}menu-item-indirect-programs-mapping`]}>
        <AdminNDDIndirectProgramApp selected={key === 'indirect'} />
      </Tab>
      <Tab eventKey="program" title={translations[`${trnPrefix}menu-item-programs-mapping`]}>
        <AdminNDDProgramApp selected={key === 'program'} />
      </Tab>
    </Tabs>
  );
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
});

NDDAdminNavigator.propTypes = {
  translations: PropTypes.object.isRequired
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(NDDAdminNavigator);
