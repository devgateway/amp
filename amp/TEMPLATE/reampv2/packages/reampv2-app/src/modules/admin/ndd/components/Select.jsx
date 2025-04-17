import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Typeahead } from 'react-bootstrap-typeahead';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/Typeahead.css';
import './css/style.css';
import RequiredMark from './common/RequiredMark';
import SelectHandler from "./SelectHandler";

class Select extends Component {
  constructor(props) {
    super(props);
    this.drawSelector = this.drawSelector.bind(this);
    this.onChangeSelect = this.onChangeSelect.bind(this);

  }

  onChangeSelect(selectedOption) {
    const { onChange, level } = this.props;
    onChange(selectedOption ? [selectedOption] : [], level);
  }

  drawSelector() {
    const {
      options, placeholder, selected, disabled
    } = this.props;
    const { translations, trnPrefix } = this.context;

    return (
        <SelectHandler
            options={options}
            placeholder={placeholder}
            selected={selected}
            disabled={disabled}
            onChange={this.onChangeSelect}
            translations={translations}
            trnPrefix={trnPrefix}
        />
    );
  }

  render() {
    const { label } = this.props;
    return (
      <div>
        <span>{label}</span>
        <RequiredMark />
        {this.drawSelector()}
      </div>
    );
  }
}

Select.contextType = NDDContext;

Select.propTypes = {
  options: PropTypes.array.isRequired,
  label: PropTypes.string.isRequired,
  placeholder: PropTypes.string.isRequired,
  selected: PropTypes.array,
  onChange: PropTypes.func.isRequired,
  level: PropTypes.number.isRequired,
  disabled: PropTypes.bool.isRequired
};
Select.defaultProps = {
  selected: [],
  disabled: false
};
const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Select);
