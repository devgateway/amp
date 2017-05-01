import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as reportsActions from '../actions/ReportsActions';
import * as startUp from '../actions/StartUpAction.jsx';
import * as Constants from '../common/Constants';
export default class Report9b extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = {};
        this.showFilters = this.showFilters.bind( this );
        this.showSettings = this.showSettings.bind( this );
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
        
        this.props.actions.fetchReport9bData(this.getRequestData(), '9b');
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
            this.props.actions.fetchReport9bData(this.getRequestData(), '9b');
            $( this.refs.filterPopup ).hide();
        }.bind( this ) );
    }
    
    getRequestData() {
        var requestData = {                
                "hierarchy" : "donor-group",
                "page" : 1,
                "recordsPerPage" : 150,
                "summary" : true    
            };
        requestData.filters = this.filter.serialize().filters;
        requestData.settings = this.settingsWidget.toAPIFormat();
        return requestData
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
            this.props.actions.fetchReport9bData(this.getRequestData(), '9b');
            $( this.refs.settingsPopup ).hide();
        }.bind( this ) );
    }

    getLocalizedColumnName(originalColumnName) {   
        var name = originalColumnName;
        if(this.props.reportData && this.props.reportData.page && this.props.reportData.page.headers){            
            var header = this.props.reportData.page.headers.filter(header => header.originalColumnName === originalColumnName)[0]
             if (header){
                 name = header.columnName;
             }
        }
        return name;        
    }
     
    getYearCell(addedGroups, row) {
        if(addedGroups.indexOf(row[Constants.YEAR]) === -1){
            addedGroups.push(row[Constants.YEAR]);
            var matches = this.props.reportData.page.contents.filter(content => content[Constants.YEAR] === row[Constants.YEAR]);
            return (<td className="year-col" rowSpan={matches.length}>{row[Constants.YEAR]}</td>)  
        }               
    }
   
    render() {
        var addedGroups = [];
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

                <div className="container-fluid indicator-stats no-padding">
                    <div className="col-md-3">
                        <div className="indicator-stat-wrapper">
                            <div className="stat-value">1,325,356,321</div>
                            <div className="stat-label">National budget execusion procedures</div>
                        </div>
                    </div>
                    <div className="col-md-3">
                        <div className="indicator-stat-wrapper">
                            <div className="stat-value">3,000,356,000</div>
                            <div className="stat-label">National financial reporting procedures</div>
                        </div>
                    </div>
                    <div className="col-md-3">
                        <div className="indicator-stat-wrapper">
                            <div className="stat-value">125,666,716</div>
                            <div className="stat-label">National auditing procedures</div>
                        </div>
                    </div>
                    <div className="col-md-3">
                        <div className="indicator-stat-wrapper">
                            <div className="stat-value">2,022,112</div>
                            <div className="stat-label">National procuderement systems</div>
                        </div>
                    </div>
                </div>
                <div className="container-fluid no-padding">
                    <ul className="year-nav">
                        <li className="active">
                            <a href="#">all years</a>
                        </li>
                        <li><a href="#">2017</a></li>
                        <li><a href="#">2016</a></li>
                        <li><a href="#">2015</a></li>
                        <li>
                            <div className="dropdown">
                                <a className="btn dropdown-toggle btn-years" type="button" id="years" data-toggle="dropdown">
                             {this.props.translations['amp.gpi-reports:other-years']}
                            <span className="caret"></span></a>
                                <ul className="dropdown-menu dropdown-years" role="menu">
                                    <li role="presentation"><a href="#">2014</a></li>
                                    <li role="presentation"><a href="#">2013</a></li>
                                    <li role="presentation"><a href="#">2012</a></li>
                                    <li role="presentation"><a href="#">2011</a></li>
                                    <li role="presentation"><a href="#">2010</a></li>
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
                        <button className="btn btn-organizations btn-large dropdown-toggle" type="button" id="donors" data-toggle="dropdown">
                    {this.props.translations['amp.gpi-reports:all-donors']}
                      <span className="caret"></span></button>
                        <ul className="dropdown-menu donor-menu" role="menu">
                            <li role="presentation"><a role="menuitem" href="#">Donor 1</a></li>
                            <li role="presentation"><a role="menuitem" href="#">Donor 2</a></li>
                            <li role="presentation"><a role="menuitem" href="#">Donor 3</a></li>
                            <li role="presentation"><a role="menuitem" href="#">Donor 4</a></li>
                        </ul>
                    </div>

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
                            <th className="col-md-2">{this.getLocalizedColumnName(Constants.DONOR_AGENCY)}</th>
                            <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_BUDGET_EXECUTION_PROCEDURES)}</th>
                            <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES)}</th>
                            <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_AUDITING_PROCEDURES)}</th>
                            <th className="col-md-2">{this.getLocalizedColumnName(Constants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES)}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.props.reportData && this.props.reportData.page && this.props.reportData.page.contents.map((row, i)=>                         
                        <tr key={i} >
                        {this.getYearCell(addedGroups, row)}
                        <td>{row[Constants.DONOR_AGENCY] ||row[Constants.DONOR_GROUP]  }</td>
                        <td>{row[Constants.NATIONAL_BUDGET_EXECUTION_PROCEDURES]}</td>
                        <td>{row[Constants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES]}</td>
                        <td>{row[Constants.NATIONAL_AUDITING_PROCEDURES]}</td>
                        <td>{row[Constants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES]}</td>
                         </tr>  
                        )}
                     
                    </tbody>
                </table>
            </div>
        );
    }
}

function mapStateToProps( state, ownProps ) {
    return {
        reportData: state.reports['9b'],
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, reportsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report9b );
