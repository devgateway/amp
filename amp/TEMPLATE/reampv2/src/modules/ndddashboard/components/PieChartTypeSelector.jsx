import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { extractPrograms } from '../utils/Utils';

class PieChartTypeSelector extends Component {
  constructor(props) {
    super(props);
    this.generateDropdown = this.generateDropdown.bind(this);
  }

  generateDropdown() {
    const {
      onChange, mapping, noIndirectMapping, selectedPrograms
    } = this.props;
    const programs = extractPrograms(mapping, noIndirectMapping);
    const options = [];
    const ids = [];
    if (programs.direct) {
      if (programs.indirect1) {
        options.push(`${programs.direct.value} + ${programs.indirect1.value}`);
        ids.push(`${programs.direct.id}-${programs.indirect1.id}`);
      }
      if (programs.indirect2) {
        options.push(`${programs.direct.value} + ${programs.indirect2.value}`);
        ids.push(`${programs.direct.id}-${programs.indirect2.id}`);
      }
      options.push(programs.direct.value);
      ids.push(programs.direct.id);
      if (programs.indirect1) {
        options.push(programs.indirect1.value);
        ids.push(programs.indirect1.id);
      }
      if (programs.indirect2) {
        options.push(programs.indirect2.value);
        ids.push(programs.indirect2.id);
      }
    }
    let defaultValue = '';
    if (selectedPrograms && selectedPrograms.length === 2) {
      defaultValue = `${selectedPrograms[0]}-${selectedPrograms[1]}`;
    } else if (selectedPrograms && selectedPrograms.length === 1) {
      // eslint-disable-next-line prefer-destructuring
      defaultValue = selectedPrograms[0];
    }
    return (
      <form className="form-inline dash-form dash-adj-type">
        <select
          defaultValue={defaultValue}
          className="form-control like-btn-sm ftype-options programs-dropdown"
          onChange={(e) => onChange(e.target.value)}>
          {options.map((i, j) => (<option key={ids[j]} value={ids[j]}>{i}</option>))}
        </select>
        <span className="cheat-lineheight" />
      </form>
    );
  }

  render() {
    return (
      <div className="pie-chart-selector">
        {this.generateDropdown()}
      </div>
    );
  }
}

// eslint-disable-next-line no-unused-vars
const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(PieChartTypeSelector);

PieChartTypeSelector.propTypes = {
  onChange: PropTypes.func.isRequired,
  // eslint-disable-next-line react/no-unused-prop-types
  defaultValue: PropTypes.string.isRequired,
  mapping: PropTypes.object.isRequired,
  noIndirectMapping: PropTypes.object.isRequired,
  selectedPrograms: PropTypes.array
};

PieChartTypeSelector.defaultProps = {
  selectedPrograms: undefined
};
