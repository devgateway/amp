import React, { Component } from "react";
import '../popups.css';

class CountryPopupExport extends Component {
    render() {
        return (
            <div className="export-wrapper">
                <ul>
                    <li class="jpg" onClick={() => alert('Its not yet implemented!')}>jpg</li>
                    <li class="xls">xls</li>
                    <li class="print">print</li>
                    <li class="email">email</li>
                    <li class="return-link">X</li>
                </ul>
            </div>
        );
    }
}

export default CountryPopupExport;
