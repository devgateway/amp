import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";

class Locations extends Component {
    render() {
        let columns = [
            {
                dataField: "col1",
                text: "Location",
                headerAttrs: {
                    hidden: true
                }
            },
            {
                dataField: "col2",
                text: "Field",
                headerAttrs: {
                    hidden: true
                }
            },
            {
                dataField: "col3",
                text: "Text",
                headerAttrs: {
                    hidden: true
                }
            },
            {
                dataField: "col4",
                text: "Actions",
                headerAttrs: {
                    hidden: true
                }
            }];

        let data = [
            { col1: 'Haiti', col2: 'project_title', col3: 'Haiti project title' },
            { col1: 'Port au Prince', col2: 'description', col3: 'Port au Prince description' },
            { col1: 'Jacmel', col2: 'objective', col3: 'Jacmel objective' }
        ]

        return ( <div>
            <p>{this.props.geocoding}</p>
            <div className={'search-result'}><b>3</b> Search Results of <b>Locations</b></div>
            <div>
                <BootstrapTable
                    keyField="id"
                    columns={columns}
                    classes={'geocoded-table'}
                    data={data}
                />
            </div>
        </div>);
    }
}

const mapStateToProps = state => {
    return {
        geocoding: state.activitiesReducer.geocoding
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Locations);