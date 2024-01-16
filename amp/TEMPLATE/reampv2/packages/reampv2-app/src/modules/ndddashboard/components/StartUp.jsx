import React, { useEffect } from 'react';
import { connect, useDispatch } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import { fetchProgramConfiguration }  from '../medashboard/reducers/fetchProgramConfigurationReducer';
import { Loading } from '../../../utils/components/Loading';
import defaultTrnPack from '../config/initialTranslations.json';
import {fetchFm} from "../medashboard/reducers/fetchFmReducer";
import { fetchSectorClassification } from '../medashboard/reducers/fetchSectorClassificationReducer';
import { getSettings} from '../reducers/fetchSettingsReducer';

export const NDDTranslationContext = React.createContext({ translations: defaultTrnPack });

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
const Startup = (props) => {
  const { translationPending, translations, _fetchTranslations, programConfigurationPending, fmReducerPending, sectorClassificationPending, settingsPending } = props;
  const dispatch = useDispatch();

  useEffect(() => {
    _fetchTranslations(defaultTrnPack);
    dispatch(getSettings());
    dispatch(fetchProgramConfiguration());
    dispatch(fetchFm());
    dispatch(fetchSectorClassification());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (translationPending || programConfigurationPending || fmReducerPending || sectorClassificationPending || settingsPending) {
    return (<Loading />);
  } else {
    document.title = translations['amp.ndd.dashboard:page-title'];
    return (
      <NDDTranslationContext.Provider value={{ translations }}>
        {props.children}
      </NDDTranslationContext.Provider>
    );
  }
}

const mapStateToProps = state => ({
  translationPending: state.translationsReducer.pending,
  translations: state.translationsReducer.translations,
  programConfigurationPending: state.programConfigurationReducer.pending,
  fmReducerPending: state.fetchFmReducer.loading,
  sectorClassificationPending: state.fetchSectorClassificationReducer.loading,
  settingsPending: state.fetchSettingsReducer.loading,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _fetchTranslations : fetchTranslations,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);
Startup.propTypes = {
  children: PropTypes.object.isRequired
};


