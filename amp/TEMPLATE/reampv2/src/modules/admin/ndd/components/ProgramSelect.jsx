import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Typeahead} from 'react-bootstrap-typeahead';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/Typeahead.css';
import './css/style.css';

class ProgramSelect extends Component {
    constructor(props) {
        super(props);
        this.drawSelector = this.drawSelector.bind(this);
    }

    onChangeSelect(selected) {
        const {onChange, level} = this.props;
        onChange(selected, level);
    }

    drawSelector() {
        const {options, placeholder, selected} = this.props;
        const isValid = (selected && selected.length === 1);
        return (<Typeahead
            id="basic-typeahead-single"
            labelKey="value"
            className={!isValid ? 'is-invalid' : ''}
            options={options}
            clearButton
            onChange={this.onChangeSelect.bind(this)}
            selected={selected}
            placeholder={placeholder}/>);
    }

    render() {
        const {label, options} = this.props;
        if (options.length === 0) {
            return null
        } else {
            return (<div>
                <span>{label}</span>
                {this.drawSelector()}
            </div>);
        }
    }
}

ProgramSelect.contextType = NDDContext;

ProgramSelect.propTypes = {
    options: PropTypes.array.isRequired,
    label: PropTypes.string.isRequired,
    placeholder: PropTypes.string.isRequired,
    selected: PropTypes.array,
    onChange: PropTypes.func.isRequired,
    level: PropTypes.number.isRequired
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelect);