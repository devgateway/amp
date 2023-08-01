import React, {Component} from 'react';
import './css/style.css';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import {getNDD, getNDDError, getNDDPending, getPrograms, getProgramsPending} from '../reducers/startupReducer';
import fetchNDD from '../actions/fetchNDD';
import fetchPrograms from '../actions/fetchAvailablePrograms';
import fetchLayout from '../actions/fetchLayout';
import fetchSettings from '../actions/fetchSettings';
import FormPrograms from './FormPrograms';
import BlockUI from './common/BlockUI';

class Main extends Component {
  constructor(props) {
    super(props);
    this.shouldComponentRender = this.shouldComponentRender.bind(this);
    this.state = {};
  }

  componentDidMount() {
    const {
      _fetchNDD, _fetchPrograms, api, _fetchLayout, _fetchSettings
    } = this.props;
    _fetchSettings().then(settings => {
      _fetchLayout().then((layout) => {
        if (layout && layout.logged && layout.administratorMode === true) {
          _fetchNDD(api.mappingConfig);
          _fetchPrograms(api.programs);
          this.setState({ isSuperAdmin: layout.email.indexOf('super') === 0, settings });
        } else {
          window.location.replace('/login.do');
        }
      }).catch(e => console.error(e));
    }).catch(e => console.error(e));
  }

  shouldComponentRender() {
    const { pendingNDD, pendingPrograms } = this.props;
    return !pendingNDD && !pendingPrograms;
  }

  render() {
    const {
      ndd, programs, api, trnPrefix, isIndirect, indirectProgramUpdatePending
    } = this.props;
    const { translations } = this.context;
    const { isSuperAdmin, settings } = this.state;
    if (!this.shouldComponentRender() || ndd.length === 0) {
      return <div className="loading">{translations[`${trnPrefix}loading`]}</div>;
    } else {
      return (
        <div className="ndd-container">
          <NDDContext.Provider value={{
            ndd, translations, programs, api, trnPrefix, isIndirect, isSuperAdmin, settings
          }}>
            <div className="col-md-12">
              <div>
                <FormPrograms />
              </div>
            </div>
            <BlockUI blocking={indirectProgramUpdatePending} />
          </NDDContext.Provider>
        </div>
      );
    }
  }
}

Main.contextType = NDDContext;

Main.propTypes = {
  _fetchNDD: PropTypes.func.isRequired,
  _fetchPrograms: PropTypes.func.isRequired,
  _fetchLayout: PropTypes.func.isRequired,
  _fetchSettings: PropTypes.func.isRequired,
  api: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  error: getNDDError(state.startupReducer),
  ndd: getNDD(state.startupReducer),
  programs: getPrograms(state.startupReducer),
  pendingNDD: getNDDPending(state.startupReducer),
  pendingPrograms: getProgramsPending(state.startupReducer),
  translations: state.translationsReducer.translations,
  indirectProgramUpdatePending: state.updateActivitiesReducer.indirectProgramUpdatePending
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _fetchNDD: fetchNDD,
  _fetchPrograms: fetchPrograms,
  _fetchLayout: fetchLayout,
  _fetchSettings: fetchSettings
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Main);
