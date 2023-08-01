import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';

class FiltersLink extends Component {
  onSelectOptions() {
    const { onChangeChartSelect, chartName, onLinkClicked } = this.props;
    onChangeChartSelect(chartName);
    if (onLinkClicked) {
      onLinkClicked();
    }
  }

  render() {
    const { chartSelected, title, chartName } = this.props;
    const { translations } = this.context;
    const onSelectedOptions = this.onSelectOptions.bind(this);
    return (
      <div className="sidebar-filter-wrapper">
        <div className="panel panel-default">
          <div
            className={`panel-heading${chartSelected === chartName ? ' selected' : ''}`}
            onClick={onSelectedOptions}>
            <h4 className={`panel-title ${chartName}`} data-toggle="collapse" data-target={`#${chartName}`}>
              {translations[title]}
            </h4>
          </div>
        </div>
      </div>
    );
  }
}

export default FiltersLink;
FiltersLink
  .contextType = SSCTranslationContext;

FiltersLink.propTypes = {
  onLinkClicked: PropTypes.func,
  onChangeChartSelect: PropTypes.func.isRequired,
  chartName: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  chartSelected: PropTypes.string.isRequired
};
