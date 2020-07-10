import React, { Component } from "react";
import '../popups.css';
import { Img } from 'react-image';
import * as Utils from '../../../../utils/Utils';
import { SSCTranslationContext } from '../../../StartUp';
import { SECTOR_LEADING_LEFT, SECTOR_MIN_SIZE } from '../../../../utils/constants';

class CountryPopupHeader extends Component {
    render() {
        const {translations, columnCount} = this.context;
        const {country, projectsBySectors} = this.props;
        return (
            <div className="header row">
                <div className={`col-md-${columnCount === 1 ? '4' : '5'} country-name`}>
                    <Img
                        src={Utils.getCountryFlag(country.name)}/>
                    {country.name}
                </div>
                <div className={`col-md-${columnCount === 1 ? '2' : '3'} projects`}>
                    <span
                        className="count">{projectsBySectors.uniqueProjects.size.toString()
                        .padStart(SECTOR_MIN_SIZE, SECTOR_LEADING_LEFT)}</span>
                    <span className="label">{translations['amp.ssc.dashboard:sectors-project']}</span>
                </div>
                <div className={`col-md-${columnCount === 1 ? '3' : '4'} projects`}>
                    <span
                        className="count">{projectsBySectors.sectors.length.toString()
                        .padStart(SECTOR_MIN_SIZE, SECTOR_LEADING_LEFT)}</span>
                    <span className="label">{translations['amp.ssc.dashboard:sectors-sector-types']}</span>
                </div>
            </div>
        );
    }
}

CountryPopupHeader.contextType = SSCTranslationContext;
export default CountryPopupHeader;
