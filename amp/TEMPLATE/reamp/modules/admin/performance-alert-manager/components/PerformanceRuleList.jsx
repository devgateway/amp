import React, {
    Component,
    PropTypes
} from 'react';
import {
    connect
} from 'react-redux';
import {
    bindActionCreators
} from 'redux';
require('../styles/less/main.less');
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions'
import * as performanceRuleActions from '../actions/PerformanceRuleActions';;
import * as Constants from '../common/Constants';
import ToolBar from './ToolBar';
import PagingSection from './PagingSection';
import PerformanceRuleForm from './PerformanceRuleForm';
import PerformanceRuleRow from './PerformanceRuleRow'

export default class PerformanceRuleList extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};      
        this.goToPage = this.goToPage.bind(this);
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind(this);
    }

    componentWillMount() {  
        this.props.actions.getTypeList();
        this.props.actions.getLevelList();        
        this.props.actions.loadPerformanceRuleList({paging: this.props.paging});
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
    
    render() {          
        return (
                <div>
                <ToolBar/> 
                {this.props.currentPerformanceRule &&
                    <PerformanceRuleForm {...this.props}/>
                }                
                <div className="panel">
                    <table className="table data-table">
                        <thead>
                            <tr>                
                                <th >{this.props.translations['amp.performance-rule:name']}</th>
                                <th >{this.props.translations['amp.performance-rule:type']}</th>
                                <th >{this.props.translations['amp.performance-rule:level']}</th>
                                <th className="actions-column">{this.props.translations['amp.performance-rule:action']}</th>
                            </tr>
                        </thead>
                        <tbody>               
                            {this.props.performanceRuleList.map((performanceRule, i) => 
                               <PerformanceRuleRow performanceRule={performanceRule} key={i}/>
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
        settings: state.commonLists.settings || {},
        performanceRuleList: state.performanceRule.performanceRuleList,
        currentPerformanceRule: state.performanceRule.currentPerformanceRule
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, performanceRuleActions, commonListsActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PerformanceRuleList);