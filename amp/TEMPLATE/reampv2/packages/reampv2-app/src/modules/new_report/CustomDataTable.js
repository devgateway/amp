import React, { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import { CSVLink } from 'react-csv';

import {loadReportData} from "./api";

const CustomDataTable = ({ selectedCoreType, selectedCountry, selectedDonor, selectedIndicator, selectedProgram, selectedActivity }) => {
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [perPage, setPerPage] = useState(10);

    const [selectedRows, setSelectedRows] = useState([]);

    const handleRowSelected = (state) => {
        setSelectedRows(state.selectedRows);
    };
    const fetchData = async (page, size) => {
        try {
            console.log("size, page", size,page)
            const response = await loadReportData(size, page, {
                core_type_name: selectedCoreType,
                country_name: selectedCountry,
                donor_name: selectedDonor,
                indicator_name: selectedIndicator,
                program_name: selectedProgram,
                activity_name: selectedActivity,
            });
            setData(response.content);
            setTotalRows(response.totalElements);
        } catch (err) {
            setError(err);
            console.error('Error fetching data:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage, perPage);
    }, [currentPage, perPage,selectedCoreType, selectedCountry, selectedDonor, selectedIndicator,selectedProgram,selectedActivity]);
    const truncateText = (text, len) => {
        const words = text?.split(' ');
        if (words?.length > len) {
            return `${words.slice(0, 10).join(' ')}...`;
        }
        return text;
    };


    const columns = [
        { name: 'Project/Activity', selector: row => row.activity_name, sortable: true },
        { name: 'Indicator', selector: row => row.indicator_name, sortable: true },
        { name: 'Type', selector: row => row.core_type_name, sortable: true },
        { name: 'Country', selector: row => row.country_name, sortable: true },
        { name: 'Donor', selector: row => row.donor_name, sortable: true },
        { name: 'Program', selector: row => row.program_name, sortable: true },

    ];



    const customStyles = {
        headRow: {
            style: {
                backgroundColor: '#78c800', // Header background color
                color: '#333', // Header text color
                fontWeight: 'bold', // Header font weight
            },
        },
        headCells: {
            style: {
                padding: '10px', // Header cell padding
            },
        },
        cells: {
            style: {
                padding: '10px', // Cell padding
                whiteSpace: 'normal', // Allow text to wrap
                wordWrap: 'break-word', // Break long words
            },
        },
        rows: {
            style: {
                '&:nth-of-type(even)': {
                    backgroundColor: 'rgba(255,255,255,0.27)',
                },
                '&:hover': {
                    backgroundColor: '#ddd',
                    cursor: 'pointer'

                },
            },
        },
    };
    const handlePageChange = page => {
        setCurrentPage(page);
    };

    const handlePerRowsChange = async (newPerPage, page) => {
        setPerPage(newPerPage);
        setCurrentPage(page);
        await fetchData(page, newPerPage); // Fetch new data based on newPerPage
    };


    if (loading) {
        return <div>loading</div>;
    }

    if (error) {
        return <div> {error.message}</div>;
    }

    if (data?.length === 0) {
        return <div>No data</div>;
    }
    let pageSizes=totalRows>500?[10, 25, 50, 100, 500,totalRows]:[10, 25, 50, 100, 500]

    return (
        <div>
            <div style={{ marginBottom: '20px', textAlign: 'right' }}>
                <CSVLink
                    data={selectedRows.length > 0 ? selectedRows : data}
                    filename={"projectList-"+currentPage+"-"+perPage+"-data.csv"}
                    className="btn btn-primary"
                    target="_blank"
                    style={{
                        backgroundColor: '#007bff',
                        color: '#fff',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px'
                    }}
                >
                    Export Data
                </CSVLink>
            </div>
            <DataTable
                columns={columns}
                data={data}
                pagination
                paginationServer
                selectableRows
                onSelectedRowsChange={handleRowSelected}
                paginationPerPage={perPage}
                paginationRowsPerPageOptions={pageSizes}
                paginationTotalRows={totalRows} // Update this with the total number of rows from your API
                onChangePage={handlePageChange}
                onChangeRowsPerPage={handlePerRowsChange}
                customStyles={customStyles}

            />
        </div>
    );
};

export default CustomDataTable;
