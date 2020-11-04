import React, {Component} from 'react';
import {NDDTranslationContext} from './components/StartUp';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';

class NDDDashboardHome extends Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return <div>main container</div>
    }
}

const mapStateToProps = state => {
    return {};
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

NDDDashboardHome.contextType = NDDTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(NDDDashboardHome);

