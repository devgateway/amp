import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';
import ProgramSelectGroup from "./ProgramSelectGroup";
import {TYPE_DST, TYPE_SRC} from "../constants/Constants";

class ProgramSelectGroupRow extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {rowData, onChange, remove} = this.props;
        return <tr>
            <td>
                <ProgramSelectGroup type={TYPE_SRC} data={rowData} onChange={onChange}/>
            </td>
            <td>
                <ProgramSelectGroup type={TYPE_DST} data={rowData} onChange={onChange}/>
            </td>
            <td className="actions-column">
                <span className="glyphicon glyphicon-custom glyphicon-remove-sign clickable"
                      onClick={() => remove(rowData)}>
                </span>
            </td>
        </tr>;
    }
}

ProgramSelectGroupRow.contextType = NDDContext;

ProgramSelectGroupRow.propTypes = {
    rowData: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired,
    remove: PropTypes.func.isRequired
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupRow);