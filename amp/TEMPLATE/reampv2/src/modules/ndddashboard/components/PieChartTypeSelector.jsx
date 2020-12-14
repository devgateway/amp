import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

class PieChartTypeSelector extends Component {
  constructor(props) {
    super(props);
    this.generateDropdown = this.generateDropdown.bind(this);
  }

  generateDropdown() {
    const { onChange } = this.props;
    const options = ['PNSD + NEW DEAL', 'PNSD + SDG', 'NEW DEAL', 'SDG']; // tomarlo del name aunque sea largo.
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
  defaultValue: PropTypes.string.isRequired
};
