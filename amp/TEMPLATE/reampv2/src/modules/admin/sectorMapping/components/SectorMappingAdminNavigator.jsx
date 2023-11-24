import React, { useState } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
//import AdminNDDIndirectProgramApp from '../indirect';
import AdminSectorMappingApp from '../sector';

// eslint-disable-next-line no-unused-vars
import styles from './css/style.css';

const SectorMappingAdminNavigator = ({ translations }) => {
    // const [key, setKey] = useState('indirect');
    //const trnPrefix = 'amp.admin.sectorMapping:';
    return (
        // <AdminNDDIndirectProgramApp selected={key === 'indirect'} />
        <>
            <AdminSectorMappingApp />
        </>

    );
};

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations,
});

SectorMappingAdminNavigator.propTypes = {
    translations: PropTypes.object.isRequired
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SectorMappingAdminNavigator);
