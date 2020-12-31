import React, { Component } from 'react';
import { ResponsiveBar } from '@nivo/bar';
import PropTypes from 'prop-types';
import { NDDTranslationContext } from './StartUp';
import ToolTip from './tooltips/ToolTip';
import { formatKMB } from '../utils/Utils';
import SimpleLegend from '../../../utils/components/SimpleLegend';

const styles = {
  fontFamily: 'sans-serif',
  textAlign: 'center',
};

class TopChart extends Component {
  getColor(item) {
    return colors[item.index];
  }

  getLabel(item) {
    const { translations } = this.context;
    const formatter = formatKMB(translations);
    return formatter(item.data.value);
  }

  getOthers(o) {
    const { translations } = this.context;
    return {
      id: -9999,
      name: translations['amp.ndd.dashboard:others'],
      value: o,
    };
  }

  render() {
    const { data, globalSettings } = this.props;
    const transformedData = data.values.slice(0, 5).map(v => ({
      id: v.id.toString(),
      formattedAmount: v.formattedAmount,
      name: v.name,
      value: v.amount,
    }));

    const others = data.total - data.values.reduce((acc, cur) => (acc + cur.amount), 0);
    if (others > 0) {
      transformedData.push(this.getOthers(others));
    }
    return (
      <div style={styles}>
        <SimpleLegend
          data={data.values}
          getColor={this.getColor.bind(this)}
        />
        <div style={{ height: '335px' }}>
          <ResponsiveBar
            data={transformedData}
            colors={this.getColor.bind(this)}
            label={this.getLabel.bind(this)}
            enableGridY={false}
            axisTop={null}
            axisRight={null}
            axisBottom={null}
            axisLeft={null}
            margin={{
              top: 20, right: 10, bottom: 0, left: 10
            }}
            labelFormat={d => (<tspan y={-5}>{`${d}`}</tspan>)}
            tooltip={(e) => (
              <ToolTip
                color={e.color}
                titleLabel={e.data.name}
                formattedValue={e.data.formattedAmount}
                value={e.data.value}
                total={data.total}
                id={e.data.id}
                currencyCode={data.currency}
                globalSettings={globalSettings}
              />
            )}
            theme={{
              tooltip: {
                container: {
                  padding: '1px',
                  borderRadius: '5px',
                  boxShadow: '0 1px 2px rgba(0, 0, 0, 0.25)'
                }
              }
            }}
          />
        </div>
      </div>
    );
  }
}

const colors = ['rgba(90, 153, 199, 1)',
  'rgba(195, 214, 238, 1)',
  'rgba(255, 160, 87, 1)',
  'rgba(255, 204, 154, 1)',
  'rgba(99, 184, 99, 1)',
  'rgba(153, 153, 153, 1)',
  'rgba(217, 91, 95, 1)',
  'rgba(253, 170, 170, 1)',
  'rgba(166, 133, 196, 1)',
  'rgba(206, 189, 218, 1)',

];
TopChart.contextType = NDDTranslationContext;

TopChart.propTypes = {
  data: PropTypes.object.isRequired,
  formatter: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired
};
export default TopChart;
