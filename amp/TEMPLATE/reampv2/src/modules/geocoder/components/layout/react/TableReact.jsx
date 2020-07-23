import React, {Component} from 'react';
import {useTable} from "react-table";
import './react-table.css'
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

function Table({ columns, data }) {
    // Use the state and functions returned from useTable to build your UI
    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({
        columns,
        data,
    })

    // Render the UI for your table
    return (
            <table {...getTableProps()} className='ReactTable rt-table'>
            <thead className='rt-thead'>
            {headerGroups.map(headerGroup => (
                <tr {...headerGroup.getHeaderGroupProps()}>
                    {headerGroup.headers.map(column => (
                        <th {...column.getHeaderProps()}>{column.render('Header')}</th>
                    ))}
                </tr>
            ))}
            </thead>
            <tbody {...getTableBodyProps()} className='rt-body'>
            {rows.map(
                (row, i) => {
                    prepareRow(row);
                    return (
                        <tr {...row.getRowProps()} className='rt-tr'>
                            {row.cells.map(cell => {
                                return <td {...cell.getCellProps()} className='rt-td'>{cell.render('Cell')}</td>
                            })}
                        </tr>
                    )}
            )}
            </tbody>
        </table>
    )
}


class TableReact extends Component {

    render() {
        const columns =  [
                {
                    Header: 'Date',
                    accessor: 'col1',
                },
                {
                    Header: 'Project Number',
                    accessor: 'col2',
                },
                {
                    Header: 'Project Name',
                    accessor: 'col3',
                },
                {
                    Header: 'Location',
                    accessor: 'col4',
                },
                {
                    Header: 'Sector',
                    accessor: 'col5',
                },
                {
                    Header: 'Project Detail',
                    accessor: 'col6',
                },
                {
                    Header: 'Action',
                    accessor: 'col7',
                },
            ]
        return (
            <Table classes='ReactTable' columns={columns} data={this.props.activities}/>
        )
    }
}

const mapStateToProps = state => {
    return {
        activities: state.activitiesReducer.activities
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(TableReact);