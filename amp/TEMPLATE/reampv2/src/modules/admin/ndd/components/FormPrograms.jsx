import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import ProgramSelectGroupList from './ProgramSelectGroupList';
import Header from './Header';
import {
  DST_PROGRAM, PROGRAM, PROGRAM_MAPPING, SRC_PROGRAM, TYPE_DST, TYPE_SRC
} from '../constants/Constants';
import * as Utils from '../utils/Utils';
import { sendNDDError, sendNDDPending, sendNDDSaving } from '../reducers/saveNDDReducer';
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
      updatedActivities: false,
      blockUI: false,
      unsavedChanges: false,
      saved: false
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
    // eslint-disable-next-line no-unused-vars,react/no-did-mount-set-state
    this.setState(previousState => {
      if (ndd[SRC_PROGRAM]) {
        const src = { id: ndd[SRC_PROGRAM].id, value: ndd[SRC_PROGRAM].value };
        return { src };
      }
      return { src: undefined };
    });
    // eslint-disable-next-line no-unused-vars,react/no-did-mount-set-state
    this.setState(previousState => {
      if (ndd[DST_PROGRAM]) {
        const dst = { id: ndd[DST_PROGRAM].id, value: ndd[DST_PROGRAM].value };
        return { dst };
      }
      return { dst: undefined };
    });

    // Available programs.
    // eslint-disable-next-line react/no-did-mount-set-state,no-unused-vars
    this.setState(previousState => ({ programs }));

    // Load saved mapping.
    // eslint-disable-next-line react/no-did-mount-set-state
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

  componentDidUpdate(prevProps) {
    if (prevProps !== this.props) {
      // eslint-disable-next-line react/no-did-update-set-state
      this.setState(previousState => {
        if (!previousState.saved) {
          return { saved: true };
        } else {
          return null;
        }
      });
    }
  }

  /**
   * Sort by SRC lvl1->lvl2->lvl3 plus DST lvl1->lvl2->lvl3 if needed.
   * @param data
   * @returns {*[]|*}
   */
  // eslint-disable-next-line react/sort-comp,class-methods-use-this
  sortPrograms(data) {
    if (data && data.length > 0) {
      return data.sort((a, b) => {
        const compare = (a[SRC_PROGRAM].lvl1.value + a[SRC_PROGRAM].lvl2.value + a[SRC_PROGRAM].lvl3.value)
          .localeCompare(b[SRC_PROGRAM].lvl1.value + b[SRC_PROGRAM].lvl2.value + b[SRC_PROGRAM].lvl3.value);
        if (compare === 0) {
          return (a[DST_PROGRAM].lvl1.value + a[DST_PROGRAM].lvl2.value + a[DST_PROGRAM].lvl3.value)
            .localeCompare(b[DST_PROGRAM].lvl1.value + b[DST_PROGRAM].lvl2.value + b[DST_PROGRAM].lvl3.value);
        }
        return compare;
      });
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
      return { data, unsavedChanges: true, adding: true };
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
    this.setState({ unsavedChanges: true });
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
      this.setState({ unsavedChanges: true });
    }
  }

  clearMessages() {
    this.setState({
      validationErrors: undefined, updatedActivities: false, blockUI: false
    });
  }

  onUpdateActivities = () => {
    const { _updateActivities, translations } = this.props;
    const { trnPrefix } = this.context;
    const { data } = this.state;
    const validateMappings = Utils.validate(data);
    if (validateMappings === 0) {
      if (window.confirm(translations[`${trnPrefix}button-update-activities-confirmation`])) {
        this.clearMessages();
        this.setState({
          blockUI: true
        });
        _updateActivities();
      }
    } else {
      this.setState({ validationErrors: translations[`${trnPrefix}validation_error_${validateMappings}`] });
    }
  }

  saveAll() {
    const { data, src, dst } = this.state;
    const { _saveNDD, translations } = this.props;
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
        _saveNDD(src, dst, mappings, api.programsSave, api.mappingSave);
        this.setState({ unsavedChanges: false });
        this.clearMessages();
      } else {
        this.setState({
          validationErrors: translations[`${trnPrefix}validation_error_${validateMain}`],
        });
      }
    } else {
      this.setState({
        validationErrors: translations[`${trnPrefix}validation_error_${validateMappings}`],
      });
    }
  }

  onChangeMainProgram(type, program) {
    const { translations, trnPrefix } = this.context;
    const { data, src, dst } = this.state;
    this.src_ = src;
    this.dst_ = dst;
    const newProgram = (program && program.length > 0) ? program[0] : {};
    // eslint-disable-next-line react/destructuring-assignment
    const oldProgram = (this.state[type] ? this.state[type] : {});
    let autoAddRow = false;
    if (oldProgram.id !== newProgram.id) {
      if (oldProgram.id !== undefined) {
        if (data.length === 0 || window.confirm(translations[`${trnPrefix}warning_on_change_main_programs`])) {
          if (newProgram.id !== undefined) {
            // Old Program -> New Program.
            // eslint-disable-next-line no-unused-vars
            this.setState(previousState => ({ [type]: newProgram }));
            this[`${type}_`] = newProgram;
            autoAddRow = true;
          } else {
            // Old Program -> Nothing.
            // eslint-disable-next-line no-unused-vars
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
        // eslint-disable-next-line no-unused-vars
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

  // eslint-disable-next-line class-methods-use-this
  revertAllChanges() {
    window.location.reload();
  }

  render() {
    const {
      data, validationErrors, src, dst, updatedActivities, unsavedChanges, saved
    } = this.state;
    const {
      error, pending, translations, updating, errorUpdating, saving
    } = this.props;
    const { trnPrefix } = this.context;
    const messages = [];
    if (error) {
      messages.push({ isError: true, text: error.toString() });
    }
    if (validationErrors) {
      messages.push({ isError: true, text: validationErrors });
    }
    if (!saving && !error && !unsavedChanges && saved) {
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
          busy={updating || pending}
          unsavedChanges={unsavedChanges}
          dataPresent={data && data.length > 0} />
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
  translations: PropTypes.array.isRequired,
  error: PropTypes.object,
  pending: PropTypes.bool,
  updating: PropTypes.bool,
  errorUpdating: PropTypes.bool,
  _saveNDD: PropTypes.func.isRequired,
  _updateActivities: PropTypes.func.isRequired,
  saving: PropTypes.bool.isRequired
};

FormPrograms.defaultProps = {
  error: undefined,
  pending: false,
  updating: false,
  errorUpdating: false
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  error: sendNDDError(state.saveNDDReducer),
  saving: sendNDDSaving(state.saveNDDReducer),
  pending: sendNDDPending(state.saveNDDReducer),
  updating: updateActivitiesPending(state.updateActivitiesReducer),
  errorUpdating: updateActivitiesError(state.updateActivitiesReducer)
});
const mapDispatchToProps = dispatch => bindActionCreators({
  _saveNDD: saveNDD,
  _updateActivities: updateActivities
}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(FormPrograms);
