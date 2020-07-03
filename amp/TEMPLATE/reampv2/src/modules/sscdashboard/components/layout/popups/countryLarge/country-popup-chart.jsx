import React, { Component } from "react";
import SectorPierChart from './SectorPierChart';
import '../popups.css';
import { SSCTranslationContext } from '../../../StartUp';
import {
    SECTORS_DECIMAL_POINTS_CHART,
    SECTORS_LIMIT_CHART,
    SECTORS_OTHERS_ID_CHART
} from '../../../../utils/constants';
import { toCamelCase } from '../../../../utils/Utils';

class CountryPopupChart extends Component {

    render() {
        const {projectsBySectors, sectors} = this.props;
        const {translations} = this.context;
        const totalActivities = projectsBySectors.sectors.reduce((prev, s) => prev + s.activities.size, 0);

        const data = projectsBySectors.sectors.map(s => {
            const sector = {};
            sector.id = s.id;
            sector.value = s.activities.size;
            sector.percentage = ((sector.value * 100) / totalActivities);
            sector.label = `${sectors.find(ss => ss.id === s.id).name} ${sector.percentage.toFixed(SECTORS_DECIMAL_POINTS_CHART)}%`;
            return sector;
        }).sort(((a, b) => a.count > b.count ? -1 : 1));
        const firstSeven = data.slice(0, SECTORS_LIMIT_CHART);
        const others = data.slice(7, data.length);
        if (others.length > 0) {
            const other = {};
            other.id = SECTORS_OTHERS_ID_CHART;
            other.value = 0;
            other.percentage = 0;
            other.otherValues = others;
            others.forEach(o => {
                other.value += o.value;
                other.percentage += o.percentage;
            });
            other.label = `${toCamelCase(translations['amp.ssc.dashboard:sectors-others'])} ${other.percentage.toFixed(SECTORS_DECIMAL_POINTS_CHART)}%`;
            firstSeven.push(other);
        }
        return (
            <div className="chart-container">
                <SectorPierChart data={firstSeven}/>
            </div>
        );
    }

}

CountryPopupChart.contextType = SSCTranslationContext;
export default CountryPopupChart;
