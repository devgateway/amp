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
    TYPE_DST,
    TRN_PREFIX
} from "../constants/Constants";
import * as Utils from "../utils/Utils";
import {sendNDDError, sendNDDPending} from "../reducers/saveNDDReducer";
import saveNDD from "../actions/saveNDD";
import Notifications from "./Notifications";

class FormPrograms extends Component {
    constructor(props) {
        super(props);
        this.state = {data: [], validationErrors: undefined};
        this.addRow = this.addRow.bind(this);
        this.saveAll = this.saveAll.bind(this);
        this.onRowChange = this.onRowChange.bind(this);
        this.remove = this.remove.bind(this);
        this.clearMessages = this.clearMessages.bind(this);
    }

    componentDidMount() {
        const {ndd} = this.context;
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
        const {translations} = this.context;
        if (window.confirm(translations[TRN_PREFIX + 'confirm-remove-row'])) {
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
        const {data} = this.state;
        const {saveNDD, translations} = this.props;
        const validationResult = Utils.validate(data);
        if (validationResult === 0) {
            const toSave = [];
            data.forEach(pair => {
                toSave.push({
                    [SRC_PROGRAM]: pair[SRC_PROGRAM].lvl3.id,
                    [DST_PROGRAM]: pair[DST_PROGRAM].lvl3.id,
                });
            });
            saveNDD(toSave);
            this.clearMessages();
        } else {
            this.setState({validationErrors: translations[TRN_PREFIX + 'validation_error_' + validationResult]})
        }
    }

    render() {
        const {data, validationErrors} = this.state;
        const {error} = this.props;
        let messages = [];
        if (error) {
            messages.push({isError: true, text: error.toString()});
        }
        if (validationErrors) {
            messages.push({isError: true, text: validationErrors});
        }
        return (<div className="form-container">
            <Header onAddRow={this.addRow} onSaveAll={this.saveAll}/>
            <Notifications messages={messages}/>
            <ProgramSelectGroupList list={data} onChange={this.onRowChange} remove={this.remove}/>
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