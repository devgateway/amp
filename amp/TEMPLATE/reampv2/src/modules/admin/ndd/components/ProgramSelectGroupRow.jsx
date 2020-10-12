import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';
import ProgramSelectGroup from "./ProgramSelectGroup";

class ProgramSelectGroupRow extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return <tr>
            <td></td>
            <td>
                <ProgramSelectGroup/>
            </td>
            <td>
                <ProgramSelectGroup/>
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