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
    const { onChange, mapping } = this.props;
    const programs = extractPrograms(mapping);
    const options = [`${programs.direct.value} + ${programs.indirect1.value}`,
      `${programs.direct.value} + ${programs.indirect2.value}`,
      programs.indirect1.value,
      programs.indirect2.value];
    const ids = [`${programs.direct.id}-${programs.indirect1.id}`,
      `${programs.direct.id}-${programs.indirect2.id}`,
      programs.indirect1.id,
      programs.indirect2.id];
    return (
      <form className="form-inline dash-form dash-adj-type" role="form">
        <select
          className="form-control like-btn-sm ftype-options"
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

const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(PieChartTypeSelector);

PieChartTypeSelector.propTypes = {
  onChange: PropTypes.func.isRequired,
  defaultValue: PropTypes.string.isRequired,
  mapping: PropTypes.object.isRequired
};
