import React, {Component} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import TableActions from "./ActivityActions";

import './table.css';
import GeocodedActivity from "./LocationsTabel";

class ActivityTable extends Component {
    constructor(props) {
        super(props);
        this.state = { selectedRowAction: null };

        this.handleActionsClick = this.handleActionsClick.bind(this);
        this.wrapper = React.createRef();
    }

    handleActionsClick = selectedRowId => {
        this.setState({ selectedRowAction: selectedRowId });
    };


    render() {
        let expandRow = {
            onlyOneExpanding: true,
            renderer: row => (
                <GeocodedActivity/>
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

        let selectRow = {
            mode: 'checkbox',
            clickToExpand: true,
            style: { background: '#F2FFF8' }
        };

        let columns = [
            {
                dataField: "col1",
                text: "Date",
                sort: true,
                headerStyle: () => {
                    return { width: "10%" };
                }
            },
            {
                dataField: "col2",
                text: "Project Number",
                headerStyle: () => {
                    return { width: "20%" };
                },
                sort:true
            },
            {
                dataField: "col3",
                text: "Project Name",
                headerStyle: () => {
                    return { width: "50%" };
                },
                sort:true
            },
            {
                dataField: "col4",
                text: "Location",
                headerStyle: () => {
                    return { width: "15%" };
                },
                sort:true
            },
            {
                dataField: "actions",
                text: "Actions",
                formatExtraData: this.state.selectedRowAction,
                formatter: (cell, row, rowIndex, formatExtraData) => {
                    console.log(formatExtraData); // nothing happens when
                    return (
                        <TableActions
                            isOpen={formatExtraData === row.id ? true : false}
                            onActionClick={this.handleActionsClick}
                            rowId={row.id}
                        />
                    );
                },
                headerStyle: () => {
                    return { width: "5%" };
                },
            }
        ];

        return (
            <div className="activity-table">
                <BootstrapTable
                    keyField="id"
                    scrollY
                    data={this.props.activities}
                    maxHeight="200px"
                    columns={columns}
                    classes="table-striped"
                    expandRow={expandRow}
                    selectRow={selectRow}
                    pagination={paginationFactory(options)}
                />
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        activities: state.activitiesReducer.activities
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivityTable);