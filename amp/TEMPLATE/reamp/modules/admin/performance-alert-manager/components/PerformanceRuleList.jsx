import React, {
    Component,
    PropTypes
} from 'react';
import ReactDOM from 'react-dom';
import {
    connect
} from 'react-redux';
import {
    bindActionCreators
} from 'redux';
require('../styles/less/main.less');
import * as startUp from '../actions/StartUpAction';
import * as performanceRuleActions from '../actions/PerformanceRuleActions';;
import * as Constants from '../common/Constants';
import ToolBar from './ToolBar';
import PagingSection from './PagingSection';
import PerformanceRuleForm from './PerformanceRuleForm';
import PerformanceRuleRow from './PerformanceRuleRow'

class PerformanceRuleList extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};
        this.goToPage = this.goToPage.bind(this);
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind(this);
        this.focusOnForm = this.focusOnForm.bind(this);
    }

    componentWillMount() {
        this.props.actions.getTypeList();
        this.props.actions.getLevelList();
        this.props.actions.loadPerformanceRuleList({paging: this.props.paging});
    }

    componentDidUpdate() {
        if(this.props.errors.length > 0 || this.props.infoMessages.length > 0) {
            ReactDOM.findDOMNode(this.refs.messageContainer).focus();
        }
    }

    goToPage( pageNumber ) {
        const paging =  Object.assign( {}, this.props.paging);
        paging.currentPageNumber = pageNumber;
        this.props.actions.loadPerformanceRuleList({paging: paging});
    }

    updateRecordsPerPage(recordsPerPage) {
        const paging =  Object.assign( {}, this.props.paging);
        paging.recordsPerPage = recordsPerPage;
        this.props.actions.loadPerformanceRuleList({paging: paging});
    }

    showErrors() {
        const messages = [];
        this.props.errors.forEach((error, index) =>{
            messages.push(<span key={index}>{this.props.translations[error.messageKey]} <br/></span>  )
        });

        return (this.props.errors.length > 0 && <div className="alert alert-danger" role="alert">
                {messages}
        </div>)
    }

    showInfoMessages() {
        return (this.props.infoMessages.length > 0 &&
                <div className="alert alert-info" role="alert">
                {this.props.infoMessages.map((info, index) =>
                <span  key={index} >{this.props.translate(info.messageKey, info.params)} <br/></span>
                )}
        </div>)
    }

    focusOnForm() {
       ReactDOM.findDOMNode(this.refs.form).focus();
    }

    render() {
        return (
                <div>
                <ToolBar/>
                <div ref="messageContainer" tabIndex="0">
                 {this.showInfoMessages()}
                 {this.showErrors()}
                </div>
                <div ref="form" tabIndex="1">
                {this.props.currentPerformanceRule &&
                    <PerformanceRuleForm {...this.props}/>
                }
                </div>
                <div className="panel">
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <td>{this.props.translations['amp.performance-rule:name']}</td>
                                <td>{this.props.translations['amp.performance-rule:type']}</td>
                                <td>{this.props.translations['amp.performance-rule:level']}</td>
                                <td>{this.props.translations['amp.performance-rule:enabled']}</td>
                                <td className="actions-column">{this.props.translations['amp.performance-rule:action']}</td>
                            </tr>
                        </thead>
                        <tbody>
                            {this.props.performanceRuleList.map((performanceRule, i) =>
                               <PerformanceRuleRow performanceRule={performanceRule} key={i} focusOnForm = {this.focusOnForm}/>
                            )}
                        </tbody>
                    </table>
                </div>
                <PagingSection page={this.props.paging} goToPage={this.goToPage.bind(this)} updateRecordsPerPage={this.updateRecordsPerPage.bind(this)}/>
            </div>
        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        paging: state.performanceRule.paging,
        sorting: state.performanceRule.sorting,
        typeList: state.performanceRule.typeList,
        levelList: state.performanceRule.levelList,
        errors: state.performanceRule.errors || [],
        infoMessages: state.performanceRule.infoMessages || [],
        performanceRuleList: state.performanceRule.performanceRuleList,
        currentPerformanceRule: state.performanceRule.currentPerformanceRule
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, performanceRuleActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceRuleList);
