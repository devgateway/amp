import React, {Component} from 'react';
import './filters.css';

class FilterSupport extends Component {
    render() {
        return (
          <div className="sidebar-filter-wrapper">

             <div className="panel panel-default">

              <div className="panel-heading">
                <h4 className="panel-title support" data-toggle="collapse" data-target="#support">
                  Types de Support
                </h4>
              </div>
              <div id="support" className="panel-collapse collapse">
                <div className="panel-body">
                  Support options
                </div>
              </div>


             </div>


          </div>
        );
    }
}

export default FilterSupport;
