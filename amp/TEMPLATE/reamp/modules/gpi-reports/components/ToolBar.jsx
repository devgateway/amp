import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
class ToolBar extends Component {
    constructor( props, context ) {
        super( props, context );
    }

    componentDidMount() {
    }
    render() {
            return (<div className="container-fluid indicator-nav no-padding">
            <div className="col-md-6 no-padding">
            </div>
            <div className="col-md-6 no-padding">
                <ul className="export-nav">
                    <li>
                        <a onClick={this.props.downloadPdfFile} ><img src="images/export-pdf.svg" /></a>
                    </li>
                    <li>
                        <a onClick={this.props.downloadExcelFile} ><img src="images/export-excel.svg" /></a>
                    </li>
                </ul>
                <div className="btn-action-nav">
                    <button type="button" className="btn btn-action" onClick={this.props.showFilters}>{this.props.translations['amp.gpi-reports:filter-button']}</button>
                    <button type="button" className="btn btn-action" onClick={this.props.showSettings}>{this.props.translations['amp.gpi-reports:settings-button']}</button>
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

export default connect( mapStateToProps, mapDispatchToProps )( ToolBar );
