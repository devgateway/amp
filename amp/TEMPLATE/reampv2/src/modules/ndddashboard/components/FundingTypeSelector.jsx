import React, { useCallback } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

const FundingTypeSelector = (props) => {
  const { dashboardSettings, onChange, defaultValue } = props;

  const generateDropdown = useCallback(() => {
    const options = dashboardSettings.find(s => s.id === 'funding-type');
    return (
      <form className="form-inline dash-form dash-adj-type">
        <select
          defaultValue={defaultValue}
          className="form-control like-btn-sm ftype-options"
          onChange={(e) => onChange(e.target.value)}>
          {options.value.options.map(i => (<option key={i.id} value={i.id}>{i.name}</option>))}
        </select>
        <span className="cheat-lineheight" />
      </form>
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="panel-footer clearfix">
      {generateDropdown()}
    </div>
  );
};

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  dashboardSettings: state.dashboardSettingsReducer.dashboardSettings,
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingTypeSelector);

FundingTypeSelector.propTypes = {
  onChange: PropTypes.func.isRequired,
  defaultValue: PropTypes.string,
  dashboardSettings: PropTypes.array.isRequired
};

FundingTypeSelector.defaultProps = {
  defaultValue: null
};
