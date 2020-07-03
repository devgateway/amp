import React, { Component } from "react";
import '../popups.css';
import { Img } from 'react-image';
import * as Utils from '../../../../utils/Utils';
import { SSCTranslationContext } from '../../../StartUp';

class CountryPopupHeader extends Component {
    render() {
        const {translations} = this.context;
        const {country, projectsBySectors} = this.props;
        return (
            <div className="header">
                <div className="col-md-4 country-name">
                    <Img
                        src={Utils.getCountryFlag(country.name)}/>
                    {country.name}
                </div>
                <div className="col-md-2 projects">
                    <span className="count">{projectsBySectors.uniqueProjects.size}</span>
                    <span className="label">{translations['amp.ssc.dashboard:sectors-project']}</span>
                </div>
                <div className="col-md-2 projects">
                    <span className="count">{projectsBySectors.sectors.length}</span>
                    <span className="label">{translations['amp.ssc.dashboard:sectors-sector-types']}</span>
                </div>
            </div>
        );
    }
}

CountryPopupHeader.contextType = SSCTranslationContext;
export default CountryPopupHeader;
