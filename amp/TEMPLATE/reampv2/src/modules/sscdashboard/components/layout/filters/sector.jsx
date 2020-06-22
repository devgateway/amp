import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './filters.css';
import { SECTORS_CHART } from '../../../utils/constants';
import { SSCTranslationContext } from '../../StartUp';

class FilterSector extends Component {


    onSelectOptions(e) {
        const {onChangeChartSelect} = this.props;
        onChangeChartSelect(SECTORS_CHART);
    }

    render() {
        const {translations} = this.context;
        const {chartSelected} = this.props;
        return (
            <div className="sidebar-filter-wrapper">
                <div className="panel panel-default">
                    <div className={`panel-heading ${chartSelected === SECTORS_CHART ? `selected` : ``}`}>
                        <h4 className="panel-title sector" data-toggle="collapse" data-target="#sector">
                            <a onClick={this.onSelectOptions.bind(this)}>
                                {translations['amp.ssc.dashboard:Sector-Analysis']}
                            </a>
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
FilterSector.contextType = SSCTranslationContext;
