import React, {Component} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

import './table.css';
import {loadActivities} from "../../../actions/activitiesAction";
import {orderDates} from "../../../utils/utils";
import {Loading} from "../panel/Loading";
import {TranslationContext} from "../../AppContext";
import {PaginationTotal} from "./PaginationTotal";

class ActivityTable extends Component {
    constructor(props) {
        super(props);

        this.wrapper = React.createRef();
    }

    componentDidMount() {
        this.props.loadActivities();
    }

    render() {
        const {translations} = this.context;

        let options = {
            page: 1,
            sizePerPage: this.props.settings['workspace-default-records-per-page'],
            pageStartIndex: 1,
            paginationSize: 15,
            prePage: 'Prev',
            nextPage: 'Next',
            firstPage: 'First',
            lastPage: 'Last',
            hideSizePerPage: true,
            showTotal: true,
            paginationTotalRenderer: (from, to, size) => PaginationTotal(from, to, size)
        };

        let selectRow = {
            mode: 'checkbox',
            style: { background: '#F2FFF8' },
            onSelect: (row, isSelected, rowIndex, e) => {
                this.props.onSelectActivity(isSelected, row.id);
            },
            onSelectAll: this.props.onSelectAllActivities
        };

        let col1Text = translations['amp.geocoder:lastUpdatedDate'];
        let col2Text = translations['amp.geocoder:ampId'];
        let col3Text = translations['amp.geocoder:projectName'];
        let col4Text = translations['amp.geocoder:location'];


        let columns = [
            {
                dataField: "id",
                text: "ID",
                hidden: true
            },
            {
                dataField: "col1",
                text: col1Text,
                sort: true,
                sortFunc: (a, b, order) => {
                    return orderDates(a, b, order);
                },
                headerStyle: () => {
                    return { width: "10%" };
                }
            },
            {
                dataField: "col2",
                text: col2Text,
                headerStyle: () => {
                    return { width: "20%" };
                },
                sort:true
            },
            {
                dataField: "col3",
                text: col3Text,
                headerStyle: () => {
                    return { width: "50%" };
                },
                sort:true
            },
            {
                dataField: "col4",
                text: col4Text,
                headerStyle: () => {
                    return { width: "15%" };
                },
                sort:true
            }
        ];

        return (
            <div className="activity-table">
                {this.props.activitiesPending
                    ? <Loading/>
                    : <>
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
                            noDataIndication={() => translations['amp.geocoder:noActivities']}
                        />
                        </>}
               </div>
          );
    }
}

ActivityTable.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        activitiesPending: state.activitiesReducer.pending,
        activities: state.activitiesReducer.activities,
        settings: state.settingsReducer.settings
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadActivities: loadActivities,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivityTable);