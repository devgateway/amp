import React, { useEffect } from 'react';
import { connect, useDispatch, useSelector } from 'react-redux';
import { bindActionCreators } from 'redux';
import fetchTranslations from '../../../utils/actions/fetchTranslations';
import { Loading } from '../../../utils/components/Loading';
// eslint-disable-next-line import/no-unresolved
import { DefaultTranslationPackTypes } from '../types';
import { getSettings } from '../reducers/fetchSettingsReducer';

export const AdminIndicatorManagerContext = React.createContext({});

interface StartupProps {
  defaultTrnPack: any;
  _fetchTranslations: any;
  children: React.ReactNode;
  translations: DefaultTranslationPackTypes;
  translationPending: boolean;
  api?: any;
}

/**
 * Component used to load everything we need before launching the APP
 * TODO check if we should abstract it to a Load Translations component to avoid copy ^
 */
const Startup: React.FC<StartupProps> = (props: any) => {
  const {
    defaultTrnPack, _fetchTranslations, children, translations, translationPending, api
  } = props;

  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getSettings());
    _fetchTranslations(defaultTrnPack);
    // eslint-disable-next-line
  }, []);

  if (translationPending) {
    return (<Loading />);
  } else {
    document.title = translations['amp.indicatormanager:page-title'];
    return (
      <AdminIndicatorManagerContext.Provider value={{ translations, api }}>
        {children}
      </AdminIndicatorManagerContext.Provider>
    );
  }
};

const mapStateToProps = (state: any) => ({
  translationPending: state.translationsReducer.pending,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = (dispatch: any) => bindActionCreators({ _fetchTranslations: fetchTranslations }, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Startup);
