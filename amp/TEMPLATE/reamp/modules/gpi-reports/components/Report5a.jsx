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
import RemarksPopup from './RemarksPopup';
import ToolBar from './ToolBar';
import { Modal } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
import HeaderToolTip from './HeaderToolTip';
import Loading from './Loading';
export default class Report5a extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { recordsPerPage: 150, hierarchy: 'donor-agency', selectedYear: null, selectedDonor: "", remarksUrl:null, showRemarks: false, waiting:true};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );        
        this.onDonorFilterChange = this.onDonorFilterChange.bind( this );
        this.toggleHierarchy = this.toggleHierarchy.bind( this );  
        this.downloadExcelFile = this.downloadExcelFile.bind(this);
        this.downloadPdfFile = this.downloadPdfFile.bind(this);        
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
       this.setState( { calendarId: Utils.getCalendarId(this.settingsWidget) });
       Utils.showSettings(this.refs.settingsPopup, this.settingsWidget, this.onSettingsApply.bind(this), this.onSettingsCancel.bind(this));       
    }
    
    onSettingsCancel() {
        $( this.refs.settingsPopup ).hide();
    }

    onSettingsApply(){
        //if calendar has changed reset year filter
        if(Utils.hasCalendarChanged(this.settingsWidget, this.state.calendarId)) {
            this.onYearClick(null);            
        } else {
            this.fetchReportData(); 
        }  
        $( this.refs.settingsPopup ).hide();
    }
    
    getRecordsPerPage(recordsPerPage) {               
        return (this.props.mainReport && this.props.mainReport.page) ? this.props.mainReport.page.recordsPerPage : this.state.recordsPerPage;           
    }
    
    getRequestData() {
        let requestData = {
            "hierarchy": this.state.hierarchy,
            "page": 1,
            "recordsPerPage": this.getRecordsPerPage()
        };

        requestData.filters = this.filter.serialize().filters;        
        requestData.settings = this.settingsWidget.toAPIFormat();        
        if(this.state.hierarchy === 'donor-agency'){
            requestData.filters[this.state.hierarchy] = requestData.filters[this.state.hierarchy] || [];
            if (this.state.selectedDonor && requestData.filters[this.state.hierarchy].indexOf(this.state.selectedDonor) == -1) {
                requestData.filters[this.state.hierarchy].push(this.state.selectedDonor); 
            }
        }
        
        return requestData
    } 

    fetchReportData( data ) {
        let requestData = data || this.getRequestData();
        this.setState({waiting: true});
        this.props.actions.fetchReportData( requestData, '5a' ).then(function(){
            this.setState({waiting: false});  
        }.bind(this));
    }
    
    onDonorFilterChange( e ) {
        this.setState( { selectedDonor: parseInt( e.target.value ), showRemarks: false, remarksUrl: null }, function() {
            let filters = this.filter.serialize().filters;
            delete filters['donor-group'];
            delete filters['donor-agency'];
            filters[this.state.hierarchy] = [];
            filters[this.state.hierarchy].push( this.state.selectedDonor);
            this.filter.deserialize({filters: filters}, {silent : true});
            this.fetchReportData();
        }.bind( this ) );
    }

    onYearClick( selectedYear ) {
        this.setState( { selectedYear: selectedYear }, function() {                      
            let filters = this.filter.serialize().filters;
            filters.date = {};
            if (this.state.selectedYear) {
                filters.date = Utils.getStartEndDates(this.settingsWidget, this.props.calendars, this.state.selectedYear, this.props.years);
            }           
            this.filter.deserialize({filters: filters}, {silent : true});          
            this.fetchReportData();
        }.bind( this ) );

    }

    resetQuickFilters() {
        let filters = this.filter.serialize().filters;
        if (filters.date) {
            this.setState( { selectedYear: null });
        }

        if (( filters['donor-group'] && filters['donor-group'].length > 0 ) || ( filters['donor-agency'] && filters['donor-agency'].length > 0 ) ) {
            this.setState( { selectedDonor: "" });
        }
    }

    toggleHierarchy( event ) {
        this.setState( { hierarchy: $( event.target ).data( "hierarchy" ), selectedDonor: ''}, function() {
            this.props.actions.getOrgList(( this.state.hierarchy === 'donor-group' ) );
            let filters = this.filter.serialize().filters;
            delete filters['donor-group'];
            delete filters['donor-agency'];
            filters[this.state.hierarchy] = [];
            filters[this.state.hierarchy].push( this.state.selectedDonor);
            this.filter.deserialize({filters: filters}, {silent : true});          
            this.fetchReportData();                     
        }.bind( this ) );
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
        this.props.actions.fetchReportData( requestData, '5a' );
    }
    
    updateRecordsPerPage(recordsPerPage) {
        let requestData = this.getRequestData();
        requestData.recordsPerPage = recordsPerPage;
        this.props.actions.fetchReportData(requestData, '5a' );
    }
    
    closeRemarksModal() {
        this.setState({showRemarks: false, remarksUrl: null});
    }
    
    showRemarksModal(event) {       
        this.setState({showRemarks: true, remarksUrl: $(event.target).data("url")});
    }
    
    downloadExcelFile() {
        this.props.actions.downloadExcelFile(this.getRequestData(), '5a');
    }
    
    downloadPdfFile(){
        this.props.actions.downloadPdfFile(this.getRequestData(), '5a');
    }
    
    render() {        
            let addedGroups = [];           
            return (
                  <div>                   
                    {this.state.waiting &&                      
                        <Loading/>                
                    } 
                    
                    {this.props.mainReport && this.props.mainReport.page && this.settingsWidget && this.settingsWidget.definitions &&
                     <div>                    
                    <div id="filter-popup" ref="filterPopup"> </div>
                    <div id="amp-settings" ref="settingsPopup"> </div>
                    <ToolBar showFilters={this.showFilters} showSettings={this.showSettings} downloadPdfFile={this.downloadPdfFile}  downloadExcelFile={this.downloadExcelFile} />
                    <div className="section-divider"></div>
                    {this.props.mainReport && this.props.mainReport.summary && this.props.mainReport.empty == false  &&
                        <div className="container-fluid indicator-stats no-padding">
                        <div className="col-md-3 reduced-padding">
                          <div className="indicator-stat-wrapper">
                            <div className="stat-value">{this.props.mainReport.summary[Constants.DISBURSEMENTS_AS_SCHEDULED]}</div>
                            <div className="stat-label">{TranslationManager.getTranslated(Constants.DISBURSEMENTS_AS_SCHEDULED + ' ' + Constants.NATIONAL_LEVEL )}</div>
                          </div>
                        </div>
                        <div className="col-md-3 reduced-padding">
                          <div className="indicator-stat-wrapper">
                            <div className="stat-value">{this.props.mainReport.summary[Constants.OVER_DISBURSED]}</div>
                            <div className="stat-label">{TranslationManager.getTranslated(Constants.OVER_DISBURSED + ' ' + Constants.NATIONAL_LEVEL)}</div>
                          </div>
                        </div>
                        <div className="col-md-3 reduced-padding">
                        </div>
                        <div className="col-md-3 reduced-padding">
                        </div>
                      </div>                        
                    }
                    <YearsFilterSection onYearClick={this.onYearClick.bind(this)} selectedYear={this.state.selectedYear}
                                        mainReport={this.props.mainReport} filter={this.filter}
                                        dateField="date" settingsWidget={this.settingsWidget}
                                        prefix={Utils.getCalendarPrefix(this.settingsWidget,this.props.calendars,
                                            this.props.translate('amp.gpi-reports:fy'))}/>
                    <div className="container-fluid no-padding">
                        <div className="dropdown">
                            <select name="donorAgency" className="form-control donor-dropdown" value={this.state.selectedDonor} onChange={this.onDonorFilterChange}>
                                <option value="">{this.props.translations['amp.gpi-reports:all-donors']}</option>
                                {this.props.orgList.map( org =>
                                    <option value={org.id} key={org.id} >{org.name}</option>
                                )}
                            </select>
                        </div>
                        <div className="pull-right currency-label">{this.props.translations['amp.gpi-reports:currency']} {this.props.mainReport.settings['currency-code']}
                        {(this.props.settings['number-divider'] != 1) &&
                            <span className="amount-units"> ({this.props.translations['amp-gpi-reports:amount-in-' + this.props.settings['number-divider']]})</span>                    
                        }
                        </div>
                    </div>                                       
                        
                        <div className="container-fluid">
                        <div className="row">
                          <h4>{this.props.translations['amp.gpi-reports:indicator5a-description']}</h4>
                        </div>
                      </div>
                        <div className="section-divider"></div>
                        {this.state.showRemarks &&
                             <RemarksPopup showRemarks={this.state.showRemarks} closeRemarksModal={this.closeRemarksModal.bind(this)} remarksUrl={this.state.remarksUrl} code="5a" settings={this.props.settings} />                                                  
                        }  
                        {this.props.mainReport.empty == true  &&
                            <div className="text-center">{this.props.translations['amp-gpi-reports:no-data']}</div>
                        }
                        { this.props.mainReport.empty == false  &&
                        <table className="table table-bordered table-striped indicator-table">
                        <thead>
                        <tr>
                          <th className="col-md-1">{this.getLocalizedColumnName(Constants.YEAR)}</th>
                          <th className="col-md-3">
                          <img src="images/blue_radio_on.png" className={this.state.hierarchy === 'donor-agency' ? 'donor-toggle' : 'donor-toggle donor-toggle-unselected'} onClick={this.toggleHierarchy} data-hierarchy="donor-agency" /><span className="donor-header-text" onClick={this.toggleHierarchy} data-hierarchy="donor-agency">{this.props.translations['amp.gpi-reports:donor-agency']}</span><br />
                          <img src="images/blue_radio_on.png" className={this.state.hierarchy === 'donor-group' ? 'donor-toggle' : 'donor-toggle donor-toggle-unselected'} onClick={this.toggleHierarchy} data-hierarchy="donor-group" /><span className="donor-header-text" onClick={this.toggleHierarchy} data-hierarchy="donor-group">{this.props.translations['amp.gpi-reports:donor-group']}</span>
                          </th>
                          <th>{this.getLocalizedColumnName(Constants.TOTAL_ACTUAL_DISBURSEMENTS)}<span className="light-weight"></span></th>
                          <th className="col-md-2"><HeaderToolTip column={Constants.CONCESSIONAL} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.CONCESSIONAL)}?</th>
                          <th>{this.getLocalizedColumnName(Constants.ACTUAL_DISBURSEMENTS)} <span className="light-weight"></span></th>
                          <th>{this.getLocalizedColumnName(Constants.PLANNED_DISBURSEMENTS)}<span className="light-weight"></span></th>
                          <th className="col-md-2"><HeaderToolTip column={Constants.DISBURSEMENTS_AS_SCHEDULED} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.DISBURSEMENTS_AS_SCHEDULED)}</th>
                          <th className="col-md-2"><HeaderToolTip column={Constants.OVER_DISBURSED} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.OVER_DISBURSED)}</th>
                          <th>
                            <div className="popup">
                             <HeaderToolTip column={Constants.REMARK} headers={this.props.mainReport.page.headers} imgSrc="images/remarks-heading-icon.svg" tooltip={this.props.translations['amp-gpi-reports:remarks']}/>               
                            </div>
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                          {this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.contents.map(( row, i ) =>
                          <tr key={i} >
                              {this.getYearCell( addedGroups, row )}
                              <td>{row[Constants.DONOR_AGENCY] || row[Constants.DONOR_GROUP]}</td>
                              <td className="number-column">{row[Constants.TOTAL_ACTUAL_DISBURSEMENTS]}</td>
                              <td className="number-column">{row[Constants.CONCESSIONAL]}</td>
                              <td className="number-column">{row[Constants.ACTUAL_DISBURSEMENTS]}</td>
                              <td className="number-column">{row[Constants.PLANNED_DISBURSEMENTS]}</td>
                              <td className="number-column">{row[Constants.DISBURSEMENTS_AS_SCHEDULED]}</td>
                              <td className="number-column">{row[Constants.OVER_DISBURSED]}</td>
                              <td className="number-column">
                              { parseInt(row[Constants.NUMBER_OF_REMARKS]) > 0 &&
                                  <img className="table-icon" src="images/remarks-icon.svg" data-url={row[Constants.REMARK]} onClick={this.showRemarksModal.bind(this)}/> 
                              }
                              </td>
                          </tr>
                      )}                      
                      </tbody>
                      </table> 
                     }
                    <div>                 
                         <PagingSection mainReport={this.props.mainReport} goToPage={this.goToPage.bind(this)} updateRecordsPerPage={this.updateRecordsPerPage.bind(this)}/>
                    </div>
                </div>
                }
               </div>
                          
            );            
    }

}

function mapStateToProps( state, ownProps ) {
    return {
        mainReport: state.reports['5a'].mainReport,
        orgList: state.commonLists.orgList,
        years: state.commonLists.years,
        settings: state.commonLists.settings,
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        calendars: state.commonLists.calendars
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report5a );
