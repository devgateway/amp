import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';
import * as Constants from "../constants/Constants";
import {DST_PROGRAM, FIRST_LEVEL, SRC_PROGRAM, TRN_PREFIX, VALUE} from "../constants/Constants";
import ProgramSelect from "./ProgramSelect";

class ProgramsHeader extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {translations, ndd, programs} = this.context;
        const {src, dst} = this.props;
        if (src && dst && programs) {
            debugger
            return (<table className="programs-table">
                <tbody>
                <tr>
                    <td>
                        <ProgramSelect
                            placeholder={translations[Constants.TRN_PREFIX + 'choose_main_src_program']}
                            label={translations[Constants.TRN_PREFIX + 'src-program-lvl-1']}
                            options={programs}
                            selected={[src]}
                            onChange={this.onSelectChange}
                            level={0}/>
                    </td>
                    <td>
                        <ProgramSelect
                            placeholder={translations[Constants.TRN_PREFIX + 'choose_main_dst_program']}
                            label={translations[Constants.TRN_PREFIX + 'dst-program-lvl-1']}
                            options={programs}
                            selected={[dst]}
                            onChange={this.onSelectChange}
                            level={0}/>
                    </td>
                </tr>
                </tbody>
            </table>);
        }
        return null;
    }
}

ProgramsHeader.contextType = NDDContext;

ProgramsHeader.propTypes = {
    onChangeMainSrcProgram: PropTypes.func.isRequired,
    onChangeMainDstProgram: PropTypes.func.isRequired,
    src: PropTypes.object.isRequired,
    dst: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramsHeader);