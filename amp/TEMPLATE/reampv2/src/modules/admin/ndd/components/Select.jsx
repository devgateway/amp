import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Typeahead } from 'react-bootstrap-typeahead';
import PropTypes from 'prop-types';
import { NDDContext } from './Startup';
import './css/Typeahead.css';
import './css/style.css';
import RequiredMark from './common/RequiredMark';

class Select extends Component {
  constructor(props) {
    super(props);
    this.drawSelector = this.drawSelector.bind(this);
  }

  onChangeSelect(selected) {
    const { onChange, level } = this.props;
    onChange(selected, level);
  }

  drawSelector() {
    const {
      options, placeholder, selected, disabled
    } = this.props;
    const { translations, trnPrefix } = this.context;
    const isValid = (selected && selected.length === 1);
    const sortedOptions = options ? options.sort((a, b) => a.value.localeCompare(b.value)) : [];
    return (
      <Typeahead
        id="basic-typeahead-single"
        labelKey="value"
        className={!isValid ? 'is-invalid' : ''}
        options={sortedOptions || []}
        clearButton
        onChange={this.onChangeSelect.bind(this)}
        selected={selected}
        placeholder={placeholder}
        disabled={disabled}
        emptyLabel={translations[`${trnPrefix}no-matches-found`]}
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

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Select);
