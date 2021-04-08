import React, {Component} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import GeocodingActionColumn from "./GeocodingActionColumn";
import filterFactory, { textFilter } from 'react-bootstrap-table2-filter';

import './table.css';
import Locations from "./Locations";
import {Loading} from "../panel/Loading";
import {loadGeocoding} from "../../../actions/geocodingAction";
import {TranslationContext} from "../../AppContext";
import ActivityWithoutLocationsDialog from "../panel/dialog/ActivityWithoutLocationsDialog";
import ActivitySaveResultsDialog from "../panel/dialog/ActivitySaveResultsDialog";
import {orderDates} from "../../../utils/utils";
import {PaginationTotal} from "./PaginationTotal";

class GeocodingTable extends Component {
    constructor(props) {
        super(props);
        this.state = { selectedRowAction: null };

        this.handleActionsClick = this.handleActionsClick.bind(this);
        this.hasLocations = this.hasLocations.bind(this);
        this.wrapper = React.createRef();
    }

    handleActionsClick = selectedRowId => {
        this.setState({ selectedRowAction: selectedRowId });
    };

    hasLocations = ampId => {
        return this.props.activities.filter(activity => activity.amp_id === ampId)[0].locations.length > 0;
    }

    getNonExpandableIds = () => {
        return this.props.activities.filter(activity => activity.locations.length < 1).map(act => act.amp_id);
    }

    existLocationsInGeocoding = () => {
        return this.props.activities.some(activity => activity.locations.length > 0);
    }

    existSaveResults = () => {
        return this.props.save_activities_result && this.props.save_activities_result.length > 0;
    }

    componentDidMount() {
        if(this.props.geocodeShouldRun) {
            this.props.loadGeocoding();
        }
    }

    componentDidUpdate() {
        if(this.props.geocodeShouldRun) {
            setTimeout(() => this.props.loadGeocoding(), 5000);
        }
    }

    expandComponent(row) {
        return (
            <div>
                <Locations/>
            </div>
        );
    }

    expandColumnComponent({ isExpandableRow, isExpanded}) {
        let content = '';

        if (isExpandableRow) {
            content = (isExpanded ? '(-)' : '(+)' );
        } else {
            content = ' ';
        }

        return (
            <div>
            <GeocodingActionColumn onActionClick={this.handleActionsClick} />
            {content}
            </div>
        );
    }

    render() {
        const {translations} = this.context;

        if (this.props.geocodingPending || this.props.geocodeShouldRun) {
            return <Loading/>
        }

        let expandRow = {
            onlyOneExpanding: true,
            renderer: row => (
                <Locations ampId={row.amp_id}/>
            ),
            nonExpandable: this.getNonExpandableIds(),
            showExpandColumn: true,
            expandByColumnOnly: true,
            expandColumnPosition: 'right',
            expandColumnRenderer: ({ expanded, rowKey, expandable }) => (
                <GeocodingActionColumn
                    ampId={rowKey} enabled={this.hasLocations(rowKey)} message={translations['amp.geocoder:noLocations']}
                />
            )
        };

        let options = {
            page: 1,
            sizePerPage: this.props.settings['workspace-default-records-per-page'],
            pageStartIndex: 1,
            paginationSize: 5,
            prePage: 'Prev',
            nextPage: 'Next',
            firstPage: 'First',
            lastPage: 'Last',
            hideSizePerPage: true,
            showTotal: true,
            paginationTotalRenderer: (from, to, size) => PaginationTotal(from, to, size)
        };

        let lastUpdatedHeaderText = translations['amp.geocoder:lastUpdatedDate'];
        let ampIdHeaderText = translations['amp.geocoder:ampId'];
        let projectNameHeaderText = translations['amp.geocoder:projectName'];
        let locationHeaderText = translations['amp.geocoder:location'];

        let columns = [
            {
                dataField: "updated_date",
                text: lastUpdatedHeaderText,
                sort: true,
                sortFunc: (a, b, order) => {
                    return orderDates(a, b, order);
                },
                headerStyle: () => {
                    return { width: "15%" };
                }
            },
            {
                dataField: "amp_id",
                text: ampIdHeaderText,
                headerStyle: () => {
                    return { width: "20%" };
                },
                filter: textFilter({
                    placeholder: translations['amp.geocoder:select'] + ' ' + ampIdHeaderText
                }),
                sort:true
            },
            {
                dataField: "project_title",
                text: projectNameHeaderText,
                headerStyle: () => {
                    return { width: "45%" };
                },
                filter: textFilter({
                    placeholder: translations['amp.geocoder:select'] + ' ' + projectNameHeaderText
                }),
                sort:true
            },
            {
                dataField: "location",
                text: locationHeaderText,
                headerStyle: () => {
                    return { width: "15%" };
                },
                sort:true
            },
        ];

        let data = this.props.activities.filter(act => act.locations.length > 0 && act.status !== 'SAVED');

        return (
            <>
            <div className="activity-table">
                <BootstrapTable
                    keyField="amp_id"
                    scrollY
                    data={data}
                    maxHeight="200px"
                    columns={columns}
                    classes="table-striped"
                    expandRow={expandRow}
                    expandableRow={ this.isExpandableRow }
                    expandComponent={ this.expandComponent }
                    pagination={paginationFactory(options)}
                    filter={filterFactory()}
                    expandColumnOptions={{
                        expandColumnVisible: true,
                        expandColumnComponent: this.expandColumnComponent,
                        columnWidth: '200px'
                    }}/>
            </div>
                {this.existSaveResults() ? <ActivitySaveResultsDialog title={translations['amp.geocoder:saveResults']}/>
                    : !this.existLocationsInGeocoding() && <ActivityWithoutLocationsDialog title={translations['amp.geocoder:discardGeocodingButton']}/>
                }
            </>

    );
    }
}

GeocodingTable.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocodingPending: state.geocodingReducer.pending,
        activities: state.geocodingReducer.activities,
        save_activities_result: state.geocodingReducer.save_activities_result,
        geocodeShouldRun: state.geocodingReducer.geocodeShouldRun,
        settings: state.settingsReducer.settings
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadGeocoding: loadGeocoding,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocodingTable);