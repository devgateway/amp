import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import { TYPE_SRC, TYPE_DST } from '../constants/Constants';
import Select from './Select';
import HelpTooltip from './common/HelpTooltip';

class ProgramsHeader extends Component {
  // eslint-disable-next-line react/sort-comp
  render() {
    const {
      translations, programs, trnPrefix, isIndirect, isSuperAdmin
    } = this.context;
    const {
      src, dst, onChange, busy, onChangeLevel, level
    } = this.props;
    if (programs) {
      // Load levels defined in GS and max depth for each program.
      let depthRight;
      if (dst) {
        if (programs.find(i => i.id === dst.id)) {
          depthRight = this.levelsToArray(programs.find(i => i.id === dst.id).levels);
        }
      }
      const levelSelected = level > 0 ? [{ id: level, value: `${level}` }] : null;
      return (
        <table className="programs-table">
          <tbody>
            <tr>
              <td style={{ width: '40%' }}>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-direct-programs`} />
                <Select
                  disabled={busy}
                  placeholder={translations[`${trnPrefix}choose_main_src_program`]}
                  label={translations[`${trnPrefix}src-program-lvl-1`]}
                  options={programs.filter(p => p.indirect === false)}
                  selected={src ? [src] : []}
                  onChange={onChange.bind(null, TYPE_SRC)}
                  level={0} />
              </td>
              <td style={{ width: '40%' }}>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-indirect-programs`} />
                <Select
                  disabled={busy}
                  placeholder={translations[`${trnPrefix}choose_main_dst_program`]}
                  label={translations[`${trnPrefix}dst-program-lvl-1`]}
                  options={programs.filter(p => p.indirect === isIndirect)}
                  selected={dst ? [dst] : []}
                  onChange={onChange.bind(null, TYPE_DST)}
                  level={0} />
              </td>
              <td style={{ width: '20%' }}>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-max-depth`} />
                <Select
                  isIndirect={isIndirect}
                  disabled={busy || !isSuperAdmin}
                  label={translations[`${trnPrefix}max-level`]}
                  options={depthRight}
                  onChange={onChangeLevel.bind(null, 'level')}
                  selected={levelSelected} />
              </td>
            </tr>
          </tbody>
        </table>
      );
    }
    return null;
  }

  levelsToArray = (levels) => {
    const res = [];
    for (let i = 1; i <= levels; i++) {
      res.push({ id: i, value: `${i}` });
    }
    return res;
  }
}

ProgramsHeader.contextType = NDDContext;

ProgramsHeader.propTypes = {
  onChange: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  busy: PropTypes.bool.isRequired,
  onChangeLevel: PropTypes.func.isRequired,
  level: PropTypes.number
};

ProgramsHeader.defaultProps = {
  src: undefined,
  dst: undefined,
  level: -1
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramsHeader);
