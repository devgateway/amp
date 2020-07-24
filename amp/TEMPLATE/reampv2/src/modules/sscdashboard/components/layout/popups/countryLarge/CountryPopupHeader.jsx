import React, { Component } from "react";
import '../popups.css';
import { Img } from 'react-image';
import * as Utils from '../../../../utils/Utils';
import { SSCTranslationContext } from '../../../StartUp';
import { SECTOR_LEADING_LEFT, SECTOR_MIN_SIZE } from '../../../../utils/constants';
import { calculateUpdatedValuesForDropDowns } from '../../../../utils/Utils';
import ReactTooltip from 'react-tooltip';

class CountryPopupHeader extends Component {
    onCountrySelected(e) {
        const ipSelectedCountry = parseInt(e.target.value);

        const {countriesForExport, countriesForExportChanged} = this.props;
        countriesForExportChanged(calculateUpdatedValuesForDropDowns(ipSelectedCountry, countriesForExport))
    }

    render() {
        const {translations} = this.context;
        const {country, projectsBySectors, columnCount, countriesForExport} = this.props;
        return (
            <div className="header row">
                <div className={`col-md-${columnCount === 1 ? '4' : '5'}  col-xs-5 country-name`}>
                    <Img
                        src={Utils.getCountryFlag(country.name)}/>
                    {country.name}
                </div>
                <div className={`col-md-${columnCount === 1 ? '2' : '3'} col-xs-3 projects`}>
                    <span
                        className="count">{projectsBySectors.uniqueProjects.size.toString()
                        .padStart(SECTOR_MIN_SIZE, SECTOR_LEADING_LEFT)}</span>
                    <span className="label">{translations['amp.ssc.dashboard:sectors-project']}</span>
                </div>
                <div className={`col-md-${columnCount === 1 ? '3' : '4'} col-xs-4 projects`}>
                    <span
                        className="count">{projectsBySectors.sectors.length.toString()
                        .padStart(SECTOR_MIN_SIZE, SECTOR_LEADING_LEFT)}</span>

                    <span className="label">{translations['amp.ssc.dashboard:sectors-sector-types']}</span>
                    {columnCount !== 1 && <div className={'export-checkbox'}>
                        <div className="custom-checkbox">
                            <input type="checkbox" id={`chk-country-column-${country.id}`} value={country.id}
                                   onClick={this.onCountrySelected.bind(this)}
                                   checked={countriesForExport.includes(country.id)}/>
                            <label htmlFor={`chk-country-column-${country.id}`}
                                   data-tip={translations['amp.ssc.dashboard:sectors-checkbox-tooltip']}
                                   data-for={"download-checkbox"}></label>
                        </div>
                    </div>}
                </div>
                <ReactTooltip place={'bottom'} multiline id={"download-checkbox"}
                              className={"download-checkbox-tooltip"}/>
            </div>
        );
    }
}

CountryPopupHeader.contextType = SSCTranslationContext;
export default CountryPopupHeader;
