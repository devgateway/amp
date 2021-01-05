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
      list, translations, onChange, remove, dst, src
    } = this.props;
    const { trnPrefix } = this.context;
    return (
      <div>
        <table className="table table-striped">
          <thead>
            <tr>
              <td>
                <HelpTooltip labelKey="tooltip-direct-programs-list" />
                <span data-field="srcProgramSelectGroup">{translations[`${trnPrefix}src-programs-title`]}</span>
                <RequiredMark />
              </td>
              <td>
                <HelpTooltip labelKey="tooltip-direct-programs-list" />
                <span data-field="dstProgramSelectGroup">{translations[`${trnPrefix}dst-programs-title`]}</span>
                <RequiredMark />
              </td>
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
                src={src} />
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
  translations: PropTypes.array.isRequired
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupList);
