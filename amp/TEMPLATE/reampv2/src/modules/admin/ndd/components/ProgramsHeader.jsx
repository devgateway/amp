import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/style.css';
import { TYPE_SRC, TYPE_DST } from '../constants/Constants';
import ProgramSelect from './ProgramSelect';
import HelpTooltip from './common/HelpTooltip';
import LevelSelect from './LevelSelect';

class ProgramsHeader extends Component {
  render() {
    const {
      translations, programs, trnPrefix, isIndirect, isSuperAdmin, settings
    } = this.context;
    const {
      src, dst, onChange, busy
    } = this.props;
    if (programs) {
      // Load levels defined in GS and max depth for each program.
      let depthLeft;
      let depthRight;
      if (src) {
        depthLeft = this.levelsToArray(programs.find(i => i.id === src.id).levels);
      }
      if (dst) {
        depthRight = this.levelsToArray(programs.find(i => i.id === dst.id).levels);
      }
      let levelSelectedRight;
      let levelSelectedLeft;
      if (isIndirect) {
        levelSelectedLeft = settings['ndd-mapping-indirect-direct-level'];
        levelSelectedRight = settings['ndd-mapping-indirect-direct-level'];
      } else {
        levelSelectedLeft = settings['ndd-mapping-program-source-level'];
        levelSelectedRight = settings['ndd-mapping-program-destination-level'];
      }
      return (
        <table className="programs-table">
          <tbody>
            <tr>
              <td>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-direct-programs`} />
                <ProgramSelect
                  disabled={busy}
                  placeholder={translations[`${trnPrefix}choose_main_src_program`]}
                  label={translations[`${trnPrefix}src-program-lvl-1`]}
                  options={programs.filter(p => p.indirect === false)}
                  selected={src ? [src] : []}
                  onChange={onChange.bind(null, TYPE_SRC)}
                  level={0} />
              </td>
              <td>
                <LevelSelect
                  isIndirect={isIndirect}
                  disabled={busy || !isSuperAdmin}
                  label={translations[`${trnPrefix}max-level`]}
                  options={depthLeft}
                  selected={levelSelectedLeft} />
              </td>
              <td style={{ paddingRight: '0%' }}>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-indirect-programs`} />
                <ProgramSelect
                  disabled={busy}
                  placeholder={translations[`${trnPrefix}choose_main_dst_program`]}
                  label={translations[`${trnPrefix}dst-program-lvl-1`]}
                  options={programs.filter(p => p.indirect === isIndirect)}
                  selected={dst ? [dst] : []}
                  onChange={onChange.bind(null, TYPE_DST)}
                  level={0} />
              </td>
              <td>
                <LevelSelect
                  isIndirect={isIndirect}
                  disabled={busy || !isSuperAdmin}
                  label={translations[`${trnPrefix}max-level`]}
                  options={depthRight}
                  selected={levelSelectedRight} />
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
  busy: PropTypes.bool.isRequired
};

ProgramsHeader.defaultProps = {
  src: undefined,
  dst: undefined
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(ProgramsHeader);
