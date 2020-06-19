import React, { Component } from 'react';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';
import { HOME_CHART } from '../../../utils/constants';

class FilterHome extends Component {

    onSelectOptions(e) {
        const {onChangeChartSelect} = this.props;
        onChangeChartSelect(HOME_CHART);
    }

    render() {
        const {chartSelected} = this.props;
        const {translations} = this.context;
        return (
            <div className="sidebar-filter-wrapper">
                <div className="panel panel-default">
                    <div className={`panel-heading ${chartSelected === HOME_CHART ? `selected` : ``}`}>
                        <h4 className="panel-title home " data-toggle="collapse" data-target="#home">
                            <a onClick={this.onSelectOptions.bind(this)}>
                                {translations['amp.ssc.dashboard:Home-Page']}
                            </a>
                        </h4>
                    </div>
                </div>
            </div>
        );
    }
}

export default FilterHome;
FilterHome
    .contextType = SSCTranslationContext;
