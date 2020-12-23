import React, { Component } from 'react';
import { ResponsiveBar } from '@nivo/bar';
import PropTypes from 'prop-types';
import { NDDTranslationContext } from './StartUp';
import ToolTip from './tooltips/ToolTip';
import { formatKMB } from '../utils/Utils';


const styles = {
  fontFamily: 'sans-serif',
  textAlign: 'center',
};

class TopChart extends Component {

  getColor(item) {
    console.log(JSON.stringify(item));
    return colors[item.index];
  }

  getLabel(item) {
    const { translations } = this.context;
    const formatter = formatKMB(translations);
    return formatter(item.data.value);
  }

  getOthers(o) {
    return {
      id: -9999,
      name: 'others',
      value: o,
    };
  }

  render() {
    const { data } = this.props;

    const transformedData =
      data.values.slice(0, 5).map(v => {
        return {
          id: v.id.toString(),
          label: v.formattedAmount,
          name: v.name,
          value: v.amount,
        };
      });

    const others = data.total - data.values.reduce((acc, cur) => {
      return (acc + cur.amount);
    }, 0);
    if (others > 0) {
      transformedData.push(this.getOthers(others))
    }
    return (
      <div style={styles}>
        <h1>legends</h1>
        <div style={{ height: '300px' }}>
          <ResponsiveBar
            data={transformedData}
            colors={this.getColor.bind(this)}
            label={this.getLabel.bind(this)}
            enableGridY={false}
            labelFormat={d => {
              console.log(d);
              return (<tspan y={0}>{`${d}%}`}</tspan>);
            }
            }
            tooltip={(e) => {
              return (
                <ToolTip
                  color={e.color}
                  titleLabel={e.data.name}
                  formattedValue={e.data.formattedAmount}
                  value={e.data.value}
                  total={data.total}
                  id={e.data.id}
                />
              );
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
};
export default TopChart;
