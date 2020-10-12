import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import {CHILDREN, SRC_PROGRAM, FIRST_LEVEL, SECOND_LEVEL, THIRD_LEVEL, STATE_LEVEL_FIELD} from '../constants/Constants'
import * as Constants from "../constants/Constants";
import '../../../../../node_modules/react-bootstrap-typeahead/css/Typeahead.min.css';
import './css/style.css';
import ProgramSelect from "./ProgramSelect";

class ProgramSelectGroup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: undefined,
            [STATE_LEVEL_FIELD + FIRST_LEVEL]: undefined,
            [STATE_LEVEL_FIELD + SECOND_LEVEL]: undefined,
            [STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined
        };
        this.onSelectChange = this.onSelectChange.bind(this);
        this.getOptionsForLevel = this.getOptionsForLevel.bind(this);
        this.getSelectedForLevel = this.getSelectedForLevel.bind(this);
    }

    onSelectChange(selected, lvl) {
        let id = null;
        let value = null;
        if (selected && selected[0] && selected[0].id) {
            id = selected[0].id;
            value = selected[0].value;
        }
        console.error(id + value + lvl);
        switch (lvl) {
            case FIRST_LEVEL:
                if (id && value) {
                    this.setState({[STATE_LEVEL_FIELD + FIRST_LEVEL]: {id: id, value: value}});
                } else {
                    this.setState({[STATE_LEVEL_FIELD + FIRST_LEVEL]: undefined});
                }
                this.setState({[STATE_LEVEL_FIELD + SECOND_LEVEL]: undefined});
                this.setState({[STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined});
                break;
            case SECOND_LEVEL:
                if (id && value) {
                    this.setState({[STATE_LEVEL_FIELD + SECOND_LEVEL]: {id: id, value: value}});
                } else {
                    this.setState({[STATE_LEVEL_FIELD + SECOND_LEVEL]: undefined});
                }
                this.setState({[STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined});
                break;
            case THIRD_LEVEL:
                if (id && value) {
                    this.setState({[STATE_LEVEL_FIELD + THIRD_LEVEL]: {id: id, value: value}});
                } else {
                    this.setState({[STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined});
                }
                break;
        }
    }

    getOptionsForLevel(level) {
        const {ndd} = this.context;
        let options = [];
        switch (level) {
            case FIRST_LEVEL:
                if (ndd && ndd[SRC_PROGRAM]) {
                    options = ndd[SRC_PROGRAM][CHILDREN].map(i => {
                        return {id: i.id, value: i.value}
                    });
                }
                break;
            case SECOND_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + FIRST_LEVEL]) {
                    options = ndd[SRC_PROGRAM][CHILDREN]
                        .find(i => i.id === this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id)[CHILDREN];
                }
                break;
            case THIRD_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + SECOND_LEVEL]) {
                    options = ndd[SRC_PROGRAM][CHILDREN]
                        .find(i => i.id === this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id)[CHILDREN]
                        .find(i => i.id === this.state[STATE_LEVEL_FIELD + SECOND_LEVEL].id)[CHILDREN];
                }
                break;
        }
        return options;
    }

    getSelectedForLevel(level) {
        const selected = [];
        switch (level) {
            case FIRST_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + FIRST_LEVEL]) {
                    selected.push({
                        id: this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id,
                        value: this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].value
                    });
                }
                break;
            case SECOND_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + SECOND_LEVEL]) {
                    selected.push({
                        id: this.state[STATE_LEVEL_FIELD + SECOND_LEVEL].id,
                        value: this.state[STATE_LEVEL_FIELD + SECOND_LEVEL].value
                    });
                }
                break;
            case THIRD_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + THIRD_LEVEL]) {
                    selected.push({
                        id: this.state[STATE_LEVEL_FIELD + THIRD_LEVEL].id,
                        value: this.state[STATE_LEVEL_FIELD + THIRD_LEVEL].value
                    });
                } else {
                    // Auto-select when there is only 1 option available.
                    const options = this.getOptionsForLevel(THIRD_LEVEL);
                    if (options && options.length === 1) {
                        selected.push({
                            id: options[0].id,
                            value: options[0].value
                        });
                    }
                }
                break;
        }
        return selected;
    }

    render() {
        const {translations} = this.context;
        return (<div>
            <div style={{width: '75%'}}>
                <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-' + FIRST_LEVEL]}
                               label={translations[Constants.TRN_PREFIX + 'src-program-lvl-' + FIRST_LEVEL]}
                               options={this.getOptionsForLevel(FIRST_LEVEL)}
                               selected={this.getSelectedForLevel(FIRST_LEVEL)}
                               onChange={this.onSelectChange}
                               level={FIRST_LEVEL}/>
                <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-' + SECOND_LEVEL]}
                               label={translations[Constants.TRN_PREFIX + 'src-program-lvl-' + SECOND_LEVEL]}
                               options={this.getOptionsForLevel(SECOND_LEVEL)}
                               selected={this.getSelectedForLevel(SECOND_LEVEL)}
                               onChange={this.onSelectChange}
                               level={SECOND_LEVEL}/>
                <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-' + THIRD_LEVEL]}
                               label={translations[Constants.TRN_PREFIX + 'src-program-lvl-' + THIRD_LEVEL]}
                               options={this.getOptionsForLevel(THIRD_LEVEL)}
                               selected={this.getSelectedForLevel(THIRD_LEVEL)}
                               onChange={this.onSelectChange}
                               level={THIRD_LEVEL}/>
            </div>
        </div>);
    }
}

ProgramSelectGroup.propTypes = {
    ndd: PropTypes.object.isRequired
}

ProgramSelectGroup.contextType = NDDContext;
const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroup);