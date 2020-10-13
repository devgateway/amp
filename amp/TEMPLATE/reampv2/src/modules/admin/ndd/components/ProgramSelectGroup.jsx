import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import {CHILDREN, PROGRAM, FIRST_LEVEL, SECOND_LEVEL, THIRD_LEVEL, STATE_LEVEL_FIELD} from '../constants/Constants'
import * as Constants from "../constants/Constants";
import '../../../../../node_modules/react-bootstrap-typeahead/css/Typeahead.min.css';
import './css/style.css';
import ProgramSelect from "./ProgramSelect";

class ProgramSelectGroup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: undefined, // todo: remove?
            [STATE_LEVEL_FIELD + FIRST_LEVEL]: undefined,
            [STATE_LEVEL_FIELD + SECOND_LEVEL]: undefined,
            [STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined
        };
        this.onSelectChange = this.onSelectChange.bind(this);
        this.getOptionsForLevel = this.getOptionsForLevel.bind(this);
        this.getSelectedForLevel = this.getSelectedForLevel.bind(this);
    }

    componentDidMount() {
        const {data, type} = this.props;
        if (data && data[type + PROGRAM]) {
            const populated = this.findProgramInTree(data[type + PROGRAM]);
            const newState = {
                [STATE_LEVEL_FIELD + FIRST_LEVEL]: populated.lvl1,
                [STATE_LEVEL_FIELD + SECOND_LEVEL]: populated.lvl2,
                [STATE_LEVEL_FIELD + THIRD_LEVEL]: populated.lvl3
            }
            this.setState(newState);
        }
    }

    onSelectChange(selected, lvl) {
        const {onChange} = this.props;
        let id = null;
        let value = null;
        if (selected && selected[0] && selected[0].id) {
            id = selected[0].id;
            value = selected[0].value;
        }
        let level3;
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
                    level3 = {id, value}
                } else {
                    this.setState({[STATE_LEVEL_FIELD + THIRD_LEVEL]: undefined});
                }
                break;
        }
        onChange(id, value, level3);
    }

    getOptionsForLevel(level) {
        const {ndd} = this.context;
        const {type} = this.props;
        let options = [];
        switch (level) {
            case FIRST_LEVEL:
                if (ndd && ndd[type + PROGRAM]) {
                    options = ndd[type + PROGRAM][CHILDREN].map(i => {
                        return {id: i.id, value: i.value}
                    });
                }
                break;
            case SECOND_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + FIRST_LEVEL]) {
                    options = ndd[type + PROGRAM][CHILDREN]
                        .find(i => i.id === this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id)[CHILDREN];
                }
                break;
            case THIRD_LEVEL:
                if (this.state[STATE_LEVEL_FIELD + SECOND_LEVEL]) {
                    options = ndd[type + PROGRAM][CHILDREN]
                        .find(i => i.id === this.state[STATE_LEVEL_FIELD + FIRST_LEVEL].id)[CHILDREN]
                        .find(i => i.id === this.state[STATE_LEVEL_FIELD + SECOND_LEVEL].id)[CHILDREN];
                }
                break;
        }
        return options;
    }

    findProgramInTree(id) {
        let lvl1 = {}, lvl2 = {}, lvl3;
        const {ndd} = this.context;
        const {type} = this.props;
        ndd[type + PROGRAM][CHILDREN].forEach(l1 => {
            l1[CHILDREN].forEach(l2 => {
                const l3 = l2[CHILDREN].find(l3 => l3.id === id);
                if (l3) {
                    lvl3 = l3;
                    lvl2.id = l2.id;
                    lvl2.value = l2.value;
                    lvl1.id = l1.id;
                    lvl1.value = l1.value;
                }
            });
        });
        return {lvl1, lvl2, lvl3};
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
        const {type} = this.props;
        return (<div>
            <div style={{width: '100%'}}>
                <ProgramSelect
                    placeholder={translations[Constants.TRN_PREFIX + 'choose-' + type + '-lvl-' + FIRST_LEVEL]}
                    label={translations[Constants.TRN_PREFIX + type + '-program-lvl-' + FIRST_LEVEL]}
                    options={this.getOptionsForLevel(FIRST_LEVEL)}
                    selected={this.getSelectedForLevel(FIRST_LEVEL)}
                    onChange={this.onSelectChange}
                    level={FIRST_LEVEL}/>
                <ProgramSelect
                    placeholder={translations[Constants.TRN_PREFIX + 'choose-' + type + '-lvl-' + SECOND_LEVEL]}
                    label={translations[Constants.TRN_PREFIX + type + '-program-lvl-' + SECOND_LEVEL]}
                    options={this.getOptionsForLevel(SECOND_LEVEL)}
                    selected={this.getSelectedForLevel(SECOND_LEVEL)}
                    onChange={this.onSelectChange}
                    level={SECOND_LEVEL}/>
                <ProgramSelect
                    placeholder={translations[Constants.TRN_PREFIX + 'choose-' + type + '-lvl-' + THIRD_LEVEL]}
                    label={translations[Constants.TRN_PREFIX + type + '-program-lvl-' + THIRD_LEVEL]}
                    options={this.getOptionsForLevel(THIRD_LEVEL)}
                    selected={this.getSelectedForLevel(THIRD_LEVEL)}
                    onChange={this.onSelectChange}
                    level={THIRD_LEVEL}/>
            </div>
        </div>);
    }
}

ProgramSelectGroup.propTypes = {
    type: PropTypes.string.isRequired,
    data: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
}

ProgramSelectGroup.contextType = NDDContext;
const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroup);