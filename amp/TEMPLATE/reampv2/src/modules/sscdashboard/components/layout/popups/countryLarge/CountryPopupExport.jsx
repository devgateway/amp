import React, { Component } from "react";
import '../popups.css';

class CountryPopupExport extends Component {
    render() {
        const {onlyOneCountry} = this.props;
        return (
            <div className={`export-wrapper ${onlyOneCountry ? 'single-country' : ''}`}>
                <ul>
                    <li className="jpg" onClick={() => alert('Its not yet implemented!')}>png</li>
                    <li className="xls">xls</li>
                    <li className="print">print</li>
                    <li className="email">email</li>
                    <li className="return-link" onClick={() => this.props.closeLargeCountryPopin()}>X</li>
                </ul>
            </div>
        );
    }
}

export default CountryPopupExport;
