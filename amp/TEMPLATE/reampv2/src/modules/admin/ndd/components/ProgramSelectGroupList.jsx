import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import ProgramSelectGroupRow from './ProgramSelectGroupRow';

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
                <span data-field="srcProgramSelectGroup">
                  <span className="error-color">*&nbsp;</span>
                  {translations[`${trnPrefix}src-programs-title`]}
&nbsp;
                </span>
              </td>
              <td>
                <span data-field="dstProgramSelectGroup">
                  <span className="error-color">*&nbsp;</span>
                  {translations[`${trnPrefix}dst-programs-title`]}
&nbsp;
                </span>
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
  dst: PropTypes.object
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupList);
