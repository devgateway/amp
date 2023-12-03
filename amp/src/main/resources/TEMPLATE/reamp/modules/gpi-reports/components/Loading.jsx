import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
class Loading extends Component {
    constructor( props, context ) {
        super( props, context );
    }

    render() {
            return (<div className="processing">
                    <div className="processing_inner">
                    <div className="processing_content">
                      <span className="processing_image">&nbsp;</span>
                      <span className="processing_message">{this.props.translations['amp-gpi-reports:loading-message']}</span>
                    </div>
                     </div>
                    </div>
                   );

    }

}

function mapStateToProps( state, ownProps ) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return {actions: bindActionCreators({}, dispatch)}
}

export default connect( mapStateToProps, mapDispatchToProps )( Loading );
