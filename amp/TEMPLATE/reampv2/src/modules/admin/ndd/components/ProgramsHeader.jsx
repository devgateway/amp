import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';
import {TRN_PREFIX, TYPE_SRC, TYPE_DST} from "../constants/Constants";
import ProgramSelect from "./ProgramSelect";

class ProgramsHeader extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {translations, programs} = this.context;
        const {src, dst, onChange} = this.props;
        if (programs) {
            console.error(src);
            return (<table className="programs-table">
                <tbody>
                <tr>
                    <td>
                        <ProgramSelect
                            placeholder={translations[TRN_PREFIX + 'choose_main_src_program']}
                            label={translations[TRN_PREFIX + 'src-program-lvl-1']}
                            options={programs}
                            selected={src ? [src] : []}
                            onChange={onChange.bind(null, TYPE_SRC)}
                            level={0}/>
                    </td>
                    <td>
                        <ProgramSelect
                            placeholder={translations[TRN_PREFIX + 'choose_main_dst_program']}
                            label={translations[TRN_PREFIX + 'dst-program-lvl-1']}
                            options={programs}
                            selected={dst ? [dst] : []}
                            onChange={onChange.bind(null, TYPE_DST)}
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
    onChange: PropTypes.func.isRequired,
    src: PropTypes.object,
    dst: PropTypes.object
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramsHeader);