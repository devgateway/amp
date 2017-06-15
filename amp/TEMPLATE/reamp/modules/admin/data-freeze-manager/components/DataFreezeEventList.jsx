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
import * as startUp from '../actions/StartUpAction';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import * as commonListsActions from '../actions/CommonListsActions';
import DataFreezeEventRow from '../components/DataFreezeEventRow';
import * as Constants from '../common/Constants';
require('../styles/less/main.less');

export default class DataFreezeEventList extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {};
        this.showFreezeOption = this.showFreezeOption.bind(this);
        this.addNew = this.addNew.bind(this);
        this.showErrors = this.showErrors.bind(this);
        this.showInfoMessages = this.showInfoMessages.bind(this);
        this.goToClickedPage = this.goToClickedPage.bind(this);
        this.goToNextPage = this.goToNextPage.bind(this);
        this.goToPreviousPage = this.goToPreviousPage.bind(this);
        this.sort = this.sort.bind(this);
        this.showSortCaret = this.showSortCaret.bind(this);
        this.isEditing = this.isEditing.bind(this);
        this.setFilterElement = this.setFilterElement.bind(this);
        this.showFilterElement = this.showFilterElement.bind(this);
        this.hideFilterElement = this.hideFilterElement.bind(this);
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind(this);
    }

    componentWillMount() {
        this.props.actions.loadDataFreezeEventList({
            paging: this.props.paging,
            sorting: this.props.sorting
        });
        this.props.actions.getSettings();
        if (this.props.context === Constants.DATA_FREEZE_EVENTS) {
            this.initializeFilter();  
        }
            
        
    }


    componentWillReceiveProps(nextProps) {
        this.setState({
            errors: nextProps.errors,
            infoMessages: nextProps.infoMessages
        });
    }

    addNew() {
        this.props.actions.addNewDataFreezeEvent();
    }

    goToClickedPage(event) {
        const pageNumber = event.target.getAttribute('data-page');
        this.goToPage(pageNumber);
    }

    goToNextPage() {
        const pageNumber = ++this.props.paging.currentPageNumber
        this.goToPage(pageNumber);
    }

    goToPreviousPage() {
        const pageNumber = --this.props.paging.currentPageNumber;
        this.goToPage(pageNumber);
    }

    goToPage(pageNumber) {
        if (this.isEditing() == false) {
            const loadParams = {};
            loadParams.paging = this.props.paging;
            loadParams.sorting = this.props.sorting;
            loadParams.paging.currentPageNumber = pageNumber;
            loadParams.paging.offset = ((pageNumber - 1) * this.props.paging.recordsPerPage);
            this.props.actions.loadDataFreezeEventList(loadParams);
        }
    }

    sort(event) {
        if (this.isEditing() == false) {
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
            this.props.actions.loadDataFreezeEventList(loadParams);
        }
    }

    updateRecordsPerPage() {
        if ( this.refs.recordsPerPage && this.refs.recordsPerPage.value ) {
            const loadParams = {};
            loadParams.paging = this.props.paging;
            loadParams.sorting = this.props.sorting;
            loadParams.paging.recordsPerPage = parseInt( this.refs.recordsPerPage.value );
            loadParams.paging.currentPageNumber = 1
            this.props.actions.loadDataFreezeEventList(loadParams);
        }
    }

    
    showErrors() {
        const messages = [];
        this.props.errors.forEach((error, index) => {
            messages.push(<span key={index}>{this.props.translations[error.messageKey]} <br/></span>)
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

    showSortCaret(field) {
        var className = '';
        if (this.props.sorting.sortOrder == 'asc' && field === this.props.sorting.orderBy) {
            className = 'glyphicon glyphicon-chevron-up';
        } else if (this.props.sorting.sortOrder == 'desc' && field === this.props.sorting.orderBy) {
            className = 'glyphicon glyphicon-chevron-down';
        }

        return className;
    }

    isEditing() {
        return this.props.dataFreezeEventList.filter(dataFreezeEvent => {
            return dataFreezeEvent.isEditing
        }).length > 0;
    }

    showFreezeOption(freezeOption) {
        let result = '';
        if (freezeOption === Constants.FREEZE_OPTION_ENTIRE_ACTIVITY) {
            result = this.props.translations['amp.data-freezing:freeze-option-activity'];
        } else if (freezeOption === Constants.FREEZE_OPTION_FUNDING) {
            result = this.props.translations['amp.data-freezing:freeze-option-funding'];
        }
        return result;
    }
    
    initializeFilter() {
        this.filter = new ampFilter({
            draggable: true,
            caller: 'DATA-FREEZE-MANAGER'
        }); 
    }  
    
    setFilterElement() {
       this.filter.setElement( this.refs.filterPopup ); 
    }
    
    showFilterElement() {
        $( this.refs.filterPopup ).show();
    }
    
    hideFilterElement() {
        $( this.refs.filterPopup ).hide();
    }
    
    render() {
        const pages = ([...Array(this.props.paging.totalPageCount + 1).keys()]).slice(1);
        return (
            <div>
                <div id="filter-popup" ref="filterPopup"> </div>                              
                <div >                
                <div className="row">                
                <br/>  
                {this.props.context === Constants.DATA_FREEZE_EVENTS &&  
                <div className="panel panel-default">                 
                <div className="panel-body custom-panel">
                <span className="glyphicon glyphicon-plus" onClick={this.addNew}></span>
                <span  onClick={this.addNew} className="add-new-text">{ this.props.translations['amp.data-freezing:add-new']} </span>
                <span className="insert-data-text">{this.props.translations['amp.data-freezing:insert-data']}</span>
                <span> / </span> <span className="glyphicon glyphicon-ok-circle success-color"> </span> <span className="click-save-text" >{this.props.translations['amp.data-freezing:click-save']}</span>
                <span> / </span><span className="required-fields">{this.props.translations['amp.data-freezing:required-fields']}</span>                        
                </div>                 
                </div>  
                }
                {this.showErrors()}
                {this.showInfoMessages()} 
                    <table className="table table-bordered table-striped data-table">
                      <thead>
                        {this.props.context === Constants.UNFREEZE_ALL &&
                            <tr>
                             <th className="col-md-2">{this.props.translations['amp.data-freezing:data-freeze-date']}</th>
                             
                              <th>{this.props.translations['amp.data-freezing:number-of-activities']}</th>
                            </tr>
                        }
                        {this.props.context === Constants.DATA_FREEZE_EVENTS &&
                          <tr>
                          <th className="col-md-2">       
                            <img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:data-freeze-date']}<span className="error-color" >*&nbsp;</span></th>
                          <th>
                          <img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:grace-period']}<br/>
                          {this.props.translations['amp.data-freezing:days']} 
                          </th>
                          <th><img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:open-period-start']}</th>
                          <th><img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:open-period-end']}</th>
                          <th><img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:number-of-activities']}</th>
                          <th><img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:freeze-options']}<span className="error-color" >*&nbsp;</span></th>
                          <th><img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:notify']}<span className="error-color" >*&nbsp;</span></th>
                          <th><img className="tab-content-icon" src="styles/images/icon-information.svg"/>{this.props.translations['amp.data-freezing:filters']}</th>
                          <th>{this.props.translations['amp.data-freezing:enabled']}</th>
                          <th></th>
                          <th></th>
                          </tr>
                        }
                      </thead>
                      <tbody>
                        {this.props.dataFreezeEventList.map((dataFreezeEvent, i) => 
                          <DataFreezeEventRow dataFreezeEvent={dataFreezeEvent} key={i} filter={this.filter} setFilterElement={this.setFilterElement} showFilterElement={this.showFilterElement} hideFilterElement={this.hideFilterElement} {...this.props}/>                      
                        )}                  
                      
                      </tbody>
                    </table>
                  </div>               
                  </div>
               
                          <div >
                          <div className="row">
                            <div className="col-md-8 pull-right pagination-wrapper">

                              <div className="col-md-4">
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
                        <span className="sr-only">{this.props.translations['amp.data-freeze:button-next']}</span>
                        </a>
                        </li>
                        </ul>
                        </nav>}
                         </div>

                              <div className="col-md-2">
                                <div className="input-group pull-right">
                                  <span className="input-group-addon" id="basic-addon1">
                                    <span className="glyphicon glyphicon-arrow-right" onClick={this.updateRecordsPerPage}></span>
                                  </span>
                                  <input type="text" ref="recordsPerPage" className="form-control" placeholder="" defaultValue={this.props.paging.recordsPerPage}/>
                                </div>
                              </div>


                              <div className="col-md-2">
                                <div className="input-group pull-right">
                                  <span className="input-group-addon" id="basic-addon1">
                                    <span className="glyphicon glyphicon-th-list"></span>
                                  </span>
                                  <input type="text" className="form-control" placeholder=""/>
                                </div>
                              </div>
                        </div>
                          </div>
                        </div>
                     </div>
        );
    }
}

function mapStateToProps(state, ownProps) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        paging: state.dataFreeze.paging,
        sorting: state.dataFreeze.sorting,
        errors: state.dataFreeze.errors || [],
        infoMessages: state.dataFreeze.infoMessages || [],
        settings: state.commonLists.settings || {},
        dataFreezeEventList: state.dataFreeze.dataFreezeEventList
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Object.assign({}, dataFreezeActions, commonListsActions), dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventList);