import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import * as commonListsActions from '../actions/CommonListsActions';
import * as Constants from '../common/Constants';
import Utils from '../common/Utils';
import PagingSection from './PagingSection';
import YearsFilterSection from './YearsFilterSection';
import Report1Output1Row from './Report1Output1Row';
import RemarksPopup from './RemarksPopup';
import ToolBar from './ToolBar';
import HeaderToolTip from './HeaderToolTip';
import Loading from './Loading';
class Report1Output1 extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { recordsPerPage: 150, selectedYear: null, selectedDonor: "", remarksUrl:null, showRemarks: false, waiting:true};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );
        this.onDonorFilterChange = this.onDonorFilterChange.bind( this );
        this.showRemarksModal = this.showRemarksModal.bind(this);
        this.closeRemarksModal = this.closeRemarksModal.bind(this);
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
        const settings  = this.settingsWidget.toAPIFormat();
        const calendarId = settings && settings['calendar-id'] ?  settings['calendar-id'] : this.settingsWidget.definitions.getDefaultCalendarId();
        this.setState( { calendarId: calendarId });
        Utils.showSettings(this.refs.settingsPopup, this.settingsWidget, this.onSettingsApply.bind(this), this.onSettingsCancel.bind(this));
    }

    onSettingsCancel() {
        $( this.refs.settingsPopup ).hide();
    }

    onSettingsApply(){
        const settings  = this.settingsWidget.toAPIFormat();
        const currentCalendarId = settings && settings['calendar-id'] ?  settings['calendar-id'] : this.settingsWidget.definitions.getDefaultCalendarId();
        //if calendar has changed reset year filter
        if (currentCalendarId !== this.state.calendarId) {
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
        this.setState({waiting:true});
        this.props.actions.fetchReportData( requestData, '1' ).then(function(){
            this.setState({waiting: false});
        }.bind(this));
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
            const filters = this.filter.serialize().filters;
            filters['actual-approval-date'] = {};
            if (this.state.selectedYear) {
                filters['actual-approval-date'] = Utils.getStartEndDates(this.settingsWidget, this.props.calendars, this.state.selectedYear, this.props.years);
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

    downloadExcelFile() {
        this.props.actions.downloadExcelFile(this.getRequestData(), '1');
    }

    downloadPdfFile(){
        this.props.actions.downloadPdfFile(this.getRequestData(), '1');
    }

    render() {
        let addedGroups = [];
        var years = Utils.getYears(this.settingsWidget, this.props.years);
        return (
            <div>
                {this.state.waiting &&
                <Loading/>
                }
                {this.props.mainReport && this.props.mainReport.page && this.settingsWidget && this.settingsWidget.definitions &&
                <div>
                    <div id="filter-popup" ref="filterPopup"> </div>
                    <div id="amp-settings" ref="settingsPopup"> </div>
                    <ToolBar showFilters={this.showFilters} showSettings={this.showSettings}  downloadPdfFile={this.downloadPdfFile}  downloadExcelFile={this.downloadExcelFile}/>
                    <div className="section-divider"></div>
                    {this.props.mainReport && this.props.mainReport.summary && this.props.mainReport.empty == false  &&
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
                    <YearsFilterSection onYearClick={this.onYearClick.bind(this)} selectedYear={this.state.selectedYear}
                                        mainReport={this.props.mainReport} filter={this.filter}
                                        dateField="actual-approval-date" settingsWidget={this.settingsWidget}
                                        prefix={Utils.getCalendarPrefix(this.settingsWidget,this.props.calendars,
                                            this.props.translate('amp.gpi-reports:fy'))} />
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
                            <h4>{this.props.translations['amp.gpi-reports:indicator1-description']}</h4>
                        </div>
                    </div>
                    {this.state.showRemarks &&
                    <RemarksPopup showRemarks={this.state.showRemarks} closeRemarksModal={this.closeRemarksModal.bind(this)} remarksUrl={this.state.remarksUrl} code="1" settings={this.props.settings} />
                    }
                    <div className="section-divider"></div>
                    {this.props.mainReport.empty == false &&
                    <div>
                        <span className="pull-left">{this.getOrgName(this.state.selectedDonor) || this.props.translations['amp.gpi-reports:all-donors']}</span>
                        <span className="remarks pull-left"><img className="table-icon popup-icon" src="images/icon-bubble.svg" onClick={this.showRemarksModal}/><a onClick={this.showRemarksModal} > Remarks</a></span>
                    </div>
                    }
                    <div className="spacer30"></div>
                    {this.props.mainReport.empty == true  &&
                    <div className="text-center">{this.props.translations['amp-gpi-reports:no-data']}</div>
                    }
                    { this.props.mainReport.empty == false  &&
                    <table className="table indicator1-table table-bordered table-striped indicator-table complex-table">
                        <thead>
                        <tr>
                            <th className="col-md-2">{this.getLocalizedColumnName(Constants.PROJECT_TITLE)}</th>
                            <th><HeaderToolTip column={Constants.Q1} headers={this.props.mainReport.page.headers}/>
                                {this.getLocalizedColumnName(Constants.ACTUAL_COMMITMENTS)}</th>
                            <th><HeaderToolTip column={Constants.Q2} headers={this.props.mainReport.page.headers}/>
                                {this.getLocalizedColumnName(Constants.ACTUAL_APPROVAL_DATE)}</th>
                            <th className="col-md-3"><HeaderToolTip column={Constants.Q3} headers={this.props.mainReport.page.headers}/>
                                {this.getLocalizedColumnName(Constants.FINANCING_INSTRUMENT)}</th>
                            <th className="col-md-3"><HeaderToolTip column={Constants.Q4} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.IMPLEMENTING_AGENCY)}</th>
                            <th className="col-md-3"><HeaderToolTip column={Constants.Q5} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.PRIMARY_SECTOR)}</th>
                            <th><HeaderToolTip column={Constants.Q6} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.Q6)}</th>
                            <th><HeaderToolTip column={Constants.Q7} headers={this.props.mainReport.page.headers}/>
                                <a data-container="body" data-toggle="popover" data-placement="top" data-content="Total number of outcome indicators included in the projects results framework" data-original-title="" title="">
                                    {this.getLocalizedColumnName(Constants.Q7)}
                                </a>
                            </th>
                            <th><HeaderToolTip column={Constants.Q8} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.Q8)}</th>
                            <th><HeaderToolTip column={Constants.Q9} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.Q9)}</th>
                            <th><HeaderToolTip column={Constants.Q10} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.Q10)}</th>
                            <th><HeaderToolTip column={Constants.RESULT} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.RESULT)}</th>
                            <th><HeaderToolTip column={Constants.M_E} headers={this.props.mainReport.page.headers}/>{this.getLocalizedColumnName(Constants.M_E)}</th>
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
        mainReport: state.reports['1'].output1,
        orgList: state.commonLists.orgList,
        years: state.commonLists.years,
        calendars: state.commonLists.calendars,
        settings: state.commonLists.settings,
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report1Output1 );
