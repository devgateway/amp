// TODO: esto contiene el boton +, el boton save all, los mensajes de error, etc y la tabla con los programs.
// ACA GUARDO EL ESTADO GENERAL, O SEA LA LISTA DE PROGRAMS DE LA TABLA.
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import {NDDContext} from './Startup';
import './css/style.css';
import ProgramSelectGroupList from "./ProgramSelectGroupList";
import Header from "./Header";

class FormPrograms extends Component {
    constructor(props) {
        super(props);
        this.state = {data: []};
        this.addRow = this.addRow.bind(this);
        this.saveAll = this.saveAll.bind(this);
        this.onRowChange = this.onRowChange.bind(this);
    }

    addRow() {
        const {data} = this.state;
        const pair = {
            src: undefined,
            dst: undefined,
            id: Math.random() * -1
        };
        data.push(pair);
        this.setState({data: data});
    }

    onRowChange() {

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