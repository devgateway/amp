import { ResponsiveBar } from '@nivo/bar';
import React from 'react';
import { ComponentProps, MarginProps } from '../../../types';

export interface DataType {
  id: string;
  value: number;
  label: string;
  color: string;
}
export interface BarChartProps extends ComponentProps {
  title?: string;
  height?: number;
  width?: number;
  margin?: MarginProps;
}

const data = [
  {
    id: 'Baseline',
    value: 55,
    label: 'Baseline 2020',
    color: 'hsl(206, 100%, 49%)'
  },
  {
    id: 'Current',
    value: 80,
    label: 'Current 2020',
    color: 'hsl(32, 100%, 58%)'
  },
  {
    id: 'Target',
    value: 60,
    label: 'Target 2020',
    color: 'hsl(95, 54%, 40%)'
  }
]

const BarChart: React.FC<BarChartProps> = (props) => {
  const { title, height, width, margin } = props;

  return (
    <div style={{
      borderBottom: '1px solid #e8e8e8',
      paddingTop: 15
    }}>
      <span style={{
        fontSize: 14,
        paddingTop: 15,
        marginBottom: 10,
      }}>{title}</span>
       <div style={{ height: height || 192, width: width || 250, marginTop: 1 }}>
      <ResponsiveBar
      data={data}
      indexBy="id"
      colors={item => item.data.color}
      tooltipLabel={item => item.data.label}
      tooltip={
        (item) => {
          return (
            <div style={{
              padding: 10,
              fontSize: 12,
              backgroundColor: '#fff',
              borderRadius: 5,
              display: 'flex',
              alignItems: 'center',
              boxShadow: '0 1px 2px rgba(0, 0, 0, 0.25)'
            }}>
              <div style={{
                width: 10,
                height: 10,
                backgroundColor: item.data.color,
              }}></div>
              <span style={{ fontWeight: 'normal', paddingLeft: 4  }}>{item.data.label}</span>
              <span>:</span>
              <span style={{ fontWeight: 'bold', paddingLeft: 6 }}>{item.data.value} details</span>
            </div>
          )
        }
      }
      legends={[
        {
          dataFrom: 'indexes',
          anchor: 'top-left',
          direction: 'row',
          justify: false,
          itemHeight: 20,
          itemWidth: 80,
          itemDirection: 'left-to-right',
          itemsSpacing: 2,
          symbolSize: 10,
          translateX: -15,
          translateY: -35,
          effects: [
            {
              on: 'hover',
              style: {
                itemOpacity: 1
              }
            }
          ]
        }
      ]}
      animate={true}
      enableGridX={false}
      enableGridY={false}
      axisTop={null}
      axisRight={null}
      axisBottom={null}
      axisLeft={null}
      margin={margin || { top: 50, right: 30, left: 20 }}
      borderRadius={3}
      padding={0.3}
      enableLabel={false}
      theme={{
        tooltip: {
          container: {
            padding: '8px',
            borderRadius: '5px',
            boxShadow: '0 1px 2px rgba(0, 0, 0, 0.25)'
          }
        },
        legends: {
          text: {
            fontSize: 12,
          },
        },
      }}
      />
    </div>
    </div>

  )
}

export default BarChart;
