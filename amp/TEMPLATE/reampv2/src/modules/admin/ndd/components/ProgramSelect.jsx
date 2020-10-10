import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Typeahead} from 'react-bootstrap-typeahead';
import PropTypes from 'prop-types';
import {TranslationContext} from './Startup';
import '../../../../../node_modules/react-bootstrap-typeahead/css/Typeahead.min.css';
import './css/style.css';

class ProgramSelect extends Component {
    constructor(props) {
        super(props);
        this.state = {
            value: undefined,
            id: undefined
        };
        this.drawSelector = this.drawSelector.bind(this);
    }

    onChangeSelect(selected) {
        const {onChange, level} = this.props;
        if (selected && selected[0]) {
            const value = selected[0].value;
            const id = selected[0].id;
            this.setState({value: value, id: id});
            onChange(id, value, level);
        } else {
            this.setState({value: undefined, id: undefined});
            onChange(null, null, level);
        }
    }

    drawSelector() {
        const {options, placeholder} = this.props;
        const {value} = this.state;
        if (options) {
            const selected = value ? [{
                value: value
            }] : [];
            return (<Typeahead
                id="basic-typeahead-single"
                labelKey="value"
                options={options}
                clearButton
                highlightOnlyResult
                onChange={this.onChangeSelect.bind(this)}
                selected={selected}
                placeholder={placeholder}/>);
        }
        return null;
    }

    render() {
        const {label, options} = this.props;
        if (options.length === 0) {
            return <div>No Data</div>
        } else {
            return (<div style={{width: '50%'}}>
                <span>{label}</span>
                {this.drawSelector()}
            </div>);
        }
    }
}

ProgramSelect.contextType = TranslationContext;

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