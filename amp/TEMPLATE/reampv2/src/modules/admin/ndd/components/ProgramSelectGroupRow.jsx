import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import ProgramSelectGroup from './ProgramSelectGroup';
import { TYPE_DST, TYPE_SRC } from '../constants/Constants';

class ProgramSelectGroupRow extends Component {
  render() {
    const {
      rowData, onChange, remove, src, dst
    } = this.props;
    return (
      <tr>
        <td>
          <ProgramSelectGroup type={TYPE_SRC} data={rowData} onChange={onChange} src={src} dst={dst} />
        </td>
        <td>
          <ProgramSelectGroup type={TYPE_DST} data={rowData} onChange={onChange} src={src} dst={dst} />
        </td>
        <td className="actions-column">
          <span
            className="glyphicon glyphicon-custom glyphicon-remove-sign clickable"
            onClick={() => remove(rowData)} />
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
  dst: PropTypes.object
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupRow);
