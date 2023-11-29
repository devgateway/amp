import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {SectorMappingContext} from "./Startup";
import Select from "./Select";
import {ALL_SCHEMES, SECTOR, TYPE_DST, TYPE_SRC} from "../constants/Constants";

class SectorMappingTableRow extends Component {

  constructor(props) {
    super(props);
    this.state = {
      id: '',
      [TYPE_SRC + SECTOR]: undefined,
      [TYPE_DST + SECTOR]: undefined
    };
    this.onSelectChange = this.onSelectChange.bind(this);
    this.getOptionsByType = this.getOptionsByType.bind(this);
    this.getSelectedOptionByType = this.getSelectedOptionByType.bind(this);
  }

  componentDidMount() {
    const {rowData} = this.props;
    if (rowData) {
      this.setState(rowData);
    }
  }

  onSelectChange(selection, rowData, type) {
    const { onChange } = this.props;

    if (rowData) {
      const newState = {
        ...rowData,
        [type + SECTOR]: selection.length > 0 ? selection[0] : {}
      }
      this.setState(newState);
      onChange(selection, type, rowData.id);
    }
  }

  getOptionsByType(type, mappings, scheme) {
    const allSchemes = mappings[ALL_SCHEMES];
    const schemeWithChildren = allSchemes.find(s => s.id === scheme.id);
    return schemeWithChildren.children;
  }

  getSelectedOptionByType(type, rowData) {
    const selected = [];
    const propName = `${type}${SECTOR}`;

    if (rowData[propName] && rowData[propName].id !== undefined) {
      selected.push(rowData[propName]);
    }
    return selected;
  }

  render() {
    const { rowData, remove, src, dst, disabled } = this.props;
    const { mappings, translations, trnPrefix  } = this.context;

    return (
        <tr>
          <td>
            <div style={{ width: '100%' }}>
              <Select
                  placeholder={translations[`${trnPrefix}choose_src_sector`]}
                  label={translations[`${trnPrefix}src-sector`]}
                  options={this.getOptionsByType(TYPE_SRC, mappings, src)}
                  selected={this.getSelectedOptionByType(TYPE_SRC, rowData)}
                  onChange={(selection) => this.onSelectChange(selection, rowData, TYPE_SRC)}
                  rowData={rowData}
                  type={TYPE_SRC}
                  disabled={disabled} />
            </div>
          </td>
          <td>
            <div style={{ width: '100%' }}>
              <Select
                  placeholder={translations[`${trnPrefix}choose_dst_sector`]}
                  label={translations[`${trnPrefix}dst-sector`]}
                  options={this.getOptionsByType(TYPE_DST, mappings, dst)}
                  selected={this.getSelectedOptionByType(TYPE_DST, rowData)}
                  onChange={(selection) => this.onSelectChange(selection, rowData, TYPE_DST)}
                  rowData={rowData}
                  type={TYPE_DST}
                  disabled={disabled} />
            </div>
          </td>
          <td className="actions-column">
            {!disabled ? (
                <span
                    className="glyphicon glyphicon-custom glyphicon-remove-sign clickable"
                    onClick={() => remove(rowData)} />
            ) : null}
          </td>
        </tr>
    );
  }
}

SectorMappingTableRow.contextType = SectorMappingContext;

SectorMappingTableRow.propTypes = {
  rowData: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  remove: PropTypes.func.isRequired,
  src: PropTypes.object,
  dst: PropTypes.object,
  disabled: PropTypes.bool.isRequired
};

SectorMappingTableRow.defaultProps = {
  src: undefined,
  dst: undefined
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(SectorMappingTableRow);
