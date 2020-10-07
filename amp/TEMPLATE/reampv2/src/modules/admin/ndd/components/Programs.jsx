import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {getNDD, getNDDError, getNDDPending} from '../reducers/startupReducer';
import fetchNDD from '../actions/fetchNDD';
import {TranslationContext} from './Startup';


class Programs extends Component {
    constructor(props) {
        super(props);
        this.shouldComponentRender = this.shouldComponentRender.bind(this);
    }

    componentDidMount() {
        const {fetchNDD} = this.props;
        fetchNDD();
    }

    shouldComponentRender() {
        return !this.props.pending;
    }

    render() {
        if (!this.shouldComponentRender()) {
            return <div>loading...</div>
        } else {
            return (<div>${JSON.stringify(this.props.ndd)}</div>);
        }
    }
}

Programs.contextType = TranslationContext;

const mapStateToProps = state => ({
    error: getNDDError(state.startupReducer),
    ndd: getNDD(state.startupReducer),
    pending: getNDDPending(state.startupReducer),
    translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({fetchNDD: fetchNDD}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(Programs);