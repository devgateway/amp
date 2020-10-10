import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Typeahead} from 'react-bootstrap-typeahead';
import {getNDD, getNDDError, getNDDPending} from '../reducers/startupReducer';
import fetchNDD from '../actions/fetchNDD';
import {TranslationContext} from './Startup';
import {CHILDREN, SRC_PROGRAM, VALUE} from '../constants/Constants'
import * as Constants from "../constants/Constants";
import '../../../../../node_modules/react-bootstrap-typeahead/css/Typeahead.min.css';
import './css/style.css';
import ProgramSelect from "./ProgramSelect";

class ProgramSelectGroup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            src2Lvl: undefined,
            src3Lvl: undefined,
            src4Lvl: undefined
        };
        this.shouldComponentRender = this.shouldComponentRender.bind(this);
    }

    componentDidMount() {
        const {fetchNDD} = this.props;
        fetchNDD();
    }

    shouldComponentRender() {
        return !this.props.pending;
    }

    onSelectChange(id, value) {
        console.error(id + value);
    }

    render() {
        const {ndd} = this.props;
        const {translations} = this.context;
        if (!this.shouldComponentRender() || ndd.length === 0) {
            return <div>loading...</div>
        } else {
            const label = translations[Constants.TRN_PREFIX + 'src-program-lvl-2'] + ': ' + ndd[SRC_PROGRAM][VALUE];
            let options = [];
            if (ndd && ndd[SRC_PROGRAM]) {
                options = ndd[SRC_PROGRAM][CHILDREN].map(i => {
                    return {id: i.id, value: i.value}
                });
            }
            /*const selected = src2Lvl ? [{
                value: src2Lvl
            }] : [];*/
            return (<div>
                <h4>{translations[Constants.TRN_PREFIX + 'src-program-lvl-1']}: {ndd[SRC_PROGRAM][VALUE]}</h4>
                <div style={{width: '50%'}}>
                    <ProgramSelect placeholder={translations[Constants.TRN_PREFIX + 'choose-src-lvl-2']}
                                   label={translations[Constants.TRN_PREFIX + 'src-program-lvl-2']} options={options}
                                   selected={[]} onChange={this.onSelectChange}/>
                </div>
            </div>);
        }
    }
}

ProgramSelectGroup.contextType = TranslationContext;

const mapStateToProps = state => ({
    error: getNDDError(state.startupReducer),
    ndd: getNDD(state.startupReducer),
    pending: getNDDPending(state.startupReducer),
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({fetchNDD: fetchNDD}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ProgramSelectGroup);