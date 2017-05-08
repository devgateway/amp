import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import * as commonListsActions from '../actions/CommonListsActions';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/Constants';
export default class Report9b extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = {recordsPerPage: 150, hierarchy: 'donor-agency', selectedYear: null, selectedDonor: null};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );
        this.goToClickedPage = this.goToClickedPage.bind(this);
        this.goToNextPage = this.goToNextPage.bind(this);
        this.goToLastPage = this.goToLastPage.bind(this);
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind(this);
        this.onDonorFilterChange = this.onDonorFilterChange.bind(this);
        this.toggleHierarchy = this.toggleHierarchy.bind(this);
        this.onYearClick = this.onYearClick.bind(this);
    }

    componentWillMount() {
        this.filter = new ampFilter( {
            draggable: true,
            caller: 'REPORTS'
        });

        this.settingsWidget = new AMPSettings.SettingsWidget( {
            draggable: true,
            caller: 'REPORTS',
            isPopup: true,
            definitionUrl: '/rest/settings-definitions/reports'
        });
        
        this.props.actions.getOrgList(false);
        this.fetchReportData();
    }

    componentWillReceiveProps( nextProps ) {

    }

    showFilters() {
        this.filter.setElement( this.refs.filterPopup );
        this.filter.showFilters();
        $( this.refs.filterPopup ).show();

        this.filter.on( 'cancel', function() {
           $(this.refs.filterPopup ).hide();
        }.bind(this));

        this.filter.on( 'apply', function() {
            this.fetchReportData();
            $( this.refs.filterPopup ).hide();
        }.bind( this ) );
    }
    
    getRequestData() {
        var requestData = {                
                "hierarchy" : this.state.hierarchy,
                "page" : 1,
                "recordsPerPage" : this.state.recordsPerPage                  
            };
         
        requestData.filters = this.filter.serialize().filters;     
        
        if (this.state.selectedDonor) {
            requestData.filters['donor-group'] = [];
            requestData.filters['donor-agency'] = [];
            requestData.filters[this.state.hierarchy].push(this.state.selectedDonor);
        }
        
        if (this.state.selectedYear) {
            
        }
        
        requestData.settings = this.settingsWidget.toAPIFormat();
        return requestData
    }

    updateRecordsPerPage() {
        if(this.refs.recordsPerPage && this.refs.recordsPerPage.value){
            this.setState({recordsPerPage: parseInt(this.refs.recordsPerPage.value)}, function () {
                this.fetchReportData();
            }.bind(this));            
            
        }        
    }    
    
    fetchReportData(requestData) {
        var requestData = requestData || this.getRequestData();
        this.props.actions.fetchReport9bMainReport(requestData, '9b');
        var summaryRequestData = Object.assign({}, requestData);
        summaryRequestData.summary = true;
        this.props.actions.fetchReport9bSummary(summaryRequestData, '9b');
    }
    
    showSettings() {
        this.settingsWidget.setElement( this.refs.settingsPopup );
        this.settingsWidget.definitions.loaded.done( function() {
            this.settingsWidget.show();
        }.bind( this ) );

        $( this.refs.settingsPopup ).show();

        this.settingsWidget.on( 'close', function() {           
            $( this.refs.settingsPopup ).hide();
        }.bind( this ) );

        this.settingsWidget.on( 'applySettings', function() {
            this.fetchReportData();
            $( this.refs.settingsPopup ).hide();
        }.bind( this ) );
    }

    getLocalizedColumnName(originalColumnName) {   
        var name = originalColumnName;
        if(this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.headers){            
            var header = this.props.mainReport.page.headers.filter(header => header.originalColumnName === originalColumnName)[0]
             if (header){
                 name = header.columnName;
             }
        }
        return name;        
    }
     
    getYearCell(addedGroups, row) {
        if(addedGroups.indexOf(row[Constants.YEAR]) === -1){
            addedGroups.push(row[Constants.YEAR]);
            var matches = this.props.mainReport.page.contents.filter(content => content[Constants.YEAR] === row[Constants.YEAR]);
            return (<td className="year-col" rowSpan={matches.length}>{row[Constants.YEAR]}</td>)  
        }               
    }
    
    goToClickedPage(event){
        const pageNumber = parseInt(event.target.getAttribute('data-page'));
        this.goToPage(pageNumber);
    }
    
    goToNextPage(){
        const pageNumber = this.props.mainReport.page.currentPageNumber + 1;
        if(pageNumber <= this.props.mainReport.page.totalPageCount){
            this.goToPage(pageNumber);  
        }        
    }
    
    goToLastPage(){
        this.goToPage(this.props.mainReport.page.totalPageCount);
    }
    
    goToPage(pageNumber){
        var requestData = this.getRequestData();
        requestData.page = pageNumber;
        this.props.actions.fetchReport9bMainReport(requestData, '9b');
    }
    
    onDonorFilterChange(e) {       
        this.setState({selectedDonor:  parseInt(e.target.value)}, function(){                         
            this.fetchReportData();    
        }.bind(this));            
    }
    
    toggleHierarchy(event) {       
        this.setState({hierarchy: $(event.target).data("hierarchy")}, function () {
            this.props.actions.getOrgList((this.state.hierarchy === 'donor-group'));
            this.fetchReportData();
        }.bind(this));
    }
    
    getYears() {
        var yearRange = this.settingsWidget.definitions.findWhere({id: 'year-range'});
        var years = [];        
        if (yearRange) {            
            var rangeFrom = yearRange.get('value').from;
            var rangeTo = yearRange.get('value').to;
            for (var i = rangeFrom; i <= rangeTo; i++){
                years.push(i);
            }           
        }   
        return years;
    }
    
    generatePaginationLinks() {
        var paginationLinks  = [];
        for (var i = 1; i <= this.props.mainReport.page.totalPageCount; i++){            
            var classes = (i === this.props.mainReport.page.currentPageNumber) ? 'active page-item' : 'page-item';
            paginationLinks.push(<li className={classes} key={i}><a data-page={i} className="page-link" onClick={this.goToClickedPage}>{i}</a></li>);
        }
        return paginationLinks;
    }
    
    onYearClick(event) {
        this.setState({selectedYear:  $(event.target).data("year")}, function(){
            this.fetchReportData();    
        }.bind(this));
        
    }
    
    render() {              
        if (this.props.mainReport && this.props.mainReport.page) {
            var addedGroups = [];            
            var years = this.getYears();
            return (
                <div>
                    <div id="filter-popup" ref="filterPopup"> </div>
                    <div id="amp-settings" ref="settingsPopup"> </div>
                    <div className="container-fluid indicator-nav no-padding">
                        <div className="col-md-6 no-padding">
                        </div>
                        <div className="col-md-6 no-padding">
                            <ul className="export-nav">
                                <li>
                                    <a href="#"><img src="images/export-pdf.svg" /></a>
                                </li>
                                <li>
                                    <a href="#"><img src="images/export-excel.svg" /></a>
                                </li>
                                <li>
                                    <a href="#"><img src="images/export-print.svg" /></a>
                                </li>
                            </ul>
                            <div className="btn-action-nav">
                                <button type="button" className="btn btn-action" onClick={this.showFilters}>{this.props.translations['amp.gpi-reports:filter-button']}</button>
                                <button type="button" className="btn btn-action" onClick={this.showSettings}>{this.props.translations['amp.gpi-reports:settings-button']}</button>
                            </div>
                        </div>
                    </div>

                    <div className="section-divider"></div>
                   {this.props.summary && this.props.summary.page && this.props.summary.page.contents && this.props.summary.page.contents.length > 0 &&
                    <div className="container-fluid indicator-stats no-padding">
                        <div className="col-md-3">
                            <div className="indicator-stat-wrapper">
                                <div className="stat-value">{this.props.summary.page.contents[0][Constants.NATIONAL_BUDGET_EXECUTION_PROCEDURES]}</div>
                                <div className="stat-label">{this.getLocalizedColumnName(Constants.NATIONAL_BUDGET_EXECUTION_PROCEDURES)}</div>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <div className="indicator-stat-wrapper">
                                <div className="stat-value">{this.props.summary.page.contents[0][Constants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES]}</div>
                                <div className="stat-label">{this.getLocalizedColumnName(Constants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES)}</div>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <div className="indicator-stat-wrapper">
                                <div className="stat-value">{this.props.summary.page.contents[0][Constants.NATIONAL_AUDITING_PROCEDURES]}</div>
                                <div className="stat-label">{this.getLocalizedColumnName(Constants.NATIONAL_AUDITING_PROCEDURES)}</div>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <div className="indicator-stat-wrapper">
                                <div className="stat-value">{this.props.summary.page.contents[0][Constants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES]}</div>
                                <div className="stat-label">{this.getLocalizedColumnName(Constants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES)}</div>
                            </div>
                        </div>
                    </div>
                   }
                    <div className="container-fluid no-padding">
                        <ul className="year-nav">
                            <li className={this.state.selectedYear ? '' : 'active'}>
                                <a onClick={this.onYearClick}>{this.props.translations['amp.gpi-reports:all-years']}</a>
                            </li>
                             { ((years.length > 3) ? years.splice(years.length - 3, 3).reverse() : years.reverse()).map(year => 
                                  <li className={this.state.selectedYear == year ? 'active' : ''}><a data-year={year} onClick={this.onYearClick}>{year}</a></li>
                             )}
                            <li >
                                <div className="dropdown">
                                    <a className={years.includes(this.state.selectedYear) ? 'btn dropdown-toggle btn-years btn-years-active' : 'btn dropdown-toggle btn-years'} type="button" id="years" data-toggle="dropdown">
                                 {this.props.translations['amp.gpi-reports:other-years']}
                                <span className="caret"></span></a>
                                    <ul className="dropdown-menu dropdown-years" role="menu">
                                        { years.length > 3 && years.map(year => 
                                        <li role="presentation" className={this.state.selectedYear == year ? 'active' : ''}><a data-year={year} onClick={this.onYearClick}>{year}</a></li>
                                        )}
                                        
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div className="selection-legend">
                        <div className="pull-right">Selected - 2017 / 2016 / 2015 / 2014 / 2013 / 2012 / 2011 / 2010</div>
                    </div>
                    <div className="container-fluid no-padding">                        
                             <div className="dropdown">
                             <select name="donorAgency" className="form-control donor-dropdown" onChange={this.onDonorFilterChange}>
                             <option value="">{this.props.translations['amp.gpi-reports:all-donors']}</option>
                             {this.props.orgList.map(org => 
                             <option value={org.id} key={org.id} >{org.name}</option>
                             )}
                             </select>
                             </div>
                             <div className="pull-right"><h4>{this.props.translations['amp.gpi-reports:currency']} {this.props.mainReport.settings['currency-code']}</h4></div>

                    </div>
                    <div className="container-fluid">
                        <div className="row">
                            <h4>Indicator 9b - Chart title will go here</h4>
                        </div>
                    </div>
                    <div className="section-divider"></div>
                    <table className="table table-bordered table-striped indicator-table">
                        <thead>
                            <tr>
                                <th className="col-md-1">{this.getLocalizedColumnName(Constants.YEAR)}</th>
                                <th className="col-md-2">                           
                                <img src="images/blue_radio_on.png" className={this.state.hierarchy === 'donor-agency' ? 'donor-toggle' : 'donor-toggle donor-toggle-unselected'} onClick={this.toggleHierarchy} data-hierarchy="donor-agency"/><span className="donor-header-text" onClick={this.toggleHierarchy} data-hierarchy="donor-agency">{this.props.translations['amp.gpi-reports:donor-agency']}</span><br/>
                                <img src="images/blue_radio_on.png" className={this.state.hierarchy === 'donor-group' ? 'donor-toggle' : 'donor-toggle donor-toggle-unselected'} onClick={this.toggleHierarchy} data-hierarchy="donor-group"/><span className="donor-header-text" onClick={this.toggleHierarchy} data-hierarchy="donor-group">{this.props.translations['amp.gpi-reports:donor-group']}</span>
                                </th>                                
                                <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_BUDGET_EXECUTION_PROCEDURES)}</th>
                                <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES)}</th>
                                <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_AUDITING_PROCEDURES)}</th>
                                <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES)}</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.contents.map((row, i)=>                         
                            <tr key={i} >
                            {this.getYearCell(addedGroups, row)}
                            <td>{row[Constants.DONOR_AGENCY] ||row[Constants.DONOR_GROUP]  }</td>
                            <td className="number-column">{row[Constants.NATIONAL_BUDGET_EXECUTION_PROCEDURES]}</td>
                            <td className="number-column">{row[Constants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES]}</td>
                            <td className="number-column">{row[Constants.NATIONAL_AUDITING_PROCEDURES]}</td>
                            <td className="number-column">{row[Constants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES]}</td>
                             </tr>  
                            )}
                         
                        </tbody>
                    </table>
                            
                            <div >
                            <div className="row">
                           
                              <div className="col-md-8 pull-right pagination-wrapper">
                            {this.props.mainReport &&
                                <div className="col-md-4">
                                <ul className="pagination">
                                    {this.generatePaginationLinks()}
                                    <li className="next"><a onClick={this.goToNextPage}><span aria-hidden="true">&rarr;</span></a></li>
                                    <li className="page-item"><a onClick={this.goToLastPage} className="page-link">&raquo;</a></li>
                                </ul>
                                </div>
                            }
                                <div className="col-md-3">

                                </div>


                                <div className="col-md-2">
                                  <div className="input-group pull-right">
                                    <span className="input-group-addon" id="basic-addon1">
                                      <span className="glyphicon glyphicon-arrow-right" onClick={this.updateRecordsPerPage}></span>
                                    </span>
                                    <input type="text" className="form-control" ref="recordsPerPage" placeholder="" defaultValue={this.state.recordsPerPage} />
                                  </div>
                                </div>


                                <div className="col-md-3 pull-right record-number">
                                  <div>{((this.props.mainReport.page.currentPageNumber - 1) * this.props.mainReport.page.recordsPerPage) + 1} - {Math.min((this.props.mainReport.page.currentPageNumber * this.props.mainReport.page.recordsPerPage), this.props.mainReport.page.totalRecords)} of {this.props.mainReport.page.totalRecords} records</div>
                                  <div>(p {this.props.mainReport.page.currentPageNumber}/{this.props.mainReport.page.totalPageCount})</div>
                                </div>

                              </div>
                            </div>
                          </div>
                      
                            
                </div>
            );
          }
    
        return (<div></div>);
    }
    
}

function mapStateToProps( state, ownProps ) {
    return {
        mainReport: state.reports['9b'].mainReport,
        summary: state.reports['9b'].summary,
        orgList: state.commonLists.orgList,
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions, commonListsActions), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report9b );
