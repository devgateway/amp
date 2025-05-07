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
import Loading from './Loading';
class Report1Output2 extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { recordsPerPage: 150, selectedYear: null, selectedDonor: "", waiting:true};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );
        this.onDonorFilterChange = this.onDonorFilterChange.bind( this );
        this.downloadExcelFile = this.downloadExcelFile.bind(this);
        this.downloadPdfFile = this.downloadPdfFile.bind(this);
      }

    componentDidMount() {
        this.initializeFiltersAndSettings();
    }

    initializeFiltersAndSettings() {
        this.filter = Utils.initializeFilterWidget();
        this.settingsWidget = Utils.initializeSettingsWidget();
        this.props.actions.getYears();
        this.props.actions.getOrgList(false);
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
        return (this.props.output2 && this.props.output2.page) ? this.props.output2.page.recordsPerPage : this.state.recordsPerPage;
    }

    getRequestData() {
        let requestData = {
             "page": 1,
             "recordsPerPage": this.getRecordsPerPage(),
             "output": 2
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
        this.props.actions.fetchReportData(requestData, '1').then(function(){
            this.setState({waiting: false});
        }.bind(this));
    }

    onDonorFilterChange( e ) {
        this.setState( { selectedDonor: parseInt( e.target.value ) }, function() {
            let filters = this.filter.serialize().filters;
            filters['donor-agency'] = []
            filters['donor-agency'].push( this.state.selectedDonor);
            this.filter.deserialize({filters: filters}, {silent : true});
            this.fetchReportData();
        }.bind( this ) );
    }

    onYearClick( selectedYear ) {
        this.setState( { selectedYear: selectedYear}, function() {
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
        if (filters['actual-approval-date']) {
            this.setState( { selectedYear: null });
        }

        if (( filters['donor-group'] && filters['donor-group'].length > 0 ) || ( filters['donor-agency'] && filters['donor-agency'].length > 0 ) ) {
            this.setState( { selectedDonor: "" });
        }
    }

    getLocalizedColumnName( originalColumnName ) {
        let name = originalColumnName;
        if ( this.props.output2 && this.props.output2.page && this.props.output2.page.headers ) {
            let header = this.props.output2.page.headers.filter( header => header.originalColumnName === originalColumnName )[0]
            if ( header ) {
                name = header.columnName;
            }
        }
        return name;
    }

    getYearCell( addedGroups, row ) {
        if ( addedGroups.indexOf( row[Constants.YEAR] ) === -1 ) {
            addedGroups.push( row[Constants.YEAR] );
            let matches = this.props.output2.page.contents.filter( content => content[Constants.YEAR] === row[Constants.YEAR] );
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
        this.props.actions.fetchReportData( requestData, '1' );
    }

   createRows() {
       var rows = [];
       this.props.output2 && this.props.output2.page && this.props.output2.page.contents.forEach(( dataRow, i ) => {
          rows.push(<tr><td rowSpan="4">{dataRow[Constants.YEAR]}</td><td>{this.props.translations['amp.gpi-reports:indicator1-q1']}</td><td className='number-column'>{dataRow[Constants.Q1]}</td></tr>);
          rows.push(<tr><td>{this.props.translations['amp.gpi-reports:indicator1-q2']}</td><td className='number-column'>{dataRow[Constants.Q2]}</td></tr>);
          rows.push(<tr><td>{this.props.translations['amp.gpi-reports:indicator1-q3']}</td><td className='number-column'>{dataRow[Constants.Q3]}</td></tr>);
          rows.push(<tr><td>{this.props.translations['amp.gpi-reports:indicator1-q4']}</td><td className='number-column'>{dataRow[Constants.Q4]}</td></tr>);
       })
       return rows;
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
                    {this.props.output2 && this.props.output2.page && this.settingsWidget && this.settingsWidget.definitions &&
                    <div>
                    <div id="filter-popup" ref="filterPopup"> </div>
                    <div id="amp-settings" ref="settingsPopup"> </div>
                    <ToolBar showFilters={this.showFilters} showSettings={this.showSettings}  downloadPdfFile={this.downloadPdfFile}  downloadExcelFile={this.downloadExcelFile}/>
                    <div className="section-divider"></div>
                    <YearsFilterSection onYearClick={this.onYearClick.bind(this)} selectedYear={this.state.selectedYear}
                                        mainReport={this.props.output2} filter={this.filter}
                                        dateField="actual-approval-date" settingsWidget={this.settingsWidget}
                                        prefix={Utils.getCalendarPrefix(this.settingsWidget,this.props.calendars,
                                            this.props.translate('amp.gpi-reports:fy'))}/>
                    <div className="container-fluid no-padding">
                        <div className="dropdown">
                            <select name="donorAgency" className="form-control donor-dropdown" value={this.state.selectedDonor} onChange={this.onDonorFilterChange}>
                                <option value="">{this.props.translations['amp.gpi-reports:all-donors']}</option>
                                {this.props.orgList.map((org, i) =>
                                    <option value={org.id} key={org.id} >{org.name}</option>
                                )}
                            </select>
                        </div>

                    </div>
                    <div className="container-fluid">
                       <div className="row">
                         <h4>{this.props.translations['amp.gpi-reports:indicator1-description']}</h4>
                        </div>
                    </div>
                    <div className="section-divider"></div>
                         {this.props.output2.empty == true  &&
                             <div className="text-center">{this.props.translations['amp-gpi-reports:no-data']}</div>
                         }
                         { this.props.output2.empty == false  &&
                        <table className="table table-bordered table-striped indicator-table">
                        <thead>
                        <tr>
                          <th className="col-md-1">{this.getLocalizedColumnName(Constants.YEAR)}</th>
                          <th className="col-md-1">{this.props.translations['amp-gpi-reports:question']}</th>
                          <th className="col-md-1 number-column" >{this.props.translations['amp-gpi-reports:value']}</th>
                        </tr>
                      </thead>
                      <tbody>
                          {this.createRows()}
                      </tbody>
                      </table>
                        }
                    <div>
                         <PagingSection output2={this.props.output2} goToPage={this.goToPage.bind(this)} updateRecordsPerPage={this.updateRecordsPerPage.bind(this)}/>
                    </div>
                </div>
                }
                </div>
            );
    }

}

function mapStateToProps( state, ownProps ) {
    return {
        output2: state.reports['1'].output2,
        orgList: state.commonLists.orgList,
        years: state.commonLists.years,
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        calendars: state.commonLists.calendars
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report1Output2 );
