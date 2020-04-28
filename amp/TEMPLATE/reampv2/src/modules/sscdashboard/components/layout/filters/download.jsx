import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './filters.css';

class FilterDownload extends Component {
    render() {
        return (
          <div className="sidebar-filter-wrapper">

             <div class="panel panel-default">

              <div class="panel-heading">
                <h4 class="panel-title download" data-toggle="collapse" data-target="#download">
                  Téléchargez la Cart
                </h4>
              </div>
              <div id="download" class="panel-collapse collapse">
                <div class="panel-body">
                  Download options
                </div>
              </div>


             </div>


          </div>
        );
    }
}

export default FilterDownload;
