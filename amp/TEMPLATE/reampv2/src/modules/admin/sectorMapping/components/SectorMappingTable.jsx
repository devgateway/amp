import React, {Component, useState} from "react";
import {SectorMappingContext} from "./Startup";
import PropTypes from "prop-types";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import './css/style.css';
import HelpTooltip from "./common/HelpTooltip";
import RequiredMark from "../../ndd/components/common/RequiredMark";
import SectorMappingTableRow from "./SectorMappingTableRow";

class SectorMappingTable extends Component {

  render() {
    const { list, translations, onChange, remove, dst, src, busy } = this.props;
    const { trnPrefix } = this.context;

    return (
        <div>
          <table className="table table-striped">
            <thead>
            <tr>
              <th>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-src-sector-list`} />
                <span data-field="srcProgramSelectGroup">{translations[`${trnPrefix}src-sectors`]}</span>
                <RequiredMark />
              </th>
              <th>
                <HelpTooltip labelKey={`${trnPrefix}tooltip-dst-sector-list`} />
                <span data-field="dstProgramSelectGroup">{translations[`${trnPrefix}dst-sectors`]}</span>
                <RequiredMark />
              </th>
              <td className="actions-column">{translations[`${trnPrefix}actions`]}</td>
            </tr>
            </thead>
            <tbody>
            {list.map(row => (
                <SectorMappingTableRow
                    rowData={row}
                    key={Math.random()}
                    onChange={onChange}
                    remove={remove}
                    dst={dst}
                    src={src}
                    disabled={busy} />
            ))}
            </tbody>
          </table>
        </div>
    );
  }
}

SectorMappingTable.contextType = SectorMappingContext;

SectorMappingTable.propTypes = {
  list: PropTypes.array.isRequired,
  onChange: PropTypes.func.isRequired,
  remove: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  translations: PropTypes.object.isRequired,
  busy: PropTypes.bool.isRequired
};

SectorMappingTable.defaultProps = {
  src: undefined,
  dst: undefined
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SectorMappingTable);
