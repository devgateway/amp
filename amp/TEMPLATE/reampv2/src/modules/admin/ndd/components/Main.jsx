import React, {Component} from 'react';
import './css/style.css';
import {NDDContext} from './Startup';
import {
    getNDD,
    getNDDError,
    getNDDPending,
    getPrograms,
    getProgramsPending
} from "../reducers/startupReducer";
import {bindActionCreators} from "redux";
import fetchNDD from "../actions/fetchNDD";
import fetchPrograms from "../actions/fetchAvailablePrograms";
import {connect} from "react-redux";
import FormPrograms from "./FormPrograms";

class Main extends Component {

    constructor(props) {
        super(props);
        this.shouldComponentRender = this.shouldComponentRender.bind(this);
    }

    componentDidMount() {
        const {fetchNDD, fetchPrograms, api} = this.props;

        fetchNDD(api.mappingConfig);
        fetchPrograms(api.programs);
    }

    shouldComponentRender() {
        const {pendingNDD, pendingPrograms} = this.props;
        return !pendingNDD && !pendingPrograms;
    }

    render() {
        const {ndd, programs, api, trnPrefix, isIndirect} = this.props;
        const {translations} = this.context;

        if (!this.shouldComponentRender() || ndd.length === 0) {
            return <div>loading...</div>
        } else {
            return (<div className="ndd-container">
                <NDDContext.Provider value={{ndd: ndd, translations: translations, programs: programs, api: api, trnPrefix: trnPrefix, isIndirect: isIndirect}}>
                    <div className='col-md-12'>
                        <div>
                            <h2 className="title">{translations[trnPrefix + 'title']}</h2>
                            <FormPrograms/>
                        </div>
                    </div>
                </NDDContext.Provider>
            </div>);
        }
    }
}

Main.contextType = NDDContext;

const mapStateToProps = state => ({
    error: getNDDError(state.startupReducer),
    ndd: getNDD(state.startupReducer),
    programs: getPrograms(state.startupReducer),
    pendingNDD: getNDDPending(state.startupReducer),
    pendingPrograms: getProgramsPending(state.startupReducer),
    translations: state.translationsReducer.translations,
});
const mapDispatchToProps = dispatch => bindActionCreators({
    fetchNDD: fetchNDD,
    fetchPrograms: fetchPrograms,
}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(Main);
