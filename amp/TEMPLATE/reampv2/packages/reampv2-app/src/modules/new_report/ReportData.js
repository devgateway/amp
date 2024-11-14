
import React, {useEffect, useState} from 'react';
import { Container } from 'semantic-ui-react';

import CustomDataTable from './CustomDataTable';

import { useDispatch } from 'react-redux';
import Select from 'react-select';
import {loadFilterOptions} from "./api";

const FiltersList = ({type, name,selected,onSelect}) => {
    const dispatch = useDispatch();
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const handleSelect = (selectedOption) => {
        onSelect(selectedOption ? selectedOption.value : '');
    };
    useEffect(() => {
        const fetchOptions = async () => {
            try {
                const response = await loadFilterOptions(type);
                // console.log("Projects: " + response)
                setData(response); // Adjust this line based on the actual response structure
            } catch (error) {
                console.error("Error fetching projects:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchOptions();
    }, [dispatch]);

    // Ensure data is an array
    const options = Array.isArray(data) ? data.map(item => ({
        value: item,
        label: item
    })) : [];

    if (loading) {
        return <div>Loading Projects</div>;
    }

    if (options.length === 0) {
        return <div>No Projects data</div>;
    }

    return (
        <Select
            value={options.find(option => option.value === selected) || null}
            onChange={option => onSelect(option ? option.value : '')}
            options={options}
            placeholder={`Filter by ${name}`}
            className="basic-single"
            isSearchable
            isLoading={loading}
            isClearable
        />
    );
};



const ReportData = () => {
    const [selectedCoreType, setSelectedCoreType] = useState('');
    const [selectedCountry, setSelectedCountry] = useState('');
    const [selectedDonor, setSelectedDonor] = useState('');
    const [selectedIndicator, setSelectedIndicator] = useState('');
    const [selectedProgram, setSelectedProgram] = useState('');
    const [selectedActivity, setSelectedActivity] = useState('');

    const clearSearch = (e) => {
        e.preventDefault();
        setSelectedCoreType('');
        setSelectedCountry('');
        setSelectedDonor('');
        setSelectedIndicator('');
        setSelectedProgram('');
        setSelectedActivity('');
    };

    return (
        <div style={{ padding: '20px 40px', boxSizing: 'border-box' }}>
            <Container className="body" style={{ margin: '0 auto', maxWidth: '1200px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px', gap: '10px' }}>
                    <div style={{ flex: 1 }}>
                        <FiltersList type={'core_type_name'} name={'Core Type'} selected={selectedCoreType} onSelect={setSelectedCoreType} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <FiltersList type={'country_name'} name={'Country'} selected={selectedCountry} onSelect={setSelectedCountry} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <FiltersList  type={'donor_name'} name={'Donor'} selected={selectedDonor} onSelect={setSelectedDonor} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <FiltersList type={'indicator_name'} name={'Indicator'} selected={selectedIndicator} onSelect={setSelectedIndicator} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <FiltersList type={'program_name'} name={'Program'} selected={selectedProgram} onSelect={setSelectedProgram} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <FiltersList type={'activity_name'} name={'Project/Activity'} selected={selectedActivity} onSelect={setSelectedActivity} />
                    </div>
                </div>
                <div style={{ marginBottom: '20px', textAlign: 'center' }}>
                    <button
                        onClick={clearSearch}
                        style={{
                            backgroundColor: '#6cc273',
                            color: '#fff',
                            border: 'none',
                            padding: '10px 20px',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px'
                        }}
                    >
                        Clear Search
                    </button>
                </div>
                <CustomDataTable
                    selectedCountry={selectedCountry}
                    selectedActivity={selectedActivity}
                    selectedDonor={selectedDonor}
                    selectedIndicator={selectedIndicator}
                    selectedProgram={selectedProgram}
                    selectedCoreType={selectedCoreType}
                />
            </Container>
        </div>
    );
};

export default ReportData;
