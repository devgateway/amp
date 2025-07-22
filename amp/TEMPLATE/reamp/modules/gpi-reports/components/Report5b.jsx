import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import Utils from '../common/Utils';
import * as reportsActions from '../actions/ReportsActions';
import * as commonListsActions from '../actions/CommonListsActions';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/Constants';
import Loading from './Loading';
import { IMG_VALUE, INDICATOR_5B, INDICATOR_5B_CODE, GREG_BASE_CALENDAR} from '../common/Constants';
import HeaderToolTip from './HeaderToolTip';
import YearsFilterSection from './YearsFilterSection';
class Report5b extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { recordsPerPage: 150, hierarchy: 'donor-agency', selectedDonor: "", waiting: true};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );
        this.goToClickedPage = this.goToClickedPage.bind( this );
        this.goToNextPage = this.goToNextPage.bind( this );
        this.goToLastPage = this.goToLastPage.bind( this );
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind( this );
        this.onDonorFilterChange = this.onDonorFilterChange.bind( this );
        this.toggleHierarchy = this.toggleHierarchy.bind( this );
        this.onYearClick = this.onYearClick.bind( this );
        this.downloadExcelFile = this.downloadExcelFile.bind(this);
        this.downloadPdfFile = this.downloadPdfFile.bind(this);
    }

    componentDidMount() {
        this.initializeFiltersAndSettings();
    }

    initializeFiltersAndSettings() {
        this.filter = new ampFilter({
             draggable: true,
             caller: 'GPI_REPORTS'
        });
        this.settingsWidget = Utils.initializeSettingsWidget();
        this.props.actions.getYears();
        this.props.actions.getOrgList(false);
        this.props.actions.getSettings();
        this.settingsWidget.definitions.loaded.done( function() {
            this.fetchReportData();
        }.bind(this));

   }

   showFilters() {
        this.filter.setElement(this.refs.filterPopup);
        this.filter.showFilters();
        $(this.refs.filterPopup).show();

        this.filter.on('cancel', function () {
             $(this.refs.filterPopup).hide();
        }.bind(this));

        this.filter.on('apply', function () {
             this.resetQuickFilters();
             this.fetchReportData();
             $(this.refs.filterPopup).hide();
        }.bind(this));
   }


   showSettings() {
       this.setState( { calendarId: Utils.getCalendarId(this.settingsWidget) });
       this.settingsWidget.setElement(this.refs.settingsPopup);
        this.settingsWidget.definitions.loaded.done(function () {
             this.settingsWidget.show();
        }.bind(this));

        $(this.refs.settingsPopup).show();
        this.settingsWidget.on('close', function () {
             $(this.refs.settingsPopup).hide();
        }.bind(this));

        this.settingsWidget.on('applySettings', function () {
            //if calendar has changed reset year filter
            if (Utils.hasCalendarChanged(this.settingsWidget,this.state.calendarId)) {
                this.onYearClick(null);
            }else{
                this.fetchReportData();
            }
            $(this.refs.settingsPopup).hide();
        }.bind(this));
   }

    getSelectedYear()
    {
        let selectedYear = undefined;
        if (this.state.selectedYear) { //if selected year is null we need to
            // get the current year for the current Calendar
            selectedYear = this.state.selectedYear;
        }
        else {
            const settings = this.settingsWidget.toAPIFormat();
            const calendarId = settings && settings['calendar-id'] ? settings['calendar-id'] :
                this.settingsWidget.definitions.getDefaultCalendarId();
            const calendarYears = Utils.getYearByCalendarId (this.props.years ,calendarId);
            selectedYear = calendarYears.years[calendarYears.years.length - 1].year
        }
        return selectedYear;
    }
    getRequestData() {
        var requestData = {
            "hierarchy": this.state.hierarchy,
            "page": 1,
            "recordsPerPage": this.state.recordsPerPage
        };

        requestData.filters = this.filter.serialize().filters;
        requestData.settings = this.settingsWidget.toAPIFormat();
        if ( this.getSelectedYear() ) {
            requestData.filters.date = this.getStartEndDates();
        }

        if(this.state.hierarchy === 'donor-agency'){
            requestData.filters[this.state.hierarchy] = requestData.filters[this.state.hierarchy] || [];
            if (this.state.selectedDonor && requestData.filters[this.state.hierarchy].indexOf(this.state.selectedDonor) == -1) {
                requestData.filters[this.state.hierarchy].push(this.state.selectedDonor);
            }
        }

        if(requestData.filters['donor-agency'] && requestData.filters['donor-agency'].length == 0){
            delete requestData.filters['donor-agency']
        }

        if(requestData.filters['donor-group'] && requestData.filters['donor-group'].length == 0){
            delete requestData.filters['donor-group']
        }

        return requestData
    }

    getStartEndDates() {
        return Utils.getStartEndDates(this.settingsWidget, this.props.calendars, this.getSelectedYear(), this.props.years);
    }

   updateRecordsPerPage() {
        if ( this.refs.recordsPerPage && this.refs.recordsPerPage.value ) {
            this.setState( { recordsPerPage: parseInt( this.refs.recordsPerPage.value ) }, function() {
                this.fetchReportData();
            }.bind( this ) );

        }
    }

    fetchReportData( requestData ) {
        var requestData = requestData || this.getRequestData();
        this.setState({waiting:true});
        this.props.actions.fetchReportData( requestData, '5b' ).then(function(){
            this.setState({waiting: false});
        }.bind(this));
    }

    onDonorFilterChange( e ) {
        this.setState( { selectedDonor: parseInt( e.target.value ) }, function() {
            var filters = this.filter.serialize().filters;
            filters['donor-group'] = [];
            filters['donor-agency'] = [];
            filters[this.state.hierarchy].push( this.state.selectedDonor);
            this.filter.deserialize({filters: filters});
            this.fetchReportData();
        }.bind( this ) );
    }

   onYearClick( selectedYear ) {
        this.setState( { selectedYear: selectedYear }, function() {
            let filters = this.filter.serialize().filters;
            filters.date = {};
            if (this.state.selectedYear) {
                filters.date = this.getStartEndDates();
            }
            this.filter.deserialize({filters: filters}, {silent : true});
            this.fetchReportData();
        }.bind( this ) );

    }

    resetQuickFilters() {
        var filters = this.filter.serialize().filters;
        if ( filters.date ) {
            this.setState( { selectedYear: null });
        }

        if ( ( filters['donor-group'] && filters['donor-group'].length > 0 ) || ( filters['donor-agency'] && filters['donor-agency'].length > 0 ) ) {
            this.setState( { selectedDonor: "" });
        }
    }

    toggleHierarchy( event ) {
        this.setState( { hierarchy: $( event.target ).data( "hierarchy" ), selectedDonor: ''}, function() {
            this.props.actions.getOrgList(( this.state.hierarchy === 'donor-group' ) );
            var filters = this.filter.serialize().filters;
            filters['donor-group'] = [];
            filters['donor-agency'] = [];
            filters[this.state.hierarchy].push( this.state.selectedDonor);
            this.filter.deserialize({filters: filters}, {silent : true});
            this.fetchReportData();
        }.bind( this ) );
    }


    goToClickedPage( event ) {
        const pageNumber = parseInt( event.target.getAttribute( 'data-page' ) );
        this.goToPage( pageNumber );
    }

    goToNextPage() {
        const pageNumber = this.props.mainReport.page.currentPageNumber + 1;
        if ( pageNumber <= this.props.mainReport.page.totalPageCount ) {
            this.goToPage( pageNumber );
        }
    }

    goToLastPage() {
        this.goToPage( this.props.mainReport.page.totalPageCount );
    }

    goToPage( pageNumber ) {
        var requestData = this.getRequestData();
        requestData.page = pageNumber;
        this.props.actions.fetchReportData( requestData, '5b' );
    }

    generatePaginationLinks() {
        var paginationLinks = [];
        for ( var i = 1; i <= this.props.mainReport.page.totalPageCount; i++ ) {
            var classes = ( i === this.props.mainReport.page.currentPageNumber ) ? 'active page-item' : 'page-item';
            paginationLinks.push( <li className={classes} key={i}><a data-page={i} className="page-link" onClick={this.goToClickedPage}>{i}</a></li> );
        }
        return paginationLinks;
    }

    getLocalizedColumnName( originalColumnName ) {
        var name = originalColumnName;
        if ( this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.headers ) {
            var header = this.props.mainReport.page.headers.filter( header => header.originalColumnName === originalColumnName )[0]
            if ( header ) {
                name = header.columnName;
            }
        }
        return name;
    }

   displayPagingInfo() {
        var transParams = {};
        transParams.fromRecord = ( ( this.props.mainReport.page.currentPageNumber - 1 ) * this.props.mainReport.page.recordsPerPage ) + 1;
        transParams.toRecord = Math.min(( this.props.mainReport.page.currentPageNumber * this.props.mainReport.page.recordsPerPage ), this.props.mainReport.page.totalRecords );
        transParams.totalRecords = this.props.mainReport.page.totalRecords;
        transParams.currentPageNumber = this.props.mainReport.page.currentPageNumber;
        transParams.totalPageCount = this.props.mainReport.page.totalPageCount;
        return ( <div className="col-md-3 pull-right record-number">
            <div>{this.props.translate( 'amp.gpi-reports:records-displayed', transParams )}</div>
            <div>{this.props.translate( 'amp.gpi-reports:page-info', transParams )}</div>
        </div> )
    }

    getMTEFYears() {
        var years = [];
        if (this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.headers) {
            for (var header of this.props.mainReport.page.headers) {
                var columnName = parseInt(header.columnName);
                if(isNaN(columnName) == false) {
                    years.push(columnName);
                }
              }
        }

        return years.sort();
    }

    downloadExcelFile() {
        this.props.actions.downloadExcelFile(this.getRequestData(), '5b');
    }

    downloadPdfFile(){
        this.props.actions.downloadPdfFile(this.getRequestData(), '5b');
    }

    render() {

        var MTEFYears =  this.getMTEFYears();
        var addedGroups = [];
        return (
                <div>
                    {this.state.waiting &&
                        <Loading/>
                    }
                    {this.props.mainReport && this.props.mainReport.page && this.settingsWidget && this.settingsWidget.definitions &&
                     <div>
                    <div id="filter-popup" ref="filterPopup"> </div>
                    <div id="amp-settings" ref="settingsPopup"> </div>
                    <div className="container-fluid indicator-nav no-padding">
                        <div className="col-md-5b no-padding">
                        </div>
                        <div className="col-md-5b no-padding">
                            <ul className="export-nav">
                                <li>
                                     <a onClick={this.downloadPdfFile}><img src="images/export-pdf.svg" /></a>
                                </li>
                                <li>
                                     <a onClick={this.downloadExcelFile}><img src="images/export-excel.svg" /></a>
                                </li>
                            </ul>
                            <div className="btn-action-nav">
                                <button type="button" className="btn btn-action" onClick={this.showFilters}>{this.props.translations['amp.gpi-reports:filter-button']}</button>
                                <button type="button" className="btn btn-action" onClick={this.showSettings}>{this.props.translations['amp.gpi-reports:settings-button']}</button>
                            </div>
                        </div>
                    </div>

                    <div className="section-divider"></div>
                    {this.props.mainReport && this.props.mainReport.summary && this.props.mainReport.empty == false  &&
                        <div className="container-fluid indicator-stats no-padding">
                            <div className="col-md-3">
                                <div className="indicator-stat-wrapper">
                                    <div className="stat-value">{this.props.mainReport.summary[Constants.INDICATOR_5B]}</div>
                                    <div className="stat-label">{this.props.translations['amp-gpi-reports:5b-stat-label']}</div>
                                </div>
                            </div>
                        </div>
                    }
                    {this.props.mainReport.empty == true  &&
                        <div className="text-center">{this.props.translations['amp-gpi-reports:no-data']}</div>
                    }
                    <YearsFilterSection onYearClick={this.onYearClick.bind(this)}
                                        selectedYear={this.state.selectedYear} mainReport={this.props.mainReport}
                                        filter={this.filter} dateField="date" settingsWidget={this.settingsWidget}
                                        report={INDICATOR_5B_CODE} prefix={Utils.getCalendarPrefix(this.settingsWidget,this.props.calendars)}/>
                    <div className="container-fluid no-padding">
                        <div className="dropdown">
                            <select name="donorAgency" className="form-control donor-dropdown" value={this.state.selectedDonor} onChange={this.onDonorFilterChange}>
                                <option value="">{this.props.translations['amp.gpi-reports:all-donors']}</option>
                                {this.props.orgList.map( org =>
                                    <option value={org.id} key={org.id} >{org.name}</option>
                                )}
                            </select>
                        </div>
                    </div>
                        <div className="container-fluid">
                        <div className="row">
                          <h4>{this.props.translations['amp.gpi-reports:indicator5b-description']}</h4>
                        </div>
                      </div>
                      <div className="section-divider"></div>
                      { this.props.mainReport.empty &&
                          <div className="text-center">{this.props.translations['amp-gpi-reports:no-data']}</div>
                      }
                      { this.props.mainReport.empty == false  &&
                            <table className="table table-bordered table-striped indicator-table">
                            <thead>
                            <tr>
                              <th className="col-md-4">
                              <img src="images/blue_radio_on.png" className={this.state.hierarchy === 'donor-agency' ? 'donor-toggle' : 'donor-toggle donor-toggle-unselected'} onClick={this.toggleHierarchy} data-hierarchy="donor-agency" /><span className="donor-header-text" onClick={this.toggleHierarchy} data-hierarchy="donor-agency">{this.props.translations['amp.gpi-reports:donor-agency']}</span><br />                                                          <img src="images/blue_radio_on.png" className={this.state.hierarchy === 'donor-group' ? 'donor-toggle' : 'donor-toggle donor-toggle-unselected'} onClick={this.toggleHierarchy} data-hierarchy="donor-group" /><span className="donor-header-text" onClick={this.toggleHierarchy} data-hierarchy="donor-group">{this.props.translations['amp.gpi-reports:donor-group']}</span>
                              </th>
                              {MTEFYears.map(( year, i ) =>
                                <th className="col-md-2" key = {i}>
                                <HeaderToolTip column={year} headers={this.props.mainReport.page.headers}/>{
                                    Utils.getCalendarPrefix(this.settingsWidget,this.props.calendars, this.props.translate('amp.gpi-reports:fy')) +
                                    year} <br/> {this.props.translations['amp-gpi-reports:5b-column-legend']}
                                </th>
                              )}
                              <th className="col-md-2">
                              <HeaderToolTip column={INDICATOR_5B} imgType={IMG_VALUE} headers={this.props.mainReport.page.headers}/>
                              {this.props.translations['amp-gpi-reports:indicator-5b']}</th>
                            </tr>
                          </thead>
                          <tbody>
                              {this.props.mainReport && this.props.mainReport.page && this.props.mainReport.page.contents.map(( row, i ) =>
                              <tr key={i} >
                                  <td>{row[Constants.DONOR_AGENCY] || row[Constants.DONOR_GROUP]}</td>
                                  {MTEFYears.map(( year, i ) =>
                                  <td className="number-column">{row[year]}</td>
                                  )}
                                  <td className="number-column">{row[Constants.INDICATOR_5B]}</td>
                              </tr>
                          )}
                          </tbody>
                          </table>
                        }


                      <div>
                        { this.props.mainReport.empty == false  && this.props.mainReport.page.totalPageCount > 1 &&
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
                                {this.displayPagingInfo()}
                            </div>
                        </div>
                      }

                    </div>

                 </div>
                 }
                </div>
            );

    }

}

function mapStateToProps( state, ownProps ) {
    return {
        mainReport: state.reports['5b'].mainReport,
        orgList: state.commonLists.orgList,
        years: state.commonLists.years,
        translations: state.startUp.translations,
        settings: state.commonLists.settings,
        translate: state.startUp.translate,
        calendars: state.commonLists.calendars
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report5b );
