import { LineSvgProps, ResponsiveLine, Serie } from '@nivo/line'
import React from 'react';
import ChartUtils from '../../../utils/chart';

const data = [
    {
        id: 'Baseline',
        color: 'hsl(206, 100%, 49%)',
        data: [
            {
                x: '2019',
                y: 50
            },
            {
                x: '2020',
                y: 55
            },
            {
                x: '2021',
                y: 60
            },
            {
                x: '2022',
                y: 70
            },
            {
                x: '2023',
                y: 30
            }
        ]
    },
    {
        id: 'Current',
        color: 'hsl(32, 100%, 58%)',
        data: [
            {
                x: '2019',
                y: 20
            },
            {
                x: '2020',
                y: 50
            },
            {
                x: '2021',
                y: 85
            },
            {
                x: '2022',
                y: 120
            },
            {
                x: '2023',
                y: 10
            }
        ]
    },
    {
        id: 'Target',
        color: 'hsl(95, 54%, 40%)',
        data: [
            {
                x: '2019',
                y: 30
            },
            {
                x: '2020',
                y: 80
            },
            {
                x: '2021',
                y: 90
            },
            {
                x: '2022',
                y: 100
            },
            {
                x: '2023',
                y: 100
            }
        ]
    }
]

//@ts-ignore
export interface LineChartProps extends LineSvgProps {
    intervals?: number[];
    height?: number;
    width?: number;
    data?: Serie[] | undefined;
}

const LineChart: React.FC<LineChartProps> = (props) => {
    const { intervals, height, width } = props;
    const tickValues = intervals || ChartUtils.generateTickValues(0, 150, 25);

    return (
        <div style={{ height: height || 260, width: width || 650 }}>
            <ResponsiveLine
                {...props}
                data={data}
                margin={{ top: 10, right: 130, bottom: 50, left: 25 }}
                xScale={{ type: 'point' }}
                yScale={{
                    type: 'linear',
                    min: 0,
                    max: 150,
                    clamp: true,
                    stacked: false,
                    reverse: false
                }}
                enablePoints={false}
                useMesh={true}
                lineWidth={4}
                colors={item => item.color}
                axisTop={null}
                axisLeft={{
                    tickValues: tickValues,
                    tickSize: 0,
                }}
                axisBottom={{
                    tickSize: 0,
                }}
            />
        </div>
    )
}

export default LineChart;
