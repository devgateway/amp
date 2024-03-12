import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

//import * as commonListsActions from '../actions/CommonListsActions';

/**
 *
 */
class Home extends Component {
    constructor(props, context) {
        super(props, context);
        //this.state = { currentActivity: null};
    }


    render() {
        console.log('hola');
        return (
            <div>
                <div className="title-bar">
                    <h2>TEMP</h2>
                </div>
            </div>

        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    //return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) };
    return {actions: bindActionCreators({}, dispatch)};
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);

