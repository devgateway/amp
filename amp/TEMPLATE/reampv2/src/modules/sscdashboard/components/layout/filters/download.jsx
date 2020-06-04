import React, { Component } from 'react';
import './filters.css';

class FilterDownload extends Component {
    render() {
        return (
            <div className="sidebar-filter-wrapper">
                <div className="panel panel-default">
                    <div className="panel-heading">
                        <h4 className="panel-title download" data-toggle="collapse" data-target="#download">
                            Téléchargez la Cart
                        </h4>
                    </div>
                    <div id="download" className="panel-collapse collapse">
                        <div className="panel-body">
                            Download options
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default FilterDownload;
