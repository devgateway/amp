import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import {SectorMappingContext} from './Startup';
import './css/style.css';
import { TYPE_SRC, TYPE_DST } from '../constants/Constants';
import HelpTooltip from "./common/HelpTooltip";
import Select from './Select';

class SectorsHeader extends Component {

    render() {
        const { translations, schemes, trnPrefix } = this.context;
        const { src, dst, onChange, busy } = this.props

        if (schemes) {
            return (
                <table className="sectors-table">
                    <tbody>
                        <tr>
                            <td style={{ width: '50%' }}>
                                <HelpTooltip labelKey={`${trnPrefix}tooltip-direct-sector`} />
                                <Select
                                    disabled={busy}
                                    placeholder={translations[`${trnPrefix}choose_main_src_sector`]}
                                    label={translations[`${trnPrefix}src-sector-lvl-1`]}
                                    options={schemes.filter(p => p.visible === true)}
                                    selected={src ? [src] : []}
                                    onChange={onChange.bind(null, TYPE_SRC)}
                                    level={0} />
                            </td>
                            <td style={{ width: '50%' }}>
                                <HelpTooltip labelKey={`${trnPrefix}tooltip-indirect-sector`} />
                                <Select
                                    disabled={busy}
                                    placeholder={translations[`${trnPrefix}choose_main_dst_sector`]}
                                    label={translations[`${trnPrefix}dst-sector-lvl-1`]}
                                    options={schemes.filter(p => p.visible === false)}
                                    selected={dst ? [dst] : []}
                                    onChange={onChange.bind(null, TYPE_DST)}
                                    level={0} />
                            </td>
                        </tr>
                    </tbody>
                </table>
            );
        } else {
            return (
                <div></div>
            );
        }
    }
}

SectorsHeader.contextType = SectorMappingContext;

SectorsHeader.propTypes = {
    src: PropTypes.object,
    dst: PropTypes.object,
    onChange: PropTypes.func.isRequired,
    busy: PropTypes.bool.isRequired,
    level: PropTypes.number
};

SectorsHeader.defaultProps = {
    src: undefined,
    dst: undefined,
    level: 1
};

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(SectorsHeader);
