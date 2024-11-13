
import React, { useState } from 'react';
import { Container } from 'semantic-ui-react';
import CountriesList from './CountriesList';
import ProjectsList from './ProjectsList';
import InstitutionsList from './InstitutionsList';
import LanguagesList from './LanguagesList';
import TypesList from './TypesList';
import CustomDataTable from './CustomDataTable';

import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import Select from 'react-select';
import {loadWocatProjects} from "../reducers/api"; // Ensure you have installed react-select or adjust accordingly
import { injectIntl } from 'react-intl';

const CountriesList = ({type,selected,onSelect}) => {
    const dispatch = useDispatch();
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const handleSelect = (selectedOption) => {
        onSelect(selectedOption ? selectedOption.value : '');
    };
    useEffect(() => {
        const fetchProjects = async () => {
            try {
                const response = await loadWocatProjects();
                // console.log("Projects: " + response)
                setData(response); // Adjust this line based on the actual response structure
            } catch (error) {
                console.error("Error fetching projects:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchProjects();
    }, [dispatch]);

    // Ensure data is an array
    const options = Array.isArray(data) ? data.map(project => ({
        value: project.projectId,
        label: project.projectName
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
            placeholder="Wocat Project"
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
                        <CountriesList selected={selectedCountry} onSelect={setSelectedCountry} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <ProjectsList selected={selectedProject} onSelect={setSelectedProject} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <InstitutionsList selected={selectedInstitution} onSelect={setSelectedInstitution} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <LanguagesList selected={selectedLanguage} onSelect={setSelectedLanguage} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <TypesList selected={selectedType} onSelect={setSelectedType} />
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
