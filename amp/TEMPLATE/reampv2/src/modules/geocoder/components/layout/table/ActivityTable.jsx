import React, {Component} from 'react';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from 'react-bootstrap-table2-paginator';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import filterFactory, { textFilter } from 'react-bootstrap-table2-filter';
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";

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

        const renderSelectAllTooltip = props => (
            <Tooltip {...props}>{translations['amp.geocoder:selectAllTooltip']}</Tooltip>
        );

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
            selectionHeaderRenderer: ({mode, checked}) => (
                <OverlayTrigger placement="top" overlay={renderSelectAllTooltip}>
                    <input type={mode} checked={checked}/>
                </OverlayTrigger>
            ),
            onSelectAll: this.props.onSelectAllActivities
        };

        let lastUpdatedHeaderText = translations['amp.geocoder:lastUpdatedDate'];
        let ampIdHeaderText = translations['amp.geocoder:ampId'];
        let projectNameHeaderText = translations['amp.geocoder:projectName'];
        let locationHeaderText = translations['amp.geocoder:location'];

        let columns = [
            {
                dataField: "id",
                text: "ID",
                hidden: true
            },
            {
                dataField: "col1",
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
                dataField: "col2",
                text: ampIdHeaderText,
                headerStyle: () => {
                    return { width: "20%" };
                },
                sort:true,
                filter: textFilter({
                    placeholder: translations['amp.geocoder:select'] + ' ' + ampIdHeaderText
                })
            },
            {
                dataField: "col3",
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
                dataField: "col4",
                text: locationHeaderText,
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
                            filter={filterFactory()}
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