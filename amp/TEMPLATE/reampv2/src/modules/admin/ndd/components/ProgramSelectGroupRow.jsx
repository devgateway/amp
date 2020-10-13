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
        const {rowData} = this.props;
        return <tr>
            <td></td>
            <td>
                <ProgramSelectGroup type={TYPE_SRC} data={rowData}/>
            </td>
            <td>
                <ProgramSelectGroup type={TYPE_DST} data={rowData}/>
            </td>
            <td>icons here</td>
        </tr>;
    }
}

ProgramSelectGroupRow.contextType = NDDContext;

ProgramSelectGroupRow.propTypes = {
    rowData: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupRow);