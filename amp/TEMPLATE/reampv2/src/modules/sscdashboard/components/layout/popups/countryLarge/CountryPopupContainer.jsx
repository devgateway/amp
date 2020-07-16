import React, { Component } from 'react';
import CountryPopupOverlayTitle from './CountryPopupOverlayTitle';
import CountryPopupExport from './CountryPopupExport';
import CountryPopup from './CountryPopup';
import { calculateColumnCount } from '../../../../utils/Utils';
import { BOOTSTRAP_COLUMNS_COUNT } from '../../../../utils/constants';
import { SSCTranslationContext } from '../../../StartUp';

export default class CountryPopupContainer extends Component {

    render() {
        const {translations} = this.context;
        const {rows, closeLargeCountryPopinAndClearFilter, columnCount, countriesForExport, countriesForExportChanged,getExportData} = this.props;
        return (<div>

            <CountryPopupOverlayTitle/>
            <CountryPopupExport closeLargeCountryPopinAndClearFilter={closeLargeCountryPopinAndClearFilter}
                                onlyOneCountry={columnCount === 1}
                                printTitle={translations['amp.ssc.dashboard:Sector-Analysis']}
                                printFilters={[]}
                                printChartId="countries-charts"
                                countriesForExport={countriesForExport}
                                countriesForExportChanged={countriesForExportChanged}
                                getExportData={getExportData}
            />
            <div className="countries-charts" id="countries-charts">
                {this.getCountryPopup(rows)}
            </div>
        </div>)
    }

    getLeft(length, r) {
        if (length === 1) {
            return 'left';
        } else {
            const upperLower = this.getUpperLower(length, r);
            return upperLower ? upperLower + '-left' : '';
        }
    }

    getRight(length, r) {
        if (length === 1) {
            return 'right';
        } else {
            const upperLower = this.getUpperLower(length, r);
            return upperLower ? upperLower + '-right' : '';
        }
    }

    getUpperLower(length, r) {
        const firstRow = Math.min(1, length);
        const lastRow = Math.max(1, length);
        if (r === firstRow) {
            return 'upper';
        } else {
            if (r === lastRow) {
                return 'lower';
            } else {
                return null;
            }
        }
    }

    getCountryPopup(rows) {

        const {countriesForExportChanged, countriesForExport} = this.props;
        return rows.map((r, k) => {
            const columnCount = calculateColumnCount(r.length);
            const classCount = BOOTSTRAP_COLUMNS_COUNT / columnCount;
            const left = Math.min(1, r.length);
            const right = Math.max(1, r.length);

            return <div id={`country-row${k}`} key={k}
                        className={`row ${k % 2 === 0 && rows.length > 1 ? ' bottomBorder' : ''}`}>
                {r.map((c, i) => {
                    const borderClass = (i + 1) === left
                        ? this.getLeft(rows.length, k + 1) : ((i + 1) === right
                            ? this.getRight(rows.length, k + 1) : '');

                    const lineClass = (i + 1) === 1 || (i + 1 === 2 && r.length > 2) ? 'line' : '';
                    return (
                        <div className={`chart-column col-md-${classCount}`} key={c.id} id={`country-column${c.id}`}>
                            <CountryPopup project={c}
                                          columnCount={columnCount}
                                          {...(columnCount > 1 ? {'borderClass': borderClass} : {})}
                                          {...(columnCount > 1 ? {'lineClass': lineClass} : {})}
                                          countriesForExportChanged={countriesForExportChanged}
                                          countriesForExport={countriesForExport}


                            />
                        </div>);
                })}</div>
        });
    }
}
CountryPopupContainer.contextType = SSCTranslationContext;

