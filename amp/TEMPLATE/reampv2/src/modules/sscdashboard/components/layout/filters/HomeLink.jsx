import React, { Component } from 'react';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';
class HomeLink extends Component {

    onSelectOptions() {
        const {onChangeChartSelect, chartName} = this.props;
        onChangeChartSelect(chartName);
    }

    render() {
        const {chartSelected, title, chartName} = this.props;
        const {translations} = this.context;
        return (
            <div className="sidebar-filter-wrapper">
                <div className="panel panel-default">
                    <div className={`panel-heading${chartSelected === chartName ? ` selected` : ``}`}>
                        <h4 className={`panel-title ${chartName}`} data-toggle="collapse" data-target={`#${chartName}`}>
                            <a onClick={this.onSelectOptions.bind(this)} >
                                {translations[title]}
                            </a>
                        </h4>
                    </div>
                </div>
            </div>
        );
    }
}

export default HomeLink;
HomeLink
    .contextType = SSCTranslationContext;
