import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';

class Header extends Component {
  render() {
    const {
      translations, onAddRow, onSaveAll, onRevertAll, src, dst, onUpdateActivities, busy, dataPresent, unsavedChanges
    } = this.props;
    const { trnPrefix, isIndirect } = this.context;
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
                <span className="insert-data-text clickable" onClick={onAddRow}>
                  {translations[`${trnPrefix}insert-data`]}
                </span>
              </>
            ) : null}
            <span> / </span>
            <span className="required-fields">{`* ${translations[`${trnPrefix}required-fields`]}`}</span>
            <span className="float-right button-wrapper">
              <button
                type="button"
                onClick={onSaveAll}
                className="btn btn-success margin_2"
                disabled={busy}>
                {translations[`${trnPrefix}button-save-all-edits`]}
              </button>
              <button
                type="button"
                onClick={onRevertAll}
                className="btn btn-danger margin_2"
                disabled={busy || !unsavedChanges}>
                {translations[`${trnPrefix}button-revert-all-edits`]}
              </button>
              {isIndirect ? (
                <button
                  type="button"
                  onClick={onUpdateActivities}
                  className="btn btn-primary"
                  disabled={busy || !dataPresent || unsavedChanges}>
                  {translations[`${trnPrefix}button-update-activities`]}
                </button>
              ) : null}
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
  translations: PropTypes.object.isRequired,
  onUpdateActivities: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  busy: PropTypes.bool.isRequired,
  dataPresent: PropTypes.bool,
  unsavedChanges: PropTypes.bool.isRequired
};

Header.defaultProps = {
  src: undefined,
  dst: undefined,
  dataPresent: false
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Header);
