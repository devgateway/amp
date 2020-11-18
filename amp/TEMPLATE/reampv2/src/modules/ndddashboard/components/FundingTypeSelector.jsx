import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import callReport from '../actions/callReports';

class FundingTypeSelector extends Component {

  constructor(props) {
    super(props);
    this.generateDropdown = this.generateDropdown.bind(this);
  }

  generateDropdown() {
    const { settings } = this.props;
    const options = settings.find(s => s.id === 'funding-type');
    return (<form className="form-inline dash-form dash-adj-type" role="form">
      <select className="form-control like-btn-sm ftype-options">
        {options.value.options.map(i => (<option value={i.id} selected="">{i.value}</option>))}
      </select>
      <span className="cheat-lineheight"/>
    </form>);
  }

  render() {
    return (
      <div className="panel-footer clearfix">
        <div className="pull-right">
          <div className="btn-group">
            <a data-toggle="tooltip"
               title="" className="btn btn-sm btn-default download" target="_blank">
              <span className="glyphicon glyphicon-cloud-download"/>
            </a>
            <button data-toggle="tooltip"
                    title="" type="button" className="btn btn-sm btn-default expand hidden-xs hidden-sm"
                    data-original-title="Expand chart">
              <span className="glyphicon glyphicon-fullscreen"/>
            </button>
          </div>
        </div>
        {this.generateDropdown()}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  ndd: state.reportsReducer.ndd,
  settings: state.reportsReducer.settings,
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(FundingTypeSelector);

FundingTypeSelector.propTypes = {
  onChange: PropTypes.func.isRequired
};
