import React, { Component } from 'react';
import './filters.css';

class FilterYears extends Component {
    render() {
        return (
            <div className="horizontal-filter dropdown">

                <button className="btn btn-primary" type="button" data-toggle="collapse" data-target="#years">
                    Année <span className="select-count">(0/7)</span>
                </button>
                <div className="filter-list collapse" id="years">
                    <div className="well">

                        <div className="well-inner">
                            <ul>
                                <li>
                                    <input type="checkbox" name="y2019" id="y2019"/>
                                    <label htmlFor="y2019">2019</label>
                                </li>
                                <li>
                                    <input type="checkbox" name="y2018" id="y2018"/>
                                    <label htmlFor="y2018">2018</label>
                                </li>
                                <li>
                                    <input type="checkbox" name="y2017" id="y2017"/>
                                    <label htmlFor="y2017">2017</label>
                                </li>
                                <li>
                                    <input type="checkbox" name="y2016" id="y2016"/>
                                    <label htmlFor="y2016">2016</label>
                                </li>
                                <li>
                                    <input type="checkbox" name="y2015" id="y2015"/>
                                    <label htmlFor="y2015">2015</label>
                                </li>
                                <li>
                                    <input type="checkbox" name="y2014" id="y2014"/>
                                    <label htmlFor="y2014">2014</label>
                                </li>
                                <li>
                                    <input type="checkbox" name="y2013" id="y2013"/>
                                    <label htmlFor="y2013">2013</label>
                                </li>

                            </ul>
                        </div>

                    </div>
                </div>

            </div>
        );
    }
}

export default FilterYears;
