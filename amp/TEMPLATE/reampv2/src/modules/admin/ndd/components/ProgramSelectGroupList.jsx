import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import ProgramSelectGroupRow from './ProgramSelectGroupRow';
import RequiredMark from './common/RequiredMark';
import HelpTooltip from './common/HelpTooltip';

class ProgramSelectGroupList extends Component {
  render() {
    const {
      list, translations, onChange, remove, dst, src, busy
    } = this.props;
    const { trnPrefix } = this.context;
    return (
      <div>
        <table className="table table-striped">
          <thead>
            <tr>
              <th>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-direct-programs-list`} />
                <span data-field="srcProgramSelectGroup">{translations[`${trnPrefix}src-programs-title`]}</span>
                <RequiredMark />
              </th>
              <th>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-indirect-programs-list`} />
                <span data-field="dstProgramSelectGroup">{translations[`${trnPrefix}dst-programs-title`]}</span>
                <RequiredMark />
              </th>
              <td className="actions-column">{translations[`${trnPrefix}actions`]}</td>
            </tr>
          </thead>
          <tbody>
            {list.map(pair => (
              <ProgramSelectGroupRow
                rowData={pair}
                key={Math.random()}
                onChange={onChange}
                remove={remove}
                dst={dst}
                src={src}
                disabled={busy} />
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

ProgramSelectGroupList.contextType = NDDContext;

ProgramSelectGroupList.propTypes = {
  list: PropTypes.array.isRequired,
  onChange: PropTypes.func.isRequired,
  remove: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  translations: PropTypes.object.isRequired,
  busy: PropTypes.bool.isRequired
};

ProgramSelectGroupList.defaultProps = {
  src: null,
  dst: null,
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupList);
