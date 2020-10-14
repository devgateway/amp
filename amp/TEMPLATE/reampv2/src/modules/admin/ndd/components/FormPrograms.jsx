import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {NDDContext} from './Startup';
import './css/style.css';
import ProgramSelectGroupList from "./ProgramSelectGroupList";
import Header from "./Header";
import {DST_PROGRAM, PROGRAM, PROGRAM_MAPPING, SRC_PROGRAM, TYPE_SRC, TYPE_DST} from "../constants/Constants";
import * as Utils from "../utils/Utils";

class FormPrograms extends Component {
    constructor(props) {
        super(props);
        this.state = {data: []};
        this.addRow = this.addRow.bind(this);
        this.saveAll = this.saveAll.bind(this);
        this.onRowChange = this.onRowChange.bind(this);
    }

    componentDidMount() {
        const {ndd} = this.context;
        const {data} = this.state;
        // Load saved mapping.
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
        this.setState({data: data});
    }

    addRow() {
        const {data} = this.state;
        const pair = {
            [SRC_PROGRAM]: {},
            [DST_PROGRAM]: {},
            id: Math.random() * -1
        };
        data.push(pair);
        this.setState({data: data});
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

    saveAll() {
        alert('saveAll');
    }

    render() {
        const {data} = this.state;
        return (<div className="form-container">
            <Header onAddRow={this.addRow} onSaveAll={this.saveAll}/>
            <ProgramSelectGroupList list={data} key={Math.random()} onChange={this.onRowChange}/>
        </div>);
    }
}

FormPrograms.contextType = NDDContext;

FormPrograms.propTypes = {}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(FormPrograms);