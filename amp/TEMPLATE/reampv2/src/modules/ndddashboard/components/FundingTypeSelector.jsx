import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

class FundingTypeSelector extends Component {
  constructor(props) {
    super(props);
    this.generateDropdown = this.generateDropdown.bind(this);
  }

  generateDropdown() {
    const { dashboardSettings, onChange, defaultValue } = this.props;
    const options = dashboardSettings.find(s => s.id === 'funding-type');
    return (
      <form className="form-inline dash-form dash-adj-type" role="form">
        <select
          defaultValue={defaultValue}
          className="form-control like-btn-sm ftype-options"
          onChange={(e) => onChange(e.target.value)}>
          {options.value.options.map(i => (<option key={i.id} value={i.id}>{i.name}</option>))}
        </select>
        <span className="cheat-lineheight" />
      </form>
    );
  }

  render() {
    return (
      <div className="panel-footer clearfix">
        {/* <div className="pull-right">
          <div className="btn-group">
            <a
              data-toggle="tooltip"
              title=""
              className="btn btn-sm btn-default download"
              target="_blank">
              <span className="glyphicon glyphicon-cloud-download" />
            </a>
            <button
              data-toggle="tooltip"
              title=""
              type="button"
              className="btn btn-sm btn-default expand hidden-xs hidden-sm"
              data-original-title="Expand chart">
              <span className="glyphicon glyphicon-fullscreen" />
            </button>
          </div>
        </div> */}
        {this.generateDropdown()}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingTypeSelector);

FundingTypeSelector.propTypes = {
  onChange: PropTypes.func.isRequired,
  defaultValue: PropTypes.string.isRequired,
  dashboardSettings: PropTypes.array.isRequired
};
