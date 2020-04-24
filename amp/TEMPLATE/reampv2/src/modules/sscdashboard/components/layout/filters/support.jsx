import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './filters.css';

class FilterSupport extends Component {
    render() {
        return (
          <div className="sidebar-filter-wrapper">

             <div class="panel panel-default">

              <div class="panel-heading">
                <h4 class="panel-title support" data-toggle="collapse" data-target="#support">
                  Types de Support
                </h4>
              </div>
              <div id="support" class="panel-collapse collapse">
                <div class="panel-body">
                  Support options
                </div>
              </div>


             </div>


          </div>
        );
    }
}

export default FilterSupport;
