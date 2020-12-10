import React, { Component } from 'react';
import './css/style.css';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { NDDContext } from './Startup';
import * as Constants from '../constants/Constants';
import {
  getNDD,
  getNDDError,
  getNDDPending,
  getPrograms,
  getProgramsError,
  getProgramsPending
} from '../reducers/startupReducer';
import fetchNDD from '../actions/fetchNDD';
import fetchPrograms from '../actions/fetchAvailablePrograms';
import { SRC_PROGRAM, VALUE, DST_PROGRAM } from '../constants/Constants';
import FormPrograms from './FormPrograms';

class Main extends Component {
  constructor(props) {
    super(props);
    this.shouldComponentRender = this.shouldComponentRender.bind(this);
  }

  componentDidMount() {
    const { fetchNDD, fetchPrograms } = this.props;
    fetchNDD();
    fetchPrograms();
  }

  shouldComponentRender() {
    const { pendingNDD, pendingPrograms } = this.props;
    return !pendingNDD && !pendingPrograms;
  }

  render() {
    const { ndd, programs } = this.props;
    const { translations } = this.context;

    if (!this.shouldComponentRender() || ndd.length === 0) {
      return <div>loading...</div>;
    } else {
      return (
        <div className="ndd-container">
          <NDDContext.Provider value={{ ndd, translations, programs }}>
            <div className="col-md-12">
              <div>
                <h2 className="title">{translations[`${Constants.TRN_PREFIX}title`]}</h2>
                <FormPrograms />
              </div>
            </div>
          </NDDContext.Provider>
        </div>
      );
    }
  }
}

Main.contextType = NDDContext;

const mapStateToProps = state => ({
  error: getNDDError(state.startupReducer),
  ndd: getNDD(state.startupReducer),
  programs: getPrograms(state.startupReducer),
  pendingNDD: getNDDPending(state.startupReducer),
  pendingPrograms: getProgramsPending(state.startupReducer),
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({
  fetchNDD,
  fetchPrograms
}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Main);
