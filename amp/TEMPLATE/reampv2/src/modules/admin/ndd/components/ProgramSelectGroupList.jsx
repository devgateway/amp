//TODO: we need pagination.
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';
import * as Constants from "../constants/Constants";
import ProgramSelectGroupRow from './ProgramSelectGroupRow.jsx'

class ProgramSelectGroupList extends Component {

    render() {
        const {list, translations, onChange} = this.props;
        // console.error(list);
        return (<div>
            <table className="table table-striped">
                <thead>
                <tr>
                    <td></td>
                    <td>
                        <span data-field="srcProgramSelectGroup">
                            <span className="error-color">*&nbsp;</span>
                            {translations[Constants.TRN_PREFIX + 'src-programs-title']}&nbsp;
                        </span>
                    </td>
                    <td>
                        <span data-field="dstProgramSelectGroup">
                            <span className="error-color">*&nbsp;</span>
                            {translations[Constants.TRN_PREFIX + 'dst-programs-title']}&nbsp;
                        </span>
                    </td>
                    <td className="actions-column">{translations[Constants.TRN_PREFIX + 'action']}</td>
                </tr>
                </thead>
                <tbody>
                {list.map(pair =>
                    <ProgramSelectGroupRow rowData={pair} key={Math.random()} onChange={onChange}/>
                )}
                </tbody>
            </table>
        </div>);
    }
}

ProgramSelectGroupList.contextType = NDDContext;

ProgramSelectGroupList.propTypes = {
    list: PropTypes.array.isRequired,
    onChange: PropTypes.func.isRequired
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroupList);