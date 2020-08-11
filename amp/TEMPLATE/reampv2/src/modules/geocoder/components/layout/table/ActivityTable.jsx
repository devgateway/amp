import React, {Component, useState} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

import './table.css';
import {
    loadActivities,
    selectActivityForGeocoding,
    selectAllActivitiesForGeocoding
} from "../../../actions/activitiesAction";
import {Loading} from "../../../../../utils/components/Loading";

const SelectedActivitiesMessage = (props) =>  {
    return (
        <>
           <div className="activities-message"><b>{props.size}</b> activities selected for geocoding</div>
        </>
    );
}

class ActivityTable extends Component {
    constructor(props) {
        super(props);

        this.handleOnSelect = this.props.onSelectActivity;
        this.handleOnSelectAll = this.props.onSelectAllActivities;

        this.wrapper = React.createRef();
    }

    componentDidMount() {
        this.props.loadActivities();
    }

    render() {
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
            style: { background: '#F2FFF8' },
            onSelect: (row, isSelected, rowIndex, e) => {
                this.handleOnSelect(isSelected, row.id);
            },
            onSelectAll: this.handleOnSelectAll
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
            }
        ];

        return this.props.activitiesPending ? (<div className="activity-table"><Loading/></div>)
                : (<div className="activity-table">
                        <BootstrapTable
                            ref={n => this.node = n}
                            keyField="id"
                            scrollY
                            data={this.props.activities}
                            maxHeight="200px"
                            columns={columns}
                            classes="table-striped"
                            selectRow={selectRow}
                            pagination={paginationFactory(options)}
                        />
                        <SelectedActivitiesMessage size={this.props.selectedActivities.length} />
                    </div>);
    }
}

const mapStateToProps = state => {
    return {
        activitiesPending: state.activitiesReducer.pending,
        activities: state.activitiesReducer.activities,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadActivities: loadActivities,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivityTable);