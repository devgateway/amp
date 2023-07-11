import React, { useEffect } from 'react';
import { connect, useDispatch } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import { fetchProgramConfiguration }  from '../medashboard/reducers/fetchProgramConfiguration';
import { Loading } from '../../../utils/components/Loading';
import defaultTrnPack from '../config/initialTranslations.json';

export const NDDTranslationContext = React.createContext({ translations: defaultTrnPack });

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
const Startup = (props) => {
  const { translationPending, translations, _fetchTranslations, programConfigurationPending } = props;
  const dispatch = useDispatch();

  useEffect(() => {
    _fetchTranslations(defaultTrnPack);

    dispatch(fetchProgramConfiguration());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (translationPending || programConfigurationPending) {
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
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _fetchTranslations : fetchTranslations,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);
Startup.propTypes = {
  children: PropTypes.object.isRequired
};


