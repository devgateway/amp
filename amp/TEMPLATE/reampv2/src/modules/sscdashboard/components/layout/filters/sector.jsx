import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './filters.css';

class FilterSector extends Component {
    render() {
        return (
          <div className="sidebar-filter-wrapper">

             <div class="panel panel-default">

              <div class="panel-heading">
                <h4 class="panel-title sector" data-toggle="collapse" data-target="#sector">
                  Secteur
                </h4>
              </div>
              <div id="sector" class="panel-collapse collapse">
                <div class="panel-body">
                  Sector Options
                </div>
              </div>


             </div>


          </div>
        );
    }
}

export default FilterSector;
