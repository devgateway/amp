import React, { Component } from "react";
import './popups.css';
import CountryPopupContainer from './countryLarge/CountryPopupContainer';
import { calculateColumnCount } from '../../../utils/Utils';
import { generateStructureBasedOnSectorProjectCount, getChartData } from '../../../utils/ProjectUtils';
import { SSCTranslationContext } from '../../StartUp';
import { SECTORS_DECIMAL_POINTS_CHART } from '../../../utils/constants';
import { ACTIVITY_ID, PROJECT_TITLE } from '../../../utils/FieldsConstants';


class CountryPopupOverlay extends Component {
    getExportData() {
        const {translations} = this.context;
        const {
            projects, sectors, countries, activitiesDetails, countriesForExport
        } = this.props;
        const exportData = {};
        exportData.title = translations['amp.ssc.dashboard:Sector-Analysis'];
        exportData.source = translations['amp.ssc.dashboard:page-title'];
        exportData.filters = [];
        exportData.columns = [];
        exportData.columns.push({
            headerTitle: translations['amp.ssc.dashboard:Country'],
            key: "country",
            width: 30
        });
        exportData.columns.push({
            headerTitle: translations['amp.ssc.dashboard:Sector'],
            key: "sector",
            width: 30
        });
        exportData.columns.push({
            headerTitle: translations['amp.ssc.dashboard:project-percentage'],
            key: "percentage",
            width: 30
        });
        exportData.columns.push({
            headerTitle: translations['amp.ssc.dashboard:project-count'],
            key: "count",
            width: 30
        });
        exportData.columns.push({
            headerTitle: translations['amp.ssc.dashboard:sectors-projects": "Projects'],
            key: "activities",
            width: 100
        });
        exportData.rows = [];
        console.log(countriesForExport);
        projects.forEach(p => {
            debugger;
            if (countriesForExport.length === 0 || countriesForExport.includes(p.id)) {
                const projectsBySectors = generateStructureBasedOnSectorProjectCount(p);
                const chartData = getChartData(projectsBySectors, sectors, true);
                chartData.forEach(cd => {
                    const row = {};
                    row.country = countries.filter(c => c.id === p.id)[0].name;
                    row.sector = cd.simpleLabel;
                    row.percentage = cd.percentage.toFixed(SECTORS_DECIMAL_POINTS_CHART);
                    row.count = cd.value;
                    row.activities = [...cd.activities].map(a => activitiesDetails.activities.find(ad => ad[ACTIVITY_ID] === a)[PROJECT_TITLE]
                    ).join('|');
                    exportData.rows.push(row);
                });
            }
        });
        return exportData;
    }

    render() {
        if (!this.props.show) {
            return null;
        }
        const {projects, closeLargeCountryPopinAndClearFilter, countriesForExport, countriesForExportChanged} = this.props;
        if (!projects || projects.length === 0) {
            return null;
        }
        const columnCount = calculateColumnCount(projects.length);
        const rows = [];
        rows.push(projects.slice(0, columnCount));
        const secondRow = projects.slice(columnCount, projects.length);
        if (secondRow.length > 0) {
            rows.push(secondRow);
        }
        return (
            <div className={`country-popup-wrapper${columnCount === 1 ? '' : ' country' + columnCount}`}>
                <div className="container-fluid">
                    <CountryPopupContainer rows={rows} columnCount={columnCount}
                                           closeLargeCountryPopinAndClearFilter={closeLargeCountryPopinAndClearFilter}
                                           countriesForExport={countriesForExport}
                                           countriesForExportChanged={countriesForExportChanged}
                                           getExportData={this.getExportData.bind(this)}

                    />
                </div>
            </div>
        );
    }
}

export default CountryPopupOverlay;
CountryPopupOverlay.contextType = SSCTranslationContext;
