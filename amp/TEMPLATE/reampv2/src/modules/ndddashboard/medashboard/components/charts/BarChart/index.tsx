import { BarDatum, ResponsiveBar } from '@nivo/bar';
import React, { useEffect } from 'react';
import { ComponentProps, MarginProps, YearValues } from '../../../types';
import { BASE_VALUE_COLOR, CURRENT_VALUE_COLOR, TARGET_VALUE_COLOR } from '../../../../utils/constants';
import ChartUtils from '../../../utils/chart';

interface DataType {
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
  data?: YearValues [];
}

const BarChart: React.FC<BarChartProps> = (props) => {
  const { title, height, width, margin, data, translations } = props;
  const [displayDataSet, setDisplayDataSet] = React.useState<DataType[]>([]);

  const generateDataSet = () => {
    if (data) {
      let aggregateValue = {
        baseValue: 0,
        targetValue: 0,
        actualValue: 0
      };

      if (Array.isArray(data)) {
        aggregateValue = ChartUtils.computeAggregateValues(data);
      } else {
        aggregateValue = ChartUtils.computeAggregateValues([data]);
      }

      const year = new Date().getFullYear();
      if (aggregateValue.baseValue) {
        const baseData = {
          id: translations['amp.ndd.dashboard:me-baseline'],
          value: aggregateValue.baseValue,
          label: `${translations['amp.ndd.dashboard:me-baseline']} ${year}`,
          color: BASE_VALUE_COLOR
        }

        setDisplayDataSet(prev => [...prev, baseData]);
      }

      if (aggregateValue.actualValue) {
        const actualData = {
          id: translations['amp.ndd.dashboard:me-current'],
          value: aggregateValue.actualValue,
          label: `${translations['amp.ndd.dashboard:me-current']} ${year}`,
          color: CURRENT_VALUE_COLOR
        };

        setDisplayDataSet(prev => [...prev, actualData]);
      }

      if (aggregateValue.targetValue) {
        const targetData = {
          id: translations['amp.ndd.dashboard:me-target'],
          value: aggregateValue.targetValue,
          label: `${translations['amp.ndd.dashboard:me-target']} ${year}`,
          color: TARGET_VALUE_COLOR
        };

        setDisplayDataSet(prev => [...prev, targetData]);
      }

    }
  }

  useEffect(() => {
    generateDataSet();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data]);


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
        {displayDataSet &&
          <ResponsiveBar
            data={displayDataSet as unknown as BarDatum[]}
            indexBy="id"
            colors={(item: any) => item.data.color}
            tooltipLabel={(item: any) => item.data.label}
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
                      backgroundColor: item.data.color as any,
                    }}></div>
                    <span style={{ fontWeight: 'normal', paddingLeft: 4 }}>{item.data.label}</span>
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
        }
      </div>
    </div>

  )
}

export default React.memo(BarChart);
