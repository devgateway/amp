import React, { Component } from 'react';
import PropTypes from 'prop-types';
import '../popups.css';
import { ResponsivePie } from '@nivo/pie';
import { SSCTranslationContext } from '../../../StartUp';
import {
  SECTOR_COLOR_MAP, OTHERS_CODE, SECTORS_LIMIT_CHART,
  SECTORS_OTHERS_ID_CHART, SECTORS_CHART, COLOR_MAP, COLOR_MAP_CUSTOM
} from '../../../../utils/constants';
import { toCamelCase } from '../../../../utils/Utils';
import Tooltip from '../../../utils/GenericTooltip';
import CustomLegend from '../../../../../../utils/components/CustomLegend';
import '../../../utils/customLegend.css';
class CountryPopupChart extends Component {
  constructor(props) {
    super(props);
    this.colorMap = new Map();
  }

  getColor(item) {
    const { chartSelected } = this.props;
    return COLOR_MAP.get(chartSelected).get(item.code);
  }

  getChart(data) {
    const { chartSelected } = this.props;
    const chartComponents = {};
    chartComponents.chart = (
      <ResponsivePie
        data={data}
        margin={{
          top: 5, right: 5, bottom: 5, left: 5
        }}
        innerRadius={0.8}
        colors={this.getColor.bind(this)}
        borderWidth={1}
        sortByValue={false}
        borderColor={{ from: 'color', modifiers: [['darker', 0.2]] }}
        enableRadialLabels={false}
        radialLabelsSkipAngle={12}
        radialLabelsTextXOffset={6}
        radialLabelsTextColor="#333333"
        radialLabelsLinkOffset={0}
        radialLabelsLinkDiagonalLength={16}
        radialLabelsLinkHorizontalLength={24}
        radialLabelsLinkStrokeWidth={1}
        radialLabelsLinkColor={{ from: 'color' }}
        enableSlicesLabels={false}
        slicesLabelsSkipAngle={10}
        slicesLabelsTextColor="#333333"
        animate
        motionStiffness={90}
        motionDamping={15}
        tooltip={(e) => (
          <Tooltip
            color={e.color}
            titleLabel={e.label}
            values={e.otherValues}
          />
        )}
        theme={{
          tooltip: {
            container: {
              padding: '0',
            },
          }
        }}
      />

    );
    chartComponents.legend = <CustomLegend data={data} chartSelected={chartSelected}/>;
    return chartComponents;
  }

  render() {
    const { chartData, columnCount } = this.props;
    const { translations } = this.context;

    const nonGrouped = chartData.slice(0, SECTORS_LIMIT_CHART).sort((b1, b2) => b1.percentage < b2.percentage);
    const others = chartData.slice(SECTORS_LIMIT_CHART, chartData.length);
    if (others.length > 0) {
      const other = {};
      const othersLabel = toCamelCase(translations['amp.ssc.dashboard:sectors-others']);

      other.id = SECTORS_OTHERS_ID_CHART;
      other.value = 0;
      other.percentage = 0;
      other.code = OTHERS_CODE;
      other.otherValues = others;
      others.forEach(o => {
        other.value += o.value;
        other.percentage += o.percentage;
      });

      other.label = `${othersLabel} ${other.percentage}%`;
      other.simpleLabel = othersLabel;
      nonGrouped.push(other);
    }
    const chartComponents = this.getChart(nonGrouped, columnCount);
    return (
      <div className="row">
        <div className="chart-container single-50 float-left">
          {chartComponents.chart}
        </div>
        {chartComponents.legend
        && (
          <div className="chart-legend single-50 float-right">
            {chartComponents.legend}
          </div>
        )}
      </div>
    );
  }
}

CountryPopupChart.contextType = SSCTranslationContext;
export default CountryPopupChart;
CountryPopupChart.propTypes = {
  chartSelected: PropTypes.string.isRequired,
  columnCount: PropTypes.number.isRequired,
  chartData: PropTypes.array.isRequired
};
