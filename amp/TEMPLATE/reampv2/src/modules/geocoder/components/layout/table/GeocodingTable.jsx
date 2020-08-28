import React, {Component} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import GeocodingActionColumn from "./GeocodingActionColumn";

import './table.css';
import Locations from "./Locations";
import {Loading} from "../../../../../utils/components/Loading";

class GeocodingTable extends Component {
    constructor(props) {
        super(props);
        this.state = { selectedRowAction: null };

        this.handleActionsClick = this.handleActionsClick.bind(this);
        this.wrapper = React.createRef();
    }

    handleActionsClick = selectedRowId => {
        this.setState({ selectedRowAction: selectedRowId });
    };

    expandComponent(row) {
        return (
            <div>
                <Locations/>
            </div>
        );
    }

    expandColumnComponent({ isExpandableRow, isExpanded }) {
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

    isExpandableRow(row) {
        if (row.id < 2) return true;
        else return false;
    }


    render() {
        let expandRow = {
            onlyOneExpanding: true,
            renderer: row => (
                <Locations activityId={row.activity_id}/>
            ),
            showExpandColumn: true,
            expandByColumnOnly: true,
            expandColumnPosition: 'right',
            expandColumnRenderer: ({ expanded, rowKey, expandable }) => (
                <GeocodingActionColumn
                    activityId={rowKey}
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
                    return { width: "50%" };
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
                {this.props.geocodingPending
                    ? <Loading/>
                    : <BootstrapTable
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
                }
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        geocodingPending: state.geocodingReducer.pending,
        activities: state.geocodingReducer.activities,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocodingTable);