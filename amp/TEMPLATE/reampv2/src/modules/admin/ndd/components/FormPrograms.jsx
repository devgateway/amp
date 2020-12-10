import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {NDDContext} from './Startup';
import './css/style.css';
import ProgramSelectGroupList from "./ProgramSelectGroupList";
import Header from "./Header";
import {
    DST_PROGRAM,
    PROGRAM,
    PROGRAM_MAPPING,
    SRC_PROGRAM,
    TYPE_SRC,
    TYPE_DST
} from "../constants/Constants";
import * as Utils from "../utils/Utils";
import {sendNDDError, sendNDDPending} from "../reducers/saveNDDReducer";
import saveNDD from "../actions/saveNDD";
import Notifications from "./Notifications";
import ProgramsHeader from "./ProgramsHeader";

class FormPrograms extends Component {
    constructor(props) {
        super(props);
        this.state = {data: [], validationErrors: undefined, src: undefined, dst: undefined, programs: undefined};
        this.addRow = this.addRow.bind(this);
        this.saveAll = this.saveAll.bind(this);
        this.onRowChange = this.onRowChange.bind(this);
        this.remove = this.remove.bind(this);
        this.clearMessages = this.clearMessages.bind(this);
        this.onChangeMainProgram = this.onChangeMainProgram.bind(this);
        this.clearAll = this.clearAll.bind(this);
    }

    componentDidMount() {
        const {ndd, programs} = this.context;
        // Load main programs.
        this.setState(previousState => {
            if (ndd[SRC_PROGRAM]) {
                const src = {id: ndd[SRC_PROGRAM].id, value: ndd[SRC_PROGRAM].value};
                return {src};
            }
            return {src: undefined};
        });
        this.setState(previousState => {
            if (ndd[DST_PROGRAM]) {
                const dst = {id: ndd[DST_PROGRAM].id, value: ndd[DST_PROGRAM].value};
                return {dst};
            }
            return {dst: undefined};
        });

        // Available programs.
        this.setState(previousState => {
            return {programs: programs};
        });

        // Load saved mapping.
        this.setState(previousState => {
            const data = [...previousState.data];
            if (ndd[PROGRAM_MAPPING]) {
                ndd[PROGRAM_MAPPING].forEach(pm => {
                    const fullTreeSrc = Utils.findProgramInTree(pm[SRC_PROGRAM], ndd, TYPE_SRC);
                    const fullTreeDst = Utils.findProgramInTree(pm[DST_PROGRAM], ndd, TYPE_DST);
                    const pair = {};
                    pair[SRC_PROGRAM] = fullTreeSrc;
                    pair[DST_PROGRAM] = fullTreeDst;
                    pair.id = pair[SRC_PROGRAM].lvl3.id + '' + pair[DST_PROGRAM].lvl3.id;
                    data.push(pair);
                });
            }
            return {data};
        });
    }

    addRow() {
        this.setState(previousState => {
            const data = [...previousState.data];
            const pair = {
                [SRC_PROGRAM]: {},
                [DST_PROGRAM]: {},
                id: Math.random() * -1
            };
            data.push(pair);
            return {data};
        });
    }

    onRowChange(level1, level2, level3, type, id) {
        const {data} = this.state;
        // Find and remove.
        let pair = data.splice(data.findIndex(i => i[type + PROGRAM].id === id), 1);
        pair[0][type + PROGRAM].lvl1 = level1;
        pair[0][type + PROGRAM].lvl2 = level2;
        pair[0][type + PROGRAM].lvl3 = level3;
        data.push(pair[0]);
        this.setState(data);
    }

    remove(row) {
        const {translations, trnPrefix} = this.context;
        if (window.confirm(translations[trnPrefix + 'confirm-remove-row'])) {
            this.setState(previousState => {
                const data = [...previousState.data];
                data.splice(data.findIndex(i => i.id === row.id), 1);
                return {data};
            });
        }
    }

    clearMessages() {
        this.setState({validationErrors: undefined});
    }

    saveAll() {
        const {data, src, dst} = this.state;
        const {saveNDD, translations} = this.props;
        const {api, trnPrefix} = this.context;
        const validateMappings = Utils.validate(data);
        if (validateMappings === 0) {
            const validateMain = Utils.validateMainPrograms(src, dst);
            if (validateMain === 0) {
                const mappings = [];
                data.forEach(pair => {
                    mappings.push({
                        [SRC_PROGRAM]: pair[SRC_PROGRAM].lvl3.id,
                        [DST_PROGRAM]: pair[DST_PROGRAM].lvl3.id,
                    });
                });
                saveNDD(src, dst, mappings, api.programsSave, api.mappingSave);
                this.clearMessages();
            } else {
                this.setState({validationErrors: translations[trnPrefix + 'validation_error_' + validateMain]});
            }
        } else {
            this.setState({validationErrors: translations[trnPrefix + 'validation_error_' + validateMappings]});
        }
    }

    onChangeMainProgram(type, program) {
        const {translations, trnPrefix} = this.context;
        const {data} = this.state;
        const newProgram = (program && program.length > 0) ? program[0] : {};
        const oldProgram = (this.state[type] ? this.state[type] : {});
        if (oldProgram.id !== newProgram.id) {
            if (oldProgram.id !== undefined) {
                if (data.length === 0 || window.confirm(translations[trnPrefix + 'warning_on_change_main_programs'])) {
                    if (newProgram.id !== undefined) {
                        // Old Program -> New Program.
                        this.setState(previousState => {
                            return {[type]: newProgram};
                        });
                    } else {
                        // Old Program -> Nothing.
                        this.setState(previousState => {
                            return {[type]: undefined};
                        });
                    }
                    this.clearAll();
                } else {
                    // Revert to previous Program.
                    this.setState(previousState => {
                        return previousState;
                    });
                    // TODO: set focus in the selector.
                }
            } else {
                // Nothing -> Program.
                this.setState(previousState => {
                    return {[type]: newProgram};
                });
            }
        }
    }

    clearAll() {
        this.setState({
            data: []
        });
    }

    revertAllChanges() {
        window.location.reload();
    }

    render() {
        const {data, validationErrors, src, dst} = this.state;
        const {error, pending} = this.props;
        let messages = [];
        if (error) {
            messages.push({isError: true, text: error.toString()});
        }
        if (validationErrors) {
            messages.push({isError: true, text: validationErrors});
        }
        return (<div className="form-container">
            <ProgramsHeader onChange={this.onChangeMainProgram} src={src} dst={dst} key={Math.random()}/>
            <Header onAddRow={this.addRow} onSaveAll={this.saveAll} onRevertAll={this.revertAllChanges}
                    disabled={pending}/>
            <Notifications messages={messages}/>
            <ProgramSelectGroupList list={data} onChange={this.onRowChange} remove={this.remove} src={src} dst={dst}/>
        </div>);
    }
}

FormPrograms.contextType = NDDContext;

FormPrograms.propTypes = {}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations,
    error: sendNDDError(state.saveNDDReducer),
    pending: sendNDDPending(state.saveNDDReducer),
});
const mapDispatchToProps = dispatch => bindActionCreators({saveNDD: saveNDD}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(FormPrograms);