import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { ALL_PROGRAMS, DST_PROGRAM, PROGRAM_MAPPING, SRC_PROGRAM } from '../../admin/ndd/constants/Constants';

class PieChartTypeSelector extends Component {
  constructor(props) {
    super(props);
    this.generateDropdown = this.generateDropdown.bind(this);
  }

  extractPrograms = () => {
    const { mapping } = this.props;
    const ret = { direct: undefined, indirect1: undefined, indirect2: undefined };
    if (mapping && mapping[PROGRAM_MAPPING]) {
      ret.direct = mapping[ALL_PROGRAMS].find(i => i.children
        .find(j => j.children && j.children
          .find(k => k.children && k.children
            .find(l => l.id === mapping[PROGRAM_MAPPING][0][SRC_PROGRAM]))));
      ret.indirect1 = mapping[ALL_PROGRAMS].find(i => i.children
        .find(j => j.children && j.children
          .find(k => k.children && k.children
            .find(l => l.id === mapping[PROGRAM_MAPPING][0][DST_PROGRAM]))));
      ret.indirect2 = { value: 'TODO' };
    }
    // TODO: same code for indirect2.
    return ret;
  }

  generateDropdown() {
    const { onChange } = this.props;
    const programs = this.extractPrograms();
    const options = [`${programs.direct.value} + ${programs.indirect1.value}`,
      `${programs.direct.value} + ${programs.indirect2.value}`,
      programs.indirect1.value,
      programs.indirect2.value];
    return (
      <form className="form-inline dash-form dash-adj-type" role="form">
        <select
          className="form-control like-btn-sm ftype-options"
          onChange={(e) => onChange(e.target.value)}>
          {options.map(i => (<option key={i} value={i}>{i}</option>))}
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
