import React, { Component } from "react";
import '../popups.css';
import { printChart, printInnerCharts } from '../../../../utils/PrintUtils';
import { exportToXLS } from '../../../../utils/exportUtils';
import { PNG_FORMAT } from '../../../../utils/constants';
import ReactTooltip from 'react-tooltip';
import { SSCTranslationContext } from '../../../StartUp';
import PrintCountryCharts from './PrintCountryCharts';


class CountryPopupExport extends Component {

    exportToImage(printFormat) {
        const {printTitle, printChartId, printFilters} = this.props;
        if (this.props.countriesForExport.length === 0 || this.props.onlyOneCountry) {
            printChart(printTitle, printChartId, printFilters, printFormat, true, 'print-dummy-container').then(() => {
            }).catch(e => {
                alert('Error while printing');
            });
        } else {
            printInnerCharts(printTitle, printChartId, printFilters, printFormat, this.props.countriesForExport);
        }

    }

    exportChartToXls() {
        const {getExportData} = this.props;
        exportToXLS(getExportData());
    }

    render() {
        const {onlyOneCountry} = this.props;
        const {translations} = this.context;
        return (
            <div className={`export-wrapper ${onlyOneCountry ? 'single-country' : ''}`}>
                <ul>
                    <li className="png" onClick={this.exportToImage.bind(this, PNG_FORMAT)}
                        data-tip={translations['amp.ssc.dashboard:sectors-png-tooltip']}
                        data-for={"download-png"}>png
                    </li>
                    <li className="xls" data-tip={translations['amp.ssc.dashboard:sectors-xls-tooltip']}
                        data-for={"download-xls"} onClick={this.exportChartToXls.bind(this)}>xls
                    </li>
                    <PrintCountryCharts/>
                    <li className="return-link" onClick={() => this.props.closeLargeCountryPopinAndClearFilter()}>X</li>
                </ul>
                <ReactTooltip place={'bottom'} multiline id={"download-png"}
                              className={"download-checkbox-tooltip"}/>
                <ReactTooltip place={'bottom'} multiline id={"download-xls"}
                              className={"download-checkbox-tooltip"}/>
            </div>
        );
    }
}

CountryPopupExport.contextType = SSCTranslationContext;
export default CountryPopupExport;
