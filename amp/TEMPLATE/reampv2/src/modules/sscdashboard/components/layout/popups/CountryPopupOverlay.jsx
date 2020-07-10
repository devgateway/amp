import React, { Component } from "react";
import './popups.css';
import CountryPopupContainer from './countryLarge/CountryPopupContainer';
import { calculateColumnCount } from '../../../utils/Utils';


class CountryPopupOverlay extends Component {
    render() {
        if (!this.props.show) {
            return null;
        }
        const {projects, closeLargeCountryPopin} = this.props;
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
                    <CountryPopupContainer rows={rows} columnCount={columnCount} closeLargeCountryPopin={closeLargeCountryPopin}/>
                </div>
            </div>
        );
    }
}

export default CountryPopupOverlay;
