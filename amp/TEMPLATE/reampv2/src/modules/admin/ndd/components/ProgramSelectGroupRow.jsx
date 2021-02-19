import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import ProgramSelectGroup from './ProgramSelectGroup';
import { TYPE_DST, TYPE_SRC } from '../constants/Constants';
import ProgramSelectGroupList from './ProgramSelectGroupList';

class ProgramSelectGroupRow extends Component {
  render() {
    const {
      rowData, onChange, remove, src, dst, disabled, levelDst, levelSrc
    } = this.props;
    return (
      <tr>
        <td>
          <ProgramSelectGroup
            type={TYPE_SRC}
            data={rowData}
            onChange={onChange}
            src={src}
            dst={dst}
            level={levelSrc}
            disabled={disabled} />
        </td>
        <td>
          <ProgramSelectGroup
            type={TYPE_DST}
            data={rowData}
            onChange={onChange}
            src={src}
            dst={dst}
            level={levelDst}
            disabled={disabled} />
        </td>
        <td className="actions-column">
          {!disabled ? (
            <span
              className="glyphicon glyphicon-custom glyphicon-remove-sign clickable"
              onClick={() => remove(rowData)} />
          ) : null}
        </td>
      </tr>
    );
  }
}

ProgramSelectGroupRow.contextType = NDDContext;

ProgramSelectGroupRow.propTypes = {
  rowData: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  remove: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  disabled: PropTypes.bool.isRequired,
  levelSrc: PropTypes.number,
  levelDst: PropTypes.number
};

ProgramSelectGroupRow.defaultProps = {
  src: undefined,
  dst: undefined,
  levelSrc: undefined,
  levelDst: undefined
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupRow);
