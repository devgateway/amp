import React, { useState } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import AdminSectorMappingApp from '../sector';

import styles from './css/style.css';

const SectorMappingAdminNavigator = ({ translations }) => {
    return ( <AdminSectorMappingApp /> );
};

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations,
});

SectorMappingAdminNavigator.propTypes = {
    translations: PropTypes.object.isRequired
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SectorMappingAdminNavigator);
