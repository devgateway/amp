import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import * as Constants from '../constants/Constants';
import { TRN_PREFIX } from '../constants/Constants';

class Header extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const {
      translations, onAddRow, onSaveAll, onRevertAll, disabled
    } = this.props;
    return (
      <div>
        <div className="panel panel-default">
          <div className="panel-body custom-panel">
            <span className="glyphicon glyphicon-plus clickable" onClick={onAddRow} />
            <span
              onClick={onAddRow}
              className="add-new-text clickable">
              {translations[`${Constants.TRN_PREFIX}add-new`]}
              {' '}

            </span>
            <span className="insert-data-text">{translations[`${TRN_PREFIX}insert-data`]}</span>
            <span className="float-right button-wrapper">
              <button
                type="button"
                onClick={onSaveAll}
                className="btn btn-success margin_2"
                disabled={disabled}>
                {translations[`${Constants.TRN_PREFIX}button-save-all-edits`]}
              </button>
              <button
                type="button"
                onClick={onRevertAll}
                className="btn btn-danger margin_2"
                disabled={disabled}>
                {translations[`${Constants.TRN_PREFIX}button-revert-all-edits`]}
              </button>
            </span>
          </div>
        </div>
      </div>
    );
  }
}

Header.contextType = NDDContext;

Header.propTypes = {
  onAddRow: PropTypes.func.isRequired,
  onSaveAll: PropTypes.func.isRequired,
  onRevertAll: PropTypes.func.isRequired,
  disabled: PropTypes.bool
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Header);
