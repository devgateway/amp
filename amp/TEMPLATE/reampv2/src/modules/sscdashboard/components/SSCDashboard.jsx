import React, {Component} from "react";
import * as StartupActions from '../actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

class SSCDashboard extends Component {

    constructor(props) {
        super(props);
    }
    componentDidMount() {
        this.props.startupAction();
    }

    render() {
        return (<div>SSC dashboard</div>);
    }
}

const mapStateToProps = state => ({...state});

const mapDispatchToProps = dispatch => bindActionCreators(Object.assign({}, StartupActions), dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(SSCDashboard);
