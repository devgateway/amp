import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import FilterCountries from './countries';
import FilterSectorsHorizontal from './sectors-horizontal';
import FilterYears from './years';

import './filters.css';

class HorizontalFilters extends Component {
    render() {
        return (
          <div className="h-filter-wrapper">
          <div className="row inner">
              <div className="col-md-3"><FilterCountries/></div>
              <div className="col-md-3"><FilterSectorsHorizontal/></div>
              <div className="col-md-3"><FilterYears/></div>
          </div>
          </div>
        );
    }
}

export default HorizontalFilters;
