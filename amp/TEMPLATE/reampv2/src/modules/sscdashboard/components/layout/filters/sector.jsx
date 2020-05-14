import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './filters.css';
import { ROUTES_SECTOR } from '../../../utils/constants';

class FilterSector extends Component {
    render() {
        return (
            <div className="sidebar-filter-wrapper">

                <div className="panel panel-default">

                    <div className="panel-heading">
                        <h4 className="panel-title sector" data-toggle="collapse" data-target="#sector">
                            <Link to={ROUTES_SECTOR}>
                                Secteur
                            </Link>
                        </h4>
                    </div>
                    <div id="sector" className="panel-collapse collapse">
                        <div className="panel-body">
                            Sector Options
                        </div>
                    </div>
                </div>


            </div>
        );
    }
}

export default FilterSector;
