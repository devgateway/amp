import { LineSvgProps, ResponsiveLine } from '@nivo/line'
import React, {useEffect, useState} from 'react';
import ChartUtils from '../../../utils/chart';
import {YearValues} from "../../../types";


//@ts-ignore
export interface LineChartProps extends LineSvgProps {
    intervals?: number[];
    height?: number;
    width?: number;
    data?: YearValues | undefined;
}

const LineChart: React.FC<LineChartProps> = (props) => {
    const { intervals, height, width, data } = props;
    const tickValues = intervals || ChartUtils.generateTickValues(0, 150, 25);

    const [displayDataSet, setDisplayDataSet] = useState<any>([]);

    useEffect(() => {
        if (data) {
            setDisplayDataSet(ChartUtils.generateLineChartValues(data));
        }
    }, [data])

    return (
        <div style={{ height: height || 260, width: width || 650 }}>
            <ResponsiveLine
                {...props}
                data={displayDataSet}
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
