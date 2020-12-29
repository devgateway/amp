import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';

class Header extends Component {
  render() {
    const {
      translations, onAddRow, onSaveAll, onRevertAll, disabled, src, dst
    } = this.props;
    const { trnPrefix } = this.context;
    return (
      <div>
        <div className="panel panel-default">
          <div className="panel-body custom-panel">
            {(src && dst) ? (
              <>
                <span className="glyphicon glyphicon-plus clickable" onClick={onAddRow} />
                <span
                  onClick={onAddRow}
                  className="add-new-text clickable">
                  {translations[`${trnPrefix}add-new`]}
                  {' '}
                </span>
                <span className="insert-data-text">{translations[`${trnPrefix}insert-data`]}</span>
              </>
            ) : null}
            <span className="float-right button-wrapper">
              <button
                type="button"
                onClick={onSaveAll}
                className="btn btn-success margin_2"
                disabled={disabled}>
                {translations[`${trnPrefix}button-save-all-edits`]}
              </button>
              <button
                type="button"
                onClick={onRevertAll}
                className="btn btn-danger margin_2"
                disabled={disabled}>
                {translations[`${trnPrefix}button-revert-all-edits`]}
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
  disabled: PropTypes.bool.isRequired,
  translations: PropTypes.bool.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Header);
