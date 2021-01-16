import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import {
  CHILDREN, FIRST_LEVEL, PROGRAM, SECOND_LEVEL, STATE_LEVEL_FIELD, THIRD_LEVEL
} from '../constants/Constants';
import '../../../../../node_modules/react-bootstrap-typeahead/css/Typeahead.min.css';
import './css/style.css';
import ProgramSelect from './ProgramSelect';
import * as Utils from '../utils/Utils';

class ProgramSelectGroup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      [STATE_LEVEL_FIELD + FIRST_LEVEL]: undefined,
      [STATE_LEVEL_FIELD + SECOND_LEVEL]: undefined,
      [STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined
    };
    this.onSelectChange = this.onSelectChange.bind(this);
    this.getOptionsForLevel = this.getOptionsForLevel.bind(this);
    this.getSelectedForLevel = this.getSelectedForLevel.bind(this);
  }

  componentDidMount() {
    const { data, type } = this.props;
    if (data && data[type + PROGRAM]) {
      const newState = {
        id: data.id,
        [STATE_LEVEL_FIELD + FIRST_LEVEL]: data[type + PROGRAM].lvl1,
        [STATE_LEVEL_FIELD + SECOND_LEVEL]: data[type + PROGRAM].lvl2,
        [STATE_LEVEL_FIELD + THIRD_LEVEL]: data[type + PROGRAM].lvl3
      };
      this.setState(newState);
    }
  }

  onSelectChange(selected, lvl) {
    const { onChange, type, data } = this.props;
    let id = null;
    let value = null;
    if (selected && selected[0] && selected[0].id) {
      id = selected[0].id;
      value = selected[0].value;
    }
    let level1 = this.state[STATE_LEVEL_FIELD + FIRST_LEVEL];
    let level2 = this.state[STATE_LEVEL_FIELD + SECOND_LEVEL];
    let level3 = this.state[STATE_LEVEL_FIELD + THIRD_LEVEL];
    switch (lvl) {
      case FIRST_LEVEL:
        if (id && value) {
          level1 = { id, value };
          this.setState({ [STATE_LEVEL_FIELD + FIRST_LEVEL]: level1 });
        } else {
          level1 = undefined;
          this.setState({ [STATE_LEVEL_FIELD + FIRST_LEVEL]: level1 });
        }
        level2 = undefined;
        level3 = undefined;
        this.setState({ [STATE_LEVEL_FIELD + SECOND_LEVEL]: level2 });
        this.setState({ [STATE_LEVEL_FIELD + THIRD_LEVEL]: level3 });
        break;
      case SECOND_LEVEL:
        if (id && value) {
          level2 = { id, value };
          this.setState({ [STATE_LEVEL_FIELD + SECOND_LEVEL]: level2 });
        } else {
          level2 = undefined;
          this.setState({ [STATE_LEVEL_FIELD + SECOND_LEVEL]: level2 });
        }
        level3 = undefined;
        this.setState({ [STATE_LEVEL_FIELD + THIRD_LEVEL]: level3 });
        break;
      case THIRD_LEVEL:
        if (id && value) {
          level3 = { id, value };
          this.setState({ [STATE_LEVEL_FIELD + THIRD_LEVEL]: level3 });
        } else {
          level3 = undefined;
          this.setState({ [STATE_LEVEL_FIELD + THIRD_LEVEL]: level3 });
        }
        break;
    }
    onChange(level1, level2, level3, type, data.id);
  }

  getOptionsForLevel(level) {
    const { ndd } = this.context;
    const { type, src, dst } = this.props;
    let options = [];
    const tree = Utils.findFullProgramTree(ndd, type, src, dst);
    switch (level) {
      case FIRST_LEVEL:
        if (tree && tree[CHILDREN]) {
          options = tree[CHILDREN].map(i => ({ id: i.id, value: i.value }));
        }
        break;
      case SECOND_LEVEL:
        if (tree && tree[CHILDREN]) {
          if (this.state[STATE_LEVEL_FIELD + FIRST_LEVEL]) {
            options = tree[CHILDREN]
              .find(i => i.id === this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id)[CHILDREN];
          }
        }
        break;
      case THIRD_LEVEL:
        if (tree && tree[CHILDREN]) {
          if (this.state[STATE_LEVEL_FIELD + SECOND_LEVEL]) {
            options = tree[CHILDREN]
              .find(i => i.id === this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id)[CHILDREN]
              .find(i => i.id === this.state[STATE_LEVEL_FIELD + SECOND_LEVEL].id)[CHILDREN];
          }
        }
        break;
    }
    return options;
  }

  getSelectedForLevel(level) {
    const selected = [];
    const { state } = this;
    switch (level) {
      case FIRST_LEVEL:
        if (state[STATE_LEVEL_FIELD + FIRST_LEVEL]) {
          selected.push({
            id: state[STATE_LEVEL_FIELD + FIRST_LEVEL].id,
            value: state[STATE_LEVEL_FIELD + FIRST_LEVEL].value
          });
        }
        break;
      case SECOND_LEVEL:
        if (state[STATE_LEVEL_FIELD + SECOND_LEVEL]) {
          selected.push({
            id: state[STATE_LEVEL_FIELD + SECOND_LEVEL].id,
            value: state[STATE_LEVEL_FIELD + SECOND_LEVEL].value
          });
        }
        break;
      case THIRD_LEVEL:
        if (state[STATE_LEVEL_FIELD + THIRD_LEVEL]) {
          selected.push({
            id: state[STATE_LEVEL_FIELD + THIRD_LEVEL].id,
            value: state[STATE_LEVEL_FIELD + THIRD_LEVEL].value
          });
        } else {
          // Auto-select when there is only 1 option available.
          const options = this.getOptionsForLevel(THIRD_LEVEL);
          if (options && options.length === 1) {
            selected.push({
              id: options[0].id,
              value: options[0].value
            });
            this.onSelectChange(selected, level);
          }
        }
        break;
    }
    return selected;
  }

  render() {
    const { translations, trnPrefix } = this.context;
    const { type, disabled } = this.props;
    return (
      <div>
        <div style={{ width: '100%' }}>
          <ProgramSelect
            placeholder={translations[`${trnPrefix}choose-${type}-lvl-${FIRST_LEVEL}`]}
            label={translations[`${trnPrefix + type}-program-lvl-${FIRST_LEVEL}`]}
            options={this.getOptionsForLevel(FIRST_LEVEL)}
            selected={this.getSelectedForLevel(FIRST_LEVEL)}
            onChange={this.onSelectChange}
            level={FIRST_LEVEL}
            disabled={disabled} />
          <ProgramSelect
            placeholder={translations[`${trnPrefix}choose-${type}-lvl-${SECOND_LEVEL}`]}
            label={translations[`${trnPrefix + type}-program-lvl-${SECOND_LEVEL}`]}
            options={this.getOptionsForLevel(SECOND_LEVEL)}
            selected={this.getSelectedForLevel(SECOND_LEVEL)}
            onChange={this.onSelectChange}
            level={SECOND_LEVEL}
            disabled={disabled} />
          <ProgramSelect
            placeholder={translations[`${trnPrefix}choose-${type}-lvl-${THIRD_LEVEL}`]}
            label={translations[`${trnPrefix + type}-program-lvl-${THIRD_LEVEL}`]}
            options={this.getOptionsForLevel(THIRD_LEVEL)}
            selected={this.getSelectedForLevel(THIRD_LEVEL)}
            onChange={this.onSelectChange}
            level={THIRD_LEVEL}
            disabled={disabled} />
        </div>
      </div>
    );
  }
}

ProgramSelectGroup.propTypes = {
  type: PropTypes.string.isRequired,
  data: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  disabled: PropTypes.bool.isRequired
};

ProgramSelectGroup.contextType = NDDContext;
const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroup);
