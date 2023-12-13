import React, {Component} from "react";
import {SectorMappingContext} from "./Startup";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import PropTypes from "prop-types";
import './css/style.css';

class HeaderActions extends Component {
  render() {
    const {
      translations, onAddRow, onSaveAll, onRevertAll, src, dst, busy, dataPresent, unsavedChanges
    } = this.props;
    const {trnPrefix} = this.context;

    return (
        <div>
          <div className="panel panel-default">
            <div className="panel-body custom-panel">
              {(src && dst) ? (
                  <>
                    <span className="glyphicon glyphicon-plus clickable" onClick={onAddRow}/>
                    <span onClick={onAddRow} className="add-new-text clickable">
                      {translations[`${trnPrefix}add-new`]} {' '}
                    </span>
                    <span className="insert-data-text clickable" onClick={onAddRow}>
                      {translations[`${trnPrefix}insert-data`]}
                    </span>
                  </>
              ) : null}
              <span> / </span>
              <span className="required-fields">{`* ${translations[`${trnPrefix}required-fields`]}`}</span>
              <span className="float-right button-wrapper">
                <button type="button" onClick={onSaveAll} className="btn btn-success margin_2" disabled={busy}>
                        {translations[`${trnPrefix}button-save-all-edits`]}
                </button>
                <button type="button" onClick={onRevertAll} className="btn btn-danger margin_2"
                        disabled={busy || !unsavedChanges}>
                    {translations[`${trnPrefix}button-revert-all-edits`]}
                </button>
              </span>
            </div>
          </div>
        </div>
    );
  }
}

HeaderActions.contextType = SectorMappingContext;

HeaderActions.propTypes = {
  onAddRow: PropTypes.func.isRequired,
  onSaveAll: PropTypes.func.isRequired,
  onRevertAll: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  busy: PropTypes.bool.isRequired,
  dataPresent: PropTypes.bool,
  unsavedChanges: PropTypes.bool.isRequired
};

HeaderActions.defaultProps = {
  src: undefined,
  dst: undefined,
  dataPresent: false
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HeaderActions);
