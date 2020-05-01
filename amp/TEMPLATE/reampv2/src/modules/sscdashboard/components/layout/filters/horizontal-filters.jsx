import React, {Component} from 'react';
import FilterCountries from './countries';
import FilterSectorsHorizontal from './sectors-horizontal';
import FilterYears from './years';
import CountryCarousel from './carousel';
import CountrySearch from './country-search';

import './filters.css';

class HorizontalFilters extends Component {
    render() {
        return (
          <div className="h-filter-wrapper">
            <div className="carousel-filters-wrapper">
              <CountryCarousel/>
              <CountrySearch/>
            </div>

          <div className="row inner">
            <div id="dropdown-filters-wrapper" style={{display: "none"}}>
              <div className="col-md-3"><FilterCountries/></div>
              <div className="col-md-3"><FilterSectorsHorizontal/></div>
              <div className="col-md-3"><FilterYears/></div>
            </div>
          </div>
          </div>
        );
    }
}

export default HorizontalFilters;
