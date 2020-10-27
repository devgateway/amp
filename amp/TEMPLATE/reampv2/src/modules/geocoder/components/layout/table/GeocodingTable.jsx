import React, {Component} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import GeocodingActionColumn from "./GeocodingActionColumn";

import './table.css';
import Locations from "./Locations";
import {Loading} from "../panel/Loading";
import {loadGeocoding} from "../../../actions/geocodingAction";
import {TranslationContext} from "../../AppContext";

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

    hasLocations = activityId => {
        return this.props.activities.filter(activity => activity.activity_id === activityId)[0].locations.length > 0;
    }

    getNonExpandebleIds = () => {
        return this.props.activities.filter(activity => activity.locations.length < 1).map(act => act.activity_id);
    }

    componentDidMount() {
        if(this.props.geocodeShouldRun) {
            this.props.loadGeocoding();
        }
    }

    componentDidUpdate() {
        if(this.props.geocodeShouldRun) {
            this.props.loadGeocoding();
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
        debugger;

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
                <Locations activityId={row.activity_id}/>
            ),
            nonExpandable: this.getNonExpandebleIds(),
            showExpandColumn: true,
            expandByColumnOnly: true,
            expandColumnPosition: 'right',
            expandColumnRenderer: ({ expanded, rowKey, expandable }) => (
                <GeocodingActionColumn
                    activityId={rowKey} enabled={this.hasLocations(rowKey)} message={translations['amp.geocoder:noLocations']}
                />
            )
        };

        let options = {

            page: 1,
            sizePerPageList: [{
                text: '10', value: 10
            }, {
                text: '50', value: 50
            }, {
                text: 'All', value: this.props.activities.length
            }],
            sizePerPage: 10,
            pageStartIndex: 1,
            paginationSize: 5,
            prePage: 'Prev',
            nextPage: 'Next',
            firstPage: 'First',
            lastPage: 'Last',
        };

        let columns = [
            {
                dataField: "project_date",
                text: "Date",
                sort: true,
                headerStyle: () => {
                    return { width: "10%" };
                }
            },
            {
                dataField: "project_number",
                text: "Project Number",
                headerStyle: () => {
                    return { width: "20%" };
                },
                sort:true
            },
            {
                dataField: "project_title",
                text: "Project Name",
                headerStyle: () => {
                    return { width: "45%" };
                },
                sort:true
            },
            {
                dataField: "location",
                text: "Location",
                headerStyle: () => {
                    return { width: "15%" };
                },
                sort:true
            },
        ];

        return (
            <div className="activity-table">
                <BootstrapTable
                    keyField="activity_id"
                    scrollY
                    data={this.props.activities}
                    maxHeight="200px"
                    columns={columns}
                    classes="table-striped"
                    expandRow={expandRow}
                    expandableRow={ this.isExpandableRow }
                    expandComponent={ this.expandComponent }
                    pagination={paginationFactory(options)}
                    expandColumnOptions={{
                        expandColumnVisible: true,
                        expandColumnComponent: this.expandColumnComponent,
                        columnWidth: '200px'
                    }}/>
            </div>
        );
    }
}

GeocodingTable.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocodingPending: state.geocodingReducer.pending,
        activities: state.geocodingReducer.activities,
        geocodeShouldRun: state.geocodingReducer.geocodeShouldRun,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadGeocoding: loadGeocoding,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocodingTable);