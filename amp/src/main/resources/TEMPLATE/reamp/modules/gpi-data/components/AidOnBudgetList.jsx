import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import AidOnBudgetRow from './AidOnBudgetRow.jsx';
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as commonListsActions from  '../actions/CommonListsActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import { Alert } from 'react-bootstrap';
import DecimalFormat from '../common/decimal-format.jsx';
import Utils from '../common/utils.jsx';
class AidOnBudgetList extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
                errors: [],
                infoMessages:[]
        };

        this.addNew = this.addNew.bind(this);
        this.showErrors = this.showErrors.bind(this);
        this.showInfoMessages = this.showInfoMessages.bind(this);
        this.saveAllEdits = this.saveAllEdits.bind(this);
        this.goToClickedPage = this.goToClickedPage.bind(this);
        this.goToNextPage = this.goToNextPage.bind(this);
        this.goToPreviousPage = this.goToPreviousPage.bind(this);
        this.sort = this.sort.bind(this);
        this.showSortCaret =  this.showSortCaret.bind(this);
        this.isEditing = this.isEditing.bind(this);
    }

    componentWillMount() {
        this.props.actions.loadAidOnBudgetList({paging: this.props.paging, sorting: this.props.sorting});
        this.props.actions.getCurrencyList();
        this.props.actions.getOrgList(false);
        this.props.actions.getSettings();
    }

    componentWillReceiveProps(nextProps) {
        this.setState({errors: nextProps.errors, infoMessages:  nextProps.infoMessages});
    }

    addNew() {
        this.props.actions.addNewAidOnBudget();
    }

    goToClickedPage(event){
        const pageNumber = event.target.getAttribute('data-page');
        this.goToPage(pageNumber);
    }

    goToNextPage() {
        const pageNumber = ++this.props.paging.currentPageNumber
        this.goToPage(pageNumber);
    }

    goToPreviousPage(){
        const pageNumber = --this.props.paging.currentPageNumber;
        this.goToPage(pageNumber);
    }

    goToPage(pageNumber){
        if(this.isEditing() == false){
            const loadParams = {};
            loadParams.paging = this.props.paging;
            loadParams.sorting = this.props.sorting;
            loadParams.paging.currentPageNumber = pageNumber;
            loadParams.paging.offset = ((pageNumber - 1) * this.props.paging.recordsPerPage);
            this.props.actions.loadAidOnBudgetList(loadParams);
        }
    }

    sort(event) {
        if(this.isEditing() == false){
            const field = event.target.getAttribute('data-field');
            const loadParams = {};
            loadParams.paging = this.props.paging;
            loadParams.sorting = this.props.sorting;
            if (loadParams.sorting.orderBy === field) {
                loadParams.sorting.sortOrder = loadParams.sorting.sortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                loadParams.sorting.orderBy = field;
                loadParams.sorting.sortOrder = 'asc';
            }
            this.props.actions.loadAidOnBudgetList(loadParams);
        }
    }

    saveAllEdits() {
        const list = this.props.aidOnBudgetList.filter(aidOnBudget => {return aidOnBudget.isEditing});
        this.props.actions.saveAllEdits(list);
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
        return (this.state.infoMessages.length > 0 &&
                <div className="alert alert-info" role="alert">
                {this.state.infoMessages.map((info, index) =>
                <span  key={index} >{this.props.translate(info.messageKey, info.params)} <br/></span>
                )}
        </div>)
    }

    showSortCaret(field){
        var className = '';
        if(this.props.sorting.sortOrder == 'asc' && field === this.props.sorting.orderBy){
            className = 'glyphicon glyphicon-chevron-up';
        } else if(this.props.sorting.sortOrder == 'desc' && field === this.props.sorting.orderBy){
            className = 'glyphicon glyphicon-chevron-down';
        }

        return className;
    }

    isEditing(){
       return this.props.aidOnBudgetList.filter(aidOnBudget => {return aidOnBudget.isEditing}).length > 0;
    }

    render() {
        const pages = ([...Array(this.props.paging.totalPageCount + 1).keys()]).slice(1);
        const numberFormatter = new DecimalFormat(this.props.settings['number-format'] || "");
        return (
                <div>
                <p className="indicator-description">{this.props.translations['amp.gpi-data-aid-on-budget:header-info']}</p>
                <div className="panel panel-default">
                <div className="panel-body custom-panel">
                <span className="glyphicon glyphicon-plus" onClick={this.addNew}></span>
                <span  onClick={this.addNew} className="add-new-text">{ Utils.capitalizeFirst(this.props.translations['amp.gpi-data:add-new']) } </span>
                <span className="insert-data-text">{this.props.translations['amp.gpi-data:insert-data']}</span>
                <span> / </span> <span className="glyphicon glyphicon-ok-circle success-color"> </span> <span className="click-save-text" >{this.props.translations['amp.gpi-data:click-save']}</span>
                <span> / </span><span className="required-fields">{this.props.translations['amp.gpi-data:required-fields']}</span>
                <span className="float-right button-wrapper"> <button type="button" className="btn btn-success" onClick = {this.saveAllEdits}>{this.props.translations['amp.gpi-data:button-save-all-edits']}</button></span>

                </div>
                </div>
                {this.showErrors()}
                {this.showInfoMessages()}
                {(this.props.settings['number-divider'] != 1) &&
                    <span className="amount-units">{this.props.translations['amp.gpi-data:amount-in-' + this.props.settings['number-divider']]}</span>
                }

                <table className="table table-striped">
                <thead>
                <tr>
                <td className="date-column"><span className="error-color" >*&nbsp;</span><span data-field="indicatorDate" onClick={this.sort} >{this.props.translations['amp.gpi-data-aid-on-budget:date']}</span>&nbsp;<span className = {this.showSortCaret('indicatorDate')} ></span></td>
                <td><span className="error-color" >*&nbsp;</span><span data-field="donor"  onClick={this.sort}>{this.props.translations['amp.gpi-data-aid-on-budget:donor-agency']}</span>&nbsp;<span className = {this.showSortCaret('donor')} ></span></td>
                <td className="amount-column"><span className="error-color" >*&nbsp;</span><span data-field="amount" onClick={this.sort}>{this.props.translations['amp.gpi-data-aid-on-budget:amount']}</span>&nbsp;<span className = {this.showSortCaret('amount')} ></span></td>
                <td className="currency-column"><span className="error-color" >*&nbsp;</span><span data-field="currency" onClick={this.sort} >{Utils.capitalizeFirst(this.props.translations['amp.gpi-data-aid-on-budget:currency'])}</span>&nbsp;<span className = {this.showSortCaret('currency')} ></span></td>
                <td className="actions-column">{this.props.translations['amp.gpi-data-aid-on-budget:action']}</td>
                </tr>
                </thead>
                <tbody>
                {this.props.aidOnBudgetList.map(aidOnBudget =>
                <AidOnBudgetRow aidOnBudget={aidOnBudget} currencyList={this.props.currencyList} orgList={this.props.orgList} settings={this.props.settings} key={aidOnBudget.id || 'c' + aidOnBudget.cid} errors={this.props.errors} numberFormatter={numberFormatter}/>
                )}
                </tbody>
                </table>
                {pages.length > 1 &&
                    <nav>
                    <ul className="pagination fixed-pagination">
                    <li className={this.props.paging.currentPageNumber == 1 ? 'page-item disabled': 'page-item' }>
                    <a className="page-link previ pagination-link" aria-label="Previous" data-page="-"  onClick={this.goToPreviousPage}>
                    <span aria-hidden="true" data-page="-">&laquo;</span>
                <span className="sr-only">{this.props.translations['amp.gpi-data:button-previous']}</span>
                </a>
                </li>
                {pages.map(pageNumber =>
                <li className={this.props.paging.currentPageNumber == pageNumber ? 'page-item  active': 'page-item' } key={pageNumber} ><a className="page-link pagination-link" data-page={pageNumber} onClick={this.goToClickedPage}>{pageNumber}</a></li>
                )}
                <li className={this.props.paging.currentPageNumber == this.props.paging.totalPageCount ? 'page-item disabled': 'page-item' }>
                <a className="page-link pagination-link"  aria-label="Next" data-page="+" onClick={this.goToNextPage}>
                <span aria-hidden="true" data-page="+">&raquo;</span>
                <span className="sr-only">{this.props.translations['amp.gpi-data:button-next']}</span>
                </a>
                </li>
                </ul>
                </nav>}
                </div>
        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        aidOnBudgetList: state.aidOnBudget.aidOnBudgetList || [],
        paging: state.aidOnBudget.paging,
        sorting: state.aidOnBudget.sorting,
        errors: state.aidOnBudget.errors || [],
        infoMessages: state.aidOnBudget.infoMessages || [],
        currencyList: state.commonLists.currencyList || [],
        orgList: state.commonLists.orgList || [],
        settings: state.commonLists.settings || {},
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, aidOnBudgetActions, commonListsActions), dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(AidOnBudgetList);
