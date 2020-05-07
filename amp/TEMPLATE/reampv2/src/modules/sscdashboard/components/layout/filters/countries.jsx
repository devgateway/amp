import React, { Component } from 'react';
import './filters.css';

class FilterCountries extends Component {
    render() {
        return (
            <div className="horizontal-filter dropdown panel">
                <button className="btn btn-primary" type="button" type="button" data-toggle="collapse"
                        data-parent="#accordion-filter" href="#collapse2"
                        aria-controls="collapse2">
                    Pays <span className="select-count">(0/15)</span>
                </button>
                <div className="filter-list collapse" id="collapse2">
                    <div className="well">

                        <div className="well-inner">
                            <ul>
                                <li>
                                    <input type="checkbox" name="af" id="af"/>
                                    <label htmlFor="af">Afghanistan</label>
                                </li>
                            </ul>
                        </div>

                    </div>
                </div>

            </div>
        );
    }
}

export default FilterCountries;
