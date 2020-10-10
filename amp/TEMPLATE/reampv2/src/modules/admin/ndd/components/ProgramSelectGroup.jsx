import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {TranslationContext} from './Startup';
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
            [STATE_LEVEL_FIELD + FIRST_LEVEL]: {id: undefined, value: undefined},
            [STATE_LEVEL_FIELD + SECOND_LEVEL]: {id: undefined, value: undefined},
            [STATE_LEVEL_FIELD + THIRD_LEVEL]: {id: undefined, value: undefined}
        };
        this.onSelectChange = this.onSelectChange.bind(this);
        this.getOptionsForLevel = this.getOptionsForLevel.bind(this);
        this.getSelectedForLevel = this.getSelectedForLevel.bind(this);
    }

    // TODO: I can have one function with lvl or 3 functions without lvl param.
    onSelectChange(id, value, lvl) {
        console.error(id + value + lvl);
        this.setState({[STATE_LEVEL_FIELD + lvl]: {id: id, value: value}});
    }

    // TODO: same comment.
    getOptionsForLevel(level) {
        const {ndd} = this.props;
        let options = [];
        switch (level) {
            case FIRST_LEVEL:
                if (ndd && ndd[SRC_PROGRAM]) {
                    options = ndd[SRC_PROGRAM][CHILDREN].map(i => {
                        return {id: i.id, value: i.value}
                    });
                }
                break;
        }
        return options;
    }

    getSelectedForLevel(level) {
        return [];
    }

    render() {
        const {translations} = this.context;
        return (<div>
            <div style={{width: '50%'}}>
                <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-' + FIRST_LEVEL]}
                               label={translations[Constants.TRN_PREFIX + 'src-program-lvl-' + FIRST_LEVEL]}
                               options={this.getOptionsForLevel(FIRST_LEVEL)}
                               selected={[]} onChange={this.onSelectChange} level={FIRST_LEVEL}/>
                <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-' + SECOND_LEVEL]}
                               label={translations[Constants.TRN_PREFIX + 'src-program-lvl-' + SECOND_LEVEL]}
                               options={this.getOptionsForLevel(SECOND_LEVEL)}
                               selected={this.getSelectedForLevel(SECOND_LEVEL)} onChange={this.onSelectChange}
                               level={SECOND_LEVEL}/>
                <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-' + THIRD_LEVEL]}
                               label={translations[Constants.TRN_PREFIX + 'src-program-lvl-' + THIRD_LEVEL]}
                               options={this.getOptionsForLevel(THIRD_LEVEL)}
                               selected={this.getSelectedForLevel(THIRD_LEVEL)} onChange={this.onSelectChange}
                               level={THIRD_LEVEL}/>
            </div>
        </div>);
    }
}

ProgramSelectGroup.propTypes = {
    ndd: PropTypes.object.isRequired
}

ProgramSelectGroup.contextType = TranslationContext;
const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroup);