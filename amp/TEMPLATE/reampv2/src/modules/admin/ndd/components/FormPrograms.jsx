import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import ProgramSelectGroupList from './ProgramSelectGroupList';
import Header from './Header';
import {
  DST_PROGRAM,
  PROGRAM,
  PROGRAM_MAPPING,
  SRC_PROGRAM,
  TYPE_SRC,
  TYPE_DST
} from '../constants/Constants';
import * as Utils from '../utils/Utils';
import { sendNDDError, sendNDDPending } from '../reducers/saveNDDReducer';
import { updateActivitiesError, updateActivitiesPending } from '../reducers/updateActivitiesReducer';
import saveNDD from '../actions/saveNDD';
import updateActivities from '../actions/updateActivities';
import Notifications from './Notifications';
import ProgramsHeader from './ProgramsHeader';

class FormPrograms extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      validationErrors: undefined,
      src: undefined,
      dst: undefined,
      programs: undefined,
      saved: false,
      updatedActivities: false,
      blockUI: false
    };
    this.addRow = this.addRow.bind(this);
    this.saveAll = this.saveAll.bind(this);
    this.onRowChange = this.onRowChange.bind(this);
    this.remove = this.remove.bind(this);
    this.clearMessages = this.clearMessages.bind(this);
    this.onChangeMainProgram = this.onChangeMainProgram.bind(this);
    this.clearAll = this.clearAll.bind(this);
  }

  componentDidMount() {
    const {
      ndd, programs, translations, trnPrefix
    } = this.context;
    document.title = translations[`${trnPrefix}page-title`];
    // Load main programs.
    this.setState(previousState => {
      if (ndd[SRC_PROGRAM]) {
        const src = { id: ndd[SRC_PROGRAM].id, value: ndd[SRC_PROGRAM].value };
        return { src };
      }
      return { src: undefined };
    });
    this.setState(previousState => {
      if (ndd[DST_PROGRAM]) {
        const dst = { id: ndd[DST_PROGRAM].id, value: ndd[DST_PROGRAM].value };
        return { dst };
      }
      return { dst: undefined };
    });

    // Available programs.
    this.setState(previousState => ({ programs }));

    // Load saved mapping.
    this.setState(previousState => {
      const data = [...previousState.data];
      if (ndd[PROGRAM_MAPPING]) {
        ndd[PROGRAM_MAPPING].forEach(pm => {
          const fullTreeSrc = Utils.findProgramInTree(pm[SRC_PROGRAM], ndd, TYPE_SRC);
          const fullTreeDst = Utils.findProgramInTree(pm[DST_PROGRAM], ndd, TYPE_DST);
          const pair = {};
          pair[SRC_PROGRAM] = fullTreeSrc;
          pair[DST_PROGRAM] = fullTreeDst;
          // Extra validation.
          if (pair[SRC_PROGRAM] && pair[SRC_PROGRAM].lvl3 && pair[DST_PROGRAM] && pair[DST_PROGRAM].lvl3) {
            pair.id = `${pair[SRC_PROGRAM].lvl3.id}${pair[DST_PROGRAM].lvl3.id}`;
            data.push(pair);
          }
        });
      }
      return { data: this.sortPrograms(data) };
    });
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
  }

  /**
   * Sort by SRC lvl1->lvl2->lvl3 plus DST lvl1->lvl2->lvl3 if needed.
   * @param data
   * @returns {*[]|*}
   */
  sortPrograms(data) {
    if (data && data.length > 0) {
      const sortedData = data.sort((a, b) => {
        const compare = (a[SRC_PROGRAM].lvl1.value + a[SRC_PROGRAM].lvl2.value + a[SRC_PROGRAM].lvl3.value)
          .localeCompare(b[SRC_PROGRAM].lvl1.value + b[SRC_PROGRAM].lvl2.value + b[SRC_PROGRAM].lvl3.value);
        if (compare === 0) {
          const compare2 = (a[DST_PROGRAM].lvl1.value + a[DST_PROGRAM].lvl2.value + a[DST_PROGRAM].lvl3.value)
            .localeCompare(b[DST_PROGRAM].lvl1.value + b[DST_PROGRAM].lvl2.value + b[DST_PROGRAM].lvl3.value);
          return compare2;
        }
        return compare;
      });
      return sortedData;
    }
    return [];
  }

  addRow() {
    this.clearMessages();
    this.setState(previousState => {
      const data = [...previousState.data];
      const pair = {
        [SRC_PROGRAM]: {},
        [DST_PROGRAM]: {},
        id: Math.random() * -1
      };
      data.push(pair);
      setTimeout(() => (window.scrollTo(0, document.body.scrollHeight)), 500);
      return { data };
    });
  }

  onRowChange(level1, level2, level3, type, id) {
    const { data } = this.state;
    this.clearMessages();
    // Find and remove.
    const pair = data.splice(data.findIndex(i => i[type + PROGRAM].id === id), 1);
    pair[0][type + PROGRAM].lvl1 = level1;
    pair[0][type + PROGRAM].lvl2 = level2;
    pair[0][type + PROGRAM].lvl3 = level3;
    data.push(pair[0]);
    this.setState(data);
  }

  remove(row) {
    const { translations, trnPrefix } = this.context;
    if (window.confirm(translations[`${trnPrefix}confirm-remove-row`])) {
      this.clearMessages();
      this.setState(previousState => {
        const data = [...previousState.data];
        data.splice(data.findIndex(i => i.id === row.id), 1);
        return { data };
      });
    }
  }

  clearMessages() {
    this.setState({
      validationErrors: undefined, saved: false, updatedActivities: false, blockUI: false
    });
  }

  onUpdateActivities = () => {
    const { translations, updateActivities } = this.props;
    const { trnPrefix } = this.context;
    this.clearMessages();
    // TODO: block UI until it finishes.
    this.setState({
      blockUI: true
    });
    updateActivities();
  }

  saveAll() {
    const { data, src, dst } = this.state;
    const { saveNDD, translations } = this.props;
    const { api, trnPrefix } = this.context;
    const validateMappings = Utils.validate(data);
    if (validateMappings === 0) {
      const validateMain = Utils.validateMainPrograms(src, dst);
      if (validateMain === 0) {
        const mappings = [];
        data.forEach(pair => {
          mappings.push({
            [SRC_PROGRAM]: pair[SRC_PROGRAM].lvl3.id,
            [DST_PROGRAM]: pair[DST_PROGRAM].lvl3.id,
          });
        });
        saveNDD(src, dst, mappings, api.programsSave, api.mappingSave);
        this.clearMessages();
        this.setState({ saved: true });
      } else {
        this.setState({
          validationErrors: translations[`${trnPrefix}validation_error_${validateMain}`],
          saved: false
        });
      }
    } else {
      this.setState({
        validationErrors: translations[`${trnPrefix}validation_error_${validateMappings}`],
        saved: false
      });
    }
  }

  onChangeMainProgram(type, program) {
    const { translations, trnPrefix } = this.context;
    const { data, src, dst } = this.state;
    this.src_ = src;
    this.dst_ = dst;
    const newProgram = (program && program.length > 0) ? program[0] : {};
    const oldProgram = (this.state[type] ? this.state[type] : {});
    let autoAddRow = false;
    if (oldProgram.id !== newProgram.id) {
      if (oldProgram.id !== undefined) {
        if (data.length === 0 || window.confirm(translations[`${trnPrefix}warning_on_change_main_programs`])) {
          if (newProgram.id !== undefined) {
            // Old Program -> New Program.
            this.setState(previousState => ({ [type]: newProgram }));
            this[`${type}_`] = newProgram;
            autoAddRow = true;
          } else {
            // Old Program -> Nothing.
            this.setState(previousState => ({ [type]: undefined }));
            this[`${type}_`] = undefined;
          }
          this.clearAll();
        } else {
          // Revert to previous Program.
          this.setState(previousState => previousState);
          // TODO: set focus in the selector.
        }
      } else {
        // Nothing -> Program.
        this.setState(previousState => ({ [type]: newProgram }));
        this[`${type}_`] = newProgram;
        autoAddRow = true;
      }
    }
    // Note src_ and dst_ because setState() is not immediate.
    if (this.src_ && this.dst_ && autoAddRow) {
      this.addRow();
    }
  }

  clearAll() {
    this.setState({
      data: []
    });
  }

  revertAllChanges() {
    window.location.reload();
  }

  render() {
    const {
      data, validationErrors, src, dst, saved, updatedActivities
    } = this.state;
    const {
      error, pending, translations, updating, errorUpdating
    } = this.props;
    const { trnPrefix } = this.context;
    const messages = [];
    if (error) {
      messages.push({ isError: true, text: error.toString() });
    }
    if (validationErrors) {
      messages.push({ isError: true, text: validationErrors });
    }
    if (saved) {
      messages.push({ isError: false, text: translations[`${trnPrefix}notification-saved-ok`] });
    }
    if (updatedActivities) {
      messages.push({
        isError: false,
        text: translations[`${trnPrefix}update-activities-successful`]
      });
    }
    if (updating) {
      messages.push({
        isError: false,
        text: translations[`${trnPrefix}update-activities-wait`]
      });
    }
    if (errorUpdating) {
      messages.push({
        isError: true,
        text: translations[`${trnPrefix}update-activities-error`]
      });
    }

    return (
      <div className="form-container">
        <ProgramsHeader onChange={this.onChangeMainProgram} src={src} dst={dst} key={Math.random()} busy={updating} />
        <Header
          onAddRow={this.addRow}
          onSaveAll={this.saveAll}
          onRevertAll={this.revertAllChanges}
          onUpdateActivities={this.onUpdateActivities}
          src={src}
          dst={dst}
          busy={updating || pending} />
        <Notifications messages={messages} />
        <ProgramSelectGroupList
          list={data}
          onChange={this.onRowChange}
          remove={this.remove}
          src={src}
          dst={dst}
          busy={updating} />
      </div>
    );
  }
}

FormPrograms.contextType = NDDContext;

FormPrograms.propTypes = {
  translations: PropTypes.array.isRequired
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  error: sendNDDError(state.saveNDDReducer),
  pending: sendNDDPending(state.saveNDDReducer),
  updating: updateActivitiesPending(state.updateActivitiesReducer),
  errorUpdating: updateActivitiesError(state.updateActivitiesReducer)
});
const mapDispatchToProps = dispatch => bindActionCreators({ saveNDD, updateActivities }, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(FormPrograms);
