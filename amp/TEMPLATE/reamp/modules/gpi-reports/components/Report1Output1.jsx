import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import * as commonListsActions from '../actions/CommonListsActions';
import * as startUp from '../actions/StartUpAction';
import * as Constants from '../common/Constants';
import Utils from '../common/Utils';
import PagingSection from './PagingSection';
import YearsFilterSection from './YearsFilterSection';
import Report1Output1Row from './Report1Output1Row';
import RemarksPopup from './RemarksPopup';
import ToolBar from './ToolBar';
import { Modal } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
export default class Report1Output1 extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { recordsPerPage: 150, selectedYear: null, selectedDonor: "", remarksUrl:null, showRemarks: false};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );        
        this.onDonorFilterChange = this.onDonorFilterChange.bind( this );        
        this.showRemarksModal = this.showRemarksModal.bind(this);
        this.closeRemarksModal = this.closeRemarksModal.bind(this);
    }

    componentDidMount() {
        this.initializeFiltersAndSettings();
    }

    initializeFiltersAndSettings() {
        this.filter = Utils.initializeFilterWidget();
        this.settingsWidget = Utils.initializeSettingsWidget()
        this.props.actions.getYears()
        this.props.actions.getOrgList(false);
        this.props.actions.getSettings();
        this.fetchReportData();
        
    }

    showFilters() {
        Utils.showFilters(this.refs.filterPopup, this.filter, this.onFilterApply.bind(this), this.onFilterCancel.bind(this));
    }
    
    onFilterApply() {
        this.resetQuickFilters();
        this.fetchReportData();
        $( this.refs.filterPopup ).hide();
    }
    
    onFilterCancel() {
        $( this.refs.filterPopup ).hide(); 
    }

    showSettings() {
       Utils.showSettings(this.refs.settingsPopup, this.settingsWidget, this.onSettingsApply.bind(this), this.onSettingsCancel.bind(this));       
    }
    
    onSettingsCancel() {
        $( this.refs.settingsPopup ).hide();
    }

    onSettingsApply(){
        this.fetchReportData();
        $( this.refs.settingsPopup ).hide();
    }
    
    getRecordsPerPage(recordsPerPage) {               
        return (this.props.mainReport && this.props.mainReport.page) ? this.props.mainReport.page.recordsPerPage : this.state.recordsPerPage;           
    }
    
    getRequestData() {
        let requestData = {
            "page": 1,
            "recordsPerPage": this.getRecordsPerPage(),
            "output": 1
        };

        requestData.filters = this.filter.serialize().filters;        
        requestData.settings = this.settingsWidget.toAPIFormat();       
        if(this.state.selectedDonor){
            requestData.filters['donor-agency'] = requestData.filters['donor-agency'] || [];
            if (requestData.filters['donor-agency'].indexOf(this.state.selectedDonor) == -1) {
                requestData.filters['donor-agency'].push(this.state.selectedDonor); 
            }
       }   
        
       return requestData
    } 

    fetchReportData( data ) {
        let requestData = data || this.getRequestData();
        this.props.actions.fetchReportData( requestData, '1' );
    }
    
    onDonorFilterChange( e ) {
        this.setState( { selectedDonor: parseInt( e.target.value ) }, function() {
            let filters = this.filter.serialize().filters;
            filters['donor-agency'] = [];
            filters['donor-agency'].push( this.state.selectedDonor);
            this.filter.deserialize({filters: filters}, {silent : true});
            this.fetchReportData();
        }.bind( this ) );
    }

    onYearClick( selectedYear ) {
        this.setState( { selectedYear: selectedYear }, function() {  
            let requestData = this.getRequestData();
            requestData.filters['actual-approval-date'] = {};
            if (this.state.selectedYear) {
                requestData.filters['actual-approval-date']= {
                        'start': this.state.selectedYear + '-01-01',
                        'end': this.state.selectedYear + '-12-31'
                    };  
            } 
            this.fetchReportData(requestData);
        }.bind( this ) );

    }

    resetQuickFilters() {
        let filters = this.filter.serialize().filters;
        if (filters.date) {
            this.setState( { selectedYear: null });
        }

        if ( filters['donor-agency'] && filters['donor-agency'].length > 0  ) {
            this.setState( { selectedDonor: "" });
        }
    }

    getLocalizedColumnName( originalColumnName ) {
        let name = originalColumnName;
        if ( this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.headers ) {
            let header = this.props.mainReport.page.headers.filter( header => header.originalColumnName === originalColumnName )[0]
            if ( header ) {
                name = header.columnName;
            }
        }
        return name;
    }

    getYearCell( addedGroups, row ) {
        if ( addedGroups.indexOf( row[Constants.YEAR] ) === -1 ) {
            addedGroups.push( row[Constants.YEAR] );
            let matches = this.props.mainReport.page.contents.filter( content => content[Constants.YEAR] === row[Constants.YEAR] );
            return ( <td className="year-col" rowSpan={matches.length}>{row[Constants.YEAR]}</td> )
        }
    }  
    
    goToPage( pageNumber ) {
        let requestData = this.getRequestData();
        requestData.page = pageNumber;
        this.props.actions.fetchReportData( requestData, '1' );
    }
    
    updateRecordsPerPage(recordsPerPage) {
        let requestData = this.getRequestData();
        requestData.recordsPerPage = recordsPerPage;
        this.props.actions.fetchReportData(requestData, '1' );
    }
    
    closeRemarksModal() {
        this.setState({showRemarks: false, remarksUrl: null});
    }
    
    showRemarksModal(event) {       
        this.setState({showRemarks: true, remarksUrl: this.props.mainReport.summary[Constants.REMARK]});
    }
    
    getOrgName(id) {
        var org = this.props.orgList.filter(org => org.id === id)[0];
        return org ? org.name : '';
    }
       
    render() {
        if ( this.props.mainReport && this.props.mainReport.page ) {
            let addedGroups = [];
            let years = this.props.years.slice();
            return (
                <div>
                    <div id="filter-popup" ref="filterPopup"> </div>
                    <div id="amp-settings" ref="settingsPopup"> </div>
                    <ToolBar showFilters={this.showFilters} showSettings={this.showSettings}/>
                    <div className="section-divider"></div>
                    {this.props.mainReport && this.props.mainReport.summary &&                              
                            <div className="container-fluid indicator-stats no-padding">
                              <div className="col-md-3">
                                <div className="indicator-stat-wrapper">
                                  <div className="stat-value">{this.props.mainReport.summary[Constants.Q1]}</div>
                                  <div className="stat-label">{this.props.translations['amp.gpi-reports:indicator1-output1-summary-q1']} </div>
                                </div>
                              </div>
                              <div className="col-md-3">
                                <div className="indicator-stat-wrapper">
                                  <div className="stat-value">{this.props.mainReport.summary[Constants.Q2]}</div>
                                  <div className="stat-label">{this.props.translations['amp.gpi-reports:indicator1-output1-summary-q2']}</div>
                                </div>
                              </div>
                              <div className="col-md-3">
                                <div className="indicator-stat-wrapper">
                                  <div className="stat-value">{this.props.mainReport.summary[Constants.Q3]}</div>
                                  <div className="stat-label">{this.props.translations['amp.gpi-reports:indicator1-output1-summary-q3']}</div>
                                </div>
                              </div>
                              <div className="col-md-3">
                                <div className="indicator-stat-wrapper">
                                  <div className="stat-value">{this.props.mainReport.summary[Constants.Q4]}</div>
                                  <div className="stat-label">{this.props.translations['amp.gpi-reports:indicator1-output1-summary-q4']}</div>
                                </div>
                              </div>
                            </div>
                           
                    }
                    <YearsFilterSection onYearClick={this.onYearClick.bind(this)} years={this.props.years} selectedYear={this.state.selectedYear} mainReport={this.props.mainReport} filter={this.filter} dateField="actual-approval-date" />                    
                    <div className="container-fluid no-padding">
                        <div className="dropdown">
                            <select name="donorAgency" className="form-control donor-dropdown" value={this.state.selectedDonor} onChange={this.onDonorFilterChange}>
                                <option value="">{this.props.translations['amp.gpi-reports:all-donors']}</option>
                                {this.props.orgList.map( org =>
                                    <option value={org.id} key={org.id} >{org.name}</option>
                                )}
                            </select>
                        </div>
                        <div className="pull-right"><h4>{this.props.translations['amp.gpi-reports:currency']} {this.props.mainReport.settings['currency-code']}</h4></div>
                    </div>                                       
                    <div className="section-divider"></div>                               
                        
                        <div className="container-fluid">
                          <div className="row">
                            <h4>{this.props.translations['amp.gpi-reports:indicator1-description']}</h4>
                          </div>
                        </div>                    
                          {this.state.showRemarks &&
                                <RemarksPopup showRemarks={this.state.showRemarks} closeRemarksModal={this.closeRemarksModal.bind(this)} remarksUrl={this.state.remarksUrl} code="1" settings={this.props.settings} />                                                  
                          }  
                        <div className="section-divider"></div>
                        <span className="pull-left">{this.getOrgName(this.state.selectedDonor) || this.props.translations['amp.gpi-reports:all-donors']}</span><span className="remarks pull-left"><img className="table-icon popup-icon" src="images/icon-bubble.svg"/><a onClick={this.showRemarksModal} > Remarks</a></span>
                        <div className="spacer30"></div>                        
                        <table className="table table-bordered table-striped indicator-table complex-table">
                        <thead>
                        <tr>
                          <th className="col-md-2">{this.getLocalizedColumnName(Constants.PROJECT_TITLE)}</th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.ACTUAL_COMMITMENTS)}</th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.ACTUAL_APPROVAL_DATE)}</th>
                          <th className="col-md-3"><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.FINANCING_INSTRUMENT)}</th>
                          <th className="col-md-3"><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.IMPLEMENTING_AGENCY)}</th>
                          <th className="col-md-3"><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.PRIMARY_SECTOR)}</th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.Q6)}</th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>
                            <a data-container="body" data-toggle="popover" data-placement="top" data-content="Total number of outcome indicators included in the projects results framework" data-original-title="" title="">
                          {this.getLocalizedColumnName(Constants.Q7)}
                            </a>
                            </th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.Q8)}</th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.Q9)}</th>
                          <th><img className="table-icon" src="images/icon-information.svg"/>{this.getLocalizedColumnName(Constants.Q10)}</th>
                          <th><img className="table-icon" src="images/icon-value.svg"/> {this.getLocalizedColumnName(Constants.RESULT)}</th>
                          <th><img className="table-icon" src="images/icon-value.svg"/>{this.getLocalizedColumnName(Constants.M_E)}</th>
                          <th>
                              <img className="table-icon" src="images/icon-download.svg"/>
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                       {this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.contents.map(( row, i ) =>                          
                          <Report1Output1Row key={i} rowData={row} reportData={this.props.mainReport}/>                         
                      )}                      
                      </tbody>
                      </table>                             
                    <div>                 
                         <PagingSection mainReport={this.props.mainReport} goToPage={this.goToPage.bind(this)} updateRecordsPerPage={this.updateRecordsPerPage.bind(this)}/>
                    </div>
                </div>
            );
        }

        return ( <div></div> );
    }

}

function mapStateToProps( state, ownProps ) {
    return {
        mainReport: state.reports['1'].output1,
        orgList: state.commonLists.orgList,
        years: state.commonLists.years,
        settings: state.commonLists.settings,
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report1Output1 );
