import React, { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import { CSVLink } from 'react-csv';
import { loadReportData } from "./api";
import './DataTable.css'
const CustomDataTable = ({ selectedCoreType, selectedCountry, selectedDonor, selectedIndicator, selectedProgram, selectedActivity }) => {
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [perPage, setPerPage] = useState(10);
    const [selectedRows, setSelectedRows] = useState([]);
    const [fullScreen, setFullScreen] = useState(false); // State for full-screen mode

    const handleRowSelected = (state) => {
        setSelectedRows(state.selectedRows);
    };

    const fetchData = async (page, size) => {
        try {
            console.log("size, page", size, page);
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
    }, [currentPage, perPage, selectedCoreType, selectedCountry, selectedDonor, selectedIndicator, selectedProgram, selectedActivity]);

    const truncateText = (text, len) => {
        const words = text?.split(' ');
        return words?.length > len ? `${words.slice(0, len).join(' ')}...` : text;
    };

    const toggleFullScreen = () => {
        setFullScreen(!fullScreen);
    };

    const columns = [
        { name: 'Project/Activity', selector: row => row.activity_name, sortable: true },
        { name: 'Indicator', selector: row => row.indicator_name, sortable: true },
        { name: 'Type', selector: row => row.core_type_name, sortable: true },
        { name: 'Country', selector: row => row.country_name, sortable: true, wrap:true },
        { name: 'Donor', selector: row => row.donor_name, sortable: true, wrap: true },
        { name: 'Program', selector: row => row.program_name, sortable: true },
        { name: 'Target Value', selector: row => row.target_value, sortable: true },
        { name: 'Actual Value', selector: row => row.target_value, sortable: true },
    ];

    const customStyles = {
        headRow: {
            style: {
                backgroundColor: '#78c800',
                color: '#333',
                fontWeight: 'bold',
            },
        },
        headCells: {
            style: {
                padding: '10px',
            },
        },
        cells: {
            style: {
                padding: '10px',
                whiteSpace: 'normal',
                wordWrap: 'break-word',
            },
        },
        rows: {
            style: {
                '&:nth-of-type(even)': {
                    backgroundColor: 'rgba(255,255,255,0.27)',
                },
                '&:hover': {
                    backgroundColor: '#ddd',
                    cursor: 'pointer',
                },
            },
        },
    };

    const handlePageChange = (page) => setCurrentPage(page);
    const handlePerRowsChange = async (newPerPage, page) => {
        setPerPage(newPerPage);
        setCurrentPage(page);
        await fetchData(page, newPerPage);
    };

    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error.message}</div>;
    if (data?.length === 0) return <div>No data</div>;

    let pageSizes = totalRows > 500 ? [10, 25, 50, 100, 500, totalRows] : [10, 25, 50, 100, 500];

    return (
        <div className={fullScreen ? 'full-screen-container' : ''}>
            <div style={{ marginBottom: '20px', textAlign: 'right' }}>

                <CSVLink
                    data={selectedRows.length > 0 ? selectedRows : data}
                    filename={`projectList-${currentPage}-${perPage}-data.csv`}
                    className="btn btn-primary"
                    target="_blank"
                    style={{
                        backgroundColor: '#007bff',
                        color: '#fff',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px',
                    }}
                >
                    Export Data
                </CSVLink>
                <div onClick={toggleFullScreen} style={{ cursor: 'pointer', display: 'inline-block', marginLeft: '20px' }}>
                    {fullScreen
                        ? <img src={process.env.PUBLIC_URL + '/full_screen_on.png'} alt="Fullscreen" style={{ width: '40px', height: '40px' }} />
                        : <img src={process.env.PUBLIC_URL + '/full_screen_off.png'} alt="Exit Fullscreen" style={{ width: '40px', height: '40px' }} />
                    }
                </div>
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
                paginationTotalRows={totalRows}
                onChangePage={handlePageChange}
                onChangeRowsPerPage={handlePerRowsChange}
                customStyles={customStyles}
            />
        </div>
    );
};

export default CustomDataTable;
